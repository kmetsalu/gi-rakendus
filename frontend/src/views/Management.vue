<template>
<v-main v-if="loaded">
  <v-main fluid v-if="content === true" class="mt-n16 pt-13">
      <v-flex class="text-center">
        <h1>Manage Users</h1>
      </v-flex>
      <v-flex class="pt-8 pb-8">
        <v-form @submit.prevent ref="form" class="ml-5 mr-5">
          <v-layout column>
            <v-flex>
              <v-text-field
                  label="Username"
                  v-model="searchTerm"
                  :rules="searchRules"
                  outlined
                  class="mt-4"
              ></v-text-field>
            </v-flex>
            <v-flex class="text-xs-center">
              <v-btn type="submit"
                     @click="getData"
              >Search
              </v-btn>
            </v-flex>
            <v-card class="elevation-2 xs12 sm12 mt-12 pa-8" v-if="success" rounded>
              <v-checkbox v-model="permissions.search" :disabled="!isEnabled" label="Search Permission"></v-checkbox>
              <v-checkbox v-model="permissions.upload" :disabled="!isEnabled" label="Upload Permission">Upload</v-checkbox>
              <v-checkbox v-model="permissions.management" :disabled="!isEnabled" label="Management Permission">Management
              </v-checkbox>
              <v-checkbox v-if="currentUser.roles.includes('ROLE_ROOT')" v-model="isEnabled" label="User Enabled">Enabled
              </v-checkbox>

              <v-btn type="submit"
                     @click="updateRoles"
              >Modify
              </v-btn>
            </v-card>
            <v-alert v-if="message" type="error" align="center" class="xs12 sm12 mt-12 pa-5">
              {{ message }}
            </v-alert>
            <v-alert v-if="successMessage" type="success" align="center" class="xs12 sm12 mt-12 pa-5">
              {{ successMessage }}
            </v-alert>
          </v-layout>
        </v-form>
      </v-flex>
  </v-main>
  <v-main v-if="content !== true" class="pl-6 pr-6">
    <v-alert type="error" align="center" class="xs12 sm12 mt-12 pa-5">
      You don't have the access to view this page!
    </v-alert>
  </v-main>
</v-main>
</template>

<script>
import axios from 'axios';
import UserService from '@/services/user.service';
import authHeader from '@/services/auth-header';

export default {
  name: 'Management',
  metaInfo: {
    title: 'Application | Management'
  },
  data() {
    return {
      loaded: false,
      content: true,
      showTable: false,
      searchTerm: '',
      permissions: {
        root: false,
        search: false,
        upload: false,
        management: false
      },
      isEnabled: true,
      initialEnabled: true,
      success: false,
      username: '',
      roles: [],
      message: '',
      successMessage: '',
      searchRules: [
        v => !!v || 'Username is required'
      ],
    };
  },
  methods: {
    getData() {
      this.successMessage = '';
      this.success = false;
      this.message = '';
      if (this.currentUser.username.toLowerCase() === this.searchTerm) {
        this.message = '';
        this.message = 'Cannot modify your own permissions!';
      } else if (this.validate()) {
        axios.get('http://localhost:8080/api/usersearch', {params: {search: this.searchTerm},
        headers: authHeader()})
            .then(response => {
              this.username = '';
              this.isEnabled= response.data.enabled;
              this.initialEnabled = response.data.enabled
              this.initializePermissions();
              this.userRoles(response.data.roles);
              if (!this.permissions.root) {
                this.success = true;
                this.username = this.searchTerm;
              } else {
                this.message = 'Cannot modify a root user!';
              }
            }).catch(response => {
          if (response.name === 'Error') {
            this.message = 'The username not found!';
          }
        });
      }
    },
    updateRoles() {
      if (this.isEnabled !== this.initialEnabled) {
        axios.post('http://localhost:8080/api/setenabled', {username: this.username, isEnabled: this.isEnabled},
            {
              headers: {
                'Accept': 'application/json',
                'Access-Control-Allow-Credentials': true,
                'Authorization': authHeader()['Authorization']
              }
            }).then(response => {
          this.success = false;
          this.successMessage = '';
          this.successMessage = response.data.message;
        }).catch(response => {
          console.log(JSON.stringify(response.data));
        });
      } else {
        this.mapToRoleNumbers();
        axios.post('http://localhost:8080/api/updateroles', {username: this.username, roles: this.roles},
            {
              headers: {
                'Accept': 'application/json',
                'Access-Control-Allow-Credentials': true,
                'Authorization': authHeader()['Authorization']
              }
            }).then(response => {
          this.success = false;
          this.successMessage = '';
          this.successMessage = response.data.message;
        }).catch(response => {
          console.log(JSON.stringify(response.data));
        });
      }
    },
    userRoles(response) {
      for (const responseElement of response) {
        switch (responseElement.name) {
          case 'ROLE_ROOT':
            this.permissions.root = true;
            break;
          case 'ROLE_SEARCH':
            this.permissions.search = true;
            break;
          case 'ROLE_UPLOAD':
            this.permissions.upload = true;
            break;
          case 'ROLE_MANAGEMENT':
            this.permissions.management = true;
            break;
          default:
        }
      }
    },
    mapToRoleNumbers() {
      this.roles = [];
      for (const key in this.permissions) {
        switch (key) {
          case 'search':
            if (this.permissions['search']) {
              this.roles.push('2');
            }
            break;
          case 'upload':
            if (this.permissions['upload']) {
              this.roles.push('3');
            }
            break;
          case 'management':
            if (this.permissions['management']) {
              this.roles.push('4');
            }
            break;
          default:
            break;
        }
      }
    },
    initializePermissions() {
      for (let key in this.permissions) {
        this.permissions[key] = false;
      }
    },
    validate() {
      return this.$refs.form.validate();
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  beforeMount() {
    if (!this.currentUser) {
      this.$router.push('/login');
    }
    UserService.getManagement().then(
        response => {
          this.content = response.data;
          this.loaded = true
        },
        error => {
          this.content =
              (error.response && error.response.data) ||
              error.message ||
              error.toString();
          this.loaded = true
        }
    );
  }
};
</script>




<template>
  <v-main class="pa-12">
    <v-flex class="text-center xs12" mt-12>
      <h1>Sign In</h1>
    </v-flex>
    <v-main align="center">
      <v-card class="mx-auto elevation-2"
              max-width="500"
              align="center"
              justify="center">
        <v-form @submit.prevent ref="form" lazy-validation class="pa-4 pt-16 pb-16">
          <v-text-field
              v-model="username"
              label="Username"
              required
              :rules="inputRules"
          ></v-text-field>
          <v-text-field
              v-model="password"
              label="Password"
              required
              :rules="inputRules"
              :type="'password'"
          ></v-text-field>
          <v-btn
              class="mr-4"
              @click="handleLogin"
              type="submit"
          >Sign In
          </v-btn>
        </v-form>
      </v-card>
      <v-main>
        <v-alert v-if="message" type="error" max-width="500" class="mx-auto xs12 sm12 pa-5">
          {{ message }}
        </v-alert>
      </v-main>
    </v-main>
  </v-main>
</template>

<script>
import User from '../models/user';

export default {
  name: 'Login',
  metaInfo: {
    title: 'Application | Login'
  },
  data() {
    return {
      username: '',
      password: '',
      loading: false,
      message: '',
      inputRules: [
        v => !!v || 'The field is required'
      ]
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  created() {
    if (this.loggedIn) {
      this.$router.push('/');
    }
  },
  methods: {
    handleLogin() {
      if (this.validate()) {
        this.loading = true;
        if (this.username && this.password) {
          const user = new User(this.username, '', this.password);
          this.$store.dispatch('auth/login', user).then(
              () => {
                this.$router.push('/');
              },
              error => {
                this.loading = false;
                if (error.response.data.status === 401 ||
                    error.response.data.status === 404) {
                  this.message = 'Incorrect username or password';
                } else {
                  this.message = error.response.data.message;
                }
              }
          );
        }
      }
    },
    validate() {
      return this.$refs.form.validate();
    }
  }
};
</script>

<style scoped>
</style>

<template>
<v-main v-if="loaded">
  <v-main fluid v-if="content === true" class="mt-n16">
    <v-layout row wrap>
      <v-flex class="text-center">
        <h1>Search Visitation Details</h1>
      </v-flex>
      <v-flex class="xs12 sm12 mt-3 pr-8 pb-8 pl-8 pt-9">
        <v-form @submit.prevent ref="form" v-model="valid" lazy-validation>
          <v-layout column>
            <v-flex>
              <v-text-field
                  label="Identification Code"
                  :rules="searchRules"
                  v-model="searchTerm"
                  outlined
              ></v-text-field>
            </v-flex>
            <v-flex class="text-xs-center">
              <v-btn type="submit"
                     @click="getData"
              >Search
              </v-btn>
            </v-flex>
          </v-layout>
        </v-form>
        <v-data-table
            :headers="headers"
            :items="entries"
            :items-per-page="15"
            class="elevation-2 mt-12"
            v-if="showTable"
        ></v-data-table>
      </v-flex>
    </v-layout>
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
import UserService from '../services/user.service';
import authHeader from '@/services/auth-header';

export default {
  name: 'Search',
  metaInfo: {
    title: 'Application | Search'
  },
  data() {
    return {
      loaded: false,
      content: true,
      showTable: false,
      headers: [
        {text: 'Code', value: 'code'},
        {text: 'Department', value: 'dep'},
        {text: 'Visit Time', value: 'visit_time'},
        {text: 'First Name', value: 'first_name'},
        {text: 'Last Name', value: 'last_name'},
        {text: 'Email', value: 'email'},
        {text: 'ID Code', value: 'identification'},
        {text: 'Date of Birth', value: 'date_of_birth'},
        {text: 'Gender', value: 'gender'},
        {text: 'Age at Visit', value: 'age_at_visit'}],
      entries: [],
      searchRules: [
        v => !!v || 'Search term is required',
        v => /^([*]?|0[*]?|[1-9][0-9]*[*]?)$/.test(v) || 'Invalid input',
      ],
      searchTerm: '',
      valid: false
    };
  },
  methods: {
    getData() {
      this.showTable = false;
      if (this.validate()){
        axios.get('http://localhost:8080/api/search', {params: {search: this.searchTerm},
        headers: authHeader()})
            .then(response => {
              if (response.data === '') {
                this.entries = [];
                this.showTable = true;
              } else {
                this.entries = response.data;
                this.showTable = true;
              }
            }).catch(response => {
          console.log(JSON.stringify(response.data));
        });
      }
    },
    validate () {
      return this.$refs.form.validate();
    }
  },
  beforeMount() {
    UserService.getSearch().then(
        response => {
          this.content = response.data;
          this.loaded = true;
        },
        error => {
          this.content =
              (error.response && error.response.data) ||
              error.message ||
              error.toString();
          this.loaded = true;
        }
    );
  }
};
</script>
<style>
.v-data-table-header th {
  white-space: nowrap;
}
</style>



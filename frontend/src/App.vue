<template>

  <v-app>
    <v-app-bar app>
      <v-toolbar-title>
        <router-link to="/" tag="span" style="cursor: pointer" custom v-slot="{navigate}">
          <span @click="navigate" @keypress.enter="navigate" role="link">{{appTitle}}</span>
        </router-link>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-toolbar-items class="">
        <v-btn v-if="currentUser"
               text
               :to="'/'"
        >
          <v-icon left dark>{{ 'mdi-home' }}</v-icon>
          Home
        </v-btn>
        <v-btn v-if="showSearch"
               text
               :to="'/search'"
        >
          <v-icon left dark>{{ 'mdi-account-search' }}</v-icon>
          Search
        </v-btn>
        <v-btn v-if="showUpload"
               text
               :to="'/upload'"
        >
          <v-icon left dark>{{ 'mdi-cloud-upload' }}</v-icon>
          Upload
        </v-btn>
        <v-btn v-if="showManagement"
               text
               :to="'/management'"
        >
          <v-icon left dark>{{ 'mdi-account-edit' }}</v-icon>
          Management
        </v-btn>
        <v-btn v-if="!currentUser"
               text
               :to="'/register'"
        >
          <v-icon left dark>{{ 'mdi-account-plus' }}</v-icon>
          Register
        </v-btn>
        <v-btn v-if="!currentUser"
               text
               :to="'/login'"
        >
          <v-icon left dark>{{ 'mdi-login' }}</v-icon>
          Sign In
        </v-btn>
        <v-btn v-if="currentUser"
               text
               :to="'/signout'"
               @click="logOut"
        >
          <v-icon left dark>{{ 'mdi-logout' }}</v-icon>
          Sign Out
        </v-btn>
      </v-toolbar-items>
    </v-app-bar>

    <v-main>
      <router-view></router-view>
    </v-main>
  </v-app>

</template>

<script>

export default {
  name: 'App',
  data() {
    return {
      appTitle: 'Visitation Details Database',
      sidebar: false,
      menuItems: [
        {title: 'Home', path: '/', icon: 'mdi-home'},
        {title: 'Search', path: '/search', icon: 'mdi-account-search'},
        {title: 'Upload', path: '/upload', icon: 'mdi-cloud-upload'},
        {title: 'Management', path: '/management', icon: 'mdi-account-multiple-plus'}
      ],
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    showSearch() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_SEARCH') ||
            this.currentUser.roles.includes('ROLE_ROOT');
      }

      return false;
    },

    showUpload() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_UPLOAD') ||
            this.currentUser.roles.includes('ROLE_ROOT');
      }

      return false;
    },
    showManagement() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_MANAGEMENT') ||
            this.currentUser.roles.includes('ROLE_ROOT');
      }

      return false;
    }
  },
  methods: {
    logOut() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/');
    }
  },
};
</script>
<style>

</style>

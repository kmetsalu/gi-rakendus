<template>
  <v-main v-if="currentUser" class="pa-13">
    <h1 align="center">Hello, {{ currentUser.username }}!</h1>
    <v-card align="center" class="pa-11 elevation-0" v-if="currentUser.roles.length > 0">
      You have the following permissions:
      <ul>
        <li v-for="(role,index) in currentUser.roles" :key="index">{{ mapRoles(role) }}</li>
      </ul>
    </v-card>
  </v-main>
</template>

<script>
export default {
  name: 'Home',
  metaInfo: {
    title: 'Application | Home'
  },
  methods: {
    mapRoles(role) {
        switch (role) {
          case 'ROLE_ROOT':
            return 'Root access';
          case 'ROLE_SEARCH':
            return 'Searching';
          case 'ROLE_UPLOAD':
            return 'Uploading';
          case 'ROLE_MANAGEMENT':
            return 'User management';
          default:
            return
        }
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
li {
  list-style-type: none;
}
</style>

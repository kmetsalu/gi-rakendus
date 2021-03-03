<template>
  <v-main v-if="loaded" class="mt-n3">
    <v-main fluid v-if="content === true" class="mt-n16">
      <v-form class="ml-5 mr-5">
        <v-flex class="text-center xs12 mb-12">
          <h1>Upload Files</h1>
        </v-flex>
        <v-file-input
            id="uploadFile"
            v-model="file"
            outlined
            placeholder="Select a file"
            prepend-icon="mdi-paperclip"
            :show-size="1000"
        >
        </v-file-input>
        <v-btn @click="onUpload" class="ml-8"> Upload</v-btn>
        <v-progress-linear
            v-model="uploadPercentage"
            height="40"
            class="mt-12"
            v-if="progressbar"
            color="blue lighten-4"
            striped
            rounded
        >
          <strong>{{ Math.ceil(uploadPercentage) }}%</strong>
        </v-progress-linear>
        <v-alert v-if="success" type="success" align="center" class="xs12 sm12 mt-12 pa-5">
          {{ this.message }}
        </v-alert>
        <v-alert v-if="error" type="error" align="center" class="xs12 sm12 mt-12 pa-5">
          {{ this.message }}
        </v-alert>
      </v-form>
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
  name: 'Upload',
  metaInfo: {
    title: 'Application | Upload'
  },
  data() {
    return {
      file: undefined,
      loaded: false,
      content: true,
      header: '',
      uploadPercentage: 0,
      progressbar: false,
      success: false,
      message: '',
      error: false
    };
  },
  methods: {

    onUpload() {
      let formData = new FormData();
      formData.append('file', this.file);
      this.uploadPercentage = 0;
      this.progressbar = true;
      this.success = false;
      this.error = false;
      this.message = '';
      axios.post('http://localhost:8080/api/upload', formData,
          {
            headers: {
              'Accept': 'application/json',
              'content-type': 'multipart/form-data',
              'Access-Control-Allow-Credentials': true,
              'Authorization': authHeader()['Authorization']
            },
            onUploadProgress: function (progressEvent) {
              this.uploadPercentage = parseInt(Math.round((progressEvent.loaded / progressEvent.total) * 100));
            }.bind(this)
          }).then(response => {
        this.success = true;
        this.message = response.data.message;
      }).catch(response => {
        console.log(JSON.stringify(response.data));
        this.error = true;
        this.message = 'Failed to upload the file!';
      });
    }
  },
  beforeMount() {
    this.header = authHeader();
    UserService.getUpload().then(
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

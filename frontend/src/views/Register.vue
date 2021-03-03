<template>
  <v-main class="pa-12">
    <v-flex class="text-center xs12" mt-12>
      <h1>Register</h1>
    </v-flex>
    <v-main>
      <v-card v-if="!successful" class="mx-auto elevation-2" max-width="500px" align="center">
        <v-form @submit.prevent ref="form" lazy-validation class="pa-4 pt-16 pb-16">
          <v-text-field
              v-model="name"
              :counter="20"
              label="Username"
              :error-messages="nameErrors"
              :rules="inputRules"
              required
              @input="$v.name.$touch()"
              @blur="$v.name.$touch()"
          ></v-text-field>
          <v-text-field
              v-model="email"
              label="Email"
              :error-messages="emailErrors"
              :rules="inputRules"
              @input="$v.email.$touch()"
              @blur="$v.email.$touch()"
              required
          ></v-text-field>
          <v-text-field
              v-model="password"
              label="Password"
              :error-messages="passwordErrors"
              :rules="inputRules"
              @input="$v.password.$touch()"
              @blur="$v.password.$touch()"
              required
              :type="'password'"
          ></v-text-field>
          <v-text-field
              v-model="repeatPassword"
              label="Confirm Password"
              :error-messages="repeatPasswordErrors"
              :rules="inputRules"
              @input="$v.repeatPassword.$touch()"
              @blur="$v.repeatPassword.$touch()"
              required
              :type="'password'"
          ></v-text-field>
          <v-btn
              @click="handleRegister"
              type="submit"
          >Register
          </v-btn>
        </v-form>
      </v-card>
      <v-main align="center">
        <v-alert v-if="message" :type="successful ? 'success' : 'error'" max-width="500"
                 class="mx-auto xs12 sm12 pa-5">
          {{ message }}
        </v-alert>
      </v-main>
    </v-main>
  </v-main>
</template>

<script>
import User from '../models/user';
import {validationMixin} from 'vuelidate';
import {email, maxLength, minLength, required, sameAs} from 'vuelidate/lib/validators';

export default {
  mixins: [validationMixin],

  validations: {
    name: {required, minLength: minLength(3), maxLength: maxLength(20)},
    email: {required, email, maxLength: maxLength(50)},
    password: {required, minLength: minLength(6), maxLength: maxLength(40)},
    repeatPassword: {sameAsPassword: sameAs('password')}
  },
  name: 'Register',
  metaInfo: {
    title: 'Application | Register'
  },
  data() {
    return {
      name: '',
      email: '',
      password: '',
      repeatPassword: '',
      submitted: false,
      successful: false,
      message: '',
      inputRules: [
        v => !!v || 'The field is required'
      ]
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
    nameErrors() {
      const errors = [];
      if (!this.$v.name.$dirty) return errors;
      !this.$v.name.maxLength && errors.push('Name must be at most 20 characters long');
      !this.$v.name.minLength && errors.push('Name must be at least 3 characters long');
      !this.$v.name.required && errors.push('Name is required.');
      return errors;
    },
    emailErrors() {
      const errors = [];
      if (!this.$v.email.$dirty) return errors;
      !this.$v.email.email && errors.push('Must be valid e-mail');
      !this.$v.email.required && errors.push('E-mail is required');
      !this.$v.email.maxLength && errors.push('E-mail must be at most 50 characters long');
      return errors;
    },
    passwordErrors() {
      const errors = [];
      if (!this.$v.password.$dirty) return errors;
      !this.$v.password.minLength && errors.push('Password must be at least 6 characters long');
      !this.$v.password.maxLength && errors.push('Password must be at most 40 characters long');
      !this.$v.password.required && errors.push('Password is required.');
      return errors;
    },
    repeatPasswordErrors() {
      const errors = [];
      if (!this.$v.repeatPassword.$dirty) return errors;
      !this.$v.repeatPassword.sameAsPassword && errors.push('Passwords must be the same.');
      return errors;
    }
  },
  mounted() {
    if (this.loggedIn) {
      this.$router.push('/login');
    }
  },
  methods: {
    handleRegister() {
      if (this.validate()) {
        this.message = '';
        this.submitted = true;
        const user = new User(this.name, this.email, this.password);
        this.$store.dispatch('auth/register', user).then(
            data => {
              this.message = data.message;
              this.successful = true;
              setTimeout(() => this.$router.push('/login'), 2000);
            },
            error => {
              this.message =
                  error.response.data.message;
              console.log(this.message);
              this.successful = false;
            }
        );
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

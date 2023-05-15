<template>
<div class="header">
    <div>
    <div class="name" v-if="checkRoles('ADMIN')">
      <h1> Hello Admin</h1>
        <button @click="toggleForm">Create New Admin</button> 
    </div>
  </div>
</div>
<div class="form" v-if="showForm">
    <h3>SignUp</h3>
    <label for="fullName">Full Name</label>
    <input type="text" name="fullName" required v-model="fullName" />
    <label for="username" class="mt-4">User Name</label>
    <input type="text" name="username" required v-model="username" />
    <label for="email" class="mt-4">Email</label>
    <input type="email" name="email" required v-model="email" />
    <label for="password" class="mt-4">Password</label>
    <input type="password" name="password" required v-model="password" />
  <br>
  
    <button @click="SignUp" class="SignUp">SignUp</button>
  </div>
  <div v-if="successMessage" class="success-message">{{ successMessage }}</div>
</template>

<script>
import auth from "../auth";
import jwt_decode from 'jwt-decode';
import axios from 'axios';

export default {
  name: "HomeView",
  components: {
  },
  data: function() {
    return {
    authResult: auth.authenticated(),
    AdminViewContent:'',
    fullName: '',
    username: '',
    email: '',
    password: '',
    role: 'ADMIN',
    favGenre: 'Fantasy',
    dob: '1991-06-28',
    minRating: '7',
    emailPreferences: 'twicePerWeek',
    showForm: false,
    successMessage: '',

  }
  },
  methods: {
    toggleForm() {
      this.showForm = !this.showForm;
    },

    async SignUp() {
      var data = {
        fullName: this.fullName,
        username: this.username,
        email: this.email,
        password: this.password,
        role: this.role,
        favGenre: this.favGenre,
        dob: this.dob,
        minRating: this.minRating,
        emailPreferences: this.emailPreferences,
      };
      var headers = {
        'Content-Type': 'application/json',
      };
      console.log(data);
      const response = await axios
        .post('http://localhost:8081/auth/register',
          { 
            fullName: this.fullName,
            username: this.username,
            email: this.email,
            password: this.password,
            role: this.role,
            favGenre: this.favGenre,
            dob: this.dob,
            minRating: this.minRating,
            emailPreferences: this.emailPreferences,  
          },
          headers
        )
        .then((response) => {
          this.token = response.data;
          if (this.token.startsWith('ey')) {
            this.decodedToken = jwt_decode(this.token);
            this.role = this.decodedToken.role;
            console.log(this.decodedToken.role);
            localStorage.setItem('jwtToken', this.token);
            console.log(localStorage.getItem('jwtToken'));
            this.$router.push('/');
          }
          this.successMessage = `${this.username} created`;
          this.showForm = false;
        })
        .catch((e) => {
          console.log(e);
          console.log('error');
        });
      console.log('RESPONSE END: ', response);
    },
  Logout() {
      auth.logout();
      location.assign("/");
    },

  checkRoles: function(role) {
      return auth.hasAnyOf(role);
    }
  },  
  mounted() {
//   if (this.checkRoles('ADMIN')){
//       const token = localStorage.getItem('jwtToken');
//       const headers = {
//       'Content-Type': 'application/json',
//       Authorization: `Bearer ${token}` 
//       };
//       fetch("http://localhost:8080/auth/admin", { headers })
//         .then(response => response.text())
//         .then(body => {
//         this.AdminViewContent = body;
//         })
//         .catch(err => console.log(err.message)) 
//     }

//   if (this.checkRoles('USER')){
//       const token = localStorage.getItem('jwtToken');
//       // Set the Authorization header with the token value
//       const headers = {
//       'Content-Type': 'application/json',
//       Authorization: `Bearer ${token}` 
//       };
//       fetch("http://localhost:8080/auth/user", { headers })
//         .then(response => response.text())
//         .then(body => {
//         this.UserViewContent = body;
//         })
//         .catch(err => console.log(err.message)) 
//     }
}
};
</script>

<style scoped>
.success-message {
    margin: 20px auto;
    font-size: 1.2em;
    font-weight: bold;
    color: green;
  }
.form {
  max-width: 420px;
  margin: 30px auto;
  text-align: left;
  padding: 40px;
  border-radius: 6px;
}
h3 {
  text-align: center;
  color: rgb(6, 31, 31);
}
label {
  color: rgb(3, 24, 24);
  display: inline-block;
  font-size: 0.6em;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: bold;
}
input {
  display: block;
  padding: 10px 6px;
  width: 100%;
  box-sizing: border-box;
  border: 1px solid gray;
  border-radius: 6px;
  color: blue;
}
select {
  display: block;
  padding: 10px 6px;
  width: 100%;
  box-sizing: border-box;
  border: 1px solid gray;
  border-radius: 6px;
  color: blue;
}
button {
  background: rgb(5, 105, 228);
  border: 0;
  padding: 10px 20px;
  margin-top: 20px;
  color: white;
  border-radius: 6px;
  align-items: center;
  text-align: center;
}

</style>
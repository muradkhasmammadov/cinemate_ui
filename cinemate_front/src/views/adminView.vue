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
  <!-- <div v-if="successMessage" class="success-message">{{ successMessage }}</div> -->
<div class="popup-message" v-if="showPopup">
    <div class="popup-content">
      <span class="close" @click="closePopup">&times;</span>
      {{ successMessage }}
    </div>
  </div>

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
    showPopup: false,

  }
  },
  methods: {
    toggleForm() {
      this.showForm = !this.showForm;
    },
    closePopup() {
          this.showPopup = false;
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
          this.showPopup = true;
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
.popup-message {
    position: fixed;
    z-index: 1;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgba(0, 0, 0, 0.4);
  }

  .popup-content {
    position: absolute; /* Updated position */
    top: 50%; /* Center the popup vertically */
    left: 50%; /* Center the popup horizontally */
    transform: translate(-50%, -50%); /* Adjust the popup position */
    background-color: #fefefe;
    padding: 20px;
    border: 1px solid #888;
    width: 50%;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  .close {
    color: #aaaaaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
    cursor: pointer;
  }

  .close:hover,
  .close:focus {
    color: #000;
    text-decoration: none;
    cursor: pointer;
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
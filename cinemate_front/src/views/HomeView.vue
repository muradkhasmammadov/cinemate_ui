<template>
<div class="header">
    <div>
    <button v-if = "authResult" @click="Logout" class="center">Logout</button>

    <div class="name" v-if="checkRoles('ADMIN')">
      <h1> Hello Admin</h1>
      <h3>  Fetched from protected endpoint "../auth/admin": {{AdminViewContent}} </h3>
    </div>
    <div class="name" v-if="checkRoles('USER')">
      <h1> Hello User</h1>
      <h3>  Fetched from protected endpoint "../auth/user": {{UserViewContent}} </h3>
    </div>
  
  </div>
</div>
</template>

<script>
import auth from "../auth";

export default {
  name: "HomeView",
  components: {
  },
  data: function() {
    return {
    authResult: auth.authenticated(),
    AdminViewContent:'',
    UserViewContent:''
  }
  },
  methods: {
  Logout() {
            auth.logout();
            location.assign("/");
    },

  checkRoles: function(roles) {
      return auth.hasAnyOf(roles);
    }
  },  
  mounted() {
  if (this.checkRoles('ADMIN')){
      const token = localStorage.getItem('jwtToken');
      // Set the Authorization header with the token value
      const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}` 
      };
      fetch("http://localhost:8080/auth/admin", { headers })
        .then(response => response.text())
        .then(body => {
        this.AdminViewContent = body;
        })
        .catch(err => console.log(err.message)) 
    }

  if (this.checkRoles('USER')){
      const token = localStorage.getItem('jwtToken');
      // Set the Authorization header with the token value
      const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}` 
      };
      fetch("http://localhost:8080/auth/user", { headers })
        .then(response => response.text())
        .then(body => {
        this.UserViewContent = body;
        })
        .catch(err => console.log(err.message)) 
    }
}
};
</script>

<style scoped>
body{
  margin: 20px 40px;
  font-size: 1.2rem;
  letter-spacing: 1px;
  background: #fafafa;
  position: relative;
}
.name{
  background: rgb(189, 212, 199);
  margin-bottom: 5px;
  padding: 3px 5px;
  border-radius: 10px;
}
h1{
  margin: 0;
  padding: 0;
  font-family: 'Quicksand', sans-serif;
  color: #444;
  background: #7e9756;
}
p{
  background: #796dbd;
}
h1, h2, h3, h4, ul, li, a, input, label, button, div, footer{
  margin: 0;
  padding: 0;
  font-family: 'Quicksand', sans-serif;
  color: #444;
}
nav{
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 80px;
}
input{
  padding: 10px 12px;
  border-radius: 4px;
  border: 1px solid #ddd;
  font-size: 1em;
  width: 100%;
}
label{
  display: block;
  margin: 20px 0 10px;
}
button{
  margin-top: 30px;
  border-radius: 36px;
  background: #FEE996;
  border:0;
  font-weight: 700;
  font-size: 0.8em;
  display: block;
  padding: 10px 16px;
  letter-spacing: 2px;
}
nav{
  display: flex;
  align-items: center;
}
.post {
    width: 80%;
    position: relative;
    padding: 10px;
    margin: 10px auto;
    border: 1px solid gray;
    text-align: left;
}
.center {
  margin: auto;
  border: 0;
  padding: 10px 20px;
  margin-top: 20px;
  margin: 10px auto;
  width: 30%; 
}
.container {
  display: flex;
  justify-content: center;
}
</style>
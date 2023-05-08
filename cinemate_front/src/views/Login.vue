<template>
  <div class="form">
      <h3>LogIn</h3>
      <label for="username">User Name</label>
      <input type="text" name="username"  required v-model="username">
      <label for="password">Password</label>
      <input type="password" name="password" required v-model="password">
    <div class="container">
      <button @click="LogIn"  class="center">LogIn</button>
      <button @click='this.$router.push("/auth/signup")' class="center">Signup</button>
    </div>
  </div>
</template>

<script>
import jwt_decode from 'jwt-decode';
import axios from 'axios';

export default {
name: "LogIn", 

data: function() {
    return {
    username: '',
    password: '',
    }
  },
  methods: {


async LogIn() {
      var data = {
        name: this.username,
        password: this.password
      };

      // using Fetch - post method - send an HTTP post request to the specified URI with the defined body
      axios.post("http://localhost:8080/auth/login", data)
      .then(response => response.text())
      .then(response => {
        // saving the jwt returned in the response into the token variable
        this.token = response;

        //checking if a jwt token is retuned, all jwt tokes start with "ey"
        if (this.token.startsWith("ey"))
        {
        // decoding the jwt and save it in the decodedToken variable
        this.decodedToken = jwt_decode(this.token);
        // saving the returned user role into the role variable
        this.role = this.decodedToken.role
        // saving the token into the windows local storage 
        localStorage.setItem('jwtToken',  this.token);
        console.log(localStorage.getItem('jwtToken'));
        this.$router.push("/");
      }
      }) 
        .catch((e) => {
        console.log(e);
        console.log("error");
      });
    },
  },  
  }

</script>

<style scoped>
.form {
  max-width: 420px;
  margin: 30px auto;
  background: rgb(167, 154, 154);
  text-align: left;
  padding: 40px;
  border-radius: 10px;
}
h3 {
  text-align: center;
  color: rgb(8, 110, 110);
}
label {
  color: rgb(8, 110, 110);
  display: inline-block;
  margin: 25px 0 15px;
  font-size: 0.8em;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: bold;
}
input {
  display: block;
  padding: 10px 6px;
  width: 100%;
  box-sizing: border-box;
  border: none;
  border-bottom: 1px solid white;
  color: blue;
}
button {
  background: rgb(8, 110, 110);
  border: 0;
  padding: 10px 20px;
  margin: 20px 20px 20px 20px;
  color: white;
  border-radius: 20px;
  align-items: center;
  text-align: center;
}
.center {
  margin: auto;
  border: 0;
  padding: 10px 20px;
  margin-top: 20px;
  width: 30%; 
}
.container {
  display: flex;
  justify-content: center;
}
</style>
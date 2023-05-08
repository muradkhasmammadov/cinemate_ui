<template>
  <div class="form">
      <h3>Sign In</h3>
      <label for="username">User Name</label>
      <input type="text" name="username"  required v-model="username">
      <label for="password" class="mt-4">Password</label>
      <input type="password" name="password" required v-model="password">
    <div class="container">
      <button @click="LogIn"  class="center">LogIn</button> <br>
      <button @click='this.$router.push("/auth/register")' class="center">Signup</button>
    </div>
  </div>
</template>
<script>
import jwt_decode from 'jwt-decode';
import axios from 'axios';

export default {
  name: "LogIn",

  data: function () {
    return {
      username: '',
      password: '',
    };
  },
  methods: {
    async LogIn() {
      var data = {
        username: this.username,
        password: this.password,
      };

      try {
        const response = await axios.post(
          "http://localhost:8081/auth/login",
          data,
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        // saving the jwt returned in the response into the token variable
        this.token = response.data;

        //checking if a jwt token is returned, all jwt tokens start with "ey"
        if (typeof this.token === "string" && this.token.startsWith("ey")) {
          // decoding the jwt and save it in the decodedToken variable
          this.decodedToken = jwt_decode(this.token);
          // saving the returned user role into the role variable
          this.role = this.decodedToken.role;
          // saving the token into the windows local storage
          localStorage.setItem("jwtToken", this.token);
          console.log(localStorage.getItem("jwtToken"));
          this.$router.push("/metadata/search");
        }
      } catch (e) {
        console.log(e);
        console.log("error");
        alert("User doesn't exist!")
      }
    },
  },
};
</script>

<style scoped>
.form {
  max-width: 420px;
  margin: 30px auto;
  /* background: rgb(167, 154, 154); */
  text-align: left;
  padding: 40px;
  border-radius: 10px;
}
h3 {
  text-align: center;
  color: rgb(1, 5, 5);
  font-family: 'Times New Roman', Times, serif;
}
label {
  color: rgb(6, 39, 39);
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
button {
  background: rgb(5, 105, 228);
  border: 0;
  padding: 10px 20px;
  margin: 20px 20px 20px 20px;
  color: white;
  border-radius: 6px;
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
<template>
  <div class="form">
    <h3>SignUp</h3>
    <label for="fullName">Full Name</label>
    <input type="text" name="fullName" required v-model="fullName" />
    <label for="username" class="mt-4">User Name</label>
    <input type="text" name="username" required v-model="username" />
    <label for="email" class="mt-4">Email</label>
    <input type="email" name="email" required v-model="email" />
    <label for="password" class="mt-4">Password</label>
    <input type="password" name="password" required v-model="password" />
    <label for="role" class="mt-4">Role</label><br>
    <select name="role" required v-model="role"> 
      <option value="" disabled>Choose your role</option>
      <option value="USER">USER</option>
      <option value="ADMIN">ADMIN</option>
    </select> <br>
    <label for="favGenre" class="mt-4">Favorite Genre</label><br>
   <select name="favGenre" required v-model="favGenre">
    <option value="" disabled>Choose your favorite genre</option>
    <option value="Action">Action</option>
    <option value="Adult">Adult</option>
    <option value="Adventure">Adventure</option>
    <option value="Animation">Animation</option>
    <option value="Biography">Biography</option>
    <option value="Comedy">Comedy</option>
    <option value="Crime">Crime</option>
    <option value="Documentary">Documentary</option>
    <option value="Drama">Drama</option>
    <option value="Family">Family</option>
    <option value="Fantasy">Fantasy</option>
    <option value="Film-Noir">Film-Noir</option>
    <option value="Game-Show">Game-Show</option>
    <option value="History">History</option>
    <option value="Horror">Horror</option>
    <option value="Music">Music</option>
    <option value="Musical">Musical</option>
    <option value="Mystery">Mystery</option>
    <option value="News">News</option>
    <option value="Reality-TV">Reality-TV</option>
    <option value="Romance">Romance</option>
    <option value="Sci-Fi">Sci-Fi</option>
    <option value="Short">Short</option>
    <option value="Sport">Sport</option>
    <option value="Talk-Show">Talk-Show</option>
    <option value="Thriller">Thriller</option>
    <option value="War">War</option>
    <option value="Western">Western</option>
  </select>
  <br>
    <label for="dob" class="mt-4">Date of Birth</label>
    <input type="date" name="dob" required v-model="dob" />
    <label for="minRating" class="mt-4">Minimum rating</label>
    <input type="number" min="0" max="9" name="minRating" required v-model="minRating"  placeholder="Choose rating between 0-9"/>
    <label for="minRating" class="mt-4">Email preferences</label>
    <select name="emailPreferences" required v-model="emailPreferences">
    <option value="" disabled>Choose your email preferences</option>
    <option value="daily">daily</option>
    <option value="weekly">weekly</option>
    <option value="monthly">monthly</option>
    <option value="twicePerWeek">twicePerWeek</option>
    <option value="thricePerWeek">thricePerWeek</option>
    </select>
    <button @click="SignUp" class="SignUp">SignUp</button>
  </div>
</template>

<script>
import jwt_decode from 'jwt-decode';
import axios from 'axios';
export default {
  name: 'SignUp',
  data: function () {
    return {
      fullName: '',
      username: '',
      email: '',
      password: '',
      role: '',
      favGenre: '',
      dob: '',
      minRating: '',
      emailPreferences: '',
    };
  },
  methods: {
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
      // console.log('CSRF TOKEN: ', document.cookie.match(new RegExp(`XSRF-TOKEN=([^;]+)`)));

      // var CSRF_TOKEN = document.cookie.match(new RegExp(`XSRF-TOKEN=([^;]+)`))[1];

      var headers = {
        'Content-Type': 'application/json',
      };

      // using Fetch - post method - send an HTTP post request to the specified URI with the defined body
      console.log(data);
      const response = await axios
        .post(
          'http://localhost:8081/auth/register',
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
          //saving the jwt in the token variable
          this.token = response.data;
          if (this.token.startsWith('ey')) {
            //decoding the jwt and save it in the decodedToken variable
            this.decodedToken = jwt_decode(this.token);
            // saving the returned user role into the role variable
            this.role = this.decodedToken.role;
            console.log(this.decodedToken.role);
            // saving the token into the windows local storage
            localStorage.setItem('jwtToken', this.token);
            console.log(localStorage.getItem('jwtToken'));
            this.$router.push('/');
          }
        })
        .catch((e) => {
          console.log(e);
          console.log('error');
        });
      console.log('RESPONSE END: ', response);
    },
  },
};
</script>

<style scoped>
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

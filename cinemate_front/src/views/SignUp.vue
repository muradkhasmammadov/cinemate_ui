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
    <label for="favGenre" class="mt-4">Favorite Genre</label><br>
   <select name="favGenre" required v-model="favGenre">
    <option value="" disabled>Choose your favorite genre</option>
    <option v-for="genre in genres" :key="genre" :value="genre">{{ genre }}</option>
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
      role: 'USER',
      favGenre: '',
      dob: '',
      minRating: '',
      emailPreferences: '',
      genres: []
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
        })
        .catch((e) => {
          console.log(e);
          console.log('error');
        });
      console.log('RESPONSE END: ', response);
    },
  },
  created: async function () {
  try {
    const response = await axios.get('http://localhost:8081/navigator/genres');
    this.genres = response.data;
  } catch (e) {
    console.log(e);
  }
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

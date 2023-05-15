<template>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />
  <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css" />

  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="loader" v-if="isLoading">
      <div class="spinner"></div>
    </div>
    <div class="container-fluid" v-else>
      <div class="logo-container">
        <a href="#" class="navbar-brand">
          <img src="https://www.clipartmax.com/png/middle/1-10021_clipart-movies-hd-movies-logo-transparent.png" class="logo" alt="CineMate" />
        </a>
      </div>
      <button type="button" class="navbar-toggler" data-bs-toggle="collapse" data-bs-target="#navbarCollapse">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarCollapse">
        <div class="navbar-nav">
          <router-link class="nav-item nav-link active" :to="homeRoute">Home</router-link>
          <router-link v-if="isLoggedIn && userRole === 'USER'" class="nav-item nav-link active" :to="watchlistRoute">My Watchlist</router-link>
          <router-link v-if="isLoggedIn && userRole === 'USER'" class="nav-item nav-link active" :to="discoveryRoute">Discovery</router-link>
          <router-link v-if="isLoggedIn && userRole === 'USER'" class="nav-item nav-link active" :to="discoveryRoute">Update Profile</router-link>
          <router-link v-if="isLoggedIn && userRole === 'ADMIN'" class="nav-item nav-link active" to="/review/all">All Reviews</router-link>
        </div>
        <div class="navbar-nav ms-auto">
          <router-link v-if="!isLoggedIn" to="/auth/register" class="nav-item nav-link">Sign Up</router-link>
          <router-link v-if="!isLoggedIn" to="/auth/login" class="nav-item nav-link">Login</router-link>
          <button v-if="isLoggedIn" @click="Logout" class="center">Logout</button>
        </div>
      </div>
    </div>
  </nav>
  <router-view />

  <footer class="mt-5">
    <div class="footer mt-5">
      <div class="container">
        <div class="footer-content">
          <div class="footer-section">
            <h4>About CineMate</h4>
            <p>CineMate is the world's most populars and authoritative source for movie, TV, and celebrity content.</p>
          </div>
          <div class="footer-section">
            <h4>Quick Links</h4>
            <ul>
              <li><a href="#">Home</a></li>
              <li><a href="#">Metadata</a></li>
              <li><a href="#">Add Review</a></li>
              <li><a href="#">All Reviews</a></li>
              <li><a href="#">Login</a></li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>Contact Us</h4>
            <p>Email: info@cinemate.com</p>
            <p>Phone: +1 (123) 456-7890</p>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; 2023 CineMate. All rights reserved.</p>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
import jwt_decode from 'jwt-decode';
import auth from '../src/auth';
export default {
  name: 'NavBar',
  data() {
    return {
      isLoggedIn: false,
      userRole: null,
      user_id: '',
      isLoading: true,
    };
  },
  created() {
    this.checkUserStatus();
  },
  methods: {
    checkUserStatus() {
    const token = localStorage.getItem("jwtToken");
    if (token) {
      const decodedToken = jwt_decode(token);
      console.log('Decoded token:', decodedToken); 
      console.log('token:', token); 
      if (decodedToken.exp * 1000 > Date.now()) {
        this.isLoggedIn = true;
        this.userRole = decodedToken.role;
        this.user_id = decodedToken.user_id;
        this.sub = decodedToken.sub;
      } else {
        this.isLoggedIn = false;
        this.userRole = null;
        this.user_id = null;
        this.sub = null;
      }
    } else {
      this.isLoggedIn = false;
      this.userRole = null;
      this.user_id = null;
      this.sub = null;
    }
    this.isLoading = false;
  },
    Logout() {
      auth.logout();
      location.assign('/');
    },
  },
  watch: {
    $route() {
      this.checkUserStatus();
    },
  },
  computed: {
    watchlistRoute() {
      return `/watchlist/get/${this.sub}`;
    },
    discoveryRoute() {
      return `/discovery`;
    },
    homeRoute() {
    return this.isLoggedIn && this.userRole === 'ADMIN' ? '/auth/adminView' : '/metadata/search';
  },
  },
};
</script>
<style>
.loader {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-left-color: #555;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  100% {
    transform: rotate(360deg);
  }
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  font-family: 'Times New Roman', Times, serif;
}
.logo-container {
  display: flex;
  align-items: center;
  height: 100%;
}

.logo {
  height: 60px;
  width: auto;
}
.input-group-btn {
  width: 300px;
}
.navbar-collapse button {
  background: rgb(5, 105, 228);
  border: 0;
  padding: 10px 20px;
  margin: 20px 20px 20px 20px;
  color: white;
  border-radius: 6px;
  align-items: center;
  text-align: center;
}

.card-img-top {
  width: 100%;
  height: 100%!important;
  object-fit: cover;
  border-radius: 5px;
}


footer {
  background-image: linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)), url('https://images.purexbox.com/6c4ae5b99340c/imdb-tv-app-arrives-on-xbox-includes-thousands-of-free-movies.large.jpg');
  background-size: cover;
  background-position: center center;
  color: white;
  padding: 40px 0;
}

.footer h4 {
  margin-bottom: 15px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 30px;
  margin-bottom: 20px;
}

.footer-section {
  margin-bottom: 20px;
}

.footer-section ul {
  list-style-type: none;
  padding: 0;
}

.footer-section ul li a {
  color: white;
  text-decoration: none;
}

.footer-section ul li a:hover {
  color: #ffc107;
}

.footer-bottom {
  text-align: center;
  font-size: 14px;
}
</style>

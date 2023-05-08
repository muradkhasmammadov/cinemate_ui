<template>
  <h1>Watchlist</h1>
  <section class="movies-section">
  <div class="container">
    <h1>Explore Movies</h1>
   <div class="navbar-nav">
   <div class="search">
     <input type="text" v-model="ids" class="form-control" placeholder="Type the desired genre">
    <a class="btn btn-watchlist" :href="'/metadata/searchByParams/genre='+ ids + '&info=custom_info'">
     <button type="submit" @click="searchByParams" class="btn btn-default">
        <i class="fa fa-search"></i>
     </button>
   </a>
   </div>
   </div>
    <div class="movies-grid">
      <div class="row">
        <div class="col-md-3 my-3" v-for="movie in movies" :key="movie.id">
          <div class="card h-100">
            <img :src="poster(movie)" class="card-img-top" alt="Movie poster not found">
            <div class="card-body">
              <h5 class="card-title">{{ truncateTitle(movie.title) }}</h5>
              <h6 class="card-subtitle mb-2">{{ movie.genre }}</h6>
              <p class="card-text"> <i class="fa fa-star"></i> {{ movie.rating }}</p>
              <a :href=" '/metadata/searchByIDs/' + movie.id ">
                <button type="button" class="btn btn-read-more">Read More</button> 
              </a><br>
              <button type="button" class="btn btn-watchlist" @click="addToWatchlist(metadata.id)">
                <i class="fa fa-plus"></i> Watchlist
              </button>

            </div>
            <ul class="list-group list-group-flush">
              <li class="list-group-item"> {{movie.director}} </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>
</template>

<script>
import axios from "axios";
import jwt_decode from "jwt-decode";

export default {
  name: "MoviesSection",
  data() {
    return {
      metadatas: [],
      ids: "",
      isLoggedIn: false,
      userId: null,
    };
  },
  methods: {
    poster(metadata) {
      return metadata.poster ? metadata.poster : "https://via.placeholder.com/150";
    },
    truncateTitle(title) {
      return title.length > 20 ? title.substring(0, 20) + "..." : title;
    },
    async searchByParams() {
      // Implement search functionality here
    },
    async addToWatchlist(movieId) {
      if (!this.isLoggedIn) {
        this.$router.push("/auth/login");
      } else {
        try {
          const token = localStorage.getItem("jwtToken");
          const headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          };

          await axios.post(
            `http://localhost:8081/watchlist/add/${this.userId}/${movieId}`,
            {},
            { headers }
          );
        } catch (err) {
          console.log(err.message);
        }
      }
    },
  },
  async created() {
    const token = localStorage.getItem("jwtToken");
    if (token) {
      const decodedToken = jwt_decode(token);
      if (decodedToken.exp * 1000 > Date.now()) {
        this.isLoggedIn = true;
        this.userId = decodedToken.id;
      }
    }

    try {
      const response = await axios.get("http://localhost:8081/metadata/all");
      this.metadatas = response.data;
    } catch (err) {
      console.log(err.message);
    }
  },
};
</script>

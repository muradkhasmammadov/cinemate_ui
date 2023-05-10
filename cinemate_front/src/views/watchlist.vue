<template>
  <h1>Watchlist</h1>
  <section class="movies-section">
    <div class="container">
      <div class="movies-grid">
        <div class="row">
          <div class="col-md-3 my-3" v-for="movie in movies" :key="movie.id">
            <div class="card h-100">
              <img :src="poster(movie)" class="card-img-top" alt="Movie poster not found">
              <div class="card-body">
                <h5 class="card-title">{{ truncateTitle(movie.title) }}</h5>
                <h6 class="card-subtitle mb-2">{{ movie.genre }}</h6>
                <p class="card-text"> <i class="fa fa-star"></i> {{ movie.rating }}</p>
              </div>
              <ul class="list-group list-group-flush">
                <li class="list-group-item"> {{movie.director}} </li>
              </ul>
              <button type="button" class="btn btn-danger" @click="removeFromWatchlist(movie.id)">Remove</button>
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
      movies: [],
      isLoggedIn: false,
      userId: null,
    };
  },
  methods: {
    poster(movie) {
      return movie.poster === 'not found' ? 'https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/04174dbc-fe2f-4983-824a-6d80412e917e/de25zez-cffb25c6-278b-4c76-a63e-5a75b6b4892d.png/v1/fill/w_800,h_600,q_80,strp/404_not_found__20th_century_box_style__by_xxneojadenxx_de25zez-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NjAwIiwicGF0aCI6IlwvZlwvMDQxNzRkYmMtZmUyZi00OTgzLTgyNGEtNmQ4MDQxMmU5MTdlXC9kZTI1emV6LWNmZmIyNWM2LTI3OGItNGM3Ni1hNjNlLTVhNzViNmI0ODkyZC5wbmciLCJ3aWR0aCI6Ijw9ODAwIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.GMT6ZFtK1otxk4cvLolKhpYrWievHzrf64y4N7sP8ZM' : movie.poster;
    },
    truncateTitle(title) {
      return title.length > 20 ? title.substring(0, 20) + "..." : title;
    },
    async removeFromWatchlist(movieId) {
      if (this.isLoggedIn) {
        try {
          const token = localStorage.getItem("jwtToken");
          const headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          };

          await axios.delete(
            `http://localhost:8081/watchlist/${this.userId}/delete/${movieId}`,
            { headers }
          );

          // Remove the movie from the list
          this.movies = this.movies.filter((movie) => movie.id !== movieId);
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

    if (this.isLoggedIn) {
      try {
        const response = await axios.get(`http://localhost:8081/watchlist/${this.userId}`);
        this.movies = response.data;
      } catch (err) {
        console.log(err.message);
      }
    }
  },
};
</script>

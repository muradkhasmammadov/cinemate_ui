<template>

<section class="movies-section">
  <div class="container">
    <h1>{{ this.$route.params.genreValue }}  Movies</h1>
    <div class="movies-grid">
      <div class="row">
        <div class="col-md-3 my-3" v-for="metadata in metadatas" :key="metadata.id">
          <div class="card h-100">
            <img :src="poster(metadata)" class="card-img-top" alt="Movie poster not found">
            <div class="card-body">
              <h5 class="card-title">{{ truncateTitle(metadata.title) }}</h5>
              <h6 class="card-subtitle mb-2">{{ metadata.genre }}</h6>
              <p class="card-text"> <i class="fa fa-star"></i> {{ metadata.rating }}</p>
              <a :href=" '/navigator/searchByIDs/' + metadata.id ">
                <button type="button" class="btn btn-read-more">Read More</button> 
              </a><br>
              <button type="button" class="btn btn-watchlist"  @click="addToWatchlist(metadata.id)" v-if="!isInWatchlist(metadata.id)"><i class="fa fa-plus"></i> Watchlist</button>
            </div>
            <ul class="list-group list-group-flush">
              <li class="list-group-item"> {{metadata.director}} </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>
</template>


<script>
import axios from 'axios';
import jwt_decode from "jwt-decode";
export default {
  name: "AllMetada",
  data() {
    return {
      metadatas: [],
      ids: '',
      movies: [],
      watchlistIds: [],
    };
  },
  methods: {
    fetchMetadata() {
  const genre = this.$route.params.genreValue;
  axios.get(`/navigator/searchByParams/genre=${genre}&info=custom_info`)
    .then((response) => this.metadatas = response.data)
    .catch((err) => console.log(err.message));
    // alert('An error occurred while adding the review. Please try again.');
},

    truncateTitle(title){
      return title.length > 20 ? title.slice(0, 20) + '...' : title;
    },
    poster(metadata) {
      return metadata.poster === 'not found' ? 'https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/04174dbc-fe2f-4983-824a-6d80412e917e/de25zez-cffb25c6-278b-4c76-a63e-5a75b6b4892d.png/v1/fill/w_800,h_600,q_80,strp/404_not_found__20th_century_box_style__by_xxneojadenxx_de25zez-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NjAwIiwicGF0aCI6IlwvZlwvMDQxNzRkYmMtZmUyZi00OTgzLTgyNGEtNmQ4MDQxMmU5MTdlXC9kZTI1emV6LWNmZmIyNWM2LTI3OGItNGM3Ni1hNjNlLTVhNzViNmI0ODkyZC5wbmciLCJ3aWR0aCI6Ijw9ODAwIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.GMT6ZFtK1otxk4cvLolKhpYrWievHzrf64y4N7sP8ZM' : metadata.poster;
    },
    async addToWatchlist(contentId) {
          const token = localStorage.getItem("jwtToken");
          const decodedToken = jwt_decode(token)
          const userId = decodedToken.id; // Assuming you have stored the user's ID in localStorage
          const sub = decodedToken.sub; // Assuming you have stored the user's ID in localStorage
          console.log(decodedToken.id)
          if (!token || !userId) {
            this.$router.push("/auth/login");
            return;
          }

          const headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          };

          try {
            axios.post(
              `http://localhost:8081/watchlist/add`,
              {
                userId: sub,
                contentId: contentId
              },
              { headers }
            );
            this.watchlistIds.push(contentId);
          } catch (err) {
            console.log(err.message);
            // alert("An error occurred while adding the movie to the watchlist. Please try again.");
          }
    },
    isInWatchlist(contentId) {
  return this.watchlistIds.includes(contentId);
},
  },
  mounted() {
    // call fetchProducts() when this element (AllReviews()) mounts 
    this.fetchMetadata();
    console.log("mounted");
  },
  async created() {
    const token = localStorage.getItem("jwtToken");
    if (token) {
      const decodedToken = jwt_decode(token);
      if (decodedToken.exp * 1000 > Date.now()) {
        this.isLoggedIn = true;
        this.userId = decodedToken.id;
        this.sub = decodedToken.sub;
      }
    }
    if (this.isLoggedIn) {
      const token = localStorage.getItem("jwtToken");
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      try {
        const response = await axios.get(`http://localhost:8081/watchlist/get/${this.sub}`, {headers});
        this.movies = response.data;
        console.log(headers)
        this.watchlistIds = response.data.map(movie => movie.id); 

      } catch (err) {
        console.log(err.message);
      }
    }
    }
};

</script>

<style>
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
}


.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 15px;
}
.movies-section {
  background-color: #f0f0f0;
  padding: 40px 0;
}
.fa-star, .fa-plus{
  color: #ffc107;
}
.movies-section h2 {
  margin-bottom: 30px;
  font-size: 28px;
}
.search {
  display: flex;
  justify-content: center;
  align-items: center;
}

.search .form-control {
  width: 70%;
  margin-right: 10px;
}

.fa-search {
  color: chocolate;
  font-size: 22px !important;
}

.card {
  border: none;
  background-color: transparent;
}

.card-img-top {
  width: 100%;
  height: auto;
  object-fit: cover;
  border-radius: 5px;
}

.card-body {
  padding: 20px 0 0;
}

.card-title {
  font-size: 18px;
  font-weight: bold;
}

.card-subtitle {
  font-size: 14px;
  color: #6c757d;
}

.card-text {
  font-size: 14px;
  color: #6c757d;
}

.list-group-item {
  border: none;
  padding: 0.5rem 0;
  font-size: 14px;
}
.btn-watchlist {
  background-color: #f0c14b;
  border-color: #a88734;
  color: #111;
  font-weight: 600;
  border-radius: 3px;
  padding: 5px 10px;
  font-size: 14px;
}
.btn-read-more {
  background-color: #5799ef!important;
  width: 150px;
  border: 2px solid #111;
  color: #111;
  font-weight: 600;
  border-radius: 20px;
  padding: 5px 15px;
  font-size: 14px;
  margin-left: 5px;
  transition: 0.3s;
}

.btn-read-more:hover {
  background: rgba(var(--ipt-on-baseAlt-rgb,"255,255,255"),var(--ipt-baseAlt-hover-opacity,.08));
  color: #fff!important;
  animation: .5s all;
}


.footer {
  background-image: linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)),  url('https://images.purexbox.com/6c4ae5b99340c/imdb-tv-app-arrives-on-xbox-includes-thousands-of-free-movies.large.jpg');
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
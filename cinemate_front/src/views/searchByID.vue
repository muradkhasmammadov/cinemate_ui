<template>
  <section class="movies-section">
    <div class="container">
      <h1>{{}}</h1>
      <div class="movies-grid">
        <div class="row" v-for="metadata in metadatas" :key="metadata.id">
          <div class="col-md-6 my-3">
            <img :src="poster(metadata)" class="card-img-top" alt="Movie poster not found" />
          </div>
          <div class="col-md-6 my-3">
            <div class="card h-100">
              <div class="card-body">
                <h1 class="display-4">{{ metadata.title }}</h1>
                <div class="text-justify">
                  <hr class="border border-danger border-2 opacity-100" />
                  <h3>
                    <i
                      ><strong>About content:<br /></strong
                    ></i>
                    <h4>{{ metadata.description }}</h4>
                    <br />
                  </h3>
                  <h3>
                    <i><strong>Genre:</strong></i
                    ><br />
                    {{ metadata.genre }}<br /><br />
                  </h3>
                  <h3>
                    <i><strong>Cast:</strong></i
                    ><br />
                    {{ metadata.cast }}<br /><br />
                  </h3>
                  <h3>
                    <i><strong>Release Date:</strong></i> {{ metadata.release_date }}<br /><br />
                  </h3>
                  <h4>
                    <i><strong>IMDB Rating:</strong></i
                    >&nbsp;<i class="fa fa-star"></i><strong> {{ metadata.rating.toString() }}</strong
                    ><br />
                  </h4>

                  <div v-if="this.cineMateRating !== 'No ratings yet'">
                    <h4>
                      <i><strong>CineMate Rating:</strong> </i>
                      &nbsp;<i class="fa fa-star"></i><strong> {{ this.cineMateRating }}</strong>
                    </h4>
                  </div>
                  <br />
                  <div v-if="this.cineMateRating === 'No ratings yet'">
                    <h4>
                      <i><strong>No CineMate ratings yet</strong> </i>
                    </h4>
                  </div>
                  <br />
                </div>
                <hr class="border border-primary border-2 opacity-100" />
                <button type="button" class="btn btn-watchlist" @click="addToWatchlist(metadata.id)" v-if="!isInWatchlist(metadata.id)">
                  <h4><i class="fa fa-plus"></i>Add to Watchlist</h4>
                </button>
                <br />
              </div>
              <ul class="list-group list-group-flush">
                <li class="list-group-item">
                  <h5>Directed by: {{ metadata.director }}</h5>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <hr class="border border-success border-3 opacity-100" />
      <div class="container last">
        <div class="row">
          <div class="col-md-6 add-review">
            <h3>Share your thoughts</h3>
            <div class="form-group invisible">
              <input type="text" class="form-control" id="formGroupExampleInput2" placeholder="Content" required v-model="review.contentId" readonly />
            </div>
            <div class="form-group">
              <input type="text" class="form-control" id="formGroupExampleInput2" placeholder="Your Feedback" required v-model="review.body" />
            </div>
            <div class="form-group">
              <input type="number" class="form-control" id="formGroupExampleInput2" min="1" max="9" placeholder="Rate" required v-model="review.score" />
            </div>
            <input type="submit" class="btn btn-primary mt-3" @click="addReview" value="Add Review" />
          </div>

          <div class="col-md-6 col-lg-6 comments mt-5">
            <div class="card shadow-0 border" style="background-color: #f0f2f5">
              <div class="card-body p-4">
                <!-- Iterate over the comments array -->
                <div v-if="comments.length === 0">
                  <h2>
                    Nothing here yet...<br />
                    Add a review to see it here!🙂
                  </h2>
                </div>
                <div v-for="comment in comments" :key="comment.id" class="card mb-4">
                  <div class="card-body">
                    <p>{{ comment.body }}</p>

                    <div class="d-flex justify-content-between">
                      <div class="d-flex flex-row align-items-center">
                        <img src="https://cdn.pixabay.com/photo/2020/07/01/12/58/icon-5359553_960_720.png" alt="avatar" width="25" height="25" />
                        <p class="small mb-0 ms-2">{{ comment.userId }}</p>
                      </div>
                      <div class="d-flex flex-row align-items-center">
                        <p class="small text-muted mb-0">Rating</p>
                        <i class="far fa-thumbs-up mx-2 fa-xs text-black" style="margin-top: -0.16rem"></i>
                        <p class="small text-muted mb-0"><i class="fa fa-star"> </i> {{ comment.score }}</p>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- <div class="card mb-4">
          <div class="card-body">
            <p>Type your note, and hit enter to add it</p>

            <div class="d-flex justify-content-between">
              <div class="d-flex flex-row align-items-center">
                <img src="https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(4).webp" alt="avatar" width="25"
                  height="25" />
                <p class="small mb-0 ms-2">Martha</p>
              </div>
              <div class="d-flex flex-row align-items-center">
                <p class="small text-muted mb-0">Upvote?</p>
                <i class="far fa-thumbs-up mx-2 fa-xs text-black" style="margin-top: -0.16rem;"></i>
                <p class="small text-muted mb-0">3</p>
              </div>
            </div>
          </div>
        </div> -->
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import axios from 'axios';
import jwt_decode from 'jwt-decode';

export default {
  name: 'AMetada',
  data() {
    return {
      metadatas: [],
      ids: '',
      movies: [],
      review: {
        userId: 0,
        contentId: '',
        body: '',
        score: 0,
      },
      watchlistIds: [],
      comments: [],
    };
  },
  methods: {
    fetchMetadata() {
      const id = this.$route.params.id;
      axios
        .get(`/navigator/searchByIDs/${id}`)
        .then((response) => {
          this.metadatas = response.data;
        })
        .catch((err) => console.log(err.message));
      // alert('An error occurred while adding the review. Please try again.');
    },
    fetchComments(contentId) {
      const token = localStorage.getItem('jwtToken');
      const headers = {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      };
      axios
        .get(`http://localhost:8081/review/content/${contentId}`, { headers })
        .then((response) => {
          this.comments = response.data;
        })
        .catch((error) => {
          console.log(error.message);
        });
    },
    poster(metadata) {
      return metadata.poster === 'not found'
        ? 'https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/04174dbc-fe2f-4983-824a-6d80412e917e/de25zez-cffb25c6-278b-4c76-a63e-5a75b6b4892d.png/v1/fill/w_800,h_600,q_80,strp/404_not_found__20th_century_box_style__by_xxneojadenxx_de25zez-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NjAwIiwicGF0aCI6IlwvZlwvMDQxNzRkYmMtZmUyZi00OTgzLTgyNGEtNmQ4MDQxMmU5MTdlXC9kZTI1emV6LWNmZmIyNWM2LTI3OGItNGM3Ni1hNjNlLTVhNzViNmI0ODkyZC5wbmciLCJ3aWR0aCI6Ijw9ODAwIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.GMT6ZFtK1otxk4cvLolKhpYrWievHzrf64y4N7sP8ZM'
        : metadata.poster;
    },
    addReview() {
      console.log('addReview called');
      const token = localStorage.getItem('jwtToken');
      const decodedToken = jwt_decode(token);
      const userId = decodedToken.sub;
      const headers = {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      };
      var data = {
        userId: userId,
        contentId: this.review.contentId,
        body: this.review.body,
        score: this.review.score,
      };
      console.log(data);
      axios
        .post('http://localhost:8081/review/add', data, { headers })
        .then((response) => {
          console.log(response.data);
          console.log('success');
          window.location.reload();
        })
        .catch((e) => {
          console.log(e);
          console.log('error');
        });
    },
    async addToWatchlist(contentId) {
      const token = localStorage.getItem('jwtToken');
      const decodedToken = jwt_decode(token);
      const userId = decodedToken.id; // Assuming you have stored the user's ID in localStorage
      const sub = decodedToken.sub; // Assuming you have stored the user's ID in localStorage
      console.log(decodedToken.id);
      if (!token || !userId) {
        this.$router.push('/auth/login');
        return;
      }

      const headers = {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      };

      try {
        axios.post(
          `http://localhost:8081/watchlist/add`,
          {
            userId: sub,
            contentId: contentId,
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
      if (!this.isLoggedIn) {
        return false;
      }
      return this.watchlistIds.includes(contentId);
    },
    async getAvgRating(contentId) {
      const token = localStorage.getItem('jwtToken');
      const headers = {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      };
      try {
        const response = await axios.post(
          `http://localhost:8081/review/get/rating`,
          {
            contentId: contentId,
          },
          { headers }
        );
        // console.log(contentId);
        this.cineMateRating = response.data;
      } catch (error) {
        console.error('Error fetching average rating:', error);
      }
    },
  },

  mounted() {
    this.fetchMetadata();
    this.fetchComments(this.$route.params.id);
    console.log('mounted');
    this.getAvgRating(this.$route.params.id);
  },
  async created() {
    if (this.$route.params.id) {
      this.review.contentId = this.$route.params.id;
    }

    // Get the user_id from the JWT token
    const token = localStorage.getItem('jwtToken');
    if (token) {
      const decodedToken = jwt_decode(token);
      if (decodedToken.exp * 1000 > Date.now()) {
        this.review.userId = decodedToken.id;
        this.isLoggedIn = true;
        this.userId = decodedToken.id;
        this.sub = decodedToken.sub;
      }
      if (this.isLoggedIn) {
        const token = localStorage.getItem('jwtToken');
        const headers = {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        };
        try {
          const response = await axios.get(`http://localhost:8081/watchlist/get/${this.sub}`, { headers });
          this.movies = response.data;
          console.log(headers);
          this.watchlistIds = response.data.map((movie) => movie.id);
        } catch (err) {
          console.log(err.message);
        }
      } else {
        this.$router.push('/auth/login');
      }
    } else {
      this.$router.push('/auth/login');
    }
  },
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
  background-color: #ffffff;
  padding: 40px 0;
}
.fa-star,
.fa-plus {
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
.btn-add-review {
  background-color: #bf740b !important;
  width: 150px;
  border: 2px solid #111;
  color: #ffffff !important;
  font-weight: 600;
  border-radius: 20px;
  padding: 5px 15px;
  font-size: 14px;
  margin-left: 5px;
  transition: 0.3s;
}

.btn-read-more:hover {
  background: rgba(var(--ipt-on-baseAlt-rgb, '255,255,255'), var(--ipt-baseAlt-hover-opacity, 0.08));
  color: #fff !important;
  animation: 0.5s all;
}
.last {
  width: 100%;
}
.add-review {
  float: left;
}
.comments {
  width: 100%;
}

.footer {
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

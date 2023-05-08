<template>
  <!-- <div class="AllReviews"> -->
    <div id="reviews-list">
      <h1>All reviews</h1>
      <ul>
        <div>
          <a class="singlereview" :href="'/review/view/{review.userId}'">
            <table class="table table-bordered table-hover">
              <thead>
                <th>User ID</th>
                <th>Content ID</th>
                <th>Body</th>
                <th>Score</th>
              </thead>
              <tbody class="item" v-for="review in reviews" :key="review.id">
                <tr>
                  <td>{{ review.userId }}</td>
                  <td>{{ review.contentId }}</td>
                  <td>{{ review.body }}</td>
                  <td>{{ review.score }}</td>
                </tr>
              </tbody>
            </table>
          </a>
        </div>
      </ul>
    </div>
  <!-- </div> -->
  <h1>Watclish</h1>
</template>

<script>
import axios from "axios";
import jwt_decode from "jwt-decode";

export default {
  name: "WatchList",
  data() {
    return {
      reviews: [],
      AdminViewContent: "",
      UserViewContent: ""
    };
  },
  methods: {
    isLoggedIn() {
      const token = localStorage.getItem("jwtToken");
      if (token) {
        const decodedToken = jwt_decode(token);
        if (decodedToken.exp * 1000 > Date.now()) {
          return true;
        }
      }
      return false;
    },
    checkRoles(role) {
      const token = localStorage.getItem("jwtToken");
      if (token) {
        const decodedToken = jwt_decode(token);
        if (decodedToken.role && decodedToken.role === role) {
          return true;
        }
      }
      return false;
    },
    fetchReviews() {
        const userId = this.$route.params.userId;
      axios
        .get("/watchlist/" + userId)
        .then((response) => (this.reviews = response.data))
        .catch((err) => console.log(err.message));
    },
  },
  async created() {
    if (!this.isLoggedIn()) {
      this.$router.push("/auth/login");
    } else if (this.checkRoles("ADMIN")) {
      const token = localStorage.getItem("jwtToken");
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      try {
        fetch("http://localhost:8080/review/all", {headers})
        .then(response => response.text())
        .then(body => {
        this.AdminViewContent = body;
        })
        console.log("token: " + token);
      } catch (err) {
        console.log(err.message);
      }
    } else if (this.checkRoles("USER")) {
      const token = localStorage.getItem("jwtToken");
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      try {
        const response = await axios.get("http://localhost:8080/metadata/search", {
          headers,
        });
        this.UserViewContent = response.data;
      } catch (err) {
        console.log(err.message);
      }
    }
    this.fetchReviews();
    console.log("created");
  },
};
</script>

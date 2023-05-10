<template>
  <div class="AllReviews">
    <div id="reviews-list">
      <h1>All reviews</h1>
      <ul>
        <div>
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
                  <td>
                    <button class="btn btn-danger" @click="deleteReview(review.id)">Delete</button>
                  </td>
                </tr>
              </tbody>
            </table>
        </div>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import jwt_decode from "jwt-decode";

export default {
  name: "AllReviews",
  data() {
    return {
      reviews: [],
      isAdmin: false,
      isLoggedIn: false,
    };
  },
  methods: {
    async fetchReviews() {
  try {
    const response = await axios.get("/review/all");
    console.log(response.data); 
    this.reviews = response.data;
  } catch (err) {
    console.log(err.message);
  }
},
    async deleteReview(id) {
      try {
        const token = localStorage.getItem("jwtToken");
        const headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        };
        console.log(id);
        axios.delete(`http://localhost:8081/review/delete/${id}`, { headers });

        this.reviews = this.reviews.filter((review) => review.id !== id);
      } catch (err) {
        console.log(err.message);
      }
    },
  },
  async created() {
    const token = localStorage.getItem("jwtToken");
    if (token) {
      const decodedToken = jwt_decode(token);
      if (decodedToken.exp * 1000 > Date.now()) {
        this.isLoggedIn = true;
        this.isAdmin = decodedToken.role === "ADMIN";
      }
    }

    if (!this.isLoggedIn) {
      this.$router.push("/auth/login");
    } else {
      if (this.isAdmin) {
        try {
          const headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          };
          const response = await axios.get("http://localhost:8081/review/all", {
            headers,
          });
          this.reviews = response.data;
        } catch (err) {
          console.log(err.message);
        }
      } else {
        try {
          const headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          };
          const response = await axios.get("http://localhost:8081/metadata/search", {
            headers,
          });
          this.reviews = response.data;
        } catch (err) {
          console.log(err.message);
        }
      }
    }
  },
};
</script>

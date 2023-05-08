<template>
  <h1>Watchlist</h1>
</template>

<script>
import axios from 'axios';
import jwt_decode from 'jwt-decode';

export default {
  name: 'WatchList',
  data() {
    return {
      reviews: [],
      user_id: '',
    };
  },
  methods: {
    async fetchWatchlist() {
      const token = localStorage.getItem('jwtToken');
      const headers = {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      };
      try {
        const response = await axios.get(`http://localhost:8081/watchlist/${this.user_id}`, { headers });
        this.reviews = response.data;
        // console.log("REVIEWS: \n" + Object.entries(this.reviews[0]));
      } catch (err) {
        console.log(err.message);
      }
    },
  },
  async created() {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      this.$router.push('/auth/login');
    } else {
      const decodedToken = jwt_decode(token);
      const isTokenValid = decodedToken.exp * 1000 > Date.now();

      if (!isTokenValid) {
        this.$router.push('/auth/login');
      } else {
        this.user_id = decodedToken.sub;
        console.log(decodedToken);
        await this.fetchWatchlist();
      }
    }
  },
};
</script>

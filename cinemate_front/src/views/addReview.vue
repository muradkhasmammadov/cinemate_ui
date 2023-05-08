<template>
<div class="container mt-5">
  <div class="row">
    <div class="col-md-6">
    <h1>Add New Review</h1>
      <div class="form-group">
        <!-- <label for="formGroupExampleInput">Example label</label>   ` -->
        <input type="text" class="form-control" id="formGroupExampleInput" placeholder="Content" required v-model="review.user_id" readonly> 
      </div>
      <div class="form-group">
        <!-- <label for="formGroupExampleInput2">Another label</label> -->
        <input type="text" class="form-control" id="formGroupExampleInput2" placeholder="Content" required v-model="review.content_id" readonly>
      </div>
      <div class="form-group">
        <!-- <label for="formGroupExampleInput2">Another label</label> -->
        <input type="text" class="form-control" id="formGroupExampleInput2" placeholder="Your Feedback" required v-model="review.body">
      </div>
      <div class="form-group">
        <!-- <label for="formGroupExampleInput2">Another label</label> -->
        <input type="number" class="form-control" id="formGroupExampleInput2" placeholder="Rate" required v-model="review.score">
      </div>
      <a href="/review/all">
        <input type="submit" class="btn btn-primary mt-3" @click="addReview" value="Add Review">
      </a>
    </div>
  </div>
</div>
</template>

<script>
import axios from 'axios'
import jwt_decode from "jwt-decode";

export default {
  name: "AddReview",
  data() {
    return {
      review: {
        user_id: 0,
        content_id: "",
        body: "",
        score: 0,
      },
      // review: null,
    }
  },
  methods: {
    addReview() {
      var data = {
        user_id: this.review.user_id,
        content_id: this.review.content_id,
        body: this.review.body,
        score: this.review.score,
      };  
      axios.post('/review/add', data)
      .then((response) => {
        console.log(response.data);
        this.$router.push("/review/all");
      })
      .catch((e) => {
        console.log(e);
        console.log("error");
        // alert('An error occurred while adding the review. Please try again.');
      });
    },
    
  },
  created() {
  // Set content_id based on the route parameter
  if (this.$route.params.id) {
    this.review.content_id = this.$route.params.id;
  }
  
  // Get the user_id from the JWT token
  const token = localStorage.getItem("jwtToken");
  if (token) {
    const decodedToken = jwt_decode(token);
    if (decodedToken.exp * 1000 > Date.now()) {
      this.review.user_id = decodedToken.user_id;
    } else {
      this.$router.push("/auth/login");
    }
  } else {
    this.$router.push("/auth/login");
  }
},

  
};
</script>

<style scoped>
.container{
  padding-left: 350px;
}
.container input{
  float: left;
  margin-top: 20px;
}

.form-group input{
  border: 0.5px solid gray;
}
</style>
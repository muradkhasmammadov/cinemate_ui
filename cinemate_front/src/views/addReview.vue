<template>
<div class="container mt-5">
  <div class="row">
    <div class="col-md-6">
    <h1>Add New Review</h1>
      <div class="form-group">
        <input type="text" class="form-control" id="formGroupExampleInput" placeholder="Content" required :value="review.userId" readonly> 
      </div>
      <div class="form-group">
        <input type="text" class="form-control" id="formGroupExampleInput2" placeholder="Content" required :value="review.contentId" readonly>
      </div>
      <div class="form-group">
        <input type="text" class="form-control" id="formGroupExampleInput2" placeholder="Your Feedback" required :value="review.body">
      </div>
      <div class="form-group">
        <input type="number" class="form-control" id="formGroupExampleInput2" placeholder="Rate" required :value="review.score">
      </div>
        <input type="submit" class="btn btn-primary mt-3" @click="addReview" value="Add Review">
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
        userId: 0,
        contentId: "",
        body: "",
        score: 0,
      },
    }
  },
  methods: {
    addReview() {
      const token = localStorage.getItem("jwtToken");
      const decodedToken = jwt_decode(token)
      const userId = decodedToken.id;
      var data = {
        userId: userId,
        contentId: this.review.contentId,
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
      });
    },
    
  },
  created() {
  if (this.$route.params.id) {
    this.review.contentId = this.$route.params.id;
  }
  
  // Get the user_id from the JWT token
  const token = localStorage.getItem("jwtToken");
  if (token) {
    const decodedToken = jwt_decode(token);
    if (decodedToken.exp * 1000 > Date.now()) {
      this.review.userId = decodedToken.id;
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
<template>
  <!-- <div class="form">
    <h3>Add a Review</h3>

    <label for="userId">UserId: </label>
    <input contentId="userId" type="text" id="userId" required v-model="review.userId" />

    <label for="contentId">ContentId: </label>
    <input contentId="contentId" type="text" id="contentId" required v-model="review.contentId" />

    <label for="body">Body: </label>
    <input contentId="body"  type="text" id="body" required v-model="review.body"/>

    <label for="score">Score: </label>
    <input contentId="score"  type="number" id="score" required v-model="review.score"/>

    <button @click="addReview" class="addPost">Add Review</button>
  </div> -->

<div class="container mt-5">
  <div class="row">
    <div class="col-md-6">
    <h1>Add New Review</h1>
      <div class="form-group">
        <!-- <label for="formGroupExampleInput">Example label</label>    -->
        <input type="text" class="form-control" id="formGroupExampleInput" placeholder="Content" required v-model="review.userId"> 
      </div>
      <div class="form-group">
        <!-- <label for="formGroupExampleInput2">Another label</label> -->
        <input type="text" class="form-control" id="formGroupExampleInput2" placeholder="Content" required v-model="review.contentId">
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
      // review: null,
    }
  },
  methods: {
    addReview() {
      var data = {
        userId: this.review.userId,
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
        // alert('An error occurred while adding the review. Please try again.');
      });
    },
    
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
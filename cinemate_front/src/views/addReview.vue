<template>
  <div class="form">
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
        score: 0.0,
      },
      // review: null,
    }
  },
  // mounted () {
  //   axios
  //     .get('http://localhost:8080/review/all')
  //     .then(response => (this.review = response.data))
  // },
  methods: {
    addReview() {
      var data = {
        userId: this.review.userId,
        contentId: this.review.contentId,
        body: this.review.body,
        score: this.review.score,
      };  
      // using Fetch - post method - send an HTTP post request to the specified URI with the defined body
      axios.post('/review/add', data)
      .then((response) => {
        console.log(response.data);
        // redirect to /allposts view
        this.$router.push("/review/all");
      })
      .catch((e) => {
        console.log(e);
        console.log("error");
      });
    },
    
  },
  
};
</script>

<style scoped>
.form {
  max-width: 420px;
  margin: 30px auto;
  background: rgb(167, 154, 154);
  text-align: left;
  padding: 40px;
  border-radius: 10px;
}
h3 {
  text-align: center;
  color: rgb(8, 110, 110);
}
label {
  color: rgb(8, 110, 110);
  display: inline-block;
  margin: 25px 0 15px;
  font-size: 0.8em;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: bold;
}
input {
  display: block;
  padding: 10px 6px;
  width: 100%;
  box-sizing: border-box;
  border: none;
  border-bottom: 1px solid white;
  color: blue;
}
button {
  background: rgb(8, 110, 110);
  border: 0;
  padding: 10px 20px;
  margin-top: 20px;
  color: white;
  border-radius: 20px;
  align-items: center;
  text-align: center;
}
</style>
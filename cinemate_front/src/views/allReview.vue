<template>
  <div class="AllReviews">
    <div id="reviews-list">
    <h1>All reviews</h1>
      <ul>
        <div>
          <!--  We are putting an anchor for each review, when we click on it, we will be directed to the specific review view (/areview/) /  -->
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
  </div>
</template>


<script>
import axios from 'axios';
export default {
  name: "AllReviews",
  data() {
    return {
      reviews: [],
    };
  },
  methods: {
    fetchReviews() {
      // fetch is a GET request by default unless stated otherwise. Therefore, it will fetch all products from the database
    axios.get('/review/all')
        .then((response) => this.reviews = response.data)
        // .then((data) => (this.reviews = data))
        .catch((err) => console.log(err.message));
        // alert('An error occurred while adding the review. Please try again.');
    },
  },
  mounted() {
    // call fetchProducts() when this element (AllReviews()) mounts 
    this.fetchReviews();
    console.log("mounted");
  },
};

</script>

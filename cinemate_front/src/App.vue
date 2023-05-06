<template>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
        <div class="logo-container">
          <a href="#" class="navbar-brand">
              <img src="https://www.clipartmax.com/png/middle/1-10021_clipart-movies-hd-movies-logo-transparent.png" class="logo" alt="CineMate">
          </a>
        </div>
        <button type="button" class="navbar-toggler" data-bs-toggle="collapse" data-bs-target="#navbarCollapse">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarCollapse">
            <div class="navbar-nav">
                <router-link class="nav-item nav-link active" to="/metadata/search">Home</router-link>
                <router-link class="nav-item nav-link active" to="/metadata/search">Metadata</router-link>
                <router-link class="nav-item nav-link active" to="/review/add">Add Review </router-link>
                <router-link class="nav-item nav-link active" to="/review/all">All Reviews</router-link>
            </div>
            <div class="navbar-nav ms-auto">
                <router-link to="/review/all" class="nav-item nav-link">Login</router-link>
            </div>
        </div>
    </div>
</nav>
  <router-view/>
</template>


<style>
 #app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  font-family: 'Times New Roman', Times, serif;
}
.logo-container {
  display: flex;
  align-items: center;
  height: 100%;
}

.logo {
  height: 60px;
  width: auto;
}
.input-group-btn{
    width: 300px;
}
</style>

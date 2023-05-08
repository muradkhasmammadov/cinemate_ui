import { createRouter, createWebHistory } from 'vue-router'

import addReview from '../views/addReview.vue';
import allReview from '../views/allReview.vue';
import metadata from '../views/metadata';
import searchByParams from '../views/searchByParams';
import searchByID from '../views/searchByID';
import HomeView from "../views/HomeView.vue";
import adminView from "../views/adminView.vue";
import auth from "../auth";
import SignUp from "../views/SignUp.vue"
import Login from "../views/Login"
import watchlist from "../views/watchlist"
import jwt_decode from "jwt-decode";

const routes = [
    {
        path: "/",
        name: "home",
        component: HomeView,
        beforeEnter: async (to, from, next) => {
          let authResult = await auth.authenticated();
          if (!authResult) {
            next("/auth/login");
          } else {
            // Get the token from local storage
            const token = localStorage.getItem("jwtToken");
      
            // Decode the token to get the user role
            const decodedToken = jwt_decode(token);
            const userRole = decodedToken.role;
      
            // Navigate to different views based on user role
            if (userRole === "USER") {
              next("/auth/login");
            } else if (userRole === "ADMIN") {
              next("/auth/adminView");
            } else {
              // If the role is not recognized, redirect to login
              next("/auth/login");
            }
          }
        },
      },
      
      {
        path: "/auth/adminView",
        name: "adminView",
        component: adminView,
        beforeEnter: (to, from, next) => {
            const token = localStorage.getItem("jwtToken");
        
            if (!token) {
              // If no token found, redirect to the login page
              next("/auth/login");
            } else {
              // If token is found, check if it is valid and has the ADMIN role
              try {
                const decodedToken = jwt_decode(token);
        
                if (decodedToken.role && decodedToken.role.includes("ADMIN")) {
                  next(); // Proceed to the AllReviews component if the user is an admin
                } else {
                  this.$router.push("/metadata/search"); // Redirect to the homepage or any other page if the user is not an admin
                  alert("User is not admin");
    
                }
              } catch (error) {
                console.log("Invalid token", error);
                localStorage.removeItem("jwtToken"); // Remove the invalid token
                next("/auth/login"); // Redirect to the login page
              }
            }
          },
    },  
    {
        path: "/auth/register",
        name: "SignUp",
        component: SignUp,
    },
    {
        path: "/auth/login",
        name: "Login",
        component: Login,
    },
    {
    path: "/review/add",
    name: "addReview",
    component: addReview,
},
{
    path: "/review/all",
    name: "allReview",
    component: allReview,
    beforeEnter: (to, from, next) => {
        const token = localStorage.getItem("jwtToken");
    
        if (!token) {
          // If no token found, redirect to the login page
          next("/auth/login");
        } else {
          // If token is found, check if it is valid and has the ADMIN role
          try {
            const decodedToken = jwt_decode(token);
    
            if (decodedToken.role && decodedToken.role.includes("ADMIN")) {
              next(); // Proceed to the AllReviews component if the user is an admin
            } else {
              next("/"); // Redirect to the homepage or any other page if the user is not an admin
              alert("User is not admin");

            }
          } catch (error) {
            console.log("Invalid token", error);
            localStorage.removeItem("jwtToken"); // Remove the invalid token
            next("/auth/login"); // Redirect to the login page
          }
        }
      },
},
{
    path: "/watchlist/:user_id",
    name: "watchlist",
    component: watchlist,
    beforeEnter: (to, from, next) => {
        const token = localStorage.getItem("jwtToken");
        console.log(token)
        if (!token) {
          // If no token found, redirect to the login page
          next("/auth/login");
        } else {
          // If token is found, check if it is valid and has the ADMIN role
          try {
            const decodedToken = jwt_decode(token);
    
            if (decodedToken.role && decodedToken.role.includes("USER")) {
              next(); 
            } else {
              next("/"); // Redirect to the homepage or any other page if the user is not an admin
              alert("User is not admin");

            }
          } catch (error) {
            console.log("Invalid token", error);
            localStorage.removeItem("jwtToken"); // Remove the invalid token
            next("/auth/login"); // Redirect to the login page
          }
        }
      },
},
{
    path: "/metadata/search",
    name: "metadata",
    component: metadata,
},
{
    path: "/metadata/searchByParams/genre=:genreValue&info=custom_info",
    name: "searchByParams",
    component: searchByParams,
},
{
    path: "/metadata/searchByIDs/:id",
    name: "searchByID",
    component: searchByID,
},


]


const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
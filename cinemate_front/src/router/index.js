import { createRouter, createWebHistory } from 'vue-router'

import addReview from '../views/addReview.vue';
import allReview from '../views/allReview.vue';
import metadata from '../views/metadata';
import searchByParams from '../views/searchByParams';
import searchByID from '../views/searchByID';
// import HomeView from "../views/HomeView.vue";
// import auth from "../auth";
import SignUp from "../views/SignUp.vue"
import Login from "../views/Login"
const routes = [
    // {

    //     path: "/",
    //     name: "home",
    //     component: HomeView,
    //     beforeEnter: async(to, from, next) => {
    //         let authResult = await auth.authenticated();
    //         if (!authResult) {
    //             next('/login')
    //         } else {
    //             next();
    //         }
    //     }
    // },
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
import { createRouter, createWebHistory } from 'vue-router'

import addReview from '../views/addReview.vue';
import allReview from '../views/allReview';
import metadata from '../views/metadata';
import searchByParams from '../views/searchByParams';

const routes = [{
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


]


const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
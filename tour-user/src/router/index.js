import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Auth from "@/views/Auth.vue";
import Reviews from "@/views/Reviews.vue";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/index',
    name: 'Home',
    component: Home
  },
  {
    path: '/tours',
    name: 'Tours',
    component: () => import('../views/Tours.vue')
  },
  {
    path: '/booking',
    name: 'Booking',
    component: () => import('../views/Booking.vue')
  },
  {
    path: '/specialties',
    name: 'Specialties',
    component: () => import('../views/Specialties.vue')
  },
  {
    path: '/contact',
    name: 'Contact',
    component: () => import('../views/Contact.vue')
  },
  { path: '/profile', component: () => import('@/views/Profile.vue') },
  { path: '/culture', component: () => import('@/views/Culture.vue') },
  { path: '/auth', component: Auth },
  { path: '/reviews', component: Reviews },
  {
    path: '/attraction/huanghekou',
    name: 'AttractionHuanghekou',
    component: () => import('../views/AttractionHuanghekou.vue')
  },
  {
    path: '/attraction/delta',
    name: 'AttractionDelta',
    component: () => import('../views/AttractionDelta.vue')
  },
  {
    path: '/attraction/tianehu',
    name: 'AttractionTianEHu',
    component: () => import('../views/AttractionTianEHu.vue')
  },
  {
    path: '/route/detail',
    name: 'AttractionRouteDetail',
    component: () => import('../views/AttractionRouteDetail.vue')
  },
  {
    path: '/search',
    name: 'SearchResult',
    component: () => import('../views/SearchResult.vue')
  },
  {
    path: '/contact',
    name: 'Contact',
    component: () => import('../views/Contact.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
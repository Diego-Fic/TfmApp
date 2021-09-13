import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../components/login/login.vue'
import Home from '../components/user/user.vue'
import Dashboards from '../components/dashboards/dashboards.vue'
import Table_files from '../components/table_files/table_files.vue'
import Calendar from '../components/calendar/calendar.vue'
import GestorDispositivosPod from '../components/tabla_dispositivo_pod/gestor.vue';
import GestorAlumnosGrupos from '../components/tabla_alumnos_grupos/gestorAlumnos.vue';
import GestorGrupoPod from '../components/tabla_grupos_pods/gestorGrupoPod.vue';
import Terminal from '../components/terminal/gestorTerminal.vue';
import Term from '../components/terminal/vueTerminal.vue';
//import { Store } from 'vuex'
import store from '../store/store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'login',
    component: Login,
    beforeEnter: (to, from, next) => {
      if (store.state.authenticate == false){
        next();
      } else {
        next("/home");
      }
    }
  },
  {
    path: '/home',
    name: 'home',
    component: Home,
    meta: { bodyClass: 'bg-black-alt font-sans leading-normal tracking-normal' },
    beforeEnter: (to, from, next) => {
      if (store.state.authenticate == false){
        next("/");
      } else {
        next();
      }
    }
  },
  {
    path: '/dashboards',
    name: 'dashboards',
    component: Dashboards,
    meta: { bodyClass: 'bg-black-alt font-sans leading-normal tracking-normal' },
    beforeEnter: (to, from, next) => {
      if (store.state.authenticate != false){
        if(store.state.rol == "Profesor"){
          next();
        } else {
          next("/");
        }
      } else {
        next("/");
      }
    }
  },
  {
    path: '/calendario',
    name: 'calendario',
    component: Calendar,
    meta: { bodyClass: 'bg-black-alt font-sans leading-normal tracking-normal' },
    beforeEnter: (to, from, next) => {
      if (store.state.authenticate == false){
        next("/");
      } else {
        next();
      }
    }
  },
  {
    path: '/gestorDispositivosPod',
    name: 'gestorDispositivosPod',
    component: GestorDispositivosPod,
    meta: { bodyClass: 'bg-black-alt font-sans leading-normal tracking-normal' },
    beforeEnter: (to, from, next) => {
      if (store.state.authenticate != false){
        if(store.state.rol == "Profesor"){
          next();
        } else {
          next("/");
        }
      } else {
        next("/");
      } 
    }
  },
  {
    path: '/gestorAlumnoGrupo',
    name: 'gestorAlumnosGrupos',
    component: GestorAlumnosGrupos,
    meta: { bodyClass: 'bg-black-alt font-sans leading-normal tracking-normal' },
    beforeEnter: (to, from, next) => {
      if (store.state.authenticate != false){
        if(store.state.rol == "Profesor"){
          next();
        } else {
          next("/");
        }
      } else {
        next("/");
      } 
    }
  },
  {
    path: '/gestorGrupoPod',
    name: 'gestorGrupoPod',
    component: GestorGrupoPod,
    meta: { bodyClass: 'bg-black-alt font-sans leading-normal tracking-normal' },
    beforeEnter: (to, from, next) => {
      if (store.state.authenticate != false){
        if(store.state.rol == "Profesor"){
          next();
        } else {
          next("/");
        }
      } else {
        next("/");
      } 
    }
  },
  {
    path: '/documentos',
    name: 'documentos',
    component: Table_files,
    meta: { bodyClass: 'bg-black-alt font-sans leading-normal tracking-normal' },
    beforeEnter: (to, from, next) => {
      if (store.state.authenticate == false){
        next("/");
      } else {
        next();
      }
    }
  },
  {
    path: '/terminal',
    name: 'terminal',
    component: Terminal,
    meta: { bodyClass: 'bg-black-alt font-sans leading-normal tracking-normal' },
    beforeEnter: (to, from, next) => {
      if (store.state.authenticate == false){
        next("/");
      } else {
        next();
      }
    }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router

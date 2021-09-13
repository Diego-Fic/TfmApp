import Vue from 'vue'
import App from './App.vue'
import router from './router'
import "./assets/css/tailwind.css"
import axios from 'axios'
import VueAxios from 'vue-axios'
import store from './store/store'
import VueSocketIO from 'vue-socket.io'

import VueMaterial from 'vue-material'
import 'vue-material/dist/vue-material.css'
import 'vue-material/dist/theme/black-green-light.css'

Vue.use(VueMaterial)

//Vue.use(new VueSocketIO({connection: 'http://localhost:3000'}))
// Import Vuetify
import vuetify from './plugins/vuetify'

Vue.config.productionTip = false
Vue.use(VueAxios, axios)

new Vue({
  router,
  vuetify,
  render: function (h) { return h(App) },
  store: store
}).$mount('#app')

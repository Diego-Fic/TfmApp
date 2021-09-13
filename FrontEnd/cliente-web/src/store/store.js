import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex)

export default new Vuex.Store(
    {
      state:{
          authenticate: false,
          rol: "",
          username: "",
          grupo:"",
          pod:""
      },
      mutations: {
          setPod(state, pod){
            state.pod = pod;
          },
          setGrupo(state, grupo){
            state.grupo = grupo;
          },
          setAuthentication(state, status){
            state.authenticate = status;
          },
          setRolAuthenticate(state, actualRol){
            state.rol = actualRol;
          },
          setUsernameAuthenticate(state, username){
            state.username = username;
          }
      },
      plugins: [createPersistedState()]
    }
)
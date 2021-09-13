<template>

</template>

<script>
import {
  INACTIVE_USER_TIME_THRESHOLD,
  USER_ACTIVITY_THROTTLER_TIME
} from "./constants.js";

import axios from 'axios'
import store from '../../store/store'

export default {
  name: "activitTimeout",

  data() {
    return {
      isInactive: false,
      userActivityThrottlerTimeout: null,
      userActivityTimeout: null
    };
  },

  methods: {
    activateActivityTracker() {
      window.addEventListener("mousemove", this.userActivityThrottler);
      window.addEventListener("scroll", this.userActivityThrottler);
      window.addEventListener("keydown", this.userActivityThrottler);
      window.addEventListener("resize", this.userActivityThrottler);
    },

    deactivateActivityTracker() {
      window.removeEventListener("mousemove", this.userActivityThrottler);
      window.removeEventListener("scroll", this.userActivityThrottler);
      window.removeEventListener("keydown", this.userActivityThrottler);
      window.removeEventListener("resize", this.userActivityThrottler);
    },

    resetUserActivityTimeout() {
      clearTimeout(this.userActivityTimeout);
  
        this.userActivityTimeout = setTimeout(() => {
          //this.userActivityThrottler();
          this.inactiveUserAction();
        }, INACTIVE_USER_TIME_THRESHOLD);
    },

    userActivityThrottler() {
      if (this.isInactive) {
        this.isInactive = false;

        let json = {
          "usuario_log" : store.state.username,
          "tipo" : "FIN_INACTIVO"
        }; 

        axios.post('http://127.0.0.1:8080/insertLog',json, {headers: {
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
        "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
        withCredentials: true}
        });
      }

      if (!this.userActivityThrottlerTimeout) {
        this.userActivityThrottlerTimeout = setTimeout(() => {
          this.resetUserActivityTimeout();
          clearTimeout(this.userActivityThrottlerTimeout);
          this.userActivityThrottlerTimeout = null;
        }, USER_ACTIVITY_THROTTLER_TIME);
      }
    },
    inactiveUserAction() {
      //console.log(this.isInactive);
      if (!this.isInactive) {
        this.isInactive = true;
        let json = {
          "usuario_log" : store.state.username,
          "tipo" : "INACTIVO"
        };
        axios.post('http://127.0.0.1:8080/insertLog',json, {headers: {
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
        "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
        withCredentials: true}
        });
      }
    }
  },

  beforeMount() {
    this.activateActivityTracker();
  },

  beforeDestroy() {
    this.deactivateActivityTracker();
    clearTimeout(this.userActivityTimeout);
    clearTimeout(this.userActivityThrottlerTimeout);
  }
};
</script>

<style>

</style>
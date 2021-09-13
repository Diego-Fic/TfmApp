<template>
    <div class="font-sans min-h-screen antialiased bg-gray-900 pt-24 pb-5">
        <div class="flex flex-col justify-center sm:w-96 sm:m-auto mx-5 mb-5 space-y-8">
        <h1 class="font-bold text-center text-4xl text-yellow-500">Proyecto<span class="text-blue-500">TFM</span></h1>
        <form v-on:submit.prevent="login">
            <div class="flex flex-col bg-white p-10 rounded-lg shadow space-y-6">
            <h1 class="font-bold text-xl text-center">Iniciar sesión</h1>

            <div class="flex flex-col space-y-1">
                <input type="text" v-model="username" name="username" id="username" class="border-2 rounded px-3 py-2 w-full focus:outline-none focus:border-blue-400 focus:shadow" placeholder="Correo electrónico" />
            </div>

            <div class="flex flex-col space-y-1" id="passwd">
                <input :type="passwordFieldType" v-model="password" maxlength="30" name="password" id="password" class="border-2 rounded px-3 py-2 w-full focus:outline-none focus:border-blue-400 focus:shadow" placeholder="Contraseña" />
                <div class="input-group-prepend">
                    <button type="button" v-on:click="switchVisibility"><img src="../../assets/img/eye-off.png" /></button>
                </div>
            </div>
            
            <div class="flex flex-col-reverse sm:flex-row sm:justify-between items-center">
                <a href="https://matricula.udc.es/preinsmaster_c/identifObtenerIdentificacion.asp" class="inline-block text-blue-500 hover:text-blue-800 hover:underline">¿Necesitas ayuda?</a>
                <button type="submit" class="bg-blue-500 text-white font-bold px-5 py-2 rounded focus:outline-none shadow hover:bg-blue-700 transition-colors">Iniciar sesión</button>
            </div>
            </div>
        </form>

        <div id="modalAlert" class="relative py-3 pl-4 pr-10 leading-normal text-red-700 bg-red-100 rounded-lg" role="alert" v-if="error">
            <p>{{error_msg}}</p>
            <span class="absolute inset-y-0 right-0 flex items-center mr-4">
                <svg class="w-4 h-4 fill-current" v-on:click="closeAlert" role="button" viewBox="0 0 20 20"><path d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" fill-rule="evenodd"></path></svg>
            </span>
        </div>

        </div>
    </div>
</template>

<script>
import axios from 'axios'

export default {
    name : "Login",
    data () {
        return {
            username: "",
            password: "",
            rol: "",
            error: false,
            error_msg: "",
            passwordFieldType: "password"
        };
    },
    methods: {
        switchVisibility() {
            this.passwordFieldType = this.passwordFieldType === "password" ? "text" : "password";
        }, login(){
            let json = {
                "username" : this.username,
                "password" : this.password
            };
            if (this.username == "" || this.password == "") {
                    this.error = true;
                    this.error_msg = "Los campos correo electrónico y contraseña no pueden estar vacios";
            } else {
                axios.post('http://127.0.0.1:8080/checkLogin',json, {headers: {
                    "Access-Control-Allow-Origin": "*",
                    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                    withCredentials: true}
                    }).then(data => {
                    if (data.data.error == "true"){
                        this.error = true;
                        this.error_msg = data.data.error_msg;
                    } else {
                        localStorage.setItem('username',data.data.username);
                        localStorage.setItem('rol',data.data.rol);
                        this.$store.commit("setAuthentication",true);
                        this.$store.commit("setRolAuthenticate",data.data.rol);
                        this.$store.commit("setUsernameAuthenticate",data.data.username);
                        this.$router.push("/home");
                        let json = {
                            "usuario_log" : this.username,
                            "tipo" : "INICIO_SESION"
                        };
                        axios.post('http://127.0.0.1:8080/insertLog',json, {headers: {
                        "Access-Control-Allow-Origin": "*",
                        "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                        "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                        withCredentials: true}
                        });
                    }
                }).catch(error => {
                        this.error = true;
                        this.error_msg = "Conexión con el servidor interrumpida";
                });
            }
        }, closeAlert(){
            this.error = false;
        }
    }
};

</script>

<style>

#passwd {
    position: relative;
}

#passwd input[type=passwordFieldType] {
    border: none;
    width: 100%;
    padding-right: 123px;
}
#passwd .input-group-prepend {
    position: absolute;
    right: 4px;
    top: 4px;
    bottom: 4px;z-index:9;
}

#modalAlert {
    text-align: center;
}
</style>
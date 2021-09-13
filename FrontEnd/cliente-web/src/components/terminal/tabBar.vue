<template>
    <div id="tabBar">
        <div>
            <v-layout row justify-center>
                <v-dialog v-model="dialogConexion" max-width="410">
                    <v-card>
                        <v-card-title class="headline">Dispositivos asignados a tu grupo</v-card-title>
                        <v-card-text>Seleccione el dispositivo al que conectarte.</v-card-text>
                            <v-col>   
                                <v-select
                                    v-model="editedItem.nombre_dispositivo"
                                    :items="dispositivos"
                                    label="Dispositivo"
                                    no-data-text="No tienes dispositivos"
                                ></v-select>
                            </v-col>
                        <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="red" @click="cancelar()">Cancelar</v-btn>
                        <v-btn color="blue" @click="añadirConexion()">Conectar</v-btn>
                        <v-spacer></v-spacer>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-layout>
        </div>

        <v-app id="inspire" class="h-4/6">
                <v-sheet elevation="6">
                <v-tabs
                    background-color="cyan"
                    dark
                    next-icon="mdi-arrow-right-bold-box-outline"
                    prev-icon="mdi-arrow-left-bold-box-outline"
                    show-arrows
                    v-model="tab"
                >
                    <v-tabs-slider color="yellow"></v-tabs-slider>
                    <v-tab
                    v-for="item in items"
                    :key="item.item"
                    @click = "swap({ item })"
                    >
                    <v-btn
                        color="red"
                        icon
                        @click.prevent.stop="closeConection({ item })"
                        class="custom-btn" 
                    >
                        <v-icon rigth>mdi-close</v-icon>
                    </v-btn>
                    {{ item }}
                    </v-tab>
                        <div class="pa-1" @click.prevent.stop="addConection()" style="width: 100%;height: 100%;">
                            <v-btn 
                                icon
                                @click.prevent.stop="addConection()"
                                class="busqueda"
                            >
                                <v-icon>mdi-magnify-plus</v-icon>
                            </v-btn>
                        </div>
                </v-tabs>
                </v-sheet>
            <v-tabs-items v-model="tab">
            <v-tab-item
                v-for="item in items"
                :key="item.item"
                >
                <div class="page-container">
                    <md-app>
                        <md-app-content>
                            <md-field>
                            <label>Terminal</label>
                            <md-textarea v-model="textarea" disabled style="height:250px;"></md-textarea>
                            </md-field>
                            <md-field>
                                <label>Escribir</label>
                                <md-input v-model="message"></md-input>
                                <md-button class="md-primary md-raised" v-on:click="sendMessageEnter({item})">Enter</md-button>
                                <md-button class="md-primary md-raised" v-on:click="sendMessage({item})">Enviar</md-button>
                                <md-button class="md-primary md-raised" v-on:click="clear({item})">Limpiar</md-button>
                            </md-field>
                        </md-app-content>
                    </md-app>
                </div>
            </v-tab-item>
            </v-tabs-items>
        </v-app>
    </div>   
</template>

<script>
import axios from "axios";
import store from '../../store/store';
import custom from './vueTerminal.vue';

export default {
  components: { custom },
        name : "tabBar",
        data () {
            return {
                connection: null,
                tab: null,
                dispositivos: [],
                dialogConexion: false,
                conexiones: [],
                editedItem: {
                    nombre_dispositivo: ""
                },
                items: [], 
                message: "",
                textarea: "",
                savedtextarea: [],
                lastMessage: "",
                repetidoSend: false,
                lastDispositivo: null,
                cambioDispositivo: false,
                primeraConexion: false
            }
        },
        methods:{
            cancelar(){
                this.dialogConexion=false;
                this.editedItem.nombre_dispositivo="";
            },
            swap(item){
                this.textarea = "";
                var datosGuardados = JSON.parse(JSON.stringify(this.savedtextarea));
                var datosTerminal = datosGuardados.filter(d => d.dispositivo === item.item);
                this.textarea = datosTerminal[0].textsavedarea;
            },
            clear(item){
                this.textarea = ""
                for(var i = 0; i < this.savedtextarea.length; i++){
                    if(this.savedtextarea[i].dispositivo == item.item){
                        this.savedtextarea.splice(i, 1);
                    }
                };
                let jsonTextTerminal = {
                    "textsavedarea": "",
                    "dispositivo": item.item
                }
                this.savedtextarea.push(jsonTextTerminal);
            },
            sendMessageEnter (item) {
                //PARSEAR EL MENSAJE RECIBIDO
                var data = JSON.parse(JSON.stringify(this.conexiones));
                var provinceAbc = data.filter(d => d.dispositivo === item.item);

                //ACCEDER AL REGISTRO DE MENSAJES GUARDADO Y ELIMINAR EL VALOR DEL DISPOSITIVO
                var datosGuardados = JSON.parse(JSON.stringify(this.savedtextarea));
                var datosTerminal = datosGuardados.filter(d => d.dispositivo === item.item);
                var textModificado = datosTerminal[0].textsavedarea + "User> " + this.message + '\n';

                for(var i = 0; i < this.savedtextarea.length; i++){
                    if(this.savedtextarea[i].dispositivo == item.item){
                        this.savedtextarea.splice(i, 1);
                    }
                };
                //AÑADIR EL NUEVO REGISTRO A LOS MENSAJES GUARDADOS
                let jsonTextTerminal = {
                    "textsavedarea": textModificado,
                    "dispositivo": item.item
                }
                this.savedtextarea.push(jsonTextTerminal)
                //ENVIAR MENSAJE A LOS DISPOSITIVOS
                let json ={
                    "mensaje": this.message + "\n",
                    "dispositivo": provinceAbc[0].dispositivo,
                    "puerto": provinceAbc[0].puerto,
                    "flag": "SEND"
                }

                if (this.lastDispositivo == null){
                    this.lastDispositivo=provinceAbc[0].dispositivo;
                } else if (this.lastDispositivo != provinceAbc[0].dispositivo){
                        this.cambioDispositivo = true;
                        this.lastDispositivo=provinceAbc[0].dispositivo;
                        console.log("Cambio");
                }

                this.textarea += "User> " + this.message + '\n'
                this.connection.send(JSON.stringify(json));
                
                this.message = '' // empty the message bar
                this.repetidoSend = true;
            },
            sendMessage (item) {
                //PARSEAR EL MENSAJE RECIBIDO
                var data = JSON.parse(JSON.stringify(this.conexiones));
                var provinceAbc = data.filter(d => d.dispositivo === item.item);
                //ACCEDER AL REGISTRO DE MENSAJES GUARDADO Y ELIMINAR EL VALOR DEL DISPOSITIVO
                var datosGuardados = JSON.parse(JSON.stringify(this.savedtextarea));
                var datosTerminal = datosGuardados.filter(d => d.dispositivo === item.item);
                var textModificado = datosTerminal[0].textsavedarea + "User> " + this.message + '\n';

                for(var i = 0; i < this.savedtextarea.length; i++){
                    if(this.savedtextarea[i].dispositivo == item.item){
                        this.savedtextarea.splice(i, 1);
                    }
                };
                //AÑADIR EL NUEVO REGISTRO A LOS MENSAJES GUARDADOS
                let jsonTextTerminal = {
                    "textsavedarea": textModificado,
                    "dispositivo": item.item
                }
                this.savedtextarea.push(jsonTextTerminal)
                //ENVIAR MENSAJE A LOS DISPOSITIVOS
                let json ={
                    "mensaje": this.message,
                    "dispositivo": provinceAbc[0].dispositivo,
                    "puerto": provinceAbc[0].puerto,
                    "flag": "SEND"
                }

                this.textarea += "User> " + this.message + '\n'
                this.connection.send(JSON.stringify(json));

                this.message = ''; // empty the message bar
                this.repetidoSend = true;
                
            },
            addConection(){
                this.dialogConexion=true;
            }, 
            closeConection(item){
                //ELIMINAR EL ITEM DE LA TAB 
                let numItem = this.items.indexOf(item.item);
                this.items.splice(numItem,1);
                if (this.items.length == 0){
                    this.primeraConexion=false;
                }
                //AÑADIR EL DISPOSITIVO AL SELECTOR
                this.dispositivos.push(item.item);
                //ELIMINAR LOS REGISTROS DE MENSAJES DEL DISPOSITIVO
                for(var i = 0; i < this.savedtextarea.length; i++){
                    if(this.savedtextarea[i].dispositivo == item.item){
                        this.savedtextarea.splice(i, 1);
                    }
                };
                //ENVIAR EL MENSAJE DE CIERRE DE CONEXION
                var data = JSON.parse(JSON.stringify(this.conexiones));
                var provinceAbc = data.filter(d => d.dispositivo === item.item);
                let json ={
                    "mensaje": this.message,
                    "dispositivo": provinceAbc[0].dispositivo,
                    "puerto": provinceAbc[0].puerto,
                    "flag": "CLOSE"
                }

                this.connection.send(JSON.stringify(json));
                //BORRAR DATOS DEL TERMINAL
                this.textarea = "";
            }, 
            añadirConexion(){ 
                //AÑADIR A LAS TABS LA ENTRADA
                if (this.items.length == 0){
                    this.primeraConexion=true;
                }
                this.items.push(this.editedItem.nombre_dispositivo);
                //BORRAR EL DISPOSITIVO DEL SELECTOR
                let numDispositivo = this.dispositivos.indexOf(this.editedItem.nombre_dispositivo);
                this.dispositivos.splice(numDispositivo,1);
                //AÑADIR ENTRADA EN EL REGISTRO DE MENSAJES
                let jsonTextTerminal = {
                    "textsavedarea": "",
                    "dispositivo": this.editedItem.nombre_dispositivo
                }
                this.savedtextarea.push(jsonTextTerminal);

                //ENVIAR MENSAJE DE CONEXION
                var data = JSON.parse(JSON.stringify(this.conexiones));
                var provinceAbc = data.filter(d => d.dispositivo === this.editedItem.nombre_dispositivo);

                let json ={
                    "mensaje": "",
                    "dispositivo": provinceAbc[0].dispositivo,
                    "puerto": provinceAbc[0].puerto,
                    "flag": "CONNECT"
                }
                this.connection.send(JSON.stringify(json));
                //REINICIAR SELECTOR
                this.editedItem.nombre_dispositivo="";

                //CERRAR DIALOGO
                this.dialogConexion=false;
            }, inicializar_dispositivos(){
                if (this.$store.state.rol=='Profesor'){
                    axios.get('http://127.0.0.1:8080/getConexiones',{headers: {
                        "Access-Control-Allow-Origin": "*",
                        "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                        "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                        withCredentials: true}
                        }).then(data => {
                            data.data.forEach((value, index) => {
                                this.dispositivos.push(value.dispositivo);
                                let valores = {
                                    "dispositivo": value.dispositivo,
                                    "puerto": value.puerto
                                }
                                this.conexiones.push(valores);
                            });
                        }).catch(error => {
                        console.log("Error con la bd"); 
                    });
                } else {
                        let json = {
                            "grupo": this.$store.state.grupo
                        }
                    axios.post('http://127.0.0.1:8080/getConexionesByGrupo',json,{headers: {
                        "Access-Control-Allow-Origin": "*",
                        "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                        "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                        withCredentials: true}
                        }).then(data => {
                            data.data.forEach((value, index) => {
                                this.dispositivos.push(value.dispositivo);
                                this.conexiones.push(value.puerto);
                            });
                        }).catch(error => {
                        console.log("Error con la bd"); 
                    });
                }
            }
        }, mounted: function (){
            this.inicializar_dispositivos();

            //console.log("Starting connection to WebSocket Server")
            this.connection = new WebSocket("ws://localhost:3001", "echo-protocol");

            this.connection.onmessage = (event) => {

                //PARSEAR DATOS RECIBIDOS
                console.log(JSON.parse(event.data));      

                var objectValue = JSON.parse(event.data);

                //ACCEDER A LOS REGISTROS DE MENSAJES Y ELIMINAR EL ACTUAL
                if (objectValue.flag == "CONNECT"){
                        var datosGuardados = JSON.parse(JSON.stringify(this.savedtextarea));
                        var datosTerminal = datosGuardados.filter(d => d.dispositivo === objectValue.dispositivo);
                        var textModificado = datosTerminal[0].textsavedarea + objectValue.mensaje + '\n';

                        for(var i = 0; i < this.savedtextarea.length; i++){
                            if(this.savedtextarea[i].dispositivo == objectValue.dispositivo){
                                this.savedtextarea.splice(i, 1);
                            }
                        };
                        //AÑADIR EL NUEVO REGISTRO DE MENSAJES
                        let jsonTextTerminal = {
                            "textsavedarea": textModificado,
                            "dispositivo": objectValue.dispositivo
                        }
                        this.savedtextarea.push(jsonTextTerminal)
                        //AÑADIR EL MENSAJE A LA TERMINAL
                        if (this.primeraConexion){
                            this.textarea += objectValue.mensaje + '\n'; // append each new message to the textarea and add a line break
                            this.primeraConexion=false;
                        }
                        // 
                        //this.lastMessage = objectValue.mensaje;
                        //this.repetidoSend = false;
                } else {
                    if (!this.cambioDispositivo){
                        if (this.lastMessage != objectValue.mensaje){
                        //CASO: MENSAJES NO REPETIDOS
                            var datosGuardados = JSON.parse(JSON.stringify(this.savedtextarea));
                            var datosTerminal = datosGuardados.filter(d => d.dispositivo === objectValue.dispositivo);
                            var textModificado = datosTerminal[0].textsavedarea + objectValue.mensaje + '\n';

                            for(var i = 0; i < this.savedtextarea.length; i++){
                                if(this.savedtextarea[i].dispositivo == objectValue.dispositivo){
                                    this.savedtextarea.splice(i, 1);
                                }
                            };
                            //AÑADIR EL NUEVO REGISTRO DE MENSAJES
                            let jsonTextTerminal = {
                                "textsavedarea": textModificado,
                                "dispositivo": objectValue.dispositivo
                            }
                            this.savedtextarea.push(jsonTextTerminal)
                            //AÑADIR EL MENSAJE A LA TERMINAL
                            this.textarea += objectValue.mensaje + '\n' // append each new message to the textarea and add a line break
                            this.lastMessage = objectValue.mensaje;
                            this.repetidoSend = false;
                        } else if ((this.lastMessage == objectValue.mensaje) && this.repetidoSend){
                        //CASO: TE LLEGA UN REPETIDO PERO ES DESPUES DE MENSAJE TUYO EJEMPLO (MANDAS UN ENTER TE LLEGA EL PROMPT Y LO REPITES)
                            var datosGuardados = JSON.parse(JSON.stringify(this.savedtextarea));
                            var datosTerminal = datosGuardados.filter(d => d.dispositivo === objectValue.dispositivo);
                            var textModificado = datosTerminal[0].textsavedarea + objectValue.mensaje + '\n';

                            for(var i = 0; i < this.savedtextarea.length; i++){
                                if(this.savedtextarea[i].dispositivo == objectValue.dispositivo){
                                    this.savedtextarea.splice(i, 1);
                                }
                            };
                            //AÑADIR EL NUEVO REGISTRO DE MENSAJES
                            let jsonTextTerminal = {
                                "textsavedarea": textModificado,
                                "dispositivo": objectValue.dispositivo
                            }
                            this.savedtextarea.push(jsonTextTerminal)
                            //AÑADIR EL MENSAJE A LA TERMINAL
                            this.textarea += objectValue.mensaje + '\n' // append each new message to the textarea and add a line break
                            this.lastMessage = objectValue.mensaje;
                            this.repetidoSend = false;
                        }
                    } else if (this.lastMessage != objectValue.mensaje){
                        this.cambioDispositivo=false;
                        //CASO: PRIMER MENSAJE NO REPETIDO DESPUES DE ENVIAR UN MENSAJE A UN NUEVO DISPOSITIVO
                        var datosGuardados = JSON.parse(JSON.stringify(this.savedtextarea));
                        var datosTerminal = datosGuardados.filter(d => d.dispositivo === objectValue.dispositivo);
                        var textModificado = datosTerminal[0].textsavedarea + objectValue.mensaje + '\n';

                        for(var i = 0; i < this.savedtextarea.length; i++){
                            if(this.savedtextarea[i].dispositivo == objectValue.dispositivo){
                                this.savedtextarea.splice(i, 1);
                            }
                        };
                        //AÑADIR EL NUEVO REGISTRO DE MENSAJES
                        let jsonTextTerminal = {
                            "textsavedarea": textModificado,
                            "dispositivo": objectValue.dispositivo
                        }
                        this.savedtextarea.push(jsonTextTerminal)
                        //AÑADIR EL MENSAJE A LA TERMINAL
                        this.textarea += objectValue.mensaje + '\n' // append each new message to the textarea and add a line break
                        this.lastMessage = objectValue.mensaje;
                        this.repetidoSend = false;
                    }
                }

            }

            this.connection.onopen = function(event) {
                //console.log(event)
                //console.log("Successfully connected to the echo websocket server...")
            }
  }
}
</script>

<style>
#tabBar{
    height: 850px;
    min-width: 1100px;
    padding-left: 115px;
    padding-right: 115px;
    padding-top: 120px;
    padding-bottom: 70px;
}
.custom-btn:hover {
    background-color: white;
}
.busqueda:hover{
      background-color: black;  
}

.v-application--wrap {
    min-height: 0vh !important;
}

.page-container{
    max-height: 392px;
}
.md-field.md-theme-default.md-disabled.md-has-textarea{
    min-height: 250px;
}
</style>
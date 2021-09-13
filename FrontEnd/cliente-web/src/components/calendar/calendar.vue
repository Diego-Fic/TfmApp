<template>
    <section>
        <navbar/>
        <div id="calendarPage">  
            <v-app id="inspire">
                <v-row class="fill-height">
                    <v-col>
                        <v-sheet height="64">
                            <v-toolbar flat>
                                <v-btn
                                    outlined
                                    class="mr-4"
                                    color="grey darken-2"
                                    @click="dialog = true"
                                    :disabled ="this.$store.state.rol=='Profesor' && this.editedItem.nombre_grupo==''"
                                    >
                                    Reservar
                                </v-btn>
                                <v-select
                                    v-model="editedItem.nombre_grupo"
                                    :items="grupos"
                                    dense
                                    outlined
                                    hide-details
                                    class="ma-2"
                                    label="Visualizar Grupo"
                                    v-if= "this.$store.state.rol=='Profesor'"
                                    @change="visualizarGrupo()"
                                ></v-select>
                                <v-spacer></v-spacer>
                                <v-menu
                                bottom
                                right
                                >
                                    <template v-slot:activator="{ on, attrs }">
                                        <v-btn
                                            outlined
                                            color="grey darken-2"
                                            v-bind="attrs"
                                            v-on="on"
                                            >
                                            <span>{{ typeToLabel[type] }}</span>
                                            <v-icon right>
                                                mdi-menu-down
                                            </v-icon>
                                        </v-btn>
                                    </template>
                                    <v-list>
                                        <v-list-item @click="type = 'day'">
                                        <v-list-item-title>Day</v-list-item-title>
                                        </v-list-item>
                                        <v-list-item @click="type = 'week'">
                                        <v-list-item-title>Week</v-list-item-title>
                                        </v-list-item>
                                        <v-list-item @click="type = 'month'">
                                        <v-list-item-title>Month</v-list-item-title>
                                        </v-list-item>
                                        <v-list-item @click="type = '4day'">
                                        <v-list-item-title>4 days</v-list-item-title>
                                        </v-list-item>
                                    </v-list>
                                </v-menu>
                                <v-btn
                                    outlined
                                    class="ml-4"
                                    color="grey darken-2"
                                    @click="setToday"
                                    >
                                    Today
                                </v-btn>
                                <v-btn
                                    fab
                                    text
                                    small
                                    class="ml-1"
                                    color="grey darken-2"
                                    @click="prev"
                                    >
                                    <v-icon small>
                                        mdi-chevron-left
                                    </v-icon>
                                </v-btn>
                                <v-btn
                                    fab
                                    text
                                    small
                                    color="grey darken-2"
                                    @click="next"
                                    >
                                    <v-icon small>
                                        mdi-chevron-right
                                    </v-icon>
                                </v-btn>
                            </v-toolbar>
                        </v-sheet>
                    <!--Añadir dialogo reservas -->
                    <v-dialog v-model="dialog" max-width="500px" >
                        <v-card>
                            <v-card-title>
                                <span class="text-h5">Crear Reserva</span>
                            </v-card-title>
                            <v-card-text>
                                <v-container>
                                    <v-form>
                                        <v-text-field v-model="editedReserva.nombre_reserva" type="text" label="Nombre de la reserva"></v-text-field>
                                        <v-text-field v-model="editedReserva.fecha_ini" type="date" label="Fecha inicio de la reserva"></v-text-field>
                                        <v-text-field v-model="editedReserva.fecha_ini_tiempo" label="TIME" type="time" suffix="EST"></v-text-field>
                                        <v-text-field v-model="editedReserva.fecha_fin" type="date" label="Fecha fin de la reserva"></v-text-field>
                                        <v-text-field v-model="editedReserva.fecha_fin_tiempo" label="TIME" type="time" suffix="EST"></v-text-field>
                                    </v-form>
                                </v-container>
                            </v-card-text>
                            <v-card-actions>
                                <v-spacer></v-spacer>
                                    <v-btn
                                    color="blue darken-1"
                                    text
                                    @click.stop="closeReserva"
                                    >
                                    Cancelar
                                    </v-btn>
                                    <v-btn
                                    color="blue darken-1"
                                    text
                                    @click="addEvent"
                                    :disabled="editedReserva.nombre_reserva == '' || editedReserva.fecha_ini == '' || editedReserva.fecha_fin == ''"
                                    >
                                    Crear Reserva
                                    </v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-dialog>
                    <v-sheet height="500">
                    <v-calendar
                        ref="calendar"
                        v-model="focus"
                        color="primary"
                        :events="events"
                        :event-color="getEventColor"
                        :type="type"
                        @click:event="showEvent"
                        @click:more="viewDay"
                        @click:date="viewDay"
                    ></v-calendar>
                    <v-menu
                        v-model="selectedOpen"
                        :close-on-content-click="false"
                        :activator="selectedElement"
                        offset-x
                    >
                        <v-card
                        color="grey lighten-4"
                        min-width="350px"
                        flat
                        >
                        <v-toolbar
                            :color="selectedEvent.color"
                            dark
                        >
                            <v-btn icon @click="deleteEvent(selectedEvent)">
                                <v-icon>mdi-delete</v-icon>
                            </v-btn>
                            <v-toolbar-title v-html="selectedEvent.name"></v-toolbar-title>
                            <v-spacer></v-spacer>
                        </v-toolbar>
                        <v-card-text>
                            <span v-html="selectedEvent.details"></span>
                        </v-card-text>
                        <v-card-actions>
                            <v-btn
                            text
                            color="secondary"
                            @click="selectedOpen = false"
                            >
                            Cancel
                            </v-btn>
                        </v-card-actions>
                        </v-card>
                    </v-menu>
                    </v-sheet>
                </v-col>
                </v-row>
            </v-app>
        </div>
    </section>
</template>

<script>
    import Navbar from "../navbar/navbar.vue";
    import axios from "axios";
    import store from '../../store/store'


    export default {
        name : "calendar",
        components: {Navbar},
        data: () => ({
            editedItem: {
                nombre_grupo:""
            },
            dialog: false,
            grupoAlumno: "",
            grupos: [],
            editedReserva : {
                nombre_reserva: "",
                fecha_ini: "",
                fecha_ini_tiempo: "",
                fecha_fin: "",
                fecha_fin_tiempo: ""
            },
            focus: '',
            type: 'month',
            typeToLabel: {
            month: 'Month',
            week: 'Week',
            day: 'Day',
            '4day': '4 Days',
            },
            selectedEvent: {},
            selectedElement: null,
            selectedOpen: false,
            events: [],
            colors: ['blue', 'indigo', 'deep-purple', 'cyan', 'green', 'orange', 'grey darken-1'],
            names: [],
        }),
        mounted () {
            this.inicializar_grupos();
            this.$refs.calendar.checkChange();
        },
        methods: {
            setToday () {
                this.focus = ''
            },
            prev () {
                this.$refs.calendar.prev()
            },
            next () {
                this.$refs.calendar.next()
            },
            deleteEvent(selectedEvent){
                let json = {
                    "name": selectedEvent.name,
                    "start": selectedEvent.start,
                    "end": selectedEvent.end,
                    "color": selectedEvent.color,
                    "grupo": selectedEvent.grupo,
                    "pod": selectedEvent.pod
                }
                axios.post('http://127.0.0.1:8080/deleteReserva', json,{headers: {
                                            "Access-Control-Allow-Origin": "*",
                                            "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                            "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                            withCredentials: true}
                                            }).then(data => {
                                                this.events.splice(this.json, 1)
                                                this.selectedOpen = false
                                            }).catch(error => {
                                            console.log("Error con la bd"); 
                                        });

            },
            visualizarGrupo(){
                let json = {
                    "nombre_grupo" : this.editedItem.nombre_grupo
                }
                this.events = []
                axios.post('http://127.0.0.1:8080/getReservasByGrupo', json,{headers: {
                            "Access-Control-Allow-Origin": "*",
                            "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                            "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                            withCredentials: true}
                            }).then(data => {
                                data.data.forEach((value, index) => {
                                    this.events.push({
                                        name: value.nombre,
                                        start: value.fecha_ini,
                                        end: value.fecha_fin,
                                        color: value.color,
                                        grupo: value.grupo,
                                        pod: value.pod
                                    })
                                });
                            }).catch(error => {
                            console.log("Error con la bd"); 
                        });
            },
            inicializar_grupos(){
                if(this.$store.state.rol=='Profesor'){
                    axios.get('http://127.0.0.1:8080/getGrupos',{headers: {
                            "Access-Control-Allow-Origin": "*",
                            "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                            "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                            withCredentials: true}
                            }).then(data => {
                                data.data.forEach((value, index) => {
                                        this.grupos.push(value.nombre_grupo);
                                });
                            }).catch(error => {
                            console.log("Error con la bd"); 
                        });
                } else {
                    let json = {
                        "nombre_alumno": this.$store.state.username
                    }
                    axios.post('http://127.0.0.1:8080/getAsignacionAlumnoGrupo',json,{headers: {
                            "Access-Control-Allow-Origin": "*",
                            "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                            "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                            withCredentials: true}
                            }).then(data => {
                                this.grupoAlumno = data.data.grupo
                                this.$store.commit("setGrupo",data.data.grupo);

                                let jsonPod = {
                                    "nombre_grupo": this.$store.state.grupo
                                }
                                axios.post('http://127.0.0.1:8080/getAsignacionGrupoPod',jsonPod,{headers: {
                                        "Access-Control-Allow-Origin": "*",
                                        "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                        "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                        withCredentials: true}
                                        }).then(data => {
                                            this.$store.commit("setPod",data.data.pod);
                                        }).catch(error => {
                                        console.log("Error con la bd"); 
                                    });

                                this.inicializar_eventos()
                            }).catch(error => {
                            console.log("Error con la bd"); 
                        });
                }
            },
            inicializar_eventos(){
                if(this.$store.state.rol=='Alumno'){
                    let json = {
                        "nombre_grupo": this.grupoAlumno
                    }

                    axios.post('http://127.0.0.1:8080/getReservasByGrupo', json,{headers: {
                            "Access-Control-Allow-Origin": "*",
                            "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                            "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                            withCredentials: true}
                            }).then(data => {
                                data.data.forEach((value, index) => {
                                    this.events.push({
                                        name: value.nombre,
                                        start: value.fecha_ini,
                                        end: value.fecha_fin,
                                        color: value.color,
                                        grupo: value.grupo,
                                        pod: value.pod
                                    })
                                });
                            }).catch(error => {
                            console.log("Error con la bd"); 
                        });
                } else if(this.$store.state.rol=='Profesor') {
                    if (this.editedItem.nombre_grupo != ''){
                        let json = {
                            "nombre_grupo": this.editedItem.nombre_grupo
                        }
                        axios.post('http://127.0.0.1:8080/getReservasByGrupo', json,{headers: {
                                "Access-Control-Allow-Origin": "*",
                                "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                withCredentials: true}
                                }).then(data => {
                                    data.data.forEach((value, index) => {
                                        this.events.push({
                                            name: value.nombre,
                                            start: value.fecha_ini,
                                            end: value.fecha_fin,
                                            color: value.color,
                                            grupo: value.grupo,
                                            pod: value.pod
                                        })
                                    });
                                }).catch(error => {
                                console.log("Error con la bd"); 
                            });   
                    }
                }
            },
            addEvent () {
                let comprobacion_ini = this.editedReserva.fecha_ini < this.editedReserva.fecha_fin || this.editedReserva.fecha_ini == this.editedReserva.fecha_fin
                let comprobacion_fin = this.editedReserva.fecha_ini_tiempo < this.editedReserva.fecha_fin_tiempo || this.editedReserva.fecha_ini_tiempo == this.editedReserva.fecha_fin_tiempo
                if(comprobacion_ini && comprobacion_fin){
                    if(this.$store.state.rol=='Alumno'){
                        let json = {
                            "nombre_grupo": this.$store.state.grupo,
                            "fecha_ini": this.editedReserva.fecha_ini + " " + this.editedReserva.fecha_ini_tiempo,
                            "fecha_fin": this.editedReserva.fecha_fin + " " + this.editedReserva.fecha_ini_tiempo,
                            "nombre": this.editedReserva.nombre_reserva,
                            "color": this.colors[this.rnd(0, this.colors.length - 1)],
                            "pod": this.$store.state.pod
                        }
                        axios.post('http://127.0.0.1:8080/insertReserva', json,{headers: {
                                "Access-Control-Allow-Origin": "*",
                                "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                withCredentials: true}
                                }).then(data => {
                                    this.events.push({
                                        name: this.editedReserva.nombre_reserva,
                                        start: this.editedReserva.fecha_ini + " " + this.editedReserva.fecha_ini_tiempo,
                                        end: this.editedReserva.fecha_fin + " " + this.editedReserva.fecha_ini_tiempo,
                                        color: json.color,
                                        grupo: this.$store.state.grupo,
                                        pod: this.$store.state.pod
                                    })
                                    this.dialog = false
                                }).catch(error => {
                                console.log("Error con la bd"); 
                            });
                    } else if(this.$store.state.rol=='Profesor'){
                        if(this.editedItem.nombre_grupo != ''){

                            let jsonPod = {
                                    "nombre_grupo": this.editedItem.nombre_grupo
                                }

                            axios.post('http://127.0.0.1:8080/getAsignacionGrupoPod',jsonPod,{headers: {
                                            "Access-Control-Allow-Origin": "*",
                                            "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                            "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                            withCredentials: true}
                                            }).then(data => {
                                                let json = {
                                                    "nombre_grupo": this.editedItem.nombre_grupo,
                                                    "fecha_ini": this.editedReserva.fecha_ini + " " + this.editedReserva.fecha_ini_tiempo,
                                                    "fecha_fin": this.editedReserva.fecha_fin + " " + this.editedReserva.fecha_ini_tiempo,
                                                    "nombre": this.editedReserva.nombre_reserva,
                                                    "color": this.colors[this.rnd(0, this.colors.length - 1)],
                                                    "pod": data.data.pod
                                                }
                                                axios.post('http://127.0.0.1:8080/insertReserva', json,{headers: {
                                                        "Access-Control-Allow-Origin": "*",
                                                        "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                                        "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                                        withCredentials: true}
                                                        }).then(data => {
                                                            this.events.push({
                                                                name: this.editedReserva.nombre_reserva,
                                                                start: this.editedReserva.fecha_ini + " " + this.editedReserva.fecha_ini_tiempo,
                                                                end: this.editedReserva.fecha_fin + " " + this.editedReserva.fecha_ini_tiempo,
                                                                color: json.color,
                                                                grupo: this.editedItem.nombre_grupo,
                                                                pod: data.data.pod
                                                            })
                                                            this.dialog = false
                                                            //window.location.reload();
                                                        }).catch(error => {
                                                        console.log("Error con la bd"); 
                                                    });
                                }).catch(error => {
                                console.log("Error con la bd"); 
                            });
                        }
                    }
                }
            },
            closeReserva(){
                this.editedReserva.nombre_reserva = ""
                this.editedReserva.fecha_ini = ""
                this.editedReserva.fecha_ini_tiempo = ""
                this.editedReserva.fecha_fin = ""
                this.editedReserva.fecha_fin_tiempo = ""
                this.editedReserva.color = ""
                this.dialog = false
            },
            viewDay ({ date }) {
                this.focus = date
                this.type = 'day'
            },
            getEventColor (event) {
                return event.color
            },
            showEvent ({ nativeEvent, event }) {
                const open = () => {
                    this.selectedEvent = event
                    this.selectedElement = nativeEvent.target
                    requestAnimationFrame(() => requestAnimationFrame(() => this.selectedOpen = true))
                }

                if (this.selectedOpen) {
                    this.selectedOpen = false
                    requestAnimationFrame(() => requestAnimationFrame(() => open()))
                } else {
                    open()
                }

                nativeEvent.stopPropagation()
            },
            rnd (a, b) {
                return Math.floor((b - a + 1) * Math.random()) + a
            },
        },
    }
</script>

<style>

#calendarPage{
    padding: 40px;
    height: calc(100vh - 88px);
    overflow: hidden;
}

</style>
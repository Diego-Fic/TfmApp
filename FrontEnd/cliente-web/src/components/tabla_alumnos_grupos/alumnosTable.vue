<template>
<div class="pl-12 md:pl-22 pr-12 md:pr-22 pt-12 md:pt-22 pb-12 md:pb-22 top: 50% left: 50%" style="height: 100%;">
    <v-app>
        <v-data-table
            :headers="headers"
            :items="posts"
            sort-by="ID"
            class="elevation-1"
            :footer-props="footerProps"
            :search="search"
        >
        <template v-slot:top>
            <v-toolbar
            flat
            >
                <v-text-field
                    v-model="search"
                    append-icon="mdi-magnify"
                    label="Búsqueda"
                    single-line
                    hide-details
                ></v-text-field>
                <v-divider
                    class="mx-4"
                    inset
                    vertical
                ></v-divider>
                <v-spacer></v-spacer>
                <v-dialog
                    v-model="dialogGrupo"
                    max-width="500px"
                >
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn
                            color="primary"
                            dark
                            class="mb-2"
                            v-bind="attrs"
                            v-on="on"
                        >
                            Nuevo Grupo
                        </v-btn>
                    </template>
                    <v-card>
                        <v-card-title>
                            <span class="text-h5">Nuevo Grupo</span>
                        </v-card-title>
                        <v-card-text>
                            <v-container>
                                <v-row>
                                    <v-col
                                    cols="12"
                                    sm="6"
                                    md="4"
                                    >
                                        <v-text-field
                                            v-model="editedGrupo.nombre_grupo"
                                            label="Nombre Grupo"
                                        ></v-text-field>
                                    </v-col>
                                </v-row>
                            </v-container>
                        </v-card-text>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                                <v-btn
                                color="blue darken-1"
                                text
                                @click="closeGrupo"
                                >
                                Cancelar
                                </v-btn>
                                <v-btn
                                color="blue darken-1"
                                text
                                @click="saveGrupo"
                                :disabled="editedGrupo.nombre_grupo == ''"
                                >
                                Guardar
                                </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
                    <v-btn
                        color="primary"
                        dark
                        class="mb-2"
                        @click="cargarAlumnos"
                    >
                        Cargar Alumnos
                    </v-btn>
                <v-dialog v-model="dialogEdit" max-width="500px">
                    <v-card>
                        <v-card-title>
                            <span class="text-h5">Editar Alumno</span>
                        </v-card-title>
                        <v-card-text>
                            <v-container>
                                <v-row>
                                    <v-col
                                    cols="12"
                                    sm="6"
                                    md="6"
                                    >
                                    <v-select
                                        v-model="editedItem.nombre_grupo"
                                        :items="grupos"
                                        label="Grupo"
                                    ></v-select>
                                </v-col>
                                </v-row>
                            </v-container>
                        </v-card-text>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                                <v-btn
                                color="blue darken-1"
                                text
                                @click="closeEdit"
                                >
                                Cancelar
                                </v-btn>
                                <v-btn
                                color="blue darken-1"
                                text
                                @click="save"
                                >
                                Guardar
                                </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-toolbar>
            </template>
                <template v-slot:[`item.actions`]="{ item }">
                    <v-icon
                    small
                    class="mr-2"
                    @click="editItem(item)"
                    >
                    mdi-pencil
                    </v-icon>
            </template>
            <v-alert slot="no-results" :value="true" color="error" icon="mdi-alert">
                No se ha encontrado resultados para "{{ search }}".
            </v-alert>
        </v-data-table>
    </v-app>
</div>
</template>

<style scoped lang="scss">
 ::v-deep .v-application--wrap {
    min-height: fit-content;
    margin-top: 2.5%;
  }
</style>

<script>
import axios from "axios";

export default {
    name: "alumnosTable",
    inicial: "1",
    total: "10",
    data () {
      return { 
        contador: 1,
        dialogGrupo: false,
        dialog: false,
        dialogDelete: false,
        dialogEdit: false,
        repetido: false,
        search: '',
        grupos:["No"],
        footerProps: {'items-per-page-options': [5, 10, 15, 20, -1], 'items-per-page-text':'Elementos por página',pageText: '{0}-{1} de {2}'},
        headers: [
            { text: "ID", value: "id" },
            { text: "Alumno", value: "nombre_alumno" },
            { text: "Grupo", value: "nombre_grupo" },
            { text: "Acciones", value: "actions", sortable: false },
        ],
        posts: [],
        jsonEdit: {},
        editedIndex: -1,
        editedGrupo: {
            nombre_grupo: ""
        },
        copyItem: {
            nombre_alumno: "",
            nombre_grupo: ""
        },
        editedItem: {
            nombre_alumno: "",
            nombre_grupo: ""
        },
        defaultItem: {
            nombre_alumno: "",
            nombre_grupo: ""
        },
      }
    },
    watch: {
        dialog (val) {
        val || this.close()
        },
        dialogDelete (val) {
        val || this.closeDelete()
        },
        dialogGrupo (val) {
        val || this.closeGrupo()
        },
        dialogEdit (val) {
        val || this.closeEdit()
        },
    },
    methods: {
        editItem (item) {
            this.editedIndex = this.posts.indexOf(item)
            this.editedItem = Object.assign({}, item)
            this.copyItem = Object.assign({}, item)
            this.dialogEdit = true
        },
        deleteItemConfirm () {
            axios.post('http://127.0.0.1:8080/deleteDispositivo',this.jsonDelete, {headers: {
                    "Access-Control-Allow-Origin": "*",
                    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                    withCredentials: true}
                    }).then(data => {
                        this.posts.splice(this.editedIndex, 1)
                        this.closeDelete()
                    })
        },
        closeGrupo () {
            this.editedGrupo.nombre_grupo = "";
            this.dialogGrupo = false;
        },
        closeEdit () {
            this.dialogEdit = false
            this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
            })        
        },
        closeDelete () {
            this.dialogDelete = false
            this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
            })
        },
        cargarAlumnos(){
            axios.get('http://127.0.0.1:8080/getAlumnosLdap',{headers: {
                    "Access-Control-Allow-Origin": "*",
                    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                    withCredentials: true}
                    }).then(data => {
                        this.posts = [];
                        this.contador = 1;
                    }).catch(error => {
                       console.log("Error con la bd"); 
                });

            axios.get('http://127.0.0.1:8080/getAlumnos',{headers: {
                    "Access-Control-Allow-Origin": "*",
                    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                    withCredentials: true}
                    }).then(data => {
                        data.data.forEach((value, index) => {
                                let json = {
                                    "id" : this.contador,
                                    "nombre_alumno" : value.nombre_alumno,
                                    "nombre_grupo" : value.nombre_grupo
                                }
                                this.posts.push(json);
                                this.contador++;
                        });
                    }).catch(error => {
                       console.log("Error con la bd"); 
                });
        },
        saveGrupo(){
            let json = {
                "nombre_grupo": this.editedGrupo.nombre_grupo
            }
            this.grupos.find(file => {
                if (file == this.editedGrupo.nombre_grupo) {
                    this.repetido = true;
                }
            })
            if(!this.repetido){
            axios.post('http://127.0.0.1:8080/insertGrupo',json, {headers: {
                                        "Access-Control-Allow-Origin": "*",
                                        "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                        "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                        withCredentials: true}
                                        }).then(data => {
                                            this.grupos.push(this.editedGrupo.nombre_grupo)
                                            this.closeGrupo()
                                        })
            } else {
                this.repetido = false;
                this.closeGrupo()
            }
        },
        save () {
                if (this.editedIndex > -1) {
                    if(this.editedItem.nombre_grupo != this.copyItem.nombre_grupo){
                        axios.post('http://127.0.0.1:8080/insertAsignacionAlumnoGrupo',this.editedItem, {headers: {
                                    "Access-Control-Allow-Origin": "*",
                                    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                    withCredentials: true}
                                    }).then(data => {
                                        Object.assign(this.posts[this.editedIndex], this.editedItem)
                                        this.closeEdit()
                                    })
                    } else {
                        this.closeEdit()
                    }
                } 
        },
        inicializar_alumnos(){
            axios.get('http://127.0.0.1:8080/getAlumnos',{headers: {
                    "Access-Control-Allow-Origin": "*",
                    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                    withCredentials: true}
                    }).then(data => {
                        data.data.forEach((value, index) => {
                                let json = {
                                    "id" : this.contador,
                                    "nombre_alumno" : value.nombre_alumno,
                                    "nombre_grupo" : value.nombre_grupo
                                }
                                this.posts.push(json);
                                this.contador++;
                        });
                    }).catch(error => {
                       console.log("Error con la bd"); 
                });
        },
        inicializar_grupos(){
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
                }
    },
    mounted (){
        this.inicializar_alumnos();
        this.inicializar_grupos();
    },
};
</script>
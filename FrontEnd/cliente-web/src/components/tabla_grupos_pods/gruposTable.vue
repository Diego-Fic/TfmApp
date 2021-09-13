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
                <v-dialog
                    v-model="dialogPod"
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
                            Nuevo Pod
                        </v-btn>
                    </template>
                    <v-card>
                        <v-card-title>
                            <span class="text-h5">Nuevo Pod</span>
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
                                            v-model="editedPod.nombre_pod"
                                            label="Nombre Pod"
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
                                @click="closePod"
                                >
                                Cancelar
                                </v-btn>
                                <v-btn
                                color="blue darken-1"
                                text
                                @click="savePod"
                                :disabled="editedPod.nombre_pod == ''"
                                >
                                Guardar
                                </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
                <v-dialog
                    v-model="dialogEliminarPod"
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
                            Eliminar Pod
                        </v-btn>
                    </template>
                    <v-card>
                        <v-card-title>
                            <span class="text-h5">Eliminar Pod</span>
                        </v-card-title>
                        <v-card-text>
                            <v-container>
                                <v-row>
                                    <v-col
                                    cols="12"
                                    sm="6"
                                    md="4"
                                    >
                                    <v-select
                                        v-model="editedPod.nombre_pod"
                                        :items="pods_default"
                                        label="Pod"
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
                                @click="closeEliminarPod"
                                >
                                Cancelar
                                </v-btn>
                                <v-btn
                                color="blue darken-1"
                                text
                                @click="deletePod"
                                :disabled="editedPod.nombre_pod == ''"
                                >
                                Eliminar
                                </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
                <v-dialog v-model="dialogEdit" max-width="500px">
                    <v-card>
                        <v-card-title>
                            <span class="text-h5">Editar Grupo</span>
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
                                        v-model="editedItem.nombre_pod"
                                        :items="pods"
                                        label="Pods"
                                    ></v-select>
                                    <v-select
                                        v-model="editedItem.nombre_rol"
                                        :items="roles"
                                        label="Rol"
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
                <v-dialog v-model="dialogDelete" max-width="550px">
                    <v-card>
                        <v-card-title class="text-h5">Estas seguro que deseas eliminar esta fila?</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="blue darken-1" text @click="closeDelete">Cancelar</v-btn>
                            <v-btn color="blue darken-1" text @click="deleteItemConfirm">Si</v-btn>
                            <v-spacer></v-spacer>
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
                    <v-icon
                    small
                    @click="deleteItem(item)"
                    >
                    mdi-delete
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
    name: "gruposTable",
    inicial: "1",
    total: "10",
    data () {
      return { 
        contador: 1,
        dialogGrupo: false,
        dialog: false,
        dialogDelete: false,
        dialogEdit: false,
        dialogPod: false,
        dialogEliminarPod: false,
        search: '',
        pods:["No"],
        pods_default:[],
        repetido: false,
        repetidoPod: false,
        roles:  ['ACTIVO', 'INACTIVO'],
        footerProps: {'items-per-page-options': [5, 10, 15, 20, -1], 'items-per-page-text':'Elementos por página',pageText: '{0}-{1} de {2}'},
        headers: [
            { text: "ID", value: "id" },
            { text: "Grupo", value: "nombre_grupo" },
            { text: "Pods", value: "nombre_pod" },
            { text: "Rol", value: "nombre_rol" },
            { text: "Acciones", value: "actions", sortable: false },
        ],
        posts: [],
        jsonDelete: {},
        jsonEdit: {},
        editedIndex: -1,
        editedGrupo: {
            nombre_grupo: ""
        },
        editedPod: {
            deletePod: ""
        },
        editedPod: {
            nombre_pod: ""
        },
        copyItem: {
            nombre_grupo: "",
            nombre_pod: "",
            nombre_rol: ""
        },
        editedItem: {
            nombre_grupo: "",
            nombre_pod: "",
            nombre_rol: ""
        },
        defaultItem: {
            nombre_grupo: "",
            nombre_pod: "",
            nombre_rol: ""
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
        dialogPod (val) {
        val || this.closePod()
        },
        dialogEliminarPod (val) {
        val || this.closeEliminarPod()
        }
    },
    methods: {
        editItem (item) {
            this.editedIndex = this.posts.indexOf(item)
            this.editedItem = Object.assign({}, item)
            this.copyItem = Object.assign({}, item)
            this.dialogEdit = true
        },
        deleteItem(item) {
            this.editedItem = this.posts.indexOf(item)
            this.editedItem = Object.assign({}, item)
            this.dialogDelete = true
            this.jsonDelete = {
                    "nombre_grupo": item.nombre_grupo,
                    "nombre_pod": item.nombre_pod,
                    "rol": item.rol
            }
        },
        deleteItemConfirm () {
            axios.post('http://127.0.0.1:8080/deleteGrupo',this.jsonDelete, {headers: {
                                        "Access-Control-Allow-Origin": "*",
                                        "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                        "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                        withCredentials: true}
                                        }).then(data => {
                                            //this.pods_default.splice(this.jsonDelete.nombre_pod, 1)
                                            //this.pods.splice(this.jsonDelete.nombre_pod, 1)
                                            this.posts.splice(this.jsonDelete, 1)
                                            this.closeDelete()
                                        })
        },
        closeEliminarPod () {
            this.editedPod.nombre_pod = "";
            this.dialogEliminarPod = false;
        },
        closeGrupo () {
            this.editedGrupo.nombre_grupo = "";
            this.dialogGrupo = false;
        },
        closePod () {
            this.editedPod.nombre_pod = "";
            this.dialogPod = false;
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
        deletePod(){
            let json = {
                "nombre_pod": this.editedPod.nombre_pod
            }
            axios.post('http://127.0.0.1:8080/deletePod',json, {headers: {
                                        "Access-Control-Allow-Origin": "*",
                                        "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                        "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                        withCredentials: true}
                                        }).then(data => {
                                            this.pods_default.splice(this.editedPod.nombre_pod, 1)
                                            this.pods.splice(this.editedPod.nombre_pod, 1)
                                            this.closeEliminarPod()
                                        })
        },
        savePod(){
            let json = {
                "nombre_pod": this.editedPod.nombre_pod
            }
            this.pods.find(file => {
                if (file == this.editedPod.nombre_pod) {
                    this.repetidoPod = true;
                }
            })

            if (!this.repetidoPod){
                axios.post('http://127.0.0.1:8080/insertPods',json, {headers: {
                                            "Access-Control-Allow-Origin": "*",
                                            "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                            "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                            withCredentials: true}
                                            }).then(data => {
                                                this.pods.push(this.editedPod.nombre_pod)
                                                this.pods_default.push(this.editedPod.nombre_pod)
                                                this.closePod()
                                            })
            } else {
                this.repetidoPod = false;
                this.closePod()
            }
        },
        saveGrupo(){
            let json = {
                "nombre_grupo": this.editedGrupo.nombre_grupo
            }
            let jsonCrear = {
                "id": this.contador,
                "nombre_grupo": this.editedGrupo.nombre_grupo,
                "nombre_pod": "No",
                "nombre_rol": "INACTIVO"
            }
            this.posts.find(file => {
                if (file.nombre_grupo == this.editedGrupo.nombre_grupo) {
                    this.repetido = true;
                }
            })
            if (!this.repetido){
                axios.post('http://127.0.0.1:8080/insertGrupo',json, {headers: {
                                            "Access-Control-Allow-Origin": "*",
                                            "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                            "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                            withCredentials: true}
                                            }).then(data => {
                                                this.posts.push(jsonCrear)
                                                this.contador++
                                                this.closeGrupo()
                                            })
            } else {
                this.repetido = false;
                this.closeGrupo()
            }
        },
        save () {
                if (this.editedIndex > -1) {
                    if (this.editedItem.nombre_grupo != this.copyItem.nombre_grupo ||
                        this.editedItem.nombre_pod != this.copyItem.nombre_pod || 
                        this.editedItem.nombre_rol != this.copyItem.nombre_rol)
                    {
                        if (this.editedItem.nombre_Pod != "No" && this.editedItem.nombre_pod != this.copyItem.nombre_pod){
                            axios.post('http://127.0.0.1:8080/insertAsignacionGrupoPod',this.editedItem, {headers: {
                                        "Access-Control-Allow-Origin": "*",
                                        "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                        "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                        withCredentials: true}
                                        }).then(data => {
                                            if(this.editedItem.nombre_pod == "No"){
                                                this.editedItem = {
                                                    "nombre_grupo": this.editedItem.nombre_grupo,
                                                    "nombre_pod": this.editedItem.nombre_pod,
                                                    "nombre_rol": "INACTIVO"
                                                }
                                            }
                                            Object.assign(this.posts[this.editedIndex], this.editedItem)
                                            this.editedItem = this.defaultItem
                                            this.closeEdit()
                                        })
                        }
                    }
                } 
        },
        inicializar_grupos(){
            axios.get('http://127.0.0.1:8080/getGrupos',{headers: {
                    "Access-Control-Allow-Origin": "*",
                    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                    withCredentials: true}
                    }).then(data => {
                        data.data.forEach((value, index) => {
                                let json = {
                                    "id" : this.contador,
                                    "nombre_grupo" : value.nombre_grupo,
                                    "nombre_pod" : value.nombre_pod,
                                    "nombre_rol" : value.nombre_rol
                                }
                                this.posts.push(json);
                                this.contador++;
                        });
                    }).catch(error => {
                       console.log("Error con la bd"); 
                });
        },
        inicializar_pods(){
                    axios.get('http://127.0.0.1:8080/getPods',{headers: {
                            "Access-Control-Allow-Origin": "*",
                            "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                            "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                            withCredentials: true}
                            }).then(data => {
                                data.data.forEach((value, index) => {
                                        this.pods.push(value.nombre_pod);
                                        this.pods_default.push(value.nombre_pod);
                                });
                            }).catch(error => {
                            console.log("Error con la bd"); 
                        });
                }
    },
    mounted (){
        this.inicializar_grupos();
        this.inicializar_pods();
    },
};
</script>
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
                <form ref="uploadForm" @submit.prevent="submit" enctype="multipart/form-data">
                    <input 
                        type="file"
                        ref="uploadXML"
                        @change="selectFile()"
                        class="form-control"
                        accept=".xml"
                        required
                    >
                </form>
                <v-spacer></v-spacer>
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
                    v-model="dialog"
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
                            Nuevo Dispositivo
                        </v-btn>
                    </template>
                    <v-card>
                        <v-card-title>
                            <span class="text-h5">{{ formTitle }}</span>
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
                                            v-model="editedItem.serial_number"
                                            label="Serial Number"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col
                                    cols="12"
                                    sm="6"
                                    md="6"
                                    >
                                        <v-text-field
                                            v-model="editedItem.nombre_dispositivo"
                                            label="Nombre"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col
                                    cols="12"
                                    sm="6"
                                    md="6"
                                    >
                                        <v-select
                                            v-model="editedItem.tipo_dispositivo"
                                            :items="tipos"
                                            label="Tipo"
                                        ></v-select>
                                    </v-col>
                                    <v-col
                                    cols="12"
                                    sm="6"
                                    md="6"
                                    >
                                        <v-select
                                            v-model="editedItem.rol_dispositivo"
                                            :items="roles"
                                            label="Rol"
                                        ></v-select>
                                    </v-col>
                                    <v-col
                                    cols="12"
                                    sm="6"
                                    md="6"
                                    >
                                        <v-select
                                            v-model="editedItem.pod_dispositivo"
                                            :items="pods"
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
                                @click="close"
                                >
                                Cancelar
                                </v-btn>
                                <v-btn
                                color="blue darken-1"
                                text
                                @click="save"
                                :disabled="editedItem.serial_number == '' || editedItem.nombre_dispositivo == '' || editedItem.tipo_dispositivo == '' || editedItem.rol_dispositivo == '' || editedItem.pod_dispositivo == ''"
                                >
                                Guardar
                                </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
                <v-dialog v-model="dialogEdit" max-width="500px">
                    <v-card>
                        <v-card-title>
                            <span class="text-h5">{{ formTitle }}</span>
                        </v-card-title>
                        <v-card-text>
                            <v-container>
                                <v-row>
                                    <v-col
                                    cols="12"
                                    sm="6"
                                    md="6"
                                    >
                                        <v-text-field
                                            v-model="editedItem.nombre_dispositivo"
                                            label="Nombre"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col
                                    cols="12"
                                    sm="6"
                                    md="6"
                                    >
                                        <v-select
                                            v-model="editedItem.tipo_dispositivo"
                                            :items="tipos"
                                            label="Tipo"
                                        ></v-select>
                                    </v-col>
                                    <v-col
                                    cols="12"
                                    sm="6"
                                    md="6"
                                    >
                                        <v-select
                                            v-model="editedItem.rol_dispositivo"
                                            :items="roles"
                                            label="Rol"
                                        ></v-select>
                                    </v-col>
                                    <v-col
                                    cols="12"
                                    sm="6"
                                    md="6"
                                    >
                                        <v-select
                                            v-model="editedItem.pod_dispositivo"
                                            :items="pods"
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
    name: "DispositivosTable",
    inicial: "1",
    total: "10",
    data () {
      return { 
        contador: 1,
        formData:null,
        dialogPod: false,
        dialog: false,
        dialogDelete: false,
        dialogEdit: false,
        repetidoPod: false,
        repetidoDispositivo: false,
        search: '',
        tipos:  ['Router', 'Switch'],
        roles:  ['ACTIVO', 'INACTIVO'],
        pods:["No"],
        footerProps: {'items-per-page-options': [5, 10, 15, 20, -1], 'items-per-page-text':'Elementos por página',pageText: '{0}-{1} de {2}'},
        headers: [
            { text: "ID", value: "id" },
            { text: "Serial Number", value: "serial_number" },
            { text: "Nombre", value: "nombre_dispositivo" },
            { text: "Tipo", value: "tipo_dispositivo" },
            { text: "Rol", value: "rol_dispositivo"},
            { text: "Pod", value: "pod_dispositivo"},
            { text: "Acciones", value: "actions", sortable: false },
        ],
        posts: [],
        jsonDelete: {},
        jsonEdit: {},
        editedIndex: -1,
        editedPod: {
            nombre_pod: ""
        },
        editedItem: {
            serial_number: "",
            nombre_dispositivo: "",
            tipo_dispositivo: "",
            rol_dispositivo: "",
            pod_dispositivo: ""
        },
        copyItem: {
            serial_number: "",
            nombre_dispositivo: "",
            tipo_dispositivo: "",
            rol_dispositivo: "",
            pod_dispositivo: ""
        },
        defaultItem: {
            serial_number: "",
            nombre_dispositivo: "",
            tipo_dispositivo: "",
            rol_dispositivo: "",
            pod_dispositivo: ""
        },
      }
    },
    computed: {
        formTitle () {
            return this.editedIndex === -1 ? 'Nuevo Dispositivo' : 'Editar Dispositivo'
        },
    },
    watch: {
        dialog (val) {
        val || this.close()
        },
        dialogDelete (val) {
        val || this.closeDelete()
        },
        dialogPod (val) {
        val || this.closePod()
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
        close () {
            this.dialog = false
            this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
            })
        },
        closePod () {
            this.editedPod.nombre_pod = "";
            this.dialogPod = false
        },
        closeEdit () {
            this.editedItem = Object.assign({}, this.defaultItem)
            this.dialogEdit = false
        },
        closeDelete () {
            this.dialogDelete = false
            this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
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
                                                this.closePod()
                                            })
            } else {
                this.repetidoPod = false;
                this.closePod()
            }
        },
        save () {
            if (this.editedIndex > -1) {
                if (this.editedItem.nombre_dispositivo != this.copyItem.nombre_dispositivo ||
                this.editedItem.tipo_dispositivo != this.copyItem.tipo_dispositivo || 
                this.editedItem.rol_dispositivo != this.copyItem.rol_dispositivo ||
                this.editedItem.pod_dispositivo != this.copyItem.pod_dispositivo){
                axios.post('http://127.0.0.1:8080/updateDispositivo',this.editedItem, {headers: {
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
            } else {
                this.posts.find(file => {
                    if (file.serial_number == this.editedItem.serial_number) {
                        this.repetidoDispositivo = true;
                    }
                })
                if (!this.repetidoDispositivo){
                    axios.post('http://127.0.0.1:8080/insertDispositivo',this.editedItem, {headers: {
                                "Access-Control-Allow-Origin": "*",
                                "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                withCredentials: true}
                                }).then(data => {
                                    let json = {
                                        "id": this.contador,
                                        "serial_number": this.editedItem.serial_number,
                                        "nombre_dispositivo": this.editedItem.nombre_dispositivo,
                                        "tipo_dispositivo": this.editedItem.tipo_dispositivo,
                                        "rol_dispositivo": this.editedItem.rol_dispositivo,
                                        "pod_dispositivo": this.editedItem.pod_dispositivo
                                    }
                                    this.contador++;
                                    this.posts.push(json)
                                    this.close()
                                })
                } else {
                    this.repetidoDispositivo = false;
                    this.close()
                }
            }
        },
        selectFile() {

            let file = this.$refs.uploadXML.files[0]
            this.formData = new FormData()
            this.formData.append("file",file)

            axios.post('http://127.0.0.1:8080/insertDispositivoByXml',this.formData, {headers: {
                                            "Access-Control-Allow-Origin": "*",
                                            "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                                            "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                                            "Content-Type": "multipart/form-data",
                                            withCredentials: true}
                                            }).then(data => {
                                                window.location.reload();
                                            })
           
        },
        deleteItem(item) {
            this.editedIndex = this.posts.indexOf(item)
            this.editedItem = Object.assign({}, item)
            this.dialogDelete = true
            this.jsonDelete = {
                "serial_number": item.serial_number,
                "nombre_dispositivo": item.nombre_dispositivo,
                "tipo_dispositivo": item.tipo_dispositivo,
                "rol_dispositivo": item.rol_dispositivo
            }
        },
        inicializar_dispositivos(){
            axios.get('http://127.0.0.1:8080/getDispositivos',{headers: {
                    "Access-Control-Allow-Origin": "*",
                    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                    withCredentials: true}
                    }).then(data => {
                        data.data.forEach((value, index) => {
                                let json = {
                                    "id" : this.contador,
                                    "serial_number" : value.serial_number,
                                    "nombre_dispositivo" : value.nombre_dispositivo,
                                    "tipo_dispositivo" : value.tipo_dispositivo,
                                    "rol_dispositivo" : value.rol_dispositivo,
                                    "pod_dispositivo" : value.pod_dispositivo,
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
                                });
                            }).catch(error => {
                            console.log("Error con la bd"); 
                        });
                }
    },
    mounted (){
        this.inicializar_dispositivos();
        this.inicializar_pods();
    },
};
</script>
<template>
<div class="pl-12 md:pl-22 pr-12 md:pr-22 pt-12 md:pt-22 pb-12 md:pb-22 top: 50% left: 50%" style="height: 100%;">
  <v-app>
    <v-main>
        <v-container>
            <v-row>
                <v-col>
                    <v-spacer></v-spacer>
                    <v-text-field
                    v-model="search"
                    append-icon="mdi-magnify"
                    label="Búsqueda"
                    single-line
                    hide-details
                    ></v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <v-data-table
                        :headers="headers"
                        :items="posts"
                        :items-per-page="5"
                        class="elevation-1"
                        :footer-props="footerProps"
                        :search="search"
                    >
                        <template v-slot:[`item.actions`]="{ item }">
                            <v-icon small @click="downloadItem(item)">mdi-download</v-icon>
                        </template>
                        <template slot="no-data">
                            Datos no disponibles
                        </template>
                        <v-alert slot="no-results" :value="true" color="error" icon="mdi-alert">
                            No se ha encontrado resultados para "{{ search }}".
                        </v-alert>
                    </v-data-table>
                </v-col>
            </v-row>
        </v-container>
    </v-main>
  </v-app>
</div>
</template>

<style scoped lang="scss">
 ::v-deep .v-application--wrap {
    min-height: fit-content;
  }
</style>


<script>
import axios from "axios";

export default {
    name: "VuetifyDatatable",
    inicial: "1",
    total: "10",
    data () {
      return { 
        contador: 1,
        search: '', 
        footerProps: {'items-per-page-options': [5, 10, 15, 20, -1], 'items-per-page-text':'Elementos por página',pageText: '{0}-{1} de {2}'},
        headers: [
            { text: "ID", value: "id" },
            { text: "Nombre", value: "nombre" },
            { text: "Temario", value: "temario" },
            { text: "Fecha", value: "fecha" },
            { text: "Acciones", value: "actions", sortable: false },
        ],
        posts: [],
      }
    },
    methods: {
        downloadItem(item) {
            let json = {
                "temario": item.temario,
                "nombre": item.nombre
            }
            axios.get('http://127.0.0.1:8080/download', {headers: {
                    "Access-Control-Allow-Origin": "*",
                    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                    withCredentials: true,
                    }, responseType:'blob', params: {json: json}
                    }).then(response => {   
                        var fileURL = window.URL.createObjectURL(new Blob([response.data]));
                        var fileLink = document.createElement('a');
                    
                        fileLink.href = fileURL;
                        fileLink.setAttribute('download', item.nombre);
                        document.body.appendChild(fileLink);
                    
                        fileLink.click();
            
            }).catch(console.error)

        },
    },
    mounted (){
        axios.get('http://127.0.0.1:8080/findDocuments',{headers: {
                    "Access-Control-Allow-Origin": "*",
                    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                    withCredentials: true}
                    }).then(data => {
                        data.data.forEach((value, index) => {
                            value.file_list.forEach((element, index) => {
                                let json = {
                                    "id" : this.contador,
                                    "nombre" : element.name,
                                    "temario" : value.name,
                                    "fecha" : element.modification_date
                                }
                                this.posts.push(json);
                                this.contador++;
                            });
                        });
                    }).catch(error => {
                       console.log("Error con la bd"); 
                });
    },
};
</script>
<template>
    <section>
        <navbar/>
        <div style="height:calc(100vh - 88px); overflow:auto">
            <vuetify-datatable></vuetify-datatable>
        </div>
    </section>
</template>

<script>
    import Navbar from "../navbar/navbar.vue";
    import axios from "axios";
    import VuetifyDatatable from "./VuetifyDatatable";

    export default {
        name : "table_files",
        components: {Navbar,VuetifyDatatable}
    }
</script>

<style>

</style>
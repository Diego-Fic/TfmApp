<template>
    <section>
        <navbar/>
        <div style="height:calc(100vh - 88px); overflow:auto">  
            <DispositivosTable></DispositivosTable>
        </div>
    </section>    
</template>

<script>
    import Navbar from "../navbar/navbar.vue";
    import DispositivosTable from "./alumnosTable.vue";

    export default {
        name : "gestorAlumnos",
        components: {Navbar,DispositivosTable}    
    }
</script>

<style>

</style>
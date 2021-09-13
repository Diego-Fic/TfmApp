<template>
<section>
    <div id="dashboardPage" class="h-screen">  
        <Navbar/>
        <div class="pb-24 md:pb-24 pt-24 md:pt-24 flex flex-wrap mx-auto">
            <grafica_stats_1/>
            <grafica_stats_2/>
        </div>
    </div>
</section>
</template>

<script>
    import Navbar from "../navbar/navbar.vue";
    import grafica_stats_1 from "../dashboards/chart_stats_1.vue";
    import grafica_stats_2 from "./chart_stats_2.vue";

    export default {
        name : "dashboards",
        components: {Navbar,grafica_stats_1,grafica_stats_2}
    }
</script>

<style>
#dashboardPage {
    background-image: url('../../assets/img/backgroung_dashboards.jpg');
}

</style>
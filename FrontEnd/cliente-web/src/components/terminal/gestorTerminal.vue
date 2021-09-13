<template>
        <section>
            <navbar/>
                <div id="terminalPage" style="height:calc(100vh - 88px); overflow:hidden;">    
                    <tabBar/>
                </div>
        </section> 
</template>

<script>
    import Navbar from "../navbar/navbar.vue";
    import TabBar from '../terminal/tabBar.vue';

    export default {
        name : "gestorTerminal",
        components: {Navbar,TabBar}
    }
</script>

<style>

#terminalPage {
    background-image: url('../../assets/img/backgroung_dashboards.jpg');
}

</style>
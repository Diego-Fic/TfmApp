<template>
<section>
    <div id = "homePage" class="h-screen">
        <navbar/>
        <div class="leading-normal tracking-normal text-indigo-400 bg-cover bg-fixed flex flex-wrap mx-auto">
            <!--Main-->
            <div class="container pt-24 md:pt-36 flex flex-wrap flex-col md:flex-row items-center lg:w-2/5 md:w-2/5">
                <!--Left Col-->
                <div class="mx-auto flex flex-col w-full xl:w-2/5 justify-center lg:items-start overflow-y-hidden">
                    <h1 class="my-4 text-3xl md:text-5xl text-white opacity-75 font-bold leading-tight text-center md:text-left">
                    Aplicación
                    <span class="bg-clip-text text-transparent bg-gradient-to-r from-green-400 via-pink-500 to-purple-500">
                        TFM
                    </span>
                    2020/2021
                    </h1>
                    <p class="leading-normal text-base md:text-2xl mb-8 text-center md:text-left">
                    Aprovecha la conectividad de dispositivos a través de los terminales web!
                    </p>
                </div>
            </div>
            <!--Main-->
            <div class="container pt-24 md:pt-36 mx-auto flex flex-wrap flex-col md:flex-row items-center lg:w-3/5 md:w-3/5">
                <!--Right Col-->
                <div class="w-full p-12 overflow-hidden">
                    <img class="mx-auto w-full md:w-4/5 transform -rotate-6 transition hover:scale-105 duration-700 ease-in-out hover:rotate-6" src="../../assets/img/macbook.svg" />
                </div>
            </div>
        </div>
    </div>
</section>
</template>

<script>
    import Navbar from "../navbar/navbar.vue";

    export default {
        name : "user",
        components: {Navbar}
    }
</script>

<style>

#homePage {
    background-image: url('../../assets/img/header.png');
    background-size: cover;
}
</style>
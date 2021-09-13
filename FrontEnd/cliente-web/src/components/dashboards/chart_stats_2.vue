<template>
    <div class="w-full md:w-1/2 p-3">
        <!--Graph Card-->
        <div class="bg-gray-900 border border-gray-800 rounded shadow">
            <div class="border-b border-gray-800 p-3">
                <h5 class="font-bold uppercase text-gray-600">Graph</h5>
            </div>
            <div class="p-5">
                <canvas id="chartjs-2" class="chartjs" width="undefined" height="undefined"></canvas>
            </div>
        </div>
    </div>
</template>

<script>

import Chart from 'chart.js'
import axios from "axios";

export default {
  name: 'grafica_stats_2',
    data() {
        return {
            chartData: '',
            labels: [],
            data_set: []
        }
    },
    mounted: async function() {
        const ctx = document.getElementById('chartjs-2');

        await axios.get('http://127.0.0.1:8080/getMediaLatencias',{headers: {
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
                withCredentials: true}
                }).then(data => {
                    data.data.forEach((value, index) => {
                        this.labels.push(value.mes);
                        this.data_set.push(value.latencia);  
                    });
                    //this.chartData.update();
                }).catch(error => {
                    console.log("Error con la bd"); 
            });

        this.chartData = {
            "type": "line",
            "data": {
                "labels": this.labels,
                "datasets": [{
                    "label": "Media de latencia por mes",
                    "data": this.data_set,
                    "fill": false,
                    "borderColor": "rgb(75, 192, 192)",
                    "lineTension": 0.1
                }]
            },
            "options": {}
        }

        //console.log(this.chartData);
        new Chart(ctx, this.chartData);
    }
}
</script>
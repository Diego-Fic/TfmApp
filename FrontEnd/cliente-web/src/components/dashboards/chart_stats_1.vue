<template>
    <div class="w-full md:w-1/2 p-3">
        <!--Graph Card-->
        <div class="bg-gray-900 border border-gray-800 rounded shadow">
            <div class="border-b border-gray-800 p-3">
                <h5 class="font-bold uppercase text-gray-600">Graph</h5>
            </div>
            <div class="p-5">
                <canvas ref="chart" id="chartjs-1" class="chartjs" width="undefined" height="undefined"></canvas>
            </div>
        </div>
    </div>

</template>

<script>
import Chart from 'chart.js'
import axios from "axios";

export default {
  name: 'grafica_stats_1',
    data() {
        return {
            data1: [],
            labels: [],
            data2: [],
            datos_grafica_1: ""
        }
    },
    mounted: async function() {
        const ctx = document.getElementById('chartjs-1');
    
        await axios.get('http://127.0.0.1:8080/getConexionesMes',{headers: {
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
            "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token",
            withCredentials: true}
            }).then(data => {
                data.data.forEach((value, index) => {
                    this.labels.push(value.mes);
                    this.data1.push(value.total);  
                    this.data2.push(value.conexiones);  
                });
                //this.chartData.update();
            }).catch(error => {
                console.log("Error con la bd"); 
        });

        this.datos_grafica_1 = {
                "type": "bar",
                "data": {
                    "labels": this.labels,
                    "datasets": [{
                        "label": "Conexiones totales por mes",
                        "data": this.data1,
                        "borderColor": "rgb(255, 99, 132)",
                        "backgroundColor": "rgba(255, 99, 132, 0.2)"
                    }, {
                        "label": "Usuario conectados por mes",
                        "data": this.data2,
                        "type": "line",
                        "fill": false,
                        "borderColor": "rgb(54, 162, 235)"
                    }]
                },
                "options": {
                    "scales": {
                        "yAxes": [{
                            "ticks": {
                                "beginAtZero": true
                            }
                        }]
                    }
                }
            }
        
        new Chart(ctx, this.datos_grafica_1);
    }
}
</script>
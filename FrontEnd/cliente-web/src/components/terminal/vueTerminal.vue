<template>
    <div class="page-container">
        <md-app>
            <md-app-content>
                <md-field>
                <label>Terminal</label>
                <md-textarea v-model="textarea" disabled></md-textarea>
                </md-field>
                <md-field>
                <label>Escribir</label>
                <md-input v-model="message"></md-input>
                <md-button class="md-primary md-raised" v-on:click="sendMessage()">Enviar</md-button>
                </md-field>
            </md-app-content>
        </md-app>
    </div>
</template>

<script>

  export default {
    name: "custom",
    components: { },
    data () {
    return {
            message: "",
            textarea: ""
        }
    }, sockets:{
            connect () {
            console.log('connected to chat server')
        },
            count (val) {
            this.count = val.count
        },
            message (data) { // this function gets triggered once a socket event of `message` is received
            this.textarea += "Device: " + data + '\n' // append each new message to the textarea and add a line break
        }
    }, methods: {
        sendMessage () {
            // this will emit a socket event of type `function`
            let json ={
                "mensaje": "",
            }
            this.textarea += "User: " + this.message + '\n'
            this.$socket.emit('message', this.message) // send the content of the message bar to the server
            this.message = '' // empty the message bar
        }
    }
}
</script>

<style>
.md-app {
  height: 800px;
  border: 1px solid rgba(#000, .12);
}
.md-textarea {
  height: 300px;
}

</style>
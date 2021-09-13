//const server = require('server')
//const { get, socket } = require('server/router')
//const port = process.env.PORT || 3000
var express = require("express");
var bodyParser = require('body-parser');
var app = express();


const serialport = require("serialport");
const Readline = require('@serialport/parser-readline');
const serialParser = new Readline({ delimiter: '\r\n' });
const WebSocket = require('ws');
const ws = new WebSocket.Server({ port: 3001 })
const port = 3000

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.static(__dirname));
app.use(express.urlencoded({ extended: false }));

//var socketio = require('socket.io');
var conexionesSeriales = [];
var conexionSerial = null;
var lastWrite = "";
var lastMessage = "";
var sendEmpty = false;

ws.on('connection', ws => {
  //ctx.io.removeAllListeners();
    ws.on('message', ctx => {
      // Send the message to every socket
      var objectValue = JSON.parse(ctx);
      console.log("Mensaje recibido: " ,objectValue);

      if (objectValue.flag == "CONNECT"){
        let json ={
          "mensaje": "Conectado al dispositivo " + objectValue.dispositivo ,
          "dispositivo": objectValue.dispositivo,
          "puerto": objectValue.puerto,
          "flag": "CONNECT"
        }
        ws.send(JSON.stringify(json));
        conexionSerial = new serialport(objectValue.puerto, {
          baudRate: 9600
        });
        conexionSerial.pipe(serialParser);

        let jsonPorts ={
          "dispositivo": objectValue.dispositivo,
          "puerto": objectValue.puerto,
          "flag": "PORTS",
          "port": conexionSerial
        }
        lastWrite = jsonPorts;
        conexionesSeriales.push(jsonPorts);
        conexionSerial= null;
      } else if (objectValue.flag == "CLOSE"){
        var datos = conexionesSeriales.filter(d => d.dispositivo === objectValue.dispositivo);
        datos[0].port.close((err) => {
          if (err) return console.log('Error on close: ', err.message)
          else {
            for(var i = 0; i < conexionesSeriales.length; i++){
              if(conexionesSeriales[i].dispositivo == objectValue.dispositivo){
                  conexionesSeriales.splice(i, 1);
              }
            };
            console.log("Conexion con el puerto cerrada")
          }
        });
      } else if(objectValue.flag == "SEND"){
        var datos = conexionesSeriales.filter(d => d.dispositivo === objectValue.dispositivo);
        lastWrite = datos[0];
        datos[0].port.pipe(serialParser);
        datos[0].port.write(objectValue.mensaje, (err) => {
          if (err) {
            let jsonError ={
              "mensaje": "Dispositivo sin conexion" ,
              "dispositivo": objectValue.dispositivo,
              "puerto": objectValue.puerto,
              "flag": "ERROR"
            }
            //ctx.io.to(Object.keys(ctx.io.sockets.sockets)[0]).emit( 'message', JSON.stringify(jsonError));
          } else {
            console.log("Mensaje enviado al dispositivo : ", datos[0].dispositivo);
          }
        });
      }

    })

    serialParser.on('data',(line)=>{
      console.log(line);
      if ((lastMessage != line && line != "\r") || lastMessage == "Router>\r" || lastMessage == "Firewall Mode [Routed]:\r"){

        var lineBreaks = line.replace(/(\r)/gm,"");

        let jsonSend ={
          "mensaje": lineBreaks,
          "dispositivo": lastWrite.dispositivo,
          "puerto": lastWrite.puerto,
          "flag": "RESPONSE"
        }
        ws.send(JSON.stringify(jsonSend));
        lastMessage = line;
      }
    });
    
    serialParser.on('error', function(err) {
      console.log(err.message);
    });
}),
ws.on('disconnect', ctx => {
  console.log('El usuario se ha desconectado');
  //ctx.io.removeAllListeners("data");
})


app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`)
})
//server({ port }).then(() => console.log(`Server running at http://localhost:${port}`))
////////////////////////////////////////////////////////////
var express = require("express");
var cookieParser = require("cookie-parser");
var session = require('express-session');
var bodyParser = require('body-parser');
var app = express();
var serverConfig = require("./config/serverConfig.js");
///////////////////////////////////////////////////////////
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(cookieParser());
app.use(session({
  secret: 'RVMSaC74vqc',
  resave: false,
  saveUninitialized: true
}))
app.use(express.static(__dirname));
app.set("views", __dirname + "/html");
app.engine("html", require("ejs").renderFile);
app.set("view engine", "html");
app.use(express.urlencoded({ extended: false }));
///////////////////////////////////////////////////////////
require("./routes/routes")(app);
////////////////////////////////////////////////////////////

var server = app.listen(
  serverConfig.portNumber,
  serverConfig.ipAddress,
  function () {
    console.log(
      "Server running at http://" +
        serverConfig.ipAddress +
        ":" +
        serverConfig.portNumber +
        serverConfig.fistEndpoint
    );
  }
);

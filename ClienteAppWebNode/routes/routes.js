const { Console } = require("console");
const { addListener } = require("process");

module.exports = function (app) {
  var randtoken = require("rand-token");
  var request = require("request");
  var http = require("http");
  var querystring = require("querystring");
  var moment = require("moment");
  var path = require("path");
  var open = require('open');
  var AdmZip = require('adm-zip');
  var fs = require("fs");
  var count_error = 0;
  var serverStatus = "Ok";
  var userValidationError = "Ok";
  var validacion_usuario = false;
  var session = null;
  var descarga = null;
  const token = randtoken.generate(16);

  function checkSession(session, res) {
    if (session != undefined) {
      if (session.user) {
        if (session.timeout) {
          var currentDate = moment().add(2, "hours").toDate();
          var checkDate = moment(currentDate).isBefore(session.timerExpiration);

          if (!checkDate) {
            session.destroy();
            res.redirect("/login");
          }
        }
      } else {
        res.redirect("/login");
      }
    }
  }

  app.get("/login", (req, res) => {
    if (req.session != undefined) {
      if (req.session.user) {
        checkSession(req.session, res);
        res.redirect("/menu");
      } else {
        if (count_error > 0) {
          count_error = 0;
          res.render("login.html", {
            serverStatus: serverStatus,
            userValidationError: userValidationError,
          });
        } else {
          serverStatus = "Ok";
          userValidationError = "Ok";
          res.render("login.html", {
            serverStatus: serverStatus,
            userValidationError: userValidationError,
          });
        }
      }
    } else {
      res.render("login.html");
    }
  });

  app.post("/checkLogin", (req, res) => {
    var userLdap = querystring.stringify({
      username: req.body.username,
      password: req.body.password,
    });

    var requestOptions = {
      hostname: "127.0.0.1", // url or ip address
      port: 8080, // default to 80 if not provided
      path: "/checkLogin",
      method: "POST", // HTTP Method
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        "Content-Length": Buffer.byteLength(userLdap),
      },
    };

    var httpreq = http.request(requestOptions, function (response) {
      response.setEncoding("utf8");

      response.on("data", function (chunk) {
        validacion_usuario = chunk;
        count_error = 1;
      });

      response.on("end", function () {
        if (validacion_usuario != "false") {
          res.cookie(
            "session_id",
            token,
            { httpOnly: false },
            { withCredentials: true, credentials: "include" }
          );
          req.session.user = userLdap;
          req.session.remember = false;
          req.session.cookie.expires = false;
          req.session.timeout = false;
          if (req.body.remember_me != undefined) {
            req.session.remember = true;
            req.session.timeout = true;
            req.session.timerExpiration = moment()
              .add(5, "minutes")
              .add(2, "hours")
              .toDate();
            req.session.cookie.expires = moment()
              .add(5, "minutes")
              .add(2, "hours")
              .toDate();
          }
          res.redirect("http://localhost:5050/menu");
        } else {
          userValidationError = "Bad";
          res.redirect("http://localhost:5050/login");
        }
      });
    });

    httpreq.on("error", function (err) {
      console.log("Error: server unreachable");
      serverStatus = "Bad";
      count_error = 1;
      res.redirect("/login");
    });

    httpreq.write(userLdap);
    httpreq.end();
  });

  /*
  app.get("/menu", (req, res) => {
    checkSession(req.session, res);
    if (req.session != undefined) {
      if (req.session.user) {
        res.render("menu.html");
      }
    }
  });
*/
  app.get("/menu", (req, res) => {
    res.render("menu.html");
  });

  app.get("/logout", (req, res) => {
    res.clearCookie("session_id", token);
    req.session.destroy();
    res.redirect("/login");
  });

  app.post("/download", (req, res) => {
    console.log(JSON.stringify(req.body));

    var files_to_download = querystring.stringify({
      files: req.body,
    });

    var requestOptions = {
      hostname: "127.0.0.1", // url or ip address
      port: 8080, // default to 80 if not provided
      path: "/download",
      method: "POST", // HTTP Method
      headers: {
        "Content-Type": "application/json",
      },
      "Content-Length": Buffer.byteLength(files_to_download),
    };

    var httpreq = http.request(requestOptions, function (response) {
      response.setEncoding("utf8");

      response.on("data", function (chunk) {
        var lista = JSON.parse(chunk);
          descarga = lista;
          open( 'http://127.0.0.1:5050/download', function (err) {
            if ( err ) throw err;    
          });        
      });

      response.on("end", function () {});
    });

    httpreq.on("error", function (err) {
      console.log("Error: server unreachable");
    });

    httpreq.write(JSON.stringify(req.body), (encoding = "utf8"));
    httpreq.end();

  });

  app.get("/download", (req, res) => {
    if (descarga.length > 1){
      var zip = new AdmZip();
      // add local file
      descarga.forEach(element => {
        zip.addLocalFile(element.toString());
      });
      
      // get everything as a buffer
      var willSendthis = zip.toBuffer();
  
      //fs.writeFileSync("practicas.zip",willSendthis);
      res.writeHead(200, {
        'Content-Disposition': `attachment; filename="practicas.zip"`,
        'Content-Type': 'application/zip',
      })
    
      res.end(willSendthis)
  
    } else {
      res.download(descarga[0]);
    }
  });

  app.get("/term", (req, res) => {
    //res.render("term.html");
    var cp = require('child_process');
    cp.spawn('cmd', ['/C', 'start cmd.exe']);
  });
};

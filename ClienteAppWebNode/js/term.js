var term = new Terminal({
  cursorBlink: "block",
});
const ws = new WebSocket("ws://localhost:5050", "echo-protocol");
var curr_line = "";
var entries = [];
term.open(document.getElementById("terminal"));
debugger
term.write("web shell $ ");

term.prompt = () => {
  if (curr_line) {
    let data = { method: "command", command: curr_line };
    ws.send(JSON.stringify(data));
  }
};

term.prompt();

//Receive data from socket
ws.onmessage = (msg) => {
  term.write("\r\n" + JSON.parse(msg.data).data);
  curr_line = "";
};

term.on("key", function (key, ev) {
    if (ev.keyCode == 13) {
        if(curr_line){
          entries.push(curr_line);
          term.write("\r\n");
          term.prompt();
        }
    } else if (ev.keyCode == 13) {
        if(curr_line){
            curr_line = curr_line.slice(0, curr_line.length -1);
            term.write("\b \b");
          }
    } else {
      curr_line += key;
      term.write(key);
    }
});
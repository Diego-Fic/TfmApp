(function ($) {
  "use strict";

  $("ul li").on("click", function () {
    $("li").removeClass("active");
    $(this).addClass("active");
  });

  $("#valueRow").change(function () {
    $('tbody tr td input[type="checkbox"]').prop(
      "checked",
      $(this).prop("checked")
    );
  });
})(jQuery);

function searchTable() {
  // Declare variables
  var input, filter, table, tr, td, i, txtValue;
  input = document.getElementById("inputSearch");
  filter = input.value.toUpperCase();
  table = document.getElementById("documents_table");
  tr = table.getElementsByTagName("tr");

  // Loop through all table rows, and hide those who don't match the search query
  for (i = 0; i < tr.length; i++) {
    td = tr[i].getElementsByTagName("td")[1];
    if (td) {
      txtValue = td.textContent || td.innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    }
  }
}

function activarDescarga() {
  checked = $("input[type=checkbox]:checked").length;
  if (checked) {
    $("#botonDescarga").removeAttr("disabled", "");
  } else {
    $("#botonDescarga")[0].setAttribute("disabled", "");
  }
}

function descargarTodos() {
  if ($("#valueRow").is(":checked")) {
    $("#botonDescarga").removeAttr("disabled", "");
  } else if (!$("#valueRow").is(":checked")) {
    $("#botonDescarga")[0].setAttribute("disabled", "");
  }
}

function descargar() {
  var xhr = new XMLHttpRequest();

  var list = [];

  var table = document.getElementById("documents_table");
  var rows = table.getElementsByTagName("tr");
  if ($("#valueRow").is(":checked")) {
    for (i = 1, j = rows.length; i < j; ++i) {
      cells = rows[i].getElementsByTagName("td");
      var directorio = {
        temario: cells[0].innerHTML,
        nombre: cells[1].innerHTML,
      };
      list.push(directorio);
    }
  } else {
    for (i = 1, j = rows.length; i < j; ++i) {
      cells = rows[i].getElementsByTagName("td");
      var activadas = cells[3].getElementsByTagName("input")[0].checked == true;
      var directorio = {
        temario: cells[0].innerText,
        nombre: cells[1].innerText,
      };
      if (activadas) {
        list.push(directorio);
      }
    }
  }

  xhr.open("POST", "/download", true);
  xhr.setRequestHeader("Content-Type", "application/json");
  //xhr.setRequestHeader("Content-Type","application/json;charset=UTF-8");
  xhr.onreadystatechange = function () {
    if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
      console.log(xhr.responseText);
    }
  };
  xhr.send(JSON.stringify(list));
}

function openTerminal(e) {
  $('#modal').modal('show');
  e.preventDefault(); // if you want to not go to href url uncoment this
}

function cerrarPopUp(){
  $('#modal').modal('hide');
}
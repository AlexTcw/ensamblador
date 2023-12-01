function uploadFile() {
    var fileInput = document.getElementById('fileInput');
    var fileName = fileInput.files[0].name;

    // Verificar si la extensión del archivo es .asm
    if (!fileName.endsWith('.asm')) {
        alert('Por favor, selecciona un archivo con extensión .asm');
        return; // Detener la ejecución si no es un archivo .asm
    }

    var formdata = new FormData();
    formdata.append("file", fileInput.files[0]);

    var requestOptions = {
        method: 'POST',
        body: formdata,
        redirect: 'follow'
    };

    fetch("http://localhost:8080/asm/procesarArchivo", requestOptions)
        .then(response => response.json())
        .then(result => {
            // Muestra el contenido completo en jsonResult
            var jsonResultTextarea = document.getElementById('jsonResult');
            jsonResultTextarea.value = JSON.stringify(result, null, 2);

            // Accede a la propiedad reservadasInCodeMap del resultado JSON
            var reservadasInCodeMap = result.reservadasInCodeMap;

            // Muestra reservadasInCodeMap en el área de texto reservadas
            var reservadasTextarea = document.getElementById('reservadas');
            reservadasTextarea.value = JSON.stringify(reservadasInCodeMap, null, 2);

            // Accede a la propiedad palabrasReservadas del resultado JSON
            var palabrasReservadas = result.palabrasReservadas;

            // Muestra palabrasReservadas en el área de texto CodeSeg
            var codeSegTextarea = document.getElementById('CodeSeg');
            codeSegTextarea.value = formatCodeSegments(palabrasReservadas);

            // Accede a la propiedad palabrasConCodigoDetect del resultado JSON
            var palabrasConCodigoDetect = result.palabrasConCodigoDetect;

            // Muestra palabrasConCodigoDetect en el área de texto palConType
            var palConTypeTextarea = document.getElementById('palConType');
            palConTypeTextarea.value = JSON.stringify(palabrasConCodigoDetect, null, 2);

            // Después de procesar el archivo, obtén y muestra el archivo original
            getOriginal(fileInput.files[0]);

            // Ejecutar la función para obtener y mostrar errores
            getErrors(fileInput.files[0]);
        })
        .catch(error => console.log('error', error));
}

// Función para formatear los segmentos de código
function formatCodeSegments(reservadas) {
    var dataSegment = reservadas.dataSegment;
    var codeSegment = reservadas.codeSegment;

    var formattedCode = '';

    // Agrega encabezado para el segmento de datos
    formattedCode += 'Data Segment:\n';
    formattedCode += '------------------------\n';
    formattedCode += dataSegment + '\n\n';

    // Agrega encabezado para el segmento de código
    formattedCode += 'Code Segment:\n';
    formattedCode += '------------------------\n';
    formattedCode += formatAssemblyCode(codeSegment) + '\n';

    return formattedCode;
}

// Función para formatear el código ensamblador
function formatAssemblyCode(assemblyCode) {
    // Dividir el código en líneas
    var lines = assemblyCode.split('\n');

    // Agregar sangría y números de línea
    var formattedCode = lines.map(function (line, index) {
        return (index + 1) + ': ' + line;
    }).join('\n');

    return formattedCode;
}

function getOriginal(file) {
    var formdata = new FormData();
    formdata.append("file", file);

    var requestOptions = {
        method: 'POST',
        body: formdata,
        redirect: 'follow'
    };

    fetch("http://localhost:8080/asm/subirArchivo", requestOptions)
        .then(response => response.text())
        .then(result => {
            var originalDocTextarea = document.getElementById('originalDoc');
            originalDocTextarea.value = result;
        })
        .catch(error => console.log('error', error));
}

function getErrors(file) {
    var formdata = new FormData();
    formdata.append("file", file, file.name);
  
    var requestOptions = {
      method: 'POST',
      body: formdata,
      redirect: 'follow'
    };
  
    fetch("http://localhost:8080/asm/validateWords", requestOptions)
      .then(response => response.text())
      .then(result => {
        // Mostrar errores en el textarea
        document.getElementById("errors").value = result;
      })
      .catch(error => console.log('error', error));
  }
  

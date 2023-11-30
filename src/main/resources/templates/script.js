function displayFileName() {
    const fileInput = document.getElementById('fileInput');
    const fileNameDisplay = document.getElementById('fileNameDisplay');

    if (fileInput.files.length > 0) {
        const fileName = fileInput.files[0].name;
        fileNameDisplay.textContent = `Archivo seleccionado: ${fileName}`;
    } else {
        fileNameDisplay.textContent = '';
    }
}

document.getElementById('uploadForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const fileInput = document.getElementById('fileInput');

    if (fileInput.files.length > 0) {
        const formData = new FormData();
        formData.append('file', fileInput.files[0]);

        fetch('http://localhost:8080/asm/subirArchivo', {
            method: 'POST',
            body: formData,
            redirect: 'follow'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error de red: ${response.status}`);
            }
            return response.text();
        })
        .then(result => {
            console.log(result);

            // Mostrar el contenido del archivo
            const fileContentDisplay = document.getElementById('fileContent');
            if (fileContentDisplay) {
                fileContentDisplay.textContent = result;
            }

            // Mostrar el nombre del archivo seleccionado
            displayFileName();

            // Analizar el documento y mostrar el resultado en consola
            analiseDocument(fileInput.files[0]);
        })
        .catch(error => console.error('Error al enviar el archivo:', error));
    } else {
        console.warn('Ningún archivo seleccionado.');
    }
});

function analiseDocument(file) {
    var formdata = new FormData();
    formdata.append("file", file, "file");

    var requestOptions = {
        method: 'POST',
        body: formdata,
        redirect: 'follow'
    };

    fetch("http://localhost:8080/asm/procesarArchivo", requestOptions)
        .then(response => response.text())
        .then(result => console.log(result))
        .catch(error => console.log('error', error));
}

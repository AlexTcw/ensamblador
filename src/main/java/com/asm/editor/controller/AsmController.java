package com.asm.editor.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.asm.editor.model.Seccion;
import com.asm.editor.model.dto.ResponseSection;
import com.asm.editor.service.ReadASM;

@RestController
@RequestMapping("/asm")
@CrossOrigin(origins = { "*" })
public class AsmController {

	@Autowired
	ReadASM asmService;

    @PostMapping("/subirArchivo")
    public ResponseEntity<byte[]> procesarArchivo(@RequestParam("file") MultipartFile file) throws IOException {
        // Realiza las operaciones necesarias con el archivo (file) aquí

        // Convierte el MultipartFile a un array de bytes
        byte[] fileBytes = file.getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // Ajusta el nombre del archivo como desees
        headers.setContentDispositionFormData("attachment", file.getOriginalFilename());

        // Devuelve el archivo como respuesta
        return ResponseEntity.ok()
                .headers(headers)
                .body(fileBytes);
    }


	@PostMapping("/procesarArchivo")
	public ResponseSection ProcesarArchivo(@RequestParam("file") MultipartFile file) {
		if (!file.getOriginalFilename().endsWith(".asm")) {
			return null;
		}
		try {
			
			ResponseSection response = new ResponseSection();
			
			Seccion seccion = asmService.detectarSecciones(file);
			Map<String, String> reservadasEnCodigo = asmService.findReservadas(seccion);
			Map<String, String> palabrasConTipo = asmService.divideCodigoEnPalabras(seccion);
			
			response.setPalabrasReservadas(seccion);
			response.setReservadasInCodeMap(reservadasEnCodigo);
			response.setPalabrasConCodigoDetect(palabrasConTipo);

			return response;
			
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}

	}

}

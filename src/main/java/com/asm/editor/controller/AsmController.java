package com.asm.editor.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.asm.editor.model.Seccion;
import com.asm.editor.service.ReadASM;

@Controller
@RequestMapping("/asm")
public class AsmController {

	@Autowired
	ReadASM asmService;

	@RequestMapping("/getCode")
	public String mostrarFormulario() {
		return "carga-archivo-asm"; // Renderiza la plantilla de carga de archivo
	}

	@PostMapping("/procesarArchivo")
	public String procesarArchivo(@RequestParam("file") MultipartFile file, Model model) {
		if (!file.getOriginalFilename().endsWith(".asm")) {
			model.addAttribute("error", "El archivo debe tener la extensión '.asm'");
			return "error"; // Renderiza la plantilla de error si el archivo no es válido
		}

		try {
			Seccion seccion = asmService.detectarSecciones(file);
			model.addAttribute("seccion", seccion);

			// Busca palabras reservadas en la sección y las líneas de código que las
			// contienen
			Map<String, String> palabrasReservadasConLineas = asmService.findReservadas(seccion);
			model.addAttribute("palabrasReservadasConLineas", palabrasReservadasConLineas);

			return "resultado"; // Renderiza la plantilla de resultado si se procesa correctamente
		} catch (IOException e) {
			model.addAttribute("error", "Error al procesar el archivo.");
			return "error"; // Renderiza la plantilla de error en caso de excepción
		}
	}

}

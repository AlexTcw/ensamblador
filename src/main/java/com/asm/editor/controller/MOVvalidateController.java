package com.asm.editor.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asm.editor.service.validar.ValidarMov;

@RestController
public class MOVvalidateController {
	
	@Autowired
	ValidarMov validarMov;
	
	@GetMapping("/validarPalabras")
	public void validarMov() {
		
		Map<String, String> palabras = new HashMap<>();
        palabras.put("id", "1");
        palabras.put("palabra1", "mov");
        palabras.put("palabra2", "EAX");
        palabras.put("palabra3", ",");
        palabras.put("palabra4", "ebx");
        
		
		String cadenaFiltrada = validarMov.filtrar(palabras);

        System.out.println(cadenaFiltrada);

	}
}

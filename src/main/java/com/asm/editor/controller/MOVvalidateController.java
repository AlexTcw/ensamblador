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
		

	}
}

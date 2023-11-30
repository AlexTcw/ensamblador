package com.asm.editor.service.validar;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface ValidarMov {

	String filtrar(Map<String, String> palabras);
	String add(Map<String, String> palabras);
	String sub(Map<String, String> palabras);
	String jmp(Map<String, String> palabras);
	String loop(Map<String, String> palabras);
}

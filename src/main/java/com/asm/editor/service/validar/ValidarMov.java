package com.asm.editor.service.validar;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface ValidarMov {

	String filtrar(Map<String, String> palabras);

}

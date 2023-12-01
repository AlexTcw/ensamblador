package com.asm.editor.service.validar;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface ValidarMov {

	public String mov(List<String> palabras);
	public String add(List<String> palabras);
	public String sub(List<String> palabras);
	public String jmp(List<String> palabras);
	public String loop(List<String> palabras);
}

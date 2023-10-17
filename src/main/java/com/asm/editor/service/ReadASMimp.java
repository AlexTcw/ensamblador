package com.asm.editor.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.asm.editor.model.Seccion;

@Service
public class ReadASMimp implements ReadASM {

	@Override
	public Seccion detectarSecciones(MultipartFile archivo) throws IOException {
	    if (archivo == null || archivo.isEmpty()) {
	        throw new IllegalArgumentException("El archivo no puede estar vacío.");
	    }

	    if (!archivo.getOriginalFilename().endsWith(".asm")) {
	        throw new IllegalArgumentException("El archivo debe tener la extensión '.asm'");
	    }

	    Seccion seccion = new Seccion();
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(archivo.getInputStream()))) {
	        String line;
	        String currentSection = null;

	        // Patrones de expresión regular para detectar las secciones
	        Pattern dataPattern = Pattern.compile("^\\s*section\\s+\\.data", Pattern.CASE_INSENSITIVE);
	        Pattern codePattern = Pattern.compile("^\\s*section\\s+\\.text", Pattern.CASE_INSENSITIVE);

	        while ((line = reader.readLine()) != null) {
	            Matcher dataMatcher = dataPattern.matcher(line);
	            Matcher codeMatcher = codePattern.matcher(line);

	            // Detecta el data segment
	            if (dataMatcher.matches()) {
	                currentSection = "datasegment";
	            }
	            // Detecta el code segment
	            else if (codeMatcher.matches()) {
	                currentSection = "codesegment";
	            } else if (currentSection != null) {
	                // Si estamos dentro de una sección, agrega la línea al segmento correspondiente
	                if ("datasegment".equals(currentSection)) {
	                    seccion.addDataSegmentLine(line);
	                } else if ("codesegment".equals(currentSection)) {
	                    seccion.addCodeSegmentLine(line);
	                }
	            }
	        }
	    }

	    return seccion;
	}
}

package com.asm.editor.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
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
	
	
	@Override
	public Map<String, String> findReservadas(Seccion seccion) {
	    if (seccion == null) {
	        throw new IllegalArgumentException("La sección no puede ser nula.");
	    }

	    Map<String, String> reservadasEncontradas = new HashMap<>();
	    String codeSegment = seccion.getCodeSegment();

	    // Divide el código en líneas
	    String[] lineasDeCodigo = codeSegment.split("\n");

	    // Define una lista de palabras reservadas
	    String[] palabrasReservadas = {
	        "MOV", "ADD", "SUB", "MUL", "DIV", "INC", "DEC", "JMP", "CALL",
	        "RET", "CMP", "JE", "JNE", "JG", "JL", "JGE", "JLE", "PUSH", "POP",
	        "INT", "OR", "AND", "XOR", "NOT", "SHL", "SHR", "NOP"
	    };

	    // Construye una expresión regular para buscar todas las palabras reservadas
	    StringJoiner regexJoiner = new StringJoiner("|");
	    for (String palabra : palabrasReservadas) {
	        regexJoiner.add("\\b" + Pattern.quote(palabra) + "\\b");
	    }
	    Pattern reservadasPattern = Pattern.compile(regexJoiner.toString(), Pattern.CASE_INSENSITIVE);

	    for (int i = 0; i < lineasDeCodigo.length; i++) {
	        String linea = lineasDeCodigo[i];
	        Matcher matcher = reservadasPattern.matcher(linea);

	        while (matcher.find()) {
	            // Agrega la línea de código completa al mapa
	            String codigoCompleto = lineasDeCodigo[i];

	            // Puedes usar el texto coincidente (palabra reservada) como clave en el mapa
	            String match = linea.substring(matcher.start(), matcher.end());

	            // Modificamos el valor de la palabra reservada en el mapa para incluir la línea
	            String valorActual = reservadasEncontradas.get(match);
	            if (valorActual == null) {
	                reservadasEncontradas.put(match, codigoCompleto);
	            } else {
	                reservadasEncontradas.put(match, valorActual + "\n" + codigoCompleto);
	            }
	        }
	    }

	    // Ahora, busca declaraciones de variables junto con sus valores
	    Pattern variablePattern = Pattern.compile("\\b([A-Za-z_][A-Za-z0-9_]*)\\s+(BYTE|WORD|SWORD)\\s+(-?\\d+|0[xX][0-9A-Fa-f]+),\\s*(-?\\d+),\\s*(-?\\d+),\\s*(-?\\d+)\\b");
	    Matcher variableMatcher = variablePattern.matcher(codeSegment);

	    while (variableMatcher.find()) {
	        // Agrega las coincidencias de variables y sus valores al mapa
	        int start = variableMatcher.start();
	        int end = variableMatcher.end();
	        String match = codeSegment.substring(start, end);

	        // Usa el nombre de la variable como clave en el mapa
	        String variableName = variableMatcher.group(1);

	        // Modificamos el valor de la variable en el mapa para incluir la línea
	        String valorActual = reservadasEncontradas.get(variableName);
	        if (valorActual == null) {
	            reservadasEncontradas.put(variableName, match);
	        } else {
	            reservadasEncontradas.put(variableName, valorActual + "\n" + match);
	        }
	    }

	    return reservadasEncontradas;
	}

}

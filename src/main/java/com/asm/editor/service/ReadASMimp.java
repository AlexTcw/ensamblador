package com.asm.editor.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
			Pattern dataPattern = Pattern.compile("^\\s*\\.data", Pattern.CASE_INSENSITIVE);
			Pattern codePattern = Pattern.compile("^\\s*\\.code", Pattern.CASE_INSENSITIVE);

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
		String[] palabrasReservadas = { "MOV", "ADD", "SUB" };

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
		Pattern variablePattern = Pattern.compile(
				"\\b([A-Za-z_][A-Za-z0-9_]*)\\s+(BYTE|WORD|SWORD)\\s+(-?\\d+|0[xX][0-9A-Fa-f]+),\\s*(-?\\d+),\\s*(-?\\d+),\\s*(-?\\d+)\\b");
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

	public Map<String, String> divideCodigoEnPalabras(Seccion seccion) {
		if (seccion == null) {
			throw new IllegalArgumentException("La sección no puede ser nula.");
		}

		String codeSegment = seccion.getCodeSegment();
		String dataSegment = seccion.getDataSegment();

		// Divide el código en palabras usando un espacio en blanco como delimitador
		String[] palabrasCodeSegment = codeSegment.split("\\s+");
		List<String> palabrasEnCodigo = new ArrayList<>(Arrays.asList(palabrasCodeSegment));

		// Divide el Data Segment en palabras usando un espacio en blanco como
		// delimitador
		String[] palabrasDataSegment = dataSegment.split("\\s+");
		List<String> palabrasEnDataSegment = new ArrayList<>(Arrays.asList(palabrasDataSegment));

		// Combina las palabras del Code Segment y Data Segment en una sola lista
		palabrasEnCodigo.addAll(palabrasEnDataSegment);

		// Crea un mapa para asociar cada palabra con su tipo
		Map<String, String> palabrasConTipo = new HashMap<>();

		for (String palabra : palabrasEnCodigo) {
			// Verifica si la palabra es una palabra reservada, y si lo es, omítela
			if (esPalabraReservada(palabra)) {
				continue;
			}

			// Detecta el tipo de la palabra según patrones (agrega más patrones según sea
			// necesario)
			String tipo = detectarTipo(palabra);

			// Asocia la palabra con su tipo en el mapa
			palabrasConTipo.put(palabra, tipo);
		}

		return palabrasConTipo;
	}

	private boolean esPalabraReservada(String palabra) {
		// Implementa la lógica para verificar si la palabra es una palabra reservada
		// (usando tu lista de palabras reservadas)
		String[] palabrasReservadas = { "MOV", "ADD", "SUB", /* Agrega aquí tus palabras reservadas */
		};
		return Arrays.asList(palabrasReservadas).contains(palabra.toUpperCase()); // Convertir la palabra a mayúsculas
																					// antes de verificar
	}

	private static String detectarTipo(String palabra) {
		Map<Pattern, String> tipoPatronMap = new HashMap<>();
		tipoPatronMap.put(Pattern.compile("\\bbyte\\b", Pattern.CASE_INSENSITIVE), "Definición Dato tipo byte");
		tipoPatronMap.put(Pattern.compile("\\bsbyte\\b", Pattern.CASE_INSENSITIVE), "Definición Dato tipo sbyte");
		tipoPatronMap.put(Pattern.compile("\\bword\\b", Pattern.CASE_INSENSITIVE), "Definición Dato tipo word");
		tipoPatronMap.put(Pattern.compile("\\bSWORD\\b", Pattern.CASE_INSENSITIVE), "Definición Dato tipo sword");
		tipoPatronMap.put(Pattern.compile("\\bDWORD\\b", Pattern.CASE_INSENSITIVE), "Definición Dato tipo dword");
		tipoPatronMap.put(Pattern.compile("\\bSDWORD\\b", Pattern.CASE_INSENSITIVE), "Definición Dato tipo sdword");
		tipoPatronMap.put(Pattern.compile("\\bFWORD\\b", Pattern.CASE_INSENSITIVE), "Definición Dato tipo fword");
		tipoPatronMap.put(Pattern.compile("\\bQWORD\\b", Pattern.CASE_INSENSITIVE), "Definición Dato tipo qword");
		tipoPatronMap.put(Pattern.compile("\\bTBYTE\\b", Pattern.CASE_INSENSITIVE), "Definición Dato tipo tbyte");
		tipoPatronMap.put(Pattern.compile("\\bREAL4\\b", Pattern.CASE_INSENSITIVE), "Definición Dato tipo real4");
		tipoPatronMap.put(Pattern.compile("\\bREAL8\\b", Pattern.CASE_INSENSITIVE), "Definición Dato tipo real8");
		tipoPatronMap.put(Pattern.compile("\\bREAL10\\b", Pattern.CASE_INSENSITIVE), "Definición Dato tipo real10");
		tipoPatronMap.put(Pattern.compile("\\bDB\\b", Pattern.CASE_INSENSITIVE), "Directiva (entero 8 bits)");
		tipoPatronMap.put(Pattern.compile("\\bDW\\b", Pattern.CASE_INSENSITIVE), "Directiva (entero 16 bits)");
		tipoPatronMap.put(Pattern.compile("\\bDD\\b", Pattern.CASE_INSENSITIVE), "Directiva (entero 32 bits)");
		tipoPatronMap.put(Pattern.compile("\\bDQ\\b", Pattern.CASE_INSENSITIVE), "Directiva (entero 64 bits)");
		tipoPatronMap.put(Pattern.compile("\\bDT\\b", Pattern.CASE_INSENSITIVE),
				"Directiva (entero 80 bits - 10 bytes)");
		tipoPatronMap.put(
				Pattern.compile("\\b([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\b", Pattern.CASE_INSENSITIVE),
				"Variable tipo byte sin signo");
		tipoPatronMap.put(
				Pattern.compile("\\b(-[1-9][0-9]?|[0-9]|-1[0-9]{2}|-2[0-5][0-9])\\b", Pattern.CASE_INSENSITIVE),
				"Variable tipo byte con signo");
		tipoPatronMap.put(Pattern.compile("\\b(-?\\d+(\\.\\d+)?)\\b", Pattern.CASE_INSENSITIVE),
				"Variable tipo real corto");
		tipoPatronMap.put(Pattern.compile("\\b(-?\\d+\\.\\d+)\\b", Pattern.CASE_INSENSITIVE),
				"Variable tipo real largo");
		tipoPatronMap.put(Pattern.compile("\\b(-?\\d+\\.\\d+[eE][-+]?\\d+)\\b", Pattern.CASE_INSENSITIVE),
				"Variable tipo real de precisión extendida");
		tipoPatronMap.put(Pattern.compile("\\b([-+]?0[xX][0-9a-fA-F]+|[0-9a-fA-F]+h)\\b", Pattern.CASE_INSENSITIVE),
				"Constante entera hexadecimal");
		tipoPatronMap.put(Pattern.compile("\\b([-+]?0[0-7]+|[0-7]+q)\\b", Pattern.CASE_INSENSITIVE),
				"Constante entera octal");
		tipoPatronMap.put(Pattern.compile("\\b([-+]?0[0-7]+q)\\b", Pattern.CASE_INSENSITIVE),
				"Constante entera octal con sufijo q");
		tipoPatronMap.put(Pattern.compile("\\b([-+]?0[0-7]+0)\\b", Pattern.CASE_INSENSITIVE),
				"Constante entera octal con sufijo 0");
		tipoPatronMap.put(Pattern.compile("\\b([-+]?[0-9]+d)\\b", Pattern.CASE_INSENSITIVE),
				"Constante entera decimal");
		tipoPatronMap.put(Pattern.compile("\\b([-+]?[0-9]+b)\\b", Pattern.CASE_INSENSITIVE),
				"Constante entera binaria");
		tipoPatronMap.put(Pattern.compile("\\b([-+]?[0-9]+r)\\b", Pattern.CASE_INSENSITIVE),
				"Constante entera real codificado");
		tipoPatronMap.put(Pattern.compile("\\b([-+]?[0-9]+t)\\b", Pattern.CASE_INSENSITIVE),
				"Constante entera decimal(alternativo)");
		tipoPatronMap.put(Pattern.compile("\\b([-+]?[0-9]+y)\\b", Pattern.CASE_INSENSITIVE),
				"Constante entera binario(alternativo)");
		tipoPatronMap.put(Pattern.compile("\\b(MOV|ADD|SUB|JMP|CMP)\\b", Pattern.CASE_INSENSITIVE),
				"Palabra reservada");
		tipoPatronMap.put(Pattern.compile("\\b(ZF|CF|SF|OF)\\b", Pattern.CASE_INSENSITIVE), "Bandera");
		tipoPatronMap.put(Pattern.compile(
				"\\b(EAX|EBX|ECX|EDX|ESI|EDI|EBP|ESP|AX|BX|CX|DX|SI|DI|BP|SP|AL|BL|CL|DL|AH|BH|CH|DH|CS|DS|SS|ES|FS|GS)\\b",
				Pattern.CASE_INSENSITIVE), "Registro");

		// Agrega patrones para variables de otros tipos (DWORD, QWORD, TBYTE, REAL4,
		// REAL8, REAL10) según sea necesario.

		for (Map.Entry<Pattern, String> entry : tipoPatronMap.entrySet()) {
			Matcher matcher = entry.getKey().matcher(palabra);
			if (matcher.find()) {
				return entry.getValue();
			}
		}

		return "carácter";
	}

}

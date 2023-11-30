package com.asm.editor.service.validar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ValidarMovImp implements ValidarMov {

	@Override
	public String filtrar(Map<String, String> palabras) {
	    String MovRegex = "(?i)\\bMOV\\b";
	    String destinoRegex = "^(?i)(EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|\\[.*\\])$";
	    String origenRegex = "^(?i)(EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|[0-9]+|\\[.*\\])$";

	    List<String> listaPalabras = new ArrayList<>(palabras.values());

	    // Find the first occurrence of the word "MOV" in the list
	    int posicionMOV = -1;
	    for (int i = 0; i < listaPalabras.size(); i++) {
	        if (listaPalabras.get(i).matches(MovRegex)) {
	            posicionMOV = i;
	            break;
	        }
	    }

	    // If the word "MOV" exists
	    if (posicionMOV != -1) {
	        // Validate if the next word is a valid destination
	        if (listaPalabras.get(posicionMOV + 1).matches(destinoRegex)) {
	            // Validate if the next word is a comma
	            if (listaPalabras.get(posicionMOV + 2).equals(",")) {
	                // Validate if the next word is a valid origin
	                if (listaPalabras.get(posicionMOV + 3).matches(origenRegex)) {
	                    // Print the complete string
	                    return listaPalabras.get(posicionMOV) + " " + listaPalabras.get(posicionMOV + 1) + " "
	                            + listaPalabras.get(posicionMOV + 2) + " " + listaPalabras.get(posicionMOV + 3);
	                }
	                else {
	                    System.out.println("Error cerca de `" + listaPalabras.get(posicionMOV + 3) + "`: no es un valor válido");
	                }
	            } else {
	                System.out.println("Error cerca de `" + listaPalabras.get(posicionMOV + 2) + "`: falta una ','");
	            }
	        } else {
	            System.out.println("Error cerca de `" + posicionMOV + "`: no es un valor válido");
	        }
	    }

	    // If none of the conditions are met, return an empty string
	    return "";
	}

	@Override
	public String add(Map<String, String> palabras) {
    String addRegex = "(?i)\\bADD\\b";
    String destinoRegex = "^(?i)(EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|\\[.*\\])$";
    String origenRegex = "^(?i)(EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|[0-9]+|\\[.*\\])$";

    List<String> listaPalabras = new ArrayList<>(palabras.values());

    // Encontrar la primera aparición de la palabra "ADD" en la lista
    int posicionADD = -1;
    for (int i = 0; i < listaPalabras.size(); i++) {
        if (listaPalabras.get(i).matches(addRegex)) {
            posicionADD = i;
            break;
        }
    }

    // Si la palabra "ADD" existe
    if (posicionADD != -1) {
        // Validar si la siguiente palabra es un destino válido
        if (listaPalabras.get(posicionADD + 1).matches(destinoRegex)) {
            // Validar si la siguiente palabra es una coma
            if (listaPalabras.get(posicionADD + 2).equals(",")) {
                // Validar si la siguiente palabra es un origen válido
                if (listaPalabras.get(posicionADD + 3).matches(origenRegex)) {
                    // Imprimir la cadena completa
                    return listaPalabras.get(posicionADD) + " " + listaPalabras.get(posicionADD + 1) + " "
                            + listaPalabras.get(posicionADD + 2) + " " + listaPalabras.get(posicionADD + 3);
                } else {
                    System.out.println("Error cerca de `" + listaPalabras.get(posicionADD + 3) + "`: no es un valor válido");
                }
            } else {
                System.out.println("Error cerca de `" + listaPalabras.get(posicionADD + 2) + "`: falta una ','");
            }
        } else {
            System.out.println("Error cerca de `" + posicionADD + "`: no es un valor válido");
        }
    }

    // Si no se cumplen ninguna de las condiciones, devuelve una cadena vacía
    return "";
}

@Override
public String sub(Map<String, String> palabras) {
    String subRegex = "(?i)\\bSUB\\b";
    String destinoRegex = "^(?i)(EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|\\[.*\\])$";
    String origenRegex = "^(?i)(EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|[0-9]+|\\[.*\\])$";

    List<String> listaPalabras = new ArrayList<>(palabras.values());

    // Encontrar la primera aparición de la palabra "SUB" en la lista
    int posicionSUB = -1;
    for (int i = 0; i < listaPalabras.size(); i++) {
        if (listaPalabras.get(i).matches(subRegex)) {
            posicionSUB = i;
            break;
        }
    }

    // Si la palabra "SUB" existe
    if (posicionSUB != -1) {
        // Validar si la siguiente palabra es un destino válido
        if (listaPalabras.get(posicionSUB + 1).matches(destinoRegex)) {
            // Validar si la siguiente palabra es una coma
            if (listaPalabras.get(posicionSUB + 2).equals(",")) {
                // Validar si la siguiente palabra es un origen válido
                if (listaPalabras.get(posicionSUB + 3).matches(origenRegex)) {
                    // Imprimir la cadena completa
                    return listaPalabras.get(posicionSUB) + " " + listaPalabras.get(posicionSUB + 1) + " "
                            + listaPalabras.get(posicionSUB + 2) + " " + listaPalabras.get(posicionSUB + 3);
                } else {
                    System.out.println("Error cerca de `" + listaPalabras.get(posicionSUB + 3) + "`: no es un valor válido");
                }
            } else {
                System.out.println("Error cerca de `" + listaPalabras.get(posicionSUB + 2) + "`: falta una ','");
            }
        } else {
            System.out.println("Error cerca de `" + posicionSUB + "`: no es un valor válido");
        }
    }

    // Si no se cumplen ninguna de las condiciones, devuelve una cadena vacía
    return "";
}

@Override
public String jmp(Map<String, String> palabras) {
    String jmpRegex = "(?i)\\bJMP\\b";
    String destinoRegex = "^(?i)([A-Za-z_][A-Za-z0-9_]*)$";

    List<String> listaPalabras = new ArrayList<>(palabras.values());

    // Encontrar la primera aparición de la palabra "JMP" en la lista
    int posicionJMP = -1;
    for (int i = 0; i < listaPalabras.size(); i++) {
        if (listaPalabras.get(i).matches(jmpRegex)) {
            posicionJMP = i;
            break;
        }
    }

    // Si la palabra "JMP" existe
    if (posicionJMP != -1) {
        // Validar si la siguiente palabra es un destino válido
        if (listaPalabras.get(posicionJMP + 1).matches(destinoRegex)) {
            // Imprimir la cadena completa
            return listaPalabras.get(posicionJMP) + " " + listaPalabras.get(posicionJMP + 1);
        } else {
            System.out.println("Error cerca de `" + listaPalabras.get(posicionJMP + 1) + "`: no es un valor válido");
        }
    }

    // Si no se cumplen ninguna de las condiciones, devuelve una cadena vacía
    return "";
}

@Override
public String loop(Map<String, String> palabras) {
    String loopRegex = "(?i)\\bLOOP\\b";
    String destinoRegex = "^(?i)([A-Za-z_][A-Za-z0-9_]*)$";

    List<String> listaPalabras = new ArrayList<>(palabras.values());

    // Encontrar la primera aparición de la palabra "LOOP" en la lista
    int posicionLOOP = -1;
    for (int i = 0; i < listaPalabras.size(); i++) {
        if (listaPalabras.get(i).matches(loopRegex)) {
            posicionLOOP = i;
            break;
        }
    }

    // Si la palabra "LOOP" existe
    if (posicionLOOP != -1) {
        // Validar si la siguiente palabra es un destino válido
        if (listaPalabras.get(posicionLOOP + 1).matches(destinoRegex)) {
            // Imprimir la cadena completa
            return listaPalabras.get(posicionLOOP) + " " + listaPalabras.get(posicionLOOP + 1);
        } else {
            System.out.println("Error cerca de `" + listaPalabras.get(posicionLOOP + 1) + "`: no es un valor válido");
        }
    }

    // Si no se cumplen ninguna de las condiciones, devuelve una cadena vacía
    return "";
}




}

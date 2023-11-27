package com.asm.editor.service.validar;

import java.util.ArrayList;
import java.util.HashMap;
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


}

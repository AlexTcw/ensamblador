package com.asm.editor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Seccion {
	
	    private String dataSegment;
	    private String codeSegment;
	    private List<String> words; // Lista para almacenar las palabras separadas

	    public Seccion() {
	        dataSegment = "";
	        codeSegment = "";
	        words = new ArrayList<>();
	    }

	    public String getDataSegment() {
	        return dataSegment;
	    }

	    public void addDataSegmentLine(String line) {
	        dataSegment += line + "\n";
	    }

	    public String getCodeSegment() {
	        return codeSegment;
	    }

	    public void addCodeSegmentLine(String line) {
	        codeSegment += line + "\n";
	    }

	    public List<String> getWords() {
	        return words;
	    }

	    public void addWord(String word) {
	        words.add(word);
	    }
	    
	    public void separarPalabras() {
	        String[] dataWords = dataSegment.split("\\s+");
	        String[] codeWords = codeSegment.split("\\s+");

	        words.addAll(Arrays.asList(dataWords));
	        words.addAll(Arrays.asList(codeWords));
	    }


}

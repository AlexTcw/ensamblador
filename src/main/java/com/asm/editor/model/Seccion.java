package com.asm.editor.model;

public class Seccion {
	
	private StringBuilder dataSegment = new StringBuilder();
    private StringBuilder codeSegment = new StringBuilder();

    public void addDataSegmentLine(String line) {
        dataSegment.append(line).append("\n");
    }

    public void addCodeSegmentLine(String line) {
        codeSegment.append(line).append("\n");
    }

    public String getDataSegment() {
        return dataSegment.toString();
    }

    public String getCodeSegment() {
        return codeSegment.toString();
    }

}

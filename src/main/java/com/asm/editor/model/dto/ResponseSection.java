package com.asm.editor.model.dto;

import java.util.Map;

import com.asm.editor.model.Seccion;

public class ResponseSection {

	Seccion PalabrasReservadas;
	Map<String, String> reservadasInCodeMap;
	Map<String, String> palabrasConCodigoDetect;

	public Seccion getPalabrasReservadas() {
		return PalabrasReservadas;
	}

	public void setPalabrasReservadas(Seccion palabrasReservadas) {
		PalabrasReservadas = palabrasReservadas;
	}

	public Map<String, String> getReservadasInCodeMap() {
		return reservadasInCodeMap;
	}

	public void setReservadasInCodeMap(Map<String, String> reservadasInCodeMap) {
		this.reservadasInCodeMap = reservadasInCodeMap;
	}

	public Map<String, String> getPalabrasConCodigoDetect() {
		return palabrasConCodigoDetect;
	}

	public void setPalabrasConCodigoDetect(Map<String, String> palabrasConCodigoDetect) {
		this.palabrasConCodigoDetect = palabrasConCodigoDetect;
	}

}

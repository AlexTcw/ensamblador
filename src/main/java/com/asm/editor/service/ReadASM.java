package com.asm.editor.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.asm.editor.model.Seccion;

@Service
public interface ReadASM {

	Seccion detectarSecciones(MultipartFile archivo) throws IOException;
}

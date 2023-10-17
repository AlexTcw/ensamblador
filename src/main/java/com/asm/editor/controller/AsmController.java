package com.asm.editor.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.asm.editor.model.Seccion;
import com.asm.editor.service.ReadASM;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/asm")
public class AsmController {
	
	@Autowired
	ReadASM asmService;

    @RequestMapping("/getCode")
    public String mostrarFormulario() {
        return "carga-archivo-asm"; // Renderiza la plantilla de carga de archivo
    }

    @PostMapping("/procesarArchivo")
    public String procesarArchivo(@RequestParam("file") MultipartFile file, Model model) {
        if (!file.getOriginalFilename().endsWith(".asm")) {
            model.addAttribute("error", "El archivo debe tener la extensión '.asm'");
            return "error"; // Renderiza la plantilla de error si el archivo no es válido
        }

        try {
            Seccion seccion = asmService.detectarSecciones(file);
            model.addAttribute("seccion", seccion);
            return "resultado"; // Renderiza la plantilla de resultado si se procesa correctamente
        } catch (IOException e) {
            model.addAttribute("error", "Error al procesar el archivo.");
            return "error"; // Renderiza la plantilla de error en caso de excepción
        }
    }
    
    @GetMapping("/descargar")
    @ResponseBody
    public StreamingResponseBody descargarArchivo(HttpServletResponse response, Seccion seccion) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=archivo.asm");

        // Combina los segmentos de datos y código con un salto de línea
        String contenido = seccion.getDataSegment() + "\n" + seccion.getCodeSegment();

        // Crea un objeto StreamingResponseBody para transmitir el contenido del archivo
        return outputStream -> {
            try (OutputStream out = outputStream) {
                out.write(contenido.getBytes(StandardCharsets.UTF_8));
            }
        };
    }
}

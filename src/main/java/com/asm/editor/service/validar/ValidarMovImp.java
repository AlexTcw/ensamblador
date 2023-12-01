package com.asm.editor.service.validar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class ValidarMovImp implements ValidarMov {

    @Override
    public String mov(List<String> palabras) {
        String resultadoFiltrado = String.join(" ", palabras);

        String movRegex = "(?i)\\bMOV\\b\\s+([^,]+)?,\\s*([^\\s]+)";
        String destinoRegex = "(?i)(" +
                "EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|" +
                "\\[.*\\]|" +
                "\\d+|" +
                "\\[.*\\+.*\\]|" +
                "\\[.*\\]\\s*,\\s*(EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|\\d+|\\[.*\\+.*\\])" +
                "|DL|CL|BL|AH|BH|CH|DH" +
                "|[a-zA-Z_][a-zA-Z_0-9]*" +
                "|0x[0-9a-fA-F]+h" +
                ")";
        String comaDespuesDestinoRegex = "\\bMOV\\b\\s+[^,]+?,\\s*[^\\s]+";

        Pattern movPattern = Pattern.compile(movRegex);
        Matcher movMatcher = movPattern.matcher(resultadoFiltrado);
        Pattern destinoPattern = Pattern.compile(destinoRegex);
        Pattern comaDespuesDestinoPattern = Pattern.compile(comaDespuesDestinoRegex);

        List<String> casosCorrectosMov = new ArrayList<>();
        List<String> casosIncorrectosMov = new ArrayList<>();
        List<String> casosSinDestino = new ArrayList<>();
        List<String> casosOrigenIncorrecto = new ArrayList<>();

        while (movMatcher.find()) {
            String movimiento = movMatcher.group(0);
            String destino = movMatcher.group(1);
            String origen = movMatcher.group(2);

            Matcher destinoMatcher = destinoPattern.matcher(destino != null ? destino : "");
            Matcher comaDespuesDestinoMatcher = comaDespuesDestinoPattern.matcher(movimiento);

            if (destinoMatcher.matches() && comaDespuesDestinoMatcher.find()) {
                casosCorrectosMov.add(movimiento);
            } else {
                if (!destinoMatcher.matches()) {
                    casosIncorrectosMov.add(movimiento + " - Destino incorrecto");
                }
                if (!comaDespuesDestinoMatcher.find()) {
                    casosIncorrectosMov.add(movimiento + " - Falta coma después del destino");
                }
            }

            if (origen == null || origen.isEmpty()) {
                casosSinDestino.add("MOV " + (destino != null ? destino : ""));
            } else if (!destinoMatcher.matches() || !comaDespuesDestinoMatcher.find()) {
                casosOrigenIncorrecto.add(movimiento + " - Origen incorrecto o no especificado");
            }
        }

        StringBuilder respuesta = new StringBuilder();
        respuesta.append("\nCasos incorrectos de 'MOV' con destino incorrecto o falta de coma:\n");
        for (String caso : casosIncorrectosMov) {
            respuesta.append(caso).append("\n");
        }

        respuesta.append("\nCasos de 'MOV' sin destino:\n");
        for (String caso : casosSinDestino) {
            respuesta.append(caso).append("\n");
        }

        respuesta.append("\nCasos de 'MOV' con origen incorrecto o sin origen:\n");
        for (String caso : casosOrigenIncorrecto) {
            respuesta.append(caso).append("\n");
        }

        return respuesta.toString();
    }

    @Override
    public String add(List<String> palabras) {
        String resultadoFiltrado = String.join(" ", palabras);

        String addRegex = "(?i)\\bADD\\b\\s+([^,]+)?,\\s*([^\\s]+)";
        String destinoRegex = "(?i)(" +
                "EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|" +
                "\\[.*\\]|" +
                "\\d+|" +
                "\\[.*\\+.*\\]|" +
                "\\[.*\\]\\s*,\\s*(EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|\\d+|\\[.*\\+.*\\])" +
                "|DL|CL|BL|AH|BH|CH|DH" +
                "|[a-zA-Z_][a-zA-Z_0-9]*" +
                "|0x[0-9a-fA-F]+h" +
                ")";
        String comaDespuesDestinoRegex = "\\bADD\\b\\s+[^,]+?,\\s*[^\\s]+";

        Pattern addPattern = Pattern.compile(addRegex);
        Matcher addMatcher = addPattern.matcher(resultadoFiltrado);
        Pattern destinoPattern = Pattern.compile(destinoRegex);
        Pattern comaDespuesDestinoPattern = Pattern.compile(comaDespuesDestinoRegex);

        List<String> casosCorrectosAdd = new ArrayList<>();
        List<String> casosIncorrectosAdd = new ArrayList<>();
        List<String> casosSinDestino = new ArrayList<>();
        List<String> casosOrigenIncorrecto = new ArrayList<>();

        while (addMatcher.find()) {
            String movimiento = addMatcher.group(0);
            String destino = addMatcher.group(1);
            String origen = addMatcher.group(2);

            Matcher destinoMatcher = destinoPattern.matcher(destino != null ? destino : "");
            Matcher comaDespuesDestinoMatcher = comaDespuesDestinoPattern.matcher(movimiento);

            if (destinoMatcher.matches() && comaDespuesDestinoMatcher.find()) {
                casosCorrectosAdd.add(movimiento);
            } else {
                if (!destinoMatcher.matches()) {
                    casosIncorrectosAdd.add(movimiento + " - Destino incorrecto");
                }
                if (!comaDespuesDestinoMatcher.find()) {
                    casosIncorrectosAdd.add(movimiento + " - Falta coma después del destino");
                }
            }

            if (origen == null || origen.isEmpty()) {
                casosSinDestino.add("ADD " + (destino != null ? destino : ""));
            } else if (!destinoMatcher.matches() || !comaDespuesDestinoMatcher.find()) {
                casosOrigenIncorrecto.add(movimiento + " - Origen incorrecto o no especificado");
            }
        }

        StringBuilder respuesta = new StringBuilder();
        respuesta.append("\nCasos incorrectos de 'ADD' con destino incorrecto o falta de coma:\n");
        for (String caso : casosIncorrectosAdd) {
            respuesta.append(caso).append("\n");
        }

        respuesta.append("\nCasos de 'ADD' sin destino:\n");
        for (String caso : casosSinDestino) {
            respuesta.append(caso).append("\n");
        }

        respuesta.append("\nCasos de 'ADD' con origen incorrecto o sin origen:\n");
        for (String caso : casosOrigenIncorrecto) {
            respuesta.append(caso).append("\n");
        }

        return respuesta.toString();
    }

    @Override
    public String sub(List<String> palabras) {
        String resultadoFiltrado = String.join(" ", palabras);

        String subRegex = "(?i)\\bSUB\\b\\s+([^,]+)?,\\s*([^\\s]+)";
        String destinoRegex = "(?i)(" +
                "EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|" +
                "\\[.*\\]|" +
                "\\d+|" +
                "\\[.*\\+.*\\]|" +
                "\\[.*\\]\\s*,\\s*(EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|\\d+|\\[.*\\+.*\\])" +
                "|DL|CL|BL|AH|BH|CH|DH" +
                "|[a-zA-Z_][a-zA-Z_0-9]*" +
                "|0x[0-9a-fA-F]+h" +
                ")";
        String comaDespuesDestinoRegex = "\\bSUB\\b\\s+[^,]+?,\\s*[^\\s]+";

        Pattern subPattern = Pattern.compile(subRegex);
        Matcher subMatcher = subPattern.matcher(resultadoFiltrado);
        Pattern destinoPattern = Pattern.compile(destinoRegex);
        Pattern comaDespuesDestinoPattern = Pattern.compile(comaDespuesDestinoRegex);

        List<String> casosCorrectosSub = new ArrayList<>();
        List<String> casosIncorrectosSub = new ArrayList<>();
        List<String> casosSinDestino = new ArrayList<>();
        List<String> casosOrigenIncorrecto = new ArrayList<>();

        while (subMatcher.find()) {
            String movimiento = subMatcher.group(0);
            String destino = subMatcher.group(1);
            String origen = subMatcher.group(2);

            Matcher destinoMatcher = destinoPattern.matcher(destino != null ? destino : "");
            Matcher comaDespuesDestinoMatcher = comaDespuesDestinoPattern.matcher(movimiento);

            if (destinoMatcher.matches() && comaDespuesDestinoMatcher.find()) {
                casosCorrectosSub.add(movimiento);
            } else {
                if (!destinoMatcher.matches()) {
                    casosIncorrectosSub.add(movimiento + " - Destino incorrecto");
                }
                if (!comaDespuesDestinoMatcher.find()) {
                    casosIncorrectosSub.add(movimiento + " - Falta coma después del destino");
                }
            }

            if (origen == null || origen.isEmpty()) {
                casosSinDestino.add("SUB " + (destino != null ? destino : ""));
            } else if (!destinoMatcher.matches() || !comaDespuesDestinoMatcher.find()) {
                casosOrigenIncorrecto.add(movimiento + " - Origen incorrecto o no especificado");
            }
        }

        StringBuilder respuesta = new StringBuilder();
        respuesta.append("\nCasos incorrectos de 'SUB' con destino incorrecto o falta de coma:\n");
        for (String caso : casosIncorrectosSub) {
            respuesta.append(caso).append("\n");
        }

        respuesta.append("\nCasos de 'SUB' sin destino:\n");
        for (String caso : casosSinDestino) {
            respuesta.append(caso).append("\n");
        }

        respuesta.append("\nCasos de 'SUB' con origen incorrecto o sin origen:\n");
        for (String caso : casosOrigenIncorrecto) {
            respuesta.append(caso).append("\n");
        }

        return respuesta.toString();
    }

    @Override
    public String jmp(List<String> palabras) {
        String resultadoFiltrado = String.join(" ", palabras);

        String jmpRegex = "(?i)\\bJMP\\b\\s+([^\\s]+)";
        String destinoRegex = "(?i)(" +
                "EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|" +
                "\\[.*\\]|" +
                "\\d+|" +
                "\\[.*\\+.*\\]|" +
                "\\[.*\\]\\s*,\\s*(EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|\\d+|\\[.*\\+.*\\])" +
                "|DL|CL|BL|AH|BH|CH|DH" +
                "|[a-zA-Z_][a-zA-Z_0-9]*" +
                "|0x[0-9a-fA-F]+h" +
                ")";
        String sinComaDespuesDestinoRegex = "\\bJMP\\b\\s+[^,]+";

        Pattern jmpPattern = Pattern.compile(jmpRegex);
        Matcher jmpMatcher = jmpPattern.matcher(resultadoFiltrado);
        Pattern destinoPattern = Pattern.compile(destinoRegex);
        Pattern sinComaDespuesDestinoPattern = Pattern.compile(sinComaDespuesDestinoRegex);

        List<String> casosCorrectosJmp = new ArrayList<>();
        List<String> casosIncorrectosJmp = new ArrayList<>();
        List<String> casosSinDestino = new ArrayList<>();

        while (jmpMatcher.find()) {
            String salto = jmpMatcher.group(0);
            String destino = jmpMatcher.group(1);

            Matcher destinoMatcher = destinoPattern.matcher(destino != null ? destino : "");
            Matcher sinComaDespuesDestinoMatcher = sinComaDespuesDestinoPattern.matcher(salto);

            if (destinoMatcher.matches() || sinComaDespuesDestinoMatcher.find()) {
                casosCorrectosJmp.add(salto);
            } else {
                casosIncorrectosJmp.add(salto + " - Destino incorrecto o falta de coma");
            }

            if (destino == null || destino.isEmpty()) {
                casosSinDestino.add("JMP");
            }
        }

        StringBuilder respuesta = new StringBuilder();
        respuesta.append("\nCasos incorrectos de 'JMP' con destino incorrecto o falta de coma:\n");
        for (String caso : casosIncorrectosJmp) {
            respuesta.append(caso).append("\n");
        }

        respuesta.append("\nCasos de 'JMP' sin destino:\n");
        for (String caso : casosSinDestino) {
            respuesta.append(caso).append("\n");
        }

        return respuesta.toString();
    }

    @Override
    public String loop(List<String> palabras) {
        String resultadoFiltrado = String.join(" ", palabras);

        String loopRegex = "(?i)\\bLOOP\\b\\s+([^\\s]+)";
        String destinoRegex = "(?i)(" +
                "EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|" +
                "\\[.*\\]|" +
                "\\d+|" +
                "\\[.*\\+.*\\]|" +
                "\\[.*\\]\\s*,\\s*(EAX|EBX|ECX|EDX|ESI|EDI|ESP|EBP|\\d+|\\[.*\\+.*\\])" +
                "|DL|CL|BL|AH|BH|CH|DH" +
                "|[a-zA-Z_][a-zA-Z_0-9]*" +
                "|0x[0-9a-fA-F]+h" +
                ")";
        String sinComaDespuesDestinoRegex = "\\bLOOP\\b\\s+[^,]+";

        Pattern loopPattern = Pattern.compile(loopRegex);
        Matcher loopMatcher = loopPattern.matcher(resultadoFiltrado);
        Pattern destinoPattern = Pattern.compile(destinoRegex);
        Pattern sinComaDespuesDestinoPattern = Pattern.compile(sinComaDespuesDestinoRegex);

        List<String> casosCorrectosLoop = new ArrayList<>();
        List<String> casosIncorrectosLoop = new ArrayList<>();
        List<String> casosSinDestino = new ArrayList<>();

        while (loopMatcher.find()) {
            String bucle = loopMatcher.group(0);
            String destino = loopMatcher.group(1);

            Matcher destinoMatcher = destinoPattern.matcher(destino != null ? destino : "");
            Matcher sinComaDespuesDestinoMatcher = sinComaDespuesDestinoPattern.matcher(bucle);

            if (destinoMatcher.matches() || sinComaDespuesDestinoMatcher.find()) {
                casosCorrectosLoop.add(bucle);
            } else {
                casosIncorrectosLoop.add(bucle + " - Destino incorrecto o falta de coma");
            }

            if (destino == null || destino.isEmpty()) {
                casosSinDestino.add("LOOP");
            }
        }

        StringBuilder respuesta = new StringBuilder();
        respuesta.append("\nCasos incorrectos de 'LOOP' con destino incorrecto o falta de coma:\n");
        for (String caso : casosIncorrectosLoop) {
            respuesta.append(caso).append("\n");
        }

        respuesta.append("\nCasos de 'LOOP' sin destino:\n");
        for (String caso : casosSinDestino) {
            respuesta.append(caso).append("\n");
        }

        return respuesta.toString();
    }

}

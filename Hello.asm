.MODEL small
.STACK 100h

.DATA
    mensaje1 DB 'Ingrese el primer número: $'
    mensaje2 DB 'Ingrese el segundo número: $'
    resultado DB 'La suma es: $'

    num1 DW ?
    num2 DW ?
    suma DD ?

.CODE
    main PROC
        ; Imprimir mensaje para ingresar el primer número
        MOV AH, 09h
        LEA DX, mensaje1
        INT 21h

        ; Leer el primer número
        MOV AH, 01h
        INT 21h
        SUB AL, '0'
        MOV num1, AX

        ; Salto de línea
        MOV AH, 02h
        MOV DL, 0Ah
        INT 21h
        MOV DL, 0Dh
        INT 21h

        ; Imprimir mensaje para ingresar el segundo número
        MOV AH, 09h
        LEA DX, mensaje2
        INT 21h

        ; Leer el segundo número
        MOV AH, 01h
        INT 21h
        SUB AL, '0'
        MOV num2, AX

        ; Calcular la suma
        MOV EAX, num1
        ADD EAX, num2
        MOV suma, EAX

        ; Salto de línea
        MOV AH, 02h
        MOV DL, 0Ah
        INT 21h
        MOV DL, 0Dh
        INT 21h

        ; Imprimir el resultado
        MOV AH, 09h
        LEA DX, resultado
        INT 21h

        ; Convertir el resultado a carácter y mostrarlo
        MOV EAX, suma
        CALL MostrarNumero

        ; Salto de línea
        MOV AH, 02h
        MOV DL, 0Ah
        INT 21h
        MOV DL, 0Dh
        INT 21h

        ; Salir del programa
        MOV AH, 4Ch
        INT 21h

    main ENDP

    MostrarNumero PROC
        ; Mostrar un número de 32 bits en pantalla
        MOV ECX, 10
        MOV EBX, 0

    ConvertirDigito:
        XOR EDX, EDX
        DIV ECX
        ADD DL, '0'
        PUSH DX
        INC EBX
        CMP EAX, 0
        JNZ ConvertirDigito

    ImprimirDigito:
        POP DX
        MOV AH, 02h
        INT 21h
        LOOP ImprimirDigito
        RET

    MostrarNumero ENDP

END main

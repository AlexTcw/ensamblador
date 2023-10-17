section .data
    mensaje db 'Hola, mundo!',0

section .text
global _start

_start:
    ; Imprime el mensaje almacenado en el data segment
    mov eax, 4         ; syscall número 4 (write) en eax
    mov ebx, 1         ; descriptor de archivo (1 para stdout) en ebx
    mov ecx, mensaje  ; dirección de la cadena en ecx
    mov edx, 13        ; longitud de la cadena en edx
    int 0x80           ; realizar la syscall

    ; Termina el programa
    mov eax, 1         ; syscall número 1 (exit) en eax
    int 0x80           ; realizar la syscall

package mx.ipn.escom.compiladores;

public enum TipoToken {
    IDENTIFICADOR, NUMERO,  CADENA,

    // Palabras reservadas
    AND, CLASS, ELSE, FALSE, FOR, FUN, IF, NULL, OR, PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,

    // Caracteres
    COMA, PUNTO, ASTERISCO, PARENTESIS_ABRE, PARENTESIS_CIERRA, LLAVE_ABRE, LLAVE_CIERRA, MAS, MENOS, PUNTO_COMA,

    //Dimensión uno o dos caracteres
    MAYOR, MAYOR_IGUAL, MENOR, MENOR_IGUAL, DIFERENTE_DE, IGUAL, IGUAL_IGUAL, BANG, SLASH,

 EOF
}
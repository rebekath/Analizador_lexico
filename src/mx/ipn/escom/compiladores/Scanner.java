package mx.ipn.escom.compiladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TipoToken> palabrasReservadas;
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and", TipoToken.AND);
        palabrasReservadas.put("class", TipoToken.CLASS);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("false",TipoToken.FALSE );
        palabrasReservadas.put("for", TipoToken.FOR );
        palabrasReservadas.put("fun", TipoToken.FUN ); //definir funciones
        palabrasReservadas.put("if", TipoToken.IF );
        palabrasReservadas.put("null", TipoToken.NULL );
        palabrasReservadas.put("or", TipoToken.OR );
        palabrasReservadas.put("print", TipoToken.PRINT );
        palabrasReservadas.put("return", TipoToken.RETURN );
        palabrasReservadas.put("super", TipoToken.SUPER );
        palabrasReservadas.put("this", TipoToken.THIS );
        palabrasReservadas.put("true", TipoToken.TRUE );
        palabrasReservadas.put("var", TipoToken.VAR );
        palabrasReservadas.put("while", TipoToken.WHILE);
    }

    Scanner(String source){
        this.source = source + " ";
    }

    List<Token> scanTokens(){
        int estado = 0;
        char caracter = 0;
        String lexema = "";

        for(int i=0; i<source.length(); i++) {
            caracter = source.charAt(i);

            switch (estado) {
                case 0:
                    if (caracter == '*') {
                        tokens.add(new Token(TipoToken.ASTERISCO, "*", null));
                    } else if (caracter == ',') {
                        tokens.add(new Token(TipoToken.COMA, ",", null));
                    } else if (caracter == '.') {
                        tokens.add(new Token(TipoToken.PUNTO, ".", null));
                    } else if (caracter == '(') {
                        tokens.add(new Token(TipoToken.PARENTESIS_ABRE, "(", null));
                    } else if (caracter == ')') {
                        tokens.add(new Token(TipoToken.PARENTESIS_CIERRA, ")", null));
                    } else if (caracter == '{') {
                        tokens.add(new Token(TipoToken.LLAVE_ABRE, "{", null));
                    } else if (caracter == '}') {
                        tokens.add(new Token(TipoToken.LLAVE_CIERRA, "}", null));
                    } else if (caracter == '+') {
                        tokens.add(new Token(TipoToken.MAS, "+", null));
                    } else if (caracter == '-') {
                        tokens.add(new Token(TipoToken.MENOS, "-", null));
                    } else if (caracter == ';') {
                        tokens.add(new Token(TipoToken.PUNTO_COMA, ";", null));
                    } else if (Character.isAlphabetic(caracter)) {
                        //  Identificadores  o  palabras reservadas
                        estado = 1;
                        lexema = lexema + caracter;
                    } else if (caracter == '<') {
                        estado = 2;
                        lexema = String.valueOf(caracter);
                    } else if (caracter == '>') {
                        estado = 3;
                        lexema = String.valueOf(caracter);
                    } else if (caracter == '=') {
                        estado = 4;
                        lexema = String.valueOf(caracter);
                    } else if (caracter == '!') {
                        estado = 5;
                        lexema = "!";
                    } else if (caracter == '/') {
                        estado = 6;
                        lexema = "/";
                    } else if (caracter == '"'){
                        estado = 10;
                        lexema = "\"";
                    } else if(Character.isDigit(caracter)) {
                        estado = 11;
                        lexema = lexema + caracter;
                        break;
                    }
                    break;
                case 1:
                    if (Character.isAlphabetic(caracter) || Character.isDigit(caracter)) {
                        lexema = lexema + caracter;
                    } else {
                        TipoToken tt = palabrasReservadas.get(lexema);
                        if (tt == null) {
                            tokens.add(new Token(TipoToken.IDENTIFICADOR, lexema, null));
                        } else {
                            tokens.add(new Token(tt, lexema, null)); //agregar null a todos new.token
                        }

                        estado = 0;
                        i--;
                        lexema = "";
                    }
                    break;
                case 2:
                    if (caracter == '=') {
                        tokens.add(new Token(TipoToken.MENOR_IGUAL, "<=", null));
                    } else {
                        tokens.add(new Token(TipoToken.MENOR, "<", null));
                        i--;
                    }
                    estado = 0;
                    lexema = "";
                    break;
                case 3:
                    if (caracter == '=') {
                        tokens.add(new Token(TipoToken.MAYOR_IGUAL, ">=", null));
                    } else {
                        tokens.add(new Token(TipoToken.MAYOR, ">", null));
                        i--;
                    }
                    estado = 0;
                    lexema = "";
                    break;
                case 4:
                    if (caracter == '=') {
                        tokens.add(new Token(TipoToken.IGUAL_IGUAL, "==", null));
                    } else {
                        tokens.add(new Token(TipoToken.IGUAL, "=", null));
                        i--;
                    }
                    estado = 0;
                    lexema = "";
                    break;
                case 5:
                    if (caracter == '=') {
                        tokens.add(new Token(TipoToken.DIFERENTE_DE, "!=", null));
                    } else {
                        tokens.add(new Token(TipoToken.BANG, "!", null));
                        i--;
                    }
                    estado = 0;
                    lexema = "";
                    break;
                case 6:
                    if (caracter == '/') {
                        estado = 7;
                    } else if (caracter == '*') {
                        estado = 8;
                    } else {
                        tokens.add(new Token(TipoToken.SLASH, "/", null));
                        i--;
                        estado = 0;
                        lexema = "";
                    }

                    break;
                case 7:
                    if (caracter == '\n') {
                        estado = 0;
                        lexema = "";
                    }
                    break;
                case 8:
                    if (caracter == '*'){
                        estado = 9;
                    }
                    break;
                case 9:
                    if (caracter == '/'){
                        estado= 0;
                        lexema = "";
                    }
                    else{
                        estado = 8;

                    }
                    break;
                case 10:
                    if (caracter == '"'){
                        lexema +=caracter;
                        String literal = lexema.substring(1, lexema.length() - 1);
                        tokens.add(new Token(TipoToken.CADENA, lexema, literal));
                        estado =  0;
                        lexema = "";
                    }
                    else if (caracter == '\n'){
                        System.out.println("error");
                    }
                    else {
                        lexema += caracter;
                    }
                    break;
                case 11:
                    if(Character.isDigit(caracter)){
                        estado = 11;
                        lexema = lexema + caracter;
                    }
                    else if(caracter == '.'){
                        estado = 12;
                        lexema = lexema + caracter;
                    }
                    else if(caracter == 'E'){
                        estado = 14;
                        lexema = lexema + caracter;
                    }
                    else{
                        tokens.add(new Token(TipoToken.NUMERO, lexema, Double.valueOf(lexema)));
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 12:
                    if(Character.isDigit(caracter)){
                        estado = 13;
                        lexema = lexema + caracter;
                    }
                    else{
                        //Lanzar error
                    }
                    break;
                case 13:
                    if(Character.isDigit(caracter)){
                        estado = 13;
                        lexema = lexema + caracter;
                    }
                    else if(caracter == 'E'){
                        estado = 14;
                        lexema = lexema + caracter;
                    }
                    else{
                        tokens.add(new Token(TipoToken.NUMERO, lexema, Double.valueOf(lexema)));
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 14:
                    if(caracter == '+' || caracter == '-'){
                        estado = 15;
                        lexema = lexema + caracter;
                    }
                    else if(Character.isDigit(caracter)){
                        estado = 16;
                        lexema = lexema + caracter;
                    }
                    else{
                        // Lanzar error
                    }
                    break;
                case 15:
                    if(Character.isDigit(caracter)){
                        estado = 16;
                        lexema = lexema + caracter;
                    }
                    else{
                        // Lanzar error
                    }
                    break;
                case 16:
                    if(Character.isDigit(caracter)){
                        estado = 16;
                        lexema = lexema + caracter;
                    }
                    else{
                        tokens.add(new Token(TipoToken.NUMERO, lexema, Double.valueOf(lexema)));
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;

            }
        }
        tokens.add(new Token(TipoToken.EOF, "", null));

        return tokens;
    }
    char siguienteCaracter(int i){
        if (this.source.length()<=i){
            return 0;
        }
        return this.source.charAt(i+1);
    }

}
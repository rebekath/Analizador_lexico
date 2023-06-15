package mx.ipn.escom.compiladores;

import java.util.List;

public class Parser {

    private final List<Token> tokens;

    private final Token identificador = new Token(TipoToken.IDENTIFICADOR, "", null);
    private final Token select = new Token(TipoToken.LLAVE_CIERRA, "select", null);
    private final Token from = new Token(TipoToken.LLAVE_ABRE, "from", null);
    private final Token distinct = new Token(TipoToken.PARENTESIS_CIERRA, "distinct", null);
    private final Token coma = new Token(TipoToken.COMA, ",", null);
    private final Token punto = new Token(TipoToken.PUNTO, ".", null);
    private final Token asterisco = new Token(TipoToken.ASTERISCO, "*", null);
    private final Token finCadena = new Token(TipoToken.EOF, "", null);

    private int i = 0;
    private boolean hayErrores = false;

    private Token preanalisis;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public void parse(){
        i = 0;
        preanalisis = tokens.get(i);
        Q();

        if(!hayErrores && !preanalisis.equals(finCadena)){
            System.out.println(". No se esperaba el token " + preanalisis.tipo);
        }
        else if(!hayErrores && preanalisis.equals(finCadena)){
            System.out.println("Consulta válida");
        }

        /*if(!preanalisis.equals(finCadena)){
            System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }else if(!hayErrores){
            System.out.println("Consulta válida");
        }*/
    }

    void Q(){
        if(preanalisis.equals(select)){
            coincidir(select);
            D();
            coincidir(from);
            T();
        }
        else{
            hayErrores = true;
            System.out.println(". Se esperaba la palabra reservada SELECT.");
        }
    }

    void D(){
        if(hayErrores) return;

        if(preanalisis.equals(distinct)){
            coincidir(distinct);
            P();
        }
        else if(preanalisis.equals(asterisco) || preanalisis.equals(identificador)){
            P();
        }
        else{
            hayErrores = true;
            System.out.println(". Se esperaba DISTINCT, * o un identificador.");
        }
    }

    void P(){
        if(hayErrores) return;

        if(preanalisis.equals(asterisco)){
            coincidir(asterisco);
        }
        else if(preanalisis.equals(identificador)){
            A();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición \" + preanalisis.posicion + \". Se esperaba * o un identificador.");
        }
    }

    void A(){
        if(hayErrores) return;

        if(preanalisis.equals(identificador)){
            A2();
            A1();
        }
        else{
            hayErrores = true;
            System.out.println(". Se esperaba un identificador.");
        }
    }

    void A1(){
        if(hayErrores) return;

        if(preanalisis.equals(coma)){
            coincidir(coma);
            A();
        }
    }

    void A2(){
        if(hayErrores) return;

        if(preanalisis.equals(identificador)){
            coincidir(identificador);
            A3();
        }
        else{
            hayErrores = true;
            System.out.println(". Se esperaba un identificador.");
        }
    }

    void A3(){
        if(hayErrores) return;

        if(preanalisis.equals(punto)){
            coincidir(punto);
            coincidir(identificador);
        }
    }

    void T(){
        if(hayErrores) return;

        if(preanalisis.equals(identificador)){
            T2();
            T1();
        }
        else{
            hayErrores = true;
            System.out.println(". Se esperaba un identificador.");
        }
    }

    void T1(){
        if(hayErrores) return;

        if(preanalisis.equals(coma)){
            coincidir(coma);
            T();
        }
    }

    void T2(){
        if(hayErrores) return;

        if(preanalisis.equals(identificador)){
            coincidir(identificador);
            T3();
        }
        else{
            hayErrores = true;
            System.out.println(". Se esperaba un identificador.");
        }
    }

    void T3(){
        if(hayErrores) return;

        if(preanalisis.equals(identificador)){
            coincidir(identificador);
        }
    }


    void coincidir(Token t){
        if(hayErrores) return;

        if(preanalisis.tipo == t.tipo){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println(". Se esperaba un  " + t.tipo);

        }
    }

}
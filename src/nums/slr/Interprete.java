/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nums.slr;

import nums.slr.token.*;

import java.util.Vector;
import java.util.Hashtable;

/**
 * Errores que puede haber interpretando una expresion:
 * Número demasiado grande
 * División por cero
 * Operador no soportado
 * Error sintáctico
 * Resultado con decimales
 *
 * La reducción de la regla implica "implícitamente" que el token que se genera lo representa
 * un símbolo que se obtiene del primer caracter de la regla. Si tuvieramos estas dos reglas:
 * S -> n
 * S2 -> Sn
 * No obtendríamos un token con el símbolo S2, sino S, y eso sería incorrecto. Es decir,
 * hay que estar atento a que el símbolo que representa cada regla sea de un sólo caracter.
 * O también se pueden tomar medidas para que el símbolo de un token lo represente un string,
 * ¿eficiencia vs reducción de probabilidad de fallo?
 *
 * Se podrían compartir la lista de operadores entre el generador de expresiones y el intérprete.
 *
 * @author guillermo.barbero.m1
 */
public class Interprete {

    String expresion;
    Pila pila = new Pila();
    char cAct;
    int i;
    Hashtable<Integer, Hashtable<Character, String>> hEstados = new Hashtable();
    Hashtable<String, Hashtable<String, String>> hReglas = new Hashtable();
    NumeroToken tokenSig = null;
    Hashtable<String, String> hMsgErrores = new Hashtable();
    Hashtable<Integer, String> hMapeoEstadosErrores = new Hashtable();
    Vector<Character> vSimbolos = new Vector();

    Token siguienteToken() {
        Token t = new Token();
        if (tokenSig == null) {
            cAct = expresion.charAt(i++);
            switch (cAct) {
                case '(':
                case ')':
                case '$':
                    t = new OtroToken(cAct, cAct);
                    break;
                case '+':
                case '-':
                    t = new OtroToken('a', cAct);
                    break;
                case '*':
                case '/':
                    t = new OtroToken('m', cAct);
                    break;
                default:
                    String s = String.valueOf(cAct);
                    cAct = expresion.charAt(i++);
                    while (!vSimbolos.contains(cAct) && i < expresion.length())
                    {
                        s += cAct;
                        cAct = expresion.charAt(i++);
                    }
                    // El número que aparece en la cadena sabemos que será entero,
                    t = new NumeroToken('n', new Double(Integer.parseInt(s))); 
                    i--;
            }
        } else {
            t = new NumeroToken(tokenSig.simbolo, tokenSig.valor);
            tokenSig = null;
            i--;
        }
        return t;
    }

    Double opera(Character operador, Double x, Double y) {
        Double r = null;
        switch (operador) {
            case '+':
                r = x + y;
                break;
            case '-':
                r = x - y;
                break;
            case '*':
                r = x * y;
                break;
            case '/':
                r = x / (y==0?1:y);
                break;
            default :
                r = new Double(-1); // Habría que lanzar excepción de operador no soporatado
        }
        return r;
    }

    Double haz(String regla, Vector<Token> vTokens) {
        Double x = null;
        if (regla.equals("1")) {
            x = ((NumeroToken) vTokens.get(0)).valor;
        } else if (regla.equals("2") || regla.equals("3")) {
            Character operador = ((OtroToken)vTokens.get(1)).valor;
            x = opera(operador, ((NumeroToken) vTokens.get(2)).valor, ((NumeroToken) vTokens.get(0)).valor);
        } else if (regla.equals("4")) {
            x = ((NumeroToken) vTokens.get(1)).valor;
        } else if (regla.equals("5")) {
            x = ((NumeroToken) vTokens.get(0)).valor;
        } else x = new Double(-1); // Esto ocurriría si la gramática está mal hecha e intentamos reducir por una regla que no existe
        return x;
    }

    public Integer evaluaExpresion(String expresionAEvaluar) {
        Integer r = 0;
        //System.out.print(expresionAEvaluar + " = ");
        i = 0;
        expresion = expresionAEvaluar + '$'; // Añadimos el caracter de fin de línea (en realidad no haría falta, pero bueno)
        Estado estadoInicial = new Estado(0);
        pila.add(estadoInicial);
        Token token;
        String operacion;
        Estado ultEstado = null;
        boolean fin = false;
        char tipoOperacion;
        while (!fin) {
            token = siguienteToken();
            cAct = token.simbolo;
            ultEstado = pila.lastElement();
            //System.out.println("Procesando el Estado " + ultEstado.numero + ", llega el token '" + token.toString() + "'. La pila: " + pila);
            try {
                operacion = hEstados.get(ultEstado.numero).get(cAct);
                tipoOperacion = operacion.charAt(0);
            } catch (NullPointerException e) {
                String msg = new String();
                try {
                    msg = hMsgErrores.get(hMapeoEstadosErrores.get(ultEstado.numero));
                } catch (NullPointerException ne) {
                    msg = "";
                }
                System.out.println("Error sintáctico en la posición " + i + ". " + msg);
                break;
            }
            String operando = operacion.substring(1);
            switch (tipoOperacion) {
                case 'd':
                    //System.out.println("Desplazando al estado " + operando + ". ");
                    Estado sigEstado = new Estado(Integer.parseInt(operando));
                    sigEstado.token = token;
                    pila.add(sigEstado);
                    break;
                case 'r':
                    //System.out.println("Reduciendo por la regla " + operando + ". " + hReglas.get(operando).get("regla"));
                    Vector vTokens = new Vector();
                    for (int iAux = 0; iAux < Integer.parseInt(hReglas.get(operando).get("num")); iAux++) {
                        vTokens.add(pila.pop().token);
                    }
                    tokenSig = new NumeroToken(hReglas.get(operando).get("regla").charAt(0), haz(operando, vTokens));
                    break;
                case 'o':
                    Double d = ((NumeroToken) pila.pop().token).valor;
                    r = d.intValue();
                    if (!new Double(r).equals(d))
                        r = -1; // En caso de que el resultado tenga decimales, lo damos por inválido
                    fin = true;
            }
        }
        //System.out.println(r);
        return r;
    }

    public Interprete() {
        vSimbolos.add('(');
        vSimbolos.add(')');
        vSimbolos.add('+');
        vSimbolos.add('-');
        vSimbolos.add('*');
        vSimbolos.add('/');
        vSimbolos.add('$');
        hMsgErrores.put(")", "Falta un paréntesis");
        hMsgErrores.put("S", "Se esperaba un número");
        hMapeoEstadosErrores.put(5, ")");
        hMapeoEstadosErrores.put(0, "S");
        hMapeoEstadosErrores.put(8, "S");
        hMapeoEstadosErrores.put(9, "S");
        Hashtable _h1 = new Hashtable();
        _h1.put("regla", "S' -> S\0");
        _h1.put("num", "2");
        hReglas.put("1", _h1);
        Hashtable _h2 = new Hashtable();
        _h2.put("regla", "S -> S + S");
        _h2.put("num", "3");
        hReglas.put("2", _h2);
        Hashtable _h3 = new Hashtable();
        _h3.put("regla", "S -> S * S");
        _h3.put("num", "3");
        hReglas.put("3", _h3);
        Hashtable _h4 = new Hashtable();
        _h4.put("regla", "S -> ( S )");
        _h4.put("num", "3");
        hReglas.put("4", _h4);
        Hashtable _h5 = new Hashtable();
        _h5.put("regla", "S -> n");
        _h5.put("num", "1");
        hReglas.put("5", _h5);
        Hashtable h0 = new Hashtable();
        h0.put('(', "d4");
        h0.put('n', "d7");
        h0.put('S', "d1");
        hEstados.put(0, h0);
        Hashtable h1 = new Hashtable();
        h1.put('$', "ok");
        h1.put('m', "d8");
        h1.put('a', "d3");
        hEstados.put(1, h1);
        Hashtable h3 = new Hashtable();
        h3.put('(', "d4");
        h3.put('n', "d7");
        h3.put('S', "d9");
        hEstados.put(3, h3);
        Hashtable h4 = new Hashtable();
        h4.put('(', "d4");
        h4.put('n', "d7");
        h4.put('S', "d5");
        hEstados.put(4, h4);
        Hashtable h5 = new Hashtable();
        h5.put(')', "d6");
        h5.put('a', "d3");
        h5.put('m', "d8");
        hEstados.put(5, h5);
        Hashtable h6 = new Hashtable();
        h6.put(')', "r4");
        h6.put('a', "r4");
        h6.put('m', "r4");
        h6.put('$', "r4");
        hEstados.put(6, h6);
        Hashtable h7 = new Hashtable();
        h7.put(')', "r5");
        h7.put('a', "r5");
        h7.put('m', "r5");
        h7.put('$', "r5");
        hEstados.put(7, h7);
        Hashtable h8 = new Hashtable();
        h8.put('(', "d4");
        h8.put('n', "d7");
        h8.put('S', "d10");
        hEstados.put(8, h8);
        Hashtable h9 = new Hashtable();
        h9.put(')', "r2");
        h9.put('a', "r2");
        h9.put('m', "d8");
        h9.put('$', "r2");
        hEstados.put(9, h9);
        Hashtable h10 = new Hashtable();
        h10.put(')', "r2");
        h10.put('a', "r3");
        h10.put('m', "r3");
        h10.put('$', "r3");
        hEstados.put(10, h10);
    }
}

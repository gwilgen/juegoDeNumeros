/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nums;

import nums.slr.Interprete;

import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;

/**
 *
 * @author guillermo.barbero
 */
public class Main {

    static Vector<String> vOperadores = new Vector();
    static Hashtable<Integer, String> hExpr = new Hashtable();
    static Interprete i = new Interprete();
    static int minNum = 0, maxNum = 30;

    /**
     * Esta función recursiva recive el número de números que quedan por procesar,
     * el número que va a aparecer en las cadenas, la cadena que se ha formado
     * hasta el momento, el número de paréntesis abiertos y un flag que indica si
     * se puede abrir paréntesis en esta iteración o no.
     *
     * El que se puedan cerrar paréntesis o no se gestiona dentro de la propia
     * llamada recursiva, por eso no hay que pasar flags de una llamada a otra.
     * Se hace en el punto en el que indica "poda del árbol".
     *
     * @param num
     * @param n
     * @param s
     * @param npa
     * @param flagP
     */
    public static void creaExpresion(int num, int n, String s, int npa, boolean flagP) {
        if (num > 1) {
            for (int i = 0; i < num - 1; i++) {
                String sAux = s;
                for (int k = 0; k < i; k++) {
                    sAux += "(";
                }
                sAux += n;
                for (int j = 0; j <= npa; j++) {
                    String sAux2 = sAux;
                    for (int k = 0; k < j; k++) {
                        sAux2 += ")";
                    }
                    for (Enumeration<String> eOp = vOperadores.elements(); eOp.hasMoreElements();) {
                        String op = eOp.nextElement();
                        boolean b = !op.equals("?");
                        if (b || j == 0) {
                            creaExpresion(num - 1, n, sAux2 + (b ? op : ""), npa + i - j, b); //i o j siempre valdrá 0
                        }
                    }
                    if (i > 0) {
                        break; // poda del arbol, no vamos a evaluar cadenas del estilo ...(n)...
                    }
                }
                if (!flagP) {
                    break; //Si en la llamada recursiva anterior hemos prohibido abrir parentesis, hacemos caso.
                }
            }
        } else {
            s += n;
            for (int k = 0; k < npa; k++) {
                s += ")";
            }
            eval(s);
        }
    }

    public static void eval(String s) {
        int r = i.evaluaExpresion(s);
        if (r >= 0) {
            if (hExpr.get(r) == null) {
                hExpr.put(r, s);
            }
            if (r == minNum) { // Vamos a mostrar por pantalla algunas expresiones...
                String sAux = null;
                for (; minNum < maxNum; minNum++) {
                    sAux = hExpr.get(minNum);
                    if (sAux == null) {
                        break;
                    } else {
                        System.out.println(minNum + " = " + sAux);
                    }
                }
                if (minNum == maxNum) {
                    System.out.println("No ha hecho falta probar todas las combinaciones");
                    System.exit(0); //No vamos a seguir evaluando cuando ya tenemos todas las que queríamos
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        vOperadores.add("+");
        vOperadores.add("-");
        vOperadores.add("*");
        vOperadores.add("/");
        vOperadores.add("?"); // Esto es para cuando no hay operador (primer número por diez más segundo número)
        int n = 0;
        try {
            n = Integer.parseInt(args[0]);
            //n = 8;
            Main.creaExpresion(n, n, "", 0, true);

            // Por si se ha quedado algun valor sin expresión que la resuelva
            String sAux;
            for (; minNum < maxNum; minNum++) {
                sAux = hExpr.get(minNum);
                if (sAux == null) {
                    System.out.println(minNum + " ?");
                } else {
                    System.out.println(minNum + " = " + sAux);
                }
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            System.out.println("Se debe pasar un número por parámetro");
        }
    }
}

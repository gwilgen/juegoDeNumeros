/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nums.slr.token;

/**
 *
 * @author guillermo.barbero.m1
 */
public class NumeroToken extends Token {

    public Double valor;

    public NumeroToken(Character c, Double d) {
        simbolo = c;
        valor = d;
    }

    @Override
    public String toString() {
        return simbolo + " [" + valor + "]";
    }
}

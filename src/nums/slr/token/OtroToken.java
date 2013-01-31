/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nums.slr.token;

/**
 *
 * @author guillermo.barbero.m1
 */
public class OtroToken extends Token {

    public Character valor;

    public OtroToken(Character cSimbolo, Character cValor) {
        simbolo = cSimbolo;
        valor = cValor;
    }

    @Override
    public String toString() {
        return simbolo + " (" + valor + ")";
    }
}

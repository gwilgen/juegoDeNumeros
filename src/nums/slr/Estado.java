/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nums.slr;

import nums.slr.token.Token;

/**
 *
 * @author guillermo.barbero.m1
 */
public class Estado {

    public int numero;
    public Token token = null;

    public Estado(int n) {
        numero = n;
    }

    @Override
    public String toString() {
        return numero + " - " + (token==null?"":token.toString());
    }
}

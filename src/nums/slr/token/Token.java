/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nums.slr.token;

/**
 *
 * @author guillermo.barbero.m1
 */
public class Token {

    public Character simbolo;
    public Object valor;

    public void clone(Token t) {
        simbolo = t.simbolo;
        valor = (Object)t.valor;
    }

    @Override
    public String toString() {
        return simbolo + " {"+valor+"}";
    }
}

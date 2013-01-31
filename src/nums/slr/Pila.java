/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nums.slr;

import java.util.Vector;
import java.util.Enumeration;

/**
 *
 * @author guillermo.barbero.m1
 */
public class Pila {

    Vector<Estado> vPila;

    public Pila() {
        vPila = new Vector();
    }

    public void add(Estado e) {
        vPila.add(e);
    }

    public Estado lastElement() {
        return vPila.lastElement();
    }

    public Estado pop() {
        Estado e = vPila.lastElement();
        vPila.removeElementAt(vPila.size() - 1);
        return e;
    }

    @Override
    public String toString() {
        String s = new String();
        for (Enumeration<Estado> eEstado = vPila.elements(); eEstado.hasMoreElements();) {
            Estado e = eEstado.nextElement();
            s += e.toString() + ", ";
        }
        return s;
    }
}

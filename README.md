juegoDeNumeros
==============

metasolución en java al artículo http://www.kriptopolis.org/el-juego-del-cuatro

Este programa recibe por parámetro un número y en función de eso crea ecuaciones utilizando ese número tantas veces como sea el valor del mismo. Y evalúa las posibles soluciones para el juego dado.

Por ejemplo, para el 2, crearía ecuaciones tal que:

	2 + 2
	2 - 2
	2 * 2
	2 / 2
	…
	Incluso la ausencia de operador, es decir, 22 se evaluaría como expresión válida

A medida que vaya creando estas ecuaciones las evalúa con un intérprete muy sencillo (hecho a medida) que comprende la gramática formal de estas simples ecuaciones.

Por último se muestra para cada número hasta el 30 (maxNum, hardcodeado) si hay una ecuación que pueda dar ese resultado, por ejemplo:

	0 = 2-2
	1 = 2/2
	2 ?
	3 ?
	4 = 2+2
	5 ?
	…
	21 ?
	22 = 22
	23 ?
	...
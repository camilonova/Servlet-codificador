package edu.nova.log;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Esta clase provee la logica para codificar un codigo por diferentes metodos
 * <p>
 * La ultima version incluye soporte para graficar el codigo
 * 
 * @author Camilo Nova
 * @version 2.0
 */
public class Coder extends JFrame {

	/**
	 * Metodo de codificacion Duobinario
	 */
	public static int DUOBINARIO = 1111;

	/**
	 * Metodo de codificacion Duobinario con Precodificador
	 */
	public static int DUOBINARIO_PRECODIFICADOR = 2222;

	/**
	 * Metodo de codificacion Duobinario Modificado
	 */
	public static int DUOBINARIO_MODIFICADO = 3333;

	/**
	 * Metodo de codificacion Duobinario Modificado con Precodificador
	 */
	public static int DUOBINARIO_MODIFICADO_PRECODIFICADOR = 4444;

	/**
	 * Metodo de codificacion AMI Bipolar (Alternate Mark Inversion)
	 */
	public static int AMI_BIPOLAR = 5555;

	/**
	 * Metodo de codificacion B8ZS basado en AMI Bipolar
	 */
	public static int B8ZS = 6666;

	/**
	 * Metodo de codificacion HDB3 basado en AMI
	 */
	public static int HDB3 = 7777;

	/**
	 * Bit de inicio del metodo Duobinario
	 */
	public static int DUOBINARIO_INIT_BIT = 0;

	/**
	 * Bit de inicio secundario del metodo Duobinario
	 */
	public static int DUOBINARIO_SEC_BIT = 1;

	/**
	 * Cadena codificada
	 */
	private int[] codificatedCode;

	static final int width = 600;

	static final int height = 300;

	static final int upper = 100;

	static final int middle = 150;

	static final int lower = 200;

	static final int separator = 20;

	private Image image;

	private Graphics graphics;

	/**
	 * Constructor que recibe los parametros a codificar
	 * <p>
	 * Creation date 26/04/2006 - 08:38:25 AM
	 * 
	 * @param code
	 *            Codigo binario a codificar
	 * @param codeType
	 *            Metodo de codificacion
	 * 
	 * @since 1.0
	 */
	public Coder(int[] code, int codeType) {
		codificatedCode = new int[code.length];
		prepareGraphics();

		if (codeType == DUOBINARIO)
			duobinario(code);
		else if (codeType == DUOBINARIO_PRECODIFICADOR)
			duobinarioPrecodificador(code);
		else if (codeType == DUOBINARIO_MODIFICADO)
			duobinarioModificado(code);
		else if (codeType == DUOBINARIO_MODIFICADO_PRECODIFICADOR)
			duobinarioModificadoPrecodificador(code);
		else if (codeType == AMI_BIPOLAR)
			amiBipolar(code);
		else if (codeType == B8ZS)
			b8zs(code);
		else if (codeType == HDB3)
			hdb3(code);
	}

	/**
	 * Prepara el area donde se grafican los datos
	 * <p>
	 * Creation date 14/05/2006 - 09:43:47 PM
	 * 
	 * @since 1.0
	 */
	private void prepareGraphics() {
		addNotify();
		setBackground(Color.WHITE);

		image = createImage(width, height);
		graphics = image.getGraphics();

		// Graficamos los ejes horizontales
		graphics.drawString("+", 8, upper + 2);
		graphics.drawString("0", 8, middle + 3);
		graphics.drawString("-", 10, lower + 2);
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.drawLine(20, upper, width - 25, upper);
		graphics.drawLine(20, middle, width - 25, middle);
		graphics.drawLine(20, lower, width - 25, lower);

		// Graficamos los ejes verticales
		int sep = separator;
		int up = 30;
		int low = height - 40;
		for (int i = 0; i <= codificatedCode.length; i++) {
			for (int j = up; j <= low; j += 30)
				graphics.drawLine(sep, j, sep, j + 15);
			sep += separator;
		}

		// Graficamos el borde
		graphics.setColor(Color.black);
		graphics.draw3DRect(0, 0, width - 1, height - 1, true);

	}

	/**
	 * Codifica una cadena por el metodo Duobinario de la forma Ck = Bk + Bk-1
	 * <p>
	 * Creation date 26/04/2006 - 08:58:28 AM
	 * 
	 * @param code
	 *            Cadena a codificar
	 * @since 1.0
	 */
	private void duobinario(int[] code) {
		String name = "DUOBINARIO";
		graphics.drawString(name, (width - graphics.getFontMetrics()
				.stringWidth(name)) / 2, height - 10);
		System.out.print("Cadena a codificar\t");
		printArray(code);

		// Insertamos el primer byte al comienzo de la cadena
		int[] temp = new int[code.length + 1];
		temp[0] = DUOBINARIO_INIT_BIT;
		for (int i = 0; i < code.length; i++) {
			temp[i + 1] = code[i];
		}
		code = temp;

		// Convertimos de codigo a voltaje
		convertToVoltaje(code);

		// Codificacion
		int space = separator;
		int yBefore = 0;
		int xBefore = 0;
		for (int i = 1; i < code.length; i++) {
			graphics.setColor(Color.black);

			int BkMenosUno = code[i - 1];
			int Bk = code[i];
			int Ck = Bk + BkMenosUno;
			codificatedCode[i - 1] = Ck;

			int x1 = space;
			int y = 0;
			int x2 = space += separator;
			if (Ck < 0)
				y = lower;
			else if (Ck == 0)
				y = middle;
			else if (Ck > 0)
				y = upper;

			// Graficamos los datos numericos
			int ex = space - (separator / 2);
			graphics.drawString(String.valueOf(Bk), Bk < 0 ? ex - 5 : ex, 40);
			graphics.drawString(String.valueOf(Ck), Ck < 0 ? ex - 5 : ex,
					height - 40);

			// Graficamos la linea horizontal
			graphics.setColor(Color.blue);
			graphics.drawLine(x1, y, x2, y);

			// Graficamos la linea vertical
			if (i == 1) {
				xBefore = x2;
				yBefore = y;
			} else {
				// graficar
				graphics.drawLine(xBefore, yBefore, x1, y);
				xBefore = x2;
				yBefore = y;
			}

		}

		System.out.print("Cadena codificada\t");
		printArray(codificatedCode);
	}

	/**
	 * Codifica una cadena por el metodo duobinario con precodificador.
	 * <p>
	 * Creation date 26/04/2006 - 04:43:11 PM
	 * 
	 * @param code
	 * @since 1.0
	 */
	private void duobinarioPrecodificador(int[] code) {
		String name = "DUOBINARIO CON PRECODIFICADOR";
		graphics.drawString(name, (width - graphics.getFontMetrics()
				.stringWidth(name)) / 2, height - 10);
		System.out.print("Cadena a codificar\t");
		printArray(code);

		// Insertamos el primer byte al comienzo de la cadena
		int[] precodificado = new int[code.length + 1];
		precodificado[0] = Coder.DUOBINARIO_INIT_BIT;

		// Precodificacion
		for (int i = 0; i < code.length; i++) {
			int Bk = code[i];
			int Ak = precodificado[i];

			// Operacion XOR
			if (Ak == Bk)
				precodificado[i + 1] = 0;
			else
				precodificado[i + 1] = 1;
		}

		// Convertimos de codigo a voltaje
		convertToVoltaje(precodificado);

		// Codificacion
		int space = separator;
		int yBefore = 0;
		int xBefore = 0;
		for (int i = 1; i < precodificado.length; i++) {
			graphics.setColor(Color.black);

			int AkMenosUno = precodificado[i - 1];
			int Ak = precodificado[i];
			int Ck = Ak + AkMenosUno;
			codificatedCode[i - 1] = Ck;

			int x1 = space;
			int y = 0;
			int x2 = space += separator;
			if (Ck < 0)
				y = lower;
			else if (Ck == 0)
				y = middle;
			else if (Ck > 0)
				y = upper;

			// Graficamos los datos numericos
			int ex = space - (separator / 2);
			graphics.drawString(String.valueOf(Ak), Ak < 0 ? ex - 5 : ex, 40);
			graphics.drawString(String.valueOf(Ck), Ck < 0 ? ex - 5 : ex,
					height - 40);

			// Graficamos la linea horizontal
			graphics.setColor(Color.blue);
			graphics.drawLine(x1, y, x2, y);

			// Graficamos la linea vertical
			if (i == 1) {
				xBefore = x2;
				yBefore = y;
			} else {
				// graficar
				graphics.drawLine(xBefore, yBefore, x1, y);
				xBefore = x2;
				yBefore = y;
			}

		}

		System.out.print("Cadena codificada\t");
		printArray(codificatedCode);
	}

	/**
	 * Codifica una cadena por el metodo Duobinario Modificado
	 * <p>
	 * Creation date 26/04/2006 - 08:58:28 AM
	 * 
	 * @param code
	 *            Cadena a codificar
	 * @since 1.0
	 */
	private void duobinarioModificado(int[] code) {
		String name = "DUOBINARIO MODIFICADO";
		graphics.drawString(name, (width - graphics.getFontMetrics()
				.stringWidth(name)) / 2, height - 10);
		System.out.print("Cadena a codificar\t");
		printArray(code);

		// Insertamos el primer byte al comienzo de la cadena
		int[] temp = new int[code.length + 2];
		temp[0] = DUOBINARIO_INIT_BIT;
		temp[1] = DUOBINARIO_SEC_BIT;
		for (int i = 0; i < code.length; i++) {
			temp[i + 2] = code[i];
		}
		code = temp;

		// Convertimos de codigo a voltaje
		convertToVoltaje(code);

		// Codificacion
		int space = separator;
		int yBefore = 0;
		int xBefore = 0;
		for (int i = 2; i < code.length; i++) {
			graphics.setColor(Color.black);

			int BkMenosDos = code[i - 2];
			int Bk = code[i];
			int Ck = Bk - BkMenosDos;
			codificatedCode[i - 2] = Ck;

			int x1 = space;
			int y = 0;
			int x2 = space += separator;
			if (Ck < 0)
				y = lower;
			else if (Ck == 0)
				y = middle;
			else if (Ck > 0)
				y = upper;

			// Graficamos los datos numericos
			int ex = space - (separator / 2);
			graphics.drawString(String.valueOf(Bk), Bk < 0 ? ex - 5 : ex, 40);
			graphics.drawString(String.valueOf(Ck), Ck < 0 ? ex - 5 : ex,
					height - 40);

			// Graficamos la linea horizontal
			graphics.setColor(Color.blue);
			graphics.drawLine(x1, y, x2, y);

			// Graficamos la linea vertical
			if (i == 2) {
				xBefore = x2;
				yBefore = y;
			} else {
				// graficar
				graphics.drawLine(xBefore, yBefore, x1, y);
				xBefore = x2;
				yBefore = y;
			}

		}

		System.out.print("Cadena codificada\t");
		printArray(codificatedCode);
	}

	/**
	 * Codifica una cadena por el metodo Duobinario Modificado con
	 * Precodificador
	 * <p>
	 * Creation date 26/04/2006 - 06:15:53 PM
	 * 
	 * @param code
	 *            Cadena a codificar
	 * @since 1.0
	 */
	private void duobinarioModificadoPrecodificador(int[] code) {
		String name = "DUOBINARIO MODIFICADO CON PRECODIFICADOR";
		graphics.drawString(name, (width - graphics.getFontMetrics()
				.stringWidth(name)) / 2, height - 10);
		System.out.print("Cadena a codificar\t");
		printArray(code);

		// Insertamos el primer byte al comienzo de la cadena
		int[] precodificado = new int[code.length + 2];
		precodificado[0] = Coder.DUOBINARIO_INIT_BIT;
		precodificado[1] = Coder.DUOBINARIO_SEC_BIT;

		// Precodificacion
		for (int i = 0; i < code.length; i++) {
			int Bk = code[i];
			int Ak = precodificado[i];

			// Operacion XOR
			if (Ak == Bk)
				precodificado[i + 2] = 0;
			else
				precodificado[i + 2] = 1;
		}

		// Convertimos de codigo a voltaje
		convertToVoltaje(precodificado);

		// Codificacion
		int space = separator;
		int yBefore = 0;
		int xBefore = 0;
		for (int i = 2; i < precodificado.length; i++) {
			graphics.setColor(Color.black);

			int AkMenosDos = precodificado[i - 2];
			int Ak = precodificado[i];
			int Ck = Ak - AkMenosDos;
			codificatedCode[i - 2] = Ck;

			int x1 = space;
			int y = 0;
			int x2 = space += separator;
			if (Ck < 0)
				y = lower;
			else if (Ck == 0)
				y = middle;
			else if (Ck > 0)
				y = upper;

			// Graficamos los datos numericos
			int ex = space - (separator / 2);
			graphics.drawString(String.valueOf(Ak), Ak < 0 ? ex - 5 : ex, 40);
			graphics.drawString(String.valueOf(Ck), Ck < 0 ? ex - 5 : ex,
					height - 40);

			// Graficamos la linea horizontal
			graphics.setColor(Color.blue);
			graphics.drawLine(x1, y, x2, y);

			// Graficamos la linea vertical
			if (i == 2) {
				xBefore = x2;
				yBefore = y;
			} else {
				// graficar
				graphics.drawLine(xBefore, yBefore, x1, y);
				xBefore = x2;
				yBefore = y;
			}

		}

		System.out.print("Cadena codificada\t");
		printArray(codificatedCode);
	}

	/**
	 * Codifica una cadena por el metodo AMI Bipolar, con polaridad alternante
	 * <p>
	 * Creation date 15/05/2006 - 09:32:09 AM
	 * 
	 * @param code
	 *            Cadena a codificar
	 * @since 1.0
	 */
	private void amiBipolar(int[] code) {
		String name = "AMI BIPOLAR";
		graphics.drawString(name, (width - graphics.getFontMetrics()
				.stringWidth(name)) / 2, height - 10);
		System.out.print("Cadena a codificar\t");
		printArray(code);

		// Codificacion
		int space = separator;
		int yBefore = 0;
		int xBefore = 0;
		int polarity = 1;
		for (int i = 0; i < code.length; i++) {
			graphics.setColor(Color.black);

			if (code[i] == 0) {
				// Cero
				codificatedCode[i] = 0;
			} else {
				// Uno
				codificatedCode[i] = polarity;
				polarity *= -1;
			}

			int x1 = space;
			int y = 0;
			int x2 = space += separator;
			if (codificatedCode[i] < 0)
				y = lower;
			else if (codificatedCode[i] == 0)
				y = middle;
			else if (codificatedCode[i] > 0)
				y = upper;

			// Graficamos los datos numericos
			int ex = space - (separator / 2);
			graphics.drawString(String.valueOf(code[i]), code[i] < 0 ? ex - 5
					: ex, 40);
			graphics.drawString(String.valueOf(codificatedCode[i]),
					codificatedCode[i] < 0 ? ex - 5 : ex, height - 40);

			// Graficamos la linea horizontal
			graphics.setColor(Color.blue);
			graphics.drawLine(x1, y, x2, y);

			// Graficamos la linea vertical
			if (yBefore == 0) {
				xBefore = x2;
				yBefore = y;
			} else {
				// graficar
				graphics.drawLine(xBefore, yBefore, x1, y);
				xBefore = x2;
				yBefore = y;
			}

		}

		System.out.print("Cadena codificada\t");
		printArray(codificatedCode);
	}

	/**
	 * Codifica una cadena por el metodo B8ZS
	 * <p>
	 * Creation date 26/04/2006 - 09:42:18 PM
	 * 
	 * @param code
	 *            Cadena a codificar
	 * @since 1.0
	 */
	private void b8zs(int[] code) {
		String name = "B8ZS";
		graphics.drawString(name, (width - graphics.getFontMetrics()
				.stringWidth(name)) / 2, height - 10);
		System.out.print("Cadena a codificar\t");
		printArray(code);

		// Primero codificamos a AMI Bipolar
		int space = separator;
		int yBefore = 0;
		int xBefore = 0;
		int polarity = 1;
		for (int i = 0; i < code.length; i++) {
			graphics.setColor(Color.black);

			if (haveZeros(i, 8, code)) {
				// Sustitucion de los 8 ceros

				// Preguntamos la polaridad del ultimo
				polarity = codificatedCode[i - 1];

				// Codificamos los tres primeros ceros
				codificatedCode[i] = 0;
				codificatedCode[i + 1] = 0;
				codificatedCode[i + 2] = 0;

				// Primera Violacion
				codificatedCode[i + 3] = polarity;
				polarity *= -1;

				// Senal Bipolar
				codificatedCode[i + 4] = polarity;

				// Cero intermedio
				codificatedCode[i + 5] = 0;

				// Segunda Violacion
				codificatedCode[i + 6] = polarity;
				polarity *= -1;

				// Senal Bipolar
				codificatedCode[i + 7] = polarity;
				polarity *= -1;

				// Graficamos los datos numericos
				for (int j = 0; j < 8; j++) {
					int x1 = space;
					int y = 0;
					int x2 = space += separator;
					if (codificatedCode[i + j] < 0)
						y = lower;
					else if (codificatedCode[i + j] == 0)
						y = middle;
					else if (codificatedCode[i + j] > 0)
						y = upper;

					// Violaciones y Bipolar
					graphics.setColor(Color.black);
					if (j == 3 || j == 6)
						graphics.drawString("V", x1 + 8, middle - 20);
					else if (j == 4 || j == 7)
						graphics.drawString("B", x1 + 8, middle - 20);

					// Graficamos los datos numericos
					int ex = space - (separator / 2);
					graphics.drawString(String.valueOf(code[i + j]),
							code[i] < 0 ? ex - 5 : ex, 40);
					graphics.drawString(String.valueOf(codificatedCode[i + j]),
							codificatedCode[i] < 0 ? ex - 5 : ex, height - 40);

					// Graficamos la linea horizontal
					graphics.setColor(Color.blue);
					graphics.drawLine(x1, y, x2, y);

					// Graficamos la linea vertical
					if (yBefore == 0) {
						xBefore = x2;
						yBefore = y;
					} else {
						// graficar
						graphics.drawLine(xBefore, yBefore, x1, y);
						xBefore = x2;
						yBefore = y;
					}
				}

				i += 7;
			} else {
				if (code[i] == 0) {
					// Cero
					codificatedCode[i] = 0;
				} else {
					// Uno
					codificatedCode[i] = polarity;
					polarity *= -1;
				}

				int x1 = space;
				int y = 0;
				int x2 = space += separator;
				if (codificatedCode[i] < 0)
					y = lower;
				else if (codificatedCode[i] == 0)
					y = middle;
				else if (codificatedCode[i] > 0)
					y = upper;

				// Graficamos los datos numericos
				int ex = space - (separator / 2);
				graphics.drawString(String.valueOf(code[i]),
						code[i] < 0 ? ex - 5 : ex, 40);
				graphics.drawString(String.valueOf(codificatedCode[i]),
						codificatedCode[i] < 0 ? ex - 5 : ex, height - 40);

				// Graficamos la linea horizontal
				graphics.setColor(Color.blue);
				graphics.drawLine(x1, y, x2, y);

				// Graficamos la linea vertical
				if (yBefore == 0) {
					xBefore = x2;
					yBefore = y;
				} else {
					// graficar
					graphics.drawLine(xBefore, yBefore, x1, y);
					xBefore = x2;
					yBefore = y;
				}
			}
		}

		System.out.print("Cadena codificada\t");
		printArray(codificatedCode);
	}

	/**
	 * Codifica una cadena por el metodo HDB3
	 * <p>
	 * Creation date 26/04/2006 - 09:44:43 PM
	 * 
	 * @param code
	 *            Cadena a codificar
	 * @since 1.0
	 */
	private void hdb3(int[] code) {
		String name = "HDB3";
		graphics.drawString(name, (width - graphics.getFontMetrics()
				.stringWidth(name)) / 2, height - 10);
		System.out.print("Cadena a codificar\t");
		printArray(code);

		// El -1 indica que no ha habido sustituciones
		int lastSustitution = -1;

		int yBefore = 0;
		int xBefore = 0;
		int polarity = 1;
		int space = separator;
		for (int i = 0; i < code.length; i++) {
			graphics.setColor(Color.black);
			// Sustitucion de los 4 ceros
			if (haveZeros(i, 4, code)) {
				// Impar, se reemplaza por 000V
				if (lastSustitution == -1 || lastSustitution % 2 != 0) {
					polarity *= -1;
					codificatedCode[i] = 0;
					codificatedCode[i + 1] = 0;
					codificatedCode[i + 2] = 0;
					// Violacion
					codificatedCode[i + 3] = polarity;
					polarity *= -1;

					// Graficamos los datos numericos
					for (int j = 0; j < 4; j++) {
						int x1 = space;
						int y = 0;
						int x2 = space += separator;
						if (codificatedCode[i + j] < 0)
							y = lower;
						else if (codificatedCode[i + j] == 0)
							y = middle;
						else if (codificatedCode[i + j] > 0)
							y = upper;

						// Violaciones y Bipolar
						graphics.setColor(Color.black);
						if (j == 3)
							graphics.drawString("V", x1 + 8, middle - 20);

						// Graficamos los datos numericos
						int ex = space - (separator / 2);
						graphics.drawString(String.valueOf(code[i + j]),
								code[i] < 0 ? ex - 5 : ex, 40);
						graphics.drawString(String.valueOf(codificatedCode[i
								+ j]), codificatedCode[i] < 0 ? ex - 5 : ex,
								height - 40);

						// Graficamos la linea horizontal
						graphics.setColor(Color.blue);
						graphics.drawLine(x1, y, x2, y);

						// Graficamos la linea vertical
						if (yBefore == 0) {
							xBefore = x2;
							yBefore = y;
						} else {
							// graficar
							graphics.drawLine(xBefore, yBefore, x1, y);
							xBefore = x2;
							yBefore = y;
						}
					}
				}
				// Par, se reemplaza por B00V
				else {
					// Bipolar
					codificatedCode[i] = polarity;
					codificatedCode[i + 1] = 0;
					codificatedCode[i + 2] = 0;
					// Violacion
					codificatedCode[i + 3] = polarity;
					polarity *= -1;

					// Graficamos los datos numericos
					for (int j = 0; j < 4; j++) {
						int x1 = space;
						int y = 0;
						int x2 = space += separator;
						if (codificatedCode[i + j] < 0)
							y = lower;
						else if (codificatedCode[i + j] == 0)
							y = middle;
						else if (codificatedCode[i + j] > 0)
							y = upper;

						// Violaciones y Bipolar
						graphics.setColor(Color.black);
						if (j == 0)
							graphics.drawString("B", x1 + 8, middle - 20);
						else if (j == 3)
							graphics.drawString("V", x1 + 8, middle - 20);

						// Graficamos los datos numericos
						int ex = space - (separator / 2);
						graphics.drawString(String.valueOf(code[i + j]),
								code[i] < 0 ? ex - 5 : ex, 40);
						graphics.drawString(String.valueOf(codificatedCode[i
								+ j]), codificatedCode[i] < 0 ? ex - 5 : ex,
								height - 40);

						// Graficamos la linea horizontal
						graphics.setColor(Color.blue);
						graphics.drawLine(x1, y, x2, y);

						// Graficamos la linea vertical
						if (yBefore == 0) {
							xBefore = x2;
							yBefore = y;
						} else {
							// graficar
							graphics.drawLine(xBefore, yBefore, x1, y);
							xBefore = x2;
							yBefore = y;
						}
					}
				}

				i += 3;
				lastSustitution = 0;
			} else {
				if (code[i] == 0) {
					// Cero
					codificatedCode[i] = 0;
				} else {
					// Uno
					codificatedCode[i] = polarity;
					polarity *= -1;
					if (lastSustitution != -1)
						lastSustitution++;
				}

				int x1 = space;
				int y = 0;
				int x2 = space += separator;
				if (codificatedCode[i] < 0)
					y = lower;
				else if (codificatedCode[i] == 0)
					y = middle;
				else if (codificatedCode[i] > 0)
					y = upper;

				// Graficamos los datos numericos
				int ex = space - (separator / 2);
				graphics.drawString(String.valueOf(code[i]),
						code[i] < 0 ? ex - 5 : ex, 40);
				graphics.drawString(String.valueOf(codificatedCode[i]),
						codificatedCode[i] < 0 ? ex - 5 : ex, height - 40);

				// Graficamos la linea horizontal
				graphics.setColor(Color.blue);
				graphics.drawLine(x1, y, x2, y);

				// Graficamos la linea vertical
				if (yBefore == 0) {
					xBefore = x2;
					yBefore = y;
				} else {
					// graficar
					graphics.drawLine(xBefore, yBefore, x1, y);
					xBefore = x2;
					yBefore = y;
				}
			}
		}

		System.out.print("Cadena codificada\t");
		printArray(codificatedCode);
	}

	/**
	 * Verifica si el codigo tiene la cantidad de ceros indicada
	 * <p>
	 * Creation date 15/05/2006 - 09:48:44 AM
	 * 
	 * @param start
	 *            Posicion de inicio
	 * @param zeros
	 *            Cantidad de ceros a buscar
	 * @param code
	 *            Codigo en donde buscar
	 * @return True si el codigo tiene la cantidad de ceros indicada
	 * @since 1.0
	 */
	private boolean haveZeros(int start, int zeros, int[] code) {
		if (start + zeros > code.length)
			return false;

		for (int i = start; i < start + zeros; i++) {
			if (code[i] != 0)
				return false;
		}
		return true;
	}

	/**
	 * Retorna el codigo codificado por la clase
	 * <p>
	 * Creation date 26/04/2006 - 08:42:03 AM
	 * 
	 * @return Codigo codificado
	 * @since 1.0
	 */
	public int[] getCodificatedCode() {
		return codificatedCode;
	}

	/**
	 * Convierte el codigo a voltaje, las ocurrencias de 0 las convierte en -1
	 * <p>
	 * Creation date 14/05/2006 - 09:40:38 PM
	 * 
	 * @param code
	 *            Codigo a convertir
	 * @since 1.0
	 */
	private void convertToVoltaje(int[] code) {
		for (int i = 0; i < code.length; i++) {
			if (code[i] == 0)
				code[i] = -1;
		}
	}

	/**
	 * Imprime en consola el array recibido por parametro
	 * <p>
	 * Creation date 14/05/2006 - 09:41:17 PM
	 * 
	 * @param code
	 *            Array a imprimir
	 * @since 1.0
	 */
	private void printArray(int[] code) {
		for (int i = 0; i < code.length; i++) {
			System.out.print(code[i] + " ");
		}
		System.out.println("");
	}

	/**
	 * Muestra la imagen en una ventana
	 * <p>
	 * Creation date 27/04/2006 - 10:20:34 AM
	 * 
	 * @since 1.0
	 */
	public void showImage() {
		add(new JLabel(new ImageIcon(image)));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(width + 100, height + 100);
		setVisible(true);
	}

	/**
	 * Retorna la imagen para ser enviada como respuesta
	 * <p>Creation date 27/04/2006 - 09:51:47 AM
	 *
	 * @return		Imagen
	 * @since 1.0
	 */
	public Image getImage() {
		return image;
	}

}

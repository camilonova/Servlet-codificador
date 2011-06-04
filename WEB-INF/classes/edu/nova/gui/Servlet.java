package edu.nova.gui;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Acme.JPM.Encoders.GifEncoder;
import edu.nova.log.Coder;

/**
 * Servlet que codifica una cadena binaria utilizando los metodos listados
 * @author Camilo Nova
 * @version 1.0
 */
public class Servlet extends HttpServlet {

	/**
	 * Inicializa el Servlet
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			java.io.IOException {

		String webCode = request.getParameter("code");
		String webType = request.getParameter("type");

		String[] tokens = webCode.split(",");
		int code[] = new int[tokens.length];
		int codeType = 0;

		for (int i = 0; i < tokens.length; i++) {
			code[i] = Integer.parseInt(tokens[i]);
		}
		if (webType.equals("duo"))
			codeType = Coder.DUOBINARIO;
		if (webType.equals("duoPre"))
			codeType = Coder.DUOBINARIO_PRECODIFICADOR;
		if (webType.equals("duoMod"))
			codeType = Coder.DUOBINARIO_MODIFICADO;
		if (webType.equals("duoModPre"))
			codeType = Coder.DUOBINARIO_MODIFICADO_PRECODIFICADOR;
		if (webType.equals("ami"))
			codeType = Coder.AMI_BIPOLAR;
		if (webType.equals("b8zs"))
			codeType = Coder.B8ZS;
		if (webType.equals("hdb3"))
			codeType = Coder.HDB3;

		ServletOutputStream bufferSalida = response.getOutputStream();
		Coder coder = new Coder(code, codeType);
		GifEncoder encoder = new GifEncoder(coder.getImage(),
				bufferSalida);

		response.setContentType("image/gif");
		encoder.encode();
		bufferSalida.flush();
		bufferSalida.close();

	}

	public String getServletInfo() {
		return "Servlet de codificacion - Camilo Nova - 2006";
	}

	/**
	 * Metodo principal para ejecutar el programa sin servlet
	 * <p>Creation date 27/04/2006 - 07:11:28 AM
	 *
	 * @param args
	 * @since 1.0
	 */
	public static void main(String[] args) {
		//int[] code = {1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,1,0};
		int[] code = {1,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,0};
		int type = Coder.HDB3;
		
		Coder coder = new Coder(code, type);
		coder.showImage();
	}
}

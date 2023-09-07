package ia_practica_1;

import java.util.ArrayList;
import java.util.Scanner;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;

public class Principal {
	static Scanner scanner = new Scanner(System.in);
	static MemoriaArbol memoria = new MemoriaArbol();

	public static void main(String[] args) {
		Arbol arbol = inicializa_arbol();
		menu(arbol);
	}

	public static void menu(Arbol arbol) {

		// String opcion
		String opciones[] = { "\nElige la opción que gustes:", "1.- Ver árbol actual.",
				"2.- Ver solo bebidas en sistema.", "3.- Ingresar bebidas al sistema.",
				"4.- Características de una bebida.", "5.- Buscar una bebida en específico.", "6.- Guardar árbol.", "7.- Salir." };

		while (true) {
			for (String str : opciones)
				System.out.println(str);
			System.out.print("Opción: ");
			String opcion = scanner.nextLine();
			System.out.println("");
			switch (opcion) {
			case "1": {
				// Ver arbol actual.
				arbol.imprimir();
				break;
			}
			case "2": {
				// Ver solo bebidas en sistema.
				arbol.imprimirListaBebidas(false);
				break;
			}
			case "3": {
				// Ingresar bebidas al sistema.
				arbol.agregarBebida();
				break;
			}
			case "4": {
				// Características de una bebida.
				arbol.imprimirListaBebidas(true);
				break;
			}
			case "5": {
				// Características de una bebida.
				arbol.busquedaDeUnoEnParticular();
				break;
			}
			case "6": {
				// Opción para guardar el arbol en un archivo XML.
				memoria.creaXMLDeArbolYSalva(arbol);
				break;
			}
			case "7": {
				// Opción para salir del programa.
				System.out.println("\n¡Muchas gracias! Hasta luego.\n");
				return;
				// break;
			}
			default:
				// Opción en caso de respuesta erronea
				System.out.println("Respuesta Erronea.\n");
				break;
			}

		}

	}

	/**
	 * @return
	 */
	public static Arbol inicializa_arbol() {
		Arbol arbol = new Arbol();

		Nodo tempRaiz = memoria.leeYReconstruyeArbol();
		if (tempRaiz != null) {
			arbol.raiz = tempRaiz;
		} else {
			String v1 = "Alcohólica";
			String v2 = "De uvas";
			String v3 = "Caliente";
			String v4 = "Vino";
			String v5 = "Poco Alcohol";
			String v6 = "Licor";
			String v7 = "Cerveza";
			String v8 = "Té";
			String v9 = "Limonada";
			String v10 = "Legal";
			String v11 = "Tóxico";
			String v12 = "Drácula lo consume";
			String v13 = "Sangre";
			String v14 = "Agua bendita";
			String v15 = "Ayahuasca";
			String v16 = "Es sagrado";
			String v17 = "Cloro";

			// Agregar preguntas
			arbol.agregarNodo(null, v1, true, false);
			arbol.agregarNodo(v1, v2, true, true);
			arbol.agregarNodo(v1, v3, true, false);
			arbol.agregarNodo(v2, v5, true, false);
			arbol.agregarNodo(v3, v10, true, true);
			arbol.agregarNodo(v3, v11, true, false);
			arbol.agregarNodo(v10, v12, true, true);
			arbol.agregarNodo(v12, v16, true, false);

			// Agregar bebidas de prueba
			// arbol.agregarNodo(v2, v4, true, true);
			// arbol.agregarNodo(v5, v7, true, true);
			// arbol.agregarNodo(v5, v6, true, false);
			// arbol.agregarNodo(v16, v14, true, true);
			// arbol.agregarNodo(v16, v8, true, false);
			// arbol.agregarNodo(v10, v15, true, false);
			// arbol.agregarNodo(v12, v13, true, true);
			// arbol.agregarNodo(v11, v17, true);
			// arbol.agregarNodo(v11, v9, false);

		}

		return arbol;
	}

}

/*
 * El nodo es binario.
 */
class Nodo {
	Nodo padre;
	String valor;
	boolean pregunta; // Si no es pregunta es bebida.
	Nodo rama_si;
	Nodo rama_no;

	public Nodo(Nodo padre, String valor, boolean pregunta, Nodo rama_si, Nodo rama_no) {
		super();
		this.padre = padre;
		this.valor = valor;
		this.pregunta = pregunta;
		this.rama_si = rama_si;
		this.rama_no = rama_no;
	}

	public String preguntizar() {
		return "¿" + this.valor + "?";
	}

}

class Arbol {
	Nodo raiz;

	public void agregarNodo(String valor_padre, String valor, boolean pregunta, boolean si_no) {
		if (valor_padre == null) {
			this.raiz = new Nodo(null, valor, pregunta, null, null);
			return;
		}

		Nodo nodo_padre = busqueda(valor_padre, null);
		if (nodo_padre == null) {
			System.out.println("Búsqueda de nodo.valor: " + valor_padre + ", fallida.");
			return;
		}

		Nodo nueva_rama = new Nodo(nodo_padre, valor, pregunta, null, null);
		if (si_no) {
			nodo_padre.rama_si = nueva_rama;
		} else {
			nodo_padre.rama_no = nueva_rama;
		}

	}

	public void imprimirListaBebidas(boolean solicitarDatos) {
		ArrayList<Nodo> listaBebidas = new ArrayList<Nodo>();
		//busquedaDeHojasBebidas(listaBebidas, raiz);
		busquedaDeHojasBebidasPorAnchura(listaBebidas, raiz);

		if (listaBebidas.size() == 0) {
			System.out.println("");
			System.out.println("No hay bebidas registradas.");
			System.out.println("");
			return;
		}

		System.out.println("");
		for (int x = 0; x < listaBebidas.size(); x++) {
			Nodo nodo = listaBebidas.get(x);
			System.out.println("" + (x) + ") " + nodo.valor);
		}
		System.out.println("");

		if (!solicitarDatos)
			return;

		// Aquí escaneamos la bebida solicitada aprovechando que está en la lista.

		Scanner scanner = Principal.scanner;

		System.out.print("¿Cuál bebida le interesa?: ");

		String respuesta = scanner.nextLine().trim();

		if (!isNumeric(respuesta)) {
			System.out.println("Opción Erronea: Ingrese solo números.");
			return;
		}
		int opcion = Integer.parseInt(respuesta);
		if (opcion < 0 || opcion >= listaBebidas.size()) {
			System.out.println("Opción Erronea: Ingrese una opción válida.");
			return;
		}

		Nodo elegido = listaBebidas.get(opcion);

		imprimeCaracteristicasNodo(elegido);

	}

	private void imprimeCaracteristicasNodo(Nodo nodo) {
		ArrayList<Nodo> busquedaPaAtras = new ArrayList<Nodo>();

		while (nodo != null) {
			busquedaPaAtras.add(nodo);
			nodo = nodo.padre;
		}
		System.out.println("");
		
		for (int x = busquedaPaAtras.size() - 1; x >= 0; x--) {
			Nodo tempNodo = busquedaPaAtras.get(x);
			String acasoEs = "";
			
			if(x != 0) {
				if(tempNodo.rama_si == busquedaPaAtras.get(x-1)) {
					acasoEs = "(Si)";
				}else {
					acasoEs = "(No)";
				}	
			}
			
			
			if (x != 0) {
				System.out.print("" + tempNodo.valor + acasoEs + " -> ");
			} else {
				System.out.print("" + tempNodo.valor);
			}
		}
		System.out.println("");
	}
	
	private static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	// O dang se me olvidó que la búsqueda era por anchura....
	// Dejo este código porque me quedó bonito
	private void busquedaDeHojasBebidas(ArrayList<Nodo> listaBebidas, Nodo nodo) {
		if (!nodo.pregunta) {
			// Reviso que no sea pregunta y que sea bebida.
			listaBebidas.add(nodo);
		}

		if (nodo.rama_si != null)
			busquedaDeHojasBebidas(listaBebidas, nodo.rama_si);
		if (nodo.rama_no != null)
			busquedaDeHojasBebidas(listaBebidas, nodo.rama_no);
	}

	//Listo aquí lo resuelvo por anchura....
	private void busquedaDeHojasBebidasPorAnchura(ArrayList<Nodo> listaBebidas, Nodo nodo) {
		ColaNodos cola = new ColaNodos();
		Nodo checkNodo = nodo;
		while (checkNodo != null) {
			if (!checkNodo.pregunta) {
				// Reviso que no sea pregunta y que sea bebida.
				listaBebidas.add(checkNodo);
			}
			if (checkNodo.rama_si != null) {
				cola.formar(checkNodo.rama_si);
			}
			if (checkNodo.rama_no != null) {
				cola.formar(checkNodo.rama_no);
			}
			checkNodo = cola.siguiente();
		}
	}

	public void agregarBebida() {
		if (this.raiz == null) {
			System.out.println("¡Arbol vacío!");
			return;
		}

		Scanner scanner = Principal.scanner;
		System.out.println("Iniciamos Recorrido Arbol");

		Nodo nodo = this.raiz;

		while (true) {

			if (nodo.pregunta) {
				System.out.println(nodo.preguntizar());
				while (true) {
					System.out.print("Respuesta (y/n): ");
					String respuesta = scanner.nextLine().trim();
					if (!respuesta.equals("y") && !respuesta.equals("n")) {
						System.out.println("Error: Valores admitidos (y/n).");
					} else {
						if (respuesta.equals("y")) {
							if (nodo.rama_si == null) {
								nodo.rama_si = creaNodoBebida(nodo);
								System.out.println("Bebida \'" + nodo.rama_si.valor + "\' agregada.");
								return;
							} else {
								nodo = nodo.rama_si;
							}
						} else {
							if (nodo.rama_no == null) {
								nodo.rama_no = creaNodoBebida(nodo);
								System.out.println("Bebida \'" + nodo.rama_no.valor + "\' agregada.");
								return;

							} else {
								nodo = nodo.rama_no;
							}

						}

						break;
					}
				}
			} else {
				System.out.println("Bebida existente: " + nodo.valor);
				System.out.print("¿Desea sobreescribirla?: (y/n)");
				String respuesta;
				while (true) {
					respuesta = scanner.nextLine().trim();
					if (!respuesta.equals("y") && !respuesta.equals("n")) {
						System.out.println("Error: Valores admitidos (y/n).");
					} else {
						break;
					}
				}

				if (respuesta.equals("y")) {

					do {
						respuesta = scanner.nextLine().trim();
						if (respuesta.length() == 0) {
							System.out.println("Error: Ingrese un valor no vacío.");
						} else {
							nodo.valor = respuesta;
							System.out.println("Valor original editado.");
							return;
						}
					} while (respuesta.length() == 0);

				} else {
					System.out.println("Valor original no editado.");
					return;
				}

			}

		}

	}

	private Nodo creaNodoBebida(Nodo padre) {
		Scanner scanner = Principal.scanner;
		String nombre = "";

		while (true) {
			System.out.print("Nombre bebida: ");
			nombre = scanner.nextLine().trim();
			System.out.println("");
			if (nombre.length() == 0) {
				System.out.println("Error: Ingrese un valor no vacío.");
			} else {
				break;
			}
		}

		Nodo nodo = new Nodo(padre, nombre, false, null, null);
		return nodo;
	}
	
	
	public void busquedaDeUnoEnParticular() {
				
		Scanner scanner = Principal.scanner;
		
		String respuesta = "";
		
		while(respuesta.length() == 0) {
			System.out.print("Ingrese valor a buscar: ");
			respuesta = scanner.nextLine().trim();
			System.out.println("");
			if(respuesta.length() == 0) {
				System.out.println("Error: Ingrese un valor no vacío.");
			}
		}
		
		System.out.println("Iniciando búsqueda de valor: " + respuesta);
		
		ArrayList<Nodo> mapa_recorrido = new ArrayList<Nodo>();
		
		Nodo nodoMeta = busqueda(respuesta, mapa_recorrido);
		
		System.out.println("");
		if(nodoMeta == null) {
			System.out.println("Bebida no encontrada.");
		}else {
			
			System.out.println("Bebida encontrada.");
			imprimeCaracteristicasNodo(nodoMeta);
		}
		
		System.out.println("El camino de nodos siendo analizados fue: ");
		for(int x = 0; x < mapa_recorrido.size(); x++) {
			Nodo nodo = mapa_recorrido.get(x);
			if(x != mapa_recorrido.size() - 1) {
				System.out.print("" + nodo.valor + " -> ");	
			}else {
				System.out.print("" + nodo.valor);
			}
			
		}

		System.out.println("");
		
	}

	/*
	 * Algoritmo de búsqueda por anchura implementado con colas.
	 */
	private Nodo busqueda(String valor_nodo, ArrayList<Nodo> nodosRecorridos) {
		if (this.raiz == null) {
			System.out.println("¡Arbol vacío!");
			return null;
		}

		ColaNodos cola = new ColaNodos();
		Nodo nodo_actual = raiz;
		
		while (nodo_actual != null) {
			// Se encontró el valor
			if(nodosRecorridos != null)nodosRecorridos.add(nodo_actual);

			if (nodo_actual.valor.toLowerCase().equals(valor_nodo.toLowerCase()))
				return nodo_actual;
			// No se encontró el valor, agrego posibles hijos a la cola.
			if (nodo_actual.rama_si != null)
				cola.formar(nodo_actual.rama_si);
			if (nodo_actual.rama_no != null)
				cola.formar(nodo_actual.rama_no);
			nodo_actual = cola.siguiente();
		}

		// No se encontró el valor
		return null;
	}

	public void imprimir() {
		if (this.raiz == null) {
			System.out.println("Impresión: Arbol vacío.");
			return;
		}
		ArrayList<String> mapeo = new ArrayList<String>();
		mapeo_transversal(mapeo, this.raiz, "", false, 0);

		mapeo.forEach(nodo_valor -> {
			System.out.println(nodo_valor);
		});
	}

	/*
	 * Lo necesito para imprimir el arbol... Es en esencia una búsqueda por
	 * profundidad pero voy guardando los nodos visitados
	 */
	private void mapeo_transversal(ArrayList<String> mapeo, Nodo nodo, String padding, boolean izq, int nivel) {
		/*
		 * Es increible que hacer un arbol con sus búsquedas me toma 30 minutos pero
		 * lograr que se imprima bonito me tomó 3 horas de debugueo...
		 */
		if (nodo.padre == null) {
			mapeo.add(nodo.valor);
		} else {
			if (izq) {
				if (padding.charAt(padding.length() - 1) == '│') {
					mapeo.add(padding.substring(0, padding.length() - 1) + "├──Si:" + nodo.valor);
				} else {
					mapeo.add(padding + "├──Si:" + nodo.valor);
				}
			} else {
				mapeo.add(padding + "└──No:" + nodo.valor);
			}

		}
		if (nodo.rama_si != null) {
			String padding_basico = padding + generarBlancos(4, true);
			mapeo_transversal(mapeo, nodo.rama_si, padding_basico, true, nivel + 1);
		}

		if (nodo.rama_no != null) {
			String padding_basico = padding + generarBlancos(4, false);
			mapeo_transversal(mapeo, nodo.rama_no, padding_basico, false, nivel + 1);
		}
	}

	private String generarBlancos(int cantidad, boolean linea_abajo) {
		// System.out.println("" + n);
		int n = cantidad;
		String ret = "";
		for (int x = 0; x < n; x++) {
			ret += " ";
		}
		if (linea_abajo)
			return ret + "│";
		return ret;
	}
}

class ColaNodos {
	private ArrayList<Nodo> cola = new ArrayList<Nodo>();

	/*
	 * Agrega un valor a la cola.
	 */
	public void formar(Nodo nodo) {
		if (nodo == null) { // Mera precaución.
			System.out.println("No puede haber nulls en la cola.");
			return;
		}
		cola.add(nodo);
	}

	/*
	 * Regresa el siguiente valor y lo elimina de la cola. Si no hay valor siguiente
	 * en la cola, regresa Null.
	 */
	public Nodo siguiente() {
		if (this.cola.size() == 0)
			return null;
		Nodo nodo = this.cola.get(0);
		this.cola.remove(0);
		return nodo;
	}

	/*
	 * Reinicia la cola.
	 */
	public void vaciar_cola() {
		this.cola = new ArrayList<Nodo>();
	}
}

class MemoriaArbol {

	final private String archivoArbol = "arbol.xml";

	// Regresa el nodo raiz del arbol leido
	public Nodo leeYReconstruyeArbol() {
		File xmlFile = new File(archivoArbol);
		if (!xmlFile.exists()) {
			System.out.println("XML Arbol no encontrado, iniciando desde cero.");
			return null;
		}

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			System.out.println("Archivo \'" + archivoArbol + "\' encontrado. Reconstruyendo arbol.");
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult console = new StreamResult(System.out);
			transformer.transform(source, console);
			System.out.println("");
			System.out.println("");

			Nodo raiz = null;

			NodeList nodeList = doc.getChildNodes();
			Node nodeArbol = nodeList.item(0);
			NodeList nodos = nodeArbol.getChildNodes();

			for (int i = 0; i < nodos.getLength(); i++) {
				Node node = nodos.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					/*
					 * NamedNodeMap atributos = node.getAttributes(); for(int x = 0; x <
					 * atributos.getLength(); x++) { Attr attr = (Attr) atributos.item(x);
					 * System.out.println("" + attr.getNodeName() + " " + attr.getNodeValue()); }
					 */
					raiz = reconstruccionDeArbol(node, null);
				}
			}

			// raiz = reconstruccionDeArbol(raiz, archivoArbol, false)

			return raiz;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}

	private Nodo reconstruccionDeArbol(Node node, Nodo padre) {
		NamedNodeMap atributos = node.getAttributes();

		String strPregunta = atributos.getNamedItem("Pregunta").getNodeValue();
		// String strRama = atributos.getNamedItem("Rama").getNodeValue();
		String valor = atributos.getNamedItem("Valor").getNodeValue();

		boolean pregunta = strPregunta.equals("True") ? true : false;

		Nodo nodo = new Nodo(padre, valor, pregunta, null, null);

		NodeList nodos = node.getChildNodes();

		for (int i = 0; i < nodos.getLength(); i++) {
			Node tempNode = nodos.item(i);
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				NamedNodeMap tempAtributo = tempNode.getAttributes();
				String tempRama = tempAtributo.getNamedItem("Rama").getNodeValue();
				if (tempRama.equals("Si")) {
					nodo.rama_si = reconstruccionDeArbol(tempNode, nodo);
				} else {
					nodo.rama_no = reconstruccionDeArbol(tempNode, nodo);
				}

			}
		}

		return nodo;
	}

	public void creaXMLDeArbolYSalva(Arbol arbol) {
		Nodo raiz = arbol.raiz;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			Element rootElement = doc.createElement("Arbol");
			doc.appendChild(rootElement);
			rootElement.appendChild(navegaArbolYRegresaHijo(doc, raiz, "Raiz"));

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult console = new StreamResult(System.out);
			StreamResult file = new StreamResult(new File(archivoArbol));
			transformer.transform(source, console);
			transformer.transform(source, file);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Element navegaArbolYRegresaHijo(Document doc, Nodo nodo, String ramaPadre) {
		Element element = crearElemento(doc, nodo, ramaPadre);
		if (nodo.rama_si != null) {
			element.appendChild(navegaArbolYRegresaHijo(doc, nodo.rama_si, "Si"));
		}
		if (nodo.rama_no != null) {
			element.appendChild(navegaArbolYRegresaHijo(doc, nodo.rama_no, "No"));
		}

		return element;
	}

	private static Element crearElemento(Document doc, Nodo nodo, String ramaPadre) {
		Element element = doc.createElement("Nodo");
		element.setAttribute("Rama", ramaPadre);
		element.setAttribute("Valor", nodo.valor);
		element.setAttribute("Pregunta", (nodo.pregunta) ? "True" : "False");

		return element;
	}

}

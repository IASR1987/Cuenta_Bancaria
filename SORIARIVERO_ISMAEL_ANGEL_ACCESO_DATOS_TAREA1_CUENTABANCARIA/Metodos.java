package tareaLarga;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;

public class Metodos {
	static Scanner teclado = new Scanner(System.in);
	
	 /**
     * Recupera el último ID utilizado en el archivo especificado.
     *
     * @param ruta La ruta del archivo para comprobar el último ID.
     * @param longitudLinea La longitud de cada línea en el archivo.
     * @return El último ID utilizado, o -1 si no existe ninguno.
     */
	public static int ultimoId(String ruta, int longitudLinea) {
		
		//variavle para guardar el id, en -1 indica id no encontrado
		int ultimoId=-1;
		
		try(RandomAccessFile aleatorio= new RandomAccessFile(ruta,"rw");){
			
			//el ultimo id es de la division del total de extension del texto entre la longitud de la linea
			ultimoId=(int) (aleatorio.length()/longitudLinea);
			
			//cerramos aleatorio
			aleatorio.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ultimoId;
		
	}
	
	 /**
     * Comprueba si un ID dado existe en el archivo especificado.
     *
     * @param id El ID a comprobar.
     * @param longitudFila La longitud de cada fila en el archivo.
     * @param ruta La ruta del archivo a comprobar.
     * @return Un arreglo bidimensional que contiene el estado del ID y la posición en el archivo.
     */
	public static int[][] comprobarID(int id, int longitudFila, String ruta) {
		
		//creamos el arrayas
		int[][] idLista = new int[1][2];
		
		//boolean que dice si se ha encontrado
		int idEncontrado=-1;//id no encontrado
		
		//posicion del id en el archivo
		int posicion=0;
		
		//guardamos los valores
		idLista[0][0]=idEncontrado;
		idLista[0][1]=posicion;
		
		//abrimos lel flujo
		try(RandomAccessFile aleatorio = new RandomAccessFile(ruta,"rw")){
		
			//bucle que se realiza hasta que se lea todo el fichero
			while(aleatorio.length()>aleatorio.getFilePointer()) {
				//colocamos en la posicion
				aleatorio.seek(posicion);
				
				//si el id introducido por usuario coincide con el leido en el fichero
				if(id==aleatorio.readInt()) {
					
					//variable de control
					idEncontrado=1;
					
					//guardamos los valores
					idLista[0][0]=idEncontrado;
					idLista[0][1]=posicion;
					break;
					
				//sino coincide	
				}else {
					
					//pasamos a la siguiente linea
					posicion+=longitudFila;
				}
			}
			
			//cerramos el flujo 
			aleatorio.close();
		} catch (EOFException e) {
			// TODO Auto-generated catch block
			System.out.println("Has introducido un id incorrecto que no se corresponde a ninguna cuenta registrada.");
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return idLista;
	}
	
	/**
     * Comprueba el estado de actividad de un ID dado en el archivo especificado.
     *
     * @param id El ID a comprobar.
     * @param longitudFila La longitud de cada fila en el archivo.
     * @param ruta La ruta del archivo a comprobar.
     * @return Un arreglo bidimensional que contiene el estado del ID y la posición en el archivo, la actividad.
     */
	public static int[][] comprobarIdActividad(int id, int longitudFilaCuentas, String rutaCuentas) {
		
		//generamos ela arrarys
		int[][] idLista = new int[1][3];
		
		//boolean que dice si se ha encontrado
		int idEncontrado=-1;//id no encontrado
		
		//posicion de los id dentro del fichero es 0, los siguites +153
		int posicion=0;
		
		
		//Actividad se valorara -1 inactiva 1 activa
		int actividad =-1;
		
		//guardamos los valores
		idLista[0][0]=idEncontrado;
		idLista[0][1]=posicion;
		idLista[0][2]=actividad;
		
		//abrimos flujo
		try(RandomAccessFile aleatorio = new RandomAccessFile(rutaCuentas,"rw")){
			
			//bucle se realiza todo el documento
			while(aleatorio.length()>aleatorio.getFilePointer()) {
				//colocamos en la posicion
				aleatorio.seek(posicion);
				
				//damos valor al id
				int newId=aleatorio.readInt();
				
				//comparamos con el obtenido por el usuario
				//si son iguales
				if(newId==id) {
					
					//guardamos los valores
					idLista[0][0]=newId;
					
					//posicion deonde se encuentra el comienzo de la linea del id
					idLista[0][1]=posicion;
					
					//si id es correcto debemos ir hacia estado y comprobar la actividad
					aleatorio.seek(posicion+152);
					
					//si es True es q es activa la cuenta
					//faalse guardara -1 por defecto
					if(aleatorio.readBoolean()){
						//boolean es true, por lo que debe pasar 1
						idLista[0][2]=1;
					}
					
					break;
					
				}else {
					//sino coincide el id, avanzamos al siguiente
					posicion+=longitudFilaCuentas;
				}
			}
			
			//cerramos el flujo
			aleatorio.close();
		} catch(EOFException e){
			System.out.println("Id no encontrado");
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return idLista;
	}
	
    /**
     * Elimina una cuenta estableciendo su ID a -1 y vaciando los demás campos.
     *
     * @param posicion La posición en el archivo donde se encuentra la cuenta.
     * @param ruta La ruta del archivo donde se encuentra la cuenta.
     */
	public static void borrarCuenta(int posicion, String ruta) {
		try(RandomAccessFile aleatorio = new RandomAccessFile(ruta, "rw")){
			//nos colocamos en la posicion que hemos conseguido en el arrays
			aleatorio.seek(posicion);
				
			//cambiamos los valores
			//id a -1
			aleatorio.writeInt(-1);
			
			//nombre a vacio
			StringBuffer buffer = new StringBuffer("");
			buffer.setLength(50);
			
			aleatorio.writeChars(buffer.toString());
			
			//tipodeCuenta
			StringBuffer buffer1 = new StringBuffer("");
			buffer1.setLength(20);
			
			aleatorio.writeChars(buffer1.toString());
			
			//no se puede dejar double o boolean vacios
			//double a 0
			aleatorio.writeDouble(0);
			
			//valor activo/inactivo pasa a false
			aleatorio.writeBoolean(false);
			
			//cerramos el flujo
			aleatorio.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
     * Guarda una nueva cuenta en el archivo especificado.
     *
     * @param ruta La ruta del archivo donde se guardará la cuenta.
     * @param id El ID de la cuenta.
     * @param nombre El nombre del titular de la cuenta.
     * @param tipoCuenta El tipo de cuenta.
     * @param precio El saldo inicial de la cuenta.
     * @param estado El estado de la cuenta (activa/inactiva).
     */
	public static void guardarCuenta(String ruta, int id, String nombre, String tipoCuenta, double precio, boolean estado) {
		
		//abrimos el flujo
		try(RandomAccessFile aleatorio = new RandomAccessFile(ruta,"rw")){
			
			//guardaremos en la última posición del fichero
			aleatorio.seek(aleatorio.length());
			
			//guardamos la ID
			aleatorio.writeInt(id);
			
			//guardamos el nombre
			StringBuffer buffer= new StringBuffer(nombre);
			buffer.setLength(50);//longitud dada
			aleatorio.writeChars(buffer.toString());
			
			//guardamos el tipoCuenta
			buffer= new StringBuffer(tipoCuenta);
			buffer.setLength(20);//longitud dada
			aleatorio.writeChars(buffer.toString());
			
			aleatorio.writeDouble(precio);
			
			aleatorio.writeBoolean(estado);
			
			//cerramos el flujo
			aleatorio.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	  /**
     * Lee y muestra todas las cuentas almacenadas en el archivo.
     *
     * @param ruta La ruta del archivo donde se encuentran las cuentas.
     */
	public static void leerCuentas(String ruta) {
		
		//guardaremos lo que se mostrara por pantalla
		String reporte;
		
		//abrimos flujo
		try(RandomAccessFile aleatorio = new RandomAccessFile(ruta, "rw")){
			
			//leera todo el document
			while(aleatorio.getFilePointer()<aleatorio.length()) {

				//se guarda id
				int id= aleatorio.readInt();
				
				//se guarda nombre
				StringBuilder nombre = new StringBuilder();
				for(int i=0; i<50; i++) {
					nombre.append(aleatorio.readChar());
				}
				
				//se guarda tipoCuenta
				StringBuilder tipoCuenta = new StringBuilder();
				for(int i=0; i<20; i++) {
					tipoCuenta.append(aleatorio.readChar());
				}
				
				//se guarda el saldo
				double saldo = aleatorio.readDouble();
				
				//se guarda el estado
				boolean estado = aleatorio.readBoolean();
				//ternario para que aparezca un String
				String estadoS = (estado)? "Activa":"Inactiva";
				
				//barra para dar formato
				String barra="|";
				
				// Formato de columnas: int + String + String + double + boolean
		        reporte = String.format("%-20d%-5s%-20s%-5s%-20s%-5s%-20.2f%-5s%-20s%-5s",
		                id,
		                barra,
		                nombre.toString().trim(),
		                barra,
		                tipoCuenta.toString().trim(),
		                barra,
		                saldo,
		                barra,
		                estadoS,
		                barra);
		        
				System.out.println(reporte);
				System.out.println("------------------------------------------------------------------------------------------------------------------------|");	

			}
			
			//cerramos el flujo
			aleatorio.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 /**
     * Modifica el nombre de una cuenta en la posición especificada.
     *
     * @param nombre El nuevo nombre del titular de la cuenta.
     * @param posicion La posición en el archivo donde se encuentra la cuenta.
     * @param ruta La ruta del archivo donde se encuentra la cuenta.
     */
	public static void modificarNombre(String nombre, int posicion,String ruta) {
		
		//abrimos flujo
		try(RandomAccessFile aleatorio = new RandomAccessFile(ruta,"rw")){
			
			//nos colocamos en nombre que siempre sera 4 mas que la posicion del Id
			aleatorio.seek(posicion+4);
			
			//damos tamaño correcto
			StringBuffer buffer=new StringBuffer(nombre);
	    	buffer.setLength(50);
	    	
	    	//escribimos en fichero
	    	aleatorio.writeChars(buffer.toString());
	    	
	    	//cerramos el flujo
			aleatorio.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Modifica el tipo de cuenta de una entrada en el archivo.
	 *
	 * @param tipoCuenta El nuevo tipo de cuenta a establecer.
	 * @param posicion La posición en el archivo donde comienza la línea de la cuenta a modificar.
	 * @param ruta La ruta del archivo donde se encuentra la cuenta.
	 */
	public static void modificarTipoCuenta(String tipoCuenta, int posicion, String ruta) {
		
		//abrimos flujo
		try(RandomAccessFile aleatorio = new RandomAccessFile(ruta,"rw")){
				//posicion del tipoCuenta
				aleatorio.seek(posicion+104);
				
				//damos tamaño correcto
				StringBuffer buffer;
				buffer=new StringBuffer(tipoCuenta);
		    	buffer.setLength(20);
		    	
		    	//escribimos en fichero
		    	aleatorio.writeChars(buffer.toString());
		    	
		    	//cerramos el flujo
				aleatorio.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	/**
	 * Modifica el estado de actividad de una entrada en el archivo.
	 *
	 * @param estado El nuevo estado de la cuenta (activa/inactiva).
	 * @param posicion La posición en el archivo donde comienza la línea de la cuenta a modificar.
	 * @param ruta La ruta del archivo donde se encuentra la cuenta.
	 */
	public static void modificarEstado(boolean estado, int posicion, String ruta) {
		
		//abrimos flujo
		try(RandomAccessFile aleatorio = new RandomAccessFile(ruta,"rw")){
				//avance a llegar a estado
				aleatorio.seek(posicion+152);
				 
		    	//escribimos en fichero
		    	aleatorio.writeBoolean(estado);
		    	
		    	//cerramos el flujo
				aleatorio.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
	/**
    * Modifica el saldo de una cuenta en la posición especificada.
    *
    * @param saldo El nuevo saldo de la cuenta.
    * @param posicion La posición en el archivo donde se encuentra la cuenta.
    * @param ruta La ruta del archivo donde se encuentra la cuenta.
    */
		public static void modificarSaldo(Double saldo, int posicion, String ruta) {
			
			//abrimos fluijo
			try(RandomAccessFile aleatorio = new RandomAccessFile(ruta,"rw")){
				//avance a saldo
				aleatorio.seek(posicion+144);
				
				//saldo actual
		    	Double newSaldo = aleatorio.readDouble();
		    	
		    	//actualizamos saldo
		    	newSaldo+=saldo;
		    	
		    	//movemos el puntero hacia atras
		    	aleatorio.seek(aleatorio.getFilePointer()-8);
		    	
		    	//reescribimos saldo acttual
		    	aleatorio.writeDouble(newSaldo);
		    	
		    	//cerramos el flujo
				aleatorio.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/**
	* Consulta y muestra el saldo de una cuenta en un formato tabular.
	*
	* @param posicion La posición en el archivo donde comienza la línea de la cuenta.
	* @param ruta La ruta del archivo donde se encuentra la cuenta.
	*/
	public static void consultarSaldo(int posicion, String ruta) {
		System.out.println("--------------------------------------------------------------------------|");
		String cabeceraSaldo = String.format("%-20s%-5s%-20s%-5s%-20s%5s", 
                "Titular", "|", "Tipo de Cuenta", "|", "Saldo","|");

		System.out.println(cabeceraSaldo);
		System.out.println("--------------------------------------------------------------------------|");
		
		//abrimos flujo
		try(RandomAccessFile aleatorio = new RandomAccessFile(ruta,"rw")){
			
			//nos colocamos en saldo
			aleatorio.seek(posicion+4);
			
			//nombre
			StringBuilder nombreSaldo = new StringBuilder();
			for(int i=0; i<50;i++) {
				nombreSaldo.append(aleatorio.readChar());
			};
			
			//tipo de Cuenta
			StringBuilder tipoCuentaSaldo = new StringBuilder();
			for(int i=0; i<20;i++) {
				tipoCuentaSaldo.append(aleatorio.readChar());
			};
			
			//indicamos el saldo
			Double saldoTotal = aleatorio.readDouble();
			
			
			String barra="|";
			
			// Formato de columnas: int + String + String + double + boolean
	        String reporte = String.format("%-20s%-5s%-20s%-5s%-20.2f%5s",
	        		nombreSaldo.toString().trim(),
	                barra,
	                tipoCuentaSaldo.toString().trim(),
	                barra,
	                saldoTotal,
	                barra);
				
	        	//imprimimos por pantalla
				System.out.println(reporte);
				
				//cerramos el flujo
				aleatorio.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("--------------------------------------------------------------------------|");	}
	
	/**
	 * Registra un tipo de depósito o retiro en el archivo de transacciones.
	 *
	 * @param opcionDeposito La opción de depósito: "1" para depósito, "0" para error, "-1" para retiro, 3 cancelacion.
	 * @param monto La cantidad de dinero a depositar o retirar.
	 * @param rutaTrans La ruta del archivo de transacciones.
	 * @param idCuenta El ID de la cuenta asociada a la transacción.
	 * @param longitudFilaTransacciones La longitud de una fila en el archivo de transacciones.
	 * @return Un entero que indica el resultado de la operación: 1 para depósito, -1 para retiro, 0 para error.
	 */
	public static int tipoDeposito(String opcionDeposito, double monto, String rutaTrans, int idCuenta, int longitudFilaTransacciones) {
		//indica el tipo de deposito 1(deposito),0(Error auqnue creo que no se puede dar), -1 (Retiro)
		int Deposito = 0;
		
		//si el desposito es 1, deposito, 2 retiro 3 cancelacion
		if(opcionDeposito.equals("1")||opcionDeposito.equals("2")||opcionDeposito.equals("3")){
			
				//abrimos flujo
				try(RandomAccessFile aleatorio = new RandomAccessFile(rutaTrans,"rw")){
					
					//ultimo id
					int ultimoID = Metodos.ultimoId(rutaTrans, longitudFilaTransacciones);
					
					//colocamos el puntero en la primera posicion vacia:
					aleatorio.seek((int)(aleatorio.length()));
					
					//generar id
					aleatorio.writeInt(CuentaBancaria.asignarId(ultimoID));

					//idCuenta
					aleatorio.writeInt(idCuenta);
					
					//Generar fecha siempre el día actual
					String fecha = generarFecha();
					aleatorio.writeChars(fecha);
					
					//tipo ya lo debemos tener
					//devolvemos el monto en tipo de deposito
					if(opcionDeposito.equals("1")) {
						StringBuffer buffer= new StringBuffer("Depósito");
						buffer.setLength(15);
						aleatorio.writeChars(buffer.toString());
						Deposito=1;
					}else if(opcionDeposito.equals("2")){
						StringBuffer buffer= new StringBuffer("Retiro");
						buffer.setLength(15);
						aleatorio.writeChars(buffer.toString());
						Deposito=-1;
					}else {
						StringBuffer buffer= new StringBuffer("Cancelación");
						buffer.setLength(15);
						aleatorio.writeChars(buffer.toString());
						Deposito=-2;
					}
					
					//monto
					aleatorio.writeDouble(monto);
					
					//cerramos el flujo
					aleatorio.close();
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		
		return Deposito;
	}
	
	/**
	 * Genera la fecha actual en formato "dd/MM/yyyy".
	 *
	 * @return La fecha actual como una cadena en formato "dd/MM/yyyy".
	 */
	public static String generarFecha() {
		//objeto con la fecha
		GregorianCalendar calendario = new GregorianCalendar();
		//creamos objeto Date
		Date fecha=calendario.getTime();
		
		//Date no se puede guardar en el archivo BufferedReader solo acepta Sting
		SimpleDateFormat formatoCalendario= new SimpleDateFormat("dd/MM/yyyy");
		
		//damos el formato String
		String fechaString= formatoCalendario.format(fecha);
		
		return fechaString;
	}
	
	/**
	 * Averigua el monto de la cuenta a cancelar, y llamr al metodo tipoDeposito
	 *
	 * @param TipoCuenta 			La opción de depósito: "1" para depósito, "0" para error, "-1" para retiro, 3 cancelacion.
	 * @param PosicionCuentaBorrar	posicion donde comienza la linea a borrar
	 * @param IdCuenta				Id de la cuenta a borrar
	 * @param rutaTrans				Ruta de las transacciones
	 * @param rutaCuentas			Ruta de las cuentas
	 * @param longitudFiilaCuentas	longitud de la fila del fichero cuentas.dat
	 * @param longitudFilasTransaccion longitud fila del fichero transacciones.dat
	 */
	public static void Cancelacion(String TipoCuenta, int PosicionCuentaBorrar, int idCuenta, String rutaTrans, String rutaCuentas, int longitudFilaCuentas,
			int longitudFilaTransacciones) {
		
		//variables donde guardarmeos el monto
		double monto=-1;
		
		//abrimos flujo
		try(RandomAccessFile aleatorio = new RandomAccessFile(rutaCuentas,"rw")){
			//colocamos el puntero en monto	
			aleatorio.seek(PosicionCuentaBorrar+144);
			//guardmaos en la variable	
			monto=aleatorio.readDouble();
			
			//cerramos el flujo
			aleatorio.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//registra la transaccion
		Metodos.tipoDeposito(TipoCuenta, monto, rutaTrans, idCuenta, longitudFilaTransacciones);
	}
	

}

package tareaLarga;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Reportes {
	
	  /**
     * Busca las posiciones donde las cuentas están activas.
     * La posición activa se encuentra en el byte 152, correspondiente a un booleano.
     * 
     * @param rutaCuentas La ruta del archivo donde se almacenan las cuentas.
     * @param longitudFilaCuentas La longitud de cada fila de cuenta en el archivo.
     * @return Un ArrayList de enteros que contiene las posiciones de las cuentas activas.
     */
	public static ArrayList<Integer> cuentasActivas(String rutaCuentas, int longitudFilaCuentas) {
		ArrayList<Integer> posicionesCuentasActivas=new ArrayList<>();
		
		//donde comenzamos a buscar
		int posicion=152;
		

		//abrimos flujo
		try(RandomAccessFile aleatorio = new RandomAccessFile(rutaCuentas,"rw")){
			
			//se hara este bucle mientras psoicion sea menor a la longitud total del fichero
			while(posicion<aleatorio.length()) {
				//nos colocamos en el fichero
				aleatorio.seek(posicion);
				//guardamos estado de la cuenta
				boolean cuentaActiva = aleatorio.readBoolean();
				
				//si es True
				if(cuentaActiva) {
					//guardamos la psoicion del puntero menos la cadena completa asi nos situa al comienzo
					//de la linea
					posicionesCuentasActivas.add((int)(aleatorio.getFilePointer())-longitudFilaCuentas);
				}
				
				///avanzamos a la siguietne cuenta
				posicion+=longitudFilaCuentas;
			}
			
			//cerramos flujo
			aleatorio.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return posicionesCuentasActivas;
	}
	
	/**
     * Guarda los datos de las cuentas activas en un archivo de texto.
     * También imprime en consola el formato de las cuentas activas.
     * 
     * @param rutaCuentas La ruta del archivo donde se almacenan las cuentas.
     * @param rutaCuentasActivas La ruta del archivo de texto donde se guardarán las cuentas activas.
     * @param listaActiva La lista de posiciones de las cuentas activas.
     */
	public static void mostrarListasActivas(String rutaCuentas, String rutaCuentasActivas, ArrayList<Integer> listaActiva ) {
		System.out.println("-----------------------------------------------------------------------------------------------------------------------------|");

		String cabecera = String.format("%-5s%-20s%-5s%-20s%-5s%-20s%-5s%-20s%-5s%-20s%-5s",
		        "|","Id Cuenta","|", "Titular","|","Tipo de Cuenta","|", "Saldo","|", "Estado de la Cuenta","|");

		// Mostrar la cabecera
		System.out.println(cabecera);
		
		System.out.println("-----------------------------------------------------------------------------------------------------------------------------|");

		
		//guardaremos en variables los datos a guardar
		int idCuenta=0;
		String titular = null;
		String tipoCuenta = null;
		double saldo = 0;
		boolean estado;
		String estadoS = null;
		String reporte=null;
		
		//controlar al escribir en el txt
		int contador=0;
			
		//abrimos el flujo
		try(RandomAccessFile aleatorio = new RandomAccessFile(rutaCuentas,"rw")){
			
			//todo el tamaño de listaActiva
			for(int i=0; i<listaActiva.size();i++) {
				//colocamos el puntero en la primera posicion con cuenta activa
				aleatorio.seek(listaActiva.get(i));
				
				//leemos id
				idCuenta=aleatorio.readInt();
				
				//nombre
				StringBuilder nombreTitular = new StringBuilder();
				for(int j=0; j<50;j++ ) {
					nombreTitular.append(aleatorio.readChar());
				}
				titular=nombreTitular.toString().trim();//convertimos a stirng eliminamos espacios
				
				//tipo de cuenta
				StringBuilder tipoC = new StringBuilder();
				for(int h=0; h<20; h++) {
					tipoC.append(aleatorio.readChar());
				}
				
				tipoCuenta= tipoC.toString().trim();//String sin espacios
				
				//sald0
				saldo=aleatorio.readDouble();
				
				//estado
				estado = aleatorio.readBoolean();
				
				//ternario
				estadoS=(estado)?"Activa":"Inactiva";
				
				
				//cerramos flujo
				aleatorio.close();
				
					reporte = "Id Cuenta => "+idCuenta+" Titular => "+titular+"Tipo de Cuenta => "+tipoCuenta
						+ "Saldo => "+saldo+"   Estado de la Cuenta => "+estadoS+".";
				
			        String barra="|";
					// Formato de columnas: int + String + String + double + boolean
			        reporte = String.format("%-5s%-20d%-5s%-20s%-5s%-20s%-5s%-20.2f%-5s%-20s%-5s",
			        		barra,
			        		idCuenta,
			                barra,
			                titular,
			                barra,
			                tipoCuenta,
			                barra,
			                saldo,
			                barra,
			                estadoS,
			                barra);
			        
					System.out.println(reporte);
					System.out.println("-----------------------------------------------------------------------------------------------------------------------------|");
	
					
					try(BufferedWriter escritura = new BufferedWriter(new FileWriter(rutaCuentasActivas, true))){
						
						if(contador<=0) {
							escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
							escritura.newLine();
							escritura.write("|                                         LISTADO CUENTAS ACTIVAS DISTINTAS A CERO                                         |");
							escritura.newLine();
							escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
							escritura.newLine();
							escritura.write(cabecera);
							escritura.newLine();
							escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
							escritura.newLine();
							contador++;
						}
						
						escritura.write(reporte);
						escritura.newLine();
						escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
						escritura.newLine();
						
						escritura.close();
					}catch(Exception e) {
						System.out.println("Fallo escritura metodo Listas activas");
					}
				
				
			}
			
			//cerramos flujo
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
      * Busca las posiciones donde las cuentas están inactivas.
      * La posición activa se encuentra en el byte 152, correspondiente a un booleano.
      * 
      * @param rutaCuentas La ruta del archivo donde se almacenan las cuentas.
      * @param longitudFilaCuentas La longitud de cada fila de cuenta en el archivo.
      * @return Un ArrayList de enteros que contiene las posiciones de las cuentas inactivas.
      */
	public static ArrayList<Integer> cuentasInactivas(String rutaCuentas, int longitudFilaCuentas) {
		
		//guaradaremos posiciones de listaas inactivas
		ArrayList<Integer> posicionesCuentasInactivas=new ArrayList<>();
		
		
		//donde comenzamos a buscar
		int posicion=152;
		
		//abrimos flujo
		try(RandomAccessFile aleatorio = new RandomAccessFile(rutaCuentas,"rw")){
			
			//se hara este bucle mientras psoicion sea menor a la longitud total del fichero
			while(posicion<aleatorio.length()) {
				aleatorio.seek(posicion);
				//si es False
				boolean cuentaActiva = aleatorio.readBoolean();
				
				if(!cuentaActiva) {
					//guardamos la psoicion del puntero menos la cadena completa asi nos situa al comienzo
					//de la linea
					posicionesCuentasInactivas.add((int)(aleatorio.getFilePointer())-longitudFilaCuentas);
				}
				
				//avanzamos al siguiente
				posicion+=longitudFilaCuentas;
			}
			
			//cerramos flujo
			aleatorio.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return posicionesCuentasInactivas;
	}
	
	/**
     * Guarda los datos de las cuentas inactivas en un archivo de texto.
     * También imprime en consola el formato de las cuentas inactivas.
     * 
     * @param rutaCuentas La ruta del archivo donde se almacenan las cuentas.
     * @param rutaCuentasInactivas La ruta del archivo de texto donde se guardarán las cuentas inactivas.
     * @param listaInactiva La lista de posiciones de las cuentas inactivas.
     */	
	public static void mostrarListasInactivas(String rutaCuentas, String rutaCuentasInactivas, ArrayList<Integer> listaInactiva ) {
		//guardaremos en variables los datos a guardar
		int idCuenta=0;
		String titular = null;
		String tipoCuenta = null;
		double saldo = 0;
		boolean estado;
		String estadoS = null;
		String reporte=null;
		int contador=0;
		
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------|");

		String cabecera = String.format("%-5s%-20s%-5s%-20s%-5s%-20s%-5s%-20s%-5s%-20s%-5s",
		        "|","Id Cuenta","|", "Titular","|","Tipo de Cuenta","|", "Saldo","|", "Estado de la Cuenta","|");

		// Mostrar la cabecera
		System.out.println(cabecera);
		
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------|");

		
		
		//leemos el documento binario
		try(RandomAccessFile aleatorio = new RandomAccessFile(rutaCuentas,"rw")){
			
			//leeremos todo el arrya q inidca todas las posiicones de listas inactivas
			for(int i=0; i<listaInactiva.size();i++) {
				//colocamos el puntero en la primera posicion con cuenta activa
				aleatorio.seek(listaInactiva.get(i));
				
				//id
				idCuenta=aleatorio.readInt();
				
				//nombre
				StringBuilder nombreTitular = new StringBuilder();
				for(int j=0; j<50;j++ ) {
					nombreTitular.append(aleatorio.readChar());
				}
				titular=nombreTitular.toString().trim();
				
				//tipoCuenta
				StringBuilder tipoC = new StringBuilder();
				for(int h=0; h<20; h++) {
					tipoC.append(aleatorio.readChar());
				}
				
				tipoCuenta= tipoC.toString().trim();
				
				//saldo
				saldo=aleatorio.readDouble();
				
				//estado
				estado = aleatorio.readBoolean();
				
				estadoS=(estado)?"Activa":"Inactiva";
				
				//solo se muestra si el slado es distinto a cero
				if(saldo!=0) {
					reporte = "Id Cuenta => "+idCuenta+" Titular => "+titular+"Tipo de Cuenta => "+tipoCuenta
							+ "Saldo => "+saldo+"   Estado de la Cuenta => "+estadoS+".";
						
				        String barra="|";
						// Formato de columnas: int + String + String + double + boolean
				        reporte = String.format("%-5s%-20d%-5s%-20s%-5s%-20s%-5s%-20.2f%-5s%-20s%-5s",
				        		barra,
				        		idCuenta,
				                barra,
				                titular,
				                barra,
				                tipoCuenta,
				                barra,
				                saldo,
				                barra,
				                estadoS,
				                barra);
					
				        System.out.println(reporte);
				        System.out.println("|----------------------------------------------------------------------------------------------------------------------------|");

					//abrimos flujo para escribir en el txt
					
					try(BufferedWriter escritura = new BufferedWriter(new FileWriter(rutaCuentasInactivas, true))){
						
						if(contador<=0) {
							escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
							escritura.newLine();
							escritura.write("|                                         LISTADO CUENTAS INACTIVAS DISTINTAS A CERO                                         |");
							escritura.newLine();
							escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
							escritura.newLine();
							escritura.write(cabecera);
							escritura.newLine();
							escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
							escritura.newLine();
							contador++;
						}
						
						escritura.write(reporte);
						escritura.newLine();
						escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
						escritura.newLine();
						
						
						escritura.close();
					}catch(Exception e) {
						System.out.println("Exception error en Cuentas inactiva");
					}
				}
				
			}
			//cerramos flujo
			aleatorio.close();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	/**
	 * Genera un resumen financiero por tipo de cuenta. Lee los registros de cuentas desde un archivo y 
	 * agrupa el saldo total y el número de cuentas por tipo de cuenta, guardando los resultados en un archivo.
	 *
	 * @param rutaCuentas           La ruta del archivo que contiene los datos de las cuentas.
	 * @param longitudFilasCuenta    La longitud de cada fila en el archivo de cuentas.
	 * @param rutaResumenTipoCuentas La ruta del archivo donde se guardará el resumen de tipos de cuenta.
	 */
	public static void resumenTipoCuenta(String rutaCuentas, int longitudFilasCuenta, String rutaResumenTipoCuentas) {
        // Mapa donde la clave es el tipo de cuenta y el valor es un array de Double [saldo acumulado, número de cuentas]
        Map<String, Double[]> resumen = new HashMap<>();
        
        int posicion = 104; // Posición inicial para leer los tipos de cuenta
        String tipoCuenta = null;
        double saldo = 0;
        Double contador=0.0;
        System.out.println("|----------------------------------------------------------------------|");

		String cabecera = String.format("|%-20s%-5s%-20s%-5s%-20s%-5s",
		        "Tipo de Cuenta","|", "Total de cuentas","|","Saldo","|");

		// Mostrar la cabecera
		System.out.println(cabecera);
		
		//cerramos el flujo
        try (RandomAccessFile aleatorio = new RandomAccessFile(rutaCuentas, "r")) {
            
            // Bucle para leer mientras no se haya llegado al final del archivo
            while (posicion < aleatorio.length()) {
            	//colocamos el puntero
                aleatorio.seek(posicion);
                
                // Leer el tipo de cuenta (20 caracteres)
                StringBuilder tipoC = new StringBuilder();
                for (int i = 0; i < 20; i++) {
                    tipoC.append(aleatorio.readChar());
                }
                tipoCuenta = tipoC.toString().trim(); // Eliminar posibles espacios en blanco
              
                // Leer el saldo
                saldo = aleatorio.readDouble();
                
                // Actualizar la posición para la siguiente fila
                posicion += longitudFilasCuenta;
                
                // Actualizar el mapa con la información del tipo de cuenta y saldo
                
                //comprobamos que los id no son -1 que indica cuenta eliminada
                if(!tipoCuenta.equals("")) {
                	
                	//si el tipo de cuenta aprece
                	if (resumen.containsKey(tipoCuenta)) {
	                    Double[] datos = resumen.get(tipoCuenta);
	                    datos[0] += saldo;  // Incrementar el saldo acumulado
	                    datos[1]++;         // Incrementar el número de cuentas
	                    
	                    saldo=datos[0];
	                    contador=datos[1];
	                } else {
	                    // Si no existe en el mapa, se añade una nueva entrada con el saldo y 1 cuenta
	                    resumen.put(tipoCuenta, new Double[]{saldo, 1.0});
	                }
                }
                
                
                String reporte = String.format("|%-20s%-5s%-20s%-5s%-20.2f%5s",
        		        tipoCuenta,"|", contador,"|",saldo,"|");
            }
            
            //cerramos el flujo
            aleatorio.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
            
            try(BufferedWriter escritura = new BufferedWriter(new FileWriter(rutaResumenTipoCuentas,true))){
            		
            	 	escritura.write("|----------------------------------------------------------------------|");
            	 	escritura.newLine();
					escritura.write("|                  Resumen financiero por tipo de cuenta               |");
					escritura.newLine();
					escritura.write("|----------------------------------------------------------------------|");
					escritura.newLine();
					escritura.write(cabecera);
            		//para escribir en la siguiente linea
	            	escritura.newLine();
			        escritura.write("|----------------------------------------------------------------------|");
			        escritura.newLine();
	            	
	                System.out.println("|----------------------------------------------------------------------|");
	            	
	            	
	            //bucle para recorreer el mapa
            	for (Map.Entry<String, Double[]> entry : resumen.entrySet()) {
                    String tipo = entry.getKey();
                    Double[] datos = entry.getValue();
                  
                    String reporte = String.format("|%-20s%-5s%-20s%-5s%-20.0f%-5s",
            		        tipo,"|", datos[1],"|",datos[0],"|");
                    System.out.println(reporte);
                    System.out.println("|----------------------------------------------------------------------|");
                    
                    
	            	escritura.write(reporte);
	            	escritura.newLine();
			        escritura.write("|----------------------------------------------------------------------|");

	            	//para escinir en la siguiente linea
	            	escritura.newLine();
            	
            	}
            	
        		escritura.close();
            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   
	}
	
	/**
	 * Identifica la cuenta asociada con un nombre de titular dado. Si el titular tiene una cuenta, devuelve el ID de la cuenta.
	 * Si el nombre no tiene una cuenta asociada, devuelve -1.
	 *
	 * @param nombre               El nombre del titular de la cuenta que se desea buscar.
	 * @param rutaCuentas           La ruta del archivo que contiene los datos de las cuentas.
	 * @param longitudFilasCuentas  La longitud de cada fila en el archivo de cuentas.
	 * @return                      El ID de la cuenta si se encuentra el nombre; -1 si no se encuentra.
	 */
	public static int IdentificarCuentaNombre(String nombre, String rutaCuentas,int longitudFilasCuentas) {
		//si devuelve -1 el nombre no tiene cuenta
		int idEncontrado=-1;
		
		int posicion=4;
		
		try(RandomAccessFile aleatorio = new RandomAccessFile(rutaCuentas,"rw")){
			
			while(posicion<aleatorio.length()) {
				
				//colocamos el puntero
				aleatorio.seek(posicion);
				
				//guardamos el nombre que se encuentra en esa posicio y los comparamos 
				//con el que nos da por parámetros
				StringBuilder newNombre= new StringBuilder();
				for(int i=0; i<50; i++) {
					newNombre.append(aleatorio.readChar());
				}
				
				
				//pasamos el stingbuilder a string y eliminamos espacio
				//si es igual guardamos el id que son 4  menos posicion
				if(nombre.equals(newNombre.toString().trim())) {
					
					aleatorio.seek(posicion-4);
					idEncontrado=aleatorio.readInt();
					break;
				}
				
				//movemos el puntero a la posicion del siguiente nombre
				posicion+=longitudFilasCuentas;
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return idEncontrado;
	
	}	
	
	/**
	 * Muestra y guarda las transacciones asociadas con una lista de posiciones en un archivo de transacciones. Las posiciones corresponden a
	 * transacciones realizadas por el titular de la cuenta dado.
	 *
	 * @param rutaTrans                 La ruta del archivo de transacciones.
	 * @param longitudFilaTransacciones La longitud de cada fila en el archivo de transacciones.
	 * @param posiciones                Lista de posiciones donde se encuentran las transacciones a leer.
	 * @param nombreBuscar              El nombre del titular de la cuenta cuyas transacciones se mostrarán.
	 */	
	public static void movCuentas(String rutaTrans, int longitudFilaTransacciones, ArrayList<Integer> posiciones, String nombreBuscar) {
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------|");
		System.out.println("                                          Listado transacciones "+nombreBuscar);
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------|");

		String cabecera = String.format("%-5s%-20s%-5s%-20s%-5s%-20s%-5s%-20s%-5s%-20s%-5s",
		        "|","Id de la transacción","|", "Titular","|","Fecha","|", "Tipo de Cuenta","|", "Saldo","|");

		// Mostrar la cabecera
		System.out.println(cabecera);
		
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------|");

		//variables
		int id_transaccion=0;
		int id_cliente=0;
		String fecha = null;
		String tipo_transaccion = null;
		double monto = 0;
		String reporte=null;
		int contador=0;
		
		for(int i=0;i<posiciones.size();i++) {
			//abrimos RandomAccesFile
			try(RandomAccessFile aleatorio = new RandomAccessFile(rutaTrans,"rw")){
				
				//colocamos el puntero en la posición
				int pos =posiciones.get(i);
				
				aleatorio.seek(pos);
				
				//id transaccion
				id_transaccion=aleatorio.readInt();
				
				//id cliente
				id_cliente=aleatorio.readInt();
				
				//Fecha
				StringBuilder Fecha= new StringBuilder();
				for(int j=0;j<10;j++) {
					Fecha.append(aleatorio.readChar());
				}
				
				fecha= Fecha.toString();
				
				//Tipo de transaccion
				StringBuilder tipoT= new StringBuilder();
				for(int k=0;k<15;k++) {
					tipoT.append(aleatorio.readChar());
				}
				
				tipo_transaccion= tipoT.toString();
				
				monto=aleatorio.readDouble();
				
				
				String barra="|";
				// Formato de columnas: int + String + String + double + boolean
		        reporte = String.format("%-5s%-20d%-5s%-20d%-5s%-20s%-5s%-20s%-5s%-20.2f%-5s",
		        		barra,
		        		id_transaccion,
		                barra,
		                id_cliente,
		                barra,
		                fecha,
		                barra,
		                tipo_transaccion,
		                barra,
		                monto,
		                barra);
		        
				System.out.println(reporte);
				System.out.println("!----------------------------------------------------------------------------------------------------------------------------|");

				//guardamos en el Fichero de texto;
				/*ruta absoluta
				String rutaArchivo = "C:\\Users\\Ismael\\eclipse-workspace\\AccesoDatos\\src\\tareaLarga\\reportes\\movimiento_"+id_cliente+".txt";
				*/
				//ruta relativa
				String rutaArchivo = "src\\tareaLarga\\reportes\\movimiento_"+id_cliente+".txt";

				
				try(BufferedWriter escritura = new BufferedWriter(new FileWriter(rutaArchivo,true))){
					if(contador==0) {
						escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
						escritura.newLine();
						escritura.write("                                          Listado transacciones "+nombreBuscar);
						escritura.newLine();
						escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
						escritura.newLine();
						escritura.write(cabecera);
						escritura.newLine();
						escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
						contador++;
					}
					
					escritura.newLine();
					escritura.write(reporte);
					escritura.newLine();
					escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");

			
					
				};
			
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		}
	}
	
	
	/**
	 * Busca transacciones realizadas en una fecha específica en un archivo de transacciones. Si se encuentran transacciones en esa fecha,
	 * las muestra y las guarda en un archivo.
	 *
	 * @param fecha                     La fecha que se desea buscar en formato String (dd/MM/yyyy).
	 * @param rutaTrans                 La ruta del archivo de transacciones.
	 * @param longitudFilaTransacciones La longitud de cada fila en el archivo de transacciones.
	 * @param fechaRuta                 El nombre del archivo de salida donde se guardarán las transacciones encontradas.
	 */	
	public static void identificarFechas(String fecha, String rutaTrans, int longitudFilaTransacciones, String fechaRuta) {
		
		//posicion de fecha
		int posicion=8;
		
		boolean fechaEncontrada=false;
		
		int posicionFechaEncontrada =0;
		int contador=0;
		String cabecera = String.format("%-5s%-20s%-5s%-20s%-5s%-20s%-5s%-20s%-5s%-20s%-5s",
		        "|","Id Transacción","|", "Titular","|","Fecha","|", "Tipo de Cuenta","|", "Saldo","|");
		
		
		try(RandomAccessFile aleatorio= new RandomAccessFile(rutaTrans,"rw")){
			while(posicion<aleatorio.length()) {
				aleatorio.seek(posicion);
				StringBuilder newFecha = new StringBuilder();
				for(int i=0; i<10;i++) {
					newFecha.append(aleatorio.readChar());
				}
				
				//compramos la fecha dada con la obtenida en el documento Transacciones
				
				if(fecha.equals(newFecha.toString())) {
					
					if(contador==0) {
						// Mostrar la cabecera
						System.out.println("|----------------------------------------------------------------------------------------------------------------------------|");
						System.out.println("                                            Transacciones realizadas en "+fecha);
						System.out.println("|----------------------------------------------------------------------------------------------------------------------------|");						
						System.out.println(cabecera);
						
						System.out.println("|----------------------------------------------------------------------------------------------------------------------------|");

					}
					//posicion menos 8, es la psoicion del comienzo de la linea
					posicionFechaEncontrada=posicion-8;
					fechaEncontrada=true;
					
					guardarEnDocumento(aleatorio, posicionFechaEncontrada,fecha, rutaTrans,fechaRuta,cabecera, contador);
					contador++;
				}
				
				posicion+=longitudFilaTransacciones;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!fechaEncontrada) {
			System.out.println("|-----------------------------------------------|");
			System.out.println("   En "+fecha+" no se realizaron transacciones  ");
			System.out.println("|-----------------------------------------------|");
		}
		
	}
	
	/**
	 * Guarda una transacción en un archivo de texto, dado un registro que corresponde a una fecha específica.
	 *
	 * @param aleatorio                 El archivo de acceso aleatorio donde se están leyendo los registros.
	 * @param posicionFechaEncontrada    La posición en el archivo donde se encontró la fecha.
	 * @param fecha                      La fecha de la transacción a guardar.
	 * @param rutaTrans                  La ruta del archivo de transacciones.
	 * @param fechaRuta                  El nombre del archivo de salida donde se guardará la transacción.
	 * @param cabecera                   La cabecera que se utiliza para formatear el archivo de salida.
	 * @param contador                   Contador para controlar si ya se ha escrito la cabecera en el archivo.
	 */
	public static void guardarEnDocumento(RandomAccessFile aleatorio, int posicionFechaEncontrada, String fecha, String rutaTrans, String fechaRuta, String cabecera, int contador) {
		
		/*ruta absoluta
		String newRuta="C:\\Users\\Ismael\\eclipse-workspace\\AccesoDatos\\src\\tareaLarga\\reportes\\transacciones_"+fechaRuta+".txt";
		*/
		
		//ruta relativa
		String newRuta = "src/tareaLarga/reportes/transacciones_" + fechaRuta + ".txt";

		
		//variables
		int id_transaccion=0;
		int id_cliente=0;
		String tipo_transaccion = null;
		String newFecha= fecha;
		double monto = 0;
		String reporte=null;
				
		//abrimos RandomAccesFile
			
			//colocamos el puntero en la posición
			int pos =posicionFechaEncontrada;
			
		
		
			try {
aleatorio.seek(pos);
				
				//id transaccion
				id_transaccion=aleatorio.readInt();
				
				//id cliente
				id_cliente=aleatorio.readInt();
				
				//Fecha
				StringBuilder Fecha= new StringBuilder();
				for(int j=0;j<10;j++) {
					Fecha.append(aleatorio.readChar());
				}
				
				newFecha= Fecha.toString();
				
				//Tipo de transaccion
				StringBuilder tipoT= new StringBuilder();
				for(int k=0;k<15;k++) {
					tipoT.append(aleatorio.readChar());
				}
				
				tipo_transaccion= tipoT.toString();
				
				monto=aleatorio.readDouble();
				
				
				if(fecha.equals(newFecha)) {
					String barra="|";
					// Formato de columnas: int + String + String + double + boolean
			        reporte = String.format("%-5s%-20d%-5s%-20d%-5s%-20s%-5s%-20s%-5s%-20.2f%-5s",
			        		barra,
			        		id_transaccion,
			                barra,
			                id_cliente,
			                barra,
			                newFecha,
			                barra,
			                tipo_transaccion,
			                barra,
			                monto,
			                barra);
			        
					System.out.println(reporte);
					System.out.println("!----------------------------------------------------------------------------------------------------------------------------|");
					
					
					//guardamos en el Fichero de texto;
					try(BufferedWriter escritura = new BufferedWriter(new FileWriter(newRuta,true))){
						if(contador==0) {
							escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
							escritura.newLine();
							escritura.write("                                    Transacciones realizadas en la fecha:"+fecha+"                                           ");
							escritura.newLine();
							escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
							escritura.newLine();
							escritura.write(cabecera);
							escritura.newLine();
							escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
							contador++;
						}
						
						escritura.newLine();
						escritura.write(reporte);
						escritura.newLine();
						escritura.write("|----------------------------------------------------------------------------------------------------------------------------|");
						
					}
				}
				
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			
		
	}

	/**
	 * Calcula el saldo total acumulado de todas las cuentas presentes en el archivo de cuentas.
	 * 
	 * @param rutaCuentas            Ruta del archivo binario que contiene las cuentas.
	 * @param longitudFilasCuentas    Longitud en bytes de cada fila (registro) en el archivo de cuentas.
	 * @return                       El saldo total acumulado de todas las cuentas.
	 */
	public static double saldoTotalHoy(String rutaCuentas, int longitudFilasCuentas) {
		
		//posicion del tipo determina si se suma o resta
		int posicion=144;
		
		double saldo=0;
		
		try(RandomAccessFile aleatorio = new RandomAccessFile(rutaCuentas,"rw")){
			while(posicion<aleatorio.length()) {
				//colocamos el puntero
				aleatorio.seek(posicion);
				
				//se obtiene el saldo
				saldo+=aleatorio.readDouble();
					
				posicion+=longitudFilasCuentas;
				
			}
			
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return saldo;
		
	}
	
	/**
	 * Busca transacciones realizadas en una fecha específica y devuelve la posición donde
	 * comienzan los registros de transacciones de ese día.
	 * 
	 * @param rutaTrans                  Ruta del archivo binario que contiene las transacciones.
	 * @param longitudFilaTransacciones   Longitud en bytes de cada fila (registro) en el archivo de transacciones.
	 * @param fechaCorrecta              Fecha en formato LocalDate a buscar.
	 * @return                           La posición en el archivo donde comienzan las transacciones de la fecha dada, o -1 si no se encuentra.
	 */	
	public static int verFechaTransacciones(String rutaTrans, int longitudFilaTransacciones, LocalDate fechaCorrecta) {
		int posicion =8;
		int posicionRetorno=-1;
		
		try(RandomAccessFile aleatorio = new RandomAccessFile(rutaTrans,"rw")){
			
			while(posicion<=aleatorio.length()) {
				//se coloca en la posicion de fecha
				aleatorio.seek(posicion);
				
				StringBuilder fecha = new StringBuilder();
				for(int i=0; i<10;i++) {
					fecha.append(aleatorio.readChar());
				}
				
				//convertir fecha en LocalDate
				LocalDate fechaString=validarFecha(fecha.toString());
				
				if(fechaString.equals(fechaCorrecta)) {
					//poisicion donde comienza los tipos de cuenta
					posicionRetorno=posicion+20;
					
					break;
				}
				
				posicion+=longitudFilaTransacciones;
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return posicionRetorno;
	}
	
	/**
	 * Suma los saldos de las transacciones realizadas a partir de una posición específica.
	 * Si la transacción es de tipo "Retiro", resta el saldo; en otros casos, suma.
	 * 
	 * @param posicionFechaInicio       Posición en el archivo desde donde se empiezan a leer las transacciones.
	 * @param rutaTrans                 Ruta del archivo binario que contiene las transacciones.
	 * @param longitudFilaTransacciones Longitud en bytes de cada fila (registro) en el archivo de transacciones.
	 * @return                          El saldo total de las transacciones procesadas.
	 */
	public static double sumatoriotransaccionesFecha(int posicionFechaInicio, String rutaTrans, int longitudFilaTransacciones) {
			
			//posicion de fecha
			int posicion=posicionFechaInicio;
			
			//guardar saldos transacciones
			double newSaldoTransacciones=0;
			
			try(RandomAccessFile aleatorio= new RandomAccessFile(rutaTrans,"rw")){
				
				//posicion donde comienza la linea pero solo quiero los saldos por eso debo
				while(posicion<aleatorio.length()) {
					aleatorio.seek(posicion);
					
					//primero vemos el tipo
					StringBuilder tipoCuenta = new StringBuilder();
					for(int i=0; i<15;i++) {
						tipoCuenta.append(aleatorio.readChar());
					}
					
					
					if("Retiro".equals(tipoCuenta.toString().trim())) {
						
						double retiro=aleatorio.readDouble();
											
						newSaldoTransacciones-=retiro;
						
					}else {
						
						double ingreso=aleatorio.readDouble();
						newSaldoTransacciones+=ingreso;
						
					}
				
					posicion+= longitudFilaTransacciones;
				}
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return newSaldoTransacciones;
			
	}
	
	/**
	 * Guarda el balance histórico en un archivo de texto.
	 * 
	 * @param fechaRuta           Cadena utilizada para nombrar el archivo de salida relacionado con la fecha.
	 * @param sumaTotal           Suma total de saldos en las cuentas.
	 * @param sumaTransacciones   Suma total de las transacciones realizadas.
	 */	
	public static void guardarArchivo(String fechaRuta, double sumaTotal, double sumaTransacciones) {
		/*ruta absoluta
		String newRuta="C:\\Users\\Ismael\\eclipse-workspace\\AccesoDatos\\src\\tareaLarga\\reportes\\historico_financiero_"+fechaRuta+".txt";
		*/
		//ruta relativa
		String newRuta="src\\tareaLarga\\reportes\\historico_financiero_"+fechaRuta+".txt";
		
		double balanceTotal;
		//total balance
		if(sumaTotal>sumaTransacciones) {
			balanceTotal=sumaTotal-sumaTransacciones;
		}else {
			balanceTotal=sumaTransacciones-sumaTotal;
		}
		
		String reporte =("\n\n\n--------------------------------------------------------"+"\n|   BALANCE HÍSTORICO CON FECHA: "+ fechaRuta+"            |"+
				        "\n--------------------------------------------------------"
				+ "\n   Saldo total:                               "+sumaTotal +" €."
				+ "\n   Suma de transacciones realizadas "
				+ "\n   desde fecha dada al día de hoy:            "+ sumaTransacciones+" €."
				+"\n--------------------------------------------------------"				
				+ "\n   Saldo día "+ fechaRuta + " a las 00:01 minutos:  "+(sumaTotal-sumaTransacciones)+" €."+
				"\n--------------------------------------------------------");
		System.out.println(reporte);
		
		
		try(BufferedWriter escritura = new BufferedWriter(new FileWriter(newRuta))){
			escritura.write(reporte);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
	}
	
	/**
	 * Valida una fecha dada en formato de cadena y la convierte en un objeto LocalDate.
	 * El formato esperado es "dd/MM/yyyy".
	 * 
	 * @param fecha   Fecha en formato de cadena.
	 * @return        Un objeto LocalDate si la fecha es válida, o null si es inválida.
	 */
	public static LocalDate validarFecha(String fecha) {
		
		// Configuración del formateador para la fecha (día/mes/año)
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    LocalDate date=null;
	    
	    // Intentar convertir la cadena en una fecha
	    try {
	        date = LocalDate.parse(fecha, dateFormatter);
	        
	    } catch (DateTimeParseException e) {
	    	System.out.println("---------------------------------------------------------------");
	        System.out.println("Has introducido "+ fecha + " que es una fecha inválida.");
	        System.out.println("---------------------------------------------------------------");
	    }
	    
	    return date;
	}

}
		
	



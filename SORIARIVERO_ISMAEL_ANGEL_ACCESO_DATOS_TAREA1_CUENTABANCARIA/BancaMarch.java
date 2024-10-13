package tareaLarga;

import java.io.FileNotFoundException; 
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.spi.FileSystemProvider;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import tareaLarga.CuentaBancaria;
import tareaLarga.Metodos;

public class BancaMarch {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//generamos Scanner
		Scanner teclado = new Scanner(System.in);
		
		//longitud de cada linea
		final int longitudFilaCuentas=153;
		
		final int longitudFilaTransacciones= 66;
		
		//ruta absolutas Cuentas
		/*String rutaCuentas = "C:\\Users\\Ismael\\eclipse-workspace\\AccesoDatos\\src\\tareaLarga\\cuentas.dat";
		String rutaTrans= "C:\\Users\\Ismael\\eclipse-workspace\\AccesoDatos\\src\\tareaLarga\\transacciones.dat";
		
		String rutaCuentasActivas="C:\\Users\\Ismael\\eclipse-workspace\\AccesoDatos\\src\\tareaLarga\\reportes\\cuentas_activas.txt";
		String rutaCuentasInactivas="C:\\Users\\Ismael\\eclipse-workspace\\AccesoDatos\\src\\tareaLarga\\reportes\\cuentas_inactivas_con_saldo.txt";
		String rutaResumenTipoCuentas="C:\\Users\\Ismael\\eclipse-workspace\\AccesoDatos\\src\\tareaLarga\\reportes\\resumen_por_tipo.txt";
		*/
		
		// Rutas relativas para los archivos
		String rutaCuentas = "src/tareaLarga/cuentas.dat";
		String rutaTrans = "src/tareaLarga/transacciones.dat";

		String rutaCuentasActivas = "src/tareaLarga/reportes/cuentas_activas.txt";
		String rutaCuentasInactivas = "src/tareaLarga/reportes/cuentas_inactivas_con_saldo.txt";
		String rutaResumenTipoCuentas = "src/tareaLarga/reportes/resumen_por_tipo.txt";

		//variables Cuenta
		int ultimoId;
		int id;
		String nombre;
		String tipoCuenta;
		double saldo;
		boolean estado;
		
		//variable de control id numerico
		//controlar que se introduc eun id numerico
		int newId=-1;
		
		//variable de control del menu => para salir Exit(indistinto mayusculas)
		String opcion;
		do {
			System.out.println();
			System.out.println();
			System.out.println("---------------------€€€€€€€€€€€---------------------");
			System.out.println("---------------------BANCA MARCH---------------------");
			System.out.println("---------------------$$$$$$$$$$$---------------------");
			System.out.println("1.- Crear cuenta");
			System.out.println("2.- Mostrar datos cuenta");
			System.out.println("3.- Modificar Cuenta");
			System.out.println("4.- Borrar Cuenta");
			System.out.println("5.- Consulta Saldo");
			System.out.println("6.- Registrar una transacción");
			System.out.println("7.- Mostrar todas las transacciones");
			System.out.println("8.- Listar transacciones dado un ID");
			System.out.println("9.- Reportes");
			System.out.println("-----------------------------------------------------");
			opcion=teclado.nextLine();
			System.out.println("-----------------------------------------------------");
			
			//contiene todas las opciones del menú
			switch (opcion){
				case "1":
					
					System.out.println("Has seleccionado introducir cuenta");
					System.out.println("-----------------------------------------------------");
					
					//variable de control
					//el usuario debe reafrimar los datos para que se agregen al fichero
					boolean datosCorrectos =false;
					
					//se realiza el bucle mientras usuario no introduzca un usuario correcto
					while(!datosCorrectos) {
						
						//se calcula el ultimo id utilizado y se le asigna a id
						ultimoId=Metodos.ultimoId(rutaCuentas, longitudFilaCuentas);
						id= CuentaBancaria.asignarId(ultimoId);
						
						//se obtiene el nombre
						nombre= CuentaBancaria.introducirNombre();
						System.out.println("-----------------------------------------------------");
						
						//se obtiene el tipo de cuenta
						tipoCuenta = CuentaBancaria.introducirTipoCuenta();
						System.out.println("-----------------------------------------------------");
						
						//se obtiene el saldo
						saldo = CuentaBancaria.introducirSaldo();
						System.out.println("-----------------------------------------------------");
						
						//se obtiene el estado
						estado = CuentaBancaria.introducirEstado();
						
						//se pide al usuario que vea si definitivamente quiere agregar los datos a la cuenta
						//si dice q no volvemos al principio
						System.out.println("¿Quiéres agregar la cuenta definitivamente? Si/No");
						String respuesta= teclado.nextLine();
						
						if(respuesta.equals("Si")||respuesta.equals("si")||respuesta.equals("SI")||respuesta.equals("S")||respuesta.equals("s")) {
							
							//variable de control del while parasalir de el 
							datosCorrectos=true;
							
							//guardamos en cuentas.dat
							Metodos.guardarCuenta(rutaCuentas, id, nombre, tipoCuenta, saldo, estado);
							
						}else {
							
							System.out.println("Introduce de nuevo los datos");
						}
						
						
					}
					
					break;
				case "2":
					
					//mostramos el listado de las cuentas
					System.out.println("------------------------------------------------------------------------------------------------------------------------|");	
					System.out.println("-----------------------------------------------LISTADO DE CUENTAS-------------------------------------------------------|");
					System.out.println("------------------------------------------------------------------------------------------------------------------------|");	
					
					//cabecera con formato
					String cabecera = String.format("%-20s%-5s%-20s%-5s%-20s%-5s%-20s%-5s%-20s%-5s",
					        "Id Cuenta","|", "Titular","|", "Tipo de Cuenta","|", "Saldo","|", "Estado de la Cuenta","|");
					
					// Mostrar la cabecera
					System.out.println(cabecera);
					System.out.println("------------------------------------------------------------------------------------------------------------------------|");
					
					//método que lee y muestra todas las cuentas registradas
					Metodos.leerCuentas(rutaCuentas);

					break;
					
				case "3":
					System.out.println("Modificar cuenta existente");
					System.out.println("-----------------------------------------------------");
					
					//método que nos permite controlar la introduccion de un id numerico
					//devuelve id introducido por teclado por el usuario
					newId=Control.controlIdNumerico();
					System.out.println("-----------------------------------------------------");

					//creamos un arrays guardará en la primera posicion el id de la cuenta.
					//si devuelve en la primera posición -1,el id no existe;  sino devuelve el id
					//la segunda posicion indica la posición del id
					int[][] idLista = Metodos.comprobarID(newId,longitudFilaCuentas,rutaCuentas);
					
					//si el id es 0 o mayor, indica que el id es correcto
					//ya que sabemos que si el id es -1 es pq la cuenta se ha borrado
					if(idLista[0][0]>=0) {
						
						//variable control del menú
						String opcion1="0";
							
						
						
						do {
							//menu con opciones
							System.out.println("Indica que deseas modificar:");
							System.out.println("1. Nombre.");
							System.out.println("2. Tipo de Cuenta.");
							System.out.println("3. Activa/Inativa.");
							System.out.println("4. Salir.");
							opcion1= teclado.nextLine();
							
							switch(opcion1) {
							case "1":
								
								//solicita el nombre
								System.out.println("introduce el nombre nuevo");
								String newNombre= teclado.nextLine();
								
								//metodo que modifica el nombre, recibe el nombre nuevo, el id de la cuenta, y la ruta de cuentas.das
								Metodos.modificarNombre(newNombre, idLista[0][1], rutaCuentas);
								
								break;
							case "2":
								
								//solciita el nuevo tipo de cuenta
								System.out.println("introduce el nuevo tipo de Cuenta");
								String newTipoCuenta= teclado.nextLine();
								
								//método que modificia el tipo de cuenta, recibe el nuevo tipo, el id, y la ruta de cuentas.dat
								Metodos.modificarTipoCuenta(newTipoCuenta, idLista[0][1], rutaCuentas);
								
								break;
								
							case "3":
								
								//solicta el nuevo estado
								Boolean newEstado= CuentaBancaria.introducirEstado();
								
								//método que modifica el estado de la cuenta, recibe el nuevo estado, el id y la ruta de cuentas.dat
								Metodos.modificarEstado(newEstado, idLista[0][1], rutaCuentas);
								
								break;
							case "4":
								//saldremos al menu principal
								System.out.println("-----------------------------------------------------");
								System.out.println("Volvemos a menú principal.");
								System.out.println("-----------------------------------------------------");
								break;
							default:
								//vovlemos menu modificar
								System.out.println("-----------------------------------------------------");
								System.out.println("Opción incorrecta.");
								System.out.println("-----------------------------------------------------");
								
								break;
							}
						}while(!opcion1.equals("4"));
						
							
						
					}
					
					break;
					
				case "4":
					//se borra una cuenta
					System.out.println("Borrar cuenta");
					
					//metodo que nos permite controlar la introduccion de un id numerico
					//devuelve id introducido por teclado por el usuario
					newId=Control.controlIdNumerico();
					
					//creamos un arrays guardará en la primera posicion el id de la cuenta 
					// si es -1, el id no existe
					//la segunda posicion indica la posición del id
					int[][] idBorrar = Metodos.comprobarID(newId, longitudFilaCuentas, rutaCuentas);
					
					//nos indica qque existe una cuenta con ese id
					if(idBorrar[0][0]>=0) {
						//registra transaccion como Cancelación de cuenta
						Metodos.Cancelacion("3", idBorrar[0][1], idBorrar[0][0], rutaTrans, rutaCuentas,longitudFilaCuentas, longitudFilaTransacciones);
						
						//metodo que borra la cuenta, recibe el id de la cuenta y la ruta de cuentas.dat
						Metodos.borrarCuenta(idBorrar[0][1], rutaCuentas);
					}
					
					break;
				case "5":
					//Consulta de saldo
					
					//metodo que nos permite controlar la introduccion de un id numerico
					//devuelve id introducido por teclado por el usuario
					newId=Control.controlIdNumerico();
					
					//creamos un arrays guardará en la primera posicion el id de la cuenta si es -1,
					//el id no existe
					//la segunda posicion indica la posición del id
					int[][] idSaldo = Metodos.comprobarID(newId, longitudFilaCuentas, rutaCuentas);
					
					//nos indica que la cuenta ha sido encontrada por el id
					if(idSaldo[0][0]>=0) {
						
						//metodo que recibe una posicion del fichero y devuleve la consuta de slado
						//recibe posicon y cuentas.dat
						Metodos.consultarSaldo(idSaldo[0][1], rutaCuentas);
						
					}else {
						
						//mensaje de error
						System.out.println("El id no se corresponde con ninguna cuenta");
					}
					
					break;
					
				case "6":
					
					System.out.println("Registrar una transacción");
					
					//metodo que nos permite controlar la introduccion de un id numerico
					//devuelve id introducido por teclado por el usuario
					newId=Control.controlIdNumerico();
				
					//primero comprobamos que la cuenta dada este activa
					//creamos un arrays que 
					//[0][0] contiene el id dado por el usaurio
					//[0][1] posicion de la cuenta enel fichero
					//[0][2] Actividad de la cuenta => 1 Activa ;; 2 Inactiva
					int[][] idTransaccion= new int[1][3];
					
					//comrpobamos la actividad de la cuenta
					//recibe por parametor el id, la longitud del fichero y la ruta del fichero cuentas.dat
					idTransaccion=Metodos.comprobarIdActividad(newId, longitudFilaCuentas, rutaCuentas);
					
					//indica que el id es correcto, siempre q sea mayor o ifual q 0 sino devuelve -1
					if(idTransaccion[0][0]>=0) {
						
						//indicamos si la cuenta esta activa dimos el valor 1 si estaba activa
						if(idTransaccion[0][2]==1) {
							
							//este string sirve para controlar un bucle donde se le preguinta
							//al usuario si quiere realizar la transaccion con los datos introducidos
							String transaccionCorrectaS="Incorrecta";
							
							//bucle de control => aprobacion usuario para realizar transaccion
							while(!transaccionCorrectaS.equals("Ok")) {
								
								//controlar el bucle siguiente
								String opcionDeposito="-1";
								
								//este bucle controla que el usuario elija debito o retiro
								while(!opcionDeposito.equals("1")&&!opcionDeposito.equals("2")) {
									
									//Menu opciones de deposito
									System.out.println("Indica el tipo de deposito");
									System.out.println("1. Deposito");
									System.out.println("2. Retiro");
									
									opcionDeposito= teclado.nextLine();
									
									//mensaje de error si el usairo introduce otra cosa que no sea 1-2
									if(!opcionDeposito.equals("1")&&!opcionDeposito.equals("2")) {
										System.out.println("Has introducido un distinto a 1 o 2");
										System.out.println("-----------------------------------------------------");
									}
								}
								
								//indica el monto, colocandolo en -1 nos sirve para controlar el bucle siguiente
								Double monto=-1.0;
								
								//bucle de control que nos garantiza q no se introduzcan montos no numerico
								while(monto==-1) {
									try {
										
										//no aceptamos monto negativos o 0
										do {
											//usuario introduce el monto
											System.out.println("Indica el monto");
											monto=teclado.nextDouble();
											
											//mensaje solo si el monto es menor o igual a 0
											if(monto<=0) {
												System.out.println("Has introducido monto negativo o 0.");
												System.out.println("-----------------------------------------------------");
												System.out.print("De nuevo ");
											}
										//hasta que el monto sea mayor a 0	
										}while(monto<=0);
										
										//limpiar buffer
										teclado.nextLine();
										
									}catch(InputMismatchException e) {					
										System.out.println("-----------------------------------------------------");
										System.out.println("Has introducido un valor no númerico");
										System.out.println("-----------------------------------------------------");
										System.out.print("De nuevo ");
										
										//limpiar buffer
										teclado.nextLine();
									}
								}
								
								
								//Mostramo por consola los datos
								System.out.println("Vas a realizar la siguiente transacción");
								System.out.println("-----------------------------------------------------");
								
								//muestra el ida
								System.out.println("Id Cuenta           =>  "+idTransaccion[0][0]);
								//ternario para escribir deposito o retiro
								String oD= (opcionDeposito.equals("1"))?"Deposito":"Retiro";
								System.out.println("Tipo de transacción =>  "+oD);
								
								//se muestra el monto
								System.out.println("Monto transacción   =>  "+monto);
								System.out.println("-----------------------------------------------------");
								
								//autorizacion para realizar la transaccion
								System.out.println("Autorizamos la transacción Ok??");
								
								transaccionCorrectaS=teclado.nextLine();
								
								//si el usuario autoriza...
								if(transaccionCorrectaS.equals("Ok")) {
									
									//metodo que recibe los datos y los guarda en transacciones.dat
									//devuelve un int que indica el tipo de transaccion Retiro(-1) o Deposito(1)
									int tipoDeposito=Metodos.tipoDeposito(opcionDeposito, monto, rutaTrans, idTransaccion[0][0], longitudFilaTransacciones);
									
									//actualizar saldo en cuentas.dat
									//si es cero error
									//sino indica el tipo de dpeosito realizado
									if(tipoDeposito!=0){
										
										//tipo de deposito 1 Deposito -1 Retiro
										Double saldoTrans = tipoDeposito*monto;
										
										//metodo que varia el saldo en cuentas .dat, recibe el slado nuevo, el id de la cuenta y la ruta de cuentas.dat
										Metodos.modificarSaldo(saldoTrans,idTransaccion[0][1], rutaCuentas);
									}
								}else {
									System.out.println("-----------------------------------------------------");
									//el usuario no ha pulsado ok para introducir la transaccion
									System.out.println("Introduce de nuevo los datos");
									System.out.println("-----------------------------------------------------");

								}
							}
							
					
						}else {
							System.out.println("----------------------------------------------------------------");
							//si la cuenta está inactiva no es posible realizar transaccion
							System.out.println("Cuenta inactiva no puedes realizar transacciones en esta cuenta.");
							System.out.println("-----------------------------------------------------------------");
						}
					}else {
						
						
						System.out.println("-----------------------------------------------------");
						//si el usuario introduce un id que no tiene cuenta
						System.out.println("Id incorrecto");
						System.out.println("-----------------------------------------------------");
					}
					
					break;
				case "7":
					//se meustran todas las transaccione
					System.out.println("------------------------------------------------------------------------------------------------------------------------|");					
					System.out.println("                                            LISTADO TRANSACCIONES                                                       |");
					System.out.println("------------------------------------------------------------------------------------------------------------------------|");					
					//cabecera con formato
					String cabeceraTrans = String.format("%-20s%-5s%-20s%-5s%-20s%-5s%-20s%-5s%-20s%-5s",
					        "Id Transaccion","|", "Id Cuenta","|", "Fecha","|", "Tipo de Transacción","|", "Monto","|");
					
					// Mostrar la cabecera
					System.out.println(cabeceraTrans);
					System.out.println("------------------------------------------------------------------------------------------------------------------------|");					
					
					//método que muetsra todas las transacciones
					Transacciones.mostrarTransacciones(rutaTrans);
					break;
					
				case "8":
					
					//metodo que nos permite controlar la introduccion de un id numerico
					//devuelve id introducido por teclado por el usuario
					int id_cuenta=Control.controlIdNumerico();
					
					//creamos el arraysList que guardar todas las posiciones en las que un id detemirnado hace transacciones
					//lo igualamos con un método que nos devuleve las posiicones
					//le pasamos id de la cuenta, ruta de y longitud de una linea de transacciones.dat
					ArrayList<Integer> posicionesTransaccionesID=Transacciones.buscarTransaccionesId(id_cuenta, rutaTrans, longitudFilaTransacciones);
					
					//si el arrays devuelto es mayor a 0 es que tiene contenido
					if(posicionesTransaccionesID.size()>0) {
						
						//método que muestra por pantalla todas las transacciones realizdas
						//recibe un arrayList de posiciones, la longitud y la ruta de transacciones.dat
						Transacciones.listarTransacciones(posicionesTransaccionesID, longitudFilaTransacciones, rutaTrans);
					
					}else {
						//el arrays habra venido vacio, mensaje de error
						System.out.println("------------------------------------");
						System.out.println("No existen transacciones para ese ID o el ID no se encuentra registrado en el sistema");
						System.out.println("------------------------------------");
					}
					
					break;
					
				case "9":
					
						//menu Reportes
						System.out.println("Menú Reportes");
						System.out.println("Reporte 1.- Lista de Cuentas activas");
						System.out.println("Reporte 2.- Lista de Cuentas inactivas con saldo distinto de cero");
						System.out.println("Reporte 3.- Movimiento de cuenta dado el nombre");
						System.out.println("Reporte 4.- Resumen financiero por tipo de cuenta");
						System.out.println("Reporte 5.- Transacciones realizadas en una fecha");
						System.out.println("Reporte 6.- Saldo en una fecha concreta");
						
						//opcion para controlar el menú
						opcion = teclado.nextLine();
						
						switch(opcion) {
							case "1":
									//mostramos todas las cuentas activas
									System.out.println("-----------------------------------------------------------------------------------------------------------------------------|");
									System.out.println("                                             LISTADO DE CUENTAS ACTIVAS                                                      |");
									
									//metodo que introduce en un arraysList todas las posiciones de las lineas donde comienzan las cuentas activas
									//recibe ruta y longitud de cuentas.dat
									ArrayList<Integer> listaActiva=	Reportes.cuentasActivas(rutaCuentas, longitudFilaCuentas);
									
									//metodo que muestra las listas activas
									//recibe ruta de cuentas.dat, la nueva ruta cuentas_activas.txt donde se guradran los reusltados 
									//y la lista con las posicones de comienzo de la slineas con cuenta activa
									Reportes.mostrarListasActivas(rutaCuentas, rutaCuentasActivas, listaActiva);
				
								break;
								
							case "2":
									//mostramos todas las cuentas inactivas
									System.out.println("|----------------------------------------------------------------------------------------------------------------------------|");
									System.out.println("|                                    LISTADO CUENTAS INACTIVAS DISTINTAS A CERO                                              |");
									
									//metodo que introduce en un arraysList todas las posiciones de las lineas donde comienzan las cuentas inactivas
									//recibe ruta y longitud de cuentas.dat
									ArrayList<Integer> listaInactiva=	Reportes.cuentasInactivas(rutaCuentas, longitudFilaCuentas);
									
									//metodo que muestra las listas inactivas
									//recibe ruta de cuentas.dat, la nueva ruta cuentas_inactivas.txt donde se guradran los reusltados 
									//y la lista con las posicones de comienzo de la slineas con cuenta activa
									Reportes.mostrarListasInactivas(rutaCuentas, rutaCuentasInactivas, listaInactiva);
								
								break;
							case "3":
								
								System.out.println("-----------------------------------");
								System.out.println("Movimiento de cuenta dado el nombre");
								System.out.println("-----------------------------------");
								//se introuce el nombre
								System.out.println("Introduce el nombre");
								String nombreBuscar = teclado.nextLine();
								
								//metodo que devuelve el id; si es -1 ese nombre no tiene cuenta;
								//sino es el id referente a la cuenta del usuario identificado por nombre
								//recibe el nombre a buscar, ruta y longitud de cuentas.dat
								int id_Usuario=Reportes.IdentificarCuentaNombre(nombreBuscar, rutaCuentas, longitudFilaCuentas);
								
								//si el id es mayor o igual a 0  es que el nombre tiene una cuenta registrada
								if(id_Usuario>=0) {
									
									//método nos devuelve un arrays con las posiciones donde ese idCuenta esta en transacciones
									//recibe id del nombre introducido, ruta y longitud de transacciones.dat
									ArrayList<Integer> listaTrans=Transacciones.buscarTransaccionesId(id_Usuario, rutaTrans, longitudFilaTransacciones);
									
									//si la lista es 0 es que esta vacia, el nombre utilizado, existe pero no ha relizado transacciones
									if(listaTrans.size()==0) {
										System.out.println("el cliente "+nombreBuscar+" no ha efectuado ninguna transacción.");
									}else {
										//metodo que muestra y guarda enmovimiento_(id del usuario).txt las transacciones realizadas por el usuario
										//recibe ruta y longityd e transacciones.dat, el arryalist con las posiciones del comienzo
										//de las lineas dela stransacciones, y el nombre.
										Reportes.movCuentas(rutaTrans, longitudFilaTransacciones, listaTrans,nombreBuscar);
									}
									
									
								}else {
									System.out.println("-------------------------------------------");
									//mensaje si el nombre introducido no tiene cuenta
									System.out.println("Ese nombre no tiene ninguna cuenta asociada");
									System.out.println("-------------------------------------------");
								}
								
								
								break;
							case "4":
								
								//resumen por tipod e cuenta
						        System.out.println("|----------------------------------------------------------------------|");
								System.out.println("|                  Resumen financiero por tipo de cuenta               |");
								
								//metodo, que guarda en resumen_por_tipo.txt, los tipos de cuentas que hay con total de saldo y contaodr
								//recibe por parámetros ruta y longitud de cuentas.dat y la ruta resumen_por_tipo.txt
								Reportes.resumenTipoCuenta(rutaCuentas, longitudFilaCuentas, rutaResumenTipoCuentas);
								break;
							
							case "5":
								//transacciones por una fecha dada
								System.out.println("---------------------------------");
								System.out.println("Transacciones por fecha");
								System.out.println("---------------------------------");
								
								//se introduce día
								System.out.println("Introduce día");
								String dia=teclado.nextLine();
								//se le pone un 0 antes si usaurio solo introduce un dígito
								if(dia.length()==1) {
									dia="0"+dia;
								}
								
								//se introduce el mes
								System.out.println("Introduce mes");
								String mes=teclado.nextLine();;
								//se le pone un 0 antes si usaurio solo introduce un dígito
								if(mes.length()==1) {
									mes="0"+mes;
								}
								
								//se introduce el anno
								System.out.println("Introduce anno");
								String anno=teclado.nextLine();;
								
								String fecha=dia+"/"+mes+"/"+anno;
								
								//esta fecha es la que nos servirá para indicar la ruta, ya que las / son carcater de escpe y 
								//da problemas al generar las rutas
								String fechaRuta= dia+"_"+mes+"_"+anno;
								
								//metodo que confirma que la fecha es correcta.
								//si devuelve null fecha incorrecta
								LocalDate fechaCorrecta=Reportes.validarFecha(fecha);
								
								if(fechaCorrecta!=null) {
									//este método devuelve y guarda en transacciones_(fecha).txt las transacciones realizadas en una fecha concreta
									Reportes.identificarFechas(fecha, rutaTrans, longitudFilaTransacciones,fechaRuta );
								
								}
								
								break;
								
							case "6":
								//Saldo en una fecha concreta
								System.out.println("---------------------------------");
								System.out.println("Saldo en una fecha concreta");
								System.out.println("---------------------------------");
								
								//se introduce el día
								System.out.println("Introduce día");
								dia=teclado.nextLine();
								//se introduce un 0 en caso del usuario metadia del 1-9
								if(dia.length()==1) {
									dia="0"+dia;
								}
								
								//se introduce el mes
								System.out.println("Introduce mes");
								mes=teclado.nextLine();;
								
								//se introduce un 0 si el usaurio mete un mes del 1-9
								if(mes.length()==1) {
									mes="0"+mes;
								}
								
								//cariable controla el bucle
								int anio = 0;
								
								//realizamos un bucle para evitar que se metan años anteriores a la realización
								//del programa, ya que despúes se comprueba todos los días en busca de posibles transacciones
								//evitamos que se metan annos que no tenemos registrados.
								do {
									//se solicita el año
									System.out.println("Introduce anno");
									anno=teclado.nextLine();
									
									//se pasa la fecha a int para comprobar que no se ha introducido ningun caracter extraño
									try {
										anio=Integer.parseInt(anno);
										
										//si el año es anterior se pedira de nuevo
										if(anio<=2023) {
											System.out.println("Sólo se pueden consultar saldos desde 2023");
										}
									}catch(InputMismatchException e){
										
										//se ha inrodcudio letras
										System.out.println("Has introducido letras");
									}
									
								}while(anio<=2022);
								
								//hacemis un string que podamos comparar con el formato de datos de fechas que llevamos relaizando
								fecha=dia+"/"+mes+"/"+anno;
								
								//validamos la fecha como correcta
								fechaCorrecta=Reportes.validarFecha(fecha);
								
								//creamos un string con el formato necesario para guardarlo en txt ya que las / son carcater de escaape y da errores
								fechaRuta= dia+"_"+mes+"_"+anno;
				
								//nos indica si existen transferencias
								int posicionFechaInicio=-1;
								
								//si la fecha no es nula entramos en el bucle
								if(fechaCorrecta != null) {
									
									//fecha de hoy con formato (año,mes,dia)
									LocalDate hoy =LocalDate.now();
									
									// Definir nuestra fecha introducida por usuario con formato(año,mes,dia)
							        LocalDate fechaAntigua = LocalDate.of(Integer.parseInt(anno), Integer.parseInt(mes), Integer.parseInt(dia));  
							        
									//comprobamos que la fecha tiene transacciones y nos da la primera posicion de la linea
									//donde comienzan las transacciones eso nos devuelve un int igual o distinto a 0.
							        //si la fecha no tuviera transaccion pasarriamos a los diguientes días del calendario, hasta llegar al día de hoy
							        //buscando transacciones.
									do{
										
										//metodo que devuelve la psoicion donde se enceunetra la primera transaccion relaizada en esa fecha y que se encunetra
										//en el fichero transacciones.dat
										//si devuelve -1 es que no hay coincidencias
										posicionFechaInicio= Reportes.verFechaTransacciones(rutaTrans, longitudFilaTransacciones, fechaCorrecta);
										 
										// Sumar 1 día a la fecha
								        fechaAntigua = fechaAntigua.plusDays(1);
								        
								        //sumamos 1 día a la fecha que se introduce en la funcion
								        fechaCorrecta=fechaCorrecta.plusDays(1);
								        
								    //se realizara mientras no haya coincidencias o fecha antigua no sean iguales a hoy
									}while(posicionFechaInicio==-1&&(hoy.isAfter(fechaAntigua))); 
									
									
									//indica que exite transferencia entre el día seleccionado y el día de hoy
									//posiconFechainicio indica donde comienza la linea en transaciones.dat
									if(posicionFechaInicio>=0) {
										
										//suma de las transacciones desde la fecha hasta hoy
										//recibe la posicion del fichero donde comienza, la ruta y la longitud de fila de transacciones.dat
										double sumaTransacciones=Reportes.sumatoriotransaccionesFecha(posicionFechaInicio, rutaTrans, longitudFilaTransacciones);
										
										//metodo que suma el total de saLdo de las cuentas
										//recibe ruta y longitud de fila de cuentas.dat
										double sumaTotal=Reportes.saldoTotalHoy(rutaCuentas, longitudFilaCuentas);
										
										//metodo que guarda en historico_financiero_(fechaRuta).txt los datso recabados
										//recibe fechaintroducida por usaurio, el total de cuentas y la suma de las transaciones
										Reportes.guardarArchivo(fechaRuta, sumaTotal, sumaTransacciones);
									}else {
										//la suma de transacciones es cero
										//metodo que suma el total de saLdo de las cuentas
										//recibe ruta y longitud de fila de cuentas.dat
										double sumaTotal=Reportes.saldoTotalHoy(rutaCuentas, longitudFilaCuentas);
										Reportes.guardarArchivo(fechaRuta, sumaTotal, 0);
									}
									
								}else {
									//fecha incorrrecta
									System.out.println("Fecha invalida");
								}
									
								break;
							default:
								//mensaje error menu reportes
								System.out.println("Opción incorrecta");
								
								break;
						}
					break;
			
				default:
					//mensaje error menu principal
					System.out.println("Opción incorrecta");
					
					break;
			}
		//condicon pra salir del program
		}while(!opcion.toLowerCase().equals("exit"));
    	
	}

}


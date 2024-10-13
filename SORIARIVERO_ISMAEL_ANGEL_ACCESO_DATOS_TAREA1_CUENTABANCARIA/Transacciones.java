package tareaLarga;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Transacciones {
	
	/**
	 * Muestra todas las transacciones presentes en el archivo de transacciones.
	 * Para cada transacción, muestra el ID de transacción, el ID de cuenta, la fecha,
	 * el tipo de transacción y el monto.
	 *
	 * @param rutaTrans  Ruta del archivo binario que contiene las transacciones.
	 */	
	public static void mostrarTransacciones(String rutaTrans) {
		try(RandomAccessFile aleatorio = new RandomAccessFile(rutaTrans,"rw")){
			
			while(aleatorio.getFilePointer()<aleatorio.length()) {
				//id transaccion
				int id=aleatorio.readInt();
				int idCuenta=aleatorio.readInt();
				
				StringBuilder fecha = new StringBuilder();
				for(int i=0; i<10;i++) {
					fecha.append(aleatorio.readChar());
				}
				
				String fechaS = fecha.toString().trim();
				
				StringBuilder tipoTrans = new StringBuilder();
				for(int i=0; i<15;i++) {
					tipoTrans.append(aleatorio.readChar());
				}
				
				String tipoTransS =tipoTrans.toString().trim();
				
				Double monto=aleatorio.readDouble();
				String barra="|";
				
				// Formato de columnas: int + String + String + double + boolean
		        String reporte = String.format("%-20d%-5s%-20d%-5s%-20s%-5s%-20s%-5s%-20.2f%-1s",
		                id,
		                barra,
		                idCuenta,
		                barra,
		                fechaS.toString().trim(),
		                barra,
		                tipoTransS.toString().trim(),
		                barra,
		                monto,
		                barra);
		        
				System.out.println(reporte);
				System.out.println("------------------------------------------------------------------------------------------------------------------------|");	

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Busca las posiciones de las transacciones asociadas a un usuario específico, 
	 * identificado por su ID de usuario, dentro del archivo de transacciones.
	 * 
	 * @param id_Usuario                ID del usuario cuya transacción se va a buscar.
	 * @param rutaTrans                 Ruta del archivo binario que contiene las transacciones.
	 * @param longitudFilaTransacciones  Longitud en bytes de cada fila (registro) de transacciones.
	 * @return                          Una lista con las posiciones de las transacciones del usuario encontrado.
	 */
	public static ArrayList<Integer> buscarTransaccionesId(int id_Usuario, String rutaTrans, int longitudFilaTransacciones){
		ArrayList<Integer> posicionesTransaccionesID= new ArrayList<>();
		int posicion=4;
		
		try(RandomAccessFile aleatorio = new RandomAccessFile(rutaTrans,"rw")){
			
			//mientras la posicion donde coloquemos el puntero no sea mayor a la longitud del fichero
			while(posicion<aleatorio.length()) {
				
				//colocamos el punterol
				aleatorio.seek(posicion);
				
				
				//id_cliente el que se obtien del documento transacciones.data
				int id_cliente=aleatorio.readInt();
				
				if(id_cliente==id_Usuario) {
					//la posicion a guardar es la del puntero menos las dos id, cliente y transaccion
					int pos=(int)(aleatorio.getFilePointer())-8;
					
					posicionesTransaccionesID.add(pos);
					 
				}
				//colocamos el puntero en la psocion que queremos
				posicion+=longitudFilaTransacciones;
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return posicionesTransaccionesID;
	}
	
	/**
	 * Muestra en formato tabular un listado de las transacciones que corresponden a las posiciones
	 * proporcionadas en la lista de posiciones. Para cada transacción, se muestra el ID de transacción,
	 * el ID de cuenta, la fecha, el tipo de transacción y el monto.
	 *
	 * @param posicionesTransaccionesID  Lista con las posiciones de las transacciones que se van a mostrar.
	 * @param longitudFilaTransacciones  Longitud en bytes de cada fila (registro) de transacciones.
	 * @param rutaTrans                  Ruta del archivo binario que contiene las transacciones.
	 */
	public static void listarTransacciones(ArrayList<Integer> posicionesTransaccionesID, int longitudFilaTransacciones, String rutaTrans) {
		System.out.println("------------------------------------------------------------------------------------------------------------------------|");					
		System.out.println("                                            LISTADO TRANSACCIONES                                                       |");
		System.out.println("------------------------------------------------------------------------------------------------------------------------|");					
		
		String cabeceraSaldo = String.format("%-20s%-5s%-20s%-5s%-20s%-5s%-20s%-5s%-20s%-5s", 
                "Id Transacción", "|", "Id Cuenta", "|", "Fecha","|", "Tipo de Transacción","|", "Monto","|");

		System.out.println(cabeceraSaldo);
		System.out.println("------------------------------------------------------------------------------------------------------------------------|");					

		
		try(RandomAccessFile aleatorio = new RandomAccessFile(rutaTrans,"rw")){
			
			//tenemos que ir calculando las posiciones del puntero
			for(int i=0; i<posicionesTransaccionesID.size();i++) {
				
				//colocamos el puntero en el valor añadido al array
				aleatorio.seek(posicionesTransaccionesID.get(i));
				
				//id transaccion
				int Id_Transaccion=aleatorio.readInt();
				int Id_Cuenta=aleatorio.readInt();
				
				StringBuilder fecha = new StringBuilder();
				for(int j=0; j<10;j++) {
					fecha.append(aleatorio.readChar());
				}
				
				
				StringBuilder tipoTrans = new StringBuilder();
				for(int k=0; k<15; k++) {
					tipoTrans.append(aleatorio.readChar());
				}
				
								
				Double montoD =aleatorio.readDouble();
				
				String barra="|";
				// Formato de columnas: int + String + String + double + boolean
		        String reporteLT = String.format("%-20d%-5s%-20d%-5s%-20s%-5s%-20s%-5s%-20.4f%-5s",
		        		Id_Transaccion,
		                barra,
		                Id_Cuenta,
		                barra,
		                fecha.toString().trim(),
		                barra,
		                tipoTrans.toString().trim(),
		                barra,
		                montoD,
		                barra);
		        
				System.out.println(reporteLT);
				System.out.println("------------------------------------------------------------------------------------------------------------------------|");					

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
}

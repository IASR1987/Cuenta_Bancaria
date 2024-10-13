package tareaLarga;

import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class CuentaBancaria {
	
	static Scanner teclado = new Scanner(System.in);
	
	 public static int asignarId(int ultimoId) {
		 return ultimoId++;
	 }
	
	public static String introducirNombre() {
		StringBuffer buffer;
		System.out.println("Introduce nombre");
		String nombre = teclado.nextLine();
		buffer=new StringBuffer(nombre);
    	buffer.setLength(50);
    	
    	return buffer.toString();
	}
	
	public static String introducirTipoCuenta() {
		StringBuffer buffer;
		System.out.println("Introduce tipo de cuenta");
		String tipoCuenta = teclado.nextLine();
		buffer=new StringBuffer(tipoCuenta);
    	buffer.setLength(20);
    	
    	return buffer.toString();
	}
	
	public static double introducirSaldo() {
		Double saldo=null;
		while(saldo==null) {
			
			try {
				System.out.println("Introduce el saldo");
				saldo = teclado.nextDouble();
				
				//limpiamos el buffer		
				teclado.nextLine();;
			}catch(InputMismatchException e) {
				System.out.println("Has introducido un valor no númerico en saldo");
				System.out.println("-----------------------------------------------------");
				//limpiamos el buffer para cuando se inserte valor no númerico		
				teclado.nextLine();
			}
		}
		
    	return saldo;
	}
	
	public static boolean introducirEstado() {
		Boolean estado=null;
		String estadoS;
		
		while(estado==null) {
			try {
				System.out.println("Introduce el estado de la cuenta Activa/Inactiva");
				estadoS=teclado.nextLine();
				if(estadoS.equals("Activa")) {
					estado = true;
				}else if(estadoS.equals("Inactiva")) {
					estado = false;
				}else {
					throw new IllegalArgumentException();
				};
				
			}catch(IllegalArgumentException e) {
				System.out.println("Has introducido un valor distinto a Activa/Inactiva");
				System.out.println("-----------------------------------------------------");
			}
			
		}
		
    	return estado;
	}
}
	


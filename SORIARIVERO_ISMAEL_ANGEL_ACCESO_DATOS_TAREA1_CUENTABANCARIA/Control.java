package tareaLarga;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Control {
	static Scanner teclado = new Scanner(System.in);
	
	public static int controlIdNumerico() {
		//controlamos que no se introduzcan id no numerico
		int newId=-1;
		
		while(newId==-1) {
			try {
				System.out.println("introduce el id con el que vamos a operar");
				newId=teclado.nextInt();
				//limpiar buffer
				teclado.nextLine();
			}catch(InputMismatchException e) {
				System.out.println("Has introducido un valor no númerico");
				System.out.println("-----------------------------------------------------");
				//limpiar buffer
				teclado.nextLine();
			}
		}
		
		return newId;
		
	}
}

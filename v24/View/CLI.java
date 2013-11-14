package v24.View;

import java.io.File;
import java.util.Scanner;

public class CLI {
	
	private String fileName;
	private String saveName;
	
	/**
	 * Erstellt eine CLI mit Introtext und bereitet die Konsole auf die Abfrage der Daten
	 * zum laden und speichern der Konfiguration vor.
	 */
	public CLI(){
		System.out.println(" ******************\n *The GAME OF LIFE*\n ******************\n");
		System.out.println("To start the Game, please open a preset World by entering the file Location:\n");
		
		// Liest eine Eingabe von der Konsole aus und speichert sie in "fileName"
		Scanner scan = new Scanner(System.in);
		fileName = scan.nextLine();
		
		System.out.println("Geben sie jetzt einen Speicherort für die neue Konfigurationsdatei an:\n");
		
		// Liest eine Eingabe von der Konsole aus und speichert sie in "saveName"
		saveName = scan.nextLine();
		
		scan.close();
		
	}
	
	/**
	 * Gibt einen String auf der Konsole aus. Kann benutzt werden, um Fehlermeldungen
	 * auszugeben.
	 * 
	 * @param string String der auf der Konsole ausgegeben werden soll
	 */
	public void print(String string){
		System.out.println(string);
	}
	
	
	/**
	 * Gibt die aktuelle Konfiguration der Zellen auf die Konsole auf.
	 * 
	 * @param state Integer Array, in dem die Werte der Konfiguration und die Koordinaten gespeichert sind.
	 */
	private void printCellState(int[] state){
		int i = 4;
		
		System.out.println("world-width =" + state[0]);
		System.out.println("world-height =" + state[1]);
		System.out.println("infinite=" + state[2]);
		System.out.println("generation=" + state[3]);
		System.out.println("Coordinates of the living cells:");
		while(state[i] < state.length){
			System.out.println(state[i] + "," + state[i+1]);
			i=i+2;
		}
	}
	
	/**
	 * 
	 * @return Gibt die File zurück, aus der die Datei geladen werden soll
	 */
	public File getFileName(){
		return new File(fileName);
	}
	
	/**
	 * 
	 * @return Gibt den String zurück, unter dem die Datei gespeichert werden soll
	 */
	public String getSaveName(){
		return saveName;
	}
	
	/**
	 * Liest die gewünschte Anzahl der Generationen von der Konsole ein, falls diese in der Datei nicht angegeben sind.
	 * @return Anzahl der Generationen
	 *//*
	public int getGenerationConsole(){
		System.out.println("Geben sie bitte die Anzahl der Generationen ein (muss eine gültige Zahl sein): \n");
		int generation = 0;

		try{
			Scanner scan = new Scanner(System.in);
			generation = scan.nextInt();
		}catch(Exception e){
			System.out.println("Es sind nur gültige Zahlen erlaubt.");
		}
		return generation;
	}
	
	/**
	 * Liest die Art der Welt (Geschlossen/Offen) von der Konsole ein, falls diese in der Datei nicht angegeben sind.
	 * @return Geschlossene (false) oder unendliche Welt (true) 
	 *//*
	public boolean getInfiniteConsole(){
		System.out.println("Geben Sie an, ob die Welt geschlossen oder unendlich sein soll.\n(0 für geschlossen und 1 für unendlich):");
		boolean infinite = false;
		
		try{
			Scanner scan = new Scanner(System.in);
			if(scan.nextInt() == 0){
				infinite = false;
			}
			if(scan.nextInt() == 1){
				infinite = true;
			}
		}catch(Exception e){
			System.out.println("Geben Sie entweder 0 für geschlossen oder 1 für unendlich ein.");
		}
		return infinite;
	}
	*/
}

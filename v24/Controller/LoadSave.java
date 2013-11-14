package v24.Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;

import v24.Model.Controller;

public class LoadSave {
	
	String saveName;
		String[] config; 	//Array, das die Konfiguration als String enthŠlt
	int[] parsedConfig;	//die aus der Datei ausgelesenen Werte
	Controller controller;
	
	public LoadSave(String targetFile, Controller contr){
		saveName = targetFile;
		controller = contr;
	}
	

	/**
	 * Speichert die aktuelle Konfiguration der Welt in eine Textdatei, wie eine Konfigurationsdatei.
	 * Gleichzeitig wird der Zustand auf der Kosole ausgegeben.
	 */
	public void saveConfig() {
		try {
			
			if(!saveName.endsWith(".txt")){
				String newSaveName = (saveName + ".txt");
				saveName = newSaveName;
			}
			BufferedWriter textFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveName)));
			
			/*
			 * Speichert die Werte in einem BufferedWriter und gibt gleichzeitig die Konfiguration
			 * auf der Konsole aus.
			 */
			textFile.write("x="+controller.model.world.getSizeX()); 
			textFile.newLine(); 
			
			textFile.write("y="+controller.model.world.getSizeY()); 
			textFile.newLine(); 
			
			if(controller.model.world.getInfinite() == false){
				textFile.write("infinite=0"); 
				textFile.newLine(); 
			}else{
				textFile.write("infinite=1"); 
				textFile.newLine(); 
			}
			textFile.write("generation="+controller.model.world.getGeneration()); 
			textFile.newLine(); 
			
			for(int i=0; i<controller.model.world.getSizeX(); i++){
				for(int j=0; j<controller.model.world.getSizeY(); j++){
					if (controller.model.world.cellIsAlive(i, j)==true){
						textFile.write((i) + "," + (j)); textFile.newLine();
					}
				}
			}
			textFile.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Speichert die aktuelle Konfiguration der Welt in eine Textdatei, wie eine Konfigurationsdatei.
	 * Gleichzeitig wird der Zustand auf der Kosole ausgegeben.
	 */
	public void printConfig() {
		System.out.println("Calculating done...\n");
		
		/*
		 * Gibt die Konfiguration auf der Konsole aus.
		 */
		System.out.println("Breite der Welt: "+controller.model.world.getSizeX());
		System.out.println("Höhe der Welt: "+controller.model.world.getSizeY());
		
		if(controller.model.world.getInfinite() == false){
			System.out.println("Art der Welt: begrenzt");
		}else{
			System.out.println("Art der Welt: offen");
		}
		System.out.println("Es wurden "+controller.model.world.getGeneration()+" Generationen berechnet");
		
		System.out.println("Koordinaten der lebenden Zellen(x,y):");
		for(int i=0; i<controller.model.world.getSizeX(); i++){
			for(int j=0; j<controller.model.world.getSizeY(); j++){
				if (controller.model.world.cellIsAlive(i, j)==true){
					System.out.println((i) +"," + (j));
				}
			}
		}
		
		// #### ALTERNATIVE AUSGABE ####
		System.out.println();
		if(controller.model.world.getSizeX() <= 20 && controller.model.world.getSizeY() <= 20){
			for(int i=0; i<controller.model.world.getSizeY(); i++){
			 
				for(int j=0; j<controller.model.world.getSizeX(); j++){
					if (controller.model.world.cellIsAlive(j, i)==true){
						System.out.print("1");
					}
					else System.out.print("0");
						
						
				}
				System.out.println("");
			}
		}else{
			System.out.println("Die Welt ist leider zu groß für die grafische Ausgabe :(\n"+
					"Es werden nur Welten mit einer maximalen Größe von 20x20 dargestellt.");
		}
		
	}

	/**
	 * Liest eine Konfigurationsdatei und speichert jede Zeile in einem Array.
	 * Wird von parseConfigArray(String[] config) verarbeitet.
	 * 
	 * @param fileName Datei, die ausgelesen werden soll.
	 * @return config String[] das jede Zeile der Konfiguration enthält.
	 */
	public void loadFile(File file){
			
			String stringLine = null;		// String der jeweils eingelesenen Datei
			int counter = 0;				// zählt die Zeilen der Datei
			int i = 0;						// Laufvariable paramter
			
			try {
				BufferedReader countLines = new BufferedReader(new FileReader(file));
				
				/*
				 * Zählt die Zeilen der Datei um das Array mit der richtigen Länge zu initialisieren
				 */
				while ((stringLine = countLines.readLine()) != null) counter++;
				
				/*
				 * neuen BufferedRead erstellen um die Zeilen einzulesen.
				 * Array mit Anzahl der Zeilen zu erstellen.
				 */
				BufferedReader readString = new BufferedReader( new FileReader(file));
				config = new String[counter];
				
				/*
				 * Liest Jede Zeile und liest die Datei in config[]
				 */
				while ((stringLine = readString.readLine()) != null) {
					config[i] = stringLine;
					i++;
				}
						
				// schließt die BufferedReader
				readString.close();
				countLines.close();
				
			} catch (FileNotFoundException e) {
				System.out.println("Die Angegebene Datei wurde nicht gefunden");	
			} catch (IOException e2){
				System.out.println("Es ist ein Fehler bei der Verarbeitung der Datei aufgetreten.");
			}
		}
		
	/**
	 * Liest die Daten aus dem String[], der die Konfiguration der Welt enthält.
	 * Die Koordinaten werden ab parsedConfig[4] abwechselnd mit x und y Koordinate angegeben.
	 * Beispiel: 
	 * parsedConfig[4] = x1
	 * parsedConfig[5] = y1
	 * parsedConfig[6] = x2
	 * parsedConfig[7] = y2
	 * 
	 * Eine Konfigurationsdatei wird gültig eingelesen, wenn sie zB nach dem folgenden Schema notiert wurde:
	 * 
	 * x=12
	 * y=32
	 * (infinite=1) // optional, wenn nciht angegeben dann ist infinite = 0
	 * generation=5
	 * 1,3
	 * 22,3
	 * 23,4
	 * 77,0
	 * 
	 * @param config Array, dass die Konfiguration der Welt als String enthält.
	 * @return parsedConfig Liefert eine Array mit den Werten für Breite x, Höhe y, infinite(1/0), generation n, Koordinaten.
	 */
	public void parseConfigArray(String[] config){
		
		Vector<Integer> parsed = new Vector<Integer>();
		int j = 0; // Laufvariable für configArray
		
		// liest die Breite und Höhe der Welt aus
		if((config[j].toCharArray()[0] != 'x') && (config[j].toCharArray()[1] != '=')){
			parsed.set(0,-1);	// falls beim parsen ein Fehler eintritt, wird als erste Stelle im Vector eine -1 gesetzt
		}
		else{
			parsed.add(Integer.parseInt(config[j].substring(2, config[j].length())));
			j++;
		}
		if((config[j].toCharArray()[0] != 'y') && (config[j].toCharArray()[1] != '=')){
			parsed.set(0,-1); // falls beim parsen ein Fehler eintritt, wird als erste Stelle im Vector eine -1 gesetzt
		}
		else{
			parsed.add(Integer.parseInt(config[1].substring(2, config[1].length())));
			j++;
		}
		
		// liest aus, ob eine offene oder geschlossene Welt eingetragen ist
		if(config[j].contains("infinite=1")){
			parsed.add(1);
			j++;
		}
		else if(config[j].contains("infinite=0")){
			parsed.add(0);
			j++;
		}
		else{
			parsed.add(-2); // -2 gibt an, ob ein Wert von der Konsole eingelesen werden muss oder nicht
		}
		
		// liest die Anzahl der Generationen aus, die berechnet werden sollen
		if(config[j].contains("generation=")){
			parsed.add(Integer.parseInt(config[j].substring(11)));
			j++;
		}
		else{
			parsed.add(0);	// wird generation = 0 eingetragen, wird später eine Abfrage von der Konsole aufgerufen.
		}
		/*
		 * Speichert ein Koordinatenpaar in zwei folgende Plätze in Vector parsed
		 */
		while(j < config.length){
			int q = 0;
			
			String tempString = config[j];
			while(tempString.substring(q, q+1).equals(",")== false) q++; // Anzahl der Stellen der x-Koordinate ermitteln
			
			parsed.add(Integer.parseInt(config[j].substring(0,q)));
			parsed.add(Integer.parseInt(config[j].substring(q+1, tempString.length()))); // Um y-Koordinate einzulesen wird Sub-String ab dem Komma bis zum Ende des Strings gelesen und in int umgewandelt
			j++;
		}
		
		/*
		 * wandelt den Vector in ein normales Array um
		 */
		parsedConfig = new int[parsed.size()];
		for(int i=0; i<parsedConfig.length; i++){
			parsedConfig[i] = parsed.get(i);
		}
		
		/*
		int anzahl_parameter = 0;
		if(config[2].contains("infinite")) anzahl_parameter++;
		if(config[3].contains("generation")) anzahl_parameter++;
		
			parsedConfig = new int[4 + 2*(config.length-(2+anzahl_parameter))]; // Genug Platz für alle Parameter hihi (Koordinatenpaar braucht je 2 Speichplätze im Array)
			int j = 0;		// Laufvarioable um die aktuelle Stelle im Array config zu bestimmen (falls kein Infinite angegeben, beginnen die Koordianten bei j=3)
			int i = 4;		// Laufvariable für das Array parsedConfig (erste 4 Stellen sind für x,y,infinite,gerneration reserviert)
			
			if((config[0].toCharArray()[0] != 'x') && (config[0].toCharArray()[1] != '=')){
				parsedConfig[0] = -1; // -1 wird eingetragen, wenn beim parsen ein Fehler aufgetreten ist
			}
			else{
				parsedConfig[0] = Integer.parseInt(config[0].substring(2, config[0].length())); // TODO check: geht das so, mit .length? oder muss es .length()+/-1 sein?
				j++;  
			}
			if((config[1].toCharArray()[0] != 'y') && (config[1].toCharArray()[1] != '=')){
				parsedConfig[0] = -1;
			}
			else{
				parsedConfig[1] = Integer.parseInt(config[1].substring(2, config[1].length())); // TODO genau wie bei x oben, wir hier nur eine einstellige Zahl erzeugt, also maximal 9... ändern!!
				j++;
			}
			if(config[2].contains("infinite=1")){
				parsedConfig[2] = 1;
				j++;
			}
			else if(config[2].contains("infinite=0")){
				parsedConfig[2] = 0;
				j++;
			}
			else{
				parsedConfig[2] = -2; // -2 gibt an, ob ein Wert von der Konsole eingelesen werden muss oder nicht
			}
			if(config[3].contains("generation=")){
				parsedConfig[3] = Integer.parseInt(config[3].substring(11));
				j++;
			}
			else{
				parsedConfig[3] = 0;	// wird generation = 0 eingetragen, wird später eine Abfrage von der Konsole aufgerufen.
			}
	
			while(j < config.length){
				int q = 0;
				
				String tempString = config[j];
				while(tempString.substring(q, q+1).equals(",")== false) q++; // Anzahl der Stellen der x-Koordinate ermitteln
				
				parsedConfig[i] = Integer.parseInt(config[j].substring(0,q))-1;
				
				
				parsedConfig[i+1] = Integer.parseInt(config[j].substring(q+1, tempString.length()))-1; // Um y-Koordinate einzulesen wird Sub-String ab dem Komma bis zum Ende des Strings gelesen und in int umgewandelt
				j++;
				i = i+2;
			}
		*/
	}

	public int[] getConfig(File file) {
		loadFile(file);
		parseConfigArray(config);
		return parsedConfig;
	}
}

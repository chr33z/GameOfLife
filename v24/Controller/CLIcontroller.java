package v24.Controller;

import java.io.File;
import v24.Model.Controller;
import v24.Model.Model;
import v24.Model.ModelAbstract;
import v24.Model.ModelBitset;
import v24.View.CLI;

/**
 * Klasse Kontroller
 * Steuert das Programm und verändert die entsprechenden Programmteile
 * 
 * 
 * @author Christopher Gebhardt, Felix Manke
 * @date 10/23/2010
 * 
 *
 */
public class CLIcontroller extends Controller {

	CLI cli;
	
	private File fileName;		// Speicherort der Konfigurationsdatei
	private String saveName;		// Wohin die berechnete Konfiguration gespeichert werden soll

	private int sizeX = 40;		// Breite der Spielwelt
	private int sizeY = 40;		// Höhe der Spielwelt
	private boolean infinite;	// Status, ob die Welt geschlossen oder unendlich ist
	private int generation;				// Anzahl der zu spielenden Generationen
	
	public CLIcontroller(int paramInfinite, int paramGeneration){
		
		// erstellt Model und CLI
		model = new ModelBitset(sizeX, sizeY);
		cli = new CLI();
		
		// fragt Speicherort und Konfigurationsdatei ab
		fileName = cli.getFileName();
		saveName = cli.getSaveName();
		
		//liest infinite und generation ein, falls eingegeben
		if(paramInfinite == 1){
			this.infinite = true;
		}else if(paramInfinite == 0){
			this.infinite = false;
		}else{
			this.infinite = false;
			System.out.println("Keine gültige Eingabe für Parameter infinite. Es wird eine geschlossene Welt benutzt.");
		}
		
		try{
			generation = paramGeneration;
		}catch(Exception e){
			generation = 0;
		}
		
		// Läd die Datei, parsed die Konfiguration in einen String und verarbeitet die Daten
		LoadSave loadSave = new LoadSave(saveName, this);
		loadSave.getConfig(fileName);
		
		// Ändert die Konfiguration der Welt mit der geladenen Datei
		initializeWorld(loadSave.getConfig(fileName));
	
		
		/*
		// Nun müssen die Attribute für die Zellen in abhängigkeit Ihrer Nachbarn ermittelt werden => ganz normaler Generationenwechsel
		model.newGeneration();
		model.world.setGeneration(0);  // bei newGeneration wurde die generation um 1 erhöt... hier wird sie wieder auf 0 gesetzt
		*/
		
		/* an diesem Punkt ist die Welt initiiert (mit den Werten aus der Datei)
		 * nun muss sie n generationen weiterentwickelt werden, je nach anzahl der gewünschten generationen...
		 */
		while(generation > 0){
			model.newGeneration();
			generation--;
		}
		
		// Speichert die Ausgabe in der angegebenen Datei und gibt die Konfiguration auf der Konsole aus
		loadSave.saveConfig();
		loadSave.printConfig();
		
		
	}
	
	
	/**
	 * Initialisiert eine neue Welt mit den Parametern im config Array.
	 * Die ersten 4 Werte sind breite,höhe,infinite(1/0),generation.
	 * Danach folgen abwechselnd x- und y-Koordianten.
	 * 
	 * @param config Array, dass die Parameter zum erstellen einer neuen Welt enthält.
	 */
	public void initializeWorld(int[] config){
		// Laufvariable für config[] um die Koordianten auszulesen
		int k = 4;
		
		// Überprüft, ob beim parsen ein Fehler aufgetreten ist
		if(config[0] == -1){
			cli.print("Die Konfigurationsdatei ist fehlerhaft");
		}
		
		sizeX = config[0];
		sizeY = config[1];
		
		if(config[2] == 0){
			infinite = false;
		}
		if(config[2] == 1){
			infinite = true;
		}
		
		if(config[3] == 0){
			if(generation == 0){
				System.out.println("Spiel konnte nicht gestartet werden, da keine Anzahl an Generationen zur Berechnung angegeben wurden.");
				System.exit(0);
			}
		}else{
			generation = config[3];
		}
		
		model.changeWorld(sizeX, sizeY);
		model.world.setInfinite(infinite);
		
		// Setzt Zellen an den angegebenen Koordinaten auf "lebend";
		while(k < config.length){
			model.world.setCellAlive(config[k], config[k+1]);
			k=k+2;
		}
		// Nun müssen die Attribute für die Zellen in abhängigkeit Ihrer Nachbarn ermittelt werden => ganz normaler Generationenwechsel
		model.newGeneration();
		model.world.setGeneration(0);
	}
}

package v24.Controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import v24.Model.Controller;
import v24.Model.Model;
import v24.Model.ModelBitset;
import v24.View.GUI;
import v24.View.PaintGrid;

/**
 * Klasse Controller zur Steuerung von View und Model nach dem MVC (Model-View-Controller Design-Pattern)
 * 
 * @author Felix Manke, Christopher Gebhardt
 *
 */
public class GUIcontroller extends Controller implements ActionListener, ChangeListener, MouseListener, ComponentListener {
	
	GUI gui;
	int b = 0; // Hilfsvariable zur Steuerung der Optik des Play-Buttons
	int c = 0;	// Hilfsvariable für Generationen-Zähler
	int d = 0;	// Infinite Hilfsvariable
	
	int sizeX = 85;
	int sizeY = 50;
	boolean infinite = false;
	int generation;
	int speed = 500; // Geschwindigkeit des Generationenwechsels
	
	boolean mousePressed;
	
	Timer timer;
	Timer timerMouse;							// Steuer das "malen" auf der Welt
	boolean firstCellLiving = false;		//erste Zelle beim anklicken lebend?
	
	/**
	 * Konstruktor für ein Objekt der Klasse Controller.
	 */
	public GUIcontroller(){
		
		// erstellt Model und GUI
		model = new ModelBitset(sizeX,sizeY);
		gui = new GUI(this);
		
		model.addObserver(gui);
		gui.setVisible(true);
		
		timer = new Timer(speed, this);
		timer.setActionCommand("itsTime");
		timer.setRepeats(false);
		
		// Steuer das "malen" auf der Welt
		timerMouse = new Timer(10, this);
		timerMouse.setActionCommand("timerMouse");
		timerMouse.setRepeats(true);
	}
	
	private void initializeWorld(int[] livingCells) {
		
		int i = 0;
		int j = 1;
		model.world.setInfinite(infinite);
		while(j <= livingCells.length){
			model.world.setCellAlive(livingCells[i], livingCells[j]);
			i+=2;
			j+=2;
		}
	}
	

	public void actionPerformed(ActionEvent event) {
		String cmd = event.getActionCommand();
		
		// ************************************************************
		// * Action-Command-Handling für Menüleiste und Kontrollpanel *
		// ************************************************************
		
		
		// FileChooser
		if(cmd.equals("oeffnen")){
			resetPlayButton();  
			try{
				//FileChooser erstellen
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileFilter() {
	                public boolean accept(File f) {
	                    return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
	                }
	                public String getDescription() {
	                    return "Text-Dateien(*.txt)";
	                }
	            });

				
				int returnVal = fc.showOpenDialog(null);
				
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
				
					LoadSave loadSave = new LoadSave(fc.getSelectedFile().toString(), this);
					int[] config = loadSave.getConfig(file);
									
					// Überprüft, ob beim parsen ein Fehler aufgetreten ist
					if(config[0] == -1){
						gui.popupWarning("Fehler beim Laden der Datei", "Die Konfigurationsdatei ist fehlerhaft");
					}
					
					int newSizeX = config[0];
					int newSizeY = config[1];
					
					if(config[2] == -2){
				//		infinite = cli.getInfiniteConsole();
					}else{
						if(config[2] == 0){
							infinite = false;
							model.world.setInfinite(false);
						}
						if(config[2] == 1){
							infinite = true;
							model.world.setInfinite(true);
						}
					}
					if(config[3] == 0){
				//		generation = cli.getGenerationConsole();
					}else{
						generation = config[3];
					}
					
					for(int l=0;l<config.length; l++){
						System.out.print(config[l] + " ");
					}
					
					// erstellt ein Array mit den Koordinaten der lebenden Zellen
					int[] newLivingCells = new int[(config.length)-4];
					
					int j = 0;
					for(int i=4; i<config.length; i++){
						newLivingCells[j] = config[i];
						j++;
					}
					
					for(int l=0;l<newLivingCells.length; l++){
						System.out.print(newLivingCells[l] + " ");
					} 
					//nach Laden der Datei wird die Weltgrš§e angepasst und die neuen Zellen gesetzt
					 model.createWorld(newSizeX, newSizeY);
				     initializeWorld(newLivingCells);
				     gui.paintGrid.setCellArray(model.world.getCellArray());
				     gui.paintGrid.setSizeX(model.world.getSizeX());
				     gui.paintGrid.setSizeY(model.world.getSizeY());
				     gui.paintGrid.setPreferredSize(gui.paintGrid.getWorldSize());
				     
				     sizeX = newSizeX;
				     sizeY = newSizeY;
				     checkWorldSize();
				     gui.resizeWindow();
					}
				
				} catch (Exception e){
					System.out.println("Fehler beim Laden der Datei...");
					e.printStackTrace();
				}
			}
		
		// Speichern im Menü angeklickt
		
		if(cmd.equals("speichere")){
			resetPlayButton();  
			try{
				//FileChooser erstellen
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Speichern");
				fc.setApproveButtonText("Speichern");
			
				fc.setFileFilter(new FileFilter() {
	                public boolean accept(File f) {
	                    return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
	                }
	                public String getDescription() {
	                    return "Text-Dateien(*.txt)";
	                }
	            });
				
				int returnVal = fc.showOpenDialog(null);
				
				if(returnVal == JFileChooser.APPROVE_OPTION){
					LoadSave loadSave = new LoadSave(fc.getSelectedFile().toString(), this);
					loadSave.saveConfig();
				}
			
			}catch (Exception e){
			System.out.println("Fehler beim Speichern der Datei...");
			e.printStackTrace();
		}
		}
		
		// Timer zur zeitverzögerten berechnung der nächsten Geeration
		if(cmd.equals("itsTime")){
				
			if(c==1 && gui.isPlaying == true){
				if(generation<=0){
					timer.stop();
					resetPlayButton();
				}else{
					paintGeneration();
					generation--;
					gui.textfieldGeneration.setText(Integer.toString(generation));
					timer.setInitialDelay(speed);
					timer.start();
				}
			}else if(gui.isPlaying == true){
				paintGeneration();
				timer.setInitialDelay(speed);
				timer.start();
			}
			else{
				timer.stop();
			}
		}
		
		
		// Play Button angeklickt
		if (cmd.equals("play")){
			
			model.newGeneration();
			
			if(b==0){
			System.out.println("running"); // Hilfsausgabe 
			gui.buttonPlay.setIcon(gui.iconPause);
			gui.buttonPlay.setRolloverIcon(gui.iconPauseMo);
			gui.buttonPlay.setPressedIcon(gui.iconPauseOk);
			gui.isPlaying = true;
			b=1;
				/*
				 * Fragt die gewünschten Generationen vom Benutzer ab.
				 * Falls eine ungültige Eingabe auftritt, wird eine Fehlermeldung ausgegeben
				 * und die Werte werden zurückgesetzt.
				 */
				if(c==1){
					if(gui.getGenerations() != -1 || gui.getGenerations() >= 0){
						generation = gui.getGenerations();
						System.out.println(speed);
						timer.setInitialDelay(speed);
						timer.start();
					}else{
						resetPlayButton();
						gui.popupWarning("Anzahl der Generationen (="+gui.textfieldGeneration.getText()+ ") wird nicht erkannt. Bitte eine korrekte Zahl eingeben!", "Fehler!");
						gui.textfieldGeneration.setText("");
					}
					System.out.println(generation);
				}
				else{  // Falls Generationen-Button nicht aktiviert, läuft die Berechnung, bis Play gedrückt wird (oder stop...)
					timer.setInitialDelay(speed); 
					timer.start();
				}
				
			}
			else{
				resetPlayButton();
			}
		}
		
		
		// Generation-Button angeklickt
		if (cmd.equals("generationOnOf")){
			
			if(c==0){
			gui.buttonGeneration.setIcon(gui.iconGenerationOn);
			gui.buttonGeneration.setRolloverIcon(gui.iconGenerationOnMo);
			gui.buttonGeneration.setPressedIcon(gui.iconGenerationOnOk);
			gui.labelGenerationen.setEnabled(true);
			gui.textfieldGeneration.setEditable(true);
			System.out.println("Generation an");
			c=1;
			
			resetPlayButton();
			}
			else{
				gui.buttonGeneration.setIcon(gui.iconGenerationOf);
				gui.buttonGeneration.setRolloverIcon(gui.iconGenerationOfMo);
				gui.buttonGeneration.setPressedIcon(gui.iconGenerationOfOk);
				gui.labelGenerationen.setEnabled(false);
				gui.textfieldGeneration.setEditable(false);
				System.out.println("Generation aus");
				c=0;
				gui.textfieldGeneration.setText("");
				
				resetPlayButton();
			}
			model.helpSetChanged();
			
		}
		
		
		// Stop Button angeklickt
		if (cmd.equals("resetButton pressed")){
			
			resetPlayButton();
			model.createWorld(sizeX, sizeY);
			gui.paintGrid.setCellArray(model.world.getCellArray());
			gui.paintGrid.setSizeX(model.world.getSizeX());
		    gui.paintGrid.setSizeY(model.world.getSizeY());
		    gui.paintGrid.setPreferredSize(gui.paintGrid.getWorldSize());
			gui.paintGrid.repaint();
			gui.resizeWindow();
			
			
			System.out.println("Reset-Button aktiviert");
		}
		
		// Infinite Button angeklickt
		if (cmd.equals("infiniteButton pressed")){
			if(d == 0){
				gui.buttonInfinite.setIcon(gui.iconInfiniteOn);
				gui.buttonInfinite.setRolloverIcon(gui.iconInfiniteOnMo);
				gui.buttonInfinite.setPressedIcon(gui.iconInfiniteOnOk);
				
				model.world.setInfinite(true);
				gui.optionsArray[0] = "true";
				d = 1;
			}else{
				gui.buttonInfinite.setIcon(gui.iconInfiniteOf);
				gui.buttonInfinite.setRolloverIcon(gui.iconInfiniteOfMo);
				gui.buttonInfinite.setPressedIcon(gui.iconInfiniteOfOk);
				
				model.world.setInfinite(false);
				gui.optionsArray[0] = "false";
				d = 0;
			}
			model.helpSetChanged();
		}
		
		/*
		 * Malt die Zellen auf die Welt indem bei jedem Event die Koordinaten bestimmt werden
		 * und die Zelle je nachdem ob die erste Zelle am Leben war oder tot, gemalt oder gelöscht wird.
		 */
		if (cmd.equals("timerMouse")){
			
			/*
			 * Steht in try-catch, da beim verlassen der Maus aus der Welt eine IndexOutOfBounds-Exception geworfen wird.
			 * Diese beeinträchtigt aber in keiner Weise den Verlauf des Programms
			 */
			try{
				// Hole die Position der Maus und Rechne sie in x-, y-Koordinaten für die Zellen um
				Point pos = gui.panelWelt.getMousePosition();
				int cellX = ((int)pos.getX()) / (gui.paintGrid.getCellSize()+gui.paintGrid.getBorderSize());
				int cellY = ((int)pos.getY()) / (gui.paintGrid.getCellSize()+gui.paintGrid.getBorderSize());
				
				/*
				 *  Male oder lösche Zellen in abhängigkeit, welcher Zustand am Anfang angeklickt wurde.
				 *  Fängt man mit leeren Zellen an, wird gemalt, bei lebenden Zellen wird gelöscht :)...
				 */
				if(!firstCellLiving){
					if(!model.world.cellIsAlive(cellX, cellY)){
						model.world.setCellAlive(cellX, cellY);
					}
				}else{
					if(model.world.cellIsAlive(cellX, cellY)){
						model.world.setCellAlive(cellX, cellY);
					}
				}
			}catch(Exception e){
				
			}
			gui.paintGrid.setCellArray(model.world.getCellArray());
			gui.paintGrid.repaint();
		}
		
		
		// Neu im Menü angeklickt
		if(cmd.equals("neu")){
			gui.showNewWorldPane(this);
		}
		// Beenden im Menü angeklickt (Datei->Beenden)
		if (cmd.equals("ende")){
			if(gui.popupConfirm(	"Programm wirklich beenden?\n" +
								"(Ungespeicherte Daten gehen verloren...)", "Beenden?")==0){
				System.exit(0);
			}
		}
		
		// Über-Eintrag im Menü angeklickt:
		if (cmd.equals("ueber")){
			gui.popupInfo(	"Game Of Life\n\n" +
							"\u00a9 2010\n\n" +
							"Autoren: Ayfer Aslan, Christopher Gebhardt, Felix Manke\n\n" +
							"Nach der Idee und den Regeln des \"Game Of Life\" von John Horton Conway (1970)\n\n", "\u00dcber GameOfLife");
		}
		
		// Spielregeln-Eintrag im Menü angeklickt
		if (cmd.equals("regeln")){
			gui.popupInfo(	"Spielregeln und Mechanismen des \"Game Of Life\"\n\n" +
							"\"Game Of Life\" ist ein, vom Mathematiker John Horton Conway 1970 entwickeltes System, \n" +
							"das auf einem zellulären Automaten basiert. \n" +
							"Zellen in diesem System werden mit der Eigenschaft \"lebend\" oder \"tot\" belegt und \n" +
							"entwickeln sich nach einigen einfachen Regeln in jeder Generation weiter.\n\n" +
							"Die Regeln lauten:\n " +
							"* Eine tote Zelle mit genau drei lebenden Nachbarn wird in der Folgegeneration neu geboren.\n" +
							"* Lebende Zellen mit weniger als zwei lebenden Nachbarn sterben in der Folgegeneration an Einsamkeit.\n" +
							"* Eine tote Zelle mit genau drei lebenden Nachbarn wird in der Folgegeneration neu geboren.\n" +
							"* Lebende Zellen mit mehr als drei lebenden Nachbarn sterben in der Folgegeneration an Überbevölkerung.\n" +
							"\n" +
							"Aus diesen Regeln können sehr komplexe Muster und Beziehungen entstehen, \n" +
							"die den Reiz dieses \"Spiels\" ausmachen.", 
							"Spielregeln");
		}
		
		
		
		// *******************************************
		// * Action Commands für EinstellungsFenster *
		// *******************************************
		
		
		// Einstellungen-Eintrag im Menü angeklickt:
		if (cmd.equals("einstellen")){
			gui.showOptionPane(this);
		}
		
		// Radio Button Begrenzt im Einstellungsfenster im Bereich "Welt" angeklickt
		if (cmd.equals("optionenWeltBegrenzt")){
			gui.optionsArray[0] = "false";
		}
		
		
		// Radio Button Offen im Einstellungsfenster im Bereich "Welt" angeklickt
		if (cmd.equals("optionenWeltOffen")){
			gui.optionsArray[0] = "true";
		}
		
		
		// Haken bei Checkbox "an" im Einstellugnsfenster im Bereich Erweiterte Zelldarstellung angeklickt
		if (cmd.equals("optionsExpertMode")){
			if (gui.optionsArray[3] == "true")gui.optionsArray[3] = "false";
			else gui.optionsArray[3] = "true";
		}
		
		// Button Model Wahl gedrückt
		if(cmd.equals("modelwahl")){
			gui.dialogOptions.dispose();
			gui.showModelWahl(this);
		}
		
		if (cmd.equals("ChooseColorCell")){
			
			Color newColor = JColorChooser.showDialog(gui, "Wähle die Zellenfarbe", gui.paintGrid.getColorIsAlive());
			Color bgColor = gui.paintGrid.getColorIsDead();
			
			// die Methode mixColors mischt die Farben der geborenen und sterbenden Zellen mit der Hintergrundfarbe
			if(newColor != null){
				gui.paintGrid.setColorIsAlive(newColor);
				gui.paintGrid.setColorWillDie(mixColors(newColor, bgColor, 0.3));
				gui.paintGrid.setColorWillBeBorn(mixColors(newColor, bgColor, 0.74));
			}
		}
		
		if (cmd.equals("ChooseColorBorder")){
			Color newColor = JColorChooser.showDialog(gui, "Wähle die Zellenfarbe", gui.paintGrid.getColorBorder());
			
			if(newColor != null){
				gui.paintGrid.setColorBorder(newColor);
			}
		}
		
		if (cmd.equals("ChooseColorBG")){
			Color newColor = JColorChooser.showDialog(gui, "Wähle die Zellenfarbe", gui.paintGrid.getColorIsDead());
			
			// Wird die Hintergrundfarbe geändert, muss auch die Farbe der "transparenten" Zellen geändert werden
			if(newColor != null){
				gui.paintGrid.setColorIsDead(newColor);
				gui.paintGrid.setColorWillDie(mixColors(gui.paintGrid.getColorIsAlive(),newColor, 0.3));
				gui.paintGrid.setColorWillBeBorn(mixColors(gui.paintGrid.getColorIsAlive(), newColor, 0.74));
			}
		}
		
		// Button Ok im Einstellungsfenster angeklickt
		if (cmd.equals("optionDialogOk")){
			
			if(gui.popupConfirm("Änderungen übernehmen und Einstellungsfenster schließen?", "Einstellungen übernehmen?")==0){
				gui.dialogOptions.dispose();
				if(gui.optionsArray[0].equals("true")){
					model.world.setInfinite(true);
					gui.buttonInfinite.setIcon(gui.iconInfiniteOn);
					gui.buttonInfinite.setRolloverIcon(gui.iconInfiniteOnMo);
					gui.buttonInfinite.setPressedIcon(gui.iconInfiniteOnOk);
					d = 1;
				}
				if(gui.optionsArray[0].equals("false")){
					model.world.setInfinite(false);
					gui.buttonInfinite.setIcon(gui.iconInfiniteOf);
					gui.buttonInfinite.setRolloverIcon(gui.iconInfiniteOfMo);
					gui.buttonInfinite.setPressedIcon(gui.iconInfiniteOfOk);
					d = 0;
				}
				gui.paintGrid.setCellSize(Integer.parseInt(gui.optionsArray[1]));
				gui.paintGrid.setBorderSize(Integer.parseInt(gui.optionsArray[2]));
				gui.panelAlles.setSize(gui.paintGrid.getWorldSize());		// Ändert die Fenstergröße nachdem die Zellenbreite verändert wurde
				gui.paintGrid.repaint();
				
				if(gui.optionsArray[3].equals("true")){
					gui.paintGrid.setExpertMode(true);
				}
				if(gui.optionsArray[3].equals("false")){
					gui.paintGrid.setExpertMode(false);
				}
				gui.update(model, this);
				gui.resizeWindow();
				
			}
		}
		
		// Button Abbrechen im Einstellungsfenster angeklickt
		if (cmd.equals("optionDialogAbbrechen")){
			gui.dialogOptions.dispose();
		}
		
		// **********************************
		// * Action-Commands für Model-Wahl *
		// **********************************
		
		// Array im Einstellugsfenster im Bereich Wahl der Berechnungsmethode gewählt
		if (cmd.equals("array")){
			gui.modelIsBitset = false;
		}
		// Bitset in den Einstellungen gewählt
		if (cmd.equals("bitset")){
			gui.modelIsBitset = true;
		}
		// Button OK in ModelWalh gedrückt
		if (cmd.equals("modelWahlOK")){
			if(gui.popupConfirm("Sicher, dass das Model ausgetauscht werden soll?", "Warnung!")==0){
				if(gui.modelIsBitset==true){
					ModelBitset modelTemp = new ModelBitset(model.world.getSizeX(), model.world.getSizeY());
					
					for(int i = 0; i<model.world.getSizeX(); i++){
						for(int j = 0; j<model.world.getSizeY(); j++){
							if(model.world.getCellArray()[i][j].getIsAlive()==true){
								modelTemp.world.setCellAlive(i, j);
							}

						}
					}
					modelTemp.world.setGeneration(model.world.getGeneration());
					modelTemp.world.setInfinite(model.world.getInfinite());
					
					model = modelTemp;
					model.addObserver(gui);
					
					gui.setTitle("Game of Life (Bitset)");
					System.out.println(model.getClass()); // TODO hilfsausgabe löschen
				}
				if(gui.modelIsBitset==false){
					Model modelTemp = new Model(model.world.getSizeX(), model.world.getSizeY());
					
					for(int i = 0; i<model.world.getSizeX(); i++){
						for(int j = 0; j<model.world.getSizeY(); j++){
							if(model.world.getCellArray()[i][j].getIsAlive()==true){
								modelTemp.world.setCellAlive(i, j);
							}

						}
					}
					
					modelTemp.world.setGeneration(model.world.getGeneration());
					modelTemp.world.setInfinite(model.world.getInfinite());
					
					model = modelTemp;
					model.addObserver(gui);
		
					gui.setTitle("Game of Life (Array)");
					System.out.println(model.getClass()); // TODO hilfsausgabe löschen
				}
				
				gui.paintGrid.setCellArray(model.world.getCellArray());
				gui.dialogModel.dispose();
				
			}
		}
		if (cmd.equals("modelWahlAbbrechen")){
			gui.dialogModel.dispose();
			if(model.getClass()== ModelBitset.class){
				gui.modelIsBitset=true;
			}else gui.modelIsBitset=false;
		}
		
		// ************************************
		// * Action-Commands für neue Welt... *
		// ************************************
		
		
		// Radio Button "offen" im Neue-Welt-Fenster im Bereich "Art der Welt" angeklickt
		if(cmd.equals("newWorldOpen")){
			gui.newWorldIsInfinite = true;
		}
		
		// Radio Button "geschlossen" im Neue-Welt-Fenster im Bereich "Art der Welt" angeklickt
		if(cmd.equals("newWorldClosed")){
			gui.newWorldIsInfinite = false;
		}
		
		// Button "Ok" im Neue-Welt-Fenster angeklickt
		if(cmd.equals("newWorldOk")){
			resetPlayButton(); 
			model.createWorld(Integer.parseInt(gui.textFieldBreite.getText()), Integer.parseInt(gui.textFieldHoehe.getText()));
		    model.world.setInfinite(gui.newWorldIsInfinite);
		    	//Infinite Button anpassen
		    if(model.world.getInfinite()==true){
		    gui.buttonInfinite.setIcon(gui.iconInfiniteOn);
			gui.buttonInfinite.setRolloverIcon(gui.iconInfiniteOnMo);
			gui.buttonInfinite.setPressedIcon(gui.iconInfiniteOnOk);
		    }else{
			gui.buttonInfinite.setIcon(gui.iconInfiniteOf);
			gui.buttonInfinite.setRolloverIcon(gui.iconInfiniteOfMo);
			gui.buttonInfinite.setPressedIcon(gui.iconInfiniteOfOk);
		    }
			
			gui.paintGrid.setCellArray(model.world.getCellArray());
		    gui.paintGrid.setSizeX(model.world.getSizeX());
		    gui.paintGrid.setSizeY(model.world.getSizeY());
		    gui.paintGrid.setPreferredSize(gui.paintGrid.getWorldSize());
		    sizeX = Integer.parseInt(gui.textFieldBreite.getText());
		    sizeY = Integer.parseInt(gui.textFieldHoehe.getText());
		    checkWorldSize();
		    gui.resizeWindow();
		    gui.dialogNewWorld.dispose();
		    
		}
		
		// Button "Abbrechen" im Neue-Welt-Fenster angeklickt
		if(cmd.equals("newWorldAbbr")){
			gui.dialogNewWorld.dispose();
		}
	}
		
	
	
	
	/**
	 *  Bewirkt die Berechnung der folgenden Generation innerhalb des Models.
	 *  Daraufhin wird die Welt von der View neu gezeichnet.
	 */
	private void paintGeneration() {
		model.newGeneration();
	}
	
	/**
	 * Pausiert die laufende Berechnung von Generationen und sorgt dafür,
	 * dass der Play Button seine Optik entsprechend ändert.
	 */
	private void resetPlayButton(){
		gui.buttonPlay.setIcon(gui.iconPlay);
		gui.buttonPlay.setRolloverIcon(gui.iconPlayMo);
		gui.buttonPlay.setPressedIcon(gui.iconPlayOk);
		System.out.println("paused"); // Hilfsausgabe TODO löschen
		gui.isPlaying = false;
		b=0;
	}

	@Override
	/**
	 * ermittelt die Position des Mauszeigers und errechnet die Adresse der Zelle.
	 * Danach wird der Status der Zelle auf lebendig oder tot geändert.
	 */
	public void mouseClicked(MouseEvent arg0) {
		/*
		Point pos = gui.panelWelt.getMousePosition();
		
		System.out.println("x pos " + ((int)pos.getX()-5));
		System.out.println("y pos " + ((int)pos.getY()-5));
		System.out.println("Zelle: " + (((int)pos.getX()-5) / (gui.paintGrid.getCellSize()+gui.paintGrid.getBorderSize())) +","+ ((int)(pos.getY()-5) / (gui.paintGrid.getCellSize()+gui.paintGrid.getBorderSize())));
		
		// Minus 5 bei den MausKoordinaten, weil das PanelWelt nicht bei 0,0 im Fenster beginnt, sondern erst bei 5,5 (vielleicht kann man das eleganter lösen)
		model.world.setCellAlive((((int)pos.getX()-5) / (gui.paintGrid.getCellSize()+gui.paintGrid.getBorderSize())), ((int)(pos.getY()-5) / (gui.paintGrid.getCellSize()+gui.paintGrid.getBorderSize())));
		gui.paintGrid.repaint();
		*/
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	/*
	 * Hilft beim "malen" in der Welt. Zuerst wird der Status der ersten geklicketen Zelle bestimmt
	 * um festzulegen ob gemalt oder gelöscht wird.
	 */
	public void mousePressed(MouseEvent arg0) {
			Point pos = gui.panelWelt.getMousePosition();

			int posX = ((int)pos.getX()) / (gui.paintGrid.getCellSize()+gui.paintGrid.getBorderSize());
			int posY = ((int)pos.getY()) / (gui.paintGrid.getCellSize()+gui.paintGrid.getBorderSize());
			
			try{
				firstCellLiving = model.world.cellIsAlive(posX, posY);
			}catch(Exception e){
			}
			
			timerMouse.start();
	}

	// Hilft beim "malen" mit der Maus
	public void mouseReleased(MouseEvent arg0) {
			timerMouse.stop();
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		JSlider source = (JSlider)event.getSource();
		if(!source.getValueIsAdjusting()){
			int val = source.getValue();
			if(source.getName() == "cellSize") gui.optionsArray[1] = Integer.toString(val);
			if(source.getName() == "borderSize") gui.optionsArray[2] = Integer.toString(val);
			if(source.getName() == "speed") speed = val;
			
			System.out.println(source.getName());
			System.out.println(val);
		}

		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		
		try{
			int cellSpace = gui.paintGrid.getCellSize()+(gui.paintGrid.getBorderSize());
			
			int numberOfCellsX = (((gui.panelWelt.getWidth()))/cellSpace);
			int numberOfCellsY = (((gui.panelWelt.getHeight()))/cellSpace);
			model.changeWorld(numberOfCellsX, numberOfCellsY);
			System.out.println("weltgröße geändert");
			gui.paintGrid.setCellArray(model.world.getCellArray());
			gui.paintGrid.setSizeX(model.world.getSizeX());				// Weltgröße übergeben
			gui.paintGrid.setSizeY(model.world.getSizeY());
			gui.paintGrid.setPreferredSize(gui.paintGrid.getWorldSize());
			gui.update(model, this);
			
			sizeX = numberOfCellsX;
			sizeY = numberOfCellsY;
			
			System.out.println("Breite: " + model.world.getSizeX());
			System.out.println("Höhe: " + model.world.getSizeY());
		}
		catch(Exception ex){
			
		}
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		
	}
	
	public void checkWorldSize(){
	
		int diffWidth = (int)new Dimension(gui.getSize()).getWidth()-gui.panelWelt.getWidth();
		int diffHeight = (int)new Dimension(gui.getSize()).getHeight()-(gui.panelSteuerung.getHeight()+gui.panelWelt.getHeight());
		int i=0;
		
		while(i!=1){
			if(gui.paintGrid.getWorldSize().height>(Toolkit.getDefaultToolkit().getScreenSize().getHeight()- diffHeight)    ||   gui.paintGrid.getWorldSize().width>(Toolkit.getDefaultToolkit().getScreenSize().getWidth()- diffWidth) ){
	      
				
				if(gui.paintGrid.getBorderSize()>1){
					gui.paintGrid.setBorderSize(gui.paintGrid.getBorderSize()-1);
				}else{
					if(gui.paintGrid.getCellSize()>=1){
					gui.paintGrid.setCellSize(gui.paintGrid.getCellSize()-1);
						if(gui.paintGrid.getCellSize()==0){
							gui.paintGrid.setCellSize(10);
							gui.popupWarning("Welt zu Groß!", "Fehler!");
							i=1;
							model.changeWorld(85, 50);
						}
					}
				}
			}else{
				
				i=1;
			}
		//gui.setSize((gui.paintGrid.getWidth()+diffWidth),(gui.panelSteuerung.getHeight()+gui.paintGrid.getHeight()+diffHeight));	
		}
	}
	
	/**
	 * Mischt zwei Farben anhand eines Prozentualen Wertes, sodass sich die jeweiligen RGB Werte der Farben annähern
	 * @param a Color a
	 * @param b Color b
	 * @param amount Prozentualer Wert
	 * @return Die gemischte Farbe
	 */
	private Color mixColors(Color a, Color b, double amount){
		
		int newRed = a.getRed()-( (int)(( a.getRed()-b.getRed() )*amount) );
		
		int newGreen = a.getGreen()-( (int)(( a.getGreen()-b.getGreen() )*amount) );
		
		int newBlue = a.getBlue()-( (int)((a.getBlue()-b.getBlue())*amount) );
		
		return new Color(newRed, newGreen, newBlue);
	}
}

package v24.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.Hashtable;
import java.util.Observer;
import java.util.Observable;
import javax.swing.*;

import v24.Controller.GUIcontroller;
import v24.Model.ModelAbstract;

/**
 * Klasse View zur Darstellung des / Interaktion mit einem Objekt der Klasse Model
 * mittels einer grafischen Benutzeroberfläache (GUI).
 * 
 * Von ihr wird die Klasse PaingGrid benutzt um die Welt mit ihren Zellen darzustellen.
 * 
 * @see PaintGrid
 * @author Felix Manke, Christopher Gebhardt, Ayfer Aslan
 *
 */
public class GUI extends JFrame implements Observer{

	private static final long serialVersionUID = 1L; // Musste sein... kein Plan, wie wir die belegen :)
	public Dimension minimumWindowSize, windowSize, contentSize;
	
	JMenuBar menuBar;
	JMenu menuDatei, menuExtras, menuHilfe;
	public JDialog dialogOptions, dialogModel, dialogNewWorld;
	public JPanel panelAlles, panelSteuerung, panelWelt;
	public JScrollPane scrollPaneWorld;
	public JTextField textfieldGeneration, textFieldBreite, textFieldHoehe;
	public JLabel labelGenerationen;
	public JButton buttonPlay, buttonReset, buttonInfinite, buttonGeneration;
	public ImageIcon 	iconPlay, iconPlayMo, iconPlayOk, 
						iconPause, iconPauseMo, iconPauseOk, 
						iconStop, iconStopMo, iconStopOk,
						iconInfiniteOf, iconInfiniteOfMo, iconInfiniteOfOk,
						iconInfiniteOn, iconInfiniteOnMo, iconInfiniteOnOk,
						iconGenerationOf, iconGenerationOfMo, iconGenerationOfOk,
						iconGenerationOn, iconGenerationOnMo, iconGenerationOnOk;
	
	int sizeX;
	int sizeY;
	int generations;
	int cellSize = 10;		// wird nur zum starten des programms benötigt
	int borderSize = 1;		
	public PaintGrid paintGrid;

	public boolean isPlaying; // gib an, ob die Spielwelt läuft (also generationen berechnet werden)
	
	public String[] optionsArray = {"false",
									Integer.toString(cellSize),
									Integer.toString(borderSize),
									"false"};
	public boolean newWorldIsInfinite = true;
	public boolean modelIsBitset = false;
	
	
	/**
	 * Konstruktor. Erstellt ein Objekt der Klasse View und erzeugt die GUI.
	 * 
	 * @param controller Ein Controller Objekt zur Steuerung der View. Hier: GUIcontroller
	 */
	public GUI(GUIcontroller controller){
		
		this.setTitle("Game of Life (Array)");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addComponentListener(controller);
		
		
		// Menü-Leiste
		menuBar = new JMenuBar();
		ToolTipManager.sharedInstance().setInitialDelay(100);
		
		//Erster Menüpunkt
		menuDatei = new JMenu("Datei");
			//Menü-Unterpunkte
				// Neu
		JMenuItem itemNeu = new JMenuItem("Neu");
		itemNeu.setActionCommand("neu");
		itemNeu.addActionListener(controller);
				// Laden
		JMenuItem itemLaden = new JMenuItem("\u00d6ffnen...");
		itemLaden.setActionCommand("oeffnen");
		itemLaden.addActionListener(controller);
				// Speichern
		JMenuItem itemSpeichern = new JMenuItem("Speichern...");
		itemSpeichern.setActionCommand("speichere");
		itemSpeichern.addActionListener(controller);
				// Beenden
		JMenuItem itemBeenden = new JMenuItem("Beenden");
		itemBeenden.setActionCommand("ende");
		itemBeenden.addActionListener(controller);
		// Menü-Unterpunkte zum Menü-Baum hinzufügen
		menuDatei.add(itemNeu);
		menuDatei.addSeparator();
		menuDatei.add(itemLaden);
		menuDatei.add(itemSpeichern);
		menuDatei.addSeparator();
		menuDatei.add(itemBeenden);
		
		
		// Zweiter Menüpunkt
		menuExtras = new JMenu("Extras");
			//Menü-Unterpunkte
				// Einstellungen
		JMenuItem itemEinstellungen = new JMenuItem("Einstellungen");
		itemEinstellungen.setActionCommand("einstellen");
		itemEinstellungen.addActionListener(controller);
		// Menü-Unterpunkte zum Menü-Baum hinzufügen
		menuExtras.add(itemEinstellungen);
		
		
		// Dritter Menüpunkt
		menuHilfe = new JMenu("Hilfe");
			// Menü-Unterpunkte
				// Spielregeln
				JMenuItem itemSpielregeln = new JMenuItem("Spielregeln");
				itemSpielregeln.setActionCommand("regeln");
				itemSpielregeln.addActionListener(controller);
				// Über Game of Life...
				JMenuItem itemUeber = new JMenuItem("\u00dcber Game of Life...");
				itemUeber.setActionCommand("ueber");
				itemUeber.addActionListener(controller);
				
		// Menü-Unterpunkte zum Menü-Baum hinzufügen
		menuHilfe.add(itemSpielregeln);
		menuHilfe.addSeparator();
		menuHilfe.add(itemUeber);
		
		//Hinzufügen der Menüpunkte zum Menü
		menuBar.add(menuDatei);
		menuBar.add(menuExtras);
		menuBar.add(menuHilfe);
		
		this.setJMenuBar(menuBar);
		
		// ****************************
		// ***** ENDE MENUE ZEILE *****
		// ****************************
		
		
		// Panel für gesamten Inhalt
		panelAlles = new JPanel(new BorderLayout());
		panelAlles.setBackground(Color.white);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		
		// Panel für die Elemente, die den Programmablauf steuern
		panelSteuerung = new JPanel(new FlowLayout());
		panelSteuerung.setBackground(Color.white);
		
			
			// Play-/Pause Button hinzufügen
		iconPlay = createImageIcon("images/PlayButton.png", "Play Button Icon");
		iconPlayMo = createImageIcon("images/PlayButton_mo.png", "Play Button Icon on Mouseover");
		iconPlayOk = createImageIcon("images/PlayButton_ok.png", "PlayButton Icon on Click");
		iconPause = createImageIcon("images/PauseButton.png", "Pause Button Icon");
		iconPauseMo = createImageIcon("images/PauseButton_mo.png", "Pause Button Icon on Mouseover");
		iconPauseOk = createImageIcon("images/PauseButton_ok.png", "PauseButton Icon on Click");
		buttonPlay = new JButton(iconPlay);
		buttonPlay.setBorderPainted(false);
		buttonPlay.setContentAreaFilled(false);
		buttonPlay.setToolTipText("Start/Pause");
		buttonPlay.setRolloverIcon(iconPlayMo);
		buttonPlay.setPressedIcon(iconPlayOk);
		buttonPlay.setActionCommand("play");
		buttonPlay.addActionListener(controller);
		panelSteuerung.add(buttonPlay);
			
		
			//Stop-Button hinzufügen
		iconStop = createImageIcon("images/StopButton.png", "Stop Button Icon");
		iconStopMo = createImageIcon("images/StopButton_mo.png", "Stop Button Icon on Mouseover");
		iconStopOk = createImageIcon("images/StopButton_ok.png", "Stop Button Icon on Click");
		
		buttonReset = new JButton(iconStop);
		buttonReset.setToolTipText("Welt Reset - stoppt Simulation und l\u00f6scht alle Zellen");
		buttonReset.setRolloverIcon(iconStopMo);
		buttonReset.setPressedIcon(iconStopOk);
		buttonReset.setBorderPainted(false);
		buttonReset.setContentAreaFilled(false);
		buttonReset.setActionCommand("resetButton pressed");
		buttonReset.addActionListener(controller);
		panelSteuerung.add(buttonReset);
		
			//Infinite-Button hinzufügen 
		iconInfiniteOf = createImageIcon("images/InfiniteOfButton.png", "Button Infinite=Of Icon");
		iconInfiniteOfMo = createImageIcon("images/InfiniteOfButton_mo.png", "Button Infinite=Of Icon on Mouseover"); 
		iconInfiniteOfOk = createImageIcon("images/InfiniteOfButton_ok.png", "Button Infinite=Of Icon on Click");
		iconInfiniteOn = createImageIcon("images/InfiniteOnButton.png", "Button Infinite=On Icon");
		iconInfiniteOnMo = createImageIcon("images/InfiniteOnButton_mo.png", "Button Infinite=On Icon on Mouseover");
		iconInfiniteOnOk = createImageIcon("images/InfiniteOfButton_ok.png", "Button Infinite=On Icon on Click");
		
		buttonInfinite = new JButton(iconInfiniteOf);
		buttonInfinite.setToolTipText("Schaltet die Welt auf \"offen\" oder \"geschlossen\" um");
		buttonInfinite.setRolloverIcon(iconInfiniteOfMo);
		buttonInfinite.setPressedIcon(iconInfiniteOfOk);
		buttonInfinite.setBorderPainted(false);
		buttonInfinite.setContentAreaFilled(false);
		buttonInfinite.setActionCommand("infiniteButton pressed");
		buttonInfinite.addActionListener(controller);
		panelSteuerung.add(buttonInfinite);
		
		
			// Generation-Button hinzufügen
		iconGenerationOf = createImageIcon("images/GenerationOfButton.png", "Button Generation=Of Icon");
		iconGenerationOfMo = createImageIcon("images/GenerationOfButton_mo.png", "Button Generation=Of Icon on Mouseover"); 
		iconGenerationOfOk = createImageIcon("images/GenerationOfButton_ok.png", "Button Generation=Of Icon on Click");
		iconGenerationOn = createImageIcon("images/GenerationOnButton.png", "Button Generation=On Icon");
		iconGenerationOnMo = createImageIcon("images/GenerationOnButton_mo.png", "Button Generation=On Icon on Mouseover");
		iconGenerationOnOk = createImageIcon("images/GenerationOnButton_ok.png", "Button Generation=On Icon on Click");
		
		buttonGeneration = new JButton(iconGenerationOf);
		buttonGeneration.setBorderPainted(false);
		buttonGeneration.setContentAreaFilled(false);
		buttonGeneration.setToolTipText("Generationen-Counter an-/ausschalten");
		buttonGeneration.setRolloverIcon(iconGenerationOfMo);
		buttonGeneration.setPressedIcon(iconGenerationOfOk);
		buttonGeneration.setActionCommand("generationOnOf");
		buttonGeneration.addActionListener(controller);
		
		panelSteuerung.add(buttonGeneration);
		
			// Label zur Anzeige des Textes "Generationen" neben dem Generation-Button hinzufügen
		labelGenerationen = new JLabel("Generationen:");
		labelGenerationen.setEnabled(false);
		panelSteuerung.add(labelGenerationen);
		
			// TextFeld zur Eingabe der Generationen-Anzahl hinzufügen
		textfieldGeneration = new JTextField(5);
		textfieldGeneration.setBackground(Color.white);
		textfieldGeneration.setEditable(false);
		textfieldGeneration.setToolTipText("Hier die Anzahl der Generationen eingeben");
		panelSteuerung.add(textfieldGeneration);
		
		panelSteuerung.add(Box.createRigidArea(new Dimension(20, 5))); // "Spacer"
		
		
			// Label für Speed-Slider hinzufügen
		JLabel labelSpeed = new JLabel("Geschwindigkeit: ");
		panelSteuerung.add(labelSpeed);
		
			// Geschwindigkeits-Slider hinzufügen
		JSlider sliderSpeed = new JSlider(10, 1000, 500);
		sliderSpeed.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		sliderSpeed.setBackground(Color.white);
		sliderSpeed.setName("speed");
		sliderSpeed.setToolTipText("Legt Geschwindigkeit f\u00fcr Generationenwechsel fest");
		sliderSpeed.addChangeListener(controller);

		panelSteuerung.add(Box.createRigidArea(new Dimension(20, 5))); // "Spacer"
		
			//Label-Tabelle erstellen
		Hashtable labelTable = new Hashtable();
		labelTable.put(1000, new JLabel("1"));
		labelTable.put(10, new JLabel("100"));
		sliderSpeed.setLabelTable(labelTable);
		sliderSpeed.setLabelTable(labelTable);
		sliderSpeed.setPaintLabels(true);
		
		panelSteuerung.add(sliderSpeed);
		
		// Panel für die grafische Darstellung der Welt
		panelWelt = new JPanel(new BorderLayout());

		panelWelt.setBackground(Color.white);

		paintGrid = new PaintGrid(controller.model.world.getCellArray());	// Zeichenfläche erzeugen
		paintGrid.setSizeX(controller.model.world.getSizeX());				// Weltgröße übergeben
		paintGrid.setSizeY(controller.model.world.getSizeY());
		
		paintGrid.setPreferredSize(paintGrid.getWorldSize()); // Methode getWorldSize ist verfügbar!!! Sir!
		
		panelWelt.add(paintGrid, BorderLayout.CENTER);
		panelWelt.addMouseListener(controller);
		
		// Steuer-Panel und Welt-Panel dem gesamt-Panel hinzufügen
		panelAlles.add(panelSteuerung, BorderLayout.NORTH);
		panelAlles.add(panelWelt, BorderLayout.CENTER); 
		
		
		this.setIconImage((createImageIcon("images/GenerationOnButton.png", "Frame Icon")).getImage());
		this.setContentPane(panelAlles);
		this.pack();
		minimumWindowSize = new Dimension((panelSteuerung.getWidth()+10), ((panelSteuerung.getHeight()*2)));
		System.out.println("Minimum Window Size: " + minimumWindowSize);
		
		//this.setMinimumSize(minimumWindowSize); ==> wird nicht mehr benötigt wegen this.pack() in der Methode Update()
		windowSize = new Dimension(this.getSize());
		contentSize = new Dimension(panelSteuerung.getWidth(),(panelSteuerung.getHeight()+panelWelt.getHeight()));
		System.out.println("Gesamte Größe des Frames: " + windowSize);
		System.out.println("Größe des Inhalts(panelSteuerung und panelWelt): " + contentSize);
	}

	
	// ***************************
	// ***** ENDE Konstruktor*****
	// ***************************
	
	@Override
	public void update(Observable o, Object obj) {
		
		ModelAbstract m = (ModelAbstract)o;
		
		paintGrid.repaint();
		this.pack();
		System.out.println("GUI-update");
	}
	
	/**
	 * Setzt die Breite der Welt. Wird benötigt um Berechnung für die Fenstergröße vorzunehmen.
	 * @param x Breite der Welt
	 */
	public void setSizeX(int x){
		sizeX = x;
	}
	/**
	 * Setzt die Höhe der Welt. Wird benötigt um Berechnung für die Fenstergröße vorzunehmen.
	 * @param x Höhe der Welt
	 */
	public void setSizeY(int y){
		sizeY = y;
	}
	/**
	 * Setzt die Generation der Welt um sie auf der Anzeige anzuzeigen.
	 * @param gen Anzahl der Generationen
	 */
	public void setGenerations(int gen){
		generations = gen;
	}
	/**
	 * Liest die Anzahl der Generationen aus dem Textfeld aus
	 * @return Anzahl der Generationen
	 */
	public int getGenerations(){
			try{
				int gen = Integer.parseInt(textfieldGeneration.getText());
				if(gen < 0) return -1; // Wenn hier die Abfrage <0 drin ist, warum ist sie dann im controller nochmal?
				else return gen;
			}catch(Exception e){
				return -1;
			}
	}

	
	/**
	 * 
	 * @return gibt die aktuelle Fenstergrš§e wieder
	 */
	public Dimension getWindowSize(){
			Dimension dimension = new Dimension(this.getWidth(), this.getHeight());
			return dimension;	
	}
	/**
	 * 
	 * @return Gibt die Breite der Spielwelt zurück
 	 */
	public int getSizeX(){
		return sizeX;
	}
	/**
	 * 
	 * @return Gibt die Höhe der Spielwelt zurück
	 */
	public int getSizeY(){
		return sizeY;
	}
	
	
	/**
	 * Passt nach dem Laden eiber Datei automatisch die Größe der Fensters an die Spielwelt an.
	 */
	public void resizeWindow(){
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();		// größe des Bildschirms
		int width;		// neue Breite des Fensters
		int height;		// neue Höhe des Fensters
		
		// wird gebraucht, um die Größe von panelWelt auf die neue Weltgröße up"zu"daten (ist upzudaten ein wort???)
		paintGrid.setPreferredSize(paintGrid.getWorldSize());
		
		/*
		 * Bestimmt die Werte für die neue Breite und Höhe der Welt, indem
		 * die neuen Maße mit der Bildschirmgröße verglichen werden und je
		 * nachdem ein Scrollbalken hinzugefügt werden
		 */
		
		int diffWidth = (int)new Dimension(this.getSize()).getWidth()-panelWelt.getWidth();
		int diffHeight = (int)new Dimension(this.getSize()).getHeight()-(panelSteuerung.getHeight()+panelWelt.getHeight());
		
		if(paintGrid.getWorldSize().getWidth()+25 <= screenSize.getWidth()){

			width = ((int)paintGrid.getWorldSize().getWidth())+diffWidth;

		}else{
			width = (int)screenSize.getWidth();
			//TODO Scrollbalken
		}
		if(paintGrid.getWorldSize().getHeight()+panelSteuerung.getHeight()+70 <= screenSize.getHeight()){
			height = ((int)paintGrid.getWorldSize().getHeight())+panelSteuerung.getHeight()+diffHeight;

		}else{
			height = (int)screenSize.getHeight();
			//TODO Scrollbalken
		}
		
		this.setSize(width, height);
		
	}
	/**
	 * Erzeugt ein Popup mit den folgenden Parametern
	 * @param imFeld Text, der im Textfeld angezeigt werden soll
	 * @param inDerLeiste Text, der in der Popupleiste angezeigt werden soll
	 */
	public void popupWarning(String imFeld, String inDerLeiste){
		JOptionPane.showMessageDialog(this, imFeld, inDerLeiste, JOptionPane.WARNING_MESSAGE);
	}
	/**
	 * Erzeugt ein Popup mit den folgenden Parametern
	 * @param imFeld Text, der im Textfeld angezeigt werden soll
	 * @param inDerLeiste Text, der in der Popupleiste angezeigt werden soll
	 */
	public void popupInfo(String imFeld, String inDerLeiste){
		JOptionPane.showMessageDialog(this, imFeld, inDerLeiste, JOptionPane.INFORMATION_MESSAGE);
	}
	/**
	 * Erzeugt ein Popup mit den folgenden Parametern
	 * @param imFeld Text, der im Textfeld angezeigt werden soll
	 * @param inDerLeiste Text, der in der Popupleiste angezeigt werden soll
	 */
	public int popupConfirm(String imFeld, String inDerLeiste){
		return(JOptionPane.showConfirmDialog(this, imFeld, inDerLeiste, JOptionPane.YES_NO_OPTION));
	}
	/**
	 * 
	 */
	/*
	public void changeWindowSize(){
		
	}
	*/
	
	/**
	 * Methode erstellt ein ImageIcon aus einer Bilddatei (jpeg, png, gif) über angabe des 
	 * Dateipfades. Der Dateipfad wird dabei relativ zur verwendeten Klasse angegeben und von
	 * der Methode in eine absolute URL umgewandelt.
	 * 
	 * @param path	Pfad zur Bilddatei, aus der das Icon erstellt werden soll
	 * @param description	Kurze Beschreibung des Icons.
	 * @return	ImageIcon oder Null, wenn Dateipfad fehlerhaft
	 */
	protected ImageIcon createImageIcon(String path, String description){
		 java.net.URL imgURL = this.getClass().getResource(path);
		 if (imgURL != null) {
		        return new ImageIcon(imgURL, description);
		    } else {
		        System.err.println("Couldn't find file: " + path + " bei Erstellung von: " + description);
		        return null;
		    }

	}
	/*
	public int getCellSize() {
		return cellSize;
	}


	public int getBorderSize() {
		return borderSize;
	}
	*/
	/**
	 * Zeigt das Einstellungsfenster an.
	 */
	public void showOptionPane(GUIcontroller controller){
		dialogOptions = new JDialog(this, "Einstellungen", true);
		dialogOptions.setLocation(200, 50);
		JPanel panelOptionen;
		GridBagConstraints optionConstraints = new GridBagConstraints();
		JPanel panelWelt, panelZelle, panelExpertenmodus, panelModelWahl, panelConfirm;
		JButton buttonOk, buttonAbbrechen;
		
		
		panelOptionen = new JPanel(new GridBagLayout());
		panelOptionen.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		// Eistellungsbereich für Welt:
		panelWelt = new JPanel(new GridBagLayout());
		panelWelt.setBorder(BorderFactory.createTitledBorder("Welt"));
		optionConstraints.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		optionConstraints.ipadx = 10;
		optionConstraints.ipady = 10;
		optionConstraints.gridx = 0;
		optionConstraints.gridy = 0;
		
			// Radio-Button-Gruppe erstellen => dadurch lässt sich immer nur einer der RadioButtons in dieser Gruppe Aktiv setzen
		ButtonGroup rButtonGroup = new ButtonGroup();
			// Radio-Button "Begrenzt" hinzufügen
		JRadioButton rButtonBegrenzt = new JRadioButton("begrenzt", !controller.model.world.getInfinite());
		rButtonBegrenzt.setActionCommand("optionenWeltBegrenzt");
		rButtonBegrenzt.addActionListener(controller);
		rButtonGroup.add(rButtonBegrenzt);
		panelWelt.add(rButtonBegrenzt, optionConstraints);
			// Radio-Button "offen" hinzufügen
		JRadioButton rButtonOffen = new JRadioButton("offen", controller.model.world.getInfinite());
		rButtonOffen.setActionCommand("optionenWeltOffen");
		rButtonOffen.addActionListener(controller);
		rButtonGroup.add(rButtonOffen);
		optionConstraints.gridy++;
		panelWelt.add(rButtonOffen, optionConstraints);
		
		
		optionConstraints.gridy++;
		panelWelt.add(Box.createRigidArea(new Dimension(350, 5)), optionConstraints);
		
		//Einstellungsbereich für Zelle:
		panelZelle = new JPanel(new GridBagLayout());
		panelZelle.setBorder(BorderFactory.createTitledBorder("Zelle"));
		optionConstraints.gridx = 0;
		optionConstraints.gridy = 0;
			
			// ExtraPanel für die beiden Slider (nötig fürs Layout...)
		JPanel panelSlider = new JPanel(new GridBagLayout());
		
				// Slider für Zellengröße hinzufügen
		JLabel labelSliderG = new JLabel("Größe:");
		JSlider sliderG = new JSlider(1, 20, paintGrid.getCellSize());
		sliderG.setName("cellSize");
		sliderG.setLabelTable(sliderG.createStandardLabels(5, 5));
		sliderG.setPaintLabels(true);
		
		sliderG.addChangeListener(controller);
		
		optionConstraints.gridx = 0;
		optionConstraints.gridy = 0;
		panelSlider.add(labelSliderG, optionConstraints);
		optionConstraints.gridx++;
		panelSlider.add(sliderG, optionConstraints);
		
		
				// Slider für ZellenBegrenzung hinzufügen
		JLabel labelSliderB = new JLabel("Begrenzung:");
		JSlider sliderB = new JSlider(1, 8, paintGrid.getBorderSize());
		sliderB.setName("borderSize");
		sliderB.setLabelTable(sliderG.createStandardLabels(2, 2));
		sliderB.setPaintLabels(true);
		
		sliderB.addChangeListener(controller);
		
		optionConstraints.gridx = 0;
		optionConstraints.gridy++;
		panelSlider.add(labelSliderB, optionConstraints);
		optionConstraints.gridx++;
		panelSlider.add(sliderB, optionConstraints);
		
		optionConstraints.gridy = 2;
		panelZelle.add(panelSlider, optionConstraints);
		optionConstraints.gridy++;
		panelZelle.add(Box.createRigidArea(new Dimension(350, 5)), optionConstraints);
		
		// Einstellung für "Expertenmodus"
		panelExpertenmodus = new JPanel(new GridBagLayout());
		panelExpertenmodus.setBorder(BorderFactory.createTitledBorder("Erweiterte Zelldarstellung"));
		optionConstraints.gridx = 0;
		optionConstraints.gridy = 0;
		JCheckBox checkBoxExpMode = new JCheckBox("aktiviert", paintGrid.getExpertMode());
		checkBoxExpMode.setActionCommand("optionsExpertMode");
		checkBoxExpMode.addActionListener(controller);
		panelExpertenmodus.add(checkBoxExpMode, optionConstraints);
		JTextArea textAreaExpModeExpl = new JTextArea("Wenn die erweiterte Zelldarstellung aktiviert ist,\n" +
											 "werden Zellen, die in der n\u00e4chsten Generation sterben\n" +
											 "oder geboren werden ebenfalls angezeigt (aber etwas dunkler \n" +
											 "als die anderen Zellen).");
		textAreaExpModeExpl.setEditable(false);
		textAreaExpModeExpl.setOpaque(false);
		optionConstraints.gridy++;
		panelExpertenmodus.add(textAreaExpModeExpl, optionConstraints);
		optionConstraints.gridy++;
		panelExpertenmodus.add(Box.createRigidArea(new Dimension(350, 5)), optionConstraints);
		
		//TODO mal drüberschaun
		// Einstellbereich für Zellfarbe
		JPanel panelZellFarbe = new JPanel(new FlowLayout());
		panelZellFarbe.setPreferredSize(new Dimension(370,60));
		panelZellFarbe.setBorder(BorderFactory.createTitledBorder("Farbeinstellungen"));
		
		JButton buttonColorCell = new JButton("Zellen");
		buttonColorCell.addActionListener(controller);
		buttonColorCell.setActionCommand("ChooseColorCell");
		
		panelZellFarbe.add(buttonColorCell, optionConstraints);
		
		JButton buttonColorBG = new JButton("Tote Zellen");
		buttonColorBG.addActionListener(controller);
		buttonColorBG.setActionCommand("ChooseColorBG");
		
		panelZellFarbe.add(buttonColorBG, optionConstraints);
		
		JButton buttonColorBorder = new JButton("Rand");
		buttonColorBorder.addActionListener(controller);
		buttonColorBorder.setActionCommand("ChooseColorBorder");
		
		panelZellFarbe.add(buttonColorBorder, optionConstraints);

		

		// Bestätigung am Ende des Option-Fensters

		//Auswahl Bitset/Array
		panelModelWahl = new JPanel(new FlowLayout());
		panelModelWahl.setBorder(BorderFactory.createTitledBorder("Wahl der Berechnungsmethode"));
		panelModelWahl.setPreferredSize(new Dimension(370,60));
		
		JButton buttonModelWahl = new JButton("Wahl des Models");
		buttonModelWahl.setActionCommand("modelwahl");
		buttonModelWahl.addActionListener(controller);
		
		optionConstraints.gridx = 0;
		optionConstraints.gridy = 0;
		panelModelWahl.add(buttonModelWahl, optionConstraints);
		/*
		optionConstraints.gridy++;
		panelModelWahl.add(Box.createRigidArea(new Dimension(350, 5)), optionConstraints);
		*/
		
		// Bestätigung am ende des Option-Fensters

		panelConfirm = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonOk = new JButton("OK");
		buttonOk.setActionCommand("optionDialogOk");
		buttonOk.addActionListener(controller);
		buttonAbbrechen = new JButton("Abbrechen");
		buttonAbbrechen.setActionCommand("optionDialogAbbrechen");
		buttonAbbrechen.addActionListener(controller);
		
		panelConfirm.add(buttonOk);
		panelConfirm.add(buttonAbbrechen);
		
		
		// Einstellunge - Fenster "zusammenbauen"
		optionConstraints.gridx = 0;
		optionConstraints.gridy = 0;
		panelOptionen.add(panelWelt, optionConstraints);
	
		optionConstraints.gridy++;
		panelOptionen.add(panelZelle, optionConstraints);
		
		optionConstraints.gridy++;
		panelOptionen.add(panelExpertenmodus, optionConstraints);
		optionConstraints.gridy++;
		panelOptionen.add(panelModelWahl, optionConstraints);
		
		optionConstraints.gridy++;
		panelOptionen.add(panelZellFarbe, optionConstraints);
		
		optionConstraints.gridy++;
		optionConstraints.fill = GridBagConstraints.BOTH;
		panelOptionen.add(panelConfirm, optionConstraints);
		
		dialogOptions.setContentPane(panelOptionen);
		dialogOptions.pack();
		dialogOptions.setVisible(true);
	}
	
	/**
	 * Zeigt das Fenster zur Auswahl des verwendeten Models an
	 * @param controller Controller. Hier GUIController
	 */
	public void showModelWahl(GUIcontroller controller){
		
		dialogModel= new JDialog(this, "Wahl des Models", true);
		dialogModel.setLocation(220, 110);
		
		JPanel panelModel = new JPanel(new BorderLayout());
		// ButtonGroup
		ButtonGroup buttonGroupModelWalh = new ButtonGroup();
		
		JRadioButton rButtonArray = new JRadioButton("Array", !modelIsBitset);
		rButtonArray.setActionCommand("array");
		rButtonArray.addActionListener(controller);
		JRadioButton rButtonBitset = new JRadioButton("Bitset", modelIsBitset);
		rButtonBitset.setActionCommand("bitset");
		rButtonBitset.addActionListener(controller);
	
		buttonGroupModelWalh.add(rButtonArray);
		buttonGroupModelWalh.add(rButtonBitset);
	
		panelModel.add(rButtonArray, BorderLayout.NORTH);
		panelModel.add(rButtonBitset, BorderLayout.CENTER);
		
		JPanel panelModelWahlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton modelWahlOk = new JButton("OK");
		modelWahlOk.setActionCommand("modelWahlOK");
		modelWahlOk.addActionListener(controller);
		JButton modelWahlAbbrechen = new JButton("Abbrechen");
		modelWahlAbbrechen.setActionCommand("modelWahlAbbrechen");
		modelWahlAbbrechen.addActionListener(controller);
		panelModelWahlButtons.add(modelWahlOk);
		panelModelWahlButtons.add(modelWahlAbbrechen);
		
		panelModel.add(panelModelWahlButtons, BorderLayout.SOUTH);
		
		dialogModel.setContentPane(panelModel);
		dialogModel.pack();
		dialogModel.setVisible(true);
	
	
	}
	
	/**
	 * Zeigt das Fenster zu Erzeugung einer neuen Welt an.
	 * @param controller Controller. Hier: GUIcontroller
	 */
	public void showNewWorldPane(GUIcontroller controller){
		dialogNewWorld = new JDialog(this, "Neue Welt erstellen", true);
		dialogNewWorld.setLocation(200, 200);
		
		JPanel panelNewWorld, panelSize, panelInfinite, panelButtons;
		JLabel labelBreite, labelHoehe;
		JButton buttonOk, buttonAbbr;
		GridBagConstraints constraints = new GridBagConstraints();
		JRadioButton rButtonOffen, rButtonBegrenzt;
		ButtonGroup rButtonGroup;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(10, 10, 10, 10);
		
		panelNewWorld = new JPanel(new GridBagLayout());
		
		// Bereich für Weltgröße
		panelSize = new JPanel(new GridBagLayout());
		panelSize.setBorder(BorderFactory.createTitledBorder("Gr\u00f6\u00dfe der Welt"));
		
		
		labelBreite = new JLabel("Breite");
		labelHoehe = new JLabel("H\u00f6he");
		textFieldBreite = new JTextField(5);
		textFieldHoehe = new JTextField(5);
		
		panelSize.add(labelBreite, constraints);
		constraints.gridx++;
		panelSize.add(textFieldBreite, constraints);
		
		constraints.gridx=0;
		constraints.gridy++;
		panelSize.add(labelHoehe, constraints);
		constraints.gridx++;
		panelSize.add(textFieldHoehe, constraints);
		
		// Bereich für Begerenzt/ Unbegrenzt
		panelInfinite = new JPanel();
		panelInfinite.setBorder(BorderFactory.createTitledBorder("Art der Welt"));
		
		rButtonGroup = new ButtonGroup();
		
		rButtonOffen = new JRadioButton("Offen", true);
		rButtonOffen.setActionCommand("newWorldOpen");
		rButtonOffen.addActionListener(controller);
		rButtonGroup.add(rButtonOffen);
		
		rButtonBegrenzt = new JRadioButton("Begrenzt");
		rButtonBegrenzt.setActionCommand("newWorldClosed");
		rButtonBegrenzt.addActionListener(controller);
		rButtonGroup.add(rButtonBegrenzt);
		
		panelInfinite.add(rButtonOffen);
		panelInfinite.add(rButtonBegrenzt);
		
		// Bereich für OK / Abrrechen
		panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonOk = new JButton("Ok");
		buttonOk.setActionCommand("newWorldOk");
		buttonOk.addActionListener(controller);
		
		
		panelButtons.add(buttonOk);
		buttonAbbr = new JButton("Abbrechen");
		buttonAbbr.setActionCommand("newWorldAbbr");
		buttonAbbr.addActionListener(controller);
		
		panelButtons.add(buttonAbbr);
	
		
		// Neue Welt-Panel "zusammenbauen"
		constraints.gridx=0;
		constraints.gridy=0;
		panelNewWorld.add(panelSize, constraints);
		constraints.gridy++;
		panelNewWorld.add(panelInfinite, constraints);
		constraints.gridy++;
		panelNewWorld.add(panelButtons, constraints);
		
		dialogNewWorld.setContentPane(panelNewWorld);
		dialogNewWorld.pack();
		dialogNewWorld.setMinimumSize(dialogNewWorld.getSize());
		dialogNewWorld.setVisible(true);
	}
}

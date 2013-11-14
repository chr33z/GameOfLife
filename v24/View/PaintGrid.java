package v24.View;

import java.awt.Color;
import java.awt.Dimension;

import v24.Model.Cell;
import javax.swing.*;
import java.awt.Graphics;

/**
 * PaintGrid erstellt ein Gitter in dem die lebenden Zellen der Welt dargestellt werden.
 * 
 * Ist expertMode true, werden die Zellen je nach ihrem Zustand andersfarbig angezeigt.
 * Mögliche Zustände sind:
 * - lebend
 * - sterbend
 * - wird geboren
 * 
 * @author Ayfer Aslan, Christopher Gebhardt
 *
 */
public class PaintGrid extends JPanel {

	int sizeX;
	int sizeY;
	int cellSize = 10;
	int borderSize = 1;
	
	boolean expertMode = false;
	
	Color willDieColor = new Color(214,214,40);
	Color isAliveColor = new Color(255,255,0);
	Color willBeBornColor = new Color(160,160,90);
	Color isDeadColor = new Color(122,122,122);
	Color borderColor = new Color(180,180,180);

	Cell[][] cellArray;
	
	/**
	 * Konstruktor
	 * @param cellArray CellArray, dessen Zellen gezeichnet werden sollen
	 */
	public PaintGrid(Cell[][] cellArray){
		this.cellArray = cellArray;
	}
	
	/**
	 * Wird automatisch aufgerufen, wenn Änderungen am Model auftreten.
	 * Zeichnet die Welt daraufhin neu.
	 * @param g Graphics Objekt, auf das gezeichnet werden soll
	 */
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		paintWorld(g);
	}
	
	/**
	 * Erzeugt ein Graphics Objekt, dass die gesamte Welt und ihren Status enthält.
	 * @param g Graphics Objekt, auf das gezeichnet werden soll
	 */
	private void paintWorld(Graphics g){
		
		int x=0;
		int y=0;
		for(int i=0; i<sizeX; i++ ){
			for(int j=0; j<sizeY; j++){
				if(cellArray[i][j].getIsAlive()==true){
					if(cellArray[i][j].getWillDie()&&expertMode){
						paintCell(x, y, willDieColor, g);
						y=y+cellSize + borderSize;
					}else{
						paintCell(x, y, isAliveColor, g);
						y=y+cellSize + borderSize;
					}
				}else{
					if(cellArray[i][j].getWillBeBorn()&&expertMode){
						paintCell(x, y, willBeBornColor, g);
						y=y+cellSize + borderSize;
					}else{
						paintCell(x, y, isDeadColor, g);
						y=y+cellSize + borderSize;
					}
				}
				
			}
			y=0;
			x=x+cellSize+borderSize;
		}
	}
	/**
	 * 
	 * @param x X-Koordinate der Zelle, die gezeichnet werden soll
	 * @param y Y-Koordinate der Zelle, die gezeichnet werden soll
	 * @param cellColor Farbe der Zelle
	 * @param g Graphics Objekt, auf das gezeichnet werden soll
	 */
	private void paintCell(int x, int y, Color cellColor, Graphics g){
		g.setColor(borderColor);
		g.fillRect(x, y, cellSize+borderSize, cellSize+borderSize);
		g.setColor(cellColor);
		g.fillRect(x+borderSize/2, y+borderSize/2, cellSize, cellSize);
		
	}
	/**
	 * Die Breite der Welt. Wird für paintWorld benötigt
	 * @param sizeX Breite der Welt
	 */
	public void setSizeX(int sizeX){
		this.sizeX = sizeX;
	}
	/**
	 * Die Höhe der Welt. Wird für paintWorld benötigt
	 * @param sizeY Höhe der Welt
	 */
	public void setSizeY(int sizeY){
		this.sizeY = sizeY;
	}
	/**
	 * Setzt die Breite des Randes, den ein Zelle umgibt
	 * @param borderSize Breite des Randes in Pixel
	 */
	public void setBorderSize(int borderSize){
		this.borderSize = borderSize;
	}
	/**
	 * Setzt die Breite eine Zelle
	 * @param cellSize Breite in Pixel
	 */
	public void setCellSize(int cellSize){
		this.cellSize = cellSize;
	}
	/**
	 * Setzt den ExpertenModus (erweiterte Farbdarstellung der Zellen) auf true oder false
	 * @param expertMode True für erweiterte Farbdarstellung / False für normale Farben
	 */
	public void setExpertMode(boolean expertMode){
		this.expertMode = expertMode;
	}
	/**
	 * Setzt die Farbe für lebende Zellen
	 * @param isAliveColor Farbe für lebende Zellen
	 */
	public void setColorIsAlive(Color isAliveColor){
		this.isAliveColor = isAliveColor;
	}
	/**
	 * Setzt die Farben für Zellen die in der nächsten Generation sterben
	 * @param willDieColor Farbe für Zellen die sterben werden
	 */
	public void setColorWillDie(Color willDieColor){
		this.willDieColor = willDieColor;
	}
	/**
	 * Setzt die Farbe für Zellen die in der nächsten Runde geboren werden.
	 * @param willBeBornColor Farbe für Zellen die geboren werden
	 */
	public void setColorWillBeBorn(Color willBeBornColor){
		this.willBeBornColor = willBeBornColor;
	}
	/**
	 * Setzt die Farbe für Zellen, die momentan tot sind
	 * @param isDeathColor Farbe für tote Zellen
	 */
	public void setColorIsDead(Color isDeathColor){
		this.isDeadColor = isDeathColor;
	}
	/**
	 * Setzt die Farbe für den Rand zwischen den Zellen
	 * @param borderColor Farbe für den Rand
	 */
	public void setColorBorder(Color borderColor){
		this.borderColor = borderColor;
	}
	/**
	 * Setzt das CellArray, mit dem die Welt gezeichnet wird
	 * @param cellArray CellArray, das die aktuelle Konfiguration an Zellen enthält
	 */
	public void setCellArray(Cell[][] cellArray){
		this.cellArray = cellArray;
	}
	
	/**
	 * 
	 * @return Farbe der lebenden Zellen
	 */
	public Color getColorIsAlive(){
		return isAliveColor;
	}
	/**
	 * 
	 * @return Farbe der sterbenden Zellen
	 */
	public Color getColorWillDie(){
		return willDieColor;
	}
	/**
	 * 
	 * @return Farbe der Zellen, die geboren werden
	 */
	public Color getColorWillBeBorn(){
		return willBeBornColor;
	}
	/**
	 * 
	 * @return Farbe der toten Zellen
	 */
	public Color getColorIsDead(){
		return isDeadColor;
	}
	/**
	 * 
	 * @return Farbe des Rands zwischen den Zellen
	 */
	public Color getColorBorder(){
		return borderColor;
	}
	
	/**
	 * 
	 * @return Weltgröße in Pixel
	 */
	public Dimension getWorldSize(){
		Dimension dimension = new Dimension(sizeX*(cellSize+borderSize), sizeY*(cellSize+borderSize));
		return dimension;
	}
	/**
	 * 
	 * @return Einstellung, ob die erweiterten Farben benutzt werden oder nicht.
	 */
	public boolean getExpertMode(){
		return expertMode;
	}
	/**
	 * 
	 * @return Breite der Zellen in Pixel
	 */
	public int getCellSize() {
		return cellSize;
	}
	/**
	 * 
	 * @return Breite des Rands zwischen den Zellen
	 */
	public int getBorderSize() {
		return borderSize;
	}
	
}

package v24.Model;

import java.util.BitSet;

/**
 * World, das die Berechnungen auf der Grundlage von Bitsets realisiert.
 * Um die Schnittstelle zwischen Controller und World einheitlich zu halten,
 * wird das Ergebnis in die Darstellung CellArray überführt.
 * 
 * @author Ayfer Aslan, Christopher Gebhardt
 *
 */
public class WorldBitset extends WorldAbstract {
	
	private int sizeX;
	private int sizeY;
	private boolean infinite;
	private int generation;
	
	private BitSet isAlive;			// Bitset mit lebenden Zellen
	private BitSet willBeBorn;		// Bitset mit Zellen die geboren werden
	private BitSet willDie;			// Bitset mit Zellen die sterben werden
	private Cell[][] cellArray;		// CellArray das die berechneten Daten zurückgibt
	
	/**
	 * Konstruktor der die Welt mit der Breite x und der Höhe y erstellt
	 * @param x Breite der Welt
	 * @param y	Höhe der Welt
	 */
	public WorldBitset(int x, int y){		
		isAlive = new BitSet (x*y);
		willBeBorn = new BitSet (x*y);
		willDie = new BitSet (x*y);
		
		this.sizeX = x;
		this.sizeY = y;
		
		cellArray = new Cell[sizeX][sizeY];
		for(int i=0; i<sizeX; i++){
			for(int j=0; j<sizeY; j++){
				cellArray[i][j] = new Cell();
			}
		}
	}
	
	@Override
	public void setSizeX(int value) {
		this.sizeY = value;
	}

	@Override
	public void setSizeY(int value) {
		this.sizeY = value;
	}

	@Override
	public void setInfinite(boolean inf) {
		this.infinite = inf;
	}

	@Override
	public void resetGeneration() {
		this.generation = 0;
	}
	
	/**
	 * Verändert die Spielwelt auf die Größe newSizeX * newSizeY.
	 * 
	 * @param newSizeX Breite der neuen Welt
	 * @param newSizeY Höhe der neuen Welt
	 */
	public void changeWorldSize(int newSizeX, int newSizeY) {
		BitSet tempBitset = new BitSet (newSizeX*newSizeY);
		
		int indexAlt = 0; //TODO
		while(isAlive.nextSetBit(indexAlt+1) != -1){

			indexAlt = isAlive.nextSetBit(indexAlt+1);
			
			// Zellen werden beim verkleinern nur übernommen, wenn sie innerhalb der neuen Welt liegen
			if((indexAlt % sizeX) < newSizeX){
				tempBitset.set(indexAlt + ((newSizeX - sizeX) * (indexAlt /sizeX)));		// Zugrundeliegende Formel zur Berechnung der neuen Indizes von den kopierten Zellen: 
			}																				// Index der Alten Zelle + (Differenz der neuen und alten Weltbreite) * y-Koordinate = neuer Index
		}
		
		isAlive = new BitSet(newSizeX * newSizeY);
		willBeBorn = new BitSet (newSizeX*newSizeY);
		willDie = new BitSet (newSizeX*newSizeY);
		isAlive = tempBitset;

		cellArray = new Cell[newSizeX][newSizeY];
		for(int i=0; i<newSizeX; i++){
			for(int j=0; j<newSizeY; j++){
				cellArray[i][j] = new Cell();
			}
		}

		this.sizeX = newSizeX;
		this.sizeY = newSizeY;
	}

	@Override
	public int getSizeX() {
		return sizeX;
	}

	@Override
	public int getSizeY() {
		return sizeY;
	}

	@Override
	public boolean getInfinite() {
		return infinite;
	}

	@Override
	public int getGeneration() {
		return generation;
	}

	@Override
	public void setGeneration(int num) {
		this.generation = num;
	}

	/**
	 * entwickelt die Welt weiter, indem das Bitset mit den lebenden Zellen
	 * bit-weise mit or(willBeBorn) und andNot(willDie) verarbeitet wird
	 */
	public void evolveCell(int x, int y){
		isAlive.or(willBeBorn); 		// Setzt Zellen, die geboren werden auf lebend
		isAlive.andNot(willDie);		// Setzt Zellen die sterben werden auf tot
		willBeBorn.clear();				// löscht die beiden Hilfs-Bitsets für die nächste Runde
		willDie.clear();
	}

	@Override
	public boolean cellIsAlive(int x, int y) {
		return isAlive.get(y*sizeX+x);
	}

	public int getNeighbours(int x, int y){
		int neighbours = 0;
		
		if(neighbourIsValid(x-1,y-1)) neighbours++;
		if(neighbourIsValid(x,y-1)) neighbours++;
		if(neighbourIsValid(x+1,y-1)) neighbours++;
		if(neighbourIsValid(x+1,y)) neighbours++;
		if(neighbourIsValid(x+1,y+1)) neighbours++;
		if(neighbourIsValid(x,y+1)) neighbours++;
		if(neighbourIsValid(x-1,y+1)) neighbours++;
		if(neighbourIsValid(x-1,y)) neighbours++;
		
		return neighbours;
	}
	
	/**
	 * Hilfsmethode für getNeighbours()
	 * Falls infinite = false, werden alle Nachbarn ausgeschlossen, die ausserhalb des Koordinatensystems liegen.
	 * Falls infinite = true werden die Zellen die ausserhalb liegen transformiert.
	 * 
	 * @param x x-Koordinate der Zelle
	 * @param y y-Koordinate der Zelle
	 * @return Booleanwert, der angibt, ob die Nachbarzelle gültig ist.
	 */
	public boolean neighbourIsValid(int x, int y){
		int index = y*sizeX+x;
		boolean cellIsAlive = false;
		if(!infinite){
			if(((x >= 0) && (y >= 0)) && ((x+1 <= sizeX) && (y+1 <= sizeY))){
				cellIsAlive = isAlive.get(index);
			}else{
				cellIsAlive = false;
			}
		}else{
			index = neiTransY(y)*sizeX+neiTransX(x);
			cellIsAlive = isAlive.get(index);
		}
		return cellIsAlive;
	}
	/**
	 * Hilfsmethode für getNeighbours()
	 * Transformiert die x Koordinate, falls sie ausserhalb der Welt liegt auf die gegenüberliegende Seite der Welt
	 * Vergleich Torus.
	 * 
	 * @param x x-Koordinate die transformiert werden soll
	 * @return Transformierte x-Koordinate, falls sie ausserhalb der Welt liegen würde
	 */
	public int neiTransX(int x){
		if(x < 0) x = sizeX-1;
		if(x > sizeX-1) x = 0;
		return x;
	}
	/**
	 * Hilfsmethode für getNeighbours()
	 * Transformiert die y Koordinate, falls sie ausserhalb der Welt liegt auf die gegenüberliegende Seite der Welt
	 * Vergleich Torus. 
	 * 
	 * @param y y-Koordinate die transformiert werden soll
	 * @return Transformierte y-Koordinate, falls sie ausserhalb der Welt liegen würde
	 */
	public int neiTransY(int y){
		if(y < 0) y = sizeY-1;
		if(y > sizeY-1) y = 0;
		return y;
	}
	
	/**
	 * Bestimmt die Anzahl der lebenden Nachbarn einer Zelle, anhand derer dann bestimmt wird, ob eine 
	 * Zelle lebt oder stirbt.
	 * @param x x-Koordinate der Zelle
	 * @param y y-Koordinate der Zelle
	 * @return Anzahl der Nachbarn einer Zelle
	 */
	
	public void setCellAlive(int x, int y) {
		if(isAlive.get(y*sizeX+x)){
			isAlive.clear(y*sizeX+x);
			willBeBorn.clear(y*sizeX+x);
			willDie.clear(y*sizeX+x);
		}else{
			isAlive.set(y*sizeX+x);
			willBeBorn.clear(y*sizeX+x);
			willDie.clear(y*sizeX+x);
		}
	}

	@Override
	public Cell[][] getCellArray() {
		bitsetToArray();
		return cellArray;
	}

	@Override
	public void changeCell(int x, int y, boolean birth, boolean death) {
		if(birth) willBeBorn.set(y*sizeX+x);
		else willBeBorn.clear(y*sizeX+x);
		
		if(death)willDie.set(y*sizeX+x);
		else willDie.clear(y*sizeX+x);
	}
	
	/**
	 * Konvertiert die Bitset-Welt in ein cellArray
	 */
	protected void bitsetToArray(){
		for(int i=0; i<sizeX; i++){
			for(int j=0; j<sizeY; j++){
				cellArray[i][j].setIsAlive( isAlive.get(j*sizeX+i) );
				cellArray[i][j].setWillBeBorn( willBeBorn.get(j*sizeX+i) );
				cellArray[i][j].setWillDie( willDie.get(j*sizeX+i) );
			}
		}
	}

}

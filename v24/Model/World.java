package v24.Model;

import v24.Model.WorldAbstract;

/**
 * Stellt die Spielwelt dar.
 * 
 * 
 * @author Felix Manke, Ayfer Aslan, Christopher Gebhardt
 *
 */
public class World extends WorldAbstract {
	
	
	// Attribute
	
	private int sizeX;
	private int sizeY;
	private int generation;
	private boolean infinite;
	private Cell[][] cellArray;
	
	// Methoden
	
	/**
	 * Konstruktor erstellt eine neue Spielwelt
	 */
	public World(int x, int y){		// Konstruktor: Erstellt Welt mit den Werten X und Y
		cellArray = new Cell[x][y];
		
		for(int i=0; i<x; i++){
			for(int j=0; j<y; j++){
				cellArray[i][j] = new Cell();
			}
		}
		setSizeX(x);
		setSizeY(y);
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLWorld#setSizeX(int)
	 */
	public void setSizeX(int value){	//Setzt den Wert von "sizeX" auf "value"
		sizeX = value;
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLWorld#setSizeY(int)
	 */
	public void setSizeY(int value){	// Setzt den Wert von "sizeY" auf "value"
		sizeY = value;
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLWorld#setInfinite(boolean)
	 */
	public void setInfinite(boolean inf){	// Setzt "infinite" auf den Wert von "inf"
		infinite = inf;
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLWorld#resetGeneration()
	 */
	public void resetGeneration(){
		generation=0;
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLWorld#changeWorldSize(int, int)
	 */
	public void changeWorldSize(int x, int y){  // ändert die Größe der Welt
		 
		 int counterX;		// Hilfsvariable um weiter unten das Kopieren des Cell-Arrays zu steuern
		 int counterY;		// Hilfsvariable um weiter unten das Kopieren des Cell-Arrays zu steuern
		 
		 if (x>=sizeX)				// über if-else Kontrollstruktur wird "counterX" auf den kleineren X-Wert gesetzt
			 counterX = sizeX;
		 else counterX = x;
		 
		 if(y>=sizeY)				// über if-else Kontrollstruktur wird "counterY" auf den kleineren Y-Wert gesetzt
			 counterY = sizeY;
		 else counterY = y;
		 
		 Cell[][] tempCellArray = cellArray;	// kopiert aktuelle Welt in temporäres Array
		 cellArray = new Cell[x][y];
		 for(int i=0; i<x; i++){
				for(int j=0; j<y; j++){
					cellArray[i][j] = new Cell();
				}
			}
		 
		 for(int i=0; i<counterX; i++){
			 for (int j=0; j<counterY; j++){
			//	cellArray[i][j]=tempCellArray[i][j];

				Cell tempCell = tempCellArray[i][j];		// Die Zelle aus dem temporären Array (==alte Welt) wird kopiert...
				cellArray[i][j] = tempCell;				// ...und in cellArray (==neue Welt) gespeichert
			
			 }
		 }
		 
		 sizeX = x;  // abschließend die Werte für x und...
		 sizeY = y;  // ... y aktualisieren
	 }
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLWorld#getSizeX()
	 */
	public int getSizeX(){
		return sizeX;
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLWorld#getSizeY()
	 */
	public int getSizeY(){
		return sizeY;
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLWorld#getInfinite()
	 */
	public boolean getInfinite(){
		return infinite;
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLWorld#getGeneration()
	 */
	public int getGeneration(){
		return generation;
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLWorld#setGeneration(int)
	 */
	public void setGeneration(int num){
		generation=num;
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLWorld#evolveCell(int, int)
	 */
	public void evolveCell(int x, int y){
		if (cellArray[x][y].getWillDie() == true){
			cellArray[x][y].setWillDie(false);
			cellArray[x][y].setIsAlive(false);
		}
		if (cellArray[x][y].getWillBeBorn() == true){
			cellArray[x][y].setWillBeBorn(false);
			cellArray[x][y].setIsAlive(true);
		}
	}
	/**
	 * Ändert die Einstellungen für "willBeBorn" und "willDie".
	 * 
	 * @param x x-Koordinate der Zelle
	 * @param y y-Koordinate der Zelle
	 * @param birth Ob die Zelle geboren werden soll
	 * @param death Ob die Zelle sterben soll
	 */
	public void changeCell(int x, int y, boolean birth, boolean death){
		cellArray[x][y].setWillBeBorn(birth);
		cellArray[x][y].setWillDie(death);
	}
	/* (non-Javadoc)
	 * @see v24.Model.GoLWorld#cellIsAlive(int, int)
	 */
	public boolean cellIsAlive(int x, int y){
		return cellArray[x][y].getIsAlive();
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLWorld#getNeighbours(int, int)
	 */
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
		boolean cellIsAlive = false;
		if(!infinite){
			if(((x >= 0) && (y >= 0)) && ((x+1 <= sizeX) && (y+1 <= sizeY))){
				cellIsAlive = cellArray[x][y].getIsAlive();
			}else{
				cellIsAlive = false;
			}
		}else{
			cellIsAlive = cellArray[neiTransX(x)][neiTransY(y)].getIsAlive();
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
	/*
	public int getNeighbours(int x, int y){
		int neighbours=0;		// in "number" wird die Zahl der nachbarn gespeichert
		if (infinite=true){
			
			if(x==0 && y==0) {
				if(cellArray[x+1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[sizeX-1][y].getIsAlive() == true) neighbours++;
				if(cellArray[sizeX-1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x][sizeY-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][sizeY-1].getIsAlive() == true) neighbours++;
				if(cellArray[sizeX-1][sizeY-1].getIsAlive() == true) neighbours++;
			
			}
			else if(x==sizeX-1 && y==0) {
				if(cellArray[x-1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[0][0].getIsAlive() == true) neighbours++;
				if(cellArray[0][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[0][sizeY-1].getIsAlive() == true) neighbours++;
				if(cellArray[sizeX-2][sizeY-1].getIsAlive() == true) neighbours++;
				if(cellArray[sizeX-1][sizeY-1].getIsAlive() == true) neighbours++;
			}
			else if(x==sizeX-1 && y==sizeY-1) {
				if(cellArray[x-1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[sizeX-1][0].getIsAlive() == true) neighbours++;
				if(cellArray[sizeX-2][0].getIsAlive() == true) neighbours++;
				if(cellArray[0][0].getIsAlive() == true) neighbours++;
				if(cellArray[0][sizeY-2].getIsAlive() == true) neighbours++;
				if(cellArray[0][sizeY-1].getIsAlive() == true) neighbours++;
			}
			else if(x==0 && y==sizeY-1) {
				if(cellArray[x+1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[0][0].getIsAlive() == true) neighbours++;
				if(cellArray[1][0].getIsAlive() == true) neighbours++;
				if(cellArray[sizeX-1][sizeY-1].getIsAlive() == true) neighbours++;
				if(cellArray[sizeX-1][sizeY-2].getIsAlive() == true) neighbours++;
				if(cellArray[sizeX-1][sizeY-1].getIsAlive() == true) neighbours++;
			}
			else if(y==0 && !(x==0 || x==sizeX-1)) {
				if(cellArray[x-1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][sizeY-1].getIsAlive() == true) neighbours++;
				if(cellArray[x][sizeY-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][sizeY-1].getIsAlive() == true) neighbours++;
			}
			else if(x==sizeX-1 && !(y==0 || y==sizeY-1)) {
				if(cellArray[x][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[0][y].getIsAlive() == true) neighbours++;
				if(cellArray[0][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[0][y-1].getIsAlive() == true) neighbours++;
			}
	 		else if(y==sizeY-1 && !(x==0 || x==sizeX-1)) {
				if(cellArray[x-1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x][0].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][0].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][0].getIsAlive() == true) neighbours++;
			}
	 		else if(x==0 && !(y==0 || y==sizeY-1)) {
				if(cellArray[x][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[sizeX-1][y].getIsAlive() == true) neighbours++;
				if(cellArray[sizeX-1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[sizeX-1][y-1].getIsAlive() == true) neighbours++;
			}
	 		else{
	 			if(cellArray[x-1][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y].getIsAlive() == true) neighbours++;
	 		}
		}
		
		else{		// implizit: infinite==false
				if(x==0 && y==0) {
				if(cellArray[x+1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y+1].getIsAlive() == true) neighbours++;
			
			}
			else if(x==sizeX-1 && y==0) {
				if(cellArray[x-1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y+1].getIsAlive() == true) neighbours++;
			}
			else if(x==sizeX-1 && y==sizeY-1) {
				if(cellArray[x-1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y-1].getIsAlive() == true) neighbours++;
			}
			else if(x==0 && y==sizeY-1) {
				if(cellArray[x+1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y-1].getIsAlive() == true) neighbours++;
			}
			else if(y==0 && !(x==0 || x==sizeX-1)) {
				if(cellArray[x-1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y].getIsAlive() == true) neighbours++;
			}
	 		else if(x==sizeX-1 && !(y==0 || y==sizeY-1)) {
				if(cellArray[x][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x][y+1].getIsAlive() == true) neighbours++;
			}
	 		else if(y==sizeY-1 && !(x==0 || x==sizeX-1)) {
				if(cellArray[x-1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y].getIsAlive() == true) neighbours++;
			}
	 		else if(x==0 && !(y==0 || y==sizeY-1)) {
				if(cellArray[x][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x][y+1].getIsAlive() == true) neighbours++;
			}
	 		else{
	 			if(cellArray[x-1][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y-1].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y].getIsAlive() == true) neighbours++;
				if(cellArray[x+1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y+1].getIsAlive() == true) neighbours++;
				if(cellArray[x-1][y].getIsAlive() == true) neighbours++;
	 		}
		}
		return neighbours;
	}
	*/
	/* (non-Javadoc)
	 * @see v24.Model.GoLWorld#setCellAlive(int, int)
	 */
	public void setCellAlive(int x, int y){
		if(cellArray[x][y].getIsAlive() == true){
			cellArray[x][y].setIsAlive(false);
			cellArray[x][y].setWillBeBorn(false);
			cellArray[x][y].setWillDie(false);
		}else{
			cellArray[x][y].setIsAlive(true);
			cellArray[x][y].setWillBeBorn(false);
			cellArray[x][y].setWillDie(false);
		}
	}
	
	/**
	 * 
	 * @return Gibt das aktuelle cellArray zurück
	 */
	public Cell[][] getCellArray(){
		return cellArray;
	}

	@Override
	protected void bitsetToArray() {
		// TODO Auto-generated method stub
		
	}
}

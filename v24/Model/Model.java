package v24.Model;

/**
 * Das Model repr�sentiert die Spielwelt des Game of Life und stellt Methoden zum ver�ndern der Welt zur Verf�gung.
 * Die Realisierung der Welt selbst �bernimmt dir Klasse World.
 * 
 * Dieses Model repr�sentiert die Spielwelt mit Hilfe von Zellen in einem 2-Dimensionalen CellArray.
 * 
 * @author Felix Manke, Ayfer Aslan, Christopher Gebhardt
 *
 */
public class Model extends ModelAbstract{
	
	// public WorldAbstract world;
	
	public Model(int sizeX, int sizeY){
		world = new World(sizeX, sizeY);
		setChanged();
		notifyObservers();
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLModel#createWorld(int, int)
	 */
	@Override
	public void createWorld(int width, int height){		// Methode mit der der Controller sp�ter die Erzeugung einer Welt mit Initialwerten steuern kann
		world = new World(width, height);
		setChanged();
		notifyObservers();
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLModel#changeWorld(int, int)
	 */
	@Override
	public void changeWorld(int x, int y){				// Methode mit der der Controller sp�ter die Gr��e der Welt ver�ndern kann
		world.changeWorldSize(x, y);
	}
	
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLModel#newGeneration()
	 */
	@Override
	public Cell[][] newGeneration(){

		// for-for Schleife geht die Zellen der Welt durch und entwickelt jede Zelle weiter (f�r Details siehe World.evolveCell)
		for(int i=0; i<world.getSizeX(); i++){
			for(int j=0; j<world.getSizeY(); j++){
				world.evolveCell(i, j);
			}
		}
		/* nun m�ssen f�r die neue Zell-Konfiguration f�r jede Zelle die Attribute "willDie" und "willBeBorn" aktualisiert werden
		 * Dazu m�ssen die Nachbarn jeder Zelle ermittelt werden
		*/
		for(int i=0; i<world.getSizeX(); i++){
			for(int j=0; j<world.getSizeY(); j++){
				int neighbours = world.getNeighbours(i, j);
				if(neighbours<2 || neighbours>3){
					world.changeCell(i, j, false, true);
				}
				if((world.cellIsAlive(i, j) == false) && (neighbours == 3)){
					world.changeCell(i, j, true, false);
				}
			}
		}
		world.setGeneration(world.getGeneration()+1);
		// Gibt das berechnete cell Array zur�ck um es von der View verarbeiten zu lassen
		
		setChanged();
		notifyObservers();
		return world.getCellArray();
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLModel#helpSetChanged()
	 */
	@Override
	public void helpSetChanged(){
		setChanged();
		notifyObservers();
	}

}

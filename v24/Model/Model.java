package v24.Model;

/**
 * Das Model repräsentiert die Spielwelt des Game of Life und stellt Methoden zum verändern der Welt zur Verfügung.
 * Die Realisierung der Welt selbst übernimmt dir Klasse World.
 * 
 * Dieses Model repräsentiert die Spielwelt mit Hilfe von Zellen in einem 2-Dimensionalen CellArray.
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
	public void createWorld(int width, int height){		// Methode mit der der Controller später die Erzeugung einer Welt mit Initialwerten steuern kann
		world = new World(width, height);
		setChanged();
		notifyObservers();
	}
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLModel#changeWorld(int, int)
	 */
	@Override
	public void changeWorld(int x, int y){				// Methode mit der der Controller später die Größe der Welt verändern kann
		world.changeWorldSize(x, y);
	}
	
	
	/* (non-Javadoc)
	 * @see v24.Model.GoLModel#newGeneration()
	 */
	@Override
	public Cell[][] newGeneration(){

		// for-for Schleife geht die Zellen der Welt durch und entwickelt jede Zelle weiter (für Details siehe World.evolveCell)
		for(int i=0; i<world.getSizeX(); i++){
			for(int j=0; j<world.getSizeY(); j++){
				world.evolveCell(i, j);
			}
		}
		/* nun müssen für die neue Zell-Konfiguration für jede Zelle die Attribute "willDie" und "willBeBorn" aktualisiert werden
		 * Dazu müssen die Nachbarn jeder Zelle ermittelt werden
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
		// Gibt das berechnete cell Array zurück um es von der View verarbeiten zu lassen
		
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

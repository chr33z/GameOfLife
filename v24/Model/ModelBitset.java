package v24.Model;
/**
 * Die Klasse ModelBitset dient als Schnittstelle zwischen der 
 * als Bitset realisierten Welt und dem Controller/View
 * 
 * @author Ayfer Aslan, Christopher Gebhardt
 *
 */

public class ModelBitset extends ModelAbstract{
	
	public ModelBitset(int sizeX, int sizeY){
		world = new WorldBitset(sizeX, sizeY);
		setChanged();
		notifyObservers();
	}

	@Override
	public void changeWorld(int x, int y) {
		world.changeWorldSize(x, y);
		setChanged();
		notifyObservers();
	}

	@Override
	public void createWorld(int sizeX, int sizeY) {
		world = new WorldBitset(sizeX, sizeY);
		setChanged();
		notifyObservers();
		
	}

	@Override
	public void helpSetChanged() {
		setChanged();
		notifyObservers();
		
	}

	@Override
	public Cell[][] newGeneration() {
		// entwickelt die Welt weiter Parameter 0,0 wird nicht benötigt
		world.evolveCell(0, 0);
		
		/*
		 *  hier kommen die Regeln des Game of Life zum tragen.
		 *  Die Nachbarn werden ermittelt und die Zelle auf "wird geboren"
		 *  oder "wird sterben" gesetzt.
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
		
		setChanged();
		notifyObservers();
		return world.getCellArray();
	}
	
}
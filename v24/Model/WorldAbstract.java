package v24.Model;

import v24.Model.Cell;

public abstract class WorldAbstract{
	
	public abstract void setSizeX(int value);

	public abstract void setSizeY(int value);

	public abstract void setInfinite(boolean inf);

	public abstract void resetGeneration();
	
	public abstract void changeWorldSize(int newSizeX, int newSizeY);

	public abstract int getSizeX();

	public abstract int getSizeY();

	public abstract boolean getInfinite();

	public abstract int getGeneration();

	public abstract void setGeneration(int num);

	public abstract void evolveCell(int x, int y);

	public abstract boolean cellIsAlive(int x, int y);

	public abstract int getNeighbours(int x, int y);
	
	public abstract boolean neighbourIsValid(int x, int y);
	
	public abstract int neiTransX(int x);

	public abstract int neiTransY(int y);
	
	public abstract void setCellAlive(int x, int y);

	public abstract Cell[][] getCellArray();

	public abstract void changeCell(int x, int y, boolean birth, boolean death);
	
	protected abstract void bitsetToArray();
}

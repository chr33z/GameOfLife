package v24.Model;

import java.util.Observable;

public abstract class ModelAbstract extends Observable{
	
	public WorldAbstract world;
	
	public abstract void changeWorld(int x, int y);
	public abstract void createWorld(int width, int height);
	public abstract void helpSetChanged();
	public abstract Cell[][] newGeneration();
}

package v24.Model;

/**
 * 
 * @author Felix Manke 
 * @date 20/10/2010
 * 
 * Die Klasse Cell repr�sentiert eine Zelle im Game of Life und enth�lt Informationen, ob die Zelle lebt und ob 
 * sie in der n�chsten Generation �berlebt oder stirbt.
 * 
 */
public class Cell {
	private boolean isAlive;		// enth�lt Information dar�ber, ob Zelle lebt
	private boolean willBeBorn;		// wenn "true", dann wird Zelle in der n�chsten Generation "geboren"
	private boolean willDie;		// wenn "true", dann stribt Zelle beim �bergang zu n�chsten Generation
	
	/**
	 * Konstruktor initialisiert die Zelle mit den �bergebenen Werten
	 * 
	 * @param int x x-Koordinate der Zelle
	 * @param int y y-Koordinate der Zelle
	 * @param boolean isAlive Gibt an ob die Zelle momentan lebt
	 * @param boolean willBeBorn Gibt an, ob die Zelle in der n�chsten Generation geboren wird, falls sie momentan nicht lebt
	 * @param boolean willDie Gibt an, ob die Zelle in der n�chsten Generation stirbt, falls sie momentan lebt
	 */
	public Cell(){
		this.isAlive = false;
		this.willBeBorn = false;
		this.willDie = false;
	}
	
	// SET-Methoden
	
	/**
	 * @param boolean alive Gibt an, ob die Zelle momentan lebt
	 */
	public void setIsAlive(boolean alive){
		this.isAlive = alive;
	}
	
	/**
	 * @param boolean born Gibt an, ob die Zelle in der n�chsten Generation geboren wird
	 */
	public void setWillBeBorn(boolean born){
		this.willBeBorn = born;
	}
	
	/**
	 * @param boolean die Gibt an, ob die Zelle in der n�chsten Genneration stirbt
	 */
	public void setWillDie(boolean die){
		this.willDie = die;
	}
	
	// GET-Methoden
	
	/**
	 * @return isAlive Gibt zur�ck, ob die Zelle lebt
	 */
	public boolean getIsAlive(){
		return isAlive;
	}
	
	/**
	 * @return willBeBorn
	 */
	public boolean getWillBeBorn(){
		return willBeBorn;
	}
	
	/**
	 * @return willDie
	 */
	public boolean getWillDie(){
		return willDie;
	}

	
	
}

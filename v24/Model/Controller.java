package v24.Model;

/**
 * Da wir zwei verschiedene Controller f�r CLI und GUI benutzen, nutzen wir eine Abstrakte �berKlasse
 * um zb. in der Klasse LoadSave die Werte aus den verschiedenen Model auslesen zu k�nnen.
 * 
 * @author Christopher Gebhardt
 *
 */
public abstract class Controller {
	
	public ModelAbstract model;
}

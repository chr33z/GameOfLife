package v24.Model;

/**
 * Da wir zwei verschiedene Controller für CLI und GUI benutzen, nutzen wir eine Abstrakte überKlasse
 * um zb. in der Klasse LoadSave die Werte aus den verschiedenen Model auslesen zu können.
 * 
 * @author Christopher Gebhardt
 *
 */
public abstract class Controller {
	
	public ModelAbstract model;
}

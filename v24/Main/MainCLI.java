package v24.Main;

import v24.Controller.CLIcontroller;

/**
 * Startet das Programm und initiiert den Controller
 * 
 * @author Christopher Gebhardt
 *
 */
public class MainCLI {
	
	public static void main(String[] args){
		int paramInfinite = 0;
		int paramGeneration = 0;
		
		try{
			paramInfinite = Integer.parseInt(args[0]);
			paramGeneration = Integer.parseInt(args[1]);
		}catch(Exception e){
			
		}
		
		new CLIcontroller(paramInfinite, paramGeneration);
	}
}

/**
 * Dei Muddaaah war da und hat nen Kommentar hinterlassen... du sollst heimkommen und ihren Rücken waschen. :P
 * Wutz Wutz
 *
 *
*/
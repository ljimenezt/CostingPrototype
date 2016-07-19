package co.informatix.erp.utils;

import java.io.File;
import java.io.Serializable;

/**
 * this class allows validating files, such as the path of a file to create the
 * route concerned.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
public class FileController implements Serializable {

	/**
	 * Check if a path exists and if not create the folder.
	 * 
	 * @param location
	 *            : Location to verify or create the route.
	 */
	public static void fileExist(String location) {
		File fileExist = new File(location);
		if (!fileExist.exists()) {
			fileExist.mkdirs();
		}
	}

}

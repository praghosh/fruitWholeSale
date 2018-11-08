package nmfc.helper;

import java.text.SimpleDateFormat;

public class StandardItems {
	
 private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	
 public static SimpleDateFormat getFormatter() {
	return formatter;
}


public static final  String[]  purschaseOrSellingUnit = new String[] {
			"Small Crate", "Big Crate", "Petty",  "Box", "Busket", "Bag" , "Kg", "Pieces", "Kandi"};

}

package nmfc.helper;

public class Utility {
	
	
 public static long getValue (Long lonVal) {
		 
		 if (lonVal==null) {
			 return 0;
		 }
		 else return  lonVal.longValue();		
	}
 
 public static float getValue (Float fltVal) {
	 
	 if (fltVal==null) {
		 return 0.0f;
	 }
	 else return  fltVal.floatValue();		
}
 
public static double getValue (Double doubleVal) {
	 
	 if (doubleVal==null) {
		 return 0.0f;
	 }
	 else return  doubleVal.doubleValue();		
}

 public static String getString (Object objVal ) {
	 
	 if (objVal == null) {
		 return "";
	 } else  {
		 return  ("" + objVal);	  
		 
	 }	
	 
 }
 
 public static boolean getValue (Boolean boolVal) {
	 
	 if (boolVal==null) {
		 return false;
	 }
	 else return  boolVal.booleanValue();		
}

}

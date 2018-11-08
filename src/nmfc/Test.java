package nmfc;
import java.util.*;
	public class Test
	{
	    public static void call(Exception e)
	    {
	        System.out.println("Exception");
	    }
	    public static void call(NullPointerException e)
	    {
	        System.out.println("NullPointer");
	    }
	    public static void call(Object e)
	    {
	        System.out.println("Object");
	    }
	    public static void main(String args[])
	    {
	        call(null);
	    }

	}

	 


 

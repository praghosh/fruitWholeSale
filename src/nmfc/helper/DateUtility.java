package nmfc.helper;

import java.util.Calendar;
import java.util.Date;

public class DateUtility {




	public static Date getFirstDateOfMonth () {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = 1;
		c.set(year, month, day);
		//	int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
	//	System.out.println("First Day of month: " + c.getTime());
		//		c.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth-1);
		//		System.out.println("Last Day of month: " + c.getTime());
		return  c.getTime();

	}

	public static Date getLastDateOfMonth () {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = 1;
		c.set(year, month, day);
		int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth-1);
	//	System.out.println("Last Day of month: " + c.getTime());
		return  c.getTime();

	}

	public static void main(String[] args) {
		System.out.println("First Day=" + getFirstDateOfMonth ());
		System.out.println("Last Day=" + getLastDateOfMonth ());
	}
}

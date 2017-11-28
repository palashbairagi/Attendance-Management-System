package other;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Getter {
	public static String getShortName(String subject)
	{
	String shortName=null;
	char name1,name2,name3,name4,name5;
	int c1=0,c2=0,c3=0,c4=0,c5=0;

	c1=subject.indexOf(" ",0);
	shortName=subject.substring(0,c1)+" ";

	name1=subject.charAt(c1+1);
	if((int)name1>=65 && (int)name1<=90)shortName=shortName+name1;

	c2=subject.indexOf(" ",c1+1);
	name2=subject.charAt(c2+1);
	if((int)name2>=65 && (int)name2<=90)shortName=shortName+name2;
	if((int)name2==45){shortName=shortName+subject.substring(c2+1);return shortName;}

	c3=subject.indexOf(" ",c2+1);if(c3<0)return shortName;
	name3=subject.charAt(c3+1);
	if((int)name3>=65 && (int)name3<=90)shortName=shortName+name3;

	c4=subject.indexOf(" ",c3+1);if(c4<0)return shortName;
	name4=subject.charAt(c4+1);
	if((int)name4>=65 && (int)name4<=90)shortName=shortName+name4;

	c5=subject.indexOf(" ",c4+1);if(c5<0)return shortName;
	name5=subject.charAt(c5+1);
	if((int)name5>=65 && (int)name5<=90)shortName=shortName+name5;

	return shortName;
	}
	public static String getShortTime(String time)
	{
		time=time.substring(0,time.length()-3);
		return time;
	}
	public static String getPreviousDate(String date) throws ParseException {
		  final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		  final Date date1 = format.parse(date);
		  final Calendar calendar = Calendar.getInstance();
		  calendar.setTime(date1);
		  calendar.add(Calendar.DAY_OF_YEAR, -1);
		  return format.format(calendar.getTime());
	}
	public static String getNextDate(String date) throws ParseException {
		  final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		  final Date date1 = format.parse(date);
		  final Calendar calendar = Calendar.getInstance();
		  calendar.setTime(date1);
		  calendar.add(Calendar.DAY_OF_YEAR, 1);
		  return format.format(calendar.getTime()); 
	}
	public static boolean isDateValid(String date)
	{
		DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy");  
		java.util.Date date1;
		try {
				 date1= userDateFormat.parse(date);
			} 
		catch (ParseException e) 
		{
			System.out.print("Exception by Getter.isDateValid "+e);
			return false;
		}
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date2 = new Date();
		dateFormat.format(date);
		
		int comparison = date1.compareTo(date2);
	     
		if(comparison==-1)return false;
		
		else return true;
	}
	public static boolean isSessionValid(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		String userId=null;
		String userName = null;
		String password=null;
		userId=(String)session.getAttribute("userId");
		userName=(String)session.getAttribute("userName");
		password=(String) session.getAttribute("password");
		
		if(userName==null || password==null || userId==null)return false;		
		
		return true;
	}
}

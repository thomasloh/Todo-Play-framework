package com.util;

import java.util.Date;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Helper {
    
    private static SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    private static SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
    
    // Converts date string and time string to date object
    public Date convertStringToDate(String date, String time) {
      
      Date dueDateTime = null;

      try {
        Calendar dCal = Calendar.getInstance();
        dCal.setTime((Date)formatter.parse(date));
        Calendar tCal = Calendar.getInstance();
        tCal.setTime((Date)tf.parse(time));
        int hourOfDay;
        System.out.println(time);
        if (time.indexOf("AM") == -1) {
          hourOfDay = tCal.get(Calendar.HOUR_OF_DAY) + 12;
        }
        else {
          hourOfDay = tCal.get(Calendar.HOUR_OF_DAY);
        }
        dCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dCal.set(Calendar.MINUTE, tCal.get(Calendar.MINUTE));
        dCal.set(Calendar.SECOND, tCal.get(Calendar.SECOND));
        dCal.set(Calendar.MILLISECOND, tCal.get(Calendar.MILLISECOND));
        dueDateTime = dCal.getTime();
      }
      catch (Exception e) {
        System.out.println(e);
      }
      return dueDateTime;
    }
           
}

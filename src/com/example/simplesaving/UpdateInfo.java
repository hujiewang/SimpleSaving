package com.example.simplesaving;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;

// Stores last update time
public class UpdateInfo {
     private int lastDay;
     private int lastMonth;
     private SharedPreferences sharedPref;
     private SharedPreferences.Editor editor;
     private Context context;
     private Calendar calendar;
     
     
     public UpdateInfo(SharedPreferences sharedPref,Context context){
    	 this.sharedPref=sharedPref;
    	 this.context=context;
    	 editor=sharedPref.edit();
    	 calendar=Calendar.getInstance();
     }
     public void retrieveInfo(){
    	 lastDay=sharedPref.getInt(context.getString(R.string.updateinfo_last_day),R.string.ERROR_NOT_EXIST);
    	 lastMonth=sharedPref.getInt(context.getString(R.string.updateinfo_last_month),R.string.ERROR_NOT_EXIST);
    	 if(lastDay==R.string.ERROR_NOT_EXIST||lastMonth==R.string.ERROR_NOT_EXIST){
    		 setRightNow();
    	 }
     }
     public void saveInfo(){
    	  editor.putInt(context.getString(R.string.updateinfo_last_day), lastDay);
		  editor.putInt(context.getString(R.string.updateinfo_last_month), lastMonth);
		  editor.commit();
     }
     public void setRightNow(){
    	 lastDay=calendar.get(Calendar.DAY_OF_MONTH);
    	 lastMonth=calendar.get(Calendar.MONTH);
     }
     public boolean lastUpdateDayIsToday(){
    	 int today=calendar.get(Calendar.DAY_OF_MONTH);
    	 int thisMonth=calendar.get(Calendar.MONTH);
    	 return lastDay==today&&lastMonth==thisMonth;
     }
     public boolean lastUpdateMonthIsThisMonth(){
    	 int thisMonth=calendar.get(Calendar.MONTH);
    	 return lastMonth==thisMonth;
     }
     public int getLastDay(){
    	 return lastDay;
     }
     public int getLastMonth(){
    	 return lastMonth;
     }
     public void setLastDay(int lastDay){
    	 this.lastDay=lastDay;
     }
     public void setLastMonth(int lastMonth){
    	 this.lastMonth=lastMonth;
     }
     
}

package com.example.simplesaving;

import java.io.Console;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// Using Singleton design pattern
public class DataCenter{
	// Singleton
	private static DataCenter instance=null;

	// Shared variables
	public static Integer dailyEarning;
	public static Integer monthlyGoal;
	public static Integer daysLeft;
	public static Integer expenseBefore; 
	public static Integer totalDaysInAMonth;
	public static Integer fixedOutcome;
	public static Integer todayExpense;

	public static Boolean firstTimeOpenApp;

	// Special objects
	private Context context;

	// Common objects
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor editor;
	private Calendar calendar;

	// Customs objects
	public UpdateInfo updateInfo;

	// Temporary objects
	private TextView textView;
	private EditText editText;

	// Constructor
	private DataCenter(Context context){
		this.context=context;
		this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		//this.sharedPref=context.getSharedPreferences(context.getString(R.string.preference_file_key), context.MODE_PRIVATE);
		editor=sharedPref.edit();
		//editor.clear();
		//editor.commit();
		calendar=Calendar.getInstance();
		updateInfo=new UpdateInfo(this.sharedPref,this.context);
		initVar();
	}

	public static DataCenter getInstance(Context context){
		if(instance==null){
			instance=new DataCenter(context);
		}
		return instance;
	}
	public static DataCenter getInstance(){
		if(instance==null){
			Log.e("DataCenter getInstance()","instance is null");
		}
		return instance;
	}
	private void initVar(){
		dailyEarning=monthlyGoal=daysLeft=expenseBefore=totalDaysInAMonth=fixedOutcome=fixedOutcome=todayExpense=0;
		firstTimeOpenApp=true;
	}
    private void calculateDaysLeft(){
    	totalDaysInAMonth=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		daysLeft=totalDaysInAMonth-calendar.get(Calendar.DAY_OF_MONTH);
    }
	public void retrieveVariables(){

		updateInfo.retrieveInfo();

		dailyEarning=sharedPref.getInt(context.getString(R.string.var_daily_earning),R.string.ERROR_NOT_EXIST);
		monthlyGoal=sharedPref.getInt(context.getString(R.string.var_monthly_goal),R.string.ERROR_NOT_EXIST);
		expenseBefore=sharedPref.getInt(context.getString(R.string.var_expense_before),R.string.ERROR_NOT_EXIST);
		fixedOutcome=sharedPref.getInt(context.getString(R.string.var_fixed_outcome),R.string.ERROR_NOT_EXIST);
		todayExpense=sharedPref.getInt(context.getString(R.string.var_today_expense),R.string.ERROR_NOT_EXIST);
		firstTimeOpenApp=sharedPref.getBoolean(context.getString(R.string.var_first_time_open_app),true);
		if(dailyEarning==R.string.ERROR_NOT_EXIST||
				monthlyGoal==R.string.ERROR_NOT_EXIST||
				expenseBefore==R.string.ERROR_NOT_EXIST||
				fixedOutcome==R.string.ERROR_NOT_EXIST||
				todayExpense==R.string.ERROR_NOT_EXIST){
			initVar();
			calculateDaysLeft();
			saveVariables();
		}
		else{
			calculateDaysLeft();
		}
	}
	public void saveVariables(){
		updateInfo.setRightNow();
		updateInfo.saveInfo();

		editor.putInt(context.getString(R.string.var_daily_earning), dailyEarning);
		editor.putInt(context.getString(R.string.var_monthly_goal), monthlyGoal);
		editor.putInt(context.getString(R.string.var_expense_before), expenseBefore);
		editor.putInt(context.getString(R.string.var_fixed_outcome), fixedOutcome);
		editor.putInt(context.getString(R.string.var_today_expense), todayExpense);
		editor.putBoolean(context.getString(R.string.var_first_time_open_app), firstTimeOpenApp);
		editor.commit();
	}

	public boolean firstTime(){
		return firstTimeOpenApp;
	}
	public void disableFirstTime(){
		if(firstTimeOpenApp){
			firstTimeOpenApp=false;
			saveVariables();
		}
	}
	public void clearVar(){
		initVar();
		saveVariables();
	}


}

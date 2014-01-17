package com.example.simplesaving;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
public class MainActivity extends Activity {

	private FlipAnimation flipAnimation;
	// DataCenter instance
	private DataCenter dc;
    // CoreApis
	private CoreAPIs core;
	// Dialogs
	private Dialogs dialogs;
	// I/O views
	TextView dailyExpectionNumberView;
	TextView expectionView;
	EditText todayExpenseView;
	
	// Basic lifecycle methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Context context=MainActivity.this;
		init();		
	}
	
	@Override
	public void onBackPressed() {
	   Intent setIntent = new Intent(Intent.ACTION_MAIN);
	   setIntent.addCategory(Intent.CATEGORY_HOME);
	   setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	   startActivity(setIntent);
	}
	@Override
	protected void onStart() {
	   super.onStart();
	   // If it's the first time opening up the application, show settings page
	   dc.retrieveVariables();
	   if(dc.firstTime())
	     setupForTheFirstTime();
	   flipAnimation.init();
	}
	protected void onStop() {
		super.onStop();
		dc.saveVariables();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.clear:
	            clear();
	            return true;
	        case R.id.action_settings:
	            openSettings();
	            return true;
	        case R.id.debug:
	        	openDebug();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void init(){
		
		RotaryButton rb=(RotaryButton)findViewById(R.id.rotary_outside);
		//rb.setTextView((TextView)findViewById(R.id.testing));
		flipAnimation=new FlipAnimation(getFragmentManager(),rb,this);
		// Get a DataCenter instance
		dc=DataCenter.getInstance(this);
		dc.retrieveVariables();
		
		core=new CoreAPIs();
		dialogs=new Dialogs(this);
		
		dailyExpectionNumberView=(TextView)findViewById(R.id.daily_expection_number);
	    todayExpenseView=(EditText)findViewById(R.id.today_expense);
	    
	    // New Month
	    if(!dc.updateInfo.lastUpdateMonthIsThisMonth()){
	    	// TODO
	    }
	    else{
	      // New day
	      if(!dc.updateInfo.lastUpdateDayIsToday()){
	    	  DataCenter.expenseBefore+=DataCenter.todayExpense;
	    	  DataCenter.todayExpense=-1;
	      }
	    }
	    
	    String expStr=Integer.toString(core.calculateExpection());
	    /*
	    dailyExpectionNumberView.setText(expStr);
	    
		if(DataCenter.todayExpense==-1){
			todayExpenseView.setHint("Add today's expense");
		}
		else{
			todayExpenseView.setHint("Continue adding expenses");
		}
			*/
		setupButtons();
	}
	
	private void setupButtons(){
		/*
		Button updateButton=(Button)findViewById(R.id.update_button);
	    updateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			    
			    // If input string is blank, do nothing
			    if(todayExpenseView.getEditableText().toString().length()==0)
			    	return;
			    
			    if(DataCenter.todayExpense==-1){
			    	DataCenter.todayExpense=Integer.parseInt(todayExpenseView.getEditableText().toString());
			    }
			    else{
			    	DataCenter.todayExpense+=Integer.parseInt(todayExpenseView.getEditableText().toString());
			    }
			    
			    todayExpenseView.setText("");
			    todayExpenseView.setHint("Continue adding expenses");
			    
			    // Calculate daily expectation
			    String expStr=Integer.toString(core.calculateExpection());
			    dailyExpectionNumberView.setText(expStr);
			    
			}
		});
		*/
	}
	private void clear(){
		dc.clearVar();
		dialogs.OKDialog("Cleared successfully! --DEBUG ONLY");
	}
	private void openDebug(){
		Intent intent=new Intent(this,Debug.class);
		startActivity(intent);
	}
	private void openSettings(){
		Intent intent=new Intent(this,Settings.class);
		startActivity(intent);
	}
	
	private void setupForTheFirstTime(){
		// Shows up Settings for the first time
		Intent intent = new Intent(this,Settings.class);
		startActivity(intent);
	}
	
}

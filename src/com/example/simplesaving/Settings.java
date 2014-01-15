package com.example.simplesaving;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends Activity {
    
	// Data
	private DataCenter dc;
	// Dialogs
	private Dialogs dialogs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
		setupActionBar();
        // Get a DataCenter instance
		dc=DataCenter.getInstance(this);
		dialogs=new Dialogs(this);
		setupButtons();
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
    private void setupButtons(){
	    Button updateButton=(Button)findViewById(R.id.settings_update);
	    updateButton.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View v) {
		     EditText expenseBeforeEdit=(EditText)findViewById(R.id.expense_before_edit);
		     EditText fixedOutcomeEdit=(EditText)findViewById(R.id.fixed_income_edit);
		     String expenseBeforeEditStr=expenseBeforeEdit.getText().toString();
		     String fixedOutcomeEditStr=fixedOutcomeEdit.getText().toString();
		     if(expenseBeforeEditStr.length()>0){
		       if(DataCenter.expenseBefore==-1)
		         DataCenter.expenseBefore=Integer.parseInt(expenseBeforeEditStr);
		       else
		    	 DataCenter.expenseBefore+=Integer.parseInt(expenseBeforeEditStr);   
		     }
		     if(fixedOutcomeEditStr.length()>0){
		       if(DataCenter.fixedOutcome==-1)
		         DataCenter.fixedOutcome=Integer.parseInt(fixedOutcomeEditStr);
		       else
		    	 DataCenter.fixedOutcome+=Integer.parseInt(fixedOutcomeEditStr); 
		     }
		     expenseBeforeEdit.setText("");
			 fixedOutcomeEdit.setText("");
		}
	});
	
	Button finishButton=(Button)findViewById(R.id.settings_finish);
	finishButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			EditText dailyEarningEdit=(EditText)findViewById(R.id.daily_earning_edit);
			EditText monthlyGoalEdit=(EditText)findViewById(R.id.monthly_goal_edit);
			EditText expenseBeforeEdit=(EditText)findViewById(R.id.expense_before_edit);
		    EditText fixedOutcomeEdit=(EditText)findViewById(R.id.fixed_income_edit);
		    
		    String dailyEarningEditStr=dailyEarningEdit.getText().toString();
		    String monthlyGoalEditStr=monthlyGoalEdit.getText().toString();
		    String expenseBeforeEditStr=expenseBeforeEdit.getText().toString();
		    String fixedOutcomeEditStr=fixedOutcomeEdit.getText().toString();
		    
		    try{
		    	if(dailyEarningEditStr.length()>0){
		    	   if(DataCenter.dailyEarning==-1)
		    		 DataCenter.dailyEarning=Integer.parseInt(dailyEarningEditStr);
		    	   else
		    		 DataCenter.dailyEarning+=Integer.parseInt(dailyEarningEditStr);  
		    	}
		    	else
		    		throw makeException(R.string.ERROR_INCOMPLETED);
			    if(monthlyGoalEditStr.length()>0){
			    	if(DataCenter.monthlyGoal==-1)
			    	 DataCenter.monthlyGoal=Integer.parseInt(monthlyGoalEditStr);
			    	else
			    	 DataCenter.monthlyGoal+=Integer.parseInt(monthlyGoalEditStr);  
			    }
			    else
		    		throw makeException(R.string.ERROR_INCOMPLETED);
			    
			    if(expenseBeforeEditStr.length()>0){
			    	if(DataCenter.expenseBefore==-1)
			    	  DataCenter.expenseBefore=Integer.parseInt(expenseBeforeEditStr);
			    	else
			    	  DataCenter.expenseBefore+=Integer.parseInt(expenseBeforeEditStr);
			    }
			    if(fixedOutcomeEditStr.length()>0){
			    	if(DataCenter.fixedOutcome==-1)
			    	   DataCenter.fixedOutcome=Integer.parseInt(fixedOutcomeEditStr); 
			    	else
			    	   DataCenter.fixedOutcome+=Integer.parseInt(fixedOutcomeEditStr); 
			    }
			   if(DataCenter.expenseBefore==-1)
				   DataCenter.expenseBefore=0;
			   if(DataCenter.fixedOutcome==-1)
				   DataCenter.fixedOutcome=0;
			   
			    
		   dc.saveVariables();
		   Settings.this.finish();
		  }
		  catch(Exception ex){
			 dialogs.OKDialog(ex.getMessage());
		  }
		}
	});
}

	private Exception makeException(int errorCode){
		Exception ex=new Exception(getString(errorCode));
		return ex;
	}
}

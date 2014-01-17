package com.example.simplesaving;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NumberDialogPref extends DialogPreference{
	private static int DEFAULT_VALUE;
	private int result=-1;
	private View layoutView;
	public NumberDialogPref(Context context, AttributeSet attrs){
		super(context, attrs);
		setDialogLayoutResource(R.layout.number_dialogpref);
	}

	/*
	 @Override
     protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
             builder.setTitle("testing");
             builder.setPositiveButton(null, null);
             builder.setNegativeButton(null, null);
             super.onPrepareDialogBuilder(builder);  
     }
	 */
	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getInteger(index, DEFAULT_VALUE);
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		if (restorePersistedValue) {
			// Restore existing state
			result = this.getPersistedInt(DEFAULT_VALUE);
		} else {
			// Set default state from the XML attribute
			result = (Integer) defaultValue;
			persistInt(result);
		}
		//TextView currentValueTV=(TextView)layoutView.findViewById(R.id.number_dialogpref_current_value);
		//currentValueTV.setText(Integer.toString(result));
	}

	@Override
	public void onBindDialogView(View view){
		layoutView=view;

		Button addButton=(Button)view.findViewById(R.id.number_dialogpref_addbutton);
		Button resetButton=(Button)view.findViewById(R.id.number_dialogpref_resetbutton);
		TextView currentValueTV=(TextView)layoutView.findViewById(R.id.number_dialogpref_current_value);
		currentValueTV.setText(Integer.toString(result));
		addButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				View parent = (View)v.getParent();
				EditText inputDataEditText=(EditText)parent.findViewById(R.id.number_dialogpref_inputData);
				TextView currentValueTextView=(TextView)parent.findViewById(R.id.number_dialogpref_current_value);
				String inputDataStr=inputDataEditText.getText().toString();
				if(inputDataStr.length()>0){
					int value=Integer.parseInt(inputDataStr);
					result+=value;
					currentValueTextView.setText(Integer.toString(result));
					inputDataEditText.setText("");
				}
			}
		});

		resetButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				View parent = (View)v.getParent();
				TextView currentValueTextView=(TextView)parent.findViewById(R.id.number_dialogpref_current_value);

				result=0;
				currentValueTextView.setText(Integer.toString(result));
			}
		});

		super.onBindDialogView(view);
	}
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		EditText inputDataEditText=(EditText)layoutView.findViewById(R.id.number_dialogpref_inputData);
		String inputDataStr=inputDataEditText.getText().toString();
		if(inputDataStr.length()>0){
			int value=Integer.parseInt(inputDataStr);
			result+=value;
		}
		// When the user selects "OK", persist the new value
		if (positiveResult) {
			persistInt(result);
		}
	}
}
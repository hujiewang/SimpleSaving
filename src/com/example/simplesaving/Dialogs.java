package com.example.simplesaving;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dialogs {
    private Context context;
    public Dialogs(Context context){
    	this.context=context;
    }
	public void OKDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg)
		       .setCancelable(false)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //do things
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
}

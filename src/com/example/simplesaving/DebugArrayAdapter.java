package com.example.simplesaving;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DebugArrayAdapter extends ArrayAdapter<NameValuePair>{
    private Context context;
    private ArrayList<NameValuePair> nvp;
	public DebugArrayAdapter(Context context,ArrayList<NameValuePair> nvp){
		super(context,R.layout.debug_list_item,nvp);
		this.context=context;
		this.nvp=nvp;		
	}
	
	@Override
	public View getView(int position,View view,ViewGroup parent){
		LayoutInflater inflater=(LayoutInflater)context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View rowView=inflater.inflate(R.layout.debug_list_item, parent,false);
	    TextView varNameView=(TextView)rowView.findViewById(R.id.variable_name);
	    TextView varValueView=(TextView)rowView.findViewById(R.id.variable_value);
	    varNameView.setText(nvp.get(position).getName());
	    varValueView.setText(Integer.toString(nvp.get(position).getValue()));
	    return rowView;
	}
	
}

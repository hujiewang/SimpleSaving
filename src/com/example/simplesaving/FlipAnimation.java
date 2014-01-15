package com.example.simplesaving;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class FlipAnimation {
	private boolean mShowingBack = false;
	private static Context context;
	private FragmentManager fragmentManager;
	private RotaryButton rotaryButton;
	public FlipAnimation(FragmentManager fragmentManager,RotaryButton rotaryButton,Context context){
		this.fragmentManager=fragmentManager;
		this.rotaryButton=rotaryButton;
		this.context=context;
		//init();
	}
	public void init(){
		rotaryButton.setMinBoundaryListener(new BoundaryEvent(this){
			@Override
			public void fireEvent() {
				flipAnimation.flipCard();
			}
			
		});
		fragmentManager.executePendingTransactions();
		fragmentStackClear();
        fragmentManager
        .beginTransaction()
        .add(R.id.main_activity_container, new CardFrontFragment())
        .addToBackStack(null)
        .commit();
	}
	private void fragmentStackClear(){
		//while(fragmentManager.popBackStackImmediate()){}
		for(int i=0;i<fragmentManager.getBackStackEntryCount();i++){
			fragmentManager.popBackStack();
		}
	}
	public void flipCard() {
        if (mShowingBack) {
        	fragmentManager.popBackStack();
        	mShowingBack=false;
            return;
        }

        // Flip to the back.

        mShowingBack = true;

        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        fragmentManager
                .beginTransaction()

                // Replace the default fragment animations with animator resources representing
                // rotations when switching to the back of the card, as well as animator
                // resources representing rotations when flipping back to the front (e.g. when
                // the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)

                // Replace any fragments currently in the container view with a fragment
                // representing the next page (indicated by the just-incremented currentPage
                // variable).
                .replace(R.id.main_activity_container, new CardBackFragment())

                // Add this transaction to the back stack, allowing users to press Back
                // to get to the front of the card.
                .addToBackStack(null)

                // Commit the transaction.
                .commit();
        fragmentManager.executePendingTransactions();
                
    }
    /**
     * A fragment representing the front of the card.
     */
    public static class CardFrontFragment extends Fragment {
    	View thisView;
    	public CardFrontFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	thisView= inflater.inflate(R.layout.fragment_card_front, container, false);
        	init();
            return thisView;
        }
        
        private void init(){
        	CoreAPIs api=new CoreAPIs();
        	TextView textView=(TextView)thisView.findViewById(R.id.daily_expection_number);
        	textView.setText(Integer.toString(api.calculateExpection()));
        }
    }

    /**
     * A fragment representing the back of the card.
     */
    public static class CardBackFragment extends Fragment {
    	View thisView;
        public CardBackFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	thisView=inflater.inflate(R.layout.fragment_card_back, container, false);
            return thisView;
        }
        @Override
        public void onStop(){
        	super.onStop();
        	
        	EditText editText=(EditText)thisView.findViewById(R.id.today_expense);
        	String todayExpense=editText.getEditableText().toString();
        	if(todayExpense.length()!=0){
			    if(DataCenter.todayExpense==-1){
			    	DataCenter.todayExpense=Integer.parseInt(todayExpense);
			    }
			    else{
			    	DataCenter.todayExpense+=Integer.parseInt(todayExpense);
			    }
        	}
        }
    }
}

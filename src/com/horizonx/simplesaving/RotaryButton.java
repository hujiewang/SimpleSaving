package com.horizonx.simplesaving;

import com.horizonx.simplesaving.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.TextView;

public class RotaryButton extends ImageView{
	private final double IMPOSSIBLE_MOVING_ANGLE=50.00;
	private final double MINIMUM=0.0001;
	private Context context;
	private Bitmap image;
	private float cx;
	private float cy;
	private Matrix matrix;
	private TextView tv;
	private TextView tv2;
	private double rangeMax=45;
	private double rangeMin=0.00;
	private boolean rotationLocked=false;
	private boolean animating=false;
	private double accumulatedAngle=45;
	private int viewH=0;
	private int viewW=0;
	
	private BoundaryEvent maxEvent;
	private BoundaryEvent minEvent;
	public RotaryButton(Context context,AttributeSet attrs) {
		super(context,attrs);
		this.context=context;
		matrix=new Matrix();
		LoadImage();
		this.setOnTouchListener(new RotaryButtonTouchListener());
		
}
	
public void LoadImage(){
	//image= BitmapFactory.decodeResource(getResources(), R.drawable.button_inner);
	image=((BitmapDrawable)RotaryButton.this.getDrawable()).getBitmap();
	//cx=RotaryButton.this.getHeight() / 2;
	//cy=RotaryButton.this.getWidth() / 2;
	
	

	RotaryButton.this.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

    	@Override
		public void onGlobalLayout() {
    		// method called more than once, but the values only need to be initialized one time
    		if (viewH == 0 || viewW == 0) {
    			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    			Display display = wm.getDefaultDisplay();
    			Point size = new Point();
    			display.getSize(size);
    			viewH = size.y;
    			viewW = size.x;
    			
    			// resize
				Matrix resize = new Matrix();
				resize.postScale((float)Math.min(viewW, viewH) / (float)image.getWidth(), (float)Math.min(viewW, viewH) / (float)image.getHeight());
				Bitmap imageScaled = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), resize, false);
				
				
				RotaryButton.this.setImageBitmap(imageScaled);
				viewH=imageScaled.getHeight();
				viewW=imageScaled.getWidth();
				// translate to the image view's center
				//float translateX=  viewW / 2 - imageScaled.getWidth() / 2;
				//float translateY = viewH / 2 - imageScaled.getHeight() / 2;
				
				cx=viewW / 2;
				cy=viewH / 2;
				//matrix.preTranslate(translateX, translateY);
				
				
				//RotaryButton.this.setImageMatrix(matrix);
    		}
		}
	});
}
private int getActionBarHeight(){
	// Calculate ActionBar height
	TypedValue tv = new TypedValue();
	if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
	{
	    return TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
	}
	return 0;
}
private static int getQuadrant(double x, double y) {
	if (x >= 0) {
		return y >= 0 ? 4 : 1;
	} else {
		return y >= 0 ? 3 : 2;
	}
}
private double getAngle(double xTouch, double yTouch) {

	double x = xTouch - cx;
	double y = yTouch - cy;
	int quadrant=getQuadrant(x,y);

	x=Math.abs(x);
	y=Math.abs(y);
	double tangent=(quadrant==1||quadrant==3)?y/x:x/y;
	return (double)(quadrant-1)*Math.toDegrees((Math.PI/2))+Math.toDegrees(Math.atan(tangent));

}
private void rangeMaxEvent(){
	//rotationLocked=true;
	//this.post(new ReverseDeceleration(-10,0.00));
}
private void rangeMinEvent(){
	if(animating)
		return;
	rotationLocked=true;
	animating = true;
	this.post(new ReverseDeceleration(5.00,rangeMax));
	minEvent.fireEvent();
}


public class ReverseDeceleration implements Runnable {

	private double velocity;
	private double targetAngle;
	public ReverseDeceleration(double initVelocity,double targetAngle) {
		this.velocity = initVelocity;
		this.targetAngle=targetAngle;
	}

	@Override
	public void run() {
		if (Math.abs(accumulatedAngle-targetAngle)>MINIMUM) {
			rotate(velocity);
			velocity -=0.1;
			if(velocity<=0)
				velocity=1;
			// post this instance again
			RotaryButton.this.post(this);
		}
		else{
			rotationLocked=false;
			animating=false;
		}
	}
}

public void rotate(double angle){

	//this.animate().rotationBy(targetAngle).setDuration(10).start();

	if(rotationLocked&&!animating)
		return;

	if(rangeMin<=accumulatedAngle+angle&&accumulatedAngle+angle<=rangeMax){		
		accumulatedAngle=accumulatedAngle+angle;
	}
	else if(accumulatedAngle+angle>rangeMax){
		angle=rangeMax-accumulatedAngle;
		accumulatedAngle+=angle;
		//accumulatedAngle=rangeMax;
	}
	else if(accumulatedAngle+angle<rangeMin){
		angle=accumulatedAngle-rangeMin;
		accumulatedAngle+=angle; 
		//accumulatedAngle=rangeMin;
		//rangeMinEvent();
	}
	if(accumulatedAngle>360)
		accumulatedAngle=accumulatedAngle-360.00;
	else if(accumulatedAngle<0)
		accumulatedAngle=360.00+accumulatedAngle;
	matrix.postRotate(-(float)angle, (float)cx, (float)cy);
	this.setImageMatrix(matrix);
	//tv2.setText(Double.toString(accumulatedAngle));
	if(accumulatedAngle+angle>rangeMax){
		rangeMaxEvent();
	}
	else if(accumulatedAngle+angle<rangeMin){
		rangeMinEvent();
	}
	//tv.setText(Double.toString(accumulatedAngle));
}



private class RotaryButtonTouchListener implements OnTouchListener {

	private double startAngle;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:

			startAngle = getAngle(event.getX(), event.getY());
			break;

		case MotionEvent.ACTION_MOVE:
			double currentAngle = getAngle(event.getX(), event.getY());
			double deltaAngle;
			
			//tv2.setText(Double.toString(event.getX())+"  "+Double.toString(event.getY()));
			// Special cases
			if(Math.abs(startAngle - currentAngle)>=IMPOSSIBLE_MOVING_ANGLE){
				if(currentAngle-startAngle<0){
					deltaAngle=360.00-startAngle+currentAngle;
				}
				else{
					deltaAngle=-360.00-startAngle+currentAngle;
				}
			}
			else{
				deltaAngle=currentAngle-startAngle;
			}
			rotate(deltaAngle);
			startAngle = currentAngle;
			break;

		case MotionEvent.ACTION_UP:
			break;
		}
		return true;
	}
}

public void setMaxBoundaryListener(BoundaryEvent event){
	maxEvent=event;
}
public void setMinBoundaryListener(BoundaryEvent event){
	minEvent=event;
}

public void setTextView(TextView tv2){
	this.tv2=tv2;
}


}

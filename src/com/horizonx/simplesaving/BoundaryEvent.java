package com.horizonx.simplesaving;

public abstract class BoundaryEvent {
	 protected FlipAnimation flipAnimation;
     public BoundaryEvent(FlipAnimation flipAnimation){
    	 this.flipAnimation=flipAnimation;
     }
     public abstract void fireEvent();
}

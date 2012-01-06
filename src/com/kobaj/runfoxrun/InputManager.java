package com.kobaj.runfoxrun;

import android.view.MotionEvent;

public class InputManager
{
	public final int fingerCount = 4;
	
	private float[] x;
	private float[] y;
	
	private float[] oldx;
	private float[] oldy;
	
	private float[] deltax;
	private float[] deltay;
	
	private boolean[] pressed;
	
	public InputManager()
	{
		x = new float[fingerCount];
		y = new float[fingerCount];
		
		oldx = new float[fingerCount];
		oldy= new float[fingerCount];
		
		deltax = new float[fingerCount];
		deltay = new float[fingerCount];
		
		pressed = new boolean[fingerCount];
	}

	public void eventUpdate(MotionEvent event)
	{
		int action = event.getAction();
		
		int ptrId = event.getPointerId(0);
        if(event.getPointerCount() > 1)
            ptrId = (action & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;

    	action = action & MotionEvent.ACTION_MASK;
        if(action < 7 && action > 4)
            action = action - 5;  
            
		
		 if( action == MotionEvent.ACTION_DOWN )
         {
                 for( int i = 0; i < event.getPointerCount(); i++ )
                 {
                	 int id = event.getPointerId(i);
                	 
                	 x[id] = event.getX(i);
                	 y[id] = event.getY(i);
                	
                 }                       

                 pressed[ptrId] = true;
         }
         if( action == MotionEvent.ACTION_MOVE )
         {                                                       
                 for( int i = 0; i < event.getPointerCount(); i++ )
                 {
                	 int id = event.getPointerId(i);
                	 
                	 oldx[id] = x[id];
                	 oldy[id] = y[id];
                	 
                	 x[id] = event.getX(i);
                	 y[id] = event.getY(i);
                	 
                	 deltax[id] = x[id] - oldx[id];
         			 deltay[id] = y[id] - oldy[id];
                 }                                                                                       
         }
         if( action == MotionEvent.ACTION_UP )
         {
                 pressed[ptrId] = false;
                 
                 if( event.getPointerCount() == 1 )
                         for( int i = 0; i < fingerCount; i++ )
                                 pressed[i] = false;
         }
         if( action == MotionEvent.ACTION_CANCEL )
         {
                 pressed[ptrId] = false;
                 if( event.getPointerCount() == 1 )
                 for( int i = 0; i < fingerCount; i++ )
                         pressed[i] = false;
         }
	}

	public float getX(int index)
	{
		return x[index];
	}

	public float getY(int index)
	{
		return y[index];
	}

	public float getOldx(int index)
	{
		return oldx[index];
	}

	public float getOldy(int index)
	{
		return oldy[index];
	}

	public float getDeltax(int index)
	{
		return deltax[index];
	}

	public float getDeltay(int index)
	{
		return deltay[index];
	}

	public boolean getPressed(int index)
	{
		return pressed[index];
	}
}
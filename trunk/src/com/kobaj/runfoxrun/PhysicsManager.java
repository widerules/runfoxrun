package com.kobaj.runfoxrun;

import java.util.HashSet;
import java.util.Iterator;

import android.graphics.Rect;

public class PhysicsManager
{
	private int width;
	private int height;
	
	private boolean set = false;
	private Sprite physObj;
	private HashSet<Sprite> interactables;
	
	private final float userAcc = 15f / 100000;
	private float userVel = 0;
	
	private float scrollvalue;
	
	//specialized
	private boolean inTheAir = false;
	private boolean canJump = true;
	
	public void jump()
	{
		if(canJump) //better way of doing this would be "if touching object, then can jump"
		{
			inTheAir = true;
			canJump = false;
		}
	}
	
	public int getPhyObjCount()
	{
		return interactables.size();
	}
	
	public PhysicsManager(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		interactables = new HashSet<Sprite>();
	}
	
	private void removePhys(Sprite input)
	{
		interactables.remove(input);
	}
	
	public void addPhys(Sprite input)
	{
		interactables.add(input);
	}
	
	public void setScrollRate(float value)
	{
		scrollvalue = value;
	}
	
	public void onUpdate(float delta)
	{
		//make our user go doooown.
		if(inTheAir)
		{
			userVel = -2.0f / 10.0f;
			inTheAir = false;
		}
		userVel += (float)(userAcc * delta);
		physObj.setyPos((physObj.getyPos() + (userVel * delta)));
		
		//try to iterate only once
		float amount = scrollvalue * delta / 100; //arbitrary
		
		for (Iterator<Sprite> i = interactables.iterator(); i.hasNext();) 
		{
		    Sprite element = i.next();
		    if (element.getxPos() < element.getWidth() * -1) 
		    {
		        i.remove();
		    }
		    else
		    {
		    	if(element.getxPos() < width)
		    	{
		    		if(set)
		    			checkForCollisions(element);
		    	}
		    	
		    	element.setxPos((element.getxPos() + amount));	
		    }
		}
		
		//just for now, might be removed later
		//possibly deleteme
		if(physObj.getyPos() > height)
		{
			//TODO death?
			physObj.setyPos(10);
			userVel = 0;
		}
	}
	
	public void setPlayer(Sprite input)
	{
		//only ever once. Final doesn't work.
		if(!set)
		{
			physObj = input;
			set = true;
		}
	}
	
	private void handleCollisions(int amount, boolean death)
	{
		if(death)
		{
			//TODO handle death
		}
		else if(amount > 0)
		{
			canJump = true;
			userVel = 0;
			physObj.setyPos((physObj.getyPos() - amount + 1));
		}
	}
	
	private void checkForCollisions(Sprite element)
	{	
		for(Iterator<physRect> i = element.getPhysRect().iterator(); i.hasNext();)
		{
			physRect elementPhysRect = i.next();
			for(Iterator<physRect> e = physObj.getPhysRect().iterator(); e.hasNext();)
			{
				physRect physObjPhysRect = e.next();
				
				int amount = collisionDetec(elementPhysRect.getCollRect(), physObjPhysRect.getCollRect());
				
				handleCollisions(amount, elementPhysRect.getHurts());
			}
		}
	}
	
	private int collisionDetec(Rect obj1, Rect obj2)
	{
		Rect collision = new Rect();
		
		if(collision.setIntersect(obj1, obj2))
		{
			return Math.abs(collision.height());
		}
		
		return 0;
	}
}

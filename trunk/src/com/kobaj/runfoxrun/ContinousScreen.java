package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Canvas;

public class ContinousScreen
{
	private ArrayList<Sprite> hitList;
	
	private Resources mResources;
	
	private InputManager im;
	private PhysicsManager pm;
	
	Random rand = new Random();
	int nextval = 0;
	
	private boolean initialized = false;
	
	public void onInitialize(Resources resources, InputManager im, PhysicsManager pm)
	{
		// THIS CLASS IS NOT COMPLETE: simply an example of how a Screen can be
		// used
		// will be completed later.
		
		this.im = im;
		this.pm = pm;
		mResources = resources;
		
		hitList = new ArrayList<Sprite>();
		
		for (int i = 0; i < 4; i++)
			hitList.add(new Sprite());
		
		for (int i = 0; i < 4; i++)
			hitList.get(i).onInitalize(resources, R.drawable.green, i * 180, 480 - 10); // TODO fix this shit.																			// shit.
		
		for(int i = 0; i < 4; i++)
		{
			this.pm.addPhys(hitList.get(i));
		}
		
		initialized = true;	
	}
	
	public void onUpdate(float delta)
	{
		if (initialized)
		{
			// everything below this line
			
			if (pm.getPhyObjCount() < 7)
			{
				int blah = rand.nextInt(4);
				
				if (blah == 1)
				{
					Sprite green = new Sprite();
					green.onInitalize(mResources, R.drawable.green, rand.nextInt(800) + 800, rand.nextInt(370) + 30);
					hitList.add(green);
					pm.addPhys(green);
					// green
				}
				
				if (blah == 2)
				{
					Sprite green = new Sprite();
					green.onInitalize(mResources, R.drawable.red, rand.nextInt(800) + 800, rand.nextInt(370) + 30);
					hitList.add(green);
					pm.addPhys(green);
				}
				
				if (blah == 3)
				{
					Sprite green = new Sprite();
					green.onInitalize(mResources, R.drawable.blue, rand.nextInt(800) + 800, rand.nextInt(370) + 30);
					hitList.add(green);
					pm.addPhys(green);
				}
				
				if (blah > 3)
				{
					// space
				}
			}
			
			for (int i = 0; i < im.fingerCount; i++)
			{
				if (im.getPressed(i))
				{
					pm.jump();
				}
			}
		}
	}
	
	public void onDraw(Canvas canvas)
	{
		if (initialized)
		{
			for(int i = 0; i < hitList.size(); i++)
			{
				if(hitList.get(i).getxPos() < hitList.get(i).getWidth() * -1)
				{
					pm.removePhys(hitList.get(i));
					hitList.remove(i);
				}
				
				hitList.get(i).onDraw(canvas);
			}
		}
	}
}

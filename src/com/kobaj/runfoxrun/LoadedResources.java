package com.kobaj.runfoxrun;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LoadedResources
{
	private static Bitmap green;
	private static Bitmap blue;
	private static Bitmap red;
	private static Bitmap background1;
	private static Bitmap icon;
	
	private static boolean loaded = false;
	
	public static void load(Resources resources)
	{
		red = BitmapFactory.decodeResource(resources, R.drawable.red);
		green = BitmapFactory.decodeResource(resources, R.drawable.green);
		blue = BitmapFactory.decodeResource(resources, R.drawable.blue);
		
		background1 = BitmapFactory.decodeResource(resources, R.drawable.background1);
		
		icon = BitmapFactory.decodeResource(resources,  R.drawable.icon);
		
		loaded = true;
	}
	
	public static Bitmap getIcon(Resources resources)
	{
		if(loaded)
			return icon;
		
		load(resources);
			return icon;
	}
	
	public static Bitmap getBackground1(Resources resources)
	{
		if(loaded)
			return background1;
		
		load(resources);
			return background1;
	}	
	
	public static Bitmap getGreen(Resources resources)
	{
		if(loaded)
			return green;
		
		load(resources);
			return green;
	}
	
	public static Bitmap getBlue(Resources resources)
	{
		if(loaded)
			return blue;
		
		load(resources);
			return blue;
	}
	
	public static Bitmap getRed(Resources resources)
	{
		if(loaded)
			return red;
		
		load(resources);
			return red;
	}
}

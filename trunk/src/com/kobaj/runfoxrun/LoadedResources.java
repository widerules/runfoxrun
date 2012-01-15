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
	private static Bitmap badguy;
	
	private static Bitmap star;
	
	private static Bitmap bigBackGround;
	private static Bitmap bigBuilding;
	private static Bitmap bigVine;
	private static Bitmap grass;
	private static Bitmap littleVine;
	private static Bitmap sandFlat;
	private static Bitmap sandHole;
	private static Bitmap sandHoleLeft;
	private static Bitmap sandHoleRight;
	private static Bitmap smallBuilding;
	private static Bitmap smallTree;
	private static Bitmap tree;
	private static Bitmap weed; 
	
	private static Bitmap foxTwo;
	
	private static boolean loaded = false;
	
	public static void load(Resources resources)
	{
		red = BitmapFactory.decodeResource(resources, R.drawable.red);
		green = BitmapFactory.decodeResource(resources, R.drawable.green);
		blue = BitmapFactory.decodeResource(resources, R.drawable.blue);
		
		background1 = BitmapFactory.decodeResource(resources, R.drawable.background1);
		
		badguy = BitmapFactory.decodeResource(resources, R.drawable.smoke);
		
		icon = BitmapFactory.decodeResource(resources,  R.drawable.icon);
		
		star = BitmapFactory.decodeResource(resources, R.drawable.star);
		
		bigBackGround = BitmapFactory.decodeResource(resources, R.drawable.giantbackdrop);
		bigBuilding = BitmapFactory.decodeResource(resources, R.drawable.bigbuilding);
		bigVine = BitmapFactory.decodeResource(resources, R.drawable.bigvine);
		grass = BitmapFactory.decodeResource(resources, R.drawable.grass);
		littleVine = BitmapFactory.decodeResource(resources, R.drawable.littlevine);
		sandFlat = BitmapFactory.decodeResource(resources, R.drawable.sandflat);
		sandHole = BitmapFactory.decodeResource(resources, R.drawable.sandhole);
		sandHoleLeft = BitmapFactory.decodeResource(resources, R.drawable.sandholeleft);
		sandHoleRight = BitmapFactory.decodeResource(resources, R.drawable.sandholeright);
		smallBuilding = BitmapFactory.decodeResource(resources, R.drawable.smallbuilding);
		smallTree = BitmapFactory.decodeResource(resources, R.drawable.smalltree);
		tree = BitmapFactory.decodeResource(resources, R.drawable.tree);
		weed = BitmapFactory.decodeResource(resources, R.drawable.weed);
		
		foxTwo = BitmapFactory.decodeResource(resources, R.drawable.fox2);
		
		loaded = true;
	}
	
	public static Bitmap getStar(Resources resources)
	{
		if(loaded)
			return star;
		
		load(resources);
			return star;
	}
	
	public static Bitmap getBadGuy(Resources resources)
	{
		if(loaded)
			return badguy;
		
		load(resources);
			return badguy;
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
	
	//screw that resource crap

	public static Bitmap getBigBackGround()
	{
		return bigBackGround;
	}

	public static Bitmap getBigBuilding()
	{
		return bigBuilding;
	}

	public static Bitmap getBigVine()
	{
		return bigVine;
	}

	public static Bitmap getGrass()
	{
		return grass;
	}

	public static Bitmap getLittleVine()
	{
		return littleVine;
	}

	public static Bitmap getSandFlat()
	{
		return sandFlat;
	}

	public static Bitmap getSandHole()
	{
		return sandHole;
	}

	public static Bitmap getSandHoleLeft()
	{
		return sandHoleLeft;
	}

	public static Bitmap getSandHoleRight()
	{
		return sandHoleRight;
	}

	public static Bitmap getSmallBuilding()
	{
		return smallBuilding;
	}

	public static Bitmap getSmallTree()
	{
		return smallTree;
	}

	public static Bitmap getTree()
	{
		return tree;
	}

	public static Bitmap getWeed()
	{
		return weed;
	}

	public static Bitmap getFoxTwo()
	{
		return foxTwo;
	}
}

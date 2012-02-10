package com.kobaj.runfoxrun;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LoadedResources
{
	private static Bitmap green;
	private static Bitmap blue;
	private static Bitmap red;
	private static Bitmap icon;
	private static Bitmap badguy;
	
	private static Bitmap star;
	
	private static Bitmap background1;
	
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
	
	private static Bitmap deadtree;
	
	private static Bitmap level3ground;
	
	private static Bitmap foxTwo;
	
	private static Bitmap black;
	
	private static Bitmap mainFox;
	
	private static Bitmap titleScreen;
	
	private static boolean loaded = false;
	
	private static BitmapFactory.Options bfOptions = new BitmapFactory.Options();
	private static BitmapFactory.Options bfOpts = new BitmapFactory.Options();
	
	public static void preLoad()
	{
		bfOpts.inPurgeable = true;
		bfOpts.inScaled = false;
		bfOpts.inSampleSize = 1;
		//bfOpts.inDensity = 0;
		//bfOpts.inTargetDensity = 0;
		
		bfOptions.inPurgeable=true;
		bfOptions.inScaled = false;
		bfOptions.inSampleSize = 1;
		//bfOptions.inDensity = 0;
		//bfOptions.inTargetDensity = 0;
		bfOptions.inPreferredConfig = Bitmap.Config.RGB_565;
	}
	
	public static void load(Resources resources)
	{
		System.gc();
		 
		background1 = BitmapFactory.decodeResource(resources, R.drawable.background1, bfOptions);
		titleScreen = BitmapFactory.decodeResource(resources, R.drawable.titlescreen, bfOptions);
		
		red = BitmapFactory.decodeResource(resources, R.drawable.red, bfOptions);
		green = BitmapFactory.decodeResource(resources, R.drawable.green, bfOptions);
		blue = BitmapFactory.decodeResource(resources, R.drawable.blue, bfOptions);
		
		badguy = BitmapFactory.decodeResource(resources, R.drawable.smoke , bfOpts);
		
		icon = BitmapFactory.decodeResource(resources, R.drawable.icon , bfOpts);
		
		star = BitmapFactory.decodeResource(resources, R.drawable.star , bfOpts);
		
		bigBuilding = BitmapFactory.decodeResource(resources, R.drawable.bigbuilding , bfOpts);
		bigVine = BitmapFactory.decodeResource(resources, R.drawable.bigvine , bfOpts);
		grass = BitmapFactory.decodeResource(resources, R.drawable.grass , bfOpts);
		littleVine = BitmapFactory.decodeResource(resources, R.drawable.littlevine , bfOpts);
		sandFlat = BitmapFactory.decodeResource(resources, R.drawable.sandflat , bfOpts);
		sandHole = BitmapFactory.decodeResource(resources, R.drawable.sandhole , bfOpts);
		sandHoleLeft = BitmapFactory.decodeResource(resources, R.drawable.sandholeleft , bfOpts);
		sandHoleRight = BitmapFactory.decodeResource(resources, R.drawable.sandholeright , bfOpts);
		smallBuilding = BitmapFactory.decodeResource(resources, R.drawable.smallbuilding , bfOpts);
		smallTree = BitmapFactory.decodeResource(resources, R.drawable.smalltree , bfOpts);
		tree = BitmapFactory.decodeResource(resources, R.drawable.tree , bfOpts);
		weed = BitmapFactory.decodeResource(resources, R.drawable.weed , bfOpts);
		foxTwo = BitmapFactory.decodeResource(resources, R.drawable.fox2 , bfOpts);
		level3ground = BitmapFactory.decodeResource(resources, R.drawable.level3ground , bfOpts);
		deadtree = BitmapFactory.decodeResource(resources, R.drawable.deadtree , bfOpts);	
		black = BitmapFactory.decodeResource(resources, R.drawable.black , bfOptions);
		mainFox = BitmapFactory.decodeResource(resources, R.drawable.foxmain , bfOpts);
		
		BIGVine = XMLHandler.readSerialFile(resources, R.raw.bigvine, Sprite.class);
		LITtleVine = XMLHandler.readSerialFile(resources, R.raw.littlevine, Sprite.class);
		SANdHole = XMLHandler.readSerialFile(resources, R.raw.sandhole, Sprite.class);
		SMAllTree = XMLHandler.readSerialFile(resources, R.raw.smalltree, Sprite.class);
		TREe = XMLHandler.readSerialFile(resources, R.raw.tree, Sprite.class);
		WEEd = XMLHandler.readSerialFile(resources, R.raw.weed, Sprite.class);
		DEAdTree = XMLHandler.readSerialFile(resources, R.raw.deadtree, Sprite.class);
		
		System.gc();
		
		loaded = true;
	}
	
	private static Sprite BIGVine;
	private static Sprite LITtleVine;
	private static Sprite SANdHole;
	private static Sprite SMAllTree;
	private static Sprite TREe;
	private static Sprite WEEd;
	private static Sprite DEAdTree;
	
	public static Bitmap getTitle(Resources resources)
	{
		return titleScreen;
	}
	
	public static Bitmap getMainFox()
	{
		return mainFox;
	}
	
	public static Bitmap getStar(Resources resources)
	{
		if (loaded)
			return star;
		
		load(resources);
		return star;
	}
	
	public static Bitmap getBadGuy(Resources resources)
	{
		if (loaded)
			return badguy;
		
		load(resources);
		return badguy;
	}
	
	public static Bitmap getIcon(Resources resources)
	{
		if (loaded)
			return icon;
		
		load(resources);
		return icon;
	}
	
	public static Bitmap getBackground1(Resources resources)
	{
		return background1;
	}
	
	public static Bitmap getGreen(Resources resources)
	{
		if (loaded)
			return green;
		
		load(resources);
		return green;
	}
	
	public static Bitmap getBlue(Resources resources)
	{
		if (loaded)
			return blue;
		
		load(resources);
		return blue;
	}
	
	public static Bitmap getRed(Resources resources)
	{
		if (loaded)
			return red;
		
		load(resources);
		return red;
	}
	
	public static Bitmap getBackgroundONE(Resources resources)
	{ 
		return BitmapFactory.decodeResource(resources, R.drawable.backdropone, bfOptions);
	}
	public static Bitmap getBackgroundTWO(Resources resources)
	{ 
		return BitmapFactory.decodeResource(resources, R.drawable.backdroptwo, bfOptions);
	}
	public static Bitmap getBackgroundTHREE(Resources resources)
	{ 
		return BitmapFactory.decodeResource(resources, R.drawable.backdropthree, bfOptions);
	}
	public static Bitmap getBackgroundFOUR(Resources resources)
	{ 
		return BitmapFactory.decodeResource(resources, R.drawable.backdropfour, bfOptions);
	}
	public static Bitmap getBackgroundFIVE(Resources resources)
	{ 
		return BitmapFactory.decodeResource(resources, R.drawable.backdropfive, bfOptions);
	}
	
	// screw that resource crap
	
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
	
	public static Bitmap getLevel3Ground()
	{
		return level3ground;
	}
	
	public static Bitmap getDeadTree()
	{
		return deadtree;
	}
	
	public static Bitmap getBlack()
	{
		return black;
	}
	
	public static Sprite getBIGVine()
	{
		return BIGVine;
	}
	
	public static Sprite getLITtleVine()
	{
		return LITtleVine;
	}
	
	public static Sprite getSANdHole()
	{
		return SANdHole;
	}
	
	public static Sprite getSMAllTree()
	{
		return SMAllTree;
	}
	
	public static Sprite getTREe()
	{
		return TREe;
	}
	
	public static Sprite getWEEd()
	{
		return WEEd;
	}
	
	public static Sprite getDEAdTree()
	{
		return DEAdTree;
	}
}

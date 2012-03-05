package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class MapMakerScreen implements Runnable
{
	private Thread thread;
	
	private MapMakerModes currentMode;
	
	// probably could have done this a LOT better
	private custString[] arrowArray = new custString[2];
	private custString objectSelectedDisplay;
	private String objectSelected = "BigBuilding";
	private int objectSelectedInt = 0;
	private final String[] objectOptions = { "BigBuilding", "BigVine", "Black", "Blue", "DeadTree", "Grass", "Green", "LittleVine", "Red", "SandFlat", "SandHole", "SandHoleLeft", "SandHoleRight",
			"SmallBuilding", "SmallTree", "Star", "Tree", "Weed" };
	
	private Resources resources;
	
	private custnewlineString quit;
	private custnewlineString camera;
	private custnewlineString select;
	private custnewlineString move;
	private custnewlineString place;
	private custnewlineString delete;
	private custString save;
	private custString load;
	private custString cancel;
	private custString filename;
	private custString saved;
	private custString loading;
	
	private ArrayList<custString> allFiles;
	
	private int invisInt = 0;
	
	private Bitmap progressBarIcon;
	
	private ArrayList<Sprite> hitList;
	
	private Paint linePaint;
	private Paint bitmapPaint;
	private Paint hurtPaint;
	private Paint nohurtPaint;
	private Paint outlinePaint;
	private Paint selectedPaint;
	
	private float scale;
	
	private InputManager im;
	private PhysicsManager pm;
	private SoundManager sm;
	
	private int width;
	private int height;
	
	private Sprite temp;
	private Sprite selected;
	
	private int pad;
	
	private final float mapLimit = 16000.0f;
	
	// preloaded sprites
	private Sprite BIGVine;
	private Sprite LITtleVine;
	private Sprite SANdHole;
	private Sprite SMAllTree;
	private Sprite TREe;
	private Sprite WEEd;
	private Sprite DEAdTree;
	
	public MapMakerScreen(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		pad = (int) (width / 4.0f);
		
		currentMode = MapMakerModes.camera;
	};
	
	public void onInitialize(Resources resources, InputManager im, PhysicsManager pm, SoundManager sm)
	{
		this.scale = SurfacePanel.scale;
		
		this.im = im;
		this.pm = pm;
		this.sm = sm;
		
		this.resources = resources;
		
		allFiles = new ArrayList<custString>();
		
		quit = new custnewlineString(resources, "Title Screen", (int) (380 * scale), (int) (300 * scale));
		quit.setSize((int) (20.0f * scale));
		
		save = new custString(resources, "Save", (int) (300 * scale), (int) (300 * scale));
		save.setSize((int) (20.0f * scale));
		
		load = new custString(resources, "Load", (int) (200 * scale), (int) (300 * scale));
		load.setSize((int) (20.0f * scale));
		
		// should make this centered
		cancel = new custString(resources, "Cancel", (int) (100 * scale), (int) (300 * scale));
		cancel.setSize((int) (20.0f * scale));
		
		int total = 0;
		String[] temparray = XMLHandler.getFileList();
		if (temparray != null)
			if (temparray.length != 0)
				for (int i = temparray.length - 1; i >= 0; i--)
				{
					String[] splitFile = temparray[i].split("_");
					if (splitFile.length == 2)
						if (splitFile[0].equalsIgnoreCase("FoxDash"))
						{
							int myNum = 0;
							String[] splitAgain = splitFile[1].split("\\.");
							if (splitAgain.length != 0)
							{
								allFiles.add(new custString(resources, splitFile[0] + "_" + splitAgain[0], 0, 0));
								
								try
								{
									myNum = Integer.parseInt(splitAgain[0]);
								}
								catch (NumberFormatException nfe)
								{
									
								}
							}
							
							total = myNum;
						}
				}
		total += 1;
		
		setFileName("FoxDash_" + String.valueOf(total));
		
		setAllFilePositions();
		
		camera = new custnewlineString(resources, "Move\nCamera", (int) (20 * scale), (int) (50 * scale));
		camera.setColor(Color.YELLOW, Color.BLACK);
		camera.setSize((int) (20.0f * scale));
		
		select = new custnewlineString(resources, "Select\nObject", (int) (20 * scale), (int) (100 * scale));
		select.setColor(Color.WHITE, Color.BLACK);
		select.setSize((int) (20.0f * scale));
		
		move = new custnewlineString(resources, "Move\nObject", (int) (20 * scale), (int) (150 * scale));
		move.setColor(Color.WHITE, Color.BLACK);
		move.setSize((int) (20.0f * scale));
		
		place = new custnewlineString(resources, "Place\nObject", (int) (20 * scale), (int) (200 * scale));
		place.setColor(Color.WHITE, Color.BLACK);
		place.setSize((int) (20.0f * scale));
		
		delete = new custnewlineString(resources, "Delete\nObject", (int) (20 * scale), (int) (250 * scale));
		delete.setColor(Color.RED, Color.BLACK);
		delete.setSize((int) (20.0f * scale));
		
		saved = new custString(resources, "File Saved", 0, 0);
		saved.setPosition((int) (width / 2.0f - saved.measureit() / 2.0f), (int) (height / 2.0f));
		saved.setSize((int) (30.0f * scale));
		saved.setColor(Color.TRANSPARENT, Color.TRANSPARENT);
		
		loading = new custString(resources, "Loading", 0, 0);
		loading.setPosition((int) (width / 2.0f - saved.measureit() / 2.0f), (int) (height / 2.0f));
		loading.setSize((int) (30.0f * scale));
		
		progressBarIcon = LoadedResources.getIcon(resources);
		
		linePaint = new Paint();
		linePaint.setColor(Color.WHITE);
		linePaint.setStrokeWidth(1);
		linePaint.setShadowLayer(1, 0, 0, Color.BLACK);
		
		hurtPaint = new Paint();
		hurtPaint.setColor(Color.RED);
		hurtPaint.setStrokeWidth(1);
		hurtPaint.setStyle(Style.STROKE);
		
		nohurtPaint = new Paint();
		nohurtPaint.setColor(Color.BLACK);
		nohurtPaint.setStrokeWidth(1);
		nohurtPaint.setStyle(Style.STROKE);
		
		outlinePaint = new Paint();
		outlinePaint.setColor(Color.YELLOW);
		outlinePaint.setStrokeWidth(3);
		outlinePaint.setStyle(Style.STROKE);
		outlinePaint.setShadowLayer(1, 0, 0, Color.BLACK);
		
		selectedPaint = new Paint();
		selectedPaint.setColor(Color.YELLOW);
		selectedPaint.setStrokeWidth(3);
		selectedPaint.setStyle(Style.STROKE);
		selectedPaint.setShadowLayer(1, 0, 0, Color.BLACK);
		
		bitmapPaint = new Paint();
		
		hitList = new ArrayList<Sprite>();
		
		preLoadSprites();
		
		for (int i = 0; i < arrowArray.length; i++)
		{
			String temp = " v ";
			
			if (i % 2 == 0)
				temp = " ^ ";
			
			arrowArray[i] = new custString(resources, temp, (int) (415.0f * scale), (int) ((i + 1) * 38.0f * scale) + 20);
			arrowArray[i].setSize(60);
			arrowArray[i].setColor(Color.GREEN, Color.BLACK);
		}
		
		manageObjectChoice();
	};
	
	private void setAllFilePositions()
	{
		float amount = 40;
		
		int x = (int) amount;
		int y = (int) amount;
		
		for (Iterator<custString> it = allFiles.iterator(); it.hasNext();)
		{
			custString temp = it.next();
			
			temp.setPosition(x, y);
			
			y += (int) (amount * scale);
			
			if (y > 300 * scale)
			{
				x += (int) (130.0f * scale);
				y = (int) (amount * scale);
			}
		}
	}
	
	private String loadingfileName;
	
	private void loadmap(String filen)
	{
		this.loadingfileName = filen;
		pm.purge();
		hitList.clear();
		
		System.gc();
		
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run()
	{
		custLevel cutemp = XMLHandler.readSerialFile(loadingfileName, custLevel.class);
		if (cutemp != null)
		{
			cutemp.onInitialize(resources, width, height, sm);
			
			for (Iterator<Sprite> it = cutemp.getlevelSpriteList().iterator(); it.hasNext();)
			{
				temp = it.next();
				hitList.add(temp);
				pm.addPhys(temp);
			}
			
			setFileName(loadingfileName);
		}
		currentMode = MapMakerModes.camera;
	}
	
	private void savemap()
	{
		if (!hitList.isEmpty())
		{
			custLevel tobeSaved = new custLevel(filename.getString());
			tobeSaved.setObjectList(hitList);
			XMLHandler.writeSerialFile(tobeSaved, filename.getString());
		}
	}
	
	private void setFileName(String file)
	{
		filename = new custString(resources, file, (int) (50 * scale), (int) (310 * scale));
		filename.setColor(Color.GRAY, Color.BLACK);
		filename.setSize((int) (20.0f * scale));
	}
	
	private void manageObjectChoice()
	{
		objectSelectedDisplay = new custString(resources, objectSelected, (int) (415.0f * scale), (int) (60.0f * scale));
		System.gc();
	}
	
	private void preLoadSprites()
	{
		// in an attempt to make loading faster
		BIGVine = LoadedResources.getBIGVine();
		LITtleVine = LoadedResources.getLITtleVine();
		SANdHole = LoadedResources.getSANdHole();
		SMAllTree = LoadedResources.getSMAllTree();
		TREe = LoadedResources.getTREe();
		WEEd = LoadedResources.getWEEd();
		DEAdTree = LoadedResources.getDEAdTree();
	}
	
	public boolean onUpdate(float delta)
	{
		if (invisInt > 0 && invisInt < 5000)
		{
			int Multiplicity = (int) linInterp(0, 5000, invisInt, 256, 0);
			int mainColor = Color.argb(Multiplicity, 255, 255, 255);
			int outlineColor = Color.argb(Multiplicity, 0, 0, 0);
			
			saved.setColor(mainColor, outlineColor);
			
			invisInt += delta;
		}
		
		for (int i = 0; i < im.fingerCount; i++)
		{
			boolean imRelease = im.getReleased(i);
			boolean imPressed = im.getTouched(i);
			boolean taps = false;
			
			if (imRelease)
			{
				if (quit.fingertap((int) im.getX(i), (int) im.getY(i)))
					return false;
				
				if (currentMode != MapMakerModes.load)
				{
					if (place.fingertap((int) im.getX(i), (int) im.getY(i)))
					{
						currentMode = MapMakerModes.place;
						outlinePaint.setColor(Color.YELLOW);
						
						resetModeColors();
						place.setColor(Color.YELLOW, Color.BLACK);
						taps = true;
					}
					
					else if (camera.fingertap((int) im.getX(i), (int) im.getY(i)))
					{
						currentMode = MapMakerModes.camera;
						outlinePaint.setColor(Color.YELLOW);
						
						resetModeColors();
						camera.setColor(Color.YELLOW, Color.BLACK);
						taps = true;
					}
					
					else if (move.fingertap((int) im.getX(i), (int) im.getY(i)))
					{
						currentMode = MapMakerModes.move;
						outlinePaint.setColor(Color.YELLOW);
						
						resetModeColors();
						move.setColor(Color.YELLOW, Color.BLACK);
						taps = true;
					}
					
					else if (select.fingertap((int) im.getX(i), (int) im.getY(i)))
					{
						currentMode = MapMakerModes.select;
						outlinePaint.setColor(Color.YELLOW);
						
						resetModeColors();
						select.setColor(Color.YELLOW, Color.BLACK);
						taps = true;
					}
					
					else if (delete.fingertap((int) im.getX(i), (int) im.getY(i)))
					{
						if (selected != null)
						{
							hitList.remove(selected);
							pm.removePhys(selected);
							
							selected = null;
							System.gc();
						}
						
						taps = true;
					}
					else if (arrowArray[0].fingertap((int) im.getX(i), (int) im.getY(i)))
					{
						objectSelectedInt = (objectSelectedInt + 1) % (objectOptions.length);
						
						objectSelected = objectOptions[objectSelectedInt];
						
						manageObjectChoice();
						
						taps = true;
					}
					else if (arrowArray[1].fingertap((int) im.getX(i), (int) im.getY(i)))
					{
						objectSelectedInt = objectSelectedInt - 1;
						if (objectSelectedInt < 0)
							objectSelectedInt = objectOptions.length - 1;
						
						objectSelected = objectOptions[objectSelectedInt];
						
						manageObjectChoice();
						
						taps = true;
					}
					else if (save.fingertap((int) im.getX(i), (int) im.getY(i)))
					{
						invisInt = 1;
						savemap();
						taps = true;
					}
				}
				else
				{
					if (cancel.fingertap((int) im.getX(i), (int) im.getY(i)))
					{
						currentMode = MapMakerModes.camera;
						taps = true;
					}
					
					for (Iterator<custString> cuit = allFiles.iterator(); cuit.hasNext();)
					{
						custString cutemp = cuit.next();
						
						if (cutemp.fingertap((int) im.getX(i), (int) im.getY(i)))
						{
							currentMode = MapMakerModes.loading;
							loadmap(cutemp.getString());
							taps = true;
						}
					}
				}
				
				if (load.fingertap((int) im.getX(i), (int) im.getY(i)))
				{
					currentMode = MapMakerModes.load;
					taps = true;
				}
				
			}
			
			if (!taps)
			{
				if (currentMode == MapMakerModes.place)
				{
					if (imRelease)
					{
						setallNotSelected();
						getObject(i);
					}
				}
				
				if (currentMode == MapMakerModes.move)
				{
					if (imPressed)
						if (selected != null)
						// for (Iterator<physRect> phit =
						// selected.getPhysRect().iterator(); phit.hasNext();)
						{
							// physRect tempPhys = phit.next();
							
							// if (tempPhys.getCollRect().contains((int)
							// im.getX(i), (int) im.getY(i)))
							{
								selected.setxPos((selected.getxPos() + im.getDeltax(i) * scale));
								selected.setyPos((selected.getyPos() + im.getDeltay(i) * scale));
							}
						}
					
				}
				
				if (currentMode == MapMakerModes.select)
				{
					if (imRelease)
					{
						setallNotSelected();
						
						outerLoop:
						for (ListIterator<Sprite> it = hitList.listIterator(hitList.size()); it.hasPrevious();)
						{
							temp = it.previous();
							
							int spritePosx = (int) temp.getxPos();
							int spriteWidth = (int) temp.getWidth();
							if (spritePosx < width && spritePosx + spriteWidth > 0)
							{
								for (Iterator<physRect> phit = temp.getPhysRect().iterator(); phit.hasNext();)
								{
									physRect tempPhys = phit.next();
									
									if (tempPhys.getCollRect().contains((int) im.getX(i), (int) im.getY(i)))
									{
										temp.setSelected(true);
										selected = temp;
										break outerLoop;
									}
								}
							}
						}
					}
				}
				
				if (currentMode == MapMakerModes.camera)
				{
					int deltax = (int) im.getDeltax(i);
					pm.setScrollProgress(pm.getScrollProgress() + -deltax / 3.2f, false);
					
					if (pm.getScrollProgress() < 0)
						pm.setScrollProgress(0, false);
					else if (pm.getScrollProgress() > mapLimit)
						pm.setScrollProgress(mapLimit, false);
				}
				
			}
		}
		
		return true;
	};
	
	public void onDraw(Canvas canvas)
	{
		// delete me
		canvas.drawColor(Color.BLUE);
		
		if (currentMode != MapMakerModes.load && currentMode != MapMakerModes.loading)
		{
			
			// interaction layer
			for (Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
			{
				temp = it.next();
				
				int spritePosx = (int) temp.getxPos();
				int spriteWidth = (int) temp.getWidth();
				if (spritePosx < width && spritePosx + spriteWidth > 0)
				{
					temp.onDraw(canvas);
					
					for (Iterator<physRect> phit = temp.getPhysRect().iterator(); phit.hasNext();)
					{
						physRect phytemp = phit.next();
						
						if (!temp.getSelected())
						{
							if (phytemp.getHurts())
								canvas.drawRect(phytemp.getCollRect(), hurtPaint);
							else
								canvas.drawRect(phytemp.getCollRect(), nohurtPaint);
						}
						else
							canvas.drawRect(phytemp.getCollRect(), selectedPaint);
					}
				}
			}
			
			// overlay (I should really not be doing math/logic here >.<
			canvas.drawLine(pad, 20.0f / 1.5f * scale, width - pad, 20.0f / 1.5f * scale, linePaint);
			canvas.drawLine(pad, 15.0f / 1.5f * scale, pad, 27.0f / 1.5f * scale, linePaint);
			canvas.drawLine(width - pad, 15.0f / 1.5f * scale, width - pad, 27.0f / 1.5f * scale, linePaint);
			canvas.drawBitmap(progressBarIcon, linInterp(0, mapLimit / 1.5f * scale, pm.getScrollProgress(), pad, width - pad - progressBarIcon.getWidth()), 0, bitmapPaint);
			
			camera.onDraw(canvas);
			select.onDraw(canvas);
			move.onDraw(canvas);
			place.onDraw(canvas);
			delete.onDraw(canvas);
			
			saved.onDraw(canvas);
			
			load.onDraw(canvas);
			
			save.onDraw(canvas);
			filename.onDraw(canvas);
			
			objectSelectedDisplay.onDraw(canvas);
			
			for (int i = arrowArray.length - 1; i >= 0; i--)
			{
				arrowArray[i].onDraw(canvas);
			}
		}
		else if (currentMode == MapMakerModes.load)
		{
			cancel.onDraw(canvas);
			for (Iterator<custString> it = allFiles.iterator(); it.hasNext();)
			{
				it.next().onDraw(canvas);
			}
		}
		else if (currentMode == MapMakerModes.loading)
		{
			loading.onDraw(canvas);
		}
		
		// buttons
		quit.onDraw(canvas);
		
		// outline color
		// canvas.drawRect(outlineRect, outlinePaint);
	};
	
	// really should make my own Math class...
	private float linInterp(float minX, float maxX, float value, float minY, float maxY)
	{
		if (minX == maxX)
			return minY;
		
		if (value < minX)
			return minY;
		
		if (value > maxX)
			return maxY;
		
		return minY * (value - maxX) / (minX - maxX) + maxY * (value - minX) / (maxX - minX);
	}
	
	private void resetModeColors()
	{
		camera.setColor(Color.WHITE, Color.BLACK);
		select.setColor(Color.WHITE, Color.BLACK);
		move.setColor(Color.WHITE, Color.BLACK);
		place.setColor(Color.WHITE, Color.BLACK);
	}
	
	private void setallNotSelected()
	{
		for (Iterator<Sprite> it = hitList.iterator(); it.hasNext();)
		{
			it.next().setSelected(false);
		}
		
		selected = null;
	}
	
	private void getObject(int i)
	{
		Sprite biggerTemp;
		
		// hard coded
		// its a little dumb, but thats how it goes
		if (objectSelected.equalsIgnoreCase("green"))
		{
			Sprite temp = new Sprite();
			// will be changed to
			// XMLHandler.readSerialFile(getResources(), R.raw.green,
			// Sprite.class);
			temp.onInitialize(LoadedResources.getGreen(resources), (int) im.getX(i), (int) im.getY(i));
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("red"))
		{
			Sprite temp = new Sprite();
			// will be changed to
			// XMLHandler.readSerialFile(getResources(), R.raw.red,
			// Sprite.class);
			temp.onInitialize(LoadedResources.getRed(resources), (int) im.getX(i), (int) im.getY(i));
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("blue"))
		{
			Sprite temp = new Sprite();
			// will be changed to
			// XMLHandler.readSerialFile(getResources(), R.raw.blue,
			// Sprite.class);
			temp.onInitialize(LoadedResources.getBlue(resources), (int) im.getX(i), (int) im.getY(i));
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("star"))
		{
			Sprite temp = XMLHandler.readSerialFile(resources, R.raw.star, Sprite.class);
			temp.onInitialize(LoadedResources.getStar(resources), sm, (int) im.getX(i), (int) im.getY(i) - (int) (24.0f / 1.5f * SurfacePanel.scale), (int) (25.0f / 1.5f * SurfacePanel.scale),
					(int) (24.0f / 1.5f * SurfacePanel.scale));
			temp.setCollectable(CollectableStates.collectable);
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("bigbuilding"))
		{
			Sprite temp = new Sprite();
			temp.onInitialize(LoadedResources.getBigBuilding(), (int) im.getX(i), (int) im.getY(i));
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("smallbuilding"))
		{
			Sprite temp = new Sprite();
			temp.onInitialize(LoadedResources.getSmallBuilding(), (int) im.getX(i), (int) im.getY(i));
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("sandflat"))
		{
			Sprite temp = new Sprite();
			temp.onInitialize(LoadedResources.getSandFlat(), (int) im.getX(i), (int) im.getY(i));
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("bigvine"))
		{
			Sprite temp = new Sprite();
			temp.onInitialize(LoadedResources.getBigVine(), (int) im.getX(i), (int) im.getY(i) - LoadedResources.getBigVine().getHeight());
			temp.getPhysRect().clear();
			for (Iterator<physRect> iter = BIGVine.getPhysRect().iterator(); iter.hasNext();)
			{
				physRect physRectTemp = iter.next();
				Rect collRect = physRectTemp.getCollRect();
				temp.getPhysRect().add(
						new physRect(
								new Rect(collRect.left + (int) temp.getxPos(), collRect.top + (int) temp.getyPos(), collRect.right + (int) temp.getxPos(), collRect.bottom + (int) temp.getyPos()),
								physRectTemp.getHurts()));
			}
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("grass"))
		{
			Sprite temp = XMLHandler.readSerialFile(resources, R.raw.grass, Sprite.class);
			temp.onInitialize(LoadedResources.getGrass(), (int) im.getX(i), (int) im.getY(i));
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("littlevine"))
		{
			Sprite temp = new Sprite();
			temp.onInitialize(LoadedResources.getLittleVine(), (int) im.getX(i), (int) im.getY(i) - LoadedResources.getLittleVine().getHeight());
			temp.getPhysRect().clear();
			for (Iterator<physRect> iter = LITtleVine.getPhysRect().iterator(); iter.hasNext();)
			{
				physRect physRectTemp = iter.next();
				Rect collRect = physRectTemp.getCollRect();
				temp.getPhysRect().add(
						new physRect(
								new Rect(collRect.left + (int) temp.getxPos(), collRect.top + (int) temp.getyPos(), collRect.right + (int) temp.getxPos(), collRect.bottom + (int) temp.getyPos()),
								physRectTemp.getHurts()));
			}
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("sandhole"))
		{
			Sprite temp = new Sprite();
			temp.onInitialize(LoadedResources.getSandHole(), (int) im.getX(i), (int) im.getY(i));
			temp.getPhysRect().clear();
			for (Iterator<physRect> iter = SANdHole.getPhysRect().iterator(); iter.hasNext();)
			{
				physRect physRectTemp = iter.next();
				Rect collRect = physRectTemp.getCollRect();
				temp.getPhysRect().add(
						new physRect(
								new Rect(collRect.left + (int) temp.getxPos(), collRect.top + (int) temp.getyPos(), collRect.right + (int) temp.getxPos(), collRect.bottom + (int) temp.getyPos()),
								physRectTemp.getHurts()));
			}
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("sandholeleft"))
		{
			Sprite temp = new Sprite();
			temp.onInitialize(LoadedResources.getSandHoleLeft(), (int) im.getX(i), (int) im.getY(i));
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("sandholeright"))
		{
			Sprite temp = new Sprite();
			temp.onInitialize(LoadedResources.getSandHoleRight(), (int) im.getX(i), (int) im.getY(i));
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("smalltree"))
		{
			Sprite temp = new Sprite();
			temp.onInitialize(LoadedResources.getSmallTree(), (int) im.getX(i), (int) im.getY(i) - SMAllTree.getPhysRect().get(0).getCollRect().top);
			temp.getPhysRect().clear();
			for (Iterator<physRect> iter = SMAllTree.getPhysRect().iterator(); iter.hasNext();)
			{
				physRect physRectTemp = iter.next();
				Rect collRect = physRectTemp.getCollRect();
				temp.getPhysRect().add(
						new physRect(
								new Rect(collRect.left + (int) temp.getxPos(), collRect.top + (int) temp.getyPos(), collRect.right + (int) temp.getxPos(), collRect.bottom + (int) temp.getyPos()),
								physRectTemp.getHurts()));
			}
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("tree"))
		{
			Sprite temp = new Sprite();
			temp.onInitialize(LoadedResources.getTree(), (int) im.getX(i), (int) im.getY(i) - TREe.getPhysRect().get(0).getCollRect().top);
			temp.getPhysRect().clear();
			for (Iterator<physRect> iter = TREe.getPhysRect().iterator(); iter.hasNext();)
			{
				physRect physRectTemp = iter.next();
				Rect collRect = physRectTemp.getCollRect();
				temp.getPhysRect().add(
						new physRect(
								new Rect(collRect.left + (int) temp.getxPos(), collRect.top + (int) temp.getyPos(), collRect.right + (int) temp.getxPos(), collRect.bottom + (int) temp.getyPos()),
								physRectTemp.getHurts()));
			}
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("weed"))
		{
			Sprite temp = new Sprite();
			temp.onInitialize(LoadedResources.getWeed(), (int) im.getX(i), (int) im.getY(i) - LoadedResources.getWeed().getHeight());
			temp.getPhysRect().clear();
			for (Iterator<physRect> iter = WEEd.getPhysRect().iterator(); iter.hasNext();)
			{
				physRect physRectTemp = iter.next();
				Rect collRect = physRectTemp.getCollRect();
				temp.getPhysRect().add(
						new physRect(
								new Rect(collRect.left + (int) temp.getxPos(), collRect.top + (int) temp.getyPos(), collRect.right + (int) temp.getxPos(), collRect.bottom + (int) temp.getyPos()),
								physRectTemp.getHurts()));
			}
			biggerTemp = temp;
		}
		
		else if (objectSelected.equalsIgnoreCase("deadtree"))
		{
			Sprite temp = new Sprite();
			temp.onInitialize(LoadedResources.getDeadTree(), (int) im.getX(i), (int) im.getY(i));
			temp.getPhysRect().clear();
			for (Iterator<physRect> iter = DEAdTree.getPhysRect().iterator(); iter.hasNext();)
			{
				physRect physRectTemp = iter.next();
				Rect collRect = physRectTemp.getCollRect();
				temp.getPhysRect().add(
						new physRect(
								new Rect(collRect.left + (int) temp.getxPos(), collRect.top + (int) temp.getyPos(), collRect.right + (int) temp.getxPos(), collRect.bottom + (int) temp.getyPos()),
								physRectTemp.getHurts()));
			}
			biggerTemp = temp;
		}
		
		else
		// if(objectSelected.equalsIgnoreCase("black"))
		{
			Sprite temp = new Sprite();
			temp.onInitialize(LoadedResources.getBlack(), (int) im.getX(i), (int) im.getY(i));
			biggerTemp = temp;
		}
		
		biggerTemp.setSelected(true);
		selected = biggerTemp;
		biggerTemp.name = objectSelected;
		hitList.add(biggerTemp);
		pm.addPhys(biggerTemp);
	}
}

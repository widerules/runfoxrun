package com.kobaj.runfoxrun;

import android.content.res.Resources;
import android.graphics.Color;

public class PauseScreen extends ListScreens
{
	private custString contGame;
	private custString titleScreen;
	private custString quit;
	private custString highScores;
	
	private custString[] levels;
	private int levels_access;
	
	public void onInitialize(Resources resources, int identity)
	{
		float scale = SurfacePanel.scale;
		
		count = 11;
		
		contGame = new custString(resources, "Continue", (int) (100 * scale), (int) (140 * scale));
		contGame.setSize((int) (27 * scale));
		
		titleScreen = new custString(resources, "Title Screen", (int) (100 * scale), (int) (173 * scale));
		titleScreen.setSize((int) (27 * scale));
		
		highScores = new custString(resources, "level Select", (int) (100 * scale), (int) (207 * scale));
		highScores.setSize((int) (27 * scale));
		
		levels = new custString[7];
		int i = 0;
		for(i = 0; i < 7; i++)
		{
			levels[i] = new custString(resources, String.valueOf(i + 1), (int) ((250 * scale) + (20 * i * scale)), (int) (207 * scale));
			levels[i].setColor(Color.GRAY, Color.BLACK);
			levels[i].setSize((int) (27 * scale));
		}
		
		levels_access = 0;
		
		quit = new custString(resources, "Quit Game", (int) (100 * scale), (int) (240 * scale));
		quit.setColor(Color.RED, Color.BLACK);
		quit.setSize((int) (27 * scale));
		
		stringList = new custString[count];
		stringList[0] = contGame;
		stringList[1] = titleScreen;
		stringList[2] = highScores;
		stringList[3] = quit;
		
		for(i = 4; i < count; i++)
		{
			stringList[i] = levels[i-4];
		}
		
		initialized = true;
	}
	
	public void setLevels(int i)
	{
		for(int e = 0; e < i; e++)
		{
			levels[e].setColor(Color.WHITE, Color.BLACK);
		}
		
		levels_access = i;
	}
	
	public int onTouchLevels(int x, int y)
	{
		for(int e = 0; e < levels_access; e++)
		{
			if(levels[e].fingertap(x,y))
				return e;
		}
		
		return -1;
	}
	
	public GameStates onTouch(int x, int y)
	{
		if (contGame.fingertap(x, y))
			return GameStates.Resume;
		else if (titleScreen.fingertap(x, y))
			return GameStates.TitleScreen;
		else if (quit.fingertap(x, y))
			return GameStates.Quit;
		
		return GameStates.Pause;
	}
	
}

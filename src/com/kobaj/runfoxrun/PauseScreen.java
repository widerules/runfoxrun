package com.kobaj.runfoxrun;

import android.content.res.Resources;
import android.graphics.Color;

public class PauseScreen extends ListScreens
{
	private custString contGame;
	private custString titleScreen;
	private custString quit;
	private custString highScores;
	
	public void onInitialize(Resources resources, int identity)
	{
		float scale = SurfacePanel.scale;
		
		count = 4;
		
		contGame = new custString(resources, "Continue", (int) (100 * scale), (int) (140 * scale));
		contGame.setSize((int) (27 * scale));
		
		titleScreen = new custString(resources, "Title Screen", (int) (100 * scale), (int) (173 * scale));
		titleScreen.setSize((int) (27 * scale));
		
		highScores = new custString(resources, "level Select", (int) (100 * scale), (int) (207 * scale));
		highScores.setColor(Color.GRAY, Color.BLACK);
		highScores.setSize((int) (27 * scale));
		
		quit = new custString(resources, "Quit Game", (int) (100 * scale), (int) (240 * scale));
		quit.setColor(Color.RED, Color.BLACK);
		quit.setSize((int) (27 * scale));
		
		stringList = new custString[count];
		stringList[0] = contGame;
		stringList[1] = titleScreen;
		stringList[2] = highScores;
		stringList[3] = quit;
		
		initialized = true;
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

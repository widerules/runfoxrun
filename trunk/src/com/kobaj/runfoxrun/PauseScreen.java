package com.kobaj.runfoxrun;

import android.content.res.Resources;
import android.graphics.Color;

public class PauseScreen extends ListScreens
{
	private custString contGame;
	private custString titleScreen;
	private custString quit;
	
	public void onInitialize(Resources resources, int identity)
	{
		count = 3;
		
		//imgBackdrop = BitmapFactory.decodeResource(resources, identity);
		
		contGame = new custString("Continue", 150,210);
		contGame.setSize(40);
		
		titleScreen = new custString("Title Screen", 150, 260);
		titleScreen.setSize(40);
	
		quit = new custString("Quit Game", 150, 360);
		quit.setColor(Color.RED, Color.BLACK);
		quit.setSize(40);
		
		stringList = new custString[count];
		stringList[0] = contGame;
		stringList[1] = titleScreen;
		stringList[2] = quit;
		
		initialized = true;
	}
	public GameStates onTouch(int x, int y)
	{
		if (contGame.fingertap(x, y))
			return GameStates.Resume;
		else if (titleScreen.fingertap(x, y))
			return GameStates.TitleScreen;
		else if(quit.fingertap(x, y))
			return GameStates.Quit;
		
		return GameStates.Pause;
	}
	
}

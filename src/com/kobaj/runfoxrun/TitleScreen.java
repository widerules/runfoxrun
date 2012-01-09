package com.kobaj.runfoxrun;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class TitleScreen extends ListScreens
{	
	private custString newGame;
	private custString quit;
	private custString continousGame;
	private custString highScores;
	
	public void onInitialize(Resources resources, int identity)
	{
		count = 4;
		
		imgBackdrop = BitmapFactory.decodeResource(resources, identity);
		
		newGame = new custString("New Game", 100, 100);
		newGame.setColor(Color.GRAY, Color.BLACK);
		newGame.setSize(35);
		
		continousGame = new custString("Endless Game", 100, 200);
		continousGame.setColor(Color.WHITE, Color.BLACK);
		
		highScores = new custString("High Scores", 100, 300);
		highScores.setColor(Color.GRAY, Color.BLACK);
		
		quit = new custString("Quit Game", 400, 400);
		quit.setColor(Color.RED, Color.BLACK);
		
		stringList = new custString[count];
		stringList[0] = newGame;
		stringList[1] = continousGame;
		stringList[2] = highScores;
		stringList[3] = quit;
		
		initialized = true;
	}
	
	public GameStates onTouch(int x, int y)
	{
		if(newGame.fingertap(x, y))
			return GameStates.SinglePlay;
		else if (continousGame.fingertap(x, y))
			return GameStates.Continous;
		else if (highScores.fingertap(x, y))
			return GameStates.HighScore;
		else if(quit.fingertap(x, y))
			return GameStates.Quit;
		
		return GameStates.TitleScreen;
	}
}

package com.kobaj.runfoxrun;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class TitleScreen extends ListScreens
{	
	private MusicManager mm;
	
	private custString newGame;
	private custString quit;
	private custString continousGame;
	private custString highScores;
	
	public float titleScreenSoundTime = 30001;
	public int titleScreenCurrentSong = 0;
	
	public void onInitialize(Resources resources, int identity, MusicManager mm)
	{
		this.mm = mm;
		
		count = 4;
		
		imgBackdrop = BitmapFactory.decodeResource(resources, identity);
		
		newGame = new custString("New Game", 100, 100);
		newGame.setColor(Color.WHITE, Color.BLACK);
		newGame.setSize(55);
		
		continousGame = new custString("Endless Game", 100, 200);
		continousGame.setColor(Color.WHITE, Color.BLACK);
		continousGame.setSize(55);
		
		highScores = new custString("High Scores", 100, 300);
		highScores.setColor(Color.GRAY, Color.BLACK);
		highScores.setSize(55);
		
		quit = new custString("Quit Game", 400, 400);
		quit.setColor(Color.RED, Color.BLACK);
		quit.setSize(55);
		
		stringList = new custString[count];
		stringList[0] = newGame;
		stringList[1] = continousGame;
		stringList[2] = highScores;
		stringList[3] = quit;
		
		initialized = true;
	}
	
	public void onUpdate(float delta)
	{
		//music
		titleScreenSoundTime += delta;	
		if(titleScreenSoundTime > 30000)
		{
			titleScreenSoundTime = 0;
			
			if(titleScreenCurrentSong == 0)
				mm.ChangeSongs(R.raw.pulse, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
			else if (titleScreenCurrentSong == 1)
				mm.ChangeSongs(R.raw.aegissprint, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
			else if (titleScreenCurrentSong == 2)
				mm.ChangeSongs(R.raw.catchinglightning, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
			else if (titleScreenCurrentSong == 3)
				mm.ChangeSongs(R.raw.blackdiamond, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
			else if (titleScreenCurrentSong == 4)
				mm.ChangeSongs(R.raw.quicken, new SoundFade(0, 1, 0, 3000), new SoundFade(0, 0, 1, 3000));
			
			titleScreenCurrentSong++;
			
			if(titleScreenCurrentSong >=5)
				titleScreenCurrentSong = 0;
		}
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

	@Override
	public void onInitialize(Resources rescoures, int identity)
	{
	 //do nothing
	}
}

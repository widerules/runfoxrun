package com.kobaj.runfoxrun;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

public class MusicManager
{
	private MediaPlayer mp;
	private AudioManager mAudioManager;
	
	private boolean loaded = true;
	
	private SoundFade thisFade;
	private SoundFade nextFade;
	private int nextSong;
	
	private MusicStates thisState;
	
	private Context mContext;
	
	private int currentSong;
	
	public MusicStates getSituation()
	{
		return thisState;
	}
	
	public void clearFades()
	{
		nextFade = null;
		thisFade = null;
		
		nextSong = -1;
	}
	
	public void onUpdate(float delta)
	{
		if (thisState != MusicStates.playing)
			return;
		
		if (thisFade != null && thisState == MusicStates.playing)
		{
			thisFade.onUpdate(delta);
			
			if (thisFade.getValid())
			{
				float volume = getCorrectedVolume(thisFade.getVolume());
				
				setVolume(volume);
			}
			else
			{
				if (nextFade != null)
				{
					thisFade = nextFade;
					nextFade = null;
					ChangeSongs(nextSong);
					play(0);
					nextSong = -1;
				}
				else if (thisFade.getEndVolume() <= 0.01)
				{
					stop();
					thisFade = null;
					nextSong = -1;
					nextFade = null;
				}
			}
		}
	}
	
	public void addFade(SoundFade fade)
	{
		clearFades();
		thisFade = fade;
	}
	
	public MusicManager(Context context, int value)
	{
		mContext = context;
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		
		mp = new MediaPlayer();
		
		setVolume(0);
		
		mp.setOnPreparedListener(new OnPreparedListener()
		{
			
			@Override
			public void onPrepared(MediaPlayer mp)
			{
				loaded = true;
			}
			
		});
		
		ChangeSongs(value);
		
		loaded = true;
	}
	
	public void play(float volume)
	{
		setVolume(volume);
		play();
	}
	
	public void play()
	{
		mp.start();
		thisState = MusicStates.playing;
	}
	
	public void stop()
	{
		mp.stop();
		thisState = MusicStates.stopped;
	}
	
	public void pause()
	{
		mp.pause();
		thisState = MusicStates.paused;
	}
	
	public void setLooping(boolean looping)
	{
		mp.setLooping(looping);
	}
	
	public void ChangeSongs(int value)
	{
		loaded = false;
		
		mp.reset();
		
		if (value != -1)
		{
			mp = MediaPlayer.create(mContext, value);
		}

		thisState = MusicStates.playing;//.stopped;
		
		currentSong = value;
		
		mp.setLooping(true);
	}
	
	public void ChangeSongs(int value, SoundFade fade)
	{
		ChangeSongs(value, fade, fade);
	}
	
	public void ChangeSongs(int value, SoundFade fadeOut, SoundFade fadeIn)
	{
		if (thisState == MusicStates.stopped)
			fadeOut = null;
		
		if (fadeOut != null)
		{
			if (fadeIn != null)
			{
				thisFade = fadeOut;
				nextFade = fadeIn;
				nextSong = value;
			}
			else
			{
				thisFade = fadeOut;
				nextFade = null;
				nextSong = value;
			}
		}
		else
		{
			if (fadeIn != null)
			{
				ChangeSongs(value);
				thisFade = fadeIn;
				nextSong = -1;
				nextFade = null;
			}
			else
			{
				ChangeSongs(value);
				thisFade = null;
				nextSong = -1;
				nextFade = null;
			}
		}
	}
	
	private float getCorrectedVolume(float volume)
	{
		volume = Math.max(Math.min(volume, .75f), .0001f);
		
		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * volume;
		
		return streamVolume;
	}
	
	public void setVolume(float volume)
	{
		volume = getCorrectedVolume(volume);
		
		mp.setVolume(volume, volume);
	}
	
	public void release()
	{
		mp.release();
	}
	
	public boolean isLoaded()
	{
		return loaded;
	}
	
	public int getCurrentSong()
	{
		return currentSong;
	}
}

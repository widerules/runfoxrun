package com.kobaj.runfoxrun;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager
{
	private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundPoolMap;
	private AudioManager mAudioManager;
	private Context mContext;
	
	public SoundManager(Context theContext)
	{
		mContext = theContext;
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public void addSound(int index, int SoundID)
	{
		mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
	}
	
	public void removeSound(int index)
	{
		mSoundPool.unload((Integer) mSoundPoolMap.get(index));
		mSoundPoolMap.remove(index);	
	}
	
	public void playSound(int index, float volume, int loop)
	{
		//clamp
		volume = Math.max(Math.min(volume, 1.0f), 0.0f);
		
		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * volume;
		mSoundPool.play((Integer) mSoundPoolMap.get(index), streamVolume, streamVolume, 1, loop, 1f);	
	}
	
	public void playSound(int index, float volume)
	{
		playSound(index, volume, 0);
	}
	
	public void playSound(int index)
	{
		playSound(index, 1.0f, 0);
	}
	
	public void stopSound(int index)
	{
		mSoundPool.stop((Integer) mSoundPoolMap.get(index));
	}
	
	public void pauseAll()
	{
		mSoundPool.autoPause();
	}
	
	public void resumeAll()
	{
		mSoundPool.autoResume();
	}
	
}

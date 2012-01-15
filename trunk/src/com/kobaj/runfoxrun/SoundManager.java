package com.kobaj.runfoxrun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundManager
{
	private SoundPool mSoundPool;
	private HashMap<Integer, SoundObject> mSoundPoolMap;
	private AudioManager mAudioManager;
	private Context mContext;
	
	private ArrayList<SoundFade> fadeList;
	
	public boolean isAllLoaded()
	{
		//poor way of doing this
		for(Iterator<Entry<Integer, SoundObject>> it = mSoundPoolMap.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry<Integer, SoundObject> pairs = (Map.Entry<Integer, SoundObject>)it.next();
			
			if(pairs.getValue().getLoad() == LoadStates.loading)
				return false;
		}
		
		return true;
	}
	
	public LoadStates isLoaded(int index)
	{
		if(mSoundPoolMap.containsKey(index))
		{
			return mSoundPoolMap.get(index).getLoad();
		}
		else
			return LoadStates.notStarted;
	}

	public SoundManager(Context theContext)
	{
		fadeList = new ArrayList<SoundFade>();
		
		mContext = theContext;
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new HashMap<Integer, SoundObject>();
		mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
		
		mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener(){

			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
			{
				//poor way of doing this
				for(Iterator<Entry<Integer, SoundObject>> it = mSoundPoolMap.entrySet().iterator(); it.hasNext();)
				{
					Map.Entry<Integer, SoundObject> pairs = (Map.Entry<Integer, SoundObject>)it.next();
			        if(pairs.getValue().getSoundPoolint() == sampleId)
			        {
			        	pairs.getValue().setLoad(LoadStates.complete);
			        }
				}
			}	
		});
	}
	
	public void onUpdate(float delta)
	{
		for(Iterator<SoundFade> it = fadeList.iterator(); it.hasNext();)
		{
			SoundFade temp = it.next();
			
			temp.onUpdate(delta);
			
			if(!temp.getValid())
			{
				if(temp.getEndVolume() == 0)
				{
					stopSound(temp.getIndex());
				}
				
				it.remove();	
			}
			else
			{
				float volume = getCorrectedVolume(temp.getVolume());
			
				if(mSoundPoolMap.containsKey(temp.getIndex()))
					mSoundPool.setVolume(mSoundPoolMap.get(temp.getIndex()).getSoundPoolint(), volume, volume);
			}
		}
	}
	
	public void addFade(SoundFade newFade)
	{
		fadeList.add(newFade);
	}

	public void addSound(int index, int SoundID)
	{
		mSoundPoolMap.put(index, new SoundObject(index, mSoundPool.load(mContext, SoundID, 1)));
	}
	
	
	public void removeSound(int index)
	{
		mSoundPool.unload((Integer) mSoundPoolMap.get(index).getSoundPoolint());
		mSoundPoolMap.remove(index);
	}
	
	private float getCorrectedVolume(float volume)
	{
		volume = Math.max(Math.min(volume, .99f), 0.00001f);
		
		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * volume;
		
		return streamVolume;
	}
	
	public void playSound(int index, float volume, int loop)
	{
		if( mSoundPoolMap.containsKey(index))
		{
			float streamVolume = getCorrectedVolume(volume);
			
			mSoundPool.play((Integer) mSoundPoolMap.get(index).getSoundPoolint(), streamVolume, streamVolume, 1, loop, 1f);	
		}
	}
	
	public void purge()
	{
		for(int i = 0; i < mSoundPoolMap.size(); i++)
			removeSound(i);
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
		if( mSoundPoolMap.containsKey(index))
			mSoundPool.stop((Integer) mSoundPoolMap.get(index).getSoundPoolint());
	}
	
	public void pauseAll()
	{
		mSoundPool.autoPause();
	}
	
	public void resumeAll()
	{
		mSoundPool.autoResume();
	}
	
	public void release()
	{
		mSoundPool.release();
	}
	
}

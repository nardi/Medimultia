package nl.uva.multimedia.audio;

/* 
 * Framework for audio playing, visualization and filtering
 *
 * For the Multimedia course in the BSc Computer Science 
 * at the University of Amsterdam 
 *
 * I.M.J. Kamps, S.J.R. van Schaik, R. de Vries (2013)
 */

/* XXX DO NOT change this code unless you know what you are doing XXX */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.Switch;

public class AudioSourceSwitch extends Switch implements Switch.OnCheckedChangeListener {

	PlaybackManager playbackManager;

	
	public AudioSourceSwitch(Context context) {
		super(context);
		setup();
	}

	public AudioSourceSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup();
	}

	public AudioSourceSwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup();
	}
	
	private void setup() {
		this.setOnCheckedChangeListener(this);
		this.setText("Source");
		this.setTextOff("Mic");
		this.setTextOn("File");
		
	}


	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		if(arg1 == PlaybackManager.SOURCE_MIC) {
			playbackManager.setAudioSource(arg1);
		}
		else if(arg1 == PlaybackManager.SOURCE_FILE) {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		    intent.setType("audio/x-wav");
		    Intent chooser = Intent.createChooser(intent, "Select soundfile");
		    Activity activity = (Activity) playbackManager.getContext();
		    activity.startActivityForResult(chooser,1);
		}
	}

	public void setPlaybackManager(PlaybackManager playbackManager) {
		this.playbackManager = playbackManager;
	}

}

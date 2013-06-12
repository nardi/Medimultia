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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class PauseButton extends Button implements View.OnClickListener {

	PlaybackManager playbackManager;
	
	
	public PauseButton (Context context) {
		super(context);
		setup();
	}
	
	public PauseButton (Context context, AttributeSet attrs) {
		super(context,attrs);
		setup();
	}

	public PauseButton (Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup();
	}

	private void setup() {
		this.setText("Pause");
		setOnClickListener(this);
	}
	

	public void onClick(View v) {
		playbackManager.pausePlaying();
	}

	public void setPlaybackManager(PlaybackManager playbackManager) {
		this.playbackManager = playbackManager;		
	}
	
}

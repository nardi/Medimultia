package nl.uva.multimedia.audio;

/* 
 * Framework for audio playing, visualization and filtering
 *
 * For the Multimedia course in the BSc Computer Science 
 * at the University of Amsterdam 
 *
 * I.M.J. Kamps, S.J.R. van Schaik, R. de Vries (2013)
 */

/* XXX Yes, you should change stuff here */

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.Switch;

public class PlaybackManager {

	public static final int STATE_STOPPED = 1;
	public static final int STATE_PLAYING = 2;
	public static final boolean SOURCE_MIC = false;
	public static final boolean SOURCE_FILE = true;

	boolean audioSource = SOURCE_MIC;

	private AudioPlayer audioPlayer;

	private PlayButton playButton;
	private PauseButton pauseButton;
	private StopButton stopButton;
	private AudioSourceSwitch audioSourceSwitch;

	private Context context;
	private String wavePath;

	/*
	 * Tiny message listener so that the audioplayer can change the state from
	 * another thread.
	 */
	public Handler _handler = new Handler() {
		public void handleMessage(Message msg) {
			setVisiblity(msg.what);
			super.handleMessage(msg);
		}

	};

	public PlaybackManager(Context context) {
		this.context = context;
	}

	public void setPauseButton(PauseButton pauseButton) {
		this.pauseButton = pauseButton;
		pauseButton.setPlaybackManager(this);

	}

	public void setPlayButton(PlayButton playButton) {
		this.playButton = playButton;
		playButton.setPlaybackManager(this);
	}

	public void setStopButton(StopButton stopButton) {
		this.stopButton = stopButton;
		stopButton.setPlaybackManager(this);
	}

	public void setAudioSourceSwitch(AudioSourceSwitch audioSourceSwitch) {
		this.audioSourceSwitch = audioSourceSwitch;
		audioSourceSwitch.setPlaybackManager(this);
	}

	public void setVisiblity(int state) {
		if (state == STATE_STOPPED) {
			audioSourceSwitch.setVisibility(Switch.VISIBLE);
			playButton.setVisibility(Button.VISIBLE);
			pauseButton.setVisibility(Button.INVISIBLE);
			stopButton.setVisibility(Button.INVISIBLE);
		} else if (state == STATE_PLAYING) {
			audioSourceSwitch.setVisibility(Switch.INVISIBLE);
			playButton.setVisibility(Button.INVISIBLE);
			pauseButton.setVisibility(Button.VISIBLE);
			stopButton.setVisibility(Button.VISIBLE);
		}
	}

	public void stopPlaying() {
		if (audioPlayer != null)
			audioPlayer.stopPlaying();
		this.setVisiblity(STATE_STOPPED);
	}

	public void startPlaying() {
		if (audioSource == SOURCE_MIC)
			audioPlayer = new MicPlayer();
		else
			audioPlayer = new WavePlayer(wavePath, this);

		audioPlayer.start();
		setVisiblity(STATE_PLAYING);

	}

	public void pausePlaying() {
		audioPlayer.togglePause();
	}

	public void setAudioSource(boolean audioSource) {
		this.audioSource = audioSource;

		if (audioSource == SOURCE_MIC)
			audioSourceSwitch.setChecked(SOURCE_MIC);

	}

	public Context getContext() {
		return context;
	}

	public void setFileSource(String path) {
		this.audioSource = SOURCE_FILE;
		this.wavePath = path;
	}

}

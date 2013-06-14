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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Switch;

public class MainActivity extends Activity {

	Switch switch1, switch2;
	EchoDelaySlider delaySlider;
	EchoFeedbackSlider feedbackSlider;
	SurfaceView surfaceView1;
	CanvasView canvas;

	PlaybackManager playbackmanager;

	boolean useMic = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.main);
		
		playbackmanager = new PlaybackManager(this);
		playbackmanager
				.setPauseButton((PauseButton) findViewById(R.id.pausebutton));
		playbackmanager
				.setPlayButton((PlayButton) findViewById(R.id.playbutton));
		playbackmanager
				.setStopButton((StopButton) findViewById(R.id.stopbutton));
		playbackmanager
				.setAudioSourceSwitch((AudioSourceSwitch) findViewById(R.id.audioSourceSwitch));
		playbackmanager.setVisiblity(PlaybackManager.STATE_STOPPED);

		delaySlider = (EchoDelaySlider) findViewById(R.id.EchoDelaySlider);
		playbackmanager.setEchoDelaySlider(delaySlider);
		feedbackSlider = (EchoFeedbackSlider) findViewById(R.id.EchoFeedbackSlider);
		playbackmanager.setEchoFeedbackSlider(feedbackSlider);
		
		canvas = ((CanvasView) findViewById(R.id.canvasView));
		delaySlider.setCanvas(canvas);
		feedbackSlider.setCanvas(canvas);
		
		delaySlider.setProgress(50);
		feedbackSlider.setProgress(50);
	}

	/* Function to handle the incoming intent (music-file chooser)*/
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			String path;
			if ("content".equalsIgnoreCase(uri.getScheme())) {
				/*
				 * Source:
				 * http://www.androidsnippets.com/get-file-path-of-gallery-image
				 */
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(uri, proj, // Which columns to
														// return
						null, // WHERE clause; which rows to return (all rows)
						null, // WHERE clause selection arguments (none)
						null); // Order-by clause (ascending by name)
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();

				path = cursor.getString(column_index);
			} else if ("file".equalsIgnoreCase(uri.getScheme())) {
				path = uri.getPath();
			} else {
				Log.e("MainActivity", "Something went wrong..");
				playbackmanager.setAudioSource(PlaybackManager.SOURCE_MIC);
				return;
			}

			/* Check that the file is a WAVE-file and not an mp3-file */
			if (!WaveFile.isWaveFile(path)) {
				AlertDialog alertDialog = new AlertDialog.Builder(this)
						.create();
				alertDialog.setTitle("Invalid file-type");
				alertDialog.setMessage("The selected file is not a valid wave file.");
				alertDialog.show();
				playbackmanager.setAudioSource(PlaybackManager.SOURCE_MIC);
			} else {
				playbackmanager.setFileSource(path);
				canvas.setFile(path);
				canvas.invalidate();
			}

		}
		else {
			playbackmanager.setAudioSource(PlaybackManager.SOURCE_MIC);
			return;
		}
	}

	/* Stop playing when the application is in the background. */
	public void onStop() {
		super.onStop();
		playbackmanager.stopPlaying();

	}
}

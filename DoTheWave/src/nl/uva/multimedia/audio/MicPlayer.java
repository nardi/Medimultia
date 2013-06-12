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

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

public class MicPlayer extends Thread implements AudioPlayer {

	private AudioTrack audioPlayer;
	private AudioRecord recorder;
	private EchoFilter echofilter;
	private int bufferSize;
	private int bufferSizeInShorts;

	private boolean playing = true;
	private boolean pause = false;

	public MicPlayer() {
		echofilter = new EchoFilter();
		
		bufferSize = AudioRecord.getMinBufferSize(44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);

		/* Because the 16 bits fit in one short.. */
		bufferSizeInShorts = bufferSize / 2;

		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, this.bufferSize);

		audioPlayer = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, this.bufferSize,
				AudioTrack.MODE_STREAM);

	}

	public void run() {

		if (audioPlayer.getPlaybackRate() != AudioTrack.PLAYSTATE_PLAYING)
			audioPlayer.play();

		if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING)
			recorder.startRecording();

		short[] buffer = new short[bufferSize];

		while (playing) {

			/* Only continue when not paused. */
			if (pause)
				while (pause)
					;

			/* Fill the buffer */
			recorder.read(buffer, 0, bufferSizeInShorts);

			/* Now we can manipulate the buffer and create an echo. */
			echofilter.filter(buffer, bufferSizeInShorts);

			audioPlayer.write(buffer, 0, bufferSizeInShorts);

		}

		audioPlayer.pause();
		audioPlayer.flush();
		audioPlayer.release();

		recorder.stop();

	}

	public void stopPlaying() {
		playing = false;
	}

	public void togglePause() {
		pause = !pause;

		if (audioPlayer.getState() == AudioTrack.PLAYSTATE_PLAYING) {
			audioPlayer.pause();
			recorder.stop();
		}

		if (audioPlayer.getState() == AudioTrack.PLAYSTATE_PAUSED) {
			audioPlayer.play();
			recorder.startRecording();
		}
	}

	@Override
	public EchoFilter getEchoFilter() {
		return this.echofilter;
	}


}

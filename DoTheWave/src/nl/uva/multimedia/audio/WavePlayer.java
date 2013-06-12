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
import android.media.AudioTrack;
import android.os.Message;

public class WavePlayer extends Thread implements AudioPlayer {

	private WaveFile file;
	private AudioTrack audioPlayer;

	private boolean playing = true;
	private boolean pause = false;

	private int bufferSizeInBytes;
	private int bufferSizeInShorts;
	
	private PlaybackManager manager;

	public WavePlayer(String path, PlaybackManager manager) {
		this.file = new WaveFile(path);
		this.manager = manager;

		this.bufferSizeInBytes = AudioTrack.getMinBufferSize(file.getSampleRate(),
				file.getChannelConfig(), file.getAudioFormat());

		/* When 16-bit encoded, the shortBuffer has half the length of the bytebuffer..*/
		if(file.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT)
			this.bufferSizeInShorts = this.bufferSizeInBytes / 2;
		else
			this.bufferSizeInShorts = this.bufferSizeInBytes;

		audioPlayer = new AudioTrack(AudioManager.STREAM_MUSIC,
				file.getSampleRate(), file.getChannelConfig(),
				file.getAudioFormat(), this.bufferSizeInBytes,
				AudioTrack.MODE_STREAM);

	}

	public void run() {

		if (audioPlayer.getPlaybackRate() != AudioTrack.PLAYSTATE_PLAYING)
			audioPlayer.play();

		short[] buffer = new short[bufferSizeInBytes];

		EchoFilter echofilter = new EchoFilter();

		while (playing) {

			/* Only continue when not paused. */
			if (pause) {
				while (pause)
					;
			}

			/* Fill the buffer, break when done */
			if (!file.getData(buffer, bufferSizeInBytes))
				break;

			/* Now we can manipulate the buffer and create an echo. */
			echofilter.filter(buffer, bufferSizeInShorts);

			audioPlayer.write(buffer, 0, bufferSizeInShorts);

		}

		audioPlayer.pause();
		audioPlayer.flush();
		audioPlayer.release();

		/* Send message to PlaybackManager that we are done playing */
		Message msg = Message.obtain();
		msg.what = PlaybackManager.STATE_STOPPED;
		manager._handler.sendMessage(msg);
		
	}

	public void stopPlaying() {
		playing = false;
	}

	public void togglePause() {
		pause = !pause;

		if (audioPlayer.getState() == AudioTrack.PLAYSTATE_PLAYING)
			audioPlayer.pause();

		if (audioPlayer.getState() == AudioTrack.PLAYSTATE_PAUSED)
			audioPlayer.play();
	}

}

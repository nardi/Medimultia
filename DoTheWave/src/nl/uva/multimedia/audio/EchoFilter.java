package nl.uva.multimedia.audio;

/* 
 * Framework for audio playing, visualization and filtering
 *
 * For the Multimedia course in the BSc Computer Science 
 * at the University of Amsterdam 
 *
 * I.M.J. Kamps, S.J.R. van Schaik, R. de Vries (2013)
 */

public class EchoFilter {
	public static final int MAX_DELAY = 2;
	
	private int delayLength = 44100;
	private double feedback = 0.5;
	private CircularBuffer buffer;
	private int position;
	private short[] echo;
	
	public WaveFile waveFile;
	
	public void filter(short[] samples, int length)
	{
		if (echo == null || length != echo.length) {
			echo = new short[length];
			buffer = new CircularBuffer(waveFile.getSampleRate() * MAX_DELAY + length);
		}

		buffer.getFrom((position + delayLength) % buffer.getLength(), echo, length);
		for (int i = 0; i < length; i++)
			samples[i] += (short)(echo[i] * feedback);
		
		position = buffer.placeFrom(position, samples, length);
	}
	
	public void setDelay(double seconds) {
		if (seconds > MAX_DELAY) seconds = MAX_DELAY;
		delayLength = (int)(waveFile.getSampleRate() * seconds);
	}
	
	public void setFeedback(double factor) {
		feedback = factor;		
	}
}

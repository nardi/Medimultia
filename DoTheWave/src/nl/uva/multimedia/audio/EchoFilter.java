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

public class EchoFilter {
	int delayLength = 44100;
	double feedback = 0.4;
	CircularBuffer buffer = new CircularBuffer(delayLength);
	int inPos, outPos, samplesPassed;
	short[] echo;
	
	public void filter(short[] samples, int length)
	{
		if (echo == null || length != echo.length) {
			echo = new short[length];
		}

		samplesPassed += length;
		
		if (samplesPassed >= delayLength) {
			outPos = buffer.getFrom(outPos, echo, length);
			for (int i = 0; i < length; i++)
				samples[i] += (short)(echo[i] * feedback);
		}
		
		inPos = buffer.placeFrom(inPos, samples, length);
	}
}

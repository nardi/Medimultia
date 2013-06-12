package nl.uva.multimedia.audio;

/* 
 * Framework for audio playing, visualization and filtering
 *
 * For the Multimedia course in the BSc Computer Science 
 * at the University of Amsterdam 
 *
 * I.M.J. Kamps, S.J.R. van Schaik, R. de Vries (2013)
 */

/* XXX Yes, you should change stuff here (if you want to add visualization..) */

public interface AudioPlayer{

	public void start();
	public void run();
	public void stopPlaying();
	public void togglePause();
	public EchoFilter getEchoFilter();
}


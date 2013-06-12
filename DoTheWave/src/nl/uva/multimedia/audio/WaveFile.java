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

import java.io.*;

import android.R.integer;
import android.media.AudioFormat;
import android.util.Log;

public class WaveFile {

	FileInputStream file;
	
	byte[] byteBuffer;
	byte[] tempParamStorage;
	boolean isWave;
	int sampleRate;

	/* XXX Put here your variables. */


	public WaveFile(String path) {

		try {
			file = new FileInputStream(path);
			/* Please use the FileInputStream and not the Buffered variant of it..
			 * See the JAVA docs how to use it.. */
			tempParamStorage = new byte[4];
			
			try{
				file.skip(24);
				file.read(tempParamStorage, 0, 4);
			}catch(Exception IO){
				Log.e("SampleRate", "IOException while trying to read SampleRate");
			}
			
			sampleRate = this.bytesToInt(tempParamStorage, 0, 4);
			/* XXX Here you can read in the header and set some variables.. */
			

		} catch (Exception e) {
			Log.e("WaveFile", "Something went wrong..");
			return;
		}
	}


	public static boolean isWaveFile(String path) {
		/* XXX Just do a quick check on the first bytes of the file to determine
		 * whether the file is a wave file or not.. */
		
		try{
			FileReader isWave = new FileReader(path);
			char[] wave = new char[12];
			isWave.read(wave,0,12);
			String riffCheck = new String(wave,0,4);
			String waveCheck = new String(wave,8,4);
			if(riffCheck.equals("RIFF") && waveCheck.equals("WAVE")){
				Log.v("WAVECHECK", "This is a .wav file!");
				return true;
			}
			
		}catch(Exception e){
			Log.e("FNF", "Could not find file");
		}
		
		
		
		
		
		/* XXX Extra: determine if a valid wave-file can be played with Android
		 * (surround-sound, etc..)
		 */
		Log.v("WAVECHECK", "This is NOT a .wav file");
		return false;
	}


	public boolean getData(short[] shortBuffer, int bufferSizeInBytes) {
		int bytesRead;
		/* Re-use the bytebuffer for efficient memory usage.. */
		if (byteBuffer == null)
			byteBuffer = new byte[bufferSizeInBytes];
		
		Log.v("getData","bufferSizeInBytes: " + bufferSizeInBytes);
		/*
		 * XXX Read the file into the byteBuffer, and put the bytes into the
		 * given shortBuffer.. Please think about the 8 or 16-bit encoding of
		 * a wave file.. XXX
		 */
		/*
		 * Making sure the InputStream will start reading the actual data instead
		 * of metadata. Data starts at offset 44;
		 */
		try{
			
			bytesRead = file.read(byteBuffer);
			
			if(bytesRead <= 0){
				return false;
			}
			
			for (int i = 0, q = 0; q < bufferSizeInBytes; i++, q += 2){
				shortBuffer[i] = bytesToShort(byteBuffer, q);
			}
			
		}catch(Exception IO){
			Log.e("getDatra","IO exception while trying to read data chunk from wave file", IO);
			return false;
		}
		
		/* 
		 * Return false when done reading or when an error occurs.
		 */
		
		return true;

	}

	/*
	 * Return the sampleRate in Hz of the wave-file
	 */
	public int getSampleRate() {
		return this.sampleRate;
	}

	/*
	 * Return AudioFormat.ENCODING_PCM_16BIT OR AudioFormat.ENCODING_PCM_8BIT
	 * depending on the file..
	 */
	public int getAudioFormat() {
		return AudioFormat.ENCODING_PCM_16BIT;
	}

	/*
	 * Return AudioFormat.CHANNEL_OUT_STEREO or AudioFormat.CHANNEL_OUT_MONO
	 * depending on the file..
	 */
	public int getChannelConfig() {
		return AudioFormat.CHANNEL_OUT_STEREO;
	}
	
	
	/* XXX Please use the functions below instead of slow ByteBuffer objects.. */

	/* Convert bytes to an integer. Little endian. */
	private static int bytesToInt(byte[] bytes, int offset, int length) {
		int result = 0;
		for (int i = 0; i < length; i++)
			result += (bytes[i + offset] & 0xFF) << i * 8;
		return result;
	}

	/* Convert bytes to an string. Big endian */
	private static String bytesToString(byte[] bytes, int offset, int length) {
		char[] string = new char[length];

		for (int i = 0; i < length; i++)
			string[i] += (char) bytes[i + offset];

		return new String(string);

	}

	/* Bytes to short, always uses 2 bytes */
	private static short bytesToShort(byte[] bytes, int offset) {
		return (short) (((bytes[offset + 1] & 0xFF) << 8) + (bytes[offset] & 0xFF));
	}
	
	public void skipToData(){
		try{
			file.skip(16);
		}catch(Exception e){
			Log.e("Playing", "Unexpected IO error while skipping to data chunk");
		}
	}
}

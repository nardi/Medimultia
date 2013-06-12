package nl.uva.multimedia.audio;

import android.util.Log;

public class CircularBuffer {
	
	private short[] buffer;
	
	public CircularBuffer(int size) {
		buffer = new short[size];
	}

	public int getFrom(int position, short[] data, int length) {
		int bufferLeft = buffer.length - position;
		if (bufferLeft >= length) {
			System.arraycopy(buffer, position, data, 0, length);
		} else {
			System.arraycopy(buffer, position, data, 0, bufferLeft);
			System.arraycopy(buffer, 0, data, bufferLeft, length - bufferLeft);
		}
		
		return (position + length) % buffer.length;
	}

	public int placeFrom(int position, short[] data, int length) {
		int bufferLeft = buffer.length - position;
		if (bufferLeft >= length) {
			System.arraycopy(data, 0, buffer, position, length);
		} else {
			System.arraycopy(data, 0, buffer, position, bufferLeft);
			System.arraycopy(data, bufferLeft, buffer, 0, length - bufferLeft);
		}
		
		Log.d("CircularBuffer", "Wrote from " + position + " to " + (position + length) % buffer.length);
		
		return (position + length) % buffer.length;
	}
}

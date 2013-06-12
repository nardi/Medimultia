package nl.uva.multimedia.audio;

public class CircularBuffer {
	private short[] buffer;
	
	public CircularBuffer(int size) {
		buffer = new short[size];
	}

	public int getFrom(short[] data, int position) {
		int bufferLeft = buffer.length - position;
		if (bufferLeft >= data.length) {
			System.arraycopy(buffer, position, data, 0, data.length);
		} else {
			System.arraycopy(buffer, position, data, 0, bufferLeft);
			System.arraycopy(buffer, 0, data, bufferLeft, data.length - bufferLeft);
		}
		
		return (position + data.length) % buffer.length;
	}

	public int placeFrom(short[] data, int position) {
		int bufferLeft = buffer.length - position;
		if (bufferLeft >= data.length) {
			System.arraycopy(data, 0, buffer, position, data.length);
		} else {
			System.arraycopy(data, 0, buffer, position, bufferLeft);
			System.arraycopy(data, bufferLeft, buffer, 0, data.length - bufferLeft);
		}
		
		return (position + data.length) % buffer.length;
	}
}

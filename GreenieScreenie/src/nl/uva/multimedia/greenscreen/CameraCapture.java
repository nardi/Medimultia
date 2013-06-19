package nl.uva.multimedia.greenscreen;
/* 
 * Framework for camera processing and visualisation
 *
 * For the Multimedia course in the BSc Computer Science 
 * at the University of Amsterdam 
 *
 * I.M.J. Kamps, S.J.R. van Schaik, R. de Vries (2013)
 */

/* XXX Yes, you should change stuff here */


/* Handles the camera data 
 *
 * The size is based on the size of the onscreen preview and calculated in
 * CameraView. This means that smaller screen devices, will, yes, have less
 * to calculate on.
 */
import android.graphics.Color;
import android.hardware.Camera;
import nl.uva.multimedia.greenscreen.CameraData;
import nl.uva.multimedia.greenscreen.CameraView;

class CameraCapture implements CameraView.PreviewCallback {
	protected CanvasView m_canvas_view = null;
	
	int[] argb = null;
	int[] yuvComponents = null;
	float[] hsv = null;
	float[]	hsvTemp = new float[3];

	/* Is called by Android when a frame is ready */
	public void onPreviewFrame(byte[] data, Camera camera, boolean rotated) {
		Camera.Parameters parameters = camera.getParameters();
		Camera.Size       size       = parameters.getPreviewSize();

		/* Rotated is true if the height and width parameters should be swapped
		 * due to the image having been rotated internally. (Turns out it is
		 * rather tricky to patch a method in Java....) 
		 */
		if (rotated) {
			int holder;
			holder = size.height;
			size.height = size.width;
			size.width = holder;
		}

		//Log.i("CameraCapture", "Width: " + size.width + " Height: " + size.height);
		
		int arraySize = size.width * size.height;
		if (argb == null || arraySize != argb.length) {
			argb = new int[arraySize];
			yuvComponents = new int[arraySize];
			hsv = new float[2 * arraySize];
			m_canvas_view.invalidate();
		}

		/* Use the appropriate YUV conversion routine to retrieve the
		 * data we actually intend to process.
		 * 
		 * yuvComponents is filled with the U and V components alternating
		 */
		CameraData.convertYUV420SPtoARGB(argb, yuvComponents, data, size.width, size.height);

		/* Work on the argb array */

		for(int i = 0, h = 0, s = 1; s < argb.length; i++, h += 2, s += 2) {
			Color.colorToHSV(argb[i], hsvTemp);
			hsv[h] = hsvTemp[0];
			hsv[s] = hsvTemp[1];
		}
		
		/* Transfer data/results to the canvas */
		
		m_canvas_view.argb = argb;
		m_canvas_view.hsv = hsv;
		m_canvas_view.yuvComponents = yuvComponents;
		m_canvas_view.image_height = size.height;
		m_canvas_view.image_width = size.width;
	}
	
	/*
	 * Getters and setters
	 */
	public CanvasView getCanvasView() {
		return m_canvas_view;
	}
	
	public void setCanvasView(CanvasView canvas_view) {
		m_canvas_view = canvas_view;
	}
}


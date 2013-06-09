package nl.uva.multimedia.TurntableCamera;
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
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;

import nl.uva.multimedia.TurntableCamera.CameraData;
import nl.uva.multimedia.TurntableCamera.CameraView;

class CameraCapture implements CameraView.PreviewCallback {
	protected CanvasView m_canvas_view = null;

	public int curHeight;
	public int curWidth;
	public int argb[];
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

		if(curHeight != size.height || curWidth != size.width){
			Log.e("CameraCapture", "Width: " + size.width + " Height: " + size.height);
		
			curHeight = size.height;
			curWidth = size.width;
			argb = new int[size.width*size.height];
		}
	
		/* Use the appropriate YUV conversion routine to retrieve the
		 * data we actually intend to process.
		 */
		CameraData.convertYUV420SPtoARGB(argb, data, size.width, size.height);
		//m_canvas_view.setSelectedImage(Bitmap.createBitmap(argb,0,size.width,size.width, size.height, Bitmap.Config.RGB_565));
		m_canvas_view.image_height = size.height;
		m_canvas_view.image_width = size.width;
		m_canvas_view.argb = argb;
		/* Work on the argb array */

		/* Transfer data/results to the canvas */
		 
		/* Invalidate the canvas, forcing it to be redrawn with the new data.
		 * You can do this in other places, evaluate what makes sense to you.
		 */
		m_canvas_view.invalidate();
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


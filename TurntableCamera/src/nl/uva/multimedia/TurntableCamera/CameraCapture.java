package nl.uva.multimedia.TurntableCamera;
/* 
 * Framework for camera processing and visualisation
 *
 * For the Multimedia course in the BSc Computer Science 
 * at the University of Amsterdam 
 *
 * I.M.J. Kamps, S.J.R. van Schaik, R. de Vries (2013)
 */

import android.hardware.Camera;
import android.util.Log;

import nl.uva.multimedia.TurntableCamera.CameraData;
import nl.uva.multimedia.TurntableCamera.CameraView;

class CameraCapture implements CameraView.PreviewCallback {
	protected CanvasView m_canvas_view = null;

	public int rawAngle;
	private int image[] = null;
	private int rotatedImage[] = null;
	private InterpolationMode intMode;

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

		int arraySize = size.width * size.height;
		if (image == null || image.length != arraySize) {
			Log.i("CameraCapture", "Width: " + size.width + " Height: " + size.height);

			image = new int[arraySize];
			rotatedImage = new int[arraySize];
		}

		/* Use the appropriate YUV conversion routine to retrieve the
		 * data we actually intend to process.
		 */
		CameraData.convertYUV420SPtoARGB(image, data, size.width, size.height);

		/* Work on the argb array */
		Rotator.rotate(rotatedImage, image, size.width, size.height,
				(float)Math.toRadians(rawAngle), intMode);

		/* Transfer data/results to the canvas */
		m_canvas_view.setImage(rotatedImage, size.width, size.height);

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

	public void setInterpolationMode(InterpolationMode intMode) {
		this.intMode = intMode;
	}
}


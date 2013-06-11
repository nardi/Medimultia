package com.example.gametest;

import android.app.Fragment;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

/*
 * Veel hulp van http://www.mysecretroom.com/www/programming-and-software/android-game-loops
 */

public abstract class GameFragment extends Fragment implements SurfaceHolder.Callback {
	private final static int MAX_FRAME_SKIPS = 5;
	
	private GameThread thread = new GameThread();
	private boolean running = false;
	private SurfaceHolder surfaceHolder = null;
	private boolean surfaceCreated = false;
	private boolean paused = false;

	private int targetFps;
	private float updatePeriod;
	public boolean showStats = false;
	
	private Paint statsPaint = new Paint();
	private long beginTime, timeDiff, sleepTime, updateTime, updateCount, drawCount;
	
	{
		setTargetFps(60);
		
		statsPaint.setColor(Color.WHITE);
		statsPaint.setShadowLayer(3, 1, 1, Color.BLACK);
		statsPaint.setTextSize(30);
	}
	
	public void setTargetFps(int fps) {
		targetFps = fps;
		updatePeriod = 1000f / targetFps;
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		SurfaceView surface = new SurfaceView(this.getActivity());
		surface.getHolder().addCallback(this);
		return surface;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		surfaceHolder = holder;
		surfaceCreated = true;
		
		if (paused) {
			paused = false;
			this.run();
		}
	}

	public synchronized void run() {
		if (!running) {
			running = true;
			if (thread.getState() != Thread.State.NEW)
				thread = new GameThread();
			thread.start();
		}
	}
	
	public synchronized void halt() {
		if (running) {
			running = false;
			while (true) {
				try {
					thread.join();
					return;
				} catch (InterruptedException e) { }
			}
		}
	}
	
	public abstract void onUpdate();

	public abstract void onDraw(Canvas canvas);
	
	private class GameThread extends Thread {		
		private void update() {
			onUpdate();
		}
		
		private void drawStats(Canvas canvas) {
			canvas.drawText("Last update time: " + updateTime, 5, canvas.getHeight() - 75, statsPaint);
			canvas.drawText("Update count: " + updateCount, 5, canvas.getHeight() - 40, statsPaint);
			canvas.drawText("Skipped frames: " + (updateCount - drawCount), 5, canvas.getHeight() - 5, statsPaint);
		}
		
		private void draw(Canvas canvas) {
			onDraw(canvas);
			if (showStats)
				drawStats(canvas);
		}
		
		@Override
		public void run() {
			int framesSkipped;
			while (running) {
				framesSkipped = 0;
				beginTime = SystemClock.uptimeMillis();
				update();
				
				if (surfaceCreated) {
					Canvas canvas = null;
					try {
						canvas = surfaceHolder.lockCanvas();
						synchronized (surfaceHolder) {
							if (canvas != null) {
								draw(canvas);
							}
						}
					} finally {
						if (canvas != null)
							surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}

				timeDiff = SystemClock.uptimeMillis() - beginTime;
				sleepTime = (long)(updatePeriod - timeDiff);
				
				if (sleepTime > 0)
					SystemClock.sleep(sleepTime);
				
				while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
					update();
					sleepTime += updatePeriod;
					framesSkipped++;
				}
				
				updateTime = (SystemClock.uptimeMillis() - beginTime) / (framesSkipped + 1);
				updateCount += framesSkipped + 1;
				drawCount++;
			}
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		surfaceCreated = false;
		this.halt();
		surfaceHolder = null;
    }
	
	@Override
	public void onPause() {
		super.onPause();
		
		paused = true;
	}
}

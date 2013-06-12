package com.example.gametest;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;

public class TestGameFragment extends GameFragment {
	public TestGameFragment() {
		setTargetFps(60);
		this.showStats = true;
	}

	long totalTime;
	
	/*
	 * Update the game state: dt is the elapsed time in milliseconds since
	 * the (start of the) last update.
	 */
	@Override
	public void onUpdate(long dt) {
		Log.d("TestGameFragment", "onUpdate: dt = " + dt);
		totalTime += dt;
		Log.d("TestGameFragment", "onUpdate: totalTime = " + totalTime);
		SystemClock.sleep(5);
	}

	/*
	 * Draw the game: only on this canvas! (Threads enzo)
	 */
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawRGB(100, 149, 237);
	}
}

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
	
	@Override
	public void onUpdate(long dt) {
		Log.d("TestGameFragment", "onUpdate: dt = " + dt);
		totalTime += dt;
		Log.d("TestGameFragment", "onUpdate: totalTime = " + totalTime);
		SystemClock.sleep(10);
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawRGB(100, 149, 237);
	}
}

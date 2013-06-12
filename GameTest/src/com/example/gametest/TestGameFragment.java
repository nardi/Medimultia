package com.example.gametest;

import android.graphics.Canvas;
import android.os.SystemClock;

public class TestGameFragment extends GameFragment {

	public TestGameFragment() {
		setTargetFps(60);
		this.showStats = true;
	}

	@Override
	public void onUpdate() {
		SystemClock.sleep(15);
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawRGB(100, 149, 237);
	}

}

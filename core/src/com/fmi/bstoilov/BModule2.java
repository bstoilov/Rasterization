package com.fmi.bstoilov;

import java.util.Random;

import com.badlogic.gdx.graphics.Camera;
import com.fmi.modules.AbstractModule;

public class BModule2 extends AbstractModule {

	private int WIDTH = 10;
	Random rand = new Random();

	public BModule2(Camera cam) {
		super(cam);
	}

	@Override
	public void handleClick(float x, float y) {
		int r = 120 + rand.nextInt(300 / grid.getPixelSize());
		for (int i = -WIDTH / 2; i < WIDTH / 2; i++) {
			plotCircle(x, y, r + i);
		}
	}

	@Override
	public void reset() {
		WIDTH += 5;
	}

	void plotCircle(float x, float y, int r) {
		plotCircle((int) x, (int) y, r);
	}

	void plotCircle(int xm, int ym, int r) {
		int delay = rand.nextInt(5);
		int x = -r, y = 0, err = 2 - 2 * r;
		do {
			grid.putPixel(xm - x, ym + y, delay);
			grid.putPixel(xm - y, ym - x, delay);
			grid.putPixel(xm + x, ym - y, delay);
			grid.putPixel(xm + y, ym + x, delay);
			r = err;
			if (r <= y)
				err += ++y * 2 + 1;
			if (r > x || err > y)
				err += ++x * 2 + 1;
		} while (x < 0);
	}

}

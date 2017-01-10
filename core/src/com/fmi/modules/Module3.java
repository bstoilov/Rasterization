package com.fmi.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.Camera;
import com.fmi.Pixel;

public class Module3 extends AbstractModule {
	Random r = new Random();
	private List<Pixel> borderValues = new ArrayList<Pixel>();
	boolean startFlag = false;

	public Module3(Camera cam) {
		super(cam);
	}

	@Override
	public void handleClick(float x, float y) {
		if (startFlag) {
			simpleFloodFill((int) x, (int) y, 1);
		} else {
			plotCircle(x, y, 50 + r.nextInt(25));
			getOnPixels();
		}
	}

	@Override
	public void reset() {
		startFlag = !startFlag;
	}

	void plotCircle(float x, float y, int r) {
		plotCircle((int) x, (int) y, r);
	}

	void plotCircle(int xm, int ym, int r) {
		int x = -r, y = 0, err = 2 - 2 * r;
		do {

			grid.putPixel(xm - x, ym + y);
			grid.putPixel(xm - y, ym - x);
			grid.putPixel(xm + x, ym - y);
			grid.putPixel(xm + y, ym + x);
			r = err;
			if (r <= y)
				err += ++y * 2 + 1;
			if (r > x || err > y)
				err += ++x * 2 + 1;
		} while (x < 0);
	}

	public void simpleFloodFill(int x, int y, float delay) {
		Pixel p = grid.getPixel(x, y);
		delay += 1;
		if (p == null || borderValues.contains(p) || p.on) {
			return;
		} else {
			grid.putPixel(x, y, delay);
			simpleFloodFill(x + grid.getPixelSize(), y, delay);
			simpleFloodFill(x - grid.getPixelSize(), y, delay);
			simpleFloodFill(x, y + grid.getPixelSize(), delay);
			simpleFloodFill(x, y - grid.getPixelSize(), delay);
		}
	}

	private void getOnPixels() {
		List<Pixel> pixels = grid.getAllPixels();
		borderValues = pixels.stream().filter(pixel -> pixel.on).collect(Collectors.toList());
	}

}

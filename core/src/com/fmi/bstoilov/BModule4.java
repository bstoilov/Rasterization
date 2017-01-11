package com.fmi.bstoilov;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.fmi.Pixel;
import com.fmi.modules.AbstractModule;

public class BModule4 extends AbstractModule {
	Random rand = new Random();
	private List<Pixel> borderValues = new ArrayList<Pixel>();
	boolean startFlag = false;

	public BModule4(Camera cam) {
		super(cam);
	}

	@Override
	public void handleClick(float x, float y) {
		if (startFlag) {
			simpleFloodFill((int) x, (int) y, 1);
		} else {
			plotCircle(x, y, 120 + rand.nextInt(300 / grid.getPixelSize()));
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
			grid.putPixel(x, y, delay, getRandColor());
			simpleFloodFill(x + grid.getPixelSize(), y, delay);
			simpleFloodFill(x - grid.getPixelSize(), y, delay);
			simpleFloodFill(x, y + grid.getPixelSize(), delay);
			simpleFloodFill(x, y - grid.getPixelSize(), delay);
		}
	}

	private Color getRandColor() {
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		return new Color(r, g, b, 1);
	}

	private void getOnPixels() {
		List<Pixel> pixels = grid.getAllPixels();
		borderValues = pixels.stream().filter(pixel -> pixel.on).collect(Collectors.toList());
	}

}

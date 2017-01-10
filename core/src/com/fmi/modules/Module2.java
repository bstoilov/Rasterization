package com.fmi.modules;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class Module2 extends AbstractModule {

	private Vector2 start = null;
	private Vector2 end = null;

	public Module2(Camera cam) {
		super(cam);
	}

	@Override
	public void handleClick(float x, float y) {
		if (start == null) {
			start = new Vector2(x, y);
		} else {
			end = new Vector2(x, y);
			plotEllipseRect(start.x, start.y, end.x, end.y);
		}
	}

	@Override
	public void reset() {
		start = null;
		end = null;
	}

	private void plotEllipseRect(float x0, float y0, float x1, float y1) {
		plotEllipseRect((int) x0, (int) y0, (int) x1, (int) y1);
	}

	private void plotEllipseRect(int x0, int y0, int x1, int y1) {
		int a = Math.abs(x1 - x0), b = Math.abs(y1 - y0), b1 = b & 1;
		long dx = 4 * (1 - a) * b * b, dy = 4 * (b1 + 1) * a * a;
		long err = dx + dy + b1 * a * a, e2;

		if (x0 > x1) {
			x0 = x1;
			x1 += a;
		}
		if (y0 > y1)
			y0 = y1;
		y0 += (b + 1) / 2;
		y1 = y0 - b1;
		a *= 8 * a;
		b1 = 8 * b * b;

		do {
			grid.putPixel(x1, y0);
			grid.putPixel(x0, y0);
			grid.putPixel(x0, y1);
			grid.putPixel(x1, y1);
			e2 = 2 * err;
			if (e2 <= dy) {
				y0++;
				y1--;
				err += dy += a;
			}
			if (e2 >= dx || 2 * err > dy) {
				x0++;
				x1--;
				err += dx += b1;
			}
		} while (x0 <= x1);

		while (y0 - y1 < b) {
			grid.putPixel(x0 - 1, y0);
			grid.putPixel(x1 + 1, y0++);
			grid.putPixel(x0 - 1, y1);
			grid.putPixel(x1 + 1, y1--);
		}
	}

}

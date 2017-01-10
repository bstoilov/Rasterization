package com.fmi.modules;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class Module1 extends AbstractModule {

	private Vector2 start = null;
	private Vector2 end = null;

	public Module1(Camera cam) {
		super(cam);
	}

	@Override
	public void handleClick(float x, float y) {
		if (start == null) {
			start = new Vector2(x, y);
		} else {
			end = new Vector2(x, y);
			plotLine(start.x, start.y, end.x, end.y);
		}
	}

	@Override
	public void reset() {
		start = null;
		end = null;
	}

	private void plotLine(float x0, float y0, float x1, float y1) {
		plotLine((int) x0, (int) y0, (int) x1, (int) y1);
	}

	private void plotLine(int x0, int y0, int x1, int y1) {
		System.out.println(delayStep);
		int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
		int dy = -Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
		int err = dx + dy, e2;
		float delay = 1;
		for (;;) {
			if (grid.putPixel(x0, y0, delay)) {
				delay += delayStep;
			}
			if (x0 == x1 && y0 == y1)
				break;
			e2 = 2 * err;
			if (e2 >= dy) {
				err += dy;
				x0 += sx;
			}
			if (e2 <= dx) {
				err += dx;
				y0 += sy;
			}
		}
	}

}

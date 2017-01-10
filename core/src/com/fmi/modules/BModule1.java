package com.fmi.modules;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class BModule1 extends AbstractModule {

	private Vector2 start = null;
	private Vector2 end = null;
	private boolean twoWayDraw = true;
	private float delay = 1;

	public BModule1(Camera cam) {
		super(cam);
	}

	@Override
	public void handleClick(float x, float y) {
		if (start == null) {
			start = new Vector2(x, y);
			putPixel((int) x, (int) y, true);
			delay = 0;
		} else {
			end = new Vector2(x, y);
			float nx = Math.min(start.x, end.x) + Math.abs((start.x - end.x) / 2f);
			float ny = Math.min(start.y, end.y) + Math.abs((start.y - end.y) / 2f);
			Vector2 midPoint = new Vector2(nx, ny);
			plotLineWidth(start.x, start.y, midPoint.x, midPoint.y, 5f, false);
			plotLineWidth(midPoint.x, midPoint.y, end.x, end.y, 5f, twoWayDraw);
			delay = 0;
		}
	}

	@Override
	public void reset() {
		start = null;
		end = null;
	}

	void plotLineWidth(float x0, float y0, float x1, float y1, float wd, boolean decr) {
		plotLineWidth((int) x0, (int) y0, (int) x1, (int) y1, wd, decr);
	}

	void plotLineWidth(int x0, int y0, int x1, int y1, float wd, boolean decr) {
		int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
		int dy = Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
		int err = dx - dy, e2, x2, y2;
		float ed = (float) (dx + dy == 0 ? 1 : Math.sqrt((float) dx * dx + (float) dy * dy));

		for (wd = (wd + 1) / 2;;) { /* pixel loop */
			putPixel(x0, y0, decr);
			e2 = err;
			x2 = x0;
			if (2 * e2 >= -dx) { /* x step */
				for (e2 += dy, y2 = y0; e2 < ed * wd && (y1 != y2 || dx > dy); e2 += dx)
					putPixel(x0, y2 += sy, decr);
				if (x0 == x1)
					break;
				e2 = err;
				err -= dy;
				x0 += sx;
			}
			if (2 * e2 <= dy) { /* y step */
				for (e2 = dx - e2; e2 < ed * wd && (x1 != x2 || dx < dy); e2 += dy)
					putPixel(x2 += sx, y0, decr);
				if (y0 == y1)
					break;
				err += dx;
				y0 += sy;
			}
		}
	}

	private void putPixel(int x, int y, boolean decr) {
		grid.putPixel(x, y, delay);
		if (decr) {
			delay -= 0.1f;
		} else {
			delay += 0.1f;
		}
	}
}
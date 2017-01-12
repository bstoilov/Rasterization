package com.fmi.modules;

import java.util.Random;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Module1 extends AbstractModule {

	private Vector2 start = null;
	private Vector2 end = null;

	private float delay = 0;

	public Module1(Camera cam) {
		super(cam);
	}

	@Override
	public void handleClick(float x, float y) {
		if (start == null) {
			start = new Vector2(x, y);
		} else {
			end = new Vector2(x, y);
			float nx = Math.min(start.x, end.x) + Math.abs((start.x - end.x) / 2f);
			float ny = Math.min(start.y, end.y) + Math.abs((start.y - end.y) / 2f);
			Vector2 midPoint = new Vector2(nx, ny);
			Color c = getRandColor();
			plotLine(start.x, start.y, midPoint.x, midPoint.y, c);
			delay = 0;
			plotLine(end.x, end.y, midPoint.x, midPoint.y, c);
			c = getRandColor();
			plotLine(start.x, start.y, end.x, end.y, c);
			delay = 0;
		}
	}

	@Override
	public void reset() {
		start = null;
		end = null;
	}

	private void plotLine(float x0, float y0, float x1, float y1, Color c) {
		plotLine((int) x0, (int) y0, (int) x1, (int) y1, c);
	}

	private void plotLine(int x0, int y0, int x1, int y1, Color c) {
		int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
		int dy = -Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
		int err = dx + dy, e2;
		for (;;) {
			grid.putPixel(x0, y0, delay, c);
			delay += 0.5f;

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

	private Color getRandColor() {
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		return new Color(r, g, b, 1);
	}

}

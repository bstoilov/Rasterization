package com.fmi.bstoilov;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.fmi.Pixel;
import com.fmi.modules.AbstractModule;

public class BModule3 extends AbstractModule {

	private boolean rectDrawn = false;
	private boolean lineDrawn = false;
	private Vector2 start = null;
	private Vector2 end = null;
	private List<Pixel> rectPixels = new ArrayList<Pixel>();
	private List<Pixel> linePixels = new ArrayList<Pixel>();
	Rectangle rect;
	Rectangle line;

	public BModule3(Camera cam) {
		super(cam);
	}

	@Override
	public void handleClick(float x, float y) {
		if (!rectDrawn) {
			drawRect(x, y, 400, 300);
			rectDrawn = true;
		} else if (!lineDrawn) {
			if (start == null) {
				start = new Vector2(x, y);
			} else {
				end = new Vector2(x, y);
				plotLine(start.x, start.y, end.x, end.y);
				lineDrawn = true;
				cut();
			}
		}
	}

	@Override
	public void reset() {
		grid.reset();
		start = null;
		end = null;
		lineDrawn = false;
		rectDrawn = false;
		rectPixels = new ArrayList<Pixel>();
		linePixels = new ArrayList<Pixel>();
	}

	private void cut() {
		for (Pixel p : grid.getAllPixels()) {
			if (rect.contains(p.x, p.y)) {
				if (!leftOfLine(p)) {
					p.on = true;
					p.color = Color.BLUE;
				} else {
					p.on = true;
					p.color = Color.RED;
				}
			}

		}
		for (Pixel p : linePixels) {
			if (rect.contains(p.x, p.y)) {
				p.color = Color.GREEN;
			}
		}
		for (Pixel p : rectPixels) {
			p.color = Color.BLACK;
		}

	}

	private boolean leftOfLine(Pixel p) {
		for (Pixel pixel : linePixels) {
			if (p.y == pixel.y && p.x >= pixel.x) {
				return false;
			} else if (p.y > pixel.y && p.x >= pixel.x) {
				return false;
			}
		}
		return true;
	}

	void drawRect(float x, float y, float w, float h) {
		plotLine(x, y, x + w, y);
		plotLine(x, y, x, y + h);
		plotLine(x, y + h, x + w, y + h);
		plotLine(x + w, y, x + w, y + h);
		rect = new Rectangle(x, y, w, h);
	}

	private void plotLine(float x0, float y0, float x1, float y1) {
		plotLine((int) x0, (int) y0, (int) x1, (int) y1);
	}

	private void plotLine(int x0, int y0, int x1, int y1) {
		int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
		int dy = -Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
		int err = dx + dy, e2;
		float delay = 0;
		for (;;) {

			if (grid.putPixel(x0, y0, delay)) {
				if (!rectDrawn) {
					rectPixels.add(grid.getPixel(x0, y0));
				} else if (!lineDrawn) {
					linePixels.add(grid.getPixel(x0, y0));
				}
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

package com.fmi.modules;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.fmi.Pixel;

public class Module5 extends AbstractModule {
	private static final float AP = 0.5f;

	public Module5(Camera cam) {
		super(cam);
	}

	@Override
	public void handleClick(float x, float y) {

	}

	private List<Vector2> points = new ArrayList<Vector2>();

	@Override
	public void handleDrag(float x, float y) {
		points.add(new Vector2(x, y));
		grid.putPixel(x, y);
		super.handleDrag(x, y);
	}

	@Override
	public void handleTouchDone(float x, float y) {
		Vector2[] result = new Vector2[points.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = points.get(i);
		}
		Pixel[] pixels = bezier(result);
		if (pixels == null) {
			return;
		}
		for (int i = 1; i < pixels.length; i++) {
			plotLine(pixels[i - 1].x, pixels[i - 1].y, pixels[i].x, pixels[i].y);
		}
		for (Pixel p : pixels) {
			grid.putPixel(p.x, p.y);
		}
		points = new ArrayList<Vector2>();
		super.handleTouchDone(x, y);
	}

	public Pixel[] bezier(Vector2[] points) {
		int n = points.length;
		if (n < 3) {
			// Cannot create bezier with less than 3 points
			return null;
		}
		Pixel[] bPoints = new Pixel[2 * (n - 2)];
		double paX, paY;
		double pbX = points[0].x;
		double pbY = points[0].y;
		double pcX = points[1].x;
		double pcY = points[1].y;
		for (int i = 0; i < n - 2; i++) {
			paX = pbX;
			paY = pbY;
			pbX = pcX;
			pbY = pcY;
			pcX = points[i + 2].x;
			pcY = points[i + 2].y;
			double abX = pbX - paX;
			double abY = pbY - paY;
			double acX = pcX - paX;
			double acY = pcY - paY;
			double lac = Math.sqrt(acX * acX + acY * acY);
			acX = acX / lac;
			acY = acY / lac;

			double proj = abX * acX + abY * acY;
			proj = proj < 0 ? -proj : proj;
			double apX = proj * acX;
			double apY = proj * acY;

			double p1X = pbX - AP * apX;
			double p1Y = pbY - AP * apY;
			bPoints[2 * i] = new Pixel((int) p1X, (int) p1Y, grid.getPixelSize(), grid.getPixelSize());

			acX = -acX;
			acY = -acY;
			double cbX = pbX - pcX;
			double cbY = pbY - pcY;
			proj = cbX * acX + cbY * acY;
			proj = proj < 0 ? -proj : proj;
			apX = proj * acX;
			apY = proj * acY;

			double p2X = pbX - AP * apX;
			double p2Y = pbY - AP * apY;
			bPoints[2 * i + 1] = new Pixel((int) p2X, (int) p2Y, grid.getPixelSize(), grid.getPixelSize());
		}
		return bPoints;
	}

	@Override
	public void reset() {

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

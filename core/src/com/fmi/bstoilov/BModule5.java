package com.fmi.bstoilov;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.fmi.Pixel;
import com.fmi.Pixel2;
import com.fmi.modules.AbstractModule2;

public class BModule5 extends AbstractModule2 {
	private static final float AP = 0.5f;
	private List<Vector2> points = new ArrayList<Vector2>();
	private boolean showInitialPoints = true;
	private float delay = 0;

	public BModule5(Camera cam) {
		super(cam);
	}

	@Override
	public void handleClick(float x, float y) {
		points.add(new Vector2(x, y));
		if (showInitialPoints) {
			grid.putPixel(x, y);
		}
	}

	@Override
	public void reset() {
		Vector2[] result = new Vector2[points.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = points.get(i);
		}
		Pixel2[] breizerPixels = bezier(result);
		Pixel2[] regularPixels = getRegular(result);
		drawPoints(breizerPixels, Color.RED);
		drawPoints(regularPixels, Color.GREEN);
		points = new ArrayList<Vector2>();
	}

	public Pixel2[] bezier(Vector2[] points) {
		int n = points.length;
		if (n < 3) {
			// Cannot create bezier with less than 3 points
			return null;
		}
		Pixel2[] bPoints = new Pixel2[2 * (n - 2)];
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
			bPoints[2 * i] = new Pixel2((int) p1X, (int) p1Y, grid.getPixelSize(), grid.getPixelSize());

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
			bPoints[2 * i + 1] = new Pixel2((int) p2X, (int) p2Y, grid.getPixelSize(), grid.getPixelSize());
		}
		return bPoints;
	}

	private void plotLine(float x0, float y0, float x1, float y1, Color c) {
		plotLine((int) x0, (int) y0, (int) x1, (int) y1, c);
	}

	private Pixel2[] getRegular(Vector2[] points) {
		Pixel2[] pixels = new Pixel2[points.length];
		for (int i = 0; i < points.length; i++) {
			pixels[i] = new Pixel2(points[i].x, points[i].y, grid.getPixelSize(), grid.getPixelSize());
		}
		return pixels;
	}

	private void plotLine(int x0, int y0, int x1, int y1, Color c) {
		int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
		int dy = -Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
		int err = dx + dy, e2;
		for (;;) {
			if (grid.putPixel(x0, y0, delay, c)) {
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

	private void drawPoints(Pixel2[] pixels, Color c) {
		if (pixels == null) {
			System.out.println("No points");
			return;
		}
		plotLine(points.get(0).x, points.get(0).y, pixels[0].x, pixels[0].y, c);
		for (int i = 1; i < pixels.length; i++) {
			plotLine(pixels[i - 1].x, pixels[i - 1].y, pixels[i].x, pixels[i].y, c);
		}
		for (Pixel2 p : pixels) {
			grid.putPixel(p.x, p.y, 1, c);
		}
		plotLine(pixels[pixels.length - 1].x, pixels[pixels.length - 1].y, points.get(points.size() - 1).x,
				points.get(points.size() - 1).y, c);
	}

}

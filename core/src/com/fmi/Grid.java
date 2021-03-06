package com.fmi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Grid {

	private static final int DEFAULT_PIXEL_SIZE = 8;
	private static final int WIN_W = Gdx.graphics.getWidth();
	private static final int WIN_H = Gdx.graphics.getHeight();
	private static final Color DEFAULT_COLOR = Color.BLACK;
	private static final Color GRID_COLOR = Color.BLACK;

	private boolean showGrid = false;
	private int pixelSize;
	private Map<String, Pixel> pixelMap;
	private final ShapeRenderer render;
	private final Camera cam;
	private List<PixelTask> tasks = new ArrayList<>();

	public Grid(Camera cam) {
		this.cam = cam;
		render = new ShapeRenderer();
		pixelSize = DEFAULT_PIXEL_SIZE;
		genGrid();
	}

	private void genGrid() {
		pixelMap = new HashMap<String, Pixel>();
		int wCount = WIN_W / pixelSize;
		int hCount = WIN_H / pixelSize;
		for (int i = -wCount / 2; i < wCount / 2; i++) {
			for (int j = -hCount / 2; j < hCount / 2; j++) {
				float x = i * pixelSize;
				float y = j * pixelSize;
				Pixel pixel = new Pixel(x, y, pixelSize, pixelSize);
				pixelMap.put(getIndex(i, j), pixel);
			}
		}
	}

	private String getIndex(int x, int y) {
		return x + "-" + y;
	}

	public int getPixelSize() {
		return this.pixelSize;
	}

	public void reset() {
		genGrid();
	}

	public void draw() {
		render.setProjectionMatrix(cam.combined);
		Iterator<PixelTask> iter = tasks.iterator();
		while (iter.hasNext()) {
			PixelTask pt = iter.next();
			if (pt.delay >= 0) {
				pt.delay--;
			} else {
				Pixel pixel = getPixel((int) pt.x, (int) pt.y);
				if (pixel != null) {
					pixel.on = true;
					pixel.color = pt.c;
				}
				iter.remove();
			}
		}

		if (showGrid) {
			drawGrid();
		}
		render.begin(ShapeType.Filled);
		for (String key : pixelMap.keySet()) {
			Pixel pixel = pixelMap.get(key);
			if (pixel.on) {
				Color c = pixel.color;
				render.setColor(c);
				// render.circle(pixel.x + pixel.width / 2, pixel.y +
				// pixel.width / 2, pixel.width / 2);
				render.rect(pixel.x, pixel.y, pixel.width, pixel.height);
			}
		}
		render.end();

	}

	public void toggleGrid() {
		this.showGrid = !this.showGrid;
	}

	public void incrPixelSize() {
		if (pixelSize <= 128) {
			pixelSize *= 2;
			genGrid();
		}
	}

	public void decrPixelSize() {
		if (pixelSize > 4) {
			pixelSize /= 2;
			genGrid();
		}
	}

	private void drawGrid() {
		render.begin(ShapeType.Line);
		render.setColor(GRID_COLOR);
		int wCount = WIN_W / pixelSize;
		int hCount = WIN_H / pixelSize;
		for (int i = -wCount / 2; i <= wCount / 2; i++) {
			float x = i * pixelSize;
			render.line(x, -WIN_H / 2, x, WIN_H);
		}
		for (int i = -hCount / 2; i <= hCount / 2; i++) {
			float x = i * pixelSize;
			render.line(-WIN_W / 2, x, WIN_W / 2, x);
		}

		render.end();
	}

	public List<Pixel> getAllPixels() {
		List<Pixel> pixels = new ArrayList<Pixel>();
		for (String k : pixelMap.keySet()) {
			pixels.add(pixelMap.get(k));
		}
		return pixels;
	}

	public boolean putPixel(float x, float y) {
		return putPixel(x, y, 0);
	}

	public boolean putPixel(float x, float y, float delay) {
		return putPixel(x, y, delay, DEFAULT_COLOR);
	}

	public boolean putPixel(float x, float y, float delay, Color c) {

		PixelTask pt = new PixelTask(x, y);
		pt.c = c;
		pt.delay = delay;
		tasks.add(pt);

		return true;
	}

	public boolean putPixel2(float x, float y, float delay, Color c) {
		PixelTask pt = new PixelTask(x - 1, y + getPixelSize() / 2);
		pt.c = c;
		pt.delay = delay;
		tasks.add(pt);

		return true;
	}

	public Pixel getPixel(int x, int y) {
		int nx = (int) (x / pixelSize);
		int ny = (int) (y / pixelSize);
		if (x < 0) {
			nx--;
		}
		if (y < 0) {
			ny--;
		}
		return pixelMap.get(getIndex(nx, ny));
	}

	private class PixelTask {
		public final float x, y;
		public Color c;
		public float delay = 0;

		public PixelTask(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
}

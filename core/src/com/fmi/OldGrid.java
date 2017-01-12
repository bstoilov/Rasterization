package com.fmi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class OldGrid {

	private static final int DEFAULT_PIXEL_SIZE = 8;
	private static final int WIN_W = Gdx.graphics.getWidth();
	private static final int WIN_H = Gdx.graphics.getHeight();
	private static final Color DEFAULT_COLOR = Color.BLACK;
	private static final Color GRID_COLOR = Color.BLACK;

	private boolean showGrid = true;
	private int pixelSize;
	private Map<String, Pixel2> pixelMap;
	private final ShapeRenderer render;
	private final Camera cam;

	public OldGrid(Camera cam) {
		this.cam = cam;
		render = new ShapeRenderer();
		pixelSize = DEFAULT_PIXEL_SIZE;
		genGrid();
	}

	private void genGrid() {
		pixelMap = new HashMap<String, Pixel2>();
		int wCount = WIN_W / pixelSize;
		int hCount = WIN_H / pixelSize;
		for (int i = -wCount / 2; i < wCount / 2; i++) {
			for (int j = -hCount / 2; j < hCount / 2; j++) {
				float x = i * pixelSize;
				float y = j * pixelSize;
				Pixel2 pixel = new Pixel2(x, y, pixelSize, pixelSize);
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
		if (showGrid) {
			drawGrid();
		}

		for (String key : pixelMap.keySet()) {
			Pixel2 pixel = pixelMap.get(key);
			if (!pixel.on || pixel.on && pixel.delay > 0) {
				render.begin(ShapeType.Line);
				drawPixel(render, pixel);
			} else {
				render.begin(ShapeType.Filled);
				drawPixel(render, pixel);
			}
			if (pixel.delay > 0) {
				pixel.delay--;
			}
		}

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

	private void drawPixel(ShapeRenderer render, Pixel2 pixel) {
		if (pixel.on && pixel.delay <= 0) {
			Color c = pixel.color;
			c.a = pixel.alpha;
			render.setColor(c);
			render.circle(pixel.x + pixel.width / 2, pixel.y + pixel.width / 2, pixel.width / 2);
			// render.rect(pixel.x, pixel.y, pixel.width, pixel.height);
			render.flush();
		}
		render.end();
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

	public List<Pixel2> getAllPixels() {
		List<Pixel2> pixels = new ArrayList<Pixel2>();
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
		Pixel2 pixel = getPixel((int) x, (int) y);
		if (pixel != null && !pixel.on) {
			pixel.on = true;
			pixel.delay = delay;
			pixel.color = c;
			return true;
		}
		return false;
	}

	public Pixel2 getPixel(int x, int y) {
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
}
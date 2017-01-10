package com.fmi;

import com.badlogic.gdx.graphics.Color;

public class Pixel {
	public final float x, y, width, height;
	public boolean on = false;
	public Color color = Color.GREEN;
	public float alpha = 1f;
	public float delay = 0;

	public Pixel(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	

}
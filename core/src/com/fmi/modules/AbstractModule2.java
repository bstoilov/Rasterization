package com.fmi.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.fmi.OldGrid;

public abstract class AbstractModule2 implements Module {
	protected OldGrid grid;
	protected float delayStep = 1f;

	protected AbstractModule2(Camera cam) {
		grid = new OldGrid(cam);
		InputProcessor inputProcessor = new InputHandler2(grid, this);
		Gdx.input.setInputProcessor(inputProcessor);
	}

	public void incrDelay() {
		delayStep += 0.5f;
	}

	public void decrDelay() {
		delayStep -= 0.5f;
	}

	@Override
	public void draw() {
		grid.draw();
	}

	public void handleDrag(float x, float y) {

	}

	public void handleTouchDone(float x, float y) {

	}

	public void handleClick(float x, float y) {

	}

	public abstract void reset();

	private class InputHandler2 implements InputProcessor {

		private final OldGrid grid;
		private final AbstractModule2 module;

		public InputHandler2(OldGrid grid, AbstractModule2 module) {
			this.grid = grid;
			this.module = module;
		}

		@Override
		public boolean keyDown(int keycode) {
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			switch (keycode) {
			case Keys.UP:
				grid.incrPixelSize();
				grid.reset();
				break;
			case Keys.DOWN:
				grid.decrPixelSize();
				grid.reset();
				break;
			case Keys.G:
				grid.toggleGrid();
				break;
			case Keys.O:
				module.incrDelay();
				break;
			case Keys.L:
				module.decrDelay();
				break;
			case Keys.R:
				module.reset();
				break;
			default:
				break;
			}
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {
			float w = Gdx.graphics.getWidth();
			float h = Gdx.graphics.getHeight();
			x = (int) (x - w / 2);
			y = (int) (h / 2 - y);
			module.handleClick(x, y);
			return false;
		}

		@Override
		public boolean touchUp(int x, int y, int pointer, int button) {
			module.handleTouchDone(x, y);
			return false;
		}

		@Override
		public boolean touchDragged(int x, int y, int pointer) {
			float w = Gdx.graphics.getWidth();
			float h = Gdx.graphics.getHeight();
			x = (int) (x - w / 2);
			y = (int) (h / 2 - y);
			module.handleDrag(x, y);
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			return false;
		}

	}
}

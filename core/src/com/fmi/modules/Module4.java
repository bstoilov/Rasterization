package com.fmi.modules;

import com.badlogic.gdx.graphics.Camera;

public class Module4 extends AbstractModule {

	protected Module4(Camera cam) {
		super(cam);
	}

	@Override
	public void handleClick(float x, float y) {
	}

	@Override
	public void reset() {
	}

	boolean LiangBarsky2DClip(float x1, float y1, float x2, float y2, float X1, float Y1, float X2, float Y2) {
		float dx, dy, tin = 0, tout = 1;
		dx = x2 - x1;
		dy = y2 - y1;
		if (calcT(dx, Math.max(X1, X2) - x1, tin, tout)) {
			if (calcT(-dx, x1 - Math.min(X1, X2), tin, tout)) {
				if (calcT(dy, Math.max(Y1, Y2) - y1, tin, tout)) {
					if (calcT(-dy, y1 - Math.min(Y1, Y2), tin, tout)) {
						if (tin > 0) {
							X1 = x1 + tin * dx;
							Y2 = y1 + tin * dy;
						} else {
							X1 = x1;
							Y1 = y1;
						}
						if (tout < 1) {
							X2 = x1 + tout * dx;
							Y2 = y1 + tout * dy;
						} else {
							X2 = x2;
							Y2 = y2;
						}
						return true;
					}
				}
			}
		}
		return false;

	}

	boolean calcT(float R, float Q, float tin, float tout) {
		float t;
		if (R > 0) {
			t = Q / R;
			if (t < tin) {
				return false;
			}
			tout = Math.min(t, tout);
		} else if (R < 0) {
			t = Q / R;
			if (t > tout) {
				return false;
			}
			tin = Math.max(t, tin);
		} else if (Q < 0) {
			return false;
		}
		return true;
	}

}

package com.organizers_group.whack_a_mole.game.interfaces;

import android.graphics.Canvas;

/**
 * Implemented by classes that can be drawn
 *
 */
public interface Drawable {
	/**
	 * Draws to a canvas
	 * @param canvas
	 */
	public void draw(Canvas canvas);
}

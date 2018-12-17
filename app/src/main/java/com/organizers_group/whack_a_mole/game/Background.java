package com.organizers_group.whack_a_mole.game;

import com.organizers_group.whack_a_mole.game.interfaces.Drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * A game's background */

public class Background implements Drawable {
	private static Bitmap bm;

	/**
	 * Creates a background
	 * @param width in pixels
	 * @param height in pixels
	 * @param bm the background image
	 */
	Background(int width, int height, Bitmap bm) {
		if(Background.bm == null) {
			System.out.println("Setting a new background");
			// Scale the background to the game board size
			BitmapFactory.Options options = new BitmapFactory.Options(); 
			options.inPurgeable = true;
			Background.bm = Bitmap.createScaledBitmap(bm, width, height, false);
		}
	}
		
	/**
	 * Draws this background on a canvas
	 * @param canvas from the Android framework
	 */
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bm, 0, 0, null);
	}
	
	public static void freeMemory() {
		if(bm != null) {
			System.out.println("Recycling background");
			bm.recycle();
			bm = null;
		}
	}
}
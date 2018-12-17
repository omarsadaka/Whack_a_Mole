package com.organizers_group.whack_a_mole.game.actor;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * Responsible for storage of and animating of an image.
 * 
 * 
 */
class Sprite {
	private Bitmap bm;
	private Matrix matrix;
	private float additionalX;
	private float additionalY;

	Sprite() {
		matrix = new Matrix();
	}

	/**
	 * Sets this sprite's image
	 * 
	 * @param bm This sprite's image
	 * @param size The size of the sprite
	 */
	void setBitmap(Bitmap bm, int size) {
		this.bm = Bitmap.createScaledBitmap(bm, size, size, false);
	}
	
	/**
	 * Sets this sprite's matrix
	 * 
	 * @param m The sprite's matrix
	 */
	void setMatrix(Matrix m) {
		this.matrix = m;
	}

	/**
	 * Draws this sprite onto the passed canvas at the passed location
	 */
	void draw(Canvas canvas, float x, float y) {
		matrix.reset();
		matrix.postTranslate(x + additionalX, y + additionalY);
		canvas.drawBitmap(getBitmap(), matrix, null);
	}

	/**
	 * Returns this sprite's bitmap
	 */
	Bitmap getBitmap() {
		return bm;
	}
	
	/**
	 * Returns the matrix used to draw this sprite
	 */
	Matrix getMatrix() {
		return matrix;
	}

	/**
	 * Sets the bitmap to the passed parameter
	 * @param bm
	 */
	void setBitmap(Bitmap bm) {
		this.bm = bm;
	}
	
	/**
	 * Moves the sprite an additional amount vertically
	 * @param y
	 */
	void additionalY(float y) {
		this.additionalY = y;
	}
}
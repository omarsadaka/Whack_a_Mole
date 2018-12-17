package com.organizers_group.whack_a_mole.game.actor;

import android.graphics.Bitmap;

/**
 * A pop up animation
 * 
 */
class PopUpper {
	/** 
	 * The original bitmap (never changes)
	 */
	private Bitmap originalBm;
	/** 
	 * The animated bitmap (changes with each frame)
	 */
	private Bitmap animBm;
	/**
	 * The animation's start time
	 */
	private long startTime = 0;
	/**
	 * To be animated
	 */
	private Sprite sprite;
	/**
	 * The time in which to complete a pop up animation
	 */
	private int popUpTime;
	/**
	 * Indicates that the animation is ready to go
	 */
	private boolean enabled = false;
	
	/**
	 * Creates a new instance of the PopUpper
	 * @param sprite Sprite to pop up
	 * @param popUpSpeed Speed in ms
	 */
	PopUpper(Sprite sprite, int popUpSpeed) {
		this.sprite = sprite;
		this.originalBm = sprite.getBitmap();
		this.popUpTime = popUpSpeed;
	}

	/**
	 * Animates the sprite
	 */
	synchronized void animate(long runTime) {
		// don't start animating till the constructor has completed
		if(!enabled) {
			return;
		}
		
		if(startTime == 0) {
			startTime = runTime;
		}

		float difference = runTime - startTime; // time in ms between starting pop
												// up and now
		// Popping up takes popUpSpeed milliseconds. Get the percent of time between
		// the start time and popUpSpeed seconds later
		float popUpPercent = difference / popUpTime ;
		popUpPercent += 0.01; // Ensure there's always something to be drawn

		// If the animation is finished
		if (popUpPercent >= 1) {
			stop();
			return;
		}
		
		// Calculates which "slice" of the original bitmap to show
		int slice = (int) (originalBm.getHeight() - (originalBm.getHeight() * popUpPercent));
		animBm = Bitmap.createBitmap(originalBm, 0, 0, originalBm.getWidth(),
		originalBm.getHeight() - slice);

		// Move the image "up" so it looks like it's appearing from the bottom
		sprite.additionalY(slice);
		sprite.setBitmap(animBm);
	}

	/**
	 * Returns the bitmap as it's drawn
	 */
	Bitmap getBitmap() {
		return animBm;
	}

	/**
	 * Is this animation enabled?
	 * @return
	 */
	boolean enabled() {
		return enabled;
	}
	
	/**
	 * Start this animation
	 */
	void start() {
		enabled = true;
	}
	
	/**
	 * Stop this animation
	 */
	void stop() {
		enabled = false;
		sprite.additionalY(0);
		sprite.setBitmap(originalBm);
	}
}

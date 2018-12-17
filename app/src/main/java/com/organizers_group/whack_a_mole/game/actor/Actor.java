package com.organizers_group.whack_a_mole.game.actor;

import com.organizers_group.whack_a_mole.game.interfaces.Drawable;
import com.organizers_group.whack_a_mole.game.interfaces.GameComponent;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Responsible for having and setting a location, being shown and hidden and
 * being hit.
 * 
 * 
 */
public final class Actor implements Drawable, GameComponent {
	private final int POPUP_SPEED = 150;
	private Point location;
	private Rect bounds;
	private boolean visible = false;
	private final Sprite sprite;
	private final GameBoard gameBoard;
	private final PopUpBehavior behavior;
	private PopUpper popUpper;

	/**
	 * Creates a new actor that is placed with the injected placer and draws
	 * itself with the passed sprite.
	 * @param gameBoard The gameboard the actor should appear on
	 * @param meerkatPic A picture of this actor
	 * @param size The size of the actor
	 */
	public Actor(GameBoard gameBoard, Bitmap meerkatPic, int size) {
		this.gameBoard = gameBoard;
		this.sprite = new Sprite();
		this.sprite.setBitmap(meerkatPic, size);
		this.bounds = new Rect(0, 0, size, size);
		this.behavior = new PopUpBehavior(this);
		popUpper = new PopUpper(sprite, POPUP_SPEED);
	}

	/**
	 * Sets the location of the actor
	 */
	void setLocation(int x, int y) {
		this.location = new Point(x, y);
	}

	/**
	 * Gets the bounds of this actor as a rectangle
	 */
	Rect getBounds() {
		return this.bounds;
	}

	/**
	 * Is the actor visible?
	 */
	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * Shows this actor on the gameboard.
	 */
	void show() {
		gameBoard.place(this);
		popUpper = new PopUpper(sprite, POPUP_SPEED);
		popUpper.start();
		visible = true;
	}

	/**
	 * Hides the actor
	 */
	void hide() {
		visible = false;
		setLocation(-1, -1);
		gameBoard.remove(this);
		popUpper.stop();
	}

	/**
	 * Detects whether the passed Rect overlaps with this actor
	 * 
	 * @param shot
	 *            the Rect that could overlap
	 * @return
	 */
	public boolean isOverlapping(Rect shot) {
		if (!visible) {
			return false;
		}
		int x = location.x;
		int y = location.y;
		Rect thisBounds = new Rect(x, y, x + getBounds().width(), y
				+ getBounds().height());

		if (thisBounds.intersect(shot)) {
			return true;
		}
		return false;
	}

	/**
	 * Draws the actor onto the passed canvas
	 */
	@Override
	public void draw(Canvas canvas) {
		// If we're visible, draw the actor
		if (visible) {
			sprite.draw(canvas, location.x, location.y);
		}
	}

	/**
	 * Tells the actor it has been hit
	 */
	public void hit() {
		behavior.hit();
	}

	@Override
	public void play(long runTime) {
		behavior.play(runTime);
		if(popUpper.enabled()) {
			popUpper.animate(runTime);
		}
	}
}

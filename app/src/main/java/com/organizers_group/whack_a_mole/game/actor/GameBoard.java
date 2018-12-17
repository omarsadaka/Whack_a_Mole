package com.organizers_group.whack_a_mole.game.actor;



import java.util.ArrayList;
import java.util.Random;

import android.graphics.Rect;

/**
 * Manages placing of actors
 *
 */
public class GameBoard {
	private ArrayList<Actor> actors = new ArrayList<Actor>();

	private final int width;
	private final int height;

	public GameBoard(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Gets the width
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height
	 * @return
	 */
	int getHeight() {
		return height;
	}
	
	/**
	 * Registers an actor that wants to interact with
	 * the game. Each actor can only be added once.
	 * @param actor
	 */
	synchronized private void addActor(Actor actor) {
		if (actors.contains(actor)) {
			throw new RuntimeException("Actor already registered with the game board");
		} else {
			actors.add(actor);
		}
	}

	/**
	 * Does the passed rect overlap with any registered actors?
	 * 
	 * @param Sprite The mover to check for overlapping
	 * @return boolean
	 */
	synchronized private boolean overlapping(Rect rectangle) {
		for (Actor a : actors) {
			if(a.isOverlapping(rectangle)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Places an actor on the Gameboard
	 */
	synchronized void place(Actor a) {
		Random r = new Random();
		int x = 0;
		int y = 0;

		int maxX = getWidth() - a.getBounds().width();
		int maxY = getHeight() - a.getBounds().height();
		
		int count = 0;
		do {
			x = r.nextInt(maxX);
			y = r.nextInt(maxY);
			count++;
			if (count > 100) {
				throw new RuntimeException("Can't place locatable");
			}
		} while (overlapping(new Rect(x, y, x + a.getBounds().width(), y + a.getBounds().height())));
		a.setLocation(x, y);
		addActor(a);
	}
	
	synchronized void remove(Actor a) {
		actors.remove(a);
	}
}
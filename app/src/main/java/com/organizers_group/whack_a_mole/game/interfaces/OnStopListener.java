package com.organizers_group.whack_a_mole.game.interfaces;

/**
 * Implemented by classes that want
 * to be notified when the game stops
 *
 */
public interface OnStopListener {
	/**
	 * What to do when the game is stopped
	 */
	public void onStop();
}
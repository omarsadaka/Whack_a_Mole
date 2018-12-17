package com.organizers_group.whack_a_mole.game.interfaces;

/**
 * Objects of this type can stop a game.
 *
 */
public interface Stopper {
	/**
	 * Returns true when the game should stop.
	 * @return
	 */
	public boolean needToStop();
}

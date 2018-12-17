package com.organizers_group.whack_a_mole.game.interfaces;
/**
 * Implemented by a component of the game can be "played"
 *
 */
public interface GameComponent {
	/**
	 * Actions the GameComponent takes for
	 * each iteration of the game loop.
	 * @param playTime Time the game has been running in ms
	 * @throws Exception
	 */
	public void play(long playTime);
}

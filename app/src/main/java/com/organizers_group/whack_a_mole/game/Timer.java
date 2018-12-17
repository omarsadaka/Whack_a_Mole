package com.organizers_group.whack_a_mole.game;

import com.organizers_group.whack_a_mole.game.interfaces.GameComponent;
import com.organizers_group.whack_a_mole.game.interfaces.Stopper;

import android.graphics.Paint;

/**
 * Times the game
 *
 */
class Timer implements Stopper, GameComponent {
	boolean finished = false;

	long timeLimit;
	Paint textPaint;
	long playTime;
	
	/**
	 * Stops the game after a specified time
	 * @param gameTime The time to stop after
	 */
	Timer(int gameTime) {
		this.timeLimit = gameTime;
	}
	
	/**
	 * If the game is finished, return true
	 * to let other components know the game
	 * needs to stop. 
	 */
	@Override
	public boolean needToStop() {
		return finished;
	}
	
	/**
	 *  Calculate the current game time and check whether the play time
	 *  has exceeded the game time
	 */
	@Override
	public void play(long runTime) {
		playTime = runTime;

		if(playTime > timeLimit) {
			finished = true;
		}
	}
	
	/**
	 * Calculate the time left before the game ends
	 * @return
	 */
	private long getTimeLeft() {
		// We add 500 to the visible output 
		// so the timer starts from e.g. 10 instead of 9
		long timeLeft = timeLimit + 500 - playTime;
		timeLeft = timeLeft / 1000;
		return timeLeft;
	}

	/**
	 * Return a string representation of the game time
	 */
	@Override 
	public String toString() {
		return Long.toString(getTimeLeft());
	}
}

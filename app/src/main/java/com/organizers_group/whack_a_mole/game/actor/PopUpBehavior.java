package com.organizers_group.whack_a_mole.game.actor;

import java.util.Random;

import com.organizers_group.whack_a_mole.game.interfaces.GameComponent;

/**
 * Makes an Actor pop up, then hide itself after a short time
 *
 */
class PopUpBehavior implements GameComponent {
	/**
	 * Minimum time (ms) for the Actor to be shown
	 */
	final int MIN_SHOW_TIME = 1000;
	/**
	 * Maximum time (ms) for the Actor to be shown
	 */
	final int MAX_SHOW_TIME = 4000;

	/**
	 * Minimum time (ms) for the Actor to be hidden
	 */
	final int MIN_HIDE_TIME = 500;
	/**
	 * Maximum time (ms) for the Actor to be shown
	 */
	final int MAX_HIDE_TIME = 2000;

	/**
	 * The time at which the Actor should be hidden
	 */
	private long nextHideTime = 0;
	/**
	 * The time at which the Actor should be shown
	 */
	private long nextShowTime = 0;
	
	/**
	 * Copy of the current play time
	 */
	private long playTime;

	/**
	 * The actor controlled by this behavior
	 */
	Actor actor;

	/**
	 * Creates a new PopUpBehavior that controls the passed Actor.
	 * Sets the next show time to be at least a second after creation.
	 */
	public PopUpBehavior(Actor actor) {
		this.actor = actor;
		// Set the actors to start showing at least a second after being enabled
		this.nextShowTime = new Random().nextInt(500) + 1000;
	}

	/**
	 * When the actor is hit, hide the actor then show it again after a
	 * delay
	 */
	public void hit() {
		actor.hide();
		showDelayed();
	}

	/**
	 * Shows the actor after a delay
	 */
	private void showDelayed() {
		nextShowTime = playTime + MIN_HIDE_TIME + new Random().nextInt(MAX_HIDE_TIME);
	}

	/**
	 * Hides the actor after a delay
	 */
	private void hideDelayed() {
		nextHideTime = playTime + MIN_SHOW_TIME + new Random().nextInt(MAX_SHOW_TIME);
	}

	/**
	 * Repeatedly shows and hides the Actor
	 */
	@Override
	public void play(long playTime) {
		this.playTime = playTime;
		if (actor.isVisible() && playTime > nextHideTime) {
			actor.hide();
			showDelayed();
		}

		if ((!actor.isVisible()) && playTime > nextShowTime) {
			actor.show();
			hideDelayed();
		}
	}
}

package com.organizers_group.whack_a_mole.game.loops;

import java.util.ArrayList;

import com.organizers_group.whack_a_mole.game.interfaces.GameComponent;
import com.organizers_group.whack_a_mole.game.interfaces.OnStopListener;
import com.organizers_group.whack_a_mole.game.interfaces.Stopper;

import android.os.Handler;

/**
 * The main loop for a game. Iterates through components, calling them each
 * iteration giving them time to process. Checks for stop conditions to identify
 * when the game is complete and notifies any registered stop listeners when the
 * game is complete.
 * 
 * 
 */
public class GameLoop {
	// Divide the rate by 1000 to calculate how many times per second the
	// gameloop will iterate.
	private static final int GAME_RATE = 20; // 50 calls per second

	private Handler frame = new Handler();
	private ArrayList<GameComponent> components = new ArrayList<GameComponent>();
	private ArrayList<Stopper> stoppables = new ArrayList<Stopper>();
	private ArrayList<OnStopListener> stopListeners = new ArrayList<OnStopListener>();
	// Seconds the game has been running
	private long runTime;
	// Absolute time of the last loop  
	private long lastLoopTime;

	/**
	 * Adds a game component to be called each time the game loop runs
	 * 
	 * @param component
	 */
	public void addGameComponent(GameComponent component) {
		components.add(component);
	}

	/**
	 * Adds a stop listener to be called when the game loop stops
	 * 
	 * @param stopListener
	 */
	public void addStopListener(OnStopListener stopListener) {
		this.stopListeners.add(stopListener);
	}

	/**
	 * What to do each time the game loop runs
	 */
	public Runnable gameLoop = new Runnable() {
		@Override
		synchronized public void run() {
			final long currentTime = System.currentTimeMillis(); 
			runTime += currentTime - lastLoopTime;
			lastLoopTime = currentTime;
			
			for (GameComponent gameComponent : components) {
				gameComponent.play(runTime);
			}

			for (Stopper sc : stoppables) {
				frame.removeCallbacks(gameLoop);
				if (sc.needToStop()) {
					for (OnStopListener sa : stopListeners) {
						sa.onStop();
					}

					return;
				}
			}

			frame.removeCallbacks(gameLoop);
			frame.postDelayed(gameLoop, GAME_RATE);
		}
	};

	/**
	 * Registers a stoppable to be called when the game loop stops
	 * 
	 * @param sc
	 *            Stoppable to be stopped
	 */
	public void registerStoppable(Stopper sc) {
		stoppables.add(sc);
	}

	/**
	 * Stop processing when the gameloop is paused
	 */
	public void pause() {
		frame.removeCallbacks(gameLoop);
	}

	/**
	 * When the game loop is unpaused, continue running
	 */
	public void unPause() {
		lastLoopTime = System.currentTimeMillis();
		gameLoop.run();
	}
}
package com.organizers_group.whack_a_mole.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.TextView;

import com.organizers_group.whack_a_mole.R;
import com.organizers_group.whack_a_mole.activities.GameActivity;
import com.organizers_group.whack_a_mole.game.actor.Actor;
import com.organizers_group.whack_a_mole.game.actor.GameBoard;
import com.organizers_group.whack_a_mole.game.interfaces.GameComponent;
import com.organizers_group.whack_a_mole.game.interfaces.OnStopListener;
import com.organizers_group.whack_a_mole.game.loops.GameLoop;
import com.organizers_group.whack_a_mole.game.loops.GraphicsLoop;
import com.organizers_group.whack_a_mole.levels.Level;


public class GameBuilder {
	private GameLoop gameLoop;
	private GraphicsLoop graphicsLoop;
	private Level level;
	private GameController game;
	private Score score;
	private int meerkatHitSoundId;
	private SoundPool soundPool;
	private GameLogic gameLogic;
	private GameBoard gameBoard;

	/**
	 * Sets the level around which this game is built
	 * 
	 * @param level
	 */
	public void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * Sets the game board size.
	 * @param width
	 * @param height
	 * @param graphicsLoop2 
	 * @param backgroundPic 
	 */
	public void addGameBoard(int width, int height, Bitmap backgroundPic) {
		gameBoard = new GameBoard(width, height);
		Background background = new Background(width, height, backgroundPic);
		graphicsLoop.register(background);
	}

	/**
	 * Instantiates the loops needed for a game
	 * 
	 * @param graphicsLoop
	 *            A graphicsLoop to be added to the gameLoop
	 */
	public void makeLoops(GraphicsLoop graphicsLoop) {
		gameLoop = new GameLoop();
		this.graphicsLoop = graphicsLoop;
		gameLoop.addGameComponent(graphicsLoop);
	}

	/**
	 * Creates a score entity to keep score
	 */
	public void addScore(final TextView scoreText) {
		score = new Score(level);
		gameLoop.addGameComponent(new GameComponent() {	
			@Override
			public void play(long runTime) {
				scoreText.setText(score.toString());
			}
		});
	}
	
	/**
	 * Creates a count down timer
	 * @param timerText The textview to update with the time
	 */
	public void makeTimer(final TextView timerText) {
		// Set a timer to stop the game after a specified time
		final Timer timer = new Timer(level.getTimeLimit() * 1000);
		gameLoop.addGameComponent(timer);
		gameLoop.registerStoppable(timer);
		gameLoop.addGameComponent(new GameComponent() {	
			@Override
			public void play(long runTime) {
				timerText.setText(timer.toString());
			}
		});
	}

	/**
	 * Shows the level end screen when the level finishes
	 * @param endLevelStarter
	 */
	public void addLevelEnd(final GameActivity gameActivity) {
		// Show the level end screen when the game stops
		gameLoop.addStopListener(new OnStopListener() {
			@Override
			public void onStop() {
				gameActivity.endLevel();
			}
		});
	}

	/**
	 * Adds a sound pool so the game can play sound
	 * @param context
	 */
	public void addSoundPool(Context context) {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		meerkatHitSoundId = soundPool.load(context, R.raw.hit, 1);
	}

	/**
	 * Adds mole to the game
	 * @param meerkatPic
	 */
	public void addMeerkats(Bitmap meerkatPic) {
		for (int i = 0; i < level.getMeerkats(); i++) {
			Actor meerkat = addMeerkat(meerkatPic, gameBoard);
			graphicsLoop.register(meerkat);
		}
	}

	/**
	 * Adds an individual mole to the game
	 * @param meerkatPic
	 * @param gameBoard
	 * @return
	 */
	private Actor addMeerkat(final Bitmap meerkatPic, final GameBoard gameBoard) {
		final Actor meerkat = new Actor(gameBoard, meerkatPic, getMeerkatSize());
		// Set the size of the meerkat to be a fixed % of the gameboard's height
		gameLoop.addGameComponent(meerkat);
		gameLogic.addActor(meerkat);

		return meerkat;
	}
	
	private int getMeerkatSize() {
		return (int) (gameBoard.getWidth() * 0.13);
	}
	
	public void addLogic(Bitmap meerkatPic) {
		gameLogic = new GameLogic(game, score, soundPool, meerkatHitSoundId);
		graphicsLoop.setOnTouchListener(gameLogic);
	}

	/**
	 * Completes the game building process
	 */
	public GameController getGame() {
		return game;
	}

	public void addGame() {
		this.game = new GameController(score, level, gameLoop);
	}
}

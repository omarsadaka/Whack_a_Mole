package com.organizers_group.whack_a_mole.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.organizers_group.whack_a_mole.R;
import com.organizers_group.whack_a_mole.activities.GameActivity;
import com.organizers_group.whack_a_mole.game.loops.GraphicsLoop;
import com.organizers_group.whack_a_mole.levels.Level;


public class GameBuilderDirector {
	private final GameBuilder gameBuilder;
	private static Bitmap meerkatPic;
	private static Bitmap backgroundPic;

	/**
	 * Builds the game
	 * @param gameActivity Provides access to the Android views
	 * @param context Provides a sound pool
	 * @param resources Provides access to images
	 * @param width The game's width
	 * @param height The game's height
	 * @param level The level to play
	 */
	public GameBuilderDirector(GameActivity gameActivity, Context context,
                               Resources resources, int width, int height,
                               Level level) {
		this.gameBuilder = new GameBuilder();
		GraphicsLoop graphicsLoop = (GraphicsLoop) gameActivity
				.findViewById(R.id.canvas);
		gameBuilder.makeLoops(graphicsLoop);
		gameBuilder.setLevel(level);
		if(backgroundPic == null) {
			backgroundPic = BitmapFactory.decodeResource(resources,
				R.drawable.background);
		}
		gameBuilder.addGameBoard(width, height, backgroundPic);
		backgroundPic.recycle();
		backgroundPic = null;
		
		TextView scoreText = (TextView) gameActivity
				.findViewById(R.id.game_score);
		gameBuilder.addScore(scoreText);
		gameBuilder.addGame();

		gameBuilder.addLevelEnd(gameActivity);

		gameBuilder.addSoundPool(context);

		// Set up the game's timer
		TextView timerText = (TextView) gameActivity.findViewById(R.id.game_time);
		gameBuilder.makeTimer(timerText);

		if(meerkatPic == null) {
			meerkatPic = (BitmapFactory.decodeResource(resources,
					R.drawable.meerkat_hole));
		}
		
		gameBuilder.addLogic(meerkatPic);
		gameBuilder.addMeerkats(meerkatPic);
	}
	
	/**
	 * Returns the game
	 * @return
	 */
    public GameController getGame() {
		return gameBuilder.getGame();
	}
	
	public static void freeMemory() {
		if(meerkatPic != null) {
			System.out.println("Recycling meerkatpic");
			meerkatPic.recycle();
			meerkatPic = null;
		}
		
		if(backgroundPic != null) {
			System.out.println("Recycling backgroundpic");
			backgroundPic.recycle();
			backgroundPic = null;
		}
	}
}

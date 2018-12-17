package com.organizers_group.whack_a_mole.activities;

import com.organizers_group.whack_a_mole.game.GameBuilderDirector;
import com.organizers_group.whack_a_mole.game.GameController;
import com.organizers_group.whack_a_mole.game.loops.GraphicsLoop;
import com.organizers_group.whack_a_mole.levels.EndLevel;
import com.organizers_group.whack_a_mole.levels.Level;
import com.organizers_group.whack_a_mole.levels.LevelSelect;
import com.organizers_group.whack_a_mole.levels.StartLevel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.organizers_group.whack_a_mole.R;



public class GameActivity extends VolumeControlActivity {
	private GameController game;
	private Level level;
	private boolean firstRun = true;

//todo remove ads
	//private InterstitialAd interstitial;
	
	/**
	 * Calls the StartLevel activity to show the level start screen
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		level = (Level) getIntent().getExtras().getSerializable("level");
		GameActivity gameActivity = this;
		ImageView pauseButton = (ImageView) findViewById(R.id.game_pause_image);
		pauseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pause();
			}
		});
		
		Intent intent = new Intent(gameActivity, StartLevel.class);
		intent.putExtra("level", level);
		startActivity(intent);

		//todo remove ads
//		interstitial = new InterstitialAd(this);
//	    interstitial.setAdUnitId("ca-app-pub-2000610966854199/6927547667");
//
//	    AdRequest adRequest = new AdRequest.Builder().build();
//
//	    interstitial.loadAd(adRequest);

	}

	
//
//	public void displayInterstitial() {
//	    if (interstitial.isLoaded()) {
//	      interstitial.show();
//	    }
//	  }
	//todo remove ads
//	private void startAds() {
//		// Look up the AdView as a resource and load a request.
//	    AdView adView = (AdView) this.findViewById(R.id.adView);
//	    AdRequest adRequest = new AdRequest.Builder()
//	    	.addTestDevice("49DF64CC878637EC6688968DCE79C38A") // S3
//			.addTestDevice("BE33969214ADC559F5715CA6CB92E408") // Dad's HTC
//			.addTestDevice("53642F8C3689BB2559E6B295B1900288") // N7
//			.addTestDevice("5E12190FA78AE7BA8D663D9721D46D2D") // Rach's Tab3
//			.addTestDevice("62B0F0EE0ACDC837A0D7722866289A94") // Atrix
//			.build();
//	    adView.loadAd(adRequest);
//	}

	/**
	 * Unpause the game when the activity is resumed
	 */
	@Override
	protected void onResume() {
		// game won't be initialized for the first onResume call
		// as onResume is called when an activity first starts
		if (game == null && !firstRun) {
			createGame();
		}
		
		if (game != null && game.isStarted()) {
			game.unPause();
		}
		super.onResume();
	}

	/**
	 * Creates the game
	 */
	private void createGame() {
		// Set the width and height
		ImageView placeholderBackground = (ImageView) findViewById(R.id.game_background_placeholder);
		int width = placeholderBackground.getWidth();
		int height = placeholderBackground.getHeight();

		GameBuilderDirector gameBuilderDirector = new GameBuilderDirector(this, this,
				this.getResources(),width, height, level);
		game = gameBuilderDirector.getGame();
		// Hide the placeholder gameboard and show the proper gameboard
		placeholderBackground.setVisibility(View.GONE);
		GraphicsLoop graphicsLoop = (GraphicsLoop) findViewById(R.id.canvas);
		graphicsLoop.setVisibility(View.VISIBLE);
		game.start();
		game.pause();
		//todo remove ads
		//startAds();
	}

	/**
	 * If the activity is stopped and restarted, go to the level select screen.
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		Intent intent = new Intent(this, LevelSelect.class);
		startActivity(intent);
	}

	/**
	 * Disable the back button
	 */
	@Override
	public void onBackPressed() {
	}

	/**
	 * Pause the game with the menu button
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (!game.isPaused()) {
				pause();
			} else {
				game.unPause();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * If this activity is paused, pause the game
	 */
	@Override
	public void onPause() {
		firstRun = false;
		if (game != null) {
			if (!game.isPaused()) {
				game.pause();
			}
		}
		super.onPause();
	}

	/**
	 * Explicitly end the activity when it's not visible. This significantly
	 * reduces the frequency of out of memory errors in the rest of the game.
	 */
	@Override
	public void onStop() {
		finish();
		//todo remove ads
		//displayInterstitial();
		super.onStop();
	}

	/**
	 * When a level ends, show the "End Level" activity
	 */
	public void endLevel() {
		int score = game.getScore();
		Level level = game.getLevel();
		Intent intent = new Intent(this, AuthenticationActivity.class);
		intent.putExtra("score", score);
		intent.putExtra("level", level);
		startActivity(intent);
	}
	
	/**
	 * Pauses the game and shows the game paused screen
	 */
	private void pause() {
		game.pause();
		Intent intent = new Intent(this, Pause.class);
		startActivity(intent);
	}
}
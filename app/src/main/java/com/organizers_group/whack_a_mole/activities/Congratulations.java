package com.organizers_group.whack_a_mole.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.organizers_group.whack_a_mole.R;
import com.organizers_group.whack_a_mole.game.Background;
import com.organizers_group.whack_a_mole.game.GameBuilderDirector;

public class Congratulations extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GameBuilderDirector.freeMemory();
		Background.freeMemory();
		System.gc();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setContentView(R.layout.activity_congratulations);
		
		ImageView balloon1 = (ImageView) findViewById(R.id.balloon_1);
		new DelayedAnimation(balloon1, getFlyUpBounce(), 600).execute();
		
		ImageView balloon2 = (ImageView) findViewById(R.id.balloon_2);
		new DelayedAnimation(balloon2, getFlyUpBounce(), 650).execute();
		
		ImageView balloon3 = (ImageView) findViewById(R.id.balloon_3);
		new DelayedAnimation(balloon3, getFlyUpBounce(), 100).execute();
		
		ImageView balloon4 = (ImageView) findViewById(R.id.balloon_4);
		new DelayedAnimation(balloon4, getFlyUpBounce(), 650).execute();
		
		ImageView balloon5 = (ImageView) findViewById(R.id.balloon_5);
		new DelayedAnimation(balloon5, getFlyUpBounce(), 600).execute();
		
		// Enable the onclicklistener after a delay
		delayedEnable();
	}
	
	/**
	 * Enables the onClickListener after a delay
	 */
	public void delayedEnable() {
		// Delay before enabling the on click listener in ms
		final int ENABLED_DELAY = 2000;
		Handler h = new Handler();
		Runnable r = new Runnable() {
			@Override
			public void run() {
				View background = findViewById(R.id.congratulations_container);
				background.setOnClickListener(new OnClickListener() { 
					public void onClick(View v) {
						Intent intent = new Intent(Congratulations.this, TitleScreen.class); 
						startActivity(intent);
					}
				});
			}
		};

		h.postDelayed(r, ENABLED_DELAY);
	}
	
	/**
	 * Returns an animationset to fly ballons up and bounce them
	 * @return The animationset
	 */
	private AnimationSet getFlyUpBounce() {
		Animation bounce = AnimationUtils.loadAnimation(this, R.anim.balloon_bounce);
		Animation flyUp = AnimationUtils.loadAnimation(this, R.anim.float_up);
		AnimationSet flyUpBounce = new AnimationSet(false);
		flyUpBounce.addAnimation(flyUp);
		flyUpBounce.addAnimation(bounce);
		return flyUpBounce;
	}
	
	/**
	 * When the back button is pressed, go back to the start screen
	 */
	@Override
	public void onBackPressed() {
		Intent i = new Intent(Congratulations.this, TitleScreen.class);
		startActivity(i);
	}
	
	/**
	 *  Explicitly end the activity when it's not visible.
	 *  This significantly reduces the frequency of out of memory errors.
	 */
	@Override
	public void onStop() {
		finish();
		super.onStop();
	}
}
package com.organizers_group.whack_a_mole.levels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.organizers_group.whack_a_mole.R;
import com.organizers_group.whack_a_mole.activities.VolumeControlActivity;

/**
 * Shows the "Start level" screen that bounces in over the game
 */
public class StartLevel extends VolumeControlActivity implements OnClickListener {	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_level);
		
		Bundle extras = getIntent().getExtras();
		Level level = (Level) extras.getSerializable("level");
		
		TextView title = (TextView) findViewById(R.id.level_info_start_title);
		title.setText("Level " + level.getNumber() + ": " + level.getTitle());
		TextView description = (TextView) findViewById(R.id.level_info_start_description);
		description.setText(level.getDescription());

		TextView meerkatCount = (TextView) findViewById(R.id.level_info_start_meerkats);
		meerkatCount.setText(Integer.toString(level.getTargetScore()));

		TextView time = (TextView) findViewById(R.id.level_info_start_time);
		time.setText(Integer.toString(level.getTimeLimit()));

		Button b = (Button) findViewById(R.id.level_info_start_start_button);
		b.setOnClickListener(this);
		
		final LinearLayout wholeView = (LinearLayout) findViewById(R.id.login_form);
		final Animation fadeIn = AnimationUtils.loadAnimation(this,
				R.anim.anim_in);

		wholeView.setAnimation(fadeIn);
	}
	
	/**
	 * When the start button is pressed, start the game
	 */
	@Override
	public void onClick(View v) {
		finish();
	}
	
	/**
	 * When the back button is pressed show level select
	 */
	@Override
	public void onBackPressed() {
		Intent i = new Intent(StartLevel.this, LevelSelect.class);
		startActivity(i);
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}
}
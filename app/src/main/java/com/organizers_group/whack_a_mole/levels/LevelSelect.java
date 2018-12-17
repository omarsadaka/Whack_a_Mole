package com.organizers_group.whack_a_mole.levels;

import com.organizers_group.whack_a_mole.R;
import com.organizers_group.whack_a_mole.activities.TitleScreen;
import com.organizers_group.whack_a_mole.activities.VolumeControlActivity;
import com.organizers_group.whack_a_mole.activities.GameActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * The level select screen
 *
 */
public class LevelSelect extends VolumeControlActivity {
	int[] textViews = { R.id.level1, R.id.level2, R.id.level3, R.id.level4,
			R.id.level5, R.id.level6, R.id.level7, R.id.level8, R.id.level9,
			R.id.level10, R.id.level11, R.id.level12, R.id.level13,
			R.id.level14, R.id.level15, R.id.level16, R.id.level17,
			R.id.level18, R.id.level19, R.id.level20, };

	/**
	 * Draws the level select screen, dynamically populating
	 * the level numbers with either a number if the level is 
	 * playable or "-" if the level hasn't been reached yet.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_select);
		final Activity reference = this;

		final String lockedLevel = "-";
		final int currentLevel = Preferences.getLevel(this);

		// Set a border around the active level
		for (int i : textViews) {
			final TextView tv = (TextView) findViewById(i);
			if (Integer.parseInt(tv.getText().toString()) == currentLevel) {
				LinearLayout ll = (LinearLayout) tv.getParent();
				ll.setBackgroundColor(getResources().getColor(
						R.color.current_level));
			}
		}

		// Display levels not yet completed by "-" (not including the current
		// level) and stop them being selected
		for (int i : textViews) {
			TextView tv = (TextView) findViewById(i);
			// TODO: this code shouldn't rely on the text in the textview
			if (Integer.parseInt(tv.getText().toString()) > currentLevel) {
				tv.setText(lockedLevel);
			}
		}

		// Makes the available levels clickable
		for (int i : textViews) {
			final TextView tv = (TextView) findViewById(i);
			if (!tv.getText().toString().equals(lockedLevel)) {
				tv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(reference,
								GameActivity.class);
						Level level = Levels.get(Integer.parseInt(tv.getText()
								.toString()));
						intent.putExtra("level", level);
						startActivity(intent);
						overridePendingTransition(R.anim.push_left_in,
								R.anim.push_left_out);
					}
				});
			}
		}
	}

	/**
	 * When the back button is pressed, go back to the start screen
	 */
	@Override
	public void onBackPressed() {
		Intent i = new Intent(LevelSelect.this, TitleScreen.class);
		startActivity(i);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}
}

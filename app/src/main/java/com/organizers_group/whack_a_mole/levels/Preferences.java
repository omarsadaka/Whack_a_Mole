package com.organizers_group.whack_a_mole.levels;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manages the game's preferences
 *
 */
class Preferences {
	final static String PREFS_NAME = "MeerkatChallenge";

	/**
	 * Sets the current level
	 * @param a
	 * @param levelNum
	 */
	public static void setLevel(Activity a, int levelNum) {
		SharedPreferences preferences = a.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("level", levelNum);
		editor.commit();
	}
	
	/**
	 * Returns the current level
	 * @param a
	 * @return
	 */
	public static int getLevel(Activity a) {
		SharedPreferences preferences = a.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getInt("level", 1);
	}
}

package com.organizers_group.whack_a_mole.levels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.organizers_group.whack_a_mole.R;
import com.organizers_group.whack_a_mole.activities.AuthenticationActivity;
import com.organizers_group.whack_a_mole.activities.Congratulations;
import com.organizers_group.whack_a_mole.activities.VolumeControlActivity;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shows the "End level" screen that bounces in
 */
public class EndLevel extends VolumeControlActivity {

	private RequestQueue requestQueue;
	// Delay before enabling the button in ms
	final static int ENABLED_BUTTON_DELAY = 700;
	Level level;
	int wrongClick = 0;
	String totalScore;
	String UserId;
	Class<? extends Activity> nextAction = LevelSelect.class;
	int score;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end_level);




		requestQueue = Volley.newRequestQueue(this);
		 score = getIntent().getExtras().getInt("score");
		level = (Level) getIntent().getExtras().getSerializable("level");
		//UserId  = getIntent().getExtras().getInt("userId");
		String description;
		totalScore = String.valueOf(level.getTargetScore());
		if (score >= level.getTargetScore()) {			
			if(level.getNumber() == 20) {
				nextAction = Congratulations.class;
			} else {
				// Only update progress if the user hasn't completed this level
				if(level.getNumber() >= Preferences.getLevel(this)) { 
					Preferences.setLevel(this, level.getNumber() + 1);
				}
			}
			int ss1 = Integer.parseInt("d83d", 16);
			int ss2 = Integer.parseInt("de03", 16);
			char chars = Character.toChars(ss1)[0];
			char chars2 = Character.toChars(ss2)[0];
			int codepoint = Character.toCodePoint(chars, chars2);
			String emojiString = new String(Character.toChars(codepoint));
			description = "You did it" + emojiString;
		} else {
			int ss1 = Integer.parseInt("d83d", 16);
			int ss2 = Integer.parseInt("de14", 16);

			char chars = Character.toChars(ss1)[0];
			char chars2 = Character.toChars(ss2)[0];

			int codepoint = Character.toCodePoint(chars, chars2);
			String emojiString = new String(Character.toChars(codepoint));

			description = "You didn't make it "+ emojiString;
		}
		if (level.getTargetScore() > score){
			wrongClick = level.getTargetScore() - score;
			Log.d("wrongClick" , String.valueOf(wrongClick));
		}else {
			wrongClick = wrongClick;
			Log.d("wrongClick" , String.valueOf(wrongClick));
		}
		TextView titleView = (TextView) findViewById(R.id.level_info_end_title);
		String title = "Level "+ level.getNumber() + ": " + level.getTitle();
		titleView.setText(description);
		TextView descriptionView = (TextView) findViewById(R.id.level_info_end_description);
		descriptionView.setText(title);
		TextView meerkatCountView = (TextView) findViewById(R.id.level_end_meerkat_count);
		meerkatCountView.setText(Integer.toString(score) + "/" + level.getTargetScore());
		/* Enable the button after a delay
		 * This stops the player hitting a button when they 
		 * were aiming at an actor that's suddenly been replaced
		 * by a button */
		delayedEnable();
		final LinearLayout wholeView = (LinearLayout) findViewById(R.id.level_end_container);
		final Animation fadeIn = AnimationUtils.loadAnimation(this,
				R.anim.anim_in);
		wholeView.setAnimation(fadeIn);
	}
	
	/**
	 * Enables the buttons after a delay
	 */
	private void delayedEnable() {
		Handler h = new Handler();
		Runnable r = new Runnable() {
			@Override
			public void run() {
				Button next = (Button) findViewById(R.id.level_end_continue_button);
				next.setOnClickListener(new OnClickListener() { 
					public void onClick(View v) {

						postData();
						Intent intent = new Intent(EndLevel.this, nextAction); 
						startActivity(intent);
						overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
					}
				});
			}
		};

		h.postDelayed(r, ENABLED_BUTTON_DELAY);
	}
	
	/**
	 * When the back button is pressed, go to the level select screen
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent i = new Intent(EndLevel.this, LevelSelect.class);
			startActivity(i);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
//	public void getDate(){
//     //todo send data to ahmed
//		String email  = getIntent().getExtras().getString("email");
//		String name = getIntent().getExtras().getString("name");
//		String age = getIntent().getExtras().getString("age");
//
//		Log.e("email" , email);
//		Log.e("name" , name);
//		Log.e("age" , age);
//		Log.e("score" , String.valueOf(score));
//		Log.e("wrongScore" , String.valueOf(wrongClick));
//
//
//	}


	public void postData() {

		SharedPreferences prefs = getSharedPreferences("ID", MODE_PRIVATE);
		String restoredId = prefs.getString("user_id", null);
		if (restoredId != null) {
			UserId = prefs.getString("user_id", "No name defined");//"No name defined" is the default value.

		}

		String Url = "http://cartaman.com/game/wp-json/org/v1/son_season";

		Log.e("UserID" , String.valueOf(UserId));
		Log.e("score" , String.valueOf(score));
		Log.e("wrongScore" , String.valueOf(wrongClick));
		Log.e("tatalScore" , totalScore);
		Log.e("level" , String.valueOf(level.getNumber()));


//		JSONObject json = new JSONObject();
//		try {
//			json.put("user_id" , UserId );
//			json.put("right_clicks" , score);
//			json.put("wrong_clicks" , wrongClick);
//			json.put("total_clicks" , totalScore);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}

		// post API
		StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,Url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.e("Response" , String.valueOf(response));

					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						Log.e("Error" , String.valueOf(error));

						ConnectivityManager cm = (ConnectivityManager) getSystemService(EndLevel.this.CONNECTIVITY_SERVICE);
						if (cm.getActiveNetworkInfo() == null){
							Toast.makeText(EndLevel.this, "No Internet Connection", Toast.LENGTH_LONG).show();
						}
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("user_id",UserId);
				params.put("right_clicks", String.valueOf(score));
				params.put("wrong_clicks", String.valueOf(wrongClick));
				params.put("total_clicks", totalScore);
				params.put("level" , String.valueOf(level.getNumber()));


				return params;

			}
		};

		jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
			@Override
			public int getCurrentTimeout() {
				return 50000;
			}

			@Override
			public int getCurrentRetryCount() {
				return 50000;
			}

			@Override
			public void retry(VolleyError error) {

			}
		});
		requestQueue.add(jsonObjectRequest);
	}

}

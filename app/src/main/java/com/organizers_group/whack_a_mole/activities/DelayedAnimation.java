package com.organizers_group.whack_a_mole.activities;

import android.os.AsyncTask;
import android.view.animation.Animation;
import android.widget.ImageView;

public class DelayedAnimation extends AsyncTask<Void, Void, Void>{
	private ImageView imageView;
	private Animation animation;
	private int delay;
	
	public DelayedAnimation(ImageView imageView, Animation animation, int delay) {
		this.imageView = imageView;
		this.animation = animation;
		this.delay = delay;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			Thread.sleep((long) this.delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		imageView.startAnimation(animation);
	}


}

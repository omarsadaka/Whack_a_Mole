package com.organizers_group.whack_a_mole.game.loops;

import java.util.ArrayList;

import com.organizers_group.whack_a_mole.game.interfaces.Drawable;
import com.organizers_group.whack_a_mole.game.interfaces.GameComponent;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Maintains a list of the current drawables to be drawn,
 * the background to draw them on and draws them each time
 * the play() method is called.
 *
 */
public class GraphicsLoop extends View implements GameComponent {
	ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	boolean running = true;
	
	public GraphicsLoop(Context context, AttributeSet aSet) {
		super(context, aSet);
	}
	
	/**
	 * When we're asked to be drawn
	 */
	@Override
	synchronized public void onDraw(Canvas canvas) {
		if(!running) {
			return;
		}
		for(Drawable d : drawables) {
			d.draw(canvas);
		}
	}

	/**
	 * Registers a new drawable to be drawn
	 * @param drawable
	 */
	public void register(Drawable drawable) {
		drawables.add(drawable);
	}

	/**
	 * Each time we're asked to play(), invalidate
	 * the view so it's redrawn. This causes the 
	 * onDraw() method to be called.
	 */
	@Override
	public void play(long runTime) {
		invalidate();
	}
}
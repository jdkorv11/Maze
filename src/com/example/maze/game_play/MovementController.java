package com.example.maze.game_play;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;

@SuppressWarnings("serial")
public class MovementController extends EventObject {

	public MovementController(Object source) {
		super(source);
	}

	public static final int WEST = 0;
	public static final int NORTH = 1;
	public static final int EAST = 2;
	public static final int SOUTH = 3;

	public interface MovementListener extends EventListener {
		/**
		 * indicates a step has been taken
		 */
		public void onStep(int direction);
	}

	protected ArrayList<MovementListener> eventListenerList = new ArrayList<MovementListener>();

	/**
	 * Adds an eventListener to eventListenerList
	 * 
	 * @param listener
	 */
	public void addMovementListener(MovementListener listener) {
		eventListenerList.add(listener);
	}

	/**
	 * Removes an eventListener from eventListenerList
	 * 
	 * @param listener
	 */
	public void removeMovementListener(MovementListener listener) {
		eventListenerList.remove(listener);
	}

	private int direction;

	public void takeStep(int direction) {
		Object[] listeners = eventListenerList.toArray();
		this.direction = direction;
		for (int i = 0; i < listeners.length; i++) {
			((MovementListener) listeners[i]).onStep(direction);
		}
	}

	public int getDirection() {
		return direction;
	}

}

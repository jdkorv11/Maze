package com.example.maze.game_play;

import com.example.maze.game_play.Maze.Square;

public class Location {

	private int row;
	private int column;
	private Square type;

	public Location(int row, int column, char c) {
		this.row = row;
		this.column = column;
		type = Square.fromChar(c);
	}

	@Override
	public String toString() {
		return "Location [row=" + row + ", column=" + column + ", type=" + type
				+ "]";
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public Square getType() {
		return type;
	}
}

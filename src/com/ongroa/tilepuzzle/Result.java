package com.ongroa.tilepuzzle;

public class Result {

	private int size;
	private String time;
	private int move;
	
	Result(int size, String time, int move) {
		this.setSize(size);
		this.setTime(time);
		this.setMove(move);
	}

	public Result() {
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getMove() {
		return move;
	}

	public void setMove(int move) {
		this.move = move;
	}
	
}

package com.abrus.life;

public class LifeCell {

	private boolean mAlive = false;
	
	public LifeCell(boolean alive) {
		this.mAlive = alive;
	}

	public LifeCell() {
		// TODO Auto-generated constructor stub
	}

	public boolean isAlive() {
		return mAlive;
	}

	public void alive() {
		this.mAlive = true;
	}

	public void die() {
		this.mAlive = false;
	}
	
	@Override
	public String toString() {
		if(this.mAlive) return "*";
		return "O";
	}


}

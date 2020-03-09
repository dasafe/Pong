package com.pong.game;

import com.badlogic.gdx.Game;

public class ArenaRoamer extends Game {

	public static final int WIDTH = 480;
	public static final int HEIGHT = 320;
	public GameScreen gameScreen;
	
	@Override
	public void create() {
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}
}

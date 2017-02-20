package com.sameperson.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sameperson.game.Drops;
import com.sameperson.game.GameScreen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "First Gdx";
		config.width = 800;
		config.height = 480;
        config.resizable = false;
		new LwjglApplication(new Drops(), config);
	}
}

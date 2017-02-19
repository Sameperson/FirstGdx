package com.sameperson.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Application extends ApplicationAdapter {
	private Texture bucketImage;
    private Texture dropletImage;
    private Music rainBackgroundSound;
    private Sound dropSound;

    private OrthographicCamera camera;
	private SpriteBatch batch;

    private Rectangle bucket;


    @Override
	public void create () {
        Gdx.app.setLogLevel(com.badlogic.gdx.Application.LOG_DEBUG);

        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        dropletImage = new Texture(Gdx.files.internal("droplet.png"));
        rainBackgroundSound = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainBackgroundSound.setLooping(true);
        rainBackgroundSound.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        bucket = new Rectangle();
        bucket.width = 64;
        bucket.height = 64;
        bucket.x = Gdx.graphics.getWidth() / 2 - bucket.width / 2;
        bucket.y = 20;

		batch = new SpriteBatch();

    }

	@Override
	public void render () {
		if (Gdx.input.isTouched()) {
            Gdx.app.debug("Input", "Mouse x=" + Gdx.input.getX() + ", y=" + Gdx.input.getY());
        }
		Gdx.gl.glClearColor(0, 0, 0.2F, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bucketImage, bucket.x, bucket.y);
        batch.end();

        if(Gdx.input.isTouched()) {
            Vector3 touchPosition = new Vector3();
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            bucket.x = touchPosition.x - 32;
        }

	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}

package com.sameperson.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {

    private final Drops game;
    //Assets
	private Texture bucketImage;
    private Texture dropletImage;
    private Music rainBackgroundSound;
    private Sound dropSound;
    //Camera and Batch
    private OrthographicCamera camera;
	private SpriteBatch batch;
    //Rectangles
    private Rectangle bucket;
    private Array<Rectangle> raindrops;
    //etc.
    private Vector3 touchPosition;
    private long lastDropTime;
    private int dropsGathered;

    public GameScreen(Drops game) {
        this.game = game;
        this.game.gameScreen = this;

        Gdx.app.setLogLevel(com.badlogic.gdx.Application.LOG_DEBUG);

        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        dropletImage = new Texture(Gdx.files.internal("droplet.png"));
        rainBackgroundSound = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainBackgroundSound.setLooping(true);


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        bucket = new Rectangle();
        bucket.width = 64;
        bucket.height = 64;
        bucket.x = Gdx.graphics.getWidth() / 2 - bucket.width / 2;
        bucket.y = 20;

        raindrops = new Array<Rectangle>();
        spawnRaindrop();
    }

    @Override
	public void render (float delta) {
		if (Gdx.input.isTouched()) {
            Gdx.app.debug("Input", "Mouse x=" + Gdx.input.getX() + ", y=" + Gdx.input.getY());
        }
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
        game.batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);
        for (Rectangle raindrop : raindrops) {
            game.batch.draw(dropletImage, raindrop.x, raindrop.y);
        }
        game.batch.end();

        if (Gdx.input.isTouched()) {
            touchPosition = new Vector3();
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            bucket.x = touchPosition.x - 64 / 2;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && bucket.x > 0) {
            bucket.x -= 300 * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && bucket.x < Gdx.graphics.getWidth() - 64) {
            System.out.println(Gdx.graphics.getWidth());
            bucket.x += 300 * Gdx.graphics.getDeltaTime();
        }

        if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
            spawnRaindrop();

        Iterator<Rectangle> iterator = raindrops.iterator();
        while (iterator.hasNext()) {
            Rectangle raindrop = iterator.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0)
                iterator.remove();
            if (raindrop.overlaps(bucket)) {
                dropsGathered++;
                dropSound.play();
                iterator.remove();
            }
        }
	}

    @Override
    public void show() {
        rainBackgroundSound.play();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
	public void dispose () {
        dropletImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainBackgroundSound.dispose();
        batch.dispose();
	}

	private void spawnRaindrop() {
	    Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }
}

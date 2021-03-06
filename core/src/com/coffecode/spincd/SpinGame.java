package com.coffecode.spincd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.coffecode.spincd.scenes.Hud;
import com.coffecode.spincd.screens.PlayScreen;

public class SpinGame extends com.badlogic.gdx.Game {
    public SpriteBatch batch;
    public static final int WIDTH = 480;
    public static final int HEIGHT = 800;

    public static final int HALF_WIDTH = WIDTH / 2;
    public static final int HALF_HEIGHT = HEIGHT / 2;

    public static final int QUARTER_WIDTH = WIDTH / 4;
    public static final int QUARTER_HEIGHT = HEIGHT / 4;

    public static final long SEED = 420;
    private Screen playScreen;


    @Override
    public void create() {
        batch = new SpriteBatch();
        playScreen = new PlayScreen(this);
        setScreen(playScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}

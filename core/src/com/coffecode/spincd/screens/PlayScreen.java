package com.coffecode.spincd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.coffecode.spincd.SpinGame;
import com.coffecode.spincd.scenes.Hud;
import com.coffecode.spincd.sprites.Disk;

/**
 * Created by Laptop on 3/10/2016.
 */
public class PlayScreen implements Screen {

    private SpinGame game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    private Disk disk;

    public PlayScreen(SpinGame game) {
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(SpinGame.WIDTH, SpinGame.HEIGHT, gameCam);
        hud =  new Hud(game.batch, gamePort);
        disk = new Disk(new Vector2(SpinGame.WIDTH / 2, SpinGame.HEIGHT / 2));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(3, 1, 0 ,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput(delta);

        game.batch.setProjectionMatrix(gameCam.combined);
        hud.stage.draw();

        disk.getSprite().setRotation(disk.getSprite().getRotation() + disk.getAngularVelocity());
        game.batch.begin();
        disk.getSprite().draw(game.batch);
        game.batch.end();
        disk.slow();

    }

    private void handleInput(float delta) {
        if (Gdx.input.justTouched()) {
            disk.addAngularVelocity(3.0f);
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);

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
    public void dispose() {

    }
}

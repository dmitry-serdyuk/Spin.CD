package com.coffecode.spincd.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Laptop on 3/10/2016.
 */
public class Hud {
    public Stage stage;

    private int score = 0;
    Label scoreLabel;

    public static final int SCORE_OFFSET = 40;

    public Hud(SpriteBatch batch, Viewport viewport) {
        stage = new Stage(viewport, batch);

        FileHandle handle = Gdx.files.internal("fonts/zig.fnt");
        scoreLabel = new Label("djnjsanadnasdkjadsa", new Label.LabelStyle(new BitmapFont(handle), Color.WHITE));
        Container labelContainer = new Container(scoreLabel);
        labelContainer.setFillParent(true);
        labelContainer.top();
        stage.addActor(labelContainer);
    }
}

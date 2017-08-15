package com.coffecode.spincd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.coffecode.spincd.SpinGame;
import com.coffecode.spincd.input.GameGestureListener;
import com.coffecode.spincd.processing.Generator;
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
    private Generator generator;
    private Texture background;
    private ShapeRenderer shapeRenderer;
    private boolean collision = false;
    private Vector3 collisionPos;
    private Vector3 unprojected;
    private boolean gameOver = false;

    private int requiredMove;
    private Vector2 moveArrowLocation;

    private TextureAtlas arrowAtlas;
    private Animation arrowAnimation;
    private int arrowAnimationWidth;
    private int arrowAnimationHeight;
    private float arrowAnimationScaleX;
    private float arrowAnimationScaleY;
    private float arrowAnimationRotation;
    private float elapsedTime = 0;

    private final int MIN_VELOCITY = 400;
    private final float VELOCITY_REDUCTION = 1000f;
    private final int ARROW_SIDE_OFFSET = 30;

    public PlayScreen(SpinGame game) {
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(SpinGame.WIDTH, SpinGame.HEIGHT, gameCam);
        hud = new Hud(game.batch, gamePort);
        disk = new Disk(new Vector2(SpinGame.HALF_WIDTH, SpinGame.HALF_HEIGHT));
        generator =  new Generator();
        background = new Texture("bg.jpg");
        collisionPos = new Vector3();
        shapeRenderer = new ShapeRenderer();

        //First move is left fling
        requiredMove = 0;

        arrowAtlas = new TextureAtlas(Gdx.files.internal("animation/arrows.atlas"));
        arrowAnimationWidth = arrowAtlas.findRegion("one").getRegionWidth();
        arrowAnimationHeight = arrowAtlas.findRegion("one").getRegionHeight();
        moveArrowLocation = new Vector2(SpinGame.HALF_WIDTH  + (arrowAnimationWidth/2),
                SpinGame.QUARTER_HEIGHT - (arrowAnimationHeight/2));
        arrowAnimationScaleX = -1f;
        arrowAnimationScaleY = 1f;
        arrowAnimationRotation = 0;

        arrowAnimation = new Animation(1/10f, arrowAtlas.getRegions());

        //handle swipe input
        Gdx.input.setInputProcessor(new GestureDetector(new GameGestureListener(this)));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameCam.update();
        game.batch.setProjectionMatrix(gameCam.combined);

        //Calculations
        if(!collision) {
            disk.getSprite().setRotation(disk.getSprite().getRotation() + disk.getAngularVelocity());

            generator.step();
            disk.slow(delta);

            //Flip angular velocity to positive int
            hud.setSpeed((int)(disk.getAngularVelocity() * -1));
            hud.animateSpeed(Gdx.graphics.getDeltaTime());
        }

        //Render
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        elapsedTime += Gdx.graphics.getDeltaTime();
        disk.getSprite().draw(game.batch);
        game.batch.draw(arrowAnimation.getKeyFrame(elapsedTime, true),
                moveArrowLocation.x, moveArrowLocation.y, 0, 0,
                arrowAnimationWidth, arrowAnimationHeight,
                arrowAnimationScaleX, arrowAnimationScaleY,
                arrowAnimationRotation);

        generator.getBar().getSprite().draw(game.batch);
        game.batch.end();

        if (collision) {
            gameOver = true;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setProjectionMatrix(game.batch.getProjectionMatrix());
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(collisionPos.x, collisionPos.y, 20);
            //Gdx.app.log("COLLISION POS", "X: " + collisionPos.x + " Y: " + collisionPos.y);
            shapeRenderer.end();
        }

        hud.stage.draw();
    }

    public void handleFling(float velocityX, float velocityY) {
        //hud.setSpeed((int)((velocityX / 10f)  * -1f));

        //Horizontal fling detection
        if (velocityX > MIN_VELOCITY && unprojected.y > SpinGame.HEIGHT / 2) {
            //Right
            flingMoveCheck(2, (velocityX / VELOCITY_REDUCTION));
        } else if (velocityX < (MIN_VELOCITY * -1) && unprojected.y < SpinGame.HEIGHT / 2) {
            //Left
            flingMoveCheck(0, (velocityX / VELOCITY_REDUCTION  * -1f));
        } else if (velocityY > MIN_VELOCITY && unprojected.x > SpinGame.WIDTH / 2) {
            //Vertical fling detection
            //Down
            flingMoveCheck(3, (velocityY / VELOCITY_REDUCTION));
        } else if(velocityY < (MIN_VELOCITY * -1) && unprojected.x < SpinGame.WIDTH / 2 ) {
            //Up
            flingMoveCheck(1, (velocityY / VELOCITY_REDUCTION  * -1f));
        }
    }

    private void flingMoveCheck(int move, float velocity) {
        //Moves: 0 - Left, 1 - Up , 2 - Right, 3 - Down

        Gdx.app.log("BEFORE", "REQUIRED " + requiredMove);
        Gdx.app.log("BEFORE","MOVE " + move);

        if(move == requiredMove) {
            disk.addAngularVelocity(velocity);
            generator.increaseDifficulty();

            //Relocate the move arrow indicator

            //Loop Move rotation
            if(requiredMove == 3)
                requiredMove = 0;
            else
                requiredMove++;

            if (requiredMove == 0) {
                moveArrowLocation.x = SpinGame.HALF_WIDTH + (arrowAnimationWidth / 2);
                moveArrowLocation.y = SpinGame.QUARTER_HEIGHT - (arrowAnimationHeight / 2) - Hud.SCORE_OFFSET;
                arrowAnimationScaleX = -1;
                arrowAnimationScaleY = 1;
                arrowAnimationRotation = 0;
            } else if (requiredMove == 1)  {
                moveArrowLocation.x = SpinGame.QUARTER_WIDTH + (arrowAnimationHeight / 2) - ARROW_SIDE_OFFSET;
                moveArrowLocation.y = SpinGame.HALF_HEIGHT - (arrowAnimationWidth / 2) - Hud.SCORE_OFFSET;
                arrowAnimationScaleX = 1;
                arrowAnimationScaleY = 1;
                arrowAnimationRotation = 90;
            } else if (requiredMove == 2) {
                moveArrowLocation.x = SpinGame.HALF_WIDTH - (arrowAnimationWidth / 2);
                moveArrowLocation.y = (SpinGame.QUARTER_HEIGHT * 3) - (arrowAnimationHeight / 2)  - Hud.SCORE_OFFSET;
                arrowAnimationScaleX = 1;
                arrowAnimationScaleY = 1;
                arrowAnimationRotation = 0;
            } else if (requiredMove == 3) {
                moveArrowLocation.x = (SpinGame.QUARTER_WIDTH * 3) - (arrowAnimationHeight / 2) + ARROW_SIDE_OFFSET;
                moveArrowLocation.y = SpinGame.HALF_HEIGHT + (arrowAnimationWidth / 2)  - Hud.SCORE_OFFSET;

                arrowAnimationScaleX = 1;
                arrowAnimationScaleY = 1;
                arrowAnimationRotation = 270;

            }

            Gdx.app.log("AFTER", "REQUIRED " + requiredMove);
        }
    }

    public void handlePanRelease(Vector3 lastPointerPos) {
        unprojected = gameCam.unproject(lastPointerPos);
        Circle finger = new Circle(unprojected.x, unprojected.y, 20);
        Rectangle barBounds = generator.getBar().getBounds();

        if(Intersector.overlaps(finger, barBounds)&& !collision) {
            collision = true;
            disk.stop();
            collisionPos = unprojected.cpy();
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

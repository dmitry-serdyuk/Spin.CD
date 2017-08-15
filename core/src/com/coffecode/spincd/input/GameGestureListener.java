package com.coffecode.spincd.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.coffecode.spincd.screens.PlayScreen;

/**
 * Created by Laptop on 5/10/2016.
 */
public class GameGestureListener implements GestureDetector.GestureListener {

    private PlayScreen screen;
    private Vector3 pointerLoc;
    private  Vector3 pan;

    public GameGestureListener(PlayScreen screen) {
        this.screen = screen;
        pointerLoc = new Vector3();
        pan = new Vector3();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Gdx.app.log("GESTURES", "TOUCHDOWN: X = " + x + " Y = " + y);
        //pointerLoc.x = x;
        //pointerLoc.y = y;

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        //screen.handleInput();
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        Gdx.app.log("GESTURES", "FLING: VX = " + velocityX + " VY = " + velocityY);
        screen.handleFling(velocityX, velocityY);
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        pointerLoc.x = x;
        pointerLoc.y = y;
        screen.handlePanRelease(pointerLoc);
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}

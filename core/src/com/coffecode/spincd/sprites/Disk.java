package com.coffecode.spincd.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.coffecode.spincd.scenes.Hud;

/**
 * Created by Laptop on 3/10/2016.
 */
public class Disk {
    private Texture texture;
    private Sprite sprite;
    private Vector2 position;
    private float angularVelocity;
    private final float FRICTION = 1.5f;

    public Disk(Vector2 position) {
        this.position = position;
        texture = new Texture("disk.png");
        sprite = new Sprite(texture);

        sprite.setX(position.x - (texture.getWidth() / 2));
        sprite.setY(position.y - (texture.getHeight() / 2) - Hud.SCORE_OFFSET);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getCenterX() {
        return sprite.getX();
    }

    public float getCenterY() {
        return sprite.getY();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public void addAngularVelocity(float amount) {
        angularVelocity -= amount;
    }

    public void slow(float delta) {
        if (angularVelocity < 0)
            angularVelocity += (FRICTION * delta);

        if (angularVelocity > 0)
            angularVelocity = 0;
    }

    public void stop() {
        angularVelocity = 0;

        Gdx.app.log("COLLISION", "COLLISION OCCURED");
    }
}

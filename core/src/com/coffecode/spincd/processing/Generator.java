package com.coffecode.spincd.processing;

import com.coffecode.spincd.SpinGame;
import com.coffecode.spincd.processing.enums.Direction;
import com.coffecode.spincd.processing.enums.Type;
import com.coffecode.spincd.sprites.Bar;

import java.util.Random;

/**
 * Created by Laptop on 17/05/2017.
 */
public class Generator {

    private Bar[] bar;
    private float barSpeed = 1;
    private int combo;
    private int difficultyLevel;
    private Random random = new Random(SpinGame.SEED);
    private Type type; //0 = horizontal, 1 = vertical
    private Direction direction; //0 = positive, 1 = negative

    public Generator() {
        bar = new Bar[2];
        bar[0] =  new Bar();
        combo = 0;
        difficultyLevel = 1;

        generateType();
        generateDirection();
        bar[0].initialise(type, direction);
    }

    public Bar getBar() {
        return bar[0];
    }

    public void step() {

        if(combo > 10) {
            if (!bar[0].checkRunning()) {
                generateType();
                generateDirection();
                bar[0].initialise(type, direction);
            }

            bar[0].move(barSpeed);
        }

    }

    private void generateDirection() {
        direction = Direction.values()[random.nextInt(2)];
    }

    private void generateType() {
        type = Type.values()[random.nextInt(2)];
    }

    public void increaseDifficulty() {
        barSpeed += 0.2f;
        combo++;

        if (combo == 30)
            difficultyLevel++;
        else if (combo == 30)
            difficultyLevel++;
        else if (difficultyLevel == 50)
            difficultyLevel++;
    }

}

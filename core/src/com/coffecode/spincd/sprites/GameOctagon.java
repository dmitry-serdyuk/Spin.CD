package com.coffecode.spincd.sprites;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Laptop on 9/11/2016.
 */
public class GameOctagon {
    private ArrayList<OctaPart> lines;
    public static final int NUM_SIDES = 8;

    public GameOctagon() {
        lines = new ArrayList<OctaPart>();
        createLines();
    }

    private void createLines() {
        //lines.add(new OctaPart(new Vector2(140, 200), new Vector2(340, 200)));

    }


}

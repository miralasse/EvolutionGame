package com.evolution.game.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public abstract class GamePoint {
    Texture texture;
    Vector2 position;
    Vector2 velocity;
    float scale;

    public Vector2 getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
    }

    public boolean checkCollision(GamePoint another) {
        return this.position.dst(another.position) < (this.getScale() * 32.0f + another.getScale() * 32.0f) * 0.8f;
    }
}

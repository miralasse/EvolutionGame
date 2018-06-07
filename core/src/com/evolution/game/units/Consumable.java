package com.evolution.game.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.evolution.game.GameScreen;
import com.evolution.game.Rules;

public class Consumable extends GamePoint {

    public enum Type {
        FOOD("Food"),
        BAD_FOOD("BadFood");

        private String textureName;

        Type(String textureName) {
            this.textureName = textureName;
        }
    }

    private Type type;

    public Type getType() {
        return type;
    }

    public Consumable(GameScreen gs, Type type) {
        this.gs = gs;
        this.texture = gs.getAtlas().findRegion(type.textureName);
        this.position = new Vector2(MathUtils.random(0, Rules.WORLD_WIDTH), MathUtils.random(0, Rules.WORLD_HEIGHT));
        this.velocity = new Vector2(MathUtils.random(-30.0f, 30.0f), MathUtils.random(-30.0f, 30.0f));
        this.type = type;
    }

    public void consumed() {
        active = false;
    }

    public void init() {
        position.set(MathUtils.random(0, Rules.WORLD_WIDTH), MathUtils.random(0, Rules.WORLD_HEIGHT));
        active = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < -32) {
            position.x = 1312;
        }
        if (position.y < -32) {
            position.y = 752;
        }
        if (position.x > 1312) {
            position.x = -32;
        }
        if (position.y > 752) {
            position.y = -32;
        }
    }
}

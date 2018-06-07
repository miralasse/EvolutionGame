package com.evolution.game.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.evolution.game.Rules;

public class Consumable extends GamePoint {

    public enum Type {
        FOOD("core/assets/Food.png"),
        BAD_FOOD("core/assets/BadFood.png");

        private String textureName;

        Type(String textureName) {
            this.textureName = textureName;
        }
    }

    private Type type;

    public Type getType() {
        return type;
    }

    public Consumable(Type type) {
        this.texture = new Texture(type.textureName);
        this.position = new Vector2(MathUtils.random(0, Rules.WORLD_WIDTH), MathUtils.random(0, Rules.WORLD_HEIGHT));
        this.velocity = new Vector2(MathUtils.random(-30.0f, 30.0f), MathUtils.random(-30.0f, 30.0f));
        this.type = type;
    }

    public void recreate() {
        this.position.set(MathUtils.random(0, Rules.WORLD_WIDTH), MathUtils.random(0, Rules.WORLD_HEIGHT));
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

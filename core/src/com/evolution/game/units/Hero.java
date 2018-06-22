package com.evolution.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.evolution.game.Assets;
import com.evolution.game.GameScreen;
import com.evolution.game.Rules;

public class Hero extends Cell {
    private TextureRegion[] regions;
    private float animationTimer;
    private float timePerFrame;
    private float nextLevelScale;

    public Hero(GameScreen gs) {
        super(640.0f, 360.0f, 300.0f);
        this.gs = gs;
        this.regions = new TextureRegion(Assets.getInstance().getAtlas().findRegion("Char")).split(64,64)[0];
//        this.texture = this.regions[0];
        this.timePerFrame = 0.1f;
        this.nextLevelScale = 3.0f;
    }

    public float getNextLevelScale() {
        return nextLevelScale;
    }

    public void setNextLevelScale(float nextLevelScale) {
        this.nextLevelScale = nextLevelScale;
    }

    @Override
    public void consumed() {
        position.set(MathUtils.random(0, Rules.WORLD_WIDTH), MathUtils.random(0, Rules.WORLD_HEIGHT));
        scale = 1.0f;
    }

    public void update(float dt) {
        super.update(dt);
        animationTimer += dt;

        if (Gdx.input.isTouched()) {
            tmp.set(Gdx.input.getX(), Gdx.input.getY());
            gs.getViewport().unproject(tmp);
            float angleToTarget = tmp.sub(position).angle();
            if (angle > angleToTarget) {
                if (Math.abs(angle - angleToTarget) <= 180.0f) {
                    angle -= 180.0f * dt;
                } else {
                    angle += 180.0f * dt;
                }
            }
            if (angle < angleToTarget) {
                if (Math.abs(angle - angleToTarget) <= 180.0f) {
                    angle += 180.0f * dt;
                } else {
                    angle -= 180.0f * dt;
                }
            }
            velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt, acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        int currentFrame = (int)(animationTimer / timePerFrame) % regions.length;
        batch.draw(regions[currentFrame], position.x - 32, position.y - 32, 32, 32, 64, 64, scale, scale, angle);
    }
}
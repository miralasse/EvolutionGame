package com.evolution.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.evolution.game.Assets;
import com.evolution.game.GameScreen;
import com.evolution.game.Rules;

public class Hero extends Cell {
    private transient TextureRegion[] regions;
    private float animationTimer;
    private float timePerFrame;
    private StringBuilder scoreString;
    private StringBuilder levelString;
    private int score;
    private int showedScore;
    private int lifeCount;
    private StringBuilder lifeString;

    public Hero(GameScreen gs) {
        super(640.0f, 360.0f, 300.0f);
        this.gs = gs;
        this.regions = new TextureRegion(Assets.getInstance().getAtlas().findRegion("Char")).split(64, 64)[0];
        this.timePerFrame = 0.1f;
        this.scale = 1.0f;
        this.lifeCount = 5;
        this.scoreString = new StringBuilder(200);
        this.levelString = new StringBuilder(200);
        this.lifeString = new StringBuilder(200);
    }

    public void reloadResources(GameScreen gs) {
        this.gs = gs;
        this.regions = new TextureRegion(Assets.getInstance().getAtlas().findRegion("Char")).split(64, 64)[0];
    }

    public void addScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }

    public int getLifeCount() {
        return lifeCount;
    }

    @Override
    public void consumed() {
        position.set(MathUtils.random(0, Rules.WORLD_WIDTH), MathUtils.random(0, Rules.WORLD_HEIGHT));
        scale = 1.0f;
        lifeCount--;
    }

    @Override
    public void eatConsumable(Consumable.Type type) {
        super.eatConsumable(type);
        switch (type) {
            case FOOD:
                score += 100;
                break;
        }
    }

    public void update(float dt) {
        super.update(dt);
        animationTimer += dt;
        if (showedScore < score) {
            int delta = (int) ((score - showedScore) * 0.02f);
            if (delta < 4) {
                delta = 4;
            }
            showedScore += delta;
            if (showedScore > score) {
                showedScore = score;
            }
        }
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

        gs.getParticleEmitter().setup(position.x, position.y, MathUtils.random(-10, 10), MathUtils.random(-10, 10), 10.2f, 4, 4f, 0, 1, 0, 0.2f, 0, 1, 0, 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        int currentFrame = (int) (animationTimer / timePerFrame) % regions.length;
        batch.draw(regions[currentFrame], position.x - 32, position.y - 32, 32, 32, 64, 64, scale, scale, angle);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        lifeString.setLength(0);
        lifeString.append("Lives: ").append(lifeCount);
        font.draw(batch, lifeString, 20, 700);

        levelString.setLength(0);
        levelString.append("Level: ").append(gs.getLevel());
        font.draw(batch, levelString, 20, 660);

        scoreString.setLength(0);
        scoreString.append("Score: ").append(showedScore);
        font.draw(batch, scoreString, 20, 620);
    }
}
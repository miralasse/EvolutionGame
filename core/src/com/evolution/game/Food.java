package com.evolution.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Food {
    private Texture texture;
    private Vector2 position;
    private Rectangle eatingArea;
    private boolean isActive;

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getEatingArea() {
        return eatingArea;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Food() {
        this.texture = new Texture("core/assets/Food.png");
        this.position = new Vector2(MathUtils.random(0, 1280-64), MathUtils.random(0, 720-64));
        this.eatingArea = new Rectangle(position.x, position.y, 64, 64);
        this.isActive = true;
    }

    public void render(SpriteBatch batch){
        if (isActive) {
            batch.draw(texture, position.x, position.y);
        }
    }

    public void recreate(){
        position.set(MathUtils.random(0, 1280-64), MathUtils.random(0, 720-64));
        eatingArea.setPosition(position);
        isActive = true;
    }
}

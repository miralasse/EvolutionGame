package com.evolution.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Hero {
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float acceleration;
    private float scale;

    private Vector2 mousePosition;
    private Vector2 tmp;

    public Hero() {
        this.texture = new Texture("core/assets/Char.png");
        this.position = new Vector2(640.0f, 360.0f);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.mousePosition = new Vector2(0.0f, 0.0f);
        this.tmp = new Vector2(0.0f, 0.0f);
        this.angle = 0.0f;
        this.acceleration = 300.0f;
        this.scale = 1.0f;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void grow(){
        scale += 0.02;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, scale, scale, angle, 0, 0, 64, 64, false, false);
    }

    public void update(float dt) {
        if (Gdx.input.isTouched()) {
            mousePosition.set(Gdx.input.getX(), 720 - Gdx.input.getY());
            tmp.set(mousePosition);
            float angleToMouse = tmp.sub(position).angle();
            if (angle > angleToMouse) {
                if (Math.abs(angle - angleToMouse) <= 180.0f) {
                    angle -= 180.0f * dt;
                } else {
                    angle += 180.0f * dt;
                }
            }
            if (angle < angleToMouse) {
                if (Math.abs(angle - angleToMouse) <= 180.0f) {
                    angle += 180.0f * dt;
                } else {
                    angle -= 180.0f * dt;
                }
            }
            if (angle < 0.0f) {
                angle += 360.0f;
            }
            if (angle > 360.0f) {
                angle -= 360.0f;
            }
            velocity.add(acceleration * (float)Math.cos(Math.toRadians(angle)) * dt, acceleration * (float)Math.sin(Math.toRadians(angle)) * dt);
        }
        velocity.scl(0.98f);
        position.mulAdd(velocity, dt);
    }
}
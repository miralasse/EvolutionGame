package com.evolution.game.units;

import com.badlogic.gdx.math.MathUtils;
import com.evolution.game.GameScreen;
import com.evolution.game.Rules;

public class Enemy extends Cell {

    public Enemy(GameScreen gs) {
        super(MathUtils.random(0, Rules.WORLD_WIDTH), MathUtils.random(0, Rules.WORLD_HEIGHT), 100.0f);
        this.gs = gs;
        this.texture = gs.getAtlas().findRegion("Enemy");
        this.active = false;
    }

    @Override
    public void consumed() {
        active = false;
    }

    public void init() {
        position.set(MathUtils.random(0, Rules.WORLD_WIDTH), MathUtils.random(0, Rules.WORLD_HEIGHT));
        scale = 1.0f + MathUtils.random(0.0f, 0.4f);
        active = true;
    }

    public void update(float dt) {
        Cell hero = gs.getHero();

        super.update(dt);

        velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt, acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);
        if (position.x > Rules.WORLD_WIDTH) {
            position.x = 0.0f;
        }

        // <----- Мозги прописывать сюда

        tmp.set(position);
        float minDist = 10000.0f;
        for (int i = 0; i < gs.getConsumableEmitter().getActiveList().size(); i++) {
            if (gs.getConsumableEmitter().getActiveList().get(i).getType() == Consumable.Type.FOOD) {
                float distance = position.dst(gs.getConsumableEmitter().getActiveList().get(i).getPosition());
                if (distance < minDist) {
                    minDist = distance;
                    tmp.set(gs.getConsumableEmitter().getActiveList().get(i).getPosition());
                }
            }
        }

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
    }
}

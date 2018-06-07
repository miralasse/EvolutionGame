package com.evolution.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evolution.game.units.Consumable;

public class ConsumableEmitter extends ObjectPool<Consumable> {
    private GameScreen gs;
    private float time;

    public ConsumableEmitter(GameScreen gs) {
        this.gs = gs;
        this.addObjectsToFreeList(20);
    }

    @Override
    protected Consumable newObject() {
        return new Consumable(gs, Consumable.Type.values()[MathUtils.random(0, Consumable.Type.values().length - 1)]);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        time += dt;
        if (time >= 0.5f) {
            time = 0.0f;
            getActiveElement().init();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}

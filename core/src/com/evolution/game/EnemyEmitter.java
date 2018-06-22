package com.evolution.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evolution.game.units.Enemy;

public class EnemyEmitter extends ObjectPool<Enemy> {
    private GameScreen gs;
    private float time;
    private float initialScale;


    public void setInitialScale(float initialScale) {
        this.initialScale = initialScale;
    }

    @Override
    protected Enemy newObject() {
        return new Enemy(gs);
    }

    public EnemyEmitter(GameScreen gs) {
        this.gs = gs;
        this.addObjectsToFreeList(20);
        this.initialScale = 1.0f;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        time += dt;
        if (time >= 3.0f) {
            time = 0.0f;
            getActiveElement().init(initialScale);
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}

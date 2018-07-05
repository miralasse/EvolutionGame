package com.evolution.game.units;

import com.badlogic.gdx.math.MathUtils;

import com.evolution.game.Assets;
import com.evolution.game.GameScreen;
import com.evolution.game.Rules;

import java.util.ArrayList;

public class Enemy extends Cell {
    private GamePoint nearestFood;
    private GamePoint nearestDanger;

    public Enemy(GameScreen gs) {
        super(0, 0, 100.0f);
        this.gs = gs;
        this.texture = Assets.getInstance().getAtlas().findRegion("Enemy");
        this.active = false;
    }

    public void reloadResources(GameScreen gs) {
        this.gs = gs;
        this.texture = Assets.getInstance().getAtlas().findRegion("Enemy");
    }

    @Override
    public void consumed() {
        active = false;
    }

    public void init() {
        position.set(MathUtils.random(0, Rules.GLOBAL_WIDTH), MathUtils.random(0, Rules.GLOBAL_HEIGHT));
        scale = 1.0f + MathUtils.random(0.0f, 0.4f);
        active = true;
    }

    public void update(float dt) {
        super.update(dt);

        if (scale < 0.2f) {
            active = false;
        }

        if (position.x < 0) {
            position.x = Rules.GLOBAL_WIDTH;
        }
        if (position.y < 0) {
            position.y = Rules.GLOBAL_HEIGHT;
        }
        if (position.x > Rules.GLOBAL_WIDTH) {
            position.x = 0;
        }
        if (position.y > Rules.GLOBAL_HEIGHT) {
            position.y = 0;
        }

        // Мозги
        findNearestDanger();
        findNearestFood();
        if (this.position.dst(nearestFood.getPosition()) < this.position.dst(nearestDanger.getPosition())) {
            moveToTarget(nearestFood.getPosition().x, nearestFood.getPosition().y, dt);
        } else {
            moveFromDanger(nearestDanger.getPosition().x, nearestDanger.getPosition().y, dt);
        }
    }

    //Метод для поиска ближайшей еды.
    //Едой считаем желтую еду и персонажей, включая игрока, которые меньше текущего персонажа.
    private void findNearestFood(){
        ArrayList<GamePoint> foods = new ArrayList<>();
        for (int i = 0; i < gs.getConsumableEmitter().getActiveList().size(); i++) {
            if (gs.getConsumableEmitter().getActiveList().get(i).getType() == Consumable.Type.FOOD){
                foods.add(gs.getConsumableEmitter().getActiveList().get(i));
            }
        }
        for (int i = 0; i < gs.getEnemyEmitter().getActiveList().size(); i++) {
            Enemy enemy = gs.getEnemyEmitter().getActiveList().get(i);
            if (!enemy.equals(this) && enemy.getScale() < this.getScale()){
                foods.add(gs.getEnemyEmitter().getActiveList().get(i));
            }
        }
        if (gs.getHero().getScale() < this.getScale()) {
            foods.add(gs.getHero());
        }
        nearestFood = foods.get(0);
        for (int i = 0; i < foods.size(); i++) {
            if (this.position.dst(foods.get(i).getPosition()) < this.position.dst(nearestFood.getPosition())) {
                nearestFood = foods.get(i);
            }
        }
    }

    //Метод для поиска ближайшей опасности.
    //Опасностью считаем красную еду и персонажей, которые больше текущего персонажа.
    private void findNearestDanger(){
        ArrayList<GamePoint> dangers = new ArrayList<>(); // вынести ArrayList в поле класса и чистить вместо создания нового
        for (int i = 0; i < gs.getConsumableEmitter().getActiveList().size(); i++) {
            if (gs.getConsumableEmitter().getActiveList().get(i).getType() == Consumable.Type.BAD_FOOD){
                dangers.add(gs.getConsumableEmitter().getActiveList().get(i));
            }
        }
        for (int i = 0; i < gs.getEnemyEmitter().getActiveList().size(); i++) {
            Enemy enemy = gs.getEnemyEmitter().getActiveList().get(i);
            if (!enemy.equals(this) && enemy.getScale() > this.getScale()){
                dangers.add(gs.getEnemyEmitter().getActiveList().get(i));
            }
        }
        if (gs.getHero().getScale() > this.getScale()) {
            dangers.add(gs.getHero());
        }
        nearestDanger = dangers.get(0);
        for (int i = 0; i < dangers.size(); i++) {
            if (this.position.dst(dangers.get(i).getPosition()) < this.position.dst(nearestDanger.getPosition())) {
                nearestDanger = dangers.get(i);
            }
        }
    }

    //Метод для движения к цели
    private void moveToTarget (float targetX, float targetY, float dt){
        tmp.set(targetX, targetY);
        float angleToTarget = tmp.sub(position).angle();

        if (angle > angleToTarget) {
            if (Math.abs(angle - angleToTarget) <= 180.0f) {
                angle -= 180.0f * dt;    //если угол больше угла цели, уменьшаем свой угол - преследуем
            } else {
                angle += 180.0f * dt;
            }
        }
        if (angle < angleToTarget) {
            if (Math.abs(angle - angleToTarget) <= 180.0f) {
                angle += 180.0f * dt;    //если угол меньше угла цели, увеличиваем свой угол - преследуем
            } else {
                angle -= 180.0f * dt;
            }
        }
        velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt, acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);
    }

    //Метод для убегания от опасности (разворот и движение в противоложном направлении)
    private void moveFromDanger(float targetX, float targetY, float dt){
        tmp.set(targetX, targetY);
        float angleToDanger = tmp.sub(position).angle();

        if (angle > angleToDanger) {
            if (Math.abs(angle - angleToDanger) <= 180.0f) {
                angle += 180.0f * dt;    //если угол больше угла опасности, увеличиваем свой угол - убегаем
            } else {
                angle -= 180.0f * dt;
            }
        }
        if (angle < angleToDanger) {
            if (Math.abs(angle - angleToDanger) <= 180.0f) {
                angle -= 180.0f * dt;    //если угол меньше угла опасности, уменьшаем свой угол - убегаем
            } else {
                angle += 180.0f * dt;
            }
        }
        velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt, acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);
    }

}

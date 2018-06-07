package com.evolution.game.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.evolution.game.Rules;

import java.util.ArrayList;

/* Идея поведения ботов:
        Бот анализирует элементы игры (листья и других персонажей).
        Если бот видит еду (желтые листья и тех персонажей, которые меньше него), то он преследует ближайшую еду.
        Если бот видит опасность (красные листья и тех персонажей, которые больше него), то он убегает от ближайшей опасности.
        При этом принимается решение:
        если еда ближе, чем опасность, то бот движется к еде, а если опасность ближе, чем еда, то убегает от опасности.
        */

public class Enemy extends Cell {

    private Cell[] units; // ссылка на массив всех игроков
    private Consumable[] consumables; // ссылка на еду(желтые/красные листья)
    private GamePoint nearestFood;
    private GamePoint nearestDanger;

    public Enemy(Cell[] units, Consumable[] consumables) {
        super(MathUtils.random(0, Rules.WORLD_WIDTH), MathUtils.random(0, Rules.WORLD_HEIGHT), 100.0f);
        this.units = units;
        this.consumables = consumables;
        this.texture = new Texture("core/assets/Enemy.png");
    }

    @Override
    public void consumed() {
        position.set(MathUtils.random(0, Rules.WORLD_WIDTH), MathUtils.random(0, Rules.WORLD_HEIGHT));
        scale = 1.0f;
    }

    public void update(float dt) {
        super.update(dt);
        velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt, acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);

        if (position.x > Rules.WORLD_WIDTH) {
            position.x = 0.0f;
        }

        if (position.x < 0) {
            position.x = Rules.WORLD_WIDTH;
        }

        if (position.y > Rules.WORLD_HEIGHT) {
            position.y = 0.0f;
        }

        if (position.y < 0) {
            position.y = Rules.WORLD_HEIGHT;
        }

        findNearestDanger();
        findNearestFood();
        if (this.position.dst(nearestFood.getPosition()) < this.position.dst(nearestDanger.getPosition())) {
            moveToTarget(nearestFood.getPosition().x, nearestFood.getPosition().y, dt);
        } else {
            moveFromDanger(nearestDanger.getPosition().x, nearestDanger.getPosition().y, dt);
        }
    }

    /*
    Метод для поиска ближайшей еды.
    Едой считаем желтую еду и персонажей, включая игрока, которые меньше текущего персонажа.
     */
    private void findNearestFood(){
        ArrayList<GamePoint> foods = new ArrayList<>();
        for (int i = 0; i < consumables.length; i++) {
            if (consumables[i].getType() == Consumable.Type.FOOD){
                foods.add(consumables[i]);
            }
        }
        for (int i = 0; i < units.length; i++) {
            if (units[i].getScale() < this.scale){
                foods.add(units[i]);
            }
        }
        nearestFood = foods.get(0);
        for (int i = 0; i < foods.size(); i++) {
            if (this.position.dst(foods.get(i).getPosition()) < this.position.dst(nearestFood.getPosition())) {
                nearestFood = foods.get(i);

            }
        }
    }

    /*
    Метод для поиска ближайшей опасности.
    Опасностью считаем красную еду и персонажей, которые больше текущего персонажа.
     */
    private void findNearestDanger(){
        ArrayList<GamePoint> dangers = new ArrayList<>();
        for (int i = 0; i < consumables.length; i++) {
            if (consumables[i].getType() == Consumable.Type.BAD_FOOD){
                dangers.add(consumables[i]);
            }
        }
        for (int i = 0; i < units.length; i++) {
            if (units[i].getScale() > this.scale){
                dangers.add(units[i]);
            }
        }
        nearestDanger = dangers.get(0);
        for (int i = 0; i < dangers.size(); i++) {
            if (this.position.dst(dangers.get(i).getPosition()) < this.position.dst(nearestDanger.getPosition())) {
                nearestDanger = dangers.get(i);
            }
        }
    }

    /*
    Метод для движения к цели.
      */
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

    /*
    Метод для убегания от опасности (разворот и движение в противоложном направлении).
      */
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

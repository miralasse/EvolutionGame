package com.evolution.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class EvolutionGame extends ApplicationAdapter {
	SpriteBatch batch;
	Hero hero;
	List<Food> foods;
	public static final int FOOD_COUNT = 10;

	@Override
	public void create () {
		batch = new SpriteBatch();
		hero = new Hero();
		foods = new ArrayList<>(FOOD_COUNT);
		for (int i = 0; i < FOOD_COUNT; i++){
			foods.add(new Food());
		}
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		update(dt);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		hero.render(batch);
		for (int i = 0; i < foods.size(); i++){
			foods.get(i).render(batch);
		}
		batch.end();
	}

	public void checkIntersection(){
		for (int i = 0; i < foods.size(); i++) {
			if (foods.get(i).getEatingArea().contains(hero.getPosition().x + 32, hero.getPosition().y + 32)) {
				foods.get(i).setActive(false);
				foods.get(i).recreate();
				hero.grow();
			}
		}
	}

	public void update(float dt) {
		hero.update(dt);
		checkIntersection();
	}

	
	@Override
	public void dispose () {
		batch.dispose();
	}
}

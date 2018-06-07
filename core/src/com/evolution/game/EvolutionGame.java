package com.evolution.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.evolution.game.units.Cell;
import com.evolution.game.units.Consumable;
import com.evolution.game.units.Enemy;
import com.evolution.game.units.Hero;

import java.util.ArrayList;
import java.util.List;

public class EvolutionGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private BitmapFont font;
	private Consumable[] consumables;
	private Cell[] units;

	@Override
	public void create() {
		batch = new SpriteBatch();
		units = new Cell[3];
		consumables = new Consumable[20];

		units[0] = new Hero();
		for (int i = 1; i < units.length; i++) {
			units[i] = new Enemy(units, consumables);
		}
		for (int i = 0; i < consumables.length; i++) {
			consumables[i] = new Consumable(Consumable.Type.values()[MathUtils.random(0, Consumable.Type.values().length - 1)]);
		}

		generateFont();
	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		update(dt);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for (int i = 0; i < consumables.length; i++) {
			consumables[i].render(batch);
		}
		for (int i = 0; i < units.length; i++) {
			units[i].render(batch);
		}
		font.draw(batch, "Score: -", 20, 700);
		batch.end();
	}

	public void update(float dt) {
		for (int i = 0; i < units.length; i++) {
			units[i].update(dt);
		}
		for (int i = 0; i < consumables.length; i++) {
			consumables[i].update(dt);
		}
		checkCollisions();
	}

	public void checkCollisions() {
		// Проверка столкновений персонажей и еды
		for (int i = 0; i < units.length; i++) {
			for (int j = 0; j < consumables.length; j++) {
				if (units[i].getPosition().dst(consumables[j].getPosition()) < 30) {
					units[i].eatConsumable(consumables[j].getType());
					consumables[j].recreate();
				}
			}
		}
		// Проверка столкновений персонажей между собой
		for (int i = 0; i < units.length - 1; i++) {
			for (int j = i + 1; j < units.length; j++) {
				if (units[i].checkCollision(units[j])) {
					if (units[i].getScale() > units[j].getScale()) {
						units[i].grow();
						units[j].consumed();
					} else {
						units[i].consumed();
						units[j].grow();
					}
				}
			}
		}
	}

	public void generateFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/gomarice.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = 48;
		fontParameter.color = Color.WHITE;
		fontParameter.borderWidth = 2;
		fontParameter.borderColor = Color.BLACK;
		fontParameter.shadowOffsetX = 2;
		fontParameter.shadowOffsetY = 2;
		fontParameter.shadowColor = Color.GRAY;
		font = generator.generateFont(fontParameter);
		generator.dispose();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}

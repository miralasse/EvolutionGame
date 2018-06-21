package com.evolution.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.evolution.game.units.Cell;
import com.evolution.game.units.Consumable;
import com.evolution.game.units.Enemy;
import com.evolution.game.units.Hero;

public class EvolutionGame extends Game {
	private SpriteBatch batch;
	private GameScreen gameScreen;

	// Домашнее задание:
	// +1. Разбор кода
	// +2. Реализовать паузу на кнопку "P"
	// * 3. Попробуйте сделать мини-карту(зеленая точка - еда, красная - бот, оранжевая - игрок)
	// Если не получится, опишите идею просто на словах
	// ----------------------------------------------------------
	// План:
	// 1. Карта
	// 2. Служебные классы(Система экранов, экраны, интерфейс)
	// 3. Анимация
	// 4. Всякие камни, объекты которые нельзя съесть
	// 5. PowerUps

	@Override
	public void create() {
		batch = new SpriteBatch();
		gameScreen = new GameScreen(batch);
		Assets.getInstance().loadAssets();
		setScreen(gameScreen);
	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		getScreen().render(dt);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}

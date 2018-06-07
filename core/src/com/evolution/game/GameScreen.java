package com.evolution.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.evolution.game.units.Cell;
import com.evolution.game.units.Consumable;
import com.evolution.game.units.Hero;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private BitmapFont font;
    private EnemyEmitter enemyEmitter;
    private ConsumableEmitter consumableEmitter;
    private Hero hero;
    private List<Cell> cellCollisionList;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public ConsumableEmitter getConsumableEmitter() {
        return consumableEmitter;
    }

    public EnemyEmitter getEnemyEmitter() {
        return enemyEmitter;
    }

    public Hero getHero() {
        return hero;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("core/assets/game.pack");
        hero = new Hero(this);
        enemyEmitter = new EnemyEmitter(this);
        consumableEmitter = new ConsumableEmitter(this);
        cellCollisionList = new ArrayList<Cell>();
        generateFont();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        hero.render(batch);
        consumableEmitter.render(batch);
        enemyEmitter.render(batch);
        font.draw(batch, "Score: -", 20, 700);
        batch.end();
    }

    public void checkCollisions() {
        // Проверка столкновений персонажей и еды
        cellCollisionList.clear();
        cellCollisionList.add(hero);
        for (int i = 0; i < enemyEmitter.getActiveList().size(); i++) {
            cellCollisionList.add(enemyEmitter.getActiveList().get(i));
        }
        for (int i = 0; i < cellCollisionList.size(); i++) {
            for (int j = 0; j < consumableEmitter.getActiveList().size(); j++) {
                if (cellCollisionList.get(i).getPosition().dst(consumableEmitter.getActiveList().get(j).getPosition()) < 30) {
                    cellCollisionList.get(i).eatConsumable(consumableEmitter.getActiveList().get(j).getType());
                    consumableEmitter.getActiveList().get(j).consumed();
                }
            }
        }
        // Проверка столкновений персонажей между собой
        for (int i = 0; i < cellCollisionList.size() - 1; i++) {
            for (int j = i + 1; j < cellCollisionList.size(); j++) {
                if (cellCollisionList.get(i).checkCollision(cellCollisionList.get(j))) {
                    if (cellCollisionList.get(i).getScale() > cellCollisionList.get(j).getScale()) {
                        cellCollisionList.get(i).grow();
                        cellCollisionList.get(j).consumed();
                    } else {
                        cellCollisionList.get(i).consumed();
                        cellCollisionList.get(j).grow();
                    }
                }
            }
        }
    }

    public void update(float dt) {
        hero.update(dt);
        consumableEmitter.update(dt);
        enemyEmitter.update(dt);
        checkCollisions();
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
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

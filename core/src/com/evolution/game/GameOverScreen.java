package com.evolution.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameOverScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font48;
    private BitmapFont font96;
    private Stage stage;
    private Skin skin;

    private int finalScore;
    private int finalLevel;

    public GameOverScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public void setFinalLevel(int finalLevel) {
        this.finalLevel = finalLevel;
    }

    @Override
    public void show() {
        font48 = Assets.getInstance().getAssetManager().get("core/assets/gomarice48.ttf", BitmapFont.class);
        font96 = Assets.getInstance().getAssetManager().get("core/assets/gomarice96.ttf", BitmapFont.class);
        createGUI();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font96.draw(batch, "Game Over", 0, 660, 1280, 1, false);
        font48.draw(batch, "Score: " + finalScore, 0, 500, 1280, 1, false);
        font48.draw(batch, "Level: " + finalLevel, 0, 400, 1280, 1, false);
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        stage.act(dt);
    }

    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());
        skin.add("font48", font48);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = skin.getFont("font48");
        skin.add("simpleButtonSkin", textButtonStyle);

        Button menuButton = new TextButton("Menu", skin, "simpleButtonSkin");
        menuButton.setPosition(480, 180);
        stage.addActor(menuButton);

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
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
        stage.dispose();
    }
}

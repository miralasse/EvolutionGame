package com.evolution.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.evolution.game.units.Cell;
import com.evolution.game.units.Hero;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font48;
    private BitmapFont font24;
    private ConsumableEmitter consumableEmitter;
    private EnemyEmitter enemyEmitter;
    private Hero hero;
    private List<Cell> cellCollisionList;
    private Viewport viewport;
    private Camera camera;
    private Camera windowCamera;
    private Music music;
    private Sound consumeSound;
    private MiniMap miniMap;
    private boolean paused;
    private int level;

    private Stage stage;
    private Skin skin;
    private Button pauseGameButton;

    public Viewport getViewport() {
        return viewport;
    }

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
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
        level = 1;

        consumableEmitter = new ConsumableEmitter(this);
        hero = new Hero(this);
        enemyEmitter = new EnemyEmitter(this);
        cellCollisionList = new ArrayList<>();
        font48 = Assets.getInstance().getAssetManager().get("core/assets/gomarice48.ttf", BitmapFont.class);
        font24 = Assets.getInstance().getAssetManager().get("core/assets/gomarice24.ttf", BitmapFont.class);

        camera = new OrthographicCamera(1280, 720);
        viewport = new FitViewport(1280, 720, camera);
        miniMap = new MiniMap(this);
        music = Assets.getInstance().getAssetManager().get("core/assets/music.wav", Music.class);
        music.setLooping(true);
        music.setVolume(0.05f);
        music.play();
        consumeSound = Assets.getInstance().getAssetManager().get("core/assets/laser.wav", Sound.class);
        paused = false;
        windowCamera = new OrthographicCamera(1280, 720);
        windowCamera.position.set(640,360,0);
        windowCamera.update();

        createPauseButton();
    }

    public void createPauseButton() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());
        skin.add("font24", font24);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("shortButton");
        textButtonStyle.font = skin.getFont("font24");
        skin.add("shortButtonSkin", textButtonStyle);

        pauseGameButton = new TextButton("Pause", skin, "shortButtonSkin");
        pauseGameButton.setPosition(1180, 620);
        stage.addActor(pauseGameButton);
        pauseGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                paused = !paused;
            }
        });
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        consumableEmitter.render(batch);
        hero.render(batch);
        enemyEmitter.render(batch);
        batch.end();
        batch.setProjectionMatrix(windowCamera.combined);
        batch.begin();
        font48.draw(batch, "Score: -", 20, 700);
        font48.draw(batch, "Level: " +  level, 20, 650);
        miniMap.render(batch);
        batch.end();
        stage.draw();
    }

    public void checkCollisions() {
        // Проверка столкновений персонажей и еды
        cellCollisionList.clear();
        cellCollisionList.add(hero);
        cellCollisionList.addAll(enemyEmitter.getActiveList());
        for (int i = 0; i < cellCollisionList.size(); i++) {
            for (int j = 0; j < consumableEmitter.getActiveList().size(); j++) {
                if (cellCollisionList.get(i).getPosition().dst(consumableEmitter.getActiveList().get(j).getPosition()) < 30) {
                    cellCollisionList.get(i).eatConsumable(consumableEmitter.getActiveList().get(j).getType());
                    consumableEmitter.getActiveList().get(j).consumed();
                    consumeSound.play();
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            paused = !paused;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
        }
        if (!paused) {
            music.play();
            if ( (hero.getNextLevelScale() - hero.getScale()) < 0.01f) {
                loadNewLevel();
            }
            hero.update(dt);
            camera.position.set(hero.getPosition().x - 32, hero.getPosition().y - 32, 0);
            camera.update();
            enemyEmitter.update(dt);
            consumableEmitter.update(dt);
            checkCollisions();
        } else {
            music.stop();

        }
        stage.act(dt);
    }

    public void loadNewLevel(){
        while (level <= 3) {
            level++;
            enemyEmitter.clearPool();
            consumableEmitter.clearPool();
            switch (level) {
                case 2:
                    consumableEmitter.setBadFoodPercentage(15);
                    consumableEmitter.generateConsumable(10);
                    enemyEmitter.setInitialScale(hero.getNextLevelScale());
                    hero.setNextLevelScale(5.0f);
                    break;
                case 3:
                    consumableEmitter.setBadFoodPercentage(30);
                    consumableEmitter.generateConsumable(10);
                    enemyEmitter.setInitialScale(hero.getNextLevelScale());
                    hero.setNextLevelScale(8.0f);
                    break;
            }
            break;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
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
        Assets.getInstance().clear();
    }
}

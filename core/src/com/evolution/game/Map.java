package com.evolution.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Map {
    GameScreen gs;
    TextureRegion texture;
    TextureRegion textureHeroPoint;
    TextureRegion textureEnemyPoint;
    TextureRegion textureFoodPoint;
    Vector2 position;

    public Map(GameScreen gs) {
        this.position = new Vector2(gs.getHero().getPosition().x + 364.0f, gs.getHero().getPosition().y - 340.0f);
        this.gs = gs;
        this.texture = Assets.getInstance().getAtlas().findRegion("mapBack");
        this.textureHeroPoint = Assets.getInstance().getAtlas().findRegion("playerPoint");
        this.textureEnemyPoint = Assets.getInstance().getAtlas().findRegion("enemyPoint");
        this.textureFoodPoint = Assets.getInstance().getAtlas().findRegion("consumablePoint");
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);

        float mapHeroX = this.position.x + (gs.getHero().getPosition().x / Rules.GLOBAL_WIDTH)*256.0f;
        float mapHeroY = this.position.y + (gs.getHero().getPosition().x / Rules.GLOBAL_WIDTH)*144.0f;
        batch.draw(textureHeroPoint, mapHeroX, mapHeroY);

        for (int i = 0; i < gs.getEnemyEmitter().activeList.size(); i++){
            float mapEnemyX = this.position.x + (gs.getEnemyEmitter().activeList.get(i).getPosition().x / Rules.GLOBAL_WIDTH)*256.0f;
            float mapEnemyY = this.position.y + (gs.getEnemyEmitter().activeList.get(i).getPosition().y / Rules.GLOBAL_HEIGHT)*144.0f;
            batch.draw(textureEnemyPoint, mapEnemyX, mapEnemyY);
        }

        for (int i = 0; i < gs.getConsumableEmitter().activeList.size(); i++){
            float mapConsumableX = this.position.x + (gs.getConsumableEmitter().activeList.get(i).getPosition().x / Rules.GLOBAL_WIDTH)*256.0f;
            float mapConsumableY = this.position.y + (gs.getConsumableEmitter().activeList.get(i).getPosition().y / Rules.GLOBAL_HEIGHT)*144.0f;
            batch.draw(textureFoodPoint, mapConsumableX, mapConsumableY);
        }
    }

    public void update(float dt) {
        position.set(gs.getHero().getPosition().x + 364.0f, gs.getHero().getPosition().y - 340.0f);

    }
}

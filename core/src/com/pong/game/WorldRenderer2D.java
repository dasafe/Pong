package com.pong.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pong.game.Door.DoorState;

public class WorldRenderer2D {

    float stateTime;
    Animation<TextureRegion> espaldaAnimation;
    Animation<TextureRegion> caraAnimation;
    private static final float PX_PER_M = 100.0f;
    private World world;
    private TextureAtlas atlas;
    private TextureAtlas pj;
    private TextureRegion espalda;
    private TextureRegion espalda0;
    private TextureRegion cara;
    private TextureRegion cara0;
    private TextureRegion wallImage;
    private TextureRegion exitImage;
    private TextureRegion gemImage;
    private TextureRegion unlockedDoorImage;
    private TextureRegion lockedDoorImage;
    private TextureRegion keyImage;
    private TextureRegion grassImage;
    private OrthographicCamera camera;
    private OrthographicCamera hudCam;
    private SpriteBatch batch;
    private SpriteBatch hudBatch;
    private ShapeRenderer debugShapes;
    private BitmapFont hudFont;

    public WorldRenderer2D(World world) {
        this.world = world;
        atlas = new TextureAtlas(Gdx.files.internal("textures/images.pack"));
        pj = new TextureAtlas(Gdx.files.internal("textures/pj.pack"));
        espalda0 = pj.findRegion("espalda0");
        espaldaAnimation = new Animation<TextureRegion>(6f, pj.findRegions("espalda"), Animation.PlayMode.LOOP);
        cara0 = pj.findRegion("cara0");

        wallImage = atlas.findRegion("wall");
        exitImage = atlas.findRegion("exit");
        gemImage = atlas.findRegion("treasure");
        unlockedDoorImage = atlas.findRegion("unlockedDoor");
        lockedDoorImage = atlas.findRegion("lockedDoor");
        keyImage = atlas.findRegion("key");
        grassImage = atlas.findRegion("grass");
        camera = new OrthographicCamera();
        hudCam = new OrthographicCamera();
        camera.setToOrtho(false, ArenaRoamer.WIDTH, ArenaRoamer.HEIGHT);
        hudCam.setToOrtho(false, ArenaRoamer.WIDTH, ArenaRoamer.HEIGHT);
        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        debugShapes = new ShapeRenderer();
        hudFont = new BitmapFont();
    }

    public void render() {
        stateTime += 1 + Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderPlayArea();
        renderHud();
        if (ArenaRoamer.DEBUG) renderDebug();
    }

    private void renderPlayArea() {
        camera.position.set(world.getPlayer().getCentrePos().x * PX_PER_M, world.getPlayer().getCentrePos().y * PX_PER_M, 0);
        camera.rotate(-world.getPlayer().getRotation());
        camera.update();
        camera.rotate(world.getPlayer().getRotation());
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int i = 0; i < world.getMap().getMap().size; i++) {
            for (int j = 0; j < world.getMap().getMap().get(i).length(); j++)
                batch.draw(grassImage, (j * PX_PER_M) - (0.5f * PX_PER_M), (i * PX_PER_M) - (0.5f * PX_PER_M), 1 * PX_PER_M, 1 * PX_PER_M);
        }
        for (int i = 0; i < world.getWalls().size; i++)
            batch.draw(wallImage, (world.getWalls().get(i).centrePosX * PX_PER_M) - ((world.getWalls().get(i).width * PX_PER_M) / 2), (world.getWalls().get(i).centrePosY * PX_PER_M) - ((world.getWalls().get(i).height * PX_PER_M) / 2), (world.getWalls().get(i).width * PX_PER_M) / 2, (world.getWalls().get(i).height * PX_PER_M) / 2, world.getWalls().get(i).width * PX_PER_M, world.getWalls().get(i).height * PX_PER_M, 1, 1, 0);
        for (int i = 0; i < world.getExits().size; i++)
            batch.draw(exitImage, (world.getExits().get(i).centrePosX * PX_PER_M) - ((world.getExits().get(i).width * PX_PER_M) / 2), (world.getExits().get(i).centrePosY * PX_PER_M) - ((world.getExits().get(i).height * PX_PER_M) / 2), (world.getExits().get(i).width * PX_PER_M) / 2, (world.getExits().get(i).height * PX_PER_M) / 2, world.getExits().get(i).width * PX_PER_M, world.getExits().get(i).height * PX_PER_M, 1, 1, 0);
        for (int i = 0; i < world.getGems().size; i++)
            batch.draw(gemImage, (world.getGems().get(i).centrePosX * PX_PER_M) - ((world.getGems().get(i).width * PX_PER_M) / 2), (world.getGems().get(i).centrePosY * PX_PER_M) - ((world.getGems().get(i).height * PX_PER_M) / 2), (world.getGems().get(i).width * PX_PER_M) / 2, (world.getGems().get(i).height * PX_PER_M) / 2, world.getGems().get(i).width * PX_PER_M, world.getGems().get(i).height * PX_PER_M, 1, 1, 0);
        for (int i = 0; i < world.getDoors().size; i++) {
            if (world.getDoors().get(i).state.equals(DoorState.CLOSED))
                batch.draw(unlockedDoorImage, (world.getDoors().get(i).centrePosX * PX_PER_M) - ((world.getDoors().get(i).width * PX_PER_M) / 2), (world.getDoors().get(i).centrePosY * PX_PER_M) - ((world.getDoors().get(i).height * PX_PER_M) / 2), (world.getDoors().get(i).width * PX_PER_M) / 2, (world.getDoors().get(i).height * PX_PER_M) / 2, world.getDoors().get(i).width * PX_PER_M, world.getDoors().get(i).height * PX_PER_M, 1, 1, 0);
            if (world.getDoors().get(i).state.equals(DoorState.LOCKED))
                batch.draw(lockedDoorImage, (world.getDoors().get(i).centrePosX * PX_PER_M) - ((world.getDoors().get(i).width * PX_PER_M) / 2), (world.getDoors().get(i).centrePosY * PX_PER_M) - ((world.getDoors().get(i).height * PX_PER_M) / 2), (world.getDoors().get(i).width * PX_PER_M) / 2, (world.getDoors().get(i).height * PX_PER_M) / 2, world.getDoors().get(i).width * PX_PER_M, world.getDoors().get(i).height * PX_PER_M, 1, 1, 0);
        }
        for (int i = 0; i < world.getKeys().size; i++)
            batch.draw(keyImage, (world.getKeys().get(i).centrePosX * PX_PER_M) - ((world.getKeys().get(i).width * PX_PER_M) / 2), (world.getKeys().get(i).centrePosY * PX_PER_M) - ((world.getKeys().get(i).height * PX_PER_M) / 2), (world.getKeys().get(i).width * PX_PER_M) / 2, (world.getKeys().get(i).height * PX_PER_M) / 2, world.getKeys().get(i).width * PX_PER_M, world.getKeys().get(i).height * PX_PER_M, 1, 1, 0);

        caraAnimation = new Animation<TextureRegion>(20f, pj.findRegions("espalda"), Animation.PlayMode.LOOP);
        espalda0 = new Sprite(caraAnimation.getKeyFrame(stateTime,true));
        batch.draw(espalda0, (world.getPlayer().getCentrePos().x * PX_PER_M) - ((world.getPlayer().getWidth() * PX_PER_M) / 2), (world.getPlayer().getCentrePos().y * PX_PER_M) - ((world.getPlayer().getDepth() * PX_PER_M) / 2), (world.getPlayer().getWidth() * PX_PER_M) / 2, (world.getPlayer().getDepth() * PX_PER_M) / 2, world.getPlayer().getWidth() * PX_PER_M, world.getPlayer().getDepth() * PX_PER_M, 1, 1, world.getPlayer().getRotation());
        batch.end();
    }

    private void renderHud() {
        hudCam.position.set(ArenaRoamer.WIDTH / 2, ArenaRoamer.HEIGHT / 2, 0.0f);
        hudCam.update();
//	    hudCam.apply(Gdx.gl20);
        hudBatch.setProjectionMatrix(hudCam.combined);
        hudBatch.begin();
        hudFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        hudFont.draw(hudBatch, "Level: " + world.getLevel(), 15, 305);
        hudFont.draw(hudBatch, "Keys Held: " + world.getInventoryKeys(), 15, 275);
        hudFont.draw(hudBatch, "Remaining Gems: " + world.getGems().size, 15, 260);
        hudFont.draw(hudBatch, "Remaining Keys: " + world.getKeys().size, 15, 245);
        hudBatch.end();
    }

    private void renderDebug() {
        debugShapes.setProjectionMatrix(camera.combined);
        debugShapes.setColor(0.0f, 0.0f, 1.0f, 1.0f);
        debugShapes.begin(ShapeType.Line);
        debugShapes.line(world.getPlayer().getHitboxFrontRight().x * PX_PER_M, world.getPlayer().getHitboxFrontRight().y * PX_PER_M, world.getPlayer().getHitboxBackRight().x * PX_PER_M, world.getPlayer().getHitboxBackRight().y * PX_PER_M);
        debugShapes.line(world.getPlayer().getHitboxBackRight().x * PX_PER_M, world.getPlayer().getHitboxBackRight().y * PX_PER_M, world.getPlayer().getHitboxBackLeft().x * PX_PER_M, world.getPlayer().getHitboxBackLeft().y * PX_PER_M);
        debugShapes.line(world.getPlayer().getHitboxBackLeft().x * PX_PER_M, world.getPlayer().getHitboxBackLeft().y * PX_PER_M, world.getPlayer().getHitboxFrontLeft().x * PX_PER_M, world.getPlayer().getHitboxFrontLeft().y * PX_PER_M);
        debugShapes.line(world.getPlayer().getHitboxFrontLeft().x * PX_PER_M, world.getPlayer().getHitboxFrontLeft().y * PX_PER_M, world.getPlayer().getHitboxFrontRight().x * PX_PER_M, world.getPlayer().getHitboxFrontRight().y * PX_PER_M);
        debugShapes.end();
    }
}

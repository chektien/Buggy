package com.boliao.buggy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
 * @startuml
 * GameObject *-- Renderable
 * GameObject *-- Intelligible
 * GameObject *-- Collidable
 * GFX *-- Renderable
 * AI *-- Intelligible
 * PHY *-- Collidable
 *
 * GameObject : addComponent()
 * GameObject : removeComponent()
 *
 * @enduml
 */

public class Game extends ApplicationAdapter {
    private static final String TAG = "Game";

    private static final int SPEED = 2;
    private static final int ROTATE_SPEED = 5;
    private static final float SCALE_SPEED = 0.2f;
    private static final float MIN_SCALE = 0.1f;
    private static final float MAX_SCALE = 5f;
    private static final float CAM_Z = 30f;
    public static int VIEWPORT_WIDTH = 720;
    public static int VIEWPORT_HEIGHT = 1280;

	SpriteBatch spriteBatch;
    ModelBatch modelBatch;
    Environment env;
    Model model;
	Texture img;

    private Viewport viewport;
    private PerspectiveCamera cam;
    private ModelInstance cube;

    private Hud hud;

	@Override
	public void create () {
        modelBatch = new ModelBatch();
		img = new Texture(Gdx.files.internal("badlogic.jpg"));

        createCam();
        createViewport();
        createCube();
        createEnvironment();

        hud = new Hud();
	}

    private void createEnvironment() {
        env = new Environment();
        env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));
        env.add(new DirectionalLight().set(0.9f, 0.9f, 0.9f, -1f, -1f, -0.2f));
    }

    private void createCam() {
        final int fov = 67;
        final float near = 1.0f;
        final float far = 300.0f;
        Vector3 pos = new Vector3(0, 0, CAM_Z);
        Vector3 lookat = new Vector3(0, 0, 0);

        cam = new PerspectiveCamera(fov, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        cam.position.set(pos);
        cam.lookAt(lookat);
        cam.near = near;
        cam.far = far;

        cam.update();
    }

    private void createViewport() {
        viewport  = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, cam);
        viewport.apply();
    }

    private void processInputs(float deltaTime) {
        // exit
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        // object transformations
        // - note that we are moving the object in it's LOCAL axes
        if (Gdx.input.isTouched()) {
            if (hud.isTranslate()) {
                /**
                 * 1. Let's do it the hard way here: manual matrix multiplication
                 */
                float x = Gdx.input.getDeltaX() * SPEED * deltaTime;
                float y = -Gdx.input.getDeltaY() * SPEED * deltaTime;
                float z = 0;
                Matrix4 mat = new Matrix4(new float[] {
                        1, 0, 0, x,
                        0, 1, 0, y,
                        0, 0, 1, z,
                        0, 0, 0, 1
                });
                cube.transform.mul(mat.tra());

                /**
                 * 2. And now the easy way
                 */
                /*
                cube.transform.translate(
                        Gdx.input.getDeltaX() * SPEED * deltaTime,
                        -Gdx.input.getDeltaY() * SPEED * deltaTime,
                        0
                );
                */
            }

            /**
             * 3. And we'll take it easy here on...
             */

            // rotations are done using quaternions
            if (hud.isRotate()) {
                cube.transform.rotate(Vector3.X, Gdx.input.getDeltaY() * ROTATE_SPEED * deltaTime);
                cube.transform.rotate(Vector3.Y, Gdx.input.getDeltaX() * ROTATE_SPEED * deltaTime);
            }

            // scaling is always a [0..1] factor value
            if (hud.isScale()) {
                cube.transform.scale(
                        1 + Gdx.input.getDeltaX() * SCALE_SPEED * deltaTime,
                        1 - Gdx.input.getDeltaY() * SCALE_SPEED * deltaTime,
                        1
                );

                Gdx.app.log(TAG, "scaleX = " + cube.transform.getScaleX() + " scaleY = " + cube.transform.getScaleY());
            }
        }

    }

    private void createCube() {
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(3f, 3f, 3f, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        cube = new ModelInstance(model);
        //cube.transform.rotate(Vector3.X, 45);
        //cube.transform.rotate(Vector3.Y, 45);
    }

	@Override
	public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        processInputs(deltaTime);

		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(cube, env);
        modelBatch.end();

        hud.draw();
	}
	
	@Override
	public void dispose () {
        spriteBatch.dispose();
        modelBatch.dispose();
        model.dispose();
		img.dispose();
	}

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        viewport.update(width, height);
    }
}

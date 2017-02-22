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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
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

	private SpriteBatch spriteBatch;
    private ModelBatch modelBatch;
    private Environment env;
    private com.boliao.buggy.Cube cube;
    private Texture img;

    private ShaderProgram shaderProg;
    private Shader shader;
    private Renderable renderable;

    private Viewport viewport;
    private PerspectiveCamera cam;

    private Hud hud;

    // temp vars for processing
    private Matrix4 mat = new Matrix4();
    private Matrix4 rotation = new Matrix4();
    private Matrix4 translation = new Matrix4();
    private Matrix4 scaling = new Matrix4();
    private Matrix4 world2Model = new Matrix4();
    private Vector3 rotAxis = new Vector3();
    private Vector3 rotAxisX = new Vector3(Vector3.X);
    private Vector3 rotAxisY = new Vector3(Vector3.Y);

    @Override
	public void create () {
        createCam();
        createViewport();
        createCube();
        createEnvironment();
        createShader();

        hud = new Hud();
    }


    private void createCube() {
        if (SETTINGS.IS_TEXTURED) {
            cube = new TexturedCube(cam, "textures/crate.png");
        }
        else {
            cube = new com.boliao.buggy.Cube(cam);
        }
    }

    private void createShader() {

    }

    private void createEnvironment() {
        env = new Environment();
        env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));
        //env.add(new DirectionalLight().set(0.9f, 0.9f, 0.9f, 2f, 2f, -3f));
        env.add(new PointLight().set(0.9f, 0.9f, 0.9f, 2f, 2f, 3f, 10f));
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
            // get world to model space transform
            world2Model.set(cube.transform).inv();

            // reset all matrices
            translation.idt();
            rotation.idt();
            scaling.idt();

            // do translation
            if (hud.isTranslate()) {
                /**
                 * 1. Let's do it the hard way here: manual matrix multiplication
                 */

                // get translation matrix
                float x = Gdx.input.getDeltaX() * SPEED * deltaTime;
                float y = -Gdx.input.getDeltaY() * SPEED * deltaTime;
                float z = 0;

                // get axes components in model space
                //axis.set(x, y, z).mul(world2Model);

                translation.set(new float[] {
                        1, 0, 0, x,
                        0, 1, 0, y,
                        0, 0, 1, z,
                        0, 0, 0, 1
                });
                translation.tra(); // libgdx stores matrices in col major

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
             * 3. And we'll take it "easy" here on...
             */
            // rotations are done using quaternions
            if (hud.isRotate()) {
                // get rotation around world x-axis
                rotAxis.set(rotAxisX).mul(world2Model);
                rotation.setToRotation(rotAxis, Gdx.input.getDeltaY() * ROTATE_SPEED * deltaTime);

                // get rotation around world y-axis
                rotAxis.set(rotAxisY).mul(world2Model);
                mat.setToRotation(rotAxis, Gdx.input.getDeltaX() * ROTATE_SPEED * deltaTime);

                // multiply the matrices
                rotation.mul(mat);
            }

            // scale along local x-y
            if (hud.isScale()) {
                // get scaling matrix
                // - scaling is always a [0..1] factor value
                scaling.setToScaling(
                        1 + Gdx.input.getDeltaX() * SCALE_SPEED * deltaTime,
                        1 - Gdx.input.getDeltaY() * SCALE_SPEED * deltaTime,
                        1
                );
            }

            // multiply all the matrices
            cube.transform.mul(translation).mul(rotation).mul(scaling);
            //Gdx.app.log(TAG, "translation=\n" + translation + "rotation=\n" + rotation + "scaling=\n" + scaling);
        }

    }

	@Override
	public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        processInputs(deltaTime);

		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        cube.draw();
        hud.draw();
	}
	
	@Override
	public void dispose () {
        spriteBatch.dispose();
        modelBatch.dispose();
        cube.dispose();
		img.dispose();
	}

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        viewport.update(width, height);
    }
}

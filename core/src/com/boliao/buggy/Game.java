package com.boliao.buggy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
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

    private Environment env;
    private com.boliao.buggy.Cube cube;

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

    /**
     * Graphics: camera
     * 1. Program flow.
     */
    @Override
	public void create () {
        createCam();
        createViewport();
        createCube();

        hud = new Hud();
    }

    /**
     * Graphics: camera
     * 2. Defining the VIEW and PROJECTION matrices
     */
    private void createCam() {
        // params for the PROJECTION matrix
        // final float aspect = VIEWPORT_WIDTH/VIEWPORT_HEIGHT; // auto calculated
        final int fov = 70;
        final float near = 1.0f;
        final float far = 300.0f;

        // params for the VIEW matrix
        Vector3 pos = new Vector3(0, 0, SETTINGS.CAM_Z);
        Vector3 lookat = new Vector3(0, 0, 0);

        // set the matrices in the camera
        cam = new PerspectiveCamera(fov, SETTINGS.VIEWPORT_WIDTH, SETTINGS.VIEWPORT_HEIGHT);
        cam.position.set(pos);
        cam.lookAt(lookat);
        cam.near = near;
        cam.far = far;
        cam.update();
    }

    private void createViewport() {
        viewport  = new FitViewport(SETTINGS.VIEWPORT_WIDTH, SETTINGS.VIEWPORT_HEIGHT, cam);
        viewport.apply();
    }

    private void createCube() {
        if (SETTINGS.IS_TEXTURED) {
            cube = new TexturedCube(cam, "textures/crate.png");
        }
        else {
            cube = new com.boliao.buggy.Cube(cam);
        }
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
                 * Graphics: matrices
                 * 1. Let's do it the hard way here: manual matrix multiplication
                 */

                // get translation matrix
                float x = Gdx.input.getDeltaX() * SETTINGS.SPEED * deltaTime;
                float y = -Gdx.input.getDeltaY() * SETTINGS.SPEED * deltaTime;
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
                 * Graphics: matrices
                 * 2. And now the easy way
                 */
                /*
                translation.setToTranslation(
                        Gdx.input.getDeltaX() * SPEED * deltaTime,
                        -Gdx.input.getDeltaY() * SPEED * deltaTime,
                        0
                );
                */
            }

            /**
             * Graphics: matrices
             * 3. And we'll take it "easy" here on...
             */
            // rotations are done using quaternions
            if (hud.isRotate()) {
                // get rotation around world x-axis
                rotAxis.set(rotAxisX).mul(world2Model);
                rotation.setToRotation(rotAxis, Gdx.input.getDeltaY() * SETTINGS.ROTATE_SPEED * deltaTime);

                // get rotation around world y-axis
                rotAxis.set(rotAxisY).mul(world2Model);
                mat.setToRotation(rotAxis, Gdx.input.getDeltaX() * SETTINGS.ROTATE_SPEED * deltaTime);

                // multiply the matrices
                rotation.mul(mat);
            }

            // scale along local x-y
            if (hud.isScale()) {
                // get scaling matrix
                // - scaling is always a [0..1] factor value
                scaling.setToScaling(
                        1 + Gdx.input.getDeltaX() * SETTINGS.SCALE_SPEED * deltaTime,
                        1 - Gdx.input.getDeltaY() * SETTINGS.SCALE_SPEED * deltaTime,
                        1
                );
            }

            /**
             * Graphics: matrices
             * 4. Multiply all transforms.
             */
            cube.transform.mul(scaling).mul(rotation).mul(translation);
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
        cube.dispose();
	}

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        viewport.update(width, height);
    }
}

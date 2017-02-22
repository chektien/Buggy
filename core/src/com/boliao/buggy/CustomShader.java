package com.boliao.buggy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by mrboliao on 17/2/17.
 */

public class CustomShader extends BaseShader {
    private static final String TAG = "CustomShader";

    ShaderProgram shaderProg;
    Camera cam;
    RenderContext renderContext;

    @Override
    public void init() {
        ShaderProgram.pedantic = false;
        shaderProg = new ShaderProgram(Gdx.files.internal("shaders/pixel_vert.glsl"), Gdx.files.internal("shaders/pixel_frag.glsl"));
        if (!shaderProg.isCompiled()) {
            throw new GdxRuntimeException("Shader cannot compile" + shaderProg.getLog());
        }
        else {
            String log = shaderProg.getLog();
            if (log.length() > 0) {
                Gdx.app.error(TAG, "Shader compilation log:" + log);
            }
        }
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        super.begin(camera, context);

        cam = camera;
        renderContext = context;
        shaderProg.begin();
        shaderProg.setUniformMatrix("u_projViewTrans", cam.combined);
    }

    @Override
    public void render(Renderable renderable) {
        super.render(renderable);

        shaderProg.setUniformMatrix("u_worldTrans", renderable.worldTransform);
        renderable.meshPart.render(shaderProg);
    }

    @Override
    public void end() {
        super.end();

        shaderProg.end();
    }
}

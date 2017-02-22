package com.boliao.buggy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by mrboliao on 21/2/17.
 */

public class Cube {
    private static final String TAG = "Cube";

    protected Camera cam;

    protected Mesh mesh;
    protected VertexAttribute vertAtt2;
    protected ShaderProgram shaderProgram;

    Matrix4 transform = new Matrix4();

    public Cube() {}

    public Cube (Camera cam) {
        this.cam = cam;

        // set the secondary vertex attribute to type:color
        vertAtt2 = new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, 4, ShaderProgram.COLOR_ATTRIBUTE);

        createMesh();
        createShader();
    }

    protected void createMesh() {
        // create new mesh
        mesh = new Mesh(
                false,
                50,
                36,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
                vertAtt2
        );

        // create the indices and vertices
        createVertices();
        createIndices();
    }

    protected void createVertices() {
        float x, y, z, r, g, b, a;
        x = y = z = SETTINGS.SIZE;
        r = g = b = a = 1f;
        mesh.setVertices(new float[] {
                -x, y, z, r, 0, 0, a,   // top left     front
                x, y, z, 0, g, 0, a,    // top right    front
                -x, -y, z, 0, 0, b, a,  // bottom left  front
                x, -y, z, r, g, 0, a,   // bottom right front

                -x, y, -z, r, 0, 0, a,  // top left     back
                x, y, -z, 0, g, 0, a,   // top right    back
                -x, -y, -z, 0, 0, b, a, // bottom left  back
                x, -y, -z, r, g, 0, a   // bottom right back
        });
    }

    protected void createIndices() {
        mesh.setIndices(new short[] {
                // front
                0, 1, 2,
                1, 3, 2,

                // top
                0, 4, 5,
                0, 5, 1,

                // back
                4, 5, 6,
                5, 7, 6,

                // bottom
                2, 6, 7,
                2, 7, 3,

                // left
                0, 2, 6,
                0, 6, 4,

                // right
                1, 3, 7,
                1, 7, 5
        });
    }

    protected void createShader() {
        shaderProgram = new ShaderProgram(
                Gdx.files.internal(SETTINGS.VERT_SHADER_PATH),
                Gdx.files.internal(SETTINGS.FRAG_SHADER_PATH)
        );
        if (!shaderProgram.isCompiled()) {
            throw new GdxRuntimeException("Shader cannot compile" + shaderProgram.getLog());
        }
        else {
            String log = shaderProgram.getLog();
            if (log.length() > 0) {
                Gdx.app.error(TAG, "Shader compilation log:" + log);
            }
        }
    }

    public void draw () {
        shaderProgram.begin();

        // send uniform vars to shader
        shaderProgram.setUniformMatrix("u_projViewTrans", cam.combined);
        shaderProgram.setUniformMatrix("u_worldTrans", transform);
        shaderProgram.setUniformi("u_hasTexture", 0);

        // render mesh using shader
        mesh.render(shaderProgram, GL20.GL_TRIANGLES);

        shaderProgram.end();
    }

    public void dispose() {
        shaderProgram.dispose();
        mesh.dispose();
    }
}

package com.boliao.buggy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.Timer;

/**
 * Created by mrboliao on 22/2/17.
 */

public class TexturedCube extends Cube {
    Texture tex;
    private final long timeRef = System.currentTimeMillis();

    public TexturedCube(Camera cam, String texFilePath) {
        this.cam = cam;

        // set the secondary vertex attribute to type:textureCoordinates and link to shader a_texCoord0
        vertAtt2 = new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0");

        // set texture
        tex = new Texture(Gdx.files.internal(texFilePath));

        createMesh();
        createShader();
    }

    @Override
    protected void createVertices() {
        float x, y, z, u, v;
        x = y = z = SETTINGS.SIZE;
        u = v = 1f;
        mesh.setVertices(new float[] {
                // front
                -x, y, z, 0, 0,         // top left
                x, y, z, 0.5f, 0,       // top right
                x, -y, z, 0.5f, 0.5f,   // bottom right
                -x, -y, z, 0, 0.5f,     // bottom left

                // back (similarly note the reverse mapping)
                -x, y, -z, 1f, 0f,     // top left
                x, y, -z, 0.5f, 0,       // top right
                x, -y, -z, 0.5f, 0.5f,    // bottom right
                -x, -y, -z, 1f, 0.5f, // bottom left

                // left
                -x, y, -z, 0, 0.5f,         // top left
                -x, y, z, 0.5f, 0.5f,       // top right
                -x, -y, z, 0.5f, 1f,   // bottom right
                -x, -y, -z, 0, 1f,     // bottom left

                // right
                x, y, z, 0, 0.5f,         // top left
                x, y, -z, 0.5f, 0.5f,      // top right
                x, -y, -z, 0.5f, 1f,   // bottom right
                x, -y, z, 0, 1f,     // bottom left

                // top
                -x, y, -z, 0, 0.5f,        // top left
                x, y, -z, 0.5f, 0.5f,      // top right
                x, y, z, 0.5f, 1f,   // bottom right
                -x, y, z, 0, 1f,     // bottom left

                // bottom
                -x, -y, z, 0, 0.5f,         // top left
                x, -y, z, 0.5f, 0.5f,      // top right
                x, -y, -z, 0.5f, 1f,   // bottom right
                -x, -y, -z, 0, 1f,     // bottom left
        });
    }

    @Override
    protected void createIndices() {
        mesh.setIndices(new short[] {
                // front
                0, 1, 2,
                0, 2, 3,

                // back (note this order is reverse from rest as we are rendering the back face!)
                5, 4, 6,
                6, 4, 7,

                // left
                8, 9, 10,
                8, 10, 11,

                // right
                12, 13, 14,
                12, 14, 15,

                // top
                16, 17, 18,
                16, 18, 19,

                // bottom
                20, 21, 22,
                20, 22, 23
        });
    }

    @Override
    public void draw() {
        tex.bind();

        shaderProgram.begin();

        // send uniform vars to shader
        shaderProgram.setUniformMatrix("u_projViewTrans", cam.combined);
        shaderProgram.setUniformMatrix("u_worldTrans", transform);
        shaderProgram.setUniformi("u_texture", 0);
        shaderProgram.setUniformi("u_hasTexture", 1);
        shaderProgram.setUniformf("u_time", ((System.currentTimeMillis()-timeRef)*SETTINGS.PLASMA_TIME_SCALE));
        //shaderProgram.setUniformf("u_mouse", Gdx.input.getX(), Gdx.input.getY(), Gdx.input.getX(), Gdx.input.getY());

        // render mesh using shader
        mesh.render(shaderProgram, GL20.GL_TRIANGLES);

        shaderProgram.end();
    }
}

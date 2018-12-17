package com.boliao.buggy;

/**
 * Created by mrboliao on 21/2/17.
 */

public class SETTINGS {
    public static boolean IS_DEBUG = true;

    public static boolean IS_TEXTURED = true;

    public static final String SHADER_NAME = "glow"; // ffp, glow

    public static final float SIZE = 3f;
    public static final float PLASMA_TIME_SCALE = 1f/100f;
    public static final String VERT_SHADER_PATH = "shaders/" + SHADER_NAME + "_vert.glsl";
    public static final String FRAG_SHADER_PATH = "shaders/" + SHADER_NAME + "_frag.glsl";

    public static final int SPEED = 2;
    public static final int ROTATE_SPEED = 5;
    public static final float SCALE_SPEED = 0.2f;
    public static final float CAM_Z = 30f;
    public static int VIEWPORT_WIDTH = 720;
    public static int VIEWPORT_HEIGHT = 1280;
}

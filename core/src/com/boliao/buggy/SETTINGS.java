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
}

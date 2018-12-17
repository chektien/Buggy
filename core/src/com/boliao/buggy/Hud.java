package com.boliao.buggy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by mrboliao on 16/2/17.
 */

public class Hud {
    private static final String TAG = "Hud";

    private Viewport viewportStage;
    private Stage stage;
    private SpriteBatch spriteBatch;

    private boolean isTranslate = false;
    private boolean isRotate = false;
    private boolean isScale = false;

    public Hud() {
        spriteBatch = new SpriteBatch();
        viewportStage = new FitViewport(SETTINGS.VIEWPORT_WIDTH, SETTINGS.VIEWPORT_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewportStage, spriteBatch);

        // create table
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        // create text to display on screen
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParams.size = 70;
        fontParams.color = Color.WHITE;
        fontParams.shadowOffsetX = 3;
        fontParams.shadowOffsetY = 3;
        fontParams.shadowColor = new Color(1, 1, 1, 0.5f);
        BitmapFont font = fontGenerator.generateFont(fontParams);

        // button style
        final TextButton.TextButtonStyle textButtonStyleUp = new TextButton.TextButtonStyle();
        textButtonStyleUp.font = font;
        textButtonStyleUp.fontColor = Color.CYAN;
        textButtonStyleUp.overFontColor = Color.RED;

        final TextButton.TextButtonStyle textButtonStyleDown = new TextButton.TextButtonStyle();
        textButtonStyleDown.font = font;
        textButtonStyleDown.fontColor = Color.RED;
        textButtonStyleDown.overFontColor = Color.CYAN;

        // Create title
        Label title = new Label("buggy app", new Label.LabelStyle(font, Color.WHITE));

        // create buttons
        Gdx.input.setInputProcessor(stage);
        final TextButton translateButton = new TextButton("TRANSLATE", textButtonStyleUp);
        translateButton.addListener( new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                toggleIsTranslate();
                toggleChecked(translateButton, textButtonStyleUp, textButtonStyleDown);
                Gdx.app.log(TAG, "TRANSLATE TOUCHUP! isTranslate=" + isTranslate);
            }
        });
        Gdx.input.setInputProcessor(stage);
        final TextButton rotateButton = new TextButton("ROTATE", textButtonStyleUp);
        rotateButton.addListener( new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                toggleIsRotate();
                toggleChecked(rotateButton, textButtonStyleUp, textButtonStyleDown);
                Gdx.app.log(TAG, "ROTATE TOUCHUP! isRotate=" + isRotate);
            }
        });
        Gdx.input.setInputProcessor(stage);
        final TextButton scaleButton = new TextButton("SCALE", textButtonStyleUp);
        scaleButton.addListener( new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                toggleIsScale();
                toggleChecked(scaleButton, textButtonStyleUp, textButtonStyleDown);
                Gdx.app.log(TAG, "SCALE TOUCHUP! isScale=" + isScale);
            }
        });

        // place ui elements
//        table.add(title).expandX().align(Align.center).colspan(3).padTop(10);
//        table.row();
        table.add(translateButton).center().expandX().padTop(10);
        table.add(rotateButton).center().expandX().padTop(10);
        table.add(scaleButton).center().expandX().padTop(10);

        stage.addActor(table);
    }

    private void toggleChecked(TextButton button, TextButton.TextButtonStyle styleUp, TextButton.TextButtonStyle styleDown) {
        if (button.isChecked()) {
            button.setStyle(styleDown);
        }
        else {
            button.setStyle(styleUp);
        }
    }

    private void toggleIsTranslate() {
        if (isTranslate) {
            isTranslate = false;
        }
        else {
            isTranslate = true;
        }
    }

    private void toggleIsRotate() {
        if (isRotate) {
            isRotate = false;
        }
        else {
            isRotate = true;
        }
    }

    private void toggleIsScale() {
        if (isScale) {
            isScale = false;
        }
        else {
            isScale = true;
        }
    }

    public void draw() {
        stage.draw();
    }

    public boolean isTranslate() {
        return isTranslate;
    }

    public boolean isRotate() {
        return isRotate;
    }

    public boolean isScale() {
        return isScale;
    }
}

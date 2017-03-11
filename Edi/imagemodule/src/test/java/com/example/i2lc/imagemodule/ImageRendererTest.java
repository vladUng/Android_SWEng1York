package com.example.i2lc.imagemodule;

import com.example.i2lc.ImageRenderer;

import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vlad on 11/03/2017.
 */
public class ImageRendererTest {
    private ImageRenderer testImage;
    // mock creation

    @Before
    public void setUp() throws Exception {
        testImage = new ImageRenderer(0.1f, 0.1f, 0.3f, 0.4f, 999, 123, "aPath");

        testImage.setActualXpos(10);
        testImage.setActualYpos(10);
        testImage.setActualHeight(300);
        testImage.setActualWidth(300);
    }

    @Test
    public void onDraw() throws Exception {
        testImage.setOpacity(0.5f);
        assertEquals(0.5f, testImage.getOpacity(), 0.0f);
    }

    @Test
    public void intersects() throws Exception {

        assertEquals(true, testImage.intersects(50,50));
        assertEquals(true, testImage.intersects(30,290));

        testImage.setActualHeight(10);
        testImage.setActualWidth(10);

        assertEquals(false, testImage.intersects(50,50));
        assertEquals(false, testImage.intersects(30,290));
    }

    @Test
    public void onClick() throws Exception {

        String onClickAction = "onClickAction";
        String onClickInfo = "onClickInfo";

        testImage.setOnClickAction("");
        testImage.setOnClickInfo(onClickInfo);

        assertEquals(onClickInfo, testImage.onClick());

        testImage.setOnClickAction(onClickAction);

        assertNotEquals(onClickInfo, testImage.onClick());
        assertEquals(onClickAction, testImage.onClick());
    }

    @Test
    public void loadImage() throws Exception {

        testImage.loadImage();
        assertEquals(null, testImage.getImage());
    }

    @Test
    public void discardImage() throws Exception {

        testImage.discardImage();
        assertEquals(null, testImage.getImage());
    }
}
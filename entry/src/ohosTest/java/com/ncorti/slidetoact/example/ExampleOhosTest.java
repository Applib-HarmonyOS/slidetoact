package com.ncorti.slidetoact.example;

import com.ncorti.slidetoact.SlideToActView;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.agp.components.Attr;
import ohos.agp.components.AttrSet;
import ohos.agp.components.element.VectorElement;
import ohos.agp.utils.Color;
import ohos.app.Context;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class ExampleOhosTest {

    private SlideToActView slideToActView;

    @Before
    public void setUp() {
        Context context = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();

        AttrSet attrSet = new AttrSet() {
            @Override
            public Optional<String> getStyle() {
                return Optional.empty();
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Optional<Attr> getAttr(int i) {
                return Optional.empty();
            }

            @Override
            public Optional<Attr> getAttr(String s) {
                return Optional.empty();
            }
        };

        slideToActView = new SlideToActView(context, attrSet);
    }

    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("com.ncorti.slidetoact.example", actualBundleName);
    }

    @Test
    public void testResetSlider() {
        slideToActView.resetSlider();
        assertFalse(slideToActView.isCompleted());
    }

    @Test
    public void testTextMessage() {
        CharSequence charSequence = "Test";
        slideToActView.setText(charSequence);
        assertEquals(charSequence, slideToActView.getText());
    }

    @Test
    public void testBorderRadius() {
        slideToActView.setBorderRadius(20);
        assertEquals(20, slideToActView.getBorderRadius());
    }

    @Test
    public void testOuterColor() {
        slideToActView.setOuterColor(Color.GREEN);
        assertEquals(Color.GREEN, slideToActView.getOuterColor());
    }

    @Test
    public void testInnerColor() {
        slideToActView.setInnerColor(Color.RED);
        assertEquals(Color.RED, slideToActView.getInnerColor());
    }

    @Test
    public void testTextColor() {
        slideToActView.setTextColor(Color.BLACK);
        assertEquals(Color.BLACK, slideToActView.getTextColor());
    }

    @Test
    public void testIconColor() {
        slideToActView.setIconColor(Color.RED);
        assertEquals(Color.RED, slideToActView.getIconColor());
    }

    @Test
    public void testSliderReverse() {
        slideToActView.setSliderReversed(true);
        assertTrue(slideToActView.isSliderReversed());
    }

    @Test
    public void testRotateIcon() {
        slideToActView.setRotateIcon(true);
        assertTrue(slideToActView.isRotateIcon());
    }

    @Test
    public void testSliderLocked() {
        slideToActView.setSliderLocked(true);
        assertTrue(slideToActView.isSliderLocked());
    }

    @Test
    public void testAnimationCompletion() {
        slideToActView.setAnimateCompletion(true);
        assertTrue(slideToActView.isAnimateCompletion());
    }

    @Test
    public void testTextSize() {
        slideToActView.setTextSize(60);
        assertEquals(60, slideToActView.getTextSize());
    }

    @Test
    public void testIconMargin() {
        slideToActView.setIconMargin(25);
        assertEquals(25, slideToActView.getIconMargin());
    }

    @Test
    public void testAreaMargin() {
        slideToActView.setAreaMargin(25);
        assertEquals(25, slideToActView.getAreaMargin());
    }

    @Test
    public void testAnimationDuration() {
        slideToActView.setAnimationDuration(2000);
        assertEquals(2000, slideToActView.getAnimationDuration());
    }

    @Test
    public void testBumpVibration() {
        slideToActView.setBumpVibration(2000);
        assertEquals(2000, slideToActView.getBumpVibration());
    }

    @Test
    public void testSliderIcon() {
        VectorElement vectorElement = new VectorElement(slideToActView.getContext(), ResourceTable.Graphic_ic_cloud);
        slideToActView.setSliderIcon(vectorElement);
        assertEquals(vectorElement, slideToActView.getSliderIcon());
    }

    @Test
    public void testCompleteIcon() {
        VectorElement vectorElement = new VectorElement(slideToActView.getContext(), ResourceTable.Graphic_slidetoact_ic_check);
        slideToActView.setCompleteIcon(vectorElement);
        assertEquals(vectorElement, slideToActView.getCompleteIcon());
    }

}
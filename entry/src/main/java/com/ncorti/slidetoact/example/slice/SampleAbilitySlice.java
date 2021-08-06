
/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ncorti.slidetoact.example.slice;

import com.ncorti.slidetoact.SlideToActView;
import com.ncorti.slidetoact.example.ResourceTable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.VectorElement;
import ohos.global.resource.NotExistException;

/**
 * SampleAbilitySlice.
 */
public class SampleAbilitySlice extends AbilitySlice {

    public static final String EXTRA_PRESSED_BUTTON = "extra_pressed_button";
    public static final String LABEL = "Sample";

    private List<SlideToActView> mSlideList;
    private SimpleDateFormat dateFormat;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        final int pressedButton = intent.getIntParam(EXTRA_PRESSED_BUTTON, 0);
        dateFormat = new SimpleDateFormat("HH:mm:ss", getResourceManager().getConfiguration().getFirstLocale());

        switch (pressedButton) {
            case ResourceTable.Id_button_area_margin:
                super.setUIContent(ResourceTable.Layout_content_area_margin);
                break;
            case ResourceTable.Id_button_icon_margin:
                super.setUIContent(ResourceTable.Layout_content_icon_margin);
                break;
            case ResourceTable.Id_button_colors:
                super.setUIContent(ResourceTable.Layout_content_color);
                break;
            case ResourceTable.Id_button_border_radius:
                super.setUIContent(ResourceTable.Layout_content_border_radius);
                break;
            case ResourceTable.Id_button_text_size:
                super.setUIContent(ResourceTable.Layout_content_text_size);
                break;
            case ResourceTable.Id_button_slider_dimension:
                super.setUIContent(ResourceTable.Layout_content_slider_dimensions);
                break;
            case ResourceTable.Id_button_event_callbacks:
                super.setUIContent(ResourceTable.Layout_content_event_callbacks);
                setupEventCallbacks();
                break;
            case ResourceTable.Id_button_locked_slider:
                super.setUIContent(ResourceTable.Layout_content_locked_slider);
                break;
            case ResourceTable.Id_button_custom_icon:
                super.setUIContent(ResourceTable.Layout_content_custom_icon);
                final SlideToActView slider = (SlideToActView) findComponentById(ResourceTable.Id_slide_custom_icon);
                Component.ClickedListener listener = component -> {
                    switch (component.getId()) {
                        case ResourceTable.Id_button_app_icon:
                            //You can set png format icon using PixelMapElement.
                            try {
                                slider.setSliderIcon(new PixelMapElement(
                                        getResourceManager().getResource(ResourceTable.Media_ic_lock)));
                            } catch (IOException | NotExistException e) {
                                e.printStackTrace();
                            }
                            break;
                        case ResourceTable.Id_button_cloud_icon:
                            slider.setSliderIcon(new VectorElement(getContext(), ResourceTable.Graphic_ic_cloud));
                            slider.setRotateIcon(false);
                            break;
                        case ResourceTable.Id_button_complete_icon:
                            slider.setCompleteIcon(new VectorElement(getContext(), ResourceTable.Graphic_custom_icon));
                            break;
                        default:
                            break;
                    }
                };

                findComponentById(ResourceTable.Id_button_app_icon).setClickedListener(listener);
                findComponentById(ResourceTable.Id_button_cloud_icon).setClickedListener(listener);
                findComponentById(ResourceTable.Id_button_complete_icon).setClickedListener(listener);
                break;
            case ResourceTable.Id_button_reversed_slider:
                super.setUIContent(ResourceTable.Layout_content_reversed_slider);
                break;
            case ResourceTable.Id_button_animation_duration:
                super.setUIContent(ResourceTable.Layout_content_animation_duration);
                break;
            case ResourceTable.Id_button_bump_vibration:
                super.setUIContent(ResourceTable.Layout_content_bumb_vibration);
                break;
            default:
                break;
        }
        mSlideList = getSlideList();

        findComponentById(ResourceTable.Id_btn_reset_slider)
                .setClickedListener(component -> resetAllSlider());
    }

    private void resetAllSlider() {
        for (SlideToActView slide : mSlideList) {
            slide.resetSlider();
        }
    }

    private List<SlideToActView> getSlideList() {
        final List<SlideToActView> slideList = new ArrayList<>();
        final DirectionalLayout container = (DirectionalLayout) findComponentById(ResourceTable.Id_slide_container);
        for (int i = 0; i < container.getChildCount(); i++) {
            final Component child = container.getComponentAt(i);
            if (child instanceof SlideToActView) {
                slideList.add((SlideToActView) child);
            }
        }
        return slideList;
    }

    private void setupEventCallbacks() {
        final SlideToActView slide = (SlideToActView) findComponentById(ResourceTable.Id_event_slider);
        final Text log = (Text) findComponentById(ResourceTable.Id_event_log);

        slide.setOnSlideCompleteListener(view -> log.append("\n" + getTime() + " onSlideComplete"));

        slide.setOnSlideResetListener(view -> log.append("\n" + getTime() + " onSlideReset"));

        slide.setOnSlideUserFailedListener((view, isOutside) ->
                log.append("\n" + getTime() + " onSlideUserFailed - Clicked outside: " + isOutside));

        slide.setOnSlideToActAnimationEventListener(new SlideToActView.OnSlideToActAnimationEventListener() {
            @Override
            public void onSlideCompleteAnimationStarted(SlideToActView view, float threshold) {
                log.append("\n" + getTime() + " onSlideCompleteAnimationStarted - " + threshold + "");
            }

            @Override
            public void onSlideCompleteAnimationEnded(SlideToActView view) {
                log.append("\n" + getTime() + " onSlideCompleteAnimationEnded");
            }

            @Override
            public void onSlideResetAnimationStarted(SlideToActView view) {
                log.append("\n" + getTime() + " onSlideResetAnimationStarted");
            }

            @Override
            public void onSlideResetAnimationEnded(SlideToActView view) {
                log.append("\n" + getTime() + " onSlideResetAnimationEnded");
            }
        });
    }

    private String getTime() {
        return dateFormat.format(new Date());
    }
}

# Slide To Act
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=applibgroup_slideToActView&metric=alert_status)](https://sonarcloud.io/dashboard?id=applibgroup_slideToActView) [![License](https://img.shields.io/badge/license-MIT%20License-brightgreen.svg)](https://opensource.org/licenses/MIT)

A simple *Slide to Unlock* **Material** widget for **Harmony os**, written in [**Java**].

<p align="center">
  <img src="assets/slidetoact.gif" alt="sample-slidetoact gif"/>
</p>

* [Source](#Source)
* [Getting Started](#getting-started-)
* [Example](#example-)
* [Features](#features-)
    * [Attributes](#attributes)
        * [``areaMargin``](#areaMargin)
        * [``innerColor`` & ``outerColor``](#innerColor--outerColor)
        * [``borderRadius``](#borderRadius)
        * [``text``, ``text_size``](#text-textSize)
        * [``slideHeight``](#sliderHeight)
        * [``sliderLocked``](#sliderLocked)
        * [``animationDuration``](#animationDuration)
        * [``sliderReversed``](#sliderReversed)
        * [``sliderIcon``](#sliderIcon)
        * [``completeIcon``](#completeIcon)
        * [``bumpVibration``](#bumpVibration)
        * [``rotateIcon``](#rotateIcon)
    * [Event callbacks](#event-callbacks)
* [Future Work](#future-work)
* [Demo](#demo-)
* [Building/Testing](#buildingtesting-)
    * [CircleCI](#circleci-)
    * [TravisCI](#travisci-)
    * [Building locally](#building-locally)
    * [Testing](#testing)
* [Contributing](#contributing-)
* [Honorable Mentions](#honorable-mentions-)
* [License](#license-)

## Source
Inspired from android library https://github.com/cortinico/slidetoact

## Getting Started 👣

**Slide To Act** is distributed through [JCenter](https://bintray.com/bintray/jcenter?filterByPkgName=slidetoact). To use it you need to add the following **Gradle dependency** to your **harmony entry gradle file** (NOT the root file):

```groovy
dependencies {
   implementation "com.ncorti:slidetoact:0.9.0"
}
```

Or you can download the .AAR artifact [directly from Bintray](https://bintray.com/cortinico/maven/download_file?file_path=com%2Fncorti%2Fslidetoact%2F0.9.0%2Fslidetoact-0.9.0.aar).

## Example 🚸

After setting up the Gradle dependency, you can use ``SlideToActView`` widgets inside your **XML Layout files**

```xml
<com.ncorti.slidetoact.SlideToActView
    ohos:id="$+id:example"
    ohos:height="match_content"
    ohos:width="match_parent"
    ohos:text="Example" />
```

And bind them inside your **Java/Kotlin** code:
```java
SlideToActView slideToActView = (SlideToActView) findComponentById(ResourceTable.Id_example);
```

## Features 🎨

* **100% Vectorial**, no .png or other assets provided.
* **Fancy animations!** 🦄
* Easy to integrate (just a gradle compile line).
* Works **out of the box**, no customization needed.
* **UX Friendly** 🐣, button will bump to complete if it's over the 80% of the slider (see the following gif).

<p align="center">
  <img src="assets/ux_friendly.gif" alt="ux_friendly gif"/>
</p>

### Attributes

By the default, every ``SlideToActView`` widget fits to your app using the some default values and colors. You can customize your ``SlideToActView`` using the following **custom attributes**.

```xml
<com.ncorti.slidetoact.SlideToActView
    ohos:id="$+id:welcome_slider"
    ohos:height="match_content"
    ohos:width="match_parent"
    ohos:areaMargin="4vp"
    ohos:animationDuration="250"
    ohos:outerColor="#FF00FF00"
    ohos:innerColor="#FF222222"
    ohos:borderRadius="2vp"
    ohos:text="Testing all the custom attributes"
    ohos:textSize="12fp"
    ohos:sliderHeight="80vp"
    ohos:sliderLocked="false"
/>
```

#### ``areaMargin``

Use the ``areaMargin`` attribute to control the **margin of the inner circular button** from the outside area. If not set, this attribute defaults to **8vp**.

<p align="center"><img src="assets/area_margin_1.png" alt="area_margin_1" width="40%"/></p>


#### ``iconMargin``

The attribute ``iconMargin`` let you control the margin of the icon inside the circular button. This makes the icon bigger because can take up more space in the button. 

This is especially useful when you want to make the height of the slider smaller (see ``sliderHeight``). In this case, if you don't adjust the ``iconMargin`` the image can be too much tiny. By default, the ``iconMargin`` is set to 16vp.

In next image you can see how it looks like:

<p align="center"><img src="assets/icon_margin_1.png" alt="icon_margin" width="40%"/></p>

#### ``innerColor`` & ``outerColor``

Use the ``outerColor`` attribute to control the **color of the external area** and the **color of the arrow icon**. If not set, this attribute defaults to 
**- ![#FF4081](https://via.placeholder.com/15/FF4081/000000?text=+) `#FF4081`**.

Use the ``innerColor`` attribute to control the **color of the inner circular button**, the **color of the tick icon** and the **color of the text**. If not set, this attribute defaults to 
**- ![#FFFFFF](https://via.placeholder.com/15/FFFFFF/000000?text=+) `#FFFFFF`**.

<p align="center"><img src="assets/color_1.png" alt="color_1" width="40%"/></p>

#### ``borderRadius``

Use the ``borderRadius`` attribute to control the **radius** of the **inner circular button** and of the **external area**. A ``border_radius`` set to **0vp** will result in a square slider. If not set, this attribute will render your slider as a **circle** (default behavior).

<p align="center"><img src="assets/border_radius_1.png" alt="border_radius_1" width="40%"/> <img src="assets/border_radius_2.png" alt="border_radius_2" width="40%"/></p>

#### ``text``, ``textSize``

Use the ``text`` attribute to control the **text of your slider**. If not set, this attribute defaults to **SlideToActView**. 

Use the ``textSize`` attribute to control the **size** of the **text of your slider**. A ``textSize`` set to **0fp** will result in hiding the text. If not set, this attribute defaults to **16fp**.

<p align="center"><img src="assets/text.png" alt="slider_text" width="40%"/></p>

#### ``sliderHeight``

Use the ``sliderHeight`` attribute to control the **desired height** of the widget. If not set, the widget will try to render with **72vp** of height.

<p align="center"><img src="assets/slider_height_1.png" alt="slider_height_1" width="40%"/> <img src="assets/slider_height_2.png" alt="slider_height_2" width="40%"/></p>

#### ``sliderLocked``

Use the ``sliderLocked`` attribute to **lock the slider** (this is a boolean attribute). When a slider is locked, will always bump the button to the beginning (default is false).

<p align="center">
  <img src="assets/locked_slider.gif" alt="locked_slider gif"/>
</p>

You can also toggle this attribute programmatically with the provided setter.

```java
SlideToActView sta = (SlideToActView) findComponentById(ResourceTable.Id_slider);
sta.setLocked(true);
```

#### ``animationDuration``

Use the ``animationDuration`` attribute to **set the duration** of the complete and reset animation (in milliseconds).

You can also set animation duration programmatically with the provided setter.

```kotlin
val sta = (SlideToActView) findComponentById(ResourceTable.Id_slider);
sta.setAnimationDuration(600);
```

#### ``sliderReversed``

Use the ``sliderReversed`` attribute to **reverse the slider** (this is a boolean attribute). When a slider is reversed, the cursor will appear on the right and will progress to the left. (default is false).

<p align="center">
  <img src="assets/reversed_slider.gif" alt="reversed_slider gif"/>
</p>

You can also toggle this attribute programmatically with the provided setter.

```java
SlideToActView sta = findComponentById(ResourceTable.Id_slider);
sta.setReversed(true);
```

#### ``sliderIcon``

You can set a custom icon by setting the ``sliderIcon``attribute to a drawable resource.

<p align="center">
  <img src="assets/custom_icon.png" alt="custom_icon" width="40%"/>
</p>

```xml
ohos:sliderIcon="$graphic:custom_icon"
```

You can also set a custom icon programmatically with the provided setter.

```java
SlideToActView sta = findComponentById(ResourceTable.Id_slider);
sta.setSliderIcon(new VectorElement(getContext(), ResourceTable.Graphic_custom_icon));
```

You can also disable the rotation by setting the ``rotateIcon`` attribute to false.

#### ``completeIcon``
You can set a custom complete icon by setting the ``completeIcon``attribute to a drawable resource.

<p align="center">
  <img src="assets/complete_icon.gif" alt="custom_complete_iconcon" width="40%"/>
</p>

```xml
app:complete_icon="$graphic:slidetoact_ic_check"
```

You can also set a custom complete icon programmatically with the provided setter.

```java
SlideToActView sta = findComponentById(ResourceTable.Id_slider);
slider.setCompleteIcon(new VectorElement(getContext(), ResourceTable.Graphic_slidetoact_ic_check));
```

#### ``sliderIconColor``

You can set a custom color for the icon by setting the ``sliderIconColor`` attribute.

<p align="center">
  <img src="assets/slider_icon_color.png" alt="custom_icon" width="40%"/>
</p>

This attribute defaults to the ``outerColor`` if set. If ``outerColor`` is not set, this attribute defaults to 
**- ![#FF4081](https://via.placeholder.com/15/FF4081/000000?text=+) `#FF4081`**.

#### ``bumpVibration``

You can make the device vibrate when the cursor "bumps" to the end of the sliding path by setting the period of vibration through bumpVibration attribute in your layout XML (default is 0)

```xml
ohos:bumpVibration="200"
```

Note that the period of vibration is in milliseconds

You can achieve the same programmatically using the setter:

```java
SlideToActView sta = findComponentById(ResourceTable.Id_slider);
sta.setBumpVibration(200);
```

### Event callbacks

You can use the ``OnSlideCompleteListener`` and the ``OnSlideResetListener`` to simply interact with the widget. If you need to perform operations during animations, you can provide an ``OnSlideToActAnimationEventListener``. With the latter, you will be notified of every animation start/stop.

You can try the **Event Callbacks** in the [Demo app](#demo) to better understand where every callback is called.

<p align="center"><img src="assets/event_log.png" alt="event_log" width="40%"/></p>

## Future Work
1. Adding Elevation support.
2. Adding clipChildren support to obtain this behavior:

<p align="center">
  <img src="assets/area_margin_3.png" alt="custom_icon" width="40%"/>
</p>

3. Adding Text style and custom text feature.

## Demo 📲

Wonna see the widget in action? Just give a try to the **Example App**, it's inside the [**example**](example/) folder. 

Otherwise, you can just [download the **APK** from a CircleCI build](https://45-58338361-gh.circle-artifacts.com/0/tmp/circle-artifacts.uQdJ7rB/outputs/apk/example-debug.apk), and try it on a real device/emulator. 

<p align="center">
  <img src="assets/example_app.gif" alt="example_app gif"/>
</p>

## Building/Testing ⚙️

### CircleCI [![CircleCI](https://circleci.com/gh/cortinico/slidetoact/tree/master.svg?style=shield)](https://circleci.com/gh/cortinico/slidetoact/tree/master)

This projects is built with [**Circle CI**](https://circleci.com/gh/cortinico/slidetoact/). The CI environment takes care of building the library .AAR, the example app and to run the **Espresso** tests. **Artifacts** are exposed at the end of every build (both the .AAR and the .APK of the example app).

### TravisCI [![Build Status](https://travis-ci.org/cortinico/slidetoact.svg?branch=master)](https://travis-ci.org/cortinico/slidetoact)

[**TravisCI**](https://travis-ci.org/cortinico/slidetoact) builds are also running but they are considered **Legacy**. I'm probably going to dismiss it soon or later.

### Building locally

Before building, make sure you have the following **updated components** from the Android SDK:

* tools
* platform-tools
* build-tools-25.0.3
* android-25
* extra-android-support
* extra-android-m2repository
* extra-google-m2repository

Then just clone the repo locally and build the .AAR with the following command:

```bash
git clone git@github.com:cortinico/slidetoact.git
cd slidetoact/
./gradlew slidetoact:assemble
```

The assembled .AAR will be inside the **slidetoact/build/outputs/aar** folder.

### Testing

Once you're able to build successfully, you can run Espresso tests locally with the following command.

```bash
./gradlew clean build connectedCheck 
```

Make sure your tests are all green ✅ locally before submitting PRs.

## Contributing 🤝

**Looking for contributors! Don't be shy.** 😁 Feel free to open issues/pull requests to help me improve this project.

* When reporting a new Issue, make sure to attach **Screenshots**, **Videos** or **GIFs** of the problem you are reporting.
* When submitting a new PR, make sure tests are all green. Write new tests if necessary.

## Honorable mentions 🎖

* [flutter-slide-to-act](https://github.com/imtoori/flutter-slide-to-act) - A porting of this widget for Flutter

## License 📄

This project is licensed under the MIT License - see the [License](License) file for details

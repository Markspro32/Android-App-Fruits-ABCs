# Fruit Learning App (MaterialTest)

An Android app built to help children learn more about fruits in a fun and visual way.

## Why I built this

I built this project because I want to teach children to know more about fruits through pictures, simple introductions, and interaction.

## Features

- Grid list of fruits with images
- Fruit detail page with a large header image
- Real fruit introductions (easy-to-read text for each fruit)
- Comment feature: children (or parents/teachers) can add comments for each fruit
- Pull-to-refresh on the fruit list
- Material Design UI components

## Tech Stack

- Kotlin
- Android SDK (compileSdk 36, minSdk 24)
- AndroidX + Material Components
- RecyclerView + SwipeRefreshLayout
- Glide (image loading)

## Project Structure

- `app/src/main/java/com/example/materialtest/MainActivity.kt`  
  Fruit list screen
- `app/src/main/java/com/example/materialtest/FruitActivity.kt`  
  Fruit detail, introduction, and comment logic
- `app/src/main/res/layout/activity_main.xml`  
  Main page layout
- `app/src/main/res/layout/activity_fruit.xml`  
  Fruit detail layout

## How to run

1. Open the project in Android Studio.
2. Let Gradle sync dependencies.
3. Run the `app` configuration on an emulator or Android device.

You can also build from terminal:

```bash
./gradlew :app:assembleDebug
```

## Educational Goal

This app is designed as a simple fruit-learning tool for children:

- recognize fruits by image and name
- read short fruit introductions
- encourage interaction by writing comments

## Future Improvements

- Add pronunciation audio for each fruit
- Add quiz mode (match image to fruit name)
- Add multi-language support
- Store comments in a database or cloud backend

# Wallpaper — AI Wallpaper Creator

An Android application that allows users to select a photo, transform it using a text prompt, save the result to the device gallery, and set it as the home screen and lock screen wallpaper.

## Features

- Select a photo from the device gallery
- Take a photo using the camera
- Choose a photo from an online collection
- Edit an image using a text prompt
- Save the resulting image to the gallery
- Set the image as the home screen and lock screen wallpaper

## Technologies

- **Kotlin**
- **Jetpack Compose** and **Material 3**
- **MVVM** with `presentation`, `domain`, and `data` layers
- **Koin** for dependency injection
- **Retrofit**, **OkHttp**, and **Gson** for API communication
- **Coil** for image loading
- **Coroutines** and **StateFlow** for asynchronous operations and UI state management
- **Navigation Compose** for screen navigation
- **MediaStore** and **WallpaperManager** for saving and setting wallpapers

## Requirements

- Android Studio with support for Android Gradle Plugin 9.0.1
- JDK 11
- Android SDK 36
- A device or emulator running Android 10 (API 29) or newer
- A backend available at `http://10.0.2.2:8080/`

The `10.0.2.2` address allows the Android emulator to access the computer running the backend. When testing on a physical device, change the `baseUrl` in `NetworkModule.kt` to the computer’s local IP address. Both devices must be connected to the same network.

## Required Backend Endpoints

The application expects two endpoints:

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/photography?q=nature&image_type=photo` | Returns a list of photos using the Pixabay response format |
| `POST` | `/edit-image` | Accepts `multipart/form-data` containing `prompt` and `image`, then returns the URL of the edited image |

Example response from `/edit-image`:

```json
{
  "imageUrl": "https://example.com/edited-image.jpg"
}
```

The backend source code is not included in this repository.

## Permissions

The application uses the following Android permissions:

- `CAMERA` — taking photos
- `INTERNET` — downloading and editing images
- `SET_WALLPAPER` and `SET_WALLPAPER_HINTS` — setting the device wallpaper

## Project Structure

```text
app/src/main/java/com/example/wallpaper/
├── data/          # APIs, DTOs, mappers, and repository implementations
├── di/            # Koin modules
├── domain/        # Models, repository interfaces, and use cases
├── navigation/    # Screen and route definitions
└── presentation/  # Compose screens, ViewModels, and UI states
```

The application contains three main screens:

- **Main** — image source selection
- **Photos** — online photo collection
- **Editor** — image preview, editing, saving, and wallpaper setup
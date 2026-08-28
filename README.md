# Wallpaper — Kreator Tapet

Aplikacja na Androida pozwalająca wybrać zdjęcie, przekształcić je za pomocą opisu tekstowego, zapisać rezultat w galerii oraz ustawić go jako tapetę ekranu głównego i blokady.

## Funkcje

- wybór zdjęcia z galerii urządzenia,
- wykonanie zdjęcia aparatem,
- wybór fotografii z katalogu online,
- edycja obrazu na podstawie tekstowego promptu,
- zapis gotowego obrazu w galerii,
- ustawienie obrazu jako tapety ekranu głównego i blokady.

## Technologie

- **Kotlin**
- **Jetpack Compose** i **Material 3**
- **MVVM** z podziałem na warstwy `presentation`, `domain` i `data`
- **Koin** — dependency injection
- **Retrofit**, **OkHttp** i **Gson** — komunikacja z API
- **Coil** — ładowanie obrazów
- **Coroutines** oraz **StateFlow** — operacje asynchroniczne i stan interfejsu
- **Navigation Compose** — nawigacja między ekranami
- **MediaStore** i **WallpaperManager** — zapis oraz ustawianie tapety

## Wymagania

- Android Studio z obsługą Android Gradle Plugin 9.0.1,
- JDK 11,
- Android SDK 36,
- urządzenie lub emulator z Androidem 10 (API 29) albo nowszym,
- backend dostępny pod adresem `http://10.0.2.2:8080/`.

Adres `10.0.2.2` wskazuje z emulatora Androida na komputer, na którym uruchomiony jest backend. Podczas testowania na fizycznym urządzeniu należy zmienić `baseUrl` w `NetworkModule.kt` na adres IP komputera w tej samej sieci.

## Wymagane endpointy backendu

Aplikacja oczekuje dwóch endpointów:

| Metoda | Endpoint | Działanie |
|---|---|---|
| `GET` | `/photography?q=nature&image_type=photo` | Zwraca listę zdjęć w formacie odpowiedzi Pixabay |
| `POST` | `/edit-image` | Przyjmuje `multipart/form-data` z polami `prompt` i `image`, a następnie zwraca adres zmodyfikowanego obrazu |

Przykładowa odpowiedź endpointu `/edit-image`:

```json
{
  "imageUrl": "https://example.com/edited-image.jpg"
}
```

Kod backendu nie znajduje się w tym repozytorium.

## Uprawnienia

Aplikacja korzysta z następujących uprawnień:

- `CAMERA` — wykonywanie zdjęć,
- `INTERNET` — pobieranie i edycja obrazów,
- `SET_WALLPAPER` oraz `SET_WALLPAPER_HINTS` — ustawianie tapety.

## Struktura projektu

```text
app/src/main/java/com/example/wallpaper/
├── data/          # API, DTO, mapery i implementacje repozytoriów
├── di/            # moduły Koin
├── domain/        # modele, interfejsy repozytoriów i przypadki użycia
├── navigation/    # definicje ekranów i tras
└── presentation/  # ekrany Compose, ViewModele i stany UI
```

Główne ekrany aplikacji:

- **Main** — wybór źródła obrazu,
- **Photos** — katalog fotografii online,
- **Editor** — podgląd, edycja, zapis i ustawienie tapety.
# InvScan

InvScan is an Android inventory app built with Kotlin and Jetpack Compose. It was developed for the Mobile Application Development course.

**Team:** Mateus P., Ellyson V., André G.

Project decisions and implementation notes are documented in [`DOCUMENTACAO.md`](DOCUMENTACAO.md).

## Features

- Browse, search, create, edit, and delete inventory assets.
- Mark assets as checked during an inventory.
- Organize assets by location and view each location's assets and check progress.
- Search for an asset by its label code (the code is typed manually; camera scanning is not implemented).
- Add images to an asset using Android's Photo Picker.
- Edit the user profile, unlock usage-based achievements, change the theme, increase text size, and open the support screen.
- Persist assets and locations in a local Room database.

**Persistence note:** assets and locations are stored locally in Room and remain after closing the app. The profile and settings are still kept in memory and reset when the app process is restarted. Image references are stored as text in the asset record; the app does not upload images to a server.

## Requirements

- Android Studio with a compatible Android SDK and Gradle setup.
- Kotlin and Jetpack Compose enabled in the Android app module.
- Google Maven repository configured.
- Kotlin Symbol Processing (KSP), required by Room 3.
- A physical Android device or an Android emulator.

This ZIP contains the Kotlin source and documentation, not the full Android Studio project: Gradle files, the manifest, version catalog, and image assets are not included. Open the original project to retain its existing Android Gradle Plugin, Kotlin, Compose, and SDK versions.

## Dependencies

The source uses Jetpack Compose, Navigation Compose, Lifecycle ViewModel Compose, Android Activity Compose, Material icons, Kotlin coroutines, and Room 3.

In the app module's `build.gradle.kts`, make sure the matching dependencies are present. Keep the versions compatible with the versions already configured in the project:

```kotlin
dependencies {
    // If the project uses the Compose BOM, manage Compose library versions through it.
    implementation("androidx.activity:activity-compose:<compatible-version>")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:<compatible-version>")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:<compatible-version>")

    // Room 3: use the same version for runtime and compiler.
    val roomVersion = "3.0.3"
    implementation("androidx.room3:room3-runtime:$roomVersion")
    ksp("androidx.room3:room3-compiler:$roomVersion")
}
```

Room 3 requires the KSP Gradle plugin. Add it to the module's `plugins` block using a KSP version compatible with the project's Kotlin version:

```kotlin
plugins {
    id("com.google.devtools.ksp") version "<ksp-version-compatible-with-your-kotlin>"
}
```

Use the project's existing plugin-management setup if KSP is already declared in the root build or version catalog; do not declare the same plugin twice. Ensure `google()` is present in the dependency repositories. The exact Activity, Navigation, Lifecycle, Compose BOM, Android Gradle Plugin, Kotlin, and KSP versions cannot be verified from this source-only ZIP because the Gradle configuration is not included.

## Run the app

1. Open the complete project in Android Studio.
2. Check that the dependencies and KSP plugin above are configured, adjusting versions to match the project.
3. Sync Gradle and resolve any build errors.
4. Start an emulator or connect an Android device with USB debugging enabled.
5. Select the app configuration and click **Run**.

## Screens and routes

| Screen | Route |
|---|---|
| Home | `inicio` |
| Feature menu | `menu` |
| Assets list | `patrimonios` |
| Asset details | `patrimonio/{id}` |
| Create asset | `criar_item` |
| Locations list | `locais` |
| Location details | `local/{id}` |
| Search by label code | `escanear` |
| User profile | `usuario` |
| Settings | `configuracoes` |
| Support | `suporte` |

## Project structure

```text
data/           Room database, entities, DAOs, profile and settings models
navigation/     Routes and NavHost
viewmodel/      Shared app state and persistence operations
ui/components/  Reusable bars, fields, cards and image components
ui/screens/     The 11 app screens
ui/theme/       Colors, typography, dimensions and light/dark theme
```

## Current limitations

- Label codes are entered manually; camera-based barcode/QR scanning is not implemented.
- Only assets and locations are persisted in Room. Profile information and settings are currently in-memory state.
- There is no remote backend or account synchronization.

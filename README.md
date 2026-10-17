# 4PLAY TV

Нативная Android TV / Google TV оболочка для party-hub **4PLAY** от **AlterNative**.

Текущий статус: ранний MVP (`0.1.0-dev`).

Основная игровая логика и TV-интерфейс живут в PWA: https://shrekerianez.github.io/alter-native-4play/

Android-приложение загружает TV-режим PWA и отвечает за системную TV-интеграцию: fullscreen, пульт, сеть, загрузочный и offline-экраны.

## Техническая база MVP

- Kotlin
- Android TV / Google TV
- WebView
- package id: `app.alternative.fourplay.tv`
- `compileSdk 36`
- `targetSdk 36`
- `minSdk 23`
- AGP 9.4.0
- Gradle 9.6
- JDK 17

TV-клиент открывается по адресу: `https://shrekerianez.github.io/alter-native-4play/?tv`.

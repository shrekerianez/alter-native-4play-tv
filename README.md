# 4PLAY TV

Нативная Android TV / Google TV оболочка для party-hub **4PLAY** от **AlterNative**.

Текущий статус: ранний MVP (`0.2.0-dev`).

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


## APK 0.2.0-dev · Автозвук

[Скачать отладочный APK 4PLAY TV v0.2.0-dev](https://raw.githubusercontent.com/shrekerianez/alter-native-4play-tv/main/downloads/4PLAY-TV-v0.2.0-dev-debug.apk)

В Android WebView выставлено `mediaPlaybackRequiresUserGesture = false`: телевизор играет музыку и эффекты без пульта, поскольку запуск, настройки громкости и все действия выполняются со смартфона организатора. Фактический автозапуск звука необходимо проверить на конкретной модели Android TV, так как Web Audio также зависит от версии WebView и системных аудионастроек.

Веб-интерфейс TV вписывает фиксированную сцену 1920×1080 в физический экран без прокрутки. Все экраны и музыка обновляются через PWA, повторной сборки APK для изменений в игре не требуется.

**Важно:** это отладочная Android-сборка. Debug-сертификат может отличаться от ранее установленного APK, и система откажет в обновлении поверх него. Тогда удалите старое приложение и установите v0.2.0-dev. Для последующих обычных обновлений нужна постоянная подпись APK.

# Telegram Bot для расписания БИТИ НИЯУ МИФИ

Этот проект представляет собой Telegram-бота, который автоматически отправляет расписание студентам БИТИ НИЯУ МИФИ, беря данные с сайта ВУЗА. Бот работает в групповых чатах и ежедневно отправляет актуальное расписание.

## Основные функции

- Автоматическая отправка расписания каждый день.
- Поддержка групповых чатов.
- Интеграция с базой данных для хранения информации о чатах, расписании.
- Развертка через Docker Compose.

## Технологии

- **Spring Boot** — основа проекта.
- **TelegramBots API** — для взаимодействия с Telegram.
- **PostgreSQL** — для хранения данных о чатах.
- **Docker** — для контейнеризации приложения.
- **Docker Compose** — для удобной развертки.


#!/bin/bash
set -e

echo "Собираем и запускаем окружение вместе с тестами в Docker"
docker compose up --build --exit-code-from app-tests

echo "Сборка завершена. Очистка окружения"
docker compose down -v

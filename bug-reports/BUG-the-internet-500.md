**Summary:** [API] GET /status_codes/500 эндпоинт возвращает 500 вместо 200

**Environment:**
- Окружение: Staging (https://the-internet.herokuapp.com)
- Версия API: 1.0.0
- Дата: 2026-09-30

**Preconditions:**
- Авторизация не требуется

**Steps to Reproduce:**
1. Выполнить HTTP-запрос:
```bash
curl -X POST https://the-internet.herokuapp.com/status_codes/500 \
  -H "Content-Type: application/json"
```

**Actual Result:**
Сервер падает во внутреннюю ошибку и возвращает HTTP-статус `500 Internal Server Error`.
Тело ответа содержит html заглушку страницы `This page returned a 500 status code`

**Expected Result:**
* HTTP Status 200
* Пустое тело ответа

**Severity: Major**
* Ключевой эндпоинт
* Priority: High

**Связь с автотестом**
* Класс `TracedTest`
* Метод `testNegative`
* Тест упал в билде `http://localhost:8081/job/tests/58/`

**Attachments:**
* Скриншот ответа: [screenshots/the-internet-500.png]
* Allure-отчёт: http://localhost:8081/job/tests/58/allure/#904844fd60793cd2711a78d93b324203
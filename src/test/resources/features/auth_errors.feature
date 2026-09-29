Feature: Валидация ошибок аутентификации

  @Negative @Auth
  Scenario Outline: Проверка авторизации с некорректными учетными данными: <test_name>
    When отправлен запрос на авторизацию с логином "<username>" и паролем "<password>"
    Then сервис возвращает ошибку авторизации со статусом 200 и причиной "Bad credentials"

    Examples:

      | username  | password    | test_name              |
      | anton     | password123 | Неправильный логин     |
      | not_admin | password123 | Неверный логин         |
      | anton     | password    | Неправильный пароль    |
      | Admin     | password123 | Неверный регистр логина|
      | admin     | Password123 | Неверный регистр пароля|
      |           | password123 | Пустой логин           |
      | admin     |             | Пустой пароль          |
      |           |             | Пустые поля            |

  @Negative @AuthPayload
  Scenario Outline: Проверка отправки некорректной структуры тела запроса: <test_name>
    When отправлен запрос на авторизацию с некорректным телом "<payload>" и типом контента "<content_type>"
    Then сервис возвращает ошибку валидации запроса со статусом <expected_status>

    Examples:

      | payload   | content_type | test_name          | expected_status |
      | {}        | JSON         | Пустой JSON объект | 200             |
      |           | TEXT         | Пустое тело        | 200             |
      | {username | JSON         | Невалидный JSON    | 400             |

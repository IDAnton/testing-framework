Feature: Тестирование формы авторизации в UI

  @UI @Login
  Scenario Outline: Проверка авторизации на UI: <test_name>
    Given открыта страница авторизации
    When пользователь вводит имя "<username>" и пароль "<password>"
    Then система отображает ожидаемый текст сообщения "<expected_text>"

    Examples:

      | username     | password             | expected_text                  | test_name                        |
      | ?/!@#$%^&*   | ?/!@#$%^&*           | Your username is invalid!      | Спецсимволы                      |
      | test         | ' OR 1=1 --          | Your username is invalid!      | SQL инъекция                     |
      |              |                      | Your username is invalid!      | Пустые поля                      |
      | a            | b                    | Your username is invalid!      | Один символ                      |
      | tomsmith     | 123                  | Your password is invalid!      | Правильный логин, неверный пароль|
      | tomsmith123  | SuperSecretPassword! | Your username is invalid!      | Правильный пароль, неверный логин|
      | tomsmith     | SuperSecretPassword! | You logged into a secure area! | Правильный логин пароль          |

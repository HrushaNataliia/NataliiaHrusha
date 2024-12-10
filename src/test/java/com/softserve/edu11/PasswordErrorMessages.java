package com.softserve.edu11;

public enum PasswordErrorMessages {
    EMPTY_PASSWORD("Введіть пароль"),
    SHORT_PASSWORD("Пароль повинен містити принаймі 8 символів без пробілів"),
    LONG_PASSWORD("Пароль повинен містити менше 20 символів без пробілів.");

    private final String message;

    PasswordErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

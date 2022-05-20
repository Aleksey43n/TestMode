package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.Api;
import ru.netology.web.data.DataGenerator;

import static com.codeborne.selenide.Condition.text;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    DataGenerator.UserInfo generator1 = DataGenerator.getUserInfoActive();
    DataGenerator.UserInfo generator2 = DataGenerator.getUserInfoBlocked();

    @BeforeEach
    public void openPage() {
        open("http://localhost:9999");
    }

    public void registration(String login, String password) {
        $("[name=login]").setValue(login);
        $("[name=password]").setValue(password);
        $(".button").click();
    }

    @Test
    public void shouldSignInIfExistentUser() {

        Api.signUp(generator1);
        registration(generator1.getLogin(), generator1.getPassword());
        $(byText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    public void shouldNotSignIfNotExistentUser() {

        registration(generator2.getLogin(), generator2.getPassword());
        $(".notification__title").shouldHave(text("Ошибка")).shouldBe(visible);
        $(".notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    public void shouldNotSignIfBlockUser() {

        Api.signUp(generator2);
        registration(generator2.getLogin(), generator2.getPassword());
        $(".notification__title").shouldHave(text("Ошибка")).shouldBe(visible);
        $(".notification__content").shouldHave(text("Ошибка! Пользователь заблокирован")).shouldBe(visible);
    }

    @Test
    public void shouldNotSignIfInvalidLogin() {

        Api.signUp(generator2);
        registration(DataGenerator.getInvalidLogin(), generator2.getPassword());
        $(".notification__title").shouldHave(text("Ошибка")).shouldBe(visible);
        $(".notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    public void shouldNotSignIfInvalidPassword() {

        Api.signUp(generator2);
        registration(generator2.getLogin(), DataGenerator.getInvalidPassword());
        $(".notification__title").shouldHave(text("Ошибка")).shouldBe(visible);
        $(".notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль")).shouldBe(visible);
    }
}
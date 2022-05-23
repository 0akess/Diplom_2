package register.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import site.stellarburgers.nomoreparties.data.GetDataUser;
import site.stellarburgers.nomoreparties.model.User;
import site.stellarburgers.nomoreparties.requests.user.DeleteUser;
import site.stellarburgers.nomoreparties.requests.user.PostRegister;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Негативный набор тестов на ручку PostRegisterUser")
public class NegativeRegisterUserTest {

    private static String token;
    private static final GetDataUser list = new GetDataUser();
    private static final String email = list.dataForRegister().get(0);
    private static final String password = list.dataForRegister().get(1);
    private static final String name = list.dataForRegister().get(2);

    @BeforeClass
    @DisplayName("Создаем пользователя для тестов")
    public static void startTest() {
        token = new PostRegister().registerUser(new User(email, password, name))
                .statusCode(HttpStatus.SC_OK)
                .extract().path("accessToken");
    }

    @AfterClass
    @DisplayName("Удаляем тестового пользователя")
    public static void endTests() {
        new DeleteUser().deleteUser(token)
                .statusCode(HttpStatus.SC_ACCEPTED);
    }

    @Test
    @DisplayName("Негативная проверка регистрации с использованием данных ранее использованных для регистрации")
    public void registerUser_CreateWithSameData() {

        PostRegister registerUser = new PostRegister();
        ValidatableResponse response = registerUser.registerUser
                        (new User(email, password, name))
                .statusCode(HttpStatus.SC_FORBIDDEN);

        assertThat(
                response.extract().path("message"),
                equalTo("User already exists"));
    }

    @Test
    @DisplayName("Негативная проверка регистрации без почты")
    public void registerUser_CreateWithoutEmail_Error() {

        PostRegister registerUser = new PostRegister();
        ValidatableResponse response = registerUser.registerUser
                        (new User("", password, name))
                .statusCode(HttpStatus.SC_FORBIDDEN);

        assertThat(
                response.extract().path("message"),
                equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Негативная проверка регистрации без пароля")
    public void registerUser_CreateWithoutPassword_Error() {

        PostRegister registerUser = new PostRegister();
        ValidatableResponse response = registerUser.registerUser
                        (new User(email, "", name))
                .statusCode(HttpStatus.SC_FORBIDDEN);

        assertThat(
                response.extract().path("message"),
                equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Негативная проверка регистрации без имени")
    public void registerUser_CreateWithoutName_Error() {

        PostRegister registerUser = new PostRegister();
        ValidatableResponse response = registerUser.registerUser
                        (new User(email, password, ""))
                .statusCode(HttpStatus.SC_FORBIDDEN);

        assertThat(
                response.extract().path("message"),
                equalTo("Email, password and name are required fields"));
    }
}
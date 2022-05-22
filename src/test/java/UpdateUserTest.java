import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import site.stellarburgers.nomoreparties.model.User;
import site.stellarburgers.nomoreparties.data.GetListUser;
import site.stellarburgers.nomoreparties.requests.user.DeleteUser;
import site.stellarburgers.nomoreparties.requests.user.PatchUpdateUser;
import site.stellarburgers.nomoreparties.requests.user.PostRegister;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@DisplayName("Набор тестов на ручку PostUpdateUser")
public class UpdateUserTest {

    private static String token;
    private static final GetListUser list = new GetListUser();
    private static final String email = list.dataForRegister().get(0);
    private static final String password = list.dataForRegister().get(1);
    private static final String name = list.dataForRegister().get(2);

    @BeforeClass
    @DisplayName("Создаем пользователя для тетсов")
    public static void startTest() {

        PostRegister registerUser = new PostRegister();
        token = registerUser.registerUser(new User(email, password, name))
                .statusCode(HttpStatus.SC_OK)
                .extract().path("accessToken");
    }

    @AfterClass
    @DisplayName("Удаляем тестового пользователя")
    public static void endTests() {

        DeleteUser deleteUser = new DeleteUser();
        deleteUser.deleteUser(token)
                .statusCode(HttpStatus.SC_ACCEPTED);
    }

    @Test
    @DisplayName("Позитивная проверка обновления ПОЧТЫ пользователя с авторизацией")
    public void updateUserEmail_Success() {

        GetListUser list = new GetListUser();

        PatchUpdateUser update = new PatchUpdateUser();
        ValidatableResponse response = update.updateUser(
                new User(list.dataForRegister().get(0),
                        null, null), token).statusCode(HttpStatus.SC_OK);

        assertThat(
                response.extract().path("success"),
                equalTo(true));
    }

    @Test
    @DisplayName("Позитивная проверка обновления ПАРОЛЯ пользователя с авторизацией")
    public void updateUserPassword_Success() {

        GetListUser list = new GetListUser();

        PatchUpdateUser update = new PatchUpdateUser();
        ValidatableResponse response = update.updateUser(
                new User(null, list.dataForRegister().get(1),
                        null), token).statusCode(HttpStatus.SC_OK);

        assertThat(
                response.extract().path("success"),
                equalTo(true));
    }

    @Test
    @DisplayName("Позитивная проверка обновления ИМЕНИ пользователя с авторизацией")
    public void updateUserName_Success() {

        GetListUser list = new GetListUser();

        PatchUpdateUser update = new PatchUpdateUser();
        ValidatableResponse response = update.updateUser(
                new User(null, null,
                        list.dataForRegister().get(2)), token).statusCode(HttpStatus.SC_OK);

        assertThat(
                response.extract().path("success"),
                equalTo(true));
    }

    @Test
    @DisplayName("Негативная проверка обновления ИМЕНИ пользователя без авторизацией")
    public void updateUserName_WithoutLogin_Error() {

        GetListUser list = new GetListUser();

        PatchUpdateUser update = new PatchUpdateUser();
        ValidatableResponse response = update.updateUser(
                new User(null, null,
                        list.dataForRegister().get(2)), "").statusCode(HttpStatus.SC_UNAUTHORIZED);

        assertThat(
                response.extract().path("message"),
                equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Негативная проверка обновления ПОЧТЫ пользователя без авторизацией")
    public void updateUserEmail_WithoutLogin_Error() {

        GetListUser list = new GetListUser();

        PatchUpdateUser update = new PatchUpdateUser();
        ValidatableResponse response = update.updateUser(
                new User(list.dataForRegister().get(0), null,
                        null), "").statusCode(HttpStatus.SC_UNAUTHORIZED);

        assertThat(
                response.extract().path("message"),
                equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Негативная проверка обновления ПАРОЛЯ пользователя без авторизацией")
    public void updateUserPassword_WithoutLogin_Error() {

        GetListUser list = new GetListUser();

        PatchUpdateUser update = new PatchUpdateUser();
        ValidatableResponse response = update.updateUser(
                new User(null, list.dataForRegister().get(1),
                        null), "").statusCode(HttpStatus.SC_UNAUTHORIZED);

        assertThat(
                response.extract().path("message"),
                equalTo("You should be authorised"));
    }
}
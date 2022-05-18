import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import site.stellarburgers.nomoreparties.data.GetListUser;
import site.stellarburgers.nomoreparties.model.User;
import site.stellarburgers.nomoreparties.requests.user.DeleteUser;
import site.stellarburgers.nomoreparties.requests.user.PostLoginUser;
import site.stellarburgers.nomoreparties.requests.user.PostRegister;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Набор тестов на ручку PostLoginUser")
public class LoginUserTest {

    private static String token;
    private static GetListUser list = new GetListUser();
    private static String email = list.dataForRegister().get(0);
    private static String password = list.dataForRegister().get(1);
    private static String name = list.dataForRegister().get(2);

    @BeforeClass
    @DisplayName("Создание пользователя для тестов")
    public static void startTest() {

        PostRegister register = new PostRegister();
        ValidatableResponse response = register.registerUser
                        (new User(email, password, name))
                .statusCode(HttpStatus.SC_OK);
    }

    @AfterClass
    @DisplayName("Удаление пользователя после тестов")
    public static void endTests() {

        DeleteUser deleteUser = new DeleteUser();
        ValidatableResponse response = deleteUser.deleteUser(token)
                .statusCode(HttpStatus.SC_ACCEPTED);

        assertThat(
                response.extract().path("success"),
                equalTo(true));
    }

    @Test
    @DisplayName("Позитивная проверка авторизации пользователя")
    public void loginUserTest_Success() {

        PostLoginUser login = new PostLoginUser();
        ValidatableResponse response = login.loginUser
                (new User(email, password)).statusCode(HttpStatus.SC_OK);

        token = response.extract().path("accessToken");
        assertThat(
                response.extract().path("success"),
                equalTo(true));

    }

    @Test
    @DisplayName("Негативная проверка авторизации с невалидными данными")
    public void loginUser_WithSameData_Error() {

        PostLoginUser login = new PostLoginUser();
        GetListUser list = new GetListUser();
        String email = list.dataForRegister().get(0);
        String password = list.dataForRegister().get(1);

        ValidatableResponse response = login.loginUser
                (new User(email, password)).statusCode(HttpStatus.SC_UNAUTHORIZED);

        assertThat(
                response.extract().path("message"),
                equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Негативная проверка авторизации с невалидной почтой")
    public void loginUser_WithSameEmail_Error() {

        PostLoginUser login = new PostLoginUser();
        GetListUser list = new GetListUser();
        String email = list.dataForRegister().get(0);

        ValidatableResponse response = login.loginUser
                (new User(email, password)).statusCode(HttpStatus.SC_UNAUTHORIZED);

        assertThat(
                response.extract().path("message"),
                equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Негативная проверка авторизации с невалидным паролем")
    public void loginUser_WithSamePassword_Error() {

        PostLoginUser login = new PostLoginUser();
        GetListUser list = new GetListUser();
        String password = list.dataForRegister().get(1);

        ValidatableResponse response = login.loginUser
                (new User(email, password)).statusCode(HttpStatus.SC_UNAUTHORIZED);

        assertThat(
                response.extract().path("message"),
                equalTo("email or password are incorrect"));
    }
}
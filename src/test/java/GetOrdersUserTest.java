import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import site.stellarburgers.nomoreparties.model.User;
import site.stellarburgers.nomoreparties.data.GetListUser;
import site.stellarburgers.nomoreparties.requests.order.GetOrdersUser;
import site.stellarburgers.nomoreparties.requests.user.DeleteUser;
import site.stellarburgers.nomoreparties.requests.user.PostRegister;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@DisplayName("Набор тестов на ручку GetOrdersUser")
public class GetOrdersUserTest {

    private static String token;
    private static GetListUser list = new GetListUser();
    private static String email = list.dataForRegister().get(0);
    private static String password = list.dataForRegister().get(1);
    private static String name = list.dataForRegister().get(2);

    @BeforeClass
    @DisplayName("Создание пользователя для тестов")
    public static void startTest() {

        PostRegister registerUser = new PostRegister();
        ValidatableResponse response = registerUser.registerUser
                        (new User(email, password, name))
                .statusCode(HttpStatus.SC_OK);

        token = response.extract().path("accessToken");
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
    @DisplayName("Позитивная проверка получение списка заказов пользователя")
    public void getOrdersUser_Success() {

        GetOrdersUser orders = new GetOrdersUser();
        ValidatableResponse response = orders.getOrdersUser(token)
                .statusCode(HttpStatus.SC_OK);

        assertThat(
                response.extract().path("success"),
                equalTo(true));
    }

    @Test
    @DisplayName("Негативная пррверка получения списка заказов пользователя без авторизации")
    public void getOrdersUser_WithoutLogin_Error() {

        GetOrdersUser orders = new GetOrdersUser();
        ValidatableResponse response = orders.getOrdersUser("")
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

        assertThat(
                response.extract().path("success"),
                equalTo(false));
    }
}
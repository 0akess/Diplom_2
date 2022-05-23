import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import site.stellarburgers.nomoreparties.data.GetDataUser;
import site.stellarburgers.nomoreparties.model.Ingredients;
import site.stellarburgers.nomoreparties.model.User;
import site.stellarburgers.nomoreparties.requests.order.PostCreateOrder;
import site.stellarburgers.nomoreparties.requests.user.DeleteUser;
import site.stellarburgers.nomoreparties.requests.user.PostRegister;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Набор тестов на ручку PostCreateOrder")
public class CreateOrderTest {

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
    @DisplayName("Позитивная проверка создания заказа с авторизацией")
    public void createOrderWithLogin_Success() {

        PostCreateOrder create = new PostCreateOrder();
        ValidatableResponse response = create.createOrder(new Ingredients(
                        new String[]{"61c0c5a71d1f82001bdaaa6d", "60d3b41abdacab0026a733c6"}),
                token).statusCode(HttpStatus.SC_OK);

        assertThat(
                response.extract().path("name"),
                equalTo("Флюоресцентный бургер"));
    }

    @Test
    @DisplayName("Позитивная проверка создания заказа с ингредиентами")
    public void createOrderWithIngredients_Success() {

        PostCreateOrder create = new PostCreateOrder();
        ValidatableResponse response = create.createOrder(new Ingredients(
                        new String[]{"61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa75", "61c0c5a71d1f82001bdaaa74",
                                "61c0c5a71d1f82001bdaaa75", "61c0c5a71d1f82001bdaaa75", "61c0c5a71d1f82001bdaaa75",
                                "61c0c5a71d1f82001bdaaa75", "61c0c5a71d1f82001bdaaa79", "61c0c5a71d1f82001bdaaa77",
                                "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa76",
                                "61c0c5a71d1f82001bdaaa78", "61c0c5a71d1f82001bdaaa7a", "61c0c5a71d1f82001bdaaa6d"}),
                token).statusCode(HttpStatus.SC_OK);

        assertThat(
                response.extract().path("name"),
                equalTo("Альфа-сахаридный антарианский астероидный традиционный-галактический минеральный " +
                        "флюоресцентный фалленианский экзо-плантаго space люминесцентный био-марсианский бургер"));
    }

    @Test
    @DisplayName("Позитивная проверка создания заказа без авторизации")
    public void createOrderWithoutLogin_Success() {
        // Нет инфы в доке баг, или не баг, в целом можно же заказывать без регистрации в других апках

        PostCreateOrder create = new PostCreateOrder();
        ValidatableResponse response = create.createOrder(new Ingredients(
                        new String[]{"61c0c5a71d1f82001bdaaa6d", "60d3b41abdacab0026a733c6"}), "")
                .statusCode(HttpStatus.SC_OK);

        assertThat(
                response.extract().path("name"),
                equalTo("Флюоресцентный бургер"));
    }

    @Test
    @DisplayName("Негативная проверка создания заказа без ингредиентов")
    public void createOrderWithoutIngredients_Error() {

        PostCreateOrder create = new PostCreateOrder();
        ValidatableResponse response = create.createOrder(
                new Ingredients(new String[]{}), token).statusCode(HttpStatus.SC_BAD_REQUEST);

        assertThat(
                response.extract().path("message"),
                equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Негативная проверка создания заказа с неверным хешом ингредиента")
    public void createOrderWithIncorrectHash_Error() {

        PostCreateOrder create = new PostCreateOrder();
        create.createOrder(new Ingredients(
                        new String[]{"61c0c5a71d1f82001bd34dsdd", "60d3b41abd34dasda333c6"}),
                token).statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
package site.stellarburgers.nomoreparties.data;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class GetDataUser {

    /**
     * Метод отдает лист поля
     * 0 - почта
     * 1 - пароль
     * 2 - имя
     */
    public List<String> dataForRegister() {

        List<String> list = new ArrayList<>();
        list.add(RandomStringUtils.randomAlphabetic(10) + "@mfnf.ru");
        list.add(RandomStringUtils.randomAlphabetic(10));
        list.add(RandomStringUtils.randomAlphabetic(10));

        return list;
    }

    public String getRandomToken(){
        return RandomStringUtils.randomAlphabetic(25);
    }
}
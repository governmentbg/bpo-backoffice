package bg.duosoft.ipas.test;

import java.util.Random;

/**
 * User: ggeorgiev
 * Date: 27.3.2019 г.
 * Time: 18:31
 */
public class TestUtils {
    public static String generateRandomString(int length){
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static void main(String[] args) {
//        System.out.println(generateRandomString(15).length());
    }
}

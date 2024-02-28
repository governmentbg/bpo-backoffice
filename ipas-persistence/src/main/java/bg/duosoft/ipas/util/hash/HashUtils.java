package bg.duosoft.ipas.util.hash;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class HashUtils {

    public static String sha256Hex(String data) {
        return Hashing.sha256().hashString(data, StandardCharsets.UTF_8).toString();
    }

}

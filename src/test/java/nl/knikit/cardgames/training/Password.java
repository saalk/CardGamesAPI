package nl.knikit.cardgames.training;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Password {

    public static void main(String[] args) throws UnsupportedEncodingException {
        // when called make a base64 encodes string from the arguments passed (password)
        // 1: change the "password" in a password
        // 2: compile with: javac src/main/java/nl/knikit/cardgames/utils/Password.java
        // 3: execute with: java -cp src/main/java nl.knikit.cardgames.training.Password
        // cp = classpath, a path of directories in which .class files are located
        // 4: change the "cGFzc3dvcmQ=" in the result, redo step 2 and 3 and see if ok

        String password = "password";
        String urlEncoded = encode(password);
        System.out.println("Using URL Alphabet the normal string: " + password + " is: " +
                urlEncoded);

        String obfuscatedPassword = "cGFzc3dvcmQ=";
        String urlDecoded = decode(obfuscatedPassword);
        System.out.println("Using URL Alphabet the decoded string: " + obfuscatedPassword + " is: " +
                urlDecoded);
    }

    public static String encode(String property) throws UnsupportedEncodingException {
        String urlEncoded = Base64.getUrlEncoder().encodeToString(property.getBytes("utf-8"));
        return urlEncoded;
    }

    public static String decode(String property) throws UnsupportedEncodingException {
        byte[] asBytes = Base64.getDecoder().decode(property);
        String urlDecoded = new String(asBytes, "utf-8");
        return urlDecoded;
    }
}

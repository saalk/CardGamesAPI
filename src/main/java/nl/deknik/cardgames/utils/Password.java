package nl.deknik.cardgames.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Password {

    /**
     * Soorten encryptie:
     * <p>
     * Symmetry: zender + ontvanger hebben dezelfde sleutel
     * <p>
     * Blowfish is een symmetrische blokversleutelingstechniek, die met
     * sleutellengtes van 32 tot 448 bits overweg kan. Blowfish is een krachtig
     * algoritme en voor zover bekend is het nog niet gebroken.
     * <p>
     * <p>
     * Asymmetry: zender + ontvanger hebben beide een set van public and private
     * key
     * <p>
     * Hashing: transform string into a shorter hash
     * <p>
     * Hashing is the transformation of a string of characters into a usually
     * shorter fixed-length value or key that represents the original string.
     * Hashing is used to :
     * <p>
     * - index and retrieve items in a database one-way: no need to derive but
     * the hash should not produce the same hash for 2 inputs (collision) and
     * used in many encryption algorithms. -
     */

    enum EncryptionKind {
        SYMMETRY, ASYMMETRY, HASHING
    }

    ;

    enum OneWayHashing {
        SHA_0, SHA_1, SHA_2, DIVISION_REMAINDER
    }

    ;

    enum SymmetryEncryption {
        BLOWFISH, TWOFISH, DES, AES
    }

    ;

    enum AsymmetryEncryption {
        RSA, TWOFISH, DES, AES
    }

    ;

    private static final String MY_ENCRYPTION_KEY = "SleutelVan16Lang";
    private static final String UNICODE_FORMAT = "UTF8";

	/*
     * private String publicKey; private String privateKey; private String
	 * password;
	 */

    public String encode(SymmetryEncryption inputSym, String inputPassword) throws Exception {

        if (!(inputSym == SymmetryEncryption.BLOWFISH)) {
            throw new Exception();
        }

        // use a key known in both encrypt and decrypt
        byte[] keyData = (MY_ENCRYPTION_KEY).getBytes(UNICODE_FORMAT);

        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] hasil = cipher.doFinal(inputPassword.getBytes());
        return new BASE64Encoder().encode(hasil);

    }

    public String decode(SymmetryEncryption inputSym, String inputSymPassword) throws Exception {

        if (!(inputSym == SymmetryEncryption.BLOWFISH)) {
            throw new Exception();
        }

        // use a key known in both encrypt and decrypt
        byte[] keyData = (MY_ENCRYPTION_KEY).getBytes(UNICODE_FORMAT);

        // Your myKey variable lenght must be multiple of 8
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] hasil = cipher.doFinal(new BASE64Decoder().decodeBuffer(inputSymPassword));
        return new String(hasil);

    }

}

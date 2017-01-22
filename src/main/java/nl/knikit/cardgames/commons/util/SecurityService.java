package nl.knikit.cardgames.commons.util;

import com.gregmarut.commons.encryption.Encryption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

/**
 * Service that provides encrypting and decrypting of {@link String} values.
 */
@Component
@Slf4j
@Lazy
public class SecurityService {

    @Autowired
    private Encryption encryption;

    private String sharedKey;

    @Resource(name = "sharedSecret")
    public void setSharedKey(String sharedKey) {
        this.sharedKey = sharedKey;
    }

    /**
     * Decrypt specified cypher text.
     * <p>
     * Will throw {@link SecurityException} if the supplied input cipher text is not valid.
     *
     * @param input
     *            The value to decrypt.
     * @return the decrypted value.
     */
    public String decrypt(final String input) {
        try {
            byte[] b = input.getBytes();
            return new String(encryption.decrypt(b));
        } catch (final Exception e) {
            throw new WebApplicationException("ErrorResponse decrypting value: " + input, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Encrypt specified value.
     * <p>
     * Will throw {@link SecurityException} if the supplied input text is not valid.
     *
     * @param input
     *            The value to encrypt.
     * @return the encrypted value.
     */
    public String encrypt(final String input) {
        try {
            byte[] b = input.getBytes();
            return new String(encryption.encrypt(b));
        } catch (final Exception e) {
            throw new WebApplicationException("ErrorResponse encrypting value: " + input, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}

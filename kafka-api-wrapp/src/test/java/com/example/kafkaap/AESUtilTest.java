package com.example.kafkaap;

import com.example.kafkaap.wrapper.util.AESUtil;  // ✅ This import is required
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;  // ✅ Required for assertEquals, assertThrows

public class AESUtilTest {

    private final String secret = "12345678901234567890123456789012"; // ✅ 32-char AES key

    @Test
    void testEncryptDecrypt_Success() throws Exception {
        String input = "{\"UserId\":123}";
        String encrypted = AESUtil.encrypt(input, secret);
        String decrypted = AESUtil.decrypt(encrypted, secret);
        assertEquals(input, decrypted);
    }

    @Test
    void testDecrypt_InvalidCipher() {
        String invalid = "InvalidCipherText";
        assertThrows(RuntimeException.class, () -> AESUtil.decrypt(invalid, secret));
    }

    @Test
    void testEncrypt_NotNull() throws Exception {
        String input = "{\"Mobile\":9999999999}";
        String encrypted = AESUtil.encrypt(input, secret);
        assertNotNull(encrypted);
        assertNotEquals(input, encrypted); // ✅ should not match plain text
    }
}

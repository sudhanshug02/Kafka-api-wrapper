package com.example.kafkaap.wrapper.test;

import com.example.kafkaap.wrapper.util.ChecksumUtil; // ✅ add this
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*; // ✅ fixed import

public class ChecksumUtilTest {

    @Test
    void testChecksum_Consistency() throws Exception {
        String input = "{\"Amount\":5000}";
        String checksum1 = ChecksumUtil.generateMD5(input);
        String checksum2 = ChecksumUtil.generateMD5(input);
        assertEquals(checksum1, checksum2);
    }

    @Test
    void testChecksum_DifferentInputs() throws Exception {
        String a = "{\"UserId\":123}";
        String b = "{\"UserId\":124}";
        assertNotEquals(ChecksumUtil.generateMD5(a), ChecksumUtil.generateMD5(b));
    }
}

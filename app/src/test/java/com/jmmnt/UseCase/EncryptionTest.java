package com.jmmnt.UseCase;

import static org.junit.Assert.*;

import org.junit.Test;

public class EncryptionTest {

    @Test
    public void encrypt() {
        assertEquals("MuGWlZ8+2oDqw1XgzgcZiw==", Encryption.encrypt("Nicklas"));
    }

    @Test
    public void decrypt() {
        assertEquals("Nicklas", Encryption.decrypt("MuGWlZ8+2oDqw1XgzgcZiw=="));
    }
}
package com.g9team04.techmind.conteudo.internal;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class HashUtils {
    private HashUtils() {}
    public static String sha256( String texto) {
        try{
            var digest = MessageDigest.getInstance("SHA-256");
            var hashBytes = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Erro calcular hash do texto", e);
        }
    }
}

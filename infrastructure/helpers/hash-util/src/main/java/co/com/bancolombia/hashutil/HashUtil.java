package co.com.bancolombia.hashutil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class HashUtil {

    private static final String SHA256_ALGORITHM = "SHA-256";
    private static final String MD5_ALGORITHM = "MD5";
    private static final int ARGON2_MEMORY_MIB = 19 * 1024;
    private static final int ARGON2_ITERATIONS = 2;
    private static final int ARGON2_PARALLELISM = 1;


    public static String sha256(String input) {
        return hashWithAlgorithm(input, SHA256_ALGORITHM);
    }

    public static String md5(String input) {
        return hashWithAlgorithm(input, MD5_ALGORITHM);
    }

    public static String argon2id(String input) {
        Argon2 argon2 = Argon2Factory.create(
                Argon2Factory.Argon2Types.ARGON2id
        );

        try {
            return argon2.hash(ARGON2_ITERATIONS, ARGON2_MEMORY_MIB, ARGON2_PARALLELISM, input.toCharArray());
        } finally {
            argon2.wipeArray(input.toCharArray());
        }
    }

    private static String hashWithAlgorithm(String input, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar hash con " + algorithm, e);
        }
    }
}

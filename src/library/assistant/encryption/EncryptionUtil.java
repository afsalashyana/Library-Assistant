package library.assistant.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EncryptionUtil {

    private final static Logger LOGGER = LogManager.getLogger(EncryptionUtil.class.getName());
    private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5PADDING";
    private static final String SECRET_KEY_SPEC = "AES";
    private static final File KEY_STORE = new File("store/key.spec");
    private static final Lock LOCK = new ReentrantLock(true);

    public static String encrypt(String plainText) {
        LOCK.tryLock();
        try {
            CipherSpec spec = getCipherSpec();
            if (spec == null || !spec.isValid()) {
                throw new RuntimeException("Cant load encryption");
            }
            return encrypt(spec.getKey(), spec.getIV(), plainText);
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, "Encryption failure", ex);
        } finally {
            LOCK.unlock();
        }
        return null;
    }

    public static String decrypt(String cipherText) {
        LOCK.lock();
        try {
            CipherSpec spec = getCipherSpec();
            if (spec == null || !spec.isValid()) {
                throw new RuntimeException("Cant load encryption");
            }
            return decrypt(spec.getKey(), spec.getIV(), cipherText);
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, "Encryption failure", ex);
        } finally {
            LOCK.unlock();
        }
        return null;
    }

    private static String encrypt(byte[] key, byte[] initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, SECRET_KEY_SPEC);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static String decrypt(byte[] key, byte[] initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, SECRET_KEY_SPEC);

            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void init() throws Exception {
        CipherSpec spec = getCipherSpec();
        if (spec == null || !spec.isValid()) {
            LOGGER.log(Level.INFO, "Preparing new cipher setup");
            byte[] key = generateSecureKey();
            byte[] initVector = prepareIV();
            spec = new CipherSpec(key, initVector);
            writeKey(spec);
        } else {
            LOGGER.log(Level.INFO, "Encryption params are loaded.");
        }
    }

    private static byte[] generateSecureKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(SECRET_KEY_SPEC);
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();
        byte[] data = secretKey.getEncoded();
        return data;
    }

    private static byte[] prepareIV() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        String randomVal = String.valueOf(new Random(System.currentTimeMillis()).nextLong());
        byte[] hash = digest.digest(randomVal.getBytes(StandardCharsets.UTF_8));
        return Arrays.copyOfRange(hash, 0, 16);
    }

    private static void writeKey(CipherSpec spec) throws Exception {
        KEY_STORE.mkdirs();
        if (KEY_STORE.exists()) {
            LOGGER.log(Level.INFO, "Clearing existing encryption info");
            KEY_STORE.delete();
        } else {
            KEY_STORE.createNewFile();
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(KEY_STORE, false))) {
            out.writeObject(spec);
        }
        if (KEY_STORE.exists()) {
            LOGGER.log(Level.INFO, "Added new encryption setup");
        }
    }

    private static CipherSpec getCipherSpec() throws Exception {
        if (KEY_STORE.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(KEY_STORE))) {
                return (CipherSpec) in.readObject();
            }
        }
        return null;
    }
}

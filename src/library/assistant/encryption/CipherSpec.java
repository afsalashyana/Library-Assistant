package library.assistant.encryption;

import java.io.Serializable;

/**
 * @author Villan
 */
public class CipherSpec implements Serializable {

    private final byte[] key;
    private final byte[] iv;

    public CipherSpec(byte[] key, byte[] iv) {
        this.key = key;
        this.iv = iv;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getIV() {
        return iv;
    }

    public boolean isValid() {
        return key != null && iv != null;
    }

    @Override
    public String toString() {
        return "CipherSpec{" + "key=" + key + ", iv=" + iv + '}';
    }
    
}

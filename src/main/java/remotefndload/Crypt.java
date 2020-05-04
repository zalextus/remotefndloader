package remotefndload;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {

    private static byte[] iv =
    { '0', '1', '2', '3', '4', '5', '6', '7'};

    public static String encrypt(String password) throws Exception {
        SecretKeySpec desKey = new SecretKeySpec(iv, "DES");
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, desKey);
        byte[] encrypted = desCipher.doFinal(password.getBytes());
        return toHexString(encrypted);
    }

    public static String decrypt(String encryptedHex) throws Exception {
        byte[] encrypted = fromHexString(encryptedHex);
        SecretKeySpec desKey = new SecretKeySpec(iv, "DES");
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desCipher.init(Cipher.DECRYPT_MODE, desKey);
        byte[] decrypted = desCipher.doFinal(encrypted);

        return new String(decrypted);
    }

    /*
   * Converts a byte to hex digit and writes to the supplied buffer
   */

    private static void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars =
        { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
          'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    /*
   * Converts a byte array to hex string
   */

    private static String toHexString(byte[] block) {
        StringBuffer buf = new StringBuffer();
        int len = block.length;
        for (int i = 0; i < len; i++)
            byte2hex(block[i], buf);
        return buf.toString();
    }

    private static byte[] fromHexString(String encryptedHex) {
        int length = encryptedHex.length() / 2;
        byte[] buffer = new byte[length];
        for (int i = 0; i < length; i++)
            buffer[i] =
                    (byte)Integer.parseInt(encryptedHex.substring(i * 2, i *
                                                                  2 + 2), 16);
        return buffer;
    }

}


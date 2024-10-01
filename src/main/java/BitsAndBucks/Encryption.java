package BitsAndBucks;

public class Encryption {
    public static String encrypt(String text) {
        int shift=3;
        String encrypted = "";
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'a' & c <= 'z') {
                encrypted += (char)(97+ (c - 97 + shift) % 26);
            } else if (c >= 'A' && c <= 'Z') {
                encrypted += (char) ('A' + (c - 'A' + shift) % 26);
            }
            else if (c >= '0' && c <= '9') {
                encrypted += (char) ('0' + (c - '0' + shift) % 10);
            }
        }
        return encrypted;
    }
    public static String decrypt(String encryptedText) {
        int shift=3;
        String decrypted = "";
        for (int i = 0; i < encryptedText.length(); i++) {
            char character = encryptedText.charAt(i);
            if (character >= 'a' && character <= 'z') {
                decrypted += (char) ('a' + (character - 'a' - shift + 26) % 26);
            } else if (character >= 'A' && character <= 'Z') {
                decrypted += (char) ('A' + (character - 'A' - shift + 26) % 26);
            }
            else if (character >= '0' && character <= '9') {
                decrypted += (char) ('0' + (character - '0' - shift + 10) % 10);
            }
        }
        return decrypted;
    }
}

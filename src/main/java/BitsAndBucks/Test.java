package BitsAndBucks;

public class Test {
    //CUR5060324629
    //12345678
    //CUR1327754525
    //Mo6341588
    //SAV3450240100

    public static void main(String[] args) {
        System.out.println(Encryption.decrypt("0123456789"));
        // 0+3, 1+3, 2+3,
        System.out.println(Encryption.decrypt("abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz".toUpperCase()));
    }
}

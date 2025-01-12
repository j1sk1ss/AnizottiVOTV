package org.cordell.anizotti.anizottiVOTV.computer.finder;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;


@Getter
@Setter
public class Signal {
    public static Signal generateSignal(int maxX, int maxY, String data) {
        return new Signal(new Random().nextInt(maxX), new Random().nextInt(maxY), new Random().nextInt(6), xorEncryptDecrypt(data));
    }

    private static final String KEY = "secret-key-anizotti-votv";

    private static String xorEncryptDecrypt(String data) {
        char[] key = KEY.toCharArray();
        char[] input = data.toCharArray();
        char[] result = new char[input.length];

        for (int i = 0; i < input.length; i++) {
            result[i] = (char) (input[i] ^ key[i % key.length]);
        }

        return new String(result);
    }

    // Non-static logic

    public Signal(int x, int y, int type, String data) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.data = data;
        this.isDecrypted = false;
    }

    private int x;
    private int y;
    private int type;
    private String data;
    private boolean isDecrypted;

    public String decryptData() {
        return xorEncryptDecrypt(data);
    }
}

package ru.zubkov.collections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.Normalizer;

public class Task2ConsoleMain {
    public static void main(String[] args) throws IOException {
        // Читаем в системной кодировке (Windows: Cp1251/866; PowerShell часто UTF-8, но так надёжнее)
        Charset cs = Charset.defaultCharset();
        var br = new BufferedReader(new InputStreamReader(System.in, cs));

        System.out.println("[info] Console charset: " + cs.displayName());
        System.out.print("Введите строку s: ");
        String s = br.readLine();
        System.out.print("Введите строку t: ");
        String t = br.readLine();

        // Нормализуем Юникод (случаи комбинированных символов)
        if (s != null) s = Normalizer.normalize(s, Normalizer.Form.NFC);
        if (t != null) t = Normalizer.normalize(t, Normalizer.Form.NFC);

        boolean result = AnagramChecker.isValidAnagram(s, t);
        System.out.println(result);
    }
}

package ru.litvinov.charsetEncoder;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static final Map<String,String> sverka = new LinkedHashMap<>();

    static {
        sverka.put("smallRus","абвгдеёжзийклмнопрстуфхчшщьыъэюя");
        sverka.put("bigRus","АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЧШЩЬЫЪЭЮЯ");
        sverka.put("smallEng","abcdefgjijklmnopqrstuvwxyz");
        sverka.put("bigEng","ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        sverka.put("numbers","1234567890");
        sverka.put("symbols","~!@#$%^&*()_+-=`{}|;:'<>?,./\"\\ ");
    }

    public static void main(String[] args) throws IOException {
        String text = "Текст в кодировке text v kodirovke 123456";
        writeFile(text, "utf8.txt", false, Charset.forName("UTF-8"));
        writeFile(text, "textWin1251.txt", false, Charset.forName("Windows-1251"));
        writeFile(text, "UTF16.txt", false, Charset.forName("UTF-16"));
        byte[] bytes = inputStreamToByte(new FileInputStream("UTF16.txt"),1024);
        charsetDetector(bytes);
    }

    public static synchronized void writeFile(String text, String path, boolean b, Charset charset) {
        //System.out.println(text);
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path, b), charset)) {
            //writer.write(text);
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] inputStreamToByte(InputStream in, Integer countBytes) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (countBytes != null) {
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        } else {
            byte[] bytes = new byte[countBytes];
            baos.write(in.read(bytes,0,countBytes));
        }
        baos.flush();
        in.close();
        byte[] bb = baos.toByteArray();
        System.out.println(Arrays.toString(bb));
        return baos.toByteArray();
    }

    public static void charsetDetector(byte[] in) throws IOException {
        final String[] encodings = {"UTF-8", "Windows-1251", "US-ASCII", "UTF-16", "ISO-8859-1", "UTF-16BE", "UTF-16LE"};
        Map<String,Double> equalsPercentMap = new LinkedHashMap<>();
        for (String encoding : encodings) {
            InputStream tempIn = new ByteArrayInputStream(in);
            BufferedReader reader = new BufferedReader(new InputStreamReader(tempIn, encoding));
            StringBuilder sb = new StringBuilder();
            while (reader.ready()) {
                sb.append(reader.readLine());
            }
            System.out.println(encoding + ":");
            System.out.println(sb.toString());
            System.out.println(encodingResult(sb.toString()));
        }
    }

    public static double encodingResult(String inputString) {
        String[] strings = inputString.split("");
        String[] allSymbols = sverka.values().stream().reduce("",(x,y)->x+y).split("");
        Map<String,String> statistics = new LinkedHashMap<>();
        double countOfEquals = 0.0D;
        for(String s : strings) {
            if (Arrays.asList(allSymbols).contains(s)){
                countOfEquals++;
            }
        }
        double percentOfEquals = 100.0D * (countOfEquals / strings.length);
        System.out.println("Процент совпадения:" + percentOfEquals);
        if (countOfEquals == 0.0) {
            return 0.0D;
        } else {
            return percentOfEquals;
        }
    }
}
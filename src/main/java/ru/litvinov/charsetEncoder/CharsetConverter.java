package ru.litvinov.charsetEncoder;

import java.io.*;
import java.nio.charset.Charset;

public class CharsetConverter {

    public static String convertString(
            String inputString,
            String from,   //encoding of input
            String to) {

        byte[] input = inputString.getBytes(Charset.forName(from));
        String output = new String(input, Charset.forName(to));
        return output;
    }

    public static void convert(
            InputStream inputStream,
            OutputStream outputStream,
            String from,   //encoding of input
            String to)     //encoding of output
            throws IOException {

        // Use default encoding if no encoding is specified.
        if (from == null) from = System.getProperty("file.encoding");
        if (to == null) to = System.getProperty("file.encoding");

        // Set up character stream
        Reader r = new BufferedReader(new InputStreamReader(inputStream, from));
        Writer w = new BufferedWriter(new OutputStreamWriter(outputStream, to));

        char[] buffer = new char[4096];
        int len;
        while ((len = r.read(buffer)) != -1) {
            w.write(buffer, 0, len);
        }
        r.close();
        w.flush();
        w.close();
    }

    public static void convert(
            String infile, //input file name, if null reads from console/stdin
            String outfile, //output file name, if null writes to console/stdout
            String from,   //encoding of input file (e.g. UTF-8/windows-1251, etc)
            String to)     //encoding of output file (e.g. UTF-8/windows-1251, etc)
            throws IOException, UnsupportedEncodingException {
        // set up byte streams
        InputStream in;
        if (infile != null) {
            in = new FileInputStream(infile);
        } else {
            in = System.in;
        }

        OutputStream out;
        if (outfile != null) {
            out = new FileOutputStream(outfile);
        } else {
            out = System.out;
        }

        // Use default encoding if no encoding is specified.
        if (from == null) from = System.getProperty("file.encoding");
        if (to == null) to = System.getProperty("file.encoding");

        // Set up character stream
        Reader r = new BufferedReader(new InputStreamReader(in, from));
        Writer w = new BufferedWriter(new OutputStreamWriter(out, to));

        char[] buffer = new char[4096];
        int len;
        while ((len = r.read(buffer)) != -1) {
            w.write(buffer, 0, len);
        }
        r.close();
        w.flush();
        w.close();
    }

    public static void convert(
            byte[] inputBytes, //input
            byte[] outputBytes, //output
            String from,   //encoding of input file (e.g. UTF-8/windows-1251, etc)
            String to)     //encoding of output file (e.g. UTF-8/windows-1251, etc)
            throws IOException, UnsupportedEncodingException {

        InputStream in = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Use default encoding if no encoding is specified.
        if (from == null) from = System.getProperty("file.encoding");
        if (to == null) to = System.getProperty("file.encoding");

        // Set up character stream

        Reader r = new BufferedReader(new InputStreamReader(in, from));
        Writer w = new BufferedWriter(new OutputStreamWriter(out, to));

        char[] buffer = new char[4096];
        int len;
        while ((len = r.read(buffer)) != -1) {
            w.write(buffer, 0, len);
        }
        r.close();
        w.flush();
        w.close();
        outputBytes = out.toByteArray();
    }
}

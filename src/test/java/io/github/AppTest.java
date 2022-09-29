package io.github;


import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void probeTest() throws IOException {
        Assert.assertEquals("text/plain", Files.probeContentType(Paths.get("foo/test.txt")));
        // Assert.assertEquals("application/vndms-excel", Files.probeContentType(Paths.get("foo/test.xls")));
        // Assert.assertEquals("chemical/x-pdb", Files.probeContentType(Paths.get("foo/test.xyz")));
        // Assert.assertNull(Files.probeContentType(Paths.get("foo/test.bib")));
        // Assert.assertNull(Files.probeContentType(Paths.get("foo/txt")));


        // http://tika.apache.org/
        // String mimeType = URLConnection.guessContentTypeFromName(new File("foo/test.txt").getName());
        // String mimeType = URLConnection.guessContentTypeFromName(new File("foo/test.xls").getName());
        // System.out.println("mimeType = " + mimeType);


        // File file = new File("product.png");
        // MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        // mimeType = fileTypeMap.getContentType(file.getName());
        // System.out.println("mimeType = " + mimeType);
    }


    @Test
    public void test_mime() throws IOException {
        // String line = "    text/html                             html htm shtml;";
        // String[] split = line.split("\\s");
        // Arrays.stream(split)
        //         .filter(s -> !s.trim().isEmpty())
        //         .forEach(line2 -> {
        //             System.out.println(line2);
        //         });
    }

    @Test
    public void test_mime_write() throws IOException {
        Map<String, String> mimeMap = new HashMap<>(128);

        File file = new File("src/test/resources/mime.txt");
        List<String> list = Files.readAllLines(file.toPath());
        list.stream().forEach(line -> {
            // System.out.println(line);
            processLine(line, mimeMap);
        });

        StringBuffer stringBuffer = new StringBuffer();
        mimeMap.entrySet().stream().forEach(entry -> {
            stringBuffer
                    .append(entry.getKey()).append("=").append(entry.getValue())
                    .append(System.lineSeparator());
        });


        File toFile = new File("src/test/resources/content-type.properties");
        Files.write(toFile.toPath(), stringBuffer.toString().getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
    }


    public static void processLine(String line, Map<String, String> mimeMap) {
        String[] split = line.split("\\s");
        List<String> stringList = Arrays.stream(split)
                .filter(s -> !s.trim().isEmpty())
                .collect(Collectors.toList());

        if (stringList.size() >= 2) {
            String mimeType = stringList.get(0);

            IntStream.range(1, stringList.size()).forEach(index -> {
                String suffix = stringList.get(index);
                if (index == (stringList.size() - 1)) {
                    // remove ";"
                    suffix = suffix.substring(0, suffix.length() - 1);
                }
                mimeMap.put(suffix, mimeType);
            });
        }
    }

}

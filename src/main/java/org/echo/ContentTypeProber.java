package org.echo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;
import java.util.Properties;

/**
 * 文件的 content type 探测工具类，通过 SPI 自动注册到 JVM FileTypeDetector，
 * 通过调用 Files.probeContentType(Path) 使用。
 */
public class ContentTypeProber extends FileTypeDetector {

    public ContentTypeProber() {

    }

    /**
     * Content type properties file path.
     */
    private static final String CONTENT_TYPE_PROPS_PATH = "config/content-type.properties";


    /**
     * Content type properties loaded from file CONTENT_TYPE_PROPS_PATH.
     */
    private static final Properties CONTENT_TYPE_PROPS = new Properties();

    static {
        // Loading content type when class loaded.
        loadContentTypes();
    }

    /**
     * 加载 content type.
     */
    private static void loadContentTypes() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream in = classLoader.getResourceAsStream(CONTENT_TYPE_PROPS_PATH)) {
            if (in != null) {
                CONTENT_TYPE_PROPS.load(in);
            } else {
                // log.warn("[错误] content type file 不存在");
            }
        } catch (IOException ex) {
            // log.warn("[错误] 加载 content type file 异常: {} ", ex.getMessage());
        }

        // log.info("[结束] 加载 content type file");
    }

    /**
     * 获取传入的 Path 的文件名后缀.
     *
     * @param path 路径
     * @return 返回文件名后缀
     */
    public static String getFilenameExtension(Path path) {
        String name = path.getFileName().toString();
        int dot = name.lastIndexOf(".");
        return dot == -1 ? "" : name.substring(dot + 1);
    }


    /**
     * 根据文件后缀名获取 content type，如果返回 null 则继续调用系统中其他注册的 FileTypeDetector 继续尝试获取。
     *
     * @param path 文件路径
     * @return 返回文件的 content type，如果不存在则返回 null
     */
    @Override
    public String probeContentType(Path path) {
        String ext = getFilenameExtension(path);
        return CONTENT_TYPE_PROPS.getProperty(ext);
    }
}
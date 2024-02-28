package bg.duosoft.ipas.util.attachment;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.springframework.core.io.FileSystemResource;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class AttachmentUtils {

    public static final String DOCX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    public static Map<String, String> contentTypeToExtensionMap;

    public static Map<String, String> tikaMimeTypeConvertMap;


    static {
        tikaMimeTypeConvertMap = new HashMap<>();
        tikaMimeTypeConvertMap.put("application/x-tika-msoffice", "application/msword");
        tikaMimeTypeConvertMap.put("application/zip", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        tikaMimeTypeConvertMap.put("application/pdf", "application/pdf");

        contentTypeToExtensionMap = new HashMap<>();
        contentTypeToExtensionMap.put("application/msword", ".doc");
        contentTypeToExtensionMap.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx");
        contentTypeToExtensionMap.put("application/pdf", ".pdf");
        contentTypeToExtensionMap.put("application/zip", ".docx");// Tika use application/zip for docx files
    }

    public static String getContentType(byte[] bytes) {
        try {
            TikaConfig tikaConfig = new TikaConfig();
            return tikaConfig.getDetector().detect(new ByteArrayInputStream(bytes), new Metadata()).toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Cannot get file content type !");
        }
    }

    public static void writeData(HttpServletResponse response, byte[] data, String contentType, String fileName) {
        try {
            if (Objects.nonNull(contentType))
                response.setContentType(contentType);
            if (Objects.nonNull(fileName))
                response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20"));

            OutputStream outputStream = response.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            response.flushBuffer();
            outputStream.close();
        } catch (IOException e) {
            //TODO
            log.error("Error writing image to response!", e);
        }
    }

    public static byte[] readFileSystemResource(String fileSystemPath) {
        try {
            FileSystemResource resource = new FileSystemResource(fileSystemPath);
            try (FileInputStream fr = new FileInputStream(resource.getFile())) {
                return fr.readAllBytes();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Cannot read file system resource ! Path: " + fileSystemPath);
        }
    }

}

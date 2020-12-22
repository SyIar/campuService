package cn.edu.fudan.campuservice.utils;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    private static String getFileName(String fileName) {
        int index = fileName.lastIndexOf(".");
        final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowTimeStr = sDateFormat.format(new Date());
        fileName = fileName.substring(0, index) + "_" + nowTimeStr + fileName.substring(index);
        return fileName;
    }

    public static String getUploadPath() {
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!path.exists()) {
            path = new File("");
        }
        File upload = new File(path.getAbsolutePath(), "static/upload/");
        if (!upload.exists()) {
            upload.mkdirs();
        }
        return upload.getAbsolutePath();
    }

    public static String saveFile(MultipartFile file) throws Exception {
        String fileName = FileUtil.getFileName(file.getOriginalFilename());
        String filepath = FileUtil.getUploadPath();
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(new File(filepath + File.separator + fileName)));
        out.write(file.getBytes());
        out.flush();
        return fileName;
    }
}

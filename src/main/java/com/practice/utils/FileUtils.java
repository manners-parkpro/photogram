package com.practice.utils;

import com.practice.domain.types.FileType;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

public class FileUtils {

    /* Result Code */
    public static final String RS_STATUS = "rs_st";
    public static final String RS_MESSAGE = "rs_msg";
    public static final String RS_DATA = "rs_data";

    /* Result Status Code */
    public static final int RS_SUCC = 0;
    public static final int RS_FAIL = 1;
    public static final int RS_ERROR = 2;

    /* Not Allowed File Extension */
    public static final String FILE_PERMIT_EXTENSION = "hwp,pdf,xls,xlsx,doc,docx,ppt,pptx,zip,alz,jpg,jpeg,png,txt";

    /* Image File Extension */
    public static final String IMAGE_FILE_EXTENSION = "bmp,jpg,jpeg,gif,png,psd,ai,pic";

    /* Audio File Extension */
    public static final String AUDIO_FILE_EXTENSION = "mp3,mp4,ogg,wma,wav,au,rm,mid";

    public static boolean permitExtensionCheck(String originalFileName) {
        if (!StringUtils.hasText(originalFileName)) return false;

        String fileExtension = FilenameUtils.getExtension(originalFileName);

        if (Arrays.asList(StringUtils.commaDelimitedListToStringArray(FILE_PERMIT_EXTENSION.trim().toLowerCase())).contains(fileExtension.trim().toLowerCase())) {
            return true;
        }

        return false;
    }

    public static FileType defineMediaType(String fileExtension) {
        FileType fileType = FileType.Document;

        if (Arrays.asList(StringUtils.commaDelimitedListToStringArray(IMAGE_FILE_EXTENSION.trim().toLowerCase())).contains(fileExtension.trim().toLowerCase()))
            fileType = FileType.Image;

        if (Arrays.asList(StringUtils.commaDelimitedListToStringArray(AUDIO_FILE_EXTENSION.trim().toLowerCase())).contains(fileExtension.trim().toLowerCase()))
            fileType = FileType.Audio;

        return fileType;
    }

    public static String getFileName(String userAgent, String file_name) {
        String f = "";

        if (StringUtils.isEmpty(userAgent) || StringUtils.isEmpty(file_name)) return f;

        if (userAgent.indexOf("Trident") > -1) {
            try {
                f = URLEncoder.encode(file_name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            f = file_name.replaceAll("\\+", "%20");
        } else if( userAgent.indexOf("MSIE") > -1 ) {
            try {
                f = URLEncoder.encode(file_name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            f = file_name.replaceAll("\\+", " ");
        } else if( userAgent.indexOf("Mozilla") > -1 ) {
            try {
                f = new String(file_name.getBytes("UTF-8"), "ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                f = URLEncoder.encode(file_name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return f;
    }
}

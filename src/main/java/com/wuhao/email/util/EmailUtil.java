package com.wuhao.email.util;

import com.wuhao.email.Enum.EmailCode;
import com.wuhao.email.excption.MyExcption;
import com.wuhao.email.form.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class EmailUtil {
    public static final Integer MAX_SUBJECT_COUNT = 128;

    /**
     * 新建一个文件路径工具
     *
     * @param multipartFile
     * @return
     */
    public static String getFilePath(MultipartFile multipartFile) {
        File temp = new File("");
        String filePath = temp.getAbsolutePath() + "\\" + multipartFile.getOriginalFilename();
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
            out.write(multipartFile.getBytes());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    /**
     * 获取视图map工具
     *
     * @param
     * @return
     */
    public static Map<String, Object> getModelAndViewMap(EmailCode emailCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", emailCode.getCode());
        map.put("message", emailCode.getMessage());
        return map;
    }

    /**
     * 判断mail传值是否为空，返回对应的EmailCode
     *
     * @param message
     * @param
     * @return
     */
    public static EmailCode isMessage(Message message, MultipartFile multipartFile) {
        if (message.getTo().equals("")&& message.getSubject().equals("") && message.getText().equals("")) {
            log.error("[邮件发送错误]：{}", EmailCode.EMAIL_MESSAGE_NULL.getMessage());
            return EmailCode.EMAIL_MESSAGE_NULL;
        } else {
            if (message.getTo().equals("")) {
                log.error("[邮件发送错误]：{}", EmailCode.EMAIL_MESSAGE_TO_NULL.getMessage());
                return EmailCode.EMAIL_MESSAGE_TO_NULL;
            } else if (message.getSubject().equals("") || message.getSubject().length() >= MAX_SUBJECT_COUNT) {
                log.error("[邮件发送错误]：{}", EmailCode.EMAIL_MESSAGE_SUBJECT_NULL.getMessage());
                return EmailCode.EMAIL_MESSAGE_SUBJECT_NULL;
            } else if (message.getText().equals("") && multipartFile.isEmpty()) {
                log.error("[邮件发送错误]：{}", EmailCode.EMAIL_MESSAGE_TEXTANDMT_NULL.getMessage());
                return EmailCode.EMAIL_MESSAGE_TEXTANDMT_NULL;
            }
        }
        return EmailCode.EMAIL_SEND_SUCCESS;
    }
}

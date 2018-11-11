package com.wuhao.email.YUNSMS;

import com.wuhao.email.YUNSMS.client.AbsRestClient;
import com.wuhao.email.YUNSMS.client.JsonReqClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 云资讯短信发送
 */
@Slf4j
public class YUNSMS {

    static AbsRestClient InstantiationRestAPI() {
        return new JsonReqClient();
    }

    public static String testSendSms(String sid, String token, String appid, String templateid, String param, String mobile, String uid){
        try {
            String result=InstantiationRestAPI().sendSms(sid, token, appid, templateid, param, mobile, uid);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 外部调用发送短信
     * @param mobile 电话号码
     * @param param 验证码 6位
     * @return
     */
    public String sendSms(String mobile,String param){
        String AppId = "8df062a56eab451bb3f934698ba1785c";

        String AccountSid = "1825206176dbea4db525c41cd722bb0f";

        String AuthToken = "f128ce632398f18bef7ac6a00458d867";

        String TemplateId = "395942";

        String MaxAge = "60";

        String uid="";

        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(param)){
            log.error("[短信发送错误]：目标手机号或者验证码为空");
            return null;
        }
        param=param+","+MaxAge;
        return testSendSms(AccountSid,AuthToken,AppId,TemplateId,param,mobile,uid);
    }
}

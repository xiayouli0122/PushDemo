package com.yuri.push;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;



 

 
/**
 * 
 * @author PSVM
 * 
 */
public class BaiduPushUtil {
 
    /**
     * MD5加密
     * 
     * @param s
     * @return
     */
    public String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
 
    /**
     * Map排序
     * 
     * @param unsort_map
     * @return
     */
 
    private SortedMap<String, String> mapSortByKey(Map<String, String> unsort_map) {
        TreeMap<String, String> result = new TreeMap<String, String>();
        Object[] unsort_key = unsort_map.keySet().toArray();
        Arrays.sort(unsort_key);
        for (int i = 0; i < unsort_key.length; i++) {
            result.put(unsort_key[i].toString(), unsort_map.get(unsort_key[i]));
        }
        return result.tailMap(result.firstKey());
    }
 
    public static String getMessage(String title, String description) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setTitle(title);
        pushMessage.setDescription(description);
        //String messages = JSONObject.fromObject(pushMessage).toString();
        String messages = "{\"title\":\""+ title +"\",\"description\":\""+ description  +"\"}";
        
        return messages;
    }
 
    /**
     * 获取签名
     * 
     * @param url
     * @param parameters
     * @param secret
     * @return
     */
    public String getSignature(String url, Map<String, String> parameters, String secret) {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new HashMap<String, String>(parameters);
        sortedParams = mapSortByKey(sortedParams);
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder baseString = new StringBuilder();
        baseString.append("POST");
        baseString.append(url);
        for (String key : sortedParams.keySet()) {
            if (null != key && !"".equals(key)) {
                baseString.append(key).append("=").append(sortedParams.get(key));
            }
            sortedParams.get(key);
        }
        baseString.append(secret);
        String encodeString = URLEncoder.encode(baseString.toString());
        String sign = MD5(encodeString);
        return sign;
    }
 
    public String push_msg(String user_id, String title, String description) {
        return this.push_msg(user_id, null, title, description);
    }
 
    /**
     * 
     * @param user_id
     * @param title
     * @param description
     * @return
     */
    public String push_msg(String user_id, String channel_id, String title, String description) {
        String url = "http://channel.api.duapp.com/rest/2.0/channel/channel";
        // 方法名
        String method = "push_msg";
 
        // 访问令牌
        String apikey = "uLIeT0avoGc0Li9rQ9Q60Nl0";
        // 密匙
        String secret = "aGoEWNT82HsjOgwgkHVhGydVYjfsqN9S";
        // 推送类型，取值范围为：1～3
        // 1：单个人，必须指定user_id 和 channel_id （指定用户的指定设备）或者user_id（指定用户的所有设备）
        // 2：一群人，必须指定 tag
        // 3：所有人，无需指定tag、user_id、channel_id
        int push_type = 1;
        // 设备类型
        // 1：浏览器设备；
        // 2：PC设备；
        // 3：Andriod设备；
        // 4：iOS设备；
        // 5：Windows Phone设备；
        int device_type = 3;
        // 消息类型
        // 0：消息（透传给应用的消息体）
        // 1：通知（对应设备上的消息通知）
        // 默认值为0。
        int message_type = 1;
        // 推送信息
        String messages = getMessage(title, description);
        // 消息标识。
        // 指定消息标识，必须和messages一一对应。相同消息标识的消息会自动覆盖。
        String msg_keys = UUID.randomUUID().toString();
        // 用户发起请求时的Unix时间戳。本次请求签名的有效时间为该时间戳+10分钟。
        String timestamp = Long.toString(new Date().getTime());
 
        Map<String, String> parameters = new TreeMap<String, String>();
        parameters.put("method", method);
        parameters.put("apikey", apikey);
        parameters.put("user_id", user_id);
        if (null != channel_id) {
            parameters.put("channel_id", channel_id);
        }
        parameters.put("push_type", push_type + "");
        parameters.put("device_type", device_type + "");
        parameters.put("message_type", message_type + "");
        parameters.put("messages", messages);
        parameters.put("msg_keys", msg_keys);
        parameters.put("timestamp", timestamp + "");
        String sign = getSignature(url, parameters, secret);
        parameters.put("sign", sign);
        String responseStr = HttpXmlClient.post(url, parameters);
        return responseStr;
    }
 
    public String push_msgToAll(String title, String description) {
        String url = "http://channel.api.duapp.com/rest/2.0/channel/channel";
        // 方法名
        String method = "push_msg";
        // 访问令牌
        String apikey = "uLIeT0avoGc0Li9rQ9Q60Nl0";
        // 密匙
        String secret = "aGoEWNT82HsjOgwgkHVhGydVYjfsqN9S";
        // 推送类型，取值范围为：1～3
        // 1：单个人，必须指定user_id 和 channel_id （指定用户的指定设备）或者user_id（指定用户的所有设备）
        // 2：一群人，必须指定 tag
        // 3：所有人，无需指定tag、user_id、channel_id
        int push_type = 3;
 
        // 设备类型
        // 1：浏览器设备；
        // 2：PC设备；
        // 3：Android设备；
        // 4：iOS设备；
        // 5：Windows Phone设备；
        int device_type = 3;
 
        // 消息类型
        // 0：消息（透传给应用的消息体）
        // 1：通知（对应设备上的消息通知）
        // 默认值为0。
        int message_type = 1;
 
        // 推送信息
        String messages = getMessage(title, description);
 
        // 消息标识。
        // 指定消息标识，必须和messages一一对应。相同消息标识的消息会自动覆盖。
        String msg_keys = UUID.randomUUID().toString();
 
        // 用户发起请求时的Unix时间戳。本次请求签名的有效时间为该时间戳+10分钟。
        String timestamp = Long.toString(new Date().getTime());
 
        Map<String, String> parameters = new TreeMap<String, String>();
        parameters.put("method", method);
        parameters.put("apikey", apikey.toString());
        parameters.put("push_type", push_type + "");
        parameters.put("device_type", device_type + "");
        parameters.put("message_type", message_type + "");
        parameters.put("messages", messages.toString());
        parameters.put("msg_keys", msg_keys.toString());
        parameters.put("timestamp", timestamp + "");
        String sign = getSignature(url, parameters, secret);
        parameters.put("sign", sign.toString());
        String responseStr = HttpXmlClient.post(url, parameters);
        return responseStr;
    }
 
}

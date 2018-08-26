package cn.javaGang.ip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import cn.javaGang.util.Configuration;
import cn.javaGang.util.Constant;
import cn.javaGang.util.HttpUtils;

public class IPService {

    /**
     * @param
     * @return City where ip is located
     */
    public static String getAddressByIP(String ip) {
        try {
            Map<String, String> querys = new HashMap<String, String>();
            querys.put("ak", Constant.bidu_ip_ak);
            querys.put("coor", "BD09ll");
            querys.put("ip", ip);
            String ss = HttpUtils.buildUrl(Configuration.getInstance().getValue(Constant.bidu_ip_use), null, querys);
            URL url = new URL(ss);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            String ipAddr = result.toString();
            try {
                JSONObject detail = JSONObject.parseObject(ipAddr);
                if ("0".equals(detail.get("status").toString())) {
                    JSONObject ipInfo = JSONObject.parseObject(detail.get("content").toString());
                    JSONObject address = JSONObject.parseObject(ipInfo.get("address_detail").toString());
                    return address.toJSONString();
                } else {
                    return "error";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "error";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
}

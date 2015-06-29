package tool;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2015/4/13.
 */
@WebFilter(filterName = "SignOnFilter")
public class SignOnFilter implements Filter {
    FilterConfig config;
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
//        chain.doFilter(req, resp);
        HttpServletRequest hreq=(HttpServletRequest) req;
        HttpServletResponse hres=(HttpServletResponse) resp;
        String token = getCookieByName(hreq,"portToken").toString();
        String sid = getCookieByName(hreq,"portSid").toString();
        try{
            if(token!=null&&sid!=null){
                Map dataMap = toMap(GetDataByURL(token, sid));
                String uid = dataMap.get("uid").toString();
//                String username = dataMap.get("username").toString();
                String chinesename = dataMap.get("chinesename").toString();
                hreq.setAttribute("chinesename",chinesename);
                hreq.setAttribute("uid",uid);
                chain.doFilter(req, resp);
            }else hres.sendRedirect("http://port.ledu.com/leduuser/login");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public String GetDataByURL(String token,String sid){
        StringBuffer temp = new StringBuffer();
        long regt = new Date().getTime();
        String dateline = regt + "";
        String reqTime = dateline.substring(0, 10);
        String appId = "0";
        String reqAppId = "0";
        String sign_key = "hEYbTIq1J5t$b(M}fqyLgFYB!#XXK*W62di";
        Map<String,String> param = new HashMap<String, String>();
        param.put("appId",appId);
        param.put("reqAppId",reqAppId);
        param.put("reqTime",reqTime);
        param.put("sid",sid);
        param.put("token",token);
        try {
            String sign = GetMD5Code(toQueryString(param) + sign_key);
            String url = "http://newport.joyport.com/oauth";
            HttpURLConnection uc = (HttpURLConnection)new URL(url).openConnection();
            uc.setConnectTimeout(10000);
            uc.setDoOutput(true);
            uc.setRequestMethod("GET");
            uc.setUseCaches(false);
            DataOutputStream out = new DataOutputStream(uc.getOutputStream());

            // 要传的参数
            String s = URLEncoder.encode("token", "UTF-8") + "=" +
                    URLEncoder.encode(token, "UTF-8");
            s += "&" + URLEncoder.encode("sid", "UTF-8") + "=" +
                    URLEncoder.encode(sid, "UTF-8");
            s += "&" + URLEncoder.encode("reqTime", "UTF-8") + "=" +
                    URLEncoder.encode(reqTime, "UTF-8");
            s += "&" + URLEncoder.encode("appId", "UTF-8") + "=" +
                    URLEncoder.encode(appId, "UTF-8");
            s += "&" + URLEncoder.encode("reqAppId", "UTF-8") + "=" +
                    URLEncoder.encode(reqAppId, "UTF-8");
            s += "&" + URLEncoder.encode("sign", "UTF-8") + "=" +
                    URLEncoder.encode(sign, "UTF-8");
            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
            out.writeBytes(s);
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(uc.getInputStream());
            Reader rd = new InputStreamReader(in, "UTF-8");
            int c = 0;
            while ((c = rd.read()) != -1) {
                temp.append((char) c);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp.toString();
    }
    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }
    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 返回形式只为数字
    private static String byteToNum(byte bByte) {
        int iRet = bByte;
        System.out.println("iRet1=" + iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf(iRet);
    }
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    public String toQueryString(Map<String,String> data) throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();

        for (Map.Entry<String,String> pair : data.entrySet()) {
            queryString.append(URLEncoder.encode(pair.getKey(), "UTF-8") + "=");
            queryString.append ( URLEncoder.encode (pair.getValue (), "UTF-8" ) + "&" );
        }

        if (queryString.length () > 0) {
            queryString.deleteCharAt ( queryString.length () - 1 );
        }

        return queryString.toString ();
    }
    /**
     * 将Json对象转换成Map
     *
     * @param jsonObject
     *            json对象
     * @return Map对象
     * @throws JSONException
     */
    public static Map toMap(String jsonString) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);

        Map result = new HashMap();
        Iterator iterator = jsonObject.keys();
        String key = null;
        String value = null;

        while (iterator.hasNext()) {

            key = (String) iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);

        }
        return result;

    }
    public Cookie getCookieByName(HttpServletRequest request,String name){
        Map<String,Cookie> cookieMap = ReadCookieMap(request);
        if(cookieMap.containsKey(name)){
            Cookie cookie = cookieMap.get(name);
            return cookie;
        }else{
            return null;
        }
    }
    /**
      * 将cookie封装到Map里面
      * @param request
      * @return
      */
    private Map<String,Cookie> ReadCookieMap(HttpServletRequest request){
        Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
        Cookie[] cookies = request.getCookies();
        if(null!=cookies){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
                }
            }
        return cookieMap;
    }
    public void init(FilterConfig config) throws ServletException {
        this.config=config;
    }

}

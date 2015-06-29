API地址: http://host/sendmail  
请求方法: GET/POST 请求长度大于2083字符必须用post  
签名算法: sign = md5($sender.$recipient.$time.$key);  
密钥key为"nNgvTlMWYzLFyW2k"  
请求参数:  
  
sender	发送邮箱，若为空则默认
recipient	收件邮箱，多个收件人用分号";"分隔  
cc	抄送邮箱，多个抄送人用分号";"分隔，可为空  
bcc	密送邮箱，多个密送人用分号";"分隔，可为空  
time	时间戳  
subject	邮件主题  
content	邮件正文  
attachname	附件全名，包括后缀，可为空  
attachment	附件文件字节流的base64编码字符串，可为空  
sign	请求签名  
返回值: unicode编码提示信息  
以php代码示例  
  
$postData =   
           "sender=".$sender."&".  
           "recipient=".$recipient."&".  
           "cc=".$cc."&".  
           "bcc=".$bcc."&".  
           "subject=".$subject."&".  
           "content=".$content."&".  
           "time=".$time."&".  
           "sign=".$sign;  
$this_header = array(  
   "content-type: application/x-www-form-urlencoded;  
   charset=UTF-8"  
);  
$ch = curl_init();  
curl_setopt($ch,CURLOPT_HTTPHEADER,$this_header);  
curl_setopt($ch, CURLOPT_URL, "121.41.74.21/mailproxy/sendmail");  
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);  
curl_setopt($ch, CURLOPT_POST, 1);  
curl_setopt($ch, CURLOPT_POSTFIELDS, $postData);  
$output = curl_exec($ch);  
curl_close($ch);  
  

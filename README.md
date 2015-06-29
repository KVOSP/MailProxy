API地址: [http://host/sendmail]

<br/>请求方法: GET/POST   请求长度大于2083字符必须用post

签名算法: sign = md5($sender.$recipient.$time.$key);

<br/>密钥key为"nNgvTlMWYzLFyW2k"

请求参数:

{| style="width: 500px" border="1" cellspacing="1" cellpadding="1"
|-
| sender
| 发送邮箱，若为空则默认为内网通知账号cx-notice-infomation@joyport.com
|-
| recipient
| 收件邮箱，多个收件人用分号";"分隔<br/>
|-
| cc
| 抄送邮箱，多个抄送人用分号";"分隔，可为空<br/>
|-
|  bcc
| 密送邮箱，多个密送人用分号";"分隔，可为空<br/>
|-
|  time
| 时间戳<br/>
|-
|  subject
| 邮件主题<br/>
|-
| content 
|  邮件正文<br/>
|-
| attachname
| 附件全名，包括后缀，可为空<br/>
|-
| attachment
| 附件文件字节流的base64编码字符串，可为空<br/>
|-
| sign 
| 请求签名<br/>
|}

<br/>返回值: unicode编码提示信息

<br/>以php代码示例

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

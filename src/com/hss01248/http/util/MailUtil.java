package com.hss01248.http.util;

import org.simplejavamail.email.Email;
import org.simplejavamail.internal.util.ConfigLoader;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.ServerConfig;
import org.simplejavamail.mailer.config.TransportStrategy;

import javax.mail.Message;

/**
 * Created by Administrator on 2017/1/18 0018.
 */
public class MailUtil {


    public static void tellLeftTickets(){

    }

    public static void send(String title,String content){
        //http://service.mail.qq.com/cgi-bin/help?subtype=1&no=167&id=28
        /*Properties props = new Properties ();
　　　     props.put("mail.smtp.host", "smtp.163.com");//可以换上你的smtp主机名。 */
        ConfigLoader.loadProperties("E:\\ASprogects\\simplejavamail.properties",true); // optional default
       // ConfigLoader.loadProperties("overrides.properties"); // optional extra

        Email email = new Email();

        email.setFromAddress("1587981164", "1587981164@qq.com");
        email.setReplyToAddress("1587981164", "1587981164@qq.com");
        email.addRecipient("793466045", "793466045@qq.com", Message.RecipientType.TO);
       /* email.addRecipient("lollypop", "lolly.pop@somemail.com", Message.RecipientType.TO);
        email.addRecipient("C. Cane", "candycane@candyshop.org", Message.RecipientType.TO);
        email.addRecipient("C. Bo", "chocobo@candyshop.org", Message.RecipientType.CC);*/
        email.setSubject(title);
        email.setText(content);
       // email.setTextHTML("&lt;img src=&#39;cid:wink1&#39;&gt;&lt;b&gt;We should meet up!&lt;/b&gt;&lt;img src=&#39;cid:wink2&#39;&gt;");


      //  email.signWithDomainKey(privateKeyData, "somemail.com", "selector");

       /* new Mailer(
                new ServerConfig("smtp.host.com", 456, "793466045@qq.com", "20136861jm273c5o"),
                TransportStrategy.SMTP_TLS,
                new ProxyConfig("socksproxy.host.com", 1080, "proxy user", "proxy password")
        ).sendMail(email);*/

        new Mailer(
                new ServerConfig("smtp.qq.com", 587, "1587981164@qq.com", "uebgkizhmokaibjd"),
                TransportStrategy.SMTP_TLS
        ).sendMail(email);
    }



}

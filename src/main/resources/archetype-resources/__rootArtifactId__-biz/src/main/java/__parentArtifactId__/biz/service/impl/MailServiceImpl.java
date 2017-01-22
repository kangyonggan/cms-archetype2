#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.biz.service.impl;

import ${package}.${parentArtifactId}.biz.service.MailService;
import ${package}.${parentArtifactId}.biz.service.TokenService;
import ${package}.${parentArtifactId}.biz.util.PropertiesUtil;
import ${package}.${parentArtifactId}.model.annotation.LogTime;
import ${package}.${parentArtifactId}.model.vo.User;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kangyonggan
 * @since 2017/1/19
 */
@Service
@Log4j2
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private TokenService tokenService;

    @Override
    @LogTime
    public void sendResetMail(User user, String callbackUrl) {
        String code = tokenService.saveToken("reset", user.getId());
        log.info("发邮件的消息码code：{}", code);

        callbackUrl += "${symbol_pound}validate/reset/" + code;
        log.info("最终邮件回调地址:{}", callbackUrl);

        Map<String, Object> map = new HashMap();
        map.put("title", "请点击下面的链接重置密码");
        map.put("url", callbackUrl);
        map.put("appName", PropertiesUtil.getProperties("app.name"));
        map.put("author", PropertiesUtil.getProperties("app.author"));

        String text;
        try {
            text = getString(map, "email.ftl");
        } catch (Exception e) {
            log.error("邮件模板出错！", e);
            return;
        }

        send(user.getEmail(), "找回密码", text, true);
    }

    @Override
    public void send(String to, String title, String text, boolean isHtml) {
        MimeMessage msg = javaMailSender.createMimeMessage();
        try {
            log.info("发件人邮箱：{}", PropertiesUtil.getProperties("mail.username"));
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setFrom(PropertiesUtil.getProperties("mail.username"), PropertiesUtil.getProperties("app.name"));
            helper.setTo(to);
            helper.setSubject(title);

            helper.setText(text, isHtml);
        } catch (Exception e) {
            log.error("邮件发送失败！" + to, e);
            return;
        }

        log.info("正在给{}发邮件...", to);
        try {
            javaMailSender.send(msg);
            log.info("邮件发送成功...");
        } catch (Exception e) {
            log.error("邮件发送失败", e);
        }
    }

    private String getString(Map body, String templatePath) throws IOException, TemplateException {
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templatePath);
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, body);
        return text;
    }
}

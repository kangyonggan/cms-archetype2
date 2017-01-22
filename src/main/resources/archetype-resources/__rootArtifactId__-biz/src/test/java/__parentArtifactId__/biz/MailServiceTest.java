#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.biz;

import ${package}.${parentArtifactId}.biz.service.MailService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kangyonggan
 * @since 2017/1/19
 */
public class MailServiceTest extends AbstractServiceTest {

    @Autowired
    private MailService mailService;

    /**
     * 测试发送找回密码的邮件
     */
    @Test
    public void testSendResetMail() {
        mailService.send("2825176081@qq.com", "找回密码", "test:<h1>测试邮件</h1>", true);
    }

}

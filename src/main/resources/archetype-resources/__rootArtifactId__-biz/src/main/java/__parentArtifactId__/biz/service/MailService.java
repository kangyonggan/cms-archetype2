#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.biz.service;

import ${package}.${parentArtifactId}.model.vo.User;

/**
 * @author kangyonggan
 * @since 2017/1/19
 */
public interface MailService {

    /**
     * 发找回密码的邮件
     *
     * @param user
     * @param callbackUrl
     */
    void sendResetMail(User user, String callbackUrl);

    /**
     * 发送邮件
     *
     * @param to
     * @param title
     * @param text
     * @param isHtml
     */
    void send(String to, String title, String text, boolean isHtml);
}

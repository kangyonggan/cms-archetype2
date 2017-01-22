#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.biz.util;

import ${package}.${parentArtifactId}.model.constants.AppConstants;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author kangyonggan
 * @since 2017/1/4
 */
@Log4j2
public class Ftp {

    @Setter
    private String path;

    @Setter
    private String ip;

    @Setter
    private int port;

    @Setter
    private String username;

    @Setter
    private String password;

    /**
     * 登录ftp服务器
     *
     * @return
     * @throws Exception
     */
    private FTPClient connect() throws Exception {
        FTPClient ftp = new FTPClient();
        int reply;
        ftp.connect(ip, port);
        ftp.login(username, password);
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return null;
        }
        ftp.changeWorkingDirectory(path);
        log.info("连接文件服务器成功, 上传路径path:" + path);
        return ftp;
    }

    /**
     * 上传的文件
     *
     * @param name
     * @return 返回在文件服务器的相对路径
     */
    public String upload(String name) {
        FTPClient ftp = null;
        FileInputStream in = null;
        try {
            ftp = connect();
            File file = new File(PropertiesUtil.getProperties(AppConstants.FILE_PATH_ROOT) + name);
            in = new FileInputStream(file);
            ftp.storeFile(file.getName(), in);

            log.info("文件上传成功,name=" + file.getName());
            return file.getName();
        } catch (Exception e) {
            log.error("文件上传异常", e);
        } finally {
            try {
                if (ftp != null) {
                    ftp.disconnect();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                log.error(e);
            }
        }
        return "";
    }
}

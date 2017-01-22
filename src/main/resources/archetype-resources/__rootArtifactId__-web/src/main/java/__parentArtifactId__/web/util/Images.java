#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.web.util;

import ${package}.${parentArtifactId}.biz.util.Ftp;
import ${package}.${parentArtifactId}.biz.util.PropertiesUtil;
import ${package}.${parentArtifactId}.model.constants.AppConstants;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author kangyonggan
 * @since 2016/12/6
 */
@Component
public class Images {

    @Autowired
    private Ftp ftp;

    @Autowired
    private FileUpload fileUpload;

    //大Logo
    public String large(String source) throws FileUploadException {
        return thumbnails(source, "l", 200, 200);
    }

    //中Logo
    public String middle(String source) throws FileUploadException {
        return thumbnails(source, "m", 128, 128);
    }

    //小Logo
    public String small(String source) throws FileUploadException {
        return thumbnails(source, "s", 64, 64);
    }

    private String thumbnails(String source, String suffix, int width, int height) throws FileUploadException {
        String desc = fileUpload.extractFilePath(source, suffix);

        try {
            Thumbnails.of(PropertiesUtil.getProperties(AppConstants.FILE_PATH_ROOT) + source)
                    .size(width, height)
                    .keepAspectRatio(false)
                    .toFile(PropertiesUtil.getProperties(AppConstants.FILE_PATH_ROOT) + desc);

            ftp.upload(desc);
        } catch (Exception e) {
            throw new FileUploadException("文件转换异常", e);
        }

        return desc;
    }
}

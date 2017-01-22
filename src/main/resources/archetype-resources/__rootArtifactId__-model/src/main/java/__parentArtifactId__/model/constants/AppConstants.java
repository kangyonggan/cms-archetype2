#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.model.constants;

/**
 * 系统常量
 *
 * @author kangyonggan
 * @since 2016/12/25
 */
public interface AppConstants {

    /**
     * 是否删除
     */
    byte IS_DELETED_NO = 0;
    byte IS_DELETED_YES = 1;

    /**
     * 文件根目录的name
     */
    String FILE_PATH_ROOT = "file.root.path";

    /**
     * 上传目录
     */
    String FILE_UPLOAD_PATH = "upload/";

    /**
     * 默认分页大小
     */
    int PAGE_SIZE = 10;

    /**
     * 注册用户时用户有的默认角色(普通角色)
     */
    String DEFAULT_ROLE_CODE = "ROLE_USER";

    /**
     * Shiro常量
     */
    String HASH_ALGORITHM = "SHA-1";
    int HASH_INTERATIONS = 2;
    int SALT_SIZE = 8;

    /**
     * 把验证码存放在session中的key
     */
    String KEY_CAPTCHA = "key-captcha";

}

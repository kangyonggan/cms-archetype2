#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.model.constants;

import lombok.Getter;

/**
 * 通用错误
 *
 * @author kangyonggan
 * @since 2016/12/3
 */
public enum CommonErrors {

    SUCCESS("0000", "请求成功"),
    FAILURE("1111", "请求失败, 请稍后重试!"),
    BAD_ARGS("2222", "请求参数不合法"),
    NO_RESULT("3333", "没有符合条件的结果"),
    UNKNOW_EXCEPTION("9999", "发生未知异常, 请联系管理员!");

    /**
     * 错误码
     */
    @Getter
    private final String errCode;

    /**
     * 错误信息
     */
    @Getter
    private final String errMsg;

    CommonErrors(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

}

#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.model.constants;

/**
 * 响应状态
 *
 * @author kangyonggan
 * @since 2016/12/3
 */
public enum ResponseState {

    Y,// 成功
    F,// 失败
    E,// 异常
    I;// 处理中

}

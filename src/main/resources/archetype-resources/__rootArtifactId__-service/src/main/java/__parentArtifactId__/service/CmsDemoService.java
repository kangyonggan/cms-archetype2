#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.service;

import ${package}.${parentArtifactId}.model.annotation.Param;
import ${package}.${parentArtifactId}.model.dto.request.DemoRequest;
import ${package}.${parentArtifactId}.model.dto.response.CommonResponse;

/**
 * @author kangyonggan
 * @since 2017/1/18
 */
public interface CmsDemoService {

    /**
     * 请求参数是引用类型
     *
     * @param request
     * @return
     */
    CommonResponse hello(@Param(name = "request") DemoRequest request);

    /**
     * 请求参数是基本类型
     *
     * @param name
     * @param value
     * @return
     */
    CommonResponse world(String name, @Param(name = "value", min = 0, max = 99) int value);

}

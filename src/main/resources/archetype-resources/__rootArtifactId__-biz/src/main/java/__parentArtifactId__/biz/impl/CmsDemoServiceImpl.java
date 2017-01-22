#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.biz.impl;

import ${package}.${parentArtifactId}.model.annotation.CacheDeleteAll;
import ${package}.${parentArtifactId}.model.annotation.CacheGetOrSave;
import ${package}.${parentArtifactId}.model.annotation.Param;
import ${package}.${parentArtifactId}.model.constants.CommonErrors;
import ${package}.${parentArtifactId}.model.dto.request.DemoRequest;
import ${package}.${parentArtifactId}.model.dto.response.CommonResponse;
import ${package}.${parentArtifactId}.service.CmsDemoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * @author kangyonggan
 * @since 2017/1/18
 */
@Service("${parentArtifactId}DemoService")
@Log4j2
public class CmsDemoServiceImpl implements CmsDemoService {

    @Override
    @CacheGetOrSave("demo:name:{0:name}")
    public CommonResponse hello(@Param(name = "request") DemoRequest request) {
        CommonResponse response = new CommonResponse();

        try {
            // TODO hello的业务逻辑
            log.info("hello的业务逻辑");

            response.setRespCode(CommonErrors.SUCCESS.getErrCode());
            response.setRespMsg(CommonErrors.SUCCESS.getErrMsg());
        } catch (Exception e) {
            log.error("调用hello接口报错", e);
            response.setRespCode(CommonErrors.UNKNOW_EXCEPTION.getErrCode());
            response.setRespMsg(CommonErrors.UNKNOW_EXCEPTION.getErrMsg());
        }

        return response;
    }

    @Override
    @CacheDeleteAll("demo:name")
    public CommonResponse world(String name, @Param(name = "value", min = 0, max = 99) int value) {
        CommonResponse response = new CommonResponse();

        try {
            // TODO world的业务逻辑
            log.info("world的业务逻辑");

            response.setRespCode(CommonErrors.SUCCESS.getErrCode());
            response.setRespMsg(CommonErrors.SUCCESS.getErrMsg());
        } catch (Exception e) {
            log.error("调用world接口报错", e);
            response.setRespCode(CommonErrors.UNKNOW_EXCEPTION.getErrCode());
            response.setRespMsg(CommonErrors.UNKNOW_EXCEPTION.getErrMsg());
        }

        return response;
    }
}

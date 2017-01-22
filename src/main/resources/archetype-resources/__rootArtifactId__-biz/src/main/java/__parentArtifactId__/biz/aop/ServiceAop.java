#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.biz.aop;

import ${package}.${parentArtifactId}.biz.util.AopUtil;
import ${package}.${parentArtifactId}.biz.util.DateUtils;
import ${package}.${parentArtifactId}.biz.util.PropertiesUtil;
import ${package}.${parentArtifactId}.biz.util.Reflections;
import ${package}.${parentArtifactId}.model.annotation.Param;
import ${package}.${parentArtifactId}.model.annotation.Valid;
import ${package}.${parentArtifactId}.model.constants.CommonErrors;
import ${package}.${parentArtifactId}.model.constants.ResponseState;
import ${package}.${parentArtifactId}.model.dto.response.CommonResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 切于所有对外接口的实现方法上，打印入参和出参，打印方法执行时间, 慢接口打印error日志，最外层try{}catch(){}，入参校验
 *
 * @author kangyonggan
 * @since 2016/11/30
 */
@Component
@Log4j2
public class ServiceAop {

    /**
     * 设定的接口最大执行时间
     */
    private Long slowInterfaceTime;

    public ServiceAop() {
        String val = PropertiesUtil.getPropertiesOrDefault("slow.interface.time", "5");
        slowInterfaceTime = Long.parseLong(val);
    }

    /**
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object args[] = joinPoint.getArgs();
        Class clazz = joinPoint.getTarget().getClass();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = clazz.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        String targetName = "[" + clazz.getName() + "." + method.getName() + "]";
        Object result;

        try {
            log.info("进入接口:" + targetName + " - args:" + AopUtil.getStringFromRequest(args));

            String checkMessage = check(args, method);
            if (StringUtils.isNotEmpty(checkMessage)) {
                log.error("调用接口{}参数校验未通过, 失败信息:{}", targetName, checkMessage);
                CommonResponse response = new CommonResponse();
                response.setState(ResponseState.F);
                response.setRespCode(CommonErrors.BAD_ARGS.getErrCode());
                response.setRespMsg(CommonErrors.BAD_ARGS.getErrMsg() + "," + checkMessage);
                return response;
            }
            log.info("参数校验通过！");

            long beginTime = DateUtils.getNow().getTime();
            result = joinPoint.proceed(args);
            long endTime = DateUtils.getNow().getTime();
            long time = endTime - beginTime;

            log.info("离开接口:" + targetName + " - return:" + AopUtil.getStringFromResponse(result));
            log.info("接口耗时:" + time + "ms - " + targetName);

            if (time > slowInterfaceTime * 1000) {
                log.error("接口执行超过设定时间" + slowInterfaceTime + "s," + targetName);
            }
        } catch (Exception e) {
            log.error("调用接口" + targetName + "发送未知异常", e);
            CommonResponse response = new CommonResponse();
            response.setState(ResponseState.E);
            response.setRespCode(CommonErrors.UNKNOW_EXCEPTION.getErrCode());
            response.setRespMsg(CommonErrors.UNKNOW_EXCEPTION.getErrMsg());
            return response;
        }

        return result;
    }

    /**
     * 校验
     *
     * @param args
     * @param method
     * @return 校验成功返回null， 校验失败返回失败信息
     */
    private String check(Object args[], Method method) {
        Annotation annos[][] = method.getParameterAnnotations();

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];

            String checkMessage = checkParam(arg, annos[i]);
            if (StringUtils.isNotEmpty(checkMessage)) {
                return checkMessage;
            }

            Field fields[] = Reflections.getFields(arg.getClass());
            for (Field field : fields) {
                checkMessage = checkArgs(field.getAnnotation(Valid.class), arg, field);
                if (StringUtils.isNotEmpty(checkMessage)) {
                    return checkMessage;
                }
            }
        }
        return null;
    }

    /**
     * 校验@Param
     *
     * @param arg
     * @param annos
     * @return
     */
    private String checkParam(Object arg, Annotation[] annos) {
        for (Annotation anno : annos) {
            String checkMessage = checkArgs(anno, arg, null);
            if (StringUtils.isNotEmpty(checkMessage)) {
                return checkMessage;
            }
        }
        return null;
    }

    /**
     * 参数校验
     *
     * @param anno
     * @param arg
     * @param field
     * @return
     */
    private String checkArgs(Annotation anno, Object arg, Field field) {
        String name;
        boolean required;
        long min;
        long max;
        int minlength;
        int length;
        int maxlength;
        String pattern;
        String message;

        if (anno instanceof Param) {
            Param param = (Param) anno;

            name = param.name();
            required = param.required();
            min = param.min();
            max = param.max();
            minlength = param.minLength();
            length = param.length();
            maxlength = param.maxLength();
            pattern = param.pattern();
            message = param.message();
        } else if (anno instanceof Valid) {
            Valid valid = (Valid) anno;

            name = field.getName();
            required = valid.required();
            min = valid.min();
            max = valid.max();
            minlength = valid.minLength();
            length = valid.length();
            maxlength = valid.maxLength();
            pattern = valid.pattern();
            message = valid.message();

            arg = Reflections.getFieldValue(arg, field.getName());
        } else {
            return null;
        }

        // 不允许为空校验
        if (required && arg == null) {
            return name + "不允许为空";
        }

        // int范围判断
        if (arg instanceof Integer && (arg != null || required)) {
            Integer val = (Integer) arg;
            if (val < min) {
                return name + "不能小于" + min + ",实际值为" + val;
            }
            if (val > max) {
                return name + "不能大于" + max + ",实际值为" + val;
            }
        }

        // long范围判断
        if (arg instanceof Long && (arg != null || required)) {
            Long val = (Long) arg;
            if (val < min) {
                return name + "不能小于" + min + ",实际值为" + val;
            }
            if (val > max) {
                return name + "不能大于" + max + ",实际值为" + val;
            }
        }

        // String长度判断
        if (arg instanceof String && (arg != null || required)) {
            String val = (String) arg;
            if (val.length() < minlength) {
                return name + "长度不能小于" + minlength + ",实际值为" + val;
            }
            if (length != -1 && val.length() != length) {
                return name + "长度不等于" + length + ",实际值为" + val;
            }
            if (maxlength != -1 && val.length() > maxlength) {
                return name + "长度不能大于" + maxlength + ",实际值为" + val;
            }
        }

        // 正则判断
        if ((arg != null || required) && arg instanceof String) {
            String val = (String) arg;
            if (StringUtils.isNotEmpty(pattern) && !val.matches(pattern)) {
                if (StringUtils.isEmpty(message)) {
                    return name + "不合法" + ",实际值为" + val;
                }
                return message + ",实际值" + val;
            }
        }

        return null;
    }
}

#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.model.annotation;

import java.lang.annotation.*;

/**
 * 在字段上加此注解，会在所有service/impl/*方法进入时校验参数
 *
 * @author kangyonggan
 * @since 2016/12/8
 */
@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Valid {

    /**
     * 是否必要。 当必要时, 并且用户送的恰好为空时，下面所有的条件都不校验
     *
     * @return
     */
    boolean required() default true;

    /**
     * 最小长度，仅对字符串有效, 默认0
     *
     * @return
     */
    int minLength() default 0;

    /**
     * 确定的长度，仅对字符串有效, 默认-1
     *
     * @return
     */
    int length() default -1;

    /**
     * 最大长度，仅对字符串有效，默认-1表示无限长
     *
     * @return
     */
    int maxLength() default -1;

    /**
     * 最小值，仅对int,long有效
     *
     * @return
     */
    long min() default Long.MIN_VALUE;

    /**
     * 最大值，仅对int,long有效
     *
     * @return
     */
    long max() default Long.MAX_VALUE;

    /**
     * 正则表达式，默认为空
     *
     * @return
     */
    String pattern() default "";

    /**
     * 正则表达式验证失败后的提示信息, 没配正则表达式的时候无效
     *
     * @return
     */
    String message() default "";

}

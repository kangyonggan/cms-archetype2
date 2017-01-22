#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.model.dto.request;

import ${package}.${parentArtifactId}.model.BaseObject;
import ${package}.${parentArtifactId}.model.annotation.Valid;
import lombok.Data;

/**
 * @author kangyonggan
 * @since 2016/12/25
 */
@Data
public class PageRequest extends BaseObject {

    /**
     * 当前页
     */
    @Valid(min = 1, max = Integer.MAX_VALUE)
    private int pageNum = 1;

    /**
     * 每页大小
     */
    @Valid(min = 1, max = Integer.MAX_VALUE)
    private int pageSize = 10;

}

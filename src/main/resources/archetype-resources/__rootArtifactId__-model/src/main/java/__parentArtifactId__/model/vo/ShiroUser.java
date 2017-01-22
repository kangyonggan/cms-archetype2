#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author kangyonggan
 * @since 2016/12/2
 */
@Data
public class ShiroUser implements Serializable {

    private Long id;

    private String username;
}

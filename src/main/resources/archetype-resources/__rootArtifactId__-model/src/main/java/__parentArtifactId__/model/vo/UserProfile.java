#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.model.vo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Table(name = "user_profile")
@Data
public class UserProfile implements Serializable {
    /**
     * 主键, 自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 中头像
     */
    @Column(name = "medium_avatar")
    private String mediumAvatar;

    /**
     * 大头像
     */
    @Column(name = "large_avatar")
    private String largeAvatar;

    /**
     * 性别:{0:男, 1:女}
     */
    private Byte sex;

    /**
     * 座机号
     */
    private String phone;

    /**
     * QQ号
     */
    private String qq;

    /**
     * 微信号
     */
    private String weixin;

    /**
     * 身份证
     */
    @Column(name = "id_card")
    private String idCard;

    /**
     * 个人网站
     */
    @Column(name = "web_site")
    private String webSite;

    /**
     * 暂住址
     */
    private String address;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 逻辑删除:{0:未删除, 1:已删除}
     */
    @Column(name = "is_deleted")
    private Byte isDeleted;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private Date updatedTime;

    private static final long serialVersionUID = 1L;
}
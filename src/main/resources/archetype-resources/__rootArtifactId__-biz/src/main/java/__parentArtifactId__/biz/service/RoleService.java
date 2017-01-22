#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.biz.service;

import ${package}.${parentArtifactId}.model.vo.Role;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/1/19
 */
public interface RoleService {

    /**
     * 根据用户名查找角色
     *
     * @param username
     * @return
     */
    List<Role> findRolesByUsername(String username);

    /**
     * 校验角色代码是否存在
     *
     * @param code
     * @return
     */
    boolean existsRoleCode(String code);

    /**
     * 查找所有角色
     *
     * @return
     */
    List<Role> findAllRoles();

    /**
     * 搜索角色
     *
     * @param pageNum
     * @param code
     * @param name
     * @return
     */
    List<Role> searchRoles(int pageNum, String code, String name);

    /**
     * 保存角色
     *
     * @param role
     */
    void saveRole(Role role);

    /**
     * 根据id查找角色
     *
     * @param id
     * @return
     */
    Role findRoleById(Long id);

    /**
     * 更新角色
     *
     * @param role
     */
    void updateRole(Role role);

    /**
     * 更新角色菜单
     *
     * @param code
     * @param menuCodes
     */
    void updateRoleMenus(String code, String menuCodes);
}

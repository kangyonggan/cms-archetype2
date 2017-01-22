#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.web.controller.web;

import ${package}.${parentArtifactId}.biz.service.*;
import ${package}.${parentArtifactId}.model.vo.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author kangyonggan
 * @since 2016/12/3
 */
@Controller
@RequestMapping("validate")
public class ValidateController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private TokenService tokenService;

    /**
     * 校验用户名是否可用
     *
     * @param username
     * @param oldUsername
     * @return
     */
    @RequestMapping(value = "user", method = RequestMethod.POST)
    @ResponseBody
    public boolean validateUsername(@RequestParam("username") String username,
                                    @RequestParam(value = "oldUsername", required = false, defaultValue = "") String oldUsername) {
        if (username.equals(oldUsername)) {
            return true;
        }

        return !userService.existsUsername(username);
    }

    /**
     * 校验邮箱是否存在
     *
     * @param email
     * @return
     */
    @RequestMapping(value = "email", method = RequestMethod.POST)
    @ResponseBody
    public boolean validateEmail(@RequestParam("email") String email) {
        if (StringUtils.isEmpty(email)) {
            return true;
        }

        return userService.existsEmail(email);
    }

    /**
     * 校验邮箱是否存在
     *
     * @param email
     * @param oldEmail
     * @return
     */
    @RequestMapping(value = "email2", method = RequestMethod.POST)
    @ResponseBody
    public boolean validateEmail2(@RequestParam("email") String email,
                                  @RequestParam(value = "oldEmail", required = false, defaultValue = "") String oldEmail) {
        if (email.equals(oldEmail)) {
            return true;
        }

        return !userService.existsEmail(email);
    }

    /**
     * 校验手机号是否存在
     *
     * @param mobile
     * @param oldMobile
     * @return
     */
    @RequestMapping(value = "mobile", method = RequestMethod.POST)
    @ResponseBody
    public boolean validateMobile(@RequestParam("mobile") String mobile,
                                  @RequestParam(value = "oldMobile", required = false, defaultValue = "") String oldMobile) {
        if (mobile.equals(oldMobile)) {
            return true;
        }

        return !userService.existsMobile(mobile);
    }

    /**
     * 校验角色代码是否可用
     *
     * @param code
     * @param oldCode
     * @return
     */
    @RequestMapping(value = "role", method = RequestMethod.POST)
    @ResponseBody
    public boolean validateRoleCode(@RequestParam("code") String code,
                                    @RequestParam(value = "oldCode", required = false, defaultValue = "") String oldCode) {
        if (code.equals(oldCode)) {
            return true;
        }

        return !roleService.existsRoleCode(code);
    }

    /**
     * 校验字典代码是否可用
     *
     * @param code
     * @param oldCode
     * @return
     */
    @RequestMapping(value = "dictionary", method = RequestMethod.POST)
    @ResponseBody
    public boolean validateDictionaryCode(@RequestParam("code") String code,
                                    @RequestParam(value = "oldCode", required = false, defaultValue = "") String oldCode) {
        if (code.equals(oldCode)) {
            return true;
        }

        return !dictionaryService.existsDictionaryCode(code);
    }

    /**
     * 校验菜单代码是否可用
     *
     * @param code
     * @param oldCode
     * @return
     */
    @RequestMapping(value = "menu", method = RequestMethod.POST)
    @ResponseBody
    public boolean validateMenuCode(@RequestParam("code") String code,
                                    @RequestParam(value = "oldCode", required = false, defaultValue = "") String oldCode) {
        if (code.equals(oldCode)) {
            return true;
        }

        return !menuService.existsMenuCode(code);
    }

    /**
     * 重置密码界面
     *
     * @param code
     * @param model
     * @return
     */
    @RequestMapping(value = "reset/{code}", method = RequestMethod.GET)
    public String reset(@PathVariable("code") String code, Model model) {
        Token token = tokenService.findTokenByCode(code);

        if (token == null) {
            model.addAttribute("message", "链接不合法，请检查在拷贝过程中是否遗漏！");
        } else if (token.getExpireTime().before(new Date())) {
            model.addAttribute("message", "链接已过期，请重新发起找回密码请求！");
        } else if (token.getIsDeleted() == 1) {
            model.addAttribute("message", "链接已被使用，不能重复使用！");
        } else {
            model.addAttribute("user", userService.findUserById(token.getUserId()));
            model.addAttribute("token", token);
        }

        return "web/login/reset-password";
    }
}
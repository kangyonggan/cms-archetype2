#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.web.controller.dashboard;

import ${package}.${parentArtifactId}.biz.service.DictionaryService;
import ${package}.${parentArtifactId}.biz.service.UserService;
import ${package}.${parentArtifactId}.model.vo.ShiroUser;
import ${package}.${parentArtifactId}.model.vo.User;
import ${package}.${parentArtifactId}.model.vo.UserProfile;
import ${package}.${parentArtifactId}.web.controller.BaseController;
import ${package}.${parentArtifactId}.web.util.FileUpload;
import ${package}.${parentArtifactId}.web.util.Images;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author kangyonggan
 * @since 2017/1/20
 */
@Controller
@RequestMapping("dashboard/user")
public class DashboardUserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private Images images;

    /**
     * 基本信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "profile", method = RequestMethod.GET)
    @RequiresPermissions("USER_PROFILE")
    public String profile(Model model) {
        ShiroUser shiroUser = userService.getShiroUser();
        User user = userService.findUserById(shiroUser.getId());
        UserProfile userProfile = userService.findUserProfileByUsername(shiroUser.getUsername());

        model.addAttribute("user", user);
        model.addAttribute("userProfile", userProfile);
        return getPathRoot() + "/profile";
    }

    @RequestMapping(value = "profile", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> profile(@ModelAttribute(value = "userProfile") @Valid UserProfile userProfile, BindingResult result,
                                       @ModelAttribute(value = "user") @Valid User user, BindingResult bindingResult,
                                       @RequestParam(value = "avatar", required = false) MultipartFile avatar) throws FileUploadException {
        Map<String, Object> resultMap = getResultMap();
        ShiroUser shiroUser = userService.getShiroUser();

        if (!result.hasErrors() && !bindingResult.hasErrors()) {
            if (avatar != null && !avatar.isEmpty()) {
                String fileName = fileUpload.upload(avatar);
                String large = images.large(fileName);
                userProfile.setLargeAvatar(large);
                String middle = images.middle(fileName);
                userProfile.setMediumAvatar(middle);
                String small = images.small(fileName);
                user.setSmallAvatar(small);
            }

            userService.updateUserAndProfile(user, userProfile);
            resultMap.put("user", userService.findUserById(shiroUser.getId()));
            resultMap.put("userProfile", userService.findUserProfileByUsername(shiroUser.getUsername()));
        } else {
            setResultMapFailure(resultMap);
        }

        return resultMap;
    }

}

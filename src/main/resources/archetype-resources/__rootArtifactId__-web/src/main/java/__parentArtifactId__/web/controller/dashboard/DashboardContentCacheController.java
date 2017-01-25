#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.web.controller.dashboard;

import ${package}.${parentArtifactId}.biz.service.DictionaryService;
import ${package}.${parentArtifactId}.biz.service.RedisService;
import ${package}.${parentArtifactId}.model.constants.DictionaryType;
import ${package}.${parentArtifactId}.model.vo.Dictionary;
import ${package}.${parentArtifactId}.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author kangyonggan
 * @since 2017/1/9
 */
@Controller
@RequestMapping("dashboard/content/cache")
public class DashboardContentCacheController extends BaseController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 缓存管理
     *
     * @param project
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions("CONTENT_CACHE")
    public String list(@RequestParam(value = "project", required = false, defaultValue = "") String project,
                       Model model) {
        Set<String> keys = new HashSet();
        if (StringUtils.isNotEmpty(project)) {
            keys = redisService.getKeys(project + "*");
        }
        List<Dictionary> projects = dictionaryService.findDictionariesByType(DictionaryType.SYSTEM.getType());

        model.addAttribute("keys", keys);
        model.addAttribute("projects", projects);
        return getPathList();
    }

    /**
     * 缓存详情
     *
     * @param key
     * @param model
     * @return
     */
    @RequestMapping(value = "{key:[${symbol_escape}${symbol_escape}w:]+}", method = RequestMethod.GET)
    @RequiresPermissions("CONTENT_CACHE")
    public String detail(@PathVariable("key") String key, Model model) {
        Object cache = redisService.get(key);

        model.addAttribute("key", key);
        model.addAttribute("cache", cache);
        model.addAttribute("isList", cache instanceof List);
        return getPathDetail();
    }

    /**
     * 清空缓存
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "{key:[${symbol_escape}${symbol_escape}w:]+}/clear", method = RequestMethod.GET)
    @RequiresPermissions("CONTENT_CACHE")
    @ResponseBody
    public Map<String, Object> clear(@PathVariable("key") String key) {
        redisService.delete(key);
        return getResultMap();
    }

    /**
     * 清空列表缓存
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "clearall", method = RequestMethod.GET)
    @RequiresPermissions("CONTENT_CACHE")
    @ResponseBody
    public Map<String, Object> clearList(@RequestParam("project") String project) {
        redisService.deleteAll(project + "*");
        return getResultMap();
    }

}

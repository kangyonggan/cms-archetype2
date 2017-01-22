#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.web.controller.dashboard;

import com.github.pagehelper.PageInfo;
import ${package}.${parentArtifactId}.biz.service.DictionaryService;
import ${package}.${parentArtifactId}.model.constants.DictionaryType;
import ${package}.${parentArtifactId}.model.vo.Dictionary;
import ${package}.${parentArtifactId}.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author kangyonggan
 * @since 2017/1/9
 */
@Controller
@RequestMapping("dashboard/content/dictionary")
public class DashboardContentDictionaryController extends BaseController {

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 字典列表
     *
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions("CONTENT_DICTIONARY")
    public String list(@RequestParam(value = "p", required = false, defaultValue = "1") int pageNum,
                       @RequestParam(value = "type", required = false, defaultValue = "") String type,
                       @RequestParam(value = "value", required = false, defaultValue = "") String value,
                       Model model) {
        List<Dictionary> dictionaries = dictionaryService.searchDictionsries(pageNum, type, value);
        PageInfo<Dictionary> page = new PageInfo(dictionaries);

        model.addAttribute("page", page);
        model.addAttribute("types", DictionaryType.values());
        return getPathList();
    }

    /**
     * 添加
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    @RequiresPermissions("CONTENT_DICTIONARY")
    public String create(Model model) {
        model.addAttribute("types", DictionaryType.values());
        model.addAttribute("dictionary", new Dictionary());
        return getPathFormModal();
    }

    /**
     * 删除/恢复
     *
     * @param id
     * @param isDeleted
     * @param model
     * @return
     */
    @RequestMapping(value = "{id:[${symbol_escape}${symbol_escape}d]+}/{isDeleted:${symbol_escape}${symbol_escape}bundelete${symbol_escape}${symbol_escape}b|${symbol_escape}${symbol_escape}bdelete${symbol_escape}${symbol_escape}b}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @RequiresPermissions("CONTENT_DICTIONARY")
    public String delete(@PathVariable("id") Long id, @PathVariable("isDeleted") String isDeleted, Model model) {
        Dictionary dictionary = dictionaryService.findDictionaryById(id);
        dictionary.setIsDeleted((byte) (isDeleted.equals("delete") ? 1 : 0));
        dictionaryService.updateDictionary(dictionary);

        model.addAttribute("dictionary", dictionary);
        model.addAttribute("types", DictionaryType.values());
        return getPathTableTr();
    }

    /**
     * 保存
     *
     * @param dictionary
     * @param result
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @RequiresPermissions("CONTENT_DICTIONARY")
    @ResponseBody
    public Map<String, Object> save(@ModelAttribute("dictionary") @Valid Dictionary dictionary, BindingResult result) {
        Map<String, Object> resultMap = getResultMap();

        if (!result.hasErrors()) {
            dictionaryService.saveDictionary(dictionary);
        } else {
            setResultMapFailure(resultMap);
        }

        return resultMap;
    }

    /**
     * 编辑
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "{id:[${symbol_escape}${symbol_escape}d]+}/edit", method = RequestMethod.GET)
    @RequiresPermissions("CONTENT_DICTIONARY")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("dictionary", dictionaryService.findDictionaryById(id));
        model.addAttribute("types", DictionaryType.values());
        return getPathFormModal();
    }

    /**
     * 更新
     *
     * @param dictionary
     * @param result
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @RequiresPermissions("CONTENT_DICTIONARY")
    @ResponseBody
    public Map<String, Object> update(@ModelAttribute("dictionary") @Valid Dictionary dictionary, BindingResult result) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> resultMap = getResultMap();

        if (!result.hasErrors()) {
            dictionaryService.updateDictionary(dictionary);
        } else {
            setResultMapFailure(resultMap);
        }

        return resultMap;
    }
}

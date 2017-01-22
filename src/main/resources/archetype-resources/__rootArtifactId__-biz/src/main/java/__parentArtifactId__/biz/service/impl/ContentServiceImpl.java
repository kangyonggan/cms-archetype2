#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.biz.service.impl;

import com.github.pagehelper.PageHelper;
import ${package}.${parentArtifactId}.biz.service.AttachmentService;
import ${package}.${parentArtifactId}.biz.service.ContentService;
import ${package}.${parentArtifactId}.biz.util.StringUtil;
import ${package}.${parentArtifactId}.model.annotation.CacheDelete;
import ${package}.${parentArtifactId}.model.annotation.CacheGetOrSave;
import ${package}.${parentArtifactId}.model.annotation.LogTime;
import ${package}.${parentArtifactId}.model.constants.AppConstants;
import ${package}.${parentArtifactId}.model.vo.Attachment;
import ${package}.${parentArtifactId}.model.vo.Content;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/1/21
 */
@Service
public class ContentServiceImpl extends BaseService<Content> implements ContentService {

    @Autowired
    private AttachmentService attachmentService;

    @Override
    @LogTime
    public List<Content> searchContents(int pageNum, String template, String title) {
        Example example = new Example(Content.class);
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotEmpty(template)) {
            criteria.andEqualTo("template", template);
        }
        if (StringUtils.isNotEmpty(title)) {
            criteria.andLike("title", StringUtil.toLikeString(title));
        }
        example.setOrderByClause("id desc");

        PageHelper.startPage(pageNum, AppConstants.PAGE_SIZE);
        return super.selectByExample(example);
    }

    @Override
    @LogTime
    public void saveContentWithAttachments(Content content, List<Attachment> attachments) {
        super.insertSelective(content);

        if (attachments != null && !attachments.isEmpty()) {
            attachmentService.saveAttachments(content.getId(), attachments);
        }
    }

    @Override
    @LogTime
    @CacheGetOrSave("content:id:{0}")
    public Content findContentById(Long id) {
        return super.selectByPrimaryKey(id);
    }

    @Override
    @LogTime
    @CacheDelete("content:id:{0:id}")
    public void updateContentWithAttachments(Content content, List<Attachment> attachments) {
        super.updateByPrimaryKeySelective(content);

        if (attachments != null && !attachments.isEmpty()) {
            attachmentService.saveAttachments(content.getId(), attachments);
        }
    }

    @Override
    @LogTime
    @CacheDelete("content:id:{0}")
    public void deleteContent(Long id) {
        super.deleteByPrimaryKey(id);
    }
}

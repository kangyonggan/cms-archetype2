#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.biz.service.impl;

import ${package}.${parentArtifactId}.biz.service.AttachmentService;
import ${package}.${parentArtifactId}.mapper.AttachmentMapper;
import ${package}.${parentArtifactId}.model.annotation.CacheDelete;
import ${package}.${parentArtifactId}.model.annotation.CacheDeleteAll;
import ${package}.${parentArtifactId}.model.annotation.CacheGetOrSave;
import ${package}.${parentArtifactId}.model.annotation.LogTime;
import ${package}.${parentArtifactId}.model.constants.AppConstants;
import ${package}.${parentArtifactId}.model.vo.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/1/21
 */
@Service
public class AttachmentServiceImpl extends BaseService<Attachment> implements AttachmentService {

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Override
    @LogTime
    @CacheDeleteAll("attachment:source:{0}")
    public void saveAttachments(Long sourceId, List<Attachment> attachments) {
        attachmentMapper.insertAttachments(sourceId, attachments);
    }

    @Override
    @LogTime
    @CacheGetOrSave("attachment:source:{0}:type:{1}")
    public List<Attachment> findAttachmentsBySourceIdAndType(Long sourceId, String type) {
        Attachment attachment = new Attachment();
        attachment.setSourceId(sourceId);
        attachment.setType(type);
        attachment.setIsDeleted(AppConstants.IS_DELETED_NO);

        return super.select(attachment);
    }

    @Override
    @LogTime
    @CacheDelete("attachment:source:{1}:type:{2}")
    public void deleteAttachment(Long id, Long sourceId, String type) {
        Attachment attachment = new Attachment();
        attachment.setIsDeleted(AppConstants.IS_DELETED_YES);

        Example example = new Example(Attachment.class);
        example.createCriteria().andEqualTo("id", id).andEqualTo("sourceId", sourceId).andEqualTo("type", type);

        attachmentMapper.updateByExampleSelective(attachment, example);
    }
}

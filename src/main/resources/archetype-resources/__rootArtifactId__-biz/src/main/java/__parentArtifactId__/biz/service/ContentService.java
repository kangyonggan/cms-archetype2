#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.biz.service;

import ${package}.${parentArtifactId}.model.vo.Attachment;
import ${package}.${parentArtifactId}.model.vo.Content;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/1/21
 */
public interface ContentService {

    /**
     * 搜索内容
     *
     * @param pageNum
     * @param template
     * @param title
     * @return
     */
    List<Content> searchContents(int pageNum, String template, String title);

    /**
     * 保存内容和附件
     *
     * @param content
     * @param attachments
     */
    void saveContentWithAttachments(Content content, List<Attachment> attachments);

    /**
     * 查找内容
     *
     * @param id
     * @return
     */
    Content findContentById(Long id);

    /**
     * 更新内容
     *
     * @param content
     * @param attachments
     */
    void updateContentWithAttachments(Content content, List<Attachment> attachments);

    /**
     * 删除
     *
     * @param id
     */
    void deleteContent(Long id);
}

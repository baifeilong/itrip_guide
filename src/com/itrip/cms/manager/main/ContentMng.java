package com.itrip.cms.manager.main;

import java.util.List;
import java.util.Map;

import com.itrip.cms.entity.main.CmsUser;
import com.itrip.cms.entity.main.Content;
import com.itrip.cms.entity.main.ContentExt;
import com.itrip.cms.entity.main.ContentTxt;
import com.itrip.cms.entity.main.Content.ContentStatus;
import com.itrip.cms.staticpage.exception.ContentNotCheckedException;
import com.itrip.cms.staticpage.exception.GeneratedZeroStaticPageException;
import com.itrip.cms.staticpage.exception.StaticPageNotOpenException;
import com.itrip.cms.staticpage.exception.TemplateNotFoundException;
import com.itrip.cms.staticpage.exception.TemplateParseException;
import com.itrip.common.page.Pagination;

public interface ContentMng {
	public Pagination getPageByRight(Integer id, String title, Integer typeId, Integer inputUserId, boolean topLevel, boolean recommend,
			ContentStatus status, Byte checkStep, Integer siteId, Integer channelId, Integer userId, boolean needRegenerate, int orderBy, int pageNo,
			int pageSize);

	/**
	 * 获得文章分页。供会员中心使用。
	 * 
	 * @param title
	 *            文章标题
	 * @param channelId
	 *            栏目ID
	 * @param siteId
	 *            站点ID
	 * @param memberId
	 *            会员ID
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页大小
	 * @return 文章分页对象
	 */
	public Pagination getPageForMember(String title, Integer channelId, Integer siteId, Integer memberId, int pageNo, int pageSize);

	/**
	 * 根据内容ID数组获取文章列表
	 * 
	 * @param ids
	 * @param orderBy
	 * @return
	 */
	public List<Content> getListByIdsForTag(Integer[] ids, int orderBy);

	public Content getSide(Integer id, Integer siteId, Integer channelId, boolean next);

	public Pagination getPageBySiteIdsForTag(Integer[] siteIds, Integer[] typeIds, Boolean titleImg, Boolean recommend, String title, int orderBy,
			int pageNo, int pageSize);

	public List<Content> getListBySiteIdsForTag(Integer[] siteIds, Integer[] typeIds, Boolean titleImg, Boolean recommend, String title, int orderBy,
			Integer first, Integer count);

	public Pagination getPageByChannelIdsForTag(Integer[] channelIds, Integer[] typeIds, Boolean titleImg, Boolean recommend, String title,
			int orderBy, int option, int pageNo, int pageSize);

	public List<Content> getListByChannelIdsForTag(Integer[] channelIds, Integer[] typeIds, Boolean titleImg, Boolean recommend, String title,
			int orderBy, int option, Integer first, Integer count);

	public Pagination getPageByChannelPathsForTag(String[] paths, Integer[] siteIds, Integer[] typeIds, Boolean titleImg, Boolean recommend,
			String title, int orderBy, int pageNo, int pageSize);

	public List<Content> getListByChannelPathsForTag(String[] paths, Integer[] siteIds, Integer[] typeIds, Boolean titleImg, Boolean recommend,
			String title, int orderBy, Integer first, Integer count);

	public Pagination getPageByTopicIdForTag(Integer topicId, Integer[] siteIds, Integer[] channelIds, Integer[] typeIds, Boolean titleImg,
			Boolean recommend, String title, int orderBy, int pageNo, int pageSize);

	public List<Content> getListByTopicIdForTag(Integer topicId, Integer[] siteIds, Integer[] channelIds, Integer[] typeIds, Boolean titleImg,
			Boolean recommend, String title, int orderBy, Integer first, Integer count);

	public Pagination getPageByTagIdsForTag(Integer[] tagIds, Integer[] siteIds, Integer[] channelIds, Integer[] typeIds, Integer excludeId,
			Boolean titleImg, Boolean recommend, String title, int orderBy, int pageNo, int pageSize);

	public List<Content> getListByTagIdsForTag(Integer[] tagIds, Integer[] siteIds, Integer[] channelIds, Integer[] typeIds, Integer excludeId,
			Boolean titleImg, Boolean recommend, String title, int orderBy, Integer first, Integer count);

	public Content findById(Integer id);

	public Content save(Content bean, ContentExt ext, ContentTxt txt, Integer[] channelIds, Integer[] topicIds, Integer[] viewGroupIds,
			String[] tagArr, String[] attachmentPaths, String[] attachmentNames, String[] attachmentFilenames, String[] picPaths, String[] picDescs,
			Integer channelId, Integer typeId, Boolean draft, CmsUser user, boolean forMember);

	public Content update(Content bean, ContentExt ext, ContentTxt txt, String[] tagArr, Integer[] channelIds, Integer[] topicIds,
			Integer[] viewGroupIds, String[] attachmentPaths, String[] attachmentNames, String[] attachmentFilenames, String[] picPaths,
			String[] picDescs, Map<String, String> attr, Integer channelId, Integer typeId, Boolean draft, CmsUser user, boolean forMember);

	public Content check(Integer id, CmsUser user);

	public Content[] check(Integer[] ids, CmsUser user);

	public Content update(Content bean);

	/**
	 * 重置内容页静态化
	 * 
	 * @return
	 */
	public int resetNeedStatic();

	public Content reject(Integer id, CmsUser user, Byte step, String opinion);

	public Content[] reject(Integer[] ids, CmsUser user, Byte step, String opinion);

	public Content cycle(Integer id);

	public Content[] cycle(Integer[] ids);

	public Content recycle(Integer id);

	public Content[] recycle(Integer[] ids);

	public Content deleteById(Integer id);

	public Content[] deleteByIds(Integer[] ids);

	/**
	 * 内容列表页生成静态页面
	 * 
	 * @param ids
	 *            内容ID
	 * @return
	 * @throws TemplateNotFoundException
	 * @throws TemplateParseException
	 * @throws GeneratedZeroStaticPageException
	 * @throws StaticPageNotOpenException
	 * @throws ContentNotCheckedException
	 */
	public Content[] contentStatic(Integer[] ids) throws TemplateNotFoundException, TemplateParseException, GeneratedZeroStaticPageException,
			StaticPageNotOpenException, ContentNotCheckedException;

	public Pagination getPageForCollection(Integer siteId, Integer memberId, int pageNo, int pageSize);

	public void updateFileByContent(Content bean, Boolean valid);
}
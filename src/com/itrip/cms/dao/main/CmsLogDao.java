package com.itrip.cms.dao.main;

import java.util.Date;

import com.itrip.cms.entity.main.CmsLog;
import com.itrip.common.page.Pagination;

public interface CmsLogDao {
	public Pagination getPage(Integer category, Integer siteId, Integer userId, String title, String ip, int pageNo, int pageSize);

	public CmsLog findById(Integer id);

	public CmsLog save(CmsLog bean);

	public CmsLog deleteById(Integer id);

	public int deleteBatch(Integer category, Integer siteId, Date before);
}
package com.itrip.cms.dao.assist;

import java.util.List;

import com.itrip.cms.entity.assist.CmsFile;
import com.itrip.common.hibernate3.Updater;

public interface CmsFileDao {
	public List<CmsFile> getList(Boolean valid);

	public CmsFile findById(Integer id);

	public CmsFile findByPath(String path);

	public CmsFile save(CmsFile bean);

	public CmsFile updateByUpdater(Updater<CmsFile> updater);

	public CmsFile deleteById(Integer id);

	public CmsFile deleteByPath(String path);

	public void deleteByContentId(Integer contentId);
}
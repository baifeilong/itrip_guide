package com.itrip.cms.action.member;

import static com.itrip.cms.Constants.TPLDIR_MEMBER;
import static com.itrip.common.page.SimplePage.cpn;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itrip.cms.entity.assist.CmsJobApply;
import com.itrip.cms.entity.main.CmsSite;
import com.itrip.cms.entity.main.CmsUser;
import com.itrip.cms.entity.main.CmsUserExt;
import com.itrip.cms.entity.main.CmsUserResume;
import com.itrip.cms.entity.main.Content;
import com.itrip.cms.entity.main.MemberConfig;
import com.itrip.cms.manager.assist.CmsJobApplyMng;
import com.itrip.cms.manager.main.CmsUserExtMng;
import com.itrip.cms.manager.main.CmsUserResumeMng;
import com.itrip.cms.manager.main.ContentMng;
import com.itrip.cms.web.CmsUtils;
import com.itrip.cms.web.FrontUtils;
import com.itrip.cms.web.WebErrors;
import com.itrip.common.page.Pagination;
import com.itrip.common.web.CookieUtils;
import com.itrip.common.web.ResponseUtils;

/**
 * 简历中心Action
 */
@Controller
public class ResumeAct {
	private static final Logger log = LoggerFactory.getLogger(ResumeAct.class);

	// 简历修改页
	public static final String MEMBER_RESUME = "tpl.resumeEdit";
	// 我的职位申请记录
	public static final String MEMBER_APPLYS = "tpl.jobApplys";

	/**
	 * 简历输入页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/resume.jspx", method = RequestMethod.GET)
	public String resumeInput(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		return FrontUtils.getTplPath(request, site, TPLDIR_MEMBER, MEMBER_RESUME);
	}

	/**
	 * 简历提交页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/member/resume.jspx", method = RequestMethod.POST)
	public String resumeSubmit(CmsUserResume resume, CmsUserExt ext, String nextUrl, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		ext.setId(user.getId());
		cmsUserExtMng.update(ext, user);
		resume.setId(user.getId());
		cmsUserResumeMng.update(resume, user);
		log.info("update CmsUserExt success. id={}", user.getId());
		return FrontUtils.showSuccess(request, model, nextUrl);
	}

	@RequestMapping(value = "/member/myapplys.jspx", method = RequestMethod.GET)
	public String myapplys(Integer pageNo, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		Pagination pagination = jobApplyMng.getPage(user.getId(), null, null, true, cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		return FrontUtils.getTplPath(request, site, TPLDIR_MEMBER, MEMBER_APPLYS);
	}

	@RequestMapping(value = "/member/jobapply_delete.jspx")
	public String delete(Integer[] ids, HttpServletRequest request, String nextUrl, HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		WebErrors errors = validateDelete(ids, site, user, request);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		CmsJobApply[] arr = jobApplyMng.deleteByIds(ids);
		log.info("member contribute delete Content success. ids={}", StringUtils.join(arr, ","));
		return FrontUtils.showSuccess(request, model, nextUrl);
	}

	@RequestMapping(value = "/member/jobapply.jspx")
	public void jobapply(Integer cId, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws JSONException {
		CmsUser user = CmsUtils.getUser(request);
		JSONObject object = new JSONObject();
		if (user == null) {
			object.put("result", -1);
		} else if (cId == null) {
			object.put("result", -2);
		} else {
			Content c = contentMng.findById(cId);
			if (c == null) {
				object.put("result", -3);
			} else if (user.getUserResume() == null) {
				object.put("result", -4);
			} else if (user.hasApplyToday(cId)) {
				object.put("result", 0);
			} else {
				CmsJobApply jobApply = new CmsJobApply();
				jobApply.setApplyTime(Calendar.getInstance().getTime());
				jobApply.setContent(c);
				jobApply.setUser(user);
				jobApplyMng.save(jobApply);
				object.put("result", 1);
			}
		}
		ResponseUtils.renderJson(response, object.toString());
	}

	private WebErrors validateDelete(Integer[] ids, CmsSite site, CmsUser user, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldOpt(errors, site, user, ids)) {
			return errors;
		}
		return errors;
	}

	private boolean vldOpt(WebErrors errors, CmsSite site, CmsUser user, Integer[] ids) {
		for (Integer id : ids) {
			if (errors.ifNull(id, "id")) {
				return true;
			}
			CmsJobApply jobapply = jobApplyMng.findById(id);
			// 数据不存在
			if (errors.ifNotExist(jobapply, CmsJobApply.class, id)) {
				return true;
			}
			// 非本用户数据
			if (!jobapply.getUser().getId().equals(user.getId())) {
				errors.noPermission(Content.class, id);
				return true;
			}
		}
		return false;
	}

	@Autowired
	private CmsUserExtMng cmsUserExtMng;
	@Autowired
	private CmsUserResumeMng cmsUserResumeMng;
	@Autowired
	private CmsJobApplyMng jobApplyMng;
	@Autowired
	private ContentMng contentMng;
}

package com.hao.app.manager.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hao.app.commons.entity.result.JsonResult;
import com.hao.app.commons.entity.result.JsonResultAjax;
import com.hao.app.pojo.SysRole;
import com.hao.app.service.SysRolePrivilegeService;
import com.hao.app.service.SysRoleService;

/**
 * 角色管理
 * @author haoguowei
 *
 */
@Controller
public class SysRoleController extends BaseController{
	
	private Logger logger = LoggerFactory.getLogger(SysRoleController.class);
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private SysRolePrivilegeService sysRolePrivilegeService;

	@RequestMapping("/initRole.do")
	public String initRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return "jsp/role";
	}
	
	@RequestMapping("/searchRoles.do")
	public void searchRoles(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<SysRole> ls = sysRoleService.queryAllRoles();
		
		JsonResult<SysRole> result = new JsonResult<SysRole>(ls.size(), ls);
		writeResponse(response, result);
	}
	
	@RequestMapping("/saveRolePrivileges.do")
	public void saveRolePrivileges(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int roleId = NumberUtils.toInt(request.getParameter("roleId"));
		String priIds = request.getParameter("priIds");
		
		boolean result = sysRolePrivilegeService.saveRolePrivileges(roleId, priIds);
		sysLogsService.writeLog(getCurrentUserName(request),"设置角色权限，角色："+roleId+";权限："+priIds);
		
		writeResponse(response, new JsonResultAjax(result));
	}
}

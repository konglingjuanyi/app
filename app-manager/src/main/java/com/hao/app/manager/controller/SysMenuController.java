package com.hao.app.manager.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hao.app.commons.entity.TreeNodeMode;
import com.hao.app.commons.entity.result.JsonResult;
import com.hao.app.pojo.SysMember;
import com.hao.app.pojo.SysMenu;
import com.hao.app.service.SysMenuService;

@Controller
public class SysMenuController extends BaseController{
	
	private Logger logger = LoggerFactory.getLogger(SysMenuController.class);
	
	@Autowired
	private SysMenuService sysMenuService;
	
	@RequestMapping("/initMenu.do")
	public String initMenu(HttpServletRequest request,HttpServletResponse response) throws IOException {
		return "jsp/menu";
	}
	
	@RequestMapping("/searchMenus.do")
	public void searchMenus(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<SysMenu> ls = sysMenuService.getMenuList();
		JsonResult<SysMenu> result = new JsonResult<SysMenu>(ls.size(), ls);
		writeResponse(response, result);
	}
	
	/**
	 * 左侧菜单树的数据
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/getSysMenuTree.do")
	public void getSysMenuTree(HttpServletRequest request,HttpServletResponse response) throws IOException {
		SysMember user = getCurrentUser(request);
		if(user == null){
			logger.error("currentUser is null!");
			return;
		}
		
		//得到用户的权限url
		List<String> priList = user.getPriUrls();
		
		//得到所有目录树数据
		List<TreeNodeMode> all = sysMenuService.getMenuTree();
		
		//只返回有权限的节点
		List<TreeNodeMode> result = new ArrayList<TreeNodeMode>();
		
		for(TreeNodeMode node : all){
			if(!node.isLeaf()){
				List<TreeNodeMode> childs = node.getChildren();
				List<TreeNodeMode> newChilds = new ArrayList<TreeNodeMode>();
				for(TreeNodeMode tmp : childs){
					if(priList.contains(tmp.getHref())){
						newChilds.add(tmp);
					}
				}
				if(newChilds.size() > 0){
					node.setChildren(newChilds);
					result.add(node);
				}
			}else{
				if(priList.contains(node.getHref())){
					result.add(node);
				}
			}
		}
		writeResponse(response, result);
	}
		
}
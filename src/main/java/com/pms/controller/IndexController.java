package com.pms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pms.entity.Administrator;
import com.pms.entity.Employee;
import com.pms.service.AdminService;
import com.pms.service.EmployeeService;
import com.pms.util.AESUtil;

/**
 * 主页控制器
 * @author Taowd
 * @version 2018年9月2日
 * @see IndexController
 */
@Controller
// 注意 这里不要定义@RequestMapping
public class IndexController {

	/**
	 * 日志工具
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private AdminService adminService;

	@Autowired
	private EmployeeService employeeService;

	private static final String ERROR = "error";

	/**
	 * 主页导航功能.
	 * @return 登录主页地址
	 */
	@RequestMapping("/")
	public String index() {
		// 如果你的spring mvc配置文件中配置了跳转后缀则不需要加.jsp后缀
		// 即直接return "demo/pagefile";
		LOGGER.debug("登录主页");
		return "index";
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();

		request.getSession().removeAttribute("role");

		LOGGER.debug("logout");

		return "index";
	}

	/**
	 * 登录功能 此处使用重定向方式，注意：重定向可以修改URL地址 防止表单重复提交..
	 * @param userName 用户名
	 * @param password 密码
	 * @param role     角色
	 * @return 主页地址
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(final Model model, final @RequestParam(name = "userName") String userName,
			final @RequestParam(name = "password") String password,
			final @RequestParam(name = "role") String role, HttpSession httpSession) {
		try {
			// 检查用户信息
			if (StringUtils.equals(role, "user")) {

				Employee employee = employeeService.checkEmployee(userName,
						AESUtil.parseByte2HexStr(AESUtil.encrypt(password)), role);

				if (employee == null) {// 登录失败
					LOGGER.error("管理员用户校验失败：user" + userName);
					model.addAttribute(ERROR, "用户名或密码输入错误!");
					return "index";
				} else {
					// 设置session
					httpSession.setAttribute("user", employee);
					httpSession.setAttribute("role", role);
				}

			} else if (StringUtils.equals(role, "admin")
					|| StringUtils.equals(role, "superAdmin")) { // 检查管理员信息

				Administrator admin = adminService.checkAdmin(userName,
						AESUtil.parseByte2HexStr(AESUtil.encrypt(password)), role);
				if (admin == null) {// 登录失败
					LOGGER.error("管理员用户校验失败：user" + userName);
					model.addAttribute(ERROR, "用户名或密码输入错误!");
					return "index";
				} else {
					// 设置session
					httpSession.setAttribute("user", admin);
					httpSession.setAttribute("role", role);
				}
			} else {
				model.addAttribute(ERROR, "角色不存在");
				return "index";
			}

		} catch (Exception e) {
			LOGGER.error("登录发生异常，请联系管理员!", e);
			model.addAttribute(ERROR, "登录发生异常，请联系管理员!");
			return "index";
		}

		return "redirect:main";
	}

	/**
	 * 访问主页地址，封装参数.
	 * @param model   model实体
	 * @param request http请求
	 * @return 主页地址
	 */
	@RequestMapping("/main")
	public String main(Model model, HttpSession httpSession) {
		// 更新session中的数据
		String role = (String) httpSession.getAttribute("role");
		Object user = httpSession.getAttribute("user");

		if (user == null || StringUtils.isBlank(role)) {
			return "redirect:logout";
		}

		return "main";
	}
}

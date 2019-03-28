package com.atguigu.atcrowdfunding.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

//服务器启动或停止(卸载)时执行监听器。
@WebListener
public class SystemStartUpListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext application = sce.getServletContext();
		application.setAttribute("APP_PATH", application.getContextPath());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}

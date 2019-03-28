package com.atguigu.atcrowdfunding.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.atguigu.atcrowdfunding.service.MemberService;
import com.atguigu.atcrowdfunding.util.ApplicationContextUtils;

public class PassListener implements ExecutionListener {

	//1.无法依赖注入MemberService对象，因为流程监听器是Activiti框架创建，而MemberService对象是IOC容器框架创建，他们没有在同一个容器中。
	//@Autowired
	//MemberService memberService ;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		//修改会员实名认证状态   1->2
		Integer memberId = (Integer)execution.getVariable("memberId");
		
		//2.不能自己创建IOC容器来获取memberService对象，否则IOC容器就不是单例的。		
		//ApplicationContext ioc = new ClassPathXmlApplicationContext("") ;
		
		//3.通过工具类，获取服务器启动时创建的IOC容器；由于获取不到application对象，所以，没办法获取到IOC对象。
		//ApplicationContext ioc = WebApplicationContextUtils.getWebApplicationContext(application);
		
		//4.定义工具类ApplicationContextUtils，采用接口注入方式，获取IOC容器对象。
		MemberService memberService = ApplicationContextUtils.applicationContext.getBean(MemberService.class);
		memberService.updateMemberAuthstatus(memberId,"2");
	}

}

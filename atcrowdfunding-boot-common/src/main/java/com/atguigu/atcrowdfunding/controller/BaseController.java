package com.atguigu.atcrowdfunding.controller;

import java.util.HashMap;
import java.util.Map;

public class BaseController {
	
	ThreadLocal<Map<String,Object>> theadLocal = new ThreadLocal<Map<String,Object>>();
	

	public void start() {
		Map<String,Object> result = new HashMap<String,Object>();
		theadLocal.set(result);
	}
	
	public void success(boolean success) {
		theadLocal.get().put("success",success);
	}
	

	public void data(Object data) {
		theadLocal.get().put("data",data);
	}
	
	public void message(String message) {
		theadLocal.get().put("message",message);
	}
	
	
	public Map<String,Object> end(){
		return theadLocal.get(); 
	}
	
}

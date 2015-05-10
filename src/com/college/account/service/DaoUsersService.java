package com.college.account.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.college.account.bean.Users;
import com.college.util.Cause;
import com.college.util.JacksonUtils;
import com.college.util.Json2Obj;
import com.college.util.ServiceFactoryBean;


public class DaoUsersService extends DaoService<Users>{
	
	private int USELOGINLOSTNAMEORID = 1001;
	private int USELOGINIDNOTEXIST = 1002;
	private int USEPWDWRONG = 1003;
	private int USELOGINEXISTORWRONG = 1004;
	private int USEORGIDWRONG = 1005;
	private int USEPOSIDWRONG = 1006;
	private int USEIDNOTEXIST = 1007;
	private int USELOGIDNOTUPT = 1007;
	
	private String tablename = "Users";
	
	public String login(String jsonString) throws Exception{
		
		@SuppressWarnings("unchecked")
		Map<String,String> map = JacksonUtils.objectMapper.readValue(jsonString, Map.class);
		
		if(null == map.get("userName") || null == map.get("password")){
			return Cause.getFailcode(USELOGINLOSTNAMEORID, "login", "can not null");
		}
		
		Users users = searchByFeild(tablename, "loginId", map.get("userName").toString());
		
		if(null == users){
			return Cause.getFailcode(USELOGINIDNOTEXIST, "name", "name not exist");
		}
		
		if(users.getLoginPassword().equals(map.get("password").toString())){
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			resultMap.put("ack", "success");
            dataMap.put("id", users.getId());
            dataMap.put("loginId", users.getLoginId());
            dataMap.put("token", null);
            resultMap.put("data", dataMap);
			return JacksonUtils.getJsonString(resultMap);
		}else{
			return Cause.getFailcode(USEPWDWRONG, "pwd", "pwd wrong");
		}
	}
	
	
	public String save(String jsonString){
		
		Users users = null;
		Users usersfind = null;
		Integer id = null;
		
	    users=(Users)Json2Obj.getObj(jsonString, Users.class);
	    
	    if(null == users.getLoginId()){
	    	return Cause.getFailcode(USELOGINLOSTNAMEORID, "login", "can not null");
	    }
	    
	    /* if org id exist must find it */
	    
	    if(null != users.getOrgId()){
	  
	    	String result = ServiceFactoryBean.getOrganizationService().search(users.getOrgId());
	    	
	    	if(!Cause.isSuccess(result)){
	    		return Cause.getFailcode(USEORGIDWRONG, "orgId", "orgId must exist in Organization table");
	    	}
	    }
	    
	    if(null != users.getPositionId()){
	    	String result = ServiceFactoryBean.getPositionService().search(users.getPositionId());
	    	if(!Cause.isSuccess(result)){
	    		return Cause.getFailcode(USEPOSIDWRONG, "PositionId", "PositionId must exist in Position table");
	    	}
	    }
	    
	    usersfind = searchByFeild(tablename, "loginId", users.getLoginId());
	    
	    if(null != usersfind){
	    	return Cause.getFailcode(USELOGINEXISTORWRONG, "loginId", "exist or lost loginId filed");
	    }
	    
	    users.setCreateTime(new Date());
	    id = create(users);
	    
	    return Cause.getSuccess(id);
	}
	
	public String[] upd(Integer id, String jsonString){
		
		Users users = null;
		Users usersfind = null;
		
		users=(Users)Json2Obj.getObj(jsonString, Users.class);
		
		usersfind = searchByid(id, tablename);
		
		if(null == usersfind)
		{
	    	return new String[]{Cause.getFailcode(USEIDNOTEXIST, "Id", "id not find"),null};
	    }
		
		
		if(users.getLoginId() != null){
			if(!users.getLoginId().equals(usersfind.getLoginId())){
				return new String[]{Cause.getFailcode(USELOGIDNOTUPT, "loginId", "loginId can not update"),null};
			}
		}
		

		if(null != users.getOrgId()){
			  
	    	String result = ServiceFactoryBean.getOrganizationService().search(users.getOrgId());
	    	
	    	if(!Cause.isSuccess(result)){
	    		return new String[]{Cause.getFailcode(USEORGIDWRONG, "orgId", "orgId must exist in Organization table"),null};
	    	}
	    }
	    
	    if(null != users.getPositionId()){
	    	String result = ServiceFactoryBean.getPositionService().search(users.getPositionId());
	    	if(!Cause.isSuccess(result)){
	    		return new String[]{Cause.getFailcode(USEPOSIDWRONG, "PositionId", "PositionId must exist in Position table"),null};
	    	}
	    }
	    
		Json2Obj.repalceDiffObjMem(users, usersfind, Users.class);
		
		update(usersfind);
		
		return new String[]{Cause.getSuccess(id), usersfind.getLoginId()};
	}
	
	public String[] del(Integer id){
		
		/* search */
		Users usersfind = null;
		
		usersfind = searchByid(id, tablename);
		
		if(null == usersfind){
			return new String[]{Cause.getFailcode(USEIDNOTEXIST, "Id", "id not find"),null};
		}
		
		delete(Users.class, usersfind.getId());
		
		return new String[]{Cause.getSuccess(id), usersfind.getLoginId()};
	}
	
	
	/************************************************************************************************************************/
	/* 直接使用dao service 实现具体的dao */
	public String selDao(Integer id){
//		List<Object> list = (List<Object>)getDao().query("getUsersUserExtInfo", new Integer[]{id});
//		if(list.size() > 0)
//			return list.get(0);
//		else
//			return null;
		return null;
	}
}
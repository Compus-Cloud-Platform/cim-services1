package com.college.account.jersey;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import com.college.account.bean.Organization;
import com.college.util.JacksonUtils;
import com.college.util.Json2Obj;
import com.college.util.Logger4j;
import com.college.util.Obj2Map;
import com.college.util.ServiceFactoryBean;


@Path("/organization")
public class OrganizationRestService
{
	// error code  from 2000
    private static final Logger log = Logger4j.getLogger(OrganizationRestService.class);
    @GET
    @Path("/getOrganizations")
    @Consumes({MediaType.APPLICATION_JSON})
    public String getInfo() 
    {
        String result = null;
        try
        {
            @SuppressWarnings("unchecked")
            List<Organization> lists = ServiceFactoryBean.getOrganizationService().findAllOrganizations();
            
            ObjectMapper mapper = new ObjectMapper();
            Map<String, List<Organization>> p =new HashMap<String, List<Organization>>();
            p.put("Organization", lists);
            result =  mapper.writeValueAsString(p).toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)  
    @Path("/save")
    public String createInfo(@QueryParam("jsonString") String jsonString) 
    {
        Integer id = null;
        Map resultMap = new HashMap();
        Map dataMap = new HashMap();
        
        try
        {	
        			
        	Organization orgObj = (Organization)Json2Obj.getObj(jsonString, Organization.class);
        	
        	String name = orgObj.getName();
        	
        	Date createDate = orgObj.getCreateDate();
            Integer operId = orgObj.getOperId();
        	
        	if(null == name || !StringUtils.isNotBlank(name)){
        		resultMap.put("ack", "failure");
    			dataMap.put("code", 2001);
    			dataMap.put("field", "name");
    			dataMap.put("description", "name not exist or null");
    			resultMap.put("errors", dataMap);
        	}else if(null == createDate){
        		resultMap.put("ack", "failure");
    			dataMap.put("code", 2003);
    			dataMap.put("field", "createDate");
    			dataMap.put("description", "createDatee can not exist");
    			resultMap.put("errors", dataMap);;
        	}else if(null == operId){
        		resultMap.put("ack", "failure");
    			dataMap.put("code", 2004);
    			dataMap.put("field", "operId");
    			dataMap.put("description", "operId must exist");
    			resultMap.put("errors", dataMap);;
        	}else{
        		id = ServiceFactoryBean.getOrganizationService().createOrganization(orgObj);
        	
        		resultMap.put("ack", "success");
    			dataMap.put("id", id);
    			resultMap.put("data", dataMap);;
        	}
        	
        }
        catch (Exception e) {
            log.error("Save organization failed.");
            log.error(e);
        }
        
        return JacksonUtils.getJsonString(resultMap);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)  
    @Path("/save")
    public String createInfoPost(@QueryParam("jsonString") String jsonString) {
    		
    	return createInfo(jsonString);
    }
    
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/update")
    public String updInfoGet(@QueryParam("jsonString") String jsonString)
    {
    	Organization org = null;
    	Organization orgFind = null;
    	Map resultMap = new HashMap();
        Map dataMap = new HashMap();
    	
    	org = (Organization)Json2Obj.getObj(jsonString, Organization.class);
    	
    	if(null != org){
    		orgFind = ServiceFactoryBean.getOrganizationService().findAllOrganizationsById(org.getId());
    	}
    	//ServiceFactoryBean.getOrganizationService().
    	if(null == org || null == orgFind){
    		
    		resultMap.put("ack", "failure");
			dataMap.put("code", 2002);
			dataMap.put("field", "id");
			dataMap.put("description", "id not find or no id");
			resultMap.put("errors", dataMap);
    	}else{
    		
    		Json2Obj.repalceDiffObjMem(org, orgFind, Organization.class);
    				
    		ServiceFactoryBean.getOrganizationService().updateOrganization(orgFind);
    		
    		resultMap.put("ack", "success");
    	}
    	
    	return JacksonUtils.getJsonString(resultMap);
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/update")
    public String updInfoPost(@QueryParam("jsonString") String jsonString)
    {
    	return updInfoGet(jsonString);
    }
    
    
    
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/delete")
    public String deleteInfoGet(@QueryParam("id") String id)
    {
    	Organization org = null;
    	Organization orgFind = null;
    	Map resultMap = new HashMap();
        Map dataMap = new HashMap();
        Integer idtemp = Integer.valueOf(id);
    	
    
    	orgFind = ServiceFactoryBean.getOrganizationService().findAllOrganizationsById(idtemp);

    	//ServiceFactoryBean.getOrganizationService().
    	if(null == orgFind){
    		resultMap.put("ack", "failure");
			dataMap.put("code", 2002);
			dataMap.put("field", "id");
			dataMap.put("description", "id =" + id + " not find");
			resultMap.put("errors", dataMap);
    	}else{
    		
    		ServiceFactoryBean.getOrganizationService().deleteOrganization(Organization.class, idtemp);
    		
    		resultMap.put("ack", "success");
    	}
    	
    	return JacksonUtils.getJsonString(resultMap);
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/delete")
    public String deleteInfoPost(@QueryParam("id") String id)
    {
        
        return deleteInfoGet(id);
    }
    
    
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/search")
    public String getInfoGet(@QueryParam("id") String id){
    	Integer idtemp = Integer.valueOf(id);
    	
    	Map resultMap = new HashMap();
        Map dataMap = new HashMap();
        
        Organization org = null;
        
        org= ServiceFactoryBean.getOrganizationService().findAllOrganizationsById(idtemp);
        
        if(null == org){
        	resultMap.put("ack", "failure");
			dataMap.put("code", 2002);
			dataMap.put("field", "id");
			dataMap.put("description", "id =" + id + " not find");
			resultMap.put("errors", dataMap);
        }else{
        	
        	Map userMap = Obj2Map.toMap(org, Organization.class);
        	

			List data = new ArrayList();
			data.add(userMap);
			
			resultMap.put("ack", "success");
			
			resultMap.put("data", data);
			resultMap.put("datanum", 1);
        }
        
        return JacksonUtils.getJsonString(resultMap);
    }
}

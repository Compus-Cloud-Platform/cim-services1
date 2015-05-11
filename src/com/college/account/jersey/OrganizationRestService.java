package com.college.account.jersey;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

import com.college.account.bean.Department;
import com.college.account.bean.DeptOrg;
import com.college.account.bean.Organization;
import com.college.account.service.DaoDepartmentService;
import com.college.account.service.DaoDeptOrgService;
import com.college.account.service.DaoOrganizationService;
import com.college.util.Cause;
import com.college.util.Json2Obj;
import com.college.util.Logger4j;
import com.college.util.Obj2Map;
import com.college.util.ServiceFactoryBean;


@Path("/organizations")
public class OrganizationRestService
{
    private static DaoOrganizationService p = ServiceFactoryBean.getOrganizationService();
    private static DaoDepartmentService pD = ServiceFactoryBean.getDepartmentService();
    private static DaoDeptOrgService pDO = ServiceFactoryBean.getDeptOrgService();
    
    private static final Logger log = Logger4j.getLogger(OrganizationRestService.class);
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON) 
	public String save(String jsonString){
    	
    	try {
			
			return p.save(jsonString);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return Cause.getFailcode(3000, "", "system error");
    }
    
    @PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}")
	public String updateInfo(@PathParam("id") String id,
	                                          String jsonString){
		try {
			
			String result=  p.upd(Integer.parseInt(id), jsonString);
			return result;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		
		return Cause.getFailcode(3000, "", "system error");
	}
    
    @DELETE
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}")
	public String deleteInfo(@PathParam("id") String id){
		try {
			
			String result=  p.del(Integer.parseInt(id));
			return result;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		
		return Cause.getFailcode(3000, "", "system error");
	}
    
    @GET
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}")
	public String searchInfo(@PathParam("id") String id)
	{
		try {
			
			return p.sel(Integer.parseInt(id));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		
		return Cause.getFailcode(3000, "", "system error");
		
	}
    
    
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}/departments")
    public String saveDep(@PathParam("id") String id,
                                           String jsonString){
    	try {
			
			if(!p.selIsExist(Integer.parseInt(id))){
				return Cause.getFailcode(DaoOrganizationService.ORGIDNOTFIND, "id", "id not find");
			}
			
			Department department = (Department)Json2Obj.getObj(jsonString, Department.class);
			
			if(null == department.getId()){
				/* need save*/
				if(null == department.getName()){
					return Cause.getFailcode(DaoDepartmentService.DEPARTMENTLOSTNAME, "name", "must has name");
				}else{
					Integer resultid = null;
					if(pD.selNameUniq(department.getName())){
						resultid = Cause.getResultId(pD.save(jsonString));
					}else{
						resultid = ((Department)pD.searchByFeild(pD.tablename, "name", department.getName())).getId();
					}
					
					pDO.save(Integer.parseInt(id), resultid, department.getOperId());
					
					return Cause.getSuccess(resultid);
				}
			}
			
			if(!pD.selIsExist(department.getId())){
				return Cause.getFailcode(DaoOrganizationService.ORGIDNOTFIND, "id", "id not find");
			}
			
			pDO.save(Integer.parseInt(id), department.getId(), department.getOperId());
			
			return Cause.getSuccess(department.getId());
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return Cause.getFailcode(3000, "", "system error");
    }
    
    @DELETE
    @Path("/{id}/departments/{idD}")
    public String deleteDep(@PathParam("id") String id,
    		                @PathParam("idD") String idD){
    	
    	try{
    		return pDO.del(id, idD);
    	}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return Cause.getFailcode(3000, "", "system error");
    	
    }
    
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}/departments")
    public String getDep(@PathParam("id") String id){
    	
    	try{
    		
    		return pDO.get(id);
    		
    	}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return Cause.getFailcode(3000, "", "system error");
    }
    
    @DELETE
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}/departments/{idD}")
    public String getOneDep(@PathParam("id") String id,
                            @PathParam("idD") String idD){
    	try{
    		
    		return pDO.get(id);
    		
    	}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return Cause.getFailcode(3000, "", "system error");
    }
    
    @GET
    @Path("")
    public String getAll(){
    	try{
    		
    		return p.getAllObject();
    		
    	}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		return Cause.getFailcode(3000, "", "system error");
    }
    
    
    
    
}

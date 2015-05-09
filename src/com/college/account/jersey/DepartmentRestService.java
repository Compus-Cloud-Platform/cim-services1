package com.college.account.jersey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.college.account.bean.Department;
import com.college.util.Cause;
import com.college.util.Json2Obj;
import com.college.util.Obj2Map;
import com.college.util.ServiceFactoryBean;


@Path("/departments")
public class DepartmentRestService {
	
	private static int DEPLOSTNAME = 4001;
	private static int DEPIDNOTFIND = 4002;
	
	private static String tablename = "Department";

	@GET
    @Path("/save")
	@Consumes({MediaType.APPLICATION_JSON})
	public String save(@QueryParam("jsonString") String jsonString){
		Integer id = null;
		Department department = null;
		department = (Department)Json2Obj.getObj(jsonString, Department.class);
		
		if(null == department.getName()){
			return Cause.getFailcode(DEPLOSTNAME, "name", null);
		}
		
		department.setCreateTime(new Date());
		
		id = ServiceFactoryBean.getDepartmentService().create(department);
		
		return Cause.getSuccess(id);
	}
	
	 @GET
	 @Consumes({MediaType.APPLICATION_JSON})
	 @Path("/id")
	 public String searchInfo(@QueryParam("id") String id)
	 {
		 Department department = null;
		 List list = new ArrayList(1);
		 
		 department = ServiceFactoryBean.getDepartmentService().searchByid(Integer.parseInt(id), tablename);
		 
		 if(null == department){ return Cause.getFailcode(DEPIDNOTFIND, "id", null);}
		 
		 list.add(Obj2Map.toMap(department, Department.class));
		 
		 return Cause.getData(list);
	 }
	 
	 @GET
	 @Consumes({MediaType.APPLICATION_JSON})
	 @Path("/update")
	 public String updateInfo(@QueryParam("jsonString") String jsonString){
		 
		 Department department = null;
		 Department departmentfind = null;
		 department = (Department)Json2Obj.getObj(jsonString, Department.class);
		 
		 departmentfind = ServiceFactoryBean.getDepartmentService().searchByid(department.getId(), tablename);
		 
		 if(null == department){ return Cause.getFailcode(DEPIDNOTFIND, "id", null);}
		 
		 Json2Obj.repalceDiffObjMem(department, departmentfind, Department.class);
		 
		 ServiceFactoryBean.getDepartmentService().update(departmentfind);
		 
		 return Cause.getSuccess(null);
	 }
	 
//	 @GET
//	 @Consumes({MediaType.APPLICATION_JSON})
//	 @Path("/id")
//	 public String deleteInfo(@QueryParam("id") String id){
//		 
//		 ServiceFactoryBean.getDepartmentService().delete(Department.class, Integer.parseInt(id));
//		 
//		 return Cause.getSuccess(null);
//	 }

}

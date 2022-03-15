package com.web.upload;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import com.CheckUtil;
import com.JSONListFormat;
import com.SystemProperties;
import com.web.WebUtil;


@WebServlet("/uploadFile")
@MultipartConfig 
public class UploadFile extends HttpServlet {
	
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	
	private static final long serialVersionUID = 1L;
	
	
	private SimpleDateFormat nameDateFormat    = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	
	private String systemFilePath     = SystemProperties.getInstance().getProperty("systemFilePath"); 
	private String serverURL          = SystemProperties.getInstance().getProperty("serverURL"); 
	private File   uploadSaveFile    = new  File(systemFilePath);
	
	
	public UploadFile(){
		
		
		logger.info("===========================");
		logger.info("  文件上传接口（2） web启动成功");
		logger.info("===========================");
			
		logger.info("[uploadFile][systemFilePath ]"+systemFilePath);

		
	}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
    	
    	res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		String responseMessage = "";

		JSONListFormat jsonListFormat  = WebUtil.createJSONListFormat(req, false);
		String path = req.getParameter("path");
		if(CheckUtil.isEmpty(path))path="test";
		
		System.out.println();  
		
		String contentType = req.getContentType();
	
		System.err.println("[req.contentTypes]"+contentType);
		
		      
        if(!req.getContentType().split(";")[0].equals("multipart/form-data"))  return;  
        //print info
        
        Collection<Part> parts = req.getParts();
        int index=0;
        for (Part part : parts) {
        	index++;
        	System.out.println("======================== part"+index+"========================");
        	System.out.println("[part_Name]"+part.getName());
        	System.out.println("[part_ContentType]"+part.getContentType());
        	System.out.println("[part_Size]"+part.getSize());
        	Collection<String> names = part.getHeaderNames();
        	for (String string : names) {
        		System.out.println("["+string+"]"+part.getHeader(string));
			}
        }
        System.out.println();
        
        
      
        Part namePart = req.getPart("name");
        if(namePart!=null) {
        	InputStream inputStream = namePart.getInputStream();
        	StringBuilder sb = new StringBuilder();
        	String line;
        	BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        	while ((line = br.readLine()) != null) {
        	    sb.append(line);
        	}
        	String str = sb.toString();
        	System.err.println("name:"+str);
        	System.out.println();
        }
        
        
        
        Part filePart = req.getPart("file");
    	if(filePart==null)responseMessage="error-file";
    	if(responseMessage==""){
    		logger.info("       name = "+filePart.getName());
  	      	logger.info("   FileName = "+filePart.getSubmittedFileName()); 
  	      	logger.info("       Size = "+filePart.getSize()); 
  	      	logger.info("ContentType = "+filePart.getContentType());
  	      
  	      try {
  	    	  
  	    	  
  	    	  String    fileName = filePart.getSubmittedFileName();
  	    	  String lowFileName = fileName.toLowerCase();
  	    	  String subFileName = "";
  	    	  String[] uploadFormats = SystemProperties.getInstance().getProperty("uploadFormat").split("\\|");
 			
  	    	  for (String string : uploadFormats) {
  	    		  if(lowFileName.endsWith(string)) subFileName = string;
  	    	  }
  	    	  if(subFileName=="") responseMessage="error-format";
        	
  	    	  
  	    	  String uuid = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase().substring(20);
  	    	  
  	    	  if(responseMessage==""){
  	    		  String newName          = nameDateFormat.format(new Date()) + uuid +"."+subFileName ;
  	    		  String uriPath  = "upload/"+ path+"/"+dateSimpleDateFormat.format(new Date())  +"/" +newName   ;
  	    		  File  storeFile = new File(uploadSaveFile ,uriPath);
  	    		  storeFile.getParentFile().mkdirs();
  	    		  filePart.write(storeFile.getAbsolutePath());
             	
  	    		  logger.info("[uploadFile]     uriPath-->"+uriPath);
  	    		  logger.info("[uploadFile] 文件保存路经-->"+storeFile.getAbsolutePath());
             	
  	    		  HashMap<String,String> map =  new HashMap<String,String>();
  	    		
  	    		  map.put("serverURL", serverURL);
  	    		  map.put(  "fileURI", uriPath);
  	    		  map.put( "filePath", storeFile.getAbsolutePath());
  	    		  map.put( "fileSize", String.valueOf(storeFile.length()));
  	    		  map.put( "fileName", filePart.getSubmittedFileName());
  	    		  jsonListFormat.addObject(map);
	 				
  	    	  }   	
  	    	  
  	      } catch (Exception e) {
  	    	  responseMessage = e.getClass().getName();
  	    	  logger.error(e,e);
  	      }
    	}
      
        
    

		
		if(responseMessage=="")responseMessage="success";
		jsonListFormat.setServerMsg(responseMessage);
    	PrintWriter out = res.getWriter();
        out.println(jsonListFormat.toString());
        out.close();
			
			

	}

	
}
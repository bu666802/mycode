package com.web.upload;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.SystemConfig;


@WebServlet(name="uploadAction", urlPatterns={"/uploads/*"}, loadOnStartup=1)
public class UploadAction extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(this.getClass());

	private String savePath = SystemConfig.getInstance().getProperty("uploadSavePath"); 
	
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
    	
    	
    	String uri = req.getRequestURI().substring(1);
    	String mapPath= uri.substring(uri.indexOf("/uploads/")+1, uri.length());
    	File    saveFile = new  File(savePath,mapPath);
    	logger.debug("[uploadAction][URL ]"+req.getRequestURL());
    	logger.debug("[uploadAction][Disk]"+saveFile.getAbsolutePath());   	
    	if(saveFile.exists()){
    		writeFile(res,saveFile);
    	}	
	
    }
    
    void writeFile(  HttpServletResponse res ,File file)throws ServletException, IOException {
        
    	java.util.Date date = new java.util.Date();  
		res.setDateHeader("Last-Modified",date.getTime());
		res.setDateHeader("expires",System.currentTimeMillis()+60*60*24*30);
    	
    	ServletOutputStream sout=res.getOutputStream();
		FileInputStream  in = new FileInputStream(file);  
		byte b[]=new byte[4096];
        while(in.read(b)!=-1){
            sout.write(b);
           
        } 
       
       in.close();
       sout.flush();
       sout.close();
    	
    }
    	
		
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        
    	doPost(req ,resp );
    	
    }
    
    

    
  
    
}











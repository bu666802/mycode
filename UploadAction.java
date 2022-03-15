package com.web.upload;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.SystemProperties;



@WebServlet(urlPatterns={"/upload/*"},loadOnStartup=1)
public class UploadAction extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private File   uploadSaveFile ;   
	
	
	public UploadAction(){
				
		//可将路径放置设定档
		
		String systemFilePath = SystemProperties.getInstance().getProperty("systemFilePath");
		File rootFile  = new File(systemFilePath);
		//File  ftpFile  = new File(rootFile,"ftp");
		uploadSaveFile = rootFile;
   
		
	}
	
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
    	
    	
    	//String uri         = req.getRequestURI();
    	
    	String uri = URLDecoder.decode(req.getRequestURI(), "UTF-8"); 	
    	String uriPath     = uri.substring(uri.indexOf("upload/"), uri.length() );
    	System.out.println(uriPath);
    	File   saveFile    = new  File(uploadSaveFile,uriPath);
    	
    	System.out.println(saveFile.getAbsolutePath());
    	
    	if(saveFile.exists()) {
    		writeFile(res,saveFile);
    	}else {
    		
    		System.out.println(saveFile.getCanonicalPath());
    		System.out.println("文件不存在" );
    	}
    }
    
    
    
    void writeFile(  HttpServletResponse res ,File file)throws ServletException, IOException {
    	
    	
    	if(file.getName().endsWith("xlsx")) {
    	
    		res.setContentType("application/vnd.ms-excel");
    		res.setCharacterEncoding("UTF-8");
    		res.setHeader("Content-Disposition", "attachment; filename="+file.getName());
    		System.out.println("download excel..");
        	ServletOutputStream out=res.getOutputStream();
    		
        	Workbook workbook;
			try {
				workbook = new XSSFWorkbook(file);
				res.setContentType("application/vnd.ms-excel");
	        	//res.setHeader("Content-Disposition", "attachment; filename=YourFilename.xlsx");
	        	ServletOutputStream outputStream = res.getOutputStream();
	        	workbook.write(outputStream);
	            out.flush();
	            out.close();
				
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        	
    		
    		
    	}else {
    		Date date = new Date();  
        	res.setContentType("image/*");
        	res.setDateHeader("Last-Modified",date.getTime());
    		res.setDateHeader("expires",System.currentTimeMillis()+9999999);
    		res.setCharacterEncoding("UTF-8");
    		
    		
    		
        	ServletOutputStream out=res.getOutputStream();
    		FileInputStream  in = new FileInputStream(file);  
    		byte b[]=new byte[4096];
            while(in.read(b)!=-1){
                out.write(b);
            }  
            in.close();
            out.flush();
            out.close();
    		
    	}
    	
    
		
		
		
	
    	
    }
    
  


}











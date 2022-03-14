package com.web.upload;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.CheckUtil;
import com.JSONListFormat;
import com.SystemConfig;
import com.web.WebUtil;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

@WebServlet(name="uploadFile", urlPatterns={"/uploadFile"}, loadOnStartup=1)
public class UploadFile extends HttpServlet {
	
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	
	
	private static final long serialVersionUID = 1L;
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;      // 3MB
	private static final int MAX_FILE_SIZE    = 1024 * 1024 * 40;     // 40MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 1350;   // 150MB
	
	
	
	private SimpleDateFormat simpleDateFormat    = new SimpleDateFormat("HHmm");
	private SimpleDateFormat nowSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public UploadFile(){
		
		System.out.println("== uploadFile start  ===");
	}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
    	
    	res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		String responseMessage = "";
		
		String path = req.getParameter("path");
		
		
		
		if(CheckUtil.isEmpty(path))path= nowSimpleDateFormat.format(new Date());
	
		path = URLDecoder.decode(path,"utf-8");
		path= path.replaceAll("\\.", "");
		
		
		System.out.println("###### start upload ######");
		
		
		
		JSONListFormat jsonListFormat  = WebUtil.createJSONListFormat(req, false);
		
	
		
		if (!ServletFileUpload.isMultipartContent(req)) {
			
			responseMessage="error-noMultipartContent";
		}
		
		
		
		
		if(responseMessage == ""){
			
			
			
			
			
			try {
				
				
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(MEMORY_THRESHOLD);
				factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setFileSizeMax(MAX_FILE_SIZE);
				upload.setSizeMax( MAX_REQUEST_SIZE);
				
				
				List<FileItem> formItems =   upload.parseRequest(req);
				
				
				if (formItems != null && formItems.size() > 0) {
					
					
					for (FileItem item : formItems) {
						String fileName = item.getName();
						
						System.out.println("fileName="+fileName);
						
						if (item.isFormField()) {
							
							System.out.println("普通输入项的数据");
					
						}else{
							
						
							fileName = fileName.toLowerCase();
							String subFileName ="";
							String[] uploadFormats = SystemConfig.getInstance().getProperty("uploadFormat").split("\\|");
							for (String string : uploadFormats) {
								if(fileName.endsWith(string)) subFileName = string;
							}
							if(subFileName=="") responseMessage="error-format";     
						
							
							
							if(responseMessage ==""){
								
								String uploadServerURL = SystemConfig.getInstance().getProperty("uploadServerURL");
								String uploadSavePath  = SystemConfig.getInstance().getProperty("uploadSavePath");
							
								
								String uploadPath      =  "uploads/"+path+"/"+nowSimpleDateFormat.format(new Date());
								File   uploadFoldFile  = new File(uploadSavePath,uploadPath  );
								
								if(!uploadFoldFile.exists())uploadFoldFile.mkdirs();
								
								
								
								String name =  simpleDateFormat.format(new Date()) + UUID.randomUUID().toString().substring(0, 8);
							
								File storeFile = new File(uploadFoldFile ,   name+ "."+subFileName);
								
								item.write(storeFile);
								System.out.println("[save]-->"+ storeFile.getAbsoluteFile());
								
								
//								if("amr".equals(subFileName)){
//									
//									File storeFile2= new File(uploadFoldFile , name +".mp3" );
//									changeToMp3(storeFile,storeFile2 );
//									storeFile = storeFile2;
//									subFileName = "mp3";
//									
//								}
							
								String urlPath =  uploadPath+"/"+ name+ "."+subFileName;
								
							
								HashMap<String,String> map =  new HashMap<String,String>();
								map.put("fileURL", uploadServerURL+urlPath);
								map.put("fileURI", urlPath);
								jsonListFormat.addMap(map);
								
								
							}
							
						
							
						}
					}
					
				}else{
					responseMessage ="error-fileEmpty";
				}
			
				
				
				
			} catch (Exception e) {
				responseMessage = e.getMessage();
				logger.error(e,e);
				
			}
		
			

			
			
		}

		
		if(responseMessage=="")responseMessage="success";
		jsonListFormat.setServerMsg(responseMessage);
    	PrintWriter out = res.getWriter();
        out.println(jsonListFormat.toString());
        out.close();
			
			

	}
    
    public  void changeToMp3( File source,  File target) {  
        
        AudioAttributes audio = new AudioAttributes();  
        Encoder encoder = new Encoder();  
  
        audio.setCodec("libmp3lame");  
        EncodingAttributes attrs = new EncodingAttributes();  
        attrs.setFormat("mp3");  
        attrs.setAudioAttributes(audio);  
  
        try {  
            encoder.encode(source, target, attrs);  
        }catch (InputFormatException e) {  
        	  System.out.println("2:"+e);  
        }catch (EncoderException e) {  
        
        }catch (Exception e) {  
        	throw e;
        } 
    } 
    
    

	
}
package com;


import java.io.File;
import java.util.Properties;
import java.util.regex.Matcher;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SystemConfig {
	
	static SystemConfig instance ;
	
	
	static {
		instance = new SystemConfig() ;
		
	}
	
	
	private Properties dataProperties = null;
	private File         realPathFile = new File("");
	


	private  SystemConfig() {
		

		String fileName = "beans-config-system.xml";
		File file = new File(fileName);
	
		if(file.exists()){
			
				FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(fileName);
				dataProperties     = (Properties)context.getBean("systemConfig");
				context.close();
			 
			 
		}else{
				ClassPathXmlApplicationContext  context = new ClassPathXmlApplicationContext(fileName);
			    dataProperties     = (Properties)context.getBean("systemConfig");
				context.close();
		}
		
	
		
		
	}
	
	public  String  getProperty(String name) {
		return dataProperties.getProperty(name);
	}
	
	public  File getURLFile(String url) {
		
		if(realPathFile==null)return  null;
		if(url.startsWith("/"))url= url.substring(1, url.length());
	
		String path = url.replaceAll(Matcher.quoteReplacement(File.separator), "/");
		
		File urlFile = new File ( realPathFile ,path);
		urlFile.getParentFile().mkdirs();
		return urlFile;
		
		
	}
	
	
	
	
	public static  SystemConfig getInstance(){
		
		return instance;
		
	}



	public File getRealPathFile() {
		return realPathFile;
	}





	public void setRealPathFile(File webPathFile) {
		this.realPathFile = webPathFile;
	}


	public static void main(String[] args) {
		
		
		SystemConfig systemConfig = SystemConfig.getInstance();
		systemConfig.setRealPathFile(new File("WebContent"));
		
		File urlfile = systemConfig.getURLFile("/aa.jpg");
		
		System.out.println(     urlfile.getAbsolutePath());
		System.out.println(systemConfig.getRealPathFile());
		
		
	}
	
	





}

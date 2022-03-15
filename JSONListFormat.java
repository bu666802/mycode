package com;



import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONListFormat {
	
	
	
	
	
	private static Map<String,Properties>  languagesData = new HashMap<String,Properties> ();
	private Logger logger = Logger.getLogger(this.getClass());
	private String languageName = "NameZh-CN";
	

	private String showMsg	  =  "";
	private String serverCode =  "";
	private String serverMsg  =  "";
	
	private int    dataTotal  =    0;
	private int    startIndex =   0;
	
	
	private int  currentIndex =     0;
	private int      endIndex = 10000;
	
	private boolean isUseMemoryPage = false;
	
	private int offset = 10;
	
	private long createTime = System.currentTimeMillis();
	
	
	public int findPageNum( ){
		int pageNum     =   (startIndex/ offset)+1 ;
		return pageNum;
	}
	
	public int findPageSize( ){
		return offset;
	}
	
	public void setUseMemoryPage(boolean isUseMemoryPage) {
		this.isUseMemoryPage = isUseMemoryPage;
	}


	public void setDataTotal(long dataCount) {
		if(dataCount<0)	return;
		
		this.dataTotal = (int)dataCount;
	}
	public int getDataTotal() {
		return dataTotal;
	}

	private List<Object>   data = new LinkedList<Object>();

	public List<Object> getData() {
		return data;
	}

	public String getLanguageName() {
		return languageName;
	}
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
	
	
	public String getShowMsg() {
		return showMsg;
	}
	public void setShowMsg(String showMsg) {
		this.showMsg = showMsg;
	}

	public String getServerCode() {
		return serverCode;
	}
	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}
	
	public String getServerMsg() {
		return serverMsg;
	}
	public void setServerMsg(String str) {
		this.serverMsg = str;
	}

	public static void putLanguage(String  languageFileName , Properties p ) {
	 	 
		languagesData.put(languageFileName, p);
    	
	}
	
	public void setLimit(int start, int offset){
		startIndex = start;
		  endIndex = start  + offset;
		  this.offset = offset;
	}
	

	
	
	
	public void addObject(Object jObject) throws Exception {
		if(!isUseMemoryPage){
			data.add(jObject);
			currentIndex++;
			return;
		}
		
		if( startIndex <= currentIndex & currentIndex < endIndex ){
			data.add(jObject);
		}
		currentIndex++;
	}
		
	public void addObject(List<?> list) throws Exception {
		if(!isUseMemoryPage){
			for (int i = 0; i < list.size(); i++) {
				data.add(list.get(i));
				currentIndex++;
			}
			return;
		}
		
		for (int i = 0; i < list.size(); i++) {
			if( startIndex <= currentIndex & currentIndex < endIndex ){
				data.add(list.get(i));
				currentIndex++;
			}else{
				return;
			}
		}
		
	}


	//[2017/11/24]
	static{
		
		try {
			loadLanguage("NameZh-CN","language-cn.properties");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
			
	//[2017/11/24]
	public static   void loadLanguage(String langName , String resourcePath  ) throws Exception {
		
		InputStream inputStream = JSONListFormat.class.getClassLoader().getResourceAsStream(resourcePath);
	   	InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
	   	Properties languagesProperties = new Properties();
		languagesProperties.load(inputStreamReader); 
	   	inputStream.close();
	   	inputStreamReader.close();
		languagesData.put(langName, languagesProperties);
	}	
	
	

	public double getRunTime() {
		
		return (System.currentTimeMillis()-createTime)/1000.0;
	}

	
	@Override
	public String toString(){
		
		
	 
		try {
			
			
			if(serverMsg=="") serverMsg= "success";
	 		if(showMsg==""){
	 			if(languageName==null)languageName="";
	 			Properties languages = languagesData.get(languageName);
	 			if (languages!=null)showMsg   = languages.getProperty(serverMsg);
	 			if(   showMsg==null| showMsg==""){
	 				showMsg = "【"+serverMsg+"】";
	 				logger.error("[JSONListFormat]错误码提示不存在："+showMsg);
	 			}
	 				
	 		}
	 		
	 		

			if(dataTotal==0) dataTotal = currentIndex;
		
	
	 		
			JSONObject jsonObject = new JSONObject(this);
		
			
		  	return jsonObject.toString(4);
		  	
		  	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		  	
		return"";
	}

	

	public static void main(String[] args) throws Exception {
		
		HashMap<String,String> map1 = new HashMap<String,String>();
		map1.put("id"     ,  "1");
		map1.put("name"   ,  "will");
		map1.put("phone"  , "000000000");
		
		
		JSONListFormat format = new JSONListFormat();
		format.setServerCode("userList");
		format.addObject(map1);
		format.addObject(map1);
		
		System.out.println(format.toString());
	}
	
	
	
}

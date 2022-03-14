package com.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.CheckUtil;
import com.IPUtil;
import com.Manager;
import com.db.SQLClient;
import com.web.a02.db.LogDB;
import com.web.a02.db.ManagerDB;

@WebFilter(filterName="ServletFilterSession",urlPatterns="/*")
public class ServletFilterSession  implements Filter {
	
	
	private Logger logger = Logger.getLogger(ServletFilterSession.class);
	
	@Override
    public void destroy() {
        System.out.println("ServletFilterSession过滤器销毁");
    }
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
       
        
        HttpServletRequest req = (HttpServletRequest) request;
        //String urlstr = req.getRequestURI();
        String serverCode = req.getParameter("serverCode");
       
        
        if(serverCode!=null) {
        	
        	logger.info("Servlet Filter Session Check:"+serverCode);
        	
        	HttpSession session = req.getSession();
        	Manager manager = (Manager)session.getAttribute("manager");
        	if(manager==null) {
        		try {
        			Manager manager0 = (Manager)WebSession.getAttribute(req, "manager");
        			if(manager0!=null) {
        				session.setAttribute("manager", manager0);
        				logger.info("Servlet Filter Session Check:reload");
        			}else {
        				logger.info("Servlet Filter Session Check:no session");
        			}
        		} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        	else {
        		logger.info("Servlet Filter Session Check:success");
        	}
        	
        	
        }
        
        
        chain.doFilter(request, response);
        
    }
 
    @Override
    public void init(FilterConfig config) throws ServletException {
        System.out.println("过滤器初始化");

    }
}

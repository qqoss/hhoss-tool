/**
 * Created on 2008-5-15 
 */
package com.hhoss.web.filters;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hhoss.Logger;
import com.hhoss.boot.App;

/**
 * servlet filter encoding
 * @author kejun.zheng
 * @date 2008-5-15
 * @version 1.0
 */
public class EncodingFilter implements Filter {
	private static final Logger logger = Logger.get();
	private String encoding;
	private boolean ignore;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,ServletException {
		tryApplyEncoding(request);
		chain.doFilter(request, response);
	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.encoding = filterConfig.getInitParameter("encoding");
		String value = filterConfig.getInitParameter("ignore");
		if ("ture".equalsIgnoreCase(value)||"yes".equalsIgnoreCase(value)||"1".equals(value)){
			ignore = true;
		}
	}

	@Override
	public void destroy() {
		encoding = null;
		ignore = false;
	}
	
    private void tryApplyEncoding(ServletRequest request) {
        try {
        	String enc = getEncoding(request);
            if (ignore||enc==null||enc.equals(request.getCharacterEncoding())) {
            	return;
                // if the encoding is already correctly set and the parameters have been already read
                // do not try to set encoding because it is useless and will cause an error
            }
            request.setCharacterEncoding(enc);
        } catch (Exception e) {
        	logger.warn("Error setting character encoding to '{}' - ignoring.", encoding, e);
        }
    }

	private String getEncoding(ServletRequest request) {
		String enc = request.getCharacterEncoding();
		if(isWrong(enc)){
			enc=request.getParameter("encoding");
		}
		if(isWrong(enc)){
			enc=(String)request.getAttribute("env.request.encoding");
		}
		if(isWrong(enc) && request instanceof HttpServletRequest){
			HttpSession hts = ((HttpServletRequest)request).getSession(false);
			if(hts!=null){
			   enc = (String)hts.getAttribute("env.request.encoding");
			}
		}
		if(isWrong(enc)){
			enc = App.getRootEnv("env.request.encoding");
		}
		
		if(isWrong(enc)){
			enc = this.encoding;
		}
		
		if(isWrong(enc)){
			enc = App.getRootEnv("file.encoding");
		}
		
		if(isWrong(enc)){
			enc = "UTF-8";
		}
		
		return enc;
		
	}
	
	private boolean isWrong(String enc){
		if( enc==null ){
			return true;
		}
		//TODO encoding or charset? 
		return !Charset.isSupported(enc);
	}

	/*
	 * 
	 * 
	 * <filter> <filter-name>encodingFilter</filter-name>
	 * <filter-class>com.hoss.core.web.filter.EncodingFilter</filter-class>
	 * <init-param> <param-name>encoding</param-name> <param-value>UTF-8</param-value>
	 * </init-param> <init-param> <param-name>ignore</param-name>
	 * <param-value>false</param-value> </init-param> </filter>
	 * <filter-mapping> <filter-name>encodingFilter</filter-name>
	 * <url-pattern>/*</url-pattern> </filter-mapping>
	 * 
	 */
}

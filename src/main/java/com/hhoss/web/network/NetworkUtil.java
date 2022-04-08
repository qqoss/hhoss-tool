package com.hhoss.web.network;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

/**
 * 常用获取客户端信息的工具
 * 
 */
public final class NetworkUtil {

	/**
	 * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	
	public static String getRemoteIP(HttpServletRequest request) {
		String[] heads = {"X-Forwarded-For","X-Real-IP","Proxy-Client-IP","WL-Proxy-Client-IP","HTTP_CLIENT_IP","HTTP_X_FORWARDED_FOR"};
		String ip=null;
		for(String head: heads){
			ip = normalize(request.getHeader(head));
			if( ip!=null ){
				return ip;
			}
		}
		ip = request.getRemoteAddr();
		return valid(ip)?ip:null;
		//return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}
	
	private static String normalize(String val){
		if(val == null || val.trim().isEmpty()){
			return null;
		}
		String ip=val.trim();
		if(ip.indexOf(',')>0) {
			for(String s:val.split(",")){
				if(valid(s)) {
					ip=s;
					break;
				}
			}
			ip=null;
		}
		
		return valid(ip)?ip:null;
	}
	
	/** judge a ip string is a Internet ip;
	 * @param ip
	 * @return true if ip is a valid Internet ip
	 */
	private static boolean valid(String ip){
		if(ip == null || ip.trim().isEmpty() || "unknown".equalsIgnoreCase(ip)){
			return false;
		}
		if(ip.startsWith("0.")||ip.startsWith("127.")||ip.startsWith("10.")||ip.startsWith("192.168.")||ip.startsWith("169.254.")){
			return false;
		}
		if( ip.startsWith("172.")){//172.16.x.x至172.31.x.x
			int idx = ip.indexOf('.',4);
			if(idx<=4){return false;}
			
			String p2 = ip.substring(4,idx);
			int num = Integer.parseInt(p2);
			if(num>=16&&num<32){return false;}			
		}
		return true;
	}

}
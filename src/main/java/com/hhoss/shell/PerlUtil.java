package com.hhoss.shell;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.hhoss.Logger;

/**
 * perl env prepare
 * @author kejun
 *
 */
public class PerlUtil {
	private Properties env ;
	private static final Logger logger = Logger.get();
	
	public PerlUtil(Properties env){
		this.env=env;
	}
	
	public String run(List<String> params){
		String threadKey = System.currentTimeMillis()+"."+env.getProperty("runtime.task.instance");
		params.add(0,env.getProperty("working.perl.execute"));
		params.add(1,env.getProperty("working.perl.scripts"));
		logger.debug(threadKey+" cmd params: "+params);
		ProcessBuilder pb = new ProcessBuilder(params);
		pb.directory(new File(env.getProperty("working.path.context")));
		logger.debug(threadKey+" cmd context: "+pb.directory());
		StringBuffer sb = new StringBuffer();
		try {
			int ch;
			InputStream is = pb.start().getInputStream();
			while((ch=is.read())>0){sb.append((char)ch);}
			is.close();
		} catch (IOException e) {
			logger.error(threadKey+" cmd exception: ", e);
		}
		String result = sb.toString();
		logger.debug(threadKey+" perl return: "+result);
		return result;
	}

}

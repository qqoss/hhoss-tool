package com.hhoss.spring;

import java.util.ArrayList;
import java.util.Properties;

import com.hhoss.util.token.TokenProvider;

/**
 * @author kejun
 * holder all properties when spring loading, for verify and test. 
 * DON'T using in business codes! *
 */
public final class SpringProvider extends TokenProvider {
	static ArrayList<Properties> holders = new ArrayList<>();
	static void append(Properties holder ){
		holders.add(holder);
	}

	private static final long serialVersionUID = 2466820225432321258L;
	@Override public String getName(){return PREFIX+"spring";}
    @Override public String get(final String key) {
    	for(Properties holder: holders){
    		String s = holder.getProperty(key);
    		if(s!=null){return s;}
    	}
    	return null;
    }
}

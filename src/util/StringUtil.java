package util;

import java.util.List;

public class StringUtil {
	
	public static String join(List<String> list, String separator) {
		StringBuilder buff = new StringBuilder();
		for(String str : list) {
			if (buff.length() > 0)
				buff.append(separator);
			buff.append(str);
		}		
		return buff.toString();		
	}
	
}

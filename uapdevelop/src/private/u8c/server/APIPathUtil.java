package u8c.server;

import u8c.bs.utils.PubAppTool;

public class APIPathUtil {

	  /**
	   * @param String
	   * @return
	   */
	  public static String getPath(String pathInfo) {
	    String[] retStrs = pathInfo.split("/");
	    StringBuilder sb = new StringBuilder();
	    for (String str : retStrs) {
	      if (!PubAppTool.isNull(str)) {
	        sb.append(str);
	        sb.append(".");
	      }

	    }
	    pathInfo = sb.deleteCharAt(sb.lastIndexOf(".")).toString();
	    return pathInfo;
	  }
}

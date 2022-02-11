package u8c.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.logging.Logger;

/**
 * @功能描述：JSON对象servlet外部入口
 * @author: xuxq3
 * @version: 2018-1-23 下午3:26:00
 */
public class APIServletForJSON implements IHttpServletAdaptor {

  @Override
  public void doAction(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String path = u8c.server.APIPathUtil.getPath(request.getPathInfo());
    String rettext = null;
    if(path.startsWith("file.")){
    	rettext = new FileManageController().forWard(request,response);
    } else if (path.startsWith("dev.define")) {
    	rettext = new APICustmerDevelopContrller().forWard(request);
	} else {
    	rettext = new APIController().forWard(request);
    }
    // 返回调用结果
    this.responseOutWithJson(response, rettext);
  }

  private void responseOutWithJson(HttpServletResponse response,
      String responseStr) {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=utf-8");
    PrintWriter out = null;
    try {
      out = response.getWriter();
      out.append(responseStr);
      Logger.debug("返回是\n");
      Logger.debug(responseStr);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      if (out != null) {
        out.close();
      }
    }
  }

}

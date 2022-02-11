//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package u8c.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.InstException;
import nc.bs.framework.mx.thread.ThreadTracer;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.logging.Logger;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.sm.login.NCEnv;
import u8c.bs.APIConst;
import u8c.bs.exception.ConfigException;
import u8c.bs.exception.SecurityException;
import u8c.bs.message.MessageTools;
import u8c.pubitf.action.IAPICustmerDevelop;
import u8c.pubitf.config.IAPIConfigFileService;
import u8c.vo.entity.InputDataVO;
import u8c.vo.pub.APIMessageVO;

public class APICustmerDevelopContrller extends APIController {
    public APICustmerDevelopContrller() {
    }

    public String forWard(HttpServletRequest request) {
        long requestTime = System.currentTimeMillis();
        APIMessageVO messageVO = new APIMessageVO();
        String rettext = null;
        String errorMessage = null;
        InputDataVO inputData = this.parseInputParameter(request);
        UFBoolean needMiddleFile = null;

        try {
            needMiddleFile = ((IAPIConfigFileService)NCLocator.getInstance().lookup(IAPIConfigFileService.class)).getGlobalParameter().getNeedMiddleFile();
        } catch (ConfigException var24) {
            return MessageTools.getFailureMessage(messageVO, var24, UFBoolean.valueOf(inputData.getNeedStackTrace()));
        }

        if (NCEnv.isDebug()) {
            InvocationInfoProxy.getInstance().setUserDataSource("design");
        } else {
            InvocationInfoProxy.getInstance().setUserDataSource("U8cloud");
        }

        String custdevpclass = null;

        try {
            String errorMsg = this.checkUser(inputData);
            if (errorMsg != null) {
                throw new SecurityException("安全验证出错：" + errorMsg, "ERROR-APP-USER");
            }

            custdevpclass = request.getHeader("custdevpclass");
            if (custdevpclass == null || custdevpclass.length() == 0) {
                throw new SecurityException("客户化API接口调用出错：没有指定API接口的实现类.");
            }

            Class cdclass = null;

            try {
                cdclass = NewObjectService.newInstance(custdevpclass).getClass();
            } catch (InstException var23) {
                throw new SecurityException("客户化API接口调用出错：指定的custdevpclass实现类" + custdevpclass + "不存在.");
            }

            if (!(cdclass.newInstance() instanceof IAPICustmerDevelop)) {
                throw new SecurityException("客户化API接口调用出错：指定的custdevpclass实现类没有实现系统预制的接口类IAPICustmerDevelop.");
            }

            Method method = cdclass.getMethod("doAction", HttpServletRequest.class);
            if (method == null) {
                throw new SecurityException("客户化API接口调用出错：指定的custdevpclass实现类没有实现系统预制的接口类IAPICustmerDevelop.");
            }

            String retStr;
            retStr = (String)method.invoke(cdclass.newInstance(), request);
            String encryptData = this.encrypt(inputData.isEncrypt(), retStr);
            //rettext = MessageTools.getSuccessMessage(messageVO, encryptData);
            rettext = encryptData;
        } catch (SecurityException var25) {
            errorMessage = MessageTools.getFailureMessage(messageVO, var25, UFBoolean.FALSE);
        } catch (Exception var26) {
            errorMessage = MessageTools.getFailureMessage(messageVO, var26, UFBoolean.valueOf(inputData.getNeedStackTrace()));
        } finally {
            Logger.debug("After Invoke: " + request.getPathInfo() + " " + (System.currentTimeMillis() - requestTime));
            ThreadTracer.getInstance().endThreadMonitor();
        }

        if (errorMessage != null) {
            rettext = errorMessage;
        }

        return rettext;
    }

    private String getRequestPostStr(InputStream tInputStream) throws IOException {
        String retStr = null;
        if (tInputStream != null) {
            BufferedReader br = null;
            InputStreamReader isr = null;

            try {
                isr = new InputStreamReader(tInputStream, "UTF-8");
                br = new BufferedReader(isr);
                StringBuffer tStringBuffer = new StringBuffer();
                new String("");

                String sTempOneLine;
                while((sTempOneLine = br.readLine()) != null) {
                    tStringBuffer.append(sTempOneLine);
                }

                retStr = tStringBuffer.toString();
            } finally {
                if (br != null) {
                    br.close();
                }

                if (isr != null) {
                    isr.close();
                }

            }
        }

        return retStr;
    }
}

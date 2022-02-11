package u8c.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.mx.thread.ThreadTracer;
import nc.bs.logging.Logger;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.vo.pub.BusinessException;
import nc.vo.pub.filesystem.NCFileNode;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.sm.login.NCEnv;
import u8c.bs.APIConst;
import u8c.bs.auth.AuthQueryer;
import u8c.bs.exception.SecurityException;
import u8c.bs.message.MessageTools;
import u8c.vo.entity.InputDataVO;
import u8c.vo.entity.enumtype.IAppErrCode;
import u8c.vo.pub.APIMessageVO;

public class FileManageController extends APIController {

	protected InputDataVO parseInputParameter(HttpServletRequest request) {
		InputDataVO inputData = super.parseInputParameter(request);
		inputData
				.setAttributeValue(
						"opertype",
						APIPathUtil.getPath(request.getPathInfo()).contains(
								".upload") ? "upload" : "download");
		inputData.setAttributeValue("parentpath",
				request.getHeader("parentpath"));
		inputData.setAttributeValue("filename", request.getHeader("filename"));
		inputData.setAttributeValue("creator", request.getHeader("creator"));
		inputData.setAttributeValue("filelength",
				request.getHeader("filelength"));
		inputData.setUrl(request.getRemoteAddr() + ":"
				+ request.getRemotePort());
		inputData.setAttributeValue("path", request.getHeader("path"));
		return inputData;
	}

	public String forWard(HttpServletRequest request,
			HttpServletResponse response) {
		long requestTime = System.currentTimeMillis();
		APIMessageVO messageVO = new APIMessageVO();
		String rettext = null;
		String errorMessage = null;
		// ��ʼ������
		InputDataVO inputData = this.parseInputParameter(request);
		// ��������Դ
		if (NCEnv.isDebug()) {
			InvocationInfoProxy.getInstance()
					.setUserDataSource(APIConst.DESIGN);
		} else {
			InvocationInfoProxy.getInstance().setUserDataSource(
					APIConst.DEFALUTDB);
		}
		try {
			// У���û�����
			if (!NCEnv.isDebug()) {
				String errorMsg = checkUser(inputData);
				if (errorMsg != null) {
					throw new SecurityException("��ȫ��֤����" + errorMsg,
							IAppErrCode.FAILURE_USER);
				}
			}
			// ������Ȩ�ܿ��ڿ��Žӿ�ƽ̨
			if (!AuthQueryer.haveAuth("1250")) {
				throw new SecurityException(
						"û�й���U8 cloud���Žӿ�ƽ̨��Ȩ������ϵU8Cʵʩ��Ա��������Ա����U8cloud���Žӿ�ƽ̨��1250��ģ����Ȩ��",
						IAppErrCode.FAILURE_NOAUTH);
			}
			this.doAction(inputData,request, response);
			// �����ķ���
			rettext = MessageTools.getSuccessMessage(messageVO, null);
		} catch (SecurityException e) {
			errorMessage = MessageTools.getFailureMessage(messageVO, e,
					UFBoolean.FALSE);
		} catch (Exception e) {
			errorMessage = MessageTools.getFailureMessage(messageVO, e,
					UFBoolean.valueOf(inputData.getNeedStackTrace()));
		}

		finally {
			Logger.debug("After Invoke: " + request.getPathInfo() + " "
					+ (System.currentTimeMillis() - requestTime));
			ThreadTracer.getInstance().endThreadMonitor();
		}
		UFBoolean issuccess = UFBoolean.TRUE;
		if (null != errorMessage) {
			rettext = errorMessage;
			issuccess = UFBoolean.FALSE;
		}
		return rettext;
	}

	private void doAction(InputDataVO inputData ,HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,BusinessException, ClassNotFoundException {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(request.getInputStream());
			String oper = (String) inputData.getAttributeValue("opertype");
			if ("upload".equals(oper)) {
				doUploadFile(inputData, in, response);
			} else if ("download".equals(oper)) {
				doDownLoadFile(inputData, response);
			}
		} catch (BusinessException e) {
			throw new BusinessException(e.getCause());
		} finally {
			if (in != null) {
				in.close();
			}
		}

	}

	private void doDownLoadFile(InputDataVO inputData,
			HttpServletResponse response) {
		String path = (String) inputData.getAttributeValue("path");
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			IFileSystemService service = NCLocator.getInstance().lookup(
					IFileSystemService.class);
			service.downLoadFile(path, out);
			out.flush();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void doUploadFile(InputDataVO inputData,
			ObjectInputStream in, HttpServletResponse response) throws IOException, BusinessException {
		String parentPath = (String) inputData.getAttributeValue("parentpath");
		String fileName = (String) inputData.getAttributeValue("filename");
		String creator = (String) inputData.getAttributeValue("creator");
		String fileLengthStr = (String) inputData.getAttributeValue("filelength");
		long fileLen = -1;
		if (fileLengthStr != null) {
			fileLen = Long.parseLong(fileLengthStr.trim());
		}
		ObjectOutputStream oos = null;
		IFileSystemService service = NCLocator.getInstance().lookup(
				IFileSystemService.class);
		try {
			oos = new ObjectOutputStream(response.getOutputStream());
			NCFileNode node = service.createNewFileNodeWithStream(parentPath,
					fileName, creator, in, fileLen);
			oos.writeObject(node);
			oos.flush();
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}

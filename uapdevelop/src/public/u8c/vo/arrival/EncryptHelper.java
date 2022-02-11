package u8c.vo.arrival;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import u8c.bs.exception.SecurityException;
import u8c.bs.utils.EncryptUtil;
import com.yonyou.yht.sdk.SDKUtils;
public class EncryptHelper {
	public String decrypt(String obj) throws SecurityException {
	    String data = null;
	    try {
	        data =EncryptUtil.decryptForAESECB256(obj,SDKUtils.encodeUsingMD5("u8cloudapi"));	      
	    }
	    catch (InvalidKeyException | NoSuchAlgorithmException
	        | NoSuchPaddingException | IllegalBlockSizeException
	        | BadPaddingException | NoSuchProviderException
	        | UnsupportedEncodingException e) {
	      throw new SecurityException("无法解析来源数据,如果您不是在API Link平台购买的API,请购买后再使用！",
	          e.getCause());
	    }
	    return data;
	  }
	public String encrypt(String retStr)
		      throws SecurityException {
		    if (null == retStr) {
		      return retStr;
		    }
		    String rettext = null;
		    try {		     
		        rettext =EncryptUtil.encryptForAESEBC256(retStr,SDKUtils.encodeUsingMD5("u8cloudapi"));		     
		    }
		    catch (InvalidKeyException | NoSuchAlgorithmException
		        | NoSuchPaddingException | IllegalBlockSizeException
		        | BadPaddingException | NoSuchProviderException
		        | UnsupportedEncodingException e) {
		      throw new SecurityException(e.getMessage(), e.getCause());
		    }
		    return rettext;
		  }
}

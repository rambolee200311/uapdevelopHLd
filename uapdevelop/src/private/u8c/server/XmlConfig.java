package u8c.server;
import nc.fi.arap.pubutil.RuntimeEnv;
import java.io.File;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
public class XmlConfig {
	private static String fileName=RuntimeEnv.getNCHome()
			+ File.separator+"resources"
			+ File.separator+"busiitf"
			+File.separator+"config.xml";
	
	public static String getUrl(String id){
		String strResult="";
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(new File(fileName));
			//Element rootElement = document.getRootElement();
			Element e=(Element)document.selectSingleNode("//datas/data[@id='"+id+"']");
			if(e!=null){				
					strResult=e.element("url").getText();
				
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strResult;
	}
}

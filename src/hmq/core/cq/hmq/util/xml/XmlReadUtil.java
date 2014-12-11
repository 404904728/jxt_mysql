package core.cq.hmq.util.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.ResourceUtils;

/**
 * xml读取工具类 待完善
 * 
 * @author 何建
 */
public class XmlReadUtil {

	@SuppressWarnings("unchecked")
	public static List<String> read(String xmlName) {
		List<String> listSql = new ArrayList<String>();
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(ResourceUtils.getFile("classpath:"
					+ xmlName));
			Element root = document.getRootElement();
			for (Iterator it = root.elementIterator("pojo"); it.hasNext();) {
				Element pojo = (Element) it.next();
				String tableName = pojo.attributeValue("name") + "_t";
				for (Iterator itSql = pojo.elementIterator("sql"); itSql
						.hasNext();) {
					Element sql = (Element) itSql.next();
					String[] strSql = sql.getStringValue().split("/");
					listSql.add(tableName + "   (" + strSql[0] + ")   "
							+ "values   (" + strSql[1] + ")");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listSql;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(XmlReadUtil.read("initdata_update.xml").size());
	}

}

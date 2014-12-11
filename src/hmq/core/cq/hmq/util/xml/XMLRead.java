package core.cq.hmq.util.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.ResourceUtils;

import core.cq.hmq.modal.FlowModal;


public class XMLRead {
	@SuppressWarnings("unchecked")
	public static List<FlowModal> read(String xmlPath) {
		List<FlowModal> listModal = new ArrayList<FlowModal>();
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(ResourceUtils.getFile("classpath:"
					+ xmlPath));
			Element root = document.getRootElement();
			for (Iterator it = root.elementIterator("start"); it.hasNext();) {
				Element statr = (Element) it.next();
				FlowModal fwStart = new FlowModal();
				fwStart.setId(statr.attributeValue("id"));
				fwStart.setName(statr.attributeValue("name"));
				String[] point = statr.attributeValue("g").split(",");
				fwStart.setX(Integer.parseInt(point[0]));
				fwStart.setY(Integer.parseInt(point[1]));
				fwStart.setType("start");
				fwStart.setDecision(false);
				for (Iterator itSql = statr.elementIterator("transition"); itSql
						.hasNext();) {
					Element transition = (Element) itSql.next();
					fwStart.setToName(transition.attributeValue("to"));
				}
				listModal.add(fwStart);
			}
			listModal.addAll(findElement(root, false));
			listModal.addAll(findElement(root, true));
			Element endo = root.element("end");
			System.out.println(endo.attributeValue("name") + "__________");
			for (Iterator it = root.elementIterator("end"); it.hasNext();) {
				Element end = (Element) it.next();
				FlowModal fwEnd = new FlowModal();
				fwEnd.setId(end.attributeValue("id"));
				fwEnd.setName(end.attributeValue("name"));
				String[] point = end.attributeValue("g").split(",");
				fwEnd.setX(Integer.parseInt(point[0]));
				fwEnd.setY(Integer.parseInt(point[1]));
				fwEnd.setType("end");
				fwEnd.setDecision(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listModal;
	}

	/**
	 * 根据toName找到节点
	 * 
	 * @param el
	 * @param toName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<FlowModal> findElement(Element el, boolean decision) {
		List<FlowModal> listModal = new ArrayList<FlowModal>();
		for (Iterator it = el.elementIterator(decision ? "decision" : "task"); it
				.hasNext();) {
			FlowModal fwStart = new FlowModal();
			Element task = (Element) it.next();
			fwStart.setId(task.attributeValue("id"));
			fwStart.setName(task.attributeValue("name"));
			String[] point = task.attributeValue("g").split(",");
			fwStart.setX(Integer.parseInt(point[0]));
			fwStart.setY(Integer.parseInt(point[1]));
			fwStart.setDecision(decision);
			String line = "";
			for (Iterator itSql = task.elementIterator("transition"); itSql
					.hasNext();) {
				Element transition = (Element) itSql.next();
				line += transition.attributeValue("to") + ",";
			}
			fwStart.setToName(line);
			listModal.add(fwStart);
		}
		return listModal;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XMLRead.read("contractApp.xml");
	}
}

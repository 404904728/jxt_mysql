package common.cq.hmq.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.dom4j.Document;

public class GrabWebpage {

	public static final String host = "http://www.cdqzsy.com";

	public static final String newslist = "/index.aspx?menuid=4&type=article&lanmuid=6&language=cn";

	private static final String introduction = "/index.aspx?menuid=3&type=article&lanmuid=1&language=cn";

	private String loadPageFromUrl(String urlP) throws HttpException,
			IOException {
		String url = host + urlP;
		HttpClient httpClient = new HttpClient();
		httpClient.getHostConfiguration().setHost(url, 80, "http");

		GetMethod get = new GetMethod(url);
		get.releaseConnection();
		HttpMethod method = get;
		httpClient.executeMethod(method);
		String response = method.getResponseBodyAsString();
		// String response = new
		// String(method.getResponseBodyAsString().getBytes("ISO-8859-1"));
		return response;
	}

	public static List<Map<String, String>> loadIntroduction()
			throws HttpException, IOException {
		String response = new GrabWebpage().loadPageFromUrl(introduction)
				.replaceAll(" ", "");
		String[] contents = response
				.substring(response.indexOf("<divclass=\"uc_lanmu_content\">"),
						response.indexOf("<divclass=\"uc_lanmu_page\">"))
				.trim().split("</ul></div>");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (String content : contents) {
			String href = content.substring(content.indexOf("<ahref=\"") + 8,
					content.indexOf("\"target=\"_self\">"));
			String title = content.substring(
					content.indexOf("\"target=\"_self\">") + 16,
					content.indexOf("</a></li>"));
			String date = content.substring(
					content.indexOf("style=\"display:none\">") + 21,
					content.lastIndexOf("</li>"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("date", date);
			map.put("title", title);
			map.put("href", href);
			System.out.println(title);
			list.add(map);
		}
		return list;
	}

	public static List<Map<String, String>> loadNews() throws HttpException,
			IOException {
		String response = new GrabWebpage().loadPageFromUrl(newslist)
				.replaceAll(" ", "");
		String[] contents = response
				.substring(
						response.indexOf("<divclass=\"uc_lanmu_article_style_1\">"),
						response.indexOf("<divclass=\"uc_lanmu_page\">"))
				.trim().split("</ul></div>");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (String content : contents) {
			String href = content.substring(content.indexOf("<ahref=\"") + 8,
					content.indexOf("\"target=\"_self\">"));
			String title = content.substring(
					content.indexOf("\"target=\"_self\">") + 16,
					content.indexOf("</a></li>"));
			String date = content.substring(
					content.indexOf("style=\"display:inline\">") + 23,
					content.lastIndexOf("</li>"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("date", date);
			map.put("title", title);
			map.put("href", href);
			System.out.println(title);
			list.add(map);
		}
		return list;
	}

	public static String loadPage(String url) throws HttpException, IOException {
		String response = new GrabWebpage().loadPageFromUrl(url);
		String content = response
				.substring(
						response.indexOf("id=\"infor_content\"") + 19,
						response.indexOf("<script type=\"text/javascript\" src=\"/incs/internal_page.js\"></script>"))
				.replace("background-color: rgb(255, 255, 255);", "").replace("background-color: rgb(246, 250, 253);","")
				.replace("color: rgb(66, 66, 66);", "").replace("<img src=\"", "<img src=\"http://www.cdqzsy.com/");
		System.out.println(content);
		return content;
	}

	public static void main(String[] args) throws HttpException, IOException {
		loadPage("/index.aspx?menuid=4&type=articleinfo&lanmuid=6&infoid=1909&language=cn");
	}
}

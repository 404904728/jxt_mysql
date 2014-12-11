package common.cq.hmq.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestUploadAttach {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		uploadHttpURLConnection(null, null, "D:/Documents/Pictures/image-4.jpg");
	}

	
	private static final String BOUNDARY = "---------------------------7db1c523809b2";//数据分割线

	public static boolean uploadHttpURLConnection(String username, String password, String path) throws Exception {
		//找到sdcard上的文件
		File file = new File(path);
                  //仿Http协议发送数据方式进行拼接
		StringBuilder sb = new StringBuilder();
		sb.append("--" + BOUNDARY + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"username\"" + "\r\n");
		sb.append("\r\n");
		sb.append(username + "\r\n");

		sb.append("--" + BOUNDARY + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"password\"" + "\r\n");
		sb.append("\r\n");
		sb.append(password + "\r\n");

		sb.append("--" + BOUNDARY + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + path + "\"" + "\r\n");
		sb.append("Content-Type: image/pjpeg" + "\r\n");
		sb.append("\r\n");

		byte[] before = sb.toString().getBytes("UTF-8");
		byte[] after = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");

		URL url = new URL("http://192.168.1.162:9999/jxt/uploadify/upload.htm");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		conn.setRequestProperty("Content-Length", String.valueOf(before.length + file.length() + after.length));
		conn.setRequestProperty("HOST", "192.168.1.162:9999");
		conn.setDoOutput(true);	
		conn.setChunkedStreamingMode(Integer.parseInt(String.valueOf(file.length())));   
		
		//OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(),"utf-8");
		OutputStream out = conn.getOutputStream();
		InputStream in = new FileInputStream(file);
		out.write(before);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) != -1)
			out.write(buf, 0, len);

		out.write(after);

		in.close();
		out.close();
		
		
		ByteArrayOutputStream bo = new ByteArrayOutputStream();  
		InputStream is = conn.getInputStream();  
		byte[] inbuf = new byte[(1024/10)];  
		while ((len = is.read(inbuf, 0, 1024/10)) > 0) {  
		                bo.write(inbuf);  
		}  
		is.close();  
		String ret = new String(bo.toByteArray(),"UTF-8") ;  
		bo.close(); 
		System.out.println(ret);
		
		/*int contentLength = conn.getContentLength();
		System.out.println("httputil,contentLength:"+contentLength);
		
		if (contentLength > 0) {
			System.out.println(new String(readDataFromLength(conn, contentLength)));
		}
		*/
		
		/*System.out.println(conn.getResponseCode());
		System.out.println(conn.getResponseMessage());
		System.out.println(conn.getHeaderField("Server"));
		Map<String, List<String>> map = conn.getRequestProperties();
		Set<String> set =  map.keySet();
		for(String list : set){
			System.out.println(list);
			System.out.println(map.get(list));
		}*/
		
		return conn.getResponseCode() == 200;
	}
	
	private static byte[] readDataFromLength(HttpURLConnection huc,int contentLength) throws Exception {
		InputStream in = huc.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(in);
		byte[] receData = new byte[contentLength];
		int readLength = 0;
		int offset = 0;
		readLength = bis.read(receData, offset, contentLength);
		int readAlreadyLength = readLength;
		while (readAlreadyLength < contentLength) {
			readLength = bis.read(receData, readAlreadyLength, contentLength-readAlreadyLength);
			readAlreadyLength = readAlreadyLength + readLength;
		}
		return receData;
	}
}

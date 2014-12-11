package core.cq.hmq.util.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.web.multipart.MultipartFile;


/**
 * 文件工具类
 * 
 * @author 孙宇
 * 
 */
public class FileUtil {

	private final static int BUFFER = 1024;

	/**
	 * 功 能: 移动文件(只能移动文件) 参 数: strSourceFileName:指定的文件全路径名 strDestDir: 移动到指定的文件夹
	 * 返回值: 如果成功true;否则false
	 * 
	 * @param strSourceFileName
	 * @param strDestDir
	 * @return
	 */
	public static boolean copyTo(String strSourceFileName, String strDestDir) {
		File fileSource = new File(strSourceFileName);
		File fileDest = new File(strDestDir);

		// 如果源文件不存或源文件是文件夹
		if (!fileSource.exists() || !fileSource.isFile()) {
			System.out.println("源文件[" + strSourceFileName + "],不存在或是文件夹!");
			return false;
		}

		// 如果目标文件夹不存在
		if (!fileDest.isDirectory() || !fileDest.exists()) {
			if (!fileDest.mkdirs()) {
				System.out.println("目录文件夹不存，在创建目标文件夹时失败!");
				return false;
			}
		}

		try {
			String strAbsFilename = strDestDir + File.separator
					+ fileSource.getName();

			FileInputStream fileInput = new FileInputStream(strSourceFileName);
			FileOutputStream fileOutput = new FileOutputStream(strAbsFilename);

			System.out.println("开始拷贝文件");

			int count = -1;

			long nWriteSize = 0;
			long nFileSize = fileSource.length();

			byte[] data = new byte[BUFFER];

			while (-1 != (count = fileInput.read(data, 0, BUFFER))) {

				fileOutput.write(data, 0, count);

				nWriteSize += count;

				long size = (nWriteSize * 100) / nFileSize;
				long t = nWriteSize;

				String msg = null;

				if (size <= 100 && size >= 0) {
					msg = "\r拷贝文件进度:   " + size + "%   \t" + "\t   已拷贝:   " + t;
					System.out.println(msg);
				} else if (size > 100) {
					msg = "\r拷贝文件进度:   " + 100 + "%   \t" + "\t   已拷贝:   " + t;
					System.out.println(msg);
				}

			}

			fileInput.close();
			fileOutput.close();

			System.out.println("拷贝文件成功!");
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 功 能: 文件上传
	 * 
	 * @param file
	 *            MultipartFile
	 * @param strDestDir
	 *            目标文件夹
	 * @param fileName
	 *            文件名
	 * @return 如果成功true;否则false
	 */
	public static boolean copy(MultipartFile file, String strDestDir) {
		File fileDest = new File(strDestDir);
		// 如果目标文件夹不存在
		if (!fileDest.isDirectory() || !fileDest.exists()) {
			if (!fileDest.mkdirs()) {
				System.out.println("目录文件夹不存，在创建目标文件夹时失败!");
				return false;
			}
		}
		try {
			// 获取文件名，转码
			String fileName = new String(file.getOriginalFilename().getBytes(
					"ISO8859-1"), "UTF-8");
			// 输入目标点+输入文件名
			String strAbsFilename = strDestDir + File.separator + fileName;
			// 获取输出流，用于拷贝文件
			FileOutputStream fileOutput = new FileOutputStream(strAbsFilename);
			// 获取输入流
			InputStream inputStream = file.getInputStream();
			System.out.println("开始拷贝文件");
			int count = -1;
			// long nWriteSize = 0;
			// long nFileSize = file.getSize();//文件大小
			byte[] data = new byte[BUFFER];
			while (-1 != (count = inputStream.read(data, 0, BUFFER))) {
				fileOutput.write(data, 0, count);
				// nWriteSize += count;
				// long size = (nWriteSize * 100) / nFileSize;
				// long t = nWriteSize;
				// String msg = null;
				// if (size <= 100 && size >= 0) {
				// msg = "\r拷贝文件进度:   " + size + "%   \t" + "\t   已拷贝:   " + t;
				// System.out.println(msg);
				// } else if (size > 100) {
				// msg = "\r拷贝文件进度:   " + 100 + "%   \t" + "\t   已拷贝:    " + t;
				// System.out.println(msg);
				// }
			}
			inputStream.close();
			fileOutput.close();
			System.out.println("拷贝文件成功!");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 功 能: 删除指定的文件 参 数: 指定绝对路径的文件名 strFileName 返回值: 如果删除成功true否则false;
	 * 
	 * @param strFileName
	 * @return
	 */
	public static boolean delete(String strFileName) {
		File fileDelete = new File(strFileName);

		if (!fileDelete.exists() || !fileDelete.isFile()) {
			System.out.println(strFileName + "不存在!");
			return false;
		}

		return fileDelete.delete();
	}

	/**
	 * 功 能: 移动文件(只能移动文件) 参 数: strSourceFileName: 是指定的文件全路径名 strDestDir:
	 * 移动到指定的文件夹中 返回值: 如果成功true; 否则false
	 * 
	 * @param strSourceFileName
	 * @param strDestDir
	 * @return
	 */
	public static boolean moveFile(String strSourceFileName, String strDestDir) {
		if (copyTo(strSourceFileName, strDestDir))
			return delete(strSourceFileName);
		else
			return false;
	}

	/**
	 * 功 能: 创建文件夹 参 数: strDir 要创建的文件夹名称 返回值: 如果成功true;否则false
	 * 
	 * @param strDir
	 * @return
	 */
	public static boolean makeDir(String strDir) {
		File fileNew = new File(strDir);
		if (!fileNew.exists()) {
			return fileNew.mkdirs();
		} else {
			return true;
		}
	}

	/**
	 * 功 能: 删除文件夹 参 数: strDir 要删除的文件夹名称 返回值: 如果成功true;否则false
	 * 
	 * @param strDir
	 * @return
	 */
	public static boolean removeDir(String strDir) {
		File rmDir = new File(strDir);
		if (rmDir.isDirectory() && rmDir.exists()) {
			String[] fileList = rmDir.list();

			for (int i = 0; i < fileList.length; i++) {
				String subFile = strDir + File.separator + fileList[i];
				File tmp = new File(subFile);
				if (tmp.isFile())
					tmp.delete();
				else if (tmp.isDirectory())
					removeDir(subFile);
			}
			rmDir.delete();
		} else
			return false;
		return true;
	}

	/**
	 * 跟文件加上日期与随机数防止重复
	 * 
	 * @return
	 */
	public static String fileAddRand(String fileNameAll) {
		int i = fileNameAll.lastIndexOf(".");// 分开文件名与文件类型
		String fileName = fileNameAll.substring(0, i);// 文件名
		String fileType = fileNameAll.substring(i + 1).toLowerCase();// 文件类型
		Random r = new Random();
		return fileName + DateUtil.format(DateUtil.currentDate(), "yyMMdd")
				+ r.nextInt(100) + "." + fileType;
	}

	/**
	 * 根据文件名 取出文件名与文件类型 string[0] 文件名 xxx string[1] 文件类型 txt
	 * 
	 * @param fileNameAll
	 *            xxx.txt
	 * @return
	 */
	public static String[] findFileNameAndSuffix(String fileNameAll) {
		String[] s = new String[2];
		int i = fileNameAll.lastIndexOf(".");// 分开文件名与文件类型
		String fileName = fileNameAll.substring(0, i);// 文件名
		String fileType = fileNameAll.substring(i + 1).toLowerCase();// 文件类型
		s[0] = fileName;
		s[1] = fileType;
		return s;
	}

	/**
	 * 根据 路径查找文件夹下的文件，遍历所有
	 * 
	 * @param filepath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String> readfile(String filepath) {
		try {
			File file = new File(filepath);
			if (!file.isDirectory()) {// 参数地址是文件
				// System.out.println("路径=" + file.getPath());
				// System.out.println("绝对路径=" + file.getAbsolutePath());
				// System.out.println("名字=" + file.getName());
				return null;

			} else if (file.isDirectory()) {// 参数地址是文件夹
				String[] filelist = file.list();
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filepath + "\\" + filelist[i]);
					if (!readfile.isDirectory()) {
						// System.out.println("路径=" + readfile.getPath());
						// System.out.println("绝对路径=" +
						// readfile.getAbsolutePath());
						// System.out.println("名字=" + readfile.getName());
						list.add(readfile.getName());
					} else if (readfile.isDirectory()) {
						readfile(filepath + "\\" + filelist[i]);
					}
				}
				return list;
			}

		} catch (Exception e) {
			System.out.println("readfile()   Exception:" + e.getMessage());
		}
		return null;
	}

	private static final int BUFFER_SIZE = 16 * 1024;

	/**
	 * 文件对拷
	 * 
	 * @param src
	 *            源文件
	 * @param dst
	 *            目标文件
	 */
	public static void copy(File src, File dst) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst),
					BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 写文件到本地
	 * 
	 * @param in
	 * @param fileName
	 * @throws IOException
	 */
	private void copyFile(InputStream in, String fileName) throws IOException {
		FileOutputStream fs = new FileOutputStream("d:/upload/" + fileName);
		byte[] buffer = new byte[1024 * 1024];
		int bytesum = 0;
		int byteread = 0;
		while ((byteread = in.read(buffer)) != -1) {
			bytesum += byteread;
			fs.write(buffer, 0, byteread);
			fs.flush();
		}
		fs.close();
		in.close();
	}

}

package common.cq.hmq.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
public class ConvertVoice {
 
 private final static String PATH = "C:\\Users\\Administrator\\Desktop\\1.flv";
 public static void main(String[] args) {
	 List<String> commend=new java.util.ArrayList<String>();
     commend.add("D:/dcode/ffmpeg");
     commend.add("-ss");
     commend.add("1");
     commend.add("-i");
     commend.add(PATH);
     commend.add("-f");
     commend.add("image2");
     commend.add("-y");
     commend.add("C:\\Users\\Administrator\\Desktop\\test1.jpg");
     try {
         ProcessBuilder builder = new ProcessBuilder();
         builder.command(commend);
         builder.start();
     } catch (Exception e) {
         e.printStackTrace();
     }
 }
 
 public static boolean processVoice(String ofileName,String nfileName,String toolPath) {       
	 	int type = checkContentType(ofileName);
        boolean status = false;
        if (type==0) {
            status = processAACand3gptoMp3(ofileName,nfileName,toolPath);
        } else if(type==1) {
            String avifilepath = processAVI(type);
            if (avifilepath == null)
                return false;
            status = processAACand3gptoMp3(avifilepath,nfileName,toolPath);
        }
        return status;
    }
 
    private static int checkContentType(String fileName) {
        String type = fileName.substring(fileName.lastIndexOf(".") + 1,
        		fileName.length()).toLowerCase();
        /** ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等） */
        if (type.equals("avi")) {
            return 0;
        } else if (type.equals("mpg")) {
            return 0;
        } else if (type.equals("wmv")) {
            return 0;
        } else if (type.equals("3gp")) {
            return 0;
        } else if (type.equals("mov")) {
            return 0;
        } else if (type.equals("mp4")) {
            return 0;
        } else if (type.equals("asf")) {
            return 0;
        } else if (type.equals("asx")) {
            return 0;
        } else if (type.equals("flv")) {
            return 0;
        } else if (type.equals("aac")) {
            return 0;
        } else if (type.equals("amr")) {
            return 0;
        }
        /** 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
         *  可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式. */
        else if (type.equals("wmv9")) {
            return 1;
        } else if (type.equals("rm")) {
            return 1;
        } else if (type.equals("rmvb")) {
            return 1;
        }       
        return 9;
    }
   
  public static boolean checkfile(String path){
     File file=new File(path);
     if(!file.isFile()){
      return false;
     }
     return true;
  }
    
    /** 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 
     * 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.*/
    private static String processAVI(int type) {
        List<String> commend=new java.util.ArrayList<String>();
        commend.add("d:\\mencoder");
        commend.add(PATH);
        commend.add("-oac");
        commend.add("lavc");
        commend.add("-lavcopts");
        commend.add("acodec=mp3:abitrate=64");
        commend.add("-ovc");
        commend.add("xvid");
        commend.add("-xvidencopts");
        commend.add("bitrate=600");
        commend.add("-of");
        commend.add("avi");
        commend.add("-o");
        commend.add("c:\\home\\a.avi");
       
        try{
         ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            builder.redirectErrorStream(true);
            Process proc =  builder.start();
            BufferedReader stdout = new BufferedReader(  
            		                    new InputStreamReader(proc.getInputStream()));  
            		          String line;  
        		            while ((line = stdout.readLine()) != null) {  
        		                   System.err.println(line);  
        		           }  
            		            proc.waitFor();     
            		            stdout.close();  

            return "c:\\home\\a.avi";
        }catch(Exception e){
         e.printStackTrace();
         return null;
        }
    }
    private static boolean processAACand3gptoMp3(String oldfilepath,String newfilepath,String toolpath) {
      if(!checkfile(oldfilepath)){
          System.out.println(oldfilepath+" is not file");
          return false;
         }
      /*-ab bitrate 设置音频码率
      -ar freq 设置音频采样率
      -ac channels 设置通道 缺省为1
      -an 不使能音频纪录
      -b bitrate 设置比特率，缺省200kb/s
      -r fps 设置帧频 缺省25
      -acodec codec 使用codec编解码*/
       
        List<String> commend=new java.util.ArrayList<String>();
        commend.add(toolpath);
        commend.add("-i");
        commend.add(oldfilepath);
        commend.add("-ab");
        commend.add("64");
        commend.add("-acodec");
        commend.add("mp3");
        commend.add("-ac");
        commend.add("2");
        commend.add("-ar");
        commend.add("22050");
        commend.add("-b");
        commend.add("230");
        commend.add("-r");
        commend.add("24");
        commend.add("-y");
        commend.add(newfilepath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            builder.redirectErrorStream(true);
            Process proc =  builder.start();
            if(null == proc || null == proc.getInputStream()){
            	return false;
            }
            BufferedReader stdout = new BufferedReader(  
            		                    new InputStreamReader(proc.getInputStream()));  
            		          String line;  
        		            while ((line = stdout.readLine()) != null) {  
        		                    //System.out.println(line);   
        		           }  
    		            proc.waitFor();     
    		            stdout.close();  
     
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static int jietu(String sp,String picPath){
   	 //$ time ffmpeg -ss 00:02:06 -i test1.flv -f image2 -y test1.jpg
   	 List<String> commend=new java.util.ArrayList<String>();
     commend.add("D:/dcode/ffmpeg");
     commend.add("-i");
     commend.add(PATH);
     commend.add("-y");
     commend.add("-f");
     commend.add("image2");
     commend.add("-ss");
    //commend.add("00:08");
     commend.add("8");
     commend.add("-s");
     commend.add("352x240");
     commend.add("C:\\Users\\Administrator\\Desktop\\test1.jpg");
     try {
         ProcessBuilder builder = new ProcessBuilder();
         builder.command(commend);
         builder.start();
     } catch (Exception e) {
         e.printStackTrace();
     }
     return 0;
    }
    
 
}
package common.cq.hmq.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.util.ConvertVoice;

import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.EasyData;
import core.cq.hmq.modal.PageModel;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.DateUtil;
import core.cq.hmq.util.tools.FileUtil;
import core.cq.hmq.util.tools.LogUtil;
import core.cq.hmq.util.tools.ResourceUtil;

@Service(value = "attachService")
public class AttachService extends BaseService {

	/**
	 * 创建附件记录
	 * 
	 * @return
	 */
	@Transactional
	public void inster(Attach attach) {
		dao.insert(attach);
	}

	/**
	 * 附件上传
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public AjaxMsg upload(HttpServletRequest request) {
		AjaxMsg am = new AjaxMsg(AjaxMsg.SUCCESS);
		if((request instanceof MultipartHttpServletRequest) == false){
			am.setType(AjaxMsg.ERROR);
			return am;
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// MultipartResolver resolver = new
		// CommonsMultipartResolver(request.getSession().getServletContext());
		// MultipartHttpServletRequest multipartRequest =
		// resolver.resolveMultipart(request);
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		FileUtil.makeDir(ResourceUtil.getUploadPath());
		String fileName = null;
		Long fileSize = null;
		String attachIds = "";
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			try {
				MultipartFile mf = entity.getValue();
				fileSize = (Long) mf.getSize();
				fileName = mf.getOriginalFilename();
				/** 存入数据库 */
				Attach attach = insert(fileName, fileSize);
				if (attach.getId() == null) {
					am.setMsg("保存失败");
					am.setType(AjaxMsg.ERROR);
					return am;
				} else {
					am.setId(attach.getId());
					// 以id为名字，attach为后缀存入磁盘
					String fileNameAttach = attach.getId() + ".attach";
					File uploadFile = new File(ResourceUtil.getUploadPath()
							+ "/" + fileNameAttach);
					FileCopyUtils.copy(mf.getBytes(), uploadFile);
					am.setId(attach.getId());
					attachIds += attach.getId() + ",";
				}
			} catch (Exception e) {
				e.printStackTrace();
				am.setMsg("保存失败");
				am.setType(AjaxMsg.ERROR);
				return am;
			}
		}
		am.setMsgId(attachIds);
		am.setMsg("保存成功");
		return am;
	}

	/**
	 * 
	 * @param fileName
	 *            附件名字
	 * @param fileSize
	 *            附件大小
	 * @return
	 */
	@SuppressWarnings("unused")
	@Transactional
	public Attach insert(String fileName, Long fileSize) {
		Attach attach = new Attach();
		attach.setDate(DateUtil.currentDate());
		attach.setFileName(fileName);
		attach.setSuffix(FileUtil.findFileNameAndSuffix(fileName)[1]);
		attach.setSize(fileSize);
		inster(attach);
		return attach;
	}

	/**
	 * 查找出附件
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public EasyData<Attach> findAttach(PageModel model) {
		EasyData<Attach> ed = new EasyData<Attach>();
		String hql = "from " + Attach.class.getName();
		if (model.getQuerySql() != null) {
			hql += "  where " + model.getQuerySql();
		}
		PageList<Attach> attachs = page(model, hql);
		ed.setRows(attachs.getList());
		ed.setTotal(attachs.getTotalCount());
		return ed;
	}

	/**
	 * 获取附件数据流
	 * 
	 * @param id
	 * @return
	 */
	public Attach getAttachWithFile(Long id) {
		Attach attach = dao.findOne(Attach.class, "id", id);
		try {
			FileInputStream fis = new FileInputStream(getPath(attach));
			if (fis != null) {
				attach.setFile(fis);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("没找到附件");
			// e.printStackTrace();
		}
		return attach;
	}

	public Attach findById(Long id) {
		return dao.findOne(Attach.class, "id", id);
	}
	
	@Transactional
	public AjaxMsg judgeFileExistForCj(Attach attach,String relType) {
		AjaxMsg am = new AjaxMsg();
		Attach a = (Attach) dao.findOne("from Attach where fileName = ? and relType = ?", attach.getFileName(),relType);
		if(null != a){
			am.setType(2);
			try {
				FileUtil.delete(getPath(attach));
				dao.delete(attach);
				am.setMsg("该文件已经导入,请勿重复操作！");
			} catch (Exception e) {
				am.setMsg("该文件已经导入,请勿重复操作！");
			}
		}
		return am;
	}

	private String getPath(Attach attach) {
		return ResourceUtil.getUploadPath() + "\\" + attach.getId() + ".attach";
	}

	@Transactional
	public AjaxMsg delete(Long id) {
		Attach dbAttach = dao.findOne(Attach.class, "id", id);
		AjaxMsg am = new AjaxMsg();
		if (dbAttach == null) {
			am.setType(2);
			am.setMsg("附件不存在");
			return am;
		}
		try {
			FileUtil.delete(getPath(dbAttach));
			dao.delete(dbAttach);
			LogUtil.getLog("警告").info("删除附件" + dbAttach.getFileName());
			am.setMsg("删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			am.setType(1);
			am.setMsg("删除失败");
		}
		return am;
	}

	/**
	 * 变更附件的 实体名字与实体id
	 * 
	 * @param relType
	 * @param relId
	 * @param attachs
	 */
	@Transactional
	public void changeRelType(String relType, Long relId, String[] attachs) {
		for (int i = 0; i < attachs.length; i++) {
			Attach dbAttach = dao.findOne(Attach.class, "id",
					Long.parseLong(attachs[i]));
			dbAttach.setRelId(relId);
			dbAttach.setRelType(relType);
			dao.update(dbAttach);
		}
	}

	/**
	 * 变更附件的 实体名字
	 * 
	 */
	@Transactional
	public AjaxMsg changeRelType(Long id) {
		AjaxMsg am = new AjaxMsg();
		try {
			Attach dbAttach = dao.findOne(Attach.class, "id", id);
			dbAttach.setRelType("table");
			dao.update(dbAttach);
			am.setMsg("更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			am.setMsg("更新失败，error：" + e.getMessage());
			am.setType(am.ERROR);
		}
		return am;
	}

	/**
	 * 根据实体名称与实体ID查找附件
	 * 
	 * @param relType
	 * @param relId
	 * @return
	 */
	public List<Attach> findByRel(String relType, Long relId) {
		return dao.find("from " + Attach.class.getName()
				+ " a where a.relType=? and a.relId=?", relType, relId);
	}

	/**
	 * 根据实体名称与实体ID删除附件
	 * 
	 * @param relType
	 * @param relId
	 */
	public void deleteByRel(String relType, Long relId) {
		dao.delete(findByRel(relType, relId));
	}

	/**
	 * 更新实体
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	public AjaxMsg update(Attach dbAttach) {
		AjaxMsg am = new AjaxMsg();
		try {
			if (null != dbAttach && null != dbAttach.getId()) {
				dao.update(dbAttach);
				am.setMsg("更新成功");
			}
		} catch (Exception e) {
			// TODO: handle exception
			am.setMsg("更新失败，error：" + e.getMessage());
			am.setType(am.ERROR);
		}
		return am;
	}

	public static void main(String[] args) {
		String a = "222.3";
		System.out.println(a);
		if (a.indexOf(".3") != -1) {
			System.out.println(11);
		}
	}

	@Transactional
	public AjaxMsg uploadVoice(HttpServletRequest request) {
		AjaxMsg am = new AjaxMsg();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		FileUtil.makeDir(ResourceUtil.getUploadPath());
		String fileName = null;
		Long fileSize = null;
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			try {
				String suffix = "mp3";
				MultipartFile mf = entity.getValue();
				fileSize = (Long) mf.getSize();
				fileName = mf.getOriginalFilename();
				if (fileName.toLowerCase().indexOf(".aac") == -1
						&& fileName.toLowerCase().indexOf(".3gp") == -1) {
					am.setMsg("语音格式不正确");
					am.setType(AjaxMsg.ERROR);
					return am;
				}
				/** 原声文件 */
				String path = ResourceUtil.getUploadPath() + "/";
				File selfFile = new File(path + fileName);
				FileCopyUtils.copy(mf.getBytes(), selfFile);
				/** 转换文件 */
				String newFileName = FileUtil.findFileNameAndSuffix(fileName)[0]
						+ "." + suffix;

				/** 存入附件表 */
				Attach attach = insert(newFileName, fileSize);

				if (attach.getId() == null) {
					am.setMsg("保存失败");
					am.setType(AjaxMsg.ERROR);
					return am;
				} else {
					boolean status = ConvertVoice.processVoice(path + fileName,path + newFileName,
							ResourceBundle.getBundle("config").getString("converToolPath"));
					if (status) {
						Long a = System.currentTimeMillis();
						Long b = -1L;
						File temp = null;
						while ((null == temp || !temp.exists())  && b < 18) {
							temp = new File(path + newFileName);
							b = (System.currentTimeMillis() - a) / 1000;
						}
						if (b > 17) {
							am.setMsg("保存失败");
							am.setType(AjaxMsg.ERROR);
							selfFile.delete();
							dao.delete(attach);
							return am;
						}
						//Thread.currentThread().sleep(2100);
						selfFile.delete();
						attach.setSize(temp.length());
						/** 以id为名字，attach为后缀存入磁盘 */
						String fileNameAttach = attach.getId() + ".attach";
						File uploadFile = new File(path + "/" + fileNameAttach);
						FileCopyUtils.copy(temp, uploadFile);
						am.setId(attach.getId());
						temp.delete();
					} else {
						am.setMsg("保存失败");
						am.setType(AjaxMsg.ERROR);
						selfFile.delete();
						dao.delete(attach);
						return am;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				am.setMsg("保存失败");
				am.setType(AjaxMsg.ERROR);
				return am;
			}
		}
		am.setMsg("保存成功");
		return am;
	}
	
	@Transactional
	public AjaxMsg uploadImage(HttpServletRequest request) {
		AjaxMsg am = new AjaxMsg(AjaxMsg.SUCCESS);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		FileUtil.makeDir(ResourceUtil.getUploadPath());
		String fileName = null;
		Long fileSize = null;
		String attachIds = "";
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				MultipartFile mf = entity.getValue();
				fileSize = (Long) mf.getSize();
				fileName = mf.getOriginalFilename();
				try {
					if(fileSize > 0 && !mf.getContentType().startsWith("image/")){
						am.setMsg("文件不为图片格式,请重新上传");
						am.setType(AjaxMsg.ERROR);
						return am;
					}
					if(fileSize < 1){
						continue;
					}
					BufferedImage bi = ImageIO.read(mf.getInputStream());
					if(null == bi){
						am.setMsg("文件不为图片格式,请重新上传");
						am.setType(AjaxMsg.ERROR);
						return am;
					}
				} catch (IOException e1) {
					am.setMsg("文件不为图片格式,请重新上传");
					am.setType(AjaxMsg.ERROR);
					return am;
				}
				
				
			try {	
				/** 存入数据库 */
				Attach attach = insert(fileName, fileSize);
				if (attach.getId() == null) {
					am.setMsg("保存失败");
					am.setType(AjaxMsg.ERROR);
					return am;
				} else {
					am.setId(attach.getId());
					// 以id为名字，attach为后缀存入磁盘
					String fileNameAttach = attach.getId() + ".attach";
					File uploadFile = new File(ResourceUtil.getUploadPath()
							+ "/" + fileNameAttach);
					FileCopyUtils.copy(mf.getBytes(), uploadFile);
					am.setId(attach.getId());
					attachIds += attach.getId() + ",";
				}
			} catch (Exception e) {
				e.printStackTrace();
				am.setMsg("保存失败");
				am.setType(AjaxMsg.ERROR);
				return am;
			}
		}
		am.setMsgId(attachIds);
		am.setMsg("保存成功");
		return am;
	}
}

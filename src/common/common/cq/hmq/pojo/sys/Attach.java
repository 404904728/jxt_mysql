package common.cq.hmq.pojo.sys;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import core.cq.hmq.pojo.sys.IdEntity;
import core.cq.hmq.service.util.JsonDateSerializer;
import core.cq.hmq.util.tools.ResourceUtil;
import core.cq.hmq.util.tools.StringUtil;

@Entity
public class Attach {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** 存的文件名 */
	private String fileName;

	/** 附件后缀 */
	private String suffix;

	/** 文件大小 */
	private Long size;

	/** 附件所属pojo对象的主键 */
	private Long relId;

	/** 所属pojo的类型 */
	@Column(length = 30)
	private String relType;

	/** 上传日期 */
	private Date date;

	/** 附件描述 */
	@Column(length = 150)
	private String desc;

	/** 临时属性，用户存放下载地址 */
	@Transient
	private String htmlUrl;

	/** 临时属性，附件文件对应的输入流 */
	@Transient
	private InputStream file;

	/** 临时属性，友好展示附件大小 */
	@Transient
	private String sizeView;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getRelId() {
		return relId;
	}

	public void setRelId(Long relId) {
		this.relId = relId;
	}

	public String getRelType() {
		return relType;
	}

	public void setRelType(String relType) {
		this.relType = relType;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Transient
	public String getHtmlUrl() {
		return buildUrl(id);
	}

	private String buildUrl(Object fid) {
		return "download/" + fid + "." + suffix;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public String getSizeView() {
		return StringUtil.fomatFileSize(size);
	}

	public void setSizeView(String sizeView) {
		this.sizeView = sizeView;
	}

	public InputStream getFile() {
		return file;
	}

	public void setFile(InputStream file) {
		this.file = file;
	}

	public String getSuffix() {
		// 取得文件后缀
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public File getFilePath() {
		String fileNameAttach = getId() + ".attach";
		return new File(ResourceUtil.getUploadPath() + "/" + fileNameAttach);

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}

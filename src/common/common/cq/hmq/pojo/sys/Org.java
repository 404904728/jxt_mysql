package common.cq.hmq.pojo.sys;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import core.cq.hmq.service.util.JsonDateSerializer;

@Entity
public class Org {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String name;

	/**
	 * 正级leader id 1,2,3
	 */
	private String mLeader;
	
	/**
	 * 副级leader id 1,2,3
	 */
	private String sLeader;

	/** 排序 */
	private Integer order;

	@ManyToOne
	private Org parent;

	/** 是否可用 */
	private Boolean status = true;

	/** 联系方式 */
	private String tel;

	/** 结业年 该值不为空时 是班级 */
	private Date date;

	/** 0:校级 1：阶段 2：年级3：班级 4:老师组织结构*/
	private Integer type;
	
	/**部门编号*/
	private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Org getParent() {
		return parent;
	}

	public void setParent(Org parent) {
		this.parent = parent;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setmLeader(String mLeader) {
		this.mLeader = mLeader;
	}

	public String getmLeader() {
		return mLeader;
	}

	public void setsLeader(String sLeader) {
		this.sLeader = sLeader;
	}

	public String getsLeader() {
		return sLeader;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}

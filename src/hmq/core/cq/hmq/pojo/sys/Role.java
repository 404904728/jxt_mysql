package core.cq.hmq.pojo.sys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	private Boolean use = true;

	private String desc;

	/**
	 * 领导
	 */
	private Long leader;

	/**
	 * 电话
	 */
	private String tel;

	/**
	 * 角色类型 0 普通角色1老师角色
	 */
	private Integer type;

	/** 0 默认公共组 2 中学组 1：小学组 3管理组 */
	private Integer isSorM = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getUse() {
		return use;
	}

	public void setUse(Boolean use) {
		this.use = use;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Long getLeader() {
		return leader;
	}

	public void setLeader(Long leader) {
		this.leader = leader;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIsSorM() {
		return isSorM;
	}

	public void setIsSorM(Integer isSorM) {
		this.isSorM = isSorM;
	}

}

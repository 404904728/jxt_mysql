package common.cq.hmq.pojo.sys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 用户 角色关系表
 * 
 * @author cqmonster
 * 
 */
@Entity
@SuppressWarnings("serial")
public class UserRole {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Long roleId;

	private Long userId;

	private Boolean teacher = true;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getTeacher() {
		return teacher;
	}

	public void setTeacher(Boolean teacher) {
		this.teacher = teacher;
	}

}

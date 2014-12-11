package core.cq.hmq.pojo.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class HbMySq {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id = 0l;

	@Column(name = "val_f")
	private Long val = 0l;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVal() {
		return val;
	}

	public void setVal(Long val) {
		this.val = val;
	}
}

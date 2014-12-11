package core.cq.hmq.modal;

import java.util.List;

public class FlowModal {
	
	/**
	 * 节点ID
	 */
	private String id;
	
	/**
	 * 节点名称
	 */
	private String name;
	
	/**
	 * 连接线所到的节点名称
	 */
	private String toName;

	/**
	 * 是节点还是 决策
	 */
	private Boolean decision;
	
	private String type;
	
	private int x;
	private int y;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public Boolean getDecision() {
		return decision;
	}

	public void setDecision(Boolean decision) {
		this.decision = decision;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

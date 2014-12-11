package core.cq.hmq.modal;

/**
 * easy 信息框
 * 
 * @author monster
 * 
 */
public class AjaxMsg {
	public static int SUCCESS = 0;
	public static int INFO = 1;
	public static int WORN = 2;
	public static int ERROR = 3;

	/**
	 * 0：成功 1：信息 2：警告 3：错误 默认为成功
	 */
	private int type = 0;

	/**
	 * 返回的信息
	 */
	private String msg;

	/**
	 * 返回的对象ID
	 */
	private Long id;

	private String msgId;
	
	public AjaxMsg() {
		super();
	}

	public AjaxMsg(int type) {
		super();
		this.type = type;
	}

	public AjaxMsg(int type, String msg) {
		super();
		this.type = type;
		this.msg = msg;
	}
	public AjaxMsg(String msg) {
		super();
		this.msg = msg;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		if (msg.indexOf("失败") > 0) {
			this.type = ERROR;
		}
		this.msg = msg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}

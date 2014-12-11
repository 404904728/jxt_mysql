package core.cq.hmq.util;

public class MsgException extends RuntimeException {
	public MsgException(String msg) {
		super(msg);
	}

	public MsgException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public String toString() {
		return getMessage();
	}

	public static MsgException create(Object data, String ftl) {
		// String msg = FtlUtil.build(data, WebContext.config().getMsgFtl(ftl));
		return new MsgException("错误");
	}
}

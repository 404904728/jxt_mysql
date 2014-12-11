package core.cq.hmq.annotation.instance;

import core.cq.hmq.annotation.ControllerAnn;


/**
 * @since Fan Houjun 2009-7-15
 */
public class AnnInstance {

	@ControllerAnn
	public void ControllerAnn() {
		// 忽略
	}

	private static ControllerAnn controllerAnn = null;

	public static ControllerAnn getControllerAnn() throws SecurityException,
			NoSuchMethodException {
		if (controllerAnn == null) {
			controllerAnn = AnnInstance.class.getMethod("ControllerAnn")
					.getAnnotation(ControllerAnn.class);
		}
		return controllerAnn;
	}
}

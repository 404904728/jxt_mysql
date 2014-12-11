package core.cq.hmq.util.tools;

import java.util.List;

public class HqlUtil {

	public static Number toNumber(List list) {
		return toNumber(list, 0);
	}

	public static Number toNumber(List list, Number dft) {
		if (list == null || list.isEmpty() || list.get(0) == null) {
			return dft;
		}
		return (Number) list.get(0);
	}
}

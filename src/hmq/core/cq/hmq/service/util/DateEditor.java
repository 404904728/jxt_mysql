package core.cq.hmq.service.util;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

import core.cq.hmq.util.tools.DateUtil;

public class DateEditor extends PropertyEditorSupport {

	private static final DateFormat DATE_PATTERN = new SimpleDateFormat(
			DateUtil.DATE_PATTERN);
	private static final DateFormat DATETIME_PATTERN = new SimpleDateFormat(
			DateUtil.DATETIME_PATTERN);

	private DateFormat dateFormat;
	private boolean allowEmpty = true;

	public DateEditor() {
	}

	public DateEditor(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public DateEditor(DateFormat dateFormat, boolean allowEmpty) {
		this.dateFormat = dateFormat;
		this.allowEmpty = allowEmpty;
	}

	/**
	 * 解析日期
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			setValue(null);
		} else {
			try {
				if (this.dateFormat != null)
					setValue(this.dateFormat.parse(text));
				else {
					if (text.contains(":"))
						setValue(DATETIME_PATTERN.parse(text));
					else
						setValue(DATE_PATTERN.parse(text));
				}
			} catch (ParseException ex) {
				throw new IllegalArgumentException("不能解析该日期: "
						+ ex.getMessage(), ex);
			}
		}
	}

	/**
	 * Format the Date as String, using the specified DateFormat.
	 */
	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		DateFormat dateFormat = this.dateFormat;
		if (dateFormat == null)
			dateFormat = DATETIME_PATTERN;
		return (value != null ? dateFormat.format(value) : "");
	}
}

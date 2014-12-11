package core.cq.hmq.service.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.springframework.stereotype.Component;

import core.cq.hmq.util.tools.DateUtil;

@Component
public class JsonDateSerializer extends JsonSerializer<Date> {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			DateUtil.DATETIME_PATTERN);

	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider pro)
			throws IOException, JsonProcessingException {
		String formattedDate = dateFormat.format(date);
		gen.writeString(formattedDate);
	}

	@Override
	public void serializeWithType(Date value, JsonGenerator jgen,
			SerializerProvider provider, TypeSerializer typeSer)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub

		super.serializeWithType(value, jgen, provider, typeSer);
	}

}

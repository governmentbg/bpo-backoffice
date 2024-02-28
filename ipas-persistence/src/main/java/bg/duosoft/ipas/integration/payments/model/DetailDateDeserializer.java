package bg.duosoft.ipas.integration.payments.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailDateDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		String val = jsonParser.getText();
		if(val != null && !val.isEmpty()){
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			try{
				return sdf.parse(val);
			} catch (ParseException e) {}
		}
		return null;
	}
}

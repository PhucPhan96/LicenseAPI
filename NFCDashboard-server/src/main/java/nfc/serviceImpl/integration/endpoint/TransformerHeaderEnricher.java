package nfc.serviceImpl.integration.endpoint;

import java.util.Collections;

import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.integration.transformer.support.ExpressionEvaluatingHeaderValueMessageProcessor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public class TransformerHeaderEnricher {
	public HeaderEnricher headerEnricher() {
		return new HeaderEnricher(Collections.singletonMap(SimpMessageHeaderAccessor.SESSION_ID_HEADER,
				new ExpressionEvaluatingHeaderValueMessageProcessor<Object>("payload", null)));
	}
}

package edu.uaeh.sagahand.components;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.RequestContextHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("MensajesC")
public class MensajesC {
	
	public MensajesC() {
		super();
		log.debug("Se crea componente MensajesC");
	}

	public void mensajeError(String mensaje) {
		RequestContextHolder.getRequestContext().getMessageContext().addMessage(new MessageBuilder().error().defaultText(mensaje).build());
	}
	
	public void mensajeInfo(String mensaje) {
		RequestContextHolder.getRequestContext().getMessageContext().addMessage(new MessageBuilder().info().defaultText(mensaje).build());
	}
	
	public void mensajeWarn(String mensaje) {
		RequestContextHolder.getRequestContext().getMessageContext().addMessage(new MessageBuilder().warning().defaultText(mensaje).build());
	}
	
	public void mensajeFatal(String mensaje) {
		RequestContextHolder.getRequestContext().getMessageContext().addMessage(new MessageBuilder().fatal().defaultText(mensaje).build());
	}
}

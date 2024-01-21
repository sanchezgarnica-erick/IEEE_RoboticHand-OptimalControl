package edu.uaeh.sagahand.components.administracion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component("ParametrosC")
public class ParametrosC {
	
	public static BigDecimal BIGDECIMAL_ZERO = BigDecimal.ZERO;
	public static BigDecimal BIGDECIMAL_CIEN = new BigDecimal(100);
	
	private final String version = "0.0.1";
	private LocalDate fechaHoy;
	private DateTimeFormatter fechaFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private DateTimeFormatter horaFormato = DateTimeFormatter.ofPattern("hh:mm a");
	private DateTimeFormatter fechaHoraFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
	
	public ParametrosC() {
		super();
		fechaHoy = LocalDate.now();
		log.debug("Se crea objeto ParametrosC");
	}
	
	@Scheduled(cron = "1 * * * * *")
	private void actualizarFechaHoy() {
		fechaHoy = LocalDate.now();
	}
}

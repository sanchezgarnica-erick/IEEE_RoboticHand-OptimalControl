package edu.uaeh.sagahand.components.config;

import java.time.LocalDateTime;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uaeh.sagahand.components.MensajesC;
import edu.uaeh.sagahand.database.control.HandEO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("MqttConfig")
public class MqttConfig {
	
	@Autowired 
	private MensajesC mensajesC;
	
	private static String SAGAHAND_CONECTED = "Ok";
	
	private static String CHECK_STATUS = "a";
	private static String REQUEST_INFO = "b";
	private static String REQUEST_PID = "c";
	
	private IMqttClient mqttClient;
	private MqttConnectOptions options;
	private MqttCallback mqttCallback;
	
	private HandEO hand;
	
	private ObjectMapper objectMapper;
	
	/*
	private String username = "WextLna2t1CpRrP";
	private String password = "2ODW6tIgZTuPpU1";
	private String esp32InputTopic = "LQfjj1Gu4avGzn7/esp32/input";
	private String esp32OutputTopic = "LQfjj1Gu4avGzn7/esp32/output";
	private String url = "tcp://ioticos.org:1883";
	*/
	private String username = "sagahand_web";
	private String password = "M7gcycry8gy3aswFYCRCd5UEAe8Bwt";
	private String esp32InputTopic = "sagahand/esp32/input";
	private String esp32OutputTopic = "sagahand/esp32/output";
	private String url = "tcp://saganet.com.mx:1883"; 
	//private String url = "tcp://localhost:1883";
	private String sagaLinuxDir = "/sagahand_mqtt";
	
	public MqttConfig() {
		super();
		hand = new HandEO();
		objectMapper = new ObjectMapper();
		options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		options.setConnectionTimeout(10);
		options.setUserName(username);
		options.setPassword(password.toCharArray());
		try {
			//mqttClient = new MqttClient(url, MqttClient.generateClientId()); //Windows
			mqttClient = new MqttClient(url, MqttClient.generateClientId(), new MqttDefaultFilePersistence(sagaLinuxDir)); //Linux SaGaServer
			mqttClient.connect(options);
			log.debug("cliente MQTT conectado");
			mqttClient.subscribe(esp32OutputTopic);
			setupCallback();
			mqttClient.setCallback(mqttCallback);
		} catch (MqttException e) {
			log.debug("Error al crear cliente MQTT");
			e.printStackTrace();
		}
		log.debug("Se crea componente MqttConfig");
	}
	
	public boolean publish(String message) {
		MqttMessage mqttMessage;
		
		/*
		if(!mqttClient.isConnected()) {
			mensajesC.mensajeError("Cliente MQTT no conectado");
			return false;
		}
		*/
		
		mqttMessage = new MqttMessage(message.getBytes());
		mqttMessage.setQos(0);
		mqttMessage.setRetained(false);
		try {
			mqttClient.publish(esp32InputTopic, mqttMessage);
		} catch (MqttException e) {
			log.debug("Error al publicar en MQTT");
			e.printStackTrace();
		}
		//mensajesC.mensajeInfo("Mensaje MQTT enviado exitosamente");
		
		return true;
	}
	
	private void setupCallback() {
		mqttCallback = new MqttCallback() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				String payload = message.toString(); 
				Map<String, Object> map;
				
				//log.debug("Mensaje recibido ({}): {}", topic, payload);
				
				if(payload.equals(SAGAHAND_CONECTED)) {
					hand.setActiva(true);
					hand.setUltimaComunicacion(LocalDateTime.now());
					return;
				}
				
				if(payload.substring(0, 5).equals("info:")) {
					try {
					map = objectMapper.readValue(payload.substring(5), Map.class);
					//log.debug("Map: {}", map);
					
					hand.getIndexFinger().setState((double) map.get("indexFingerState"));
					hand.getIndexFinger().setControl((double) map.get("indexFingerControl"));
					hand.getIndexFinger().setSetPoint((double) map.get("indexFingerSetPoint"));
					
					hand.getMiddleFinger().setState((double) map.get("middleFingerState"));
					hand.getMiddleFinger().setControl((double) map.get("middleFingerControl"));
					hand.getMiddleFinger().setSetPoint((double) map.get("middleFingerSetPoint"));
					
					hand.getRingFinger().setState((double) map.get("ringFingerState"));
					hand.getRingFinger().setControl((double) map.get("ringFingerControl"));
					hand.getRingFinger().setSetPoint((double) map.get("ringFingerSetPoint"));
					
					hand.getLittleFinger().setState((double) map.get("littleFingerState"));
					hand.getLittleFinger().setControl((double) map.get("littleFingerControl"));
					hand.getLittleFinger().setSetPoint((double) map.get("littleFingerSetPoint"));
					
					hand.getThumb().setState((double) map.get("thumbState"));
					hand.getThumb().setControl((double) map.get("thumbControl"));
					hand.getThumb().setSetPoint((double) map.get("thumbSetPoint"));
					hand.setUltimaComunicacion(LocalDateTime.now());
					hand.compute();
					//log.debug("Hand updated: {}", hand);
					} catch(Exception e) {
						e.printStackTrace();
					}
					return;
				}
				
				if(payload.substring(0, 4).equals("PID:")) {
					try {
					map = objectMapper.readValue(payload.substring(4), Map.class);
					log.debug("Map PID: {}", map);
					hand.getIndexFinger().setKp((double) map.get("indexFingerKp"));
					hand.getIndexFinger().setKi((double) map.get("indexFingerKi"));
					hand.getIndexFinger().setKd((double) map.get("indexFingerKd"));
					
					hand.getMiddleFinger().setKp((double) map.get("middleFingerKp"));
					hand.getMiddleFinger().setKi((double) map.get("middleFingerKi"));
					hand.getMiddleFinger().setKd((double) map.get("middleFingerKd"));
					
					hand.getRingFinger().setKp((double) map.get("ringFingerKp"));
					hand.getRingFinger().setKi((double) map.get("ringFingerKi"));
					hand.getRingFinger().setKd((double) map.get("ringFingerKd"));
					
					hand.getLittleFinger().setKp((double) map.get("littleFingerKp"));
					hand.getLittleFinger().setKi((double) map.get("littleFingerKi"));
					hand.getLittleFinger().setKd((double) map.get("littleFingerKd"));
					
					hand.getThumb().setKp((double) map.get("thumbKp"));
					hand.getThumb().setKi((double) map.get("thumbKi"));
					hand.getThumb().setKd((double) map.get("thumbKd"));
					
					hand.setUltimaComunicacion(LocalDateTime.now());
					log.debug("Hand updated PID: {}", hand);
					} catch(Exception e) {
						e.printStackTrace();
					}
					return;
				}
			}
			
			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
				/*
				try {
					log.debug("Entrega de mensaje completada: {}", token.getMessage().toString());
				} catch (MqttException e) {
					log.debug("Error en deliveryComplete");
					e.printStackTrace();
				}
				*/
			}
			
			@Override
			public void connectionLost(Throwable cause) {
				log.debug("Conexión con MQTT perdida: {}", cause.getMessage());
			}
		};
	}
	
	@PreDestroy
	private void desconectar() {
		try {
			mqttClient.disconnect();
		} catch (MqttException e) {
			log.debug("Error al desconectar MQTT");
			e.printStackTrace();
		}
	}
	
	public boolean checkStatus() {
		publish(CHECK_STATUS);
		mensajesC.mensajeInfo("Estatus verificado exitosamente");
		return true;
	}
	
	public HandEO updateSetPoint(HandEO hand) {
		this.hand.getIndexFinger().setSetPoint(hand.getIndexFinger().getSetPoint());
		this.hand.getMiddleFinger().setSetPoint(hand.getMiddleFinger().getSetPoint());
		this.hand.getRingFinger().setSetPoint(hand.getRingFinger().getSetPoint());
		this.hand.getLittleFinger().setSetPoint(hand.getLittleFinger().getSetPoint());
		this.hand.getThumb().setSetPoint(hand.getThumb().getSetPoint());
		this.hand.compute();
		
		//log.debug("Actualizando SetPoint ************** {}", this.hand.getTestFinger().getSetPoint());
		StringBuilder sb;
		sb = new StringBuilder("s:");
		sb.append(this.hand.getIndexFinger().getSetPoint());
		sb.append(":");
		sb.append(this.hand.getMiddleFinger().getSetPoint());
		sb.append(":");
		sb.append(this.hand.getRingFinger().getSetPoint());
		sb.append(":");
		sb.append(this.hand.getLittleFinger().getSetPoint());
		sb.append(":");
		sb.append(this.hand.getThumb().getSetPoint());
		publish(sb.toString());
		return this.hand;
	}
	
	public HandEO updatePID(HandEO hand) {
		this.hand.getIndexFinger().setKp(hand.getIndexFinger().getKp());
		this.hand.getIndexFinger().setKi(hand.getIndexFinger().getKi());
		this.hand.getIndexFinger().setKd(hand.getIndexFinger().getKd()); 
		
		this.hand.getMiddleFinger().setKp(hand.getMiddleFinger().getKp());
		this.hand.getMiddleFinger().setKi(hand.getMiddleFinger().getKi());
		this.hand.getMiddleFinger().setKd(hand.getMiddleFinger().getKd());
		
		this.hand.getRingFinger().setKp(hand.getRingFinger().getKp());
		this.hand.getRingFinger().setKi(hand.getRingFinger().getKi());
		this.hand.getRingFinger().setKd(hand.getRingFinger().getKd());
		
		this.hand.getLittleFinger().setKp(hand.getLittleFinger().getKp());
		this.hand.getLittleFinger().setKi(hand.getLittleFinger().getKi());
		this.hand.getLittleFinger().setKd(hand.getLittleFinger().getKd());
		
		this.hand.getThumb().setKp(hand.getThumb().getKp());
		this.hand.getThumb().setKi(hand.getThumb().getKi());
		this.hand.getThumb().setKd(hand.getThumb().getKd());
		
		StringBuilder sb;
		sb = new StringBuilder("p:"); 
		sb.append(this.hand.getIndexFinger().getKp());
		sb.append(":");
		sb.append(this.hand.getIndexFinger().getKi());
		sb.append(":");
		sb.append(this.hand.getIndexFinger().getKd());
		sb.append(":");
		sb.append(this.hand.getMiddleFinger().getKp());
		sb.append(":");
		sb.append(this.hand.getMiddleFinger().getKi());
		sb.append(":");
		sb.append(this.hand.getMiddleFinger().getKd());
		sb.append(":");
		sb.append(this.hand.getRingFinger().getKp());
		sb.append(":");
		sb.append(this.hand.getRingFinger().getKi());
		sb.append(":");
		sb.append(this.hand.getRingFinger().getKd());
		sb.append(":");
		sb.append(this.hand.getLittleFinger().getKp());
		sb.append(":");
		sb.append(this.hand.getLittleFinger().getKi());
		sb.append(":");
		sb.append(this.hand.getLittleFinger().getKd());
		sb.append(":");
		sb.append(this.hand.getThumb().getKp());
		sb.append(":");
		sb.append(this.hand.getThumb().getKi());
		sb.append(":");
		sb.append(this.hand.getThumb().getKd());
		publish(sb.toString());
		
		return this.hand;
	}
	
	@Scheduled(initialDelay = 10*1000, fixedDelay = 1*1000)
	public void requestInfo() {
		publish(REQUEST_INFO);
	}
	
	public void requestPID() {
		publish(REQUEST_PID);
	}
	
	public HandEO getHand() {
		return hand;
	}
	
	public void voiceCommand(String command) {
		log.info("Comando: {}", command.toUpperCase());
		switch(command.toUpperCase()) {
		case "CERRAR MANO":
			publish("s:250:250:250:250:250");
			break;
		case "ABRIR MANO":
			publish("s:0:0:0:0:0");
			break;
		case "ACTIVAR AGARRE":
			publish("g");
			break;
		case "DESACTIVAR AGARRE":
			publish("h");
			break;
		case "SUJETAR BOLSA":
			publish("s:140:140:140:140:100");
			break; 
		case "AMOR Y PAZ":
			publish("s:0:0:250:250:200");
			break;
		default:
			log.info("Comando no reconocido");
		}
	}
}

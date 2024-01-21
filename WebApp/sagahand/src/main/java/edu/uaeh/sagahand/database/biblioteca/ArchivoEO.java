package edu.uaeh.sagahand.database.biblioteca;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import org.apache.ibatis.type.Alias;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.webflow.execution.RequestContextHolder;

import edu.uaeh.sagahand.database.dominios.TiposMimeDO;
import edu.uaeh.sagahand.utils.EntityObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
@Alias("ArchivoEO")
public class ArchivoEO extends EntityObject implements Serializable {
	private static final long serialVersionUID = -7165243624268795320L;

	private Integer id;
	private String nombre;
	private TiposMimeDO tipoMime;
	private byte[] objeto;
	private String descripcion;
	
	public ArchivoEO() {
		super();
		tipoMime = TiposMimeDO.GENERICO;
	}
	
	public void uploadFile(FileUploadEvent event) throws Exception{
		log.debug("**************************Cargando archivo");
		UploadedFile uploadedFile;
		uploadedFile = event.getFile();
		objeto = uploadedFile.getContent();
		nombre = uploadedFile.getFileName();
		tipoMime = TiposMimeDO.porTipo(uploadedFile.getContentType());
		RequestContextHolder.getRequestContext().getMessageContext().addMessage(new MessageBuilder().info().defaultText("Archivo Cargado").build());
		
		log.debug("Cargando archivo...");
		log.debug("Nombre: {}", nombre);
		log.debug("TipoMIME: {}", tipoMime);
		log.debug("Descripcion: {}", descripcion);
	}
	
	public boolean isUpload() {
		return objeto != null;
	}
	
	public StreamedContent getStreamedContent() {
		StreamedContent streamedContent;
		streamedContent = DefaultStreamedContent.builder()
				.contentType(tipoMime.getCodigo())
				.name(nombre)
				.stream(() -> new ByteArrayInputStream(objeto))
				.build();
		return streamedContent;
	}
}

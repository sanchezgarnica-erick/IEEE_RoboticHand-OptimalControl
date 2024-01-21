package edu.uaeh.sagahand.database.dominios;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Alias("TiposMimeDO")
public enum TiposMimeDO {
	GENERICO("No identificado", "application/octet-stream", ""),
	
	WORD("Microsoft WORD", "application/msword", ".doc"),
	EXCEL("Microsoft Excel", "	application/vnd.ms-excel", ".xls"),
	POWER_POINT("Microsoft PowerPoint", "application/vnd.ms-powerpoint", ".ppt"),
	WORD2007("Microsoft WORD 2007", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx"),
	EXCEL2007("Microsoft Excel 2007", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"),
	POWER_POINT2007("Microsoft PowerPoint 2007", "application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx"),
	POWER_POINT2007_PRESENTACION("Microsoft PowerPoint 2007 (Presentación)", "application/vnd.openxmlformats-officedocument.presentationml.slideshow", ".ppsx"),
	PDF("Adobe Portable Document Format (PDF)", "application/pdf", ".pdf"),
	RAR("Archivo Comprimido (RAR)", "application/x-rar-compressed", ".rar"),
	ZIP("Archivo Comprimido (ZIP)", "application/zip", ".zip"),
	MPEG("Video MPEG", "video/mpeg", ".mpeg"),
	OPENDOCUMENT_DOCUMENT("Documento de texto OpenDocument", "application/vnd.oasis.opendocument.text", ".odt"),
	OPENDOCUMENT_SPREADSHEET("Hoja de Cálculo OpenDocument", "application/vnd.oasis.opendocument.spreadsheet", ".ods"),
	OPENDOCUMENT_PRESENTATION("Presentación de OpenDocument", "application/vnd.oasis.opendocument.presentation", ".odp"),
	JPG("Imagen (JPG)", "image/jpeg", ".jpg"),
	JPEG("Imagen (JPEG)", "image/jpeg", ".jpeg"),
	PNG("Imagen (PNG)", "image/png", ".png"),
	GIF("Imagen (GIF)", "image/gif", ".gif"),
	TEXTO("Archivo de Texto", "text/plain", ".txt"),
	AAC("Archivo de audio AAC", "audio/aac", ".aac"),
	ABW("Documento AbiWord", "application/x-abiword", ".abw"),
	ARC("Documento de Archivo (múltiples archivos incrustados)", "application/octet-stream", ".arc"),
	AVI("AVI: Audio Video Intercalado", "video/x-msvideo", ".avi"),
	AZW("Formato  eBook Amazon Kindle ", "application/vnd.amazon.ebook", ".azw"),
	BIN("Cualquier tipo de datos binarios", "application/octet-stream", ".bin"),
	BZ("Archivo BZip", "application/x-bzip", ".bz"),
	BZ2("Archivo BZip2", "application/x-bzip2", ".bz2"),
	CSH("Script C-Shell", "application/x-csh", ".csh"),
	CSS("Hojas de estilo (CSS)", "text/css", ".css"),
	CSV("Valores separados por coma (CSV)", "text/csv", ".csv"),
	EPUB("Publicación Electrónica (EPUB)", "application/epub+zip", ".epub"),
	HTM("Hipertexto (HTML)", "text/html", ".htm"),
	HTML("Hipertexto (HTML)", "text/html", ".html"),
	ICO("Formato Icon", "image/x-icon", ".ico"),
	ICS("Formato iCalendar", "text/calendar", ".ics"),
	JAR("Archivo Java (JAR)", "application/java-archive", ".jar"),
	JS("JavaScript (ECMAScript)", "application/javascript", ".js"),
	JSON("Formato JSON", "application/json", ".json"),
	MID("Interfaz Digital de Instrumentos Musicales (MIDI)", "audio/midi", ".mid"),
	MIDI("Interfaz Digital de Instrumentos Musicales (MIDI)", "audio/midi", ".midi"),
	MPKG("Paquete de instalación de Apple", "application/vnd.apple.installer+xml", ".mpkg"),
	OGA("Audio OGG", "audio/ogg", ".oga"),
	OGV("Video OGG", "video/ogg", ".ogv"),
	OGX("OGG", "application/ogg", ".ogx"),
	RTF("Formato de Texto Enriquecido (RTF)", "application/rtf", ".rtf"),
	SH("Script Bourne shell", "application/x-sh", ".sh"),
	SVG("Gráficos Vectoriales (SVG)", "image/svg+xml", ".svg"),
	SWF("Small web format (SWF) o Documento Adobe Flash", "application/x-shockwave-flash", ".swf"),
	TAR("Aerchivo Tape (TAR)", "application/x-tar", ".tar"),
	TIF("Formato de archivo de imagen etiquetado (TIFF)", "image/tiff", ".tif"),
	TIFF("Formato de archivo de imagen etiquetado (TIFF)", "image/tiff", ".tiff"),
	TTF("Fuente TrueType", "font/ttf", ".ttf"),
	VSD("Microsft Visio", "application/vnd.visio", ".vsd"),
	WAV("Formato de audio de forma de onda (WAV)", "audio/x-wav", ".wav"),
	WEBA("Audio WEBM", "audio/webm", ".weba"),
	WEBM("Video WEBM", "video/webm", ".webm"),
	WEBP("Imágenes WEBP", "image/webp", ".webp"),
	WOFF("Formato de fuente abierta web (WOFF)", "font/woff", ".woff"),
	WOFF2("Formato de fuente abierta web (WOFF)", "font/woff2", ".woff2"),
	XHTML("XHTML", "application/xhtml+xml", ".xhtml"),
	XML("XML", "application/xml", ".xml"),
	XUL("XUL", "application/vnd.mozilla.xul+xml", ".xul"),
	_3GP("Contenedor de audio/video 3GPP", "video/3gpp", ".3gp"),
	_3GP2("Contenedor de audio/video 3GPP2", "video/3gpp2", ".3g2"),
	_7Z("Archivo 7-zip", "application/x-7z-compressed", ".7z");

	
	private final String nombre;
	private final String codigo;
	private final String extension;
	
	private static Map<String, TiposMimeDO> MAPA_TIPOS;
	static {
		MAPA_TIPOS = new HashMap<String, TiposMimeDO>();
		for(TiposMimeDO tipo : TiposMimeDO.values()) {
			MAPA_TIPOS.put(tipo.codigo, tipo);
		}
		MAPA_TIPOS = Collections.unmodifiableMap(MAPA_TIPOS);
	}
	
	public static TiposMimeDO porTipo(String tipo) {
		if(MAPA_TIPOS.containsKey(tipo)) {
			return MAPA_TIPOS.get(tipo);
		} 
		return GENERICO;
	}
}

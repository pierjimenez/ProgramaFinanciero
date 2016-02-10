package pe.com.bbva.iipf.pf.bo.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;


import pe.com.bbva.iipf.pf.bo.CorreoBO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.model.MensajeCorreo;
import pe.com.bbva.iipf.util.Constantes;

@Service("correoBO")
public class CorreoBOImpl implements CorreoBO {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ParametroBO parametroBO;
	
	@Override
	public void enviarCorreo(Map<String, Object> parametros) throws Exception {
	
		// 1.- Configuracion
		String smpt_host = parametroBO.findByNombreParametro(Constantes.CORREO_IP_SERVER_SMTP).getValor();
		String smpt_port = parametroBO.findByNombreParametro(Constantes.CORREO_PORT_SERVER_SMTP).getValor();
		String smpt_user = parametroBO.findByNombreParametro(Constantes.CORREO_USER_SERVER_SMTP).getValor();
		String smpt_pswd = parametroBO.findByNombreParametro(Constantes.CORREO_PSWD_SERVER_SMTP).getValor();
		String smpt_ssl  = parametroBO.findByNombreParametro(Constantes.CORREO_SSL_SERVER_SMTP).getValor();
		String smpt_from = parametroBO.findByNombreParametro(Constantes.CORREO_FROM_SERVER_SMTP).getValor();
		// 2.- Cuentas de correo
		String from = parametroBO.findByNombreParametro(Constantes.CORREO_DE).getValor();
		String to   = parametroBO.findByNombreParametro(Constantes.CORREO_PARA).getValor();
		String cc   = parametroBO.findByNombreParametro(Constantes.CORREO_COPIA).getValor();
		String bcc  = parametroBO.findByNombreParametro(Constantes.CORREO_OCULTO).getValor();
		from	=(from	==null)?"":from;
		to		=(to	==null)?"":to;
		cc		=(cc	==null)?"":cc;
		bcc		=(bcc	==null)?"":bcc;
		
		Properties props = new Properties();
		props.put("mail.debug", "true");
		props.put("mail.smtp.host", smpt_host); // "smtp.gmail.com"
		props.put("mail.smtp.port", smpt_port); // "465"
		
		if(smpt_user != null) {
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.user", smpt_user); // "romanilopez@gmail.com"
		}
		if(smpt_pswd != null) {
			props.put("mail.password",  smpt_pswd);
		}

		if ("1".equals(smpt_ssl)) {
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.socketFactory.port", smpt_port);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");
		}

		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setJavaMailProperties(props);
		if(smpt_user != null) {
			sender.setUsername(smpt_user);
		}
		if(smpt_pswd != null) {
			sender.setPassword(smpt_pswd);
		}
		sender.setHost(smpt_host);
		sender.setPort(Integer.parseInt(smpt_port));

		MimeMessage message = sender.createMimeMessage();
//		
//		String to2  = (String) parametros.get("<<correoPara>>");
//		if(to2 != null && !to2.trim().equals("")) {
//			if(to.trim().equals("")) {
//				to = to2;
//			} else {
//				to += ((to.substring(to.length()-1, to.length()).equals(Constantes.CORREO_SEPARADOR))?"":Constantes.CORREO_SEPARADOR) + to2;
//			}
//		}
		if(smpt_from != null && !smpt_from.trim().equals("")) {
			from = smpt_from;
		}
		//
		message.setFrom(new InternetAddress(from));
		
		//PARA
		if(to != null && !to.equals("")) {
			message.setRecipients(Message.RecipientType.TO, generarDireccionesCorreo(to));
		}
		//COPIA
		if(cc != null && !cc.equals("")) {
			message.setRecipients(Message.RecipientType.CC, generarDireccionesCorreo(cc));
		}
		//OCULTO
		if(bcc != null && !bcc.equals("")) {
			message.setRecipients(Message.RecipientType.BCC, generarDireccionesCorreo(bcc));
		}
		message.setSubject(parametroBO.findByNombreParametro(Constantes.CORREO_ASUNTO).getValor());
		message.setSentDate(new Date());
		    
		String cuerpo = parametroBO.findByNombreParametro(Constantes.CORREO_CUERPO).getValor();
		if(cuerpo != null && parametros != null) {
			for (Iterator<String> iterator = parametros.keySet().iterator(); iterator.hasNext();) {
				String llave = iterator.next();
				if(parametros.get(llave) instanceof String) {
					cuerpo = cuerpo.replaceAll(llave,parametros.get(llave)+"");
				}
			}
		}
	
		
//		cuerpo=cuerpo.replaceAll("<USUARIO>"		,usuario);
//		cuerpo=cuerpo.replaceAll("<NOMBRE_ARCHIVO>"	,nombreArchivo);
//		cuerpo=cuerpo.replaceAll("<ESTADO_ARCHIVO>"	,estado);
//		cuerpo=cuerpo.replaceAll("<FECHA_ACTUAL>"	,fechaHoraActual);
		
		String formato = "text/plain";
		
		if (Constantes.CORREO_FORMATO_HTML.equalsIgnoreCase(parametroBO.findByNombreParametro(Constantes.CORREO_FORMATO).getValor())) {
			formato = "text/html";
			String estilos = "<style> " +
					         "body  {font-family:Verdana; font-size:11} " +
					         "table {font-family:Verdana; font-size:11} " +
					         "</style> ";
			cuerpo = estilos + cuerpo;
			//cuerpo = "<div style='font-family:Verdana; font-size:10'>" + cuerpo + "</div>";
		}
		
		Multipart mp = new MimeMultipart();
		
		List<File> archivos = (List<File>) parametros.get("archivos");
		
		if(archivos != null) {
			for (File archivo : archivos)
			{
				MimeBodyPart mbp = new MimeBodyPart();

		        // attach the file to the message
				FileDataSource fds = new FileDataSource(archivo);
				mbp.setDataHandler(new DataHandler(fds));
				mbp.setFileName(fds.getName());
				
				mp.addBodyPart(mbp);
			}
		}
		
		MimeBodyPart mbp = new MimeBodyPart();
		mbp.setContent(cuerpo, formato);
		mp.addBodyPart(mbp);
		
		message.setContent(mp);

		// 4.- Envio
		sender.send(message);
	}
	@Override
	public void enviarCorreo(MensajeCorreo mensajeCorreo) {
		try {
			HashMap<String,Object> parametros=new HashMap<String,Object>();
			
			
			if (mensajeCorreo!=null){
				
	
		    	String fechaHoraActual	=mensajeCorreo.getFechaCreacion();	    	
		    	String usuario			=mensajeCorreo.getUsuario();
		    	String nombreArchivo	=mensajeCorreo.getNombreArchivo();
		    	String descripcion		=mensajeCorreo.getDescripcion();
		    	String estado			=mensajeCorreo.getEstado();
		    	
		    	
				
				String smpt_host = parametroBO.findByNombreParametro(Constantes.CORREO_IP_SERVER_SMTP).getValor();
				String smpt_port = parametroBO.findByNombreParametro(Constantes.CORREO_PORT_SERVER_SMTP).getValor();
				
				String smpt_user = parametroBO.findByNombreParametro(Constantes.CORREO_USER_SERVER_SMTP).getValor();
				String smpt_pswd = parametroBO.findByNombreParametro(Constantes.CORREO_PSWD_SERVER_SMTP).getValor();
				String smpt_ssl  = parametroBO.findByNombreParametro(Constantes.CORREO_SSL_SERVER_SMTP).getValor();
				String smpt_from = parametroBO.findByNombreParametro(Constantes.CORREO_FROM_SERVER_SMTP).getValor();
				// 2.- Cuentas de correo
				final String from = parametroBO.findByNombreParametro(Constantes.CORREO_DE).getValor();
				final String to   = parametroBO.findByNombreParametro(Constantes.CORREO_PARA).getValor();
				String cc   = parametroBO.findByNombreParametro(Constantes.CORREO_COPIA).getValor();
				String bcc  = parametroBO.findByNombreParametro(Constantes.CORREO_OCULTO).getValor();
				
				String usuarioEnvio=parametroBO.findByNombreParametro(Constantes.CORREO_USUARIO_ENVIO).getValor();
				String cuerpo = parametroBO.findByNombreParametro(Constantes.CORREO_CUERPO).getValor();
				
				parametros.put("<USUARIO>"		,usuarioEnvio);
		    	parametros.put("<NOMBRE_ARCHIVO>", nombreArchivo);
		    	parametros.put("<ESTADO_ARCHIVO>",estado);
		    	parametros.put("<FECHA_ACTUAL>"	, fechaHoraActual);	    	
		    	parametros.put("<DESCRIPCION>"	, descripcion);
				
				
				if(cuerpo != null && parametros != null) {
					for (Iterator<String> iterator = parametros.keySet().iterator(); iterator.hasNext();) {
						String llave = iterator.next();
						if(parametros.get(llave) instanceof String) {
							cuerpo = cuerpo.replaceAll(llave,parametros.get(llave)+"");
						}
					}
				}
				
				final String  mensaje=cuerpo;
				
	//			final String SSL_EMAIL  = parameter.get(Constantes.COD_PARAM_SSL_CORREO);
	//			final String PASS_EMAIL = parameter.get(Constantes.COD_PARAM_PASS_CORREO);
	//			final String HOST_EMAIL = parameter.get(Constantes.COD_PARAM_HOST_CORREO);
	//			final String FROM_EMAIL = parameter.get(Constantes.COD_PARAM_FROM_CORREO);
	//			final int PORT_EMAIL = Integer.valueOf(parameter.get(Constantes.COD_PARAM_PORT_CORREO));
	//			
	//			String activaNotificacion=parametroBO.findParametro(Constantes.COD_PARAM__ACTIVAR_NOTIFICACIONES).getValor();
	//			final Parametro paramDestTest = parametroBO.findByNombreParametro(Constantes.DESTINAT_MAIL_TEST);
				
				
				JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
				mailSender.setHost(smpt_host);
				mailSender.setPort(Integer.parseInt(smpt_port));
	
	
				MimeMessagePreparator preparator = new MimeMessagePreparator() {
					public void prepare(MimeMessage mimeMessage) throws Exception {
						MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
						message.setTo(to);					
						message.setFrom(from);
						message.setSubject(parametroBO.findByNombreParametro(Constantes.CORREO_ASUNTO).getValor());
						message.setText(mensaje,true);
					}
				};
				
				mailSender.send(preparator);
				logger.debug("Se envio correo.");
			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	
	
	
	
	private Address[] generarDireccionesCorreo(String direcciones) throws Exception {
		String[]  arregloStr = direcciones.split(Constantes.CORREO_SEPARADOR);
    	Address[] arregloAdd = new InternetAddress[arregloStr.length];
    	
    	for (int i = 0; i < arregloAdd.length; i++) {
    		arregloAdd[i] = new InternetAddress(arregloStr[i]);
		}
    	return arregloAdd;
	}
	
	private Session obtenerSession() throws Exception {
		Authenticator authenticator = new Authenticator();
		
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", "118.180.25.77");
        properties.setProperty("mail.smtp.port", "25");
        
        return Session.getInstance(properties, authenticator);
	}

    private class Authenticator extends javax.mail.Authenticator {
    	private PasswordAuthentication authentication;

        public Authenticator() {
        	String username = "AdministradorMonitor@grupobbva.com.pe";
            String password = "p@ssw0rd";
            authentication = new PasswordAuthentication(username, password);
        }
        @Override
		protected PasswordAuthentication getPasswordAuthentication() {
        	return authentication;
        }
    }
    @Override
    public void enviarCorreoPrubeba(){
    	try{
	    	ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
	    	CorreoBO correoBO= (CorreoBO)context.getBean("correoBO");
	    	HashMap<String,Object> parametros=new HashMap<String,Object>();
	    	String fechaHoraActual	="";
	    	String usuario			="";
	    	String nombreArchivo	="";
	    	String estado			="";
	    	try{
	    		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    		fechaHoraActual=sdf.format(new Date());
	    	}catch (Exception e) {}
	
	    	parametros.put("<USUARIO>"		,"Walter Duran" );
	    	parametros.put("<NOMBRE_ARCHIVO>", "RENBEC");
	    	parametros.put("<ESTADO_ARCHIVO>", "ACTIVO");
	    	parametros.put("<FECHA_ACTUAL>"	, fechaHoraActual);
	    //	send(parametros);
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    }
}

<%@taglib prefix="s" uri="/struts-tags" %>
<%@page import="pe.com.bbva.iipf.util.Constantes"%>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>
<%@page import="pe.com.stefanini.core.host.UsuarioSesion"%>
<%
UsuarioSesion usuarioSession = (UsuarioSesion)GenericAction.getObjectSession(Constantes.USUARIO_SESSION); 
String activoDescargaManual = (GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_REF_DESCARGAMANUAL)==null?"1":GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_REF_DESCARGAMANUAL).toString());

%>

<script type="text/javascript">

function beforeDescargar(){		
	try {resetTiempoSession();}	catch(err) {}
}
</script>

<table bgcolor="#FFFFFF" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="60%" style="color: #005a8c; font-size: 10px; font-family: Verdana, Geneva, sans-serif; text-align: left;">
    	<div class="banner"></div>
	</td>
    <td width="35%" valign="bottom">
    	<div class="espace">Sistema de Programas Financieros&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>    	
  	</td >
  	<td rowspan="2"  valign="middle" width="5%">
  		<div id="home"><a href="<s:url action='home'/>"><img src="imagentabla/bbva.HomeAzul48.png" alt="Ir al Panel" border="0"></a></div>

  	</td>
 
  
  </tr>
  
  <tr style="text-align: right;">
    <td width="60%" style="color: #0078D2; font-size: 10px; font-family: Verdana, Geneva, sans-serif; text-align: left;">
        &nbsp;Bienvenido:<%=usuarioSession.getNombre()%>&nbsp;&nbsp;   
    	<a href="<s:url action="closeSession"/>" style="text-decoration: none;color: #0078D2; font-size: 10px;font-weight:bold; font-family: Verdana, Geneva, sans-serif; text-align: left;">(Cerrar Sesi&oacute;n)</a>
	</td>
<!--	<td width="35%" valign="bottom" align="right">-->
<!--    	<span class="espaceTime"  id="TimeLeft"></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
<!--    </td>  -->
    <td>
    		<div class="control_sesion" style="position:fixed !important; right:30px; top:5px; z-index:10 !important z-index:10 !important; border-radius: 15px;">
				<span id="tiempo_letras" class="texto_azul12b"></span>								
				<div class="contenedor_barra">
					<div class="barra_progreso" style="width: 70.55000000000001px; background-color: rgb(130, 255, 0);">
						<span class="barra_animada"></span>
					</div>
					<div class="barra_rejilla"></div>
				</div>			
			</div>
	    <%if(activoDescargaManual.equals("1")){ %>
		<a href="<s:url action='downloadManual'/>" onclick="javascript:beforeDescargar();" style="text-decoration: none;color: #0078D2; font-size: 10px;font-weight:bold; font-family: Verdana, Geneva, sans-serif; text-align: left;">
			<img align="center" src="imagentabla/bbva.PdfAzul16.png" alt="Descargar Manual" border="0">&nbsp;PFA-Manual de Llenado&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</a>
		<%} %>
    </td>

  </tr>
<!--  <tr>-->
<!--    <td colspan="3" bgcolor="#0C71B5"><div class="ancho">-->
<!--    </div></td>-->
<!--  </tr>-->
<!--  <tr>-->
<!--    <td colspan="3" bgcolor="#02A9DE"><div class="ancho">-->
<!--    </div></td>-->
<!--  </tr>-->
<!--  <tr>-->
<!--    <td colspan="3" bgcolor="#9DD7ED"><div class="ancho"></div></td>-->
<!--  </tr>-->
  
</table>

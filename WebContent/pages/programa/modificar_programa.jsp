<%@taglib prefix="s" uri="/struts-tags" %>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>
<%@page import="pe.com.bbva.iipf.util.Constantes"%>  
<s:include value="/pages/programa/opciones_programa.jsp"></s:include>
<script type="text/javascript"> 
var flagGuardado=true;

	$(window).load(function() { setTimeout($.unblockUI, 1);});
</script>
	<table width="100%">
		<tr>
			<td>
				<div class="titulo">
					Modificar Programa: Seleccione una Secci&oacute;n
				</div>
			</td>
		</tr>
<!--		<tr>-->
<!--			<td>-->
<!--				<a class="btn" href="<s:url action="consultasModificaciones" includeParams="none"/>" style="float: right;text-decoration: none;"><img align="middle" src="/ProgramaFinanciero/icono/appbar.layout.expand.left.variant.png" style="border:0px">retornar</a>-->
<!--			</td>-->
<!--		</tr>-->
	</table>

<div class="seccion">
	<div class="datosGrupoEmpresa">
		<%String tipo_empresa = GenericAction.getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
		if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){%>
		<div>
		<label>&nbsp;</label><br>
		<label style="font-size: 12pt;font-weight: bold;">Grupo: <%=request.getSession().getAttribute("nombre_empresa_grupo_session")%></label>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="btn" onclick="mostrarEspere();" href="<s:url action="editargrupoAbiertoAction"/>"><img align="middle" src="/ProgramaFinanciero/icono/icono32/appbar.edit.box.png" border="0" alt="Editar Grupo" ></img>Actualizar</a>
		</div>
		<div>
		<label style="font-size: 12pt;font-weight: bold;">Empresa Principal: <%=request.getSession().getAttribute(Constantes.NOMBRE_EMPRESA_PRINCIPAL)%></label>
		<a href="#" onclick="ocultarMostrarOtEmp();" id="verMas" class="learn-more2">...Ver Mas</a>
		</div>
		<div id="otrasEmpresas" style="display:none">
		</div>
		<%}else{ %>
			<label style="font-size: 12pt;font-weight: bold;">Empresa: <%=request.getSession().getAttribute("nombre_empresa_grupo_session")%></label>
		<%}%>
	</div>
	<div style="width:400px" class="links">
	<ul >
		<li>
			
			<a class="learn-more" onclick="mostrarEspere();" href="<s:url action="initDatosBasicos"/>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.datosbasico.png" style="border:0px">Datos B&aacute;sicos</a>
			
		</li>
		<li>
			
		    <a class="learn-more" onclick="mostrarEspere();" href="<s:url action="initAnalisisEconomica"/>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.sintesis.png" style="border:0px">S&iacute;ntesis Econ&oacute;mico Financiero</a>
		   
		</li>
		<li>
			
		    <a class="learn-more" onclick="mostrarEspere();" href="<s:url action="initRating"/>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.rating.png" style="border:0px">Rating</a>
   			
		</li>
		<li>
			
		    <a class="learn-more" onclick="mostrarEspere();" href="<s:url action="initAnalisisSectorial"/>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.analisis.png" style="border:0px">An&aacute;lisis Sectorial</a>
   			
		</li>
		<li>
			
		    <a class="learn-more" onclick="mostrarEspere();" href="<s:url action="initRelacionesBancarias"/>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.relaciones.png" style="border:0px">Relaciones Bancarias</a>
   			
		</li>
		<li>
			
			<a class="learn-more" onclick="mostrarEspere();" href="<s:url action="initFactoresRiesgo"/>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.factores.png" style="border:0px">Factores de Riesgo</a>
			
		</li>
		<li>
			
		    <a class="learn-more" onclick="mostrarEspere();" href="<s:url action="initPropuestaRiesgo"/>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.propuesta.png" style="border:0px">Propuesta de Riesgo</a>
   			
		</li>
		<li>
			
		    <a class="learn-more" onclick="mostrarEspere();" href="<s:url action="initPoliticasRiesgo"/>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.politica.png" style="border:0px">Politicas de Riesgo</a>
   			
		</li>
		<li>
			
		    <a class="learn-more" onclick="mostrarEspere();" href="<s:url action="initAnexos"/>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.posicion.png" style="border:0px">Posición Principal</a>
			
		</li>
		<li>
			
		    <a class="learn-more" onclick="mostrarEspere();" href="<s:url action="initReporteCredito"/>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.reporte.png" style="border:0px">Reporte de Credito</a>
			
		</li>
	</ul>
	</div>
</div>
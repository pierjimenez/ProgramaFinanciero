<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page import="pe.com.bbva.iipf.mantenimiento.model.*"%>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>

<script language="JavaScript">

	function onloadConsulta(){	
		document.forms["listarControlpfFrom"].action='reporteControlpfIni.do';
		document.forms["listarControlpfFrom"].submit();	
	}
	function descargarExcelrc(){		
		document.forms["listarControlpfFrom"].action='downloadReporteControlpf.do';
		document.forms["listarControlpfFrom"].submit();	
		resetTiempoSession();		
	}
	
	function validarSelect(){
		var codReporte= $("#tipoReporte").val();
		if (codReporte=="999"){
			alert("Seleccione el Tipo de Repote");
			return false;
		}
		return true;
	}
	
	
   $(document).ready(function() {
	   	$("thead tr").attr("class","ui-widget-header");	   	
	   	$('select[name=tipoReporte]').change(function () {
	   		  		 onloadConsulta(); 			
	    });
	   
	    
	   	
	});	
		
</script>

<s:form  action = "reporteControlpfAction" id="listarControlpfFrom" theme="simple">

<%String tipoReportecfpf = GenericAction.getObjectSession("tipoReportecpf").toString();%>
<table width="100%">
	<tr>
		<td>
			<div class="titulo">Consulta de Control de Programa Financiero</div>
		</td>
	</tr>
	<tr>
		<td>
				<table cellspacing="0" cellpadding="1" width="100%" border="0" cellspacing="0" >
					<tr class="formpanel">	
										
						<td class="label" width="100px">
							Tipo Reporte:&nbsp;           						 
						</td>
						<td width="80px">
						 <s:select id="tipoReporte" name="tipoReporte" list="#{ '999':' SELECCIONAR ','1':' Reporte Programa por Estado ','2':' Reporte Programa por Cliente ','3':' Reporte Cantidad de Programa por Cliente '}" 
						 label="Archivo"/>						 
						</td>
						<td width="50px">								 
	  						<s:submit onclick="return validarSelect()" value="buscar" id="btablas" theme="simple" cssClass="btn"/>&nbsp;
						</td>
							
					</tr>
			
					<tr>
						<td colspan="3">
							<img src="image/xls.png" alt="download" border="0">
							<a href="javascript:descargarExcelrc();">Descargar Reporte de Control</a>
						</td>
						
					</tr>
					<tr>
						<td colspan="3">
							&nbsp;
						</td>						
					</tr>
					
					<% 					
					if(tipoReportecfpf.equals("1")){
					%>
					<tr>
						<td colspan="3">
							<div  class="my_div">
								    <display:table id="reporteControlpf" 
								    	name="listaReporteControlpfestado" uid="tb" 
        								pagesize="10" export="false" 
        								requestURI="/paginadoReporteControlpf.do" 
        								class="ui-widget ui-widget-content">   								  
								    <display:column property="idprograma"   title="Programa"/>
								    <display:column property="numeroSolicitud" title="Numero Solicitud"/>
								    <display:column property="estado" title="Estado"/>
								    <display:column property="fechaCreacion" title="Fecha Creacion"/>	
								    <display:column property="codusuarioCreacion" title="Usuario Creacion"/>
								    <display:column property="fechaModificacion" title="Fecha Modificacion"/>
								    <display:column property="codusuarioModificacion" title="Usuario Modificacion"/>															    									    
								    <display:setProperty name="paging.banner.placement" value="bottom" />
								    </display:table>
							</div>
						</td>
					</tr>
					
					<%}else if (tipoReportecfpf.equals("2")){ %>
					<tr>
						<td colspan="3">
							<div  class="my_div">
								    <display:table id="reporteControlpfEmpresa" 
								    	name="listaReporteControlpfEmpresa" uid="tb" 
        								pagesize="10" export="false" 
        								requestURI="/paginadoReporteControlpfEmpresa.do" 
        								class="ui-widget ui-widget-content"> 
       								  								  
								    <display:column property="codcentralEmpprincipal"   title="Código Emp. Principal"/>
								    <display:column property="rucEmpprincipal" title="Ruc Emp. Principal"/>
								    <display:column property="empresaprincipal" title="Empresa Principal"/>
								    <display:column property="numeroSolicitud" title="Número Solicitud"/>
								    <display:column property="tipo" title="Tipo"/>	
								    <display:column property="codigoGrupo" title="Código Grupo"/>
								    <display:column property="nombreGrupo" title="Nombre Grupo"/>
								    <display:column property="fechaCreacion" title="Fecha Creación"/>								    
							       <display:column property="fechaModificacion" title="Fecha Modificación"/>
							       <display:column property="estado" title="Estado"/>
							       <display:column property="usuarioCreacion" title="Usuario Creacion"/>
							       <display:column property="usuarioModificacion" title="Usuario Modificación"/>
							       
							       <display:column property="usuarioCierre" title="Usuario Cierre"/>
							       <display:column property="fechaCierre" title="Fecha Cierre"/>
							       <display:column property="motivoCierre" title="Motivo Cierre"/>
							       <display:column property="gestor" title="Gestor"/>
							       <display:column property="oficina" title="Oficina"/>
							       <display:column property="proximaRevision" title="Próxima Revisión"/>
							       <display:column property="rgopropbbvabc" title="Riesgo Prop. bbva bc."/>
							       <display:column property="propRiesgo" title="Propuesta Riesgo"/>
							       
							       <display:column property="rvglEPrincipal" title="RVGL Empresa Principal"/>
							       <display:column property="rvglESecundaria" title="RVGL Empresa Secundarias"/>
							       														    									    
								   <display:setProperty name="paging.banner.placement" value="bottom" />
								   </display:table>
							</div>
						</td>
					</tr>
					
					<%}else if (tipoReportecfpf.equals("3")){ %>
					<tr>
						<td colspan="3">
							<div  class="my_div">
								    <display:table id="reporteControlcantpfEmpresa" 
								    	name="listaReporteControlCantidadpfEmpresa" uid="tb" 
        								pagesize="10" export="false" 
        								requestURI="/paginadoReporteControlpfCantEmpresa.do" 
        								class="ui-widget ui-widget-content"> 
       								  								  
								    <display:column property="empresaPrincipal"   title="Emp. Principal"/>
								    <display:column property="rucEmpprincipal" title="Ruc Emp. Principal"/>								    
								    <display:column property="codcentralEmpprincipal" title="Codigo Emp. Principal"/>
								    <display:column property="cantPrograma" title="Cantidad programa"/>															    									    
								    <display:setProperty name="paging.banner.placement" value="bottom" />
								    </display:table>
							</div>
						</td>
					</tr>					
					<%}else{%>
					<tr>
						<td class="label" colspan="3">
						&nbsp;
						</td>
					</tr>
					<%}%>
				</table>
		</td>
	</tr>
</table>

</s:form>
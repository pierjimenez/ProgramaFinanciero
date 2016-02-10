<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page import="pe.com.bbva.iipf.pf.model.*"%>

<%@page import="pe.com.bbva.iipf.util.Constantes"%>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>

<% 
	String accesoexecute = GenericAction.getObjectParamtrosSession(Constantes.CODACCESOEXEC).toString();
%>
<link rel="stylesheet" type="text/css"
	href="/ProgramaFinanciero/css/table.css" />
<script type="text/javascript"
	src="/ProgramaFinanciero/js/jquery-ui-1.11.4.js"></script>
<link rel="stylesheet" type="text/css"
	href="/ProgramaFinanciero/css/demos.css" />

<link rel="stylesheet" type="text/css"
	href="/ProgramaFinanciero/css/jquery-ui-1.11.4.css" />

<script language="JavaScript">

	$(function() {
	
		$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$( "#dialog-formResult" ).dialog({
			autoOpen: false,
			height: 500,
			width: 950,
			modal: true,
			buttons: {
				"Aceptar": function() {						
						$( this ).dialog( "close" );					
				}
			},
			close: function() {
				
			}
		});
		

	
		
	});
		
	function descargarFileMnt(nomarch,ruta){
		document.forms["busquedaTablaForm"].action='downloadFileMnt.do';
		document.forms["busquedaTablaForm"].rutaMnt.value =ruta; 	
		document.forms["busquedaTablaForm"].nombreArchivo2.value =nomarch;
		document.forms["busquedaTablaForm"].submit();	
		resetTiempoSession();
	}
	
	
	function eliminarFileMnt(nomarch,ruta){
		document.forms["busquedaTablaForm"].action='eliminarFileMnt.do';
		document.forms["busquedaTablaForm"].rutaMnt.value =ruta; 	
		document.forms["busquedaTablaForm"].nombreArchivo2.value =nomarch;
		document.forms["busquedaTablaForm"].submit();	
		resetTiempoSession();
	}

	
   $(document).ready(function() {
	   	$("thead tr").attr("class","ui-widget-header");
	   	
	   	 $("#exeScritp").click( function (){                    
			 $.blockUI({message:  '<h3>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } });			 
	   		$( "#idresultScritp" ).html('');
	   		var tesxquery= $("#textScritp").val();
		    var tipoquery= $("#tipoScritp").val(); 
		    //var acceso= "1234";  
		    var acceso = prompt('Introduce tu codigo Acceso','[ codigo Acceso ]');
		    
		         
		   		$.post("resultScriptAction.do", 
			   			   {textScritp:tesxquery,tipoScritp:tipoquery,codigoAcceso:acceso},
					   		function(data){			   						   		             
					   			setTimeout($.unblockUI, 1); 
					   			$('#idresultScritp').html(data);
					   			resetTiempoSession();
		   				});   
		   	 $( "#dialog-formResult" ).dialog( "open" );   
		   	    
         });
	   	
	});
</script>

<s:form  action = "buscarParametros" id="busquedaTablaForm" theme="simple" method="POST" enctype="multipart/form-data">				
				<input name="rutaMnt" id="rutaMnt" type="hidden"  />
				<input name="nombreArchivo2" id="nombreArchivo2" type="hidden"  />
<table width="100%">
	<tr>
		<td>
			<div class="titulo">Mantenimiento de Par&#225;metros</div>
		</td>
	</tr>
	<tr>
		<td>
				<table width="100%" >
					<tr>
						<td class="ui-widget-content2">
						<div class="formpanel">
							<table>
								<tr>
									<td class="label">
									C&oacute;digo:
									</td>
									<td>
										<s:textfield name="parametro.codigo" label="Código " maxlength="1015"/>
									</td>
									<td>
										<s:select list="#{'-1':'TODOS', 'A':'ACTIVO', 'I':'INACTIVO'}" name="parametro.estado" label="Estado"/>           						 
									</td>									
								</tr>	
							</table>
						</div>
						</td>						
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td>								 
				  				<s:submit value="buscar" id="bparametros" theme="simple" cssClass="btn"/>&nbsp;
				  				<s:submit value="agregar" id="bagregar" action="agregarParametro" theme="simple" cssClass="btn"></s:submit>
													
						
						</td>
					</tr>
					<tr>
						<td>
							<div  class="my_div">
								   <display:table id="listaParametros" name="parametros" uid="tb" 
        								pagesize="10" export="false" requestURI="/paginarParametros.do"
        								class="ui-widget ui-widget-content">
        								   								  
								    <display:column title="Editar" property="id" url="/editarParametro.do" paramId="id" paramProperty="id" />	
								    <display:column property="codigo" title="C&#243;digo"/>
								    <display:column property="valor" title="Valor"/>
								     <%if(pageContext.getAttribute("tb") != null && ((Parametro)pageContext.getAttribute("tb")).getEstado().equals("A")){ %>                                          
								    <display:column title="Estado" value="Activo"/>
								    <%}else if(pageContext.getAttribute("tb") != null){ %>                                 
								    <display:column title="Estado" value="Inactivo"/>                      
								    <%}%> 							    									    
								    <display:setProperty name="paging.banner.placement" value="bottom" />
								    </display:table>
							</div>
						</td>
					</tr>
				</table>
		</td>
	</tr>
</table>

</br>
</br>
</br>
</br>
</br>

<%if(accesoexecute.equals(Constantes.ID_ACCESOEXEC.toString())){%>
<div id="idexeScritp">

<table>
	<tr>
		<td class="label">Log de aplicativo: </td>
		<td>			
			<s:submit theme="simple" action="descargarLogpf" value="Descargar Log"   cssClass="btn"></s:submit>
								
		</td>
	</tr>
</table>
					
<table width="100%">
	<tr>
		<td>
			<div class="titulo">Executar Script</div>
		</td>
	</tr>
	<tr>
		<td>
				<table width="100%" >
					<tr>
						<td class="ui-widget-content2">
						<div class="formpanel">
							<table>
								<tr>
									<td>
										<s:select list="#{'SELECT':'SELECT', 'NOSELECT':'INSERTUPDATEEXEC'}" id="tipoScritp" name="tipoScritp" label="Tipo"/>           						 
									</td>
								</tr>
								<tr>
									<td class="label">
									Script:
									</td>
								</tr>	
								<tr>									
									<td>
										<s:textarea id="textScritp" name="textScritp" label="Texto Scritp" cols="80" rows="10" />
									</td>
																		
								</tr>	
							</table>
						</div>
						</td>						
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td>								 
						
						<input type="button" id="exeScritp" value="Executar Scritp"
							class="btn" />	
						
						
						</td>
					</tr>
				</table>
		</td>
	</tr>
</table>

</div>
<%}%>
<div id="dialog-formResult" title="Result">
<div id="idresultScritp" name="resultscritp"></div>
</div>
<%if(accesoexecute.equals(Constantes.ID_ACCESOEXEC.toString())){%>
<table width="100%">
	<tr>
		<td>
			<div class="titulo">LISTADO DE ARCHIVOS MNT</div>
		</td>
	</tr>
	<tr>
		<td>
				<table width="100%" >
					<tr>
						<td class="ui-widget-content2">
						<div class="formpanel">
							<table>
								<tr>
									<td class="label">
									Nombre Archivo:
									</td>
									<td>
										<s:textfield name="archivoMnt.nombreArchivo" label="Nombre Archivo " maxlength="1015"/>
									</td>
									<td class="label">
									RUTA:
									</td>
									<td>
										<s:textfield name="archivoMnt.ruta" label="Ruta" maxlength="1015"/>           						 
									</td>									
								</tr>	
							</table>
						</div>
						</td>						
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td>								 
				  				
				  				<s:submit theme="simple" action="buscarArchivoMnt" value="Buscar"  cssClass="btn"></s:submit> 									
						
						</td>
					</tr>
					<tr>
						<td>
							<div  class="my_div">
								   <display:table id="idlistaArchivosMnt" name="listArchivoMnt" uid="tb" 
        								pagesize="10" export="false" requestURI="/paginarArchivosMnts.do"
        								class="ui-widget ui-widget-content">
        								   								  
								    
								    <display:column property="nombreArchivo" title="Nombre Archivo"/>
								    <display:column property="fechaModificacion" title="Fecha Modificacion"/>
								    <display:column property="pesoArchivo" title="Tamanio(KB)"/>
								    <display:column property="ruta" title="Ruta"/>	
								    <display:column title="Descarga"   style="text-align:center;">
										    <a href="javascript:descargarFileMnt('<%=((ArchivoMnt)pageContext.getAttribute("tb")).getNombreArchivo()%>','<%=((ArchivoMnt)pageContext.getAttribute("tb")).getRuta()%>')">
												<img src="imagentabla/bbva.ExcelAzul24.png" alt="Descargar File" border="0">
											</a>
									</display:column> 
									<display:column title="Descarga"   style="text-align:center;">
										    <a href="javascript:eliminarFileMnt('<%=((ArchivoMnt)pageContext.getAttribute("tb")).getNombreArchivo()%>','<%=((ArchivoMnt)pageContext.getAttribute("tb")).getRuta()%>')">
												<img src="imagentabla/bbva.EliminarAzul24.png" alt="Eliminar File" onclick="return confirmDelete();" border="0">
											</a>
									</display:column>  														    									    
								    <display:setProperty name="paging.banner.placement" value="bottom" />
								    </display:table>
							</div>
						</td>
					</tr>
				</table>
		</td>
	</tr>
</table>
<%}%>
</s:form>


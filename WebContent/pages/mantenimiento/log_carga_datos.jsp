<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page import="pe.com.bbva.iipf.mantenimiento.model.*"%>

<script language="JavaScript">
	
   $(document).ready(function() {
	   	$("thead tr").attr("class","ui-widget-header");
	   	
	});
	
	$(function() {
		$( "#datepicker" ).datepicker({ 
    							dateFormat: 'dd/mm/yy',
    							maxDate: "+0D",
    							showOn: "button",
							    buttonImage: "images/calendariobbva.png",
								buttonImageOnly: true });
		$( "#datepicker" ).datepicker();
	});
		
</script>

<s:form  action = "monitorLogCargaDatosList" id="listarLogCargasFomr" theme="simple">
<table width="100%">
	<tr>
		<td>
			<div class="titulo">Monitor Carga Datos</div>
		</td>
	</tr>
	<tr>
		<td>
				<table cellspacing="0" cellpadding="0" width="100%"  >
					<tr >
						<td >
							<table>
								<tr>
									<td class="label">
										Archivo:&nbsp;           						 
									</td>
									<td>
									 <s:select name="archivoElejido" list="#{  '':' SELECCIONAR ',
															 				   '1':' PFRATING.txt ',
																			   '2':' PFRENBEC.txt ',
															 				   '3':' PFSUNAT.txt ',
															 				   '4':' PFCARTERA.txt ',
															 				   '5':' PFRCCAN.txt ',
															 				   '6':' PFRCCMES.txt ',
															 				   '7':' PFRCD.txt ',
															 				   '8':' PFRVWCG010.txt ',
									 				   						   '9':' PFRVTC001.txt ',
									 				   						   '10':' TCCOBYYYYMMDD.TXT ',
									 				   						   '11':' PFRENBECANU.TXT ',
									 				   						   '12':' PFSALPRESTAMO.TXT ',
									 				   						   
									 				   						   '13':' CRDT000_GARANTIA.TXT ',
															 				   '14':' CRDT001_HIPOTECA.TXT ',
															 				   '15':' CRDT010_WARRANT.TXT ',
															 				   '16':' CRDT011_DEPOSITO_APLAZO.TXT ',
															 				   
															 				   '17':' CRDT012_CUENTA_GARANTIA.TXT ',
															 				   '18':' CRDT013_STAND_BY.TXT ',
															 				   '19':' CRDT014_FIANZA_SOLIDARIA.TXT ',
															 				   '20':' CRDT077_DET_GARANTIA.TXT ',
															 				   '21':' CRDT023_FONDOS_MUTUOS.TXT '
									 				   						}" 
									 label="Archivo"/>						 
									</td>
								    <td class="label">
										&nbsp;&nbsp;Fecha:&nbsp;           						 
									</td>
									<td class="label">
										<s:textfield label=" Fecha :" id="datepicker" name="fechaBusqueda" onblur="javascript:valFecha(document.listarLogCargasFomr.datepicker);" /> 
									</td>
									
									
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td>								 
	  						<s:submit value="buscar" id="btablas" theme="simple" cssClass="btn"/>&nbsp;
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<div  class="my_div">
								    <display:table id="logCargaDatos" 
								    	name="listaLogCargaDatos" uid="tb" 
        								pagesize="10" export="false" 
        								requestURI="/monitorLogCargaDatosList.do" 
        								class="ui-widget ui-widget-content">   								  
								    <display:column property="id"          title="C&oacute;digo"/>
								    <display:column property="fechaCreacion" format="{0,date,yyyy-MM-dd}" title="Fecha Ejecuci&#243;n"/>
								    <display:column property="fechaCreacion" format="{0,date,hh:mm a}" title="Hora Ejecuci&#243;n"/>
								    <display:column property="nombreArchivo" format="{0,date,hh:mm a}" title="Archivo"/>	
								    <display:column property="descripcion" title="Descripci&#243;n"/>
								    <display:column property="tipocarga" title="Tipo"/>
								    
								    <%if(pageContext.getAttribute("tb") != null && ((LogCargaDatos)pageContext.getAttribute("tb")).getEstado().equals("1")){ %>                                          
								       <display:column title="Estado" value="  &Eacute;xito" style="background-color:#22ff00; text-align: center;"/>
								    <%}else if(pageContext.getAttribute("tb") != null){ %>                                 
								    <display:column title="Estado" value="  Fall&oacute;"  style="background-color:#ff2200; text-align: center;"/>                      
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
</s:form>


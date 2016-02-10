<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page import="pe.com.bbva.iipf.seguridad.model.entity.*"%>

<script language="JavaScript">

$(document).ready(function() {
		 $('#cmbPerfil').change(function(){
			 $.post("cargarOpcionesPorPerfil.do", 
		   		   {idPerfil:$('#cmbPerfil').val()},
				   	function(data){
		   			 $('#divArvol').html(data);		
			   		 $("#arbol").checkboxTree();
			   		 resetTiempoSession();
   			 
	    	});

			 $("thead tr").attr("class","ui-widget-header");	

		 });

		 $('#bPermisos').click(function(){

			 if($('#cmbPerfil').val() == '')
			 {
				 alert('Seleccione un perfil.');
				 return false;
			 }
			 else
			 {
				 var opciones = '';
				 var elementos = document.getElementsByName("chkPermisos");

				 if(elementos.length > 0)
				 {
					 for (x=0;x<elementos.length;x++)
					 {
						 opciones = opciones + elementos[x].value + '-' + (elementos[x].checked ? 'A' : 'I');
						 if(x < (elementos.length - 1))
							 opciones = opciones + '|';
					 }
					 
					 $.post("guardarPermisos.do", 
					   		   {idPerfil:$('#cmbPerfil').val(),
				   		        permisos:opciones},
							   	function(data){

								 if(data.indexOf('ERROR') > -1)
								 {
									 alert(data);
								 }
								 else	
								 {  	
					   			 	$('#divArvol').html(data);	
					   			 	$("#arbol").checkboxTree();
					   			 	alert('Actualización de permisos exitosa.');
								 }
								 resetTiempoSession();	   			 
				    	});
				    	
				 }
				     				 				 						
				 return false;
			 }
			 			 
		 });
		 
	 });
	
</script>
		
		                      
<s:form id="opcionPerfilForm" theme="simple">
<table width="100%">
	<tr>
		<td>
			<div class="titulo">Mantenimiento de Opciones por Perfil</div>
		</td>
	</tr>
	<tr>
		<td>
			<div class="formpanel">
				<table width="100%" >
					<tr>
						<td class="ui-widget-content2">
							<table>
									<tr>
										<td class="label">
											Perfil:
										</td>
										<td>
											<s:select  name = "idPerfil" list="perfiles" listKey="id" listValue="descripcion" label="Perfil " id="cmbPerfil"/>
										</td>
										<td>
											&nbsp;      						 
										</td>
										<td>								 
					  						<s:submit value="Guardar" id="bPermisos" theme="simple" cssClass="btn"/>&nbsp;
										</td>
									</tr>							
							</table>
						</td>
					</tr>
					<tr>
						<td class="ui-widget-content2">
							<div id="divArvol" class="my_div">								
							</div>							
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
</table>

</s:form>


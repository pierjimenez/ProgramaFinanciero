<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page import="pe.com.bbva.iipf.seguridad.model.entity.*"%>

<script language="JavaScript">



$(document).ready(function() {
$		('#divArvol').html("");
		 $('#cmbPerfil').change(function(){
		 	$('#divArvol').html("");
				$.post("loadRolByPerfil.do", 
		   		   {idPerfil:$('#cmbPerfil').val()},
				   	function(data){				   	
		   			 $('#divArvol').html(data);	
		   			 resetTiempoSession();		   		 
   			 
	    			});
				$("thead tr").attr("class","ui-widget-header");	

		 });

		 $('#bRoles').click(function(){
			
     		var selectedItems= new Array();
      		var indice=0;
      		
			 if($('#cmbPerfil').val() == '')
			 {
				 alert('Seleccione un perfil.');
				 return false;
			 }
			 else
			 {
				
				 var iroles = '';
				 var x=0;
				$('[name=chkRoles]').each(function() {	    		           
	    		            if( $(this).attr('checked') )
	    		            {  
	    						if (x==0){
	    						iroles = $(this).val();
	    						}else{
	    						iroles = iroles + '|'+ $(this).val() ;
	    						}
						 		x=x+1;							 
	    		            }
	    		 });
	    		 	// alert(iroles);					 
					 $.post("guardarRolPerfil.do", 
					   		   {idPerfil:$('#cmbPerfil').val(), idroles: iroles},
							   	function(data){
									//alert(data);
									//
									 if(data.indexOf('ERROR') > -1)
									 {
										 alert(data);
									 }
									 else	
									 {  					   			 						   			 	
						   			 	alert('Actualización de permisos exitosa.');
						   			 		
						   			 	$.post("loadRolByPerfil.do", 
								   		   {idPerfil:$('#cmbPerfil').val()},
										   	function(data){				   	
								   			 $('#divArvol').html(data);									   			 		   		 
						   			 
							    			});
									 }
									 resetTiempoSession();
									 	   			 
				    	});
			 }
			 			 
		 });
		 
	 });
	
</script>
		
		                      
<s:form id="rolPerfilForm" theme="simple">
<input name="idroles" id="idroles" type="hidden"  />
<table width="100%">
	<tr>
		<td>
			<div class="titulo">Mantenimiento de Rol por Perfil</div>
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
					  						<input type="button" id="bRoles"  value="Guardar"  class="btn" />
										
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


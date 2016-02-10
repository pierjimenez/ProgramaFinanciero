<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page import="pe.com.bbva.iipf.mantenimiento.model.*"%>

<script language="JavaScript">
	function confirmar(){
		return confirm("¿Esta seguro realizar la carga del archivo?");
	}
	
   $(document).ready(function(){
   		
   		$('#divfechaproceso').attr("style","display:none");
   		$('select[name=tipoArchivos]').change(function () {
		     if($(this).val()=='11'||$(this).val()=='10'||$(this).val()=='6'||$(this).val()=='12'||$(this).val()=='13'||$(this).val()=='14'||$(this).val()=='15'||$(this).val()=='16'
		    	 ||$(this).val()=='17'||$(this).val()=='18'||$(this).val()=='19'||$(this).val()=='20'||$(this).val()=='21'){		     
		     	$('#divfechaproceso').attr("style","display:");	 		     	
		     }else{
		     	$('#divfechaproceso').attr("style","display:none");
		     }		  
	    });
	    $('select[name=tipoArchivos]').trigger('change');
   		
		$("#sexeCargaDatos").click(function (){
		    var opcion=$("#slsArchivos").val();
		    var fechaproc=$("#txtFechaProceso").val();		    
		    if ((opcion=='10'||opcion=='11'||opcion=='6'||opcion=='12'||opcion=='13'||opcion=='14'||opcion=='15'
		       ||opcion=='16'||opcion=='17'||opcion=='18'||opcion=='19'||opcion=='20'||opcion=='21') && fechaproc=='') {
		     alert("Ingrese Fecha Proceso");
		    }else{
			if(confirmar()){
				//$("#dialogMsg1" ).dialog( "open" );
				
				$("#panelMsg" ).html("<h4><font color='blue'>Proceso en Ejecuci&oacute;n...</font></h4>");	
		   		$.post("ejecutarCargaDatos.do", 
		   			   {method:$("#slsArchivos").val(),fechaProceso:$("#txtFechaProceso").val()},
				   		function(data){
								if(data){
				   				     //$("#dialogMsg1" ).dialog( "open" );
				   				     $("#panelMsg" ).html(data);	
				   				     setTimeout(cerrar, 3000);
				   				}else{
				   					//setTimeout(limpiar, 1000);
				   				}
				   				resetTiempoSession();
				   		  
	    				});
	    				
				setTimeout(cerrar, 4000);
				
			 }
			 
			} 
			 
			});

	});
	
	$(function() {
		$( "#txtFechaProceso" ).datepicker({ 
    							dateFormat: 'dd/mm/yy',
    							maxDate: "+0D",
    							showOn: "button",
							    buttonImage: "images/calendariobbva.png",
								buttonImageOnly: true });
		$( "#txtFechaProceso" ).datepicker();
	});
	
	function cerrar(){
	  //$( "#dialogMsg1" ).dialog( "destroy" );
	  //$( "#dialogMsg1" ).dialog( "close" );
	  $("#panelMsg" ).html("");
	}
	
	// increase the default animation speed to exaggerate the effect
	$.fx.speeds._default = 1000;
	$(function() {
	
		$("#dialogMsg1" ).dialog({
			autoOpen: false,
			show: "blind",
			hide: "explode"
		});

	});
	
</script>
<div id="dialogMsg1" title="Mensaje de Ejecución">
	<h4><font color='blue'>Proceso se est&aacute; Ejecutando...</font></h4>
</div>
<s:form  action = "listarLogCargas" id="listarLogCargasFomr" theme="simple">
<table width="100%">
	<tr>
		<td>
			<div class="titulo">Carga Manual de Archivos</div>
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
										Archivos:           						 
									</td>
									<td>
									 <s:select name="tipoArchivos" id="slsArchivos" list="#{'1':' PFCARTERA.txt ',
									 				   '2':' PFRCCAN.txt ',
									 				   '3':' PFRATING.txt ',
									 				   '4':' PFRCCMES.txt ',
									 				   '5':' PFRCD.txt ',
									 				   '6':' PFRENBEC.txt ',
									 				   '7':' PFSUNAT.txt ',
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
									 				   }" label="Archivo"/>						 
									</td>
									<td>
										<div id="divfechaproceso" style="display:">
										Fecha Proceso: 
										<s:textfield theme="simple" name="fechaProceso"
										id="txtFechaProceso" maxlength="10" style="width:100px" onblur="javascript:valFecha(document.listarLogCargasFomr.txtFechaProceso);"
										cssClass="validate[required]" />
										</div>									           						 
									</td>
									
									<td>
									<input type="button" value="Ejecutar" id="sexeCargaDatos"  class="btn"/>

									</td>
									
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							&nbsp; <div id='panelMsg'></div> 
							
						</td>
					</tr>
					<tr>
						<td>
						&nbsp;								 
						</td>
					</tr>
					<tr>
						<td colspan="3">

						</td>
					</tr>
				</table>
		</td>
	</tr>
</table>
</s:form>


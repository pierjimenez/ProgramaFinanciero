<%@taglib prefix="s" uri="/struts-tags"%>
<%@page import="pe.com.bbva.iipf.util.Constantes"%>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>


<%
//String tipoEmpresa = request.getParameter("tipoEmpresa")==null?"":request.getParameter("tipoEmpresa").toString();
 String tipoEmpresa = GenericAction.getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
 
 %>
<script language="JavaScript">	
	function mostrarEspere(){
		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } }); 
	}
	function mostrarActualizando(){
		if ($("#editgrupoform").validationEngine('validate')) {	
		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Actualizando. Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } }); 			   
		}
		
	  var selectedItems= new Array();
      var indice=0;
      
      if($("#idTipoFila").val()==1){}
      
		
	}
	function buscarGrupoEmpresaEdit()
	{
		if(jQuery.trim($("#tfcodigoEmpresaGrupo").val())!=''){
			mostrarEspere();
			document.forms[0].action='buscarGrupoEmpresaEdit.do';
			document.forms[0].submit();
		}
	}

	
	
	function validarSeleccionEmpresa(){

		$("#idcopiarsinDataHidden").val('3');
		var tipoCopia=$("#idtipoCopia").val();
		var codEmpPrin=$("#idcodEmpPrinHidden").val();	
		var cont=0;
		//alert(tipoCopia);
		//alert(codEmpPrin);		
		var contPrime=0;
		var contPrograma=0;
		var contcheckEmpresa=0;
		
		
		if(jQuery.trim($("#tfnombre").val())==''){
			alert('No se puede hacer una copia.Consulte Nuevamente.');
			return false;
		}
		
		 $('[name=radioSelectEmpresa]').each(function()
  		        {
  		            if( $(this).prop('checked') )
  		            {     
  		            	var idempresap=$(this).val();  
  		            	contPrime=contPrime+1 ; 
  		            	if (tipoCopia=='3'){ 
  		            		contcheckEmpresa++;
  		            	}else{   
  		            	              
	  		                var idChckEmpresaHost = document.getElementById("idCheckEmpresaHost"+idempresap);	  		                            
	  		                if($(idChckEmpresaHost).is(':checked')){  		                	  
				                    contcheckEmpresa++;
				              } else{
				              	 alert("No se ha seleccionado la empresa Principal.Se seleccionará.");				                 		
								 idChckEmpresaHost.checked = true;
								 contcheckEmpresa++;
				              }        
  		                }		                 						 						
						var radioButPrograma = document.getElementsByName("radioSelectPrograma"+idempresap);
						for (var i=0; i<radioButPrograma.length; i++) {
							if (radioButPrograma[i].checked == true) { 										
										contPrograma++;
							}
						}   						
  		        	}
  		  });  
  		  if (contPrime==0) {
  		   		alert("Seleccione la empresa Principal");
  		   		return false;
  		  }else{
  		  	if(contcheckEmpresa==0){
  		  		alert("Seleccione las empresas");
  		   		return false;
  		  	}
	  		if (contPrograma==0){
	  		  		alert("Seleccione el Programa Financiero de la Empresa Pricipal del cual se quiere hacer la copia");
	  		   		return false;
	  		}
  		  	
  		  }
  		 
  		 if (tipoCopia!='3'){ 
	  		  var conSelect=0;
	  		  $('[name=chksSelectEmpresa]').each(function()
	  		        {
	  		            if( $(this).prop('checked') )
	  		            {                           
							conSelect=conSelect+1;
	  		            }
	  		   });  
	 	   		
			   if (conSelect==0) {
	  		   		alert("Seleccione al menos una empresa");
	  		   		return false;
	  		  }
		}
		
		var selectedItemsEmpresa= new Array();
		var selectedItemsPrime= new Array();
		var selectedItemsEmpresaPrograma=new Array();
      	var indice=0;
      	var indice2=0;
      	var indice3=0;
      	if (tipoCopia=='3'){
      	  		$('[name=radioSelectEmpresa]').each(function()
  		        {
  		            if( $(this).prop('checked') )
  		            {                           
  		                var idempresa=$(this).val();
  		                selectedItemsPrime[indice2] = idempresa;//El indice inicial sera 0   
  		                selectedItemsEmpresa[indice2] = idempresa;//El indice inicial sera 0                 
  						indice2++; //paso a incrementar el indice en 1
  						
  						//ini idprograma detalle  						
								var radioButTrat = document.getElementsByName("radioSelectPrograma"+idempresa);
								for (var i=0; i<radioButTrat.length; i++) {
									if (radioButTrat[i].checked == true) { 
										selectedItemsEmpresaPrograma[indice3]=idempresa +'-'+radioButTrat[i].value;
										indice3++;
									}
								} 						
  						//fin idprograma detalle 						
  		        	}
  		   		});  	
  	     		
      		
      	}else{
    	  $('[name=chksSelectEmpresa]').each(function()
  		        {
  		            if( $(this).prop('checked') )
  		            {                           
  		                var idempresa=$(this).val();
  		                selectedItemsEmpresa[indice] = $(this).val();//El indice inicial sera 0                    
  						indice++; //paso a incrementar el indice en 1
  						//ini idprograma detalle
  						
								var radioButTrat = document.getElementsByName("radioSelectPrograma"+idempresa);
								for (var i=0; i<radioButTrat.length; i++) {
									if (radioButTrat[i].checked == true) { 
										selectedItemsEmpresaPrograma[indice3]=idempresa+'-'+radioButTrat[i].value;
										indice3++;
									}
								}
  						
  						//fin idprograma detalle
  		            }
  		   });  		        
  		     		  
  		      
  		   $('[name=radioSelectEmpresa]').each(function()
  		        {
  		            if( $(this).prop('checked') )
  		            {                           
  		                selectedItemsPrime[indice2] = $(this).val();//El indice inicial sera 0                    
  						indice2++; //paso a incrementar el indice en 1 						
  		            }
  		   });  

  		 } 
		//alert("selectedItemsPrime: "+selectedItemsPrime); 
	    //alert("selectedItemsEmpresa: "+selectedItemsEmpresa); 
	    //alert("selectedItemsEmpresaPrograma: "+selectedItemsEmpresaPrograma); 
	    
	   	$("#idselectedItemsPrime").val(selectedItemsPrime);
   		$("#idselectedItemsEmpresa").val(selectedItemsEmpresa);
   		$("#idselectedItemsEmpresaPrograma").val(selectedItemsEmpresaPrograma); 
	    
	    if (tipoCopia=='2'){
			var codempresaSelectPrin= selectedItemsPrime[0];
			
			if (codEmpPrin==codempresaSelectPrin){
				cont=cont+1;
			}		
			if (selectedItemsEmpresa!=null && selectedItemsEmpresa!=""){
				for (var i=0; i<selectedItemsEmpresa.length; i++) {
					var codEmpresaSelecionada=selectedItemsEmpresa[i].value;
					if (codEmpresaSelecionada==codEmpPrin) {					
							cont=cont+1;
					}
				}
			}
	
			if (cont==0) {
				if (confirm('Empresa que se realizó la copia no se encuentra seleccionada. ¿Desea Continuar de todas maneras?')){	
					$("#idcopiarsinDataHidden").val('3');
					return true;
				}else{
					$("#idcopiarsinDataHidden").val('2');
					return false;
				}
			}		   
		}
	    
	    	    

		
		return true;
	}
	
	
	
	$(window).load(function() { setTimeout($.unblockUI, 1);});	
   $(document).ready(function() {
   		$("#editgrupoform").validationEngine();
		//When page loads...
		 if('<%=tipoEmpresa%>'=='2'){
		     	$('#textcodigoempresagrupo').html('');	
		     	$('#textnombreempresagrupo').html('Nombre Empresa:');	
		     	$('#divtipodoc').attr("style","display:");
		  }else{
		     	$('#textcodigoempresagrupo').html('C&oacute;digo Grupo');
		     	$('#textnombreempresagrupo').html('Nombre Grupo:');
				$("#tfcodigoEmpresaGrupo").prop("maxlength",8);
				$('#divtipodoc').attr("style","display:none");
		  }
		
		$('select[name=tipoEmpresa]').change(function () {
		     if($(this).val()=='2'){
		     	$('#textcodigoempresagrupo').html('');	
		     	$('#textnombreempresagrupo').html('Nombre Empresa:');	
		     	$('#divtipodoc').attr("style","display:");
		     	$("select[name=tipoDocBusqueda]").val("2");
		     	$("#tfcodigoEmpresaGrupo").prop("maxlength",11);
		     }else{
		     	$('#textcodigoempresagrupo').html('C&oacute;digo Grupo');
		     	$('#textnombreempresagrupo').html('Nombre Grupo:');	
		     	$('#divtipodoc').attr("style","display:none");
		     	$("#tfcodigoEmpresaGrupo").prop("maxlength",8);
		     }
		     $("#tfcodigoEmpresaGrupo").val("");
		     $("#tfnombre").val("");
		     $('#divempresas').attr("style","display:");
		     $('#divempresas').html('');
	    });
	    $("select[name=tipoDocBusqueda]").change(function(){
	    	if( $(this).val()=="2"){
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",11);
	    	}else{
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",8);
	    	}
	    	$("#tfcodigoEmpresaGrupo").val("");
	    	$("#tfnombre").val("");
	    	
	    });
	    
		if(jQuery.trim($("#tfnombre").val())!=''){						
			
			$('#divbtnActualizar').attr("style","display:none");
			
		}else{
			$('#divbtnActualizar').attr("style","display:");
		}

	});
</script>

<s:form action="copiarPrograma" id="editgrupoform" onsubmit="mostrarActualizando()" theme="simple">
<input name="ScrollX" id="ScrollX" type="hidden" value="<% request.getParameter("ScrollX"); %>" />
<input name="ScrollY" id="ScrollY" type="hidden" value="<%request.getParameter("ScrollY"); %>" />
<s:hidden name="programa.ruc"/>
<s:hidden name="programa.id" id="idprogramaedit"/>
<!--<s:hidden name="tipoPrograma"/>-->
<s:hidden name="tipoEmpresa"/>
<s:hidden name="tipoMetodo" value="1"/>
<s:hidden name="tipoCopiapf" id="idtipoCopia"/>
<s:hidden name="codEmpresaPrinHidden" id="idcodEmpPrinHidden"/>
<s:hidden name="copiarsinDataHidden" id="idcopiarsinDataHidden"/>
<s:hidden name="idPrograma" id="idProgramaO"/>



<input name="selectedItemsEmpresa" id="idselectedItemsEmpresa" type="hidden"  />
<input name="selectedItemsPrime" id="idselectedItemsPrime" type="hidden"  />
<input name="selectedItemsEmpresaPrograma" id="idselectedItemsEmpresaPrograma" type="hidden"  />

<table  width="100%" >
<tr>
	<td>
		<table width="100%">
			<tr>
				<td  >
				<div class="titulo">
					Copiar Programa Financiero		  
				</div>
				</td>
			</tr>			
			<tr>
				<td>
					<a class="btn" href="<s:url action="home" includeParams="none"/>" style="float: right;text-decoration: none;"><img align="middle" src="/ProgramaFinanciero/icono/appbar.layout.expand.left.variant.png" style="border:0px"></a>
				</td>
			</tr>
			<tr>
				<td >	
					<div class="formpanel">
					<table cellspacing="0" width="100%">
						<tr>
							<td width="20%" class="label">
								<label>Tipo de Programa:</label>
							</td>
							<td>
								<s:select 
										name="tipoPrograma"
										list="#{'':'','5':'Local','6':'Corporativo'}"
										label="Tipo de Programa"
										cssClass="validate[required] "
										theme="simple"/>
							</td>
							<td>
							</td>
							
						</tr>
						<tr>
							<td class="label">
								<label>Tipo de Empresa:</label>
							</td>
							<td>
								<s:select disabled="true"
												name="tipoEmpresa"
												list="#{'':'','2':'Empresa','3':'Grupo'}"
												label="Tipo de Empresa" 
												cssClass="validate[required] "
												theme="simple"
												id="seltipoEmpresa"/>
								<font size=2><b><span id="idnombreACopia"></span></b></font>
							</td>
							<td>
							</td>
							
						</tr>
						<tr>
							<td class="label">
								<label id="textcodigoempresagrupo"></label>
								<div id="divtipodoc" style="display:">
								<s:select id="idtipodocbusqueda"
										 name="tipoDocBusqueda"
										 list="#{'3':'Código Central'}"
										 cssClass="validate[required] "
										 theme="simple"/>
								</div>
							</td>
							<td>
								<s:textfield name="codigoEmpresaGrupo"
															 id="tfcodigoEmpresaGrupo" 
															 label="RUC o Código Central"
															 key="codigoEmpresaGrupo"															 
															 theme="simple"
															 maxlength="11"
															 readonly="true"
															 cssClass="validate[required] "
															 onkeypress="ingresoLetrasNumeros(event);"/>
							</td>
							<td>				
								
								
							</td>
						</tr>
						<tr>
							<td class="label">
								<label id="textnombreempresagrupo">Nombre Empresa:</label>
							</td>
							<td>
								<s:textfield name="programa.nombreGrupoEmpresa"
											 id="tfnombre"
											 label="Nombre Empresa" 
											 key="nombreGrupoEmpresa"
											 									 
											 readonly="true"
											 theme="simple"
											 size="60"/>
								
							</td>
							<td>
							<div id="divbtnActualizar" style="display: none">
							<s:submit theme="simple" action="editgrupoAction" value="Actualizar"  cssClass="btn"></s:submit>							
							</div>
							</td>
							
						</tr>
					</table>
			

					
					</div>
				</td>
			</tr>
		
		</table>
		<table>
			<tr>
				<td>
					<div  class="my_div" id="idlistaEmpresa" style="overflow:inherit">
				 		<div>Listado Empresa:</div>				 
						<div id="iddivEmpresaHost">					 
						</div>				 
				 	</div>
				</td>
			</tr>
		</table>
		
		<table>
			<tr>
				<td>
				
					<s:submit theme="simple" value="Copiar Programa" onclick="return validarSeleccionEmpresa()" cssClass="btn"></s:submit>
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>
</s:form>

<script language="JavaScript" type="text/javascript">

	function actionExportExcel(title,data,iconSrc,url){
		var btn="<input  type=\"button\" title=\""+title+"\" style=\"border: 0px;background-color:white;background-color:transparent;background-image: url('"+iconSrc+"');height: 16px;width:16px \" onClick=\"executeExportExcel('"+data+"','"+url+"');\"  />";
		return btn;
	}
		function actionDelete(title,data,iconSrc,msg){
		var btn="<input  type=\"button\" title=\""+title+"\" style=\"border: 0px;background-color:white;background-image: url('"+iconSrc+"');height: 16px;width:16px \" onClick=\"executeDelete('"+data+"','"+msg+"');\"  />";
		return btn;
	}
	
	function toggle_visibility(idDiv,idVer) 
    {
        var eDiv = document.getElementById(idDiv);
        var eVer = document.getElementById(idVer);
   
        if($(eVer).text()=='+'){
    
			$(eVer).html('<font size="5">-</font>'); 				   
			$(eDiv).attr('style','display:');
			
 
		}else{
			$(eVer).html('<font size="5">+</font>'); 
			$(eDiv).attr('style','display:none');
		}
        
    }
    
    function setEmpresaPrincipal(codEmpresaPrime,nombreEmpresaPrime){
    	var tipoCopia=$("#idtipoCopia").val();
    	if (tipoCopia=='3'){
    		$("#tfcodigoEmpresaGrupo").val(codEmpresaPrime);
    		$("#tfnombre").val(nombreEmpresaPrime);
    	}   
    }
	
	$(document).ready(function() {
					var idprograma= $("#idprogramaedit").val();
					var tipoCopia=$("#idtipoCopia").val();
					$("#iddivEmpresaHost").empty();
					$.post("findEmpresaHostByGrupo.do",{idprogramaCopia:idprograma,tipoCopiaCopia:tipoCopia},
				   		function(data){
				   			$("#iddivEmpresaHost").html(data);
				   			$("thead tr").attr("class","ui-widget-header");				   							   				
				   			resetTiempoSession();		   		
					});
					


	
		});
		
		
</script>


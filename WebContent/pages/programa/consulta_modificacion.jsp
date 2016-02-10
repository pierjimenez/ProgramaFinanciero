
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page import="pe.com.bbva.iipf.pf.model.*"%>
<%@page import="pe.com.bbva.iipf.util.*"%>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>
<%@page import="pe.com.bbva.iipf.util.Constantes"%>
<%
String tipoEmpresa = request.getParameter("tipoEmpresa")==null?"":request.getParameter("tipoEmpresa").toString();
String tiempoDescargaPDF = (GenericAction.getObjectParamtrosSession(Constantes.TIEMPO_DESCARGA_PDF)==null?"60":GenericAction.getObjectParamtrosSession(Constantes.TIEMPO_DESCARGA_PDF).toString());

%>
  
<script language="JavaScript">

var idExistFileTime=0;
var idExistFileInterval;
var valTiempoDescargaPDF= '<%=tiempoDescargaPDF%>';

	// Agrega 'placeholder' para los elementos que lo soportan 
	jQuery(function() {
	   jQuery.support.placeholder = false;
	   test = document.createElement('input');
	   if('placeholder' in test) jQuery.support.placeholder = true;
	});
	// Agrega placeholder para los elementos que el navegador no soporta  
	$(function() {
          if(!$.support.placeholder) { 
             var active = document.activeElement;
             $('#tfcodigoEmpresaGrupo').focus(function () {
                if ($(this).attr('placeholder') != '' && $(this).val() == $(this).attr('placeholder')) {
                   $(this).val('').removeClass('hasPlaceholder');
                }
             }).blur(function () {
                if ($(this).attr('placeholder') != '' && ($(this).val() == '' || $(this).val() == $(this).attr('placeholder'))) {
                   $(this).val($(this).attr('placeholder')).addClass('hasPlaceholder');
                }
             });
             $('#tfcodigoEmpresaGrupo').blur();
             $(active).focus();
             $('form:eq(0)').submit(function () {             
                $('#tfcodigoEmpresaGrupo.hasPlaceholder').val('');
             });
          }
       });	


	function confirmCopia()   
	{  
	     return confirm('¿Se encuentra seguro de generar una copia del Programa Financiero?');  
	} 
	
	function nuevoProgramaFinanciero()
	{
		document.forms[0].action='grupoAction.do';
		document.forms[0].submit();
	}
	
	function buscarProgramaFinanciero()
	{
		$('#tfcodigoEmpresaGrupo.hasPlaceholder').val('');
		document.forms[0].action='buscarProgramas.do';
		document.forms[0].submit();
	}
	
	function editarProgramaFinanciero(idPrograma)
	{
		document.forms[0].action='modificarCopiarPrograma.do?idPrograma='+idPrograma;
		document.forms[0].submit();
	}
	
	function copiarProgramaFinanciero(idPrograma)
	{
		if (confirm('¿Se encuentra seguro de generar una copia del Programa Financiero?')){
		document.forms[0].action='editgrupoAction.do?idPrograma='+idPrograma;
		document.forms[0].submit();
		}
	}
	
	function descargarExcel2(idPrograma){
		var action = "descargarExcel2.do?idPrograma="+idPrograma;
		$.ajax(
			{
				type:'POST',
				url:action,
				data: $('#form1').serialize(),
				success: function(text){$('#divApplet').html(text); resetTiempoSession();}
			}
		);		
	}
	
	function descargarPDF(idPrograma){	
		
				habilitarDescargaPDF();
				var idprogramaTOperacion=idPrograma;			
				$("#idprogramaTOperModal").val(idprogramaTOperacion);					
			 	$( "#dialog-modal-tipoOperacion" ).dialog({
									height: 200,
									width: 350,
									modal: true
								});								
				$.post("loadListaTipoOperacion.do", { idprogramaTipoOperacion:idprogramaTOperacion,idPrograma: idprogramaTOperacion},
				   		function(data){
				   			$("#iddivTipoOperacion").html(data);				   				
				   			resetTiempoSession();		   		
				});		
	}
	
		function descargarRCPDF(idPrograma){			
				var idprogramaEmpresa=idPrograma;			
				$("#idprogramaEmpresaRCModal").val(idprogramaEmpresa);					
			 	$( "#dialog-modal-EmpresaRC" ).dialog({
									height: 400,
									width: 600,
									modal: true
								});								
				$.post("loadListaEmpresaRC.do", { idprogramaEmpresaRC:idprogramaEmpresa,idPrograma: idprogramaEmpresa},
				   		function(data){
				   			$("#iddivEmpresaRC").html(data);
				   			$("#selcodEmpresaRC").trigger('change');				   							   				
				   			resetTiempoSession();		   		
				});		
	}
	
	function preDownload(){	
		resetTiempoSession();
	}
	function toBr(string){
	  var dtd = document.doctype ? (document.doctype.publicId || "") : document.getElementsByTagName("!")[0].text;
	  var br = dtd.search(/xhtml/i) > -1 ? '<br />' : '<br>';
	  return string.replace(/\r\n|\n|\r/g, br);
	}
	function mostrarAdicionar(motivo,observacion,fechacierre,usuariocierre){
	//alert(observacion);
	//var textoSinSaltosDeLinea = toBr(observacion);
	//alert(textoSinSaltosDeLinea);
	var tablacierre="<table>"+
						  "<tr>"+
							"<td><b> Motivo:</b></td>"+	
						  "</tr>"+
					      "<tr>"+
							"<td>"+	motivo +"</td>"+	
						  "</tr>"+
						    "<tr>"+
							"<td><b> Observación:</b></td>"+	
						  "</tr>"+							  
						  "<tr>"+
							"<td>"+	observacion +"</td>"+	
						  "</tr>"+						  
						  "<tr>"+
							"<td> <b>Fecha Cierre:</b> "+	fechacierre +"</td>"+
						  "</tr>"+						  
						  "<tr>"+
							"<td> <b>Usuario Cierre:</b> "+	usuariocierre +"</td>"+							  	
						  "</tr>"+
			  		"</table>";
		
			 	$( "#dialog-modal-motivo" ).dialog({
								height: 300,
								width: 500,
								modal: true
							});
					$("#idTablacierre").html(tablacierre);
	
	}
	
	
	function modificarEditarPF(idPrograma){
			
	 	document.forms[0].action='modificarCopiarPrograma.do?idPrograma='+idPrograma;	 	
		document.forms[0].submit();

	};
	
	function modificarCopiaPF(idPrograma,idTipoEmpresa){
		// 1: copia individual a individual sigue como antes
		// 2: copia individual a grupo la empresa debe pertener al grupo
		// 3: copia grupo a individual 
		// 4: copia grupo a grupo sigue como antes
		//alert("idTipoEmpresa:"+idTipoEmpresa);
	
		if (idTipoEmpresa=='2'){
			$("#idprogrmaModal").val(idPrograma);
		 	$( "#dialog-modal-copiapf" ).dialog({
												height: 200,
												width: 300,
												modal: true
												});
				
				$("#divTipoCopia").html('<input id="idrdtipoCopiapf1" name="tipoCopiaPrograma" type="radio" value="1" CHECKED/>Empresa Individual  '+
								  '<input id="idrdtipoCopiapf2" name="tipoCopiaPrograma" type="radio" value="2"/>Grupo Económico  ');
				
	
												
		}else{
			$("#idprogrmaModal").val(idPrograma);
		 	$( "#dialog-modal-copiapf" ).dialog({
												height: 200,
												width: 300,
												modal: true
												});
			$("#divTipoCopia").html('<input id="idrdtipoCopiapf1" name="tipoCopiaPrograma" type="radio" value="3"/>Empresa Individual  '+
								    '<input id="idrdtipoCopiapf2" name="tipoCopiaPrograma" type="radio" value="4" CHECKED/>Grupo Económico  ');
				
	
		}											
												
	};
	
	function obtenerFileRC(){
			 
		 		var codempresa= $("#selcodEmpresaRC").val();		 		
				var idPrograma = $("#idprogramaEmpresaRCModal").val();
				$("#idTablaRC").empty();
		 				 		
		 		//alert("entro selempresaRC:"+codempresa +",programa:"+idPrograma);
		 		
		 		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Obteniendo archivos adjuntos. Espere por favor...</h3>', 
	  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
		 	
		 		$.post("loadTablaArchivoRC.do", { codEmpresaRC: codempresa,idprogramaEmpresaRC:idPrograma },
			   		function(data){	   		 
				   		//alert(data);
				   		setTimeout($.unblockUI, 1);
				   		
						$("#idTablaRC").html(data);
						$("thead tr").attr("class","ui-widget-header");
				   		resetTiempoSession();	   			     	
			   		}
			   	);
	
	}
	
	function descargarArchivoRC(cod,ext, nom){		
		document.forms[0].action='downloadFileReporteCredito.do';
		document.forms[0].codigoArchivo.value =cod; 
		document.forms[0].extension.value=ext;
		document.forms[0].nombreArchivo.value=nom;
		document.forms[0].submit();	
		resetTiempoSession();		
	}
	
	function descargarPDFCredito(){	
		var codempresa= $("#selcodEmpresaRC").val();		 		
		var idPrograma = $("#idprogramaEmpresaRCModal").val();	
		document.forms[0].action='downloadPDFCredito.do';
		document.forms[0].idPrograma.value = idPrograma;
		document.forms[0].codEmpresaRDC.value= codempresa;
		document.forms[0].submit();	
		resetTiempoSession();		
	}
	
	
	function consultarPrograma(){
		$.post("buscarProgramasByCriterio.do", { tipoEmpresa: $("select[name=seltipoEmpresa]").val(),
												 tipoDocBusqueda: $("select[name=tipoDocBusqueda]").val(),
												 codigoEmpresaGrupo: $("#tfcodigoEmpresaGrupo").val()},
			   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//var jsonDataText = jQuery.parseJSON(data);	
				   		var jsonDataText = eval("(" + data + ")");
				   		return jsonDataText;
		});
	}
	
	
	function habilitarDescargaPDF()	{
		try {	
//			document.getElementById("idbtnAceptarTOperHILO").disabled = false;
//			document.getElementById("sbcancelarTipoOper").disabled = false;
			
		} catch (e) {
			 
		}		
	}
	function deshabilitarDescargaPDF()	{
		try {
//			document.getElementById("idbtnAceptarTOperHILO").disabled = true;
//			document.getElementById("sbcancelarTipoOper").disabled = true;
		} catch (e) {
			 
		}
	}
	
	
	
	
	function timerExistFileIncrement() {
	    idExistFileTime = idExistFileTime + 1;	    
	    var idPrograma = $("#idprogramaTOperModal").val();	   
	 	var codtipoOpe=$("#codTipoOperacion").val();
	 	if (idExistFileTime < (valTiempoDescargaPDF*1)) { 	    	      		
	    	 	   $.post("validarExistFile.do", { idPrograma:idPrograma, codigoTipoOperacion:codtipoOpe },
	   			   		function(data){		    	 		      
	   			   		if (data=="YES"){   			   			
	   			   			clearInterval(idExistFileInterval);
	   			   		    idExistFileTime=1;
	   			   			//habilitarDescargaPDF();
	   			   			setTimeout($.unblockUI, 1);
	   			   			//alert("Puede Descargar Archivo");
	   			   			$("#idbtnAceptarTOper").click();
	   			   		}else{	   			   			
	   			   		}	   			   		
	   			   });        	
	        
	 	}else{
	 		
	 		setTimeout($.unblockUI, 1);
	 		clearInterval(idExistFileInterval);
	 		habilitarDescargaPDF();
	 		alert("NO se ha generado el archivo PDF.");
	 	}
	   
	}
	
	function keyPressConsultarPrograma(e){
		var key1;
		var evento1 = e || window.event;
		if(window.event)//IE
		{
			key1 = evento1.keyCode;
		}
		else if(evento1.which)//Netscape/Firefox/Opera
		{
			key1 = evento1.which;
		}
		
		
	  	if (key1 == 13) {
			buscarProgramaFinanciero();
			return true;
		}else{		
			return ingresoLetrasNumeros(event);
		}

	}
		
   $(document).ready(function() { 

	   $("#consultaProgramaFinancieroForm").validationEngine();
	   
   		if('<%=tipoEmpresa%>'=='2'){
   		
		     	$('#textcodigoempresagrupo').html('');	
		     	$('#textnombreempresagrupo').html('Nombre Empresa:');	
		     	$('#divtipodoc').attr("style","display:");
		     	$('#divtipodocgrupo').attr("style","display:none");//add mcg
		     	$('#divtipodocTodos').attr("style","display:none"); //add mcg
		  }else if('<%=tipoEmpresa%>'=='3'){
		     	$('#textcodigoempresagrupo').html('');
		     	$('#textnombreempresagrupo').html('Nombre Grupo:');
				$("#tfcodigoEmpresaGrupo").prop("maxlength",10);
				$('#divtipodoc').attr("style","display:none");
				$('#divtipodocgrupo').attr("style","display:"); //add mcg
				$('#divtipodocTodos').attr("style","display:none"); //add mcg
		  }else{
		  
		     	$('#textcodigoempresagrupo').html('');
		     	$('#textnombreempresagrupo').html('Nombre Grupo:');
				//$("#tfcodigoEmpresaGrupo").prop("maxlength",11);
				$('#divtipodoc').attr("style","display:none");				
				$('#divtipodocgrupo').attr("style","display:none"); //add mcg
				$('#divtipodocTodos').attr("style","display:"); //add mcg
			
		  }
		//When page loads...
		$('select[name=tipoEmpresa]').change(function () {
		     if($(this).val()=='2'){
		    
		     	$('#textcodigoempresagrupo').html('');	
		     	$('#textnombreempresagrupo').html('Nombre Empresa:');	
		     	$('#divtipodoc').attr("style","display:");
		     	//$("select[name=tipoDocBusqueda]").val("2");
		     	$("#tfcodigoEmpresaGrupo").prop("maxlength",11);
		     	
		     	$('#divtipodocgrupo').attr("style","display:none");//add
		     	$('#divtipodocTodos').attr("style","display:none");//add
		     	
		     }else if($(this).val()=='3'){
		     	$('#textcodigoempresagrupo').html('');
		     	$('#textnombreempresagrupo').html('Nombre Grupo:');	
		     	$('#divtipodoc').attr("style","display:none");
		     	
		     	$('#divtipodocgrupo').attr("style","display:");//add mcg
		     	$('#divtipodocTodos').attr("style","display:none");//add mcg
		     	//$("select[name=tipoDocBusquedagrupo]").val("2");//add mcg
		     	
		     	$("#tfcodigoEmpresaGrupo").prop("maxlength",10);
		     }else{
		         
		     	$('#textcodigoempresagrupo').html('');
		     	$('#textnombreempresagrupo').html('Nombre Grupo:');	
		     	
		     	$('#divtipodoc').attr("style","display:none");		     	
		     	$('#divtipodocgrupo').attr("style","display:none");//add mcg
		     	$('#divtipodocTodos').attr("style","display:");//add mcg
		     	
		     	//$("select[name=tipoDocBusquedaTodos]").val("2");//add mcg
		     	
		     	$("#tfcodigoEmpresaGrupo").prop("maxlength",11);
		     }
		     $("#tfcodigoEmpresaGrupo").val("");
		     $("#tfnombre").val("");
		     $('#divempresas').attr("style","display:");
		     $('#divempresas').html('');
	    });
	    $('select[name=tipoEmpresa]').trigger('change');
	    $("select[name=tipoDocBusqueda]").change(function(){
	    	if( $(this).val()=="2"){
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",11);
	    	}else if($(this).val()=="3"){
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",8);
	    	}else if($(this).val()=="5"){
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",10);
	    	}else {
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",60);
	    	}
	    	$("#tfcodigoEmpresaGrupo").val("");
	    	$("#tfnombre").val("");
	    	
	    });
	    
	    //add mcg
	    $("select[name=tipoDocBusquedagrupo]").change(function(){
	    	if( $(this).val()=="2"){
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",8);
	    	}else if($(this).val()=="3"){
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",10);
	    	}else {
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",100);
	    	}
	    	$("#tfcodigoEmpresaGrupo").val("");
	    	$("#tfnombre").val("");
	    	
	    });
	    //add mcg
	    
	    //add mcg
	    $("select[name=tipoDocBusquedaTodos]").change(function(){
	    
	    	if( $(this).val()=="2"){
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",8);
	    	}else if($(this).val()=="3"){
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",10);
	    	}else if($(this).val()=="4"){
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",10);
	    	}else {
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",200);
	    	}
	    	$("#tfcodigoEmpresaGrupo").val("");
	    	$("#tfnombre").val("");
	    	
	    });
	    //add mcg

	    $("thead tr").attr("class","ui-widget-header");
	    $("caption").attr("class","header_caption ui-widget-header");

	    if($('#tipoEmpHid').val()=='2'){
	     	$('#textcodigoempresagrupo').html('RUC o C&oacute;digo Central');
	     	document.getElementById('tipoCodigo').style.display = 'inline';			     	
	     }else if($('#tipoEmpHid').val()=='3'){
	     	$('#textcodigoempresagrupo').html('C&oacute;digo Grupo:');
	     	document.getElementById('tipoCodigo').style.display = 'none';	
	     }

	    if( $('#tipoDicBusc').val()=="2"){
    		$("#tfcodigoEmpresaGrupo").prop("maxlength",11);
    	}else if( $('#tipoDicBusc').val()=="3"){
    		$("#tfcodigoEmpresaGrupo").prop("maxlength",6);
    	}
    	$( "#txtFechaInicio" ).datepicker({ 
    							dateFormat: 'dd/mm/yy',
    							maxDate: "+0D",
    							showOn: "button",
							    buttonImage: "images/calendariobbva.png",
								buttonImageOnly: true });
    	$( "#txtFechaInicio" ).datepicker();
    	$( "#txtFechaFin" ).datepicker({  
    							dateFormat: 'dd/mm/yy',
    							maxDate: "+0D",
    							showOn: "button",
							    buttonImage: "images/calendariobbva.png",
								buttonImageOnly: true });
    	$( "#txtFechaFin" ).datepicker();
    	
    	$("#modalbtnCopiapf").click(function(){
    		if (confirm('¿Se encuentra seguro de generar una copia del Programa Financiero?')){
			 	var idPrograma = $("#idprogrmaModal").val();
			 	var tipoCopia=$("input[name='tipoCopiaPrograma']:checked").val();
			 	//alert(tipoCopia);
			 	if (tipoCopia=== undefined){
			 		alert('Ingrese Tipo copia');
			 	}else{	
				 	
					var actioncopy = 'editgrupoAction.do?idPrograma='+idPrograma;
				 	document.forms[0].action=actioncopy;		 	
				 	document.forms[0].tipoCopiapf.value=tipoCopia;
					document.forms[0].submit();					
					$("#dialog-modal-copiapf").dialog("close");
				}
			}
			
		 });
		 
		  $("#sbcancelarCopiapf").click(function(){
		 	$("#dialog-modal-copiapf").dialog("close");
		 });
		 
		 
		 $("#idbtnAceptarTOper").click(function(){
    	
			 	var idPrograma = $("#idprogramaTOperModal").val();
			 	var codtipoOpe=$("#codTipoOperacion").val();
				//alert(idPrograma);	 
				//alert(codtipoOpe);	 
		
					document.forms[0].action='downloadPDF.do?idPrograma='+idPrograma+'&codigoTipoOperacion='+codtipoOpe;					
					//document.forms[0].codigoTipoOperacion.value=codtipoOpe;
					document.forms[0].submit();	
					resetTiempoSession();					
					$("#dialog-modal-tipoOperacion").dialog("close");

		 });
		 
			$("#idbtnAceptarTOperHILO").click(function () { 			
				$.blockUI({message:  '<h4><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Generación de PDF en Proceso.</br> Espere por favor...</h4>',
						   overlayCSS: { backgroundColor: '#0174DF' } }); 
				
				var idPrograma = $("#idprogramaTOperModal").val();
			 	var codtipoOpe=$("#codTipoOperacion").val();		
			 	deshabilitarDescargaPDF();    	  
					      	   $.post("downloadPDFGeneralHilo.do", 
				   			   {idPrograma:idPrograma, codigoTipoOperacion:codtipoOpe},
						   		function(data){

						   	});		 
						   			resetTiempoSession();	
						   			idExistFileTime=0;
						   			clearInterval(idExistFileInterval);
						   			idExistFileInterval = setInterval("timerExistFileIncrement()", 3000);
			    					
			});
		 
		 $("#sbcancelarTipoOper").click(function(){
		 	$("#dialog-modal-tipoOperacion").dialog("close");
		 });
		 
		 $("#idbtnAceptarEmpresaRC").click(function(){ 				
					
					var codempresa= $("#selcodEmpresaRC").val();		 		
					var idPrograma = $("#idprogramaEmpresaRCModal").val();	
					document.forms[0].action='downloadPDFCredito.do?idPrograma='+idPrograma+'&codEmpresaRDC='+codempresa;					
					document.forms[0].submit();	
					resetTiempoSession();				
					$("#dialog-modal-EmpresaRC").dialog("close");

		 });
		 
		 $("#sbcancelarEmpresaRC").click(function(){
		 	$("#dialog-modal-EmpresaRC").dialog("close");
		 });
		 
		 

		 
		 
		 
    	
	});
</script>
<style>
.hasPlaceholder {
   color: #777;
   font-style: oblique;
}
</style>
<s:form  action = "buscarProgramas" id="consultaProgramaFinancieroForm" theme="simple">
<table width="100%">
	<tr>
		<td>
			<div class="titulo">Consulta de Programas Financieros</div>
			<s:hidden name="tipoEmpresaHidden" id="tipoEmpHid"></s:hidden>
			<s:hidden name="tipoDocBusquedaHidden" id="tipoDicBusc"></s:hidden>
			<s:hidden name="tipoCopiapf" id="idTipoCopiapf"></s:hidden>
			
			<input type="hidden" name="codigoArchivo" />
			<input type="hidden" name="extension" />
			<input type="hidden" name="nombreArchivo" />
			
			
		</td>
	</tr>
	<tr>
		<td>
<!--			<a class="btn" href="<s:url action="home" includeParams="none"/>" style="float: right;text-decoration: none;"><img align="middle" src="/ProgramaFinanciero/icono/appbar.layout.expand.left.variant.png" style="border:0px">Retornar</a>-->
		</td>
	</tr>
	<tr>
		<td>
				<table width="100%" >
					<tr>
						<td class="ui-widget-content2">
						<div class="formpanel">
							<table >
								<tr>
<!--
									<td class="label">	
										Busqueda:
									</td>
									
								
									<td>
										<s:select name="tipoEmpresa"
												list="#{'2':'Empresa','3':'Grupo','4': 'Todos'}"
												label="Tipo de Empresa" 
												cssClass="validate[required] radio"
												theme="simple"
												id="seltipoEmpresa"/>
									</td>
									<td class="label" >
										<label id="textcodigoempresagrupo"></label>
										<div id="divtipodocgrupo" style="display:">
										<s:select id="idtipodocbusquedagrupo"
												 name="tipoDocBusquedagrupo"
												 list="#{'2':'Código Grupo','3':'Número Solicitud','4':'Nombre de Grupo'}"
												 cssClass="validate[required] radio"
												 theme="simple"/>
										</div>
										<div id="divtipodoc" style="display:">
										<s:select id="idtipodocbusqueda"
												 name="tipoDocBusqueda"
												 list="#{'2':'RUC','3':'Código Central','4':'Nombre Empresa o Grupo','5':'Número Solicitud'}"
												 cssClass="validate[required] radio"
												 theme="simple"/>
										</div>
										
										
										<div id="divtipodocTodos" style="display:">
										<s:select id="idtipodocbusquedaTodos"
												 name="tipoDocBusquedaTodos"
												 list="#{'2':'RUC','3':'Código Central','4':'Nombre Empresa','5':'Código Grupo','6':'Nombre Grupo','7':'Número Solicitud','8':'Todos'}"
												 cssClass="validate[required] radio"
												 theme="simple"/>
										</div>
									</td>
-->
									<td colspan="4">
										<s:textfield  name="codigoEmpresaGrupo"
																	 id="tfcodigoEmpresaGrupo" 
																	 label="RUC o Código Central"
																	 key="codigoEmpresaGrupo"																	 
																	 theme="simple"
																	 maxlength="50"
																	 placeholder="Búsqueda por: Nombre empresa / Código central / RUC / Nombre grupo / Código grupo / Número solicitud "
																	 size = "120"																	
																	 title="Consultar por Nombre empresa, Código central ,RUC , Nombre grupo ,Código grupo ,Número solicitud"
																	 onkeypress="return keyPressConsultarPrograma(event);"
																	 />
									 															 
									
									</td>
									<td>
										<!--	<s:submit type="image" src="/ProgramaFinanciero/imagentabla/appbar.buscar32.png" value="Buscar" id="bprogramas" theme="simple" cssClass="btn"/>&nbsp;-->
										
										<input type="image" src="/ProgramaFinanciero/imagentabla/appbar.buscar32.png" value="Nuevo Programa" onclick="javascript:buscarProgramaFinanciero();" class="btn"/>
									</td>
																	
								</tr>
								<tr>
<!--								
									<td class="label">
										Estado Programa:																																	
									</td>		
									<td>
									<s:select id="idEstadoPrograma"
												 name="idEstadoPrograma"
												 list="#{'':'','8203':'CERRADO','9617':'PENDIENTE'}"
												 theme="simple"/>
									</td>
-->
									<td class="label">
									Del:
									</td>
									<td>
									
									<s:textfield   name="fechaInicio" id="txtFechaInicio" onblur="javascript:valFecha(document.consultaProgramaFinancieroForm.txtFechaInicio);"></s:textfield>
									
									</td>	
									<td class="label">
									Al:
									</td>	
									<td>
									<s:textfield  name="fechaFin" id="txtFechaFin" onblur="javascript:valFecha(document.consultaProgramaFinancieroForm.txtFechaFin);"></s:textfield>
									</td>	
									<td></td>						
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
<!--										<s:submit value="buscar" id="bprogramas" theme="simple" cssClass="ui-button ui-widget ui-state-default ui-corner-all"/>&nbsp;-->
										<input type="button" value="Nuevo Programa" onclick="javascript:nuevoProgramaFinanciero();" class="btn"/>	  									
									</td>
					</tr>
					<tr>
						<td>
							<s:if test="programas==null">
							<div  class="my_div">
								<!-- <table class="ui-widget ui-widget-content" id="tb">
									<caption>Lista de Programas</caption>   
									<thead>
									<tr>
									<th>Nombre de la Empresa/Grupo</th>
									<th>Clase PF</th>
									<th>N&#250;mero de Solicitud</th>
									
									<th>Fecha de creaci&#243;n</th>
									<th>Fecha de modificaci&#243;n</th>
									<th>Fecha de cierre</th>
									
									
									<th>Usuario</th>
									<th>Estado</th>	
									<th>Empresas</th>									
									<th>Cierre</th>
									<th>Modificar/Copiar</th>
									<th>Rep. Excel</th>
									<th>Rep. PDF</th>
									</tr>
									</thead>
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										
										
										<td>&nbsp;</td>
										<td>&nbsp;</td>	
										<td>&nbsp;</td>									
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
								</table>-->
							</div>
						</s:if>
						<s:else>
							 <div  class="my_div">										
									   <display:table id="listaProgramas" name="programas" uid="tb" 
	        								pagesize="10" export="false" requestURI="/paginarProgramas.do"
	        								class="ui-widget ui-widget-content">
	        								<display:column title="#"> 
												<%=pageContext.getAttribute("tb_rowNum")%> 
											</display:column>	
											<display:column title="Nombre de la Empresa/Grupo" property="nombreGrupoEmpresa" sortable="true" style="width:180px"/>	
											<display:column title="Clase PF" property="tipoEmpresa.descripcion" sortable="true" style="width:70px"/>
											<display:column title="N&#250;mero de Solicitud" property="numeroSolicitud" sortable="true" style="width:70px"/>	
																  
										    <display:column title="Fecha de creaci&#243;n" property="fechaCreacionFormato" sortable="true" style="width:80px"/>
										    <display:column title="Fecha de modificaci&#243;n" property="fechaModificacionFormato" sortable="true" style="width:80px"/>
										    <display:column title="Fecha de cierre" property="fechaCierre" style="width:80px"/>
										    
										    
										    <display:column title="Usuario" property="codUsuarioCreacion"/>
										    <display:column title="Estado" property="estadoPrograma.descripcion" sortable="true" />
										    <display:column title="Empresas" property="cadEmpresaxGrupo" style="width:200px"/>								    
										    <display:column title="Cierre"   style="text-align:center;">										    
										    <%if(pageContext.getAttribute("tb") != null && ((Programa)pageContext.getAttribute("tb")).getMotivoCierre()!=null && ((Programa)pageContext.getAttribute("tb")).getMotivoCierre().getDescripcion()!=null){ %>
										    <a href="javascript:mostrarAdicionar('<%=((Programa)pageContext.getAttribute("tb")).getMotivoCierre().getDescripcion()%>',
										    									 '<%=((Programa)pageContext.getAttribute("tb")).getObservacionCierre()%>',
										    									 '<%=((Programa)pageContext.getAttribute("tb")).getFechaCierre()%>',
										    									 '<%=((Programa)pageContext.getAttribute("tb")).getCodUsuarioCierre()%>');">
												<img  src="imagentabla/bbva.VerAzul24.png" alt="Demas Campos" border="0">
											</a>
											<%}%>
										    </display:column> 
										    
										    <%if(pageContext.getAttribute("tb") != null && ((Programa)pageContext.getAttribute("tb")).getEstadoPrograma().getId().equals(Constantes.ID_ESTADO_PROGRAMA_PENDIENTE)){ %>												    
										    <display:column title="Modificar Copiar"   style="text-align:center;width:50px">
										    	<a href="javascript:modificarEditarPF('<%=((Programa)pageContext.getAttribute("tb")).getId()%>')">
												<img src="imagentabla/bbva.Editar2Azul24.png" border="0" alt="Editar Programa"></img>
												</a>
										    </display:column>									    
										    <%}%> 
										    <%if(pageContext.getAttribute("tb") != null && ((Programa)pageContext.getAttribute("tb")).getEstadoPrograma().getId().equals(Constantes.ID_ESTADO_PROGRAMA_CERRADO)){ %>
										    <display:column title="Modificar Copiar"   style="text-align:center;width:50px">
										    	<a href="javascript:modificarCopiaPF('<%=((Programa)pageContext.getAttribute("tb")).getId()%>','<%=((Programa)pageContext.getAttribute("tb")).getTipoEmpresa().getId()%>')">
												<img src="imagentabla/bbva.CopiaAzul24.png" alt="Copiar Programa" border="0"></img>
												</a>
										    </display:column>                        
										    <%}%>
										    
<%-- 										    <display:column title="Rep. Excel"   style="text-align:center;"> --%>
<%-- 										    <a href="javascript:descargarExcel2('<%=((Programa)pageContext.getAttribute("tb")).getId()%>')"> --%>
<!-- 												<img src="imagentabla/bbva.ExcelAzul24.png" alt="Descargar Excel" border="0"> -->
<!-- 											</a> -->
<%-- 										    </display:column>      --%>
										    
										    <display:column title="Rep. PDF"   style="text-align:center;">
										    <a href="javascript:descargarPDF('<%=((Programa)pageContext.getAttribute("tb")).getId()%>')">
												<img src="imagentabla/bbva.PdfAzul24.png" alt="Descargar PDF" border="0">
											</a>
										    </display:column>   
										    <display:column title="Rep. Cred. PDF"   style="text-align:center;">
										    <a href="javascript:descargarRCPDF('<%=((Programa)pageContext.getAttribute("tb")).getId()%>')">
												<img src="imagentabla/bbva.PdfAzul24.png" alt="Descargar PDF" border="0">
											</a>
										    </display:column>                               

									    <display:setProperty name="paging.banner.placement" value="bottom" />
									    </display:table>
								</div>		
						 </s:else>					
						</td>
					</tr>
				</table>
		</td>
	</tr>
</table>
	<div id="divApplet">
	</div>
	
<div id="dialog-modal-copiapf" title="Seleccionar la Opción" style="display: none;" >

		<div id="divOptionTipoCopia" >
					<table class="ln_formatos" cellspacing="0">

						<tr>
							<td><input name="nameprogramaModal" id="idprogrmaModal" type="hidden"  /></td>
						</tr>
						<tr>
							<td>La copia del PF se realizará para:</td>
						</tr>
						<tr>
							<td>&nbsp;&nbsp;</td>
						</tr>
						<tr>
							<td>
							<div id="divTipoCopia"></div>				
							</td>
						</tr>
						<tr>
							<td>&nbsp;&nbsp;</td>
						</tr>
					</table>
		</div>
		<div>
			&nbsp;&nbsp;
		</div>
		<input type="button" value="Aceptar" id="modalbtnCopiapf" class="btn">
		<input type="button" value="Cancelar"  id="sbcancelarCopiapf" class="btn"/>
</div>

	<div id="dialog-modal-tipoOperacion" title="Tipo Operacion" style="display:none" >
				<input name="programaTOperacion" id="idprogramaTOperModal" type="hidden"  />		
				 <div  class="my_div" id="idTabTipoOperacion" style="overflow:inherit">
				 	<div>
				  	Tipo Operacion:
				 	</div>				 
					<div id="iddivTipoOperacion">					 
					</div>				 
				 </div>
				 <div>
					&nbsp;&nbsp;
				</div>
				<div>
<!--				<input type="button" value="Generar PDF" id="idbtnAceptarTOperHILO" class="btn">	-->							
					<input type="button" value="Descargar PDF" id="idbtnAceptarTOper" class="btn">
				<input type="button" value="Cancelar"  id="sbcancelarTipoOper" class="btn"/>				
				</div>	
<!-- 				<div id="divdesvargapdf" style="display:none;">	 -->
<!-- 					<input type="button" value="Descargar PDF" id="idbtnAceptarTOper" class="btn"> -->
<!-- 				</div>	 -->
					
		</div>
		
		<div id="dialog-modal-EmpresaRC" title="Seleccione la Empresa - Exportar PDF Reporte Credito" style="display:none" >
				<input name="programaEmpresaRC" id="idprogramaEmpresaRCModal" type="hidden"  />		
				 <div  class="my_div" id="idTabEmpresaRC" style="overflow:inherit">
				 	<div>
				  	Empresa:
				 	</div>				 
					<div id="iddivEmpresaRC">					 
					</div>				 
				 </div>
				 
				  <div  class="my_div" id="idTablaRC" style="overflow:inherit">
				 </div>
				 
				 <div>
					&nbsp;&nbsp;
				</div>
				<div>
				<input type="button" value="Descargar" id="idbtnAceptarEmpresaRC" class="btn">				
				<input type="button" value="Cancelar"  id="sbcancelarEmpresaRC" class="btn"/>
				</div>			
		</div>
	
</s:form>

		<div id="dialog-modal-motivo" title="Datos de Cierre" style="display:none" >			
				 <div  class="my_div" id="idTablacierre" style="overflow:inherit">				 
				 </div>			
		</div>





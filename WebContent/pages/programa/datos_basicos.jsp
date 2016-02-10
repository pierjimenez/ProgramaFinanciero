<%@taglib prefix="s" uri="/struts-tags" %>  
<%@taglib uri="http://java.fckeditor.net" prefix="FCK" %>

<%@page import="pe.com.bbva.iipf.util.Constantes"%>
<%@page import="java.util.List"%>
<%@page import="pe.com.bbva.iipf.pf.model.Parametro"%>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>

<% 
	String max_size_nota_accionistas = GenericAction.getObjectParamtrosSession(Constantes.MAX_SIZE_TEXT_ACCIONARIADO).toString();
	String max_size_nota_participaciones = GenericAction.getObjectParamtrosSession(Constantes.MAX_SIZE_TEXT_PARTICIPACIONES).toString();
	String max_size_nota_ratingexterno = GenericAction.getObjectParamtrosSession(Constantes.MAX_SIZE_TEXT_RATING_EXTERNO).toString();
	String max_size_nota_valoracion_global = GenericAction.getObjectParamtrosSession(Constantes.MAX_SIZE_TEXT_VALORACION_GLOBAL).toString();
	String max_size_nota_actividad_principal = GenericAction.getObjectParamtrosSession(Constantes.MAX_SIZE_TEXT_ACTIVIDAD_PRINCIPAL).toString();
	int anio = Integer.parseInt(GenericAction.getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
	long tipoPrograma = Long.parseLong(GenericAction.getObjectSession(Constantes.ID_TIPO_PROGRAMA_SESSION).toString());
	String x = request.getAttribute("scrollX")==null?"ningun valor":request.getAttribute("scrollX").toString();
	String y = request.getAttribute("scrollY")==null?"ningun valor":request.getAttribute("scrollY").toString();
	
	String activoBtnGeneral = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_BTN_GENERAL).toString();
	String activoBtnValidar = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_BTN_VALIDAR).toString();
	String activoValidarEditor = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_VALIDAR_EDITOR).toString();

 %>
 <%
String tipoEmpresa = GenericAction.getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION)==null?"":GenericAction.getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
%>
<script language="JavaScript">
var dirurl='';
var vtextarea='';
var activarmensajeAutograbar=0;
var comenSinteEmpres=0;
var comenDatosMatriz=0;
var comenCompraVenta=0;
var comenConcentracion=0;
var campoEspacioLibre=0;
var campoValoracionGlobal=0;

var COMEN_SINTE_EMPRES=1;
var COMEN_DATOS_MATRIZ=2;
var COMEN_COMPRA_VENTA=3;
var COMEN_CONCENTRACION=4;
var COMEN_ESPACIO_LIBRE=5;
var COMEN_VALORACION=6;




	function editado(campo){
		if(campo==COMEN_SINTE_EMPRES){
			comenSinteEmpres=1;sincronizado=0;
		}else if(campo==COMEN_DATOS_MATRIZ){
			comenDatosMatriz=1;sincronizado=0;
		}else if(campo==COMEN_COMPRA_VENTA){
			comenCompraVenta=1;sincronizado=0;
		}else if(campo==COMEN_CONCENTRACION){
			comenConcentracion=1;sincronizado=0;
		}else if(campo==COMEN_ESPACIO_LIBRE){
			campoEspacioLibre=1;sincronizado=0;
		}else if(campo==COMEN_VALORACION){
			campoValoracionGlobal=1;sincronizado=0;
		}
		return false;
	}
	
	function cerrarmsnAutoGuardado(){	  
	  $("#idmsnAutoGuardado" ).html("");
	}
	function inimsnAutoGuardado(){
		$("#idmsnAutoGuardado" ).html("<font size='3' color='blue'>Autoguardado en proceso....</font>");
	}
	
	
	
	function closeMsnGuardado(){	  
		var divs = document.getElementById('idMenssageSave');	  
		divs.style.display = "none";
		divs.innerHTML="";
	}
	function showMsnGuardado(){
		var divs = document.getElementById('idMenssageSave');	  
		divs.style.display = "";
		divs.innerHTML="<font size='3' color='blue'> Proceso de guardado Terminado...</font>";
	}
	
	
	function guardarFormulariosActualizadosPagina(uri,vtipoGrabacion){
	   	url=uri;	   
	   	if(vtipoGrabacion=="AUTO"){
	   		activarmensajeAutograbar=0;
	   		inimsnAutoGuardado();
	   		
	   	}else if (vtipoGrabacion=="MANUAL"){
	   		activarmensajeAutograbar=1;
	   	}else{
	   		activarmensajeAutograbar=0;
	   	}
	   	//guardadoGeneral=1;	   	
	   	sincrono=false;
		if(comenSinteEmpres==1)				{		$("#ssavesintesis").click(); 	if (vtipoGrabacion=="MANUAL"){comenSinteEmpres=1;}}
		if(comenDatosMatriz==1)				{		$("#ssavedatosmatriz").click(); 	if (vtipoGrabacion=="MANUAL"){comenDatosMatriz=1;}}
		if(comenCompraVenta==1)				{		$("#ssavecomencompraventa").click(); 	if (vtipoGrabacion=="MANUAL"){comenCompraVenta=1;}}
		if(comenConcentracion==1)			{	    $("#ssaveconcentracion").click(); 	if (vtipoGrabacion=="MANUAL"){comenConcentracion=1;}}
		if(campoEspacioLibre==1)			{		$("#ssaveespaciolibre").click(); 	if (vtipoGrabacion=="MANUAL"){campoEspacioLibre=1;}}
		if(campoValoracionGlobal==1)		{		$("#ssavevaloracion").click(); 	if (vtipoGrabacion=="MANUAL"){campoValoracionGlobal=1;}}
		//setInterval("cambiarPagina()", 1000);
		if(edicionCampos==1){			
			try {
					
				if (vtipoGrabacion=="MANUAL"){
					showMsnGuardado();
			   	}
					 $.ajax({
				          	async:false,
				          	type: "POST",
				          	url:document.getElementById("datosBasicos").action,
				          	data:$("#datosBasicos").serialize(),	   	           
				       		success: function (data) {
				       			//guardadoGeneral=0;
				       			ocultarGuardando();//
				       			//cambiarPagina();
				       			setTimeout(cerrarmsnAutoGuardado, 6000);
				       			resetTiempoSession();
				       			closeLoading();					       			
				       			setTimeout(closeMsnGuardado, 3000);
				       		}    	
					});
			} catch (e) {
				 
				ocultarGuardando();
				closeLoading();
				closeMsnGuardado();
			}
		}else{
		ocultarGuardando();
		closeLoading();
		setTimeout(cerrarmsnAutoGuardado, 9000);
		}
		
		if(vtipoGrabacion=="AUTO"){
			sincronizado=1;
		}
		return true;
	}
	
	function noEditado(campo){
		if(campo==COMEN_SINTE_EMPRES){
			comenSinteEmpres=0;
		}else if(campo==COMEN_DATOS_MATRIZ){
			comenDatosMatriz=0;
		}else if(campo==COMEN_COMPRA_VENTA){
			comenCompraVenta=0;
		}else if(campo==COMEN_CONCENTRACION){
			comenConcentracion=0;
		}else if(campo==COMEN_ESPACIO_LIBRE){
			campoEspacioLibre=0;
		}else if(campo==COMEN_VALORACION){
			campoValoracionGlobal=0;
		}
		
		if(comenSinteEmpres==0				&
				comenDatosMatriz==0			&
				comenCompraVenta==0			&
				comenConcentracion==0       &
				campoEspacioLibre==0		&
				campoValoracionGlobal==0){
			sincronizado=1;
		}
		
		return false;
	}
	
	function ocultarGuardando(){
	//	if(guardadoGeneral==0){
			setTimeout($.unblockUI, 1);
	//	}
	}
	
	function getValuesCheck(nombre){
		var values = '';
		var flag=true;
		$("input[name="+nombre+"]").each(function(){
			if($(this).is(':checked')){
				if(flag==true){
					values = $(this).val();
					flag = false;
				}else{
					values = values +','+$(this).val();
				}
			}
		});
		return values;
	}
	
	function getDescriptionsCheck(nombre){
		$("input[name="+nombre+"]").each(function(){
			alert($(this).text());
		});
	}
	
	function saveProgramaBlob(campo)
	{
		document.forms[0].action='saveProgramaBlob.do';
		
		document.forms[0].submit();
	}
	
	function sumar(campos,para){
		var add = 0;
		$(campos).each(function() {
			add += Number($(this).val());
		});
		$(para).val(add);
		
	}
	
	function sumarPorcentaje(campos,para){
		var add = 0;
		$(campos).each(function() {

			cvalue=($(this).val()).toString();
			//cvalue=cvalue.split(".").join("");
			cvalue=cvalue.split(",").join("");				
			if(!isNaN(parseFloat(cvalue)) && cvalue.length!=0) {
				var sumatemp = add + parseFloat(cvalue);
				//alert(parseFloat(cvalue));
				//alert(parseFloat(sumatemp.toFixed(2)));
				if(parseFloat(sumatemp.toFixed(2)) >100){
					alert('Los valores suman mas de 100%');
					$(this).focus();
				}else{
					add += parseFloat(cvalue);
				}	
			}
		});
		//add=add/100;
		add=NumberFormat(add, '2', '.', ',');	
		add.toString();
		$(para).val(add);		
	}

	function existeElementos(campos){
		var add = 0;
		$(campos).each(function() {
			add = add+1;
		});
		if(add==0){
			alert('No existen elementos para ser guardados...');
			return false;
		}
		return true;
	}
	

	function cantidadRepetido(campos1,elemento){
		var addc = 0;
		var valorc='';
		$(campos1).each(function() {					
			valorc=($(this).val()).toString();								
			if (valorc==elemento){				
				addc=addc+1;				
			}
		});
		return addc;	
	}
	
	function existeElementosRepetido(campos){
		var add = 0;
		var cont=0;
		var valor='';
		$(campos).each(function() {			
			valor=($(this).val()).toString();
			if (valor!=''){
				//alert(valor);
				add = cantidadRepetido(campos,valor)
				if(add>1){
					cont = cont+1;
				}
			}
		});	
		if(cont>0){
			alert('Existen Elemento repetidos...');
			return false;
		}	
		
		return true;
	}
	
	function validaElementosAccionista(campos,camporep){
		var add = 0;
		$(campos).each(function() {
			add = add+1;
		});
		if(add==0){
			alert('No existen elementos para ser guardados...');
			return false;
		}
		//valida elementos repetidos
		var res=existeElementosRepetido(camporep);
		//alert(res);
		return res;		
		
	}
	
	function existeElementosCapitalizacion(campos,campos3){
		var add = 0;
		$(campos).each(function() {
			add = add+1;
		});
		if(add==0){
			alert('No existen elementos para ser guardados...');
			return false;
		}

		
		var add3 = 0;
		var valor3='';
		$(campos3).each(function() {			
			valor3 = $(this).val();									
			if (valor3==''){
				add3 = add3+1;
			}
		});
		
		if(add3>0){
			alert('Seleccione la Divisa en el cuadro de Capitalizacion Bursatil...');
			return false;
		}		

		return true;
	}
	
	function validarSeleccioneDatosBasicos(camposx){
	
		var add1 = 0;
		var valor1='';
		
		$(camposx).each(function() {			
			valor1 = $(this).val();						
			if (valor1==''){
				add1 = add1+1;
			}
		});
		
		if(add1>0){
			alert('Seleccione la Divisa en el Cuadro de Capitalización Bursatil...');
			return false;
		}
		return true;
	}
	
	function validarRango(obj, valmin, valmax){
		if(Number(obj.value)>Number(valmax) &&
		   Number(obj.value)<Number(valmin)){
			alert('Solo se puede ingresar valores entre '+valmin+' y '+ valmax);
			return true;   
		}
		return false;
	}
	
	function validarCantidadCaracteres(id){
		$textarea = $(id);
		//alert($(id).val().length);  
   		if($textarea.val().length > <%=max_size_nota_accionistas%>){
   		 		alert('Solo puede ingresar '+<%=max_size_nota_accionistas%>+' caracteres');
   		 	}
   	} 	
	(function($){
        $(
            function(){
                $('input:text').setMask();
            }
        );
    })(jQuery);
    $(function() {
		$( "#accordion" ).accordion();
	});
	
	
	function onloadDatosBasico(codigo,flag){
	//alert(codigo);
		document.forms["datosBasicos"].action='initDatosBasicos.do';
		document.forms["datosBasicos"].codempresagrupo.value =codigo; 	
		document.forms["datosBasicos"].flagChangeEmpresa.value =flag; 		
		document.forms["datosBasicos"].submit();	
	}
	
	function mostrarRefresh(){
		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Actualizando. Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } }); 
	}
	
	function addCalendar(){
	$('.cssidCalendardb').each(function(){
		$(this).datepicker({ 
    							dateFormat: 'dd/mm/yy',
    							showOn: "button",
							    buttonImage: "images/calendariobbva.png",
								buttonImageOnly: true });
    	$(this).datepicker();
   	});
	}
	
	function obtenerDatoAccionistaOnblur(fila){
	
  		var vtipoDocumento= $("#datosBasicos_listaAccionistas_"+fila+"__tipoDocumento_id").val();	
		var vnumeroDocumento= $("#datosBasicos_listaAccionistas_"+fila+"__numeroDocumento").val();
	  	
	  	if (vtipoDocumento.length==0 ||vnumeroDocumento.length==0)	{
			alert("El Ingrese Tipo Documento o Numero de Documento");
			
			$("#datosBasicos_listaAccionistas_"+fila+"__nombre").removeAttr("readonly");
			$("#datosBasicos_listaAccionistas_"+fila+"__codigoCentral").val("");
			$("#datosBasicos_listaAccionistas_"+fila+"__nombre").val("");
			$("#datosBasicos_listaAccionistas_"+fila+"__edad").val("");
			$("#datosBasicos_listaAccionistas_"+fila+"__tipoNumeroDocumentoHost").val("");
			
	  	
	  	}else{
	  	
		  		$("#idmsnAcctividad" ).html("<font color='blue'>Obteniendo Datos de Accionista...</font>");
		  		mostrarRefresh();				
				//alert(vtipoDocumento);
				//alert(vnumeroDocumento);
				
						$.post("obtenerAccionista.do", { idtipoDocumento: vtipoDocumento ,numeroDocumento: vnumeroDocumento }, function(data){
							//alert(data);
							try{
								var jsonDataText = eval("(" + data + ")");		
								
								//alert(jsonDataText.codigoCentral);
								$("#datosBasicos_listaAccionistas_"+fila+"__nombre").val(jsonDataText.nombreAccionista);
								$("#datosBasicos_listaAccionistas_"+fila+"__codigoCentral").val(jsonDataText.codigoCentral);
								$("#datosBasicos_listaAccionistas_"+fila+"__edad").val(jsonDataText.edadAccionista);
								$("#datosBasicos_listaAccionistas_"+fila+"__tipoNumeroDocumentoHost").val(jsonDataText.tipoNumeroDocumentoHost);
	
								
									$("#idmsnAcctividad" ).html("<font color='blue'>"+jsonDataText.msnAccionista+"</font>");
								
							
								ocultarGuardando();
								setTimeout(cerrarmsnAcc, 15000);
								resetTiempoSession();
								if (jsonDataText.codigoCentral.length>0 && jsonDataText.nombreAccionista.length>0){
								//alert(jsonDataText.codigoCentral.length);
								$("#datosBasicos_listaAccionistas_"+fila+"__nombre").prop('readonly', true );
								
								}else{
								//alert("en");
								$("#datosBasicos_listaAccionistas_"+fila+"__nombre").prop("readonly",false);
								
								}
								
								if (jsonDataText.edadAccionista.length>0){
								
								$("#datosBasicos_listaAccionistas_"+fila+"__edad").prop('readonly', true );
								}else{								
								$("#datosBasicos_listaAccionistas_"+fila+"__edad").prop("readonly",false);
								}
							}catch (e) {
								$("#idmsnAcctividad" ).html("<font color='blue'>No se obtuvo respuesta de Host....</font>");
								ocultarGuardando();
								setTimeout(cerrarmsnAcc, 15000);
								resetTiempoSession();								
														
							}						
							
							 
						});
						
						
						

			}	
		
	}	
	function obtenerDatoAccionista(e,fila){
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
	
  		var vtipoDocumento= $("#datosBasicos_listaAccionistas_"+fila+"__tipoDocumento_id").val();	
		var vnumeroDocumento= $("#datosBasicos_listaAccionistas_"+fila+"__numeroDocumento").val();
	  	
	  	if (vtipoDocumento.length==0 ||vnumeroDocumento.length==0)	{
			alert("Ingrese Tipo Documento o Numero de Documento");
			
			$("#datosBasicos_listaAccionistas_"+fila+"__nombre").removeAttr("readonly");
			$("#datosBasicos_listaAccionistas_"+fila+"__codigoCentral").val("");
			$("#datosBasicos_listaAccionistas_"+fila+"__nombre").val("");
			$("#datosBasicos_listaAccionistas_"+fila+"__edad").val("");
			
	  	
	  	}else{
	  	
		  		$("#idmsnAcctividad" ).html("<font color='blue'>Obteniendo Datos de Accionista...</font>");
		  		mostrarRefresh();				
				//alert(vtipoDocumento);
				//alert(vnumeroDocumento);
				
						$.post("obtenerAccionista.do", { idtipoDocumento: vtipoDocumento ,numeroDocumento: vnumeroDocumento }, function(data){
							//alert(data);
							try{
								var jsonDataText = eval("(" + data + ")");		
								
								//alert(jsonDataText.codigoCentral); 
								$("#datosBasicos_listaAccionistas_"+fila+"__nombre").val(jsonDataText.nombreAccionista);
								$("#datosBasicos_listaAccionistas_"+fila+"__codigoCentral").val(jsonDataText.codigoCentral);
								$("#datosBasicos_listaAccionistas_"+fila+"__edad").val(jsonDataText.edadAccionista);
	
								
									$("#idmsnAcctividad" ).html("<font color='blue'>"+jsonDataText.msnAccionista+"</font>");
								
							
								ocultarGuardando();
								setTimeout(cerrarmsnAcc, 15000);
								resetTiempoSession();
								if (jsonDataText.codigoCentral.length>0 && jsonDataText.nombreAccionista.length>0){
								//alert(jsonDataText.codigoCentral.length);
								$("#datosBasicos_listaAccionistas_"+fila+"__nombre").prop('readonly', true );
								
								}else{
								//alert("en");
								$("#datosBasicos_listaAccionistas_"+fila+"__nombre").prop("readonly",false);
								
								}
								
								if (jsonDataText.edadAccionista.length>0){
								
								$("#datosBasicos_listaAccionistas_"+fila+"__edad").prop('readonly',true );
								}else{								
								$("#datosBasicos_listaAccionistas_"+fila+"__edad").prop("readonly",false);
								}
							}catch (e) {
								$("#idmsnAcctividad" ).html("<font color='blue'>No se obtuvo respuesta de Host.....</font>");
								ocultarGuardando();
								setTimeout(cerrarmsnAcc, 15000);
								resetTiempoSession();					
														
							}						
							
							 
						});
						
						
						

			}	
			return true;		
		}else{		
			return acceptNum(e);
		}

	}
	
	
	function habilitarAccionista(){
		var i=0;
		$(".csscodCentralAcci").each(function() {
		var codCentral=	$("#datosBasicos_listaAccionistas_"+i+"__codigoCentral").val();
		//alert(codCentral);
		if (codCentral !='' ){
			$("#datosBasicos_listaAccionistas_"+i+"__nombre").prop('readonly', true );
		}
	 		i=i+1;
		});
	}
	
	function cerrarmsnAcc(){
	  $("#idmsnAcctividad" ).html("");
	}
	
   $(window).load(function() { setTimeout($.unblockUI, 1);});
   
   $(document).keypress(function(e) {
			    if(e.which == 13) {
			       // alert('You pressed enter!');
			        return false;
			    }
			});
	
   $(document).ready(function() {
          

	   	 //When page loads...
	   	 //Hide (Collapse) the toggle containers on load
		 $(".toggle_container").hide(); 
		 //Switch the "Open" and "Close" state per click then slide up/down (depending on open/close state)
		 $("h2.trigger").click(function(){
			$(this).toggleClass("active").next().slideToggle("slow");
			return false; //Prevent the browser jump to the link anchor
		 });
	   	 
	   	 if($("#chkPlanilla").is(':checked')) {
            $("#divPlanilla").attr('style','display:');
         }
         if($("#chkSintesisEmpresa").is(':checked')) {
            $("#divSintesisEmpresa").attr('style','display:');
         }
         
         if($("#chkConcentracion").is(':checked')) {
            $("#divConcentracion").attr('style','display:');
         }
         if($("#chkDatosMatriz").is(':checked')) {
            $("#divDatosMatriz").attr('style','display:');
         }
         if($("#chkAccionariado").is(':checked')) {
            $("#divAccionariado").attr('style','display:');
         }
         
         if($("#chkCapitalizacion").is(':checked')) {
            $("#divCapitalizacion").attr('style','display:');
         }
         
         if($("#chkPrincipalesEjecutivos").is(':checked')) {
            $("#divPrincipalesEjecutivos").attr('style','display:');
         }
         if($("#chkParticipacionesSignificativas").is(':checked')) {
            $("#divParticipacionesSignificativas").attr('style','display:');
         }
         if($("#chkRatingExterno").is(':checked')) {
            $("#divRatingExterno").attr('style','display:');
         }
         if($("#chkComprasVentas").is(':checked')) {
            $("#divComprasVentas").attr('style','display:');
         }
         if($("#chkNegocioBeneficio").is(':checked')) {
            $("#divNegocioBeneficio").attr('style','display:');
         }
         if($("#chkValoracionGlobal").is(':checked')) {
            $("#divValoracionGlobal").attr('style','display:');
         }
	   	 if($("#chkValoracion").is(':checked')) {
            $("#divValoracion").attr('style','display:');
         }
	   	 
	   	 $("#chkPlanilla").click(function () {
			$("#divPlanilla").toggle("slow");
		 });   
		 $("#chkSintesisEmpresa").click(function () {
			$("#divSintesisEmpresa").toggle("slow");
		 });  
		 
		 $("#chkConcentracion").click(function () {
			$("#divConcentracion").toggle("slow");
		 });  
		 
		 
		 $("#chkDatosMatriz").click(function () {
			$("#divDatosMatriz").toggle("slow");
		 });     
		 $("#chkAccionariado").click(function () {
			$("#divAccionariado").toggle("slow");
		 }); 
		 
		 $("#chkCapitalizacion").click(function () {
			$("#divCapitalizacion").toggle("slow");
		 }); 
		 
		 $("#chkPrincipalesEjecutivos").click(function () {
			$("#divPrincipalesEjecutivos").toggle("slow");
		 }); 
		 $("#chkParticipacionesSignificativas").click(function () {
			$("#divParticipacionesSignificativas").toggle("slow");
		 }); 
		 $("#chkRatingExterno").click(function () {
			$("#divRatingExterno").toggle("slow");
		 }); 
		 $("#chkComprasVentas").click(function () {
			$("#divComprasVentas").toggle("slow");
		 }); 
		 $("#chkNegocioBeneficio").click(function () {
			$("#divNegocioBeneficio").toggle("slow");
		 });
		 $("#chkValoracionGlobal").click(function () {
			$("#divValoracionGlobal").toggle("slow");
		 }); 
		$("#chkValoracion").click(function () {
			$("#divValoracion").toggle("slow");
		 }); 
		 
	   	 
		 $(".tab_content").hide(); //Hide all content
		 $("#li1").addClass("active").show(); //Activate first tab
		 $("#tab1").show(); //Show first tab content
   		 $("#datosBasicos").validationEngine();
   		 $(".col1").css('text-align', 'right');
   		 $(".col2").css('text-align', 'right');
   		 $(".col3").css('text-align', 'right');
   		 $(".col4").css('text-align', 'right');
   		 
   		 $("#tanotasAccionariado").counter({
			count: 'up',
			goal: <%=max_size_nota_accionistas%>
		 });

		$("#tanotasParticipaciones").counter({
			count: 'up',
			goal: <%=max_size_nota_participaciones%>
		 });
		 
		 $("#tanotasRatingExterno").counter({
			count: 'up',
			goal: <%=max_size_nota_ratingexterno%>
		 });
		 $("#tcomentvaloraGlobal").counter({
			count: 'up',
			goal: <%=max_size_nota_valoracion_global%>
		 });
		  
		 $("#stextactividadPrincipal").counter({
			count: 'up',
			goal: <%=max_size_nota_actividad_principal%>
		 });
		 
   		 $(".col1").blur(function () { 
   		 	sumar(".col1","#total1");
   		 } );
   		 $(".col2").blur(function () { 
			sumar(".col2","#total2");
   		 } );
   		 $(".col3").blur(function () { 
			sumar(".col3","#total3");
   		 } );
   		 $(".col4").blur(function () { 
			sumar(".col4","#total4");
   		 } );
   		 
   		 
   		 sumarPorcentaje(".tact1","#totalAI1");
		 sumarPorcentaje(".tact2","#totalAI2");
		 sumarPorcentaje(".tact3","#totalAI3");
		 sumarPorcentaje(".tact4","#totalAB1");
		 sumarPorcentaje(".tact5","#totalAB2");
		 sumarPorcentaje(".tact6","#totalAB3");
		 sumarPorcentaje(".tneg1","#totalNI1");
		 sumarPorcentaje(".tneg2","#totalNI2");
		 sumarPorcentaje(".tneg3","#totalNI3");
		 sumarPorcentaje(".tneg4","#totalNB1");
		 sumarPorcentaje(".tneg5","#totalNB2");
		 sumarPorcentaje(".tneg6","#totalNB3");
   		 
   		 $(".tact1").blur(function () { 
			sumarPorcentaje(".tact1","#totalAI1");
   		 } );
   		 $(".tact2").blur(function () { 
			sumarPorcentaje(".tact2","#totalAI2");
   		 } );
   		 $(".tact3").blur(function () { 
			sumarPorcentaje(".tact3","#totalAI3");
   		 } );
   		 $(".tact4").blur(function () { 
			sumarPorcentaje(".tact4","#totalAB1");
   		 } );
   		 $(".tact5").blur(function () { 
			sumarPorcentaje(".tact5","#totalAB2");
   		 } );
   		 $(".tact6").blur(function () { 
			sumarPorcentaje(".tact6","#totalAB3");
   		 } );
   		
   		$(".tneg1").blur(function () { 
			sumarPorcentaje(".tneg1","#totalNI1");
   		 } );
   		 $(".tneg2").blur(function () { 
			sumarPorcentaje(".tneg2","#totalNI2");
   		 } );
   		 $(".tneg3").blur(function () { 
			sumarPorcentaje(".tneg3","#totalNI3");
   		 } );
   		 $(".tneg4").blur(function () { 
			sumarPorcentaje(".tneg4","#totalNB1");
   		 } );
   		 $(".tneg5").blur(function () { 
			sumarPorcentaje(".tneg5","#totalNB2");
   		 } );
   		 $(".tneg6").blur(function () { 
			sumarPorcentaje(".tneg6","#totalNB3");
   		 } );
   		 
   		var checkboxes = $(':checkbox[class=chkaccionista]');
		checkboxes.click(function(){
		  var self = this;
		  checkboxes.each(function(){
		    if(this!=self) this.checked = '';
		  });
		});
   		 
   		sumarPorcentaje(".porcenAcci","#totalPorceAcci");
   		 
   		$(".porcenAcci").blur(function(){
	   		sumarPorcentaje(".porcenAcci","#totalPorceAcci");
	   			
   		});
   		
   		habilitarAccionista();

 	   $('#stextareasintesis').wysiwyg({			
			rmUnusedControls: true,			
			controls: {				
			bold: { visible : true },				
			html: { visible : true },				
			insertOrderedList: { visible : true },				
			removeFormat: { visible : true }			
			}		
		});

	   $('#stextareasintesis').wysiwyg("addControl", 
		   "UploadFile", {				
		   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
		   tooltip: 'Upload',tags: ['Upload'],				
		   exec:  function() { 		   		
		   		vtextarea='stextareasintesis'; 
		   		newWindowUploadFile(vtextarea);  }
	       }
	    );
   		
   		$("#bversintesis").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divsintesisempresa").attr("style","");
   			$("#ssavesintesis").prop("disabled","");
   			$("#scleansintesis").prop("disabled","");
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();
	   		var codempresa= $("#codigoEmpresa").val();
	   		comenSinteEmpres=1;
   			if(tipoempresa == '2' ){ 
		   		$.post("consultarProgramaBlob.do", { campoBlob: "sintesisEmpresa" },
			   		function(data){
			   		setTimeout($.unblockUI, 1);  
			   		//$('#stextareasintesis').wysiwyg('setContent', data);
			   		var iframe = document.getElementById("sintesisEmpresa___Frame");				
					var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
					var eInnerElement = oCell.firstChild;				
					if ( eInnerElement ){					
						eInnerElement.contentWindow.document.body.innerHTML = data;	
						resetTiempoSession();				
					}  			     	
			   	});
		    }else{
			     if (codempresa==codEprincipal){
					    $.post("consultarProgramaBlob.do", { campoBlob: "sintesisEmpresa" },
					   		function(data){
					   		setTimeout($.unblockUI, 1);  
					   		//$('#stextareasintesis').wysiwyg('setContent', data);
					   		var iframe = document.getElementById("sintesisEmpresa___Frame");				
							var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
							var eInnerElement = oCell.firstChild;				
							if ( eInnerElement ){					
								eInnerElement.contentWindow.document.body.innerHTML = data;	
								resetTiempoSession();				
							}  			     	
					   	});
			     
			     }else{
			     		$.post("consultarDatosBasicoBlob.do", { campoBlob: "sintesisEmpresa", codEmpresa:codempresa },
					   		function(data){
					   		setTimeout($.unblockUI, 1);  
					   		//$('#stextareasintesis').wysiwyg('setContent', data);
					   		var iframe = document.getElementById("sintesisEmpresa___Frame");				
							var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
							var eInnerElement = oCell.firstChild;				
							if ( eInnerElement ){					
								eInnerElement.contentWindow.document.body.innerHTML = data;	
								resetTiempoSession();				
							}  			     	
					   	});
			     
			     }			
			}
	   		//editado(COMEN_SINTE_EMPRES);
		});
		$("#ssavesintesis").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			
			var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();	   		
	   		var codempresa= $("#codigoEmpresa").val();	
					   
			var iframe = document.getElementById("sintesisEmpresa___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			var accionGraba=1;
			if(0 < <%=activoValidarEditor%>){ 	
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}

					var dataGet={campoBlob:'sintesisEmpresa',valorBlob:valblob};
					
					if(tipoempresa == '2' ){						
						   	guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_SINTE_EMPRES);
		    		}else{ 				    						
					      if (codempresa==codEprincipal){	
							guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_SINTE_EMPRES);			      
					      }else{			      
						      $.post("saveDatosBasicoBlob.do", 	  
					   			  {campoBlob:"sintesisEmpresa", valorBlob:valblob, codEmpresa:codempresa},
							   		function(data){
							   			setTimeout($.unblockUI, 1); 
							   			resetTiempoSession();
				    		  }); 
					      
					      }					   			
					}
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activarmensajeAutograbar);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){
					var dataGet={campoBlob:'sintesisEmpresa',valorBlob:valblob};
					
					if(tipoempresa == '2' ){						
						   	guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_SINTE_EMPRES);
		    		}else{ 				    						
					      if (codempresa==codEprincipal){	
							guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_SINTE_EMPRES);			      
					      }else{			      
						      $.post("saveDatosBasicoBlob.do", 	  
					   			  {campoBlob:"sintesisEmpresa", valorBlob:valblob, codEmpresa:codempresa},
							   		function(data){
							   			setTimeout($.unblockUI, 1); 
							   			resetTiempoSession();
				    		  }); 
					      
					      }					   			
					}
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUAL("SINTESIS EMPRESA"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}	
			}
		});
		$("#scleansintesis").click(function () { 
			comenSinteEmpres=1;
			//$('#stextareasintesis').wysiwyg('setContent', '');
			var iframe = document.getElementById("sintesisEmpresa___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			} 
		});
		
		$("#sverificasintesis").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });
			comenSinteEmpres=1;
			var iframe = document.getElementById("sintesisEmpresa___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		
		
		///mcg
		
		$('#stextareaconcentracion').wysiwyg({			
			rmUnusedControls: true,			
			controls: {				
			bold: { visible : true },				
			html: { visible : true },				
			insertOrderedList: { visible : true },				
			removeFormat: { visible : true }			
			}		
		});

	   $('#stextareaconcentracion').wysiwyg("addControl", 
		   "UploadFile", {				
		   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
		   tooltip: 'Upload',tags: ['Upload'],				
		   exec:  function() { 		   		
		   		vtextarea='stextareaconcentracion'; 
		   		newWindowUploadFile(vtextarea);  }
	       }
	    );
   		
   		$("#bverconcentracion").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divconcentracion").attr("style","");
   			$("#ssaveconcentracion").prop("disabled","");
   			$("#scleanconcentracion").prop("disabled","");
   			comenConcentracion=1;
   			
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();
	   		var codempresa= $("#codigoEmpresa").val();
			
			if(tipoempresa == '2' ){ 
					$.post("consultarProgramaBlob.do", { campoBlob: "concentracion" },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareaconcentracion').wysiwyg('setContent', data);
				   		var iframe = document.getElementById("concentracion___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;	
							resetTiempoSession();				
						}  			  			     	
				   });
			
			}else{
			      if (codempresa==codEprincipal){
				      $.post("consultarProgramaBlob.do", { campoBlob: "concentracion" },
					   		function(data){
					   		setTimeout($.unblockUI, 1);  
					   		//$('#stextareaconcentracion').wysiwyg('setContent', data);
					   		var iframe = document.getElementById("concentracion___Frame");				
							var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
							var eInnerElement = oCell.firstChild;				
							if ( eInnerElement ){					
								eInnerElement.contentWindow.document.body.innerHTML = data;	
								resetTiempoSession();				
							}  			  			     	
					   });
			
			      }else{
				      	$.post("consultarDatosBasicoBlob.do", { campoBlob: "concentracion", codEmpresa:codempresa },
					   		function(data){
					   		setTimeout($.unblockUI, 1);  
					   		//$('#stextareaconcentracion').wysiwyg('setContent', data);
					   		var iframe = document.getElementById("concentracion___Frame");				
							var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
							var eInnerElement = oCell.firstChild;				
							if ( eInnerElement ){					
								eInnerElement.contentWindow.document.body.innerHTML = data;	
								resetTiempoSession();				
							}  			  			     	
					   });			
			      }			
			}	
	   		//editado(COMEN_CONCENTRACION);
		});
		$("#ssaveconcentracion").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			
			var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();	   		
	   		var codempresa= $("#codigoEmpresa").val();	
			
			var iframe = document.getElementById("concentracion___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			var accionGraba=1;
			if(0 < <%=activoValidarEditor%>){ 	
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}			
				//{campoBlob:"concentracion", valorBlob:$("#stextareaconcentracion").val()},

					var dataGet={campoBlob:'concentracion',valorBlob:valblob};
					
					if(tipoempresa == '2' ){ 
						guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_CONCENTRACION);
					
					}else{
					      if (codempresa==codEprincipal){
							guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_CONCENTRACION);
					
					      }else{
					      	   $.post("saveDatosBasicoBlob.do", 
				   			   {campoBlob:"concentracion", valorBlob:valblob, codEmpresa:codempresa},
						   		function(data){
						   			setTimeout($.unblockUI, 1); 
						   			resetTiempoSession();
			    				});			
					      }			
					}
			}else{
			
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activarmensajeAutograbar);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}			
				//{campoBlob:"concentracion", valorBlob:$("#stextareaconcentracion").val()},
				if (accionGraba==1 || accionGraba==2){
					var dataGet={campoBlob:'concentracion',valorBlob:valblob};
					
					if(tipoempresa == '2' ){ 
						guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_CONCENTRACION);
					
					}else{
					      if (codempresa==codEprincipal){
							guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_CONCENTRACION);
					
					      }else{
					      	   $.post("saveDatosBasicoBlob.do", 
				   			   {campoBlob:"concentracion", valorBlob:valblob, codEmpresa:codempresa},
						   		function(data){
						   			setTimeout($.unblockUI, 1); 
						   			resetTiempoSession();
			    				});			
					      }			
					}
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUAL("CONCENTRACION"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}
			
		});
		$("#scleanconcentracion").click(function () { 
			comenConcentracion=1;
			//$('#stextareaconcentracion').wysiwyg('setContent', '');
			var iframe = document.getElementById("concentracion___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}  
		});
		
		$("#sverificaconcentracion").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });
			comenConcentracion=1;
			var iframe = document.getElementById("concentracion___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		
		
		///mcg
		
		
		
		
		
		$('#stextareacomencompraventa').wysiwyg("addControl", 
		   "UploadFile", {				
		   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
		   tooltip: 'Upload',tags: ['Upload'],				
		   exec:  function() { 		   		
		   		vtextarea='stextareacomencompraventa'; 
		   		newWindowUploadFile(vtextarea);  }
	       }
	    );
		$("#bvercomencompraventa").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divcomencompraventa").attr("style","");
   			$("#ssavecomencompraventa").prop("disabled","");
   			$("#scleancomencompraventa").prop("disabled","");
   			comenCompraVenta=1;	
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();
	   		var codempresa= $("#codigoEmpresa").val();
	   		
	   		if(tipoempresa == '2' ){ 
				$.post("consultarProgramaBlob.do", { campoBlob: "comenComprasVentas" },
			   		function(data){
			   		setTimeout($.unblockUI, 1);  
			   		//$('#stextareacomencompraventa').wysiwyg('setContent', data);
			   		var iframe = document.getElementById("comencompraventa___Frame");				
					var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
					var eInnerElement = oCell.firstChild;				
					if ( eInnerElement ){					
						eInnerElement.contentWindow.document.body.innerHTML = data;	
						resetTiempoSession();				
					}  			     	  			     	
			   });
			
			}else{
			      if (codempresa==codEprincipal){
						$.post("consultarProgramaBlob.do", { campoBlob: "comenComprasVentas" },
					   		function(data){
					   		setTimeout($.unblockUI, 1);  
					   		//$('#stextareacomencompraventa').wysiwyg('setContent', data);
					   		var iframe = document.getElementById("comencompraventa___Frame");				
							var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
							var eInnerElement = oCell.firstChild;				
							if ( eInnerElement ){					
								eInnerElement.contentWindow.document.body.innerHTML = data;	
								resetTiempoSession();				
							}  			     	  			     	
					   });
			      }else{
					   $.post("consultarDatosBasicoBlob.do", { campoBlob: "comenComprasVentas" , codEmpresa:codempresa},
					   		function(data){
					   		setTimeout($.unblockUI, 1);  
					   		//$('#stextareacomencompraventa').wysiwyg('setContent', data);
					   		var iframe = document.getElementById("comencompraventa___Frame");				
							var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
							var eInnerElement = oCell.firstChild;				
							if ( eInnerElement ){					
								eInnerElement.contentWindow.document.body.innerHTML = data;		
								resetTiempoSession();			
							}  			     	  			     	
					   });
			      }			
			}
	   		//editado(COMEN_COMPRA_VENTA);
		});
		$("#ssavecomencompraventa").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			
			var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();
	   		var codempresa= $("#codigoEmpresa").val();
	   				   
			var iframe = document.getElementById("comencompraventa___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			var accionGraba=1;
			
			if(0 < <%=activoValidarEditor%>){ 	
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				//  {campoBlob:"comenComprasVentas", valorBlob:$("#stextareacomencompraventa").val()},
				//var dataGet="campoBlob=comenComprasVentas&valorBlob="+valblob;

					var dataGet={campoBlob:'comenComprasVentas',valorBlob:valblob};
					//guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_COMPRA_VENTA);
				
					if(tipoempresa == '2' ){ 
						   guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_COMPRA_VENTA);
					
					}else{
					      if (codempresa==codEprincipal){
							   	guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_COMPRA_VENTA);
					      }else{
							   	$.post("saveDatosBasicoBlob.do", 
				   			   {campoBlob:"comenComprasVentas", valorBlob:valblob, codEmpresa:codempresa},
						   		function(data){
						   			setTimeout($.unblockUI, 1); 
						   			resetTiempoSession();
			    				});
					      }			
					}
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activarmensajeAutograbar);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				//  {campoBlob:"comenComprasVentas", valorBlob:$("#stextareacomencompraventa").val()},
				//var dataGet="campoBlob=comenComprasVentas&valorBlob="+valblob;
				if (accionGraba==1 || accionGraba==2){
					var dataGet={campoBlob:'comenComprasVentas',valorBlob:valblob};
					//guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_COMPRA_VENTA);
				
					if(tipoempresa == '2' ){ 
						   guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_COMPRA_VENTA);
					
					}else{
					      if (codempresa==codEprincipal){
							   	guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_COMPRA_VENTA);
					      }else{
							   	$.post("saveDatosBasicoBlob.do", 
				   			   {campoBlob:"comenComprasVentas", valorBlob:valblob, codEmpresa:codempresa},
						   		function(data){
						   			setTimeout($.unblockUI, 1); 
						   			resetTiempoSession();
			    				});
					      }			
					}
				
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUAL("COMPRA-VENTAS"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}			
			
			}
			
		});
		$("#scleancomencompraventa").click(function () { 
			comenCompraVenta=1;
			//$('#stextareacomencompraventa').wysiwyg('setContent', '');
			var iframe = document.getElementById("comencompraventa___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}  
		});
		
		$("#sverificacomencompraventa").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });
			comenCompraVenta=1;
			var iframe = document.getElementById("comencompraventa___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});


		   $('#stextareadatosmatriz').wysiwyg({			
				rmUnusedControls: true,			
				controls: {				
				bold: { visible : true },				
				html: { visible : true },				
				insertOrderedList: { visible : true },				
				removeFormat: { visible : true }			
				}		
			});

		   $('#stextareadatosmatriz').wysiwyg("addControl", 
			   "UploadFile", {				
			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
			   tooltip: 'Upload',tags: ['Upload'],				
			   exec:  function() { 		   		
			   		vtextarea='stextareadatosmatriz'; 
			   		newWindowUploadFile(vtextarea);  }
		       }
		    );
		
		$("#bverdatosmatriz").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divdatosmatriz").attr("style","");
   			$("#ssavedatosmatriz").prop("disabled","");
   			$("#scleandatosmatriz").prop("disabled","");
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();
	   		var codempresa= $("#codigoEmpresa").val();
	   		comenDatosMatriz=1;
			if(tipoempresa == '2' ){ 
				$.post("consultarProgramaBlob.do", { campoBlob: "datosMatriz" },
			   		function(data){
			   		setTimeout($.unblockUI, 1);  
			   		//$('#stextareadatosmatriz').wysiwyg('setContent', data);
			   		var iframe = document.getElementById("datosmatriz___Frame");				
					var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
					var eInnerElement = oCell.firstChild;				
					if ( eInnerElement ){					
						eInnerElement.contentWindow.document.body.innerHTML = data;	
						resetTiempoSession();				
					}  			     	
			   });
			   
			}else{
			      if (codempresa==codEprincipal){
						$.post("consultarProgramaBlob.do", { campoBlob: "datosMatriz" },
					   		function(data){
					   		setTimeout($.unblockUI, 1);  
					   		//$('#stextareadatosmatriz').wysiwyg('setContent', data);
					   		var iframe = document.getElementById("datosmatriz___Frame");				
							var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
							var eInnerElement = oCell.firstChild;				
							if ( eInnerElement ){					
								eInnerElement.contentWindow.document.body.innerHTML = data;	
								resetTiempoSession();				
							}  			     	
					   });
			      }else{
						$.post("consultarDatosBasicoBlob.do", { campoBlob: "datosMatriz", codEmpresa:codempresa },
					   		function(data){
					   		setTimeout($.unblockUI, 1);  
					   		//$('#stextareadatosmatriz').wysiwyg('setContent', data);
					   		var iframe = document.getElementById("datosmatriz___Frame");				
							var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
							var eInnerElement = oCell.firstChild;				
							if ( eInnerElement ){					
								eInnerElement.contentWindow.document.body.innerHTML = data;	
								resetTiempoSession();				
							}  			     	
					   });
			      }			
			}
		   
	   		//editado(COMEN_DATOS_MATRIZ);
		});
		$("#ssavedatosmatriz").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			
			var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();
	   		var codempresa= $("#codigoEmpresa").val();
	   		
			var iframe = document.getElementById("datosmatriz___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			var accionGraba=1;
			if(0 < <%=activoValidarEditor%>){ 	
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				//{campoBlob:"datosMatriz", valorBlob:$("#stextareadatosmatriz").val()},
				//var dataGet="campoBlob=datosMatriz&valorBlob="+valblob;

					var dataGet={campoBlob:'datosMatriz',valorBlob:valblob};
					//guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_DATOS_MATRIZ);			
					if(tipoempresa == '2' ){ 
			   			   	guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_DATOS_MATRIZ);
		
					}else{
					      if (codempresa==codEprincipal){
							   	guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_DATOS_MATRIZ);
					
					      }else{
							   	$.post("saveDatosBasicoBlob.do", 
				   			   {campoBlob:"datosMatriz", valorBlob:valblob, codEmpresa:codempresa},
						   		function(data){
						   			setTimeout($.unblockUI, 1); 
						   			resetTiempoSession();
			    				});
					      }			
					}
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activarmensajeAutograbar);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				//{campoBlob:"datosMatriz", valorBlob:$("#stextareadatosmatriz").val()},
				//var dataGet="campoBlob=datosMatriz&valorBlob="+valblob;
				if (accionGraba==1 || accionGraba==2){
					var dataGet={campoBlob:'datosMatriz',valorBlob:valblob};
					//guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_DATOS_MATRIZ);			
					if(tipoempresa == '2' ){ 
			   			   	guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_DATOS_MATRIZ);
		
					}else{
					      if (codempresa==codEprincipal){
							   	guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_DATOS_MATRIZ);
					
					      }else{
							   	$.post("saveDatosBasicoBlob.do", 
				   			   {campoBlob:"datosMatriz", valorBlob:valblob, codEmpresa:codempresa},
						   		function(data){
						   			setTimeout($.unblockUI, 1); 
						   			resetTiempoSession();
			    				});
					      }			
					}
				
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUAL("DATOS MATRIZ"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}
			
		});
		$("#scleandatosmatriz").click(function () { 
			comenDatosMatriz=1;
			//$('#stextareadatosmatriz').wysiwyg('setContent', '');
			var iframe = document.getElementById("datosmatriz___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			} 
		});
		
		$("#sverificadatosmatriz").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });
			comenDatosMatriz=1;
			var iframe = document.getElementById("datosmatriz___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});

		   $('#stextareaespaciolibre').wysiwyg({			
				rmUnusedControls: true,			
				controls: {				
				bold: { visible : true },				
				html: { visible : true },				
				insertOrderedList: { visible : true },				
				removeFormat: { visible : true }			
				}		
			});

		   $('#stextareaespaciolibre').wysiwyg("addControl", 
			   "UploadFile", {				
			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
			   tooltip: 'Upload',tags: ['Upload'],				
			   exec:  function() { 		   		
			   		vtextarea='stextareaespaciolibre'; 
			   		newWindowUploadFile(vtextarea);  }
		       }
		    );
		
		
		
							
				
		$("#bverespaciolibre").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divespaciolibre").attr("style","");
   			$("#ssaveespaciolibre").prop("disabled","");
   			$("#scleanespaciolibre").prop("disabled","");
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();
	   		var codempresa= $("#codigoEmpresa").val();
	   		campoEspacioLibre=1;
			if(tipoempresa == '2' ){
				$.post("consultarProgramaBlob.do", { campoBlob: "espacioLibre" },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareaespaciolibre').wysiwyg('setContent', data);
				   		var iframe = document.getElementById("espaciolibre___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;
							resetTiempoSession();					
						}  	  			     	
				   });
			
			}else{
			      if (codempresa==codEprincipal){
						$.post("consultarProgramaBlob.do", { campoBlob: "espacioLibre" },
						   		function(data){
						   		setTimeout($.unblockUI, 1);  
						   		//$('#stextareaespaciolibre').wysiwyg('setContent', data);
						   		var iframe = document.getElementById("espaciolibre___Frame");				
								var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
								var eInnerElement = oCell.firstChild;				
								if ( eInnerElement ){					
									eInnerElement.contentWindow.document.body.innerHTML = data;	
									resetTiempoSession();				
								}  	  			     	
						   });
			
			      }else{
					$.post("consultarDatosBasicoBlob.do", { campoBlob: "espacioLibre" , codEmpresa:codempresa},
					   		function(data){
					   		setTimeout($.unblockUI, 1);  
					   		//$('#stextareaespaciolibre').wysiwyg('setContent', data);
					   		var iframe = document.getElementById("espaciolibre___Frame");				
							var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
							var eInnerElement = oCell.firstChild;				
							if ( eInnerElement ){					
								eInnerElement.contentWindow.document.body.innerHTML = data;	
								resetTiempoSession();				
							}  	  			     	
					  });
			
			      }			
			}
	   		//editado(COMEN_ESPACIO_LIBRE);
	   		
	   		
	   		
		});
		
		
		
		
		$("#ssaveespaciolibre").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 

			var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();
	   		var codempresa= $("#codigoEmpresa").val();		   					   
					   
					   
			var iframe = document.getElementById("espaciolibre___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			var accionGraba=1;
			if(0 < <%=activoValidarEditor%>){ 	
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				//{campoBlob:"espacioLibre", valorBlob:$("#stextareaespaciolibre").val()},
				
				//var dataGet="campoBlob=espacioLibre&valorBlob="+valblob;
					var dataGet={campoBlob:'espacioLibre',valorBlob:valblob};			
					if(tipoempresa == '2' ){ 
						   	guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_ESPACIO_LIBRE);
					
					}else{
					      if (codempresa==codEprincipal){
							   guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_ESPACIO_LIBRE);
					
					      }else{
							   	$.post("saveDatosBasicoBlob.do", 
				   			   {campoBlob:"espacioLibre", valorBlob:valblob, codEmpresa:codempresa},
						   		function(data){
						   			setTimeout($.unblockUI, 1); 
						   			resetTiempoSession();
			    				});			
					      }			
					}
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activarmensajeAutograbar);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				//{campoBlob:"espacioLibre", valorBlob:$("#stextareaespaciolibre").val()},
				
				//var dataGet="campoBlob=espacioLibre&valorBlob="+valblob;
				if (accionGraba==1 || accionGraba==2){
					var dataGet={campoBlob:'espacioLibre',valorBlob:valblob};			
					if(tipoempresa == '2' ){ 
						   	guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_ESPACIO_LIBRE);
					
					}else{
					      if (codempresa==codEprincipal){
							   guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_ESPACIO_LIBRE);
					
					      }else{
							   	$.post("saveDatosBasicoBlob.do", 
				   			   {campoBlob:"espacioLibre", valorBlob:valblob, codEmpresa:codempresa},
						   		function(data){
						   			setTimeout($.unblockUI, 1); 
						   			resetTiempoSession();
			    				});			
					      }			
					}
				
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUAL("NEGOCIO-BENEFICIO: ESPACIO LIBRE"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}
			
		});
		$("#scleanespaciolibre").click(function () { 
			campoEspacioLibre=1;
			//$('#stextareaespaciolibre').wysiwyg('setContent', '');
			var iframe = document.getElementById("espaciolibre___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}  
		});
		
		$("#sverificaespaciolibre").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });
			campoEspacioLibre=1;
			var iframe = document.getElementById("espaciolibre___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		
		$("#sbuscarAE").click(function(){
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Buscando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
			$.post("buscarCIIUAction.do", 
	   			   {},
			   		function(data){
			   			setTimeout($.unblockUI, 1); 
				   		var tipoempresa = $("input[name=tipo_empresa]").val();
				   		//mostrar un popup cuando el programa sea para un grupo
				   		if(tipoempresa == '3' ){
							$( "#dialog:ui-dialog" ).dialog( "destroy" );
							$( "#dialog-modal" ).dialog({
								height: 200,
								width: 500,
								modal: true
							});
				   			$("#divselecactividades").html(data);
				   			$("#dialog-modal").dialog();
				   		}else{
				   			$("#stextactividadPrincipal").val(data);
				   		}
				   		resetTiempoSession();
    				});
		});
		$("#baceptaractividades").click(function(){
			$("#stextactividadPrincipal").val(getValuesCheck("chkactividades"));
			$("#dialog-modal").dialog("close");
		});
		
	  $('select[name=codigoEmpresa]').change(function () {
	 	   var codempresa1= $("#codigoEmpresa").val();	
	 	   $.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } });	
	  		 onloadDatosBasico(codempresa1,'C');	    
   			$("#ssavesintesis").prop("disabled","true");
   			$("#scleansintesis").prop("disabled","true");

   			
  			
	    });
	    
	    		///mcg
		
		$('#stextareavaloracion').wysiwyg({			
			rmUnusedControls: true,			
			controls: {				
			bold: { visible : true },				
			html: { visible : true },				
			insertOrderedList: { visible : true },				
			removeFormat: { visible : true }			
			}		
		});

	   $('#stextareavaloracion').wysiwyg("addControl", 
		   "UploadFile", {				
		   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
		   tooltip: 'Upload',tags: ['Upload'],				
		   exec:  function() { 		   		
		   		vtextarea='stextareavaloracion'; 
		   		newWindowUploadFile(vtextarea);  }
	       }
	    );
   		
   		$("#bvervaloracion").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divvaloracion").attr("style","");
   			$("#ssavevaloracion").prop("disabled","");
   			$("#scleanvaloracion").prop("disabled","");
	   		
   			campoValoracionGlobal=1;
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();
	   		var codempresa= $("#codigoEmpresa").val();
			
			if(tipoempresa == '2' ){ 
					$.post("consultarProgramaBlob.do", { campoBlob: "valoracion" },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareavaloracion').wysiwyg('setContent', data);
				   		var iframe = document.getElementById("valoracion___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;	
							resetTiempoSession();				
						}  			  			     	
				   });
			
			}else{
			      if (codempresa==codEprincipal){
				      $.post("consultarProgramaBlob.do", { campoBlob: "valoracion" },
					   		function(data){
					   		setTimeout($.unblockUI, 1);  
					   		//$('#stextareavaloracion').wysiwyg('setContent', data);
					   		var iframe = document.getElementById("valoracion___Frame");				
							var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
							var eInnerElement = oCell.firstChild;				
							if ( eInnerElement ){					
								eInnerElement.contentWindow.document.body.innerHTML = data;	
								resetTiempoSession();				
							}  			  			     	
					   });
			
			      }else{
				      	$.post("consultarDatosBasicoBlob.do", { campoBlob: "valoracion", codEmpresa:codempresa },
					   		function(data){
					   		setTimeout($.unblockUI, 1);  
					   		//$('#stextareavaloracion').wysiwyg('setContent', data);
					   		var iframe = document.getElementById("valoracion___Frame");				
							var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
							var eInnerElement = oCell.firstChild;				
							if ( eInnerElement ){					
								eInnerElement.contentWindow.document.body.innerHTML = data;	
								resetTiempoSession();				
							}  			  			     	
					   });			
			      }			
			}	
	   		//editado(COMEN_VALORACION);
		});
		$("#ssavevaloracion").click(function () { 			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando. Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			
			var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();	   		
	   		var codempresa= $("#codigoEmpresa").val();	
			
			var iframe = document.getElementById("valoracion___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null; //false 0 true 1 
			var accionGraba=1;
			if(0 < <%=activoValidarEditor%>){ 	
				if ( eInnerElement ){
						valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}			
				//{campoBlob:"valoracion", valorBlob:$("#stextareavaloracion").val()},
	
					var dataGet={campoBlob:'valoracion',valorBlob:valblob};
					
					if(tipoempresa == '2' ){					 
						guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_VALORACION);
					
					}else{
					      if (codempresa==codEprincipal){				    	  
							guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_VALORACION);
					
					      }else{				    	  
					      	   $.post("saveDatosBasicoBlob.do", 
				   			   {campoBlob:"valoracion", valorBlob:valblob, codEmpresa:codempresa},
						   		function(data){
						   			setTimeout($.unblockUI, 1); 
						   			resetTiempoSession();
			    				});			
					      }			
					}
			}else{
				if ( eInnerElement ){
					accionGraba=validaGeneralHTMLRender(eInnerElement,activarmensajeAutograbar);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}			
				//{campoBlob:"valoracion", valorBlob:$("#stextareavaloracion").val()},
	
				if (accionGraba==1 || accionGraba==2){
					var dataGet={campoBlob:'valoracion',valorBlob:valblob};
					
					if(tipoempresa == '2' ){					 
						guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_VALORACION);
					
					}else{
					      if (codempresa==codEprincipal){				    	  
							guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_VALORACION);
					
					      }else{				    	  
					      	   $.post("saveDatosBasicoBlob.do", 
				   			   {campoBlob:"valoracion", valorBlob:valblob, codEmpresa:codempresa},
						   		function(data){
						   			setTimeout($.unblockUI, 1); 
						   			resetTiempoSession();
			    				});			
					      }			
					}
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUAL("VALORACION GLOBAL"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}
			
		});
		$("#scleanvaloracion").click(function () { 
			//$('#stextareavaloracion').wysiwyg('setContent', '');
			campoValoracionGlobal=1;
			var iframe = document.getElementById("valoracion___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}  
		});
		
		$("#sverificavaloracion").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });
			campoValoracionGlobal=1;
			var iframe = document.getElementById("valoracion___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		
		$("#idgrabarGeneral").click(function () { 
			showLoading();
				//sincronizado=1;
	    		//flagGuardado=true;
	    		//edicionCampos=1;
	    		activarmensajeAutograbar=1;
	    		var vtipoGrabacion="MANUAL";
	    		guardarFormulariosActualizadosPagina(null,vtipoGrabacion); 		
	    		
		});
		///mcg

	    addCalendar();
		
		var valorBtnPulsado= $("#btnPulsadoGrilla").val();		
		if (valorBtnPulsado=="AA"){
			desactivarFlag();
		}
		
	});
	function FCKeditor_OnComplete( editorInstance )
       {
               if (document.all) {        // If Internet Explorer.
                     editorInstance.EditorDocument.attachEvent("onkeydown", function(event){desactivarFlagEditor(editorInstance);} ) ;
               } else {                // If Gecko.
                     editorInstance.EditorDocument.addEventListener( 'keypress', function(event){desactivarFlagEditor(editorInstance);}, true ) ;
           	   }   			
       }
	
	function desactivarFlagEditor(editorInstance)
	{		
		if(editorInstance.Name=='sintesisEmpresa'){comenSinteEmpres=1; flagGuardado=false;sincronizado=0;idleTime=0;}
		if(editorInstance.Name=='datosmatriz'){comenDatosMatriz=1;flagGuardado=false;sincronizado=0;idleTime = 0;}
		if(editorInstance.Name=='comencompraventa'){comenCompraVenta=1;flagGuardado=false;sincronizado=0;idleTime = 0;}
		if(editorInstance.Name=='concentracion'){comenConcentracion=1;flagGuardado=false;sincronizado=0;idleTime = 0;}
		if(editorInstance.Name=='espaciolibre'){campoEspacioLibre=1;flagGuardado=false;sincronizado=0;idleTime = 0;}
		if(editorInstance.Name=='valoracion'){campoValoracionGlobal=1;flagGuardado=false;sincronizado=0;idleTime = 0;}
		
	}
</script>


 <s:include value="/pages/programa/opciones_programa.jsp"></s:include>
	<%@include file="/pages/programa/tabsCabecera.jsp" %>
	<script type="text/javascript">
			var flagGuardado = true;
	</script>
<div class="tab_container">
	<div class="seccion datosGrupoEmpresa"> 
		<%String tipo_empresa = GenericAction.getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
		String id_grupo="0";
		String cod_empresa_principal="0";
		if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
		id_grupo = GenericAction.getObjectSession(Constantes.COD_GRUPO_SESSION).toString();
		cod_empresa_principal= GenericAction.getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION).toString(); 
		%>
		<div>
		<label style="font-size: 12pt;font-weight: bold;">Grupo: <%=request.getSession().getAttribute("nombre_empresa_grupo_session")%></label>
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
    <div id="tab1" class="tab_content">
		<s:form action="updateDBAction" id="datosBasicos" onsubmit="SaveScrollXY(this);" theme="simple" >
		<input name="scrollX" id="scrollX" type="hidden"  />
		<input name="scrollY" id="scrollY" type="hidden"  />
		<input type="hidden" name ="tipo_empresa" value="<%=tipo_empresa%>"/>
		<input type="hidden" name ="id_grupo" value="<%=id_grupo%>"/>
		<input type="hidden" name ="cod_empresaPrincipal" value="<%=cod_empresa_principal%>"/>
		<input name="codempresagrupo" id="codempresagrupo" type="hidden"  />
		<input name="flagChangeEmpresa" id="flagChangeEmpresa" type="hidden"  />
		<s:hidden name="btnPulsadoGrilla" id="btnPulsadoGrilla"></s:hidden>
		
	
		
		<%if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){ %>
		<div>
		<table>
			<tr>
				<td>
					Empresa del Grupo:
				</td>
				<td>
					<s:select id="codigoEmpresa"  name="codigoEmpresa" list="listaEmpresasGrupoDB" listKey="codigo" listValue="nombre" theme="simple" ></s:select>
				</td>
			</tr>
		</table>
		</div>
		<%} %>
		
		<table width="100%">
			<tr>
				<td class="bk_tabs">
					<table class="ln_formatos" cellspacing="0" width="100%">
						<tr>
							<td align="left">
								<s:submit value="Guardar" 
								onclick="return validarSeleccioneDatosBasicos('.cssvalordivisa')" 
								cssClass="btn" theme="simple"></s:submit>
							</td>													
							<td align="right">
								<div id="idmsnAutoGuardado"></div>
							</td>
						</tr>		
						<tr>
							<td colspan="2">
								<label class = "subTitulo">Actividad Principal</label>
								<input type="button" id="sbuscarAE" value="Buscar" class="btn"></input>					
							</td>
						</tr>
					</table>
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td>
								<s:textarea name="programa.actividadPrincipal" 
											rows="3" 
											cols="100"
											theme="simple"
											id="stextactividadPrincipal" onkeypress="desactivarFlag();"></s:textarea>
							</td>
						</tr>
					</table>

					<table class="ln_formatos" cellspacing="0" >							
						<tr>							
							<td class="label">
								Pais:
							</td>
							<td >
							
								<s:textfield name="programa.pais"
											 id="tfpais" 
											 label="Pais"											 										 
											 readonly="true"/>
							</td>
							<td class="label">
							<s:submit onclick="mostrarRefresh();" theme="simple" action="refreshDatosBasicos" value="Refrescar" cssClass="btn"></s:submit>
							</td>
						</tr>
						<tr>
							<td class ="label">
							Antiguedad Negocio:
							</td>
							<td>
								<s:textfield name="programa.antiguedadNegocio"
											 id="tfantiguenegocio" 
											 label="Antiguedad Negocio"											 										 
											 readonly="true"/> <b>Años</b>
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td class ="label">
								Antiguedad Cliente:
							</td>
							<td>
								<s:textfield name="programa.antiguedadCliente"
											 id="tfantiguecliente" 
											 label="Antiguedad Cliente"
											 readonly="true"/> <b>Años</b>
							</td>
							<td>
							</td>
						</tr>
						<%if(tipoPrograma == Long.parseLong(Constantes.ID_TIPO_PROGRAMA_LOCAL.toString())){%>
							<tr>
								<td class ="label">
									Grupo Buro:
								</td>
								<td>
									<s:textfield name="programa.grupoRiesgoBuro"
											 id="tfgrupobureau" 
											 readonly="true"
											 size="5"/>
								</td>
								<td>
								</td>	
							</tr>
					<%}%>
					</table>
					
							<div class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagPlanilla"   id="chkPlanilla"/>Planilla</div>
							<div id="divPlanilla" style="display: none" class="my_div " >
									<table width="100%" cellspacing="0" class="ui-widget ui-widget-content" >
										<thead>

											<tr class="ui-widget-header ">
												<th>
													&nbsp;
												</th>
												<th>
													&nbsp;
												</th>
												<s:iterator var="pct" value="listaCabTablaPlanilla" id="cabtabla" status="rowstatus">													

														<td>															
															<s:hidden name="listaCabTablaPlanilla[%{#rowstatus.index}].id"></s:hidden>
															<s:hidden name="listaCabTablaPlanilla[%{#rowstatus.index}].posicion"></s:hidden>
										                	<s:textfield label="cabeceraTabla"  size="15"  
										                				 name="listaCabTablaPlanilla[%{#rowstatus.index}].cabeceraTabla" value="%{cabeceraTabla}" 
										                				 theme="simple"  cssClass="validate[required]" onkeypress="desactivarFlag();" maxlength="59"/>
										             	</td>																
												</s:iterator>
											</tr>
										</thead>
										<tbody>
									        <tr>
									        	<td>
									                   &nbsp;
									                   <s:hidden name="planillaAdmin.id"></s:hidden>
									            </td>
									            <td>
									                	<s:label  value="Planilla" theme="simple"/>
									            </td>
									            <td>
									                    <s:textfield name="planillaAdmin.total1"  theme="simple" size="8" cssClass="col1" onkeypress="return acceptNum(event);" maxlength="7" onblur="this.value = validarSiNumero(this.value);"/>
									            </td>
									            <td>
									                    <s:textfield name="planillaAdmin.total2"  theme="simple" size="8" cssClass="col2" onkeypress="return acceptNum(event);" maxlength="7" onblur="this.value = validarSiNumero(this.value);"/>
									            </td>
									            <td>
									                    <s:textfield name="planillaAdmin.total3"  theme="simple" size="8" cssClass="col3" onkeypress="return acceptNum(event);" maxlength="7" onblur="this.value = validarSiNumero(this.value);"/>
									            </td>
									            <td>
									                    <s:textfield name="planillaAdmin.total4"  theme="simple" size="8" cssClass="col4" onkeypress="return acceptNum(event);" maxlength="7" onblur="this.value = validarSiNumero(this.value);"/>
									            </td>
									        </tr>
									        <tr style="display: none">
									        	<td>
									                   &nbsp;
									                   <s:hidden name="planillaNoAdmin.id"></s:hidden>
									            </td>
									            <td>
									                	<s:label  value="Planilla No Administrativo" theme="simple"/>
									            </td>
									            <td>
									                    <s:textfield name="planillaNoAdmin.total1"  theme="simple" size="8" cssClass="col1" onkeypress="return acceptNum(event);" maxlength="7" onblur="this.value = validarSiNumero(this.value);"/>
									            </td>
									            <td>
									                    <s:textfield name="planillaNoAdmin.total2"  theme="simple" size="8" cssClass="col2" onkeypress="return acceptNum(event);" maxlength="7" onblur="this.value = validarSiNumero(this.value);"/>
									            </td>
									            <td>
									                    <s:textfield name="planillaNoAdmin.total3"  theme="simple" size="8" cssClass="col3" onkeypress="return acceptNum(event);" maxlength="7" onblur="this.value = validarSiNumero(this.value);"/>
									            </td>
									            <td>
									                    <s:textfield name="planillaNoAdmin.total4"  theme="simple" size="8" cssClass="col4" onkeypress="return acceptNum(event);" maxlength="7" onblur="this.value = validarSiNumero(this.value);"/>
									            </td>
									        </tr>
									        <tr style="display: none">
									        	<td>
									                   &nbsp;
									                    <s:hidden name="totalPlanilla.id"></s:hidden>
									            </td>
									        	<td>
									        		Total
									        	</td>
									        	<td>
									        		<s:textfield name="totalPlanilla.total1"  theme="simple" size="8" id="total1" readonly="true" cssStyle="text-align: right;" maxlength="8"/>
									        	</td>
									        	<td>
									        		<s:textfield name="totalPlanilla.total2" theme="simple" size="8" id="total2" readonly="true" cssStyle="text-align: right;" maxlength="8"/>
									        	</td>
									        	<td>
									        		<s:textfield name="totalPlanilla.total3" theme="simple" size="8" id="total3" readonly="true" cssStyle="text-align: right;" maxlength="8"/>
									        	</td>
									        	<td>
									        		<s:textfield name="totalPlanilla.total4" theme="simple" size="8" id="total4" readonly="true" cssStyle="text-align: right;" maxlength="8"/>
									        	</td>
									        </tr>
								        </tbody>
								   	</table>
							</div>
						
						<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagSintesisEmpresa"   id="chkSintesisEmpresa"/>Sintesis Empresa</h3>
						<div id="divSintesisEmpresa" style="display: none" class="">
							<table class="ln_formatos" cellspacing="0">
								<tr>
									<td>
										<label class = "subTitulo">Sintesis Empresa</label>
									</td>	
									<td>
										<input type="button" id="bversintesis"  value="Ver"  class="btn" />
									</td>	
									<td>
										<input type="button" value="Guardar" id="ssavesintesis" disabled="disabled"  class="btn"/>
									</td>	
									<td>
										<input type="button" value="Limpiar" id="scleansintesis" disabled="disabled"  class="btn"/>
									</td>
									<td>
										<input type="button" value="Validar" id="sverificasintesis" class="btn"/>
									</td>	
								</tr>
							</table>
							<div id="divsintesisempresa" style="display:none;"> 
<!--								<table class="ln_formatos" cellspacing="0">-->
<!--									<tr>-->
<!--										<td>-->
<!--											<s:textarea name="sintesisEmpresa"  id="stextareasintesis" cssClass="wysiwyg" rows="5" cols="100"></s:textarea>-->
<!--										</td>-->
<!--									</tr>-->
<!--								</table>-->
								<FCK:editor instanceName="sintesisEmpresa" height="250px">
									<jsp:attribute name="value">&nbsp;
									</jsp:attribute>									
									
								</FCK:editor>
							</div>
						</div>
						<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagDatosMatriz"   id="chkDatosMatriz"/>Datos Matriz</h3>
						<div id="divDatosMatriz" style="display: none" class="">
							<table class="ln_formatos" cellspacing="0">
								<tr>
									<td>
										<label class = "subTitulo">Datos Matriz</label>
									</td>	
									<td>
										<input type="button" id="bverdatosmatriz"  value="Ver"  class="btn"/>
									</td>	
									<td>
										<input type="button" value="Guardar" id="ssavedatosmatriz" disabled="disabled"  class="btn"/>
									</td>	
									<td>
										<input type="button" value="Limpiar" id="scleandatosmatriz" disabled="disabled"  class="btn"/>
									</td>	
									<td>
										<input type="button" value="Validar" id="sverificadatosmatriz"  class="btn"/>
									</td>
								</tr>
							</table>
							<div id="divdatosmatriz" style="display:none;"> 
<!--								<table class="ln_formatos" cellspacing="0">-->
<!--									<tr>-->
<!--										<td>-->
<!--											<s:textarea name="datosMatriz"  id="stextareadatosmatriz" cssClass="wysiwyg" rows="5" cols="100"></s:textarea>-->
<!--										</td>-->
<!--									</tr>-->
<!--								</table>-->
								<FCK:editor instanceName="datosmatriz" height="250px">
									<jsp:attribute name="value">&nbsp;
									</jsp:attribute>									
									
								</FCK:editor>
							</div>
						</div>
						<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagAccionariado"   id="chkAccionariado"/>Accionariado</h3>
						<div id="divAccionariado" style="display: none"  class="my_div " role="tabpanel">
						<div>
								
								
						</div>
						<table cellspacing="0">
							<tr>
								<td>
		
									<s:set name="varmetodo" value="metodo"/>
									<table class="ui-widget ui-widget-content">
									<thead>
										<tr>
											<th colspan="3" align="left">
											<s:submit theme="simple" action="addAccionista" value="+"  cssClass="btn"></s:submit>
								
											<s:submit theme="simple" action="deleteAccionista" value="-" onclick="return confirmDelete();"   cssClass="btn"></s:submit>
											
											<s:submit id="idsaveAllAccionistas" theme="simple" action="saveAllAccionistas" value="Guardar Lista" onclick="return validaElementosAccionista('.porcenAcci','.clsnombreAcci')"   cssClass="btn"></s:submit>
											</th>
											<th colspan="2" align="left">Búsqueda automática de accionista por tipo de documento</th>
											
											<th colspan="3" align="left">
												<div id="idmsnAcctividad"></div>
											</th>
										</tr>
										<tr class="ui-widget-header ">
											<th>
												&nbsp;
											</th>
											<th>
												Tipo Documento
											</th>
											<th>
												Número Documento
											</th>
											<!-- <th>
												Codigo Central
											</th> -->
											<th>
												Principales Accionistas
											</th>
											<th>
												Edad / Ant.Negocio
											</th>
											<th>
												%
											</th>
											<th>
												Nacionalidad
											</th>
											<th>
												Información Adicional
											</th>
										</tr>
									</thead>			
												<s:iterator var="p" value="listaAccionistas" id="accionista" status="rowstatus">
													<tr>
														<td>
															
															<input type="checkbox" value="<s:property  value="%{#rowstatus.index}"/>" name="chksAccionistas"></input>
															<s:hidden name="listaAccionistas.id" value="%{id}" ></s:hidden>
														</td>
														<td>
											       			<s:select
																name="listaAccionistas[%{#rowstatus.index}].tipoDocumento.id"
																cssClass="cssvalortipoDoc"
																list="itemTipoDocumento" listKey="value" listValue="label" cssStyle="width:100px">
															</s:select>												
									       				</td>
														<td>
										                	<s:textfield label="numeroDocumento"  size="15"  
										                				 name="listaAccionistas[%{#rowstatus.index}].numeroDocumento" value="%{numeroDocumento}" 
										                				 theme="simple"  maxlength="11" 										                				 
										                				 onkeypress="return obtenerDatoAccionista(event,'%{#rowstatus.index}');"
										                				 onblur="javascript:obtenerDatoAccionistaOnblur('%{#rowstatus.index}');"
										                				 />
										             	</td>								       				
														<td>
										                	<s:textfield label="nombre"  size="40"  
										                				 name="listaAccionistas[%{#rowstatus.index}].nombre" value="%{nombre}" 
										                				 theme="simple"  maxlength="59" 
										                				 readonly="false"
										                				 onkeypress="desactivarFlag();"/>
										             	</td>
										             	<td>
										                	<s:textfield label="edad"  size="2"  
										                				 name="listaAccionistas[%{#rowstatus.index}].edad" value="%{edad}" 
										                				 theme="simple"  maxlength="4" 
										                				 readonly="false"
										                				 onkeypress="desactivarFlag();"/>
										             	</td>
										             	<td>
										                	<s:textfield label="porcentaje" size="5" 
										                				 name="listaAccionistas[%{#rowstatus.index}].porcentaje" 
										                				 value="%{porcentaje}" theme="simple" 
										                				
										                				 cssClass="porcenAcci"
										                				 onkeypress="EvaluateText('%f', this);"
																	     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
																	     cssStyle="text-align:right"
																	     maxlength="6"
																	     />
										            	</td>
										            	<td>
										                	<s:textfield label="nacionalidad"  size="15"  
										                				 name="listaAccionistas[%{#rowstatus.index}].nacionalidad" 
										                				 value="%{nacionalidad}" theme="simple" maxlength="59" onkeypress="desactivarFlag();"/>
										            	</td>
										            	<td>
										                	<s:textfield label="capitalizacionBurs"  size="20" 
										                				 name="listaAccionistas[%{#rowstatus.index}].capitalizacionBurs" 
										                				 value="%{capitalizacionBurs}" theme="simple"
										                				 maxlength="59" onkeypress="desactivarFlag();"/>
										            	</td>
										            	<td>
										             		<s:hidden name="listaAccionistas[%{#rowstatus.index}].codigoCentral" value="%{codigoCentral}" />
										             		<s:hidden name="listaAccionistas[%{#rowstatus.index}].tipoNumeroDocumentoHost" value="%{tipoNumeroDocumentoHost}" />
										             	</td> 
													</tr>				
												</s:iterator>
												<tr>
													<td>
														&nbsp;
													</td>
													<td>
														&nbsp;
													</td>
													<td>
														&nbsp;
													</td>
													<td>
														&nbsp;
													</td>
													
													<td>
														&nbsp;
													</td>
													<td>
														TOTAL:
													</td>
													
													<td>
														<s:textfield name="totalAccionista" size="5" id="totalPorceAcci" theme="simple" readonly="true"
																	 cssStyle="text-align: right;" ></s:textfield>%
													</td>
													<td>
														&nbsp;
													</td>
													<td>
														&nbsp;
													</td>
												</tr>
									</table>
								</td>
							</tr>
						</table>
						<table class="ln_formatos" cellspacing="0" width="100%">
							<tr>
								<td>
									<label style="font-size: 8pt;font-weight: bold;">Comentario Accionariado:</label>
								</td>
							</tr>
							<tr>
								<td>
									<s:textarea name="programa.comentAccionariado"
												id="tanotasAccionariado"
												rows="3"
												cols="100"
												theme="simple" onkeypress="desactivarFlag();">
									</s:textarea>
								</td>	
							</tr>
						</table>
					</div>
										
					<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagCapitalizacion"   id="chkCapitalizacion"/>Capitalización Bursatil</h3>
						<div id="divCapitalizacion" style="display: none"  class="my_div " role="tabpanel">
						<div>
								
								
						</div>
						<table cellspacing="0">
							<tr>
								<td>
		
									<table class="ui-widget ui-widget-content">
									<thead>
										<tr >
											<th colspan="6" align="left">
											<s:submit theme="simple" action="addCapitalizacion" value="+"  cssClass="btn"></s:submit>
								
											<s:submit theme="simple" action="deleteCapitalizacion" value="-" onclick="return confirmDelete();"  cssClass="btn"></s:submit>
											
											<s:submit id="idsaveAllCapitalizacion" theme="simple" action="saveAllCapitalizacion" value="Guardar Lista" onclick="return existeElementosCapitalizacion('.porcenCapi','.cssvalordivisa')"  cssClass="btn"></s:submit>
											</th>
										</tr>
										<tr class="ui-widget-header ">
											<th>
												&nbsp;
											</th>
											<th>
												Divisa
											</th>
											<th>
												Importe
											</th>
											<th>
												% Fondos Propios
											</th>
											<th>
												Fecha(dd/mm/yyyy)
											</th>
											<th>
												Observación
											</th>
										</tr>
									</thead>			
												<s:iterator var="p" value="listaCapitalizacion" id="capitalizacion" status="rowstatus">
													<tr>
														<td>
															
															<input type="checkbox" value="<s:property  value="%{#rowstatus.index}"/>" name="chksCapitalizacion"></input>
															<s:hidden name="listaCapitalizacion.id" value="%{id}" ></s:hidden>
														</td>
														<td>
												           <s:select
																name="listaCapitalizacion[%{#rowstatus.index}].divisa.id"
																cssClass="cssvalordivisa"
																list="itemDivisaDB" listKey="value" listValue="label"  cssStyle="width:95px">
															</s:select>
										             	</td>						
														<td>
															<s:textfield label="importe" 
																			 name="listaCapitalizacion[%{#rowstatus.index}].importe" value="%{importe}"  
																 			 size="20"
																		     onkeypress="EvaluateText('%f', this);"
																		     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
																		     cssStyle="text-align:right"
																		     maxlength="20">
														    </s:textfield>
														</td>
										             	
										             	
										             	<td>
										                	<s:textfield label="fondosPropios" size="6" 
										                				 name="listaCapitalizacion[%{#rowstatus.index}].fondosPropios" 
										                				 value="%{fondosPropios}" theme="simple" 										                				
										                				 cssClass="porcenCapi"
										                				 onkeypress="EvaluateText('%f', this);"
																	     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
																	     cssStyle="text-align:right"
																	     maxlength="5"
																	     />
										            	</td>
													    <td>
												            
																			 
																<s:textfield theme="simple" name="listaCapitalizacion[%{#rowstatus.index}].fecha" 
																		 maxlength="10" value="%{fecha}" cssClass="cssidCalendardb" cssStyle="width:70px;" onblur="javascript:valFecha(this);"/>
												       	</td>
	
										            	<td>
										                	<s:textfield label="observacion"  size="80" 
										                				 name="listaCapitalizacion[%{#rowstatus.index}].observacion" 
										                				 value="%{observacion}" theme="simple"
										                				 maxlength="200" onkeypress="desactivarFlag();"/>
										            	</td>
													</tr>				
												</s:iterator>
									</table>
								</td>
							</tr>
						</table>
					</div>
														
					
					<% 
						if(tipoPrograma == Long.parseLong(Constantes.ID_TIPO_PROGRAMA_LOCAL.toString())){
					%>
					<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagPrincipalesEjecutivos"   id="chkPrincipalesEjecutivos"/>Principales Ejecutivos</h3>
					<div id="divPrincipalesEjecutivos" style="display: none;"  class="my_div ">
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td>
								<label class = "subTitulo">Principales Ejecutivos</label>
														
							</td>
						</tr>
					</table>
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td>
								<table class="ui-widget ui-widget-content">
								<thead>
									<tr>
										<th colspan="4" align="left">
										<s:submit theme="simple" action="addEjecutivo" value="+" cssClass="btn"></s:submit>
										<s:submit theme="simple" action="deleteEjecutivo" value="-" onclick="return confirmDelete();" cssClass="btn"></s:submit>
										<s:submit id="idsaveAllEjecutivo" theme="simple" action="saveAllEjecutivo" value="Guardar Lista" onclick="return existeElementos('.cssejecutivo')" cssClass="btn"></s:submit>
										</th>
									</tr>
									<tr class="ui-widget-header ">
										<th>
											&nbsp;
										</th>
										<th>
											Principales Ejecutivos
										</th>
										<th>
											Puesto
										</th>
										<th>
											Informacion Adicional
										</th>
									</tr>
								</thead>
									<s:iterator var="p" value="listaPrinciEjecutivos" id="ejecutivo" status="rowstatus">
										<tr>
											<td>
													<input type="checkbox" value="<s:property  value="%{#rowstatus.index}"/>" name="chkEjecutivos"></input>
												   <s:hidden name="listaPrinciEjecutivos.id" value="%{id}" ></s:hidden>
											</td>
											<td>
									              	<s:textfield label="nombre"  size="35"  
									              				 name="listaPrinciEjecutivos[%{#rowstatus.index}].nombres" value="%{nombres}" 
									              				 theme="simple"  
									              				 cssClass="validate[required]"
									              				 maxlength="59" onkeypress="desactivarFlag();"/>
									      	</td>
									       	<td>
									              	<s:textfield label="puesto" size="30" 
									              				 name="listaPrinciEjecutivos[%{#rowstatus.index}].puesto" 
									              				 value="%{puesto}" theme="simple" 
									              				 cssClass="cssejecutivo"
									              				 maxlength="59" onkeypress="desactivarFlag();"/>
									       	</td>
									       	<td>
									              	<s:textfield label="infomacionadicional"  size="45" 
																 name="listaPrinciEjecutivos[%{#rowstatus.index}].informacionAdcional" 
																 value="%{informacionAdcional}" theme="simple" maxlength="99" onkeypress="desactivarFlag();"/>
									       	</td>
										</tr>				
									</s:iterator>
								</table>
							</td>
						</tr>
					</table>
					<%} %>
					</div>
					<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagParticipacionesSignificativas"   id="chkParticipacionesSignificativas"/>Participaciones Significativas</h3>
					<div id="divParticipacionesSignificativas"  class="my_div " style="display: none">
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td>
								<label class = "subTitulo">Participaciones Significativas</label>
								
							</td>
						</tr>
					</table>
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td>
							<table class="ui-widget ui-widget-content">
							<thead>
								<tr>
									<th colspan="5" align="left">
										<s:submit theme="simple" action="addParticipaciones" value="+" cssClass="btn"></s:submit>
										<s:submit theme="simple" action="deleteParticipaciones" value="-" onclick="return confirmDelete();" cssClass="btn"></s:submit>
										<s:submit id="idsaveAllParticipaciones" theme="simple" action="saveAllParticipaciones" value="Guardar Lista" onclick="return existeElementos('.cssparticipaciones')" cssClass="btn"></s:submit>
									</th>
								</tr>
								<tr class="ui-widget-header ">
									<th>
										&nbsp;
									</th>
									<th>
										Principales Afiliadas
									</th>
									<th>
										%
									</th>
									<th>
										Consolidacion
									</th>
									<th>
										Sector Actividad
									</th>
								</tr>
							</thead>
								<s:iterator var="p" value="listaParticipaciones" id="participaciones" status="rowstatus">
									<tr>
										<td>
											<input type="checkbox" value="<s:property  value="%{#rowstatus.index}"/>" name="chkParticipaciones"></input>
											<s:hidden name="listaParticipaciones.id" value="%{id}" ></s:hidden>
										</td>
										<td>
								              	<s:textfield label="nombre"  size="45"  
								              				 name="listaParticipaciones[%{#rowstatus.index}].nombre" value="%{nombre}" 
								              				 theme="simple"  
								              				 cssClass="validate[required]"
								              				 maxlength="56" onkeypress="desactivarFlag();"/>
								      	</td>
								      	<td>
								              	<s:textfield label="porcentaje" size="6" 
								                			 name="listaParticipaciones[%{#rowstatus.index}].porcentaje" 
								                			 value="%{porcentaje}" theme="simple" 
								                			 onkeypress="EvaluateText('%f', this);"
															 onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															 cssStyle="text-align:right"
															 maxlength="3"/>
								      	</td>
								       	<td>
								              	<s:textfield label="consolidacion" size="20" 
								              				 name="listaParticipaciones[%{#rowstatus.index}].consolidacion" 
								              				 value="%{consolidacion}" theme="simple" 
								              				 cssClass="cssparticipaciones"
								              				 maxlength="59" onkeypress="desactivarFlag();"/>
								       	</td>
								       	<td>
								              	<s:textfield label="sectorActividad"  size="25" 
															 name="listaParticipaciones[%{#rowstatus.index}].sectorActividad" 
															 value="%{sectorActividad}" theme="simple"
															 maxlength="59" onkeypress="desactivarFlag();"/>
								       	</td>
									</tr>				
								</s:iterator>
							</table>
							</td>
						</tr>
					</table>
					<table class="ln_formatos" cellspacing="0" width="100%">
						<tr>
							<td>
								<label style="font-size: 8pt;font-weight: bold;">Comentario Participaciones Significativas:</label>
							</td>
						</tr>
						<tr>
							<td>
								<s:textarea name="programa.comentPartiSignificativa"
											id="tanotasParticipaciones"
											rows="3"
											cols="100"
											theme="simple" onkeypress="desactivarFlag();">
								</s:textarea>
							</td>	
						</tr>
					</table>
					</div>
					<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagRatingExterno"   id="chkRatingExterno"/>Rating Externo</h3>
					
					<div  class="my_div " id="divRatingExterno" style="display: none">
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td>
							<label class = "subTitulo">Rating Externo</label>
							
							</td>
						</tr>
					</table>
					
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td>
							<table class="ui-widget ui-widget-content" >
							<thead>
								<tr>
									<th colspan="8" align="left" >
										<s:submit theme="simple" action="addRatingExterno" value="+" cssClass="btn"></s:submit>
										<s:submit theme="simple" action="deleteRatingExterno" value="-" onclick="return confirmDelete();" cssClass="btn"></s:submit>
										<s:submit id="idsaveAllRatingExterno" theme="simple" action="saveAllRatingExterno" value="Guardar Lista" onclick="return existeElementos('.cssparticipaciones')" cssClass="btn"></s:submit>
									</th>
								</tr>
								<tr class="ui-widget-header ">
									<th>
										&nbsp;
									</th>
									<th>
										Compa&ntilde;ia Grupo
									</th>
									<th>
										Agencia
									</th>	
									<th>
										C/P
									</th>	
									<th>
										L/P
									</th>	
									<th>
										OutLook
									</th>	
									<th>
										Moneda
									</th>
									<th>
										Fecha(dd/mm/yyyy)
									</th>			
								</tr>
							</thead>
								<s:iterator var="p" value="listaRatingExterno" id="ratingExterno" status="rowstatus">
									<tr>
										<td>
												<input type="checkbox" value="<s:property  value="%{#rowstatus.index}"/>" name="chkRatingExterno"></input>
											   <s:hidden name="listaRatingExterno.id" value="%{id}" ></s:hidden>
										</td>
										<td>
								              	<s:textfield label="nombre"  size="25"  
								              				 name="listaRatingExterno[%{#rowstatus.index}].companiaGrupo" value="%{companiaGrupo}" 
								              				 theme="simple"  
								              				 cssClass="validate[required]"
								              				 maxlength="99" onkeypress="desactivarFlag();"/>
								      	</td>
								      	<td>
								              	<s:textfield label="agencia" size="5" 
								                			 name="listaRatingExterno[%{#rowstatus.index}].agencia" 
								                			 value="%{agencia}" theme="simple"
								                			 maxlength="59" onkeypress="desactivarFlag();"/>
								      	</td>
								       	<td>
								              	<s:textfield label="cp" size="5" 
								              				 name="listaRatingExterno[%{#rowstatus.index}].cp" 
								              				 value="%{cp}" theme="simple" 
								              				 cssClass="cssparticipaciones"
								              				 maxlength="14" onkeypress="desactivarFlag();"/>
								       	</td>
								       	<td>
								              	<s:textfield label="lp"  size="5" 
															 name="listaRatingExterno[%{#rowstatus.index}].lp" 
															 value="%{lp}" theme="simple"
															 maxlength="14" onkeypress="desactivarFlag();"/>
								       	</td>
								       	<td>
								              	<s:textfield label="outlook"  size="5" 
															 name="listaRatingExterno[%{#rowstatus.index}].outlook" 
															 value="%{outlook}" theme="simple"
															  maxlength="14" onkeypress="desactivarFlag();"/>
								       	</td>
								       	<td>
								              	<s:textfield label="moneda"  size="15" 
															 name="listaRatingExterno[%{#rowstatus.index}].moneda" 
															 value="%{moneda}" theme="simple"
															 maxLength="14" onkeypress="desactivarFlag();"/>
								       	</td>
								       	<td>
								              	<s:textfield label="fecha"  size="15" id="fechaRating"
															 name="listaRatingExterno[%{#rowstatus.index}].fecha" 
															 value="%{fecha}" theme="simple"
															 onblur="javascript:valFecha(document.datosBasicos.fechaRating);"
															 alt="date"
															 maxlength="9" onkeypress="desactivarFlag();"/>
								       	</td>
									</tr>				
								</s:iterator>
							</table>
							</td>
						</tr>
					</table>
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td>
								<label style="font-size: 8pt;font-weight: bold;">Comentario Rating Externo:</label>
							</td>
						</tr>
						<tr>
							<td>
								<s:textarea name="programa.comentRatinExterno"
											id="tanotasRatingExterno"
											rows="3"
											cols="100"
											theme="simple" onkeypress="desactivarFlag();">
								</s:textarea>
							</td>	
						</tr>
					</table>
					</div>
					
					<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagComprasVentas"   id="chkComprasVentas"/>Compras y Ventas</h3>
					<div  class="my_div " id="divComprasVentas" style="display: none">
			
						<table >
							<tr >
								<td>
									<table >
										<tr >
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td class="label">Total Importaciones</td>
										</tr>
										<tr>
											<td class="label">Importaciones ME</td>
										</tr>
										<tr>
											<td class="label">(Expresado en miles de USD)</td>
										</tr>
									</table>
								</td>
								<td>
								<div  class="my_div">
									<table class="ui-widget ui-widget-content">
									<thead>
										<tr class="ui-widget-header ">
											<th colspan="3">
												% Sobre el Total de Compras
											</th>
										</tr>

																				
										<tr class="ui-widget-header ">
												<s:iterator var="pc" value="listaCabTablaCompra" id="cabtablacompra" status="rowstatus">													

														<td>															
															<s:hidden name="listaCabTablaCompra[%{#rowstatus.index}].id"></s:hidden>
															<s:hidden name="listaCabTablaCompra[%{#rowstatus.index}].posicion"></s:hidden>
										                	<s:textfield label="cabeceraTablac" size="15"  
										                				 name="listaCabTablaCompra[%{#rowstatus.index}].cabeceraTabla" value="%{cabeceraTabla}" 
										                				 theme="simple"  cssClass="validate[required]" onkeypress="desactivarFlag();" maxlength="59"/>
										             	</td>																
												</s:iterator>
										</tr>
										
										
									</thead>
										<tr>
											<td>
												<s:hidden name="totalImportaciones.id"></s:hidden>
												<s:hidden name="totalImportaciones.tipo"></s:hidden>
												<s:textfield name="totalImportaciones.total1" theme="simple"  
														     size="6"
														     onkeypress="EvaluateText('%f', this);"
														     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     cssStyle="text-align:right"
														     maxlength="6"></s:textfield>%
											</td>
											<td>
												<s:textfield name="totalImportaciones.total2" theme="simple" 
												 			 size="6"
														     onkeypress="EvaluateText('%f', this);"
														     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     cssStyle="text-align:right"
														     maxlength="6"></s:textfield>%
											</td>
											<td>
												<s:textfield name="totalImportaciones.total3" theme="simple" 
												 			 size="6"
														     onkeypress="EvaluateText('%f', this);"
														     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     cssStyle="text-align:right"
														     maxlength="6"></s:textfield>%
											</td>
										</tr>
										<tr>
											<td>
												<s:hidden name="importacionesME.id"></s:hidden>
												<s:hidden name="importacionesME.tipo"></s:hidden>
												<s:textfield name="importacionesME.total1" theme="simple"  
															 size="15"
														     onkeypress="EvaluateText('%f', this);"
														     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     cssStyle="text-align:right"
														     maxlength="20"></s:textfield>
											</td>
											<td>
												<s:textfield name="importacionesME.total2" theme="simple"  
															 size="15"
														     onkeypress="EvaluateText('%f', this);"
														     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     cssStyle="text-align:right"
														     maxlength="20"></s:textfield>
											</td>
											<td>
												<s:textfield name="importacionesME.total3" theme="simple"  
															 size="15"
														     onkeypress="EvaluateText('%f', this);"
														     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     cssStyle="text-align:right"
														     maxlength="20"></s:textfield>
											</td>
										</tr>
									</table>
									</div>
								</td>
							</tr>
						</table>
						
						<table >
							<tr>
								<td>
									<table>
										<tr>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td class="label">Total Exportaciones</td>
										</tr>
										<tr>
											<td class="label">Exportaciones ME</td>
										</tr>
										<tr>
											<td class="label">(Expresado en miles de USD)</td>
										</tr>
									</table>
								</td>
								<td>
								<div  class="my_div">
									<table class="ui-widget ui-widget-content">
									<thead>
										<tr class="ui-widget-header ">
											<th colspan="3">
												% Sobre el Total de Ventas
											</th>
										</tr>

										
										<tr class="ui-widget-header ">
												<s:iterator var="pv" value="listaCabTablaVenta" id="cabtablaventa" status="rowstatus">													

														<td>															
															<s:hidden name="listaCabTablaVenta[%{#rowstatus.index}].id"></s:hidden>
															<s:hidden name="listaCabTablaVenta[%{#rowstatus.index}].posicion"></s:hidden>
										                	<s:textfield label="cabeceraTablav" size="15"  
										                				 name="listaCabTablaVenta[%{#rowstatus.index}].cabeceraTabla" value="%{cabeceraTabla}" 
										                				 theme="simple"  cssClass="validate[required]" onkeypress="desactivarFlag();" maxlength="59"/>
										             	</td>																
												</s:iterator>
										</tr>
										
									</thead>
										<tr>
											<td>
												<s:hidden name="totalExportaciones.id"></s:hidden>
												<s:hidden name="totalExportaciones.tipo"></s:hidden>
												<s:textfield name="totalExportaciones.total1" theme="simple"  
															 size="6"
														     onkeypress="EvaluateText('%f', this);"
														     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     cssStyle="text-align:right"
														     maxlength="6"></s:textfield>%
											</td>
											<td>
												<s:textfield name="totalExportaciones.total2" theme="simple"  
															 size="6"
														     onkeypress="EvaluateText('%f', this);"
														     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     cssStyle="text-align:right"
														     maxlength="6"></s:textfield>%
											</td>
											<td>
												<s:textfield name="totalExportaciones.total3" theme="simple"  
															 size="6"
														     onkeypress="EvaluateText('%f', this);"
														     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     cssStyle="text-align:right"
														     maxlength="6"></s:textfield>%
											</td>
										</tr>
										<tr>
											<td>
												<s:hidden name="exportacionesME.id"></s:hidden>
												<s:hidden name="exportacionesME.tipo"></s:hidden>
												<s:textfield name="exportacionesME.total1" theme="simple"  
															 size="15"
														     onkeypress="EvaluateText('%f', this);"
														     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     cssStyle="text-align:right"
														     maxlength="20"></s:textfield>
											</td>
											<td>
												<s:textfield name="exportacionesME.total2" theme="simple"  
															 size="15"
														     onkeypress="EvaluateText('%f', this);"
														     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     cssStyle="text-align:right"
														     maxlength="20"></s:textfield>
											</td>
											<td>
												<s:textfield name="exportacionesME.total3" theme="simple"  
															 size="15"
														     onkeypress="EvaluateText('%f', this);"
														     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     cssStyle="text-align:right"
														     maxlength="20"></s:textfield>
											</td>
										</tr>
									</table>
									</div>
								</td>
							</tr>
						</table>
						
						<div id="divComentarioCompraVenta" style="display: " class="">
							<table class="ln_formatos" cellspacing="0">
								<tr>
									<td>
										<label class = "subTitulo">Comentario Compras y Ventas</label>

										<input type="button" id="bvercomencompraventa"  value="Ver"  class="btn" />

										<input type="button" value="Guardar" id="ssavecomencompraventa" disabled="disabled"  class="btn"/>

										<input type="button" value="Limpiar" id="scleancomencompraventa" disabled="disabled"  class="btn"/>
										
										<input type="button" value="Validar" id="sverificacomencompraventa" class="btn"/>
									</td>	
								</tr>
							</table>
							<div id="divcomencompraventa" style="display:none;"> 
<!--								<table class="ln_formatos" cellspacing="0">-->
<!--									<tr>-->
<!--										<td>-->
<!--											<s:textarea name="comenComprasVentas"  id="stextareacomencompraventa" cssClass="wysiwyg" rows="5" cols="100"></s:textarea>-->
<!--										</td>-->
<!--									</tr>-->
<!--								</table>-->
								<FCK:editor instanceName="comencompraventa" height="250px">
									<jsp:attribute name="value">&nbsp;
									</jsp:attribute>									
									
								</FCK:editor>
							</div>
						</div>
						
						<div class="seccion "><a class="learn-more" href="http://www.adexdatatrade.com" target="_blank">ADEX-DATA TRADE</a></div>
					</div>									
					
					<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagConcentracion"   id="chkConcentracion"/>Concentracion</h3>
						<div id="divConcentracion" style="display: none" class="">
							<table class="ln_formatos" cellspacing="0">
								<tr>
									<td>
										<label class = "subTitulo">Concentracion</label>
									</td>	
									<td>
										<input type="button" id="bverconcentracion"  value="Ver"  class="btn" />
									</td>	
									<td>
										<input type="button" value="Guardar" id="ssaveconcentracion" disabled="disabled"  class="btn"/>
									</td>	
									<td>
										<input type="button" value="Limpiar" id="scleanconcentracion" disabled="disabled"  class="btn"/>
									</td>
									<td>
										<input type="button" value="Validar" id="sverificaconcentracion"   class="btn"/>
									</td>	
								</tr>
							</table>
							<div id="divconcentracion" style="display:none;"> 
<!--								<table class="ln_formatos" cellspacing="0">-->
<!--									<tr>-->
<!--										<td>-->
<!--											<s:textarea name="concentracion"  id="stextareaconcentracion" cssClass="wysiwyg" rows="5" cols="100"></s:textarea>-->
<!--										</td>-->
<!--									</tr>-->
<!--								</table>-->
								<FCK:editor instanceName="concentracion" height="250px">
									<jsp:attribute name="value">&nbsp;
									</jsp:attribute>									
									
								</FCK:editor>
							</div>
						</div>									
										
					<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagNegocioBeneficio"   id="chkNegocioBeneficio"/>Cuadro Negocio Beneficio</h3>
					<div  class="my_div " id="divNegocioBeneficio" style="display: none">
						<table>
							<tr>
								<td>
									<label class = "subTitulo">Cuadro Negocio Beneficio</label>
								</td>
							</tr>
						</table>
						
												<div  class="my_div">
						<table  class="ln_formatos">
							<tr>
								<td>
								
								<table  class="ui-widget ui-widget-content">
								<thead>
									<tr>
										<th colspan="8" align="left" >
											<s:submit theme="simple" action="addLineaNegocio" value="+" cssClass="btn"></s:submit>
											<s:submit theme="simple" action="deleteLineaNegocio" value="-" onclick="return confirmDelete();" cssClass="btn"></s:submit>
											<s:submit id="idsaveAllLineaNegocio" theme="simple" action="saveAllLineaNegocio" value="Guardar Lista" onclick="return existeElementos('.csslineanegocio')" cssClass="btn"></s:submit>
										</th>
									</tr>
									<tr class="ui-widget-header ">
										<th>
											&nbsp;
										</th>
										<th>
											Detalle Cifra de Negocio y Beneficio 
										</th>
										<th colspan="3">
											% de Ingresos 
										</th>
										<th  colspan="3">
											% de Beneficios 
										</th>
									</tr>

									
									<tr class="ui-widget-header ">
										<th>
											&nbsp;
										</th>
<!--										<th>-->
<!--											Por L&iacute;nea de Negocio  -->
<!--										</th>-->
												<% int contcbn=0;%>
												<s:iterator var="pn" value="listaCabTablaNegocio" id="cabtablanegocio" status="rowstatus">													

														<td>															
															<s:hidden name="listaCabTablaNegocio[%{#rowstatus.index}].id"></s:hidden>
															<s:hidden name="listaCabTablaNegocio[%{#rowstatus.index}].posicion"></s:hidden>
										                	<% if (contcbn==0) {%>
										                	<s:textfield label="cabeceraTablaneg" size="40"  
										                				 name="listaCabTablaNegocio[%{#rowstatus.index}].cabeceraTabla" value="%{cabeceraTabla}" 
										                				 theme="simple"  cssClass="validate[required]" onkeypress="desactivarFlag();"  maxlength="59"/>

															<% } else {%>
															<s:textfield label="cabeceraTablaneg" size="10"  
										                				 name="listaCabTablaNegocio[%{#rowstatus.index}].cabeceraTabla" value="%{cabeceraTabla}" 
										                				 theme="simple"  cssClass="validate[required]" onkeypress="desactivarFlag();" maxlength="59"/>
															<% }%>																				                	
										                <% contcbn=contcbn+1;%>										                
										             	</td>									             																
												</s:iterator>
									</tr>
								</thead>
									<s:iterator var="n" value="listaNBNegocio" id="negocio" status="rowstatus">
										<tr>
											<td>
												<input type="checkbox" value="<s:property  value="%{#rowstatus.index}"/>" name="chkNBNegocio"></input>
												<label class="csslineanegocio">&nbsp;</label>
											</td>
											<td>
												   <s:hidden name="listaNBNegocio.id" value="%{id}" ></s:hidden>
												   	<s:textfield label="detalle"  size="45"  
									              				 name="listaNBNegocio[%{#rowstatus.index}].descripcion" value="%{descripcion}" 
									              				 theme="simple"  
									              				 cssClass="validate[required]"
									              				 onkeypress="desactivarFlag();" 
									              				 maxlength="100"
									              				/>
											</td>
											<td>
									              	<s:textfield label="totalI2"  size="6"  
									              				 name="listaNBNegocio[%{#rowstatus.index}].totalI1" value="%{totalI1}" 
									              				 theme="simple"  
									              				 cssClass="tneg1"
									              				 onkeypress="EvaluateText('%f', this);"
															     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															     cssStyle="text-align:right"
															     maxlength="6" />
									      	</td>
									      	<td>
									              	<s:textfield label="totalI2" size="6" 
									                			 name="listaNBNegocio[%{#rowstatus.index}].totalI2" 
									                			 value="%{totalI2}" theme="simple"
									                			 cssClass="tneg2"
									                			 onkeypress="EvaluateText('%f', this);"
															     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															     cssStyle="text-align:right"
															     maxlength="6" />
									      	</td>
									       	<td>
									              	<s:textfield label="totalI3" size="6" 
									              				 name="listaNBNegocio[%{#rowstatus.index}].totalI3" 
									              				 value="%{totalI3}" theme="simple" 
									              				  cssClass="tneg3"
									              				 onkeypress="EvaluateText('%f', this);"
															     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															     cssStyle="text-align:right"
															     maxlength="6"/>
									       	</td>
									       	<td>
									              	<s:textfield label="totalB1"  size="6" 
																 name="listaNBNegocio[%{#rowstatus.index}].totalB1" 
																 value="%{totalB1}" theme="simple"
																 cssClass="tneg4"
																 onkeypress="EvaluateText('%f', this);"
															     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															     cssStyle="text-align:right"
															     maxlength="6"/>
									       	</td>
									       	<td>
									              	<s:textfield label="totalB2"  size="6" 
																 name="listaNBNegocio[%{#rowstatus.index}].totalB2" 
																 value="%{totalB2}" theme="simple"
																 cssClass="tneg5"
																 onkeypress="EvaluateText('%f', this);"
															     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															     cssStyle="text-align:right"
															     maxlength="6"/>
									       	</td>
									       	<td>
									              	<s:textfield label="totalB3"  size="6" 
																 name="listaNBNegocio[%{#rowstatus.index}].totalB3" 
																 value="%{totalB3}" theme="simple"
																 cssClass="tneg6"
																 onkeypress="EvaluateText('%f', this);"
															     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															     cssStyle="text-align:right"
															     maxlength="6"/>
									       	</td>
										</tr>				
									</s:iterator>
									<tr>
										<td>
											&nbsp;
										</td>
										<td>
											TOTAL:
										</td>
										<td >
											<s:textfield name="totalNegocio.totalI1" size="6" id="totalNI1" theme="simple" readonly="true" cssStyle="text-align:right" ></s:textfield>%
										</td>
										<td>
											<s:textfield name="totalNegocio.totalI2" size="6" id="totalNI2" theme="simple" readonly="true" cssStyle="text-align:right"></s:textfield>%
										</td>
										<td>
											<s:textfield name="totalNegocio.totalI3" size="6" id="totalNI3" theme="simple" readonly="true" cssStyle="text-align:right"></s:textfield>%
										</td>
										<td>
											<s:textfield name="totalNegocio.totalB1" size="6" id="totalNB1" theme="simple" readonly="true" cssStyle="text-align:right"></s:textfield>%
										</td>
										<td>
											<s:textfield name="totalNegocio.totalB2" size="6" id="totalNB2" theme="simple" readonly="true" cssStyle="text-align:right"></s:textfield>%
										</td>
										<td>
											<s:textfield name="totalNegocio.totalB3" size="6" id="totalNB3" theme="simple" readonly="true" cssStyle="text-align:right"></s:textfield>%
										</td>
									</tr>
								</table>
								</td>
							</tr>
						</table>
						</div>
					
						<table  class="ln_formatos">
							<tr>
								<td>
								<table  class="ui-widget ui-widget-content">
								<thead>
									<tr>
										<th colspan="8" align="left" >
											<s:submit theme="simple" action="addLineaActividad" value="+" cssClass="btn"></s:submit>
											<s:submit theme="simple" action="deleteLineaActividad" value="-" onclick="return confirmDelete();" cssClass="btn"></s:submit>
											<s:submit id="idsaveAllLineaActividad" theme="simple" action="saveAllLineaActividad" value="Guardar Lista" onclick="return existeElementos('.csslineaactividades')" cssClass="btn"></s:submit>
										</th>
									</tr>
									
									<tr class="ui-widget-header ">
										<th>
											&nbsp;
										</th>
										<th>
											Detalle Cifra de Negocio y Beneficio 
										</th>
										<th colspan="3">
											% de Ingresos 
										</th>
										<th  colspan="3">
											% de Beneficios 
										</th>
									</tr>

									
									
									<tr class="ui-widget-header ">
										<th>
											&nbsp;
										</th>
<!--										<th>-->
<!--											Por L&iacute;nea de Actividad -->
<!--										</th>-->
										<% int contcb=0;%>
												<s:iterator var="pa" value="listaCabTablaActividad" id="cabtablaactividad" status="rowstatus">													
														
														<td>															
															<s:hidden name="listaCabTablaActividad[%{#rowstatus.index}].id"></s:hidden>
															<s:hidden name="listaCabTablaActividad[%{#rowstatus.index}].posicion"></s:hidden>
															<% if (contcb==0) {%>
										                	<s:textfield label="cabeceraTablaact" size="40"  
										                				 name="listaCabTablaActividad[%{#rowstatus.index}].cabeceraTabla" value="%{cabeceraTabla}" 
										                				 theme="simple"  cssClass="validate[required]" onkeypress="desactivarFlag();" maxlength="59"/>
										                	<% } else {%>
										                	 <s:textfield label="cabeceraTablaact" size="10"  
										                				 name="listaCabTablaActividad[%{#rowstatus.index}].cabeceraTabla" value="%{cabeceraTabla}" 
										                				 theme="simple"  cssClass="validate[required]" onkeypress="desactivarFlag();" maxlength="59"/>
										                	<% }%>			 
										             	</td>	
										             	<% contcb=contcb+1;%>															
												</s:iterator>
									</tr>
									
									
									
								</thead>
									<s:iterator var="a" value="listaNBActividades" id="actividades" status="rowstatus">
										<tr>
											<td>
												<input type="checkbox" value="<s:property  value="%{#rowstatus.index}"/>" name="chkNBActividades"></input>
												<label class="csslineaactividades">&nbsp;</label>
											</td>
											<td>
												   <s:hidden name="listaNBActividades.id" value="%{id}" ></s:hidden>
												   <s:textfield label="detalle"  size="45"  
									              				 name="listaNBActividades[%{#rowstatus.index}].descripcion" value="%{descripcion}" 
									              				 theme="simple"  
									              				 cssClass="validate[required]"
									              				 onkeypress="desactivarFlag();"
									              				 maxlength="100"
									              				 />
											</td>
											<td>
									              	<s:textfield label="totalI2"  size="6"  
									              				 name="listaNBActividades[%{#rowstatus.index}].totalI1" value="%{totalI1}" 
									              				 theme="simple"  
									              				 cssClass="tact1"
															     onkeypress="EvaluateText('%f', this);"
															     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															     cssStyle="text-align:right"
															     maxlength="6"/>
									      	</td>
									      	<td>
									              	<s:textfield label="totalI2" size="6" 
									                			 name="listaNBActividades[%{#rowstatus.index}].totalI2" 
									                			 value="%{totalI2}" theme="simple"
									                			 cssClass="tact2" 
									                			 onkeypress="EvaluateText('%f', this);"
															     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															     cssStyle="text-align:right"
															     maxlength="6"/>
									      	</td>
									       	<td>
									              	<s:textfield label="totalI3" size="6" 
									              				 name="listaNBActividades[%{#rowstatus.index}].totalI3" 
									              				 value="%{totalI3}" theme="simple" 
									              				 cssClass="tact3"
									              				 onkeypress="EvaluateText('%f', this);"
															     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															     cssStyle="text-align:right"
															     maxlength="6"/>
									       	</td>
									       	<td>
									              	<s:textfield label="totalB1"  size="6" 
																 name="listaNBActividades[%{#rowstatus.index}].totalB1" 
																 value="%{totalB1}" theme="simple"
																 cssClass="tact4"
																 onkeypress="EvaluateText('%f', this);"
															     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															     cssStyle="text-align:right"
															     maxlength="6"/>
									       	</td>
									       	<td>
									              	<s:textfield label="totalB2"  size="6" 
																 name="listaNBActividades[%{#rowstatus.index}].totalB2" 
																 value="%{totalB2}" theme="simple"
																 cssClass="tact5"
																 onkeypress="EvaluateText('%f', this);"
															     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															     cssStyle="text-align:right"
															     maxlength="6"/>
									       	</td>
									       	<td>
									              	<s:textfield label="totalB3"  size="6" 
																 name="listaNBActividades[%{#rowstatus.index}].totalB3" 
																 value="%{totalB3}" theme="simple"
																 cssClass="tact6"
																 onkeypress="EvaluateText('%f', this);"
															     onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															     cssStyle="text-align:right"
															     maxlength="6"/>
									       	</td>
										</tr>				
									</s:iterator>
									<tr>
										<td>
											&nbsp;
										</td>
										<td>
											TOTAL:
										</td>
										<td>
											<s:textfield name="totalActividad.totalI1" size="6" id="totalAI1" theme="simple" readonly="true" cssStyle="text-align:right"></s:textfield>%
										</td>
										<td>
											<s:textfield name="totalActividad.totalI2" size="6" id="totalAI2" theme="simple" readonly="true" cssStyle="text-align:right"></s:textfield>%
										</td>
										<td>
											<s:textfield name="totalActividad.totalI3" size="6" id="totalAI3" theme="simple" readonly="true" cssStyle="text-align:right"></s:textfield>%
										</td>
										<td>
											<s:textfield name="totalActividad.totalB1" size="6" id="totalAB1" theme="simple" readonly="true" cssStyle="text-align:right"></s:textfield>%
										</td>
										<td>
											<s:textfield name="totalActividad.totalB2" size="6" id="totalAB2" theme="simple" readonly="true" cssStyle="text-align:right"></s:textfield>%
										</td>
										<td>
											<s:textfield name="totalActividad.totalB3" size="6" id="totalAB3" theme="simple" readonly="true" cssStyle="text-align:right"></s:textfield>%
										</td>
									</tr>
									
								</table>
								</td>
							</tr>
						</table>
						
						
						
							<div>
								<table class="ln_formatos" cellspacing="0">
									<tr>
										<td>
											<label class = "subTitulo">Espacio Libre</label>

											<input type="button" id="bverespaciolibre"  value="Ver" class="btn"/>

											<input type="button" value="Guardar" id="ssaveespaciolibre" disabled="disabled" class="btn"/>

											<input type="button" value="Limpiar" id="scleanespaciolibre" disabled="disabled" class="btn"/>
											
											<input type="button" value="Validar" id="sverificaespaciolibre"  class="btn"/>
										</td>	
									</tr>
								</table>
							</div>
							<div id="divespaciolibre" style="display:none;"> 
<!--								<table class="ln_formatos" cellspacing="0">-->
<!--									<tr>-->
<!--										<td>-->
<!--											<s:textarea name="espacioLibre"  id="stextareaespaciolibre" cssClass="wysiwyg" rows="5" cols="100"></s:textarea>-->
<!--										</td>-->
<!--									</tr>-->
<!--								</table>-->
								<FCK:editor instanceName="espaciolibre" height="250px">
									<jsp:attribute name="value">&nbsp;
									</jsp:attribute>									
									
								</FCK:editor>
							</div>
					</div>					
					
					<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagValoracion"   id="chkValoracion"/>Valoración Global</h3>
						<div id="divValoracion" style="display: none" class="">
							<table class="ln_formatos" cellspacing="0">
								<tr>
									<td>
										<label class = "subTitulo">Valoración Global</label>
									</td>	
									<td>
										<input type="button" id="bvervaloracion"  value="Ver"  class="btn" />
									</td>	
									<td>
										<input type="button" value="Guardar" id="ssavevaloracion" disabled="disabled"  class="btn"/>
									</td>	
									<td>
										<input type="button" value="Limpiar" id="scleanvaloracion" disabled="disabled"  class="btn"/>
									</td>
									<td>
										<input type="button" value="Validar" id="sverificavaloracion"  class="btn"/>
									</td>
								</tr>
							</table>
							<div id="divvaloracion" style="display:none;"> 
								<FCK:editor instanceName="valoracion" height="250px">
									<jsp:attribute name="value">&nbsp;
									</jsp:attribute>									
									
								</FCK:editor>
							</div>
						</div>									
<!--					<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagValoracionGlobal"   id="chkValoracionGlobal"/>Valoracion Global</h3>-->
<!--					<div  class="my_div " id="divValoracionGlobal" style="display: none">-->
<!--						<table class="ln_formatos" cellspacing="0">-->
<!--							<tr>-->
<!--								<td>-->
<!--									<label class = "subTitulo">Valoracion Global</label>-->
<!--								</td>-->
<!--							</tr>-->
<!--						</table>-->
<!--						<table class="ln_formatos" cellspacing="0">-->
<!--							<tr>-->
<!--								<td>-->
<!--								</td>	-->
<!--							</tr>-->
<!--						</table>-->
<!--					</div>-->
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td>
								<!--<s:submit value="Guardar" 
								onclick="return validarSeleccioneDatosBasicos('.cssvalordivisa')" 
								theme="simple" cssClass="btn"></s:submit>-->
							</td>
						</tr>		
					</table>
					</td>
			</tr>
		</table>
		   
		<!--  <div style="position:fixed !important; right:100px; top:720px; z-index:10 !important z-index:10 !important; border-radius: 15px;">
			<s:submit value="Guardar" 
								onclick="return validarSeleccioneDatosBasicos('.cssvalordivisa')" 
								theme="simple" cssClass="btn" cssStyle="width: 100px; height: 40px;"></s:submit>-->
		
		
		<div style="position:fixed !important; right:100px; top:400px; z-index:10 !important z-index:10 !important; border-radius: 15px;">
									
			<input type="button" value="Guardar" id="idgrabarGeneral" class="btn" Style="width: 100px; height: 40px;"/>
			<div id="idMenssageSave" style="display:none;"></div>
			
		</div>
		
		</s:form>
		
		<div id="dialog-modal" title="Seleccione las Actividades Económicas" style="display: none;">
			
			<div id="divselecactividades" >
						
			</div>
			<input type="button" value="Aceptar" id="baceptaractividades" class="btn" >
		</div>
	</div>
</div>


<script language="JavaScript">
var btnssavesintesis = document.getElementById("ssavesintesis");
var btnssavedatosmatriz = document.getElementById("ssavedatosmatriz");
var btnsaveAllAccionistas = document.getElementById("idsaveAllAccionistas");
var btnsaveAllCapitalizacion = document.getElementById("idsaveAllCapitalizacion");
var btnsaveAllEjecutivo = document.getElementById("idsaveAllEjecutivo");
var btnsaveAllParticipaciones = document.getElementById("idsaveAllParticipaciones");
var btnsaveAllRatingExterno= document.getElementById("idsaveAllRatingExterno");

var btnssavecomencompraventa= document.getElementById("ssavecomencompraventa");
var btnssaveconcentracion= document.getElementById("ssaveconcentracion");
var btnsaveAllLineaNegocio= document.getElementById("idsaveAllLineaNegocio");
var btnsaveAllLineaActividad= document.getElementById("idsaveAllLineaActividad");
var btnssaveespaciolibre= document.getElementById("ssaveespaciolibre");
var btnssavevaloracion= document.getElementById("ssavevaloracion");


var btnsverificasintesis = document.getElementById("sverificasintesis");
var btnsverificadatosmatriz = document.getElementById("sverificadatosmatriz");
var btnsverificacomencompraventa= document.getElementById("sverificacomencompraventa");
var btnsverificaconcentracion= document.getElementById("sverificaconcentracion");
var btnsverificaespaciolibre= document.getElementById("sverificaespaciolibre");
var btnsverificavaloracion= document.getElementById("sverificavaloracion");



	if(0 < <%=activoBtnGeneral%>){ 		
		btnssavesintesis.style.display = 'none'; // No ocupa espacio   
		btnssavedatosmatriz.style.display = 'none';  
		btnsaveAllAccionistas.style.display = 'none';
		btnsaveAllCapitalizacion.style.display = 'none'; 
		btnsaveAllEjecutivo.style.display = 'none'; 
		btnsaveAllParticipaciones.style.display = 'none';
		btnsaveAllRatingExterno.style.display = 'none';
		
		btnssavecomencompraventa.style.display = 'none';
		btnssaveconcentracion.style.display = 'none';
		btnsaveAllLineaNegocio.style.display = 'none';		
		btnsaveAllLineaActividad.style.display = 'none';
		btnssaveespaciolibre.style.display = 'none';
		btnssavevaloracion.style.display = 'none';
		
   }else{	   
	   btnssavesintesis.style.visibility  = 'visible'; // Se ve
	   btnssavedatosmatriz.style.visibility  = 'visible'; 
	   btnsaveAllAccionistas.style.visibility  = 'visible';
	   btnsaveAllCapitalizacion.style.visibility  = 'visible';
	   btnsaveAllEjecutivo.style.visibility  = 'visible';
	   btnsaveAllParticipaciones.style.visibility  = 'visible';
	   btnsaveAllRatingExterno.style.visibility  = 'visible';
	   
	   btnssavecomencompraventa.style.visibility  = 'visible';
	   btnssaveconcentracion.style.visibility  = 'visible';
	   btnsaveAllLineaNegocio.style.visibility  = 'visible';
	   btnsaveAllLineaActividad.style.visibility  = 'visible';
	   btnssaveespaciolibre.style.visibility  = 'visible';
	   btnssavevaloracion.style.visibility  = 'visible';
   }
	
//para boton de validacion

	if(0 < <%=activoBtnValidar%>){ 		
		   btnsverificasintesis.style.visibility  = 'visible'; // Se ve
		   btnsverificadatosmatriz.style.visibility  = 'visible';   
		   btnsverificacomencompraventa.style.visibility  = 'visible';
		   btnsverificaconcentracion.style.visibility  = 'visible';	   
		   btnsverificaespaciolibre.style.visibility  = 'visible';
		   btnsverificavaloracion.style.visibility  = 'visible';		
	
   }else{	
			btnsverificasintesis.style.display = 'none'; // No ocupa espacio   
			btnsverificadatosmatriz.style.display = 'none';		
			btnsverificacomencompraventa.style.display = 'none';
			btnsverificaconcentracion.style.display = 'none';		
			btnsverificaespaciolibre.style.display = 'none';
			btnsverificavaloracion.style.display = 'none';	

   }

   
	//var valorBtnPulsado = document.getElementById("idbtnPulsadoGrilla").value;

	
	
   
   		 	
</script>
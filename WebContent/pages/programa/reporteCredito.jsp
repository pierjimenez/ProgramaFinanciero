<%@taglib prefix="s" uri="/struts-tags" %>  
<%@taglib uri="http://java.fckeditor.net" prefix="FCK" %>


<%@page import="pe.com.bbva.iipf.util.Constantes"%>
<%@page import="java.util.List"%>
<%@page import="pe.com.bbva.iipf.pf.model.Parametro"%>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>

<% 
	String max_size_nota_vencimiento = GenericAction.getObjectParamtrosSession(Constantes.MAX_SIZE_TEXT_VENCIMIENTO).toString();
	String max_size_nota_clasecredito = GenericAction.getObjectParamtrosSession(Constantes.MAX_SIZE_TEXT_NOTA_CLASECRED).toString();
	String max_size_coment_garantia = GenericAction.getObjectParamtrosSession(Constantes.MAX_SIZE_TEXT_COMENT_GARANTIA).toString();
	String max_size_sustento_opera = GenericAction.getObjectParamtrosSession(Constantes.MAX_SIZE_TEXT_SUSTENTO_OPER).toString();
	String max_size_posicion_cliente= GenericAction.getObjectParamtrosSession(Constantes.MAX_SIZE_TEXT_POSICION_CLIENTE).toString();
	String max_size_vulnerabilidad= GenericAction.getObjectParamtrosSession(Constantes.MAX_SIZE_TEXT_VULNERABILIDAD).toString();
	String max_size_coment_admision= GenericAction.getObjectParamtrosSession(Constantes.MAX_SIZE_TEXT_COMENT_ADMISION).toString();
			
	String x = request.getAttribute("scrollX")==null?"ningun valor":request.getAttribute("scrollX").toString();
	String y = request.getAttribute("scrollY")==null?"ningun valor":request.getAttribute("scrollY").toString();
 %>
 <%
	String tipoEmpresa = GenericAction.getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION)==null?"":GenericAction.getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
 %>
<script language="JavaScript">
var TIPO_M='20305';
var dirurl='';
var vtextarea='';
var USD='20402';
var PEN='20401';
var TIPO_VENC = 'V';
var TIPO_REEM = 'R';

var comenDatosAdic=0;
var comenSintesisAdicional=0;

var COMEN_DATOS_ADIC=1;
var COMEN_SINTESIS_ADICIONAL=2;

	function editado(campo){
		if(campo==COMEN_DATOS_ADIC){
			comenDatosAdic=1;sincronizado=0;
		}else if(campo==COMEN_SINTESIS_ADICIONAL){
			comenSintesisAdicional=1;sincronizado=0;
		}
		return false;
		
	}

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
		if(editorInstance.Name=='datosBasicosAdicional'){comenDatosAdic=1; flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='sintesiseconfinadicional'){comenSintesisAdicional=1; flagGuardado=false;sincronizado=0;idleTime=0}
	    
	}
		
	function guardarFormulariosActualizadosPagina(uri){
	   	url=uri;
	   //	guardadoGeneral=1;
	   	sincrono=false;
		if(comenDatosAdic==1)						{		$("#ssavedatosbasicoadicionales").click(); 	}
		if(comenSintesisAdicional==1)				{		$("#ssavesintesiseconfinadicional").click(); 	}
		sincronizado = 1;
		cambiarPagina();
		
		return false;
	}
	
	function noEditado(campo){
		if(campo==COMEN_DATOS_ADIC){
			comenDatosAdic=0;
		}else if(campo==COMEN_SINTESIS_ADICIONAL){
			comenSintesisAdicional=0;
		}
		
		if(	comenDatosAdic==0 &
			comenSintesisAdicional==0){
			sincronizado=1;
		}
		return false;
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
	
		function existeElementos2(campos,campos2){
		var add = 0;
		$(campos).each(function() {
			add = add+1;
		});
		if(add==0){
			alert('No existen elementos para ser guardados...');
			return false;
		}

		var add1 = 0;
		var valor='';
		$(campos2).each(function() {			
			valor = $(this).val();						
			if (valor==''){
				add1 = add1+1;
			}
		});
		
		if(add1>0){
			alert('Seleccione la Cuenta en el cuadro de Garantia...');
			return false;
		}
		return true;
		
	}
	
	function existeElementos3(campos,campos2,campos3){
		var add = 0;
		$(campos).each(function() {
			add = add+1;
		});
		if(add==0){
			//alert('No existen elementos para ser guardados...');
			//return false;
		}

		var add1 = 0;
		var valor='';
		$(campos2).each(function() {			
			valor = $(this).val();						
			if (valor==''){
				add1 = add1+1;
			}
		});
		
		if(add1>0){
			alert('Seleccione la Cuenta en el cuadro de Garantia...');
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
			alert('Seleccione el conector en el cuadro de Garantia...');
			return false;
		}
		
		return true;
		
	}
	
		function validarSeleccioneEmpresa(campos2){
		var add1 = 0;
		var valor='';
		$(campos2).each(function() {			
			valor = $(this).val();						
			if (valor==''){
				add1 = add1+1;
			}
		});
		
		if(add1>0){
			alert('Seleccione la Cuenta en el Cuadro de Garantia...');
			return false;
		}
		return true;
	}
	
	
	function validarSeleccioneReporteCredito(camposx,campos2,campos3,campos4,campos5,campos6,campos7,campo8Conector){
	
		var add1 = 0;
		var valor1='';
		
		//var rvgl= $("#tfnumeroRVGL").val();
		//if (rvgl==''){
		//	alert('Ingrese número RVGL');
		//	return false;
		//}
		
		$(camposx).each(function() {			
			valor1 = $(this).val();						
			if (valor1==''){
				add1 = add1+1;
			}
		});
		
		if(add1>0){
			alert('Seleccione la Cuenta en el Cuadro de Clase de Credito...');
			return false;
		}
		
		
		var add2 = 0;
		var valor2='';
		$(campos2).each(function() {			
			valor2 = $(this).val();						
			if (valor2==''){
				add2 = add2+1;
			}
		});
		
		if(add2>0){
			alert('Seleccione la moneda en el Cuadro de Clase de Credito...');
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
			alert('Seleccione la Cuenta en el Cuadro de Garantias...');
			return false;
		}
		
		var add4 = 0;
		var valor4='';
		$(campos4).each(function() {			
			valor4 = $(this).val();						
			if (valor4==''){
				add4 = add4+1;
			}
		});
		
		if(add4>0){
			alert('Seleccione la Cuenta en el Cuadro de Sustento de Operación...');
			return false;
		}
		
		var add5 = 0;
		var valor5='';
		var fila = 0;
		$(campos5).each(function() {
		var el = document.getElementById("div"+fila);
		if ( el.style.display != 'none' ) {
			valor5 = $(this).val();							
			if (valor5==''){
				add5 = add4+1;
			}
		}
		fila = fila+1;
		});
		if(add5>0){
			alert('Seleccione Vencimiento/Reembolso en el cuadro de clase de credito...');
			return false;
		}
		
		var add6 = 0;
		var valor6='';
		var fila = 0;
		$(campos6).each(function() {
			var el = document.getElementById("divx"+fila);
			if ( el.style.display != 'none' ) {
			valor6 = $(this).val();					
			if (valor6==''){
				add6 = add5+1;
			}
		}
			fila = fila+1;	
		});
		if(add6>0){
			alert('Seleccione el Vencimiento/Reembolso en el cuadro de clase de credito...');
			return false;
		}
		
		var add7 = 0;
		var valor7='';
		var fila = 0;
		$(campos7).each(function() {
			valor7 = $(this).val();					
			if (valor7==''){
				add7 = add7+1;
			}
		});
		if(add7>0){
			alert('Seleccione el Tipo de Vencimiento en el cuadro de clase de credito...');
			return false;
		}
		
		//ini MCG20140610
		var add8 = 0;
		var valor8='';
		$(campo8Conector).each(function() {			
			valor8 = $(this).val();						
			if (valor8==''){
				add8 = add8+1;
			}
		});
		
		if(add8>0){
			alert('Seleccione el conector en el Cuadro de Garantias...');
			return false;
		}
		//fin MCG20140610
		
		
		return true;
	}
	
	
	
		function validarCuentaPrimer(campos){
		var add = 0;
		var band='TG';
		var valor='';		
		$(campos).each(function() {	
			add = add+1;		
			if (add==1){
				valor = $(this).val();				
				if (valor='TC'){
					band = 'TC';
				}
			}		
		});
		if(band=='TG'){
			alert('Es necesario ingresar primero la Cuenta...');
			return false;
		}
		return true;
	}
	
	function existeElementosCuadroClaseCred(campos,campos2,campos3,campos4,campos5,campos6){
		var add = 0;
		$(campos).each(function() {
			add = add+1;
		});
		if(add==0){
			alert('No existen elementos para ser guardados...');
			return false;
		}
		var add1 = 0;
		var valor='';
		$(campos2).each(function() {			
			valor = $(this).val();								
			if (valor==''){
				add1 = add1+1;
			}
		});
		
		if(add1>0){
			alert('Seleccione la Cuenta en el cuadro de clase de credito...');
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
			alert('Seleccione la Moneda en el cuadro de clase de credito...');
			return false;
		}
		
		var add4 = 0;
		var valor4='';
		var fila = 0;
		$(campos4).each(function() {
		var el = document.getElementById("div"+fila);
		if ( el.style.display != 'none' ) {
			valor4 = $(this).val();							
			if (valor4==''){
				add4 = add4+1;
			}
		}
		fila = fila+1;
		});
		if(add4>0){
			alert('Seleccione Vencimiento/Reembolso en el cuadro de clase de credito...');
			return false;
		}
		
		var add5 = 0;
		var valor5='';
		var fila = 0;
		$(campos5).each(function() {
			var el = document.getElementById("divx"+fila);
			if ( el.style.display != 'none' ) {
			valor5 = $(this).val();					
			if (valor5==''){
				add5 = add5+1;
			}
		}
			fila = fila+1;	
		});
		if(add5>0){
			alert('Seleccione el Vencimiento/Reembolso en el cuadro de clase de credito...');
			return false;
		}
		
		var add6 = 0;
		var valor6='';
		var fila = 0;
		$(campos6).each(function() {
			valor6 = $(this).val();					
			if (valor6==''){
				add6 = add6+1;
			}
		});
		if(add6>0){
			alert('Seleccione el Tipo de Vencimiento en el cuadro de clase de credito...');
			return false;
		}
		return true;
	}
	
		function existeElementosCuadroClaseCredCancel(campos,campos2,campos3,campos4,campos5,campos6){
		var add = 0;
		$(campos).each(function() {
			add = add+1;
		});
		if(add==0){
			alert('No existen elementos para ser cancelados...');
			return false;
		}
		var add1 = 0;
		var valor='';
		$(campos2).each(function() {			
			valor = $(this).val();								
			if (valor==''){
				add1 = add1+1;
			}
		});
		
		if(add1>0){
			alert('Seleccione la Cuenta en el cuadro de clase de credito...');
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
			alert('Seleccione la Moneda en el cuadro de clase de credito...');
			return false;
		}
		
		var add4 = 0;
		var valor4='';
		var fila = 0;
		$(campos4).each(function() {
		var el = document.getElementById("div"+fila);
		if ( el.style.display != 'none' ) {
			valor4 = $(this).val();							
			if (valor4==''){
				add4 = add4+1;
			}
		}
		fila = fila+1;
		});
		if(add4>0){
			alert('Seleccione Vencimiento en el cuadro de clase de credito...');
			return false;
		}
		
		var add5 = 0;
		var valor5='';
		var fila = 0;
		$(campos5).each(function() {
			var el = document.getElementById("divx"+fila);
			if ( el.style.display != 'none' ) {
			valor5 = $(this).val();						
			if (valor5==''){
				add5 = add5+1;
			}
		}	
		fila = fila+1;
		});
		if(add5>0){
			alert('Seleccione el Vencimiento/Reembolso en el cuadro de clase de credito...');
			return false;
		}
		
		var add6 = 0;
		var valor6='';
		var fila = 0;
		$(campos6).each(function() {
			valor6 = $(this).val();					
			if (valor6==''){
				add6 = add6+1;
			}
		});
		if(add6>0){
			alert('Seleccione el Tipo de Vencimiento en el cuadro de clase de credito...');
			return false;
		}
		return true;
	}
	
		function existeElementosCuadroSustentacion(campos,campos2){
		var add = 0;
		$(campos).each(function() {
			add = add+1;
		});
		if(add==0){
			alert('No existen elementos para ser guardados...');
			return false;
		}
		var add1 = 0;
		var valor='';
		$(campos2).each(function() {			
			valor = $(this).val();								
			if (valor==''){
				add1 = add1+1;
			}
		});
		
		if(add1>0){
			alert('Seleccione la Cuenta en el cuadro de Sustentación...');
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
	
	function onloadReporteCredito(codigo,flag){		
		document.forms["reporteCredito"].action='initReporteCredito.do';
		document.forms["reporteCredito"].codempresagrupo.value =codigo; 	
		document.forms["reporteCredito"].flagChangeEmpresa.value =flag; 		
		document.forms["reporteCredito"].submit();	
	}
	function mostrarRefreshRC(){
		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Actualizando. Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } }); 
	}
	
	function descargarArchivoRC(cod,ext, nom){		
		document.forms["reporteCredito"].action='downloadFileReporteCredito.do';
		document.forms["reporteCredito"].codigoArchivo.value =cod; 
		document.forms["reporteCredito"].extension.value=ext;
		document.forms["reporteCredito"].nombreArchivo.value=nom;
		document.forms["reporteCredito"].submit();	
		resetTiempoSession();		
	}
	
	function eliminarRC(id,ext,nomarch){
		document.forms["reporteCredito"].action='eliminarArchiReporteCredito.do';
		document.forms["reporteCredito"].codigoArchivo.value =id; 
		document.forms["reporteCredito"].extension.value =ext;
		document.forms["reporteCredito"].nombreArchivo.value =nomarch;
		document.forms["reporteCredito"].submit();	
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
	
$(window).load(function() { setTimeout($.unblockUI, 1);});

   $(document).ready(function() {
   
	   	 //When page loads...
	   	 //Hide (Collapse) the toggle containers on load
		 $(".toggle_container").hide(); 
		 //Switch the "Open" and "Close" state per click then slide up/down (depending on open/close state)
		 $("h2.trigger").click(function(){
			$(this).toggleClass("active").next().slideToggle("slow");
			return false; //Prevent the browser jump to the link anchor
		 });
		 
		 
	   	 
	
         if($("#chkDatosBasicosAdicionalesPrin").is(':checked')) {
            $("#divDatosBasicosAdicionalPrin").attr('style','display:');
         }  
         if($("#chkSintesisEconFinAdicional").is(':checked')) {
            $("#divSintesisEconFinAdicional").attr('style','display:');
         }
         if($("#chkClaseCreditos").is(':checked')) {
            $("#divClaseCreditos").attr('style','display:');
         }
         if($("#chkCuadroGarantias").is(':checked')) {
            $("#divCuadroGarantias").attr('style','display:');
         }         
         if($("#chkSustentos").is(':checked')) {
            $("#divSustentos").attr('style','display:');
         }     
         if($("#chkPosicionClientes").is(':checked')) {
            $("#divPosicionClientes").attr('style','display:');
         }
         
      	 
	 
		 $("#chkDatosBasicosAdicionalesPrin").click(function () {
			$("#divDatosBasicosAdicionalPrin").toggle("slow");
		 });   		 
		 $("#chkSintesisEconFinAdicional").click(function () {
			$("#divSintesisEconFinAdicional").toggle("slow");
		 });  
		 $("#chkClaseCreditos").click(function () {
			$("#divClaseCreditos").toggle("slow");
		 }); 		 
		 $("#chkCuadroGarantias").click(function () {
			$("#divCuadroGarantias").toggle("slow");
		 }); 		 
		 $("#chkSustentos").click(function () {
			$("#divSustentos").toggle("slow");
		 }); 	
		 $("#chkPosicionClientes").click(function () {
			$("#divPosicionClientes").toggle("slow");
		 }); 
		 
		 if($("#chkArchivoReporteCredito").is(':checked')) {
            $("#divArchivoReporteCredito").attr('style','display:');
        }
        
        $("#chkArchivoReporteCredito").click(function () {
			$("#divArchivoReporteCredito").toggle("slow");
		});  
	
	
		 
	   	 
		 $(".tab_content").hide(); //Hide all content
		 $("#li10").addClass("active").show(); //Activate first tab
		 $("#tab1").show(); //Show first tab content
   		 $("#reporteCredito").validationEngine();
  		    		
   		 $(".RCnumGar").css('text-align', 'right');
   		 $(".RCImporte").css('text-align', 'right');
   		 
   		 $(".RCriesgo").css('text-align', 'right');
   		 $("#tftotalInversion").css('text-align', 'right');
   		 $("#tfmontoPrestamo").css('text-align', 'right');
   		 $("#tfentorno").css('text-align', 'right');
   		 $("#tfpoblacionAfectada").css('text-align', 'right');
   		 
   		 
		 $(".tareanota").each(function() {
	        var self = $(this);
	        self.counter({
	            count: 'up',
				goal: <%=max_size_nota_clasecredito%>
	        });
	     });
		 
		 $(".tareacoment").each(function() {
		        var self = $(this);
		        self.counter({
				count: 'up',
				goal: <%=max_size_coment_garantia%>
		 	});
	     });
		 
   		 $("#stextvulnerabilidad").counter({
			count: 'up',
			goal: <%=max_size_vulnerabilidad%>
		 });

		$("#tacomentarioAdmision").counter({
			count: 'up',
			goal: <%=max_size_coment_admision%>
		 });		 
		 	   
		 
		 $( "#tffechaRDC" ).datepicker({ 
    							dateFormat: 'dd/mm/yy',
    							maxDate: "+0D",
    							showOn: "button",
							    buttonImage: "images/calendariobbva.png",
								buttonImageOnly: true });
    	$( "#tffechaRDC" ).datepicker();
		 
   		 	
   		 
   		var checkboxes = $(':checkbox[class=chkaccionista]');
		checkboxes.click(function(){
		  var self = this;
		  checkboxes.each(function(){
		    if(this!=self) this.checked = '';
		  });
		});
   		 
	
   		$("#bverdatosbasicoadicionales").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divdatosbasicosadicional").attr("style","");
   			$("#ssavedatosbasicoadicionales").prop("disabled","");
   			$("#scleandatosbasicoadicionales").prop("disabled","");
   			$("#idSincronizarDBA").prop("disabled","");
   			
   			var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();
	   		var codempresa= $("#codigoEmpresa").val();
   			
		   if(tipoempresa == '2' ){
		   		$.post("consultarProgramaBlob.do", { campoBlob: "datosBasicosAdicional" },
			   		function(data){
			   		setTimeout($.unblockUI, 1);  
			   		//$('#stextareasintesis').wysiwyg('setContent', data);
			   		var iframe = document.getElementById("datosBasicosAdicional___Frame");				
					var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
					var eInnerElement = oCell.firstChild;				
					if ( eInnerElement ){					
						eInnerElement.contentWindow.document.body.innerHTML = data;					
					}  			     	
			   	});
		    }else{
			     if (codempresa==codEprincipal){
	   		$.post("consultarProgramaBlob.do", { campoBlob: "datosBasicosAdicional" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
					   		//$('#stextareasintesis').wysiwyg('setContent', data);
					   		var iframe = document.getElementById("datosBasicosAdicional___Frame");				
							var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
							var eInnerElement = oCell.firstChild;				
							if ( eInnerElement ){					
								eInnerElement.contentWindow.document.body.innerHTML = data;					
							}  			     	
					   	});
		   		
			     }else{
			     		$.post("consultarDatosBasicoBlobRC.do", { campoBlob: "datosBasicosAdicional", codEmpresa:codempresa },
					   		function(data){
					   		setTimeout($.unblockUI, 1);  
					   		//$('#stextareasintesis').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("datosBasicosAdicional___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;					
				}  			     	
		   });
			     
			     }			
			}
			
	   		//editado(COMEN_DATOS_ADIC);
		});
		
		//mlj: Boton sincronizar de datos basicos adicionales
		$("#idSincronizarDBA").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divdatosbasicosadicional").attr("style","");
   			$("#ssavedatosbasicoadicionales").prop("disabled","");
   			$("#scleandatosbasicoadicionales").prop("disabled","");
   			
   			var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();
	   		var codempresa= $("#codigoEmpresa").val();
   			
		   if(tipoempresa == '2' ){
		   		$.post("consultarProgramaBlob.do", { campoBlob: "datosBasicosAdicionalSincronizar" },
			   		function(data){
			   		setTimeout($.unblockUI, 1);  
			   		//$('#stextareasintesis').wysiwyg('setContent', data);
			   		var iframe = document.getElementById("datosBasicosAdicional___Frame");				
					var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
					var eInnerElement = oCell.firstChild;				
					if ( eInnerElement ){					
						eInnerElement.contentWindow.document.body.innerHTML = data;					
					}  			     	
			   	});
		    }else{
			     if (codempresa==codEprincipal){
		   			$.post("consultarProgramaBlob.do", { campoBlob: "datosBasicosAdicionalSincronizar" },
			   		function(data){
			   			setTimeout($.unblockUI, 1);  
						   		//$('#stextareasintesis').wysiwyg('setContent', data);
						   		var iframe = document.getElementById("datosBasicosAdicional___Frame");				
								var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
								var eInnerElement = oCell.firstChild;				
								if ( eInnerElement ){					
									eInnerElement.contentWindow.document.body.innerHTML = data;					
								}  			     	
					});
		   		
			     }else{
			     		$.post("consultarDatosBasicoBlobRC.do", { campoBlob: "datosBasicosAdicionalSincronizar", codEmpresa:codempresa },
					   		function(data){
					   		setTimeout($.unblockUI, 1);  
					   		//$('#stextareasintesis').wysiwyg('setContent', data);
					   		var iframe = document.getElementById("datosBasicosAdicional___Frame");				
							var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
							var eInnerElement = oCell.firstChild;				
							if ( eInnerElement ){					
								eInnerElement.contentWindow.document.body.innerHTML = data;					
							}  			     	
					   });
			     
			     }			
			}
			
	   		editado(COMEN_DATOS_ADIC);
		});
		
		$("#ssavedatosbasicoadicionales").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
					   
			var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var codEprincipal = $("input[name=cod_empresaPrincipal]").val();	   		
	   		var codempresa= $("#codigoEmpresa").val();
					   
			var iframe = document.getElementById("datosBasicosAdicional___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			if ( eInnerElement ){
				validarEditor(eInnerElement);
				valblob = eInnerElement.contentWindow.document.body.innerHTML;
			}
			
			//var dataGet="campoBlob=datosBasicosAdicional&valorBlob="+valblob;
			var dataGet={campoBlob:'datosBasicosAdicional',valorBlob:valblob};
			if(tipoempresa == '2' ){						
				   	guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_DATOS_ADIC);
    		}else{ 				    						
			      if (codempresa==codEprincipal){	
			guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_DATOS_ADIC);
			      }else{			      
				      $.post("saveDatosBasicoBlobRC.do", 	  
			   			  {campoBlob:"datosBasicosAdicional", valorBlob:valblob, codEmpresa:codempresa},
					   		function(data){
					   			setTimeout($.unblockUI, 1); 
		    		  }); 
			
			      }					   			
			} 
			
		});
		$("#scleandatosbasicoadicionales").click(function () { 
			
			var iframe = document.getElementById("datosBasicosAdicional___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			} 
		});
				
		$("#bversintesiseconfinadicional").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divsintesiseconfinad").attr("style","");
   			$("#ssavesintesiseconfinadicional").prop("disabled","");
   			$("#scleansintesiseconfinadicional").prop("disabled","");
   			$("#idSincronizarSintesisA").prop("disabled","");
	   		
			var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var idgrupo = $("input[name=id_grupo]").val();
	   		var codempresa= $("#codigoEmpresa").val();
	   		
	   		if(tipoempresa == '2' ){ 
	   		$.post("consultarProgramaBlob.do", { campoBlob: "sintesisEconFinAdicional" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  		   		
		   		var iframe = document.getElementById("sintesiseconfinadicional___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;					
				}  			     	
		   });
		    }else{
			     
		   		$.post("consultarSintesisEconBlobRC.do", { campoBlob: "sintesisEconFinAdicional", codEmpresa:codempresa  },
			   		function(data){
			   		setTimeout($.unblockUI, 1);  
			   		var iframe = document.getElementById("sintesiseconfinadicional___Frame");				
					var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
					var eInnerElement = oCell.firstChild;				
					if ( eInnerElement ){					
						eInnerElement.contentWindow.document.body.innerHTML = data;					
					}  
			   });	
			}
   			
	   		//editado(COMEN_SINTESIS_ADICIONAL);
		});
		
		//mlj: Boton sinconizar para sintesis economica 
		$("#idSincronizarSintesisA").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divsintesiseconfinad").attr("style","");
   			$("#ssavesintesiseconfinadicional").prop("disabled","");
   			$("#scleansintesiseconfinadicional").prop("disabled","");
	   		
			var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var idgrupo = $("input[name=id_grupo]").val();
	   		var codempresa= $("#codigoEmpresa").val();
	   		
	   		if(tipoempresa == '2' ){ 
	   		$.post("consultarProgramaBlob.do", { campoBlob: "sintesisEconFinAdicionalSincronizar" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  		   		
		   		var iframe = document.getElementById("sintesiseconfinadicional___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;					
				}  			     	
		   });
		    }else{
			     
		   		$.post("consultarSintesisEconBlobRC.do", { campoBlob: "sintesisEconFinAdicionalSincronizar", codEmpresa:codempresa  },
			   		function(data){
			   		setTimeout($.unblockUI, 1);  
			   		var iframe = document.getElementById("sintesiseconfinadicional___Frame");				
					var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
					var eInnerElement = oCell.firstChild;				
					if ( eInnerElement ){					
						eInnerElement.contentWindow.document.body.innerHTML = data;					
					}  
			   });	
			}
   			
	   		editado(COMEN_SINTESIS_ADICIONAL);
		});
		
		$("#ssavesintesiseconfinadicional").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			
			var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var idgrupo = $("input[name=id_grupo]").val();	   		
	   		var codempresa= $("#codigoEmpresa").val();	
	   		
			var iframe = document.getElementById("sintesiseconfinadicional___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			if ( eInnerElement ){
				validarEditor(eInnerElement);
				valblob = eInnerElement.contentWindow.document.body.innerHTML;
			}
			
			if(tipoempresa == '2' ){
			//var dataGet="campoBlob=sintesisEconFinAdicional&valorBlob="+valblob;
			var dataGet={campoBlob:'sintesisEconFinAdicional',valorBlob:valblob};
			guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_DATOS_ADIC);
    		}else{
			      
	    	  	//var dataGet="campoBlob=sintesisEconFinAdicional&valorBlob="+valblob+"&codEmpresa="+codempresa;
	    	  	var dataGet={campoBlob:'sintesisEconFinAdicional',valorBlob:valblob,codEmpresa:codempresa};	    	  	
	   			guardarDatosTexto("saveSintesisEconBlobRC.do",dataGet,COMEN_DATOS_ADIC);
			}
		});
		$("#scleansintesiseconfinadicional").click(function () { 			
			var iframe = document.getElementById("sintesiseconfinadicional___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			} 
		});
		$(".garantiasOcultas."+TIPO_M).css("display", "");
			
		$('select[name=codigoEmpresa]').change(function () {
	 	   var codempresa1= $("#codigoEmpresa").val();	
	 	   $.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } });	
	  		 onloadReporteCredito(codempresa1,'C');	
	    });
	    $("#limpiarSustentoModal").click(function(){
	    	var iframe = document.getElementById("valorSustento___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}
			$("#sustentoOrden").val("");
			$("#sustentoCuentaId").val("");
			$("#sustentoOrdenFinal").val("");
			$("#sustentoCuentaIdFinal").val("");
	    });
	    $("#frmUpdateEditor").validationEngine();
	    
	    $("#btnGuardarSustento2").click(function () {
	    	var add1 = 0;
	    	if($("#sustentoOrden").val()==''){
				alert('Debe ingresar el orden por rango inicial');
				add1 = add1+1;
			}
			if($("#sustentoOrdenFinal").val()==''){
				alert('Debe ingresar el orden por rango final');
				add1 = add1+1;
			}
			if($("#sustentoCuentaId").val()==''){
				alert('Debe ingresar la cuenta');
				add1 = add1+1;
			}
			if($("#sustentoCuentaIdFinal").val()==''){
				alert('Debe ingresar la cuenta final');
				add1 = add1+1;
			}
	    	
	    	if(add1<1){
				document.forms["reporteCredito"].action='saveAllSustento.do';
				document.forms["reporteCredito"].hIdSustento.value =$("#sustentoId").val(); 	
				document.forms["reporteCredito"].hOrdenSustento.value =$("#sustentoOrden").val();
				document.forms["reporteCredito"].hOrdenFinalSustento.value =$("#sustentoOrdenFinal").val();
				document.forms["reporteCredito"].hCuentaSustento.value =$("#sustentoCuentaId").val();
				document.forms["reporteCredito"].hCuentaFinalSustento.value =$("#sustentoCuentaIdFinal").val();
				var dataSustento = '';
				var iframe = document.getElementById("valorSustento___Frame");			
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				if(oCell!=null){
					var eInnerElement = oCell.firstChild;								
					if ( eInnerElement ){					
						dataSustento = eInnerElement.contentWindow.document.body.innerHTML;					
					}
					document.forms["reporteCredito"].hValorSustento.value = dataSustento;
				}
				document.forms["reporteCredito"].submit();  	
			}
	    });
	    
		
		loadDataSustento();
		addCalendar();
		loadVctoReemblso();
		toggleRow();

	});
	
	function loadVctoReemblso(){
		var n=0;
		$(".cssvalortipo").each(function() {
			var data = $("#reporteCredito_listaClaseCredito_"+n+"__tipoVcto").val();
			if(data == TIPO_VENC){
				$("#div"+n).show();
	 			$("#divx"+n).hide();
	 			n=n+1;
			}else if (data == TIPO_REEM){
				$("#div"+n).hide();
		 		$("#divx"+n).show();
	 			n=n+1;
			}else{
				$("#div"+n).hide();
		 		$("#divx"+n).hide();
		 		n=n+1;
			}
		});
	}
	
	function changeReembolso(){
	  	var i=0;
		var tipoControl= $('select[name="tipoVctoRblso"]').val();
		if(tipoControl == TIPO_VENC){
		var i=0;
		$(".cssidCalendar").each(function() {
			$("#reporteCredito_listaClaseCredito_"+i+"__vencimiento").val("");
			$("#reporteCredito_listaClaseCredito_"+i+"__tipoVcto").val("V");
			$("#div"+i).show();
	 		$("#divx"+i).hide();
	 		i=i+1;
		});
		}else if(tipoControl == TIPO_REEM){
		var i=0;
		$(".cssidCombo").each(function() {
			$("#reporteCredito_listaClaseCredito_"+i+"__reembolso").val("");
	 		$("#reporteCredito_listaClaseCredito_"+i+"__tipoVcto").val("R");
			$("#div"+i).hide();
	 		$("#divx"+i).show();
	 		i=i+1;
	 		});
		}else {
		$(".cssidCombo").each(function() {
			$("#div"+i).hide();
	 		$("#divx"+i).hide();
	 		$("#reporteCredito_listaClaseCredito_"+i+"__vencimiento").val("");
	 		$("#reporteCredito_listaClaseCredito_"+i+"__reembolso").val("");
	 		$("#reporteCredito_listaClaseCredito_"+i+"__tipoVcto").val("");
	 		i=i+1;
	 		});
		$(".cssidCalendar").each(function() {
			$("#div"+i).hide();
	 		$("#divx"+i).hide();
	 		$("#reporteCredito_listaClaseCredito_"+i+"__vencimiento").val("");
	 		$("#reporteCredito_listaClaseCredito_"+i+"__reembolso").val("");
	 		$("#reporteCredito_listaClaseCredito_"+i+"__tipoVcto").val("");
	 		i=i+1;
		});
		}
	}
	
	function changeReembolsoFila(fila){
		var tipoControl= $("#reporteCredito_listaClaseCredito_"+fila+"__tipoVcto").val();
		if(tipoControl == TIPO_VENC){
			$("#reporteCredito_listaClaseCredito_"+fila+"__vencimiento").val("");
			$("#reporteCredito_listaClaseCredito_"+fila+"__tipoVcto").val("V");
			$("#div"+fila).show();
	 		$("#divx"+fila).hide();
	 	}else if(tipoControl==TIPO_REEM){
 			$("#reporteCredito_listaClaseCredito_"+fila+"__reembolso").val("");
	 		$("#reporteCredito_listaClaseCredito_"+fila+"__tipoVcto").val("R");
		 	$("#div"+fila).hide();
	 		$("#divx"+fila).show();
	 	}else{
	 		$("#reporteCredito_listaClaseCredito_"+fila+"__reembolso").val("");
	 		$("#reporteCredito_listaClaseCredito_"+fila+"__vencimiento").val("");
	 		$("#div"+fila).hide();
	 		$("#divx"+fila).hide();
	 	}	
	}
	
	function addCalendar(){
	$('.cssidCalendar').each(function(){
		$(this).datepicker({ 
    							dateFormat: 'dd/mm/yy',
    							showOn: "button",
							    buttonImage: "images/calendariobbva.png",
								buttonImageOnly: true });
    	$(this).datepicker();
   	});
	}
	function cambiarImporte(combo,id,monto){
		var txtImporte=$("#reporteCredito_listaClaseCredito_"+id+"__importe").val();
		var montoSoles;
		var tipoCambio =  $("#tipoCambio").val();
  		if(monto!=""){
	  		if(combo.value==USD){
		  		var formattedUSD = monto;
	            var unformattedUSD = formattedUSD.replace(/\,/g , "");
	 			$("#reporteCredito_listaClaseCredito_"+id+"__importe").val(unformattedUSD);
	 			document.getElementById("reporteCredito_listaClaseCredito_"+id+"__importe").onblur();
			 }else if(combo.value==PEN){
			 	var formatted = txtImporte;
	            var unformatted = formatted.replace(/\,/g , "");
	 			montoSoles = Math.round(parseFloat(unformatted)*parseFloat(tipoCambio)*100)/100;
	 			$("#reporteCredito_listaClaseCredito_"+id+"__importe").val(montoSoles);
	 			document.getElementById("reporteCredito_listaClaseCredito_"+id+"__importe").onblur();
	 		}
 		}
   }
	function toggleRow(){
		var cancelado =  $("#cancelado").val();
		var tipoControl= $('select[name="tipoVctoRblso"]').val();
		var row; 
		var indice;
		
		if(cancelado>0){
			row	= document.getElementById("hiddenRow"+cancelado);
			row.style.display = ""; 
		}
	}
   	//FUNCION PARA CARGAR LOS SUSTENTOS EN DIV
	function loadDataSustento(){
		var n=0;
		$(".cssidsustento").each(function() {
			var data = $("#reporteCredito_listaSustentoOperacion_"+n+"__sustentoString").val();
			$('#sustento'+n).html(data);
			n = n +1;						
		});
	}
   function cambiarTipoCuenta(combo,id,tipo,numerogarantia,comentario){
	   var nombre="garantiaAnterior"+id;	
	   $("#reporteCredito_listGarantia_"+id+"__idcuentatmpFinal").val(combo.value);
	   if(combo.value==TIPO_M){
	   		$("#"+nombre).css("display", "");
	   		var txtNumeroGarantia=$("#reporteCredito_listGarantia_"+id+"__numeroGarantiaAnterior");
	   		var txtComentario=$("#reporteCredito_listGarantia_"+id+"__comentarioAnterior");	
	   		//cambio por paso de parametro de textarea - el javascript no acepta enter como parametro
	   		var txtareaComentario=$("#reporteCredito_listGarantia_"+id+"__comentario").val();   		
	   		//txtComentario.prop("readonly","readonly");
	   		//txtNumeroGarantia.prop("readonly","readonly");	   		
	   		if(tipo!=TIPO_M){
	   			txtNumeroGarantia.val(numerogarantia);
		   		txtComentario.val(txtareaComentario);
	   		}
   		}else{
   			$("#"+nombre).css("display", "none");
   		}
   }
   function cambiarTipoCuentaFinal(combo,id,tipo,numerogarantia,comentario){
	 
	   var nombre="reporteCredito_listGarantia_"+id+"__ordenFinal";
	   if(combo.value==""){
		   alert($("#"+nombre).val());
		   $("#"+nombre).prop("disabled","disabled");
	   }else{
		   $("#"+nombre).removeAttr("disabled");
	   }
   }function setearPosicion(indice){
	 
	   $("#posicionSustento").val(indice);
		   
   }
   
	//MOSTRAR EL MODAL PARA INGRESAR LA INFORMACION
	function executeModal(id){
		
		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   					  
		var ceg = $("#codEmpresaGrupoS").val();
		$("#ceg").val(ceg);
		
		var selectedItems= new Array();
      	var indice=0;
      	$("#divEdicionSustentos").hide();
      	$('[name=chkSustento]').each(function(){
    	  	var seleccionado=$(this);
            if( seleccionado.prop('checked') )
            {                           
                selectedItems[indice] = $(this).val();//El indice inicial cera 0                    
				indice++; //paso a incrementar el indice en 1
				
            }
        });
		$("#posicionSustento").val(selectedItems);
		
		if(id!=""){
			$.post("updateSustento.do", { idSustento: id ,codEmpresaGrupoS: ceg }, function(data){
				//var jsonDataText = jQuery.parseJSON(data);
				var jsonDataText = eval("(" + data + ")");	
				
				$("#rowId").val(jsonDataText.rowId);
				$("#sustentoId").val(jsonDataText.sustentoId);
				$("#sustentoOrden").val(jsonDataText.sustentoOrden);
				$("#sustentoCuentaId").val(jsonDataText.sustentoCuentaId);
				$("#sustentoOrdenFinal").val(jsonDataText.sustentoOrdenFinal);
				$("#sustentoCuentaIdFinal").val(jsonDataText.sustentoCuentaIdFinal);
			});
		}else{
			var n=0;
			/*$(".cssidsustento").each(function() {
				n = n +1;						
			});*/
			$("#sustentoId").val("");
			$("#sustentoOrden").val("");
			$("#sustentoCuentaId").val("");
			$("#sustentoOrdenFinal").val("");
			$("#sustentoCuentaIdFinal").val("");
			//$("#sustentoPosicion").val(n);
		}
		setTimeout($.unblockUI, 1);  
		$("#dialog-modal-detalle-sustento").dialog({ height: 480, width: 745, modal: true });
		//$("#btnVerSustento").trigger("click");
		
		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
			if(id!=""){
				$.post("consultarSustento.do", { idSustento: id  }, function(data){
					var iframe = document.getElementById("valorSustento___Frame");				
					var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
					if(oCell!=null){
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;					
						} 
					}
				});
			}else{
				var iframe = document.getElementById("valorSustento___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				if(oCell!=null){
					var eInnerElement = oCell.firstChild;
					eInnerElement.contentWindow.document.body.innerHTML = "";
				}				
			}
			setTimeout($.unblockUI, 1);  
			$("#divEdicionSustentos").show();
			//$("#divEdicionSustentos").toggle("slow");
		
	}
</script>

 <s:include value="/pages/programa/opciones_programa.jsp"></s:include>
	<%@include file="/pages/programa/tabsCabecera.jsp" %>
<script type="text/javascript">
	var flagGuardado=true;
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
		<s:form action="updateCreditoAction" id="reporteCredito" onsubmit="SaveScrollXY(this);" method="post"  theme="simple" enctype="multipart/form-data" >
		<s:hidden id="codEmpresaGrupoS" name="codEmpresaGrupoS" />
		<input name="scrollX" id="scrollX" type="hidden"  />
		<input name="scrollY" id="scrollY" type="hidden"  />
		<input type="hidden" name ="tipo_empresa" value="<%=tipo_empresa%>"/>
		<input type="hidden" name ="id_grupo" value="<%=id_grupo%>"/>
		<input type="hidden" name ="cod_empresaPrincipal" value="<%=cod_empresa_principal%>"/>
		<input name="codempresagrupo" id="codempresagrupo" type="hidden"  />
		<input name="flagChangeEmpresa" id="flagChangeEmpresa" type="hidden"  />
		
		<input name="ideSustento" id="hIdSustento" type="hidden"  />
		<input name="ordenSustento" id="hOrdenSustento" type="hidden"  />
		<input name="ordenFinalSustento" id="hOrdenFinalSustento" type="hidden"  />
		<input name="idCuentaSustento" id="hCuentaSustento" type="hidden"  />
		<input name="idCuentaFinalSustento" id="hCuentaFinalSustento" type="hidden"  />
		<input name="valorSustento" id="hValorSustento" type="hidden"  />
		<input name="tipoCambio" id="tipoCambio" type="hidden"  />
		
		<input type="hidden" name="codigoArchivo" />
		<input type="hidden" name="extension" />
		<input type="hidden" name="nombreArchivo" />
				
		<%if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){ %>
		<div>
		<table>
			<tr>
				<td>
					Empresa del Grupo:
				</td>
				<td>
					<s:select id="codigoEmpresa"  name="codigoEmpresa" list="listaEmpresasGrupoRC" listKey="codigo" listValue="nombre" theme="simple" ></s:select>
				</td>
			</tr>
		</table>
		</div>
		<%} %>
		
		<table width="90%">
			<tr>
				<td class="bk_tabs">
					<table class="ln_formatos" cellspacing="0" width="100%">
						<tr>
							<td align="left">
								<s:submit value="Guardar" 
								onclick="return validarSeleccioneReporteCredito('.cssvalorcuenta','.cssvalormoneda','.cssvalorCuentaGarant','.cssvalorcuentaSust','.cssidCalendar','.cssidCombo','.cssvalortipo','.cssvalorConectorGarant')" 
								cssClass="btn" 
								theme="simple"></s:submit>
							</td>
						</tr>
					</table>
					
					<br>	
					<br>	
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td class="label" width="150">Nombre:
							</td>
							<td width="200">							
							<s:textfield name="nombreEmpresaRDC"
											 id="tfnombreempresaRDC" 
											 label="nombreEmpresaRDC"
											 cssStyle="width:250px"	
											 cssClass="form-control" 										 
											 readonly="true"/>
							</td>
							<td class="label" width="150">Fecha:
							</td>
							<td width="250">
								<s:textfield name="programa.fechaRDC" 
											 id="tffechaRDC" 
											 label="fechaRDC"
											 maxlength="10"
											 cssClass="form-control"
											 onblur="javascript:valFecha(document.reporteCredito.tffechaRDC);"											 
											 />	
							</td>
							
						</tr>
						<tr>
							<td class="label">Cuenta Corriente:
							</td>
							<td >
							<s:textfield name="programa.cuentaCorriente"
											 id="tfcuentaCorriente" 
											 label="CuentaCorriente"
											 maxlength="20"
											 cssClass="form-control" 
											 alt="cc"											 
											 />
							</td>
							<td class="label">Nº RVGL:
							</td>
							<td >
								<s:textfield name="programa.numeroRVGL"
											 id="tfnumeroRVGL" 
											 label="numeroRVGL"	
											 onkeypress="return acceptNumComa(event);"										 
											 maxlength="199"	
											 cssClass="form-control" 										 
											 />
							</td>
						</tr>	
						<tr>
							<td class="label">Oficina:
							</td>
							<td colspan="3">
		 								<s:textfield name="programa.oficina"
											 id="idOficinaBEC" 
											 label="oficina"
											 maxlength="300"	
											 cssStyle="width:585px"	
											 readonly="true"	
											 cssClass="form-control" 									 
											 />	
							</td>
							
						</tr>	
						<tr>
							<td class="label">RUC:
							</td>
							<td >
							<s:textfield name="programa.ruc"
											 id="tfrucRDC" 
											 label="rucRDC"
											 maxlength="11"	
											 readonly="true"
											 cssClass="form-control" 										 
											 />
							</td>
							<td class="label">Rating:
							</td>
							<td >
							<s:textfield name="ratingRDC"
											 id="tfratingRDC" 
											 label="ratingRDC"	
											 cssClass="form-control" 										 
											 readonly="true"/>
							</td>
						</tr>	
						<tr>
							<td class="label">CIIU:
							</td>
							<td colspan="3">
							<s:textfield name="ciiuRDC"
											 id="tfciiuRDC" 
											 label="ciiuRDC"											 
											  maxlength="250"
											  cssClass="form-control" 	
											  cssStyle="width:585px"										  
											  />							
							</td>
							
						</tr>	

						<tr>
							<td class="label">Gestor:
							</td>
							<td colspan="3">
							<s:textfield name="programa.gestor"
											 id="tfgestor" 
											 label="gestor"
											 maxlength="300"
											  cssStyle="width:585px"
											  cssClass="form-control" 
											  readonly="true"											 
											 />
							</td>							
							
						</tr>						
						
						<tr>
							<td class="label">Salem:
							</td>
							<td >
							<s:textfield name="programa.salem"
											 id="tfsalem" 
											 label="salem"
											 maxlength="2"
											 cssClass="form-control" 											 
											 />
							</td>
							<td class="label">Clasific. Banco:
							</td>
							<td >							
							<s:textfield name="clasificacionBancoRDC"
											 id="tfclasificacionBancoRDC" 
											 label="clasificacionBancoRDC"	
											 cssClass="form-control" 										 
											 readonly="true"/>
							</td>
							
						</tr>	
												
						<tr>
							<td class="label">Grupo Economico:
							</td>
							<td >
							<s:textfield name="programa.idGrupo"
											 id="tfidGrupo" 
											 label="idGrupo"
											 cssClass="form-control" 											 
											 readonly="true"/>
							</td>
							<td class="label">Cod. Central:
							</td>
							<td >
							<s:textfield name="codigoCentralRDC"
											 id="tfcodigoCentralRDC" 
											 label="codigoCentralRDC"
											 cssClass="form-control" 											 
											 readonly="true"/>
							</td>
						</tr>	
						<tr>							
							<td class="label">Segmento:
							</td>
							<td colspan="3" >
								<s:textfield name="programa.segmento"
											 id="idSegmento" 
											 label="segmento"
											 maxlength="300"
											 cssStyle="width:585px"	
											 readonly="true"
											 cssClass="form-control" 											 
											 />	 
											 
							</td>
				
						</tr>	
						<tr>
							<td colspan="4" class="label">
							<s:submit onclick="mostrarRefreshRC();" theme="simple" action="refreshReporteCredito" value="Refrescar" cssClass="btn"></s:submit>
							</td>					
						</tr>
					</table>
					
					
					<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagClaseCredito"   id="chkClaseCreditos"/>Cuadro Clase de Cr&#233;dito</h3>
					<div id="divClaseCreditos" style="display: none;"  class="my_div ">
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td>
								<label class = "subTitulo">Cuadro de Clase Cr&#233;dito</label>
														
							</td>
						</tr>
					</table>
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td>
								<table class="ui-widget ui-widget-content">
								<thead>
									<tr>
										<th colspan="10" align="left">
										<s:submit theme="simple" action="addClaseCredito" value="+ limite-Operacion" cssClass="btn"></s:submit>
										<s:submit theme="simple" action="addClaseCreditoSublimite" value="+ Sublimite" cssClass="btn"></s:submit>
										<s:submit theme="simple" action="deleteClaseCredito" value="-" onclick="return confirmDelete();" cssClass="btn"></s:submit>
										<s:submit theme="simple" action="saveAllClaseCredito" value="Guardar Lista" onclick="return existeElementosCuadroClaseCred('.cssclasecreditordc','.cssvalorcuenta','.cssvalormoneda','.cssidCalendar','.cssidCombo','.cssvalortipo')" cssClass="btn"></s:submit>
										<s:submit theme="simple" action="addCanceledRows" value="Agregar Filas Canceladas" onclick="return existeElementosCuadroClaseCredCancel('.cssclasecreditordc','.cssvalorcuenta','.cssvalormoneda','.cssidCalendar','.cssidCombo','.cssvalortipo');" cssClass="btn"></s:submit>
										<s:submit id="idSincronizarClase" theme="simple" action="sincronizarClaseCredito" value="Sincronizar" onclick="return confirm('¿Esta seguro que desea sincronizar?')" cssClass="btn"/>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; EN MILES
										</th>
									</tr>
									<tr class="ui-widget-header ">
										<th>
											&nbsp;
										</th>
										<th colspan="2">
											Orden/Cuenta
										</th>
										<th>
											Moneda
										</th>
										<th>
											Importe
										</th>
										<th>
											Clase de Credito
										</th>
										<th>
											Tasa/Comision
										</th>
										<th>
											Vencimiento/ Reembolso
										</th>
										<th>
											<s:select  id="tipoVctoRblso" name ="tipoVctoRblso" list="itemVctoRblso" listKey="value" listValue="label" onchange="changeReembolso();" cssStyle="width:125px;"/>
										</th>
										<th>
											Nota
										</th>
									</tr>
								</thead>
									<% int cnt=0;%>
									<s:iterator var="p" value="listaClaseCredito" id="ejecutivo" status="rowstatus">
										
										
										<s:set name="tipoc" value="%{listaClaseCredito[#rowstatus.index].flagCancelado}" /> 
											<s:if test="%{#tipoc=='CC'}">
																						
												<% if (cnt==0){%>
												<tr class="ui-widget-header ">
												<td colspan="10" style="font-weight : bold;"><p>LIMITES QUE SE CANCELAN</p>	     										
	     										</td> 
												</tr>
												<% }
												cnt=cnt+1;%>											
											</s:if> 											
											<s:else>											
											</s:else>
										
										<tr>
											<s:hidden name="listaClaseCredito.id" value="%{id}" ></s:hidden>
											<s:hidden name="listaClaseCredito[%{#rowstatus.index}].flagCancelado"></s:hidden>
											<s:hidden name="listaClaseCredito[%{#rowstatus.index}].flagTipoLimte"></s:hidden>
											<s:set name="tipoLimite" value="%{listaClaseCredito[#rowstatus.index].flagTipoLimte}" /> 
																	
											<td>
												  <input type="checkbox" value="<s:property  value="%{#rowstatus.index}"/>" name="chkClaseCredito"></input>
											</td>											
											<td align="right">
													<s:if test="%{#tipoLimite=='SL'}">
																<s:textfield label="orden"  size="3" 
									              				 name="listaClaseCredito[%{#rowstatus.index}].orden" value="%{orden}" 
									              				 theme="simple"  cssStyle="width:25px;text-align: right; display: none"									              				 
									              				 readonly="true"
									              				 cssClass="cssclasecreditordc"/>
													</s:if> 											
													<s:else>	
																<s:textfield label="orden"  size="3" 
									              				 name="listaClaseCredito[%{#rowstatus.index}].orden" value="%{orden}" 
									              				 theme="simple"  cssStyle="width:25px;text-align: right;"									              				 
									              				 readonly="true"
									              				 cssClass="cssclasecreditordc"/>										
													</s:else>

									      	</td>
									      	
									      	<td>	
									      			<s:if test="%{#tipoLimite=='SL'}">
										      			<s:select
															name="listaClaseCredito[%{#rowstatus.index}].cuenta.id"
															cssClass="cssvalorcuenta"
															list="itemCuentaCreditoRDC" listKey="value" listValue="label" 
															cssStyle="width:95px; display: none;">
														</s:select>
													</s:if> 											
													<s:else>
														<s:select
															name="listaClaseCredito[%{#rowstatus.index}].cuenta.id"
															cssClass="cssvalorcuenta"
															list="itemCuentaCreditoRDC" listKey="value" listValue="label" 
															cssStyle="width:95px">
														</s:select>													
													</s:else>	
													
									      	</td>
									      	<td>
									      			<s:select
														name="listaClaseCredito[%{#rowstatus.index}].moneda.id"
														cssClass="cssvalormoneda"
														list="itemMonedaRDC" listKey="value" listValue="label" onchange="cambiarImporte(this,'%{#rowstatus.index}','%{importe}');" cssStyle="width:95px">
													</s:select>
									      	</td>
									      	
									       	<td>
									       	
									       		<s:textfield size="15" label="importe"
												name="listaClaseCredito[%{#rowstatus.index}].importe"
												value="%{importe}" theme="simple" cssClass="RCImporte"								 
												onkeypress="EvaluateText('%f', this);" 
												onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
												maxlength="12" cssStyle="width:95px"/>
													
												
											</td>
									       	<td>
									              	<s:textfield label="claseCredito"  size="35" 
																 name="listaClaseCredito[%{#rowstatus.index}].claseCredito" 
																 value="%{claseCredito}" theme="simple" maxlength="1800" cssStyle="width:250px"/>
									       	</td>
									       	<td>
									              	<s:textfield label="tasaComision"  size="15" 
																 name="listaClaseCredito[%{#rowstatus.index}].tasaComision" 
																 value="%{tasaComision}" theme="simple" maxlength="200" cssStyle="width:95px"/>
									       	</td>
									       	<td>
									       			<s:select
														name="listaClaseCredito[%{#rowstatus.index}].tipoVcto"
														cssClass="cssvalortipo"
														list="itemVctoRblso" listKey="value" listValue="label" onchange="changeReembolsoFila('%{#rowstatus.index}');" cssStyle="width:120px">
													</s:select>													
													
									       	</td>
									       	<td> 
											       	<div id="div<s:property value="%{#rowstatus.index}"/>" >
		     												<s:textfield theme="simple" name="listaClaseCredito[%{#rowstatus.index}].vencimiento" 
																		 maxlength="10" value="%{vencimiento}" cssClass="cssidCalendar" cssStyle="width:75px;" onblur="javascript:valFecha(this);"/>
		     										</div>
												     <div id="divx<s:property value="%{#rowstatus.index}"/>" >
		     												<s:select theme="simple" name="listaClaseCredito[%{#rowstatus.index}].reembolso" list="itemReembolso"
																 	  listKey="value" listValue="label" cssClass="cssidCombo" cssStyle="width:95px;"/>
		     										</div>
											</td>
									       	
									       	<td><s:textarea cols="40" label="Nota" 
												cssClass="tareanota"
												name="listaClaseCredito[%{#rowstatus.index}].nota"
												value="%{nota}" cssStyle="width:130px"/>
											</td>
										</tr>
											

										

									</s:iterator>
								</table>
							</td>
						</tr>
					</table>				
					</div>		
					
					
					<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagGarantia"   id="chkCuadroGarantias"/>Cuadro de Garantias</h3>
					<div id="divCuadroGarantias" style="display: none;"  class="my_div ">
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td>
								<label class = "subTitulo">Cuadro de Garantias</label>
														
							</td>
						</tr>
					</table>
					<table class="ln_formatos" width="100%" cellspacing="0">
						<tr>
							<td>
							<table width="100%" class="ui-widget ui-widget-content">
								<thead>
									<tr>
										<td colspan="8">
											<s:submit id="idaddcuenta" theme="simple" value="+ Garantía"
											action="addCuentaGarantia"
											cssClass="btn" /> 
											<!--											
											<s:submit id="idaddgarantia" theme="simple" value="+ " action="addNumeroGarantia"
											onclick="return validarCuentaPrimer('.csstipoGarantia')" 
											cssClass="btn" />
											-->
											<s:submit id="iddelgarantia" theme="simple" action="delNumeroGarantia" value="Eliminar" onclick="return confirmDelete();" disabled="disabled"  cssClass="btn"></s:submit>
											<s:submit id="idsavegarantia" theme="simple" action="saveGarantia" value="Guardar Lista" onclick="return existeElementos3('.RCnumGar','.cssvalorCuentaGarant','.cssvalorConectorGarant')" disabled="disabled"  cssClass="btn"></s:submit>
											
											<s:submit id="idSincronizar" theme="simple" action="sincronizarDatos" value="Sincronizar" onclick="return confirm('¿Esta seguro que desea sincronizar?')" cssClass="btn"/>
										
										</td>					
									</tr>
									<tr class="ui-widget-header">
										<th>#</th>
										<th colspan="5">Orden/Cuenta</th>
										<th>Numero Garantia</th>										
										<th>Comentarios</th>
									</tr>
								</thead>
								<tbody>
									<s:iterator var="vgarantia" value="listGarantia"
										status="rowstatus">
										<tr>
		
											<s:hidden name="listGarantia[%{#rowstatus.index}].id"></s:hidden>
											<s:hidden name="listGarantia[%{#rowstatus.index}].programa.Id"></s:hidden>
											<s:hidden name="listGarantia[%{#rowstatus.index}].cuenta.id"></s:hidden>
											<s:hidden name="listGarantia[%{#rowstatus.index}].cuentaFinal.id"></s:hidden>												
											<s:hidden name="listGarantia[%{#rowstatus.index}].codigoAnexoGarantia"/>											
											<s:hidden cssClass="csstipoGarantia" name="listGarantia[%{#rowstatus.index}].tipo"></s:hidden>
											<s:hidden name="listGarantia[%{#rowstatus.index}].conector.id"></s:hidden>
											
											<td >
												<input type="checkbox" value="<s:property  value="%{#rowstatus.index}"/>" name="chksParaEliminar"></input>
											</td>
											
											<s:set name="tipo" value="%{listGarantia[#rowstatus.index].tipo}" /> 
											<s:if test="%{#tipo=='TG'}">
<!--												<td width="2" ></td>-->
<!--												<td width="5" ></td>	-->
<!--												<td width="2" ></td>-->
<!--												<td width="5" ></td>	-->
												<td align="right" >
													<s:textfield size="3" maxlength="3" label="orden"
													onkeypress="return acceptNum(event);" onblur="this.value = validarSiNumero(this.value);"
													name="listGarantia[%{#rowstatus.index}].orden"
													value="%{orden}" theme="simple" 
													cssStyle="width:25px;text-align: right;"/>
												</td>
												<td >
													<s:select
														name="listGarantia[%{#rowstatus.index}].idcuentatmp"
														cssClass="cssvalorCuentaGarant"
														list="itemCuentaGarantiaRDC" listKey="value" listValue="label"  onchange="cambiarTipoCuenta(this,'%{#rowstatus.index}','%{listGarantia[#rowstatus.index].idcuentatmp}','%{numeroGarantia}','');">
													</s:select>
												</td>
												
												<td >
													<s:select
														name="listGarantia[%{#rowstatus.index}].idconectortmp"
														cssClass="cssvalorConectorGarant"
														list="itemConectorGarantiaRDC" listKey="value" listValue="label">
													</s:select>
												</td>
												
												<td align="right" >
													<s:textfield size="3" maxlength="3" label="orden"
													onkeypress="return acceptNum(event);" onblur="this.value = validarSiNumero(this.value);"
													name="listGarantia[%{#rowstatus.index}].ordenFinal"
													value="%{ordenFinal}" theme="simple" 
													cssStyle="width:25px;text-align: right;"/>
												</td>
												<td >
													<s:select
														name="listGarantia[%{#rowstatus.index}].idcuentatmpFinal"
														cssClass="cssvalorCuentaGarant"
														list="itemCuentaGarantiaRDC" listKey="value" listValue="label" >
													</s:select>
												</td>
																							
											</s:if> 
											<s:elseif test="%{#tipo=='TC'}">		
												<td align="right" >
												<s:textfield size="3" maxlength="3" label="orden"
												onkeypress="return acceptNum(event);" onblur="this.value = validarSiNumero(this.value);"
												name="listGarantia[%{#rowstatus.index}].orden"
												value="%{orden}" theme="simple" 
												cssStyle="width:25px;text-align: right;"/>
												</td>
												<td >
												<s:select
													name="listGarantia[%{#rowstatus.index}].idcuentatmp"
													cssClass="cssvalorCuentaGarant"
													list="itemCuentaGarantiaRDC" listKey="value" listValue="label"  onchange="cambiarTipoCuenta(this,'%{#rowstatus.index}','%{listGarantia[#rowstatus.index].idcuentatmp}','%{numeroGarantia}','');">
												</s:select>
												</td>
												
												<td >
													<s:select
														name="listGarantia[%{#rowstatus.index}].idconectortmp"
														cssClass="cssvalorConectorGarant"
														list="itemConectorGarantiaRDC" listKey="value" listValue="label">
													</s:select>
												</td>
												
												<td align="right" >
												<s:textfield size="3" maxlength="3" label="orden"
												onkeypress="return acceptNum(event);" onblur="this.value = validarSiNumero(this.value);"
												name="listGarantia[%{#rowstatus.index}].ordenFinal"
												value="%{ordenFinal}" theme="simple" 
												cssStyle="width:25px;text-align: right;"/>
												</td>
												<td >
												<s:select
													name="listGarantia[%{#rowstatus.index}].idcuentatmpFinal"
													cssClass="cssvalorCuentaGarant"
													list="itemCuentaGarantiaRDC" listKey="value" listValue="label" >
												</s:select>
												</td>
											</s:elseif> 
											<s:else>											
											</s:else>								
											
											<td>
												
												<s:textfield size="20" label="numeroGarantia"
												name="listGarantia[%{#rowstatus.index}].numeroGarantia"
												value="%{numeroGarantia}" theme="simple" cssClass="RCnumGar"
												onkeypress="return acceptNum(event);"	
												maxlength="20"/>
												
												
												
											</td>											
											<td>
												<s:textarea cols="100" label="comentario" 
												cssClass="tareacoment"
												name="listGarantia[%{#rowstatus.index}].comentario"
												value="%{comentario}" />
												
												
												
											</td>
										</tr>
										
										<tr class="garantiasOcultas <s:property  value="%{listGarantia[#rowstatus.index].idcuentatmp}"/>" id="garantiaAnterior<s:property  value="%{#rowstatus.index}"/>" style="display:none">
											<td colspan="6">Garantia Anterior</td>											
											<td>
											<s:textfield size="20" label="numeroGarantiaAnterior"
												name="listGarantia[%{#rowstatus.index}].numeroGarantiaAnterior"
												value="%{numeroGarantiaAnterior}" theme="simple" cssClass="RCnumGar"
												onkeypress="return acceptNum(event);"	
												maxlength="20"/>
											</td>
											<td>
											<s:textarea cols="100" label="comentarioAnterior" 
												cssClass="tareacoment"
												name="listGarantia[%{#rowstatus.index}].comentarioAnterior"
												value="%{comentarioAnterior}" />
											
											</td>
										</tr>
									</s:iterator>
									
								</tbody>
							</table>
							</td>
						</tr>
					</table>				
					</div>	
					
				
					<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagSustento"   id="chkSustentos"/>Cuadro Sustento de Operaciones</h3>
					<div id="divSustentos" style="display: none;"  class="my_div ">
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td>
								<label class = "subTitulo">Cuadro de Sustentos de Operaciones</label>
														
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
											<input type="button" id="addSustento" value="+" class="btn" onclick="executeModal('')" />
											<s:submit theme="simple" action="deleteSustento" value="-" onclick="return confirmDelete();" cssClass="btn"></s:submit>
										</th>
									</tr>
									<tr class="ui-widget-header ">
										<th Style="width:2px">&nbsp;</th>
										<th colspan="2" Style="width:30px">Orden/Cuenta</th>
										<th>Sustento</th>
										<th>Editar</th>
									</tr>
								</thead>
								<s:iterator var="p" value="listaSustentoOperacion" id="idsustento" status="rowstatus">
									<tr>
										<td Style="width:5px">
											<input type="checkbox" value="<s:property  value="%{#rowstatus.index}"/>" name="chkSustento"></input>
											<s:hidden name="listaSustentoOperacion[%{#rowstatus.index}].id" value="%{id}" cssClass="cssidsustento %{id}"></s:hidden>
											<s:hidden name="listaSustentoOperacion[%{#rowstatus.index}].sustentoString" value="%{sustentoString}" ></s:hidden>
											<s:hidden name="listaSustentoOperacion[%{#rowstatus.index}].orden" value="%{orden}" ></s:hidden>
											<s:hidden name="listaSustentoOperacion[%{#rowstatus.index}].cuenta.descripcion" value="%{cuenta.descripcion}" ></s:hidden>
											<s:hidden name="listaSustentoOperacion[%{#rowstatus.index}].ordenFinal" value="%{ordenFinal}" ></s:hidden>
											<s:hidden name="listaSustentoOperacion[%{#rowstatus.index}].cuentaFinal.descripcion" value="%{cuentaFinal.descripcion}" ></s:hidden>
										</td>
										<td align="right" Style="width:25px">
									    	<s:property  value="orden" ></s:property><s:property  value="cuenta.descripcion" ></s:property>
										</td>
										<td align="right" Style="width:25px">
									    	<s:property  value="ordenFinal" ></s:property><s:property  value="cuentaFinal.descripcion" ></s:property>
										</td>
										<td>
											<div id="sustento<s:property value="%{#rowstatus.index}" />"></div>
										</td>
										<td align="center" Style="width:10px"> 
											<a onclick="executeModal(<s:property value="%{id}" />)" > 
												<img border="0" alt="Consultar Sustento" src="imagentabla/bbva.Editar2Azul24.png">
											</a>
										</td>
									</tr>
								</s:iterator>
								</table>
							</td>
						</tr>
					</table>

					</div>				

						
						<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagDatosBasicosAdicionalesPrin"   id="chkDatosBasicosAdicionalesPrin"/>Datos Basicos</h3>
						<div id="divDatosBasicosAdicionalPrin" style="display: none" class="">
						
						<table class="ln_formatos" width="100%" cellspacing="0" border="0">
							<tr>
								<td>
									<table class="ln_formatos" cellspacing="0" border="0">
										<tr>
											<td class ="label">
											Actividad Principal:
											</td>
											<td colspan="3">
												<s:textarea name="programa.actividadPrincipal" 
															rows="3" 
															cols="60"
															theme="simple"
															readonly="true"
															id="stextactividadPrincipal2"></s:textarea>
											</td>
										</tr>
										<tr>
											<td class ="label">
											Antiguedad Negocio(Años):
											</td>
											<td>
												<s:textfield name="programa.antiguedadNegocio"
															 id="tfantiguenegocio" 
															 label="Antiguedad Negocio"															 
															 readonly="true"/>
											</td>										
											<td class ="label">
												Antiguedad Cliente(Años):
											</td>
											<td>
												<s:textfield name="programa.antiguedadCliente"
															 id="tfantiguecliente" 
															 label="Antiguedad Cliente"
															 readonly="true"/>
											</td>
										</tr>
										<tr>
											<td class ="label">Cuota Financiera:</td>
											<td><s:textfield size="5" name="programa.cuotaFinanciera"
												id="tfcuotafinan" 					
												readonly="true"/>%</td>
											<td class ="label">Deuda SBS(Miles de soles):</td>
											<td><s:textfield name="deudaSBS"
												id="tfDeudaSBS" 					
												readonly="true"/></td>
										</tr>
										<tr>
											<td class ="label">Buro:</td>
											<td><s:textfield size="5" name="programa.grupoRiesgoBuro"
												id="tfBureau" 					
												readonly="true"/></td>
											<td class ="label"></td>
											<td></td>
										</tr>
									</table>
								</td >
								<td>
									<table cellspacing="0">
										<tr>
											<td>
																	
												<table class="ui-widget ui-widget-content">
												
													<tr height="30px" class="ui-widget-header ">
														<th>
															&nbsp;
														</th>
														<th>Principales Accionistas
														</th>
														<th>
															%
														</th>														
													</tr>
															
															<s:iterator var="p" value="listaAccionistas" id="accionista" status="rowstatus">
																<tr>
																	<td>																		
																		
																		<s:hidden name="listaAccionistas.id" value="%{id}" ></s:hidden>
																	</td>
																	<td>
													                	<s:textfield label="nombre"  size="45"  
													                				 name="listaAccionistas[%{#rowstatus.index}].nombre" value="%{nombre}" 
													                				 theme="simple" maxlength="59" readonly="true"/>
													             	</td>
													             	<td>
													                	<s:textfield label="porcentaje" size="6" 
													                				 name="listaAccionistas[%{#rowstatus.index}].porcentaje" 
													                				 value="%{porcentaje}" theme="simple" 
													                				 readonly="true"													                				 
																				     cssStyle="text-align:right"
																				     maxlength="6"
																				     />
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
																	<s:textfield name="totalAccionista" size="6" id="totalPorceAcci" theme="simple" readonly="true"
																				 cssStyle="text-align: right;" ></s:textfield>%
																</td>
														
															</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
								</tr>
							</table>
							<br>
							<br>
						
							<table class="ln_formatos" cellspacing="0">
								<tr>
									<td>
										<label class = "subTitulo">Datos B&#225;sicos Adicionales</label>
									</td>	
									<td>
										<input type="button" id="bverdatosbasicoadicionales"  value="Ver"  class="btn" />
									</td>	
									<td>
										<input type="button" value="Guardar" id="ssavedatosbasicoadicionales" disabled="disabled"  class="btn"/>
									</td>	
									<td>
										<input type="button" value="Limpiar" id="scleandatosbasicoadicionales" disabled="disabled"  class="btn"/>
									</td>
									<td>
										<input type="button" id="idSincronizarDBA" value="Sincronizar" disabled="disabled"  onclick="return confirm('¿Esta seguro que desea sincronizar?')" 
										class="btn"/>
									</td>	
								</tr>
							</table>
							<div id="divdatosbasicosadicional" style="display:none;"> 

								<FCK:editor instanceName="datosBasicosAdicional" height="250px">
									<jsp:attribute name="value">&nbsp;
									</jsp:attribute>									
									
								</FCK:editor>
							</div>
						</div>
						<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagSintesisEconFinAdicional"   id="chkSintesisEconFinAdicional"/>An&#225;lisis de Situaci&#243;n Econ&#243;mica y Financiera</h3>
						<div id="divSintesisEconFinAdicional" style="display: none" class="">
							<table class="ln_formatos" cellspacing="0">
								<tr>
									<td>
										<label class = "subTitulo">An&#225;lisis de Situaci&#243;n Econ&#243;mica y Financiera Adicionales</label>
									</td>	
									<td>
										<input type="button" id="bversintesiseconfinadicional"  value="Ver"  class="btn"/>
									</td>	
									<td>
										<input type="button" value="Guardar" id="ssavesintesiseconfinadicional" disabled="disabled"  class="btn"/>
									</td>	
									<td>
										<input type="button" value="Limpiar" id="scleansintesiseconfinadicional" disabled="disabled"  class="btn"/>
									</td>
									<td>
										<input type="button" id="idSincronizarSintesisA" value="Sincronizar" disabled="disabled" onclick="return confirm('¿Esta seguro que desea sincronizar?')" 
										class="btn"/>
									</td>
								</tr>
							</table>
							<div id="divsintesiseconfinad" style="display:none;"> 

								<FCK:editor instanceName="sintesiseconfinadicional" height="250px">
									<jsp:attribute name="value">&nbsp;
									</jsp:attribute>									
									
								</FCK:editor>
							</div>
						</div>


					<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagPosicionCliente"   id="chkPosicionClientes"/>Posici&#243;n del Cliente y del Grupo Econ&#243;mico</h3>
					<div id="divPosicionClientes"  class="" style="display: none">
					
					<table class="ln_formatos" width="100%" cellspacing="0" border="0">
							<tr>
								<td class ="label" colspan="2">
								POSICION DEL CLIENTE Y DEL GRUPO ECONOMICO:
								</td>
								<td colspan="3">									
								</td>
							</tr>
							<tr>
								<td class ="label">
								VULNERABILIDAD:
								</td>
								<td colspan="4">
									<s:textarea name="programa.vulnerabilidad" 
												rows="2" 
												cols="80"
												theme="simple"												
												id="stextvulnerabilidad">
									</s:textarea>
								</td>
							</tr>
							<tr>										
								<td class ="label" colspan="5">
									IMPACTO AMBIENTAL:
								</td>								
							</tr>
							<tr>										
								<td>									
								</td>
								<td class ="label">
									Total Inversion S/.:
								</td>
								<td >
									<s:textfield name="programa.totalInversion"
															 id="tftotalInversion" 
															 label="Total Inversion"
															 onkeypress="EvaluateText('%f', this);" 
															 onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     maxlength="13"
															 />
								</td>
								<td class ="label">
									Monto Prestamo S/.:
								</td>
								<td >
									<s:textfield name="programa.montoPrestamo"
															 id="tfmontoPrestamo" 
															 label="Monto Prestamo"
															 onkeypress="EvaluateText('%f', this);" 
															 onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															 maxlength="13"
															 />
								</td>
							</tr>
							<tr>										
								<td>									
								</td>
								<td class ="label">
									Entorno(km):
								</td>
								<td >
									<s:textfield name="programa.entorno"
															 id="tfentorno" 
															 label="Entorno"
															 onkeypress="EvaluateText('%f', this);" 
															 onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
															 maxlength="13"
															 />
								</td>
								<td class ="label">
									Poblacion Afectada(Hab):
								</td>
								<td >
									<s:textfield name="programa.poblacionAfectada"
															 id="tfpoblacionAfectada" 
															 label="Poblacion Afectada"
															 onkeypress="EvaluateText('%f', this);" 
															 onblur="this.value = NumberFormat(this.value, '0', '.', ',')"
															 maxlength="15"
															 />
								</td>
							</tr>
							<tr>										
								<td>									
								</td>
								<td class ="label">
									Categorización Ambiental:
								</td>
								<td colspan="3">
									<s:textfield name="programa.categorizacionAmbiental"
															 id="tfcategorizacionAmbiental" 
															 label="Categorización Ambiental"
															 maxlength="20"
															 size="81"
															 />
								</td>
																
								
							</tr>
							<tr>										
								<td class ="label" colspan="2">
									RESPONSABILIDAD TOTAL GRUPAL
								</td>
								<td class ="label" colspan="3">
									OFICINA
								</td>
							</tr>
							<tr>
																		
								<td align="center" class ="label" colspan="2">
									EN MILES DE USD
								</td>
								<td class ="label">
									Gerente
								</td>
								<td class ="label">
									Gestor
								</td>
								<td class ="label">
									Sub-Gerente
								</td>
								
							</tr>
							<tr>
																		
								<td colspan="2">
									<table>
										<tr>
											<td class ="label">
											Riesgo Vigente
											</td>
											<td>
												<s:textfield name="riesgovigenteRDC"
												 id="tfriesgovigenteRDC" 
												 label="riesgovigenteRDC"	
												 cssClass="RCriesgo"										 
												 readonly="true"/>
											</td>
										</tr>
										<tr>
											<td class ="label">
											Incremento
											</td>
											<td>
												<s:textfield name="incrementoRDC"
												 id="tfincrementoRDC" 
												 label="incrementoRDC"
												 cssClass="RCriesgo"											 
												 readonly="true"/>
											</td>
										</tr>
										<tr>
											<td class ="label">
											Riesgo Propuesto
											</td>
											<td>
												<s:textfield name="riesgopropuestoRDC"
												 id="tfriesgopropuestoRDC" 
												 label="riesgopropuestoRDC"	
												 cssClass="RCriesgo"										 
												 readonly="true"/>
											</td>
											
										</tr>
										
									</table>									
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
								
							</tr>
							
						</table>								
						<br>
						<br>
						<table class="ln_formatos" cellspacing="0" width="100%">
							<tr>
								<td>
									<label style="font-size: 8pt;font-weight: bold;">Comentario de Admisi&#243;n:</label>
								</td>
							</tr>
							<tr>
								<td>
									<s:textarea name="programa.comentarioAdmision"
												id="tacomentarioAdmision"
												rows="5"
												cols="100"
												theme="simple">
									</s:textarea>
								</td>	
							</tr>
						</table>
					</div>
					
					
					<!--ini MCG20141024-->
					<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagArchivoReporteCredito"   id="chkArchivoReporteCredito"/>Adjuntar Archivos</h3>
					<div id="divArchivoReporteCredito" style="display: none;" class="">
							<table>
								<tr>
									<td class="bk_tabs">
						
									<table class="ln_formatos" cellspacing="0">
										<tr>
											<td><s:file name="fileReporteCredito" label="Ruta Archivo" size="70"
												cssClass="btn"
												theme="simple" /></td>
											<td><s:submit value="Grabar" action="saveFileReporteCredito"
												cssClass="btn"
												theme="simple" /> 
											</td>
										</tr>
									</table>
									<div class="my_div">
						
									<table class="ln_formatos" width="100%" cellspacing="0">
										<tr>
											<td>
											<label style="font-size: 10pt;font-weight: bold;">Listado Archivos:</label>
											</td>
										</tr>
										<tr>
											<td>
											<table class="ui-widget ui-widget-content">
												<thead>
													<tr class="ui-widget-header ">
														<th>Fecha</th>
														<th>usuario</th>
														<th>Nombre Archivo</th>
														<th>Extension</th>
														<th>Descargar</th>
													    <th>...</th>
													</tr>
												</thead>
												<tbody>
													<s:iterator value="listaArchivosReporteCredito" var="varfilesReporteCredito"
														status="userStatus">
														<tr>
															<td><s:date name="fechaCreacion" format="dd/MM/yyyy" /></td>
															<td><s:property value="codUsuarioCreacion" /></td>
															<td><s:property value="nombreArchivo" /></td>
															<td><s:property value="extencion" /></td>
															<td align="center"> <a
																href="javascript:descargarArchivoRC('<s:property value="id" />','<s:property value="extencion" />','<s:property value="nombreArchivo" />');">
															<img src="imagentabla/bbva.documentoAzul24.png" border="0" alt="download"> </a></td>
															<td align="center">
																<a href="javascript:eliminarRC('<s:property value="id" />','<s:property value="extencion" />','<s:property value="nombreArchivo" />');">
																<img src="imagentabla/bbva.EliminarAzul24.png" alt="Eliminar" border="0" onclick="return confirmDelete();">
																</a>
															</td>
														</tr>
						
													</s:iterator>
												</tbody>
											</table>
											</td>
										</tr>
									</table>
									</div>
									<br />
									<br />						
									</td>
								</tr>
							</table>

					</div>	
					
					
					<!--fin MCG20141024-->
				
													
					<br>
					<br>					
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td>
								<s:submit value="Guardar" theme="simple" 
								onclick="return validarSeleccioneReporteCredito('.cssvalorcuenta','.cssvalormoneda','.cssvalorCuentaGarant','.cssvalorcuentaSust','.cssidCalendar','.cssidCombo','.cssvalortipo','.cssvalorConectorGarant')" 
								cssClass="btn"></s:submit>
							</td>
						</tr>		
					</table>
				</td>
			</tr>
		</table>
		</s:form>
		

		<div id="dialog-modal-detalle-sustento" title="Editar Sustento" style="display:none" >
	<s:form action="saveAllSustento" method="post" theme="simple" name="frmUpdateEditor" id="frmUpdateEditor" >
		<s:hidden id="rowId" name="rowId"/>
		<s:hidden id="sustentoId" name="ideSustento"/>
		<s:hidden id="ceg" name="codEmpresaGrupoS" />
		<s:hidden id="posicionSustento" name="chkSustento" />
		
		
		<table width="100%" cellpadding="0" cellspacing="0" align="center">
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td width="25%"><label>ORDEN POR RANGO INICIAL:</label></td>
				<td width="25%"><label>CUENTA:</label></td>
				<td width="25%"><label>ORDEN FINAL POR RANGO FINAL:</label></td>
				<td width="25%"><label>CUENTA FINAL:</label></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td>
		    		<s:textfield label="orden" size="5" name="ordenSustento" id="sustentoOrden" 
		    		theme="simple" cssStyle="width:25px;text-align: right;" maxlength="3" cssClass="validate[required]" 
		    		onkeypress="return acceptNum(event);" onblur="this.value = validarSiNumero(this.value);"/>
				</td>
				<td>
					<s:select name="idCuentaSustento" list="itemCuentaRDC" id="sustentoCuentaId" 
					listKey="value" listValue="label" cssClass="validate[required]"></s:select>
				</td>
				<td>
			    	<s:textfield label="orden" size="5" name="ordenFinalSustento" id="sustentoOrdenFinal" 
			    	theme="simple" cssStyle="width:25px;text-align: right;" maxlength="3" cssClass="validate[required]" 
			    	onkeypress="return acceptNum(event);" onblur="this.value = validarSiNumero(this.value);"/>
		      	</td>
		      	<td>
	      			<s:select name="idCuentaFinalSustento" list="itemCuentaRDC" id="sustentoCuentaIdFinal" 
	      			listKey="value" listValue="label" cssClass="validate[required]"></s:select>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
		</table>
		<div id="divEdicionSustentos" style="display:none;">
			<FCK:editor instanceName="valorSustento" height="300px">
				<jsp:attribute name="value">&nbsp;</jsp:attribute>									
				
			</FCK:editor>
		</div>
		<table width="100%" cellpadding="0" cellspacing="0" align="left">
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td colspan="3" align="left">
					<input id="btnVerSustento" type="hidden" value="Ver" class="btn" />
					<input id="btnGuardarSustento2" type="button" value="Guardar" class="btn" />
					<input type="button" value="Limpiar" id="limpiarSustentoModal" class="btn"/>
				</td>
			</tr>
		</table>
	</s:form>
</div>
<!--gmp-20130410-->
		
		
	</div>
</div>
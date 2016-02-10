<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://java.fckeditor.net" prefix="FCK" %>


<%@page import="pe.com.bbva.iipf.util.Constantes"%>
<%@page import="java.util.List"%>
<%@page import="pe.com.bbva.iipf.pf.model.Parametro"%>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>
<%
	String x = request.getAttribute("scrollX")==null?"ningun valor":request.getAttribute("scrollX").toString();
	String y = request.getAttribute("scrollY")==null?"ningun valor":request.getAttribute("scrollY").toString();
	String activoBtnValidar = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_BTN_VALIDAR).toString();
	String activoValidarEditor = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_VALIDAR_EDITOR).toString();

%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:head />

<script> 
	function chekseliminar(){
	
	}
	
   function NumberFormat(num, numDec, decSep, thousandSep){ 
    var arg; 
    var Dec; 
    Dec = Math.pow(10, numDec);  
    if (typeof(num) == 'undefined') return;  
    if (typeof(decSep) == 'undefined') decSep = ','; 
    if (typeof(thousandSep) == 'undefined') thousandSep = '.'; 
    if (thousandSep == '.') 
     arg=/./g; 
    else 
     if (thousandSep == ',') arg=/,/g; 
    if (typeof(arg) != 'undefined') num = num.toString().replace(arg,''); 
    num = num.toString().replace(/,/g, '.');  
    if (isNaN(num)) num = "0"; 
    sign = (num == (num = Math.abs(num))); 
    num = Math.floor(num * Dec + 0.50000000001); 
    cents = num % Dec; 
    num = Math.floor(num/Dec).toString();  
    if (cents < (Dec / 10)) cents = "0" + cents;  
    for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++) 
     num = num.substring(0, num.length - (4 * i + 3)) + thousandSep + num.substring(num.length - (4 * i + 3)); 
    if (Dec == 1) 
     return (((sign)? '': '-') + num); 
    else 
     return (((sign)? '': '-') + num + decSep + cents); 
   }  
   function EvaluateText(cadena, obj){ 
    opc = false;  
    if (cadena == "%d") 
     if (event.keyCode > 47 && event.keyCode < 58) 
      opc = true; 
    if (cadena == "%f"){  
     if (event.keyCode > 47 && event.keyCode < 58) 
      opc = true; 
     if (obj.value.search("[.*]") == -1 && obj.value.length != 0) 
      if (event.keyCode == 46) 
       opc = true; 
    } 
    if(opc == false) 
     event.returnValue = false;  
   } 
  </script>
<script language="JavaScript">
var dirurl='';
var vtextarea='';
var tipoEstructura='0';
var VALORMANUAL=1;

var comenPropRiesgo=0;
var comenEstrucLimit=0;
var comenConsidRiesgo=0;

var COMEN_PROP_RIESGO=1;
var COMEN_ESTRUC_LIMIT=2;
var COMEN_CONSID_RIESGO=3;

	function editado(campo){
		if(campo==COMEN_PROP_RIESGO){
			comenPropRiesgo=1;sincronizado=0;
		}else if(campo==COMEN_ESTRUC_LIMIT){
			comenEstrucLimit=1;sincronizado=0;
		}else if(campo==COMEN_CONSID_RIESGO){
			comenConsidRiesgo=1;sincronizado=0;
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
		if(editorInstance.Name=='comentPropRiesgo'){comenPropRiesgo=1; flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='comentEstrucLimite'){comenEstrucLimit=1; flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='considPropRiesgo'){comenConsidRiesgo=1; flagGuardado=false;sincronizado=0;idleTime=0}
	    
	}
	
	function cerrarmsnAutoGuardado(){
	  $("#idmsnAutoGuardado" ).html("");
	}
	function inimsnAutoGuardado(){
		$("#idmsnAutoGuardado" ).html("<font size='3' color='blue'>Autoguardado en proceso....</font>");
	}
		
	function guardarFormulariosActualizadosPagina(uri,vtipoGrabacion){
	   	url=uri;

	   	if(vtipoGrabacion=="AUTO"){
	   		   		VALORMANUAL=0;
	   		   		inimsnAutoGuardado();	   		   		
		}else if (vtipoGrabacion=="MANUAL"){
					VALORMANUAL=1;
		}else{
					VALORMANUAL=0;
		}
	   	//inimsnAutoGuardado();
	   //	guardadoGeneral=1;
	   	sincrono=false;
		if(comenPropRiesgo==1)			{		$("#ssavecomentPropRiesgo").click(); 	}
		if(comenEstrucLimit==1)			{		$("#ssavecomentEstrucLimite").click();	}
		if(comenConsidRiesgo==1)		{		$("#ssaveconsidPropRiesgo").click();	}
		//setInterval("cambiarPagina()", 1000);
		//alert(edicionCampos);
		 if(edicionCampos==1){
	  		try {
	  			$.ajax({
		          	async:false,
		          	type: "POST",
		          	url:document.getElementById("idformPropuestaRiesgo").action,
		          	data:$("#idformPropuestaRiesgo").serialize(),
		       		success: function (data) {
		       			//guardadoGeneral=0;
		       			//sincronizado=1;
		       			//ocultarGuardando();
		       			//cambiarPagina();
		       			setTimeout(cerrarmsnAutoGuardado, 6000);
		       			resetTiempoSession();
		       		}    	
				});
	  		}catch	(e)	{
	  		}
		}else{
		setTimeout(cerrarmsnAutoGuardado, 9000);
		}
		
		ocultarGuardando();
	  	sincronizado = 1;
		cambiarPagina();
		
		return false;
	}

	  
	function noEditado(campo){
		if(campo==COMEN_PROP_RIESGO){
			comenPropRiesgo=0;
		}else if(campo==COMEN_ESTRUC_LIMIT){
			comenEstrucLimit=0;
		}else if(campo==COMEN_CONSID_RIESGO){
			comenConsidRiesgo=0;
		}
		
		if(comenPropRiesgo==0				&
				comenEstrucLimit==0			&
				comenConsidRiesgo==0){
			sincronizado=1;
		}
		
		return false;
	}
	
	function ocultarGuardando(){
		if(guardadoGeneral==0){
			setTimeout($.unblockUI, 1);
		}
	}
	
	function existeElementos(campos,campos2){
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
			if (valor=='9999999'){
				add1 = add1+1;
			}
		});
		
		if(add1>0){
			alert('Seleccione la Empresa en la Estructura Limite...');
			return false;
		}
		return true;
		
	}

	function validarSeleccioneEmpresa(campos2){
		var add1 = 0;
		var valor='';
		$(campos2).each(function() {			
			valor = $(this).val();						
			if (valor=='9999999'){
				add1 = add1+1;
			}
		});
		
		if(add1>0){
			alert('Seleccione la Empresa en la Estructura Limite...');
			return false;
		}
		return true;
	}

	function validarEmpresaPrimer(campos){
		var add = 0;
		var band='TT';
		var valor='';		
		$(campos).each(function() {	
			add = add+1;		
			if (add==1){
				valor = $(this).val();				
				if (valor='TE'){
					band = 'TE';
				}
			}		
		});
		if(band=='TT'){
			alert('Es necesario ingresar primero la Empresa...');
			return false;
		}
		return true;
	}

	function sumar(campos,para){
		var add = 0;
		$(campos).each(function() {
			add += Number($(this).val());			
		});
		$(para).val(add);
		
	}
		
	function sumarDecimal(campos,para){
		var add = 0;
		$(campos).each(function() {

			cvalue=($(this).val()).toString();
			//cvalue=cvalue.split(".").join("");
			cvalue=cvalue.split(",").join("");				
			if(!isNaN(parseFloat(cvalue)) && cvalue.length!=0) {
				add += parseFloat(cvalue);
			}
				
		});		
		add=NumberFormat(add, '2', '.', ',');	
		add.toString();
		$(para).val(add);		
	}
	
	function delAllEstructura()
	{
		document.formPropuestaRiesgo.action='dodelAllEstructuraLimite.do';
		document.formPropuestaRiesgo.submit();
	}
	
	function loadEstructuraini()
	{
		document.formPropuestaRiesgo.action='doloadPropuestaRiesgoIni.do';
		document.formPropuestaRiesgo.submit();
	}
	
	function desactivarFlagValida(valor){
		if(valor!=9999999){
			desactivarFlag();
		}
	}
	$(window).load(function() { setTimeout($.unblockUI, 1);});
	
	(function($){
        $(
            function(){
                $('input:text').setMask();
            }
        );
    })(jQuery);
   $(document).ready(function() {

	   sumarDecimal(".PRcol1","#PRtotal1");
	   sumarDecimal(".PRcol2","#PRtotal2");
		 $(".tareaobs").each(function() {
	        var self = $(this);
		    self.counter({
				count: 'up',
				goal: 1000
			});
		 });
	   	 //When page loads...
		 $(".tab_content").hide(); //Hide all content
		 $("#li7").addClass("active").show(); //Activate first tab
		 $("#tab1").show(); //Show first tab content
		 
   		 $("#idformPropuestaRiesgo").validationEngine();
   		 $(".PRcol1").css('text-align', 'right');
   		 $(".PRcol2").css('text-align', 'right');
   		
   		 
   		 $(".PRcol1").blur(function () { 
   			sumarDecimal(".PRcol1","#PRtotal1");
   		 } );
   		 $(".PRcol2").blur(function () { 
   			sumarDecimal(".PRcol2","#PRtotal2");
   		 } );


  	   $('#stextareacomentPropRiesgo').wysiwyg({			
			rmUnusedControls: true,			
			controls: {				
			bold: { visible : true },				
			html: { visible : true },				
			insertOrderedList: { visible : true },				
			removeFormat: { visible : true }			
			}		
		});

	   $('#stextareacomentPropRiesgo').wysiwyg("addControl", 
		   "UploadFile", {				
		   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
		   tooltip: 'Upload',tags: ['Upload'],				
		   exec:  function() { 		   		
		   		vtextarea='stextareacomentPropRiesgo'; 
		   		newWindowUploadFile(vtextarea);  }
	       }
	    );
   		 
   		$("#bvercomentPropRiesgo").click(function () { 
      			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
      					  overlayCSS: { backgroundColor: '#0174DF' } }); 
      			$("#divcomentPropRiesgo").attr("style","");
      			$("#ssavecomentPropRiesgo").prop("disabled","");
      			$("#scleancomentPropRiesgo").prop("disabled","");
   	   		$.post("consultarProgramaBlob.do", { campoBlob: "campoLibrePR" },
   		   		function(data){
   		   		setTimeout($.unblockUI, 1);  
   		   		//$('#stextareacomentPropRiesgo').wysiwyg('setContent', data);
   		   		var iframe = document.getElementById("comentPropRiesgo___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}    			     	
   		   });
   	   		//editado(COMEN_PROP_RIESGO);
   		});
   		$("#ssavecomentPropRiesgo").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
   					   overlayCSS: { backgroundColor: '#0174DF' } }); 
   			var iframe = document.getElementById("comentPropRiesgo___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			var accionGraba=1;
			var activoTipoGuardado=1;			
			if (VALORMANUAL==0){
				activoTipoGuardado=0;
			}
			if(0 < <%=activoValidarEditor%>){ 	
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
					//var dataGet="campoBlob=campoLibrePR&valorBlob="+valblob;
					var dataGet={campoBlob:'campoLibrePR',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_PROP_RIESGO);
			}else{
			
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=campoLibrePR&valorBlob="+valblob;
					var dataGet={campoBlob:'campoLibrePR',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_PROP_RIESGO);
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("COMENTARIO PROPUESTA RIESGO"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}
   		});
   		$("#scleancomentPropRiesgo").click(function () { 
   		//	$('#stextareacomentPropRiesgo').wysiwyg('setContent', ''); 
   			var iframe = document.getElementById("comentPropRiesgo___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			} 
   		});
   		
		$("#sverificacomentPropRiesgo").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("comentPropRiesgo___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});


 	   $('#stextareacomentEstrucLimite').wysiwyg({			
			rmUnusedControls: true,			
			controls: {				
			bold: { visible : true },				
			html: { visible : true },				
			insertOrderedList: { visible : true },				
			removeFormat: { visible : true }			
			}		
		});

	   $('#stextareacomentEstrucLimite').wysiwyg("addControl", 
		   "UploadFile", {				
		   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
		   tooltip: 'Upload',tags: ['Upload'],				
		   exec:  function() { 		   		
		   		vtextarea='stextareacomentEstrucLimite'; 
		   		newWindowUploadFile(vtextarea);  }
	       }
	    );
   		
   		$("#bvercomentEstrucLimite").click(function () { 
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divcomentEstrucLimite").attr("style","");
  			$("#ssavecomentEstrucLimite").prop("disabled","");
  			$("#scleancomentEstrucLimite").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "estructuraLimite" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareacomentEstrucLimite').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("comentEstrucLimite___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}   			     	
		   });
	   		
	   	   //editado(COMEN_ESTRUC_LIMIT);
		});
		$("#ssavecomentEstrucLimite").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			var iframe = document.getElementById("comentEstrucLimite___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			var accionGraba=1;
			var activoTipoGuardado=1;			
			if (VALORMANUAL==0){
				activoTipoGuardado=0;
			}
			if(0 < <%=activoValidarEditor%>){ 	
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}	
					//var dataGet="campoBlob=estructuraLimite&valorBlob="+valblob;
					var dataGet={campoBlob:'estructuraLimite',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_ESTRUC_LIMIT);
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}			
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=estructuraLimite&valorBlob="+valblob;
					var dataGet={campoBlob:'estructuraLimite',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_ESTRUC_LIMIT);
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("COMENTARIO ESTRUCTURA LIMITE"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}	
		});
		$("#scleancomentEstrucLimite").click(function () { 
			//$('#stextareacomentEstrucLimite').wysiwyg('setContent', '');
			var iframe = document.getElementById("comentEstrucLimite___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}  
		});
		
		$("#sverificacomentEstrucLimite").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("comentEstrucLimite___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});

		
		   $('#stextareaconsidPropRiesgo').wysiwyg({			
				rmUnusedControls: true,			
				controls: {				
				bold: { visible : true },				
				html: { visible : true },				
				insertOrderedList: { visible : true },				
				removeFormat: { visible : true }			
				}		
			});

		   $('#stextareaconsidPropRiesgo').wysiwyg("addControl", 
			   "UploadFile", {				
			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
			   tooltip: 'Upload',tags: ['Upload'],				
			   exec:  function() { 		   		
			   		vtextarea='stextareaconsidPropRiesgo'; 
			   		newWindowUploadFile(vtextarea);  }
		       }
		    );
		
		$("#bverconsidPropRiesgo").click(function () { 
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divconsidPropRiesgo").attr("style","");
  			$("#ssaveconsidPropRiesgo").prop("disabled","");
  			$("#scleanconsidPropRiesgo").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "consideracionPR" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareaconsidPropRiesgo').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("considPropRiesgo___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}  			     	
		   });
	   	   //editado(COMEN_CONSID_RIESGO);
		});
		$("#ssaveconsidPropRiesgo").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			var iframe = document.getElementById("considPropRiesgo___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			var accionGraba=1;
			var activoTipoGuardado=1;			
			if (VALORMANUAL==0){
				activoTipoGuardado=0;
			}
			if(0 < <%=activoValidarEditor%>){ 	
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				//var dataGet="campoBlob=consideracionPR&valorBlob="+valblob;
				var dataGet={campoBlob:'consideracionPR',valorBlob:valblob};
				guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_CONSID_RIESGO);
				
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}			
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=consideracionPR&valorBlob="+valblob;
					var dataGet={campoBlob:'consideracionPR',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_CONSID_RIESGO);
	
				}else if (accionGraba==0){
						alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("CONSIDERACIONES"));
						setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
						setTimeout($.unblockUI, 1);
				}
			}	
		});
		$("#scleanconsidPropRiesgo").click(function () { 
			//$('#stextareaconsidPropRiesgo').wysiwyg('setContent', '');
			var iframe = document.getElementById("considPropRiesgo___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}  
		});   
		
		$("#sverificaconsidPropRiesgo").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("considPropRiesgo___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		
		$('input[name=tipoEstructuraHidden]:radio').click(function () {
			
		     if($(this).val()=='1'){		     	
		     			     	
		     	loadEstructuraini();		     	
		     }else if($(this).val()=='2'){		     		
		     	
		     	delAllEstructura();		     	
		     }
	    });	 
		 
	});
	
     
</script>
 <s:include value="/pages/programa/opciones_programa.jsp"></s:include>
	<%@include file="/pages/programa/tabsCabecera.jsp" %>
<script type="text/javascript">
	var flagGuardado=true;
</script>
<div class="tab_container">
<div class="seccion datosGrupoEmpresa">
<script type="text/javascript">
	var flagGuardado=true;
</script>
<%
	String tipo_empresa = GenericAction.getObjectSession(
			Constantes.COD_TIPO_EMPRESA_SESSION).toString();
	if (tipo_empresa
			.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())) {
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
<%
	} else {
%> <label style="font-size: 12pt; font-weight: bold;">Empresa:
<%=request.getSession().getAttribute(
								"nombre_empresa_grupo_session")%></label> <%
 	}
 %>
</div>

<div id="tab1" class="tab_content">
<s:form
	action="doUpdateBDPropuestaRiesgo" id="idformPropuestaRiesgo" name="formPropuestaRiesgo"
	theme="simple"
	onsubmit="SaveScrollXY(this);">
		<input name="scrollX" id="scrollX" type="hidden"  />
		<input name="scrollY" id="scrollY" type="hidden"  />
	<table border=0 width="100%">
		<tr>
			<td class="bk_tabs">

			<table class="ln_formatos" cellspacing="0">
				<tr>
					<td><label style="font-size: 10pt; font-weight: bold;">Propuesta
					Riesgo:</label></td>
					<td><input type="button" id="bvercomentPropRiesgo" value="Ver"
						class="btn" /> <input
						type="button" value="Guardar" id="ssavecomentPropRiesgo"
						disabled="disabled"
						class="btn"/> <input
						type="button" value="Limpiar" id="scleancomentPropRiesgo"
						disabled="disabled"
						class="btn" />
						<input
						type="button" value="Validar" id="sverificacomentPropRiesgo"
						class="btn" /></td>
				</tr>
			</table>

			<div id="divcomentPropRiesgo" style="display: none;">
					<FCK:editor instanceName="comentPropRiesgo" height="250px">
						<jsp:attribute name="value">&nbsp;
						</jsp:attribute>									
						
					</FCK:editor>
			</div>
			
			<br />
	<div id="divLineasocultarpropries" style="display: none" >
			<s:radio id="idtipoEstructuraHidden" name="tipoEstructuraHidden"
				list="#{'1':'Estructura Limite','2':'Comentario Estructura Limite'}"				
				label="Estructura"/>				
			<div id="iddivEL" class="my_div">
			<table class="ln_formatos" width="100%" cellspacing="0">
				<tr>
					<td>
					<table width="100%" class="ui-widget ui-widget-content">
						<thead>
							<tr>
								<td colspan="8">
									<s:submit id="idaddEmpresaEL" theme="simple" value="+ Empresa"
									action="doaddEmpresaEstructuraLimite"
									cssClass="btn" /> 
									<s:submit id="idaddLineaEL" theme="simple" value="+ Linea" action="doaddEstructuraLimite"
									onclick="return validarEmpresaPrimer('.csstipoEstructura')" 
									cssClass="btn" />
									<s:submit id="iddelestructuraEL" theme="simple" action="dodelEstructuraLimite" value="Eliminar" onclick="return confirmDelete();" disabled="disabled"  cssClass="btn"></s:submit>
									<s:submit id="idsaveestructuraEL" theme="simple" action="dosaveEstructuraLimite" value="Guardar Lista" onclick="return existeElementos('.PRcol1','.cssvalorEmpresa')" disabled="disabled"  cssClass="btn"></s:submit>
								&nbsp;&nbsp;&nbsp;
								Cifras en <s:select  name = "tipoMiles" list="listaTipoMiles" listKey="id" listValue="descripcion" label="" />
								de Dolares(USD)</td>					
							</tr>
							<tr class="ui-widget-header">
								<th>#</th>
								<th>BBVA BANCO CONTINENTAL</th>
								<th>Limite Autorizado</th>
								<th>Limite Formalizado</th>
								<th>Saldo Utilizado</th>
								<th>Propuesto Oficina</th>
								<th>Propuesto Riesgo</th>
								<th>Observacion</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator var="vestructuralimite" value="listEstructuraLimite"
								status="rowstatus">
								<tr>

									<s:hidden name="listEstructuraLimite[%{#rowstatus.index}].id"></s:hidden>
									<s:hidden
										name="listEstructuraLimite[%{#rowstatus.index}].programa.Id"></s:hidden>
									<s:hidden
										name="listEstructuraLimite[%{#rowstatus.index}].empresa.id"></s:hidden>
									
									<s:hidden cssClass="csstipoEstructura" name="listEstructuraLimite[%{#rowstatus.index}].tipo"></s:hidden>
									<td>
										<input type="checkbox" value="<s:property  value="%{#rowstatus.index}"/>" name="chksParaEliminar"></input>
									</td>
									<td>
										<s:set name="tipo" value="%{listEstructuraLimite[#rowstatus.index].tipo}" /> 
										<s:if test="%{#tipo=='TT'}">
											<s:textfield size="40" label="tipoOperacion"
												name="listEstructuraLimite[%{#rowstatus.index}].tipoOperacion"
												value="%{tipoOperacion}" cssClass="validate[required]" maxlength="60"/>
										</s:if> <s:elseif test="%{#tipo=='TE'}">	
											<s:select
												name="listEstructuraLimite[%{#rowstatus.index}].codEmpresatmp"
												cssClass="cssvalorEmpresa"
												list="listaEmpresa" listKey="id" listValue="nombre"
												headerKey="9999999" headerValue="--Seleccione Empresa--" onchange="desactivarFlagValida(this.value);">
											</s:select>
	
										</s:elseif> <s:else>
										</s:else>
									</td>
									<td><s:textfield size="15" label="LimiteAutorizado"
										name="listEstructuraLimite[%{#rowstatus.index}].limiteAutorizado"
										value="%{limiteAutorizado}" theme="simple" cssClass="PRcol1"										 
										onkeypress="EvaluateText('%f', this);" 
										onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
										maxlength="12"/></td>
									<td><s:textfield size="12" label="LimitePropuesto"
										name="listEstructuraLimite[%{#rowstatus.index}].limitePropuesto"
										value="%{limitePropuesto}" theme="simple" cssClass="PRcol2"										
										onkeypress="EvaluateText('%f', this);" 
										onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
										maxlength="12"/></td>
									<td><s:textfield size="12" label="SaldoUtilizado"
										name="listEstructuraLimite[%{#rowstatus.index}].saldoUtilizado"
										value="%{saldoUtilizado}" theme="simple" cssClass="PRcol2"										
										onkeypress="EvaluateText('%f', this);" 
										onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
										maxlength="12"/></td>
									<td><s:textfield size="12" label="PropuestoOficina"
										name="listEstructuraLimite[%{#rowstatus.index}].propuestoOficina"
										value="%{propuestoOficina}" theme="simple" cssClass="PRcol2"										
										onkeypress="EvaluateText('%f', this);" 
										onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
										maxlength="12"/></td>
									<td><s:textfield size="12" label="PropuestoRiesgo"
										name="listEstructuraLimite[%{#rowstatus.index}].propuestaRiesgo"
										value="%{propuestaRiesgo}" theme="simple" cssClass="PRcol2"										
										onkeypress="EvaluateText('%f', this);" 
										onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
										maxlength="12"/></td>
									<td><s:textarea cols="40" label="Observacion" 
										cssClass="tareaobs"
										name="listEstructuraLimite[%{#rowstatus.index}].observacion"
										value="%{observacion}" onchange="desactivarFlag();"/></td>
								</tr>
							</s:iterator>
							
						</tbody>
					</table>
					</td>
				</tr>
			</table>
			</div>

			<br />

			<table class="ln_formatos" cellspacing="0">
				<tr>
					<td><label style="font-size: 10pt; font-weight: bold;">Estructura
					Limite:</label></td>
					<td><input type="button" id="bvercomentEstrucLimite"
						value="Ver"	disabled="disabled"					
						class="btn" /> <input
						type="button" value="Guardar" id="ssavecomentEstrucLimite"
						disabled="disabled"
						class="btn"/> <input
						type="button" value="Limpiar" id="scleancomentEstrucLimite"
						disabled="disabled"
						class="btn" />
						<input
						type="button" value="Validar" id="sverificacomentEstrucLimite"						
						class="btn" /></td>
				</tr>
			</table>
			<div id="divcomentEstrucLimite" style="display: none;">
					<FCK:editor instanceName="comentEstrucLimite" height="250px">
						<jsp:attribute name="value">&nbsp;
						</jsp:attribute>									
						
					</FCK:editor>
			</div>
			
		</div>
			
			
			<br />
			<table class="ln_formatos" cellspacing="0" >
				<tr>
					<td><label style="font-size: 10pt; font-weight: bold;">Consideraciones:</label>
					</td>
					<td><input type="button" id="bverconsidPropRiesgo" value="Ver"
						class="btn" /> <input
						type="button" value="Guardar" id="ssaveconsidPropRiesgo"
						disabled="disabled"						
						class="btn"/> <input
						type="button" value="Limpiar" id="scleanconsidPropRiesgo"
						disabled="disabled"
						class="btn" />
						<input
						type="button" value="Validar" id="sverificaconsidPropRiesgo"						
						class="btn" /></td>
				</tr>
			</table>

			<div id="divconsidPropRiesgo" style="display: none;">
					<FCK:editor instanceName="considPropRiesgo" height="250px">
						<jsp:attribute name="value">&nbsp;
						</jsp:attribute>									
						
					</FCK:editor>
			</div>
			</td>
		</tr>
	</table>
	<script language="JavaScript">	
	   document.getElementById("idtipoEstructuraHidden1").disabled=true;
		var bestructura=false;//document.getElementById("idtipoEstructuraHidden1").checked ;
		var bcomentario=true;//document.getElementById("idtipoEstructuraHidden2").checked ;
		if (bestructura){		
		document.getElementById("idaddEmpresaEL").disabled = false;
		document.getElementById("idaddLineaEL").disabled = false;
		document.getElementById("iddelestructuraEL").disabled = false;
		document.getElementById("idsaveestructuraEL").disabled = false;
		document.getElementById("iddivEL").disabled = false;
		document.getElementById("bvercomentEstrucLimite").disabled = true;
				
		
		}
		if (bcomentario){		
		document.getElementById("idaddEmpresaEL").disabled = true;
		document.getElementById("idaddLineaEL").disabled = true;
		document.getElementById("iddelestructuraEL").disabled = true;
		document.getElementById("idsaveestructuraEL").disabled = true;
		document.getElementById("iddivEL").disabled = true;	
		document.getElementById("bvercomentEstrucLimite").disabled = true;
		}
		
		var btnsverificacomentPropRiesgo= document.getElementById("sverificacomentPropRiesgo");
		var btnsverificacomentEstrucLimite= document.getElementById("sverificacomentEstrucLimite");
		var btnsverificaconsidPropRiesgo= document.getElementById("sverificaconsidPropRiesgo");
		
		if(0 < <%=activoBtnValidar%>){ 	
			btnsverificacomentPropRiesgo.style.visibility  = 'visible'; // Se ve
			btnsverificacomentEstrucLimite.style.visibility  = 'visible'; 
			btnsverificaconsidPropRiesgo.style.visibility  = 'visible'; 
		}else{	   
			btnsverificacomentPropRiesgo.style.display = 'none'; // No ocupa espacio
			btnsverificacomentEstrucLimite.style.display = 'none';
			btnsverificaconsidPropRiesgo.style.display = 'none';
		}

		
	</script>
</s:form>


</div>
</div>
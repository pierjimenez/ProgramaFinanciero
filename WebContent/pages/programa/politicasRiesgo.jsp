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

  
<script language="JavaScript">
var dirurl='';
var vtextarea='';
var VALORMANUAL=1;

var comenOperaGaran=0;
var comenRiesgoTeso=0;
var comenPolitRiesgo=0;
var comenPolitDeleg=0;

var COMEN_OPERA_GARAN=1;
var COMEN_RIESGO_TESO=2;
var COMEN_POLIT_RIESGO=3;
var COMEN_POLIT_DELEG=4;

	function editado(campo){
		if(campo==COMEN_OPERA_GARAN){
			comenOperaGaran=1;sincronizado=0;
		}else if(campo==COMEN_RIESGO_TESO){
			comenRiesgoTeso=1;sincronizado=0;
		}else if(campo==COMEN_POLIT_RIESGO){
			comenPolitRiesgo=1;sincronizado=0;
		}else if(campo==COMEN_POLIT_DELEG){
			comenPolitDeleg=1;sincronizado=0;
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
		if(editorInstance.Name=='comentdetOperGar'){comenOperaGaran=1; flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='comentRiesgoTeso'){comenRiesgoTeso=1; flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='comentPolRiesgoGroup'){comenPolitRiesgo=1; flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='comentPolDelegacion'){comenPolitDeleg=1; flagGuardado=false;sincronizado=0;idleTime=0}
	    
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
	   	//guardadoGeneral=1;
	   	sincrono=false;
		if(comenOperaGaran==1)			{		$("#ssavecomentdetOperGar").click(); 	}
		if(comenRiesgoTeso==1)			{		$("#ssavecomentRiesgoTeso").click(); 	}
		if(comenPolitRiesgo==1)			{		$("#ssavecomentPolRiesgoGroup").click(); 	}
		if(comenPolitDeleg==1)			{		$("#ssavecomentPolDelegacion").click(); 	}
		//setInterval("cambiarPagina()", 1000);
		
		if(edicionCampos==1){
			try {
				$.ajax({
		          	async:false,
		          	type: "POST",
		          	url:document.getElementById("idformPolticasRiesgo").action,
		          	data:$("#idformPolticasRiesgo").serialize(),
		       		success: function (data) {
		       			//guardadoGeneral=0;
		       			//sincronizado=1;
		       			//ocultarGuardando();
		       			//cambiarPagina();
		       			setTimeout(cerrarmsnAutoGuardado, 5000);
		       			resetTiempoSession();
		       		}    	
				});
			} catch (e) {
				 
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
		if(campo==COMEN_OPERA_GARAN){
			comenPropRiesgo=0;
		}else if(campo==COMEN_RIESGO_TESO){
			comenRiesgoTeso=0;
		}else if(campo==COMEN_POLIT_RIESGO){
			comenPolitRiesgo=0;
		}else if(campo==COMEN_POLIT_DELEG){
			comenPolitDeleg=0;
		}
		
		if(comenPropRiesgo==0				&
				comenRiesgoTeso==0			&
				comenPolitRiesgo==0			&
				comenPolitDeleg==0){
			sincronizado=1;
		}
		
		return false;
	}
	
	function ocultarGuardando(){
		if(guardadoGeneral==0){
			setTimeout($.unblockUI, 1);
		}
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
		//add=add/100;
		add=NumberFormat(add, '2', '.', ',');	
		add.toString();
		$(para).val(add);		
	}
	function sumarDecimalColumanTotal(para){
		var add = 0;
		var valor1=0;
		var valor2=0;		

			cvalue2=($("#PoRtotal2").val()).toString();
			cvalue3=($("#PoRtotal3").val()).toString();
			
			//cvalue=cvalue.split(".").join("");
			cvalue2=cvalue2.split(",").join("");
			cvalue3=cvalue3.split(",").join("");	
						
			if(!isNaN(parseFloat(cvalue2)) && cvalue2.length!=0) {
				valor1= parseFloat(cvalue2);
			}
			
			if(!isNaN(parseFloat(cvalue3)) && cvalue3.length!=0) {
				valor2= parseFloat(cvalue3);
			}
			add=valor1+valor2;
				
		
		//add=add/100;
		add=NumberFormat(add, '2', '.', ',');	
		add.toString();
		$(para).val(add);		
	}
	
	function addCalendar(){
	$('.cssidCalendarPRI').each(function(){
		$(this).datepicker({ 
    							dateFormat: 'dd/mm/yy',
    							showOn: "button",
							    buttonImage: "images/calendariobbva.png",
								buttonImageOnly: true });
    	$(this).datepicker();
   	});
	}
	
   $(window).load(function() { setTimeout($.unblockUI, 1);});
   $(document).ready(function() {

	   //sumarDecimal(".PoRcol1","#PoRtotal1");
	   //sumarDecimal(".PoRcol2","#PoRtotal2");
	   //sumarDecimal(".PoRcol3","#PoRtotal3");
	   //sumarDecimal(".PoRcol4","#PoRtotal4");
	   //sumarDecimal(".PoRcol5","#PoRtotal5");
	   //sumarDecimal(".PoRcol6","#PoRtotal6");

	   	 //When page loads...
		 $(".tab_content").hide(); //Hide all content
		 $("#li8").addClass("active").show(); //Activate first tab
		 $("#tab1").show(); //Show first tab content
		 
   		 $("#idformPolticasRiesgo").validationEngine();
   		 $(".PoRcol1").css('text-align', 'right');
   		 $(".PoRcol2").css('text-align', 'right');
   		 $(".PoRcol3").css('text-align', 'right');
   		 $(".PoRcol4").css('text-align', 'right');
   		 $(".PoRcol5").css('text-align', 'right');
   		 $(".PoRcol6").css('text-align', 'right');   		
   		
   		 
   		 $(".PoRcol1").change(function () { 
   			sumarDecimal(".PoRcol1","#PoRtotal1");  	
   		 } );
   		 $(".PoRcol2").change(function () { 
   			sumarDecimal(".PoRcol2","#PoRtotal2");
   			var pos=0;
   			$(".PoRcol2").each(function(){
   				sumarDecimal("input[title='C'][name^='listLimiteFormalizado\["+pos+"\]']",
             "input[title='TC'][name^='listLimiteFormalizado\["+pos+"\]']"); 
             pos++;
   			});   
   		 } );
   		 $(".PoRcol3").change(function () { 
   			sumarDecimal(".PoRcol3","#PoRtotal3");
   			var pos=0;
   			$(".PoRcol3").each(function(){
   				sumarDecimal("input[title='C'][name^='listLimiteFormalizado\["+pos+"\]']",
             "input[title='TC'][name^='listLimiteFormalizado\["+pos+"\]']"); 
             pos++;
   			});   
    		 } );
   		 $(".PoRcol4").blur(function () { 
   			sumarDecimal(".PoRcol4","#PoRtotal4");
    		 } );
   		 $(".PoRcol5").blur(function () { 
   			sumarDecimal(".PoRcol5","#PoRtotal5");
    		 } );
   		 $(".PoRcol6").change(function () { 
   			sumarDecimal(".PoRcol6","#PoRtotal6");
    		 } );
    		 
    	  $("#PoRtotal2").blur(function () { 
   			sumarDecimalColumanTotal("#PoRtotal4");
    		 } );
    		 
    	  $("#PoRtotal3").blur(function () { 
   			sumarDecimalColumanTotal("#PoRtotal4");
    		 } ); 
    		 

  	   $('#stextareacomentdetOperGar').wysiwyg({			
			rmUnusedControls: true,			
			controls: {				
			bold: { visible : true },				
			html: { visible : true },				
			insertOrderedList: { visible : true },				
			removeFormat: { visible : true }			
			}		
		});

	   $('#stextareacomentdetOperGar').wysiwyg("addControl", 
		   "UploadFile", {				
		   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
		   tooltip: 'Upload',tags: ['Upload'],				
		   exec:  function() { 		   		
		   		vtextarea='stextareacomentdetOperGar'; 
		   		newWindowUploadFile(vtextarea);  }
	       }
	    );

   		$("#bvercomentdetOperGar").click(function () { 
      			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
      					  overlayCSS: { backgroundColor: '#0174DF' } }); 
      			$("#divcomentdetOperGar").attr("style","");
      			$("#ssavecomentdetOperGar").prop("disabled","");
      			$("#scleancomentdetOperGar").prop("disabled","");
   	   		$.post("consultarProgramaBlob.do", { campoBlob: "detalleOperacionGarantia" },
   		   		function(data){
   		   		setTimeout($.unblockUI, 1);  
   		   		//$('#stextareacomentdetOperGar').wysiwyg('setContent', data);
   		   		var iframe = document.getElementById("comentdetOperGar___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;
					resetTiempoSession();					
				}   			     	
   		   });
   	   		//editado(COMEN_OPERA_GARAN);
   		});
   		$("#ssavecomentdetOperGar").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
   					   overlayCSS: { backgroundColor: '#0174DF' } }); 
   			var iframe = document.getElementById("comentdetOperGar___Frame");
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
				
				//var dataGet="campoBlob=detalleOperacionGarantia&valorBlob="+valblob;
				var dataGet={campoBlob:'detalleOperacionGarantia',valorBlob:valblob};
				guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_OPERA_GARAN);

			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=detalleOperacionGarantia&valorBlob="+valblob;
					var dataGet={campoBlob:'detalleOperacionGarantia',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_OPERA_GARAN);
	
				}else if (accionGraba==0){
						alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("DETALLE DE OPERACION GARANTIA"));
						setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
						setTimeout($.unblockUI, 1);
				}	
			}
   		});
   		$("#scleancomentdetOperGar").click(function () { 
   			//$('#stextareacomentdetOperGar').wysiwyg('setContent', '');
   			var iframe = document.getElementById("comentdetOperGar___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			} 
   		});
   		
		$("#sverificacomentdetOperGar").click(function () { 
				
				$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
						   overlayCSS: { backgroundColor: '#0174DF' } });			
				var iframe = document.getElementById("comentdetOperGar___Frame");
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;
				if ( eInnerElement ){
					validarHTMLRenderTiny(eInnerElement);				
				}  
			});


 	   $('#stextareacomentRiesgoTeso').wysiwyg({			
			rmUnusedControls: true,			
			controls: {				
			bold: { visible : true },				
			html: { visible : true },				
			insertOrderedList: { visible : true },				
			removeFormat: { visible : true }			
			}		
		});

	   $('#stextareacomentRiesgoTeso').wysiwyg("addControl", 
		   "UploadFile", {				
		   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
		   tooltip: 'Upload',tags: ['Upload'],				
		   exec:  function() { 		   		
		   		vtextarea='stextareacomentRiesgoTeso'; 
		   		newWindowUploadFile(vtextarea);  }
	       }
	    );
   		
   		$("#bvercomentRiesgoTeso").click(function () { 
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divcomentRiesgoTeso").attr("style","");
  			$("#ssavecomentRiesgoTeso").prop("disabled","");
  			$("#scleancomentRiesgoTeso").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "riesgoTesoreria" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareacomentRiesgoTeso').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("comentRiesgoTeso___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}   			     	
		   });
	   		//editado(COMEN_RIESGO_TESO);
		});
		$("#ssavecomentRiesgoTeso").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			var iframe = document.getElementById("comentRiesgoTeso___Frame");
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
					//var dataGet="campoBlob=riesgoTesoreria&valorBlob="+valblob;
					var dataGet={campoBlob:'riesgoTesoreria',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_RIESGO_TESO);

			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=riesgoTesoreria&valorBlob="+valblob;
					var dataGet={campoBlob:'riesgoTesoreria',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_RIESGO_TESO);
	
				}else if (accionGraba==0){
						alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("RIESGO DE TESORERIA"));
						setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
						setTimeout($.unblockUI, 1);
				}
			}
		});
		$("#scleancomentRiesgoTeso").click(function () { 
			//$('#stextareacomentRiesgoTeso').wysiwyg('setContent', '');
			var iframe = document.getElementById("comentRiesgoTeso___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}  
		});
		
		$("#sverificacomentRiesgoTeso").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("comentRiesgoTeso___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});

		   $('#stextareacomentPolRiesgoGroup').wysiwyg({			
				rmUnusedControls: true,			
				controls: {				
				bold: { visible : true },				
				html: { visible : true },				
				insertOrderedList: { visible : true },				
				removeFormat: { visible : true }			
				}		
			});

		   $('#stextareacomentPolRiesgoGroup').wysiwyg("addControl", 
			   "UploadFile", {				
			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
			   tooltip: 'Upload',tags: ['Upload'],				
			   exec:  function() { 		   		
			   		vtextarea='stextareacomentPolRiesgoGroup'; 
			   		newWindowUploadFile(vtextarea);  }
		       }
		    );

		$("#bvercomentPolRiesgoGroup").click(function () { 
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divcomentPolRiesgoGroup").attr("style","");
  			$("#ssavecomentPolRiesgoGroup").prop("disabled","");
  			$("#scleancomentPolRiesgoGroup").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "politicasRiesGrupo" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareacomentPolRiesgoGroup').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("comentPolRiesgoGroup___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}   			     	
		   });
	   		//editado(COMEN_POLIT_RIESGO);
		});
		$("#ssavecomentPolRiesgoGroup").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			var iframe = document.getElementById("comentPolRiesgoGroup___Frame");
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
					//var dataGet="campoBlob=politicasRiesGrupo&valorBlob="+valblob;
					var dataGet={campoBlob:'politicasRiesGrupo',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_POLIT_RIESGO);
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
	
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=politicasRiesGrupo&valorBlob="+valblob;
					var dataGet={campoBlob:'politicasRiesGrupo',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_POLIT_RIESGO);
	
				}else if (accionGraba==0){
						alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("POLITICAS DE RIESGO DE GRUPO"));
						setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
						setTimeout($.unblockUI, 1);
				}
			}
		});
		$("#scleancomentPolRiesgoGroup").click(function () { 
			//$('#stextareacomentPolRiesgoGroup').wysiwyg('setContent', '');
			var iframe = document.getElementById("comentPolRiesgoGroup___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}   
		});   
		
		$("#sverificacomentPolRiesgoGroup").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("comentPolRiesgoGroup___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});

		   $('#stextareacomentPolDelegacion').wysiwyg({			
				rmUnusedControls: true,			
				controls: {				
				bold: { visible : true },				
				html: { visible : true },				
				insertOrderedList: { visible : true },				
				removeFormat: { visible : true }			
				}		
			});

		   $('#stextareacomentPolDelegacion').wysiwyg("addControl", 
			   "UploadFile", {				
			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
			   tooltip: 'Upload',tags: ['Upload'],				
			   exec:  function() { 		   		
			   		vtextarea='stextareacomentPolDelegacion'; 
			   		newWindowUploadFile(vtextarea);  }
		       }
		    );
		
		$("#bvercomentPolDelegacion").click(function () { 
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divcomentPolDelegacion").attr("style","");
  			$("#ssavecomentPolDelegacion").prop("disabled","");
  			$("#scleancomentPolDelegacion").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "politicasDelegacion" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareacomentPolDelegacion').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("comentPolDelegacion___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}   			     	
		   });
	   		//editado(COMEN_POLIT_DELEG);
		});
		$("#ssavecomentPolDelegacion").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			var iframe = document.getElementById("comentPolDelegacion___Frame");
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

					//var dataGet="campoBlob=politicasDelegacion&valorBlob="+valblob;
					var dataGet={campoBlob:'politicasDelegacion',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_POLIT_DELEG);
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=politicasDelegacion&valorBlob="+valblob;
					var dataGet={campoBlob:'politicasDelegacion',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_POLIT_DELEG);
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("POLITICAS DE DELEGACION"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
						setTimeout($.unblockUI, 1);
				}	
			}	
		});
		$("#scleancomentPolDelegacion").click(function () { 
			//$('#stextareacomentPolDelegacion').wysiwyg('setContent', '');
			var iframe = document.getElementById("comentPolDelegacion___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}  
		});   
		
		$("#sverificacomentPolDelegacion").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("comentPolDelegacion___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		
		addCalendar();
	});
	
     
</script>
 <s:include value="/pages/programa/opciones_programa.jsp"></s:include>
	<%@include file="/pages/programa/tabsCabecera.jsp" %>
<script type="text/javascript">
	var flagGuardado=true;
</script>
<div class="tab_container">
<div class="seccion datosGrupoEmpresa">
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
	action="doUpdateBDPoliticasRiesgo" id="idformPolticasRiesgo"
	theme="simple"
	onsubmit="SaveScrollXY(this);">
		<input name="scrollX" id="scrollX" type="hidden"  />
		<input name="scrollY" id="scrollY" type="hidden"  />
	<table border=0 width="100%">
		<tr>
			<td class="bk_tabs">
			<table class="ln_formatos" cellspacing="0" width="100%">
				<tr>
					<td><s:submit value="Guardar"
						cssClass="btn"
						theme="simple"></s:submit></td>
						<td colspan="4" align="right">
								<div id="idmsnAutoGuardado"></div>
						</td>
				</tr>
			</table>
			<br />
			<div class="my_div">

			<table class="ln_formatos" width="100%" cellspacing="0">

				<tr>
					<td>


					<table width="100%" class="ui-widget ui-widget-content">
						<thead>
							<tr class="ui-widget-header ">
								<th colspan="2">Cifras en <s:select  name = "tipoMiles" list="listaTipoMiles" listKey="id" listValue="descripcion" label="" /> de Dolares</th>
								<th colspan="3">LIMITE FORMALIZADO</th>
								<th colspan="2"></th>
							</tr>
							<tr class="ui-widget-header ">
								<th></th>
								<th>LTE AUTORIZADO</th>
								<th>COMPROMETIDO</th>
								<th>NO COMPROMETIDO</th>
								<th>TOTAL</th>
								<th>DISPUESTO</th>
								<th>LTE PROPUESTO</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator var="vlimiteformalizado"
								value="listLimiteFormalizado" status="rowstatus">
								
								<s:set name="tipo" value="%{listLimiteFormalizado[#rowstatus.index].tipoTotal}" /> 
								<s:if test="%{#tipo=='DD'}">
									<tr>
	
										<s:hidden name="listLimiteFormalizado[%{#rowstatus.index}].id"></s:hidden>
										<s:hidden
											name="listLimiteFormalizado[%{#rowstatus.index}].programa.Id"></s:hidden>
										<s:hidden
											name="listLimiteFormalizado[%{#rowstatus.index}].tipoRiesgo.id"></s:hidden>
										<s:hidden
											name="listLimiteFormalizado[%{#rowstatus.index}].tipoTotal"></s:hidden>
	
										<td><s:textfield size="47" label="tipoRiesgo"
											readonly="true"
											name="listLimiteFormalizado[%{#rowstatus.index}].tipoRiesgo.descripcion"
											value="%{tipoRiesgo.descripcion}" /></td>
										<td><s:textfield size="14" label="LimiteAutorizado"
											name="listLimiteFormalizado[%{#rowstatus.index}].limiteAutorizado"
											value="%{limiteAutorizado}" theme="simple" cssClass="PoRcol1"										
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')" 
											maxlength="12"/></td>
										<td><s:textfield size="15" label="comprometido"
											name="listLimiteFormalizado[%{#rowstatus.index}].comprometido"
											value="%{comprometido}" theme="simple" cssClass="PoRcol2"										
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')" 
											maxlength="12"
											title="C"/></td>
										<td><s:textfield size="15" label="noComprometido"
											name="listLimiteFormalizado[%{#rowstatus.index}].noComprometido"
											value="%{noComprometido}" theme="simple" cssClass="PoRcol3"										
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
											maxlength="12"
											title="C"/></td>
										<td><s:textfield size="14" label="total"
											name="listLimiteFormalizado[%{#rowstatus.index}].total"
											value="%{total}" theme="simple" cssClass="PoRcol4"										
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
											maxlength="12"
											title="TC"
											readonly="true"/></td>
										<td><s:textfield size="14" label="dispuesto"
											name="listLimiteFormalizado[%{#rowstatus.index}].dispuesto"
											value="%{dispuesto}" theme="simple" cssClass="PoRcol5"										
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')" 
											maxlength="12"/></td>
	
										<td><s:textfield size="14" label="limitePropuesto"
											name="listLimiteFormalizado[%{#rowstatus.index}].limitePropuesto"
											value="%{limitePropuesto}" theme="simple" cssClass="PoRcol6"										
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')" 
											maxlength="12"/></td>
	
									</tr>								
								</s:if> 
								<s:elseif test="%{#tipo=='TT'}">	
								
										<tr class="ui-widget-header ">
	
										<s:hidden name="listLimiteFormalizado[%{#rowstatus.index}].id"></s:hidden>
										<s:hidden
											name="listLimiteFormalizado[%{#rowstatus.index}].programa.Id"></s:hidden>
										<s:hidden
											name="listLimiteFormalizado[%{#rowstatus.index}].tipoRiesgo.id"></s:hidden>
										<s:hidden
											name="listLimiteFormalizado[%{#rowstatus.index}].tipoTotal"></s:hidden>
	
										<td><s:textfield size="47" label="tipoRiesgo"
											readonly="true"
											name="listLimiteFormalizado[%{#rowstatus.index}].tipoRiesgo.descripcion"
											value="%{tipoRiesgo.descripcion}" /></td>
										<td><s:textfield size="14" label="LimiteAutorizado"
											name="listLimiteFormalizado[%{#rowstatus.index}].limiteAutorizado"
											value="%{limiteAutorizado}" theme="simple" 
											id="PoRtotal1" 
											cssStyle="text-align: right;"										
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')" 
											maxlength="12"/></td>
										<td><s:textfield size="15" label="comprometido"
											name="listLimiteFormalizado[%{#rowstatus.index}].comprometido"
											value="%{comprometido}" theme="simple" 
											id="PoRtotal2"
											cssStyle="text-align: right;"										
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')" 
											maxlength="12"
											title="C"/></td>
										<td><s:textfield size="15" label="noComprometido"
											name="listLimiteFormalizado[%{#rowstatus.index}].noComprometido"
											value="%{noComprometido}" theme="simple" 
											id="PoRtotal3" 
											cssStyle="text-align: right;"									
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
											maxlength="12"
											title="C"/></td>
										<td><s:textfield size="14" label="total"
											name="listLimiteFormalizado[%{#rowstatus.index}].total"
											value="%{total}" theme="simple" 
											id="PoRtotal4"
											cssStyle="text-align: right;"										
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
											maxlength="12"
											title="TC"
											readonly="true"/></td>
										<td><s:textfield size="14" label="dispuesto"
											name="listLimiteFormalizado[%{#rowstatus.index}].dispuesto"
											value="%{dispuesto}" theme="simple" 
											id="PoRtotal5"
											readonly="true"
											cssStyle="text-align: right;"										
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')" 
											maxlength="12"/></td>
	
										<td><s:textfield size="14" label="limitePropuesto"
											name="listLimiteFormalizado[%{#rowstatus.index}].limitePropuesto"
											value="%{limitePropuesto}" theme="simple" 
											id="PoRtotal6"
											cssStyle="text-align: right;"										
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')" 
											maxlength="12"/></td>
	
									</tr>		

								</s:elseif> 
								<s:else>
								</s:else>								
								
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
					<td><label style="font-size: 10pt; font-weight: bold;">Detalles
					de Operaciones y Garantias:</label></td>
					<td><input type="button" id="bvercomentdetOperGar" value="Ver"
						class="btn" /> <input
						type="button" value="Guardar" id="ssavecomentdetOperGar"
						disabled="disabled"
						class="btn"/> <input
						type="button" value="Limpiar" id="scleancomentdetOperGar"
						disabled="disabled"
						class="btn" />
						<input
						type="button" value="Validar" id="sverificacomentdetOperGar"						
						class="btn" /></td>
				</tr>
			</table>

			<div id="divcomentdetOperGar" style="display: none;">
						<FCK:editor instanceName="comentdetOperGar" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
			</div>


			<br />

			<table class="ln_formatos" cellspacing="0">
				<tr>
					<td><label style="font-size: 10pt; font-weight: bold;">Riesgo
					de Tesoreria:</label></td>
					<td><input type="button" id="bvercomentRiesgoTeso" value="Ver"
						class="btn" /> <input
						type="button" value="Guardar" id="ssavecomentRiesgoTeso"
						disabled="disabled"
						class="btn"/> <input
						type="button" value="Limpiar" id="scleancomentRiesgoTeso"
						disabled="disabled"
						class="btn" />
						<input
						type="button" value="Validar" id="sverificacomentRiesgoTeso"						
						class="btn" />
						</td>
				</tr>
			</table>
			<div id="divcomentRiesgoTeso" style="display: none;">
						<FCK:editor instanceName="comentRiesgoTeso" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
			</div>


			<br />

			<table class="ln_formatos" cellspacing="0">
				<tr>
					<td><label style="font-size: 10pt; font-weight: bold;">Politicas
					de Riesgo de Grupo:</label></td>
					<td><input type="button" id="bvercomentPolRiesgoGroup"
						value="Ver"
						class="btn" /> <input
						type="button" value="Guardar" id="ssavecomentPolRiesgoGroup"
						disabled="disabled"
						class="btn"/> <input
						type="button" value="Limpiar" id="scleancomentPolRiesgoGroup"
						disabled="disabled"
						class="btn" />
						<input
						type="button" value="Validar" id="sverificacomentPolRiesgoGroup"						
						class="btn" />
						</td>
				</tr>
			</table>

			<div id="divcomentPolRiesgoGroup" style="display: none;">
						<FCK:editor instanceName="comentPolRiesgoGroup" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
			</div>

			<br />

			<table class="ln_formatos" cellspacing="0">
				<tr>
					<td><label style="font-size: 10pt; font-weight: bold;">Politicas
					de Delegacion:</label></td>
					<td><input type="button" id="bvercomentPolDelegacion"
						value="Ver"
						class="btn" /> <input
						type="button" value="Guardar" id="ssavecomentPolDelegacion"
						disabled="disabled"
						class="btn"/> <input
						type="button" value="Limpiar" id="scleancomentPolDelegacion"
						disabled="disabled"
						class="btn" />
						<input
						type="button" value="Validar" id="sverificacomentPolDelegacion"						
						class="btn" />
						</td>
				</tr>
			</table>

			<div id="divcomentPolDelegacion" style="display: none;">
						<FCK:editor instanceName="comentPolDelegacion" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
			</div>
			<br />

			<table class="ui-widget ui-widget-content" cellspacing="0">
				<tr>
					<td Style="border: 1px solid #eee;">Limites Anteriores
					Autorizados por:</td>
					<td><s:textfield name="programa.limiteAutorizadoPRG"
						id="tflimiteAuto" cssClass="validate[required]" size="70" maxlength="60"/></td>
				</tr>
				<tr>
					<td Style="border: 1px solid #eee;">Nueva fecha de vencimiento PF:</td>
					<td><s:textfield name="programa.proximaRevisionPRG"
						id="tfproximaRevision" 						
						size="10" 
						cssClass="validate[required] cssidCalendarPRI" 
						onblur="javascript:valFecha(this);"
						maxlength="10"/></td>
				</tr>
				<tr>
					<td Style="border: 1px solid #eee;">Ámbito de sanción:</td>
					<td><s:textfield name="programa.motivoProximaPRG"
						id="tfmotivoProxima" cssClass="validate[required]" size="70" maxlength="60"/></td>
				</tr>

			</table>

			<br />
			<br />

			<table class="ln_formatos" cellspacing="0">
				<tr>
					<td><s:submit value="Guardar" theme="simple"
						cssClass="btn"></s:submit>
					</td>
				</tr>
			</table>

			</td>
		</tr>
	</table>
</s:form></div>
</div>
<script language="JavaScript">

var btnsverificacomentdetOperGar= document.getElementById("sverificacomentdetOperGar");
var btnsverificacomentRiesgoTeso= document.getElementById("sverificacomentRiesgoTeso");
var btnsverificacomentPolRiesgoGroup= document.getElementById("sverificacomentPolRiesgoGroup");
var btnsverificacomentPolDelegacion= document.getElementById("sverificacomentPolDelegacion");


if(0 < <%=activoBtnValidar%>){ 	
	btnsverificacomentdetOperGar.style.visibility  = 'visible'; // Se ve	
	btnsverificacomentRiesgoTeso.style.visibility  = 'visible'; 
	btnsverificacomentPolRiesgoGroup.style.visibility  = 'visible';
	btnsverificacomentPolDelegacion.style.visibility  = 'visible'; 
}else{	   
	btnsverificacomentdetOperGar.style.display = 'none'; // No ocupa espacio
	btnsverificacomentRiesgoTeso.style.display = 'none';
	btnsverificacomentPolRiesgoGroup.style.display = 'none';
	btnsverificacomentPolDelegacion.style.display = 'none';
	
}
		 	
</script>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://java.fckeditor.net" prefix="FCK" %>

<%@page import="pe.com.bbva.iipf.util.Constantes"%>
<%@page import="java.util.List"%>
<%@page import="pe.com.bbva.iipf.pf.model.Parametro"%>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>

<%
String activoBtnValidar = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_BTN_VALIDAR).toString();
String activoValidarEditor = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_VALIDAR_EDITOR).toString();

%>

<script language="JavaScript">
var dirurl='';
var vtextarea='';
var fortalezaRiesgo=0;
var comentOprt=0;
var comentDebil=0;
var comenAmena=0;
var comenFoda=0;
var VALORMANUAL=1;

var FORTALEZA_RIESGO=1;
var COMEN_OPORT=2;
var COMEN_DEBIL=3;
var COMEN_AMENA=4;
var COMEN_FODA=5;

	function editado(campo){
		if(campo==FORTALEZA_RIESGO){
			fortalezaRiesgo=1;sincronizado=0;
		}else if(campo==COMEN_OPORT){
			comentOprt=1;sincronizado=0;
		}else if(campo==COMEN_DEBIL){
			comentDebil=1;sincronizado=0;
		}else if(campo==COMEN_AMENA){
			comenAmena=1;sincronizado=0;
		}else if(campo==COMEN_FODA){
			comenFoda=1;sincronizado=0;
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
		if(editorInstance.Name=='comentfortaleza'){fortalezaRiesgo=1; flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='comentOportunidades'){comentOprt=1; flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='comentDebilidades'){comentDebil=1; flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='comentAmenaza'){comenAmena=1; flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='concluFODA'){comenFoda=1; flagGuardado=false;sincronizado=0;idleTime=0}
	    
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
		if(fortalezaRiesgo==1)	{		$("#ssavecomentfortaleza").click(); 	}
		if(comentOprt==1)		{		$("#ssavecomentOportunidades").click();	}
		if(comentDebil==1)		{		$("#ssavecomentDebilidades").click();	}	
		if(comenAmena==1)		{		$("#ssavecomentAmenaza").click();		}
		if(comenFoda==1)		{		$("#ssaveconcluFODA").click();		}		
		sincronizado = 1;
		setTimeout(cerrarmsnAutoGuardado, 9000);
		cambiarPagina();
		return false;
	}


	function noEditado(campo){
		if(campo==FORTALEZA_RIESGO){
			fortalezaRiesgo=0;
		}else if(campo==COMEN_OPORT){
			comentOprt=0;
		}else if(campo==COMEN_DEBIL){
			comentDebil=0;
		}else if(campo==COMEN_AMENA){
			comenAmena=0;
		}else if(campo==COMEN_FODA){
			comenFoda=0;
		}
		
		if(fortalezaRiesgo==0			&
				comentOprt==0			&
				comentDebil==0 			&
				comenAmena==0 			&
				comenFoda==0){
			sincronizado=1;
		}
		
		return false;
	}
	
	function ocultarGuardando(){
		if(guardadoGeneral==0){
			setTimeout($.unblockUI, 1);
		}
	}
	$(document).ready(function() {	
			   
	  	 //When page loads...
		 $(".tab_content").hide(); //Hide all content
		 $("#li6").addClass("active").show(); //Activate first tab
		 $("#tab1").show(); //Show first tab content	


		   $('#stextareacomentfortaleza').wysiwyg({			
				rmUnusedControls: true,			
				controls: {				
				bold: { visible : true },				
				html: { visible : true },				
				insertOrderedList: { visible : true },				
				removeFormat: { visible : true }			
				}		
			});

		   $('#stextareacomentfortaleza').wysiwyg("addControl", 
			   "UploadFile", {				
			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
			   tooltip: 'Upload',tags: ['Upload'],				
			   exec:  function() { 		   		
			   		vtextarea='stextareacomentfortaleza'; 
			   		newWindowUploadFile(vtextarea);  }
		       }
		    );


	   		$("#bvercomentfortaleza").click(function () { 
      			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
      					  overlayCSS: { backgroundColor: '#0174DF' } }); 
      			$("#divcomentfortaleza").attr("style","");
      			$("#ssavecomentfortaleza").prop("disabled","");
      			$("#scleancomentfortaleza").prop("disabled","");
   	   			$.post("consultarProgramaBlob.do", { campoBlob: "fodaFotalezas" },
   		   		function(data){
   		   		setTimeout($.unblockUI, 1);  
   		   		//$('#stextareacomentfortaleza').wysiwyg('setContent', data);
   		   		var iframe = document.getElementById("comentfortaleza___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;
					resetTiempoSession();					
				}  	  	
				//editado(FORTALEZA_RIESGO);
   		   });
   		});
   		$("#ssavecomentfortaleza").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
   					   overlayCSS: { backgroundColor: '#0174DF' } }); 
   			var iframe = document.getElementById("comentfortaleza___Frame");
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
				
				//var dataGet="campoBlob=fodaFotalezas&valorBlob="+valblob;
				var dataGet={campoBlob:'fodaFotalezas',valorBlob:valblob};
				guardarDatosTexto("saveProgramaBlob.do",dataGet,FORTALEZA_RIESGO);

			
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=fodaFotalezas&valorBlob="+valblob;
					var dataGet={campoBlob:'fodaFotalezas',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,FORTALEZA_RIESGO);
	
				}else if (accionGraba==0){
						alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("FORTALEZA"));
						setTimeout($.unblockUI, 1);
					}else if (accionGraba==3){
						setTimeout($.unblockUI, 1);
				}
			}	
   		});
   		$("#scleancomentfortaleza").click(function () { 
   			//$('#stextareacomentfortaleza').wysiwyg('setContent', '');
   			var iframe = document.getElementById("comentfortaleza___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			} 
   		});
   		
	$("#sverificacomentfortaleza").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("comentfortaleza___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});



 	   $('#stextareacomentOportunidades').wysiwyg({			
			rmUnusedControls: true,			
			controls: {				
			bold: { visible : true },				
			html: { visible : true },				
			insertOrderedList: { visible : true },				
			removeFormat: { visible : true }			
			}		
		});

	   $('#stextareacomentOportunidades').wysiwyg("addControl", 
		   "UploadFile", {				
		   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
		   tooltip: 'Upload',tags: ['Upload'],				
		   exec:  function() { 		   		
		   		vtextarea='stextareacomentOportunidades'; 
		   		newWindowUploadFile(vtextarea);  }
	       }
	    );
   		
   		$("#bvercomentOportunidades").click(function () { 
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divcomentOportunidades").attr("style","");
  			$("#ssavecomentOportunidades").prop("disabled","");
  			$("#scleancomentOportunidades").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "fodaOportunidades" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareacomentOportunidades').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("comentOportunidades___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}  		  			     	
		   });
	   		//editado(COMEN_OPORT);
		});
		$("#ssavecomentOportunidades").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
					   
			var iframe = document.getElementById("comentOportunidades___Frame");
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
				//var dataGet="campoBlob=fodaOportunidades&valorBlob="+valblob;
				var dataGet={campoBlob:'fodaOportunidades',valorBlob:valblob};
				guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_OPORT);

			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=fodaOportunidades&valorBlob="+valblob;
					var dataGet={campoBlob:'fodaOportunidades',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_OPORT);
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("OPORTUNIDAD"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}	
		});
		$("#scleancomentOportunidades").click(function () { 
			//$('#stextareacomentOportunidades').wysiwyg('setContent', '');
			var iframe = document.getElementById("comentOportunidades___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			} 
		});
		
	$("#sverificacomentOportunidades").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("comentOportunidades___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});

		   $('#stextareacomentDebilidades').wysiwyg({			
				rmUnusedControls: true,			
				controls: {				
				bold: { visible : true },				
				html: { visible : true },				
				insertOrderedList: { visible : true },				
				removeFormat: { visible : true }			
				}		
			});

		   $('#stextareacomentDebilidades').wysiwyg("addControl", 
			   "UploadFile", {				
			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
			   tooltip: 'Upload',tags: ['Upload'],				
			   exec:  function() { 		   		
			   		vtextarea='stextareacomentDebilidades'; 
			   		newWindowUploadFile(vtextarea);  }
		       }
		    );
		
		$("#bvercomentDebilidades").click(function () { 
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divcomentDebilidades").attr("style","");
  			$("#ssavecomentDebilidades").prop("disabled","");
  			$("#scleancomentDebilidades").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "fodaDebilidades" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareacomentDebilidades').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("comentDebilidades___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}   			     	
		   });
	   		//editado(COMEN_DEBIL);
		});
		$("#ssavecomentDebilidades").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			var iframe = document.getElementById("comentDebilidades___Frame");
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
				//var dataGet="campoBlob=fodaDebilidades&valorBlob="+valblob;
				var dataGet={campoBlob:'fodaDebilidades',valorBlob:valblob};
				guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_DEBIL);
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=fodaDebilidades&valorBlob="+valblob;
					var dataGet={campoBlob:'fodaDebilidades',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_DEBIL);
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("DEBILIDADES"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}	
		});
		$("#scleancomentDebilidades").click(function () { 
		//	$('#stextareacomentDebilidades').wysiwyg('setContent', '');
			var iframe = document.getElementById("comentDebilidades___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}  
		}); 
		
		$("#sverificacomentDebilidades").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("comentDebilidades___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});

		   $('#stextareacomentAmenaza').wysiwyg({			
				rmUnusedControls: true,			
				controls: {				
				bold: { visible : true },				
				html: { visible : true },				
				insertOrderedList: { visible : true },				
				removeFormat: { visible : true }			
				}		
			});

		   $('#stextareacomentAmenaza').wysiwyg("addControl", 
			   "UploadFile", {				
			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
			   tooltip: 'Upload',tags: ['Upload'],				
			   exec:  function() { 		   		
			   		vtextarea='stextareacomentAmenaza'; 
			   		newWindowUploadFile(vtextarea);  }
		       }
		    );
		
		$("#bvercomentAmenaza").click(function () { 
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divcomentAmenaza").attr("style","");
  			$("#ssavecomentAmenaza").prop("disabled","");
  			$("#scleancomentAmenaza").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "fodaAmenazas" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareacomentAmenaza').wysiwyg('setContent', data);
		   	 	var iframe = document.getElementById("comentAmenaza___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;		
					resetTiempoSession();			
				}  			     	
		   });
	   		
		   //editado(COMEN_AMENA);
		});
		$("#ssavecomentAmenaza").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			var iframe = document.getElementById("comentAmenaza___Frame");
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
				//var dataGet="campoBlob=fodaAmenazas&valorBlob="+valblob;
				var dataGet={campoBlob:'fodaAmenazas',valorBlob:valblob};
				guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_AMENA);

			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
	
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=fodaAmenazas&valorBlob="+valblob;
					var dataGet={campoBlob:'fodaAmenazas',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_AMENA);
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("AMENAZA"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}	
			}	
		});
		$("#scleancomentAmenaza").click(function () { 
			//$('#stextareacomentAmenaza').wysiwyg('setContent', '');
			var iframe = document.getElementById("comentAmenaza___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}   
		});  
		
		$("#sverificacomentAmenaza").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("comentAmenaza___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		 

		   $('#stextareaconcluFODA').wysiwyg({			
				rmUnusedControls: true,			
				controls: {				
				bold: { visible : true },				
				html: { visible : true },				
				insertOrderedList: { visible : true },				
				removeFormat: { visible : true }			
				}		
			});

		   $('#stextareaconcluFODA').wysiwyg("addControl", 
			   "UploadFile", {				
			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
			   tooltip: 'Upload',tags: ['Upload'],				
			   exec:  function() { 		   		
			   		vtextarea='stextareaconcluFODA'; 
			   		newWindowUploadFile(vtextarea);  }
		       }
		    );

		$("#bverconcluFODA").click(function () { 
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divconcluFODA").attr("style","");
  			$("#ssaveconcluFODA").prop("disabled","");
  			$("#scleanconcluFODA").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "conclucionFoda" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareaconcluFODA').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("concluFODA___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}    			     	
		   });
	   		//editado(COMEN_FODA);
		});
		$("#ssaveconcluFODA").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			var iframe = document.getElementById("concluFODA___Frame");
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
				//var dataGet="campoBlob=conclucionFoda&valorBlob="+valblob;
				var dataGet={campoBlob:'conclucionFoda',valorBlob:valblob};
				guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_FODA);

			}else{
			
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=conclucionFoda&valorBlob="+valblob;
					var dataGet={campoBlob:'conclucionFoda',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_FODA);
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("CONCLUSION FODA"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}	
		});
		$("#scleanconcluFODA").click(function () { 
			//$('#stextareaconcluFODA').wysiwyg('setContent', '');
			var iframe = document.getElementById("concluFODA___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}  
		});  
		
		$("#sverificaconcluFODA").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("concluFODA___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});

		 
	
	}); 

	$(function() {
		$( "#tabsFR" ).tabs();		
	});
	
</script>
 <s:include value="/pages/programa/opciones_programa.jsp"></s:include>
	<%@include file="/pages/programa/tabsCabecera.jsp" %>
<script type="text/javascript">
	var flagGuardado=true;
	$(window).load(function() { setTimeout($.unblockUI, 1);});
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
	action="doUpdateBDFactoresRiesgo" name="fromFactoresRiesgo"
	theme="simple">
	<input name="scrollX" id="scrollX" type="hidden"  />
	<input name="scrollY" id="scrollY" type="hidden"  />
	<div class="my_div" >
		<table>
			<tr>	
				<td align="right">
					<div id="idmsnAutoGuardado"></div>
				</td>
			</tr>
			<tr>
				<td class="bk_tabs">
	
				<DIV>
				<DIV id=tabsFR>
				<UL>
					<LI><A href="#tabsFR1">Fortalezas</A></LI>
					<LI><A href="#tabsFR2">Oportunidades</A></LI>
					<LI><A href="#tabsFR3">Debilidades</A></LI>
					<LI><A href="#tabsFR4">Amenazas</A></LI>
				</UL>
				<DIV id=tabsFR1>
	
	
				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td><label style="font-size: 10pt; font-weight: bold;">Fortaleza:</label>
						</td>
						<td><input type="button" id="bvercomentfortaleza" value="Ver"
							class="btn" /> 
							<input type="button" value="Guardar" id="ssavecomentfortaleza"
							disabled="disabled"
							class="btn"/> 
							<input type="button" value="Limpiar" id="scleancomentfortaleza"
							disabled="disabled"
							class="btn" />
							<input type="button" value="Validar" id="sverificacomentfortaleza"
							class="btn" /></td>
					</tr>
				</table>
				<div id="divcomentfortaleza" style="display: none;">
						<FCK:editor instanceName="comentfortaleza" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
				</div>
				</DIV>
	
				<DIV id=tabsFR2>
	
	
				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td><label style="font-size: 10pt; font-weight: bold;">Oportunidades:</label>
						</td>
						<td><input type="button" id="bvercomentOportunidades"
							value="Ver"
							class="btn" /> 
							<input type="button" value="Guardar" id="ssavecomentOportunidades"
							disabled="disabled"
							class="btn"/> 
							<input type="button" value="Limpiar" id="scleancomentOportunidades"
							disabled="disabled"
							class="btn" />
							<input type="button" value="Validar" id="sverificacomentOportunidades"
							class="btn" /></td>
					</tr>
				</table>
	
				<div id="divcomentOportunidades" style="display: none;">
						<FCK:editor instanceName="comentOportunidades" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
				</div>
				</DIV>
	
				<DIV id=tabsFR3>
	
				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td><label style="font-size: 10pt; font-weight: bold;">Debilidades:</label>
						</td>
						<td><input type="button" id="bvercomentDebilidades"
							value="Ver"
							class="btn" /> 
							<input type="button" value="Guardar" id="ssavecomentDebilidades"
							disabled="disabled"
							class="btn"/> 
							<input type="button" value="Limpiar" id="scleancomentDebilidades"
							disabled="disabled"
							class="btn" />
							<input type="button" value="Validar" id="sverificacomentDebilidades"
							class="btn" /></td>
					</tr>
				</table>
	
				<div id="divcomentDebilidades" style="display: none;">
						<FCK:editor instanceName="comentDebilidades" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
				</div>
	
	
	
				</DIV>
				<DIV id=tabsFR4>
	
				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td><label style="font-size: 10pt; font-weight: bold;">Amenaza:</label>
						</td>
						<td><input type="button" id="bvercomentAmenaza" value="Ver"
							class="btn" /> 
							<input type="button" value="Guardar" id="ssavecomentAmenaza"
							disabled="disabled"
							class="btn"/> 
							<input type="button" value="Limpiar" id="scleancomentAmenaza"
							disabled="disabled"
							class="btn" />
							<input type="button" value="Validar" id="sverificacomentAmenaza"
							class="btn" /></td>
					</tr>
				</table>
	
				<div id="divcomentAmenaza" style="display: none;">
						<FCK:editor instanceName="comentAmenaza" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
				</div>
	
				</DIV>
				</DIV>
	
				<br />
				<br />
				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td><label style="font-size: 10pt; font-weight: bold;">Conclusiones:</label>
						</td>
						<td><input type="button" id="bverconcluFODA" value="Ver"
							class="btn" /> 
							<input type="button" value="Guardar" id="ssaveconcluFODA"
							disabled="disabled"
							class="btn"/> 
							<input type="button" value="Limpiar" id="scleanconcluFODA"
							disabled="disabled"
							class="btn" />
							<input type="button" value="Validar" id="sverificaconcluFODA"							
							class="btn" /></td>
					</tr>
				</table>
	
				<div id="divconcluFODA" style="display: none;">
						<FCK:editor instanceName="concluFODA" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
				</div>
	
	
				</DIV>
				<!-- End demo --></td>
			</tr>
		</table>	
	</div>
</s:form></div>
</div>

<script language="JavaScript">

var btnsverificaconcluFODA= document.getElementById("sverificaconcluFODA");
var btnsverificacomentAmenaza= document.getElementById("sverificacomentAmenaza");
var btnsverificacomentDebilidades= document.getElementById("sverificacomentDebilidades");
var btnsverificacomentOportunidades= document.getElementById("sverificacomentOportunidades");
var btnsverificacomentfortaleza= document.getElementById("sverificacomentfortaleza");



if(0 < <%=activoBtnValidar%>){ 	
	btnsverificaconcluFODA.style.visibility  = 'visible'; // Se ve
	btnsverificacomentAmenaza.style.visibility  = 'visible';
	btnsverificacomentDebilidades.style.visibility  = 'visible';
	btnsverificacomentOportunidades.style.visibility  = 'visible';
	btnsverificacomentfortaleza.style.visibility  = 'visible';
	
}else{	   
	btnsverificaconcluFODA.style.display = 'none'; // No ocupa espacio
	btnsverificacomentAmenaza.style.display = 'none';
	btnsverificacomentDebilidades.style.display = 'none';
	btnsverificacomentOportunidades.style.display = 'none';
	btnsverificacomentfortaleza.style.display = 'none';
	
}

		 	
</script>
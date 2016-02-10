<%@taglib prefix="s" uri="/struts-tags" %>  
<%@taglib uri="http://java.fckeditor.net" prefix="FCK" %>


<%@page import="pe.com.stefanini.core.action.GenericAction"%>
<%@page import="pe.com.bbva.iipf.util.Constantes"%>
<%
String tipoEmpresa = GenericAction.getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION)==null?"":GenericAction.getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
String activoBtnGeneral = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_BTN_GENERAL).toString();
String activoBtnValidar = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_BTN_VALIDAR).toString();
String activoValidarEditor = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_VALIDAR_EDITOR).toString();

%>
<script language="JavaScript">
var dirurl='';
var vtextarea='';
var activarmensajeAutograbar=0;
var situacionFinanciera=0;//1
var situacionEconomica=0;//2
var situacionEconomicaFinanciera=0;//3
var fueraBalance=0;//4


var SITUACION_FINANCIERA=1;//1
var SITUACION_ECONOMICA=2;//2
var SITUACION_ECONOMICA_FINANCIERA=3;//3
var FUERA_BALANCE=4;//4
var sincrono=true;


	function editado(campo){
		if(campo==SITUACION_FINANCIERA){
			situacionFinanciera=1;sincronizado=0;
		}else if(campo==SITUACION_ECONOMICA){
			situacionEconomica=1;sincronizado=0;
		}else if(campo==SITUACION_ECONOMICA_FINANCIERA){
			situacionEconomicaFinanciera=1;sincronizado=0;
		}else if(campo==FUERA_BALANCE){
			fueraBalance=1;sincronizado=0;
		}
		return false;
	}
	function noEditado(campo){
		
		if(campo==SITUACION_FINANCIERA){
			situacionFinanciera=0;
		}else if(campo==SITUACION_ECONOMICA){
			situacionEconomica=0;
		}else if(campo==SITUACION_ECONOMICA_FINANCIERA){
			situacionEconomicaFinanciera=0;
		}else if(campo==FUERA_BALANCE){
			fueraBalance=0;
		}
		if(situacionFinanciera==0				&
				situacionEconomica==0			&
				situacionEconomicaFinanciera==0 &
				fueraBalance==0){
			sincronizado=1;
		}
		return false;
	}

	function mostrarGuardando(){
		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/>Guardando Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } }); 
	}
	function ocultarGuardando(){
		//if(guardadoGeneral==0){
			setTimeout($.unblockUI, 1);
		//}
	}

	function closeMsnGuardado(){	  
		var divs = document.getElementById('idMenssageSave');	  
		divs.style.display = "none";
		divs.innerHTML="";
	}
	function showMsnGuardado(){
		var divs = document.getElementById('idMenssageSave');	  
		divs.style.display = "";
		divs.innerHTML="<font size='3' color='blue'> Se ha Guardado Satisfactoriamente....</font>";
	}
	
	function descargarExcel(){		
		document.forms["sintesisEconomico"].action='downloadPlantillaCompletada.do';
		document.forms["sintesisEconomico"].submit();			
	}
	
	function descargarSintesisFinanciero(id,ext,nomarch){
		document.forms["sintesisEconomico"].action='downloadSintesis.do';
		document.forms["sintesisEconomico"].codigoArchivo.value =id; 
		document.forms["sintesisEconomico"].extension.value =ext;
		document.forms["sintesisEconomico"].nombreArchivo.value =nomarch;
		document.forms["sintesisEconomico"].submit();	
		resetTiempoSession();
	}
	
	function eliminar(id,ext,nomarch){
		document.forms["sintesisEconomico"].action='eliminarArchiSinteEcono.do';
		document.forms["sintesisEconomico"].codigoArchivo.value =id; 
		document.forms["sintesisEconomico"].extension.value =ext;
		document.forms["sintesisEconomico"].nombreArchivo.value =nomarch;
		document.forms["sintesisEconomico"].submit();	
	}
	function onloadSintesisEconomico(){
	    document.forms["sintesisEconomico"].action='initAnalisisEconomica.do';
		document.forms["sintesisEconomico"].submit();	
	}

	$(window).load(function() { setTimeout($.unblockUI, 1);});	
    $(document).ready(function() {
	   	 //When page loads...
		 $(".tab_content").hide(); //Hide all content
		 $("#li2").addClass("active").show(); //Activate first tab
		 $("#tab1").show(); //Show first tab content
   		 $("#sintesisEconomico").validationEngine();

  	   $('#stextareasitFina').wysiwyg({			
			rmUnusedControls: true,			
			controls: {				
			bold: { visible : true },				
			html: { visible : true },				
			insertOrderedList: { visible : true },				
			removeFormat: { visible : true }			
			}		
		});

	   $('#stextareasitFina').wysiwyg("addControl", 
		   "UploadFile", {				
		   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
		   tooltip: 'Upload',tags: ['Upload'],				
		   exec:  function() { 		   		
		   		vtextarea='stextareasitFina'; 
		   		newWindowUploadFile(vtextarea);  }
	       }
	    );
   		 
   		 $("#bversitFina").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divsitFina").attr("style","");
   			$("#ssavesitFina").prop("disabled","");
   			$("#scleansitFina").prop("disabled","");
   			situacionFinanciera=1;
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var idgrupo = $("input[name=id_grupo]").val();
	   		var codempresa= $("#codigoEmpresa").val();
	   		if(tipoempresa == '2' ){ 
			   		$.post("consultarProgramaBlob.do", { campoBlob: "comenSituFinanciera" },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareasitFina').wysiwyg('setContent', data);
				   		var iframe = document.getElementById("sitFina___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;	
							resetTiempoSession();				
						}  
						//editado(SITUACION_FINANCIERA);
				   });		   
		    }else{
			     if (codempresa==idgrupo){
			   		$.post("consultarProgramaBlob.do", { campoBlob: "comenSituFinanciera" },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareasitFina').wysiwyg('setContent', data);
				   		var iframe = document.getElementById("sitFina___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;	
							resetTiempoSession();				
						} 
						//editado(SITUACION_FINANCIERA);
				   });	
			     }else{
			   		$.post("consultarSintesisEconBlob.do", { campoBlob: "comenSituFinanciera", codEmpresa:codempresa  },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareasitFina').wysiwyg('setContent', data);
				   		var iframe = document.getElementById("sitFina___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;	
							resetTiempoSession();				
						}  
						//editado(SITUACION_FINANCIERA);
				   });	
			     }			
			}		   
		   
		});
		$("#ssavesitFina").click(function () { 
			mostrarGuardando();	   		
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var idgrupo = $("input[name=id_grupo]").val();	   		
	   		var codempresa= $("#codigoEmpresa").val();	
	   		var iframe = document.getElementById("sitFina___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			var accionGraba=1;
			
			if(0 < <%=activoValidarEditor%>){
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
		   		if(tipoempresa == '2' ){
   					//var dataGet="campoBlob=comenSituFinanciera&valorBlob="+valblob;
   					var dataGet={campoBlob:'comenSituFinanciera',valorBlob:valblob};
   					guardarDatosTexto("saveProgramaBlob.do",dataGet,SITUACION_FINANCIERA);
				}else{ 				    						
				      if (codempresa==idgrupo){			     
				      		//var dataGet="campoBlob=comenSituFinanciera&valorBlob="+valblob;
				      		var dataGet={campoBlob:'comenSituFinanciera',valorBlob:valblob};
			   				guardarDatosTexto("saveProgramaBlob.do",dataGet,SITUACION_FINANCIERA);
				      	
				      }else{	
				    	  	//var dataGet="campoBlob=comenSituFinanciera&valorBlob="+valblob+"&codEmpresa="+codempresa;
				    	  	var dataGet={campoBlob:'comenSituFinanciera',valorBlob:valblob,codEmpresa:codempresa};
				   			guardarDatosTexto("saveSintesisEconBlob.do",dataGet,SITUACION_FINANCIERA);
				      }					   			
				}
				
			}else{
			
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activarmensajeAutograbar);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){
			   		if(tipoempresa == '2' ){
			   					//var dataGet="campoBlob=comenSituFinanciera&valorBlob="+valblob;
			   					var dataGet={campoBlob:'comenSituFinanciera',valorBlob:valblob};
			   					guardarDatosTexto("saveProgramaBlob.do",dataGet,SITUACION_FINANCIERA);
		    		}else{ 				    						
					      if (codempresa==idgrupo){			     
					      		//var dataGet="campoBlob=comenSituFinanciera&valorBlob="+valblob;
					      		var dataGet={campoBlob:'comenSituFinanciera',valorBlob:valblob};
				   				guardarDatosTexto("saveProgramaBlob.do",dataGet,SITUACION_FINANCIERA);
					      	
					      }else{	
					    	  	//var dataGet="campoBlob=comenSituFinanciera&valorBlob="+valblob+"&codEmpresa="+codempresa;
					    	  	var dataGet={campoBlob:'comenSituFinanciera',valorBlob:valblob,codEmpresa:codempresa};
					   			guardarDatosTexto("saveSintesisEconBlob.do",dataGet,SITUACION_FINANCIERA);
					      }					   			
					}	
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUAL("COMENTARIO SITUACION FINANCIERA"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}
			
		});
		$("#scleansitFina").click(function () { 
			//$('#stextareasitFina').wysiwyg('setContent', '');
			var iframe = document.getElementById("sitFina___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}   
		});
		
		$("#sverificasitFina").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });
			situacionFinanciera=1;
			var iframe = document.getElementById("sitFina___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});


		   $('#stextareasitEcono').wysiwyg({			
				rmUnusedControls: true,			
				controls: {				
				bold: { visible : true },				
				html: { visible : true },				
				insertOrderedList: { visible : true },				
				removeFormat: { visible : true }			
				}		
			});

		   $('#stextareasitEcono').wysiwyg("addControl", 
			   "UploadFile", {				
			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
			   tooltip: 'Upload',tags: ['Upload'],				
			   exec:  function() { 		   		
			   		vtextarea='stextareasitEcono'; 
			   		newWindowUploadFile(vtextarea);  }
		       }
		    );
		
		 $("#bversitEcono").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divsitEcono").attr("style","");
   			$("#ssavesitEcono").prop("disabled","");
   			$("#scleansitEcono").prop("disabled","");
   			situacionEconomica=1;
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var idgrupo = $("input[name=id_grupo]").val();
	   		var codempresa= $("#codigoEmpresa").val();
	   		if(tipoempresa == '2' ){ 	
		   		$.post("consultarProgramaBlob.do", { campoBlob: "comenSituEconomica" },
			   		function(data){
			   		setTimeout($.unblockUI, 1);  
			   		//$('#stextareasitEcono').wysiwyg('setContent', data);
			   		var iframe = document.getElementById("sitEcono___Frame");				
					var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
					var eInnerElement = oCell.firstChild;				
					if ( eInnerElement ){					
						eInnerElement.contentWindow.document.body.innerHTML = data;		
						resetTiempoSession();			
					} 
					//editado(SITUACION_ECONOMICA);
			   	});
		    }else{		   	    						
				if (codempresa==idgrupo){		
			   		$.post("consultarProgramaBlob.do", { campoBlob: "comenSituEconomica" },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareasitEcono').wysiwyg('setContent', data);  		
				   		var iframe = document.getElementById("sitEcono___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;	
							resetTiempoSession();				
						}   
						//editado(SITUACION_ECONOMICA);
				   	});			   
			   }else{
			   		$.post("consultarSintesisEconBlob.do", { campoBlob: "comenSituEconomica", codEmpresa:codempresa },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareasitEcono').wysiwyg('setContent', data);
				   		var iframe = document.getElementById("sitEcono___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;	
							resetTiempoSession();				
						} 
						//editado(SITUACION_ECONOMICA);
				   	});			   
			   }			
			}	   
		   
		});
		$("#ssavesitEcono").click(function () { 
			mostrarGuardando();
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var idgrupo = $("input[name=id_grupo]").val();	   		
	   		var codempresa= $("#codigoEmpresa").val();		   	  		
	   		var iframe = document.getElementById("sitEcono___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			var accionGraba=1;
			if(0 < <%=activoValidarEditor%>){
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}	
				if(tipoempresa == '2' ){
	   				//var dataGet="campoBlob=comenSituEconomica&valorBlob="+valblob;
	   				var dataGet={campoBlob:'comenSituEconomica',valorBlob:valblob};
	   				guardarDatosTexto("saveProgramaBlob.do",dataGet,SITUACION_ECONOMICA);
	   		 	   		
	    		}else{ 				    						
					if (codempresa==idgrupo){
						//var dataGet="campoBlob=comenSituEconomica&valorBlob="+valblob;
						var dataGet={campoBlob:'comenSituEconomica',valorBlob:valblob};
			   			guardarDatosTexto("saveProgramaBlob.do",dataGet,SITUACION_ECONOMICA);									
					}else{	
						//var dataGet="campoBlob=comenSituEconomica&valorBlob="+valblob+"&codEmpresa="+codempresa;
						var dataGet={campoBlob:'comenSituEconomica',valorBlob:valblob,codEmpresa:codempresa};
		   				guardarDatosTexto("saveSintesisEconBlob.do",dataGet,SITUACION_ECONOMICA);						
					}					   			
				}
	
				
			}else{
				
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activarmensajeAutograbar);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}			
	
				if (accionGraba==1 || accionGraba==2){
			   		if(tipoempresa == '2' ){
			   				//var dataGet="campoBlob=comenSituEconomica&valorBlob="+valblob;
			   				var dataGet={campoBlob:'comenSituEconomica',valorBlob:valblob};
			   				guardarDatosTexto("saveProgramaBlob.do",dataGet,SITUACION_ECONOMICA);
			   		 	   		
		    		}else{ 				    						
						if (codempresa==idgrupo){
							//var dataGet="campoBlob=comenSituEconomica&valorBlob="+valblob;
							var dataGet={campoBlob:'comenSituEconomica',valorBlob:valblob};
				   			guardarDatosTexto("saveProgramaBlob.do",dataGet,SITUACION_ECONOMICA);									
						}else{	
							//var dataGet="campoBlob=comenSituEconomica&valorBlob="+valblob+"&codEmpresa="+codempresa;
							var dataGet={campoBlob:'comenSituEconomica',valorBlob:valblob,codEmpresa:codempresa};
			   				guardarDatosTexto("saveSintesisEconBlob.do",dataGet,SITUACION_ECONOMICA);						
						}					   			
					}
	
				}else if (accionGraba==0){
						alert(MENSAJEERROR_GUARDADO_MANUAL("COMENTARIO SITUACION ECONOMICA"));
						setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
						setTimeout($.unblockUI, 1);
				}		
			}		
		});
		$("#scleansitEcono").click(function () { 
			//$('#stextareasitEcono').wysiwyg('setContent', '');
			var iframe = document.getElementById("sitEcono___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}    
		});
		
		$("#sverificasitEcono").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });
			situacionEconomica=1;
			var iframe = document.getElementById("sitEcono___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		
		   $('#stextareavalsitEconoFin').wysiwyg({			
				rmUnusedControls: true,			
				controls: {				
				bold: { visible : true },				
				html: { visible : true },				
				insertOrderedList: { visible : true },				
				removeFormat: { visible : true }			
				}		
			});

		   $('#stextareavalsitEconoFin').wysiwyg("addControl", 
			   "UploadFile", {				
			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
			   tooltip: 'Upload',tags: ['Upload'],				
			   exec:  function() { 		   		
			   		vtextarea='stextareavalsitEconoFin'; 
			   		newWindowUploadFile(vtextarea);  }
		       }
		    );

		
		$("#bvervalsitEconoFin").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divvalsitEconoFin").attr("style","");
   			$("#ssavevalsitEconoFin").prop("disabled","");
   			$("#scleanvalsitEconoFin").prop("disabled","");
   			situacionEconomicaFinanciera=1;
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var idgrupo = $("input[name=id_grupo]").val();
	   		var codempresa= $("#codigoEmpresa").val();
	   		if(tipoempresa == '2' ){ 	   		
		   		$.post("consultarProgramaBlob.do", { campoBlob: "valoracionEconFinanciera" },
			   		function(data){
			   		setTimeout($.unblockUI, 1);  
			   		//$('#stextareavalsitEconoFin').wysiwyg('setContent', data);
			   			var iframe = document.getElementById("valsitEconoFin___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;	
							resetTiempoSession();				
						}    
						//editado(SITUACION_ECONOMICA_FINANCIERA);
			   });
			   
			}else{	
			     if (codempresa==idgrupo){
			   		$.post("consultarProgramaBlob.do", { campoBlob: "valoracionEconFinanciera" },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareavalsitEconoFin').wysiwyg('setContent', data);
				   		var iframe = document.getElementById("valsitEconoFin___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;	
							resetTiempoSession();				
						}  
						//editado(SITUACION_ECONOMICA_FINANCIERA);
				   });
			     }else{
			   		$.post("consultarSintesisEconBlob.do", { campoBlob: "valoracionEconFinanciera" , codEmpresa:codempresa },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareavalsitEconoFin').wysiwyg('setContent', data);
				   		var iframe = document.getElementById("valsitEconoFin___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;	
							resetTiempoSession();				
						}   
						//editado(SITUACION_ECONOMICA_FINANCIERA);
				   });
			     }			
			}
		   
		   
		});
		$("#ssavevalsitEconoFin").click(function () { 
			mostrarGuardando();
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var idgrupo = $("input[name=id_grupo]").val();	   		
	   		var codempresa= $("#codigoEmpresa").val();	
	   		var iframe = document.getElementById("valsitEconoFin___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			var accionGraba=1;
			
			if(0 < <%=activoValidarEditor%>){ 
				
					if ( eInnerElement ){
						//validarEditor(eInnerElement);
						valblob = eInnerElement.contentWindow.document.body.innerHTML;
					}
			   		if(tipoempresa == '2' ){  		
		   				//var dataGet="campoBlob=valoracionEconFinanciera&valorBlob="+valblob;
		   				var dataGet={campoBlob:'valoracionEconFinanciera',valorBlob:valblob};
							guardarDatosTexto("saveProgramaBlob.do",dataGet,SITUACION_ECONOMICA_FINANCIERA);		   		
					}else{ 				    						
					      if (codempresa==idgrupo){
					    	  	//var dataGet="campoBlob=valoracionEconFinanciera&valorBlob="+valblob;
					    	  	var dataGet={campoBlob:'valoracionEconFinanciera',valorBlob:valblob};
				   				guardarDatosTexto("saveProgramaBlob.do",dataGet,SITUACION_ECONOMICA_FINANCIERA);			    	
					      }else{	
					    	  	//var dataGet="campoBlob=valoracionEconFinanciera&valorBlob="+valblob+"&codEmpresa="+codempresa;
					    	  	var dataGet={campoBlob:'valoracionEconFinanciera',valorBlob:valblob,codEmpresa:codempresa};
				   				guardarDatosTexto("saveSintesisEconBlob.do",dataGet,SITUACION_ECONOMICA_FINANCIERA);	
					    	
					      }		
					} 
				
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activarmensajeAutograbar);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){
			   		if(tipoempresa == '2' ){  		
				   				//var dataGet="campoBlob=valoracionEconFinanciera&valorBlob="+valblob;
				   				var dataGet={campoBlob:'valoracionEconFinanciera',valorBlob:valblob};
		   						guardarDatosTexto("saveProgramaBlob.do",dataGet,SITUACION_ECONOMICA_FINANCIERA);		   		
					}else{ 				    						
					      if (codempresa==idgrupo){
					    	  	//var dataGet="campoBlob=valoracionEconFinanciera&valorBlob="+valblob;
					    	  	var dataGet={campoBlob:'valoracionEconFinanciera',valorBlob:valblob};
				   				guardarDatosTexto("saveProgramaBlob.do",dataGet,SITUACION_ECONOMICA_FINANCIERA);			    	
					      }else{	
					    	  	//var dataGet="campoBlob=valoracionEconFinanciera&valorBlob="+valblob+"&codEmpresa="+codempresa;
					    	  	var dataGet={campoBlob:'valoracionEconFinanciera',valorBlob:valblob,codEmpresa:codempresa};
				   				guardarDatosTexto("saveSintesisEconBlob.do",dataGet,SITUACION_ECONOMICA_FINANCIERA);	
					    	
					      }		
					}    				
	
				}else if (accionGraba==0){
						alert(MENSAJEERROR_GUARDADO_MANUAL("VALORACION SITUACION ECONOMICA-FINANCIERA"));
						setTimeout($.unblockUI, 1);
					}else if (accionGraba==3){
						setTimeout($.unblockUI, 1);
				}	
				
			}
			
		});
		$("#scleanvalsitEconoFin").click(function () { 
			//$('#stextareavalsitEconoFin').wysiwyg('setContent', '');
			var iframe = document.getElementById("valsitEconoFin___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}   
		});
		
		$("#sverificavalsitEconoFin").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });
			situacionEconomicaFinanciera=1;
			var iframe = document.getElementById("valsitEconoFin___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});


		   $('#stextareaanalisitEconoFin').wysiwyg({			
				rmUnusedControls: true,			
				controls: {				
				bold: { visible : true },				
				html: { visible : true },				
				insertOrderedList: { visible : true },				
				removeFormat: { visible : true }			
				}		
			});

		   $('#stextareaanalisitEconoFin').wysiwyg("addControl", 
			   "UploadFile", {				
			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
			   tooltip: 'Upload',tags: ['Upload'],				
			   exec:  function() { 		   		
			   		vtextarea='stextareaanalisitEconoFin'; 
			   		newWindowUploadFile(vtextarea);  }
		       }
		    );
		
		$("#bveranalisitEconoFin").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divanalisitEconoFin").attr("style","");
   			$("#ssaveanalisitEconoFin").prop("disabled","");
   			$("#scleananalisitEconoFin").prop("disabled","");
   			fueraBalance=1;
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var idgrupo = $("input[name=id_grupo]").val();
	   		var codempresa= $("#codigoEmpresa").val();
	   		if(tipoempresa == '2' ){	   		
		   		$.post("consultarProgramaBlob.do", { campoBlob: "valoracionPosiBalance" },
			   		function(data){
			   		setTimeout($.unblockUI, 1);  
			   		//$('#stextareaanalisitEconoFin').wysiwyg('setContent', data);
			   			var iframe = document.getElementById("analisitEconoFin___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;	
							resetTiempoSession();				
						}  
						//editado(FUERA_BALANCE);
			   });			   
			 }else{	  
			     if (codempresa==idgrupo){
			   		$.post("consultarProgramaBlob.do", { campoBlob: "valoracionPosiBalance" },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareaanalisitEconoFin').wysiwyg('setContent', data);
					   	var iframe = document.getElementById("analisitEconoFin___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;	
							resetTiempoSession();				
						}    
						//editado(FUERA_BALANCE);
				   });	
			     }else{
			   		$.post("consultarSintesisEconBlob.do", { campoBlob: "valoracionPosiBalance", codEmpresa:codempresa },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareaanalisitEconoFin').wysiwyg('setContent', data);
				   		var iframe = document.getElementById("analisitEconoFin___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;	
							resetTiempoSession();				
						}   
						//editado(FUERA_BALANCE);
				   });	
			     }			
			}		   
		   
		});
		$("#ssaveanalisitEconoFin").click(function () { 
			mostrarGuardando();
	   			   		
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var idgrupo = $("input[name=id_grupo]").val();	   		
	   		var codempresa= $("#codigoEmpresa").val();	
	   		var iframe = document.getElementById("analisitEconoFin___Frame");
			var oCell =	iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			var valblob = null;
			var accionGraba=1;
			if(0 < <%=activoValidarEditor%>){ 
				if ( eInnerElement ){
					//validarEditor(eInnerElement);					
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if(tipoempresa == '2' ){
		   			
   					//var dataGet="campoBlob=valoracionPosiBalance&valorBlob="+valblob;
   					var dataGet={campoBlob:'valoracionPosiBalance',valorBlob:valblob};
						guardarDatosTexto("saveProgramaBlob.do",dataGet,FUERA_BALANCE);
				}else{ 				    						
				      if (codempresa==idgrupo){			    	  
				    	  	//var dataGet="campoBlob=valoracionPosiBalance&valorBlob="+valblob;
				    	  	var dataGet={campoBlob:'valoracionPosiBalance',valorBlob:valblob};
			   				guardarDatosTexto("saveProgramaBlob.do",dataGet,FUERA_BALANCE);			   		
				      }else{					    	  
				    	  	//var dataGet="campoBlob=valoracionPosiBalance&valorBlob="+valblob+"&codEmpresa="+codempresa;
				    	  	var dataGet={campoBlob:'valoracionPosiBalance',valorBlob:valblob,codEmpresa:codempresa};
			   				guardarDatosTexto("saveSintesisEconBlob.do",dataGet,FUERA_BALANCE);			    	  				
				      }					   			
				}
				
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activarmensajeAutograbar);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){				
				   		if(tipoempresa == '2' ){
				   			
				   					//var dataGet="campoBlob=valoracionPosiBalance&valorBlob="+valblob;
				   					var dataGet={campoBlob:'valoracionPosiBalance',valorBlob:valblob};
			   						guardarDatosTexto("saveProgramaBlob.do",dataGet,FUERA_BALANCE);
			    		}else{ 				    						
						      if (codempresa==idgrupo){			    	  
						    	  	//var dataGet="campoBlob=valoracionPosiBalance&valorBlob="+valblob;
						    	  	var dataGet={campoBlob:'valoracionPosiBalance',valorBlob:valblob};
					   				guardarDatosTexto("saveProgramaBlob.do",dataGet,FUERA_BALANCE);			   		
						      }else{					    	  
						    	  	//var dataGet="campoBlob=valoracionPosiBalance&valorBlob="+valblob+"&codEmpresa="+codempresa;
						    	  	var dataGet={campoBlob:'valoracionPosiBalance',valorBlob:valblob,codEmpresa:codempresa};
					   				guardarDatosTexto("saveSintesisEconBlob.do",dataGet,FUERA_BALANCE);			    	  				
						      }					   			
						}
				   		
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUAL("ANALISIS DE VALORACION POSICION"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}
    				
		});
		$("#scleananalisitEconoFin").click(function () { 
			//$('#stextareaanalisitEconoFin').wysiwyg('setContent', ''); 
			var iframe = document.getElementById("analisitEconoFin___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}   
		});	
		
		$("#sverificaanalisitEconoFin").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });
			fueraBalance=1;
			var iframe = document.getElementById("analisitEconoFin___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		
		
		
		//add mcg
		$('select[name=codigoEmpresa]').change(function () {		    
   			$("#ssavesitFina").prop("disabled","true");
   			$("#scleansitFina").prop("disabled","true");
   			
  			$("#ssavesitEcono").prop("disabled","true");
   			$("#scleansitEcono").prop("disabled","true");

   			$("#ssavevalsitEconoFin").prop("disabled","true");
   			$("#scleanvalsitEconoFin").prop("disabled","true");

   			$("#ssaveanalisitEconoFin").prop("disabled","true");
   			$("#scleananalisitEconoFin").prop("disabled","true"); 
   			onloadSintesisEconomico();

	    });
		
		$("#idgrabarGeneralSE").click(function () { 
			showLoading();
// 				sincronizado=1;
// 	    		flagGuardado=true;
	    		activarmensajeAutograbar=1;
	    		var vtipoGrabacion="MANUAL";	    			    		
	    		guardarFormulariosActualizadosPagina(null,vtipoGrabacion);
// 	    		showMsnGuardado();
// 	    		setTimeout(closeMsnGuardado, 3000);
					   
		});
		
	});

   function FCKeditor_OnComplete( editorInstance )
       {	   
               if (document.all) {        // If Internet Explorer.
                     editorInstance.EditorDocument.attachEvent("onkeydown", function(event){desactivarFlagEditor(editorInstance);} ) ;                     
                     //editorInstance.EditorDocument.attachEvent("blur", function(event){desactivarFlagEditor(editorInstance);} ) ;                     
                     
               } else {                // If Gecko.
                     editorInstance.EditorDocument.addEventListener( 'keypress', function(event){desactivarFlagEditor(editorInstance);}, true ) ;
           }         
       }
	
	function desactivarFlagEditor(editorInstance)
	{
		if(editorInstance.Name=='sitFina'){situacionFinanciera=1; flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='sitEcono'){situacionEconomica=1;flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='valsitEconoFin'){situacionEconomicaFinanciera=1;flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='analisitEconoFin'){fueraBalance=1;flagGuardado=false;sincronizado=0;idleTime=0}
	    
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
	   		activarmensajeAutograbar=0;
	   		inimsnAutoGuardado();
	   		
	   	}else if (vtipoGrabacion=="MANUAL"){
	   		activarmensajeAutograbar=1;
	   	}else{
	   		activarmensajeAutograbar=0;
	   	}
	   	

	   	//guardadoGeneral=1;
	   	sincrono=false;
		if(situacionFinanciera==1)			{$("#ssavesitFina").click();	if (vtipoGrabacion=="MANUAL"){situacionFinanciera=1;}	}
		if(situacionEconomica==1)			{$("#ssavesitEcono").click();		if (vtipoGrabacion=="MANUAL"){situacionEconomica=1;}}
		if(situacionEconomicaFinanciera==1)	{$("#ssavevalsitEconoFin").click();	if (vtipoGrabacion=="MANUAL"){situacionEconomicaFinanciera=1;}}
		if(fueraBalance==1)					{$("#ssaveanalisitEconoFin").click();if (vtipoGrabacion=="MANUAL"){fueraBalance=1;}}
		//setInterval("cambiarPagina()", 1000);
		if(vtipoGrabacion=="AUTO"){
		 sincronizado=1;
		}
		setTimeout(cerrarmsnAutoGuardado, 9000);
		cambiarPagina();
		closeLoading();
		return false;
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
		if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
		
		id_grupo = GenericAction.getObjectSession(Constantes.COD_GRUPO_SESSION).toString();%>
		
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
		
		<div >
			<s:form id="sintesisEconomico" name="sintesisEconomico" method="post" enctype="multipart/form-data" theme="simple">
				<input name="scrollX" id="scrollX" type="hidden"  />
				<input name="scrollY" id="scrollY" type="hidden"  />
				<input name="codigoArchivo" id="codigoArchivo" type="hidden"  />
				<input name="extension" id="extension" type="hidden"  />
				<input name="nombreArchivo" id="nombreArchivo" type="hidden"  />
				<input type="hidden" name ="tipo_empresa" value="<%=tipo_empresa%>"/>
				<input type="hidden" name ="id_grupo" value="<%=id_grupo%>"/>

				<table cellspacing="0"  class="ln_formatos" >
					<tr>				
				 		<td><s:file name="archivoExcel"
				 					id="sfileExcel" 
				 					label="Ruta Archivo" 
				 					size="70" onchange="check_extension(this.value,'ssavexls');"
				 					cssClass="btn"/>
				 		</td>
				 		<td align="left">
				 			
				 		<s:submit action="saveExcel" value="Grabar" id="ssavexls" name="ssavexls" cssClass="btn"/>
				 		</td>	
				 		<td colspan="2" align="right">
										<div id="idmsnAutoGuardado"></div>
						</td>	
					</tr>
				</table>	
				<%if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){ %>
				<div>
				<table>
					<tr>
						<td>
							Empresa del Grupo:
						</td>
						<td>
							<s:select id="codigoEmpresa"  name="codigoEmpresa" list="listaEmpresasGrupo" listKey="codigo" listValue="nombre" theme="simple" ></s:select>
						</td>
					</tr>
				</table>
				</div>
				<%} %>

				<table>
					<tr>
						<td>
							<img src="image/xls.png" alt="download" border="0">
							<a href="javascript:descargarExcel();">
									Descargar Plantilla Actualizada
							</a>
						</td>
					</tr>
				</table>
				
				<div  class="my_div">
				<table cellspacing="0"  class="ln_formatos" >
					<tr>				
				 		<td>
						<table class="ui-widget ui-widget-content">
						<thead>
							<tr class="ui-widget-header ">
								<th>Fecha Carga</th>			
								<th>Empresa</th>
								<th>Usuario</th>
								<th>Descarga</th>
								<th align="center">...</th>
							</tr>
						</thead>	
							<s:iterator value="listaSintesis" var="sintesiseconomico" status="userStatus">
								<tr>
									<td><s:date name="fechaCarga" format="dd/MM/yyyy hh:mm:ss" /></td>			
									<td><s:property value="nombreEmpresa" /></td>
									<td><s:property value="usuario" /></td>	
									<td align="center">
										<a href="javascript:descargarSintesisFinanciero('<s:property value="id" />','<s:property value="extension" />','<s:property value="nombreArchivo" />');">
										<img src="imagentabla/bbva.ExcelAzul24.png" alt="download" border="0">
										</a>
									</td>
									<td align="center">
										<a href="javascript:eliminar('<s:property value="id" />','<s:property value="extension" />','<s:property value="nombreArchivo" />');">
										<img src="imagentabla/bbva.EliminarAzul24.png" alt="download" border="0" onclick="return confirmDelete();"/>
										</a>
									</td>
								</tr>
							</s:iterator>
						</table>
						</td>
					</tr>
				</table>	
				</div>
				</s:form>
				
				
				<s:form id="sintesisEconomico2" theme="simple">
				<table cellspacing="0"  class="ln_formatos">
					<tr>
						<td>
							<label style="font-size: 10pt;font-weight: bold;">Comentarios Situaci&oacute;n Financiera</label>
						</td>	
						<td>
							<input type="button" value="Ver" id="bversitFina" class="btn"/>
						</td>	
						<td>
							<input type="button" value="Guardar" id="ssavesitFina" disabled="disabled" class="btn"/>
						</td>	
						<td>
							<input type="button" value="Limpiar" id="scleansitFina" disabled="disabled" class="btn"/>
						</td>	
						<td>
							<input type="button" value="Validar" id="sverificasitFina"  class="btn"/>
						</td>
					</tr>
				</table>
				<div id="divsitFina" style="display:none;"> 

						<FCK:editor instanceName="sitFina" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
				</div>
				
				<table  cellspacing="0" class="ln_formatos">
					<tr>
						<td>
							<label style="font-size: 10pt;font-weight: bold;">Comentarios Situaci&oacute;n Econ&oacute;mica</label>
						</td>	
						<td>
							<input type="button" id="bversitEcono"  value="Ver" class="btn"/>
						</td>	
						<td>
							<input type="button" value="Guardar" id="ssavesitEcono" disabled="disabled" class="btn"/>
						</td>	
						<td>
							<input type="button" value="Limpiar" id="scleansitEcono" disabled="disabled" class="btn"/>
						</td>
						<td>
							<input type="button" value="Validar" id="sverificasitEcono" class="btn"/>
						</td>	
					</tr>
				</table>
				<div id="divsitEcono" style="display:none;"> 

						<FCK:editor instanceName="sitEcono" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
				</div>
				
				<table cellspacing="0" class="ln_formatos">
					<tr>
						<td>
							<label style="font-size: 10pt;font-weight: bold;">Valoraci&oacute;n Situaci&oacute;n Econ&oacute;mica - Financiera</label>
						</td>	
						<td>
							<input type="button" id="bvervalsitEconoFin"  value="Ver"  class="btn"/>
						</td>	
						<td>
							<input type="button" value="Guardar" id="ssavevalsitEconoFin" disabled="disabled" class="btn"/>
						</td>	
						<td>
							<input type="button" value="Limpiar" id="scleanvalsitEconoFin" disabled="disabled"  class="btn"/>
						</td>	
						<td>
							<input type="button" value="Validar" id="sverificavalsitEconoFin"  class="btn"/>
						</td>
					</tr>
				</table>
				<div id="divvalsitEconoFin" style="display:none;"> 
<!--					<table  cellspacing="0">-->
<!--						<tr>-->
<!--							<td>-->
<!--								<textarea id="stextareavalsitEconoFin" class="wysiwyg" rows="5" cols="100"></textarea>-->
<!--							</td>-->
<!--						</tr>-->
<!--					</table>-->
						<FCK:editor instanceName="valsitEconoFin" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
				</div>
				
				<table  cellspacing="0" class="ln_formatos" >
					<tr>
						<td>
							<label style="font-size: 10pt;font-weight: bold; ">An&aacute;lisis Valoraci&oacute;n Posiciones Fuera del Balance y Riesgo Estructural</label>
						</td>	
						<td>
							<input type="button" id="bveranalisitEconoFin"  value="Ver" class="btn"/>
						</td>	
						<td>
							<input type="button" value="Guardar" id="ssaveanalisitEconoFin" disabled="disabled" class="btn"/>
						</td>	
						<td>
							<input type="button" value="Limpiar" id="scleananalisitEconoFin" disabled="disabled" class="btn"/>
						</td>	
						<td>
							<input type="button" value="Validar" id="sverificaanalisitEconoFin"  class="btn"/>
						</td>
					</tr>
				</table>
				<div id="divanalisitEconoFin" style="display:none;"> 
						<FCK:editor instanceName="analisitEconoFin" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
				</div>
				
				<div style="position:fixed !important; right:100px; top:400px; z-index:10 !important z-index:10 !important; border-radius: 15px;">
									
					<input type="button" value="Guadar" id="idgrabarGeneralSE" class="btn" Style="width: 100px; height: 40px;"/>
					<div id="idMenssageSave" style="display:none;"></div>
				</div>
				</s:form>
			</div>
		
	</div>
</div>

<script language="JavaScript">
var btnssavesitFina= document.getElementById("ssavesitFina");
var btnssavesitEcono= document.getElementById("ssavesitEcono");
var btnssavevalsitEconoFin= document.getElementById("ssavevalsitEconoFin");
var btnssaveanalisitEconoFin= document.getElementById("ssaveanalisitEconoFin");

var btnsverificaanalisitEconoFin= document.getElementById("sverificaanalisitEconoFin");
var btnsverificavalsitEconoFin= document.getElementById("sverificavalsitEconoFin");
var btnsverificasitEcono= document.getElementById("sverificasitEcono");
var btnsverificasitFina= document.getElementById("sverificasitFina");



if(0 < <%=activoBtnGeneral%>){ 		
	btnssavesitFina.style.display = 'none'; // No ocupa espacio   
	btnssavesitEcono.style.display = 'none';
	btnssavevalsitEconoFin.style.display = 'none';
	btnssaveanalisitEconoFin.style.display = 'none';
	
}else{	   
	btnssavesitFina.style.visibility  = 'visible'; // Se ve
	btnssavesitEcono.style.visibility  = 'visible'; 
	btnssavevalsitEconoFin.style.visibility  = 'visible'; 
	btnssaveanalisitEconoFin.style.visibility  = 'visible'; 
}

if(0 < <%=activoBtnValidar%>){ 	
	btnsverificaanalisitEconoFin.style.visibility  = 'visible'; // Se ve
	btnsverificavalsitEconoFin.style.visibility  = 'visible'; 
	btnsverificasitEcono.style.visibility  = 'visible'; 
	btnsverificasitFina.style.visibility  = 'visible'; 
	
}else{	   
	btnsverificaanalisitEconoFin.style.display = 'none'; // No ocupa espacio   
	btnsverificavalsitEconoFin.style.display = 'none';
	btnsverificasitEcono.style.display = 'none';
	btnsverificasitFina.style.display = 'none';
}

		 	
</script>
		
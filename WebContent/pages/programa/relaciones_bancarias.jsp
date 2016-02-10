<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://java.fckeditor.net" prefix="FCK" %>


<%@page import="pe.com.bbva.iipf.util.Constantes"%>
<%@page import="java.util.List"%>
<%@page import="pe.com.bbva.iipf.pf.model.Parametro"%>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>

<link rel="stylesheet" type="text/css"	href="/ProgramaFinanciero/css/table.css" />

<% 
	String max_size_comentcuotaFinanciera = GenericAction.getObjectParamtrosSession(Constantes.MAX_SIZE_TEXT_CUOTAFINANCIERA).toString(); 
String activoBtnValidar = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_BTN_VALIDAR).toString();
String activoValidarEditor = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_VALIDAR_EDITOR).toString();

 %>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:head />


  
<script language="JavaScript">




var dirurl='';
var vtextarea='';
var VALORMANUAL=1;
var lineasComentario=0;
var poolBancario=0;
var indiceTransaComentario=0;

var POOL_BANCARIO=1;
var LINEAS_COMENTARIO=2;
var INDICETRANSA_COMENTARIO=3;

	$(function() {
	
		$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$( "#dialog-form" ).dialog({
			autoOpen: false,
			height: 500,
			width: 950,
			modal: true,
			buttons: {
				"Aceptar": function() {						
						$( this ).dialog( "close" );						
				 		document.getElementById('idrefreshpool').click();
					
				},
			"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				
			}
		});
		
		$( "#dialog-formMR" ).dialog({
			autoOpen: false,
			height: 600,
			width: 710,
			modal: true,
			buttons: {
				"Aceptar": function() {						
						$( this ).dialog( "close" );					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				
			}
		});
	
		
	});

	function seccionEditada(){
		$("#flagEditado").val(true);
		sincronizado=0;
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

	function addlineas() {		
		document.forms[0].action = 'doaddLinea.do';	
		document.forms[0].submit();
	}

	function sumara(campos,para){
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
	
	function sumarIndicaTransa(campos1,campos2,para){
		var valor1 = 0;
		var valor2 = 0;
		var division=0;	
				
		cvalue1=($(campos1).val()).toString();		
		cvalue1=cvalue1.split(",").join("");				
		if(!isNaN(parseFloat(cvalue1)) && cvalue1.length!=0) {
			valor1 = parseFloat(cvalue1);
		}

		cvalue2=($(campos2).val()).toString();		
		cvalue2=cvalue2.split(",").join("");				
		if(!isNaN(parseFloat(cvalue2)) && cvalue2.length!=0) {
			valor2 = parseFloat(cvalue2);
		}	
					
		if (valor2>0){
			division=valor1/valor2;
		}	else{
			division=0;
		}

		var div1=0;
		div1=NumberFormat(division, '2', '.', ',');			
		div1.toString();		
		$(para).val(div1);
			
	}
	
	function addCalendar(){
	$('.cssidCalendarRB').each(function(){
		$(this).datepicker({ 
    							dateFormat: 'dd/mm/yy',
    							showOn: "button",
							    buttonImage: "images/calendariobbva.png",
								buttonImageOnly: true });
    	$(this).datepicker();
   	});
	}


	function iniciodialog() {		
		$( "#idpoolbancario" ).html('');		
	}

	function iniciodialogMR() {		
		$( "#idmodRentabilidad" ).html('');		
	}

	function editado(campo){
		if(campo==LINEAS_COMENTARIO){
			lineasComentario=1;sincronizado=0;
		}else if(campo==POOL_BANCARIO){
			poolBancario=1;sincronizado=0;
		}else if(campo==INDICETRANSA_COMENTARIO){
			indiceTransaComentario=1;sincronizado=0;
		}
		return false;
	}
	
	function noEditado(campo){
		if(campo==LINEAS_COMENTARIO){
			lineasComentario=0;
		}else if(campo==POOL_BANCARIO){
			poolBancario=0;
		}else if(campo==INDICETRANSA_COMENTARIO){
		   indiceTransaComentario=0;
		}
		
		if(	lineasComentario==0 &
				poolBancario==0 & 
				indiceTransaComentario==0){
			sincronizado=1;
		}
		return false;
	}
	
	function mostrarGuardandoRB(){
		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } }); 
	}
	
	function mostrarGuardapool(){
		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Pool Bancario. Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } }); 
	}
	function mostrarRefresh(){
		if ($("#idformrelbancaria").validationEngine('validate')) {	
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Actualizando. Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } }); 
		}
	}
	
   $(window).load(function() { setTimeout($.unblockUI, 1);});
	
   $(document).ready(function() {
	  	if($("#chkLineas").is(':checked')) {
            $("#divLineas").attr('style','display:');
        }
        if($("#chkPoolBancario").is(':checked')) {
            $("#divPoolBancario").attr('style','display:');
        }
        if($("#chkCuotaFinanciera").is(':checked')) {
            $("#divCuotaFinanciera").attr('style','display:');
        }
        if($("#chkModeloRentabilidad").is(':checked')) {
            $("#divModeloRentabilidad").attr('style','display:');
        }
        if($("#chkEfectividadCartera").is(':checked')) {
            $("#divEfectividadCartera").attr('style','display:');
        }
        if($("#chkIndiceTransa").is(':checked')) {
            $("#divIndiceTransa").attr('style','display:');
        }
        
        if($("#chkComex").is(':checked')) {
            $("#divComex").attr('style','display:');
        }        
        
        $("#chkLineas").click(function () {
			$("#divLineas").toggle("slow");
		});   
		
		$("#chkIndiceTransa").click(function () {
			$("#divIndiceTransa").toggle("slow");
		});  
		
		$("#chkComex").click(function () {
			$("#divComex").toggle("slow");
		});  
		
		$("#chkPoolBancario").click(function () {
			$("#divPoolBancario").toggle("slow");
		});  
		$("#chkCuotaFinanciera").click(function () {
			$("#divCuotaFinanciera").toggle("slow");
		}); 
		$("#chkModeloRentabilidad").click(function () {
			$("#divModeloRentabilidad").toggle("slow");
		}); 
		$("#chkEfectividadCartera").click(function () {
			$("#divEfectividadCartera").toggle("slow");
		}); 
         
	   sumarDecimal(".RBcol1","#RBtotal1");
	   sumarDecimal(".RBcol2","#RBtotal2");
	   sumarDecimal(".RBcol3","#RBtotal3");
	   sumarDecimal(".RBcol4","#RBtotal4");
	   sumarDecimal(".RBcol5","#RBtotal5");
	   sumarDecimal(".RBcol6","#RBtotal6");
	   sumarDecimal(".RBcol7","#RBtotal7");
	   sumarDecimal(".RBcol8","#RBtotal8");
	   sumarDecimal(".RBcol9","#RBtotal9");
	   sumarDecimal(".RBcol10","#RBtotal10");
	   
	   sumarIndicaTransa("#idvalorsaldo","#idvalorcaja","#idresultsaldo");
	   sumarIndicaTransa("#idflujotransa","#idventacosto","#idresultflujo");
   
	   	 //When page loads...
		 $(".tab_content").hide(); //Hide all content
		 $("#li5").addClass("active").show(); //Activate first tab
		 $("#tab1").show(); //Show first tab content

   		 $("#tacomentcuotaFinanciera").counter({
 			count: 'up',
 			goal: <%=max_size_comentcuotaFinanciera%>
 		 });
		 
		 $("#enviar").click( function (){                    
			 $.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } });
			 iniciodialog();
	   		 $('#idBancoSelect option').each(function(i) {
		   		  $(this).prop("selected", "selected");
		   		 });   
	   		 $('#idEmpresaSelect option').each(function(i) {
		   		  $(this).prop("selected", "selected");
		   		 }); 
	   		 
				//empresa
				 var vempresa = '';
	             $("#idEmpresaSelect option:selected").each(function(){
	            	 vempresa += $(this).val() + ','; 
	                 });
	             fine = vempresa.length - 1; // calculo cantidad de caracteres menos 1 para eliminar la coma final
	             vempresa = vempresa.substr( 0, fine ); // elimino la coma final     
	             
				


	   		 	//banco
	             var selected = '';
	             $("#idBancoSelect option:selected").each(function(){
	                 selected += $(this).val() + ','; 
	                 });
	             finb = selected.length - 1; // calculo cantidad de caracteres menos 1 para eliminar la coma final
	             selected = selected.substr( 0, finb ); // elimino la coma final     
	             		            
	        
	             var checkedm = '';             
	             if ($('#idcheckTipoDeudaTotal').is(':checked')) {		             
		             checkedm += '1' + ','; 
	             }
	             if ($('#idcheckTipoDeudaDirecta').is(':checked')) {		             
		             checkedm += '2' + ','; 
	             }
	             if ($('#idcheckTipoDeudaIndirecta').is(':checked')) {		             
		             checkedm += '3' + ','; 
	             }
	             fin2 = checkedm.length - 1; // calculo cantidad de caracteres menos 1 para eliminar la coma final
	             checkedm = checkedm.substr( 0, fin2 );
	             
		            
		   		$.post("PoolbancarioAction.do", 
			   			   {rightSideempresa:vempresa,rightSidebanco:selected,tipoDeuda:checkedm},
					   		function(data){			   						   		             
					   			setTimeout($.unblockUI, 1); 
					   			$('#idpoolbancario').html(data);
					   			resetTiempoSession();
		   				});   
		   	 $( "#dialog-form" ).dialog( "open" );    
		   	 //setTimeout($.unblockUI, 1);    
         });


		//MODELO RENTABILIDAD
		
		$("#verModeloR").click( function (){  
			//alert("Aki estoy");
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } });
			$("#idmodRentabilidadGlobal").hide();   
			$("#idcomenModRentabilidadBEC").hide();
			$("#srefreshModelBEC").prop("disabled","disabled");
			 iniciodialogMR();	 		          
	             var checkedm = '';
	             $('#idradiomodRent :radio:checked').each(function(){	            	 
	            	 checkedm += $(this).val() + ','; 
	                 });
	             fin2 = checkedm.length - 1; // calculo cantidad de caracteres menos 1 para eliminar la coma final
	             checkedm = checkedm.substr( 0, fin2 ); // elimino la coma final  
				//alert(checkedm);
		         if (checkedm=='1') {  
			        $("#idcomenModRentabilidadBEC").show();  
			   		$.post("modeloRentabilidadAction.do", 
				   			   {modeloRentabilidad:checkedm},
						   		function(data){			   						   		             
						   			setTimeout($.unblockUI, 1); 
						   			$('#idmodRentabilidad').html(data);	
						   			resetTiempoSession();
						   			$("#srefreshModelBEC").prop("disabled","");					   			
						   			
						   			
			   				});   
		         }
		         if (checkedm=='2') {
		         	 setTimeout($.unblockUI, 1); 			        
		        	 $("#idmodRentabilidadGlobal").show();
			         }
			     if (checkedm=='') {
			     //alert("entrog");
		         	 setTimeout($.unblockUI, 1);	        	 
			         }
			         
		        
   				
		   	 $( "#dialog-formMR" ).dialog( "open" );    
         });
       
		
		$("#verModeloRupdate").click( function (){  
			//alert("Aki estoy");
			$("#idmodRentabilidadGlobal").hide();   
			$("#idcomenModRentabilidadBEC").hide();
			 iniciodialogMR();	 		          
	             var checkedm = '';
	             $('#idradiomodRent :radio:checked').each(function(){	            	 
	            	 checkedm += $(this).val() + ','; 
	                 });
	             fin2 = checkedm.length - 1; // calculo cantidad de caracteres menos 1 para eliminar la coma final
	             checkedm = checkedm.substr( 0, fin2 ); // elimino la coma final  
				//alert(checkedm);
		         if (checkedm=='1') {  
			        $("#idcomenModRentabilidadBEC").show();  
			   		$.post("modeloRentabilidadAction.do", 
				   			   {modeloRentabilidad:checkedm},
						   		function(data){			   						   		             
						   			setTimeout($.unblockUI, 1); 
						   			$('#idmodRentabilidad').html(data);						   			
						   			resetTiempoSession();
						   			
			   				});   
		         }
		         if (checkedm=='2') {			        
		        	 $("#idmodRentabilidadGlobal").show();
			         }
		        
   				
		   	 $( "#dialog-formMR" ).dialog( "open" );    
         });

		 $("#idformrelbancaria").validationEngine();   		 
   		 $(".RBcol1").css('text-align', 'right');
   		 $(".RBcol2").css('text-align', 'right');
   		 $(".RBcol3").css('text-align', 'right');
   		 $(".RBcol4").css('text-align', 'right');

   		 $(".RBcol5").css('text-align', 'right');
   		 $(".RBcol6").css('text-align', 'right');
   		 $(".RBcol7").css('text-align', 'right');
   		 $(".RBcol8").css('text-align', 'right');
   		 $(".RBcol9").css('text-align', 'right');
   		 $(".RBcol10").css('text-align', 'right');
   		 
   		 $(".RBcol1").blur(function () { 
   			sumarDecimal(".RBcol1","#RBtotal1");
   			var pos=0;
   			$(".RBcol1").each(function(){
   				sumarDecimal("input[title='F'][name^='listLineasRelacionesBancarias\["+pos+"\]']",
             "input[title='TF'][name^='listLineasRelacionesBancarias\["+pos+"\]']"); 
             pos++;
   			});   						 
   		 } );
   		 $(".RBcol2").blur(function () { 
   			sumarDecimal(".RBcol2","#RBtotal2");
   			var pos=0;
   			$(".RBcol2").each(function(){
   				sumarDecimal("input[title='D'][name^='listLineasRelacionesBancarias\["+pos+"\]']",
             "input[title='TD'][name^='listLineasRelacionesBancarias\["+pos+"\]']"); 
             pos++;
   			});   		
   		 } );
   		 $(".RBcol3").blur(function () { 
   			sumarDecimal(".RBcol3","#RBtotal3");
   			var pos=0;
   			$(".RBcol3").each(function(){
   				sumarDecimal("input[title='F'][name^='listLineasRelacionesBancarias\["+pos+"\]']",
             "input[title='TF'][name^='listLineasRelacionesBancarias\["+pos+"\]']"); 
             pos++;
   			});   	
   		 } );
   		 $(".RBcol4").blur(function () { 
   			sumarDecimal(".RBcol4","#RBtotal4");
   			var pos=0;
   			$(".RBcol4").each(function(){
   				sumarDecimal("input[title='D'][name^='listLineasRelacionesBancarias\["+pos+"\]']",
             "input[title='TD'][name^='listLineasRelacionesBancarias\["+pos+"\]']"); 
             pos++;
   			});   	
   		 } );
   		 $(".RBcol5").blur(function () { 
   			sumarDecimal(".RBcol5","#RBtotal5");
   			var pos=0;
   			$(".RBcol5").each(function(){
   				sumarDecimal("input[title='F'][name^='listLineasRelacionesBancarias\["+pos+"\]']",
             "input[title='TF'][name^='listLineasRelacionesBancarias\["+pos+"\]']"); 
             pos++;
   			});   	
    	 } );
   		 $(".RBcol6").blur(function () { 
   			sumarDecimal(".RBcol6","#RBtotal6");
   			var pos=0;
   			$(".RBcol6").each(function(){
   				sumarDecimal("input[title='D'][name^='listLineasRelacionesBancarias\["+pos+"\]']",
             "input[title='TD'][name^='listLineasRelacionesBancarias\["+pos+"\]']"); 
             pos++;
   			});   	
    	 } );
   		 $(".RBcol7").blur(function () { 
   			sumarDecimal(".RBcol7","#RBtotal7");
   			var pos=0;
   			$(".RBcol7").each(function(){
   				sumarDecimal("input[title='F'][name^='listLineasRelacionesBancarias\["+pos+"\]']",
             "input[title='TF'][name^='listLineasRelacionesBancarias\["+pos+"\]']"); 
             pos++;
   			});   	
    	 } );
   		 $(".RBcol8").blur(function () { 
   			sumarDecimal(".RBcol8","#RBtotal8");
   			var pos=0;
   			$(".RBcol8").each(function(){
   				sumarDecimal("input[title='D'][name^='listLineasRelacionesBancarias\["+pos+"\]']",
             "input[title='TD'][name^='listLineasRelacionesBancarias\["+pos+"\]']"); 
             pos++;
   			});   	
    	 } );
   		 $(".RBcol9").blur(function () { 
   			sumarDecimal(".RBcol9","#RBtotal9");
   			
    	 } );
   		 $(".RBcol10").blur(function () { 
   			sumarDecimal(".RBcol10","#RBtotal10");
    		 } );
    		 
	
		$("#idvalorsaldo").blur(function () { 
   			sumarIndicaTransa("#idvalorsaldo","#idvalorcaja","#idresultsaldo");
    		 } );
    	$("#idvalorcaja").blur(function () { 
   			sumarIndicaTransa("#idvalorsaldo","#idvalorcaja","#idresultsaldo");
    		 } );
    		 
    	$("#idflujotransa").blur(function () { 
   			sumarIndicaTransa("#idflujotransa","#idventacosto","#idresultflujo");
    		 } );
    	$("#idventacosto").blur(function () { 
   			sumarIndicaTransa("#idflujotransa","#idventacosto","#idresultflujo");
    		 } );
    		 
    		 
   		 
   		$('#stextareacomenLineas').wysiwyg({			
			rmUnusedControls: true,			
			controls: {				
			bold: { visible : true },				
			html: { visible : true },				
			insertOrderedList: { visible : true },				
			removeFormat: { visible : true }			
			}		
		});
  	   $('#stextareacomenLineas').wysiwyg("addControl", 
  			   "UploadFile", {				
  			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
  			   tooltip: 'Upload',tags: ['Upload'],				
  			   exec:  function() { 		   		
  			   		vtextarea='stextareacomenLineas'; 
  			   		newWindowUploadFile(vtextarea);  }
  		       }
  		    );
		$("#stextareacomenClientGlob").wysiwyg("addControl", 
  			   "UploadFile", {				
  			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
  			   tooltip: 'Upload',tags: ['Upload'],				
  			   exec:  function() { 		   		
  			   		vtextarea='stextareacomenClientGlob'; 
  			   		newWindowUploadFile(vtextarea);  }
  		       }
  		    );
  		 $("#stextareacomenBEC").wysiwyg("addControl", 
  			   "UploadFile", {				
  			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
  			   tooltip: 'Upload',tags: ['Upload'],				
  			   exec:  function() { 		   		
  			   		vtextarea='stextareacomenBEC'; 
  			   		newWindowUploadFile(vtextarea);  }
  		       }
  		    );
  		 $("#stextareacomenPoolBanc").wysiwyg("addControl", 
  			   "UploadFile", {				
  			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
  			   tooltip: 'Upload',tags: ['Upload'],				
  			   exec:  function() { 		   		
  			   		vtextarea='stextareacomenPoolBanc'; 
  			   		newWindowUploadFile(vtextarea);  }
  		       }
  	     );
  		    
   		$("#bvercomenLineas").click(function () { 
   	   		
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divcomenLineas").attr("style","");
  			$("#ssavecomenLineas").prop("disabled","");
  			$("#scleancomenLineas").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "comenLineas" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareacomenLineas').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("comenLineas___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}   			     	
		   });
	   		//editado(LINEAS_COMENTARIO);
		});
		$("#ssavecomenLineas").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			var iframe = document.getElementById("comenLineas___Frame");
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

					//var dataGet="campoBlob=comenLineas&valorBlob="+valblob;
					var dataGet={campoBlob:'comenLineas',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,LINEAS_COMENTARIO);

			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=comenLineas&valorBlob="+valblob;
					var dataGet={campoBlob:'comenLineas',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,LINEAS_COMENTARIO);
			   		/*$.post("saveProgramaBlob.do", 
			   			   {campoBlob:"comenLineas", valorBlob:valblob},
					   		function(data){
					   			setTimeout($.unblockUI, 1); 
		   				});*/
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("COMENTARIO LINEAS"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}	
		});
		$("#scleancomenLineas").click(function () { 
			//$('#stextareacomenLineas').wysiwyg('setContent', '');
			var iframe = document.getElementById("comenLineas___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}   
		});
		
		$("#sverificacomenLineas").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("comenLineas___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		
		//ini MCG20140815
		$("#bvercomenIndiceTransa").click(function () { 
   	   		
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divcomenIndiceTransa").attr("style","");
  			$("#ssavecomenIndiceTransa").prop("disabled","");
  			$("#scleancomenIndiceTransa").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "comenIndiceTransa" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareacomenLineas').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("comenIndiceTransa___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}   			     	
		   });
	   		//editado(LINEAS_COMENTARIO);
		});
		$("#ssavecomenIndiceTransa").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			var iframe = document.getElementById("comenIndiceTransa___Frame");
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
					//var dataGet="campoBlob=comenLineas&valorBlob="+valblob;
					var dataGet={campoBlob:'comenIndiceTransa',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,LINEAS_COMENTARIO);
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=comenLineas&valorBlob="+valblob;
					var dataGet={campoBlob:'comenIndiceTransa',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,LINEAS_COMENTARIO);
			   		/*$.post("saveProgramaBlob.do", 
			   			   {campoBlob:"comenLineas", valorBlob:valblob},
					   		function(data){
					   			setTimeout($.unblockUI, 1); 
		   				});*/
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("COMENTARIO INDICE TRANSACCIONAL"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}	
			}
		});
		$("#scleancomenIndiceTransa").click(function () { 
			//$('#stextareacomenLineas').wysiwyg('setContent', '');
			var iframe = document.getElementById("comenIndiceTransa___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}   
		});
		
		$("#sverificacomenIndiceTransa").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("comenIndiceTransa___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		
		
		//fin MCG20140815
		
		
		//Modelo rentabilidad BEC
		$("#srefreshModelBEC").click( function (){  
			//alert("Aki estoy");
			$("#idmodRentabilidadGlobal").hide();   
			$("#idcomenModRentabilidadBEC").hide();
			$("#srefreshModelBEC").prop("disabled","disabled");
			 iniciodialogMR();	 		          
	             var checkedm = '';
	             $('#idradiomodRent :radio:checked').each(function(){	            	 
	            	 checkedm += $(this).val() + ','; 
	                 });
	             fin2 = checkedm.length - 1; // calculo cantidad de caracteres menos 1 para eliminar la coma final
	             checkedm = checkedm.substr( 0, fin2 ); // elimino la coma final  
				//alert(checkedm);
		         if (checkedm=='1') {  
			        $("#idcomenModRentabilidadBEC").show();  
			   		$.post("refreshModeloRentabilidadAction.do", 
				   			   {modeloRentabilidad:checkedm},
						   		function(data){			   						   		             
						   			setTimeout($.unblockUI, 1); 
						   			$('#idmodRentabilidad').html(data);	
						   			
						   			$("#srefreshModelBEC").prop("disabled","");
						   			resetTiempoSession();					   			
						   			
						   			
			   				});   
		         }
		         if (checkedm=='2') {			        
		        	 $("#idmodRentabilidadGlobal").show();
			         }
		        
   				
		   	 $( "#dialog-formMR" ).dialog( "open" );    
         });
		
		$("#bvercomenBEC").click(function () { 
   	   		
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divcomenBEC").attr("style","");
  			$("#ssavecomenBEC").prop("disabled","");
  			$("#scleancomenBEC").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "rentaModelBEC" },
		   		function(data){
		   		setTimeout($.unblockUI, 1); 
		   		//alert("Hola");
		   		//$('#stextareacomenBEC').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("comenBEC___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}  			     	
		   });
		});
		$("#ssavecomenBEC").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			var iframe = document.getElementById("comenBEC___Frame");
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

			   		$.post("saveProgramaBlob.do", 
			   			   {campoBlob:"rentaModelBEC", valorBlob:valblob},
					   		function(data){
					   			setTimeout($.unblockUI, 1); 
					   			resetTiempoSession();
		   				});
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){
			   		$.post("saveProgramaBlob.do", 
			   			   {campoBlob:"rentaModelBEC", valorBlob:valblob},
					   		function(data){
					   			setTimeout($.unblockUI, 1); 
					   			resetTiempoSession();
		   				});
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("COMENTARIO BEC"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}
		});
		$("#scleancomenBEC").click(function () { 
			//$('#stextareacomenBEC').wysiwyg('setContent', '');
			var iframe = document.getElementById("comenBEC___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}  
		}); 	
		
		$("#sverificacomenBEC").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("comenBEC___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});

		//Modelo rentabilidad Cliente Global
   		$("#bvercomenClientGlob").click(function () { 
   	   		
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divcomenClientGlob").attr("style","");
  			$("#ssavecomenClientGlob").prop("disabled","");
  			$("#scleancomenClientGlob").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "rentaModelGlobal" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareacomenClientGlob').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("comenClientGlob___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;		
					resetTiempoSession();			
				}    			     	
		   });
		});
		$("#ssavecomenClientGlob").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });
			var iframe = document.getElementById("comenClientGlob___Frame");
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
			   		$.post("saveProgramaBlob.do", 
			   			   {campoBlob:"rentaModelGlobal", valorBlob:valblob},
					   		function(data){
					   			setTimeout($.unblockUI, 1); 
					   			resetTiempoSession();
		   				});
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				} 
				if (accionGraba==1 || accionGraba==2){
			   		$.post("saveProgramaBlob.do", 
			   			   {campoBlob:"rentaModelGlobal", valorBlob:valblob},
					   		function(data){
					   			setTimeout($.unblockUI, 1); 
					   			resetTiempoSession();
		   				});
	
				}else if (accionGraba==0){
						alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("COMENTARIO CLIENTE GLOBAL"));
						setTimeout($.unblockUI, 1);
					}else if (accionGraba==3){
						setTimeout($.unblockUI, 1);
				}	
			}
		});
		$("#scleancomenClientGlob").click(function () { 
			//$('#stextareacomenClientGlob').wysiwyg('setContent', '');
			var iframe = document.getElementById("comenClientGlob___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}   
		}); 
		
		$("#sverificacomenClientGlob").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("comenClientGlob___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		
		$("#bvercomenPoolBanc").click(function () { 
   	   		
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divcomenPoolBanc").attr("style","");
  			$("#ssavecomenPoolBanc").prop("disabled","");
  			$("#scleancomenPoolBanc").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "comenPoolBanc" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareacomenPoolBanc').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("comenPoolBanc___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}  	  			     	
		   });
	   		//editado(POOL_BANCARIO);
		});
		$("#ssavecomenPoolBanc").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			var iframe = document.getElementById("comenPoolBanc___Frame");
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
					//var dataGet="campoBlob=comenPoolBanc&valorBlob="+valblob;
					var dataGet={campoBlob:'comenPoolBanc',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,POOL_BANCARIO);
			}else{
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
				if (accionGraba==1 || accionGraba==2){
					//var dataGet="campoBlob=comenPoolBanc&valorBlob="+valblob;
					var dataGet={campoBlob:'comenPoolBanc',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,POOL_BANCARIO);
			   		/*$.post("saveProgramaBlob.do", 
			   			   {campoBlob:"comenPoolBanc", valorBlob:valblob},
					   		function(data){
					   			setTimeout($.unblockUI, 1); 
		   				});*/
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("COMENTARIO POOL BANCARIO"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}
		});
		$("#scleancomenPoolBanc").click(function () { 
			//$('#stextareacomenPoolBanc').wysiwyg('setContent', '');
			var iframe = document.getElementById("comenPoolBanc___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}   
		});	
		
		$("#sverificacomenPoolBanc").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("comenPoolBanc___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		
		addCalendar();
// 		$("#actualizarEfec").click(function () { 
// 			$.post("refreshEfectividades.do",
// 	   		function(data){
// 	   			setTimeout($.unblockUI, 1); 
	   			//alert(data);
// 			});
// 		});	
		
	});     
   function validarFormulario(obj){
	   SaveScrollXY(obj);
	  	var validar=true;
	   $.each($('.lineaSubgrupo'), function(index, obj) {
		   //alert(obj.value);
		   if(obj.value==""){
			   validar=false;
		   }
		});
	   if(!validar){
		   alert("Ingrese la descripción a la columna Por Filial/SubGrupo");
	   }
	   return validar;
   }

	function FCKeditor_OnComplete(editorInstance)
       {
               if (document.all) {        // If Internet Explorer.
                     editorInstance.EditorDocument.attachEvent("onkeydown", function(event){desactivarFlagEditor(editorInstance);} ) ;
               } else {                // If Gecko.
                     editorInstance.EditorDocument.addEventListener( 'keypress', function(event){desactivarFlagEditor(editorInstance);}, true ) ;
           }         
       }
	
	function desactivarFlagEditor(editorInstance)
	{//comenClientGlobComentario..comenBECComentario
		if(editorInstance.Name=='comenLineas'){lineasComentario=1; flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='comenPoolBanc'){poolBancario=1; flagGuardado=false;sincronizado=0;idleTime=0}
		if(editorInstance.Name=='comenIndiceTransa'){indiceTransaComentario=1; flagGuardado=false;sincronizado=0;idleTime=0}
	    
	}
	
	function cerrarmsnAutoGuardado(){
	  $("#idmsnAutoGuardado" ).html("");
	}
	function inimsnAutoGuardado(){
		$("#idmsnAutoGuardado" ).html("<font size='3' color='blue'>Autoguardado en proceso....</font>");
	}
   
   function  guardarFormulariosActualizadosPagina(uri,vtipoGrabacion){	   
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
	   if(lineasComentario==1)			{$("#ssavecomenLineas").click();	}
	   if(poolBancario==1)				{$("#ssavecomenPoolBanc").click();	}
	   if(indiceTransaComentario==1)			{$("#ssavecomenIndiceTransa").click();	}
	  // setInterval("cambiarPagina()", 1000);
	  if(edicionCampos==1){	  
	  	try {
			 $.ajax({
	          	async:false,
	          	type: "POST",
	          	url:document.getElementById("idformrelbancaria").action,
	          	data:$("#idformrelbancaria").serialize(),	   	           
	       		success: function (data) {
	       			//guardadoGeneral=0;
	       			//sincronizado=1;
	       			//ocultarGuardando();
	       			//cambiarPagina();
	       			setTimeout(cerrarmsnAutoGuardado, 6000);
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
<s:form	action="doUpdateBDRelacionesBancarias" id="idformrelbancaria" theme="simple" onsubmit="return validarFormulario(this);">
	<%--<input type="text" name="camposEditados" 	id="camposEditados" 	value="${camposEditados}"/>--%>	
	<s:hidden name="flagEditado" id="flagEditado"/>
	
	
	
	<input name="scrollX" id="scrollX" type="hidden"  />
	<input name="scrollY" id="scrollY" type="hidden"  />
	<table width="100%">
		<tr>
			<td class="bk_tabs">

			<table class="ln_formatos" cellspacing="0" width="100%">
				<tr>
					<td><s:submit value="Guardar"
						cssClass="btn"
						theme="simple"></s:submit>
					</td>											
					<td colspan="2" align="right">
						<div id="idmsnAutoGuardado"></div>
					</td>
				</tr>
			</table>


			<br />

			<table class="ln_formatos" width="100%" cellspacing="0">
				<tr>
					<td><label style="font-size: 10pt; font-weight: bold;">Clasificaci&oacute;n
					Financiera:</label></td>
				</tr>
				<tr>
					<td>
					<div class="my_div">
					<table class="ui-widget ui-widget-content" cellspacing="0" border="1" width="100%">
						<tr >
							<th class="ui-widget-header" align="center">CALIFICACION SBS</th>
							<td align="center">NORMAL(0)</td>
								<td><s:textfield size="10"  name="programa.porcentajeNormalSF" readonly="true" cssStyle="text-align: right;" />%</td>
							<td align="center">CPP(1)</td>
								<td><s:textfield size="10"  name="programa.porcentajeProblemaPotencialSF" readonly="true" cssStyle="text-align: right;" />%</td>
							<td align="center">DEFICIENTE(2)</td>
								<td><s:textfield size="10"  name="programa.porcentajeDeficienteSF" readonly="true" cssStyle="text-align: right;" />%</td>
							<td align="center">DUDOSO(3)</td>
								<td><s:textfield size="10"  name="programa.porcentajeDudosoSF" readonly="true" cssStyle="text-align: right;" />%</td>
							<td align="center">PERDIDA(4)</td>
							<td><s:textfield size="10" name="programa.porcentajePerdidaSF" readonly="true" cssStyle="text-align: right;" />%</td>
						</tr>										
					</table>
					</div>
					</td>
				</tr>
				<tr>
					<td><s:submit onclick="mostrarRefresh();" value="Actualizar" action="refreshClasificacionFinanciera"
						cssClass="btn"
						theme="simple"></s:submit></td>
					
				</tr>
			</table>

			<br />

			<table class="ln_formatos" width="550px" cellspacing="0">
				<tr>
					<td>
					<div class="my_div">
					<table class="ui-widget ui-widget-content" width="100%">						
							<tr class="ui-widget-header">
								<th style="width:150px">CALIFICACION BANCO</th>
								<td width="250px">
								<s:hidden name="programa.calificacionBanco"/>
								<s:textfield size="50"  name="desCalifBanco" readonly="true" /></td>								
								<td>
								<s:submit onclick="mostrarRefresh();" value="Actualizar" action="refrecarClasificacion"	cssClass="btn"	theme="simple"/>
								
								</td>
							</tr>						
					</table>
					</div>
					</td>
				</tr>
			</table>

			<br />

			<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagLineas"   id="chkLineas"/>Lineas</h3>
			<div id="divLineas" style="display: none" class="">
			
			<div id="divLineasocultar" style="display: none" >
				<table class="ln_formatos" width="100%" cellspacing="0">
					<tr>
						<td><label style="font-size: 10pt; font-weight: bold;">Lineas:</label>
						
						</td>
	
					</tr>
	
					<tr>
						<td>
	
						<table width="100%" class="ui-widget ui-widget-content">
							<thead>
								<tr>
									<th colspan="12" align="left">
									<s:submit id="idaddLineaRB" theme="simple" value="+" action="doaddLinea"
										cssClass="btn" />
									<s:submit id="iddelLineaRB" theme="simple" action="dodelLinea" value="-"
										onclick="return confirmDelete();" disabled="disabled"
										cssClass="btn"></s:submit>
									<s:submit id="idsaveLineaRB" theme="simple" action="dosaveLineasRelacionesBancarias" value="Guardar Lista" onclick="return existeElementos('.RBcol1')" disabled="disabled"  cssClass="btn"></s:submit>
									&nbsp;&nbsp;&nbsp;
									CIFRAS EN <s:select id="idselTipoMilesRB"   name = "tipoMiles" list="listaTipoMiles" listKey="id" listValue="descripcion" label="" /> USD
									</th>				
								</tr>
								<tr class="ui-widget-header ">
									<th rowspan="2">&nbsp;</th>
									<th rowspan="2">Por Filial/SubGrupo</th>
									<th colspan="2">Riesgo Comercial</th>
									<th colspan="2">Riesgo Puro</th>
									<th colspan="2">Riesgo de Firma</th>
									<th colspan="2">Tesoreria</th>
									<th colspan="2">Total</th>
								</tr>
								<tr class="ui-widget-header ">
	
									<th width="15">LTE FORM</th>
									<th width="15">DPTO</th>
	
									<th width="15">LTE FORM</th>
									<th width="15">DPTO</th>
	
									<th width="15">LTE FORM</th>
									<th width="15">DPTO</th>
	
									<th width="15">LTE FORM</th>
									<th width="15">DPTO</th>
	
									<th width="15">LTE FORM</th>
									<th width="15">DPTO</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator var="p" value="listLineasRelacionesBancarias"
									status="rowstatus">
									<tr>
										<td>
											<input type="checkbox" onclick="seccionEditada()" value="<s:property  value="%{#rowstatus.index}"/>" name="chkLineas" ></input>
											<s:hidden
												name="listLineasRelacionesBancarias[%{#rowstatus.index}].id"></s:hidden>
											<s:hidden
												name="listLineasRelacionesBancarias[%{#rowstatus.index}].programa.Id"></s:hidden>
										</td>
										<td><s:textfield size="50" label="linea"
											name="listLineasRelacionesBancarias[%{#rowstatus.index}].linea"
											value="%{linea}" cssClass="lineaSubgrupo" maxlength="100" onblur="seccionEditada();seccionEditada()" onkeypress="desactivarFlag();" readonly="true"/></td>
										<td><s:textfield size="9" label="sgrclteform"
											name="listLineasRelacionesBancarias[%{#rowstatus.index}].sgrclteform"
											value="%{sgrclteform}" theme="simple" cssClass="RBcol1"
											onkeypress="EvaluateText('%f', this);"
											onblur="this.value = NumberFormat(this.value, '2', '.', ',');seccionEditada()"
											maxlength="12" title="F" readonly="true"/></td>
										<td><s:textfield size="9" label="sgrcdpto"
											name="listLineasRelacionesBancarias[%{#rowstatus.index}].sgrcdpto"
											value="%{sgrcdpto}" theme="simple" cssClass="RBcol2"
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
											maxlength="12"  title="D" readonly="true"/></td>
										<td><s:textfield size="9" label="sgrplteform"
											name="listLineasRelacionesBancarias[%{#rowstatus.index}].sgrplteform"
											value="%{sgrplteform}" theme="simple" cssClass="RBcol3"
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',');seccionEditada()"
											maxlength="12"  title="F" readonly="true"/></td>
										<td><s:textfield size="9" label="sgrpdpto"
											name="listLineasRelacionesBancarias[%{#rowstatus.index}].sgrpdpto"
											value="%{sgrpdpto}" theme="simple" cssClass="RBcol4"
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',');seccionEditada()" 
											maxlength="12"  title="D" readonly="true"/></td>
										<td><s:textfield size="9" label="sgrflteform"
											name="listLineasRelacionesBancarias[%{#rowstatus.index}].sgrflteform"
											value="%{sgrflteform}" theme="simple" cssClass="RBcol5"
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',');seccionEditada()" 
											maxlength="12"  title="F" readonly="true"/></td>
										<td><s:textfield size="9" label="sgrfdpto"
											name="listLineasRelacionesBancarias[%{#rowstatus.index}].sgrfdpto"
											value="%{sgrfdpto}" theme="simple" cssClass="RBcol6"
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',');seccionEditada()"
											maxlength="12" title="D" readonly="true"/></td>
										<td><s:textfield size="9" label="sgtesolteform"
											name="listLineasRelacionesBancarias[%{#rowstatus.index}].sgtesolteform"
											value="%{sgtesolteform}" theme="simple" cssClass="RBcol7"
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',');seccionEditada()" 
											maxlength="12"  title="F" readonly="true" /></td>
										<td><s:textfield size="9" label="sgtesodpto"
											name="listLineasRelacionesBancarias[%{#rowstatus.index}].sgtesodpto"
											value="%{sgtesodpto}" theme="simple" cssClass="RBcol8"
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',');seccionEditada()" 
											maxlength="12" title="D" readonly="true"/></td>
										<td><s:textfield size="9" label="sgtotallteform"
											name="listLineasRelacionesBancarias[%{#rowstatus.index}].sgtotallteform"
											value="%{sgtotallteform}" theme="simple" cssClass="RBcol9"
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')" 
											maxlength="12"  title="TF" readonly="true"/></td>
										<td><s:textfield size="9" label="sgtotaldpto"
											name="listLineasRelacionesBancarias[%{#rowstatus.index}].sgtotaldpto"
											value="%{sgtotaldpto}" theme="simple" cssClass="RBcol10"
											onkeypress="EvaluateText('%f', this);" 
											onblur="this.value = NumberFormat(this.value, '2', '.', ',');" 
											maxlength="12"  title="TD" readonly="true" /> <!--                <s:property value="%{id}" />-->
										</td>
									</tr>
								</s:iterator>
								<tr class="ui-widget-header ">
									<td>&nbsp;</td>
									<td>TOTAL</td>
									<td><s:textfield size="9" id="RBtotal1" readonly="true"
										cssStyle="text-align: right;" /></td>
									<td><s:textfield size="9" id="RBtotal2" readonly="true"
										cssStyle="text-align: right;" /></td>
									<td><s:textfield size="9" id="RBtotal3" readonly="true"
										cssStyle="text-align: right;" /></td>
									<td><s:textfield size="9" id="RBtotal4" readonly="true"
										cssStyle="text-align: right;" /></td>
	
									<td><s:textfield size="9" id="RBtotal5" readonly="true"
										cssStyle="text-align: right;" /></td>
									<td><s:textfield size="9" id="RBtotal6" readonly="true"
										cssStyle="text-align: right;" /></td>
	
									<td><s:textfield size="9" id="RBtotal7" readonly="true"
										cssStyle="text-align: right;" /></td>
									<td><s:textfield size="9" id="RBtotal8" readonly="true"
										cssStyle="text-align: right;" /></td>
	
									<td><s:textfield size="9" id="RBtotal9" readonly="true"
										cssStyle="text-align: right;" /></td>
									<td><s:textfield size="9" id="RBtotal10" readonly="true"
										cssStyle="text-align: right;" /></td>
								</tr>
							</tbody>
						</table>
	
	
						</td>
					</tr>
	
				</table>
				
				</div>
				
				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td><label style="font-size: 10pt; font-weight: bold;">Detalle de líneas y operaciones aprobadas:</label>
						</td>
						<td><input type="button" id="bvercomenLineas" value="Ver"
							class="btn" /> 
							
							<input
							type="button" value="Guardar" id="ssavecomenLineas"
							disabled="disabled"
							class="btn"/> 
							
							<input
							type="button" value="Limpiar" id="scleancomenLineas"
							disabled="disabled"
							class="btn" />
							<input
							type="button" value="Validar" id="sverificacomenLineas"							
							class="btn" /></td>
					</tr>
				</table>
				<div id="divcomenLineas" style="display: none;">

						<FCK:editor instanceName="comenLineas" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
				</div>
			</div>

			

			<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagCuotaFinanciera"   id="chkCuotaFinanciera"/>Cuota Financiera</h3>
			<div id="divCuotaFinanciera" style="display: none" class="">
	
				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td><label style="font-size: 10pt; font-weight: bold;">Periodo</label></td>
						<td><s:textfield size="5" name="programa.periodoArchivo"
							id="fechaPeriodoCuotafinan" readonly="true"/></td>
					</tr>
					<tr>
						<td><label style="font-size: 10pt; font-weight: bold;">Cuota Financiera</label></td>
						<td><s:textfield size="5" name="programa.cuotaFinanciera"
							id="tfcuotafinan" cssClass="validate[required]"						
							onkeypress="EvaluateText('%f', this);" 
							onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
							maxlength="6" readonly="true"/>%</td>
					</tr>
	
				</table>
				
				<table class="ln_formatos" cellspacing="0" width="100%">	
					<tr>
						<td>
							<s:textarea name="programa.comentcuotaFinanciera"
										id="tacomentcuotaFinanciera"
										rows="3"
										cols="100"
										theme="simple" onkeypress="desactivarFlag();" >
							</s:textarea>
						</td>	
					</tr>
				</table>
			</div>


			<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagPoolBancario"   id="chkPoolBancario"/>Pool Bancario</h3>
			<div id="divPoolBancario" style="display: none" class="">

				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td><label style="font-size: 10pt; font-weight: bold;">POOL
						BANCARIO:</label></td>
					</tr>
				</table>
	
				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td>Seleccione Empresas para informacion individual Adicional:</td>
					</tr>
					<tr>
						<td>
						<table border="0">
							<tr>
								<td width="150" rowspan="2"><label for="leftTitle">Empresa
								por Seleccionar</label><br />
								<s:select name="selectedItemsIzquierdaEmpresa"
									list="listaEmpresa" listKey="codEmpresa" listValue="nomEmpresa"
									headerKey="999999" headerValue="----- Seleccionar Empresa -----"
									size="15" id="idEmpresa" multiple="true"
									style="height:200px;width:200">
								</s:select> 
								<br />
								<br />
								<input type="hidden" id="__multiselect_idEmpresa"
									name="__multiselect_listaEmpresa" value="" /></td>
								<td valign="middle" align="center"><br />
								<br />
								<s:submit value=">>> Agregar Todos" action="doaDerechaTodosEmpresa"
									cssClass="btn" onclick="seccionEditada()" /><br /> 
								<br />
								<s:submit value=">> Agregar" action="doaDerechaEmpresa"
									cssClass="btn"  onclick="seccionEditada()" /><br />
								<br />
								<s:submit value="<< Quitar" action="doaIzquierdaEmpresa"
									cssClass="btn"  onclick="seccionEditada()" /><br />
								<br />
								<s:submit value="<<< Quitar Todos" action="doaIzquierdaTodosEmpresa"
									cssClass="btn"  onclick="seccionEditada()" /><br />
								<br />
	
								<br />
								<br />
								</td>
								<td width="150" rowspan="2"><label for="rightTitle">Empresa
								Seleccionada</label><br />
								<s:select name="selectedItemsDerechaEmpresa" size="15"
									list="listaEmpresaSelect" listKey="codEmpresa"
									listValue="nomEmpresa" headerKey="999999"
									headerValue="----- Seleccionar Empresa -----" multiple="true"
									id="idEmpresaSelect" style="height:200px;width:200">
								</s:select> <input type="hidden" id="__multiselect_idEmpresaSelect"
									name="__multiselect_listaEmpresaSelect" value="" /></td>
							</tr>
						</table>
	
						</td>
					</tr>
				</table>
	
						
				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td>Seleccione Tipo de Deuda:</td>
					</tr>
					<tr>
						<td>
						<s:checkbox id="idcheckTipoDeudaTotal" theme="simple" name="checkTipoDeudaTotal" fieldValue="true" label="Deuda Total" onclick="seccionEditada()" /> Deuda Total<br />
						<s:checkbox id="idcheckTipoDeudaDirecta" theme="simple" name="checkTipoDeudaDirecta" fieldValue="true" label="Deuda Directa" onclick="seccionEditada()" />Deuda Directa<br />
						<s:checkbox id="idcheckTipoDeudaIndirecta" theme="simple" name="checkTipoDeudaIndirecta" fieldValue="true" label="Deuda Inddirecta" onclick="seccionEditada()" />Deuda Indirecta<br />															
						</td>
					</tr>				
				</table>
				
				
	
				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td>Seleccione Bancos:</td>
					</tr>
					<tr>
						<td>
						<table border="0">
							<tr>
								<td width="150" rowspan="2"><label for="leftTitle">Banco
								por Seleccionar</label><br />
								<s:select name="selectedItemsIzquierdabanco" list="listaBanco"
									listKey="codBanco" listValue="nombreBanco" headerKey="999"
									headerValue="-----   Seleccionar Banco   -----" size="15"
									id="idBanco" multiple="true" style="height:200px;width:200" >
								</s:select> <input type="hidden" id="__multiselect_idBanco"
									name="__multiselect_listaBanco" value="" /></td>
								<td valign="middle" align="center">
								<br />
								<br />
								<br />
								<br />
								<s:submit
									value=">>> Agregar Todos" action="doaDerechaTodosBanco"
									cssClass="btn" onclick="seccionEditada()"  /><br />
								<br />
								<s:submit value=">> Agregar" action="doaDerechaBanco"
									cssClass="btn" onclick="seccionEditada()"  /><br />
								<br />
								<s:submit value="<< Quitar" action="doaIzquierdaBanco"
									cssClass="btn" onclick="seccionEditada()"  /><br />
								<br />
								<s:submit value="<<< Quitar Todos" action="doaIzquierdaTodosBanco"
									cssClass="btn"  onclick="seccionEditada()" /><br />
								<br />
	
								<br />
								<br />
								</td>
								<td width="150" rowspan="2"><label for="rightTitle">Banco
								Seleccionada</label><br />
								<s:select name="selectedItemsDerechabanco" size="15"
									list="listaBancoSelect" listKey="codBanco"
									listValue="nombreBanco" headerKey="999"
									headerValue="-----   Seleccionar Banco   -----" multiple="true"
									id="idBancoSelect" style="height:200px;width:200">
								</s:select> <input type="hidden" id="__multiselect_idBancoSelect"
									name="__multiselect_listaBancoSelect" value="" /></td>
							</tr>
						</table>
	
						</td>
					</tr>
				</table>

				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td><input type="button" id="enviar" value="Vista Previa"
							class="btn" /></td>
											
						<td><s:submit value="Actualizar Pool Bancario" action="refreshPoolBancario"
						id="idrefreshpool"	
						onclick="mostrarGuardapool();" 					
						cssClass="btn"
						theme="simple"></s:submit>
						</td>
				
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</table>
	
				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td><label style="font-size: 10pt; font-weight: bold;">Comentario:</label>
						</td>
						<td><input type="button" id="bvercomenPoolBanc" value="Ver"
							class="btn" onclick="seccionEditada()"  /> 
							
							<input
							type="button" value="Guardar" id="ssavecomenPoolBanc"
							disabled="disabled"
							class="btn"/> 
							
							<input
							type="button" value="Limpiar" id="scleancomenPoolBanc"
							disabled="disabled"
							class="btn" />
							<input
							type="button" value="Validar" id="sverificacomenPoolBanc"
							class="btn" /></td>
					</tr>
				</table>
				<div id="divcomenPoolBanc" style="display: none;">
						<FCK:editor instanceName="comenPoolBanc" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
				</div>
	
				<div id="dialog-form" title="Pool Bancario">
				<div id="idpoolbancario" name="pool"></div>
				</div>
	
				<br />
			</div>
			
			
			<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagModeloRentabilidad"   id="chkModeloRentabilidad"/>Modelo Rentabilidad</h3>
			<div id="divModeloRentabilidad" style="display: none" class="">
			
				<div id="idradiomodRent">
					<table class="ln_formatos" cellspacing="0">
						<tr>
							<td><label style="font-size: 10pt; font-weight: bold;">MODELO
							DE RENTABILIDAD:</label></td>
						</tr>
						<tr>
							<td>Seleccionar Modelo de Rentabilidad:</td>
						</tr>
						<tr>
							<td><s:radio name="modeloRentabilidad"
												list="#{'1':'Banca Empresa(BEC)','2':'Cliente Global'}"
												label="Tipo de Modelo de Rentabilidad" 
												cssClass="validate[required] radio"
												theme="simple" onclick="seccionEditada()" />						
							</td>
						</tr>
					</table>
				</div>
					
					
					
		
				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td><input type="button" id="verModeloR" value="Vista Previa"
							class="btn" /></td>
					</tr>
				</table>
					
	
				<div id="dialog-formMR" title="MODELO DE RENTABILIDAD">
					<div id="idmodRentabilidadPopup">
						<div id="idmodRentabilidad" name="modRentabilidad"></div>
						</br>						
						<div id="idcomenModRentabilidadBEC" style="display: none;">
							<table class="ui-widget ui-widget-content" cellspacing="0">
								<tr>
									<td><label>Comentario:</label></td>
									<td><input type="button" id="bvercomenBEC" value="Ver" class="btn"/>
									</td>
									<td><input type="button" value="Guardar"
										id="ssavecomenBEC" disabled="disabled" class="btn"/></td>
									<td><input type="button" value="Limpiar"
										id="scleancomenBEC" disabled="disabled" class="btn"/></td>
										<td><input type="button" value="Validar"
										id="sverificacomenBEC"  class="btn"/></td>
									<td><input type="button" onclick="mostrarRefresh();" value="Actualizar Rentabilidad"
										id="srefreshModelBEC" class="btn"/></td>
								</tr>
							</table>
							<div id="divcomenBEC" style="display: none;">
									<FCK:editor instanceName="comenBEC" height="300px">
										<jsp:attribute name="value">&nbsp;
										</jsp:attribute>									
										
									</FCK:editor>
							</div>
						</div>
					</div>
					<div id="idmodRentabilidadGlobal" name="modRentabilidadGlobal">
						<table class="ui-widget ui-widget-content" cellspacing="0">
							<tr>
								<td><label>Comentario:</label></td>
								<td><input type="button" id="bvercomenClientGlob" value="Ver" class="btn"/>
								</td>
								<td><input type="button" value="Guardar"
									id="ssavecomenClientGlob" disabled="disabled" class="btn"/></td>
								<td><input type="button" value="Limpiar"
									id="scleancomenClientGlob" disabled="disabled" class="btn"/></td>
									<td><input type="button" value="Validar"
									id="sverificacomenClientGlob"  class="btn"/></td>
							</tr>
						</table>
						<div id="divcomenClientGlob" style="display: none;">
							<FCK:editor instanceName="comenClientGlob" height="300px">
								<jsp:attribute name="value">&nbsp;
								</jsp:attribute>									
								
							</FCK:editor>
						</div>
					</div>
				</div>
			</div>

<!--INI MCG20141030-->			

			<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagComex"   id="chkComex"/>Exportaciones e Importaciones</h3>
			<div id="divComex" style="display: none" class="">
				<table class="ln_formatos" width="80%" cellspacing="0" border="0">
					<tr>
					<td>
					<s:submit onclick="mostrarRefresh();" value="Actualizar" action="actualizarComex"
						cssClass="btn"
						theme="simple"/>
					</td>
					</tr>
					<tr>
						<td>
						<div class="my_div">
						<table border="1" class="ui-widget ui-widget-content" width="60%">
						
											<tr class="ui-widget-header">
												<th colspan="3" align="center">
													&nbsp;
												</th>												
												
												<th align="center">
													PERIODO
												</th>
												<th align="center">
													<s:property  value="periodoComex"/>
												</th>
											</tr>
						
											<tr class="ui-widget-header">
												<th  colspan="2" align="center">
													&nbsp;
												</th>												
												<th align="center" style="width: 120px;">
													CANTIDAD
												</th>
												<th align="center" style="width: 120px;">
													IMPORTE ACUMULADO
												</th>
												<th align="center" style="width: 120px;">
													COMISIONES ACUMULADAS
												</th>
											</tr>
	
							<s:iterator var="pi" value="listaComexImportacion" id="ComexImportacion" status="rowstatus">
									  <s:if test="#rowstatus.first == true">									  			
											<tr>
											<td rowspan='<s:property value="listaComexImportacion.size"/>' align="center" style="font-weight: bold;">
													IMPORTACIONES
											</td>
											<td align="left">
											   <s:property   value="descripcion"></s:property>
											   
											</td>
											<td align="right">
											   <s:property value="cantidad" ></s:property>
											</td>
											<td align="right">
											   <s:property  value="importeAcumulado" ></s:property>
											</td>
											<td align="right">
											   <s:property  value="comisionesAcumuladas" ></s:property>
											</td>
																					
										</tr>

									  </s:if>
									  <s:else>	  
									
										<tr>
											<td align="left">
											   <s:property   value="descripcion"></s:property>
											   
											</td>
											<td align="right">
											   <s:property value="cantidad" ></s:property>
											</td>
											<td align="right">
											   <s:property  value="importeAcumulado" ></s:property>
											</td>
											<td align="right">
											   <s:property  value="comisionesAcumuladas" ></s:property>
											</td>
																					
										</tr>	
									</s:else>							
							</s:iterator>
										<tr>
												<td colspan="5" align="center">
													&nbsp;
												</td>			
										</tr>

							<s:iterator var="pe" value="listaComexExportacion" id="ComexExportacion" status="rowstatus">
										<s:if test="#rowstatus.first == true">									  
											<tr >
												<td rowspan='<s:property value="listaComexImportacion.size"/>' style="font-weight: bold;" align="center">
													EXPORTACIONES
												</td>
												<td align="left">
												   <s:property   value="descripcion"></s:property>
												</td>
												<td align="right">
												   <s:property value="cantidad" ></s:property>
												</td>
												<td align="right">
												   <s:property  value="importeAcumulado" ></s:property>
												</td>
												<td align="right">
												   <s:property  value="comisionesAcumuladas" ></s:property>
												</td>
											</tr>
									  </s:if>
									  <s:else>										
											<tr>
												<td align="left">
												   <s:property   value="descripcion"></s:property>
												</td>
												<td align="right">
												   <s:property value="cantidad" ></s:property>
												</td>
												<td align="right">
												   <s:property  value="importeAcumulado" ></s:property>
												</td>
												<td align="right">
												   <s:property  value="comisionesAcumuladas" ></s:property>
												</td>
																						
											</tr>
										</s:else>								
							</s:iterator>
						</table>
						</div>
						</td>						
					</tr>											

				</table>
				<table>
					<tr>
					<td style="width:200px">
					</td>
					<td>
					<div class="my_div">
						<table style="width:450px" class="ui-widget ui-widget-content" border="1">							
							<tr>
								
								<th  colspan="3">Ratios de Reciprocidad(%)</th>
								<th>&nbsp;</th>
							</tr>								
							<tr>
								
								<td colspan="3">Importaciones</td>
								<td align="center" valign="middle">
									<s:textfield size="20"  
									name="ratioReprocidadImp" 
									readonly="true"																		
									cssStyle="text-align: right;" />
								</td>									
							</tr>
							<tr>								
								<td colspan="3">Exportaciones</td>
								<td align="center" valign="middle">
									<s:textfield size="20"  
									name="ratioReprocidadExp" 
									readonly="true"																		
									cssStyle="text-align: right;" />
								</td>									
							</tr>
						</table>
						</div>					
					
					</td>						
					</tr>
				</table>
				
			</div>

<!--FIN MCG20141030-->

<!--INI MCG20140815-->			

			<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagIndiceTransa"   id="chkIndiceTransa"/>Indice Transaccional</h3>
			<div id="divIndiceTransa" style="display: none" class="">
				<table class="ln_formatos" width="100%" cellspacing="0" border="0">
					<tr>
						<td><label style="font-size: 10pt; font-weight: bold;">Indice Transaccional:</label></td>	
					</tr>	
					<tr>
						<td>	
						<table width="75%" class="ui-widget ui-widget-content" border="0">
							<thead>
								<tr class="ui-widget-header ">
									<th style="height:30px" colspan="3">&nbsp;</th>
									<th style="height:30px">Fecha</th>
								</tr>

							</thead>
							<tbody>	
									<tr>
										<td colspan="3">&nbsp;</td>
										<td align="center" valign="middle">
											<s:textfield size="20"  
											name="programa.fechaIndiceTransa" 
											maxlength="10"
											cssClass="cssidCalendarRB" 
											onblur="javascript:valFecha(this);"
											cssStyle="text-align: right;" />
										</td>									
									</tr>	
									<tr>
										
										<td align="center" valign="middle">Saldo Medio Recursos Gestionados</td>
									
										<td align="center" valign="middle">
											<s:textfield size="20"  name="programa.saldoMedioRecGest" 
											onkeypress="EvaluateText('%f', this);"
											onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
											id="idvalorsaldo"
											cssStyle="text-align:right"
											maxlength="20"/>
										</td>	
										<td align="center" valign="middle" rowspan="3">&nbsp;&nbsp;&nbsp;=&nbsp;&nbsp;&nbsp;</td>
										<td  align="center" valign="middle" rowspan="3">
											<s:textfield size="20"  name="programa.resultSaldo"	id="idresultsaldo"						
											cssStyle="text-align:right"
											readonly="true"										
											maxlength="20"/>
										</td>									
									</tr>	
									<tr >
										<td style="height:2px" align="center" valign="middle" BGCOLOR="gray"></td>
										<td style="height:2px" align="center" valign="middle" BGCOLOR="gray"></td>
									</tr>	
									<tr>
										
										<td align="center" valign="middle">Caja (*)</td>
									
										<td align="center" valign="middle">
										<s:textfield size="20"  name="programa.caja" 
										onkeypress="EvaluateText('%f', this);"
										id="idvalorcaja"
										onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
										cssStyle="text-align:right"
										maxlength="20"/>
										</td>	
													
									</tr>	
									<tr>
									<td colspan="4">&nbsp;</td>
									</tr>
									<tr>
										
										<td align="center" valign="middle">Flujos Transaccionales</td>
									
										<td align="center" valign="middle">
										<s:textfield size="20"  name="programa.flujoTransaccional" 										 
										onkeypress="EvaluateText('%f', this);"
										onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
										id="idflujotransa"
										cssStyle="text-align:right"
										maxlength="20"/>
										</td>	
										<td align="center" valign="middle" rowspan="3">&nbsp;&nbsp;&nbsp;=&nbsp;&nbsp;&nbsp;</td>
										<td align="center" valign="middle" rowspan="3">
											<s:textfield size="20"  
											name="programa.resultFlujo"  
											id="idresultflujo" 
											readonly="true"
											cssStyle="text-align: right;" />
										</td>									
									</tr>
									<tr >
										<td style="height:2px" align="center" valign="middle" BGCOLOR="gray"></td>
										<td style="height:2px" align="center" valign="middle" BGCOLOR="gray"></td>
									</tr>		
									<tr>										
										<td align="center" valign="middle">Ventas + Costo de Venta (*)</td>
									
										<td align="center" valign="middle">
										<s:textfield size="20"  name="programa.ventaCostoVenta"  
										onkeypress="EvaluateText('%f', this);"
										id="idventacosto"
										onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
										cssStyle="text-align:right"
										maxlength="20"/>
										</td>	
								
									</tr>			
							</tbody>
							<tfoot>
								<tr ><td colspan="2"><br/></td></tr>
								<tr ><td colspan="2"><br/></td></tr>
								<tr ><td colspan="2" >(*) Dato de último ejercicio</td></tr>
							</tfoot>
						</table>	
						</td>
					</tr>
	
				</table>
				<br />
				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td><label style="font-size: 10pt; font-weight: bold;">Comentario Indice Transaccional:</label>
						</td>
						<td><input type="button" id="bvercomenIndiceTransa" value="Ver"
							class="btn" /> 
							
							<input
							type="button" value="Guardar" id="ssavecomenIndiceTransa"
							disabled="disabled"
							class="btn"/> 
							
							<input
							type="button" value="Limpiar" id="scleancomenIndiceTransa"
							disabled="disabled"
							class="btn" />
							<input
							type="button" value="Validar" id="sverificacomenIndiceTransa"							
							class="btn" /></td>
					</tr>
				</table>
				
				<div id="divcomenIndiceTransa" style="display: none;">

						<FCK:editor instanceName="comenIndiceTransa" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
				</div>
			</div>

<!--FIN MCG20140815-->




			<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagEfectividadCartera"   id="chkEfectividadCartera"/>Efectividad de la Cartera</h3>
			<div id="divEfectividadCartera" style="display: none" class="">

				<table class="ln_formatos" width="450px" cellspacing="0">
					<tr>
						<td><label style="font-size: 10pt; font-weight: bold;">EFECTIVIDAD DE CARTERA:</label></td>
					</tr>
					<tr>
						<td>
						<s:submit onclick="mostrarRefresh();" value="Actualizar" action="refreshEfectividades"
						cssClass="btn"
						theme="simple"/>
						
<!-- 						<input type="button" id="actualizarEfec" value="Actualizar" -->
<!-- 							class="btn" /> -->
							
							</td>
					</tr>
					<tr>
						<td>
						<div class="my_div">
						<table class="ui-widget ui-widget-content" cellspacing="0"
							width="100%">
							<tr class="ui-widget-header">
								<th>EFECTIVIDAD</th>
								<th>PORCENTAJE</th>
							</tr>	
							<tr>
								<td>EFECTIVIDAD PROMEDIO ULTIMO 6 MESES SOLES:</td>
								<td><s:textfield size="5"  name="programa.efectividadProm6sol" readonly="true" cssStyle="text-align: right;" />%</td>
							</tr>	
							<tr>
								<td>EFECTIVIDAD PROMEDIO ULTIMO 6 MESES DOLARES:</td>
								<td><s:textfield size="5"  name="programa.efectividadProm6dol" readonly="true" cssStyle="text-align: right;" />%</td>
							</tr>
							<tr>
								<td>PROTESTOS PROMEDIO ULTIMO 6 MESES SOLES:</td>
								<td><s:textfield size="5"  name="programa.protestoProm6sol" readonly="true" cssStyle="text-align: right;" />%</td>
							</tr>
							<tr>
								<td>PROTESTOS PROMEDIO ULTIMO 6 MESES DOLARES:</td>
								<td><s:textfield size="5"  name="programa.protestoProm6dol" readonly="true" cssStyle="text-align: right;" />%</td>
							</tr>
							<tr>
								<td>EFECTIVIDAD ULTIMO A&Ntilde;O SOLES:</td>
								<td><s:textfield size="5" name="programa.efectividadUltmaniosol"  cssStyle="text-align: right;" onkeypress="return acceptNum(event);" onblur="seccionEditada()"  maxlength="3"/>%</td>
							</tr>	
							<tr>
								<td>EFECTIVIDAD ULTIMO A&Ntilde;O DOLARES:</td>
								<td><s:textfield size="5" name="programa.efectividadUltmaniodol"  cssStyle="text-align: right;" onkeypress="return acceptNum(event);" onblur="seccionEditada()" maxlength="3"/>%</td>
							</tr>									
	
						</table>
						</div>
						</td>
					</tr>
				</table>

			</div>






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
	
	<script language="JavaScript">
		document.getElementById("idaddLineaRB").disabled = true;
		document.getElementById("iddelLineaRB").disabled = true;
		document.getElementById("idsaveLineaRB").disabled = true;	
		
		var btnsverificacomenLineas= document.getElementById("sverificacomenLineas");
		var btnsverificacomenIndiceTransa= document.getElementById("sverificacomenIndiceTransa");
		var btnsverificacomenBEC= document.getElementById("sverificacomenBEC");
		var btnsverificacomenClientGlob= document.getElementById("sverificacomenClientGlob");
		var btnsverificacomenPoolBanc= document.getElementById("sverificacomenPoolBanc");
			
		
		
		if(0 < <%=activoBtnValidar%>){ 	
			btnsverificacomenLineas.style.visibility  = 'visible'; // Se ve
			btnsverificacomenIndiceTransa.style.visibility  = 'visible';
			btnsverificacomenBEC.style.visibility  = 'visible';
			btnsverificacomenClientGlob.style.visibility  = 'visible';
			btnsverificacomenPoolBanc.style.visibility  = 'visible';
			
		}else{	   
			btnsverificacomenLineas.style.display = 'none'; // No ocupa espacio
			btnsverificacomenIndiceTransa.style.display = 'none'; 
			btnsverificacomenBEC.style.display = 'none'; 
			btnsverificacomenClientGlob.style.display = 'none'; 
			btnsverificacomenPoolBanc.style.display = 'none'; 
			
		}

	</script>
</s:form>
</div>
</div>
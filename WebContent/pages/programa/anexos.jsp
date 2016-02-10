<%@taglib prefix="s" uri="/struts-tags" %>  
<%@taglib uri="http://java.fckeditor.net" prefix="FCK" %>


<%@page import="pe.com.stefanini.core.action.GenericAction"%>
<%@page import="pe.com.bbva.iipf.util.Constantes"%>
<%
	String x = request.getAttribute("scrollX")==null?"ningun valor":request.getAttribute("scrollX").toString();
	String y = request.getAttribute("scrollY")==null?"ningun valor":request.getAttribute("scrollY").toString();
	String max_size_coment_garantiaAnexo= GenericAction.getObjectParamtrosSession(Constantes.MAX_SIZE_TEXT_COMENT_GARANTIAANEXO).toString();
	String flagActivo= GenericAction.getObjectSession(Constantes.COD_FLAG_ACTIVO_ANEXO)==null?"0":GenericAction.getObjectSession(Constantes.COD_FLAG_ACTIVO_ANEXO).toString();
	String activoBtnValidar = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_BTN_VALIDAR).toString();
	String activoValidarEditor = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_VALIDAR_EDITOR).toString();

	
%>
<script language="JavaScript">

var comenLibreAnexos=0;
var VALORMANUAL=1;

var COMEN_LIBRE_ANEXOS=1;

	function editado(campo){
		if(campo==COMEN_LIBRE_ANEXOS){
			comenLibreAnexos=1;sincronizado=0;
		}
		return false;
	}
	//campoLibreAnexos
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
		if(editorInstance.Name=='campoLibreAnexos'){comenLibreAnexos=1; flagGuardado=false;sincronizado=0;idleTime=0;}
	    
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
	   	sincrono=false;
		if(comenLibreAnexos==1)				{		$("#ssavecampoLibreAnexos").click(); 	}
		
		if(edicionCampos==1){
			try {
				$.ajax({
		          	async:false,
		          	type: "POST",
		          	url:document.getElementById("formAnexos").action,
		          	data:$("#formAnexos").serialize(),
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

	
	function noEditado(campo){
		if(campo==COMEN_LIBRE_ANEXOS){
			comenLibreAnexos=0;
		}
		if(comenLibreAnexos==0){
				sincronizado=1;
		}
		return false;
	}
	
	function ocultarGuardando(){
		//if(guardadoGeneral==0){
			setTimeout($.unblockUI, 1);
		//}
	}
	
	function descargarArchivo(cod,ext, nom){		
		document.forms["formAnexos"].action='downloadFileAnexo.do';
		document.forms["formAnexos"].codigoArchivo.value =cod; 
		document.forms["formAnexos"].extension.value=ext;
		document.forms["formAnexos"].nombreArchivo.value=nom;
		document.forms["formAnexos"].submit();	
		resetTiempoSession();		
	}
	
		
	function eliminar(id,ext,nomarch){
		document.forms["formAnexos"].action='eliminarArchiAnexos.do';
		document.forms["formAnexos"].codigoArchivo.value =id; 
		document.forms["formAnexos"].extension.value =ext;
		document.forms["formAnexos"].nombreArchivo.value =nomarch;
		document.forms["formAnexos"].submit();	
	}
	
	function eliminarFila(pos){
		document.forms["formAnexos"].action='deleteFila.do';
		document.forms["formAnexos"].posAnexo.value =pos; 
		document.forms["formAnexos"].submit();	
	}
	function limpiarAnexo(){
		document.forms["formAnexos"].action='limpiarAnexo.do';		
		document.forms["formAnexos"].submit();	
	}
	function actualizarsaldo(){
		document.forms["formAnexos"].action='recalcularSaldo.do';		
		document.forms["formAnexos"].submit();	
	}
	
	function insertarImagen(){
		var vtextarea='campoLibreAnexos'; 
		newWindowUploadFile(vtextarea);
	}
	function validar(obj){		
		if($("#selTipoFila").val()=='3'){
			if($("#selOperaciones").val()==''){
				alert('Ingrese la descripción de la operación');
				$("#selOperaciones").focus();
				return false;
			}
		}
		if($("#selTipoFila").val()=='4'){
			if($("#selSubLimite").val()==''){
				alert('Ingrese la descripción del sublimite');
				$("#selSubLimite").focus();
				return false;
			}
		}
		else if($("#selTipoFila").val()=='2'){
			if($("#idRating").val()==''){
				alert('Ingrese el Rating');
				$("#idRating").focus();
				return false;
			}
		}
		
		if($("#selTipoFila").val()=='7'){
		
			if($("#selAccionista").val()==0){
				alert('Seleccione el Accionista');
				$("#selAccionista").focus();
				return false;
			}
		}
		
		return SaveScrollXY(obj);
	}
	
	
	
	
	
	function consultarSaldo(pos){
		if(pos != null){
			$( "#dialog-modal-detalle-saldo" ).dialog({
										height: 500,
										width: 600,
										modal: true
									});
		
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
		   			  overlayCSS: { backgroundColor: '#0174DF' } });	
	   		$("#posAnexoModalsaldo").val(pos);
	   		$("#editAnexosaldo").val('1'); 
	   		$("#cklinea").prop('checked', false);  
	   		$("#idTabla").empty();
	   	    $.post("loadDetalleSaldo.do", { posAnexo:pos },
			   		function(data){
			   		setTimeout($.unblockUI, 1); 
			   		//alert(data);
			   					   		
			   		$("#idTabla").html(data);
			   		$("#idTipoFila").val("0");			   		
			   		$("thead tr").attr("class","ui-widget-header");
			   		resetTiempoSession();
			});
		

		}else{	
		   alert("Posicion de columna invalida");
		}	
	}
	
	function consultarSaldoSublimite(pos){
		if(pos != null){
			$( "#dialog-modal-detalle-saldo" ).dialog({
										height: 500,
										width: 600,
										modal: true
									});
		
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
		   			  overlayCSS: { backgroundColor: '#0174DF' } });	
	   		$("#posAnexoModalsaldo").val(pos);
	   		$("#editAnexosaldo").val('1');  
	   		$("#cklinea").prop('checked', false); 
	   		$("#idTabla").empty();
	   	    $.post("loadDetalleSaldoSublimite.do", { posAnexo:pos },
			   		function(data){
			   		setTimeout($.unblockUI, 1); 
			   		//alert(data);
			   					   		
			   		$("#idTabla").html(data);
			   		$("#idTipoFila").val("0");			   		
			   		$("thead tr").attr("class","ui-widget-header");
			   		resetTiempoSession();
			});
		

		}else{	
		   alert("Posicion de columna invalida");
		}	
	}
	
	
	function contarPosicion(){
		var indice=0;
		 $('[name=chkanexo]').each(function()
			        {
			               	var seleccionado1=$(this);
				            if( seleccionado1.prop('checked') )
				            {  
					                //alert(indice);									 
									return false;
 
							}
							indice++;//paso a incrementar el indice en 1
			        });
		 //alert(indice);
		return indice;
	}
	function consultarSaldoAgregarFila(){
		
		//var pos1=contarPosicion();
		var pos=$("#posAnexoModal").val();
		var tipofila=$("#selTipoFila").val();
		//alert(tipofila);
		
			$( "#dialog-modal-detalle-saldo" ).dialog({
										height: 500,
										width: 600,
										modal: true
									});
		
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
		   			  overlayCSS: { backgroundColor: '#0174DF' } });	
	   		$("#posAnexoModalsaldo").val(pos);
	   		$("#editAnexosaldo").val('1'); 	   		 
	   		$("#cklinea").prop('checked', false);
	   		$("#idTabla").empty();
	   		if (tipofila==4){
	   		//alert("entro 4");
		   	    $.post("loadDetalleSaldoSublimite.do", { posAnexo:pos },
				   		function(data){
				   		setTimeout($.unblockUI, 1);
				   		$("#idTabla").html(data);
				   		$("#idTipoFila").val("1");			   		
				   		$("thead tr").attr("class","ui-widget-header");
				   		resetTiempoSession();
				   		
				});
			}else{
				$.post("loadDetalleSaldo.do", { posAnexo:pos },
				   		function(data){
				   		setTimeout($.unblockUI, 1);
				   		$("#idTabla").html(data);
				   		$("#idTipoFila").val("1");			   		
				   		$("thead tr").attr("class","ui-widget-header");
				   		resetTiempoSession();
				});
			
			}
	
	}

	function asignarSeleccionado(){		
      var selectedItems= new Array();
      var indice=0;
     	
	      if($("#idTipoFila").val()==1){
		      var montoTotal=0;
		      var nroContratos="";
		     
		      $('[name=contratocheck]').each(function()
		        {
		    	  	var seleccionado=$(this);
		            if( seleccionado.prop('checked') )
		            {                           
		                selectedItems[indice] = $(this).val();//El indice inicial sera 0                    
						indice++; //paso a incrementar el indice en 1
						nroContratos+=seleccionado.parents("TR").children("TD").next().next().html()+",";
						montoTotal+=parseFloat(seleccionado.parents("TR").children("TD").next().next().next().html());
						
		            }
		        });
		      	montoTotal=roundNumber(montoTotal,2);
		      	if($("#selTipoFila").val()==5){      	
			      	//$("#frmfila2_listaColumnasFormulario_4__valor").val(montoTotal);
			      	//$("#frmfila2_listaColumnasFormulario_3__valor").val(montoTotal);
			      	
		      		$("input[name^='columnLteAutorizado']").val(montoTotal);							
					$("input[name^='columnLteForm']").val(montoTotal);						
						
		      	}    
		      	
		      	//$("#frmfila2_listaColumnasFormulario_5__valor").val(montoTotal);
		      	//$("#frmfila2_listaColumnasFormulario_5__contratos").val(nroContratos.substring(0,nroContratos.length-1));
		      	
		      	$("input[name^='columnRgoActual']").val(montoTotal);
		      	$("input[name^='columnContrato']").val(nroContratos.substring(0,nroContratos.length-1));
		      	
		      	
		      	$("#dialog-modal-detalle-saldo").dialog("close");
		      	$("#idaddcontratos").val(selectedItems);
		        
		      return false;  
	      }else{	    
	      	  
	    	  $('[name=contratocheck]').each(function()
	    		        {
	    		            if( $(this).prop('checked') )
	    		            {                           
	    		                selectedItems[indice] = $(this).val();//El indice inicial sera 0                    
	    						indice++; //paso a incrementar el indice en 1
	    		            }
	    		        });
	    		        //alert(selectedItems);
	    		        $("#idaddcontratos").val(selectedItems);
	    		        return true;
	      }

	}
	function obtenerRating(){	
		var codempresa= $("#selEmpresas").val();
		//alert("entero obtener rating:"+codempresa);
			
		//$("#idmsnbureau" ).html("<font size='3' color='blue'>Obteniendo Bureau....</font>");
		
 		$.post("obtenerRatingAnexo.do", { codempresaRating: codempresa },
	   		function(data){				   		 
		   		//var jsonDataText = jQuery.parseJSON(data);
		   		//$("#idmsnbureau" ).html("");
		   		var jsonDataText = eval("(" + data + ")");
		   			
		   		var rating=jsonDataText.rating;	
		   		var fecharating=jsonDataText.fecharating;
		   		var bureau=jsonDataText.bureau;
		   		if (rating!=""){				   		
		   			$("#idRating").val(rating);
		   		}
		   		if (fecharating!=""){				   		
		   			$("#idRatingFecha").val(fecharating); 
		   		}
		   		if (bureau!=""){				   		
		   			$("#idBureau").val(bureau); 
		   		}
		   		resetTiempoSession();	   			     	
	   		}
	   	);	
	}
	
	
	
	function obtenerBureauWS(){	
		var codempresa= $("#selEmpresas").val();					
		$("#idmsnbureau" ).html("<font size='3' color='blue'>Actualizando Buro....</font>");
		
 		$.post("obtenerBureauAnexo.do", { codempresaRating: codempresa },
	   		function(data){				   		 
		   		//var jsonDataText = jQuery.parseJSON(data);
		   		$("#idmsnbureau" ).html("");
		   		var jsonDataText = eval("(" + data + ")");	
		   		var bureau=jsonDataText.bureau;
		   		$("#idBureau").val(bureau); 		   		
		   		resetTiempoSession();	   			     	
	   		}
	   	);	
	}
	function obtenerBuroAccionistaWS(){	
		var ndocumento= $("#selAccionista").val();
						
		$("#idmsnburoAccionista" ).html("<font size='3' color='blue'>Actualizando Buro...</font>");
		
 		$.post("obtenerBuroAccionistaAnexo.do", { codempresaAccionista: ndocumento },
	   		function(data){				   		 
		   		//var jsonDataText = jQuery.parseJSON(data);
		   		$("#idmsnburoAccionista" ).html("");
		   		var jsonDataText = eval("(" + data + ")");	
		   		var bureau=jsonDataText.bureau;
		   		$("#idBuroAccionista").val(bureau); 		   		
		   		resetTiempoSession();	   			     	
	   		}
	   	);	
	}	
	
	
	 function ocultarFila(numFila) {                
                var form = document.form;
                fila = document.getElementById('tbRemsaldo').getElementsByTagName('tr')[numFila];
                //if(form.cklinea.checked == true) {  
                if($("#cklinea").is(':checked')){    
                    fila.style.display = 'none';
                } else {                   
                    fila.style.display = '';
                }          
     }
     
     function filtroCredito(campos){
		var flag = 0;
		var fila=2;
		$(campos).each(function() {			
			flag = $(this).val();
			if (flag=="0"){
				ocultarFila(fila)
			}
			fila=fila+1
		});
		
		
	}
	function toBr(string){
	  var dtd = document.doctype ? (document.doctype.publicId || "") : document.getElementsByTagName("!")[0].text;
	  var br = dtd.search(/xhtml/i) > -1 ? '<br />' : '<br>';	
	  return string.replace(/\>/g,'&gt;').replace(/\</g,'&lt;').replace(/\r\n|\n|\r/g, br);
	}
	
	

	function ocultarColumna(num,ver) {
	  dis= ver ? '' : 'none';
	  fila=document.getElementById('tablaAnexo').getElementsByTagName('tr');
	  for(i=0;i<fila.length;i++)
	    fila[i].getElementsByTagName('td')[num].style.display=dis;
	}
	
    function roundNumber(num, dec) {
        var result = Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
        return result;
      }
	
	function confirmDeleteFila()   
	{  
	     return confirm('¿Se encuentra seguro de eliminar la fila?');  
	} 
	
	function confirmLimpiarAnexo()   
	{  
	     return confirm('¿Se encuentra seguro de Limpiar todos los detalle de la Posición?');  
	} 
	
	function confirmDeleteColumna()   
	{  
	     return confirm('¿Se encuentra seguro de eliminar la columna?');  
	} 
	(function($){
        $(
            function(){
                $('input:text').setMask();
            }
        );
    })(jQuery);
	
	$(window).load(function() { setTimeout($.unblockUI, 1);});
	
    $(document).ready(function() {
	   	 $(".tab_content").hide(); //Hide all content
		 $("#li9").addClass("active").show(); //Activate first tab
		 $("#tab1").show(); //Show first tab content
		  
		  $("#idtxtObservacion").counter({
			count: 'up',
			goal: '1800'
		 });
	 
		 $("#btnAgregarFila").click(function(){
		 	var pos =  $("input[name=chkanexo]:checked").val() ;
		 	//alert(pos);
		 	$("#posAnexoModal").val(pos);
			$("#editAnexo").val('0');
			
			$.post("nuevaFila.do", 
			   		function(data){
				   		var jsonDataText = eval("(" + data + ")");
				   						   		
				   		$("#idlblColumna1").html(jsonDataText.cabcolumna1); 
						$("#idlblColumna2").html(jsonDataText.cabcolumna2);
						$("#idlblColumna3").html(jsonDataText.cabcolumna3);
						$("#idlblColumna4").html(jsonDataText.cabcolumna4);
						$("#idlblColumna5").html(jsonDataText.cabcolumna5);
						$("#idlblColumna6").html(jsonDataText.cabcolumna6);
						$("#idlblColumna7").html(jsonDataText.cabcolumna7);
						$("#idlblColumna8").html(jsonDataText.cabcolumna8);
						$("#idlblColumna9").html(jsonDataText.cabcolumna9);
						$("#idlblColumna10").html(jsonDataText.cabcolumna10);
				   					   		
				   					   		
				   		if (jsonDataText.actCol1=="0"){
				   		$("#idColumna1Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol2=="0"){
				   		$("#idColumna2Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol3=="0"){
				   		$("#idColumna3Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol4=="0"){
				   		$("#idColumna4Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol5=="0"){
				   		$("#idColumna5Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol6=="0"){
				   		$("#idColumna6Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol7=="0"){
				   		$("#idColumna7Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol8=="0"){
				   		$("#idColumna8Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol9=="0"){
				   		$("#idColumna9Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol10=="0"){
				   		$("#idColumna10Edit").attr("style","display:none");
				   		}	
						resetTiempoSession();
			   			     	
			   		}
			   	);
			
		 	$( "#dialog-modal" ).dialog({
								height: 550,
								width: 700,
								modal: true
							});
			$("#selTipoFila").val('1');
			$("#selTipoFila").trigger('change');
			//obtenerRating();
			//$("#selEmpresas").trigger('change');
			
			$(".input_form").val('');
		 });
		 
		 
		 $(".tareacoment").each(function() {
		        var self = $(this);
		        self.counter({
				count: 'up',
				goal: '2000'
		 	});
	     });
	     
	     $(".tareacomentnota").each(function() {
		        var self = $(this);
		        self.counter({
				count: 'up',
				goal: '2000'
		 	});
	     });
		 
		 
		 
		 $("#btnEliminarFila").click(function(){
		 	
		   		 var pos =  $("input[name=chkanexo]:checked").val() ;
		   		 if(pos != null){
			   		  if(confirmDeleteFila()){
			   		 	eliminarFila(pos);
			   		 	}
		   		 }else{
		   		 	alert("Seleccione una fila");
		   		 }
	   		
		 });
		 
		 $("#btnLimpiarAnexo").click(function(){		
	   		  if(confirmLimpiarAnexo()){
	   		 	limpiarAnexo();
	   		 	}   		
		 });
		 
		 $("#btnRecalcular").click(function(){			   		
			   		 actualizarsaldo();
		 });
		 
		 $("#btnEditarFila").click(function(){

		 	var pos =  $("input[name=chkanexo]:checked").val() ;
		 	//alert(pos);
		 	if(pos != null){
			 	$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
	  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
	  				  
		   		$.post("editFila.do", { posAnexo: pos },
			   		function(data){
			   		
				   		setTimeout($.unblockUI, 1);
				   		 
				   		//var jsonDataText = jQuery.parseJSON(data);
				   		
				   		var jsonDataText = eval("(" + data + ")");
				   		
				   		$("#selTipoFila").val(jsonDataText.tipoFila);
				   		$("#posAnexoModal").val(jsonDataText.posAnexo);
				   		$("#editAnexo").val('1');
				   		
				   		if(jsonDataText.tipoFila=='1'){
				   			$("#selBanco").val(jsonDataText.desFila);
				   		}else if(jsonDataText.tipoFila=='2'){
				   		//alert("editboton" +jsonDataText.desFila);
				   			$("#selEmpresas").val(jsonDataText.desFila);
				   			//obtenerRating();
				   			//$("#selEmpresas").trigger('change');
				   		}else if(jsonDataText.tipoFila=='7'){				   		
				   			$("#selAccionista").val(jsonDataText.desFila);				   			
				   		}else if(jsonDataText.tipoFila=='3'){
				   			$("#selOperaciones").val(jsonDataText.desFila);
				   		}else if(jsonDataText.tipoFila=='5'){
				   			$("#selLimite").val(jsonDataText.desFila);
				   		}else if(jsonDataText.tipoFila=='4'){
				   			$("#selSubLimite").val(jsonDataText.desFila);
				   		}
				   		$("#selTipoFila").trigger('change');		 
				   		$("input[name^='columnBureau']").val(jsonDataText.bureau);
				   		$("input[name^='columnBuroAccionista']").val(jsonDataText.bureau);
						$("input[name^='columnRating']").val(jsonDataText.rating);
						$("#idRatingFecha").val(jsonDataText.fecha);
						
						$("input[name^='columnFecha']").val(jsonDataText.fecha);
						$("input[name^='columnLteAutorizado']").val(jsonDataText.lteAutorizado);
						$("input[name^='columnLteForm']").val(jsonDataText.lteForm);
						$("input[name^='columnRgoActual']").val(jsonDataText.rgoActual);	
						$("input[name^='columnRgoPropBbvaBc']").val(jsonDataText.rgoPropBbvaBc);
						$("input[name^='columnPropRiesgo']").val(jsonDataText.propRiesgo);
						$("input[name^='columnObservaciones']").val(jsonDataText.observaciones);
						
						$("input[name^='columnColumna1']").val(jsonDataText.columna1); 
						$("input[name^='columnColumna2']").val(jsonDataText.columna2);
						$("input[name^='columnColumna3']").val(jsonDataText.columna3);
						$("input[name^='columnColumna4']").val(jsonDataText.columna4);
						$("input[name^='columnColumna5']").val(jsonDataText.columna5);
						$("input[name^='columnColumna6']").val(jsonDataText.columna6);
						$("input[name^='columnColumna7']").val(jsonDataText.columna7);
						$("input[name^='columnColumna8']").val(jsonDataText.columna8);
						$("input[name^='columnColumna9']").val(jsonDataText.columna9);
						$("input[name^='columnColumna10']").val(jsonDataText.columna10);
						$("input[name^='columnContrato']").val(jsonDataText.contrato); 
				   		
				   		$("#idlblColumna1").html(jsonDataText.cabcolumna1); 
						$("#idlblColumna2").html(jsonDataText.cabcolumna2);
						$("#idlblColumna3").html(jsonDataText.cabcolumna3);
						$("#idlblColumna4").html(jsonDataText.cabcolumna4);
						$("#idlblColumna5").html(jsonDataText.cabcolumna5);
						$("#idlblColumna6").html(jsonDataText.cabcolumna6);
						$("#idlblColumna7").html(jsonDataText.cabcolumna7);
						$("#idlblColumna8").html(jsonDataText.cabcolumna8);
						$("#idlblColumna9").html(jsonDataText.cabcolumna9);
						$("#idlblColumna10").html(jsonDataText.cabcolumna10);
				   	
				   		
				   					   		
				   		if (jsonDataText.actCol1=="0"){
				   		$("#idColumna1Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol2=="0"){
				   		$("#idColumna2Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol3=="0"){
				   		$("#idColumna3Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol4=="0"){
				   		$("#idColumna4Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol5=="0"){
				   		$("#idColumna5Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol6=="0"){
				   		$("#idColumna6Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol7=="0"){
				   		$("#idColumna7Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol8=="0"){
				   		$("#idColumna8Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol9=="0"){
				   		$("#idColumna9Edit").attr("style","display:none");
				   		}
				   		if (jsonDataText.actCol10=="0"){
				   		$("#idColumna10Edit").attr("style","display:none");
				   		}
					   	
						resetTiempoSession();
						$( "#dialog-modal" ).dialog({
									height: 550,
									width: 700,
									modal: true
								});			   			     	
			   		}
			   	);
		   	}else{	
		   		alert("Seleccione una fila");
		   	}
		 });
		 
		$("#btnAgregarColmna").click(function(){
			$("#idNombreColumna").val("");
		 	$( "#dialog-modal-columna" ).dialog({
												height: 150,
												width: 500,
												modal: true
												});
		 });
		 
		 $("#selTipoFila").change(function(){
		 	$("#dialog-modal").attr("style","display:");
		 	if($(this).val()=='1'){
		 		$("#divBanco").attr("style","display:");
		 		$("#divEmpresa").attr("style","display:none");
		 		$("#divAccionista").attr("style","display:none");
		 		
		 		$("#formcolumnas").attr("style","display:none");
		 		$("#formEmpresa").attr("style","display:none");
		 		$("#divListaLimite").attr("style","display:none");
		 		$("#divListaLimiteLabel").attr("style","display:none");
		 		$("#divRiesgos").attr("style","display:none");
		 		$("#divRiesgosLabel").attr("style","display:none");
		 		//limpiarRating();
		 		
		 	}else if($(this).val()=='2'){
		 		$("#divEmpresa").attr("style","display:");		 		
		 		$("#divBanco").attr("style","display:none");
		 		$("#divAccionista").attr("style","display:none");
		 		
		 		$("#formcolumnas").attr("style","display:none");
		 		$("#formEmpresa").attr("style","display:");
		 		$("#divListaLimite").attr("style","display:none");
		 		$("#divListaLimiteLabel").attr("style","display:none");
		 		$("#divRiesgos").attr("style","display:none");
		 		$("#divRiesgosLabel").attr("style","display:none");
		 		//alert("entro seltipo");
		 		obtenerRating();
		 		//$("#selEmpresas").trigger('change');
		 	}else if($(this).val()=='3'){
		 		
		 		$("#divEmpresa").attr("style","display:none");
		 		$("#divBanco").attr("style","display:none");
		 		$("#formcolumnas").attr("style","display:");
		 		$("#formEmpresa").attr("style","display:none");
		 		$("#divAccionista").attr("style","display:none");
		 		
		 		$("#divListaOperacion").attr("style","display:");
		 		$("#divSubLimite").attr("style","display:none");
		 		$("#divListaOperacionLabel").attr("style","display:");
		 		$("#divSubLimiteLabel").attr("style","display:none");
		 		$("#divListaLimite").attr("style","display:none");
		 		$("#divListaLimiteLabel").attr("style","display:none");
		 		$("#divRiesgos").attr("style","display:");
		 		$("#divRiesgosLabel").attr("style","display:");
		 		limpiarFormulario();
		 		//limpiarRating();
		 	} else if($(this).val()=='4'){
		 		
		 		$("#divEmpresa").attr("style","display:none");
		 		$("#divBanco").attr("style","display:none");
		 		$("#formcolumnas").attr("style","display:");
		 		$("#formEmpresa").attr("style","display:none");
		 		$("#divAccionista").attr("style","display:none");
		 		
		 		$("#divListaOperacion").attr("style","display:none");
		 		$("#divSubLimite").attr("style","display:");
		 		$("#divListaOperacionLabel").attr("style","display:none");
		 		$("#divSubLimiteLabel").attr("style","display:");
		 		$("#divListaLimite").attr("style","display:none");
		 		$("#divListaLimiteLabel").attr("style","display:none");		 		
		 		$("#divRiesgos").attr("style","display:");		 		
		 		$("#divRiesgosLabel").attr("style","display:");
		 		//limpiarRating();
		 	} else if($(this).val()=='5'){
		 		$("#divEmpresa").attr("style","display:none");
		 		$("#divBanco").attr("style","display:none");
		 		$("#formcolumnas").attr("style","display:");
		 		$("#formEmpresa").attr("style","display:none");
		 		$("#divAccionista").attr("style","display:none");
		 		
		 		$("#divListaOperacionLabel").attr("style","display:none");
		 		$("#divListaOperacion").attr("style","display:none");
		 		$("#divSubLimite").attr("style","display:none");		 		
		 		$("#divSubLimiteLabel").attr("style","display:none");
		 		$("#divListaLimite").attr("style","display:");
		 		$("#divListaLimiteLabel").attr("style","display:");
		 		$("#divRiesgos").attr("style","display:");
		 		$("#divRiesgosLabel").attr("style","display:");
		 		limpiarFormulario();	
		 		//limpiarRating();	 		
		 	}else if($(this).val()=='7'){
		 		$("#divBanco").attr("style","display:none");
		 		$("#divEmpresa").attr("style","display:none");
		 		$("#divAccionista").attr("style","display:");
		 		
		 		$("#formcolumnas").attr("style","display:none");
		 		$("#formEmpresa").attr("style","display:none");
		 		$("#divListaLimite").attr("style","display:none");
		 		$("#divListaLimiteLabel").attr("style","display:none");
		 		$("#divRiesgos").attr("style","display:none");
		 		$("#divRiesgosLabel").attr("style","display:none");
		 	}
		 });
		 
		 $("#selEmpresas").change(function(){
		 
		 		var codempresa= $("#selEmpresas").val();
		 		limpiarRating();		 		
		 		//alert("entro selempresa:"+codempresa);
		 		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Obteniendo Rating. Espere por favor...</h3>', 
	  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
		 	
		 		$.post("obtenerRatingAnexo.do", { codempresaRating: codempresa },
			   		function(data){				   		 
				   		//var jsonDataText = jQuery.parseJSON(data);
				   		setTimeout($.unblockUI, 1);
				   		var jsonDataText = eval("(" + data + ")");
				   			
				   		var rating=jsonDataText.rating;	
				   		var fecharating=jsonDataText.fecharating;
				   		var bureau=jsonDataText.bureau;
				   		if (rating!=""){				   		
				   			$("#idRating").val(rating);
				   		}
				   		if (fecharating!=""){				   		
				   			$("#idRatingFecha").val(fecharating); 
				   		}
				   		if (bureau!=""){				   		
				   			$("#idBureau").val(bureau); 
				   		}				   					   		 		
				   		//$("#selEmpresas").trigger('change');	
				   		resetTiempoSession();	   			     	
			   		}
		 		
			   	);
		 
		  });

		 $("#selAccionista").change(function(){
			 	obtenerBuroAccionistaWS()
		  });

		 
		 function limpiarFormulario(){
 
			 $("input[name^='columnLteAutorizado']").val("");				
			 $("input[name^='columnLteForm']").val("");			
			 $("input[name^='columnRgoActual']").val("");
			 $("input[name^='columnContrato']").val("");
			 
			 
		 }
		 function limpiarRating(){
		 //revisar
		 	$("#idRating").val("");  
		 	$("#idRatingFecha").val("");
		 	$("#idBureau").val("");
		 }
		 function limpiarBuroAccionista(){
			$("#idBuroAccionista").val("");
		 }
		 
		 $("#modalAgregarColumna").click(function(){
		 	var nombre = $("#idNombreColumna").val();
		 	if(jQuery.trim(nombre)==''){
		 		alert('Ingrese un nombre de columna');
		 	}else{
		 	$("#nombreColumna").val(nombre);
		 	document.forms["formAnexos"].action='addColumna.do';
		 	document.forms["formAnexos"].nombreColumna.value=nombre;
			document.forms["formAnexos"].submit();
			}
		 });
		 
		 $("#sbcancelar").click(function(){
		 	$("#dialog-modal").dialog("close");
		 });
		 
		 $("#sbcancelarsaldo").click(function(){
		 	$("#dialog-modal-detalle-saldo").dialog("close");
		 });
		 
		 $("#sbcancelarcol").click(function(){
		 	$("#dialog-modal-columna").dialog("close");
		 });
		 $( "#idfecha" ).datepicker({ 
    							dateFormat: 'dd/mm/yy',    							
    							showOn: "button",
							    buttonImage: "images/calendariobbva.png",
								buttonImageOnly: true });
    	$( "#idfecha" ).datepicker();
    	
    	if($("#chkAnexo1").is(':checked')) {
            $("#divAnexo1").attr('style','display:');
        }
        
        $("#chkAnexo1").click(function () {
			$("#divAnexo1").toggle("slow");
		});
		
		if($("#chkAnexo2").is(':checked')) {
            $("#divAnexo2").attr('style','display:');
        }
        
        $("#chkAnexo2").click(function () {
			$("#divAnexo2").toggle("slow");
		}); 
		
		if($("#chkAnexo3").is(':checked')) {
            $("#divAnexo3").attr('style','display:');
        }
        
        $("#chkAnexo3").click(function () {
			$("#divAnexo3").toggle("slow");
		});  
        
        if($("#chkAnexo4").is(':checked')) {
            $("#divAnexo4").attr('style','display:');
        }
        
        $("#chkAnexo4").click(function () {
			$("#divAnexo4").toggle("slow");
		});
		
		$("#stextareacampoLibreAnexos").wysiwyg("addControl", 
  			   "UploadFile", {				
  			   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
  			   tooltip: 'Upload',tags: ['Upload'],				
  			   exec:  function() { 		   		
  			   		vtextarea='stextareacampoLibreAnexos'; 
  			   		newWindowUploadFile(vtextarea);  }
  		       }
  	     );
  		    
   		$("#bvercampoLibreAnexos").click(function () { 
   	   		
  			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
  					  overlayCSS: { backgroundColor: '#0174DF' } }); 
  			$("#divcampoLibreAnexos").attr("style","");
  			$("#ssavecampoLibreAnexos").prop("disabled","");
  			$("#scleancampoLibreAnexos").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "campoLibreAnexos" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareacampoLibreAnexos').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("campoLibreAnexos___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}  			     	
		   });
	   		//editado(COMEN_LIBRE_ANEXOS);
		});
		$("#ssavecampoLibreAnexos").click(function () { 
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); 
			var iframe = document.getElementById("campoLibreAnexos___Frame");
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
				var dataGet={campoBlob:'campoLibreAnexos',valorBlob:valblob};
				guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_LIBRE_ANEXOS);
				
			}else{
			
				if ( eInnerElement ){
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}
	
				if (accionGraba==1 || accionGraba==2){
					var dataGet={campoBlob:'campoLibreAnexos',valorBlob:valblob};
					guardarDatosTexto("saveProgramaBlob.do",dataGet,COMEN_LIBRE_ANEXOS);
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("INFORMACION ADICIONAL ANEXO"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
						setTimeout($.unblockUI, 1);
				}
			}
		});
		$("#scleancampoLibreAnexos").click(function () { 
			//$('#stextareacampoLibreAnexos').wysiwyg('setContent', '');
			var iframe = document.getElementById("campoLibreAnexos___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}  
		});
		
		$("#sverificacampoLibreAnexos").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("campoLibreAnexos___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		
		

		$("#btnAgregarFilaGarantias").click(function(){
			if(!validarDatosGeneral()){return;}
		 	$("#formAnexos").attr("action","agregarGarantia.do");
		 	$("#formAnexos").submit();
		 });
		
		
		$("#btnEliminarFilaGarantias").click(function(){	
			if($("#indiceGarantia").val()==""){alert("Seleccione una garantia para eliminar.");return;}
		 	$("#formAnexos").attr("action","eliminarGarantia.do");		 	
		 	$("#formAnexos").submit();
		 });
		 
		 $("#btnSincronicarGarantias").click(function(){
			 $.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Garantias. Espere por favor...</h3>', 
					  overlayCSS: { backgroundColor: '#0174DF' } }); 
			$("#formAnexos").attr("action","sincronizarGarantia.do");
		 	$("#formAnexos").submit();
		 });
		
		if(<%=flagActivo%>==1){			
	   		 desactivarFlag();	
	   	}	
	
	});
   
	function seleccionarGarantia(obj){		
		//alert(obj.value);
		$("#indiceGarantia").val(obj.value);
	}
   
   
   
   
   
</script>

<script src="/ProgramaFinanciero/js/comunValidacion.js" type="text/javascript"></script>

 <s:include value="/pages/programa/opciones_programa.jsp"></s:include>
	<%@include file="/pages/programa/tabsCabecera.jsp" %>
<script type="text/javascript">
	var flagGuardado=true;
</script>
<div class="tab_container">
	<div class="seccion datosGrupoEmpresa">
		<%String tipo_empresa = GenericAction.getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
		if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){%>
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
    	<s:form action="saveAnexos" 
    			id="formAnexos" 
    			name="formAnexos" 
    			method="post" 
    			theme="simple"
    			enctype="multipart/form-data" 
				onsubmit="SaveScrollXY(this);">
		<input name="scrollX" id="scrollX" type="hidden"  />
		<input name="scrollY" id="scrollY" type="hidden"  />
		<s:hidden name="tipoFila" id="tipoFila"></s:hidden>
		<s:hidden name="codigoTipoFila" id="codigoTipoFila" ></s:hidden>
		<s:hidden name="nombreColumna" id="nombreColumna" ></s:hidden>
		<s:hidden name="posAnexo" id="posAnexo" ></s:hidden>
		<s:hidden name="indiceGarantia" id="indiceGarantia" />
		<input type="hidden" name="codigoArchivo" />
		<input type="hidden" name="extension" />
		<input type="hidden" name="nombreArchivo" />
		<input type="hidden" name="pos" />
		
    	<table width="90%">
			<tr>
				<td class="bk_tabs">
					
					<h3 class="ui-widget-header_1 ui-corner-all">
					<s:checkbox name="flagAnexo1"   id="chkAnexo1"/>Detalle de posiciones</h3>
					<div id="divAnexo1" style="display: none;" class="">
							<table class="ln_formatos" cellspacing="0" width="100%">
								<tr>
									<td>
										<s:submit action="saveAnexos" value="Guardar"  theme="simple" cssClass="btn"></s:submit>
									</td>
									<td>
									&nbsp;
									</td>						
									<td colspan="2" align="right">
										<div id="idmsnAutoGuardado"></div>
									</td>
								</tr>
								
							</table>
				
							<div  class="my_div">
								<table  class="">
									<tr>										
										<th colspan="15" align="left">
										<input id="btnAgregarFila" type="button" value="Agregar Fila" class="btn"/>
										
										<input id="btnEditarFila" type="button" value="Editar" class="btn"/>
										
										<input id="btnEliminarFila" type="button" value="Eliminar" class="btn"/>
										<!-- <input id="btnLimpiarAnexo" type="button" value="Limpiar" class="btn"/> -->
										
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										
										<input id="btnAgregarColmna" type="button" value="Agregar Columna" class="btn"/>
										
										<s:submit action="deleteColumna" value="-" theme="simple" onclick="return confirmDeleteColumna();" cssClass="btn"></s:submit>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input id="btnRecalcular" type="button" value="Actualizar Saldo" class="btn"/>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EN MILES DE USD
										</th>
										
																												
									</tr>
									<tr>
										<td>
											<table id="tablaAnexo" class="ln_formatos ui-widget ui-widget-content">
<!--												<thead>-->
<!--												</thead>-->
<!--											<tbody>-->
												<%int cntorden=0;%>
													<s:iterator var="lista" value="listaFilaAnexos" id="dddd" status="rowstatus">
													
													<s:if test="anexo.tipoFila==0">
															<tr class="ui-widget-header " id="<s:property  value="anexo.id"/>">
																<td>#</td>
																<td>Nro</td>
																<td width="200px">
																	<s:hidden name="pos"></s:hidden>
																	<s:property  value="anexo.descripcion"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.bureau"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rating"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.fecha"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.lteAutorizado"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.lteForm"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rgoActual"></s:property>
																</td>
																<td style="text-align: left;" >																			
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rgoPropBbvaBc"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.propRiesgo"></s:property>
																</td>
																
																<td width="50px">																	
																		<s:property  value="anexo.observaciones"></s:property>																		
																</td>
																
																<s:if test='anexo.activoCol1=="1"'>
																	<td width="50px" id="idDTcolumna1">																			
<!--																		<s:hidden name="anexo.numColumna"></s:hidden>														-->
																		<s:property  value="anexo.columna1"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol2=="1"'>
																	<td width="50px" id="idDTcolumna2">																	
																		<s:property  value="anexo.columna2"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol3=="1"'>
																	<td width="50px" id="idDTcolumna3">																	
																		<s:property  value="anexo.columna3"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol4=="1"'>
																	<td width="50px" id="idDTcolumna4">																	
																		<s:property  value="anexo.columna4"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol5=="1"'>
																	<td width="50px" id="idDTcolumna5">																	
																		<s:property  value="anexo.columna5"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol6=="1"'>														
																	<td width="50px" id="idDTcolumna6">																	
																		<s:property  value="anexo.columna6"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol7=="1"'>	
																	<td width="50px" id="idDTcolumna7">																	
																		<s:property  value="anexo.columna7"></s:property>
																	</td>
																</s:if>
																
																
																<s:if test='anexo.activoCol8=="1"'>	
																	<td width="50px" id="idDTcolumna8">																	
																		<s:property  value="anexo.columna8"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol9=="1"'>	
																	<td width="50px" id="idDTcolumna9">																	
																		<s:property  value="anexo.columna9"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol10=="1"'>
																	<td width="50px" id="idDTcolumna10">																	
																		<s:property  value="anexo.columna10"></s:property>
																	</td>										
																</s:if>
																

															</tr>
														</s:if>
														<s:elseif test="anexo.tipoFila==1">
															<tr bgcolor="#A9D0F5" id="<s:property  value="anexo.id"/>">
																<td>
																	<input type="radio" value="<s:property  value="pos"/>" name="chkanexo"></input>
																</td>
																<td>	
																&nbsp;															
																</td>
																<td width="200px">
																	<s:hidden name="pos"></s:hidden>
																	<s:property  value="anexo.descripcion"></s:property>
																</td>
																
																
																<td width="50px">																	
																	<s:property  value="anexo.bureau"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rating"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.fecha"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.lteAutorizado"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.lteForm"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rgoActual"></s:property>
																</td>
																<td style="text-align: left;" >																			
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rgoPropBbvaBc"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.propRiesgo"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.observaciones"></s:property>
																	
																</td>
																<s:if test='anexo.activoCol1=="1"'>
																	<td width="50px" class="cssDTcolumna1">																	
																		<s:property  value="anexo.columna1"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol2=="1"'>
																	<td width="50px" class="cssDTcolumna2">																	
																		<s:property  value="anexo.columna2"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol3=="1"'>
																	<td width="50px" class="cssDTcolumna3">																	
																		<s:property  value="anexo.columna3"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol4=="1"'>
																	<td width="50px" class="cssDTcolumna4">																	
																		<s:property  value="anexo.columna4"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol5=="1"'>
																	<td width="50px" class="cssDTcolumna5">																	
																		<s:property  value="anexo.columna5"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol6=="1"'>														
																	<td width="50px" class="cssDTcolumna6">																	
																		<s:property  value="anexo.columna6"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol7=="1"'>	
																	<td width="50px" class="cssDTcolumna7">																	
																		<s:property  value="anexo.columna7"></s:property>
																	</td>
																</s:if>
																
																
																<s:if test='anexo.activoCol8=="1"'>	
																	<td width="50px" class="cssDTcolumna8">																	
																		<s:property  value="anexo.columna8"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol9=="1"'>	
																	<td width="50px" class="cssDTcolumna9">																	
																		<s:property  value="anexo.columna9"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol10=="1"'>
																	<td width="50px" class="cssDTcolumna10">																	
																		<s:property  value="anexo.columna10"></s:property>
																	</td>										
																</s:if>									
									
															</tr>
														</s:elseif>												
														<s:elseif test="anexo.tipoFila==2">
															<tr bgcolor="#CEE3F6" id="<s:property  value="anexo.id"/>" >
																<td>
																	<input type="radio" value="<s:property  value="pos"/>" name="chkanexo"></input>
																</td>
																<td>
																<% cntorden=0;%>																																
																</td>
																<td width="200px">
																	<s:hidden name="pos"></s:hidden>
																	<s:property  value="anexo.descripcion"></s:property>
																</td>
																
																<td width="50px">																	
																	<s:property  value="anexo.bureau"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rating"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.fecha"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.lteAutorizado"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.lteForm"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rgoActual"></s:property>
																</td>
																<td style="text-align: left;" >																			
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rgoPropBbvaBc"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.propRiesgo"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.observaciones"></s:property>
																</td>
																<s:if test='anexo.activoCol1=="1"'>
																	<td width="50px" class="cssDTcolumna1">																	
																		<s:property  value="anexo.columna1"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol2=="1"'>
																	<td width="50px" class="cssDTcolumna2">																	
																		<s:property  value="anexo.columna2"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol3=="1"'>
																	<td width="50px" class="cssDTcolumna3">																	
																		<s:property  value="anexo.columna3"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol4=="1"'>
																	<td width="50px" class="cssDTcolumna4">																	
																		<s:property  value="anexo.columna4"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol5=="1"'>
																	<td width="50px" class="cssDTcolumna5">																	
																		<s:property  value="anexo.columna5"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol6=="1"'>														
																	<td width="50px" class="cssDTcolumna6">																	
																		<s:property  value="anexo.columna6"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol7=="1"'>	
																	<td width="50px" class="cssDTcolumna7">																	
																		<s:property  value="anexo.columna7"></s:property>
																	</td>
																</s:if>
																
																
																<s:if test='anexo.activoCol8=="1"'>	
																	<td width="50px" class="cssDTcolumna8">																	
																		<s:property  value="anexo.columna8"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol9=="1"'>	
																	<td width="50px" class="cssDTcolumna9">																	
																		<s:property  value="anexo.columna9"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol10=="1"'>
																	<td width="50px" class="cssDTcolumna10">																	
																		<s:property  value="anexo.columna10"></s:property>
																	</td>										
																</s:if>										
															</tr>
														</s:elseif>
														
														
														
														
														<s:elseif  test="anexo.tipoFila==3">
															<tr bgcolor="#FFFFFF" id="<s:property  value="anexo.id"/>">
																<td>
																	<input type="radio" value="<s:property  value="pos"/>" name="chkanexo"></input>
																</td>
																<td>
																<%cntorden=cntorden+1;%>
																<%=cntorden%>
																</td>
																<td width="200px">
																	<s:hidden name="pos"></s:hidden>
																	<s:property  value="anexo.descripcion"></s:property>
																</td>
																
																<td width="50px">																	
																	<s:property  value="anexo.bureau"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rating"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.fecha"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.lteAutorizado"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.lteForm"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rgoActual"></s:property>
																</td>
																<td>																			
																	<s:if  test="activosaldo=='AA'">
																				<a href="#" onclick="consultarSaldo(<s:property  value="pos"/>)" > <img src="imagentabla/bbva.ConsultaAzul24.png" border="0" alt="Consultar Saldo" ></img></a>
																	</s:if>	
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rgoPropBbvaBc"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.propRiesgo"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.observaciones"></s:property>
																</td>
																<s:if test='anexo.activoCol1=="1"'>
																	<td width="50px" class="cssDTcolumna1">																	
																		<s:property  value="anexo.columna1"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol2=="1"'>
																	<td width="50px" class="cssDTcolumna2">																	
																		<s:property  value="anexo.columna2"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol3=="1"'>
																	<td width="50px" class="cssDTcolumna3">																	
																		<s:property  value="anexo.columna3"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol4=="1"'>
																	<td width="50px" class="cssDTcolumna4">																	
																		<s:property  value="anexo.columna4"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol5=="1"'>
																	<td width="50px" class="cssDTcolumna5">																	
																		<s:property  value="anexo.columna5"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol6=="1"'>														
																	<td width="50px" class="cssDTcolumna6">																	
																		<s:property  value="anexo.columna6"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol7=="1"'>	
																	<td width="50px" class="cssDTcolumna7">																	
																		<s:property  value="anexo.columna7"></s:property>
																	</td>
																</s:if>
																
																
																<s:if test='anexo.activoCol8=="1"'>	
																	<td width="50px" class="cssDTcolumna8">																	
																		<s:property  value="anexo.columna8"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol9=="1"'>	
																	<td width="50px" class="cssDTcolumna9">																	
																		<s:property  value="anexo.columna9"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol10=="1"'>
																	<td width="50px" class="cssDTcolumna10">																	
																		<s:property  value="anexo.columna10"></s:property>
																	</td>										
																</s:if>																
															</tr>
														</s:elseif>
														<s:elseif  test="anexo.tipoFila==4">
															<tr bgcolor="#FFFFFF" id="<s:property  value="anexo.id"/>">
																<td>
																	<input type="radio" value="<s:property  value="pos"/>" name="chkanexo"></input>
																</td>
																<td>
																&nbsp;																
																</td>
																<td width="200px">
																	<s:hidden name="pos"></s:hidden>
																	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:property  value="anexo.descripcion"></s:property>
																</td>
																
																<td width="50px">																	
																	<s:property  value="anexo.bureau"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rating"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.fecha"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.lteAutorizado"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.lteForm"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rgoActual"></s:property>
																</td>
																<td>																			
																			<s:if  test="activosaldo=='AA'">
																				<a href="#" onclick="consultarSaldoSublimite(<s:property  value="pos"/>)" > <img src="imagentabla/bbva.ConsultaAzul24.png" border="0" alt="Consultar Saldo" ></img></a>
																			</s:if>	
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rgoPropBbvaBc"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.propRiesgo"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.observaciones"></s:property>
																</td>
																<s:if test='anexo.activoCol1=="1"'>
																	<td width="50px" class="cssDTcolumna1">																	
																		<s:property  value="anexo.columna1"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol2=="1"'>
																	<td width="50px" class="cssDTcolumna2">																	
																		<s:property  value="anexo.columna2"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol3=="1"'>
																	<td width="50px" class="cssDTcolumna3">																	
																		<s:property  value="anexo.columna3"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol4=="1"'>
																	<td width="50px" class="cssDTcolumna4">																	
																		<s:property  value="anexo.columna4"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol5=="1"'>
																	<td width="50px" class="cssDTcolumna5">																	
																		<s:property  value="anexo.columna5"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol6=="1"'>														
																	<td width="50px" class="cssDTcolumna6">																	
																		<s:property  value="anexo.columna6"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol7=="1"'>	
																	<td width="50px" class="cssDTcolumna7">																	
																		<s:property  value="anexo.columna7"></s:property>
																	</td>
																</s:if>
																
																
																<s:if test='anexo.activoCol8=="1"'>	
																	<td width="50px" class="cssDTcolumna8">																	
																		<s:property  value="anexo.columna8"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol9=="1"'>	
																	<td width="50px" class="cssDTcolumna9">																	
																		<s:property  value="anexo.columna9"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol10=="1"'>
																	<td width="50px" class="cssDTcolumna10">																	
																		<s:property  value="anexo.columna10"></s:property>
																	</td>										
																</s:if>																
															</tr>
														</s:elseif>
														<s:elseif  test="anexo.tipoFila==5">
															<tr bgcolor="#FFFFFF" id="<s:property  value="anexo.id"/>">
																<td>
																	<input type="radio" value="<s:property  value="pos"/>" name="chkanexo"></input>
																</td>
																<td>
																<%cntorden=cntorden+1;%>
																<%=cntorden%>
																</td>
																<td width="200px">
																	<s:hidden name="pos"></s:hidden>
																	<s:property  value="anexo.descripcion"></s:property>
																</td>
																
																<td width="50px">																	
																	<s:property  value="anexo.bureau"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rating"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.fecha"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.lteAutorizado"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.lteForm"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rgoActual"></s:property>
																</td>
																<td>																			
																			<s:if  test="activosaldo=='AA'">
																				<a href="#" onclick="consultarSaldo(<s:property  value="pos"/>)" > <img src="imagentabla/bbva.ConsultaAzul24.png" border="0" alt="Consultar Saldo" ></img></a>
																			</s:if>	
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rgoPropBbvaBc"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.propRiesgo"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.observaciones"></s:property>
																</td>
																
																<s:if test='anexo.activoCol1=="1"'>
																	<td width="50px" class="cssDTcolumna1">																	
																		<s:property  value="anexo.columna1"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol2=="1"'>
																	<td width="50px" class="cssDTcolumna2">																	
																		<s:property  value="anexo.columna2"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol3=="1"'>
																	<td width="50px" class="cssDTcolumna3">																	
																		<s:property  value="anexo.columna3"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol4=="1"'>
																	<td width="50px" class="cssDTcolumna4">																	
																		<s:property  value="anexo.columna4"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol5=="1"'>
																	<td width="50px" class="cssDTcolumna5">																	
																		<s:property  value="anexo.columna5"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol6=="1"'>														
																	<td width="50px" class="cssDTcolumna6">																	
																		<s:property  value="anexo.columna6"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol7=="1"'>	
																	<td width="50px" class="cssDTcolumna7">																	
																		<s:property  value="anexo.columna7"></s:property>
																	</td>
																</s:if>
																
																
																<s:if test='anexo.activoCol8=="1"'>	
																	<td width="50px" class="cssDTcolumna8">																	
																		<s:property  value="anexo.columna8"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol9=="1"'>	
																	<td width="50px" class="cssDTcolumna9">																	
																		<s:property  value="anexo.columna9"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol10=="1"'>
																	<td width="50px" class="cssDTcolumna10">																	
																		<s:property  value="anexo.columna10"></s:property>
																	</td>										
																</s:if>													

															</tr>
														</s:elseif>

														<s:elseif test="anexo.tipoFila==7">
															<tr bgcolor="#CEE3F6" id="<s:property  value="anexo.id"/>" >
																<td>
																	<input type="radio" value="<s:property  value="pos"/>" name="chkanexo"></input>
																</td>
																<td>
																<% cntorden=0;%>																																
																</td>
																<td width="200px">
																	<s:hidden name="pos"></s:hidden>
																	<s:property  value="anexo.descripcion"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.bureau"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rating"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.fecha"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.lteAutorizado"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.lteForm"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rgoActual"></s:property>
																</td>
																<td style="text-align: left;" >																			
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.rgoPropBbvaBc"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.propRiesgo"></s:property>
																</td>
																<td width="50px">																	
																	<s:property  value="anexo.observaciones"></s:property>
																</td>
																
																<s:if test='anexo.activoCol1=="1"'>
																	<td width="50px" class="cssDTcolumna1">																	
																		<s:property  value="anexo.columna1"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol2=="1"'>
																	<td width="50px" class="cssDTcolumna2">																	
																		<s:property  value="anexo.columna2"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol3=="1"'>
																	<td width="50px" class="cssDTcolumna3">																	
																		<s:property  value="anexo.columna3"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol4=="1"'>
																	<td width="50px" class="cssDTcolumna4">																	
																		<s:property  value="anexo.columna4"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol5=="1"'>
																	<td width="50px" class="cssDTcolumna5">																	
																		<s:property  value="anexo.columna5"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol6=="1"'>														
																	<td width="50px" class="cssDTcolumna6">																	
																		<s:property  value="anexo.columna6"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol7=="1"'>	
																	<td width="50px" class="cssDTcolumna7">																	
																		<s:property  value="anexo.columna7"></s:property>
																	</td>
																</s:if>
																
																
																<s:if test='anexo.activoCol8=="1"'>	
																	<td width="50px" class="cssDTcolumna8">																	
																		<s:property  value="anexo.columna8"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol9=="1"'>	
																	<td width="50px" class="cssDTcolumna9">																	
																		<s:property  value="anexo.columna9"></s:property>
																	</td>
																</s:if>
																
																<s:if test='anexo.activoCol10=="1"'>
																	<td width="50px" class="cssDTcolumna10">																	
																		<s:property  value="anexo.columna10"></s:property>
																	</td>										
																</s:if>
																
															</tr>
														</s:elseif>
														<s:else>
														</s:else>
													</s:iterator>
													
													
													<tr bgcolor="#E6E6E6" id="<s:property  value="filaTotal.anexo.id"/>">
										
														<td>															
														<s:hidden name="filaTotal.anexo.tipoFila"></s:hidden>																																
														</td>
														<td>	
														&nbsp;															
														</td>
														<td width="200px">
															<s:textfield name="filaTotal.anexo.descripcion"  theme="simple" size="15" maxlength="20" readonly="true"/>
														</td>																										
														
														<td width="50px">																	
															<s:property  value="filaTotal.anexo.bureau"></s:property>
														</td>
														<td width="50px">																	
															<s:property  value="filaTotal.anexo.rating"></s:property>
														</td>
														<td width="50px">																	
															<s:property  value="filaTotal.anexo.fecha"></s:property>
														</td>
														<td width="50px">
															<s:textfield label="valortot"  size="5"  
										                				 		name="filaTotal.anexo.lteAutorizado" 
										                				 		onkeypress="EvaluateText('%d', this);"
										                				 		onblur="this.value = NumberFormat(this.value, '0', '.', ',')"
										                				 		theme="simple"  maxlength="15"
										                				 		cssStyle="text-align:right"/>
														</td>
														<td width="50px">
															<s:textfield label="valortot"  size="5"  
										                				 		name="filaTotal.anexo.lteForm" 
										                				 		onkeypress="EvaluateText('%d', this);"
										                				 		onblur="this.value = NumberFormat(this.value, '0', '.', ',')"
										                				 		theme="simple"  maxlength="15"
										                				 		cssStyle="text-align:right"/>
														</td>
														<td width="50px">
															<s:textfield label="valortot"  size="5"  
										                				 		name="filaTotal.anexo.rgoActual" 
										                				 		onkeypress="EvaluateText('%d', this);"
										                				 		onblur="this.value = NumberFormat(this.value, '0', '.', ',')"
										                				 		theme="simple"  maxlength="15"
										                				 		cssStyle="text-align:right"/>
														</td>
														<td style="text-align: left;" >																			
														</td>
														<td width="50px">
															<s:textfield label="valortot"  size="5"  
										                				 		name="filaTotal.anexo.rgoPropBbvaBc" 
										                				 		onkeypress="EvaluateText('%d', this);"
										                				 		onblur="this.value = NumberFormat(this.value, '0', '.', ',')"
										                				 		theme="simple"  maxlength="15"
										                				 		cssStyle="text-align:right"/>
														</td>
														<td width="50px">
															<s:textfield label="valortot"  size="5"  
										                				 		name="filaTotal.anexo.propRiesgo" 
										                				 		onkeypress="EvaluateText('%d', this);"
										                				 		onblur="this.value = NumberFormat(this.value, '0', '.', ',')"
										                				 		theme="simple"  maxlength="15"
										                				 		cssStyle="text-align:right"/>
														</td>
														<td width="50px">																	
															<s:property  value="filaTotal.anexo.observaciones"></s:property>
														</td>
																	
																<s:if test='filaTotal.anexo.activoCol1=="1"'>
																	<td width="50px" class="cssDTcolumna1">																	
																		<s:property  value="anexo.columna1"></s:property>
																	</td>
																</s:if>
																
																<s:if test='filaTotal.anexo.activoCol2=="1"'>
																	<td width="50px" class="cssDTcolumna2">																	
																		<s:property  value="anexo.columna2"></s:property>
																	</td>
																</s:if>
																
																<s:if test='filaTotal.anexo.activoCol3=="1"'>
																	<td width="50px" class="cssDTcolumna3">																	
																		<s:property  value="anexo.columna3"></s:property>
																	</td>
																</s:if>
																
																<s:if test='filaTotal.anexo.activoCol4=="1"'>
																	<td width="50px" class="cssDTcolumna4">																	
																		<s:property  value="anexo.columna4"></s:property>
																	</td>
																</s:if>
																
																<s:if test='filaTotal.anexo.activoCol5=="1"'>
																	<td width="50px" class="cssDTcolumna5">																	
																		<s:property  value="anexo.columna5"></s:property>
																	</td>
																</s:if>
																
																<s:if test='filaTotal.anexo.activoCol6=="1"'>														
																	<td width="50px" class="cssDTcolumna6">																	
																		<s:property  value="anexo.columna6"></s:property>
																	</td>
																</s:if>
																
																<s:if test='filaTotal.anexo.activoCol7=="1"'>	
																	<td width="50px" class="cssDTcolumna7">																	
																		<s:property  value="anexo.columna7"></s:property>
																	</td>
																</s:if>
																
																
																<s:if test='filaTotal.anexo.activoCol8=="1"'>	
																	<td width="50px" class="cssDTcolumna8">																	
																		<s:property  value="anexo.columna8"></s:property>
																	</td>
																</s:if>
																
																<s:if test='filaTotal.anexo.activoCol9=="1"'>	
																	<td width="50px" class="cssDTcolumna9">																	
																		<s:property  value="anexo.columna9"></s:property>
																	</td>
																</s:if>
																
																<s:if test='filaTotal.anexo.activoCol10=="1"'>
																	<td width="50px" class="cssDTcolumna10">																	
																		<s:property  value="anexo.columna10"></s:property>
																	</td>										
																</s:if>
													</tr>
<!--												</tbody>-->
											</table>
										</td>
									</tr>
								</table>
							</div>
							
							
						
						<div id="dialog-modal-columna" title="Agregar Columna" style="display: none;" >
							
								<div id="divNombreColumna" >
									Nombre Columna:
									<s:textfield id="idNombreColumna" theme="simple" size="60" maxlength="60"
												 name="nombreColumna"
												 onkeypress="ingresoLetrasNumeros(event);"></s:textfield>
								</div>
								<div>
									&nbsp;&nbsp;
								</div>
								<input type="button" value="Agregar" id="modalAgregarColumna" class="btn">
								<input type="button" value="Cancelar"  id="sbcancelarcol" class="btn"/>
						</div>
						
					
				</div>	
							
				
				<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagAnexo4"   id="chkAnexo4"/>Garantias</h3>
					<div id="divAnexo4" style="display: none;" class="">
					
						<table>
								<tr>
									<td>
										<s:submit action="saveGarantias" value="Guardar"  theme="simple" onclick="return validarDatosGeneral();" cssClass="btn"></s:submit>
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
								</tr>
								
							</table>
				
							<div  class="my_div">
								<table  class="">
									<tr>
										<td>
<!-- 											<div id="divEmpresaGarantia"> -->
<!-- 												Empresa: -->
<%-- 												<s:select name="codigoEmpresaGarantia" id="selEmpresasgarantia" list="listaEmpresas" listKey="id" listValue="nombre" theme="simple"></s:select> --%>
<!-- 											</div> -->
										</td>
									</tr>
									<tr>
										<td>
										
											<table class="ln_formatos ui-widget ui-widget-content">
												<thead>
													<tr>
														<th colspan="4" align="left">
														
															<input id="btnAgregarFilaGarantias" type="button" value="Agregar Fila" class="btn"/>														
<!-- 															<input id="btnEditarFilaGarantias" type="button" value="Editar" class="btn"/>														 -->
															<input id="btnEliminarFilaGarantias" type="button" value="Eliminar" class="btn"/>
															<input id="btnSincronicarGarantias" type="button" value="Sincronizar" class="btn"/>														
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														
														</th>
													</tr>
													<tr  class="ui-widget-header ">
														<th>
															#
														</th>
														<th>
															Empresa
														</th>
														<th>
															Numero Garantia
														</th>
														<th>
															Importe
														</th>
														<th>
															Descripci&oacute;n
														</th>
														<th>
															Nota
														</th>
													</tr>
												</thead>
												<tbody>
													<s:iterator var="lista" value="listaGarantiaAnexo" id="dddd" status="rowstatus">
															<tr>
																<td  bgcolor="#A9D0F5" width="30px"> 
																	<input type="radio" value="<s:property  value="%{codigoGarantiaAnexo}"/>" name="chkanexo" onclick="seleccionarGarantia(this)"/>
																	<s:hidden name="listaGarantiaAnexo[%{#rowstatus.index}].codigoGarantiaAnexo" value="%{codigoGarantiaAnexo}" />																	
																	<s:hidden name="listaGarantiaAnexo[%{#rowstatus.index}].id" value="%{id}" />
																	<s:hidden name="listaGarantiaAnexo[%{#rowstatus.index}].codEmpresaGrupo" value="%{codEmpresaGrupo}" />
																	<s:hidden name="listaGarantiaAnexo[%{#rowstatus.index}].tipoGarantia" value="%{tipoGarantia}" />
																	<s:hidden name="listaGarantiaAnexo[%{#rowstatus.index}].flagSincro"></s:hidden>
																	<s:set name="sincro" value="%{listaGarantiaAnexo[#rowstatus.index].flagSincro}" /> 
																</td>
																<td>
																<s:if test="%{#sincro=='SI'}">
																	<s:hidden name="listaGarantiaAnexo[%{#rowstatus.index}].empresa" value="%{empresa}" />
																	<s:select disabled="true" name="listaGarantiaAnexo[%{#rowstatus.index}].empresa" id="selEmpresasgarantia" list="listaEmpresas" listKey="nombre" value="%{empresa}" listValue="nombre" theme="simple"></s:select>
																</s:if> 											
																<s:else>
																	<s:select name="listaGarantiaAnexo[%{#rowstatus.index}].empresa" id="selEmpresasgarantia" list="listaEmpresas" listKey="nombre" value="%{empresa}" listValue="nombre" theme="simple"></s:select>
																</s:else>
																</td>
																<td width="100px">	
																	<s:if test="%{#sincro=='SI'}">	
																		<s:textfield readonly="true" label="Número Garantia" maxlength="20" size="15"
									              				 			name="listaGarantiaAnexo[%{#rowstatus.index}].numeroGarantia" value="%{numeroGarantia}" 
									              				 			theme="simple"  cssStyle="text-align: right;" cssClass="obligatorio" />
									              				 	</s:if> 											
																	<s:else>
																		<s:textfield label="Número Garantia" maxlength="20" size="15"
									              				 			name="listaGarantiaAnexo[%{#rowstatus.index}].numeroGarantia" value="%{numeroGarantia}" 
									              				 			theme="simple"  cssStyle="text-align: right;" cssClass="obligatorio" />																
																	</s:else>
																</td>
																<td width="50px">	
																	<s:if test="%{#sincro=='SI'}">		
																		<s:textfield readonly="true" label="Importe" maxlength="20" size="10" 
									              				 			name="listaGarantiaAnexo[%{#rowstatus.index}].importe" value="%{importe}" 
									              				 			onkeypress="EvaluateText('%f', this);"
														     				onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     				theme="simple"  cssStyle="text-align: right;" cssClass="obligatorio" />
														     		</s:if> 											
																	<s:else>
																		<s:textfield label="Importe" maxlength="20" size="10" 
									              				 			name="listaGarantiaAnexo[%{#rowstatus.index}].importe" value="%{importe}" 
									              				 			onkeypress="EvaluateText('%f', this);"
														     				onblur="this.value = NumberFormat(this.value, '2', '.', ',')"
														     				theme="simple"  cssStyle="text-align: right;" cssClass="obligatorio" />
																	</s:else>
																</td>
																<td >	
																	<s:if test="%{#sincro=='SI'}">																
																	 	<s:textarea readonly="true" cols="50" rows="5" label="Descripción" 	cssClass="tareacoment"																	
																		name="listaGarantiaAnexo[%{#rowstatus.index}].descripcionGarantia"
																		value="%{descripcionGarantia}"  />
																	</s:if> 											
																	<s:else>
																		<s:textarea cols="50" rows="5" label="Descripción" 	cssClass="tareacoment"																	
																		name="listaGarantiaAnexo[%{#rowstatus.index}].descripcionGarantia"
																		value="%{descripcionGarantia}"  />
																	
																	</s:else>
																</td>
																<td >																	
																	<s:textarea cols="50" rows="5" label="Nota" 	cssClass="tareacomentnota"																	
																		name="listaGarantiaAnexo[%{#rowstatus.index}].nota"
																		value="%{nota}"  />
																</td>
																
																
															</tr>
																								
													</s:iterator>
												</tbody>
											</table>
										</td>
									</tr>
								</table>
							</div>
														
					</div>
				
								
				<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagAnexo2"   id="chkAnexo2"/>Informaci&oacuten Adicional</h3>
					<div id="divAnexo2" style="display: none;" class="">
						<table class="ln_formatos" cellspacing="0">
							<tr>
								<td><label style="font-size: 10pt; font-weight: bold;">Informaci&oacuten Adicional:</label>
								</td>
								<td><input type="button" id="bvercampoLibreAnexos" value="Ver"
									class="btn" /> 
									
									<input
									type="button" value="Guardar" id="ssavecampoLibreAnexos"
									disabled="disabled"
									class="btn"/> 
									
									<input
									type="button" value="Limpiar" id="scleancampoLibreAnexos"
									disabled="disabled"
									class="btn" />
									<input
									type="button" value="Validar" id="sverificacampoLibreAnexos"									
									class="btn" />
									</td>
							</tr>
						</table>
						<div id="divcampoLibreAnexos" style="display: none;">

							<FCK:editor instanceName="campoLibreAnexos" height="250px">
								<jsp:attribute name="value">&nbsp;
								</jsp:attribute>									
								
							</FCK:editor>
						</div>
					</div>	
				<h3 class="ui-widget-header_1 ui-corner-all"><s:checkbox name="flagAnexo3"   id="chkAnexo3"/>Adjuntar Archivos</h3>
					<div id="divAnexo3" style="display: none;" class="">
							<table>
								<tr>
									<td class="bk_tabs">
						
									<table class="ln_formatos" cellspacing="0">
										<tr>
											<td><s:file name="fileAnexo" label="Ruta Archivo" size="70"
												cssClass="btn"
												theme="simple" /></td>
											<td><s:submit value="Grabar" action="saveFileAnexo"
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
													<s:iterator value="listaArchivosAnexo" var="filesAnexo"
														status="userStatus">
														<tr>
															<td><s:date name="fechaCreacion" format="dd/MM/yyyy" /></td>
															<td><s:property value="codUsuarioCreacion" /></td>
															<td><s:property value="nombreArchivo" /></td>
															<td><s:property value="extencion" /></td>
															<td align="center"> <a
																href="javascript:descargarArchivo('<s:property value="id" />','<s:property value="extencion" />','<s:property value="nombreArchivo" />');">
															<img src="imagentabla/bbva.documentoAzul24.png" border="0" alt="download"> </a></td>
															<td align="center">
																<a href="javascript:eliminar('<s:property value="id" />','<s:property value="extencion" />','<s:property value="nombreArchivo" />');">
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
				</td>
			</tr>			
		</table>
	
	</s:form>	
			    
		<div id="dialog-modal" title="Agregar Fila" style="display:none" >
			<s:form action="addFila" method="post" theme="simple" name="frmfila2" id="frmfila2" onsubmit="return validar(this);">
				<input name="scrollX" id="scrollX" type="hidden"  />
				<input name="scrollY" id="scrollY" type="hidden"  />
				<input name="posAnexo" id="posAnexoModal" type="hidden"  />
				<input name="editAnexo" id="editAnexo" type="hidden"  />
				<div id="divtiposFila" >
					Tipo Fila:
					<s:select   name="tipoFila"
								id="selTipoFila"
								list="#{'1':'Banco','2':'Empresa','7':'Accionista','3':'Limite','5':'Operacion','4':'Sub Limite'}"
								theme="simple"/>
					<div>
						&nbsp;&nbsp;
					</div>
					<div id="divBanco" style="display:" >
						Bancos:
						
						<s:select name="codigoBanco" id="selBanco" list="listaBancos" listKey="id" listValue="descripcion" theme="simple"></s:select>
					</div>
					<div id="divEmpresa" style="display: none;">
						Empresas:
						<s:select name="idEmpresaAnexo" id="selEmpresas" list="listaEmpresas" listKey="id" listValue="nombre" theme="simple"></s:select>
					</div>
					
					<div id="divAccionista" style="display: none;">
					<table>
						<tr>
							<td>Accionista:</td>
							<td colspan="3">
								<s:select name="idAccionistaAnexo" id="selAccionista" 
								   list="listaAccionistaAnexo" listKey="id" style="width:300px" listValue="nombre"
								   cssClass="validate[required]" />
							</td>
						</tr>	
						<tr>
							<td>
											BURO:
							</td>
							<td>
								<table>
									<tr>
										<td>
										<s:textfield name="columnBuroAccionista" theme="simple"											 
											 size="15"
										     maxlength="20"
										     cssClass="input_form"
										     id="idBuroAccionista"
										     onkeypress="ingresoLetrasNumerosSingos(event);"></s:textfield>
										</td>
										<td>
										<a href="javascript:obtenerBuroAccionistaWS();">
											<img align="center" src="imagentabla/bbva.ActualizarAzul24.png" alt="Refresh" border="0">
										</a>
										</td>
									</tr>
								</table>
										     
							</td>
							<td>
																													
							</td>
							<td>
								<div id="idmsnburoAccionista"></div>
							</td>
						</tr>		   
					</table>
					</div>

					<div id="formcolumnas" style="display: none;">
						<table>
							<tr>
								<td>
								<div id="divListaOperacionLabel">
									DESCRIPCIÓN
								</div>
								<div id="divListaLimiteLabel">
									DESCRIPCIÓN
								</div>
								<div id="divSubLimiteLabel">
									SUB LIMITE
								</div>
								<div id="divRiesgosLabel">
									RIESGOS
								</div>
								</td>
								<td>
								<!--ojo los hijos de operacion se considera para limite se actualizo descripcion del padre operacion-->
								<div id="divListaOperacion">
									<s:select name="operacion" id="selOperaciones" list="listaOperaciones" listKey="id" listValue="descripcion" theme="simple"></s:select>
								</div>
								<!--ojo se considera para operacion se creo nueva tabla para operacion-->
								<div id="divListaLimite">
									<s:select name="limite" id="selLimite" list="listaLimites" listKey="id" listValue="descripcion" theme="simple"></s:select>
								</div>
								<div id="divSubLimite">
									<s:textfield name="subLimite" id="selSubLimite" maxLength="1900" size="65"
												 cssClass="input_form"
												 onkeypress="ingresoLetrasNumeros(event);"></s:textfield>
								</div>
								<div id="divRiesgos">
									<a href="#" onclick="consultarSaldoAgregarFila()" > <img src="imagentabla/bbva.ConsultaAzul24.png" border="0" alt="Consultar Saldo" ></img></a>
								</div>
								</td>
							</tr>
							
							<tr>									
								<td>										   
									FECHA:
								</td>
								<td>
									<s:textfield name="columnFecha" 
												 theme="simple" size="15" 
												 cssClass="input_form"
												 id="idfecha"
												 onblur="javascript:valFecha(document.frmfila2.idfecha);">
									</s:textfield>(dd/mm/yyyy)
								</td>
							</tr>
							<tr>
								<td>										   
									LTE AUTORIZADO:
								</td>
								<td>
									<s:textfield name="columnLteAutorizado" 
									 theme="simple" 											
									 size="15"
									 onkeypress="EvaluateText('%d', this);" 
								     onblur="this.value = NumberFormat(this.value, '0', '.', ',')"
								     cssStyle="text-align:right"
								     maxlength="20"
								     cssClass="input_form"></s:textfield>
								</td>									
							</tr>
							<tr>	
								<td>										   
									LTE FORM:
								</td>
								<td>
									<s:textfield name="columnLteForm"
									 theme="simple"  
									 size="15"
									 onkeypress="EvaluateText('%d', this);" 
								     onblur="this.value = NumberFormat(this.value, '0', '.', ',')"
								     cssStyle="text-align:right"
								     maxlength="20"
								     cssClass="input_form"></s:textfield>
								</td>								
							</tr>
							<tr>
								<td>										   
									RGO ACTUAL:
								</td>
								<td>
									<s:textfield name="columnRgoActual" 
									theme="simple"  
									size="15"
									onkeypress="EvaluateText('%d', this);" 
								    onblur="this.value = NumberFormat(this.value, '0', '.', ',')"
								    cssStyle="text-align:right"
								    maxlength="20"
								    cssClass="input_form">
								    </s:textfield>
								    CONTRATOS:<s:textfield name="columnContrato" 
													 theme="simple"  
													 cssClass="input_form" 
													 maxlength="3500"
													 onkeypress="ingresoLetrasNumeros(event);">
									</s:textfield>
								</td>
							</tr>
							<tr>				
											
								<td>										   
									RGO PROP BBVA BC:
								</td>								
								<td>
									<s:textfield name="columnRgoPropBbvaBc" 
									theme="simple" 
									size="15"
									onkeypress="EvaluateText('%d', this);" 
								    onblur="this.value = NumberFormat(this.value, '0', '.', ',')"
								    cssStyle="text-align:right"
								    maxlength="20"
								    cssClass="input_form"></s:textfield>
								</td>										
							</tr>
							<tr>		
								<td>										   
									RGO RIESGO:
								</td>
								<td>
									<s:textfield name="columnPropRiesgo" 
									theme="simple"  
									size="15"
									onkeypress="EvaluateText('%d', this);" 
								    onblur="this.value = NumberFormat(this.value, '0', '.', ',')"
								    cssStyle="text-align:right"
								    maxlength="20"
								    cssClass="input_form"></s:textfield>
								</td>	
							</tr>
							<tr>		
								<td>										   
									OBSERVACIONES:
								</td>
								<td>
									<s:textfield name="columnObservaciones" 
												 theme="simple" 
												 size="80"													 
												 cssClass="input_form"></s:textfield>
								</td>
							</tr>
													
								<tr id="idColumna1Edit">		
									<td>										   
										<div id="idlblColumna1"></div>
									</td>
									<td>
										<s:textfield name="columnColumna1" 
													 theme="simple" 
													 maxlength="45"													 
													 cssClass="input_form"></s:textfield>
									</td>
								</tr >
								
								<tr id="idColumna2Edit">		
									<td>										   
										<div id="idlblColumna2"></div>
									</td>
									<td>
										<s:textfield name="columnColumna2" 
													 theme="simple" 
													 maxlength="45"														 
													 cssClass="input_form"></s:textfield>
									</td>
								</tr>
								<tr id="idColumna3Edit">		
									<td>										   
										<div id="idlblColumna3"></div>
									</td>
									<td>
										<s:textfield name="columnColumna3" 
													 theme="simple" 
													 maxlength="45"													 
													 cssClass="input_form"></s:textfield>
									</td>
								</tr>
								<tr id="idColumna4Edit">		
									<td>										   
										<div id="idlblColumna4"></div>
									</td>
									<td>
										<s:textfield name="columnColumna4" 
													 theme="simple"
													 maxlength="45" 													 
													 cssClass="input_form"></s:textfield>
									</td>
								</tr>
								<tr id="idColumna5Edit">		
									<td>										   
										<div id="idlblColumna5"></div>
									</td>
									<td>
										<s:textfield name="columnColumna5" 
													 theme="simple" 
													 maxlength="45"														 
													 cssClass="input_form"></s:textfield>
									</td>
								</tr>
								<tr id="idColumna6Edit">		
									<td>										   
										<div id="idlblColumna6"></div>
									</td>
									<td>
										<s:textfield name="columnColumna6" 
													 theme="simple" 
													 maxlength="45"														 
													 cssClass="input_form"></s:textfield>
									</td>
								</tr>
								<tr id="idColumna7Edit">		
									<td>										   
										<div id="idlblColumna7"></div>
									</td>
									<td>
										<s:textfield name="columnColumna7" 
													 theme="simple" 
													 maxlength="45"														 
													 cssClass="input_form"></s:textfield>
									</td>
								</tr>
								<tr id="idColumna8Edit">		
									<td>										   
										<div id="idlblColumna8"></div>
									</td>
									<td>
										<s:textfield name="columnColumna8" 
													 theme="simple" 
													 maxlength="45"														 
													 cssClass="input_form"></s:textfield>
									</td>
								</tr>
								<tr id="idColumna9Edit">		
									<td>										   
										<div id="idlblColumna9"></div>
									</td>
									<td>
										<s:textfield name="columnColumna9" 
													 theme="simple" 
													 maxlength="45"														 
													 cssClass="input_form"></s:textfield>
									</td>
								</tr>
								<tr id="idColumna10Edit">		
									<td>										   
										<div id="idlblColumna10"></div>
									</td>
									<td>
										<s:textfield name="columnColumna10" 
													 theme="simple" 
													 maxlength="45"														 
													 cssClass="input_form"></s:textfield>
									</td>
								</tr>
								
							
							
						</table>
					</div>
					
					
					<div id="formEmpresa" style="display: none;">
						<table>
									<tr>
										<td>
											BURO:
										</td>
										<td>
											<s:textfield name="columnBureau" theme="simple"											 
											 size="15"
										     maxlength="20"
										     cssClass="input_form"
										     id="idBureau"
										     onkeypress="ingresoLetrasNumerosSingos(event);"></s:textfield>
										     
										</td>
										<td>
										<a href="javascript:obtenerBureauWS();">
											<img align="center" src="imagentabla/bbva.ActualizarAzul24.png" alt="Refresh" border="0">
										</a>																					
										</td>
										<td>
											<div id="idmsnbureau"></div>
										</td>
									</tr>

									<tr>
										<td>
											RATING:
										</td>
										<td>
											<s:textfield name="columnRating" theme="simple"											 
											 size="15"
										     maxlength="20"
										     cssClass="input_form"
										     id="idRating"
										     onkeypress="ingresoLetrasNumerosSingos(event);"></s:textfield>
										</td>
										<td colspan="2">											
										</td>
									</tr>
	
									<tr>
										<td>
											FECHA RATING:
										</td>
										<td>
											<s:textfield name="fechaRatingA" theme="simple"  											 
											 size="15"
										     maxlength="20"
										     cssClass="input_form"
										     id="idRatingFecha"></s:textfield>										     
										</td>
										<td colspan="2">											
										</td>
									</tr>
								
	
						</table>
					</div>
				</div>
				<div>
					&nbsp;&nbsp;
				</div>
				<div>
				<s:submit value="Agregar" theme="simple" cssClass="btn"></s:submit>
				<input type="button" value="Cancelar"  id="sbcancelar" class="btn"/>
				</div>
			</s:form>
		</div>
		
		
<!--		ini MCG20121119-->
		<div id="dialog-modal-detalle-saldo" title="Saldo Deudor" style="display:none" >
			<s:form action="updatesaldo" method="post" theme="simple" name="frmupdatesaldo" id="frmupdatesaldo" 
				onsubmit="SaveScrollXY(this);">
				<input name="scrollX" id="scrollX" type="hidden"  />
				<input name="scrollY" id="scrollY" type="hidden"  />
				<input name="posAnexo" id="posAnexoModalsaldo" type="hidden"  />
				<input name="editAnexo" id="editAnexosaldo" type="hidden"  />
				<input name="addcontratos" id="idaddcontratos" type="hidden"  />
				<input name="idTipoFila" id="idTipoFila" type="hidden"  />
				<div>
				  EN MILES USD &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Prestamos dentro de línea (DL): <input type="checkbox" id="cklinea" onclick="filtroCredito('.clscredito')"/>
				 
				 </div>				 
				 <div  class="my_div" id="idTabla" style="overflow:inherit">
				 </div>
				 <div>
					&nbsp;&nbsp;
				</div>
				<div>				
				<s:submit value="Aceptar" onclick="return asignarSeleccionado();" theme="simple" cssClass="btn"></s:submit>
				<input type="button" value="Cancelar"  id="sbcancelarsaldo" class="btn"/>
				</div>
			</s:form>
		</div>		
<!--		fin MCG20121119-->
	
	</div>
</div>

<div id="dialog-modal-msjElimFila" title="Resultado" style="display: none;" >
							
								<div id="divMjsElimFila" >
									
								</div>
								<div>
									&nbsp;&nbsp;
								</div>
								<input type="button" value="Aceptar" id="modalElimFilaAceptar" class="btn">
</div>

<script language="JavaScript">

var btnsverificacampoLibreAnexos= document.getElementById("sverificacampoLibreAnexos");
if(0 < <%=activoBtnValidar%>){ 	
	btnsverificacampoLibreAnexos.style.visibility  = 'visible'; // Se ve	
}else{	   
	btnsverificacampoLibreAnexos.style.display = 'none'; // No ocupa espacio
}

		 	
</script>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>
<%@page import="pe.com.bbva.iipf.util.Constantes"%>



<!--href="javascript:history.back(1)-->
<% 
	Long idPrograma  = (Long.valueOf(request.getSession().getAttribute("id_programa_session").toString()));
    String codEmpresaRC="";  
	codEmpresaRC=(String.valueOf(request.getSession().getAttribute("cod_grupoempresa_rdc_session").toString()));
	if (codEmpresaRC==null){
	codEmpresaRC=(String.valueOf(request.getSession().getAttribute("cod_central_empresa_session").toString()));
	}
		String valBDAutoguardado="";
	valBDAutoguardado = (String.valueOf(request.getSession().getAttribute("TIEMPO_AUTOGUARDADO").toString()));
	
	String dir_uploadImagen_url = GenericAction.getObjectParamtrosSession(Constantes.DIR_UPLOADFILE_URL).toString();	
	String tiempoDescargaPDF = (GenericAction.getObjectParamtrosSession(Constantes.TIEMPO_DESCARGA_PDF)==null?"60":GenericAction.getObjectParamtrosSession(Constantes.TIEMPO_DESCARGA_PDF).toString());


%>
<script type="text/javascript">
var url=null;
var sincrono=true;
var sincronizado=1;
var edicionCampos=0;
var flagGuardado=false;
var idleTime = 0;
var idExistFileTime=0;
var idExistFileInterval;
var valFijoAutoguardado= '<%=valBDAutoguardado%>';
var valTiempoDescargaPDF= '<%=tiempoDescargaPDF%>';

	function nuevoPrograma(){		
		document.forms[0].action='grupoAction.do';
		document.forms[0].submit();			
	}
	
	function consultarPrograma(){		
		document.forms[0].action='consultasModificaciones.do';
		document.forms[0].submit();			
	}
	
	function decararExcel(){		
		document.forms[0].action='generarExcel.do';
		document.forms[0].idPrograma.value = <%=idPrograma%>;
		document.forms[0].submit();			
	}
	
	function mostrarEspere(){
		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } }); 
	}
	
	function descargarPDF(){
		
			habilitarDescargaPDF();		
				var idprogramaTOperacion=<%=idPrograma%>;			
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
	
	function descargarPDFCredito(){		
		document.forms[0].action='downloadPDFCredito.do';
		document.forms[0].idPrograma.value = <%=idPrograma%>;
		document.forms[0].codEmpresaRDC.value= '<%=codEmpresaRC%>';
		document.forms[0].submit();	
		resetTiempoSession();		
	}
	
	function concluirPrograma(){		
		//document.forms[0].action='conluirPrograma.do';
		//document.forms[0].idPrograma.value = idPrograma;
		//document.forms[0].submit();	
		
		var idprogramaCierre=<%=idPrograma%>;	
			
				$("#idprogramaModal").val(idprogramaCierre);					
			 	$( "#dialog-modal-motivo-cierre" ).dialog({
									height: 400,
									width: 800,
									modal: true
								});
				$.post("loadListaMotivo.do", { idprogramaCierre:idprogramaCierre},
				   		function(data){		 
				   			var vali = data;
					   		//alert(vali);
					   		if (vali=="NO"){			   		
					   			$("#iddivmotivo").html("");	
					   			$(".input_form").val('No se puede Concluir este Programa porque falta registrar el RVGL en todas las empresas');
					   			$("#idbtnconcluir").prop("disabled","true");
					   			$("#selobservacionCierre").css({							   
								   "color":"red"
								})	   		
					   		}else if(vali=="NOROL") {		   		
					   			$("#iddivmotivo").html("");	
					   			$(".input_form").val('Usuario no facultado para culminar el Programa Financiero');
					   			$("#idbtnconcluir").prop("disabled","true");
					   			$("#selobservacionCierre").css({							   
								   "color":"red"
								})
				   			}else{		   		
				   				$("#iddivmotivo").html(data);	
				   				$(".input_form").val('');
				   			}
				   		resetTiempoSession();		   		
				});
				//$("#selMotivo").val('1');
				//$("#selMotivo").trigger('change');
				//$(".input_form").val('');
	
				
	}

	
	function confirmConcluirPrograma()   
	{  
	     return confirm('¿Se encuentra seguro de cambiar el estado del Programa a CERRADO?. De aceptar el Programa ya no podra ser editado.');    
	}
	 
    function asignarcodMotivo(){		
		var codmotivo=$("#codMotivoCierre").val();
		//alert(codmotivo);
	 	$("#idcodigoMotivoModal").val(codmotivo);
	 	return confirmConcluirPrograma() ;
	}	
	

	
	function descargarExcel2(){
			var action = "descargarExcel2.do?idPrograma=<%=idPrograma%>";
			$.ajax(
				{
					type:'POST',
					url:action,
					data: $('#form1').serialize(),
					success: function(text){$('#divApplet').html(text); resetTiempoSession();}
				}
			);		
	}
	
	function ocultarMostrarOtEmp(){
		if($('#verMas').text()=='...Ver Mas'){
			$('#verMas').html('...Ocultar'); 
						
			$.post("listarOtrasEmpresas.do", { },
		   		function(data){				
					$("#otrasEmpresas").html(data);			    	
		   	});
		   
			$("#otrasEmpresas").attr('style','display:');
		}else{
			$('#verMas').html('...Ver Mas'); 
			$("#otrasEmpresas").attr('style','display:none');
		}
	}
	
	function mostrarGuardando(){
		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando!! Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } }); 
	}
	function ocultarGuardando(){
		//	if(guardadoGeneral==0){
			setTimeout($.unblockUI, 1);
		//}
	}
	function cambiarPagina(){	 
		  if(sincronizado==1){
			  setTimeout($.unblockUI, 1);
			  sincrono=true;			  
			  if(url!=null){location.href=url.href;}
		  }
	}
	
	function guardarDatosTexto(metodo,dataGet,tipo){
         $.ajax({
                  async:sincrono,                   
                  contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                  type: 'POST',
                  url:metodo,
                  data:dataGet,
               success: function (data) {              			
               		   
                       ocultarGuardando(); 
                       resetTiempoSession();                      
                       noEditado(tipo);//revisar 
                       
                       
            }
        });    
		sincronizado=1;
    }
    
    function saveFormulariosActualizadosChange(uri){
		url=uri;
		if(sincronizado==0){
		//alert("entro");
			if(confirm('¿Desea guardar los datos antes de cambiar de opción?')){
				try{
					var vtipoGrabacion="CHANGE";
					return guardarFormulariosActualizadosPagina(uri,vtipoGrabacion);
				}
				catch (e) {
					alert("Error...");
					return false;
				}
				
			}else{
			//alert("entro2");
			mostrarEspere();
			}
		}else{
			//alert("entro3");
			mostrarEspere();
			return true;
		}
	}
	
	function timerIncrement() {
	    idleTime = idleTime + 1;
	    if (idleTime > (valFijoAutoguardado*60)) {	    
	    	if(!flagGuardado){
	    		sincronizado=1;
	    		flagGuardado=true;
	    		//document.getElementById('idSincronizacion').value=1;
	    		var vtipoGrabacion="AUTO";
	    		guardarFormulariosActualizadosPagina(null,vtipoGrabacion);
	        	
	        }
	    
	    }
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
	   			   			//alert("Puede Descargar Archivo PDF");
	   			   			$("#idbtnAceptarTOper").click();
	   			   		}else{	   			   			
	   			   		}	   			   		
	   			   });        	
	        
 	 	}else{	 		
	 		setTimeout($.unblockUI, 1);
	 		clearInterval(idExistFileInterval);
	 		habilitarDescargaPDF();
	 		alert("No se ha generado el archivo PDF...");
	 	} 
	   
	}
	//PARA ACTIVAR EL AUTOGUARDADO
	function desactivarFlag(){
		flagGuardado=false;
		sincronizado=0;
		edicionCampos=1;
	}
	
	function MENSAJEERROR_GUARDADO_MANUAL(nombreEditor){
		
		var vmnsErrorGuardadManual="NO SE PUEDE GUARDAR EL CONTENIDO DEL EDITOR: ["+nombreEditor+"]"+
		"\n Por la siguiente razones: "+
		"\n - El contenido debe tener imagenes copiadas de Word o Excel."+
		"\n - El contenido debe tener imagenes que no se subieron utilizando el boton subir Imagen del Editor."+ 
		"\n - El contenido tiene tabla no validas."+
		"\n Corrija el contenido del editir por favor..." + 
		"\n SOLO SE GUARDARA LOS DEMAS CAMBIOS MAS NO DE ESTE EDITOR.";
		
		return vmnsErrorGuardadManual;
	}
	
	function MENSAJEERROR_GUARDADO_MANUALONLYEDITOR(nombreEditor){
		
		var vmnsErrorGuardadManual="NO SE PUEDE GUARDAR EL CONTENIDO DEL EDITOR: ["+nombreEditor+"]"+
		"\n Por la siguiente razones: "+
		"\n - El contenido debe tener imagenes copiadas de Word o Excel."+
		"\n - El contenido debe tener imagenes que no se subieron utilizando el boton subir Imagen del Editor."+ 
		"\n - El contenido tiene tabla no validas."+
		"\n Corrija el contenido del editir por favor...";
		
		return vmnsErrorGuardadManual;
	}
	
	
	function validarEditor(eInnerElement){
		
		var contador=0;
		var conError=0;
	    try{
			if ( eInnerElement ){	
				var url_upload='<%=dir_uploadImagen_url%>';
 				var valblobini = eInnerElement.contentWindow.document.body.innerHTML;
 				var url_upload_ErrorImgDefault=url_upload.concat("/ErrorImagenD.png");
 				
 				var expReg=/data:([^;]*)(;base64)?,([0-9A-Za-z+/=]+)/g ; 					
				var valblobclear=valblobini.replace(expReg,url_upload_ErrorImgDefault);
 				eInnerElement.contentWindow.document.body.innerHTML="";
 				eInnerElement.contentWindow.document.body.innerHTML=valblobclear;
				
				var parafoimagen=eInnerElement.contentWindow.document.getElementsByTagName('img');	
				var contadorImg=parafoimagen.length;
				for (x=0;x<parafoimagen.length;x++){				
					var cadenaSRC=parafoimagen[x].getAttribute("src");				
					if (parafoimagen[x]!=null){
						var valorSrcset=parafoimagen[x].getAttribute("srcset");					
						if (valorSrcset!=null && valorSrcset!=''){					
							parafoimagen[x].removeAttribute("srcset");
							parafoimagen[x].setAttribute("srcset","");
						}
					}				
					var widthImg=parafoimagen[x].getAttribute("width");
					if (widthImg!=null && widthImg && widthImg>740){									
						parafoimagen[x].removeAttribute("width");					
						parafoimagen[x].style.width = '600px ';					
					}
					
					var url_upload_ErrorImg=url_upload.concat("/ErrorImagen"+x+".png");
					
					var posicion = cadenaSRC.indexOf(url_upload);	
					//alert("cadenaSRC: "+cadenaSRC+", posicion:"+posicion+", url_upload_ErrorImg: "+url_upload_ErrorImg);
					if (posicion==-1){					
						parafoimagen[x].removeAttribute("src");
						parafoimagen[x].setAttribute("src",url_upload_ErrorImg);
						conError=conError+1;						
					}else{
						contador=contador+1;
						var url_Error=url_upload.concat("/ErrorImagen");
						var posicion_error = cadenaSRC.indexOf(url_Error);						
						if (posicion_error!=-1){							
							conError=conError+1;							
						}
						
					}
					if (parafoimagen[x]!=null){
						var cadenaSRCfinal=parafoimagen[x].getAttribute("src");						
						var valorfcksave=parafoimagen[x].getAttribute("_fcksavedurl");						
						if (valorfcksave!=null && valorfcksave!=''){
						parafoimagen[x].setAttribute("_fcksavedurl",cadenaSRCfinal);
						}
					}
				}
				

				var elements = eInnerElement.contentWindow.document.getElementsByTagName('*');				
				 for (var i=0; i<elements.length; i++) {
					  var bgImagen=elements[i].style.backgroundImage ;	
					  //alert(bgImagen);
					  if (typeof(bgImagen) != "undefined" && bgImagen!=='' && bgImagen!=='none'){	
						  //alert("entro");
						  elements[i].style.backgroundImage='';
						  elements[i].style.backgroundImage="url('none')";				  
					  		
				 		}
					 }				 
			}
			return conError;
	    } catch(err) {
	        //alert("Error: " + err + ".");
	        return 0;
	    }
	}
	

	function validarHTMLRenderTiny(eInnerElement){
		
		
		var valblobini=eInnerElement.contentWindow.document.body.innerHTML;
		
	 	   $.post("validarRenderBlob.do", { valorBlob: valblobini },
			   		function(data){
			   		setTimeout($.unblockUI, 1); 
			   		if (data=="ERROR999999"){
			   			
			   			
			   			if(confirm("Error en la Validación. ¿Desea limpiar el contenido?")) {
			   				var oboby = eInnerElement.contentWindow.document.body;
			   				valblobini=CleanWordNew(oboby,true,true);
			   				eInnerElement.contentWindow.document.body.innerHTML=(valblobini);
			   				return true;
			   			}else {
			   	               return false;
			   	        } 
			   			
			   		}else{
			   			alert("Validación Satisfactoria. Grabe el Contenido.");
			   			return true;
			   		}
			   		
			   });	
		
	}
	
	
	function validarSaveManualHTMLRenderTiny(eInnerElement){
		
		var valorresul=true;
		var valblobini=eInnerElement.contentWindow.document.body.innerHTML;		
		$.ajax({
	            async:false,            
	            type: 'POST',
	            url:"validarRenderBlob.do",
	            data:{ valorBlob: valblobini},
	         success: function (data) {  
	        	 
			   		if (data=="ERROR999999"){			   			
			   			if(confirm("Error en la Validación. ¿Desea limpiar el contenido?")) {
			   				var oboby = eInnerElement.contentWindow.document.body;
			   				valblobini=CleanWordNew(oboby,true,true);
			   				eInnerElement.contentWindow.document.body.innerHTML=(valblobini);			   				
			   				valorresul= true;
			   			}else {
			   				valorresul= false;
			   	        } 
			   			
			   		}else{			   			
			   			valorresul= true;
			   		}                 
	                 
	      }
	  });		
		
		return valorresul;
	}
	
	function validarSaveAutoHTMLRenderTiny(eInnerElement){	
		   var valorresul=true;
		   var valblobini=eInnerElement.contentWindow.document.body.innerHTML;	
		   $.ajax({
	            async:false,            
	            type: 'POST',
	            url:"validarRenderBlob.do",
	            data:{ valorBlob: valblobini},
	         success: function (data) { 	        	 
			   		if (data=="ERROR999999"){
			   				valorresul= false;			   			
			   		}else{			   			
			   			valorresul= true;
			   		}                 
	                 
		      }
		  });		
		  return  valorresul;	
	}
	
	function validaGeneralHTMLRender(eInnerElement,activoAutograbar){
		var intAccion=1;
		try {
		var resultado=validarEditor(eInnerElement);			
		if (activoAutograbar==1){
			//grabar Manual			
						if( resultado>0){
							intAccion=0; //0:no graba para manual
						}else{							
							var bvalido=validarSaveManualHTMLRenderTiny(eInnerElement);							
							if (bvalido){
								intAccion=1;//1:graba para manual
							}else{
								intAccion=0;//0:no graba para manual
							}
						}
			}else{
				//grabar Automatico 
				var bvalido=validarSaveAutoHTMLRenderTiny(eInnerElement);
							if (bvalido){
								//El autoguardado solo guarda cuando todo esta correcto.
								intAccion=2; //2:graba para autoguardado
							}else{
								//el autoguardado no graba cuando hay error.
								intAccion=3; //3: no graba para autoguardado
							}
			
			}
			return intAccion;
		} catch(err) {	 
			intAccion=1;
	        return intAccion;
	    }
	}

	function getStyle(x, styleProp) {
        if (x.currentStyle){			
		var y = x.currentStyle[styleProp];			
		} else if (window.getComputedStyle) {			
		var y = document.defaultView.getComputedStyle(x, null).getPropertyValue(styleProp);			
		}
        return y;
    }
	
	$(document).ready(function () {
	
	
	
	    $("#idbtnAceptarTOper").click(function(){
    	
			 	var idPrograma = $("#idprogramaTOperModal").val();
			 	var codtipoOpe=$("#codTipoOperacion").val();
		
					document.forms[0].action='downloadPDF.do';
					document.forms[0].idPrograma.value = idPrograma;
					document.forms[0].codigoTipoOperacion.value=codtipoOpe;
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
	
	    $("#sbcancelarmotivo").click(function(){
		 	$("#dialog-modal-motivo-cierre").dialog("close");
		 });
		 
		$("#sbcancelarTipoOper").click(function(){
		 	$("#dialog-modal-tipoOperacion").dialog("close");
		 });
	
	    var idleInterval = setInterval("timerIncrement()", 1000);
	
	    $(this).mousemove(function (e) {
	        idleTime = 0;
	    });
	    $(this).keypress(function (e) {
	        idleTime = 0;
	    });
	}); 
  	
</script>



<s:form id="opcionesProgramaFinanciero" theme="simple">
 <input type="hidden" name="idPrograma" />
 <input type="hidden" name="codEmpresaRDC" />
 <input type="hidden"  name="codigoTipoOperacion"/>	
 
	
	<div class="main-menu">
						<ul id="submenuOpciones" class="sub-menu active">
							<li>
								<a href="javascript:nuevoPrograma();" >
									<img align="center" src="imagenmenu/appbar.page.solid54.png" alt="Crear Nuevo Programa" border="0">Nuevo PF
								</a>
							</li>
							<li>
								<a href="javascript:consultarPrograma();">
									<img align="center" src="imagenmenu/appbar.page.search54.png" alt="B&uacute;queda Avanzada" border="0">Busqueda Avanzada
								</a>
							</li>

							<li>
								<a href="javascript:descargarPDF();">
									<img align="center" src="imagenmenu/appbar.page.pdf54.png" alt="Descargar PDF" border="0"> Descargar PDF
								</a>
							</li>
							<li>
								<a href="javascript:concluirPrograma();">
									<img align="center" src="imagenmenu/appbar.page.check54.png" alt="Concluir Programa" border="0"> Concluir PF
								</a>
							</li>
							<li>
								<a href="javascript:descargarPDFCredito();">
									<img align="center" src="imagenmenu/appbar.book.list54.png" alt="Descargar Reporte de Credito PDF" border="0"> Reporte de Credito
								</a>
							</li>
							<li style="width:160px ;">
<!-- 								<a href="javascript:descargarExcel2();"> -->
<!-- 									<img align="center" src="imagenmenu/appbar.office.excel54.png" alt="Descargar Excel" border="0">Descargar Excel -->
<!-- 								</a> -->
							</li>
							<li style="align:right ;font-size: 12pt;font-weight: bold;color:#ffffff;">
								<img align="center" src="imagenmenu/appbar.corner54.png" alt="Descargar Reporte de Credito PDF" border="0">
								<%String numerosolicitud = GenericAction.getObjectSession(Constantes.NUM_SOLICITUD_SESSION).toString();%>
								Nº Solicitud : 	<%=numerosolicitud%>											
								
							</li>

						</ul>			
						
	

	</div>

	<div id="divApplet">
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
<!-- 					<input type="button" value="Generar PDF" id="idbtnAceptarTOperHILO" class="btn"> -->
					<input type="button" value="Descargar PDF" id="idbtnAceptarTOper" class="btn">
					<input type="button" value="Cancelar"  id="sbcancelarTipoOper" class="btn"/>
				</div>
<!-- 				<div id="divdesvargapdf" style="display:none;">					 -->
<!-- 					<input type="button" value="Descargar PDF" id="idbtnAceptarTOper" class="btn"> -->
<!-- 				</div> -->
					
				
		
		</div>
	
</s:form>

<!--		ini MCG20121119-->
		<div id="dialog-modal-motivo-cierre" title="Motivo de Cierre" style="display:none" >
			<s:form action="conluirPrograma" method="post" theme="simple" name="frmcerrarPrograma" id="frmcerrarPrograma" >
				<input name="scrollX" id="scrollX" type="hidden"  />
				<input name="scrollY" id="scrollY" type="hidden"  />
				<input name="programaCierre" id="idprogramaModal" type="hidden"  />	
				<input name="codigoMotivoCierre" id="idcodigoMotivoModal" type="hidden"  />				
			
				 <div  class="my_div" id="idTabMotivoCierre" style="overflow:inherit">
				 	<div>
				  	MOTIVOS:
				 	</div>				 
					<div id="iddivmotivo" style="display:" >					 
					</div>
					<div>
				  	OBSERVACION:
				 	</div>
				    <div id="divObservacion">				       									 
						<s:textarea name="observacionCierre" id="selobservacionCierre"
											rows="15" 
											cols="122"
											cssClass="input_form"											
											theme="simple">
											</s:textarea>
														 
				    </div>				 
				 </div>
				 <div>
					&nbsp;&nbsp;
				</div>
				<div>				
				<s:submit id="idbtnconcluir" value="Aceptar" onclick="return asignarcodMotivo();" theme="simple" cssClass="btn"></s:submit>
				<input type="button" value="Cancelar"  id="sbcancelarmotivo" class="btn"/>
				</div>
			</s:form>
		</div>
		

				
				
		
<!--		fin MCG20121119-->


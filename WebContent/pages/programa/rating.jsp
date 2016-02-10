<%@taglib prefix="s" uri="/struts-tags" %>  
<%@taglib uri="http://java.fckeditor.net" prefix="FCK" %>


<%@page import="pe.com.stefanini.core.action.GenericAction"%>
<%@page import="pe.com.bbva.iipf.util.Constantes"%>

<%
String tipoEmpresa = GenericAction.getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION)==null?"":GenericAction.getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
String activoBtnValidar = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_BTN_VALIDAR).toString();
String activoValidarEditor = GenericAction.getObjectParamtrosSession(Constantes.ACTIVO_VALIDAR_EDITOR).toString();

%>
<%
	int anio = Integer.parseInt(GenericAction.getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
%>


<script language="JavaScript">
var dirurl='';
var vtextarea='';
var valRating=0;
var VALORACION_RATING=1;
var sincrono=true;
var VALORMANUAL=1;
	
	function noEditado(campo){
		if(campo==VALORACION_RATING){
			valRating=0;
		}
		if(valRating==0){
			sincronizado=1;
		}
		return false;
	}
	
	function ocultarGuardando(){
		if(guardadoGeneral==0){
			setTimeout($.unblockUI, 1);
		}
	}
	
	function editado(campo){
		if(campo==VALORACION_RATING){
			valRating=1;sincronizado=0;
		}
		
		return false;
	}
	
	function mostrarGuardando(){
		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br> Guardando Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } }); 
	}
	
	//function mostrarEspere(){
	//	$.blockUI({message:  '<h3>Espere por favor...</h3>',
	//		   overlayCSS: { backgroundColor: '#0174DF' } }); 
	//}
	
	function onloadRating(codigo,flag){
	//alert(codigo);
		document.forms["formRating"].action='initRating.do';
		document.forms["formRating"].codempresagrupo.value =codigo; 	
		document.forms["formRating"].flagChangeEmpresa.value =flag; 		
		document.forms["formRating"].submit();	
	}
	
	
	$(window).load(function() { setTimeout($.unblockUI, 1);});
	
   $(document).ready(function() {
	   	 //When page loads...
		 $(".tab_content").hide(); //Hide all content
		 $("#li3").addClass("active").show(); //Activate first tab
		 $("#tab1").show(); //Show first tab content
   		 $("#formRating").validationEngine();

  	   $('#stextareaValRating').wysiwyg({			
			rmUnusedControls: true,			
			controls: {				
			bold: { visible : true },				
			html: { visible : true },				
			insertOrderedList: { visible : true },				
			removeFormat: { visible : true }			
			}		
		});

	   $('#stextareaValRating').wysiwyg("addControl", 
		   "UploadFile", {				
		   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
		   tooltip: 'Upload',tags: ['Upload'],				
		   exec:  function() { 		   		
		   		vtextarea='stextareaValRating'; 
		   		newWindowUploadFile(vtextarea);  }
	       }
	    );
   		 
   		 $("#bverValRating").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divValRating").attr("style","");
   			$("#ssaveValRating").prop("disabled","");
   			$("#scleanValRating").prop("disabled","");
   			
   		
	   		var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var idgrupo = $("input[name=id_grupo]").val();
	   		var codempresa= $("#codigoEmpresa").val();
	   		
	   		
   			if(tipoempresa == '2' ){ 
			   		$.post("consultarProgramaBlob.do", { campoBlob: "valoracionRating" },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareaValRating').wysiwyg('setContent', data);
				   		var iframe = document.getElementById("ValRating___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;
							resetTiempoSession();					
						}
						//editado(VALORACION_RATING);
				   });
		   
		   }else{
			     if (codempresa==idgrupo){
			     		$.post("consultarProgramaBlob.do", { campoBlob: "valoracionRating" },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareaValRating').wysiwyg('setContent', data);
				   		var iframe = document.getElementById("ValRating___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;
							resetTiempoSession();					
						}
						//editado(VALORACION_RATING);
				   });
			     
		   		 }else{
		   		 		   		 	  
		   		 	   $.post("consultarRatingBlob.do", { campoBlob: "valoracionRating", codEmpresa:codempresa  },
				   		function(data){
				   		setTimeout($.unblockUI, 1);  
				   		//$('#stextareaValRating').wysiwyg('setContent', data);
				   		var iframe = document.getElementById("ValRating___Frame");				
						var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
						var eInnerElement = oCell.firstChild;				
						if ( eInnerElement ){					
							eInnerElement.contentWindow.document.body.innerHTML = data;	
							resetTiempoSession();				
						}
						editado(VALORACION_RATING);
				   });
		   		
		   		}
		   }
		   
		});
		$("#ssaveValRating").click(function () {
			/*$.blockUI({message:  '<h3>Guardando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } }); */
			mostrarGuardando();
			var tipoempresa = $("input[name=tipo_empresa]").val();
	   		var idgrupo = $("input[name=id_grupo]").val();	   		
	   		var codempresa= $("#codigoEmpresa").val();	
	   		
			var iframe = document.getElementById("ValRating___Frame");
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
				
	
					if(tipoempresa == '2' ){			
						var dataGet={campoBlob:'valoracionRating',valorBlob:valblob};
						guardarDatosTexto("saveProgramaBlob.do",dataGet,VALORACION_RATING);
					
					}else{ 				    						
					      if (codempresa==idgrupo){
					      			var dataGet={campoBlob:'valoracionRating',valorBlob:valblob};
									guardarDatosTexto("saveProgramaBlob.do",dataGet,VALORACION_RATING);
					       }else{
									var dataGet={campoBlob:'valoracionRating',codEmpresa:codempresa,valorBlob:valblob};							
									guardarDatosTexto("saveRatingBlob.do",dataGet,VALORACION_RATING);
						   }					   			
					}	

			}else{
				if ( eInnerElement ){				
					//validarEditor(eInnerElement);
					accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
					valblob = eInnerElement.contentWindow.document.body.innerHTML;
				}		
				
				if (accionGraba==1 || accionGraba==2){
					if(tipoempresa == '2' ){			
						var dataGet={campoBlob:'valoracionRating',valorBlob:valblob};
						guardarDatosTexto("saveProgramaBlob.do",dataGet,VALORACION_RATING);
					
					}else{ 				    						
					      if (codempresa==idgrupo){
					      			var dataGet={campoBlob:'valoracionRating',valorBlob:valblob};
									guardarDatosTexto("saveProgramaBlob.do",dataGet,VALORACION_RATING);
					       }else{
									var dataGet={campoBlob:'valoracionRating',codEmpresa:codempresa,valorBlob:valblob};							
									guardarDatosTexto("saveRatingBlob.do",dataGet,VALORACION_RATING);
						   }					   			
					}	
					
				}else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("VALORACION RATING"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}
		});
		$("#scleanValRating").click(function () { 
			//$('#stextareaValRating').wysiwyg('setContent', '');
			var iframe = document.getElementById("ValRating___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}  
		});
		
		
		$("#sverificaValRating").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("ValRating___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
			}  
		});
		
		
		$('select[name=codigoEmpresa]').change(function () {
		   //mostrarEspere();
	 	   var codempresa1= $("#codigoEmpresa").val();	
	 	   $.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } });	
	  		 onloadRating(codempresa1,'C');    			
  			
	    });
		
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
		if(editorInstance.Name=='ValRating'){valRating=1; flagGuardado=false;sincronizado=0;idleTime=0}
	    
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
		if(valRating==1){$("#ssaveValRating").click();}
		sincronizado=1;
		setTimeout(cerrarmsnAutoGuardado, 8000);
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
		<s:form id="formRating" theme="simple">
				<input name="scrollX" id="scrollX" type="hidden"  />
			    <input name="scrollY" id="scrollY" type="hidden"  />
			    <input name="codempresagrupo" id="codempresagrupo" type="hidden"  />
				<input name="flagChangeEmpresa" id="flagChangeEmpresa" type="hidden"  />
				<input type="hidden" name ="tipo_empresa" value="<%=tipo_empresa%>"/>
				<input type="hidden" name ="id_grupo" value="<%=id_grupo%>"/>
				
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

				<table class="ln_formatos" cellspacing="0" width="100%">
					<tr>
						<td>
							<s:submit onclick="mostrarEspere();"  theme="simple" action="actualizarRating" id="bversitEcono"  value="Actualizar Rating" cssClass="btn"/>
						</td>
						<td colspan="2" align="right">
										<div id="idmsnAutoGuardado"></div>
						</td>
						<td colspan="2" align="right">
							<s:property value="mensajeWS" />
						</td>
				</tr>
				</table>
				<div  class="my_div">
				<table cellspacing="0"  class="ln_formatos" width="80%">
					<tr>				
				 		<td>
						<table class="ui-widget ui-widget-content" width="80%">
							<tr class="ui-widget-header" >
								<th colspan="4" align="center">
									RATING
								</th>
							</tr>
							<s:iterator var="p" value="listaRating" id="rating" status="rowstatus">
								<s:if test="descripcion==null"><tr class="ui-widget-header ">
									<td align="center" style="font-weight: bold;">
									   <s:property   value="descripcion" ></s:property>
									</td>
									<td align="center">
									   <s:property value="totalAnio2" ></s:property>
									</td>
									<td align="center">
									   <s:property  value="totalAnio1" ></s:property>
									</td>
									<td align="center">
									   <s:property  value="totalAnioActual" ></s:property>
									</td>
								</tr>
								</s:if>
								<s:else><tr>
										<td align="left"  style="font-weight: bold;">
										   <s:property   value="descripcion"></s:property>
										</td>
										<td align="right">
										   <s:property value="totalAnio2" ></s:property>
										</td>
										<td align="right">
										   <s:property  value="totalAnio1" ></s:property>
										</td>
										<td align="right">
										   <s:property  value="totalAnioActual" ></s:property>
										</td>
									</tr>
								</s:else>
									
							</s:iterator>
						</table>
						</td>
					</tr>
				</table>
			</div>
			
			<table  cellspacing="0" class="ln_formatos">
					<tr>
						<td>
							<label style="font-size: 10pt;font-weight: bold;">Valoraci&oacute;n Rating</label>
						</td>	
						<td>
							<input type="button" id="bverValRating"  value="Ver" class="btn"/>
						</td>	
						<td>
							<input type="button" value="Guardar" id="ssaveValRating" disabled="disabled" class="btn"/>
						</td>	
						<td>
							<input type="button" value="Limpiar" id="scleanValRating" disabled="disabled" class="btn"/>
						</td>
						<td>
							<input type="button" value="Validar" id="sverificaValRating"  class="btn"/>
						</td>	
						
					</tr>
				</table>
				<div id="divValRating" style="display:none;"> 
						<FCK:editor instanceName="ValRating" height="250px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
				</div>			
				
				
		</s:form>
	</div>
</div>
<script language="JavaScript">

var btnsverificaValRating= document.getElementById("sverificaValRating");
if(0 < <%=activoBtnValidar%>){ 	
	btnsverificaValRating.style.visibility  = 'visible'; // Se ve	
}else{	   
	btnsverificaValRating.style.display = 'none'; // No ocupa espacio
}

		 	
</script>
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
var VALORMANUAL=1;

var analisisSectorial=0;

var ANALISIS_SECTORIAL=1;

	function descargarExcel(cod,ext, nom){		
		document.forms["formAnalisisSectorial"].action='dodownload.do';
		document.forms["formAnalisisSectorial"].codigoArchivo.value =cod; 
		document.forms["formAnalisisSectorial"].extension.value=ext;
		document.forms["formAnalisisSectorial"].nombreArchivo.value=nom;
		document.forms["formAnalisisSectorial"].submit();
		resetTiempoSession();			
	}
	
	function eliminar(id,ext,nomarch){
		document.forms["formAnalisisSectorial"].action='eliminarArchiAnalisiSect.do';
		document.forms["formAnalisisSectorial"].codigoArchivo.value =id; 
		document.forms["formAnalisisSectorial"].extension.value =ext;
		document.forms["formAnalisisSectorial"].nombreArchivo.value =nomarch;
		document.forms["formAnalisisSectorial"].submit();	
	}
	
    function editado(campo){
    	if(campo==ANALISIS_SECTORIAL){
    		analisisSectorial=1;sincronizado=0;
    	}
    	return false;
    }

	function FCKeditor_OnComplete( editorInstance )
       {
               if (document.all) {        // If Internet Explorer.
                     editorInstance.EditorDocument.attachEvent("onkeydown", function(event){desactivarFlagEditor(editorInstance);} ) ;
               } else {                // If Gecko.
                     editorInstance.EditorDocument.addEventListener( 'keypress', function(event){desactivarFlagEditor(editorInstance);}, true) ;
           }         
       }
	
	function desactivarFlagEditor(editorInstance)
	{
		if(editorInstance.Name=='analisis'){analisisSectorial=1; flagGuardado=false;sincronizado=0;idleTime=0}
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
	   	//guardadoGeneral=1;
	   	sincrono=false;
		if(analisisSectorial==1){$("#ssaveanalisis").click();}
		sincronizado = 1;
		setTimeout(cerrarmsnAutoGuardado, 8000);
		cambiarPagina();
		return false;
    }
     
    
    function noEditado(campo){
		if(campo==ANALISIS_SECTORIAL){
			analisisSectorial=0;
		}
		if(analisisSectorial==0){
			sincronizado=1;
		}
		return false;
	}
	
	function ocultarGuardando(){
		if(guardadoGeneral==0){
			setTimeout($.unblockUI, 1);
		}
	}
    $(window).load(function() { setTimeout($.unblockUI, 1);});
   $(document).ready(function() {

	   $('#stextareaanalisis').wysiwyg({			
			rmUnusedControls: true,			
			controls: {				
			bold: { visible : true },				
			html: { visible : true },				
			insertOrderedList: { visible : true },				
			removeFormat: { visible : true }			
			}		
		});

	   $('#stextareaanalisis').wysiwyg("addControl", 
		   "UploadFile", {				
		   groupIndex: 6,icon: '/ProgramaFinanciero/image/Attach.png',				
		   tooltip: 'Upload',tags: ['Upload'],				
		   exec:  function() { 		   		
		   		vtextarea='stextareaanalisis'; 
		   		newWindowUploadFile(vtextarea);  }
	       }
	    );

	  

	   	 //When page loads...
		 $(".tab_content").hide(); //Hide all content
		 $("#li4").addClass("active").show(); //Activate first tab
		 $("#tab1").show(); //Show first tab content


   		$("#bveranalisis").click(function () { 
   			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Cargando Espere por favor...</h3>', 
   					  overlayCSS: { backgroundColor: '#0174DF' } }); 
   			$("#divanalisissectorial").attr("style","");
   			$("#ssaveanalisis").prop("disabled","");
   			$("#scleananalisis").prop("disabled","");
	   		$.post("consultarProgramaBlob.do", { campoBlob: "espacioLibreAS" },
		   		function(data){
		   		setTimeout($.unblockUI, 1);  
		   		//$('#stextareaanalisis').wysiwyg('setContent', data);
		   		var iframe = document.getElementById("analisis___Frame");				
				var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
				var eInnerElement = oCell.firstChild;				
				if ( eInnerElement ){					
					eInnerElement.contentWindow.document.body.innerHTML = data;	
					resetTiempoSession();				
				}  	  
				//editado(ANALISIS_SECTORIAL);
		   });
		});
		
		 $("#ssaveanalisis").click(function () { 
            $.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Guardando Espere por favor...</h3>',
                       overlayCSS: { backgroundColor: '#0174DF' } }); 
            var iframe = document.getElementById("analisis___Frame");
            var oCell =    iframe.contentWindow.document.getElementById("xEditingArea");
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
	            var dataGet={campoBlob:'espacioLibreAS',valorBlob:valblob};            
	            guardarDatosTexto("saveProgramaBlob.do",dataGet,ANALISIS_SECTORIAL);
			}else{
			
	            if ( eInnerElement ){
	            	//validarEditor(eInnerElement);
	            	accionGraba=validaGeneralHTMLRender(eInnerElement,activoTipoGuardado);
	                valblob = eInnerElement.contentWindow.document.body.innerHTML;
	            }
	            if (accionGraba==1 || accionGraba==2){
		            var dataGet={campoBlob:'espacioLibreAS',valorBlob:valblob};            
		            guardarDatosTexto("saveProgramaBlob.do",dataGet,ANALISIS_SECTORIAL);
		            
	            }else if (accionGraba==0){
					alert(MENSAJEERROR_GUARDADO_MANUALONLYEDITOR("ANALISIS SECTORIAL"));
					setTimeout($.unblockUI, 1);
				}else if (accionGraba==3){
					setTimeout($.unblockUI, 1);
				}
			}
            
        });
        
		$("#scleananalisis").click(function () { 
			//$('#stextareaanalisis').wysiwyg('setContent', '');
			var iframe = document.getElementById("analisis___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				eInnerElement.contentWindow.document.body.innerHTML='';
			}  
		});


		$("#sverificaanalisis").click(function () { 
			
			$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Validando Espere por favor...</h3>',
					   overlayCSS: { backgroundColor: '#0174DF' } });			
			var iframe = document.getElementById("analisis___Frame");
			var oCell = iframe.contentWindow.document.getElementById("xEditingArea");
			var eInnerElement = oCell.firstChild;
			if ( eInnerElement ){
				validarHTMLRenderTiny(eInnerElement);				
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

<s:actionerror />
<s:fielderror />

<s:form id="formAnalisisSectorial"
 	name="formAnalisisSectorial"
	action="analisisectorialAction" method="post"
	enctype="multipart/form-data" theme="simple">
	<input name="scrollX" id="scrollX" type="hidden"  />
	<input name="scrollY" id="scrollY" type="hidden"  />
	<input type="hidden" name="codigoArchivo" />
	<input type="hidden" name="extension" />
	<input type="hidden" name="nombreArchivo" />
	<table border=0  width="100%">
		<tr>
			<td class="bk_tabs">

			<table class="ln_formatos" cellspacing="0">
				<tr>
					<td><s:file name="userArchivo" label="Ruta Archivo" size="70"
						cssClass="btn"
						theme="simple" /></td>
					<td><s:submit value="Grabar" action="dosave"
						cssClass="btn"
						theme="simple" /> 
						<s:submit value="Buscar" action="dobuscar"
						cssClass="btn"
						theme="simple" />
					</td>
					<td colspan="2" align="right">
										<div id="idmsnAutoGuardado"></div>
					</td>
				</tr>
			</table>
			<br />
			
			<table class="ln_formatos" cellspacing="0">
				<tr>
					<td><label style="font-size: 10pt; font-weight: bold;">Comentario:</label>
					</td>
					<td><input type="button" id="bveranalisis" value="Ver"
						class="btn" /> 
						<input	type="button" value="Guardar" id="ssaveanalisis"
						disabled="disabled"
						class="btn"/> 
						<input	type="button" value="Limpiar" id="scleananalisis"
						disabled="disabled"
						class="btn" />
						<input	type="button" value="Validar" id="sverificaanalisis" class="btn" />
						</td>
				</tr>
			</table>
			
			<div id="divanalisissectorial" style="display: none;">
						<FCK:editor instanceName="analisis" height="300px">
							<jsp:attribute name="value">&nbsp;
							</jsp:attribute>									
							
						</FCK:editor>
			</div>
			<br />
			<br />
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
								<th>Nombre Archivo</th>
								<th>usuario</th>
								<th>Codigo Archivo</th>
								<th>Extension</th>
								<th>Archivo</th>
								<th>...</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="listAnalisisSectorial" var="analisisSectorial"
								status="userStatus">
								<tr>
									<td><s:date name="fechaCarga" format="dd/MM/yyyy" /></td>
									<td><s:property value="nombreArchivo" /></td>
									<td><s:property value="usuario" /></td>
									<td><s:property value="id" /></td>
									<td><s:property value="extencionArchivo" /></td>
									<td align="center"><a
										href="javascript:descargarExcel('<s:property value="id" />','<s:property value="extencionArchivo" />','<s:property value="nombreArchivo" />');">
									<img src="imagentabla/bbva.PdfAzul24.png" alt="download" border="0">  </a></td>
									<td align="center">
										<a href="javascript:eliminar('<s:property value="id" />','<s:property value="extencionArchivo" />','<s:property value="nombreArchivo" />');">
										<img src="imagentabla/bbva.EliminarAzul24.png" alt="download" border="0" onclick="return confirmDelete();">
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

</s:form></div>
</div>
<script language="JavaScript">

var btnsverificaanalisis= document.getElementById("sverificaanalisis");
if(0 < <%=activoBtnValidar%>){ 	
	btnsverificaanalisis.style.visibility  = 'visible'; // Se ve	
}else{	   
	btnsverificaanalisis.style.display = 'none'; // No ocupa espacio
}

		 	
</script>

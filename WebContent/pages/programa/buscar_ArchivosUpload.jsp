<%@taglib prefix="s" uri="/struts-tags" %>  
<%@ page import="java.io.*"%>
<%@page import="pe.com.bbva.iipf.util.Constantes"%>
<%@page import="java.util.List"%>
<%@page import="pe.com.bbva.iipf.pf.model.Parametro"%>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>

<link href="/ProgramaFinanciero/css/jqueryFileTree.css" rel="stylesheet" type="text/css" media="screen" />
<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/table.css"/>	
<script src="/ProgramaFinanciero/js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/ProgramaFinanciero/js/jquery-migrate-1.2.1.min.js"></script>
<script src="/ProgramaFinanciero/js/jquery.easing.1.3.js" type="text/javascript"></script>
<script src="/ProgramaFinanciero/js/jqueryFileTree.js" type="text/javascript"></script>

<div>
<% 
	String dir_buscarAvan_analisisSectorial = GenericAction.getObjectParamtrosSession(Constantes.DIR_BUSCAR_ANALISISSECTORIAL).toString();
 %>
<script type="text/javascript">			
	$(document).ready( function() {				
		$('#fileTreeDemo_1').fileTree({ root: '<%=dir_buscarAvan_analisisSectorial%>', script: '/ProgramaFinanciero/pages/programa/jqueryFileTree.jsp' }, function(file) { 
			//alert(file);
			var code=file; //ya se tiene el objeto select
               //llama al servlet con el parametro seleccionado
               //$("#directoryCode").load("dodirectoryFile.do", {directoryCode:code})
				$( "#directoryCode" ).html('');
		   		$.post("dodirectoryFile.do", 
			   			   {directoryCode:code},
					   		function(data){	   	
					   			$('#directoryCode').html(data);
		   				}); 

               
              			
		});		
		
	});

	function donwload1(ruta,file){
		document.frombuscar.action = "doDownloadFile.do";
		document.frombuscar.ruta.value=ruta;
		document.frombuscar.fileName.value=file;				
		document.frombuscar.submit();				
	}

	function adjuntar(ruta,file){
		document.frombuscar.action = "doadjuntar";
		document.frombuscar.dirbuscar.value=ruta;
		document.frombuscar.filebuscar.value=file;				
		document.frombuscar.submit();				
		alert(ruta);
		}
	function buscararchivo() {
		document.frombuscar.action = "dobuscarArchivo.do";
		//alert("buscararchivo");
		document.frombuscar.submit();
		
		}
    function showFiles(){
        //obtiene los objetos productCode, y obtiene el valor del objeto
        var code=$("#directoryCodeSelect").val(); //ya se tiene el objeto select
        //llama al servlet con el parametro seleccionado
        //$("#file").load("doFile.do", {fileCode:code})

		$( "#file" ).html('');
   		$.post("doFile.do", 
	   			   {fileCode:code},
			   		function(data){	   	
			   			$('#file').html(data);
   				}); 
        
    }
</script>			


	<h2><legend>Busqueda de Archivos - Analisis Sectorial</legend></h2>				
	
	<div class="examplex">
		Directorio:
		<div id="fileTreeDemo_1" class="demox"></div>
	</div>
		
	<div class="examplex">
		Busqueda:
	<div >		
		<s:form  name= "frombuscar" method="post">							
			<table class="ln_formatos" cellspacing="0" width="100%">
				<tr><td>
				<label style="font-size: 10pt;font-weight: bold;">Directorio</label>
				</td>
			    </tr>			   
			</table>		
			<div id="directoryCode" name="divdirectoryCode"></div>				
			<table>
				<tr>
				<s:textfield name="dirbuscar" Style="display:none"/>		
			    <s:textfield name="filebuscar" Style="display:none"/>
			    </tr>
			     <tr>           
			    <input type="hidden"  name="ruta"></input> 
			    <input type="hidden" name="fileName"></input>
				</tr>
			</table>  
			<br/>
			<table class="ln_formatos" cellspacing="0" width="100%">
				<tr>
					<td>
					<label style="font-size: 10pt;font-weight: bold;">Listado de Archivos buscados:</label>
					</td>
				</tr>
			</table>			 
			 <div class="my_div">
			 	<div id="file" name="divfile"></div>
			 </div>  		
			<br/>	
			<br/>
			<br/>			
			<div class="my_div">
				<table class="ln_formatos" width="100%" cellspacing="0">
				<tr>
					<td>
					<label style="font-size: 10pt;font-weight: bold;">Busqueda Avanzada:</label>
					</td>
				</tr>
				<tr><td>
				<s:textfield size="70" label="Buscar Archivo" name="archivobuscar" theme="simple" maxlength="50"/>
				<s:submit value="Buscar" onClick="buscararchivo()" 
				cssClass="btn"
				theme="simple"/></td>		
				</tr>
				<tr>
					<td>	
						<table class="ui-widget ui-widget-content">	
							<tr class="ui-widget-header ">
					      		<th>Tipo</th>	        		 
					      		<th>Nombre</th> 
					      		<th style="display: none;">Directorio</th> 
					      		<th>Accion</th>
					      		<th>Adjuntar</th>
					     	</tr>				
						<s:iterator value="listarchivo" var="lisarchivo" status="userStatus">
							<tr>
										
								<td><s:property value="tipo" /></td>						
								<td width="300px"><s:property value="nombre" /></td>	
								<td style="display: none;"><s:property value="directory"/></td>												
								<td><img src="image/pdf.png" alt="download">																
								<a
										href="
											<s:url action="doDownloadFile">												
												<s:param name="ruta" value="directory" />
												<s:param name="fileName" value="nombre" />																								
											</s:url>
											">
									Abrir </a>
								</td>								
								<td><img src="image/Attach.png" alt="download"> <a
										href="
											<s:url action="doadjuntar">												
												<s:param name="dirbuscar" value="directory" />
												<s:param name="filebuscar" value="nombre" />																								
											</s:url>
											">
									Adjuntar </a></td>
							</tr>			
						</s:iterator>
						</table>
				</td>
				</tr>
			</table>	
			<table class="ln_formatos" cellspacing="0">
				<tr>					
					<td>
						<s:submit value="Retornar" action="doRetornarAS"
						cssClass="btn"
						theme="simple" />
					</td>
				</tr>
			</table>				
			</div>
		</s:form >
</div>
</div>
</div>
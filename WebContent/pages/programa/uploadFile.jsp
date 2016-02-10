<%@taglib prefix="s" uri="/struts-tags"%>

<head>
<title>Upload Archivo</title>
</head>

<script>


$(document).ready(function() {
	
	$("#idenviarURL").click( function (){ 			
		var valor = $('#idvalorURl').val();		
		dirurl = valor + '';				
		window.returnValue = dirurl;		
		self.close();
	});

}); 
</Script>
<div>
<s:actionerror />
<s:fielderror />
    <s:form action="doUploadFile" method="post" enctype="multipart/form-data" theme="simple">     						
							
				<table class="ln_formatos" cellspacing="0">
					<tr>
						<td><s:file name="file" label="Ruta Archivo" size="42"
						cssClass="btn"
						theme="simple" />
						</td>
						<td>
						<s:submit value="Subir Imagen" action="doUploadFile"
						cssClass="btn"
						theme="simple" />	
						</td>
					</tr>
					<tr>
						<td colspan="2">URL:<s:textfield size="70" id="iddireccionURL" name="direccionURL" readonly="true" ></s:textfield></td>
						
					</tr>
					<tr>
						<td colspan="2"><s:textfield size="70" id="idvalorURl" name="htmlURL" style="display:none;" readonly="true" ></s:textfield></td>
						
					</tr>
					<tr>
						<td><input type="button" id="idenviarURL" value="Insertar Imagen"					 
							class="btn" /></td>
						<td></td>
					</tr>
					<tr>
						<td colspan="2">
						<img style="float: left; margin: 5px; width: 150px; height: 120px; border: 1px solid rgb(192, 192, 192);" alt="Preview" src="<s:property value="direccionURL"/>"/>
						</td>
						
					</tr>
				</table>			
					<br/>
					<br/>	
					<br/>	
					<br/>	
					<br/>	
					<br/>	
					<br/>	
					<br/>
					<br/>	
						
    </s:form>


</div>

	

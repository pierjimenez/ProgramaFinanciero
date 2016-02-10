<%@taglib prefix="s" uri="/struts-tags"%>
<%@page import="pe.com.bbva.iipf.util.Constantes"%>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>

<%
//String tipoEmpresa = request.getParameter("tipoEmpresa")==null?"":request.getParameter("tipoEmpresa").toString();
 String tipoEmpresa = GenericAction.getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
 
 %>
<script language="JavaScript">	
	function mostrarEspere(){
		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } }); 
	}
	function mostrarActualizando(){
		if ($("#editgrupoform").validationEngine('validate')) {	
		$.blockUI({message:  '<h3><img src="/ProgramaFinanciero/images/spinner.gif"/> </br>Actualizando. Espere por favor...</h3>',
			   overlayCSS: { backgroundColor: '#0174DF' } }); 			   
		}
	}
	function buscarGrupoEmpresaEdit()
	{
		if(jQuery.trim($("#tfcodigoEmpresaGrupo").val())!=''){
			mostrarEspere();
			document.forms[0].action='buscarGrupoEmpresaEdit.do';
			document.forms[0].submit();
		}
	}
	
	function validarSeleccionEmpresa(){

		$("#idcopiarsinDataHidden").val('3');
		var tipoCopia=$("#idtipoCopia").val();
		var codEmpPrin=$("#idcodEmpPrinHidden").val();	
		var cont=0;
		//alert(tipoCopia);
		//alert(codEmpPrin);		   
		if (tipoCopia=='2'){
			var codempresaSelectPrin= $("#selEmpresasPrin").val();
			//alert(codempresaSelectPrin);
			if (codEmpPrin==codempresaSelectPrin){
				cont=cont+1;
			}		
			
			var codEmpSec = '';
			$('#selListaEmpresasSecundarias option:selected').each(function(){
				var codEmpSec = $(this).val(); 
				//alert(codEmpSec);
				if (codEmpPrin==codEmpSec){
					cont=cont+1;
				}
			});
			$('#selListaEmpresasAnexas option:selected').each(function(){
				var codEmpAnexo = $(this).val(); 
				//alert(codEmpAnexo);
				if (codEmpPrin==codEmpAnexo){
					cont=cont+1;
				}
			});
			
			if (cont==0) {
				if (confirm('Empresa que se realizó la copia no se encuentra seleccionada. ¿Desea Continuar de todas maneras?')){	
					$("#idcopiarsinDataHidden").val('1');
					return true;
				}else{
					$("#idcopiarsinDataHidden").val('2');
					return false;
				}
			}
			
			   
		}
		return true;
	}
	
	
	
	$(window).load(function() { setTimeout($.unblockUI, 1);});	
   $(document).ready(function() {
   		$("#editgrupoform").validationEngine();
		//When page loads...
		 if('<%=tipoEmpresa%>'=='2'){
		     	$('#textcodigoempresagrupo').html('');	
		     	$('#textnombreempresagrupo').html('Nombre Empresa:');	
		     	$('#divtipodoc').attr("style","display:");
		  }else{
		     	$('#textcodigoempresagrupo').html('C&oacute;digo Grupo');
		     	$('#textnombreempresagrupo').html('Nombre Grupo:');
				$("#tfcodigoEmpresaGrupo").prop("maxlength",8);
				$('#divtipodoc').attr("style","display:none");
		  }
		
		$('select[name=tipoEmpresa]').change(function () {
		     if($(this).val()=='2'){
		     	$('#textcodigoempresagrupo').html('');	
		     	$('#textnombreempresagrupo').html('Nombre Empresa:');	
		     	$('#divtipodoc').attr("style","display:");
		     	$("select[name=tipoDocBusqueda]").val("2");
		     	$("#tfcodigoEmpresaGrupo").prop("maxlength",11);
		     }else{
		     	$('#textcodigoempresagrupo').html('C&oacute;digo Grupo');
		     	$('#textnombreempresagrupo').html('Nombre Grupo:');	
		     	$('#divtipodoc').attr("style","display:none");
		     	$("#tfcodigoEmpresaGrupo").prop("maxlength",8);
		     }
		     $("#tfcodigoEmpresaGrupo").val("");
		     $("#tfnombre").val("");
		     $('#divempresas').attr("style","display:");
		     $('#divempresas').html('');
	    });
	    $("select[name=tipoDocBusqueda]").change(function(){
	    	if( $(this).val()=="2"){
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",11);
	    	}else{
	    		$("#tfcodigoEmpresaGrupo").prop("maxlength",8);
	    	}
	    	$("#tfcodigoEmpresaGrupo").val("");
	    	$("#tfnombre").val("");
	    	
	    });
	});
</script>

<s:form action="copiarPrograma" id="editgrupoform" onsubmit="mostrarActualizando()" theme="simple">
<input name="ScrollX" id="ScrollX" type="hidden" value="<% request.getParameter("ScrollX"); %>" />
<input name="ScrollY" id="ScrollY" type="hidden" value="<%request.getParameter("ScrollY"); %>" />
<s:hidden name="programa.ruc"/>
<s:hidden name="programa.id"/>
<!--<s:hidden name="tipoPrograma"/>-->
<s:hidden name="tipoEmpresa"/>
<s:hidden name="tipoMetodo" value="1"/>
<s:hidden name="tipoCopiapf" id="idtipoCopia"/>
<s:hidden name="codEmpresaPrinHidden" id="idcodEmpPrinHidden"/>
<s:hidden name="copiarsinDataHidden" id="idcopiarsinDataHidden"/>

<table  width="100%" >
<tr>
	<td>
		<table width="100%">
			<tr>
				<td  >
				<div class="titulo">
					Copiar Programa Financiero		  
				</div>
				</td>
			</tr>			
			<tr>
				<td>
					<a class="btn" href="<s:url action="home" includeParams="none"/>" style="float: right;text-decoration: none;"><img align="middle" src="/ProgramaFinanciero/icono/appbar.layout.expand.left.variant.png" style="border:0px"></a>
				</td>
			</tr>
			<tr>
				<td >	
					<div class="formpanel">
					<table cellspacing="0" width="100%">
						<tr>
							<td width="20%" class="label">
								<label>Tipo de Programa:</label>
							</td>
							<td>
								<s:select 
										name="tipoPrograma"
										list="#{'':'','5':'Local','6':'Corporativo'}"
										label="Tipo de Programa"
										cssClass="validate[required] "
										theme="simple"/>
							</td>
							<td>
							</td>
							
						</tr>
						<tr>
							<td class="label">
								<label>Tipo de Empresa:</label>
							</td>
							<td>
								<s:select disabled="true"
												name="tipoEmpresa"
												list="#{'':'','2':'Empresa','3':'Grupo'}"
												label="Tipo de Empresa" 
												cssClass="validate[required] "
												theme="simple"
												id="seltipoEmpresa"/>
								<font size=2><b><span id="idnombreACopia"></span></b></font>
							</td>
							<td>
							</td>
							
						</tr>
						<tr>
							<td class="label">
								<label id="textcodigoempresagrupo"></label>
								<div id="divtipodoc" style="display:">
								<s:select id="idtipodocbusqueda"
										 name="tipoDocBusqueda"
										 list="#{'2':'RUC','3':'Código Central'}"
										 cssClass="validate[required] "
										 theme="simple"/>
								</div>
							</td>
							<td>
								<s:textfield name="codigoEmpresaGrupo"
															 id="tfcodigoEmpresaGrupo" 
															 label="RUC o Código Central"
															 key="codigoEmpresaGrupo"
															 onblur="javascript:buscarGrupoEmpresaEdit();"
															 theme="simple"
															 maxlength="11"
															 cssClass="validate[required] "
															 onkeypress="ingresoLetrasNumeros(event);"/>
							</td>
							<td>
								
								
								
							</td>
						</tr>
						<tr>
							<td class="label">
								<label id="textnombreempresagrupo">Nombre Empresa:</label>
							</td>
							<td>
								<s:textfield name="programa.nombreGrupoEmpresa"
											 id="tfnombre"
											 label="Nombre Empresa" 
											 key="nombreGrupoEmpresa"
											 cssClass="validate[required] "
											 readonly="true"
											 theme="simple"
											 size="60"/>
								
							</td>
							<td>
							</td>
							
						</tr>
					</table>
					
					<%if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){ %>
		<div id="divempresas" style="display:">
			<table>
				<tr>
					<td class="label">
					Empresa Principal:
					</td>
					<td>
						<s:select name="empresaPrincipal.codigo" id="selEmpresasPrin" list="listaGrupoEmpresas"  listKey="codigo" listValue="nombre" theme="simple"></s:select>
					</td>
				</tr>
				<tr id="idseccionEmpresaSecundaria">
					<td class="label">
						Empresas Secundarias:
					</td>
					<td>
					<select multiple="multiple" id="selListaEmpresasSecundarias" name="selListaEmpresasSecundarias">
						<s:iterator value="listaGrupoEmpresas">
						    <option <s:property value="seleccionadoSecu"/> value="<s:property value="codigo"/>"><s:property value="nombre"/></option>
					    </s:iterator>
					</select>
					</td>
				</tr>
				<!--				
				<tr id="idseccionEmpresaAnexo">
					<td class="label">
						Empresas Anexas:
					</td>
					<td>
					<select multiple="multiple" id="selListaEmpresasAnexas" name="selListaEmpresasAnexas">
						<s:iterator value="listaGrupoEmpresas">
						    <option <s:property value="seleccionadoAnex"/> value="<s:property value="codigo"/>"><s:property value="nombre"/></option>
					    </s:iterator>
					</select>
					</td>
				</tr>
				-->
			</table>
		</div>
		<%}%>
					
					</div>
				</td>
			</tr>
		
		</table>
		
		<table>
			<tr>
				<td>
				
					<s:submit theme="simple" value="Copiar Programa" onclick="return validarSeleccionEmpresa()" cssClass="btn"></s:submit>
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>
	<script language="JavaScript">
	var tipoCopia=$("#idtipoCopia").val();
	//alert(tipoCopia);
	if (tipoCopia=='3'){
		document.getElementById("idseccionEmpresaSecundaria").style.display = "none";
		document.getElementById("idseccionEmpresaAnexo").style.display = "none";
		document.getElementById("idnombreACopia").innerHTML="A Empresa Individual";
		
	}else{
		document.getElementById("idseccionEmpresaSecundaria").style.display = "";
		document.getElementById("idseccionEmpresaAnexo").style.display = "";
		document.getElementById("idnombreACopia").innerHTML="";
	}
				
	</script>
</s:form>


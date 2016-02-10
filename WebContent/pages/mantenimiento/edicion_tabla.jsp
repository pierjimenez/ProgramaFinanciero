<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<script language="JavaScript">

	$(document).ready(function(){
    	$("#edicionTablaForm").validationEngine();
   	});
	
</script>

<s:form id="edicionTablaForm" action="guardarTabla" theme="simple">
<table width="100%">
	<tr>
		<td>
			<div class="titulo">Edición de Tablas</div>
			<a class="btn"  href="<s:url action="listaTablas" includeParams="none"/>" style="float: right;"><img align="middle" src="/ProgramaFinanciero/icono/appbar.layout.expand.left.variant.png" style="border:0px">Retornar</a>	
		</td>
	</tr>
	<tr>
		<td >
		<div class="formpanel">
				<table width="100%" >
					<tr>
						<td>
							<s:hidden name="id"></s:hidden>
							<s:if test="tablaEdicion!=null && tablaEdicion.id!=null">
							    <s:textfield name="tablaEdicion.id" label="Id Tabla " readonly="true" disabled="true"/>							    
							</s:if>
						</td>
					</tr>
					<tr>
						<td class="label">
						C&oacute;digo:
						</td>
						<td>						
							 <s:textfield name="tablaEdicion.codigo" label="Código " maxlength="30"  cssClass="validate[required]"/>							
						</td>
					</tr>
					<tr>
						<td class="label">
						Descripci&oacute;n:
						</td>
						<td>						
							 <s:textfield name="tablaEdicion.descripcion" label="Descripción " maxlength="200" style="width:400px;"  cssClass="validate[required]"/>							
						</td>
					</tr>
					<tr>
						<td class="label">
						Abreviado:
						</td>
						<td>						
							 <s:textfield name="tablaEdicion.abreviado" label="Abreviado " maxlength="29"/>							
						</td>
					</tr>
					<tr>
						<td class="label">
						Tabla Padre:
						</td>
						<td>
						   <s:select  name = "tablaEdicion.padre.id" list="tablasPadre" listKey="id" listValue="descripcion" label="Tabla Padre " /> 							
						</td>
					</tr>
					<tr>
						<td class="label">	
						Estado:
						</td>
						<td>
							<s:if test="tablaEdicion==null">
							    <s:select list="#{'A':'ACTIVO'}" name="tablaEdicion.estado" label="Estado"/>
							</s:if>
							<s:else>
							    <s:select list="#{'A':'ACTIVO', 'I':'INACTIVO'}" name="tablaEdicion.estado" label="Estado"/>
							</s:else>							           						
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<s:submit value="Guardar" id="bGuardar" theme="simple" cssClass="btn"/>&nbsp;																					
		</td>
	</tr>
</table>
</s:form>


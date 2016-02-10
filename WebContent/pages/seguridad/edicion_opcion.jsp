<%@taglib prefix="s" uri="/struts-tags"%>

<script language="JavaScript">

	$(document).ready(function(){
    	$("#edicionOpcionForm").validationEngine();
   	});
	
</script>

<s:form id="edicionOpcionForm" action="guardarOpcion" theme="simple">
<table width="100%">
	<tr>
		<td>
			<div class="titulo" >Edición de Opciones</div>
			<a class="btn" href="<s:url action="listaOpciones" includeParams="none"/>" style="float: right;"><img align="middle" src="/ProgramaFinanciero/icono/appbar.layout.expand.left.variant.png" style="border:0px">Retornar</a>		
		</td>
	</tr>
	<tr>
		<td class="ui-widget-content2">
			<div class="formpanel">
				<table width="100%" >
					<tr>
						<td class="label">
							Id Opci&oacute;n:
						</td>
						<td>
							<s:hidden name="id"></s:hidden>
							<s:if test="opcionEdicion!=null && opcionEdicion.id!=null">
							    <s:textfield name="opcionEdicion.id" label="Id Opción " readonly="true" disabled="true"/>							    
							</s:if>
						</td>
					</tr>
					<tr>
						<td class="label">
							C&oacute;digo:
						</td>
						<td>						
							 <s:textfield name="opcionEdicion.codigo" label="Código " maxlength="100"  cssClass="validate[required]"/>							
						</td>
					</tr>
					<tr>
						<td class="label">
							Nombre:
						</td>
						<td>						
							 <s:textfield name="opcionEdicion.nombre" label="Nombre " maxlength="200" style="width:400px;"  cssClass="validate[required]"/>							
						</td>
					</tr>
					<tr>
						<td class="label">
							Tipo:
						</td>
						<td>	
							 <s:select  name = "opcionEdicion.tipo.id" list="opcionesTipo" listKey="id" listValue="descripcion" label="Tipo " cssClass="validate[required]"/> 												
						</td>
					</tr>	
					
					<tr>
						<td class="label">
							Superior:
						</td>
            			<td>  						
               				<s:select  name = "opcionEdicion.superior.id" list="opcionesEdicion" listKey="id" listValue="nombre" label="Superior "/>       									
						</td>
					</tr>
					<tr>
						<td class="label">
							Controlador:
						</td>
						<td>						
							 <s:textfield name="opcionEdicion.action" label="Action " maxlength="100" style="width:200px;"  cssClass="validate[required]"/>							
						</td>
					</tr>	
					<tr>
						<td class="label">
							Regla de Navegaci&oacute;n:
						</td>
						<td>						
							 <s:textfield name="opcionEdicion.reglaNavegacion" label="Regla de Navegación " maxlength="100" style="width:200px;" />							
						</td>
					</tr>	
					<tr>
						<td class="label">
							M&eacute;todo:
						</td>
						<td>						
							 <s:textfield name="opcionEdicion.metodo" label="Método " maxlength="100" style="width:200px;" />							
						</td>
					</tr>	
					<tr>
						<td class="label">
							Detalle:
						</td>
						<td>						
							 <s:textfield name="opcionEdicion.detalle" label="Detalle " maxlength="300" style="width:400px;" />							
						</td>
					</tr>									
					<tr>
						<td class="label">
							Estado:
						</td>
						<td>
							<s:if test="opcionEdicion==null">
							    <s:select list="#{'A':'ACTIVO'}" name="opcionEdicion.estado" label="Estado"/>
							</s:if>
							<s:else>
							    <s:select list="#{'A':'ACTIVO', 'I':'INACTIVO'}" name="opcionEdicion.estado" label="Estado"/>
							</s:else>							           						
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;												
						</td>
					</tr>					
					
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<s:submit value="Guardar" id="gTabla" theme="simple" cssClass="btn"/>&nbsp;												
		</td>
	</tr>
</table>
</s:form>


<%@taglib prefix="s" uri="/struts-tags"%>

<script language="JavaScript">

	$(document).ready(function(){
    	$("#edicionParametroForm").validationEngine();
   	});
	
</script>

<s:form id="edicionParametroForm" action="guardarParametro" theme="simple">
<table width="100%">
	<tr>
		<td>
			<div class="titulo" >Edici&#243;n de Par&#225;metro</div>
			<a class="btn" href="<s:url action="listaParametros" includeParams="none"/>" style="float: right;"><img align="middle" src="/ProgramaFinanciero/icono/appbar.layout.expand.left.variant.png" style="border:0px">Retornar</a>			
		</td>
	</tr>
	<tr>
		<td class="ui-widget-content2">
			<div class="formpanel">
				<table width="100%" >
					<tr>
						<td class="label">
						Id Par&aacute;metro:
						</td>
						<td>
							<s:hidden name="id"></s:hidden>
							<s:if test="parametroEdicion!=null && parametroEdicion.id!=null">
							    <s:textfield name="parametroEdicion.id" label="Id Parámetro " readonly="true" disabled="true"/>							    
							</s:if>
						</td>
					</tr>
					<tr>
						<td class="label">
							C&oacute;digo:
						</td>
						<td>						
							 <s:textfield name="parametroEdicion.codigo" label="Código " maxlength="150"  cssClass="validate[required]" size="70"/>							
						</td>
					</tr>
					<tr>
						<td class="label">
							Valor:
						</td>
						<td>						
							 <s:textfield name="parametroEdicion.valor" label="Valor " maxlength="1000" cssClass="validate[required]"  size="70"/>							
						</td>
					</tr>
					<tr>
						<td class="label">
							Descripci&oacute;n:
						</td>
						<td>						
							 <s:textarea name="parametroEdicion.descripcion" label="Descripción " maxlength="300" style="width:400px;" cols="150" rows="3"/>							
						</td>
					</tr>
					<tr>
						<td class="label">
							Estado:
						</td>
						<td>
							<s:if test="parametroEdicion==null">
							    <s:select list="#{'A':'ACTIVO'}" name="parametroEdicion.estado" label="Estado"/>
							</s:if>
							<s:else>
							    <s:select list="#{'A':'ACTIVO', 'I':'INACTIVO'}" name="parametroEdicion.estado" label="Estado"/>
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


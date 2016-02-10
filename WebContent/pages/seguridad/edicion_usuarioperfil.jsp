<%@taglib prefix="s" uri="/struts-tags"%>

<script language="JavaScript">

	$(document).ready(function(){
    	$("#edicionUsuarioPerfilForm").validationEngine();
   	});
	
</script>

<s:form id="edicionUsuarioPerfilForm" action="guardarUsuarioPerfil" theme="simple">
<table width="100%">
	<tr>
		<td>
			<div class="titulo" >Edición de Usuarios Perfil</div>
			<a class="btn" href="<s:url action="listaUsuarioPerfiles" includeParams="none"/>" style="float: right;"><img align="middle" src="/ProgramaFinanciero/icono/appbar.layout.expand.left.variant.png" style="border:0px">Retornar</a>
		</td>
	</tr>
	<tr>
		<td class="ui-widget-content2">
			<div class="formpanel">
				<table >
					<tr>
						<td class="label">
						Id Usuario Perfil:
						</td>
						<td>
							<s:hidden name="id"></s:hidden>
							<s:if test="usuarioPerfilEdicion!=null && usuarioPerfilEdicion.id!=null">
							    <s:textfield name="usuarioPerfilEdicion.id" label="Id Usuario Perfil " readonly="true" disabled="true"/>							    
							</s:if>
						</td>
					</tr>
					<tr>
						<td class="label">
						Usuario Registro:
						</td>
						<td>						
							 <s:textfield name="usuarioPerfilEdicion.usuarioRegistro" label="Usuario Registro " maxlength="10"  cssClass="validate[required]"/>							
						</td>
					</tr>
					<tr>
						<td class="label">
						Perfil:
						</td>
						<td>	
							 <s:select  name = "usuarioPerfilEdicion.perfil.id" list="perfiles" listKey="id" listValue="descripcion" label="Perfil " cssClass="validate[required]"/> 												
						</td>
					</tr>						
					<tr>
						<td class="label">
						Nivel Acceso:
						</td>
            			<td>  						
               				<s:select  name = "usuarioPerfilEdicion.nivelAcceso.id" list="nivelesAcceso" listKey="id" listValue="descripcion" label="Nivel Acceso " cssClass="validate[required]"/>       									
						</td>
					</tr>												
					<tr>
						<td class="label">
						Estado:
						</td>
						<td>
							<s:if test="usuarioPerfilEdicion==null">
							    <s:select list="#{'A':'ACTIVO'}" name="usuarioPerfilEdicion.estado" label="Estado"/>
							</s:if>
							<s:else>
							    <s:select list="#{'A':'ACTIVO', 'I':'INACTIVO'}" name="usuarioPerfilEdicion.estado" label="Estado"/>
							</s:else>							           						
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


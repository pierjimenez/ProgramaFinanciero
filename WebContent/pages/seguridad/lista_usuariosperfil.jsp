<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page import="pe.com.bbva.iipf.seguridad.model.entity.*"%>

<script language="JavaScript">
	
   $(document).ready(function() {
	   	$("thead tr").attr("class","ui-widget-header");
	   	
	});
	
</script>

<s:form  action = "buscarUsuarioPerfiles" id="busquedaUsuarioPerfilForm" theme="simple">
<table width="100%">
	<tr>
		<td>
			<div class="titulo">Mantenimiento de Usuarios Perfil</div>
		</td>
	</tr>
	<tr>
		<td>
				<table cellspacing="0" cellpadding="0" width="100%" >
					<tr>
						<td class="ui-widget-content2">
							<div  class="formpanel">
							<table>
								<tr>
									<td class="label">
									Usuario Registro :
									</td>
									<td>
										<s:textfield name="usuarioPerfil.usuarioRegistro" label="Usuario Registro " maxlength="10"/>
									</td>
									<td>
										<s:select list="#{'':'TODOS', 'A':'ACTIVO', 'I':'INACTIVO'}" name="usuarioPerfil.estado" label="Estado"/>           						 
									</td>
									<td>								 
				  						 <s:select  name = "usuarioPerfil.perfil.id" list="perfiles" listKey="id" listValue="descripcion" label="Perfil " />
									</td>
								</tr>	
								<tr>
									<td class="label">
									Nivel Acceso :
									</td>
									<td>
										<s:select  name = "usuarioPerfil.nivelAcceso.id" list="nivelesAcceso" listKey="id" listValue="descripcion" label="Nivel Acceso " />
									</td>
									<td>
										           						 
									</td>
									<td>								 
				  						 
									</td>
								</tr>
							</table>
							</div>
						</td>									
					</tr>	
					<tr>
						<td>
							&nbsp;
						</td>
					</tr>				
					<tr>
						<td colspan="3">
							<s:submit value="buscar" id="busuariosperfil" theme="simple" cssClass="btn"/>&nbsp;
	  						<s:submit value="agregar" id="bagregar" action="agregarUsuarioPerfil" theme="simple" cssClass="btn"></s:submit>	  
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<div  class="my_div">
							 		<display:table id="listaUsuarioPerfiles" name="usuarioPerfiles" uid="tb" 
        										   pagesize="10" export="false" requestURI="/paginarUsuarioPerfiles.do"
        										   class="ui-widget ui-widget-content">
        								   								  
								    <display:column title="Editar" property="id" url="/editarUsuarioPerfil.do" paramId="id" paramProperty="id" />	
								    <display:column property="usuarioRegistro" title="Registro"/>
								    <display:column property="perfil.descripcion" title="Perfil"/>								
								    <display:column property="nivelAcceso.descripcion" title="Nivel Acceso"/>
								     <%if(pageContext.getAttribute("tb") != null && ((UsuarioPerfil)pageContext.getAttribute("tb")).getEstado().equals("A")){ %>                                          
								    <display:column title="Estado" value="Activo"/>
								    <%}else if(pageContext.getAttribute("tb") != null){ %>                                 
								    <display:column title="Estado" value="Inactivo"/>                      
								    <%}%> 							    									    
								    <display:setProperty name="paging.banner.placement" value="bottom" />
								    </display:table>
								    
							</div>															
						</td>
					</tr>
				</table>
		</td>
	</tr>
</table>
</s:form>


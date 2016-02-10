<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page import="pe.com.bbva.iipf.seguridad.model.entity.*"%>

<script language="JavaScript">
	
   $(document).ready(function() {
	   	$("thead tr").attr("class","ui-widget-header");
	   	
	});
	
</script>

<s:form  action = "buscarOpciones" id="busquedaOpcionForm" theme="simple">
<table width="100%">
	<tr>
		<td>
			<div class="titulo">
					Mantenimiento de Opciones	  
			</div>
		</td>
	</tr>
	<tr>
		<td>
			
				<table width="100%" >
					<tr>
						<td class="ui-widget-content2">
						<div class="formpanel">
							<table>
								<tr>
									<td class="label">
										Nombre:
									</td>
									<td>
										<s:textfield name="opcion.nombre" label="Nombre " maxlength="200"/>
									</td>
									<td>
										<s:select list="#{'':'TODOS', 'A':'ACTIVO', 'I':'INACTIVO'}" name="opcion.estado" label="Estado"/>           						 
									</td>
									<td>								 
				  						<s:select  name = "opcion.tipo.id" list="opcionesTipo" listKey="id" listValue="descripcion" label="Tipo " /> 
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
									<td>
										<s:submit value="buscar" id="bopciones" theme="simple" cssClass="btn"/>&nbsp;
	  									<s:submit value="agregar" id="bagregar" action="agregarOpcion" theme="simple" cssClass="btn"></s:submit>
									</td>
					</tr>
					<tr>
						<td>
							<div  class="my_div">										
								   <display:table id="listaOpciones" name="opciones" uid="tb" 
        								pagesize="10" export="false" requestURI="/paginarOpciones.do"
        								class="ui-widget ui-widget-content" >
        								   								  
								    <display:column title="Editar" property="id" url="/editarOpcion.do" paramId="id" paramProperty="id" />	
								    <display:column property="nombre" title="Nombre"/>
								    <display:column property="tipo.descripcion" title="Tipo"/>								
								    <display:column property="superior.nombre" title="Superior"/>
								     <%if(pageContext.getAttribute("tb") != null && ((Opcion)pageContext.getAttribute("tb")).getEstado().equals("A")){ %>                                          
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


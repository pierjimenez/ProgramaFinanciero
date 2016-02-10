<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page import="pe.com.bbva.iipf.mantenimiento.model.*"%>

<script language="JavaScript">
	
   $(document).ready(function() {
	   	$("thead tr").attr("class","ui-widget-header");
	   	
	});
</script>

<s:form  action = "buscarTablas" id="busquedaTablaForm" theme="simple">
<table width="100%">
	<tr>
		<td>
			<div class="titulo">Mantenimiento de Tablas</div>
		</td>
	</tr>
	<tr>
		<td>
				<table cellspacing="0" cellpadding="0" width="100%"  >
					<tr >
						<td >
						<div class="formpanel">
							<table>
								<tr>
									<td class="label">
										Descripci&oacute;n:
									</td>
									<td>
										<s:textfield name="tabla.descripcion" label="Descripción " maxlength="200" theme="simple"/>
									</td>
								</tr>
								<tr>
									<td class="label">
									  Tabla Padre:						
									</td>
									<td>
									   <s:select  name = "tabla.padre.id" list="tablasPadre" listKey="id" listValue="descripcion" label="Tabla Padre " theme="simple" /> 							
									</td>
								</tr>
								<tr>
									<td class="label">
										Estado:           						 
									</td>
									<td>
										<s:select list="#{'-1':'TODOS', 'A':'ACTIVO', 'I':'INACTIVO'}" name="tabla.estado" label="Estado" theme="simple"/>           						 
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
	  						<s:submit value="buscar" id="btablas" theme="simple" cssClass="btn"/>&nbsp;
	  						<s:submit value="agregar" id="bagregar" action="agregarTabla" theme="simple" cssClass="btn"></s:submit>
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<div  class="my_div">
								    <display:table id="listaTablas" 
								    	name="tablas" uid="tb" 
        								pagesize="10" export="false" 
        								requestURI="/paginarTablas.do" 
        								class="ui-widget ui-widget-content"
        								>
        								   								  
								    <display:column title="Editar" property="id" url="/editarTabla.do" paramId="id" paramProperty="id"  />	
								    <display:column property="codigo" title="C&#243;digo"/>
								    <display:column property="descripcion" title="Descripci&#243;n"/>
								    <display:column property="padre.id" title="Padre Id"/>
								    <display:column property="padre.descripcion" title="Padre Descripci&#243;n"/>
								     <%if(pageContext.getAttribute("tb") != null && ((Tabla)pageContext.getAttribute("tb")).getEstado().equals("A")){ %>                                          
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


<%@taglib prefix="s" uri="/struts-tags" %>
<div id="list-menu">
		<s:set name="opciones" value="#session.permiso_opciones_session"/>
		<s:set name="padre" value="#session.opcion_padre_actual"/>
		<ul>
			<s:iterator value="opciones">
	               <s:if test="superior.id == #padre">
	               			<li>
	            				<s:a action="%{action}" >
	            					<s:property value="nombre"/>
	            				</s:a>
	            			</li>
	               </s:if>
			</s:iterator>
		</ul>
					
</div>
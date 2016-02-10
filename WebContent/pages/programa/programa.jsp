<%@taglib prefix="s" uri="/struts-tags" %>
<%@page import="pe.com.stefanini.core.action.GenericAction"%>
<%@page import="pe.com.bbva.iipf.util.Constantes"%>
<% 
	String cabTab = GenericAction.getObjectSession("cabTab").toString();
	String contentTab = GenericAction.getObjectSession("contentTab").toString();
%>
<script language="JavaScript">  
   $(document).ready(function() {
		//When page loads...
		$(".tab_content").hide(); //Hide all content
		$("<%=cabTab%>").addClass("active").show(); //Activate first tab
		$("<%=contentTab%>").show(); //Show first tab content
	});
</script>  

<ul class="tabs">
	<li id="li0"><a href="<s:url action="grupoAction"/>">Formulario de Ingreso</a></li>
    <li id="li1"><a href="#tab1" >Datos B&aacute;sicos</a></li>
    <li id="li2"><a href="#tab2" >S&iacute;ntesis Ec./Fin.</a></li>
    <li id="li3"><a href="#tab3">Rating</a></li>
    <li id="li4"><a href="#tab4">An&aacute;lisis Sectorial</a></li>
    <li id="li5"><a href="#tab5">Relac. Bancarias</a></li>
    <li id="li6"><a href="#tab6">Fact. de Riesgo</a></li>
    <li id="li7"><a href="#tab7">Prop. de Riesgo</a></li>
    <li id="li8"><a href="#tab8">Pol. de Riesgo</a></li>
    <li id="li9"><a href="#tab9">Posicion</a></li>
</ul>
<div class="tab_container">
	<div>
		Empresa: <%=request.getSession().getAttribute("nombre_empresa_grupo_session")%>
	</div>
    <div id="tab1" class="tab_content">
       	<s:include value="/pages/programa/datos_basicos.jsp"></s:include>
    </div>
    <div id="tab2" class="tab_content">
    	<table>
    		<tr>
    			<td>
        			<s:include value="/pages/programa/sintesis_financiero.jsp"></s:include>
        		</td>
        	</tr>
        </table>
    </div>
     <div id="tab3" class="tab_content">
     
    </div>
    <div id="tab4" class="tab_content">
       <s:include value="/pages/programa/analisis_sectorial.jsp"></s:include>
    </div>
     <div id="tab5" class="tab_content">
      <s:include value="/pages/programa/relaciones_bancarias.jsp"></s:include>
    </div>
    <div id="tab6" class="tab_content">   
     <s:include value="/pages/programa/factoresRiesgo.jsp"></s:include>  
    </div>
     <div id="tab7" class="tab_content">
        <!--Content-->
    </div>
    <div id="tab8" class="tab_content">
       <!--Content-->
    </div>
    <div id="tab9" class="tab_content">
       <!--Content-->
    </div>
</div>

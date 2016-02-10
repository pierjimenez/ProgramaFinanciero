<%
String valor=Math.round((Math.random()*99999))+"";
%>

<ul class="tabs">
    <li id="li1"><a  onclick="return saveFormulariosActualizadosChange(this)" href="<s:url action="initDatosBasicos"/>?peticion=<%=valor%>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.datosbasico.png" style="border:0px">Datos B&aacute;sicos</a></li>
    <li id="li2"><a  onclick="return saveFormulariosActualizadosChange(this)" href="<s:url action="initAnalisisEconomica"/>?peticion=<%=valor%>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.sintesis.png" style="border:0px">S&iacute;ntesis Ec./Fin.</a></li>
    <li id="li3"><a  onclick="return saveFormulariosActualizadosChange(this)" href="<s:url action="initRating"/>?peticion=<%=valor%>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.rating.png" style="border:0px">Rating</a></li>
    <li id="li4"><a  onclick="return saveFormulariosActualizadosChange(this)" href="<s:url action="initAnalisisSectorial"/>?peticion=<%=valor%>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.analisis.png" style="border:0px">An&aacute;lisis Sectorial</a></li>
    <li id="li5"><a  onclick="return saveFormulariosActualizadosChange(this)" href="<s:url action="initRelacionesBancarias"/>?peticion=<%=valor%>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.relaciones.png" style="border:0px">Relac. Bancarias</a></li>
	<li id="li6"><a  onclick="return saveFormulariosActualizadosChange(this)" href="<s:url action="initFactoresRiesgo"/>?peticion=<%=valor%>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.factores.png" style="border:0px">Fact. de Riesgo</a></li>
    <li id="li7"><a  onclick="return saveFormulariosActualizadosChange(this)" href="<s:url action="initPropuestaRiesgo"/>?peticion=<%=valor%>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.propuesta.png" style="border:0px">Prop. de Riesgo</a></li>
    <li id="li8"><a  onclick="return saveFormulariosActualizadosChange(this)" href="<s:url action="initPoliticasRiesgo"/>?peticion=<%=valor%>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.politica.png" style="border:0px">Pol. de Riesgo</a></li>
    <li id="li9"><a  onclick="return saveFormulariosActualizadosChange(this)" href="<s:url action="initAnexos"/>?peticion=<%=valor%>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.posicion.png" style="border:0px">Posicion</a></li>
    <li id="li10"><a onclick="return saveFormulariosActualizadosChange(this)" href="<s:url action="initReporteCredito"/>?peticion=<%=valor%>"><img align="center" src="/ProgramaFinanciero/imagencab/appbar.reporte.png" style="border:0px">Reporte de Cr&#233;dito</a></li>
</ul>
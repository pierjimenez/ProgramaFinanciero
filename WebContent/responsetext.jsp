<% 
	Long idPrograma1  = (Long.valueOf(request.getAttribute("idPrograma").toString()));
%>
<applet id="DescargaExcel" name="DescargaExcel" code="pe/com/bbva/iipf/descarga/applet/DescargaDirectorioApplet" archive="appletExportaExcel.jar" width=0 height=0>
		<param name="idProgramaApp" value="<%=idPrograma1%>">		
</applet>
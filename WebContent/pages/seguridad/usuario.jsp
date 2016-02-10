<%@ taglib prefix="s" uri="/struts-tags" %>
<script language="JavaScript">
	
   $(document).ready(function() {
   		$("#formusuario").validationEngine();
	});
</script>
<h1>Usuario</h1>
<h2>Add Customer</h2>
<s:form action="addUsuarioAction" id="formusuario">
  <s:textfield name="usuario.codigoUsuLdap" label="Cod." 
  			   cssClass="validate[required]"/>
  <s:submit value="Guardar" id="sguardar"/>
</s:form>

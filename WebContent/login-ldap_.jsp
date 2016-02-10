<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="IBM Software Development Platform">
<meta http-equiv="Content-Style-Type" content="text/css">
<link rel="stylesheet" href="/ProgramaFinanciero/css/form.css" type="text/css"/>
<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/demos.css"/>
<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/jquery-ui-1.11.4.css"/>
<title>Acceso LDAP</title>
<style type="text/css">
body
{ 
background: #005a8c; 
}
</style>
</head>
<body >

<div class="centered">
	<div class="column" style="margin-top: -174px;">
		<div class="login">
			<s:form action="loginAction" theme="simple">
			
					<div style="margin-right: -130px;  margin-top: 200px; ">
					<s:hidden name="desa" value="desa"></s:hidden>
					</div>
					<div class="item_login ">
					    <s:actionerror theme="simple" cssClass="errorMessage_login"/>
					</div>
					<div class="item_login">
						<label>Usuario:</label>
					</div>
					<div class="item_login">
					<s:textfield name="codigoUsuario" ></s:textfield>
					</div>
					<div class="item_login">
						<label>Contrase&ntilde;a:</label>
					</div>
					<div class="item_login">
					<s:textfield name="password"></s:textfield>
					</div>
					<div style="margin-left: 275px; margin-top: 10px">
					<s:submit value="Aceptar" cssClass="ui-button ui-widget ui-state-default ui-corner-all"></s:submit>
					</div>
			
			</s:form>
		</div>
	</div>
</div>
</body>
</html>


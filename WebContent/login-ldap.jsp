<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" >
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="IBM Software Development Platform">
<meta http-equiv="Content-Style-Type" content="text/css">
<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/form.css"/>
<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/demos.css"/>
<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/jquery-ui-1.11.4.css"/>
<link rel="stylesheet" href="/ProgramaFinanciero/css/validationEngine.jquery.css" type="text/css"/>
<script type="text/javascript" src="/ProgramaFinanciero/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/ProgramaFinanciero/js/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="/ProgramaFinanciero/js/validation/jquery.validationEngine-es.js"></script>
<script type="text/javascript" src="/ProgramaFinanciero/js/validation/jquery.validationEngine.js"></script>
<title>Login: Programas Financieros</title>
<style type="text/css">
@import url("css/template.css");
.ancho{
	height:7px;
}
.ancho1{
	height: 10px;
	font-size: 5px;
}
.borde {
	border-top-style: solid;
	border-right-style: solid;
	border-bottom-style: solid;
	border-left-style: solid;
	border-top-width: thin;
	border-right-width: thin;
	border-bottom-width: thin;
	border-left-width: thin;
}
body
{
	background: #FFF;
	/*background:#005a8c;*/
	text-align: center;
}
</style>
<script type="text/javascript">
	function validar (){
		var user = $("#codigoUsuario").val();
		var pass = $("#password").val();
		if(jQuery.trim(user)==''){
		 	alert ("Ingrese el nombre de usuario.");
		 	return false;  
		}
		if(jQuery.trim(pass)==''){
		   alert ("Ingrese el password");
		   return false;
		}
		return true;
	}
</script>
</head>
<body >

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td colspan="2" bgcolor="#FFFFFF">&nbsp;</td>
    <td width="16" bgcolor="#FFFFFF">&nbsp;</td>
  </tr>
  <tr>
    <td width="35" bgcolor="#FFFFFF">&nbsp;</td>
    <td width="1025" bgcolor="#FFFFFF"><img src="logo_bbva_cab1.png" width="252" height="53"></td>
    <td bgcolor="#FFFFFF" class="texto"><img src="logo_bbva_cab2.png" width="365" height="53"></td>
  </tr>
  
</table>

<table width="100%"  valign="baseline" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr >
    <td colspan="3" bgcolor="#0D5398"><div class="ancho1"></div></td>
  </tr>
  <tr>
    <td colspan="3" bgcolor="#0C71B5"><div class="ancho1"></div></td>
  </tr>
  <tr>
    <td colspan="3" bgcolor="#02A9DE"><div class="ancho1"></div></td>
  </tr>
  <tr>
    <td colspan="3" bgcolor="#4DBAE1"><div class="ancho1"></div></td>
  </tr>
  <tr>
    <td colspan="3" bgcolor="#9DD7ED"><div class="ancho1"></div></td>
  </tr>
  <tr>
    <td colspan="3" bgcolor="#C5E5F4"><div class="ancho1"></div></td>
  </tr>
</table>
<s:form action="loginAction" theme="simple" id="formlogin" onsubmit="javascript: return validar();">
	  <%--<div style="margin-right: -130px;  margin-top: 200px; ">
		<s:hidden name="desa" value="desa"></s:hidden>
	</div>--%>

	  <table class="borde" border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td colspan="4">&nbsp;</td>
	    </tr>
	    <tr>
	      <td colspan="4"><s:actionerror theme="simple" cssClass="errorMessage_login"/></td>
	    </tr>
	    <tr>
	      <td>&nbsp;</td>
	      <td colspan="3" style="color: #005a8c; font-size: 11px; font-family: Verdana, Geneva, sans-serif; font-weight: bold;">Ingrese su nombre de Usuario y Contrase&ntilde;a:</td>
	    </tr>
	    <tr>
	      <td width="25">&nbsp;</td>
	      <td width="122">&nbsp;</td>
	      <td width="34">&nbsp;</td>
	      <td width="194">&nbsp;</td>
	    </tr>
	    <tr>
	      <td>&nbsp;</td>
	      <td style="color: #005a8c; font-size: 10px; font-family: Verdana, Geneva, sans-serif; text-align: right;">Usuario</td>
	      <td style="color: #005a8c; font-size: 10px; font-family: Verdana, Geneva, sans-serif;">:</td>
	      <td><input name="codigoUsuario" type="text" size="20" id="codigoUsuario" value="<s:property value="codldap"/>" readonly="true"/>
	      
	      
	      </td>
	    </tr>
	    <tr>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	    </tr>
	    <tr>
	      <td>&nbsp;</td>
	      <td style="color: #005a8c; font-size: 10px; font-family: Verdana, Geneva, sans-serif; text-align: right;">Contrase&ntilde;a</td>
	      <td style="color: #005a8c; font-size: 10px; font-family: Verdana, Geneva, sans-serif;">:</td>
	      <td><input name="password" type="password" size="20"  id="password"/></td>
	    </tr>
	    <tr>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	    </tr>
	    <tr>
	      <td>&nbsp;</td>
	      <td >&nbsp;</td>
	      <td>&nbsp;</td>
	      <td><div style="margin-left: 75px"><s:submit value="Aceptar" cssClass="ui-button ui-widget ui-state-default ui-corner-all"></s:submit></div></td>
	    </tr>
	    <tr>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	    </tr>
	  </table>
	  <p>&nbsp;</p>
	  <p>&nbsp;</p>
	  <p>&nbsp;</p>
	  <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0"  valign="baseline">
	    <tr>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td  bgcolor="#0C71B5">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td bgcolor="#4DBAE1">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td bgcolor="#0C71B5">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td  bgcolor="#4DBAE1">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td  bgcolor="#0C71B5">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	    </tr>
	  </table>
	  <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0"  valign="baseline">
	    <tr>
	      <td>&nbsp;</td>
	      <td bgcolor="#4DBAE1">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td bgcolor="#C5E5F4">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td bgcolor="#4DBAE1">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td bgcolor="#0C71B5">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td bgcolor="#BCCCF3">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	    </tr>
	  </table>
	  <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0"  valign="baseline">
	    <tr>
	      <td bgcolor="#C5E5F4">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td bgcolor="#0C71B5">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td bgcolor="#9DD7ED">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td bgcolor="#C5E5F4">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td bgcolor="#4DBAE1">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	    </tr>
	  </table>
	  <p>&nbsp;</p>
</s:form>
			
</body>
</html>


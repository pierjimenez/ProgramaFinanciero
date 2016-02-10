<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@page contentType="text/html;charset=UTF-8"  language="java"%>
<%@ page errorPage="error.jsp" %> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
    
<html>
    <head>
     <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" >     

        <link rel="stylesheet" href="/ProgramaFinanciero/css/jquery.wysiwyg.css" type="text/css"/>
        <link rel="stylesheet" href="/ProgramaFinanciero/css/validationEngine.jquery.css" type="text/css"/>
        <link rel="stylesheet" href="/ProgramaFinanciero/css/form.css" type="text/css"/>
        <link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/tab.css"/>
        <link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/demos.css"/>
		<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/jquery-ui-1.11.4.css"/>
		<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/displayTable.css"/>
		<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/jquery.checkboxtree.min.css">
		<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/jGlideMenu.css">
		<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/bbva-min.css?ver3.0"/>
					
        <script type="text/javascript" src="/ProgramaFinanciero/js/struts.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/jquery-1.11.3.min.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/jquery-migrate-1.2.1.min.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/editor/jquery.wysiwyg.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/editor/wysiwyg.image.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/editor/wysiwyg.link.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/editor/wysiwyg.table.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/validation/jquery.validationEngine-es.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/validation/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/pf.js?ver3.0"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/validation/jquery.meio.mask.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/jquery.counter-1.0.min.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/jquery-ui-1.11.4.js"></script>
  		<script type="text/javascript" src="/ProgramaFinanciero/js/jquery.ui.datepicker-es.js"></script>
  	
		
        <title><tiles:insertAttribute name="title" ignore="true" /></title>
            <% 
			String x = request.getAttribute("scrollX")==null?"ningun valor":request.getAttribute("scrollX").toString();
			String y = request.getAttribute("scrollY")==null?"ningun valor":request.getAttribute("scrollY").toString();
			String valBDtiempoSessionOut="";
			valBDtiempoSessionOut = (String.valueOf(request.getSession().getAttribute("TIEMPO_SESSION_TIMEOUT").toString()));
			String url_cerrar_session="";
			url_cerrar_session = (String.valueOf(request.getSession().getAttribute("PATH_URL_CERRARSESSION").toString()));
			
			int valortiemposession=Integer.parseInt(valBDtiempoSessionOut);
			valortiemposession=valortiemposession * 60000;
			
			%>
			
		<script language="JavaScript">
			
			//var iStart = 0;
			//var iMinute =15;// //Obtengo el tiempo de session permitida
			//var iMinute= '<%=valBDtiempoSessionOut%>';
			//function showTimer() { 
			
			//	iStart = 60; 
			//	iMinute -= 1
			//	lessMinutes(); 
			//} 
			
			//function lessMinutes()
			//{
				
				//Busco mi elemento que uso para mostrar los minutos que le quedan (minutos y segundos)
			//	obj = document.getElementById('TimeLeft'); 
			//	if (iStart == 0) {
			//	iStart = 60 
			//	iMinute -= 1; 
			//	}
			//	iStart = iStart - 1;
				
				//Si minuto y segundo = 0 ya expiró la sesion 
			//	if (iMinute==0 && iStart==0) {
				//alert("Su sesion ha expirado....");			
					//window.location.href= '<%=url_cerrar_session%>';
					
			//	}	
				//para soportar firefox usar obj.textContent		
			//	if (iStart < 10)
			//	obj.innerText =obj.textContent = iMinute.toString() + ':0' + iStart.toString();
			//	else
			//	obj.innerText =obj.textContent = iMinute.toString() + ':' + iStart.toString();
				
				
				//actualizo mi método cada segundo  
			//	window.setTimeout("lessMinutes();",1000);
			//} 
			
			function resetTiempoSession(){			
			 //iStart = 0;
			// iMinute =15;// //Obtengo el tiempo de session permitida
			 //iMinute='<%=valBDtiempoSessionOut%>';	
			 actualizar_tiempo_barra();		 			 
			}
	
	</script>


<script type="text/javascript">
			(function($) {
				$(document).ready(function() {
					$('.wysiwyg').wysiwyg();
				});
				
			})(jQuery);
		</script>

    </head>
    <body id="mybody" onload="ResetScrollPosition('<%=x%>','<%=y%>'); resetTiempoSession();">
        <table border="0" cellpadding="2" cellspacing="2" align="center"  >
            <tr>
                <td height="30">                
                    <tiles:insertAttribute name="header" />
                </td>
            </tr>
            <tr>
                <td valign="top"  class="ui-widget-content2 ln_formatos" style="border: #A4A4A4 1px solid;">
                	<div>
						<s:if test="hasActionErrors()">
						   <div class="errors">
						     <s:actionerror theme="simple"/>
						</div>
						</s:if>
						<s:if test="hasActionMessages()">
						   <div class="success">
							     <s:actionmessage theme="simple"/>
						   </div>
						</s:if>
					</div>
					
					<div id="loading-mask" style="display:none;"></div>
					<div id="loading" style="display:none;">
					<div class="loading-indicator">
					Procesando....
					</div>
					</div>
					
                	<div >
                    	<tiles:insertAttribute name="body" />
                    </div>
                </td>
            </tr>
            <tr>
                <td height="30"  style="border: #A4A4A4 1px solid;" class="ui-widget-content2 ln_formatos">
                    <tiles:insertAttribute name="footer" />
                </td>
            </tr>
        </table>
        
        <script language="JavaScript">
  		var tiempo_inicial = <%=valortiemposession%>;
  		function finalizarSesion(){
  			//alert("Su sesion ha expirado....");			
			window.location.href= '<%=url_cerrar_session%>';
  		}
		//showTimer();

		</script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/barra_progreso.js"></script>
		
    </body>
</html>

<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="error.jsp" %> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
 
<html>
    <head>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" >
       <meta http-equiv="pragma" content="no-cache" />
		<base target="_self" />
        <link rel="stylesheet" href="/ProgramaFinanciero/css/jquery.wysiwyg.css" type="text/css"/>
        <link rel="stylesheet" href="/ProgramaFinanciero/css/validationEngine.jquery.css" type="text/css"/>
        <link rel="stylesheet" href="/ProgramaFinanciero/css/form.css" type="text/css"/>
        <link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/tab.css"/>
        <link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/demos.css"/>
		<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/jquery-ui-1.11.4.css"/>
		<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/displayTable.css"/>
		<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/jquery.checkboxtree.min.css">
		<link rel="stylesheet" type="text/css" href="/ProgramaFinanciero/css/jGlideMenu.css">
					
        <script type="text/javascript" src="/ProgramaFinanciero/js/struts.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/jquery-1.11.3.min.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/jquery-migrate-1.2.1.min.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/editor/jquery.wysiwyg.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/editor/wysiwyg.image.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/editor/wysiwyg.link.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/editor/wysiwyg.table.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/validation/jquery.validationEngine-es.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/validation/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/pf.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/validation/jquery.meio.mask.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/jquery.counter-1.0.min.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="/ProgramaFinanciero/js/jquery-ui-1.11.4.js"></script>
		
		
        <title><tiles:insertAttribute name="title" ignore="true" /></title>
        <script type="text/javascript">
			(function($) {
				$(document).ready(function() {
					$('.wysiwyg').wysiwyg();
				});
			})(jQuery);
		</script>
		
    </head>
    <body>
        <table border="0" cellpadding="2" cellspacing="2" align="center"  >
 
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
                	<div >
                    	<tiles:insertAttribute name="body" />
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>

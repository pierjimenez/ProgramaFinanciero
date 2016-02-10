<%@taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
	 $(document).ready(function() { 	
	 	$("#optformulario").bind("hover", function (event, message1, message2) {
		 	$(this).css({fontWeight:"bolder",textDecoration:"none", fontFamily:"georgia, serif",background:"url(/ProgramaFinanciero/image/circle.gif)"});
		 	$("#optformulario b").css({left:"-22px", top:"100px",position:"absolute",display:"block",width:"100px", height:"100px",fontSize:"16px", color:"#63352c", background:"#fff"});
		 	$("#optformulario b span").css({display:"block", fontSize:"12px", color:"#888", fontWeight:"normal", marginTop:"15px"});
		});
		$("#optformulario").trigger("hover");
		
		$(".otro").hover(function(){
			$("#optformulario").css({background:"", fontWeight:"bolder" });
			$("#optformulario b").css({display:"none" });
		 	$("#optformulario b span").css({display:"none"});
		});
		$("#optformulario").hover( function () {
		 	$(this).css({fontWeight:"bolder",textDecoration:"none", fontFamily:"georgia, serif",background:"url(/ProgramaFinanciero/image/circle.gif)"});
		 	$("#optformulario b").css({left:"-22px", top:"100px",position:"absolute",display:"block",width:"100px", height:"100px",fontSize:"16px", color:"#63352c", background:"#fff"});
		 	$("#optformulario b span").css({display:"block", fontSize:"12px", color:"#888", fontWeight:"normal", marginTop:"15px"});
		});
		
	 });
	 
</script>
<div >
	<div class="titulo">
		Panel Principal
	</div>
	
	<div id="circularMenu" >
	
	  <li class="formulario" ><a href="<s:url action="grupoAction"/>" id="optformulario"><b>Formulario de Ingreso<br><span>Opción para crear un programa nuevo</span></b><img src="image/bbva.nuevo2Azul48.png" alt="Ingreso" border="0"></a></li>
	  
	  <li class="consultas"><a href="<s:url action="consultasModificaciones"/>" class="otro"><b>Consultas y Modificaciones<br><span>Opción para buscar programas</span></b><img src="image/bbva.consultaAzul48.png" alt="Consultas" border="0"></a></li>
	  
	  <li class="maestros"><a href="<s:url action="mantenimientos"/>" class="otro"><b>Mantenimientos<br><span>Opción para mantenimiento a las tablas maestras</span></b><img src="image/bbva.MantenimientoAzul48.png" alt="Mantenimientos" border="0"></a></li>
	 
	  <li class="security"><a href="<s:url action="seguridadDo"/>" class="otro"><b>Seguridad<br><span>Opción para habilitar opciones de seguridad</span></b><img src="image/bbva.seguridadAzul48.png" alt="Seguridad" border="0"></a></li>
	
	</div>
</div>

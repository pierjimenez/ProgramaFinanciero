	//300000
	
	var	tiempo_actual = tiempo_inicial; //milisegundos = 5 minutos;
	var verde_inicial = 255,
		verde_actual = verde_inicial;
	var rojo_inicial = 0,
		rojo_actual = rojo_inicial;
	
	var ancho_inicial_barra = $('.barra_progreso').width();
	var tiempo_milisegundos = 1000; //tiempo de ejecucion de la barra	
	
	var barra_tiempo = setInterval(function(){funcion_barra_tiempo()},tiempo_milisegundos);
	
	function funcion_barra_tiempo(){
		tiempo_actual -= tiempo_milisegundos;
		var tiempo = new Date(tiempo_actual);
		$('#tiempo_letras').html(tiempo.toLocaleTimeString().substring(3,8));
		
		ancho_actual_barra = (ancho_inicial_barra/tiempo_inicial)*tiempo_actual;
		
		var ch = "";
		var taux = tiempo_inicial/3;
		if(tiempo_inicial-tiempo_actual<taux){
			rojo_actual = ((tiempo_inicial-tiempo_actual)/taux)*verde_inicial;
			ch = color_hexadecimal(rojo_actual,255,0);
		}else{
			verde_actual = (tiempo_actual/(tiempo_inicial-taux))*verde_inicial;
			ch = color_hexadecimal(255,verde_actual,0);
		}
		ancho_barra = $('.barra_progreso').css({'width':ancho_actual_barra,'background-color':ch});
		
		if(tiempo_actual==0){
			$('.barra_progreso').css({'visibility':'hidden'});
				clearInterval(barra_tiempo);
			  	finalizarSesion();
		}
	}
	
	
	//funcion para retornor color rgb en hexadecimal;
	function color_hexadecimal(r,v,a){
		r = Math.round(r);v = Math.round(v);a = Math.round(a);
		
		var rojo_hexa = r.toString(16);
		var verde_hexa = v.toString(16);
		var azul_hexa = a.toString(16);
		
		if(r<16){rojo_hexa = "0"+rojo_hexa;}
		if(v<16){verde_hexa = "0"+verde_hexa;}
		if(a<16){azul_hexa = "0"+azul_hexa;}
		
		return "#"+rojo_hexa+verde_hexa+azul_hexa;
	}
	
	function actualizar_tiempo_barra(){
			tiempo_actual = tiempo_inicial;
	}
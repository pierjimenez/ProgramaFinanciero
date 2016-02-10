function validarDatosGeneral() {
   	return validarDatosGeneralPorClase("obligatorio");
}
function validarDatosGeneralPorClase(nombreClase) {
	var seleccion = null;
	$("."+nombreClase).each(function () {
		this.value = $.trim(this.value);
		if((this.value == "" || (this.type == "select" && this.value == "-1")) && this.disabled != true) {
			seleccionarElemento(this, true);
			
			if(seleccion == null) {
				seleccion = this;
			}
		} else {
			seleccionarElemento(this, false);
		}
	});
	if(seleccion != null) {
		alert("Faltan ingresar los datos obligatorios");
		try {
			seleccion.focus();
		} catch(e) {}
		return false;
	}
	return true;
}
function cancelarValidacionGeneral(nombreClase) {
   	cancelarValidacionGeneralPorClase("obligatorio");
}
function cancelarValidacionGeneralPorClase(nombreClase) {
   	$("."+nombreClase).each(function () {
		seleccionarElemento(this, false);
	});
}
function seleccionarElemento(elemento, seleccionar) {
   	elemento.style.backgroundColor = (seleccionar)?"yellow":"";
}
function seleccionarFocoGeneral() {
   	seleccionarFocoPorClase("obligatorio");
}
function seleccionarFocoPorClase(nombreClase) {
	var seleccion = null;
	try {
		var elemento = $("."+nombreClase).get(0);
		if(elemento != null) {
			elemento.focus();
			elemento.select();
		}
	} catch(e) {}
}
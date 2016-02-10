function genericUnFormat(cellvalue, options, cell){
	return $('input', cell).attr('value');
}

function genericCheckUnFormat(cellvalue, options, cell){
	var checked = $('input', cell).attr('checked');
	if(checked){
		return "1";
	}else{
		return "0";
	}
}


function valSeleccionFormat(cellvalue, options, rowObject){	
	
	var rowID = rowObject["codigo"];

	return "<input type='radio' id='"+rowID+"' name='arrayValSeleccion' value='"+1+"' />";
	
}

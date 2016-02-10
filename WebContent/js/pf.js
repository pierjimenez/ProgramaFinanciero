var lNumeros='1234567890';
var lLetras=' ABCÇDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz´';
var lSignos = '+-';
function ingresoLetrasNumeros(e){
	var key;
	var valid = '' + lLetras + lNumeros ;
	
	if(e.which){
		key = String.fromCharCode(e.which);
		if (valid.indexOf("" + key) == "-1")
			e.preventDefault();
	}
	else if(e.keyCode){
		key = String.fromCharCode(e.keyCode);
		if (valid.indexOf("" + key) == "-1")
			e.keyCode = 0;
	}
}

function ingresoLetrasNumerosSingos(e){
	var key;
	var valid = '' + lLetras + lNumeros +lSignos ;
	
	if(e.which){
		key = String.fromCharCode(e.which);
		if (valid.indexOf("" + key) == "-1")
			e.preventDefault();
	}
	else if(e.keyCode){
		key = String.fromCharCode(e.keyCode);
		if (valid.indexOf("" + key) == "-1")
			e.keyCode = 0;
	}
} 


function newWindowUploadFile2() {   
    mywindow=open('doverUploadFile.do','myname',"scrollbars=no,scrolling=no,top=" + (screen.height/8 + 80) + ",height=270,width=450,left=" + ((screen.width - 750)/2) + ",resizable=no");
    alert(mywindow);
    mywindow.location.href = 'doverUploadFile.do';
    if (mywindow.opener == null) mywindow.opener = self;
}


function newWindowUploadFile(ptextarea)
{
  var ancho=500;
  var alto=270;
  var vtextarea=ptextarea;

  if (window.showModalDialog)
  {
      var vReturnValue;
      vReturnValue = window.showModalDialog('doverUploadFile.do',"myname","scroll=no;dialogWidth:" + ancho + "px;dialogHeight:" + alto + "px");
    
      if(vReturnValue != null )
      {  
           
        if (vReturnValue.length !=0)
        {	               
        	var vtarea='#'+vtextarea;  
        	$(vtarea).wysiwyg('insertHtml', vReturnValue);        	      
       		 return true;
        }
        else
        {
        return false;
        }
        
      }
      else
      {   			       
        return false;
      }    
  }

}

function validarSiNumero(numero){
	if (!/^([0-9])*$/.test(numero))
		return 0;
	return numero; 
}
function NumberFormat(num, numDec, decSep, thousandSep){ 
    var arg; 
    var Dec; 
    Dec = Math.pow(10, numDec);  
    if (typeof(num) == 'undefined') return;  
    if (typeof(decSep) == 'undefined') decSep = ','; 
    if (typeof(thousandSep) == 'undefined') thousandSep = '.'; 
    if (thousandSep == '.') 
     arg=/./g; 
    else 
     if (thousandSep == ',') arg=/,/g; 
    if (typeof(arg) != 'undefined') num = num.toString().replace(arg,''); 
    num = num.toString().replace(/,/g, '.');  
    if (isNaN(num)) num = "0"; 
    sign = (num == (num = Math.abs(num))); 
    num = Math.floor(num * Dec + 0.50000000001); 
    cents = num % Dec; 
    num = Math.floor(num/Dec).toString();  
    if (cents < (Dec / 10)) cents = "0" + cents;  
    for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++) 
     num = num.substring(0, num.length - (4 * i + 3)) + thousandSep + num.substring(num.length - (4 * i + 3)); 
    if (Dec == 1) 
     return (((sign)? '': '-') + num); 
    else 
     return (((sign)? '': '-') + num + decSep + cents); 
   }  
   function EvaluateText(cadena, obj){ 
    opc = false;
    desactivarFlag();  
    if (cadena == "%d") 
     if (event.keyCode > 47 && event.keyCode < 58) 
      opc = true; 
    if (cadena == "%f"){  
     if (event.keyCode > 47 && event.keyCode < 58) 
      opc = true; 
     if (obj.value.search("[.*]") == -1 && obj.value.length != 0) 
      if (event.keyCode == 46) 
       opc = true; 
    } 
    if(opc == false) 
     event.returnValue = false;  
   } 
   

var hash = {   '.xls'  : 1};
function check_extension(filename,submitId) {
	var re = /\..+$/;       
	//var ext = filename.match(re);   
	var ext = filename.substring(filename.lastIndexOf("."));
	var submitEl = document.getElementById(submitId);       
	if (hash[ext]) {         
		submitEl.disabled = false;         
		return true;       
	} 
	else {         
		alert("Archivo Invalido, Por favor seleccione otro archivo");
		submitEl.disabled = true;
		return false;      
	}
} 
function redondeo2decimales(numero)
{
	var original=parseFloat(numero);
	var result=Math.round(original*100)/100 ;
	return result;
}

function confirmDelete()   
{  
     return confirm('Se encuentra seguro de eliminar el registro?');  
} 
	    
function SaveScrollXY(formulario) {
	var scrollX, scrollY;
    
    if (document.all)
    {
       if (!document.documentElement.scrollLeft)
          scrollX = document.body.scrollLeft;
       else
          scrollX = document.documentElement.scrollLeft;
            
       if (!document.documentElement.scrollTop)
          scrollY = document.body.scrollTop;
       else
          scrollY = document.documentElement.scrollTop;
    }  
    else
    {
       scrollX = window.pageXOffset;
       scrollY = window.pageYOffset;
    }

//    document.Form1.scrollLeft.value = scrollX;
//    document.Form1.scrollTop.value = scrollY;
	
	formulario.scrollX.value = scrollX;
	formulario.scrollY.value = scrollY;
}

function ResetScrollPosition(hidx, hidy ) {
//	var hidx, hidy;
//	hidx = document.forms[0].scrollX;
//	hidy = document.forms[0].scrollY;
	if (typeof hidx != 'undefined' && typeof hidy != 'undefined') {
		window.scrollTo(hidx, hidy);
	}
}

function acceptNum(evt){
	var evento = evt || window.event;
	if(window.event)//IE
	{
		key = evento.keyCode;
	}
	else if(evento.which)//Netscape/Firefox/Opera
	{
		key = evento.which;
	}
	if(key == 8){
		desactivarFlag();
		return true;
	}
	for (i=57; i>=48; i--){
		if (key==i){
			desactivarFlag();
			return true;
		}
	}
	return false;
}

function acceptNumComa(evt){
	var evento = evt || window.event;
	if(window.event)//IE
	{
		key = evento.keyCode;
	}
	else if(evento.which)//Netscape/Firefox/Opera
	{
		key = evento.which;
	}
	if(key == 8){
		desactivarFlag();
		return true;
	}
	//aceptar la coma
	if(key == 44){
		desactivarFlag();
		return true;
	}
	for (i=57; i>=48; i--){
		if (key==i){
			desactivarFlag();
			return true;
		}
	}
	
	
	return false;
}

function validarHora(campo, evt){
	var valor = campo.value;
	var e = evt;
	if(document.all){
		tecla=e.keyCode;
	}else{
		tecla=e.which;
	}
	if (tecla==8) return true;
	patron = /^(1|01|2|02|3|03|4|04|5|05|6|06|7|07|8|08|9|09|10|11|12)\:([0-5]0|[0-5][1-9])$/;
	var res = true;
	if(valor.length != 0){
		res = patron.test(valor);
		if(res == false){
			campo.value='';
			campo.focus();
		}
	}
	return res;
}

function validarCharHoras(campo){
	var valor = campo.value;
	patron = /[0-9\:]/;
	var res = true;
	if(valor.length>1){
		res = patron.test(valor);
		alert(res);
		alert(valor);
		if(res == false){
			alert(valor.length);
			if(valor.length>1){
			campo.value = valor.substring(0,valor.length-1);
			}
		}
	}
	return res;
}

function radio(clicked){
    var form = clicked.form;
    var checkboxes = form.elements[clicked.name];
    if (!clicked.checked || !checkboxes.length) {
        clicked.parentNode.parentNode.className="";
        return false;
    }

    for (i=0; i<checkboxes.length; i++) {
        if (checkboxes[i] != clicked) {
            checkboxes[i].checked=false;
            checkboxes[i].parentNode.parentNode.className="";
        }
    }

    // highlight the row    
    clicked.parentNode.parentNode.className="over";
    return true;
}


function esDigito(sChr){
	  var sCod = sChr.charCodeAt(0);
	  return ((sCod > 47) && (sCod < 58));
	  }
	 
	  function valSep(oTxt){
	  var bOk = false;
	  var sep1 = oTxt.value.charAt(2);
	  var sep2 = oTxt.value.charAt(5);
	  bOk = bOk || ((sep1 == "-") && (sep2 == "-"));
	  bOk = bOk || ((sep1 == "/") && (sep2 == "/"));
	  return bOk;
	  }
	 
	  function finMes(oTxt){
	  var nMes = parseInt(oTxt.value.substr(3, 2), 10);
	  var nAno = parseInt(oTxt.value.substr(6), 10);
	  var nRes = 0;
	  switch (nMes){
	   case 1: nRes = 31; break;
	   case 2: nRes = 28; break;
	   case 3: nRes = 31; break;
	   case 4: nRes = 30; break;
	   case 5: nRes = 31; break;
	   case 6: nRes = 30; break;
	   case 7: nRes = 31; break;
	   case 8: nRes = 31; break;
	   case 9: nRes = 30; break;
	   case 10: nRes = 31; break;
	   case 11: nRes = 30; break;
	   case 12: nRes = 31; break;
	  }
	  return nRes + (((nMes == 2) && (nAno % 4) == 0)? 1: 0);
}
	 
function valDia(oTxt){
	  var bOk = false;
	  var nDia = parseInt(oTxt.value.substr(0, 2), 10);
	  bOk = bOk || ((nDia >= 1) && (nDia <= finMes(oTxt)));
	  return bOk;
}
	 
function valMes(oTxt){
	  var bOk = false;
	  var nMes = parseInt(oTxt.value.substr(3, 2), 10);
	  bOk = bOk || ((nMes >= 1) && (nMes <= 12));
	  return bOk;
}
	 
function valAno(oTxt){
	  var bOk = true;
	  var nAno = oTxt.value.substr(6);
	  bOk = bOk && ((nAno.length == 2) || (nAno.length == 4));
	  if (bOk){
	   for (var i = 0; i < nAno.length; i++){
	   bOk = bOk && esDigito(nAno.charAt(i));
	   }
	  }
	  return bOk;
}
	 
function valFecha(oTxt){
	  var bOk = true;
	  if (oTxt.value != ""){
	   bOk = bOk && (valAno(oTxt));
	   bOk = bOk && (valMes(oTxt));
	   bOk = bOk && (valDia(oTxt));
	   bOk = bOk && (valSep(oTxt));
	   if (!bOk){
		   alert('Fecha Incorrecta');
		   oTxt.value = "";
		   oTxt.focus();
	   } 
	  }
}


function showLoading(){
	var div1 = document.getElementById('loading-mask');
	var div2 = document.getElementById('loading');
	try {
		div1.style.display = "";
		div2.style.display = "";
	}catch(exception) {}
}

function closeLoading(){
	var div1 = document.getElementById('loading-mask');
	var div2 = document.getElementById('loading');
	try {
		div1.style.display = "none";
		div2.style.display = "none";
	}catch(exception) {}
}



function CleanWordNew( oNode, bIgnoreFont, bRemoveStyles )
{
	var html = oNode.innerHTML ;

	html = html.replace(/<o:p>\s*<\/o:p>/g, '') ;
	html = html.replace(/<o:p>[\s\S]*?<\/o:p>/g, '&nbsp;') ;

	// Remove mso-xxx styles.
	html = html.replace( /\s*mso-[^:]+:[^;"]+;?/gi, '' ) ;

	// Remove margin styles.
	html = html.replace( /\s*MARGIN: 0(?:cm|in) 0(?:cm|in) 0pt\s*;/gi, '' ) ;
	html = html.replace( /\s*MARGIN: 0(?:cm|in) 0(?:cm|in) 0pt\s*"/gi, "\"" ) ;

	html = html.replace( /\s*TEXT-INDENT: 0(?:cm|in)\s*;/gi, '' ) ;
	html = html.replace( /\s*TEXT-INDENT: 0(?:cm|in)\s*"/gi, "\"" ) ;

	html = html.replace( /\s*TEXT-ALIGN: [^\s;]+;?"/gi, "\"" ) ;

	html = html.replace( /\s*PAGE-BREAK-BEFORE: [^\s;]+;?"/gi, "\"" ) ;

	html = html.replace( /\s*FONT-VARIANT: [^\s;]+;?"/gi, "\"" ) ;

	html = html.replace( /\s*tab-stops:[^;"]*;?/gi, '' ) ;
	html = html.replace( /\s*tab-stops:[^"]*/gi, '' ) ;

	// Remove FONT face attributes.
	if ( bIgnoreFont )
	{
		html = html.replace( /\s*face="[^"]*"/gi, '' ) ;
		html = html.replace( /\s*face=[^ >]*/gi, '' ) ;

		html = html.replace( /\s*FONT-FAMILY:[^;"]*;?/gi, '' ) ;
	}

	// Remove Class attributes
	html = html.replace(/<(\w[^>]*) class=([^ |>]*)([^>]*)/gi, "<$1$3") ;

	// Remove styles.
	if ( bRemoveStyles )
		html = html.replace( /<(\w[^>]*) style="([^\"]*)"([^>]*)/gi, "<$1$3" ) ;

	// Remove style, meta and link tags
	html = html.replace( /<STYLE[^>]*>[\s\S]*?<\/STYLE[^>]*>/gi, '' ) ;
	html = html.replace( /<(?:META|LINK)[^>]*>\s*/gi, '' ) ;

	// Remove empty styles.
	html =  html.replace( /\s*style="\s*"/gi, '' ) ;

	html = html.replace( /<SPAN\s*[^>]*>\s*&nbsp;\s*<\/SPAN>/gi, '&nbsp;' ) ;

	html = html.replace( /<SPAN\s*[^>]*><\/SPAN>/gi, '' ) ;

	// Remove Lang attributes
	html = html.replace(/<(\w[^>]*) lang=([^ |>]*)([^>]*)/gi, "<$1$3") ;

	html = html.replace( /<SPAN\s*>([\s\S]*?)<\/SPAN>/gi, '$1' ) ;

	html = html.replace( /<FONT\s*>([\s\S]*?)<\/FONT>/gi, '$1' ) ;

	// Remove XML elements and declarations
	html = html.replace(/<\\?\?xml[^>]*>/gi, '' ) ;

	// Remove w: tags with contents.
	html = html.replace( /<w:[^>]*>[\s\S]*?<\/w:[^>]*>/gi, '' ) ;

	// Remove Tags with XML namespace declarations: <o:p><\/o:p>
	html = html.replace(/<\/?\w+:[^>]*>/gi, '' ) ;

	// Remove comments [SF BUG-1481861].
	html = html.replace(/<\!--[\s\S]*?-->/g, '' ) ;

	html = html.replace( /<(U|I|STRIKE)>&nbsp;<\/\1>/g, '&nbsp;' ) ;

	html = html.replace( /<H\d>\s*<\/H\d>/gi, '' ) ;

	// Remove "display:none" tags.
	html = html.replace( /<(\w+)[^>]*\sstyle="[^"]*DISPLAY\s?:\s?none[\s\S]*?<\/\1>/ig, '' ) ;

	// Remove language tags
	html = html.replace( /<(\w[^>]*) language=([^ |>]*)([^>]*)/gi, "<$1$3") ;

	// Remove onmouseover and onmouseout events (from MS Word comments effect)
	html = html.replace( /<(\w[^>]*) onmouseover="([^\"]*)"([^>]*)/gi, "<$1$3") ;
	html = html.replace( /<(\w[^>]*) onmouseout="([^\"]*)"([^>]*)/gi, "<$1$3") ;

//	if ( FCKConfig.CleanWordKeepsStructure )
//	{
		// The original <Hn> tag send from Word is something like this: <Hn style="margin-top:0px;margin-bottom:0px">
		html = html.replace( /<H(\d)([^>]*)>/gi, '<h$1>' ) ;

		// Word likes to insert extra <font> tags, when using MSIE. (Wierd).
		html = html.replace( /<(H\d)><FONT[^>]*>([\s\S]*?)<\/FONT><\/\1>/gi, '<$1>$2<\/$1>' );
		html = html.replace( /<(H\d)><EM>([\s\S]*?)<\/EM><\/\1>/gi, '<$1>$2<\/$1>' );
//	}
//	else
//	{
		html = html.replace( /<H1([^>]*)>/gi, '<div$1><b><font size="6">' ) ;
		html = html.replace( /<H2([^>]*)>/gi, '<div$1><b><font size="5">' ) ;
		html = html.replace( /<H3([^>]*)>/gi, '<div$1><b><font size="4">' ) ;
		html = html.replace( /<H4([^>]*)>/gi, '<div$1><b><font size="3">' ) ;
		html = html.replace( /<H5([^>]*)>/gi, '<div$1><b><font size="2">' ) ;
		html = html.replace( /<H6([^>]*)>/gi, '<div$1><b><font size="1">' ) ;

		html = html.replace( /<\/H\d>/gi, '<\/font><\/b><\/div>' ) ;

		// Transform <P> to <DIV>
		var re = new RegExp( '(<P)([^>]*>[\\s\\S]*?)(<\/P>)', 'gi' ) ;	// Different because of a IE 5.0 error
		html = html.replace( re, '<div$2<\/div>' ) ;

		// Remove empty tags (three times, just to be sure).
		// This also removes any empty anchor
		html = html.replace( /<([^\s>]+)(\s[^>]*)?>\s*<\/\1>/g, '' ) ;
		html = html.replace( /<([^\s>]+)(\s[^>]*)?>\s*<\/\1>/g, '' ) ;
		html = html.replace( /<([^\s>]+)(\s[^>]*)?>\s*<\/\1>/g, '' ) ;
//	}

	return html ;
}
	 
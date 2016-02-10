package pe.com.bbva.iipf.util;

public interface Constantes {
	
	public static final String ID_PROGRAMA_SESSION = "id_programa_session";
	public static final String COD_RUC_EMPRESA_SESSION = "cod_ruc_empresa_session";
	public static final String COD_CENTRAL_EMPRESA_SESSION = "cod_central_empresa_session";
	public static final String COD_TIPO_EMPRESA_SESSION = "cod_tipo_empresa_session";
	public static final String NOMBRE_EMPRESA_GRUPO_SESSION = "nombre_empresa_grupo_session";
	public static final String COD_GRUPO_SESSION = "cod_grupo_session";
	public static final String ID_TIPO_PROGRAMA_SESSION = "id_tipo_programa";
	public static final String ANIO_PROGRAMA_SESSION = "anio_programa_session";
	public static final String PARAMETROS_SESSION = "parametros_session";
	public static final String USUARIO_SESSION = "usuario_session";
	public static final String NOMBRE_EMPRESA_PRINCIPAL = "nombre_empresa_principal";
	public static final String PERMISO_OPCIONES_SESSION = "permiso_opciones_session";
	public static final String OPCION_PADRE_ACTUAL = "opcion_padre_actual";
	public static final String LISTA_GRUPO_EMPRESAS_SESSION = "lista_grupo_empresas_session";
	
	public static final String LISTA_MOTIVOS_CIERRE_SESSION = "lista_motivos_cierre_session";
	
	public static final String COD_GRUPOEMPRESA_RDC_SESSION = "cod_grupoempresa_rdc_session";
	
	public static final String COLUMN_RIESGO_VIGENTE ="RGO ACTUAL";
	public static final String COLUMN_LIMITE_AUTORIZADO ="LTE AUTORIZADO";
	public static final String COLUMN_RIESGO_PROPUESTO ="RGO PROP BBVA BC";
	public static final String COLUMN_OBSERVACIONES ="OBSERVACIONES";
	public static final String COLUMN_LIMITE_FORM ="LTE FORM";

	
	//variables teclas de host
	public static final String TECLA_HOST_CTRL = "00";
	public static final String TECLA_HOST_F8 = "08";
	
	
	//MCG
	public static final String USUARIO_SESSIONLDAP = "usuario_session_ldap";
	
	//codigo bbva
	public static final String COD_BBVA = "006";
	public static final Long ID_TIPO_EMPRESA_EMPR=2L;
	public static final Long ID_TIPO_EMPRESA_GRUPO=3L;
	
	
	public static final Long ID_TIPO_PROGRAMA_LOCAL = 5L;
	public static final Long ID_TIPO_PROGRAMA_CORPORATIVO = 6L;
	
	public static final Long ID_PLANILLA_ADMINISTRATIVO = 8L;
	public static final Long ID_PLANILLA_NO_ADMINISTRATIVO = 9L;
	
	public static final Long ID_TIPO_PERIODO_MENSUAL = 11L;//	0041	MENSUAL	10
	public static final Long ID_TIPO_PERIODO_BIMESTRAL = 12L;//	0042	BIMESTRAL	10
	public static final Long ID_TIPO_PERIODO_TRIMESTRAL = 13L;//	0043	TRIMESTRAL	10
	public static final Long ID_TIPO_PERIODO_SEMESTRAL = 14L;//	0044	SEMESTRAL	10
	public static final Long ID_TIPO_PERIODO_ANUAL = 15L;//	0045	ANUAL	10
	
	public static final Long ID_TIPO_EMPRESA_PRINCIPAL = 34L;
	public static final Long ID_TIPO_EMPRESA_SECUNDARIA = 35L;
	public static final Long ID_TIPO_EMPRESA_ANEXA = 36L;
	
	//tipos de documento
	public static final String VAL_TIPO_DOC_RUC = "2";
	public static final String VAL_TIPO_DOC_COD_CENTRAL = "3";
	public static final String VAL_TIPO_DOC_DNI = "4";
	
	//Tipo de compra y venta
	public static final String EXPORTACIONES = "1";
	public static final String IMPORTACIONES = "2";
	public static final String COMPRA_VENTA_TOTAL_PORCENTAJE = "1";
	public static final String COMPRA_VENTA_TOTAL_ME = "2";
	//tipo de negocio y beneficio
	public static final String POR_LINEA_ACTIVIDAD = "1";
	public static final String POR_LINEA_NEGOCIO = "2";
	//public static final String NEGOCIO_BENEFICIO_TOTAL = "3";
	
	//PARAMETROS DEL SISTEMA
	public static final String COD_ENLACE_BBVASOAP_ADDRESS = "COD_ENLACE_BBVASOAP_ADDRESS";
	public static final String COD_SPFI_PEC6SOAP_HTTP_PORT_ADDRESS = "COD_SPFI_PEC6SOAP_HTTP_PORT_ADDRESS";
	public static final String VAL_PARAMETRO_DIR_FILES = "DIR_FILES";
	public static final String VAL_PARAMETRO_SIZE_MAX_FILE = "SIZE_MAX_FILES";
	
	public static final String COD_PARAMETRO_DIR_FILES="DIR_FILE_ANALSIS_SECTORIAL";
	public static final String COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO="DIR_FILE_SINTESIS_ECONOMICO";
	public static final String MAX_SIZE_TEXT_ACCIONARIADO = "MAX_SIZE_TEXT_ACCIONARIADO";
	public static final String MAX_SIZE_TEXT_PARTICIPACIONES = "MAX_SIZE_TEXT_PARTICIPACIONES";
	public static final String MAX_SIZE_TEXT_RATING_EXTERNO = "MAX_SIZE_TEXT_RATING_EXTERNO";
	public static final String MAX_SIZE_TEXT_VALORACION_GLOBAL = "MAX_SIZE_TEXT_VALORACION_GLOBAL";
	public static final String MAX_SIZE_TEXT_ACTIVIDAD_PRINCIPAL = "MAX_SIZE_TEXT_ACTIVIDAD_PRINCIPAL";
	public static final String DIR_PLANTILLA_EXCEL_SINTESIS_ECONOMICO = "DIR_PLANTILLA_EXCEL_SINTESIS_ECONOMICO";
	public static final String DIR_TEMPORAL = "DIR_TEMPORAL";
	public static final String DIR_FILES_CPY_HOST = "DIR_FILES_CPY_HOST";
	public static final String DIR_FILES_ANEXOS = "DIR_FILE_ANEXOS";
	public static final String NOMBRE_PLANTILLA_EXCEL_SINTESIS_ECONOMICO= "plantilla_sintesis_economico.xls";
	
	public static final String MAX_SIZE_TEXT_CUOTAFINANCIERA = "MAX_SIZE_TEXT_CUOTAFINANCIERA";
	public static final String COD_SISTEMA_CODIFICACION = "SYSCOD";
	
	//Generacion de Programa Finaciero
	public static final String DIR_BUSCAR_ANALISISSECTORIAL = "DIR_BUSCAR_ANALISISSECTORIAL";
	//public static final String DIR_PLANTILLA_SINTESISECONFIN = "DIR_PLANTILLA_SINTESISECONFIN";
	public static final String DIR_EXPPDF_PROGRAMA_FINANCIERO = "DIR_EXPPDF_PROGRAMA_FINANCIERO";
	
	public static final String DIR_UPLOADFILE_URL = "DIR_UPLOADFILE_URL";
	public static final String DIR_UPLOAD_IMAGEN_PDF = "DIR_UPLOAD_IMAGEN_PDF";
	
	
	//calificacion Bancaria
	
	public static final String CALIFICACION_NORMAL="Nomal(0)";
	public static final String CALIFICACION_PROBLEMAPOTENCIAL="Con Problemas Potenciales (CPP)(1)";
	public static final String CALIFICACION_DEFICIENTE="Deficiente(2)";
	public static final String CALIFICACION_DUDOSO="Dudoso(3)";
	public static final String CALIFICACION_PERDIDA="Perdida(4)";
	
	public static final String ID_CALIFICACION_NORMAL="0";
	public static final String ID_CALIFICACION_PROBLEMAPOTENCIAL="1";
	public static final String ID_CALIFICACION_DEFICIENTE="2";
	public static final String ID_CALIFICACION_DUDOSO="3";
	public static final String ID_CALIFICACION_PERDIDA="4";
	
	//Pool Bancario
	public static final String TIPO_DOCUMENTO_RUC="R";
	public static final String ID_TIPO_DEUDA_TOTAL="1";
	public static final String ID_TIPO_DEUDA_DIRECTA="2";
	public static final String ID_TIPO_DEUDA_INDIRECTA="3";
	
	public static final String TIPO_DEUDA_TOTAL="DEUDA TOTAL";
	public static final String TIPO_DEUDA_DIRECTA="DEUDA DIRECTA";
	public static final String TIPO_DEUDA_INDIRECTA="DEUDA INDIRECTA";
	
	public static final String ID_TIPO_OPPOOL_EMPRESA="1";
	public static final String ID_TIPO_OPPOOL_TIPODEUDA="2";
	public static final String ID_TIPO_OPPOOL_BANCO="3";
	
	//Rentabilida BEC
	public static final String ID_TIPO_COMERCIAL="1";
	public static final String ID_TIPO_RENTABILIDAD="2";
	
	public static final String ID_TIPO_BANCAEMPRESA="1";
	public static final String ID_TIPO_CLIENTEGLOBAL="2";
	public static final String TIPO_BANCAEMPRESA="BANCA EMPRESA (INFORMACIÓN ACUMULADA)";
	public static final String TIPO_CLIENTEGLOBAL="Cliente Global";
	
	
	public static final String SALDOMEDIO_INVERSIONES="Saldo Medio Inversiones"; 
	public static final String SALMEDIO_INVERSION_ACU="Saldo Medio Acumulado";
	public static final String SALDOMEDIO_RECURSOS="Saldo Medio Recursos";
	public static final String SALDOMEDIO_DESINTERMED="Saldo Medio Desintermediacion";
	public static final String IMPORTE_SERVICIO="Importe Servicios";
	public static final String CUENTA_CENTRAL="Cuenta Central";
	public static final String SPFMES_INVERSION="S.P.F Mes Inversión";
	public static final String SPFMES_RECURSOS="S.P.F Mes Recursos";
	public static final String SPFMES_DESINTERMED="S.P.F Mes Desintermediación";
	public static final String MARGEN_FINANCIERO="Margen Financiero";
	public static final String COMISIONES="Comisiones";
	public static final String MARGEN_ORDINARIO="Margen Ordinario";
	public static final String SANEAMIENTO_NETO="Saneamiento Neto";
	public static final String COSTES_OPERATIVOS="Costes Operativos";
	public static final String RESULTADOS="Resultados";
	public static final String ROA="ROA";
	public static final String swRefresh="swRefresh";
	
	//para caratula
	public static final String NAMEGROUPBBVA="NAMEGROUPBBVA"; 
	public static final String FORMATFECHABD="FORMATFECHABD"; 
	
	public static final String NAMEGLOBAL="NAMEGLOBAL";
	public static final String FIRMABANCO="FIRMABANCO";
	public static final String PIEPAGINAPROGRAMFINANCIERO="PIEPAGINAPROGRAMFINANCIERO";
	
	public static final String ESCALA_MAESTRA="ESCALA MAESTRA";
	
	
	//tabla html
	public static final String ID_SIADD_TOTAL="1";
	public static final String ID_NOADD_TOTAL="0";
	
	public static final String ID_SIADD_CABECERA="1";
	public static final String ID_NOADD_CABECERA="0";
	
	//efitividad cartera
	public static final String ID_CODIGO_SERVICIO="50";
	
	//Tabla de Tablas
	public static final Long ID_TABLA_TIPO_RIESGO=4726L;
	public static final Long ID_TABLA_TIPO_MENU = 4801L;
	public static final Long ID_TABLA_PERFIL = 5281L;
	public static final Long ID_TABLA_NIVEL_ACCESO = 5421L;
	public static final Long ID_TABLA_BANCOS = 16L;
	public static final Long ID_TABLA_BANCOS_EXTERNOS = 38L;
	public static final Long ID_TABLA_OPERACIONES= 37L;
	public static final Long ID_TABLA_LISTA_LIMITES= 20500L;
	
	public static final Long ID_TABLA_TIPO_MOTIVO = 20800L;
	
	//Estado de Programa
	public static final Long ID_ESTADO_PROGRAMA_PENDIENTE = 9617L;
	public static final Long ID_ESTADO_PROGRAMA_CERRADO = 8203L;
		
	//Menus Padres
	public static final Long ID_MENU_PADRE_SEGURIDAD = 10218L;
	public static final Long ID_MENU_PADRE_MANTENIMIENTO = 10222L;
	
	//Otros
	public static final Integer INDEX_HOJA_INI_EMP_EF = 12;
	
	//Nombres de archivos
	public static final  String FILE_REPO_XLS_05  = "reporteP05.xls";
	public static final  String FILE_REPO_XLS_10  = "reporteP10.xls";
	public static final  String FILE_REPO_XLS_20  = "reporteP20.xls";
	public static final  String FILE_REPO_XLS_30  = "reporteP30.xls";
	
	
	//MCG Nivel de Acceso
	public static final  String NIVELACCESO_REGISTRO  = "R";
	public static final  String NIVELACCESO_CARGO  = "C";
	public static final  String NIVELACCESO_OFICINA  = "O";
	
	//sintesis economico financiero
	public static final int NUM_SHEET_FINANCIERO = 4;
	public static final int NUM_SHEET_BALANCE = 1;
	public static final int NUM_SHEET_RTDOS_CASH = 2;
	public static final int NUM_SHEET_EXTRACTO= 0;
	
	public static final String ABREV_NOMB_BANCO_BBVA = "BBVA";
	public static final String ABREV_NOMB_BANCO_CTABBVA = "CTABBVA";
	
	public static final String ABREV_NOMB_BANCO_CTA = "CTA";
	public static final String ABREV_NOMB_BANCO_SBS = "SBS";
	
	// TIPOS DE TABLA
	public static final Long ID_TIPO_TABLA_PLANILLA = 40L;//	00XX	PANILLA	X
	public static final Long ID_TIPO_TABLA_COMPRA = 41L;//	00XX	COMPRA	X
	public static final Long ID_TIPO_TABLA_VENTA = 42L;//	00XX	COMPRA	X
	public static final Long ID_TIPO_TABLA_ACTIVIDAD = 43L;//	00XX	COMPRA	X
	public static final Long ID_TIPO_TABLA_NEGOCIO = 44L;//	00XX	COMPRA	X
	
	//TIPO DE LISTA PARA SITUACION ECONOMICA FINANCIERA
	
	public static final String TIPO_NORMAL="TIPONORMAL";
	public static final String TIPO_ESPECIFICO="TIPOESPECIFICO";
	public static final String TIPO_ESPECIFICODB="TIPOESPECIFICODB";
	public static final String TIPO_ESPECIFICORATING="TIPOESPECIFICORATING";
	
	//TABLA DE TIPO DE MILES
	public static final Long ID_TABLA_TIPOMILES = 45L;
	public static final String ID_TABLA_TIPOMILES_MILES = "46";
	public static final String ID_TABLA_TIPOMILES_MILLONRES = "47";
	
	//Tipo Estructura
	public static final String ID_TIPO_ESTRUCTURA_LIMITE="1";
	public static final String ID_TIPO_ESTRUCTURA_COMENTARIO="2";
	
	//Ficheros utiles para la generacion del reporte excel p05
	public static final String BLANCO_DOC = "blanco.doc";
	public static final String SCRIPT_BASIC = "patchFile.vbs";
	
	//Session
	public static final String MAX_TIME_SESSION = "MAX_TIME_SESSION";
	
	//MCG20121030
	public static final String NUM_SOLICITUD_SESSION = "num_solicitud_session";
	
	public static final String COD_DEFAULT_SELECTION = "";
	public static final String COD_DEFAULT_SELECTION2 = "-1";
	public static final String VAL_DEFAULT_SELECTION = " SELECCIONE ";
	public static final String VAL_DEFAULT_SELECTION_TODOS = " TODOS ";
	
	public static final String VAL_ACTIVO = "A";
	public static final String VAL_INACTIVO = "I";
	public static final Long ID_TABLA_TIPOSEGMENTO			= 71L;
	
	public static final Long ID_TABLA_SEGMENTO = 20180L;
	public static final Long ID_TABLA_OFICINABEC = 20190L;
	public static final Long ID_TABLA_CUENTA =20300L;
	public static final Long ID_TABLA_MONEDA =20400L;
	public static final Long ID_TABLA_VCTO_RMBLSO =20403L;
	public static final Long ID_TABLA_REEMBLSO =20406l;
	
	public static final Long ID_TABLA_DIVISADB =20820l;	
	
	public static final String MAX_SIZE_TEXT_VENCIMIENTO = "MAX_SIZE_TEXT_VENCIMIENTO";
	public static final String MAX_SIZE_TEXT_NOTA_CLASECRED = "MAX_SIZE_TEXT_NOTA_CLASECRED";
	public static final String MAX_SIZE_TEXT_COMENT_GARANTIA = "MAX_SIZE_TEXT_COMENT_GARANTIA";
	public static final String MAX_SIZE_TEXT_SUSTENTO_OPER = "MAX_SIZE_TEXT_SUSTENTO_OPER";
	public static final String PAR_ACCIONISTA_PORCENTAJE_MINIMO="PAR_ACCIONISTA_PORCENTAJE_MINIMO";
	public static final String MAX_SIZE_TEXT_COMENT_ADMISION="MAX_SIZE_TEXT_COMENT_ADMISION";
	public static final String MAX_SIZE_TEXT_POSICION_CLIENTE="MAX_SIZE_TEXT_POSICION_CLIENTE";
	public static final String MAX_SIZE_TEXT_VULNERABILIDAD="MAX_SIZE_TEXT_VULNERABILIDAD";
	
	public static final Long ID_TABLA_TIPOMILESDEFAULT = 46L;
	public static final String PREFIJO_ARCHTIPOCAMBIO = "PREFIJO_ARCHTIPOCAMBIO";
	public static final String NOM_BANCO_BBVA = "NOM_BANCO_BBVA";
	public static final String NOM_BANCO_DEFAULT = "BBVA BANCO CONTINENTAL";
	public static final String NOM_RATING = "ESCALA MAESTRA";
	
	public static final String MES_PERMANENCIAFILE = "MES_PERMANENCIAFILE";
	public static final String EXT_ARCH_TIPOCAMBIO = "EXT_ARCH_TIPOCAMBIO";
	public static final String PREFIJO_ARCHRENBEC = "PREFIJO_ARCHRENBEC";
	public static final String EXT_ARCH_RENBEC = "EXT_ARCH_RENBEC";
	public static final String PREFIJO_ARCHRENBECANUAL = "PREFIJO_ARCHRENBECANUAL";
	public static final String EXT_ARCH_RENBECANUAL = "EXT_ARCH_RENBECANUAL";	
	//mcg2014
	public static final String PREFIJO_ARCHPRESTAMO = "PREFIJO_ARCHPRESTAMO";
	public static final String EXT_ARCH_PRESTAMO = "EXT_ARCH_PRESTAMO";
	public static final String FORMAT_FECHA_PRESTAMO = "FORMAT_FECHA_PRESTAMO";
	
	
	public static final String TIPO_CUENTA_M="20305";
	public static final String TIEMPO_AUTOGUARDADO="TIEMPO_AUTOGUARDADO";
	
	
	public static final String CORREO_IP_SERVER_SMTP	=	"CORREO_IP_SERVER_SMTP";
	public static final String CORREO_PORT_SERVER_SMTP	=	"CORREO_PORT_SERVER_SMTP";
	public static final String CORREO_USER_SERVER_SMTP	=	"CORREO_USER_SERVER_SMTP";
	public static final String CORREO_PSWD_SERVER_SMTP	=	"CORREO_PSWD_SERVER_SMTP";
	public static final String CORREO_SSL_SERVER_SMTP	=	"CORREO_SSL_SERVER_SMTP";
	public static final String CORREO_FROM_SERVER_SMTP	=	"CORREO_FROM_SERVER_SMTP";
	public static final String CORREO_DE				=	"CORREO_DE";
	public static final String CORREO_PARA				=	"CORREO_PARA";
	public static final String CORREO_COPIA				=	"CORREO_COPIA";
	public static final String CORREO_OCULTO			=	"CORREO_OCULTO";
	
	public static final String CORREO_SEPARADOR			=	";";
	public static final String CORREO_ASUNTO			=	"CORREO_ASUNTO";
	public static final String CORREO_CUERPO			=	"CORREO_CUERPO";
	public static final String CORREO_FORMATO			=	"CORREO_FORMATO";
	public static final String CORREO_FORMATO_HTML		=	"HTML"; 
	public static final String CORREO_USUARIO_ENVIO		=	"CORREO_USUARIO_ENVIO";
	
	
	public static final String CODIGO_OFICINA_CUENTA		=	"CODIGO_OFICINA_CUENTA";
	public static final String CODIGO_EJECUTIVO_CUENTA		=	"CODIGO_EJECUTIVO_CUENTA";
	public static final String TERMINAL_LOGICO				=	"TERMINAL_LOGICO";
	public static final String TERMINAL_CONTABLE			=	"TERMINAL_CONTABLE";
	public static final String CODIGO_APLICACION			=	"CODIGO_APLICACION";
	
	
	
	
	public static final Integer ARCHIVO_RATING	=1;
	public static final Integer ARCHIVO_RENBEC	=2;
	public static final Integer ARCHIVO_SUNAT	=3;
	public static final Integer ARCHIVO_CARTERA	=4;	
	public static final Integer ARCHIVO_RCCANUAL=5;
	public static final Integer ARCHIVO_RCCMES	=6;	
	public static final Integer ARCHIVO_RCD		=7;
	public static final Integer ARCHIVO_RVWCG010=8;	
	public static final Integer ARCHIVO_RVTC001=9;
	public static final Integer ARCHIVO_TIPO_CAMBIO=10;
	public static final Integer ARCHIVO_RENBEC_ANUAL=11;
	//mcg2014
	public static final Integer ARCHIVO_PRESTAMO=12;
	
	public static final  String CODIGO_TIPOMONEDA  = "M00400";
	public static final  String CODIGO_MONEDAD_USA  = "M004002";
	
	public static final String COD_MONITORJOB_RVWCG010 = "MONITORJOB_RVWCG010";
	public static final String COD_MONITORJOB_RVTC001  = "MONITORJOB_RVTC001";
	public static final String COD_MONITORJOB_TIPOCAMBIO  = "MONITORJOB_TIPOCAMBIO";
	public static final String COD_MONITORJOB_RENBECANUAL  = "MONITORJOB_RENBECANUAL";
	public static final String COD_MONITORJOB_RENBECMENSUAL = "MONITORJOB_RENBECMENSUAL";
	public static final String COD_MONITORJOB_RATING = "MONITORJOB_RATING";
	public static final String COD_MONITORJOB_CARTERA = "MONITORJOB_CARTERA";	
	public static final String COD_MONITORJOB_SUNAT = "MONITORJOB_SUNAT";
	public static final String COD_MONITORJOB_RCCANUAL = "MONITORJOB_RCCANUAL";
	public static final String COD_MONITORJOB_RCCMES = "MONITORJOB_RCCMES";	
	public static final String COD_MONITORJOB_RCD= "MONITORJOB_RCD";
	
	//mcg2014
	public static final String COD_MONITORJOB_PRESTAMO  = "MONITORJOB_PRESTAMO";
	
	public static final Long ID_TIPO_CUENTA_E = 20302L;
	public static final Long ID_TIPO_CUENTA_R = 20303L;
	public static final Long ID_TIPO_CUENTA_P = 20304L;
	
	public static final String URL_RIG4_PF ="URL_RIG4_PF";
	
	public static final String TIPOCARGA_AUTOMATICO = "A";
	public static final String TIPOCARGA_MANUAL = "M";
	
	public static final Long ID_TIPO_GRUPO_POLITICAS_RIESGO= 4732L;
	public static final String TIPO_LETRA_PF = "TIPO_LETRA_PF";
	public static final String TAMANO_LETRA_PF= "TAMANO_LETRA_PF";
	
	public static final String DESC_ANEXORATING = "RATING";
	public static final String DESC_ANEXORATINGFECHA = "FECHA";
	public static final String DESC_COLUMNA_PROP_RIESGO = "PROP RIESGO";
	public static final String DESC_BUREAU = "BURO";//Bureau
	
	/*
	 *     <option value=""> SELECCIONAR </option>
    <option value="1"> PFRATING.txt </option>
    <option value="2"> PFRENBEC.txt </option>
    <option value="3"> PFSUNAT.txt </option>
    <option value="4"> PFCARTERA.txt </option>
    <option value="5"> PFRCCAN.txt </option>
    <option value="6"> PFRCCMES.txt </option>
    <option value="7"> PFRCD.txt </option>
    <option value="8"> PFRVWCG010.txt </option>
    <option value="9"> PFRVTC001.txt </option>
    <option value="10"> TCCOBYYYYMMDD.TXT </option>
	 * 
	 * */
	
	public static final Long ID_TIPO_CUENTA_M =20305L;
	public static final String VALOR_FLAGTIPO_SUBLIMITE ="SL";
	public static final String VALOR_FLAGTIPO_LIMITEOPERACION ="LO";
	
	public static final String CODACCESOEXEC = "ACCESOEXEC";
	public static final String ID_ACCESOEXEC = "1";
	public static final String CLAVE_ENCRYPT = "34i76";
	public static final String PASWW_EXEC="Syp8badiQJGSt/9th0USuQ==";
	public static final String DESC_ERROR_HOST_CLIENTENOEXISTE = "RIE02521ERROR LINK A PE2C520ERROR LINK A PE2C520";
	public static final String COD_CORRECTO_HOST = "0000";
	
	public static final String COD_PATRONES_EDITOR = "CODPATRONESEDITOR";
	public static final String NOMBRE_PLANTILLA_EXCEL_REPORTECONTROLPF= "plantillaReporteControl.xls";
	
	public static final String MAX_SIZE_TEXT_COMENT_GARANTIAANEXO = "MAX_SIZE_TEXT_COMENT_GARANTIAANEXO";
	
	
//	public static final String TITULO_INDICE_DATOS_BASICOS = "1.- DATOS BASICOS";
//	public static final String TITULO_INDICE_SINTESIS_ECON_FIN = "2.- SINTESIS ECONOMICA - FINANCIERA";
//	public static final String TITULO_INDICE_RATING = "3.- RATING BBVA CONTINENTAL";
//	public static final String TITULO_INDICE_EXTRACTO_SINT_ECON = "4.- EXTRACTO SINTESIS ECONOMICA - FINANCIERA";
//	public static final String TITULO_INDICE_INFORMACION_X_EMP = "5.- INFORMACION POR EMPRESA";
//	public static final String TITULO_INDICE_ANALISIS_SECTO = "6.- ANALISIS SECTORIAL";
//	public static final String TITULO_INDICE_RELACIONES_BANC = "7.- RELACIONES BANCARIAS";
//	public static final String TITULO_INDICE_FACTORES_RIESGO = "8.- FACTORES DE RIESGO. CONCLUSIONES";
//	public static final String TITULO_INDICE_PROPUESTAS_RIESGO = "9.- PROPUESTAS DE RIESGO";
//	public static final String TITULO_INDICE_POLITICA_RIESGO = "10.- POLITICA DE RIESGO";
//	public static final String TITULO_INDICE_POSICION_PRINC = "11.- POSICION PRINCIPAL";
	
	public static final String COD_INDICE_INFORMACION_DATOS_GRUPO = "CODPF2014IDG001";
		
	public static final String COD_INDICE_DATOS_BASICOS = "CODPF2014BD001"; //"1.- DATOS BASICOS";
	public static final String COD_INDICE_SINTESIS_ECON_FIN = "CODPF2014SE002"; //"2.- SINTESIS ECONOMICA - FINANCIERA";
	public static final String COD_INDICE_EXTRACTO_SINT_ECON = "CODPF2014EX003";//"4.- EXTRACTO SINTESIS ECONOMICA - FINANCIERA";
	public static final String COD_INDICE_RATING = "CODPF2014RT004";//"3.- RATING BBVA CONTINENTAL";
	
	public static final String COD_INDICE_INFORMACION_X_EMP = "CODPF2014IE005";//"1.- INFORMACION POR EMPRESA";
	
		public static final String COD_SUBINDICE_DATOS_BASICOS = "CODPF2014SUBBD001"; //"1.1.- DATOS BASICOS";
		public static final String COD_SUBINDICE_SINTESIS_ECON_FIN = "CODPF2014SUBSE002"; //"1.2.- SINTESIS ECONOMICA - FINANCIERA";
		public static final String COD_SUBINDICE_EXTRACTO_SINT_ECON = "CODPF2014SUBEX003";//"1.3.- EXTRACTO SINTESIS ECONOMICA - FINANCIERA";
		public static final String COD_SUBINDICE_RATING = "CODPF2014SUBRT004";//"1.4.- RATING BBVA CONTINENTAL";
		
	
	public static final String COD_INDICE_ANALISIS_SECTO = "CODPF2014AS006";//"6.- ANALISIS SECTORIAL";
	public static final String COD_INDICE_RELACIONES_BANC = "CODPF2014RB007";//"7.- RELACIONES BANCARIAS";
	public static final String COD_INDICE_FACTORES_RIESGO = "CODPF2014FR008";//"8.- FACTORES DE RIESGO. CONCLUSIONES";
	public static final String COD_INDICE_PROPUESTAS_RIESGO = "CODPF2014PRR009";//"9.- PROPUESTAS DE RIESGO";
	public static final String COD_INDICE_POLITICA_RIESGO = "CODPF2014POR010";//"10.- POLITICA DE RIESGO";
	public static final String COD_INDICE_POSICION_PRINC = "CODPF2014PP011";//"11.- POSICION PRINCIPAL";
	
	public static final String COD_INDICE_ANEXOPF = "CODPF2014ANEXO012";
	public static final String COD_SUBINDICE_ARCHIVOANEXO = "CODPF2014SUBANEXO001"; 
	
	public static final String COD_SUBINDICE_INFORM_X_EMPRESA = "CODPF2014SIE0051";//"5.1.- EMPRESA X";
	
	
	
	
	
	public static final String IDENTIFICADOR_INFORM_X_EMPRESA = "5";
	public static final String IDENTIFICADOR_ARCHIVO_ANEXO = "12";
	

	public static final String MENSAJE_NO_EXISTE_INFORMACION = "<<No se tiene información>>";
	
	public static final Long ID_TABLA_TIPO_CONECTOR =100L;
	public static final Long ID_TABLA_TIPO_CONECTOR_NINGUNO =103L;	
	public static final Long ID_TABLA_TIPO_CONECTOR_DEFAULT =101L;	
	
	public static final Long ID_TABLA_TIPODOCUMENTO =200L;
	
	public static final String MENS_SIN_INFORMACION_ACCIONISTA = "No se obtuvo datos del Accionista: Ingrese Accionista manualmente...";

	public static final String COD_FLAG_ACTIVO_ANEXO="COD_FLAG_ACTIVO_ANEXO";
	
	public static final String TIEMPO_SESSION_TIMEOUT="TIEMPO_SESSION_TIMEOUT";
	public static final String PATH_URL_CERRARSESSION="PATH_URL_CERRARSESSION";
	
	public static final String COD_PADRE_EQUIV_CLASBANCO = "ECB00300";
	public static final String COD_PADRE_VALOR_WSRATING = "RWS00400";
	
	 public static final String COD_HIJO_WSRATING_TIPO_EMPRESA="RWS004001";//I	
	 public static final String COD_HIJO_WSRATING_TIPO_GRUPO="RWS004002";//	C	
	 public static final String COD_HIJO_WSRATING_OPCION_APLICACION="RWS004003"	;//00	
	 public static final String COD_HIJO_WSRATING_TERMINAL_LOGICO	="RWS004004";	//D90L	
	 public static final String COD_HIJO_WSRATING_TERMINAL_CONTABLE="RWS004005"	;//D90L	
	 public static final String COD_HIJO_WSRATING_ITDA_APLICATIVO	="RWS004006";//RT
	 public static final String COD_HIJO_WSRATING_ITDA_TRATAMIENTO="RWS004007"	;//C
	 public static final String COD_HIJO_WSRATING_XGR7_TRATAMIENTO="RWS004008"	;//C
	 public static final String COD_HIJO_WSRATING_ITCU_TRANSACCION="RWS004009"	;//ITCU	
	 public static final String COD_HIJO_WSRATING_ITCU_TIPO_MENSAJE="RWS004010"	;//1
	 public static final String COD_HIJO_WSRATING_ITCU_TIPO_PROCESO="RWS004011"	;//O	
	 public static final String COD_HIJO_WSRATING_ITCU_CANAL_COMUNICACION="RWS004012"	;//PF	
	 public static final String COD_HIJO_WSRATING_ITCU_INDICADOR_PREFORMATO="RWS004013"	;//N	
	 public static final String COD_HIJO_WSRATING_ITCU_TIPO_MENSAJE_ME="RWS004014"	;//B	
	 public static final String COD_HIJO_WSRATING_ITCU_DIST_QNAME_IN="RWS004015"	;//QLT.GEC.RESPA
	 public static final String COD_HIJO_WSRATING_ITCU_DIST_RQNAME_OUT="RWS004016"	;//QRT.GEC.ENVIO.MPD1
	 public static final String COD_HIJO_WSRATING_ITCU_HOST_RQNAME_OUT="RWS004017"	;//QRT.GEC.RESP.QMPCINT8	
	 public static final String COD_HIJO_WSRATING_ITCU_HOST_QMGR_NAME="RWS004018"	;//MPD1
	 public static final String COD_HIJO_WSRATING_ITCU_TIPO_EMPRESA="RWS004019"	;//0
	 public static final String COD_HIJO_WSRATING_ITCU_OPCION="RWS004020"	;//	1
	 
	 public static final String COD_HIJO_WSRATING_ITDA_IP_PORT="RWS004021"	;//	http://118.180.36.26:7801
	 public static final String COD_HIJO_WSRATING_ITCU_IP_PORT="RWS004022"	;//	http://118.180.36.26:7808
	 public static final String COD_HIJO_WSRATING_ITCU_ENCODING="RWS004023"	;//	IBM-500
	 
	 
	 //PE21
	 public static final String COD_PADRE_VALOR_WSPE21 = "PWS00500";
	 
	 public static final String COD_HIJO_WSPE21_OPCION_APLICACION="PWS005001"	;//00	
	 public static final String COD_HIJO_WSPE21_TERMINAL_LOGICO	="PWS005002";	//D90L	
	 public static final String COD_HIJO_WSPE21_TERMINAL_CONTABLE="PWS005003"	;//D90L	

	 public static final String COD_HIJO_WSPE21_TRANSACCION="PWS005006"	;//PE21	
	 public static final String COD_HIJO_WSPE21_TIPO_MENSAJE="PWS005007"	;//7
	 public static final String COD_HIJO_WSPE21_TIPO_PROCESO="PWS005008"	;//O	
	 public static final String COD_HIJO_WSPE21_CANAL_COMUNICACION="PWS005009"	;//PF	
	 public static final String COD_HIJO_WSPE21_INDICADOR_PREFORMATO="PWS005010"	;//N	
	 public static final String COD_HIJO_WSPE21_TIPO_MENSAJE_ME="PWS005011"	;//B	
	 public static final String COD_HIJO_WSPE21_DIST_QNAME_IN="PWS005012"	;//QLT.GEC.RESPA
	 public static final String COD_HIJO_WSPE21_DIST_RQNAME_OUT="PWS005013"	;//QRT.GEC.ENVIO.MPD1
	 public static final String COD_HIJO_WSPE21_HOST_RQNAME_OUT="PWS005014"	;//QRT.GEC.RESP.QMPCINT8	
	 public static final String COD_HIJO_WSPE21_HOST_QMGR_NAME="PWS005015"	;//MPD1
	 
	 public static final String COD_HIJO_WSPE21_IP_PORT="PWS005004"	;//	http://118.180.36.26:7808
	 public static final String COD_HIJO_WSPE21_ENCODING="PWS005005"	;//	IBM-500
	 
	 
	 
	
	 public static final String COD_TIPO_INDIVIDUAL_RATINGWS="I";
	 public static final String COD_TIPO_CONSOLIDADO_RATINGWS="C";
	 
	 public static final  String COD_ERROR_WS_GETEMPRESA  = "ERRORWS001";
	 public static final  String MSN_WS_GETEMPRESA  = "Se utilizaran las empresas seleccionadas al momento de crear el PF";
	 
	 public static final String ETIQUETA_METODIZADO_RATIOS="RATIOS:";
	 
	 //PARA EQUIVALENCIA TIPO DE DOCUMENTO
	 
	 public static final Long TABLA_EQUIVALENCIA_TIPDOC = 21804L;
	 public static final String EQUIV_RUC = "EQUIV_RUC";
	 
	 
	 public static final String COD_ERROR_WS = "9999";
	 public static final String COD_OK_WS = "0000";		 
	 
	 public static final String ID_TIPO_COPIA_IaI="1";
	 public static final String ID_TIPO_COPIA_IaG="2";	 
	 public static final String ID_TIPO_COPIA_GaI="3";
	 public static final String ID_TIPO_COPIA_GaG="4";
	 
	 public static final String COD_COPIA_SINDATA="1";	
	 
	public static final Long ID_TABLA_TIPO_OPERACION = 600L;
	public static final String ID_TIPO_OPERACION_PROGRAMA_FINANCIERO = "601";
	public static final String ID_TIPO_OPERACION_OPERACION_PUNTUAL = "602";
	
	public static final String DIR_FILE_REPORTE_CREDITO = "DIR_FILE_REPORTE_CREDITO";
	
	public static final String COD_PADRE_VALOR_ROL = "TROLPF700";
	
	public static final String ROLES_PERFIL_SESSION = "roles_perfil_session";
	
	public static final String COD_ROL_CONCLUIR = "TROLPF7001";
	public static final String COD_ROL_CONSULTAR = "TROLPF7002";
	
	public static final String COD_TIPOGARANTIA_CRGA  = "TGCRGAPF800";
	
	public static final String COD_TG_HIPOTECA_CRGA  = "TGCRGAHIPO8001";
	public static final String COD_TG_DEPOSITOAPLAZO_CRGA  = "TGCRGADEPL8002";
	public static final String COD_TG_CUENTAGARANTIA_CRGA  = "TGCRGACUCO8003";
	public static final String COD_TG_FIANZASOLIDARIA_CRGA  = "TGCRGAFISOL8004";
	public static final String COD_TG_FONDOMUTUO_CRGA  = "TGCRGAFOMU8005";
	public static final String COD_TG_WARRANT_CRGA  = "TGCRGAWARR8006";
	public static final String COD_TG_STANDBY_CRGA  = "TGCRGASTAB8007";
	
	
//	111	HIPOTECA					TGARPF9001
//	122	DEPOSITO A PLAZO			TGARPF9011
//	123	CUENTAS EN GARANTIA			TGARPF9012
//	311	FIANZA SOLIDARIA			TGARPF9014
//	129	FONDOS MUTUOS				TGARPF9022
//	133	WARRANT						TGARPF9010
//	218	STAND BY LETTER OF CREDIT	TGARPF9013
	
	public static final String COD_HIPOTECA_LOCAL  = "TGARPF9001";
	public static final String COD_DEPOSITOAPLAZO_LOCAL  = "TGARPF9011";
	public static final String COD_CUENTAGARANTIA_LOCAL  = "TGARPF9012";
	public static final String COD_FIANZASOLIDARIA_LOCAL  = "TGARPF9014";
	public static final String COD_FONDOMUTUOS_LOCAL  = "TGARPF9022";
	public static final String COD_WARRANT_LOCAL  = "TGARPF9010";
	public static final String COD_STANDBY_LOCAL = "TGARPF9013";
	
	
	public static final String RUTALOG = "RUTALOG";
	public static final String NAME_APP_LOG = "profin";
	
	
	
	 public static final String COD_PADRE_TIPO_DOCUMENTO = "TDO00200";
	 public static final String COD_HIJO_COD_CODIGO_CENTRAL="TDO00203";	
	 public static final String COD_HIJO_COD_RUC="TDO00201";
	 
	 public static final String LIMITEPF_COPIA="LIMITEPF_COPIA";
	 public static final String NUMTOPDEFAULT="10";
	 
	 public static final String COD_MONITORJOB_GARANTIA  = "MONITORJOB_GARANTIA";
	 public static final String COD_MONITORJOB_HIPOTECA  = "MONITORJOB_HIPOTECA";
	 public static final String COD_MONITORJOB_WARRANT  = "MONITORJOB_WARRANT";
	 public static final String COD_MONITORJOB_DEPOSITOAPLAZO  = "MONITORJOB_DEPOSITOAPLAZO";
	 public static final String COD_MONITORJOB_CUENTAGARANTIA  = "MONITORJOB_CUENTAGARANTIA";
	 public static final String COD_MONITORJOB_STANDBY  = "MONITORJOB_STANDBY";
	 public static final String COD_MONITORJOB_FIANZASOLIDARIA  = "MONITORJOB_FIANZASOLIDARIA";
	 public static final String COD_MONITORJOB_DETALLEGARANTIA  = "MONITORJOB_DETALLEGARANTIA";
	 public static final String COD_MONITORJOB_FONDOSMUTUOS  = "MONITORJOB_FONDOSMUTUOS";
	 
	 
	 public static final Integer ARCHIVO_GARANTIA=13;
	 public static final Integer ARCHIVO_HIPOTECA=14;
	 public static final Integer ARCHIVO_WARRANT=15;
	 public static final Integer ARCHIVO_DEPOSITOAPLAZO=16;
	 public static final Integer ARCHIVO_CUENTAGARANTIA=17;
	 public static final Integer ARCHIVO_STANDBY=18;
	 public static final Integer ARCHIVO_FIANZASOLIDARIA=19;
	 public static final Integer ARCHIVO_DETALLEGARANTIA=20;
	 public static final Integer ARCHIVO_FONDOSMUTUOS=21;
	 
	 
	public static final String PREFIJO_ARCHGARANTIA = "PREFIJO_ARCHGARANTIA";
	public static final String EXT_ARCH_GARANTIA = "EXT_ARCH_GARANTIA";
	public static final String FORMAT_FECHA_GARANTIA = "FORMAT_FECHA_GARANTIA";
	 
	 
	public static final String PREFIJO_ARCHHIPOTECA = "PREFIJO_ARCHHIPOTECA";
	public static final String EXT_ARCH_HIPOTECA = "EXT_ARCH_HIPOTECA";
	public static final String FORMAT_FECHA_HIPOTECA = "FORMAT_FECHA_HIPOTECA"; 

 
	public static final String PREFIJO_ARCHWARRANT = "PREFIJO_ARCHWARRANT";
	public static final String EXT_ARCH_WARRANT = "EXT_ARCH_WARRANT";
	public static final String FORMAT_FECHA_WARRANT = "FORMAT_FECHA_WARRANT"; 

	public static final String PREFIJO_ARCHDEPOSITOAPLAZO = "PREFIJO_ARCHDEPOSITOAPLAZO";
	public static final String EXT_ARCH_DEPOSITOAPLAZO = "EXT_ARCH_DEPOSITOAPLAZO";
	public static final String FORMAT_FECHA_DEPOSITOAPLAZO = "FORMAT_FECHA_DEPOSITOAPLAZO";
	
	public static final String PREFIJO_ARCHCUENTAGARANTIA = "PREFIJO_ARCHCUENTAGARANTIA";
	public static final String EXT_ARCH_CUENTAGARANTIA = "EXT_ARCH_CUENTAGARANTIA";
	public static final String FORMAT_FECHA_CUENTAGARANTIA = "FORMAT_FECHA_CUENTAGARANTIA";
	
	public static final String PREFIJO_ARCHSTANDBY = "PREFIJO_ARCHSTANDBY";
	public static final String EXT_ARCH_STANDBY = "EXT_ARCH_STANDBY";
	public static final String FORMAT_FECHA_STANDBY = "FORMAT_FECHA_STANDBY";
	
	public static final String PREFIJO_ARCHFIANZASOLIDARIA = "PREFIJO_ARCHFIANZASOLIDARIA";
	public static final String EXT_ARCH_FIANZASOLIDARIA = "EXT_ARCH_FIANZASOLIDARIA";
	public static final String FORMAT_FECHA_FIANZASOLIDARIA = "FORMAT_FECHA_FIANZASOLIDARIA";
	
	public static final String PREFIJO_ARCHDETGARANTIA = "PREFIJO_ARCHDETGARANTIA";
	public static final String EXT_ARCH_DETGARANTIA = "EXT_ARCH_DETGARANTIA";
	public static final String FORMAT_FECHA_DETGARANTIA = "FORMAT_FECHA_DETGARANTIA";
	
	public static final String PREFIJO_ARCHFONDOSMUTUOS = "PREFIJO_ARCHFONDOSMUTUOS";
	public static final String EXT_ARCH_FONDOSMUTUOS = "EXT_ARCH_FONDOSMUTUOS";
	public static final String FORMAT_FECHA_FONDOSMUTUOS = "FORMAT_FECHA_FONDOSMUTUOS"; 
	
	
    
	public static final String COBRANZA_DE_IMPORTACION = "COD_COBRANZAS_DE_IMPORT";
	public static final String CARTAS_DE_CREDITO_IMP = "COD_CARTAS_CREDITO_IMP";
	public static final String FINANCIMIENTO_IMP = "COD_FINANCIAMTS_DE IMP";
	public static final String TRANSFERENCIA_EMITIDA = "COD_TRANSFERS_EMITIDAS";
	
	public static final String VENTA_FORWARD_CLIENT = "COD_VENTA_FORWARD_CLIENT";
	public static final String VENTAS_ME_CLIENTES = "COD_VENTAS_ME_CLIENTES";
	
	
	public static final String COBRANZA_DE_EXPORTACION = "COD_COBRANZAS_DE_EXPORT";	
	
	public static final String COMPRA_FORWARD_CLIEN = "COD_COMPRA_FORWARD_CLIEN";	
	public static final String COMPRAS_ME_CLIENTES = "COD_COMPRAS_ME_CLIENTES";
	
	public static final String CARTAS_DE_CREDITO_EXP = "COD_CARTAS_CREDITO_EXP";
	public static final String FINANCIMIENTO_EXPORT = "COD_FINANCIAMTS_DE_EXP";
	public static final String TRANSFERANCIA_RECIBIDAS = "COD_TRANSFERS_RECIBIDAS";
	
	
	public static final String ACTIVO_BTN_GENERAL = "ACTIVO_BTN_GENERAL";
	public static final String ESTADO_ACTIVOBTN = "1";
	public static final String ESTADO_INACTIVOBTN = "0";
	
	public static final String URL_COMEX_PF ="URL_COMEX_PF";
	
	public static final String VAL_TIPO_COMEX_IMP="I";
	public static final String VAL_TIPO_COMEX_EXP="E";
	
	public static final String COD_ERROR_COMEX="KCE0007";
	public static final String COD_CORRECTO_COMEX="KCE0000";
	
	public static final String COD_DESC_COMEX1500="COD_DESC_COMEX1500";
	
	public static final String ACTIVO_BTN_VALIDAR = "ACTIVO_BTN_VALIDAR";
	
	
	public static final int DESCARGA_PDF_HILO = 1;
	
	public static final String REP_ERROR						="0";
	public static final String REP_PROCESANDO					="1";
	public static final String REP_TERMINO						="2";
	public static final String REP_DESCARGADO					="3";
	public static final String CODIGO_TIPO_DOWNLOAD_PDF					="01";
	
	public static final String ACTIVO_VALIDAR_EDITOR = "ACTIVO_VALIDAR_EDITOR";
	public static final String TIEMPO_DESCARGA_PDF="TIEMPO_DESCARGA_PDF";
	
	
	
	public static final String RUTAMANUALPF = "RUTAMANUALPF";
	public static final String ACTIVO_REF_DESCARGAMANUAL = "ACTIVO_REF_DESCARGAMANUAL";
}

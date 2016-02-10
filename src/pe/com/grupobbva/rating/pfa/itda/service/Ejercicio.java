package pe.com.grupobbva.rating.pfa.itda.service;

import java.util.Comparator;

public class Ejercicio implements Comparable<Ejercicio> {

    private String anio;
    private String mes;
    private String tipoEmpresa;
    private String qplNumMes;
    private String codUnimedDesc;
    private String codDivisa;
    private String codTipBalaDesc;
    private String codEstProDesc;
    private String desCsecli;
    private String codAnalisis;
    private String codModtriDesc;
    private String codStatus;
    private String qnuEmpleados;
    private String wsFacturacion;
    private String desModelo;
    private String codTmodelo;
    private String qtyUnimed;
    private String codBalance;
    private String codCsecli;
    private String codEstProd01;
    private String codEstProd02;
    private String codModTrib;
    private String codEstado;
    private String desEstado;
    private String codAuditada;
    private String codAplOrigen;
    private String codEstadoRt;
    //Flag Ejercicios GRUPO
    private String flagGrupoEjercicio;
    private String estadoActivo;
    
    public Ejercicio(){
    	
    }
    
	public Ejercicio(String anio, String mes, String tipoEmpresa,
			String qplNumMes, String codUnimedDesc, String codDivisa,
			String codTipBalaDesc, String codEstProDesc, String desCsecli,
			String codAnalisis, String codModtriDesc, String codStatus,
			String qnuEmpleados, String wsFacturacion, String desModelo,
			String codTmodelo, String qtyUnimed, String codBalance,
			String codCsecli, String codEstProd01, String codEstProd02,
			String codModTrib, String codEstado, String desEstado,
			String codAuditada, String codAplOrigen, String codEstadoRt) {
		super();
		this.anio = anio;
		this.mes = mes;
		this.tipoEmpresa = tipoEmpresa;
		this.qplNumMes = qplNumMes;
		this.codUnimedDesc = codUnimedDesc;
		this.codDivisa = codDivisa;
		this.codTipBalaDesc = codTipBalaDesc;
		this.codEstProDesc = codEstProDesc;
		this.desCsecli = desCsecli;
		this.codAnalisis = codAnalisis;
		this.codModtriDesc = codModtriDesc;
		this.codStatus = codStatus;
		this.qnuEmpleados = qnuEmpleados;
		this.wsFacturacion = wsFacturacion;
		this.desModelo = desModelo;
		this.codTmodelo = codTmodelo;
		this.qtyUnimed = qtyUnimed;
		this.codBalance = codBalance;
		this.codCsecli = codCsecli;
		this.codEstProd01 = codEstProd01;
		this.codEstProd02 = codEstProd02;
		this.codModTrib = codModTrib;
		this.codEstado = codEstado;
		this.desEstado = desEstado;
		this.codAuditada = codAuditada;
		this.codAplOrigen = codAplOrigen;
		this.codEstadoRt = codEstadoRt;
	}



	public String getEstadoActivo() {
		return estadoActivo;
	}

	public void setEstadoActivo(String estadoActivo) {
		this.estadoActivo = estadoActivo;
	}

	public String getFlagGrupoEjercicio() {
		return flagGrupoEjercicio;
	}

	public void setFlagGrupoEjercicio(String flagGrupoEjercicio) {
		this.flagGrupoEjercicio = flagGrupoEjercicio;
	}

	public String getAnio() {
		return anio;
	}



	public void setAnio(String anio) {
		this.anio = anio;
	}



	public String getMes() {
		return mes;
	}



	public void setMes(String mes) {
		this.mes = mes;
	}



	public String getTipoEmpresa() {
		return tipoEmpresa;
	}



	public void setTipoEmpresa(String tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}



	public String getQplNumMes() {
		return qplNumMes;
	}



	public void setQplNumMes(String qplNumMes) {
		this.qplNumMes = qplNumMes;
	}



	public String getCodUnimedDesc() {
		return codUnimedDesc;
	}



	public void setCodUnimedDesc(String codUnimedDesc) {
		this.codUnimedDesc = codUnimedDesc;
	}



	public String getCodDivisa() {
		return codDivisa;
	}



	public void setCodDivisa(String codDivisa) {
		this.codDivisa = codDivisa;
	}



	public String getCodTipBalaDesc() {
		return codTipBalaDesc;
	}



	public void setCodTipBalaDesc(String codTipBalaDesc) {
		this.codTipBalaDesc = codTipBalaDesc;
	}



	public String getCodEstProDesc() {
		return codEstProDesc;
	}



	public void setCodEstProDesc(String codEstProDesc) {
		this.codEstProDesc = codEstProDesc;
	}



	public String getDesCsecli() {
		return desCsecli;
	}



	public void setDesCsecli(String desCsecli) {
		this.desCsecli = desCsecli;
	}



	public String getCodAnalisis() {
		return codAnalisis;
	}



	public void setCodAnalisis(String codAnalisis) {
		this.codAnalisis = codAnalisis;
	}



	public String getCodModtriDesc() {
		return codModtriDesc;
	}



	public void setCodModtriDesc(String codModtriDesc) {
		this.codModtriDesc = codModtriDesc;
	}



	public String getCodStatus() {
		return codStatus;
	}



	public void setCodStatus(String codStatus) {
		this.codStatus = codStatus;
	}



	public String getQnuEmpleados() {
		return qnuEmpleados;
	}



	public void setQnuEmpleados(String qnuEmpleados) {
		this.qnuEmpleados = qnuEmpleados;
	}



	public String getWsFacturacion() {
		return wsFacturacion;
	}



	public void setWsFacturacion(String wsFacturacion) {
		this.wsFacturacion = wsFacturacion;
	}



	public String getDesModelo() {
		return desModelo;
	}



	public void setDesModelo(String desModelo) {
		this.desModelo = desModelo;
	}



	public String getCodTmodelo() {
		return codTmodelo;
	}



	public void setCodTmodelo(String codTmodelo) {
		this.codTmodelo = codTmodelo;
	}



	public String getQtyUnimed() {
		return qtyUnimed;
	}



	public void setQtyUnimed(String qtyUnimed) {
		this.qtyUnimed = qtyUnimed;
	}



	public String getCodBalance() {
		return codBalance;
	}



	public void setCodBalance(String codBalance) {
		this.codBalance = codBalance;
	}



	public String getCodCsecli() {
		return codCsecli;
	}



	public void setCodCsecli(String codCsecli) {
		this.codCsecli = codCsecli;
	}



	public String getCodEstProd01() {
		return codEstProd01;
	}

	public void setCodEstProd01(String codEstProd01) {
		this.codEstProd01 = codEstProd01;
	}

	public String getCodEstProd02() {
		return codEstProd02;
	}

	public void setCodEstProd02(String codEstProd02) {
		this.codEstProd02 = codEstProd02;
	}

	public String getCodModTrib() {
		return codModTrib;
	}

	public void setCodModTrib(String codModTrib) {
		this.codModTrib = codModTrib;
	}

	public String getCodEstado() {
		return codEstado;
	}

	public void setCodEstado(String codEstado) {
		this.codEstado = codEstado;
	}

	public String getDesEstado() {
		return desEstado;
	}

	public void setDesEstado(String desEstado) {
		this.desEstado = desEstado;
	}

	public String getCodAuditada() {
		return codAuditada;
	}

	public void setCodAuditada(String codAuditada) {
		this.codAuditada = codAuditada;
	}

	public String getCodAplOrigen() {
		return codAplOrigen;
	}
	
	public void setCodAplOrigen(String codAplOrigen) {
		this.codAplOrigen = codAplOrigen;
	}

	public String getCodEstadoRt() {
		return codEstadoRt;
	}

	public void setCodEstadoRt(String codEstadoRt) {
		this.codEstadoRt = codEstadoRt;
	}

	public static Comparator<Ejercicio> EjerAnioComparator = new Comparator<Ejercicio>() {

		public int compare(Ejercicio s1, Ejercicio s2) {
		   String ejercicioAnio1 = s1.getAnio().toUpperCase();
		   String ejercicioAnio2 = s2.getAnio().toUpperCase();
		   
		   return ejercicioAnio2.compareTo(ejercicioAnio1);

	    }
		
	};
	public static Comparator<Ejercicio> EjerAnioMesComparator = new Comparator<Ejercicio>() {
		public int compare(Ejercicio s1, Ejercicio s2) {

			String anio1 = s1.getAnio();
           String anio2 = s2.getAnio();
           int sComp = anio2.compareTo(anio1);

           if (sComp != 0) {
              return sComp;
           } else {
              String mes1 = s1.getMes();
              String mes2 = s2.getMes();
              return mes2.compareTo(mes1);
           }
	    }
		
		
	};

	public int compareTo(Ejercicio o) {
		 
		return 0;
	}

}

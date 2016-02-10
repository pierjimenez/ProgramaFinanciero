package pe.com.bbva.iipf.pf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pe.com.stefanini.core.domain.EntidadBase;

/**
 * 
 * @author EPOMAYAY
 *
 */
@Entity
@Table(schema="PROFIN", name = "TIIPF_PROGRAMA_BLOBS")
@NamedQueries({
@NamedQuery(name="updateSintesisEmpresa", 
		query = " update ProgramaBlob set sintesisEmpresa=? where id=?"),
@NamedQuery(name="updateDatosMatriz", 
	    query = " update ProgramaBlob set datosMatriz=? where id=?"),
@NamedQuery(name="updateEspacioLibre", 
	    query = " update ProgramaBlob set espacioLibre=? where id=?"),
@NamedQuery(name="updateComenSituFinanciera", 
	    query = " update ProgramaBlob set comenSituFinanciera=? where id=?"),
@NamedQuery(name="updateComenSituEconomica", 
	    query = " update ProgramaBlob set comenSituEconomica=? where id=?"),
@NamedQuery(name="updateValoracionEconFinanciera", 
	    query = " update ProgramaBlob set valoracionEconFinanciera=? where id=?"),
@NamedQuery(name="updateValoracionPosiBalance", 
	    query = " update ProgramaBlob set valoracionPosiBalance=? where id=?"),
@NamedQuery(name="updateValoracionRating", 
	    query = " update ProgramaBlob set valoracionRating=? where id=?"),
@NamedQuery(name="updateEspacioLibreAS", 
	    query = " update ProgramaBlob set espacioLibreAS=? where id=?"),
@NamedQuery(name="updateComenLineas", 
	    query = " update ProgramaBlob set comenLineas=? where id=?"),
@NamedQuery(name="updateRentaModelGlobal", 
	    query = " update ProgramaBlob set rentaModelGlobal=? where id=?"),
@NamedQuery(name="updateCampoLibreRB", 
	    query = " update ProgramaBlob set campoLibreRB=? where id=?"),
@NamedQuery(name="updateFodaFotalezas", 
	    query = " update ProgramaBlob set fodaFotalezas=? where id=?"),
@NamedQuery(name="updateFodaOportunidades", 
	    query = " update ProgramaBlob set fodaOportunidades=? where id=?"),
@NamedQuery(name="updateFodaDebilidades", 
	    query = " update ProgramaBlob set fodaDebilidades=? where id=?"),
@NamedQuery(name="updateFodaAmenazas", 
	    query = " update ProgramaBlob set fodaAmenazas=? where id=?"),
@NamedQuery(name="updateConclucionFoda", 
	    query = " update ProgramaBlob set conclucionFoda=? where id=?"),
@NamedQuery(name="updateCampoLibrePR", 
	    query = " update ProgramaBlob set campoLibrePR=? where id=?"),
@NamedQuery(name="updateEstructuraLimite", 
	    query = " update ProgramaBlob set estructuraLimite=? where id=?"),
@NamedQuery(name="updateConsideracionPR", 
	    query = " update ProgramaBlob set consideracionPR=? where id=?"),
@NamedQuery(name="updateDetalleOperacionGarantia", 
	    query = " update ProgramaBlob set detalleOperacionGarantia=? where id=?"),
@NamedQuery(name="updateRiesgoTesoreria", 
  	    query = " update ProgramaBlob set riesgoTesoreria=? where id=?"),
@NamedQuery(name="updatePoliticasRiesGrupo", 
        query = " update ProgramaBlob set politicasRiesGrupo=? where id=?"),
@NamedQuery(name="updatePoliticasDelegacion", 
        query = " update ProgramaBlob set politicasDelegacion=? where id=?"),
@NamedQuery(name="updateRentaModelBEC", 
        query = " update ProgramaBlob set rentaModelBEC=? where id=?"),
@NamedQuery(name="updateComenComprasVentas", 
        query = " update ProgramaBlob set comenComprasVentas=? where id=?"),
@NamedQuery(name="updateComenPoolBanc", 
        query = " update ProgramaBlob set comenPoolBanc=? where id=?"),
@NamedQuery(name="updateCampoLibreAnexos", 
        query = " update ProgramaBlob set campoLibreAnexos=? where id=?"),
@NamedQuery(name="updateConcentracion", 
        query = " update ProgramaBlob set concentracion=? where id=?"),
@NamedQuery(name="updateDatosBasicosRCD", 
		query = " update ProgramaBlob set datosBasicosaddRDC=? where id=?"),
@NamedQuery(name="updateSituacionEconFinRCD", 
        query = " update ProgramaBlob set comenSintesisEconFinaddRDC=? where id=?"),
@NamedQuery(name="updateValoracion", 
                query = " update ProgramaBlob set valoracion=? where id=?") ,  
@NamedQuery(name="updateIndTransaccional", 
                query = " update ProgramaBlob set comentIndTransaccional=? where id=?")                 
})
@SequenceGenerator(name = "SEQ_PROGRAMA_BLOB", sequenceName = "PROFIN.SEQ_PROGRAMA_BLOB", allocationSize = 1, initialValue = 20000)
public class ProgramaBlob extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;//ID_BLOB NUMERIC
	private byte[] sintesisEmpresa; //DB_SINTESIS_EMPR	byte[]
	private byte[] datosMatriz; //DB_DATOS_MATR	byte[]
	private byte[] espacioLibre; //DB_ESPACIO_LIBRE	byte[]
	private byte[] comenComprasVentas; //DB_ESPACIO_LIBRE	byte[]
	private byte[] comenSituFinanciera; //SF_COMENTARIO_SITU_FINA	byte[]
	private byte[] comenSituEconomica; //SF_COMENTARIO_SITU_ECON	byte[]
	private byte[] valoracionEconFinanciera; //SF_VALORACION_ECON_FINA	byte[]
	private byte[] valoracionPosiBalance; // SF_VALORACION_POSI_BALA	byte[]
	private byte[] valoracionRating; // RA_VALORACION_RATI	byte[]
	private byte[] espacioLibreAS; //AS_ESPACIO_LIBRE	byte[]
	private byte[] comenLineas;//RB_COMENTARIOS_LINEAS	byte[]
	private byte[] rentaModelGlobal; //RB_RENTAB_MODEL_GLOB	byte[]
	private byte[] rentaModelBEC; //RB_RENTAB_MODEL_GLOB	byte[]
	private byte[] campoLibreRB; //RB_CAMPO_LIBR	byte[]
	private byte[] comenPoolBanc; //RB_COMENTARIO_POOL_BANC byte[]
	private byte[] fodaFotalezas; //FR_FODA_FORTA	byte[]
	private byte[] fodaOportunidades; //FR_FODA_OPOR	byte[]
	private byte[] fodaDebilidades; //FR_FODA_DEBI	byte[]
	private byte[] fodaAmenazas; //FR_FODA_AMEN	byte[]
	private byte[] conclucionFoda; //FR_CONCLUSION_FODA	byte[]
	private byte[] campoLibrePR; //PR_CAMPO_LIBR	byte[]
	private byte[] estructuraLimite; //PR_ESTR_LIMIT	byte[]
	private byte[] consideracionPR; //PR_CONSIDERACION	byte[]
	private byte[] detalleOperacionGarantia; //PRG_DET_OPERACI_GARANT	byte[]
	private byte[] riesgoTesoreria; //PRG_RIESGO_TESO	byte[]
	private byte[] politicasRiesGrupo; //PRG_POLITICAS_RIES_GRUP	byte[]
	private byte[] politicasDelegacion; //PRG_POLITICAS_DELE	byte[]
	private byte[] campoLibreAnexos;//RB_CAMPO_LIBRE_ANEXO 
	private Programa programa; //IIPF_PROGRAMA_ID_PROGRAMA	NUMBER
	private byte[] concentracion; //DB_CONCENTRACION byte[]
	private byte[] valoracion; //DB_VALORACION byte[]
	
	//ini MCG20121031
	private byte[] datosBasicosaddRDC;//RDC_COMEN_DATOSBASICOS_ADD	BLOB	Y			
	private byte[] comenSintesisEconFinaddRDC;//RDC_COMEN_SINT_ECONFINAN_ADD	BLOB	Y			
	//fin MCG20121031
	
	private byte[] comentIndTransaccional ;//RB_COMEN_INDTRANSA
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PROGRAMA_BLOB")
	@Column(name="ID_BLOB")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Lob
	@Column( name= "DB_SINTESIS_EMPR")
	public byte[] getSintesisEmpresa() {
		return sintesisEmpresa;
	}
	
	public void setSintesisEmpresa(byte[] sintesisEmpresa) {
		this.sintesisEmpresa = sintesisEmpresa;
	}
	
	@Lob
	@Column( name= "DB_DATOS_MATR")
	public byte[] getDatosMatriz() {
		return datosMatriz;
	}
	
	public void setDatosMatriz(byte[] datosMatriz) {
		this.datosMatriz = datosMatriz;
	}
	
	@Lob
	@Column( name= "DB_ESPACIO_LIBRE")
	public byte[] getEspacioLibre() {
		return espacioLibre;
	}
	
	public void setEspacioLibre(byte[] espacioLibre) {
		this.espacioLibre = espacioLibre;
	}
	
	@Lob
	@Column( name= "SF_COMENTARIO_SITU_FINA")
	public byte[] getComenSituFinanciera() {
		return comenSituFinanciera;
	}
	
	public void setComenSituFinanciera(byte[] comenSituFinanciera) {
		this.comenSituFinanciera = comenSituFinanciera;
	}
	
	@Lob
	@Column( name= "SF_COMENTARIO_SITU_ECON")
	public byte[] getComenSituEconomica() {
		return comenSituEconomica;
	}
	
	public void setComenSituEconomica(byte[] comenSituEconomica) {
		this.comenSituEconomica = comenSituEconomica;
	}
	
	@Lob
	@Column( name= "SF_VALORACION_ECON_FINA")
	public byte[] getValoracionEconFinanciera() {
		return valoracionEconFinanciera;
	}
	
	public void setValoracionEconFinanciera(byte[] valoracionEconFinanciera) {
		this.valoracionEconFinanciera = valoracionEconFinanciera;
	}
	
	@Lob
	@Column( name= "SF_VALORACION_POSI_BALA")
	public byte[] getValoracionPosiBalance() {
		return valoracionPosiBalance;
	}
	
	public void setValoracionPosiBalance(byte[] valoracionPosiBalance) {
		this.valoracionPosiBalance = valoracionPosiBalance;
	}
	
	@Lob
	@Column( name= "RA_VALORACION_RATI")
	public byte[] getValoracionRating() {
		return valoracionRating;
	}
	
	public void setValoracionRating(byte[] valoracionRating) {
		this.valoracionRating = valoracionRating;
	}
	
	@Lob
	@Column( name="AS_ESPACIO_LIBRE")
	public byte[] getEspacioLibreAS() {
		return espacioLibreAS;
	}
	
	public void setEspacioLibreAS(byte[] espacioLibreAS) {
		this.espacioLibreAS = espacioLibreAS;
	}
	
	@Lob
	@Column( name="RB_COMENTARIOS_LINEAS")
	public byte[] getComenLineas() {
		return comenLineas;
	}
	
	public void setComenLineas(byte[] comenLineas) {
		this.comenLineas = comenLineas;
	}
	
	@Lob
	@Column(name="RB_RENTAB_MODEL_GLOB")
	public byte[] getRentaModelGlobal() {
		return rentaModelGlobal;
	}
	
	public void setRentaModelGlobal(byte[] rentaModelGlobal) {
		this.rentaModelGlobal = rentaModelGlobal;
	}
	
	@Lob
	@Column( name = "RB_CAMPO_LIBR")
	public byte[] getCampoLibreRB() {
		return campoLibreRB;
	}
	
	public void setCampoLibreRB(byte[] campoLibreRB) {
		this.campoLibreRB = campoLibreRB;
	}
	
	@Lob
	@Column( name="FR_FODA_FORTA")
	public byte[] getFodaFotalezas() {
		return fodaFotalezas;
	}
	public void setFodaFotalezas(byte[] fodaFotalezas) {
		this.fodaFotalezas = fodaFotalezas;
	}
	
	@Lob
	@Column( name="FR_FODA_OPOR")
	public byte[] getFodaOportunidades() {
		return fodaOportunidades;
	}
	
	public void setFodaOportunidades(byte[] fodaOportunidades) {
		this.fodaOportunidades = fodaOportunidades;
	}
	
	@Lob
	@Column( name= "FR_FODA_DEBI")
	public byte[] getFodaDebilidades() {
		return fodaDebilidades;
	}
	
	public void setFodaDebilidades(byte[] fodaDebilidades) {
		this.fodaDebilidades = fodaDebilidades;
	}
	@Lob
	@Column( name= "FR_FODA_AMEN")
	public byte[] getFodaAmenazas() {
		return fodaAmenazas;
	}
	
	public void setFodaAmenazas(byte[] fodaAmenazas) {
		this.fodaAmenazas = fodaAmenazas;
	}
	
	@Lob
	@Column(name="FR_CONCLUSION_FODA")
	public byte[] getConclucionFoda() {
		return conclucionFoda;
	}
	
	public void setConclucionFoda(byte[] conclucionFoda) {
		this.conclucionFoda = conclucionFoda;
	}
	
	@Lob
	@Column(name="PR_CAMPO_LIBR")
	public byte[] getCampoLibrePR() {
		return campoLibrePR;
	}
	
	public void setCampoLibrePR(byte[] campoLibrePR) {
		this.campoLibrePR = campoLibrePR;
	}
	
	@Lob
	@Column(name = "PR_ESTR_LIMIT")
	public byte[] getEstructuraLimite() {
		return estructuraLimite;
	}
	
	public void setEstructuraLimite(byte[] estructuraLimite) {
		this.estructuraLimite = estructuraLimite;
	}
	
	@Lob
	@Column(name="PR_CONSIDERACION")
	public byte[] getConsideracionPR() {
		return consideracionPR;
	}
	
	public void setConsideracionPR(byte[] consideracionPR) {
		this.consideracionPR = consideracionPR;
	}
	
	@Lob
	@Column(name="PRG_DET_OPERACI_GARANT")
	public byte[] getDetalleOperacionGarantia() {
		return detalleOperacionGarantia;
	}
	
	public void setDetalleOperacionGarantia(byte[] detalleOperacionGarantia) {
		this.detalleOperacionGarantia = detalleOperacionGarantia;
	}
	
	@Lob
	@Column(name="PRG_RIESGO_TESO")
	public byte[] getRiesgoTesoreria() {
		return riesgoTesoreria;
	}
	
	public void setRiesgoTesoreria(byte[] riesgoTesoreria) {
		this.riesgoTesoreria = riesgoTesoreria;
	}
	
	@Lob
	@Column(name = "PRG_POLITICAS_RIES_GRUP")
	public byte[] getPoliticasRiesGrupo() {
		return politicasRiesGrupo;
	}
	
	public void setPoliticasRiesGrupo(byte[] politicasRiesGrupo) {
		this.politicasRiesGrupo = politicasRiesGrupo;
	}
	
	@Lob
	@Column(name="PRG_POLITICAS_DELE")
	public byte[] getPoliticasDelegacion() {
		return politicasDelegacion;
	}
	
	public void setPoliticasDelegacion(byte[] politicasDelegacion) {
		this.politicasDelegacion = politicasDelegacion;
	}
	
	@OneToOne(targetEntity = Programa.class)
	@JoinColumn(name="IIPF_PROGRAMA_ID_PROGRAMA")
	public Programa getPrograma() {
		return programa;
	}
	
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	@Lob
	@Column(name="RB_RENTAB_MODEL_BEC")
	public byte[] getRentaModelBEC() {
		return rentaModelBEC;
	}

	public void setRentaModelBEC(byte[] rentaModelBEC) {
		this.rentaModelBEC = rentaModelBEC;
	}

	@Lob
	@Column(name="DB_COMENTARIO_COMPR_VENT")
	public byte[] getComenComprasVentas() {
		return comenComprasVentas;
	}

	public void setComenComprasVentas(byte[] comenComprasVentas) {
		this.comenComprasVentas = comenComprasVentas;
	}

	@Lob
	@Column(name="RB_COMENTARIO_POOL_BANC")
	public byte[] getComenPoolBanc() {
		return comenPoolBanc;
	}

	public void setComenPoolBanc(byte[] comenPoolBanc) {
		this.comenPoolBanc = comenPoolBanc;
	}

	@Lob
	@Column(name="RB_CAMPO_LIBRE_ANEXO")
	public byte[] getCampoLibreAnexos() {
		return campoLibreAnexos;
	}

	public void setCampoLibreAnexos(byte[] campoLibreAnexos) {
		this.campoLibreAnexos = campoLibreAnexos;
	}

	@Lob
	@Column(name="DB_CONCENTRACION")
	public byte[] getConcentracion() {
		return concentracion;
	}

	public void setConcentracion(byte[] concentracion) {
		this.concentracion = concentracion;
	}

	@Lob
	@Column(name="RDC_COMEN_DATOSBASICOS_ADD")
	public byte[] getDatosBasicosaddRDC() {
		return datosBasicosaddRDC;
	}

	public void setDatosBasicosaddRDC(byte[] datosBasicosaddRDC) {
		this.datosBasicosaddRDC = datosBasicosaddRDC;
	}



	@Lob
	@Column(name="RDC_COMEN_SINT_ECONFINAN_ADD")
	public byte[] getComenSintesisEconFinaddRDC() {
		return comenSintesisEconFinaddRDC;
	}

	public void setComenSintesisEconFinaddRDC(byte[] comenSintesisEconFinaddRDC) {
		this.comenSintesisEconFinaddRDC = comenSintesisEconFinaddRDC;
	}

	@Lob
	@Column(name="DB_VALORACION")
	public byte[] getValoracion() {
		return valoracion;
	}

	public void setValoracion(byte[] valoracion) {
		this.valoracion = valoracion;
	}

	@Lob
	@Column(name="RB_COMEN_INDTRANSA")
	public byte[] getComentIndTransaccional() {
		return comentIndTransaccional;
	}

	public void setComentIndTransaccional(byte[] comentIndTransaccional) {
		this.comentIndTransaccional = comentIndTransaccional;
	}
	
	
	
	
}

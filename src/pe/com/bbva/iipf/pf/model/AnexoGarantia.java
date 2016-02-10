package pe.com.bbva.iipf.pf.model;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN",  name="TIIPF_ANEXO_GARANTIA")
@NamedQueries({
@NamedQuery(name="listarAnexoGarantia", 
			query = " select e from AnexoGarantia e where e.programa.id = ? order by e.id")				
})
@SequenceGenerator(name = "SEQ_ANEXO_GARANTIA", sequenceName = "PROFIN.SEQ_ANEXO_GARANTIA", allocationSize = 1, initialValue = 20000)
public class AnexoGarantia extends EntidadBase{
	
	private Long	id;
	
	private Integer codigoGarantiaAnexo;
	private String  numeroGarantia;
	private String  descripcionGarantia;
	private String empresa;
	private Programa programa ;
	private Integer pos; //POS	NUMBER
	
	private String importe;//IMPORTE	N	VARCHAR2(30)	Y			
	private String statusGarantia; //STATUS_GARANTIA	N	VARCHAR2(20)	Y			
	private String nota; //NOTA	N	VARCHAR2(3000)	Y			
	private String tipoGarantia;//ID_TIPO_GARANTIA	N	VARCHAR2(20)	Y			
	private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	N	VARCHAR2(30)	Y
	
	private String flagSincro; //FLAG_SINCRO VARCHAR2(2);
	
	public AnexoGarantia(Integer codigoGarantiaAnexo) {
		this.codigoGarantiaAnexo=codigoGarantiaAnexo;
	}
	public AnexoGarantia() {
	}
	@Transient
	public Integer getCodigoGarantiaAnexo() {
		return codigoGarantiaAnexo;
	}
	public void setCodigoGarantiaAnexo(Integer codigoGarantiaAnexo) {
		this.codigoGarantiaAnexo = codigoGarantiaAnexo;
	}
	@Column(name="NUMERO_GARANTIA", length = 20)
	public String getNumeroGarantia() {
		return numeroGarantia;
	}
	public void setNumeroGarantia(String numeroGarantia) {
		this.numeroGarantia = numeroGarantia;
	}
	@Column(name="DESCRIPCION", length = 300)
	public String getDescripcionGarantia() {
		return descripcionGarantia;
	}
	public void setDescripcionGarantia(String descripcionGarantia) {
		this.descripcionGarantia = descripcionGarantia;
	}
	@Column(name="EMPRESA", length = 50)
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	
	@ManyToOne(targetEntity = Programa.class)
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ANEXO_GARANTIA")
	@Column(name="ID_ANEXO_GARANTIA")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="POS")
	public Integer getPos() {
		return pos;
	}	
	public void setPos(Integer pos) {
		this.pos = pos;
	}
	
	@Column(name="IMPORTE")
	public String getImporte() {
		return importe;
	}
	public void setImporte(String importe) {
		this.importe = importe;
	}
	
	@Column(name="STATUS_GARANTIA")
	public String getStatusGarantia() {
		return statusGarantia;
	}
	public void setStatusGarantia(String statusGarantia) {
		this.statusGarantia = statusGarantia;
	}
	
	@Column(name="NOTA")
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
	}
	
	@Column(name="ID_TIPO_GARANTIA")
	public String getTipoGarantia() {
		return tipoGarantia;
	}
	public void setTipoGarantia(String tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}
	
	@Column(name="COD_GRUPO_EMPRESA")
	public String getCodEmpresaGrupo() {
		return codEmpresaGrupo;
	}
	public void setCodEmpresaGrupo(String codEmpresaGrupo) {
		this.codEmpresaGrupo = codEmpresaGrupo;
	}
	
	@Column(name="FLAG_SINCRO")
	public String getFlagSincro() {
		return flagSincro;
	}
	public void setFlagSincro(String flagSincro) {
		this.flagSincro = flagSincro;
	}
	
	public static Comparator<AnexoGarantia> GarantiaComparator = new Comparator<AnexoGarantia>() {
		public int compare(AnexoGarantia s1, AnexoGarantia s2) {
           String ng1 = s1.getNumeroGarantia()!=null?s1.getNumeroGarantia():"";           
           String ng2 = s2.getNumeroGarantia()!=null?s2.getNumeroGarantia():"";
           return ng2.compareTo(ng1);
	    }

	};
	
	
	
	
}

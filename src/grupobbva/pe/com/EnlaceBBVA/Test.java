package grupobbva.pe.com.EnlaceBBVA;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 

		
		//EnlaceBBVAProxy service = new EnlaceBBVAProxy();
		EnlaceBBVA_ServiceLocator ser = new EnlaceBBVA_ServiceLocator();
		
		ConsultaDatosClienteRequest reqDatosCliente = new ConsultaDatosClienteRequest();
		Cabecera cabecera = new Cabecera();
		cabecera.setUsuario("P007395");
		reqDatosCliente.setCabecera(cabecera);
		reqDatosCliente.setCodigoCentral("03779475");
		reqDatosCliente.setNumeroDocumentoIdentidad("");
		reqDatosCliente.setTipoDocumentoIdentidad("");
		try {
			ConsultaDatosClienteResponse resDatosCliente =  ser.getEnlaceBBVASOAP().consultaDatosCliente(reqDatosCliente);
			
			if(!resDatosCliente.getCodError().equals("9999")){
				System.out.println(ToStringBuilder.reflectionToString(resDatosCliente,ToStringStyle.MULTI_LINE_STYLE));
			}else{
				System.out.println("ERROR :: " );
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		//service.consultaGruposEconomico(parameters);
		//service.consultaGruposRiesoBuro(parameters);
 catch (ServiceException e) {
			 
			e.printStackTrace();
		}
		
	}

}

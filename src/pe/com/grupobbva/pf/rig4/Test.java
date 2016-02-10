package pe.com.grupobbva.pf.rig4;

import java.rmi.RemoteException;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import pe.com.grupobbva.xsd.ps9.CtHeaderRq;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 
		
		MS_PF_RIG4PortTypeProxy service = new MS_PF_RIG4PortTypeProxy();
		
		CtConDlgRIG4Rq req = new CtConDlgRIG4Rq();
		CtHeaderRq header = new CtHeaderRq();
		header.setUsuario("P016653");
		header.setTerminalContable("D90L");
		header.setTerminalLogico("D90L");
		header.setOpcionAplicacion("00");
		req.setHeader(header);
		
		CtBodyRq body = new CtBodyRq();
		body.setCodCentral("20367150");
		body.setCodEjecutivoCuenta("00");
		body.setCodOficinaAlta("00");
		
		req.setData(body);
	
			try {
				CtConDlgRIG4Rs response = service.callrig4(req);
				System.out.println(ToStringBuilder.reflectionToString(response.getData(), ToStringStyle.MULTI_LINE_STYLE));
				
			} catch (RemoteException e) {
				 
				e.printStackTrace();
			}
	
		

	}

}

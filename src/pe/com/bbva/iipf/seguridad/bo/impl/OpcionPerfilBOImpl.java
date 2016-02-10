package pe.com.bbva.iipf.seguridad.bo.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.seguridad.bo.OpcionPerfilBO;
import pe.com.bbva.iipf.seguridad.model.entity.Opcion;
import pe.com.bbva.iipf.seguridad.model.entity.OpcionPerfil;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;

@Service("opcionPerfilBO")
public class OpcionPerfilBOImpl extends GenericBOImpl<OpcionPerfil> implements OpcionPerfilBO {
	@Override
	public List<OpcionPerfil> listaPorPerfilTotal(Long idPerfil)
			throws BOException {
					
		List<OpcionPerfil> opcionesPerfil = null;
		OpcionPerfil opcPerfil = null;
				
		try {
			 		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			sb.append("select o.id_opcion, o.nombre, op.id_opcion_perfil, o.opc_superior, op.estado ");
			sb.append("from PROFIN.tiipf_opcion o left join PROFIN.tiipf_opcion_perfil op ");
			sb.append("on o.id_opcion = op.id_opcion and op.id_perfil = ");
			sb.append(idPerfil);
			sb.append(" where o.estado = 'A' order by o.id_opcion");
															
			List insurance = super.executeSQL(sb.toString());
			
			opcionesPerfil = new ArrayList<OpcionPerfil>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();															
				opcPerfil = new OpcionPerfil();
				
				Opcion opc = new Opcion();
				opc.setId(new Long(amount[0].toString()));
				opc.setNombre(amount[1].toString());
				
								
				if(amount[2] != null)
					opcPerfil.setId(new Long(amount[2].toString()));
				
				if(amount[3] != null)
				{
					Opcion opcPadre = new Opcion();
					opcPadre.setId(new Long(amount[3].toString()));
					opc.setSuperior(opcPadre);
				}
				
				if(amount[4] != null)
					opcPerfil.setEstado(amount[4].toString());
				
				opcPerfil.setOpcion(opc);
				opcionesPerfil.add(opcPerfil);
				
			}
		} catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}

		return opcionesPerfil;
		
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void actualizarPermisos(List<OpcionPerfil> permisos)
			throws BOException {
		
		super.saveCollection(permisos);
		
	}

}

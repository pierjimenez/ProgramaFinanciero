package pe.com.bbva.iipf.pf.action;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ProgramaInterceptor extends AbstractInterceptor {

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ProgramaAction dba = (ProgramaAction)invocation.getAction();
		String act = invocation.invoke();
		return act;
	}

}

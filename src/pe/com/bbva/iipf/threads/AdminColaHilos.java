package pe.com.bbva.iipf.threads;

import org.springframework.core.task.TaskExecutor;

public class AdminColaHilos {

	private TaskExecutor poolThreadTaskExecutor;

	public AdminColaHilos(TaskExecutor poolThreadTaskExecutor) {
		this.poolThreadTaskExecutor = poolThreadTaskExecutor;
	}

	public void executeThread(HiloProceso hiloProceso) {
		poolThreadTaskExecutor.execute(hiloProceso);
	}

}

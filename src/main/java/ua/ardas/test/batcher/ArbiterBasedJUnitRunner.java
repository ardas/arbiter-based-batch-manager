package ua.ardas.test.batcher;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class ArbiterBasedJUnitRunner extends BlockJUnit4ClassRunner {
	public ArbiterBasedJUnitRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		if (ArbiterBasedBatchManager.getInstance().shouldExecuteTest(
				this.getTestClass().getJavaClass().getName(), method.getName()))
		{
			super.runChild(method, notifier);
		}
	}
}

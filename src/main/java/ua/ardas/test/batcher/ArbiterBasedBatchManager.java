package ua.ardas.test.batcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;

import java.util.Properties;

public class ArbiterBasedBatchManager {
	private static final Log LOG = LogFactory.getLog(ArbiterBasedBatchManager.class);
	private static final int SUCCESSFUL_ADDED_OPERATION_CODE = 1;

	private static final String TEST_RUN_KEY_PROPERTY = "test.run.key";
	private static final String BATCH_NUMBER_PROPERTY = "thucydides.batch.number";
	private static final String REDIS_HOST_PROPERTY = "redis.host";
	private static final String REDIS_PORT_PROPERTY = "redis.port";

	private static ArbiterBasedBatchManager arbiterBasedBatchManager;

	private Jedis jedis;
	private String key;
	private String batchNumber;


	private ArbiterBasedBatchManager() {
		Properties systemProperties = System.getProperties();
		jedis = createJedis(
				systemProperties.getProperty(REDIS_HOST_PROPERTY, "localhost"),
				Integer.valueOf(systemProperties.getProperty(REDIS_PORT_PROPERTY, "6379"))
		);
		key = systemProperties.getProperty(TEST_RUN_KEY_PROPERTY);
		batchNumber = systemProperties.getProperty(BATCH_NUMBER_PROPERTY, "Unknown");
	}


	public static ArbiterBasedBatchManager getInstance() {
		if (arbiterBasedBatchManager == null) {
			synchronized (ArbiterBasedBatchManager.class) {
				if (arbiterBasedBatchManager == null) {
					arbiterBasedBatchManager = new ArbiterBasedBatchManager();
				}
			}
		}

		return arbiterBasedBatchManager;
	}

	public boolean shouldExecuteTest(String className, String testName) {
		String testFullName = createTestRedisLabel(className, testName);
		boolean shouldExecuteTest = StringUtils.isEmpty(key)
				? true
				: SUCCESSFUL_ADDED_OPERATION_CODE == jedis.sadd(key, testFullName);

		LOG.info(String.format("%s is the test execution value for %s in batch %s",
				shouldExecuteTest, testFullName, batchNumber));
		return shouldExecuteTest;
	}


	private String createTestRedisLabel(String className, String testName) {
		return String.format("%s#%s", className, testName);
	}

	private Jedis createJedis(String host, int port) {
		LOG.info(String.format("Create Jedis by settings [host: %s, port: %s]", host, port));
		return new Jedis(host, port);
	}
}
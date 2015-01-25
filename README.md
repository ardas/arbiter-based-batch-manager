# arbiter-based-batch-manager
To setup arbiter based batch manager:
*   install Redis server;
*   put the annotation @RunWith(ArbiterBasedThucydidesRunner.class) before the name of test cases
run your tests, using the  below information as parameters:

    1)   "test.run.key" - the run-time key;
    
    2)   "redis.host" - the host name of the redis-server (optional, defaults to localhost);
    
    3) "redis.port" - the port name of the  redis-server (optional, defaults to 6379).

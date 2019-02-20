package com.ais.sys.component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ThreadPoolComponent {
	private static final Logger logger = LoggerFactory.getLogger(ThreadPoolComponent.class);
	ThreadPoolExecutor executor = null;
	public ThreadPoolComponent() {
		executor = new ThreadPoolExecutor(100, 100, 2000, TimeUnit.MILLISECONDS,
	            new ArrayBlockingQueue<Runnable>(5));
	}
	
	public void exec(Runnable runable) {
		try {
			executor.execute(runable);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	
	
}

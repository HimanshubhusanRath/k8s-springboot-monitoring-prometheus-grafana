package com.hr.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringbootPrometheusGraphanaApplication {

	@GetMapping("message")
	public String getMessage()
	{
		return "Hello";
	}

	@GetMapping("flood")
	public String floodTheServer()
	{
		// Spawn multiple threads in an infinite loop where each thread runs for an infinite time period.
		try
		{
			while(true)
			{
				Runnable r = () ->
				{
					while(true)
					{
					}
				};
				new Thread(r).start();
				// Sleep for 5 seconds
				Thread.sleep(5000);
			}
		}
		catch (Exception e)
		{
		}

		return "Flooded";
	}


	public static void main(String[] args) {
		SpringApplication.run(SpringbootPrometheusGraphanaApplication.class, args);
	}

}

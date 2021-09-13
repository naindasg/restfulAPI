package edu.leicester.co2103cw3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import edu.leicester.co2103cw3.domain.Module;
import edu.leicester.co2103cw3.repository.ModuleRepository;

@SpringBootApplication
public class Co2103Cw3Application implements ApplicationRunner {

	
	
	public static void main(String[] args) {
		SpringApplication.run(Co2103Cw3Application.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
	}
	
	
	
	

}

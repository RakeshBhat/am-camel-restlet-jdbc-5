package student;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		System.out.println("-------addViewControllers-------------------------------------------");
		registry.addViewController("/").setViewName("/index.html");
		/*
		registry.addViewController("/").setViewName("forward:/index.html");
		registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
		super.addViewControllers( registry );
		 * */
	}
}

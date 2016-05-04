package student;

import java.util.HashMap;
import java.util.Map;

import org.restlet.ext.spring.SpringServerServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@ImportResource("classpath:camel-context.xml")
public class SampleTomcatJspApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SampleTomcatJspApplication.class);
	}
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SampleTomcatJspApplication.class, args);
	}
	@Bean
	public ServletRegistrationBean restletRegistrationBean(){
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(springServerServlet(),"/rs/*");
		servletRegistrationBean.setName("RestletServlet");
		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("org.restlet.component", "RestletComponent");
		servletRegistrationBean.setInitParameters(initParameters);
		return servletRegistrationBean;
	}
	@Bean
	public ServletRegistrationBean dispatcherServletRegistrationBean(){
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(dispatcherServlet(),"/ds/*");
		servletRegistrationBean.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
		return servletRegistrationBean;
	}
	@Bean
	public DispatcherServlet dispatcherServlet(){
		return new DispatcherServlet();
	}
	@Bean
	public SpringServerServlet springServerServlet(){
		SpringServerServlet springServerServlet = new SpringServerServlet();
		return springServerServlet;
	}
}

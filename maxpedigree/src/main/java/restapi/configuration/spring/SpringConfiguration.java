package restapi.configuration.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "restapi")
public class SpringConfiguration extends WebMvcConfigurerAdapter {
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

//	@Bean
//	public UrlBasedViewResolver getViewResovler() {
//		UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
//		urlBasedViewResolver.setViewClass(JstlView.class);
//		urlBasedViewResolver.setPrefix("");
//		urlBasedViewResolver.setSuffix("");
//		return urlBasedViewResolver;
//	}

	
	  @Bean
	  @DependsOn({ "jstlViewResolver" })
	  public ViewResolver viewResolver() {
	    return jstlViewResolver();
	  }

	  @Bean(name = "jstlViewResolver")
	  public ViewResolver jstlViewResolver() {
	    UrlBasedViewResolver resolver = new UrlBasedViewResolver();
	    resolver.setPrefix(""); // NOTE: no preffix here
	    resolver.setViewClass(JstlView.class);
	    resolver.setSuffix(""); // NOTE: no suffix here
	    return resolver;
	  }
	  
	  @Bean
	  public CommonsMultipartResolver multipartResolver() {
	      CommonsMultipartResolver resolver=new CommonsMultipartResolver();
	      resolver.setDefaultEncoding("utf-8");
	      return resolver;
	  }
}

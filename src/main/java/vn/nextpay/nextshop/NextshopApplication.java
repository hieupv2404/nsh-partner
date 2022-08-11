package vn.nextpay.nextshop;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vn.nextpay.nextshop.controller.dto.ContactMapper;
import vn.nextpay.nextshop.util.PartnerConnectAPI;

@SpringBootApplication
@Slf4j
public class NextshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(NextshopApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true).setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
        modelMapper.addMappings(new ContactMapper());
        return modelMapper;
    }

    @Bean
    public PartnerConnectAPI partnerConnectAPI(){
        return new PartnerConnectAPI();
    }

    private static final class WebMvcConfigurerAdapterExtension implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**").allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        }
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapterExtension();
    }

}

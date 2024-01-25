package com.fp.emailservice.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fp.emailservice.controller.handler.RestInterceptor;
import com.fp.emailservice.model.EmailRequest;
import com.fp.emailservice.model.EmailResponse;
import com.fp.emailservice.util.ApplicationProperties;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xml.MarshallingView;
import com.fp.emailservice.util.StringToEnumConverterFactory;

import java.io.IOException;

@Configuration
@ComponentScan(basePackages = "com.fp.emailservice")
@EnableWebMvc
public class AppConfig extends WebMvcConfigurerAdapter{

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(EmailRequest.class, EmailResponse.class, EmailResponse.class);
        return marshaller;
    }
    /*@Bean
    public MappingJackson2JsonView jackson2JsonView() {
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        // Modify configuration to take in empty (de)serialize and pretty print
        jsonView.getObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        jsonView.getObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        jsonView.getObjectMapper().disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return jsonView;
    }*/

    @Bean
    public MarshallingView marshallingView() {
        return new MarshallingView(jaxb2Marshaller());
    }

    @Bean
    public ConversionServiceFactoryBean conversionServiceFactoryBean() {
        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
        factoryBean.setConverters(Sets.newHashSet(new StringToEnumConverterFactory()));
        return factoryBean;
    }

    @Bean
    public ApplicationProperties buildProperties() throws IOException {
        return new ApplicationProperties();
    }

    @Bean
    public RequestMappingHandlerAdapter handlerAdapter() {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        adapter.setMessageConverters(Lists.newArrayList(jackson2HttpMessageConverter()));
        return adapter;
    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // Modify configuration to take in empty (de)serialize and pretty print
        converter.getObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        converter.getObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        converter.getObjectMapper().disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return converter;
    }

    @Bean
    public RestInterceptor createRestInterceptor() {
        return new RestInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(createRestInterceptor());
    }
}
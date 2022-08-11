package vn.nextpay.nextshop.config;

import com.mongodb.ConnectionString;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class MultipleMongoConfig {

    @Bean
    @Primary
    public MongoTemplate mongoTemplate() {
        MappingMongoConverter converter = mappingMongoConverter(primaryMongodbFactory());
        return new MongoTemplate(primaryMongodbFactory(), converter);
    }

    @Bean
    @Primary
    public MongoDatabaseFactory primaryMongodbFactory() {
        String uri = System.getenv("MONGO_URI");
        return new SimpleMongoClientDatabaseFactory(new ConnectionString(uri));
    }

    private MappingMongoConverter mappingMongoConverter(final MongoDatabaseFactory mongoDbFactory) {
        MongoMappingContext mongoMappingContext = new MongoMappingContext();
        mongoMappingContext.setAutoIndexCreation(true);
        MappingMongoConverter converter = new MappingMongoConverter(
                new DefaultDbRefResolver(mongoDbFactory), mongoMappingContext);
        converter.setCustomConversions(customConversions());
        converter.afterPropertiesSet();

        return converter;
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(DateToZonedDateTimeConverter.INSTANCE);
        converters.add(ZonedDateTimeToDateConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }

    public enum DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {
        INSTANCE;

        private DateToZonedDateTimeConverter() {
        }

        public ZonedDateTime convert(Date source) {
            return source.toInstant().atZone(ZoneOffset.UTC);
        }

    }

    public enum ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {
        INSTANCE;

        private ZonedDateTimeToDateConverter() {
        }

        public Date convert(ZonedDateTime source) {
            return Date.from(source.toInstant());
        }
    }


}

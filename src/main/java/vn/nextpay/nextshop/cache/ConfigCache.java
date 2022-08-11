//package vn.nextpay.nextshop.cache;
//
//import io.micrometer.core.instrument.util.StringUtils;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import vn.nextpay.domain.Config;
//
//import java.time.ZoneOffset;
//import java.time.ZonedDateTime;
//import java.util.HashMap;
//import java.util.List;
//
//public class ConfigCache extends FCache<String, String> {
//
//    private static ConfigCache instance;
//    private final int DEFAULT_SIZE = 100;  // record
//    private final int DEFAULT_EXPIRE_TIME = 60 * 60; // second
//    private MongoTemplate template;
//
//    private ConfigCache(MongoTemplate template) {
//        this.data = new HashMap<>();
//        this.size = DEFAULT_SIZE;
//        this.template = template;
//    }
//
//    public static ConfigCache getInstance(MongoTemplate template) {
//        if (instance == null) {
//            synchronized (ConfigCache.class) {
//                if (instance == null) {
//                    instance = new ConfigCache(template);
//                }
//            }
//        }
//        return instance;
//    }
//
//    public String get(String key) {
//        if (StringUtils.isBlank(key)) {
//            return null;
//        }
//        String data = super.get(key);
//        if (data == null) {
//            return reload(key);
//        }
//        return data;
//    }
//
//    public String reload(String key) {
//        Config config = template.findOne(new Query(Criteria.where("key").is(key).and("show").is(1)), Config.class);
//        if (config != null && config.getValue() != null) {
//            super.put(key, config.getValue(), ZonedDateTime.now(ZoneOffset.UTC).plusSeconds(DEFAULT_EXPIRE_TIME).toInstant().toEpochMilli());
//            return config.getValue();
//        }
//        return null;
//    }
//
//    public String reloadAll() {
//        this.data = new HashMap<>();
//        List<Config> config = template.find(new Query(Criteria.where("show").is(1)), Config.class);
//        config.forEach(c -> {
//            super.put(c.getKey(), c.getValue(), ZonedDateTime.now(ZoneOffset.UTC).plusSeconds(DEFAULT_EXPIRE_TIME).toInstant().toEpochMilli());
//        });
//        return null;
//    }
//
//}

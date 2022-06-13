package sample.com.hazelcast.cloud.maploader5.mongo;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.hazelcast.core.HazelcastJsonValue;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.MapLoaderLifecycleSupport;
import com.hazelcast.map.MapStore;

@Slf4j
// tag::class[]
public class MongoPersonMapStore implements MapStore<Integer, HazelcastJsonValue>, MapLoaderLifecycleSupport {

    private MongoClient mongoClient;

    private PersonRepository personRepository;

    @Override
    public void init(HazelcastInstance hazelcastInstance, Properties properties, String mapName) {
        this.mongoClient = new MongoClient(new MongoClientURI(properties.getProperty("uri")));
        MongoDatabase database = this.mongoClient.getDatabase(properties.getProperty("database"));
        this.personRepository = new MongoPersonRepository(mapName, database);
        log.info("MongoPersonMapStore::initialized");
    }

    @Override
    public void destroy() {
        MongoClient mongoClient = this.mongoClient;
        if (mongoClient != null) {
            mongoClient.close();
        }
        log.info("MongoPersonMapStore::destroyed");
    }

    @Override
    public void delete(Integer key) {
        log.info("MongoPersonMapStore::delete key {}", key);
        getRepository().delete(key);
    }

    @Override
    public void deleteAll(Collection<Integer> keys) {
        log.info("MongoPersonMapStore::delete all {}", keys);
        getRepository().delete(keys);
    }

    @Override
    public HazelcastJsonValue load(Integer key) {
        log.info("MongoPersonMapStore::load by key {}", key);
        return getRepository().find(key).orElse(null);
    }

    @Override
    public Map<Integer, HazelcastJsonValue> loadAll(Collection<Integer> keys) {
        log.info("MongoPersonMapStore::loadAll by keys {}", keys);
        return getRepository().findAll(keys).stream()
            .collect();
    }

    @Override
    public Iterable<Integer> loadAllKeys() {
        log.info("MongoPersonMapStore::loadAllKeys");
        return getRepository().findAllIds();
    }

    private PersonRepository getRepository() {
        PersonRepository personRepository = this.personRepository;
        if (personRepository == null) {
            throw new IllegalStateException("Person Repository must not be null!");
        }
        return this.personRepository;
    }

}
// end::class[]

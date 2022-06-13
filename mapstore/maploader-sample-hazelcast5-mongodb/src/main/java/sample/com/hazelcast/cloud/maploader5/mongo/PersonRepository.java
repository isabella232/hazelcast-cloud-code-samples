package sample.com.hazelcast.cloud.maploader5.mongo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.hazelcast.core.HazelcastJsonValue;

public interface PersonRepository {

    default void delete(Integer id) {
        delete(Collections.singletonList(id));
    }

    void deleteAll();

    void delete(Collection<Integer> id);

    List<HazelcastJsonValue> findAll(Collection<Integer> ids);

    Collection<Integer> findAllIds();

    default Optional<HazelcastJsonValue> find(Integer id) {
        return findAll(Collections.singletonList(id)).stream().findFirst();
    }

}

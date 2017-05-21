package pl.edu.agh.ztis.steam;

import java.util.Optional;

class Environment {

    Optional<String> getString(String key) {
        return Optional.ofNullable(System.getenv(key));
    }

    Optional<Long> getLong(String key) {
        return getString(key).map(Long::valueOf);
    }

    Optional<Integer> getInt(String key) {
        return getString(key).map(Integer::valueOf);
    }
}

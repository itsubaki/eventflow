package com.github.itsubaki.camellia.router;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RouterIF<V> {

	Set<V> get();

	Optional<V> findOne(String find);

	List<V> findAll(String find);

	void put(String key, V target);

	void remove(V target);

}
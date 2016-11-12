package com.github.itsubaki.eventflow.router;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.itsubaki.eventflow.cache.CacheFactory;
import com.github.itsubaki.eventflow.cache.CacheIF;
import com.github.itsubaki.eventflow.cache.CacheStrategy;

public class RouterRegexp<V> implements RouterIF<V> {
	private Map<Pattern, V> object = new ConcurrentHashMap<>();
	private CacheIF<String, V> cache;
	private CacheIF<String, List<V>> cacheAll;

	public RouterRegexp() {
		cache = CacheFactory.newInstance(CacheStrategy.LRU, 1024);
		cacheAll = CacheFactory.newInstance(CacheStrategy.LRU, 1024);
	}

	@Override
	public void put(String regexp, V target) {
		Pattern p = Pattern.compile(regexp);
		object.put(p, target);
	}

	@Override
	public Optional<V> findOne(String name) {
		V cached = cache.get(name);
		if (cached != null) {
			return Optional.of(cached);
		}

		V result = null;
		cache.put(name, result);

		Stream<Pattern> stream = object.keySet().stream();
		Optional<Pattern> opt = stream.filter(k -> k.matcher(name).find())
				.findFirst();

		if (opt.isPresent()) {
			result = object.get(opt.get());
		}

		return Optional.of(result);
	}

	@Override
	public List<V> findAll(String name) {
		List<V> cached = cacheAll.get(name);
		if (cached != null) {
			return cached;
		}

		List<V> result = new ArrayList<>();
		cacheAll.put(name, result);

		Stream<Pattern> stream = object.keySet().stream();
		List<Pattern> list = stream.filter(p -> p.matcher(name).find())
				.collect(Collectors.toList());
		list.stream().forEach(p -> result.add(object.get(p)));

		return result;
	}

}
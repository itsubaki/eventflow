package com.github.itsubaki.eventflow.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.itsubaki.eventflow.event.EventIF;
import com.github.itsubaki.eventflow.router.RouterIF;

public abstract class NodeABS implements NodeIF {
	private String name;
	private String regexp;
	private RouterIF<NodeIF> router;

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}

	@Override
	public String getRegexp() {
		return regexp;
	}

	@Override
	public RouterIF<NodeIF> getRouter() {
		return router;
	}

	@Override
	public void setRouter(RouterIF<NodeIF> router) {
		this.router = router;
	}

	@Override
	public Optional<String> transfer(EventIF event) {
		Optional<NodeIF> opt = router.findOne(event.getName());
		if (opt.isPresent()) {
			return opt.get().recieve(event);
		}

		return Optional.empty();
	}

	@Override
	public List<String> transferAll(EventIF event) {
		List<String> list = new ArrayList<>();
		router.findAll(event.getName()).forEach(node -> {
			Optional<String> opt = node.recieve(event);
			opt.ifPresent(value -> list.add(value));
		});
		return list;
	}

}

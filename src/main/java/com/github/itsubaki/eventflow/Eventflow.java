package com.github.itsubaki.eventflow;

import java.util.Optional;

import com.github.itsubaki.eventflow.node.NodeIF;
import com.github.itsubaki.eventflow.router.RouterIF;

public class Eventflow implements EventflowIF {
	private RouterIF<NodeIF> router;

	@Override
	public void setRouter(RouterIF<NodeIF> router) {
		this.router = router;
	}

	@Override
	public RouterIF<NodeIF> getRouter() {
		return router;
	}

	@Override
	public void add(NodeIF node) {
		node.setRouter(router);
		node.onSetup();
		router.put(node.route(), node);
	}

	@Override
	public void remove(NodeIF node) {
		router.remove(node);
		node.close();
		node.onClose();
	}

	@Override
	public void shutdown() {
		router.get().stream().forEach(n -> n.close());
		router.get().stream().forEach(n -> n.onClose());
	}

	@Override
	public Optional<NodeIF> getNode(String name) {
		return router.get().stream().filter(n -> n.name().equalsIgnoreCase(name)).findFirst();
	}

}

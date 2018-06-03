package org.box.metadata.cli.shell.impl.tree;

import java.util.HashMap;

public class TraversableTestMapModel implements TraversableTreeModel {

	private String current = "/";
	private final HashMap<String, Object[]> map;

	public TraversableTestMapModel(HashMap<String, Object[]> map) {
		this.map = map;
	}

	@Override
	public void setCurrentFolder(String path) {
		this.current = path;
	}

	@Override
	public String currentFolder() {
		return current;
	}

	@Override
	public boolean fileExists(String path) {
		return map.containsKey(path);
	}

	@Override
	public Object[] list(String path) {
		if (".".equalsIgnoreCase(path)
				|| "./".equalsIgnoreCase(path))
			path = current;
		return map.get(path);
	}

	@Override
	public Object get(String path) {
		throw new IllegalStateException("not used currently");
	}

	@Override
	public boolean isFolder(String path) {
		Object[] f = map.get(path);
		return f != null && f.length > 0;
	}
}
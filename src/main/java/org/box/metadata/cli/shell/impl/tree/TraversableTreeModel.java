package org.box.metadata.cli.shell.impl.tree;


/**
 * <p>
 * More flexible and, as the result, more complex implementation 
 * comparing to the traditional Tree Models like {@link javax.swing.tree.TreeModel}.
 * <\p>
 * 
 * <p>
 * The model designed to be well suited for implementing Tree Models like 
 * File Systems used by different progressive OS.
 * <p>
 * 
 * <p>
 * A <code>path</code> uses to access(select only) hierarchical data described 
 * in the mode. Where is no fixed <code>path</code> convention. The path 
 * syntax could be OS specific ('/' or '\\' separator) or ever user 
 * specific (e.g.: '.' separator), and may support relative or/and canonical 
 * form of the data accessing expressions.
 * </p> 
 * 
 * @author alexk
 *
 */
public interface TraversableTreeModel {

	void setCurrentFolder(String path);

	String currentFolder();
	
	boolean fileExists(String path);

	Object[] list(String path);

	Object get(String path);

	boolean isFolder(String path);
}

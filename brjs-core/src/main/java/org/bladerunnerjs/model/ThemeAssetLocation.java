package org.bladerunnerjs.model;

import java.io.File;
import java.util.List;

import org.bladerunnerjs.model.engine.Node;
import org.bladerunnerjs.model.engine.RootNode;
import org.bladerunnerjs.utility.filemodification.FileModifiedChecker;
import org.bladerunnerjs.utility.filemodification.NodeFileModifiedChecker;

public class ThemeAssetLocation extends DeepAssetLocation {
	private final FileModifiedChecker dirModifiedChecker;
	private final BRJS brjs;
	private List<String> themes;
	
	public ThemeAssetLocation(RootNode rootNode, Node parent, File dir) {
		super(rootNode, parent, dir);
		brjs = (BRJS) rootNode;
		dirModifiedChecker = new NodeFileModifiedChecker(this);
	}
	
	public List<String> themes() {
		if(dirModifiedChecker.hasChangedSinceLastCheck()) {
			for(File dir : brjs.getFileIterator(dir()).dirs()) {
				themes.add(dir.getName());
			}
		}
		
		return themes;
	}
}
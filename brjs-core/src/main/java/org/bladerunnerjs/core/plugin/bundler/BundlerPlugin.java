package org.bladerunnerjs.core.plugin.bundler;

import java.util.List;

import org.bladerunnerjs.core.plugin.content.ContentPlugin;
import org.bladerunnerjs.model.AssetFile;
import org.bladerunnerjs.model.AssetLocation;
import org.bladerunnerjs.model.LinkedAssetFile;
import org.bladerunnerjs.model.SourceFile;


public interface BundlerPlugin extends ContentPlugin {
	List<SourceFile> getSourceFiles(AssetLocation assetLocation);
	List<LinkedAssetFile> getLinkedResourceFiles(AssetLocation assetLocation);
	List<AssetFile> getResourceFiles(AssetLocation assetLocation);
}

package org.bladerunnerjs.testing.utility;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.bladerunnerjs.model.Asset;
import org.bladerunnerjs.model.AssetLocation;
import org.bladerunnerjs.model.BRJS;
import org.bladerunnerjs.model.BundleSet;
import org.bladerunnerjs.model.LinkedAsset;
import org.bladerunnerjs.model.ParsedContentPath;
import org.bladerunnerjs.model.SourceModule;
import org.bladerunnerjs.model.exception.request.BundlerProcessingException;
import org.bladerunnerjs.plugin.BundlerPlugin;
import org.bladerunnerjs.plugin.base.AbstractBundlerPlugin;
import org.bladerunnerjs.utility.ContentPathParser;
import org.bladerunnerjs.utility.ContentPathParserBuilder;


public class MockBundlerPlugin extends AbstractBundlerPlugin implements BundlerPlugin
{
	@Override
	public void setBRJS(BRJS brjs)
	{
	}

	@Override
	public String getRequestPrefix() {
		return "mock";
	}
	
	@Override
	public String getMimeType()
	{
		return "";
	}

	@Override
	public ContentPathParser getContentPathParser()
	{
		ContentPathParserBuilder contentPathParserBuilder = new ContentPathParserBuilder();
		return contentPathParserBuilder.build();
	}

	@Override
	public void writeContent(ParsedContentPath contentPath, BundleSet bundleSet, OutputStream os) throws BundlerProcessingException
	{
	}

	@Override
	public List<String> getValidDevRequestPaths(BundleSet bundleSet, String locale) throws BundlerProcessingException
	{
		return Arrays.asList();
	}

	@Override
	public List<String> getValidProdRequestPaths(BundleSet bundleSet, String locale) throws BundlerProcessingException
	{
		return Arrays.asList();
	}

	@Override
	public List<SourceModule> getSourceModules(AssetLocation assetLocation)
	{
		return Arrays.asList();
	}

	@Override
	public List<LinkedAsset> getLinkedResourceFiles(AssetLocation assetLocation)
	{
		return Arrays.asList();
	}

	@Override
	public List<Asset> getResourceFiles(AssetLocation assetLocation)
	{
		return Arrays.asList();
	}

}

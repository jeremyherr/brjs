package org.bladerunnerjs.testing.specutility;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;

import org.bladerunnerjs.model.RequestMode;
import org.bladerunnerjs.model.Workbench;
import org.bladerunnerjs.model.exception.ConfigException;
import org.bladerunnerjs.model.exception.ModelOperationException;
import org.bladerunnerjs.model.exception.RequirePathException;
import org.bladerunnerjs.plugin.Locale;
import org.bladerunnerjs.testing.specutility.engine.BundlableNodeCommander;
import org.bladerunnerjs.testing.specutility.engine.CommanderChainer;
import org.bladerunnerjs.testing.specutility.engine.SpecTest;
import org.bladerunnerjs.utility.NoTagHandlerFoundException;
import org.bladerunnerjs.utility.TagPluginUtility;

public class WorkbenchCommander extends BundlableNodeCommander<Workbench<?>> 
{
	private final Workbench<?> workbench;
	
	public WorkbenchCommander(SpecTest modelTest, Workbench<?> workbench) 
	{
		super(modelTest, workbench);
		this.workbench = workbench;
	}
	
	public CommanderChainer getBundledFiles() 
	{
		fail("the model doesn't yet support bundling!");
		return commanderChainer;
	}
	
	public BundleInfoCommander getBundleInfo() throws Exception 
	{
		return new BundleInfoCommander(workbench.getBundleSet());
	}

	public void pageLoaded(StringBuffer pageResponse, String locale) throws ConfigException, IOException, ModelOperationException, NoTagHandlerFoundException, RequirePathException 
	{
		StringWriter writer = new StringWriter();	
		TagPluginUtility.filterContent(fileUtil.readFileToString(workbench.file("index.html")), workbench.getBundleSet(), writer, RequestMode.Dev, new Locale(locale), workbench.root().getAppVersionGenerator().getDevVersion());
		pageResponse.append(writer.toString());
	}
}

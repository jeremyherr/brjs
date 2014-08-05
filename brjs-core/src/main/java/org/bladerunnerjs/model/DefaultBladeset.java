package org.bladerunnerjs.model;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.naming.InvalidNameException;

import org.bladerunnerjs.model.engine.Node;
import org.bladerunnerjs.model.engine.NodeList;
import org.bladerunnerjs.model.engine.RootNode;
import org.bladerunnerjs.model.exception.modelupdate.ModelUpdateException;


public class DefaultBladeset extends Bladeset
{
	
	private final NodeList<Blade> blades = new NodeList<>(this, Blade.class, ".", null);
	
	public DefaultBladeset(RootNode rootNode, Node parent, File dir)
	{
		super(rootNode, parent, dir, "default");
	}
	
	@Override
	public void populate() throws InvalidNameException, ModelUpdateException
	{
		super.populate();
	}
	
	@Override
	public String requirePrefix() {
		App app = parent();
		return app.getRequirePrefix();
	}
	
	@Override
	public List<TypedTestPack> testTypes()
	{
		return Collections.emptyList();
	}
	
	public List<Blade> blades()
	{
		return blades.list();
	}
	
	public Blade blade(String bladeName)
	{
		return blades.item(bladeName);
	}

}

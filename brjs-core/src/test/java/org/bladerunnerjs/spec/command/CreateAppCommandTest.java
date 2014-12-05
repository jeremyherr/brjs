package org.bladerunnerjs.spec.command;

import static org.bladerunnerjs.model.App.Messages.*;
import static org.bladerunnerjs.plugin.plugins.commands.standard.CreateAppCommand.Messages.*;

import org.bladerunnerjs.model.App;
import org.bladerunnerjs.model.DirNode;
import org.bladerunnerjs.model.exception.command.ArgumentParsingException;
import org.bladerunnerjs.model.exception.command.CommandArgumentsException;
import org.bladerunnerjs.model.exception.command.NodeAlreadyExistsException;
import org.bladerunnerjs.model.exception.name.InvalidDirectoryNameException;
import org.bladerunnerjs.model.exception.name.InvalidRootPackageNameException;
import org.bladerunnerjs.model.exception.name.UnableToAutomaticallyGenerateAppRequirePrefixException;
import org.bladerunnerjs.model.exception.template.TemplateNotFoundException;
import org.bladerunnerjs.plugin.plugins.commands.standard.CreateAppCommand;
import org.bladerunnerjs.testing.specutility.engine.SpecTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CreateAppCommandTest extends SpecTest {
	App app;
	App badApp;
	DirNode appJars;
	
	@Before
	public void initTestObjects() throws Exception
	{
		given(brjs).hasCommandPlugins(new CreateAppCommand())
			.and(brjs).hasBeenCreated();
			app = brjs.app("app");
			badApp = brjs.app("app#$@/");
			appJars = brjs.appJars();
	}
	
	@Test
	public void exceptionIsThrownIfThereAreTooFewArguments() throws Exception {
		when(brjs).runCommand("create-app");
		then(exceptions).verifyException(ArgumentParsingException.class, unquoted("Parameter 'new-app-name' is required"))
			.whereTopLevelExceptionIs(CommandArgumentsException.class);
	}
	
	@Test
	public void exceptionIsThrownIfThereAreTooManyArguments() throws Exception {
		when(brjs).runCommand("create-app", "a", "b", "c");
		then(exceptions).verifyException(ArgumentParsingException.class, unquoted("Unexpected argument: c"))
			.whereTopLevelExceptionIs(CommandArgumentsException.class);
	}
	
	@Test
	public void exceptionIsThrownIfTheAppNameIsNotAValidDirectoryName() throws Exception {
		when(brjs).runCommand("create-app", "app#$@/", "appx");
		then(exceptions).verifyException(InvalidDirectoryNameException.class, "app#$@/", badApp.dir().getPath())
			.whereTopLevelExceptionIs(CommandArgumentsException.class);
	}
	
	@Test
	public void exceptionIsThrownIfTheAppNamespaceIsNotAValidPackageName() throws Exception {
		when(brjs).runCommand("create-app", "app", "app-x");
		then(exceptions).verifyException(InvalidRootPackageNameException.class, "app-x", app.dir().getPath())
			.whereTopLevelExceptionIs(CommandArgumentsException.class);
	}
	
	@Test
	public void exceptionIsThrownIfTheAppNamespaceIsNotAValidRootPackageName() throws Exception {
		when(brjs).runCommand("create-app", "app", "caplin");
		then(exceptions).verifyException(InvalidRootPackageNameException.class, "caplin", app.dir().getPath())
			.whereTopLevelExceptionIs(CommandArgumentsException.class);
	}
	
	@Test
	public void exceptionIsThrownWhenInvalidRootPackageAppNameIsUsedAsDefaultNamespace() throws Exception {
		when(brjs).runCommand("create-app", "1app");
		then(exceptions).verifyException(UnableToAutomaticallyGenerateAppRequirePrefixException.class, unquoted("Unable to automatically calculate app namespace for app '1app'"))
			.whereTopLevelExceptionIs(CommandArgumentsException.class);
	}
	
	@Test
	public void invalidDrirectoryExceptionIsThrownWhenAppNameIsInvalidAndAppNamespaceIsAutoGenerated() throws Exception {
		when(brjs).runCommand("create-app", "...");
		then(exceptions).verifyException(InvalidDirectoryNameException.class, "...", brjs.app("...").dir().getPath())
			.whereTopLevelExceptionIs(CommandArgumentsException.class);
	}
	
	@Test
	public void exceptionIsThrownIfTheAppAlreadyExists() throws Exception {
		given(app).hasBeenCreated();
		when(brjs).runCommand("create-app", "app", "appx");
		then(exceptions).verifyException(NodeAlreadyExistsException.class, unquoted(app.getName()))
			.whereTopLevelExceptionIs(CommandArgumentsException.class);
	}
	
	@Test
	public void appIsCreatedWhenAllArgumentsAreValid() throws Exception {
		given(appJars).hasBeenCreated()
			.and(logging).enabled()
			.and(brjs.confTemplateGroup("default").template("app")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect-test-unit-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect-test-acceptance-default")).hasBeenCreated();
		when(brjs).runCommand("create-app", "app", "appx");
		then(app).dirExists()
			.and(logging).infoMessageReceived(APP_DEPLOYED_LOG_MSG, app.getName(), app.dir().getPath())
			.and(logging).containsFormattedConsoleMessage(APP_CREATED_CONSOLE_MSG, app.getName())
			.and(logging).containsFormattedConsoleMessage(APP_DEPLOYED_CONSOLE_MSG, app.getName());
	}
	
	@Test
	public void appIsCreatedWithTheSpecifiedTemplate() throws Exception {
		given(brjs.confTemplateGroup("angular").template("app")).containsFile("fileForApp.txt")
			.and(brjs.confTemplateGroup("angular").template("aspect")).hasBeenCreated()
			.and(brjs.confTemplateGroup("angular").template("aspect-test-unit-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("angular").template("aspect-test-acceptance-default")).hasBeenCreated();
		when(brjs).runCommand("create-app", "app", "--template", "angular");
		then(app).dirExists()
			.and(app).hasFile("fileForApp.txt");
	}
	
	@Test
	public void appIsCreatedWithTheSpecifiedTemplateIfMoreTemplatesExist() throws Exception {
		given(brjs.confTemplateGroup("angular").template("app")).containsFile("fileForAppAngular.txt")
			.and(brjs.confTemplateGroup("angular").template("aspect")).hasBeenCreated()
			.and(brjs.confTemplateGroup("angular").template("aspect-test-unit-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("angular").template("aspect-test-acceptance-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("app")).containsFile("fileForAppDefault.txt")
			.and(brjs.confTemplateGroup("default").template("aspect")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect-test-unit-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect-test-acceptance-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("myTemplate").template("app")).containsFile("fileForAppMyTemplate.txt")
			.and(brjs.confTemplateGroup("myTemplate").template("aspect")).hasBeenCreated()
			.and(brjs.confTemplateGroup("myTemplate").template("aspect-test-unit-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("myTemplate").template("aspect-test-acceptance-default")).hasBeenCreated();
		when(brjs).runCommand("create-app", "app", "--template", "myTemplate");
		then(app).dirExists()
			.and(app).hasFile("fileForAppMyTemplate.txt");
	}
	
	@Test
	public void defaultTemplateIsUsedIfNoneSpecifiedAndMultipleTemplatesExist() throws Exception {
		given(brjs.confTemplateGroup("angular").template("app")).containsFile("fileForAppAngular.txt")
			.and(brjs.confTemplateGroup("angular").template("aspect")).hasBeenCreated()
			.and(brjs.confTemplateGroup("angular").template("aspect-test-unit-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("angular").template("aspect-test-acceptance-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("app")).containsFile("fileForAppDefault.txt")
			.and(brjs.confTemplateGroup("default").template("aspect")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect-test-unit-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect-test-acceptance-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("myTemplate").template("app")).containsFile("fileForAppMyTemplate.txt")
			.and(brjs.confTemplateGroup("myTemplate").template("aspect")).hasBeenCreated()
			.and(brjs.confTemplateGroup("myTemplate").template("aspect-test-unit-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("myTemplate").template("aspect-test-acceptance-default")).hasBeenCreated();
		when(brjs).runCommand("create-app", "app");
		then(app).dirExists()
			.and(app).hasFile("fileForAppDefault.txt");
	}
	
	@Test
	public void exceptionIsThrownIfSpecifiedTemplateDoesNotExist() throws Exception {
		when(brjs).runCommand("create-app", "app", "--template", "nonexistent");
		then(exceptions).verifyException(TemplateNotFoundException.class);
	}
	
	public void exceptionIsThrownIfTemplateForImplicitlyPopulatedAspectDoesNotExist() throws Exception {
		given(brjs.confTemplateGroup("angular").template("app")).containsFile("fileForAppAngular.txt");
		when(brjs).runCommand("create-app", "app", "--template", "angular");
		then(exceptions).verifyException(TemplateNotFoundException.class);
	}
	
	public void exceptionIsThrownIfTemplateForImplicitlyPopulatedAspectTestUnitDefaultDoesNotExist() throws Exception {
		given(brjs.confTemplateGroup("angular").template("app")).containsFile("fileForAppAngular.txt")
			.and(brjs.confTemplateGroup("angular").template("aspect")).hasBeenCreated()
			.and(brjs.confTemplateGroup("angular").template("aspect-test-unit-default")).hasBeenCreated();
		when(brjs).runCommand("create-app", "app", "--template", "angular");
		then(exceptions).verifyException(TemplateNotFoundException.class);
	}
	
	public void exceptionIsThrownIfTemplateForImplicitlyPopulatedAspectTestAcceptanceDefaultDoesNotExist() throws Exception {
		given(brjs.confTemplateGroup("angular").template("app")).containsFile("fileForAppAngular.txt")
			.and(brjs.confTemplateGroup("angular").template("aspect")).hasBeenCreated();
		when(brjs).runCommand("create-app", "app", "--template", "angular");
		then(exceptions).verifyException(TemplateNotFoundException.class);
	}
	
	@Test
	public void requirePrefixIsOptional() throws Exception {
		App myApp = brjs.app("myApp");
		
		given(appJars).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("app")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect-test-unit-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect-test-acceptance-default")).hasBeenCreated();
		when(brjs).runCommand("create-app", "myApp");
		then(myApp).dirExists()
			.and(myApp.appConf()).namespaceIs("myapp");
	}
	
	@Test
	public void requirePrefixIsOptionalAndCorrectPrefixIsWrittenToAppConf() throws Exception {
		App myApp = brjs.app("myApp");
		
		given(appJars).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("app")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect-test-unit-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect-test-acceptance-default")).hasBeenCreated();
		when(brjs).runCommand("create-app", "myApp");
		then(myApp).dirExists()
			.and(myApp).fileContentsContains("app.conf","myapp");
	}
	
	@Test @Ignore // is this test valid now that deploy() doesnt throw an exception?
	public void appCreationConsoleOutputOccursEvenIfAppDeploymentFails() throws Exception {
		when(brjs).runCommand("create-app", "app", "appx");
		then(app).dirExists()
			.and(logging).containsFormattedConsoleMessage(APP_CREATED_CONSOLE_MSG, app.getName())
			.and(logging).doesNotcontainConsoleText(APP_DEPLOYED_LOG_MSG, app.getName())
			.and(logging).errorMessageReceived(APP_DEPLOYMENT_FAILED_LOG_MSG, app.getName(), app.dir())
			.and(exceptions).verifyException(IllegalStateException.class, appJars.dir().getPath());
	}
	
	@Test
	public void commandIsAutomaticallyLoaded() throws Exception
	{
		given(brjs).hasBeenAuthenticallyCreated()
			.and(appJars).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("app")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect-test-unit-default")).hasBeenCreated()
			.and(brjs.confTemplateGroup("default").template("aspect-test-acceptance-default")).hasBeenCreated();
		when(brjs).runCommand("create-app", "app", "appx");
		then(exceptions).verifyNoOutstandingExceptions();
	}
}

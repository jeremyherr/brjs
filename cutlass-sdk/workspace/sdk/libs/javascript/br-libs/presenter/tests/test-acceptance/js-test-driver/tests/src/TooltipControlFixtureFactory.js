TooltipControlFixtureFactory = function()
{
};

br.Core.implement(TooltipControlFixtureFactory, br.test.FixtureFactory);

TooltipControlFixtureFactory.prototype.addFixtures = function(oFixtureRegistry)
{
	oFixtureRegistry.addFixture("form", 
			new br.presenter.testing.PresenterComponentFixture('tooltip-control-test',
					'TooltipControlPresentationModel')); 
};

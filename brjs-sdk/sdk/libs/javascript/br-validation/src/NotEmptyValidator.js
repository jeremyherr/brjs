/**
 * @module br/validation/NotEmptyValidator
 */

/**
 * @class
 * @alias module:br/validation/NotEmptyValidator
 * @implements module:br/validation/Validator
 */
br.validation.NotEmptyValidator = function(sFailureMessage)
{
	this.sMessage = sFailureMessage;
};

br.Core.implement(br.validation.NotEmptyValidator, br.validation.Validator);

br.validation.NotEmptyValidator.prototype.validate = function(vValue, mAttributes, oValidationResult)
{
	if(vValue=="")
	{
		var bIsValid = false;
	}
	else
	{
		var bIsValid = true;
	}
	
	var oTranslator = require("br/I18n").getTranslator();
	var sFailureMessage = oTranslator.tokenExists(this.sMessage) ? oTranslator.getMessage(this.sMessage) : this.sMessage;
	
	oValidationResult.setResult(bIsValid, sFailureMessage);
};

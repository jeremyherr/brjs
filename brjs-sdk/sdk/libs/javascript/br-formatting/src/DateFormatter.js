/**
 * @module br/formatting/DateFormatter
 */

br.Core.thirdparty("momentjs");

/**
 * @class
 * @alias module:br/formatting/DateFormatter
 * @implements module:br/formatting/Formatter
 *
 * @classdesc
 * Formats a date value by converting it from a specified input format to a new output format.
 * <p/>
 * <code>DateFormatter</code> is typically used with Presenter, but can be invoked programmatically
 * as in the following examples which evaluate to "09-Sep-2001 01:46:40" and "2001Sep09" respectively:
 * <p/>
 * <code>br.formatting.DateFormatter.format(1e12, {inputFormat:"U"})</code><br/>
 * <code>br.formatting.DateFormatter.format(1e12, {inputFormat:"U", outputFormat:"YMd"})</code>
 * <p/>
 *
 * See {@link module:br/presenter/parser/DateParser} for the complementary parser.
 */
br.formatting.DateFormatter = function()
{
	this.m_sFormatDefault = "DD-MM-YYYY HH:mm:ss";
};

br.Core.implement(br.formatting.DateFormatter, br.formatting.Formatter);

/**
 * Formats a date by converting it from a specified input format to a new output format.
 *
 * @param {string|Date} vValue the input date
 * @param {object} mAttributes the map of attributes.
 * @param {string} [mAttributes.inputFormat='DD-MM-YYYY HH:mm:ss'] format of the input date, expressed with {@link http://momentjs.com/docs/#/parsing/string-format/|Moment.js format tokens}
 * @param {string} [mAttributes.outputFormat='DD-MM-YYYY HH:mm:ss'] format of the output date, expressed with {@link http://momentjs.com/docs/#/parsing/string-format/|Moment.js format tokens}
 * @param {boolean} [mAttributes.adjustForTimezone=false] whether the formatter should adjust the date according to the client's timezone
 * @return  the output date.
 * @type String
 */
br.formatting.DateFormatter.prototype.format = function(vValue, mAttributes) {
	if (vValue) {
		var sInputFormat = mAttributes["inputFormat"];
		var bAdjustForTimezone = mAttributes["adjustForTimezone"];
		var oDate = this.parseDate(vValue, sInputFormat);
		if (oDate) {
			var sOutputFormat = mAttributes["outputFormat"];
			vValue = this.formatDate(oDate, sOutputFormat, mAttributes.adjustForTimezone);
		}
	}
	return vValue;
};

/**
 * @private
 */
br.formatting.DateFormatter.prototype.parseDate = function(vDate, sDateFormat, bEndOfUnit)
{
	if (!vDate)
	{
		return null;
	}
	if (vDate instanceof Date)
	{
		sDateFormat = "javascript";
	}
	else if (!sDateFormat)
	{
		sDateFormat = this.getDateFormat(sDateFormat);
	}

	switch (sDateFormat) {
		case "java":
			var oDate = new Date();
			oDate.setTime(Number(vDate));
			return oDate;
		case "javascript":
			return vDate;
		case "U":
			return moment(vDate*1000).toDate();
		default:
			var oMoment = moment(String(vDate), sDateFormat);
			if (bEndOfUnit === true && sDateFormat.toLowerCase().indexOf('d') === -1) {
				oMoment.endOf(sDateFormat === 'YYYY' ? 'year' : 'month');
			}
			var sValidationString = oMoment.format(sDateFormat);
			return (sValidationString.toLowerCase() == String(vDate).toLowerCase()) ? oMoment.toDate() : null;
	}
};

/**
 * @private
 */
br.formatting.DateFormatter.prototype.formatDate = function(oDate, sDateFormat, bAdjustForTimezone) {
	if(bAdjustForTimezone)
	{

		oDate = this._adjustDateForTimezone(oDate);
	}
	sDateFormat = this.getDateFormat(sDateFormat);
	switch (sDateFormat) {
		case "java":
			return String(oDate.getTime());
		case "javascript":
			return oDate;
		case "ISO":
			return oDate.toISOString();
		case "localize":
			var oTranslator = require("br/I18n").getTranslator();
			return oTranslator.formatDate(oDate);
		default:
			var oTranslator = require("br/I18n").getTranslator();
			return oTranslator.formatDate(oDate, sDateFormat);
	}
};

/**
 * @private
 */
br.formatting.DateFormatter.prototype._adjustDateForTimezone = function(oDate) {
	var oDateClone = new Date(oDate.getTime()),
		d = new Date(),
		timezoneOffsetInMinutes = -(d.getTimezoneOffset());

	oDateClone.setMinutes(oDate.getMinutes() + timezoneOffsetInMinutes);

	return oDateClone;
};

/**
 * @private
 */
br.formatting.DateFormatter.prototype.getDateFormat = function(sDateFormat) {
	return sDateFormat || this.m_sFormatDefault;
};

/**
 * @private
 */
br.formatting.DateFormatter.prototype.toString = function() {
	return "br.formatting.DateFormatter";
};

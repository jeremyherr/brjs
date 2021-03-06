/**
 * @module br/formatting/NullValueFormatter
 */

/**
 * @class
 * @alias module:br/formatting/NullValueFormatter
 * @implements module:br/formatting/Formatter
 *
 * @classdesc
 * Substitutes text when the value is <code>null</code>, <code>undefined</code>, or the empty string.
 *
 * <p><code>NullValueFormatter</code> is typically used with Presenter, but can be invoked programmatically
 * as in the following example which evaluates to "N/A":</p>
 *
 * <pre>br.formatting.NullValueFormatter.format("", {nullValue:"N/A"})</pre>
 */
br.formatting.NullValueFormatter = function()
{
	this.m_sNullValueDefault = "\u00a0";
};

br.Core.implement(br.formatting.NullValueFormatter, br.formatting.Formatter);

/**
 * Substitutes replacement text when the string is void (null, undefined, or the empty string).
 *
 * <p>
 * Attribute Options:
 * </p>
 * <p>
 * <table>
 * <tr>
 * <th>Option</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>nullValue</td><td>  the replacement text to substitute in case of a null string (defaults to an empty string)</td></tr>
 * </table>
 *
 * @param {Variant} vValue  the string.
 * @param {Map} mAttributes  the map of attributes.
 * @return  the replacement string in the case of a void, otherwise the unchanged string.
 * @type  String
 */
br.formatting.NullValueFormatter.prototype.format = function(vValue, mAttributes) {
	return (vValue == undefined || vValue == null ||  vValue == "") ? mAttributes["nullValue"] == null ? this.m_sNullValueDefault : mAttributes["nullValue"] : vValue;
};

/**
 * @private
 */
br.formatting.NullValueFormatter.prototype.toString = function() {
	return "br.formatting.NullValueFormatter";
};

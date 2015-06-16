package org.orderofthebee.alfresco.value.assistance.webscript;

/**
 * Represents a single label/value pair.
 * 
 * 
 * @author tparzgnat, Technology Services Group
 * @version 1.0
 *
 *          <h2>Modification History</h2>
 *          <ul>
 *          <li>Jul 1, 2013 (tparzgnat) Created.</li>
 *          </ul>
 *          <p>
 *          Copyright &copy; 2013 Technology Services Group, Inc.
 *          </p>
 */
public class PicklistItem {

	protected String label;
	protected String value;

	public PicklistItem(String value, String label) {
		this.value = value;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
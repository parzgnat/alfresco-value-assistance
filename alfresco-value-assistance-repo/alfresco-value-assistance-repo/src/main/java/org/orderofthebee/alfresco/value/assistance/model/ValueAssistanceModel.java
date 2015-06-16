package org.orderofthebee.alfresco.value.assistance.model;

import org.alfresco.service.namespace.QName;

/**
 * References to all of the items defined in valueAssistanceModel.xml
 * 
 * 
 * @author tparzgnat, Technology Services Group
 * @version 1.0
 *
 *          <h2>Modification History</h2>
 *          <ul>
 *          <li>Jul 20, 2012 (gamin) Created.</li>
 *          </ul>
 *          <p>
 *          Copyright &copy; 2012 Technology Services Group, Inc.
 *          </p>
 */
public class ValueAssistanceModel {

	/** TSG Value Assistance Model URI */
	public static final String TSG_VALUE_ASSISTANCE_MODEL_URI = "http://www.tsgrp.com/model/va/1.0";
	public static final String DATALIST_MODEL_URI = "http://www.alfresco.org/model/dictionary/1.0";

	public static final String TSG_VALUE_ASSISTANCE_MODEL_PREFIX = "va";
	public static final String CONTENT_MODEL_PREFIX = "cm";
	public static final String DATALIST_MODEL_PREFIX = "dl";

	public static final QName TYPE_NON_CASCADING_VALUE_ASSISTANCE_LIST_ITEM = QName
			.createQName(TSG_VALUE_ASSISTANCE_MODEL_URI,
					"nonCascadingValueAssistanceListItem");
	public static final QName TYPE_2_LEVEL_CASCADING_VALUE_ASSISTANCE_LIST_ITEM = QName
			.createQName(TSG_VALUE_ASSISTANCE_MODEL_URI,
					"2LevelCascadingValueAssistanceListItem");
	
	public static final QName ASPECT_LEVEL_1 = QName
			.createQName(TSG_VALUE_ASSISTANCE_MODEL_URI,
					"level1Aspect");
	public static final QName ASPECT_LEVEL_2 = QName
			.createQName(TSG_VALUE_ASSISTANCE_MODEL_URI,
					"level2Aspect");
	
	public static final QName TYPE_DATALIST = QName.createQName(
			DATALIST_MODEL_URI, "dataList");

	public static final QName PROP_LEVEL_1_LABEL = QName.createQName(
			TSG_VALUE_ASSISTANCE_MODEL_URI, "level1Label");
	public static final QName PROP_LEVEL_2_LABEL = QName.createQName(
			TSG_VALUE_ASSISTANCE_MODEL_URI, "level2Label");

	public static final QName PROP_LEVEL_1_VALUE = QName.createQName(
			TSG_VALUE_ASSISTANCE_MODEL_URI, "level1Value");
	public static final QName PROP_LEVEL_2_VALUE = QName.createQName(
			TSG_VALUE_ASSISTANCE_MODEL_URI, "level2Value");
}
package com.tsgrp.va.model;

import org.alfresco.service.namespace.QName;


/**
 * References to all of the items defined in valueAssistanceModel.xml
 * 
 *  
 * @author tparzgnat, Technology Services Group
 * @version 1.0
 *
 * <h2>Modification History</h2>
 * <ul>
 *      <li>Jul 20, 2012 (gamin) Created.</li>
 * </ul>
 * <p>Copyright &copy; 2012 Technology Services Group, Inc.</p>
 */
public class ValueAssistanceModel
{
 
    /** TSG Value Assistance Model URI */
    public static final String TSG_VALUE_ASSISTANCE_MODEL_URI = "http://www.tsgrp.com/model/va/1.0";
    public static final String DATALIST_MODEL_URI = "http://www.alfresco.org/model/dictionary/1.0";

    public static final String TSG_VALUE_ASSISTANCE_MODEL_PREFIX = "va";
    public static final String CONTENT_MODEL_PREFIX = "cm";
    public static final String DATALIST_MODEL_PREFIX = "dl";
    
    public static final QName TYPE_VALUE_ASSISTANCE_LIST_ITEM = QName.createQName(TSG_VALUE_ASSISTANCE_MODEL_URI, "valueAssistanceListItem");
    public static final QName TYPE_DATALIST = QName.createQName(DATALIST_MODEL_URI, "dataList");
    
    public static final QName PROP_SORT_ORDER = QName.createQName(TSG_VALUE_ASSISTANCE_MODEL_URI, "sortOrder");
    public static final QName PROP_VALUE = QName.createQName(TSG_VALUE_ASSISTANCE_MODEL_URI, "value");
}

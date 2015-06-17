/*
 * Copyright (C) Technology Services Group, Inc.
 *
 * Licensed under the Mozilla Public License version 1.1 
 * with a permitted attribution clause. You may obtain a
 * copy of the License at
 *
 *   http://www.tsgrp.com/legal/license
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 */
package org.orderofthebee.alfresco.value.assistance.webscript;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.ParameterCheck;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.orderofthebee.alfresco.value.assistance.model.ValueAssistanceModel;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

/**
 * Returns picklist values based on the provided parameters. Also provides
 * labels given a value for dynamic picklist labels.
 * 
 * @author tparzgnat, Technology Services Group
 * @version 1.0
 *
 *          <h2>Modification History</h2>
 *          <ul>
 *          <li>July 1, 2013 (tparzgnat) Created.</li>
 *          </ul>
 *          <p>
 *          Copyright &copy; 2013 Technology Services Group, Inc.
 *          </p>
 */
public class PicklistWebScript extends DeclarativeWebScript {
	private static final Log logger = LogFactory
			.getLog(PicklistWebScript.class);

	// picklist to retrieve
	public static final String PARAM_PICKLIST_NAME = "name";
	// picklist level to retrieve info
	public static final String PARAM_PICKLIST_LEVEL = "level";
	// include an empty value at the start of picklist
	public static final String PARAM_INCLUDE_BLANK_ITEM = "includeBlankItem";
	// returns label for the provided initialValues
	public static final String PARAM_LOAD_LABELS = "loadLabels";
	// used with loadLabels to get the label for a given value
	public static final String PARAM_INITIAL_VALUES = "initialValues";

	protected SearchService searchService;
	protected NodeService nodeService;

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	/**
	 * @see org.springframework.extensions.webscripts.DeclarativeWebScript#executeImpl(org.springframework.extensions.webscripts.WebScriptRequest,
	 *      org.springframework.extensions.webscripts.Status,
	 *      org.springframework.extensions.webscripts.Cache)
	 */
	@Override
	protected Map<String, Object> executeImpl(final WebScriptRequest req,
			Status status, Cache cache) {

		final Map<String, Object> model = new HashMap<String, Object>();

		logger.debug("Begin");

		try {
			process(req, model);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		logger.debug("End");
		return model;
	}

	protected void process(WebScriptRequest req, Map<String, Object> model)
			throws Exception {
		String picklistName = req.getParameter(PARAM_PICKLIST_NAME);
		ParameterCheck.mandatoryString(PARAM_PICKLIST_NAME, picklistName);

		String picklistLevel = req.getParameter(PARAM_PICKLIST_LEVEL);
		int picklistLevelInt = 1;

		if (picklistLevel != null && picklistLevel.trim().length() > 0) {
			picklistLevelInt = Integer.parseInt(picklistLevel);
		}

		String VALUE_PARAMETER = null;

		switch (picklistLevelInt) {
		case 1:
			VALUE_PARAMETER = "";
			break;
		case 2:
			VALUE_PARAMETER = "level1";
			break;
		default:
			break;
		}

		String FILTER_VALUE = req.getParameter(VALUE_PARAMETER);

		// get the folder for the datalist
		StringBuffer query = new StringBuffer();
		query.append("=" + ValueAssistanceModel.CONTENT_MODEL_PREFIX + ":"
				+ ContentModel.PROP_TITLE.getLocalName() + ":\"" + picklistName
				+ "\"");
		query.append(" +TYPE:\"" + ValueAssistanceModel.DATALIST_MODEL_PREFIX
				+ ":" + ValueAssistanceModel.TYPE_DATALIST.getLocalName()
				+ "\"");

		logger.debug("Query: " + query);
		System.out.println("Query: " + query);

		// Set search parameters
		SearchParameters searchParameters = new SearchParameters();
		searchParameters.addStore(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);
		searchParameters.setLanguage(SearchService.LANGUAGE_FTS_ALFRESCO);
		searchParameters.setQuery(query.toString());

		ResultSet rs = searchService.query(searchParameters);
		if (rs == null || rs.length() < 1) {
			handleError("Unable to locate data list object with title "
					+ picklistName + ".", model);
		} else {
			// query for the datalist item nodes
			StringBuffer query2 = new StringBuffer();
			query2.append("PARENT:\"" + rs.getNodeRef(0).toString() + "\"");

			switch (picklistLevelInt) {
			case 1:
				query2.append(" +ASPECT:\""
						+ ValueAssistanceModel.TSG_VALUE_ASSISTANCE_MODEL_PREFIX
						+ ":"
						+ ValueAssistanceModel.ASPECT_LEVEL_1.getLocalName()
						+ "\"");
				break;
			case 2:
				query2.append(" +ASPECT:\""
						+ ValueAssistanceModel.TSG_VALUE_ASSISTANCE_MODEL_PREFIX
						+ ":"
						+ ValueAssistanceModel.ASPECT_LEVEL_2.getLocalName()
						+ "\"");
				break;
			default:
				break;
			}

			QName VALUE_PROPERTY = null;
			QName LABEL_PROPERTY = null;
			QName FILTER_PROPERTY = null;

			switch (picklistLevelInt) {
			case 1:
				VALUE_PROPERTY = ValueAssistanceModel.PROP_LEVEL_1_VALUE;
				LABEL_PROPERTY = ValueAssistanceModel.PROP_LEVEL_1_LABEL;
				break;
			case 2:
				VALUE_PROPERTY = ValueAssistanceModel.PROP_LEVEL_2_VALUE;
				LABEL_PROPERTY = ValueAssistanceModel.PROP_LEVEL_2_LABEL;
				FILTER_PROPERTY = ValueAssistanceModel.PROP_LEVEL_1_VALUE;
				break;
			default:
				break;
			}

			List<PicklistItem> picklistItems = new ArrayList<PicklistItem>();

			if (picklistLevelInt > 1
					&& (FILTER_VALUE == null || FILTER_VALUE.length() == 0)) {
				// returns a empty list because the filter value is empty
				picklistItems = new ArrayList<PicklistItem>();
				picklistItems.add(new PicklistItem("", ""));

			} else {

				if (FILTER_PROPERTY != null) {
					query2.append(" AND "
							+ ValueAssistanceModel.TSG_VALUE_ASSISTANCE_MODEL_PREFIX
							+ ":" + FILTER_PROPERTY.getLocalName() + ":\""
							+ FILTER_VALUE + "\"");
				}

				logger.debug("Query: " + query2);
				System.out.println("Query2: " + query2);
				


				// Set search parameters
				SearchParameters searchParameters2 = new SearchParameters();
				searchParameters2
						.addStore(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);

				searchParameters2.addSort("@" + VALUE_PROPERTY, true);
				searchParameters2
						.setLanguage(SearchService.LANGUAGE_FTS_ALFRESCO);
				searchParameters2.setQuery(query2.toString());

				ResultSet rs2 = searchService.query(searchParameters2);

				// see if we're just supposed to send back some labels
				String loadLabels = req.getParameter(PARAM_LOAD_LABELS);
				if (StringUtils.isNotBlank(loadLabels)) {
					List<String> labels = new ArrayList<String>();
					String initialValuesParam = req
							.getParameter(PARAM_INITIAL_VALUES);
					String[] initialValues = (initialValuesParam == null) ? new String[] { "" }
							: initialValuesParam.split(",");
					Map<String, String> valueLabelPairs = new HashMap<String, String>();

					for (NodeRef nodeRef : rs2.getNodeRefs()) {
						Map<QName, Serializable> props = nodeService
								.getProperties(nodeRef);
						valueLabelPairs.put((String) props.get(VALUE_PROPERTY),
								(String) props.get(LABEL_PROPERTY));
					}

					String label;
					for (String initialValue : initialValues) {
						label = valueLabelPairs.get(initialValue);
						if (label != null) {
							labels.add(label);
						} else {
							labels.add(initialValue);
						}
					}

					model.put("labels", StringUtils.join(labels, ","));
				} else {
					picklistItems = new ArrayList<PicklistItem>();
					List<String> returnedItems = new ArrayList<String>();

					boolean includeBlankItem = req
							.getParameter(PARAM_INCLUDE_BLANK_ITEM) == null ? false
							: Boolean.parseBoolean(req
									.getParameter(PARAM_INCLUDE_BLANK_ITEM));

					if (includeBlankItem) {
						picklistItems.add(new PicklistItem("", ""));
						returnedItems.add("");
					}

					for (NodeRef nodeRef : rs2.getNodeRefs()) {
						Map<QName, Serializable> props = nodeService
								.getProperties(nodeRef);

						String picklistValue = (String) props
								.get(VALUE_PROPERTY);

						// avoid adding repeated items
						if (!returnedItems.contains(picklistValue)) {
							picklistItems.add(new PicklistItem(picklistValue,
									(String) props.get(LABEL_PROPERTY)));
							returnedItems.add(picklistValue);
						}
					}
				}

				model.put("picklistItems", picklistItems);
			}
		}
	}

	protected void handleError(String error, Map<String, Object> model) {
		logger.error(error);
		model.put("error", error);
	}

	protected void handleError(String error, Map<String, Object> model,
			Throwable t) {
		logger.error(error, t);
		model.put("error", error);
	}

}

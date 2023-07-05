alfresco-value-assistance
=========================

Configurable value assistance module for Alfresco Share that allows picklists to be managed using datalists.

Solution presented at Alfresco Summit 2013
https://www.youtube.com/watch?v=NcYswC0S7To

It starts at 19:00 minutes

This project was first described here http://blog.tsgrp.com/2013/07/10/alfresco-data-list-driven-value-assistance/

It was when I got the source code and changed it to Alfresco SDK project format.

This project tries to reimplement (at least part of) the functionalities shown on this post http://blog.tsgrp.com/2014/12/17/harnessing-the-power-of-alfresco-data-lists-for-cascading-value-assistance/

At the moment, only 2 level dependency is working.

How to use the addon
====================

Create the properties you need.
Ex: property1 and property2

	<property name="custom:property1">
		<title>Property 1</title>
		<type>d:text</type>
		<mandatory>true</mandatory>
	</property>

	<property name="custom:property2">
		<title>Property 2</title>
		<type>d:text</type>
		<mandatory>true</mandatory>
	</property>

Into the share-config-custom.xml file, configure your custom type to something like this:

<config evaluator="node-type" condition="custom:customType">
	<forms>
		<form>
			<field-visibility>
				<show id="cm:name" />

				<!-- customProperties -->
				<show id="custom:property1" />
				<show id="custom:property2" />
			</field-visibility>
			<appearance>
				<field id="custom:property1">
					<control template="/form-controls/dynamic-dropdown.ftl">
						<control-param name="picklistName">Property 1 Datalist</control-param>
						<control-param name="level">1</control-param>
						<control-param name="loadLabel">true</control-param>
					</control>
				</field>
				<field id="custom:property2">
					<control template="/form-controls/dynamic-dropdown.ftl">
						<control-param name="picklistName">Property 2 Datalist</control-param>
						<control-param name="level">2</control-param>
						<control-param name="dependsOn">prop_custom_property1
						</control-param>
						<control-param name="loadLabel">true</control-param>
					</control>
				</field>
			</appearance>
		</form>

		<!-- Document Library pop-up Edit Metadata form -->
		<form id="doclib-simple-metadata">
			<field-visibility>
				<show id="cm:name" />

				<!-- customProperties -->
				<show id="custom:property1" />
				<show id="custom:property2" />
			</field-visibility>
			<appearance>
				<field id="custom:property1">
					<control template="/form-controls/dynamic-dropdown.ftl">
						<control-param name="picklistName">Property 1 Datalist</control-param>
						<control-param name="level">1</control-param>
						<control-param name="loadLabel">true</control-param>
					</control>
				</field>
				<field id="custom:property2">
					<control template="/form-controls/dynamic-dropdown.ftl">
						<control-param name="picklistName">Property 2 Datalist</control-param>
						<control-param name="level">2</control-param>
						<control-param name="dependsOn">prop_custom_property1
						</control-param>
						<control-param name="loadLabel">true</control-param>
					</control>
				</field>
			</appearance>
		</form>

		<!-- Document Library Inline Edit form -->
		<form id="doclib-inline-edit">
			<field-visibility>
				<show id="cm:name" />

				<!-- customProperties -->
				<show id="custom:property1" />
				<show id="custom:property2" />
			</field-visibility>
			<appearance>
				<field id="custom:property1">
					<control template="/form-controls/dynamic-dropdown.ftl">
						<control-param name="picklistName">Property 1 Datalist</control-param>
						<control-param name="level">1</control-param>
						<control-param name="loadLabel">true</control-param>
					</control>
				</field>
				<field id="custom:property2">
					<control template="/form-controls/dynamic-dropdown.ftl">
						<control-param name="picklistName">Property 2 Datalist</control-param>
						<control-param name="level">2</control-param>
						<control-param name="dependsOn">prop_custom_property1
						</control-param>
						<control-param name="loadLabel">true</control-param>
					</control>
				</field>
			</appearance>
		</form>
	</forms>
</config>

<config evaluator="model-type" condition="custom:customType">
	<forms>
		<!-- Search form -->
		<form id="search">
			<field-visibility>
				<show id="cm:name" />

				<!-- customProperties -->
				<show id="custom:property1" force="true" />
				<show id="custom:property1" force="true" />
			</field-visibility>
			<appearance>
				<field id="custom:property1">
					<control template="/form-controls/dynamic-dropdown.ftl">
						<control-param name="picklistName">Property 1 Datalist</control-param>
						<control-param name="level">1</control-param>
						<control-param name="loadLabel">true</control-param>
					</control>
				</field>
				<field id="custom:property2">
					<control template="/form-controls/dynamic-dropdown.ftl">
						<control-param name="picklistName">Property 2 Datalist</control-param>
						<control-param name="level">2</control-param>
						<control-param name="dependsOn">prop_custom_property1</control-param>
						<control-param name="loadLabel">true</control-param>
					</control>
				</field>
			</appearance>
		</form>

		<!-- New documents -->
		<form>
			<field-visibility>
				<show id="cm:name" />

				<!-- customProperties -->
				<show id="custom:property1" />
				<show id="custom:property2" />
			</field-visibility>
			<appearance>
				<field id="custom:property1">
					<control template="/form-controls/dynamic-dropdown.ftl">
						<control-param name="picklistName">Property 1 Datalist</control-param>
						<control-param name="level">1</control-param>
						<control-param name="loadLabel">true</control-param>
					</control>
				</field>
				<field id="custom:property2">
					<control template="/form-controls/dynamic-dropdown.ftl">
						<control-param name="picklistName">Property 2 Datalist</control-param>
						<control-param name="level">2</control-param>
						<control-param name="dependsOn">prop_custom_property1</control-param>
						<control-param name="loadLabel">true</control-param>
					</control>
				</field>
			</appearance>
		</form>
	</forms>
</config>

The value used in the picklistName parameter must be the same as the datalist's name.
If you create a datalist called "Property 1 Datalist", that's the text you should use as the picklistName parameter.

When creating the datalists, it's required to use of of the types defined by this addon:
1 Level Value Assistance List - for the first level of dependency
2 Level Cascading Value Assistance List - for the second level of dependency
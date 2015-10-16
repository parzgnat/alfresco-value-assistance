function objectsArrayComparator(a,b) {
	return a.label.localeCompare(b.label);
}

(function(TSG) {
	var Dom = YAHOO.util.Dom,
	Event = YAHOO.util.Event;
	var $html = Alfresco.util.encodeHTML;
	

	if (!Array.prototype.indexOf) {
		Array.prototype.indexOf = function(elt /* , from */) {
			var len = this.length >>> 0;

			var from = Number(arguments[1]) || 0;
			from = (from < 0) ? Math.ceil(from) : Math.floor(from);
			if (from < 0)
				from += len;

			for (; from < len; from++) {
				if (from in this && this[from] === elt)
					return from;
			}
			return -1;
		};
	}

	TSG.DynamicDropdown = function(htmlId)
	{
		var componentName = "TSG.DynamicDropdown";
		var returnObject = TSG.DynamicDropdown.superclass.constructor.call(this, componentName, htmlId, "TSG.DynamicDropdown"); 
		
		return returnObject;
	};
	
	YAHOO.extend(TSG.DynamicDropdown, Alfresco.component.Base,
	{
		options : {
			itemId : "",
			picklistName : "",
			initialValue : "",
			dependsOn: [],
			dependsOnValues: {},
			level : ""
		},
        updateValues : function(dependencyValue){
            console.log("get new values, dependency:")
            
            //since someone told us to update our values, clear our initial value so we don't default back to it
            this.options.initialValue = "";
            
            //clear our internal value since we've changed
            var valueElement;
    		if (this.id.indexOf("-entry") === -1)
    		{
    			valueElement = Dom.get(this.id);
    		}
    		else
    		{
    			valueElement = Dom.get(this.id.substring(0,this.id.indexOf("-entry")));
    		}
            valueElement.value = "";
            
            //need to update our internal stored values so that loadValues has all available values
            
            for (var dependencyId in dependencyValue)
            {
            	this.options.dependsOnValues[dependencyId] = dependencyValue[dependencyId];
            	//make sure we clear out further dependencies that depend on this changed value
            	//all indexes that come after the given dependencyId must be cleared, since their values potentially depended on this changing value
            	var dependencyIndex = this.options.dependsOn.indexOf(dependencyId) +1;
            	for (var i=dependencyIndex; i<this.options.dependsOn.length;i++)
            	{
            		this.options.dependsOnValues[this.options.dependsOn[i]] = "";
            	}
            }
            this.loadValues();
        },
		loadValues : function() {
    		var selectEl = Dom.get(this.id);

			// success handler, populate the dropdown
            var onSuccess = function (response)
            {
            	//clear selected item
            	selectEl.selectedIndex = -1;
            	
            	//clear the current dropdown options
            	while (selectEl.hasChildNodes())
            	{
            		selectEl.removeChild(selectEl.lastChild);
            	}
            	
            	//response is the already parsed JSON response
            	if (response.json.result && response.json.result.picklist)
            	{
            		var picklist = response.json.result.picklist;

					picklist.sort(objectsArrayComparator);

					for (var i=0; i<picklist.length;i++)
					{
						var optionElement = document.createElement("option");
						optionElement.innerHTML = picklist[i].label;
						optionElement.value = picklist[i].value;
						if (this.options.initialValue instanceof Array && this.options.initialValue.indexOf(picklist[i].value) !== -1)
						{
							optionElement.selected = "selected";
						}
						else if (this.options.initialValue === picklist[i].value)
						{
							optionElement.selected = "selected";
						}
						selectEl.appendChild(optionElement);
					}
            	}
            	
				YAHOO.Bubbling.fire("mandatoryControlValueUpdated", this);
            };
			
            // failure handler, display alert
            var onFailure = function (response)
            {
            	//clear selected item
            	selectEl.selectedIndex = -1;
            	
            	//clear the current dropdown options
            	while (selectEl.hasChildNodes())
            	{
            		selectEl.removeChild(selectEl.lastChild);
            	}
               // hide the whole field so incorrect content does not get re-submitted
               this._hideField();
               
               if (Alfresco.logger.isDebugEnabled())
                  Alfresco.logger.debug("Hidden field '" + this.id + "' as content retrieval failed");
            };

            var dependencyQuery = "";

            if (this.options.dependsOn.length > 0)
            {
                for (var dependencyId in this.options.dependsOnValues)
                {
                	var dependencyField = "level" + (this.options.level - 1);
                	
                	dependencyQuery+="&"+dependencyField+"="+this.options.dependsOnValues[dependencyId];
                }
            }
            
            Alfresco.util.Ajax.request(
            {
               url: Alfresco.constants.PROXY_URI + 'org/orderofthebee/picklist?includeBlankItem=true&name='+this.options.picklistName+'&itemId='+this.options.itemId+dependencyQuery+'&level='+this.options.level,
               method: "GET",
               responseContentType : "application/json",
               successCallback:
               {
                  fn: onSuccess,
                  scope: this
               },
               failureCallback:
               {
                  fn: onFailure,
                  scope: this
               }
            });

		},
		onReady : function() {
			if (this.options.dependsOn.length > 0)
			{
                YAHOO.Bubbling.on("tsg.changed", TSG.Dependency.changed, this );
				
				//field has dependencies so get them
				var dependencyValues = {};
				for (var i=0; i<this.options.dependsOn.length;i++)
				{
					var dependsOn = this.options.dependsOn[i];
					var domElementWithValue = YAHOO.util.Dom.get(this.id.substring(0,this.id.indexOf("prop_")) + dependsOn);
					dependencyValues[dependsOn] = domElementWithValue ? domElementWithValue.value : "";
				}
				this.options.dependsOnValues = dependencyValues;
				this.loadValues();
			}
			else
			{
				//query our service for the dropdown values, then build up the option elements, make sure to mark the selected item as well
				this.loadValues();
			}
		},
   });
	
})(window.TSG = window.TSG || {});
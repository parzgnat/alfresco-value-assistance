(function(TSG){
	
	TSG.Dependency = {};
	
//	TSG.Dependency.Mappings = {
//			"prop_dm_level1" : "level1"			
//	};
	
    TSG.Dependency.changed = function(event, eventArr, handlerObject)
    {

        var changedItem = eventArr[1];
        //changedItem is the object that was sent to the FIRE method
        //{
        //  itemId: property name that changed
        //  itemValue: new property value
        // }


        //handlerObject is the object that was fed to the ON method when we bound this event handler
        //handerObject.id is the property name that is checking if his dependsOn in the itemId
        //loop through this fields dependencies to see if we care about this change
        for (var i=0; i<handlerObject.options.dependsOn.length;i++)
        {
        	if (handlerObject.options.dependsOn[i] === changedItem.itemId)
        	{
        		console.log("matches my dependsOn, id" + handlerObject.id+", now querying for new values");
        		var dependencies = {};
        		dependencies[changedItem.itemId] = changedItem.itemValue;
        		handlerObject.updateValues(dependencies);
        	}        	
        }
    };
    
    TSG.Dependency.registerHandler = function(fieldId, htmlId)
    {   
    	//create a closure so we can get access to the htmlId field, since the multiselects use a hidden field
    	(function(){
        	YAHOO.util.Event.addListener(htmlId, "change", function(){
        		var itemValue;
        		
        		if (htmlId.indexOf("-entry") === -1)
        		{
        			itemValue=this.value;
        		}
        		else
        		{
        			var actualValueElement = YAHOO.util.Dom.get(htmlId.substring(0,htmlId.indexOf("-entry")));
        			if (actualValueElement)
        			{
        				itemValue = actualValueElement.value;
        			}
        		}
        		
        		//input value changed, fire a bubbling event with our fieldId, value
        		YAHOO.Bubbling.fire("tsg.changed", {itemId:fieldId, itemValue:itemValue});
        		
        	});
    		
    		
    	}());
    	//fieldId = prop_cm_name or prop_shc_format, etc

    };
    
    
    TSG.LoadLabel = function(htmlId)
	{
		var componentName = "TSG.LoadLabel";
		var returnObject = TSG.LoadLabel.superclass.constructor.call(this, componentName, htmlId, "TSG.LoadLabel"); 
		
		return returnObject;
	};
	
	YAHOO.extend(TSG.LoadLabel, Alfresco.component.Base,
	{
		options : {
			itemId : "",
			picklistName : "",
			initialValue : ""
		},
		loadLabels : function() {
			var spanEl = Dom.get(this.id);
			// success handler, populate the dropdown
            var onSuccess = function (response)
            {
            	//response is the already parsed JSON response
            	if (response.json.result && response.json.result.labels)
            	{
            		var labels = response.json.result.labels;
            		spanEl.innerHTML = labels;
            	}
            };
			
            // failure handler, display alert
            var onFailure = function (response)
            {
               // hide the whole field so incorrect content does not get re-submitted
               this._hideField();
               
               if (Alfresco.logger.isDebugEnabled())
                  Alfresco.logger.debug("Hidden field '" + this.id + "' as content retrieval failed");
            };
			
            Alfresco.util.Ajax.request(
            {
               url: Alfresco.constants.PROXY_URI + 'tsgrp/va/picklist/picklist?loadLabels=true&name='+this.options.picklistName+'&itemId='+this.options.itemId+'&initialValues='+this.options.initialValue,
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
			this.loadLabels();
		},
   });
	
}(window.TSG = window.TSG || {}));

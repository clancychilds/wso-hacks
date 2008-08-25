/*
  JSON object, with a single "rules" member - this is an array which defines the live WSO experiments.  Required fields are:
  
  	key: Typically a product id, determines whether the WSO control and tracking scripts will be written into the page under test.
  	     N.B. There can only be one experiment per page, so key should be unique in the rule set.
  	experiment: The experiment number as defined by the WSO front end during experiment setup.
  	type: Either "MV" or "AB" to indicate Multi-Variate and A/B tests.
  	goal: The name of the goal associated with this experiment.  This is used to determine which goal scripts to load into the goal
  	      page. 
*/
var wso_data = {"rules": [{"key":"Product1", "experiment":"1688662148", "type":"MV", "uacct":"UA-4918456-3", "goal":"order_confirm"},
	                      {"key":"Product2", "experiment":"0391758919", "type":"MV", "uacct":"UA-4918456-3", "goal":"order_confirm"},
	                      {"key":"Product3", "experiment":"0987654321", "type":"AB", "uacct":"UA-4918456-3", "goal":"email_confirm"}]
			   };

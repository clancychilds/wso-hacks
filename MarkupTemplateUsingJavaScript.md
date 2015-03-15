# Introduction #

This page describes a technique by which the WSO control and tracking script can be conditionally loaded into a page.  This is useful in situations where there are multiple pages built from a single template - but where there is a requirement that only certain pages participate in the experiment.  It also allows you to manage the development effort required to launch new tests as the template can be changed once and new experiments can be launched with only a configuration change.

This design uses JavaScript to insert the tracking script code at page load time, there is an [alternative technique](http://code.google.com/p/wso-hacks/wiki/MarkupTemplateUsingJSP) which uses JSP technology to insert the tracking code on the serverside.

# The Code #

The code supporting this design is [here](http://code.google.com/p/wso-hacks/source/browse/#svn/trunk/hacks/template-js).  The two main scripts are:

  * template-wso-rules.js
  * template-wso.js

### template-wso-rules.js ###

This file contains a JSON object which defines the experiments.  You should edit this file as appropriate.  Here is an example:

```
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
```

### template-wso.js ###

This file contains various JS functions which will insert the WSO code into the DOM appropriately given the rules defined in the template-wso-rules.js file.  The important functions are:

```
/*
  Write out the correct WSO control script into the DOM given a ruleset and key.
  This function should be called from the top of the page under test.
*/
function control_script(rules, key) {...
/*
  Write out the correct WSO tracker script into the DOM given a ruleset and key.
  This function should be called from the bottom of the page under test.
*/
function tracker_script(rules, key) {...

/*
  Write out the correct WSO goal scripts into the DOM given a ruleset and key.
  This function should be called from the bottom of the goal page.
*/
function goal_script(rules, goal_name) {...
```

# Example #

There are two example HTML pages showing the required markup:

  * template.html
  * template-goal.html

### template.html ###

This file is an example of how the scripts described above can be used to selectively markup a page.  The following is an extract showing the important markup required:

```
<html>
<head>
<title>Advanced Template Markup - Test Page</title>

<!-- Empty function definitions, ensures there are no JS errors should the WSO siteopt.js script not load. -->
<script type="text/javascript">
	function utmx_section(){}
	function utmx(){}
</script>

<!-- Load the rules and helper functions. -->
<script src='./template-wso-rules.js'></script>
<script src='./template-wso.js'></script>

<!-- Define some local variables. -->
<script>
	var rules = wso_data.rules;
	var product_id = get_param("product");
</script>
</head>
  <body>
    <!-- Debug information, for testing only. -->
      <script>
          document.write('</sc'+'ript><p>Debug Info : ' + debug(rules, "key", product_id) + '</p>');
    </script>
      
    <!-- The control script -->
    <script>
	  control_script(rules, product_id);
	</script>

    <div id="header">
      <!-- Section script, exactly as defined in the WSO documentation. -->
      <script>utmx_section("Header")</script>
      Original Header Section
      </noscript>
      </div>

    <div id="leftcol">
	<!-- Section script, exactly as defined in the WSO documentation. -->
	<script>utmx_section("Left")</script>
	Original Left Section
	</noscript>
	</div>

    <div id="content">
	<!-- Section script, exactly as defined in the WSO documentation. -->
	<script>utmx_section("Content")</script>
	Original Content Section
	<br>
	<a href="./template-goal.html">Convert me!</a>
	</noscript>
	</div>

    <div id="footer">
	<!-- Section script, exactly as defined in the WSO documentation. -->
	<script>utmx_section("Footer")</script>
	Original Footer Section
	</noscript>
    </div>

    <!-- The control script -->
    <script>
        tracker_script(rules, product_id);
    </script>
  </body>
</html>
```

### template-goal.html ###

This file is an example of how the scripts described above can be used to selectively markup the goal page.  The following is an extract showing the important markup required:

```
<html>
<title>Advanced Template Markup - Goal Page</title>
<head>
<!-- Load the rules and helper functions. -->
<script src='./template-wso-rules.js'></script>
<script src='./template-wso.js'></script>
<!-- Define some local variables. -->
<script>
	var rules = wso_data.rules;
	var goal = "order_confirm";
</script>
</head>
  <body>
  	<!-- Debug information, for testing only. -->
  	<script>
	  document.write('</sc'+'ript><p>Debug Info : ' + debug(rules, "goal", "order_confirm") + '</p>');
	</script>
	<!-- The goal script -->
	<script>
	  goal_script(wso_data.rules, "order_confirm");
	</script>
  </body>
</html>
```

Try these links to see the design at work:

  * [Product page, for a product for which there is an experiment defined.](http://v1.simonjsmith.com:8180/WSO-Hacks-Jsp/template.jsp?product=Product1)
  * [Product page, for a product where there is no corresponding experiment.](http://v1.simonjsmith.com:8180/WSO-Hacks-Jsp/template.jsp?product=Product15)
  * [Using testing codes to Force a MV variation.](http://v1.simonjsmith.com:8180/WSO-Hacks-Jsp/template.jsp?product=Product1#utmxid=EAAAANQyrCw7jaZQCCgjvxgqzKU;utmxpreview=2-0-0-0)
    * Testing technique described [here](http://www.google.com/support/websiteoptimizer/bin/answer.py?hl=en&answer=63841)
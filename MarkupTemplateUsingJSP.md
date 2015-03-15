# Introduction #

This page describes a technique by which the WSO control and tracking script can be conditionally loaded into a page. This is useful in situations where there are multiple pages built from a single template - but where there is a requirement that only certain pages participate in the experiment. It also allows you to manage the development effort required to launch new tests as the template can be changed once and new experiments can be launched with only a configuration change.

This design uses JSP code to insert the tracking script code om the serverside, there is an [alternative technique](http://code.google.com/p/wso-hacks/wiki/MarkupTemplateUsingJavaScript) which uses JavaScript technology to insert the tracking code at page load time.

# The Code #

The code supporting this design is [here](http://code.google.com/p/wso-hacks/source/browse/#svn/trunk/hacks/template-jsp).  The two main files are:

  * template-wso-rules.properties

### template-wso-rules.properties ###

This is a Java properties file which defines the experiments.  You should edit this file as appropriate.  Here is an example:

```
# WSO experiment definitions
# key: Typically a product id, determines whether the WSO control and tracking scripts will be written into the page under test.
# N.B. There can only be one experiment per page, so key should be unique in the rule set.
# experiment: The experiment number as defined by the WSO front end during experiment setup.
# type: Either "MV" or "AB" to indicate Multi-Variate and A/B tests.
# goal: The name of the goal associated with this experiment.  This is used to determine which goal scripts to load into the goal page.

key.0=Product1
experiment.0=1688662148
type.0=MV
uacc.0=UA-4918456-3
goal.0=order_confirm

key.1=Product2
experiment.1=0391758919
type.1=MV
uacc.1=UA-4918456-3
goal.1=order_confirm

key.2=Product3
experiment.2=0987654321
type.2=AB
uacc.2=UA-4918456-3
goal.2=email_confirm
```

### TemplateWso.java ###

This file contains various Java functions which will insert the WSO code into the document appropriately given the rules defined in the template-wso-rules.properties file.  The important methods are:

```
/**
 * Return the correct WSO control script given a key.  
 * This method should be called from the top of the page under test.
 *
 * @param key
 * @return The control script
 */
public String controlScript(String key) {...

/**
 * Return the correct WSO tracker script given a key.  
 * This function should be called from the bottom of the page under test.
 * 
 * @param key
 * @return The tracker script
 */
public String trackerScript(String key) {...

/**
 * Return the correct WSO goal script given the goal name.
 * This function should be called from the bottom of the goal page.
 * 
 * @param goalName
 * @return The goal script
 */
public String goalScript(String goalName) {...
```

# Example #

There are two example JSPs showing the required markup:

  * template.jsp
  * template-goal.jsp

### template.jsp ###

This file is an example of how the code described above can be used to selectively markup a page.  The following is an extract showing the important markup required:

```
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>Advanced Template Markup - Test Page</title>
<!-- Empty function definitions, ensures there are no JS errors should the WSO siteopt.js script not load. -->
<script type="text/javascript">
	function utmx_section(){}
	function utmx(){}
</script>
<!-- Define some local variables. -->
<%
  String key = request.getParameter("product");
  wso.hacks.TemplateWso wso = new wso.hacks.TemplateWso(application.getResource("/WEB-INF/template-wso-rules.properties"));
%>
</head>
  <body>
  	<!-- Debug information, for testing only. -->
	<p>Debug Info : <%=wso.debugKey(key)%></p>

	<!-- The control script -->
	<%=wso.controlScript(key)%>

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
	<a href="./template-goal.jsp">Convert me!</a>
	</noscript>
	</div>

    <div id="footer">
	<!-- Section script, exactly as defined in the WSO documentation. -->
	<script>utmx_section("Footer")</script>
	Original Footer Section
	</noscript>
	</div>

	<!-- The tracker script -->
	<%=wso.trackerScript(key)%>
  </body>
</html>
```

### template-goal.jsp ###

This file is an example of how the code described above can be used to selectively markup the goal page.  The following is an extract showing the important markup required:

```
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>Advanced Template Markup - Goal Page</title>
<!-- Empty function definitions, ensures there are no JS errors should the WSO siteopt.js script not load. -->
<script type="text/javascript">
	function utmx_section(){}
	function utmx(){}
</script>
<!-- Define some local variables. -->
<%
	String goal = "order_confirm";
	wso.hacks.TemplateWso wso = new wso.hacks.TemplateWso(application.getResource("/WEB-INF/template-wso-rules.properties"));
%>
</head>
<body>
	<!-- Debug information, for testing only. -->
	<p>Debug Info : <%=wso.debugGoal(goal)%></p>

	<!-- The goal script -->
	<%=wso.goalScript(goal)%>
</body>
</html>
```

Try these links to see the design at work:

  * [Product page, for a product for which there is an experiment defined.](http://v1.simonjsmith.com:8180/WSO-Hacks-Jsp/template.jsp?product=Product1)
  * [Product page, for a product where there is no corresponding experiment.](http://v1.simonjsmith.com:8180/WSO-Hacks-Jsp/template.jsp?product=Product15)
  * [Using testing codes to Force a MV variation.](http://v1.simonjsmith.com:8180/WSO-Hacks-Jsp/template.jsp?product=Product1#utmxid=EAAAANQyrCw7jaZQCCgjvxgqzKU;utmxpreview=2-0-0-0)
    * Testing technique described [here](http://www.google.com/support/websiteoptimizer/bin/answer.py?hl=en&answer=63841)
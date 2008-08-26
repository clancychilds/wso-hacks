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
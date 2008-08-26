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
<style type="text/css">
#header {
  background: #0f0;
  position: absolute;
  top: 50px;
  left: 0px;
  width: 800px;
  height: 100px;
}
#leftcol {
  background: #f00;
  position: absolute;
  top: 150px;
  left: 0px;
  width: 150px;
  height: 500px;
}
#content {
  background: #fff;
  position: absolute;
  top: 150px;
  left: 150px;
  width: 650px;
  height: 500px;
}
#footer {
  background: #0f0;
  position: absolute;
  top: 550px;
  left: 0px;
  width: 800px;
  height: 100px;
}
</style>
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


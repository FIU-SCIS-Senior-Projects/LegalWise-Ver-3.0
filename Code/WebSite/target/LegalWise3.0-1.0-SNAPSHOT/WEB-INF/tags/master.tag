<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Master Page template" pageEncoding="UTF-8" %>
<%@ attribute name="header" fragment="true" %>
<%@ attribute name="footer" fragment="true" %>
<%@ attribute name="sidebar" fragment="true" %>
<%@ attribute name="js" fragment="true" %>
<%@ attribute name="hasHeader" %>

<html>
<head>
	<title>LegalWise</title>	
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" 
		href="public/font-awesome-4.5.0/css/font-awesome.min.css"
		href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/
			css/font-awesome.min.css" 
		type="text/css">
	<link rel="stylesheet" href="public/css/bootstrap.min.css" type="text/css">		
	<link rel="stylesheet" href="public/css/style.css" type="text/css">
	<!-- 
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" type="text/css">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-validator/0.5.3/css/bootstrapValidator.css" type="text/css">
	-->
	<!-- Latest compiled and minified JavaScript -->
</head>
<body>
	<c:if test="${ hasHeader == true }">
		<div id="header">
			<jsp:invoke fragment="header" />
		</div>	
	</c:if>
	<div id="sidebar">
		<jsp:invoke fragment="sidebar" />
	</div>
	<div id="body">
		<jsp:doBody />
	</div>
	<div id="footer">
		<jsp:invoke fragment="footer" />
	</div>
</body>
<script type="text/javascript"
src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js">
</script>
<script type="text/javascript"
src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular.min.js">
</script>
<script type="text/javascript"
src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular-sanitize.js">
</script>
<script  type="text/javascript"
src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" 
integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<script type="text/javascript"
src="https://cdnjs.cloudflare.com/ajax/libs/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
<script type="text/javascript">
$(window).load(function() {$("body").addClass('loaded');});
/* URL FIX TO RUN LOCALLY,
 * Comment out before deploying 
 */
window.serverUrl = window.location.href.indexOf("/LegalWise2UI") != -1
	? "/LegalWise2UI" : "";
</script>
<jsp:invoke fragment="js" />
</html>
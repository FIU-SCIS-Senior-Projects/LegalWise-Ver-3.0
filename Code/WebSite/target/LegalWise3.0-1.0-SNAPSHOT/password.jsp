<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:master>
	<jsp:attribute name="hasHeader">${ false }</jsp:attribute>
	<jsp:body>
<div class="content small bck-white" ng-app="lwPassword" ng-controller="ctrl">
	<div class="login-bck hidden-sm hidden-xs"></div>
	<div class="row">
		<div class="col-md-6">
			<div class="margin-bottom-xlg text-center">
				<h3>
					<span class="fa-stack fa-lg">
					 <i class="fa fa-gavel fa-stack-1x"></i>
					 <i class="fa fa-circle-o 
				 	text-gray fa-stack-2x"></i>
					</span>
				</h3>
				<h4>
					Legal<span class="text-green">Wise</span>
					<span class="text-green">3.0</span>
				</h4>
			</div>
			<div ng-if="pass.message.show" class="text-center" 
				ng-class="{'text-red': pass.message.isError,
					'text-green': !pass.message.isError}">
				<h3>
					<i ng-if="!pass.message.isError" 
						class="fa fa-check-circle"></i>
					<i ng-if="pass.message.isError" 
						class="fa fa-warning"></i>
				</h3>
				<h4 ng-bind="pass.message.title"></h4>
				<p class="margin-bottom-lg" ng-bind="pass.message.msg"></p>
			</div>
			<div ng-if="pass.busy" class="text-center">
				<h1><i class="fa fa-spinner fa-spin"></i></h1>
				<p class="margin-bottom-lg">Creating request...</p>
			</div>
			<form id = "password-form" class="form-horizontal">
				<div style="min-height: 80px;">			
					<c:if test="${ error != null }">
						<div class="margin-bottom-sm">
							<div class="text-red control">
								<i class="fa fa-warning control-icon"></i>
								<div>${ error }</div>
							</div>
						</div>	
					</c:if>
					<div class="margin-bottom-sm">
						<div class="control form-group">									
							<i class="fa fa-envelope-o control-icon h4"></i>	
							<input ng-model="pass.body" class="form-control" type="text" name="un" placeholder="Username" type="email">
						</div>
					</div>
				</div>
				<div class="row" >
					<div class="col-xs-6 text-center">
						<button type="submit" class="control green"
							ng-click="pass.submit()" disabled>
							<i class="fa fa-check control-icon h4"></i>
							Send Link
						</button>
					</div>
					<div class="col-xs-6 text-center">
						<a  class="button control blue" href="login">
						<i class="fa fa-arrow-circle-left control-icon h4"></i>
							Go Back
						</a>
					</div>
				</div>
			</form>
		</div>
		<div class="col-md-6">
			<div class="hidden-sm hidden-xs" style="min-height: 200px;"
						class="text-center">
				<p class="margin-bottom-lg text-white">
					We provide speedy access to a lage database of Legal
					Documents.
				</p>
				<div class="row text-center">
					<div class="col-sm-4">
						<span class="fa-stack fa-2x text-white">
							<i class="fa fa-question fa-stack-1x"></i>
							<i class="fa fa-comment-o fa-stack-2x"></i>
						</span>
						<h5 class="text-white">Ask</h5>
					</div>
					<div class="col-sm-4">
						<span class="fa-stack fa-2x text-white">
							<i class="fa fa-search fa-stack-2x"></i>
						</span>
						<h5 class="text-white">Search</h5>
					</div>
					<div class="col-sm-4">
						<span class="fa-stack fa-2x text-white">
							<i class="fa fa-download fa-stack-2x"></i>
						</span>
						<h5 class="text-white">Download</h5>
					</div>
				</div>
			</div>
			<p class="margin-bottom-md visible-sm visible-xs">&nbsp;</p>
		</div>
	</div>
</div>	   
</jsp:body>
<jsp:attribute name="js">
	<script src="public/js/password.js"></script>
</jsp:attribute>
</t:master>

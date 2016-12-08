
$("#password-form").submit(function(e){
    e.preventDefault();
});
            
$('#password-form').bootstrapValidator({
	feedbackIcons: {
	    valid: 'glyphicon glyphicon-ok',
	    invalid: 'glyphicon glyphicon-remove',
	    validating: 'glyphicon glyphicon-refresh'
	},
	fields: {
	    un: {
	        validators: {
	            notEmpty: {
	                message: 'The email is required and cannot be empty'
	            },
	            emailAddress: {
	                message: 'The input is not a valid email address'
	            }
	        }
	    }
	}
});


var pass = {
	busy: false,
	body:null,
	message: {
		show: false,
		isError: false,
		title: null,
		msg: null
	},
	init: function() {
		//nothing to do
	},
	submit: function() {
		pass.hideMessage();
		var e = false;
		
		if (!e) {
			pass.busy = true;
			general.server.submit(pass, 
				function(r) {
					pass.busy = false;
					pass.showMessge(
							"Sucess",
			    			"The request were successfully submitted, " + 
			    			"please check your email",
			    			false);
				},
				function(e) {
					pass.busy = false;
					pass.showMessge(
							"There was a problem while submitting",
			    			e.msg || "No further information available",
			    			true);
				});
		}
	},
	showMessge: function(title, msg, error) {
		pass.message.show = true;
		pass.message.isError = error;
		pass.message.title = title;
		pass.message.msg = msg;
	},
	hideMessage: function() {
		pass.message.show = false;
		pass.message.isError = false;
		pass.message.title = null;
		pass.message.msg = null;
	}
};

var general = {
	ctrl: function($scope, $document) {
		$scope.pass = pass;
		$scope.serverUrl = window.serverUrl;
		general.scope = $scope;	
		
		// init
		pass.init();		
	},
	apply: function() {
 		var phase = general.scope.$root.$$phase;
		if (phase != '$apply' && phase != '$digest')
			general.scope.$apply();
 	},
 	server: {
 		submit: function(pass, onSuccess, onError) {
 			general._call("POST", 
 				"/service/password", JSON.stringify(pass), onSuccess, onError);
 		}
 	},
 	_call: function(method, serviceUrl, body, onSuccess, onError) {
 		var xhr = new XMLHttpRequest();
		xhr.open(method, (window.serverUrl || "") + serviceUrl, true);
 		setTimeout(function() {
			xhr.send(body);			
			xhr.onreadystatechange = function() {
				if (xhr.readyState == 4) {
					var v = {};
        			try {        			
        				v = JSON.parse(xhr.responseText);		        			
        		    } catch(e){}
					
					if (xhr.status >= 200 && xhr.status < 400) {
						if (onSuccess)
							onSuccess(v || {});
	        		} else {
	        			if (onError)
	        				onError(v || {});
	        		}
	        		general.apply();
	        	}
			};
		}, 1000);
 	}
};

//angular app initialization and specs
angular.module('lwPassword',[])
	.controller('ctrl', general.ctrl);
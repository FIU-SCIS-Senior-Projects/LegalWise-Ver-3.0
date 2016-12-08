
$(function() {
    $('#login-form-button').click(function(e) {
		$("#login-form").delay(100).fadeIn(100);
 		$("#register-form").fadeOut(100);
 		$('#login-form-button').fadeOut(100);
 		$('#register-form-button').fadeIn(100);
 		document.getElementById("second-panel").style.minHeight = "290px";
		$(this).addClass('active');
		e.preventDefault();
	});
	$('#register-form-button').click(function(e) {
		$("#register-form").delay(100).fadeIn(100);
 		$("#login-form").fadeOut(100);
 		$('#login-form-button').fadeIn(100);
 		$('#register-form-button').fadeOut(100);
 		document.getElementById("second-panel").style.minHeight = "290px";
		$(this).addClass('active');
		e.preventDefault();
	});
	
	 $('#login-form').bootstrapValidator({
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
	            },
	            pw: {
	                validators: {
	                    notEmpty: {
	                        message: 'The last name is required and cannot be empty'
	                    }
	                }
	            }
	        }
	    });

	 $('#register-form').bootstrapValidator({
	        feedbackIcons: {
	            valid: 'glyphicon glyphicon-ok',
	            invalid: 'glyphicon glyphicon-remove',
	            validating: 'glyphicon glyphicon-refresh'
	        },
	        fields: {
	            name: {
	                validators: {
	                    notEmpty: {
	                        message: 'The name is required and cannot be empty'
	                    },
	                    stringLength: {
	                        max: 20,
	                        min:3,
	                        message: 'The full name must be less than 20 but more than 3 characters'
	                    },
	                    regexp: {
	                        regexp: /^[a-z]+$/i,
	                        message: 'The full name can consist of alphabetical characters only'
	                    }
	                }
	            },
	            lname: {
	                validators: {
	                    notEmpty: {
	                        message: 'The lname is required and cannot be empty'
	                    },
	                    stringLength: {
	                        max: 20,
	                        min:3,
	                        message: 'The full name must be less than 20 but more than 3 characters'
	                    },
	                    regexp: {
	                        regexp: /^[a-z]+$/i,
	                        message: 'The full name can consist of alphabetical characters only'
	                    }
	                }
	            },
	            company: {
	                validators: {
	                    notEmpty: {
	                        message: 'The lname is required and cannot be empty'
	                    },
	                    stringLength: {
	                        max: 20,
	                        min:3,
	                        message: 'The full name must be less than 20 but more than 3 characters'
	                    },
	                    regexp: {
	                        regexp: /^[a-z]+$/i,
	                        message: 'The full name can consist of alphabetical characters only'
	                    }
	                }
	            },
	            email: {
	                validators: {
	                    notEmpty: {
	                        message: 'The email is required and cannot be empty'
	                    },
	                    emailAddress: {
	                        message: 'The input is not a valid email address'
	                    }
	                }
	            },
	            password: {
	                validators: {
	                    notEmpty: {
	                        message: 'The password is required and cannot be empty'
	                    },
	                    identical: {
	                        field: 'retype_pw',
	                        message: 'The password and its confirm are not the same'
	                    },
	                    stringLength: {
	                        max: 20,
	                        min:8,
	                        message: 'The full name must be less than 20 but more than 8 characters'
	                    }
	                }
	            },
	            retype_pw: {
	                validators: {
	                    notEmpty: {
	                        message: 'The password is required and cannot be empty'
	                    },
	                    identical: {
	                        field: 'password',
	                        message: 'The password and its confirm are not the same'
	                    }
	                }
	            },
	        }
	    });
});
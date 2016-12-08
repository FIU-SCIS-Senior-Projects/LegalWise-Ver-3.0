$('#password-form').bootstrapValidator({
	feedbackIcons: {
	    valid: 'glyphicon glyphicon-ok',
	    invalid: 'glyphicon glyphicon-remove',
	    validating: 'glyphicon glyphicon-refresh'
	},
	fields: {
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
        }
	}
});
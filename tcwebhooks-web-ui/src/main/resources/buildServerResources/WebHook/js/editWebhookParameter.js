WebHooksPlugin.Parameters = {
	localStore: {
		myJson: {}
	},
	handleAjaxError: function(dialog, response) {
		dialog.cleanErrors();
		if (response.status === 422 || response.status === 409) {
			if (response.responseJSON.errored) {
				$j.each(response.responseJSON.errors, function(index, errorMsg){
					dialog.ajaxError(errorMsg)
				});
			}
		} else if (response.status === 403) {
			alert("You are not permissioned to perform this operation. Message is: " + response.responseText);
		} else {
			console.log("----- begin webhooks AJAX error response -----")
			console.log(response);
			console.log("----- end webhooks AJAX error response -----")
			alert("An unexpected error occured. Please see your browser's javascript console.");
		}
	},
    editParameter: function(data) {
    	if (!restApiDetected) {
    		WebHooksPlugin.NoRestApiDialog.showDialog();
    	} else {
    		WebHooksPlugin.Parameters.EditDialog.showDialog("Edit WebHook Parameter", 'editWebhook', data);
    	}
    },
    addParameter: function(data) {
    	if (!restApiDetected) {
    		WebHooksPlugin.NoRestApiDialog.showDialog();
    	} else {
    		WebHooksPlugin.TemplateEditBuildEventDialog.showDialogAddEventTemplate("Add Build Event Template", 'addBuildEventTemplate', data);
    	}
    },
    createDefaultTemplate: function(data) {
    	if (!restApiDetected) {
    		WebHooksPlugin.NoRestApiDialog.showDialog();
    	} else {
    		WebHooksPlugin.TemplateEditBuildEventDialog.showDialogCreateDefaultTemplate("Add Default Template", 'addDefaultTemplate', data);
    	}
    },
    editTemplateDetails: function(data) {
    	if (!restApiDetected) {
    		WebHooksPlugin.NoRestApiDialog.showDialog();
    	} else {
    		WebHooksPlugin.EditTemplateDialog.showDialog("Edit Template", 'editTemplate', data);
    	}
    },
    copyTemplate: function(data) {
    	if (!restApiDetected) {
    		WebHooksPlugin.NoRestApiDialog.showDialog();
    	} else {
    		WebHooksPlugin.EditTemplateDialog.showDialog("Copy Template", 'copyTemplate', data);
    	}
    },
    exportTemplate: function(templateId) {
    	if (!restApiDetected) {
    		WebHooksPlugin.NoRestApiDialog.showDialog();
    	} else {
    		WebHooksPlugin.ExportTemplateDialog.showDialog("Export Template", 'exportTemplate', templateId);
    	}
    },
    disableTemplate: function(data) {
    	alert("This is not implemented yet.");
    },
    deleteBuildEventTemplate: function(data) {
    	if (!restApiDetected) {
    		WebHooksPlugin.NoRestApiDialog.showDialog();
    	} else {
    		WebHooksPlugin.DeleteTemplateItemDialog.showDialog("Delete Build Event Template", 'deleteBuildEventTemplate', data);
    	}
    },
    deleteTemplate: function(data) {
    	if (!restApiDetected) {
    		WebHooksPlugin.NoRestApiDialog.showDialog();
    	} else {
    		WebHooksPlugin.DeleteTemplateDialog.showDialog("Delete Template", 'deleteTemplate', data);
    	}
    },
    EditDialog: OO.extend(BS.AbstractWebForm, OO.extend(BS.AbstractModalDialog, {
        getContainer: function () {
            return $('editWebHookParameterDialog');
        },

        formElement: function () {
            return $('editWebHookParameterForm');
        },

        showDialog: function (title, action, data) {

            $j("input[id='WebHookParameteraction']").val(action);
            $j(".dialogTitle").text(title);
            this.resetAndShow(data);
            this.getWebHookParameterData(data.projectId, data.parameterId, action);

        },
        
        cancelDialog: function () {
        	this.close();
        },

        resetAndShow: function (data) {
			this.disableAndClearCheckboxes();
            this.cleanFields(data);
            this.showCentered();
        },

        cleanFields: function (data) {
            this.cleanErrors();
        },

        cleanErrors: function () {
            $j("#editWebHookParameterForm .error").remove();
        },

        error: function($element, message) {
            var next = $element.next();
            if (next != null && next.prop("class") != null && next.prop("class").indexOf('error') > 0) {
                next.text(message);
            } else {
                $element.after("<p class='error'>" + message + "</p>");
            }
        },

        ajaxError: function(message) {
        	var next = $j("#ajaxWebHookParameterEditResult").next();
        	if (next != null && next.prop("class") != null && next.prop("class").indexOf('error') > 0) {
        		next.text(message);
        	} else {
        		$j("#ajaxWebHookParameterEditResult").after("<p class='error'>" + message + "</p>");
        	}
        },

		getWebHookParameterData: function (templateId, buildTemplateId, action) {
			this.getParameterData(templateId, buildTemplateId, action);
		},
		putWebHookParameterData: function () {
			this.updateJsonDataFromForm();
			this.putTemplateData();
		},
		postWebHookParameterData: function () {
			this.updateJsonDataFromForm();
			this.postTemplateData();
		},
		disableAndClearCheckboxes: function () {
			$j("#editWebHookParameterForm input.buildState").prop("disabled", true).prop( "checked", false);
			$j("#editWebHookParameterForm label").addClass("checkboxLooksDisabled");
		},
		disableCheckboxes: function () {
			$j("#editWebHookParameterForm input.buildState").prop("disabled", true);
			$j("#editWebHookParameterForm label").addClass("checkboxLooksDisabled");
		},
		enableCheckboxes: function () {
			$j("#editWebHookParameterForm input.buildState").prop("disabled", false);
		},
		updateJsonDataFromForm: function () {
			WebHooksPlugin.Parameters.localStore.myJson.templateText.content = editor.getValue();
			WebHooksPlugin.Parameters.localStore.myJson.templateText.useTemplateTextForBranch = $j("#editWebHookParameterForm input#useTemplateTextForBranch").is(':checked');
			WebHooksPlugin.Parameters.localStore.myJson.branchTemplateText.content = editorBranch.getValue();

    		$j(WebHooksPlugin.Parameters.localStore.myJson.buildState).each(function() {
    			this.enabled = $j("#editTemplateItemForm input[id='" + this.type + "']").prop( "checked");
    		});
		},
		getParameterData: function (projectId, parameterId, action) {
			var dialog = this;
    		$j.ajax ({
    			url: window['base_uri'] + '/app/rest/webhooks/parameters/' + projectId + '/id:' + parameterId + '?fields=**',
    		    type: "GET",
    		    headers : {
    		        'Accept' : 'application/json'
    		    },
    		    success: function (response) {
    				WebHooksPlugin.Parameters.localStore.myJson = response;
    				dialog.handleGetSuccess(action);
    		    },
				error: function (response) {
					console.log(response);
					WebHooksPlugin.handleAjaxError(dialog, response);
				}
    		});
		},

		handleGetSuccess: function (action) {
			this.updateCheckboxes(action);
		},
		putParameterData: function () {
			var dialog = this;
			$j.ajax ({
				url: window['base_uri'] + WebHooksPlugin.Parameters.localStore.myJson.href,
				type: "PUT",
				data: JSON.stringify(WebHooksPlugin.Parameters.localStore.myJson),
				dataType: 'json',
				headers : {
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				},
				success: function (response) {
					dialog.close();
					$("buildEventTemplatesContainer").refresh();
				},
				error: function (response) {
					console.log(response);
					WebHooksPlugin.handleAjaxError(dialog, response);
				}
			});
		},
		postParameterData: function () {
			var dialog = this;
			var templateSubUri = "/templateItem";
			if ($j("input[id='WebhookTemplateaction']").val() === "addDefaultTemplate") {
				templateSubUri = "/defaultTemplate";
			}
			$j.ajax ({
				url: window['base_uri'] + WebHooksPlugin.Parameters.localStore.myJson.parentTemplate.href + templateSubUri,
				type: "POST",
				data: JSON.stringify(WebHooksPlugin.Parameters.localStore.myJson),
				dataType: 'json',
				headers : {
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				},
				success: function (response) {
					dialog.close();
					$("buildEventTemplatesContainer").refresh();
				},
				error: function (response) {
					console.log(response);
					WebHooksPlugin.handleAjaxError(dialog, response);
				}
			});
		},
		handlePutSuccess: function () {
			$j("#templateHeading").text(WebHooksPlugin.Parameters.localStore.myJson.parentTemplateDescription);
			this.updateCheckboxes();
			this.updateEditor();
		},
		updateCheckboxes: function (action) {

        	if (action === 'copyBuildEventTemplate' || action === 'addBuildEventTemplate') {
        		if (WebHooksPlugin.Parameters.localStore.myJson.id == 'defaultTemplate') {
            		$j(WebHooksPlugin.Parameters.localStore.myJson.buildState).each(function() {
            			$j("#editTemplateItemForm input[id='" + this.type + "']").prop( "checked", false).prop( "disabled", ! this.enabled);
            			if (this.enabled) {
            				$j("#editTemplateItemForm td[class='" + this.type + "'] label").removeClass("checkboxLooksDisabled");
            			}
            		});
        		} else {
            		$j(WebHooksPlugin.Parameters.localStore.myJson.buildState).each(function() {
            			$j("#editTemplateItemForm input[id='" + this.type + "']").prop( "checked", false).prop( "disabled", ! this.editable && ! this.enabled);
            			if (this.editable && ! this.enabled) {
            				$j("#editTemplateItemForm td[class='" + this.type + "'] label").removeClass("checkboxLooksDisabled");
            			}
            		});
        		}
        		WebHooksPlugin.Parameters.localStore.myJson.id = '_new';
        	} else {
	    		$j(WebHooksPlugin.Parameters.localStore.myJson.buildState).each(function() {
	    			if (action === 'addDefaultTemplate') {
	    				$j("#editTemplateItemForm input[id='" + this.type + "']").prop( "checked", this.editable).prop( "disabled", true);
	    			} else {
		    			$j("#editTemplateItemForm input[id='" + this.type + "']").prop( "checked", this.enabled).prop( "disabled", ! this.editable);
		    			if (this.editable) {
		    				$j("#editTemplateItemForm td[class='" + this.type + "'] label").removeClass("checkboxLooksDisabled");
		    			}
	    			}
	    		});
        	}

        	if (action === 'addDefaultTemplate' || action === 'addBuildEventTemplate') {
	    		$j("#editTemplateItemForm input[id='useTemplateTextForBranch']").prop( "checked", false).prop( "disabled", false);
				$j("label.useTemplateTextForBranch").removeClass("checkboxLooksDisabled");
				WebHooksPlugin.Parameters.localStore.myJson.id = '_new';
        	} else {
	    		$j("#editTemplateItemForm input[id='useTemplateTextForBranch']").prop( "checked", WebHooksPlugin.Parameters.localStore.myJson.templateText.useTemplateTextForBranch).prop( "disabled", false);
				$j("label.useTemplateTextForBranch").removeClass("checkboxLooksDisabled");
			}
		},

		doPost: function() {
			if (WebHooksPlugin.Parameters.localStore.myJson.id == '_new' || WebHooksPlugin.Parameters.localStore.myJson.id == '_copy') {
				this.postWebHookParameterData();
			} else {
				this.putWebHookParameterData();
			}
			return false;
		}

    })),

    NoRestApiDialog: OO.extend(BS.AbstractWebForm, OO.extend(BS.AbstractModalDialog, {
    	getContainer: function () {
    		return $('noRestApiDialog');
    	},

    	formElement: function () {
    		return $('noRestApiForm');
    	},

    	showDialog: function () {
    		this.showCentered();
    	}

    })),
    DeleteTemplateItemDialog: OO.extend(BS.AbstractWebForm, OO.extend(BS.AbstractModalDialog, {
    	getContainer: function () {
    		return $('deleteTemplateItemDialog');
    	},

    	formElement: function () {
    		return $('deleteTemplateItemForm');
    	},

    	showDialog: function (title, action, data) {
    		$j("input[id='WebHookParameteraction']").val(action);
    		$j(".dialogTitle").text(title);
    		this.cleanFields(data);
    		this.cleanErrors();
    		this.showCentered();
    	},

    	cleanFields: function (data) {
    		$j("#deleteWebHookParameterForm input[id='parameterId']").val(data.templateId);
    		this.cleanErrors();
    	},

    	cleanErrors: function () {
    		$j("#deleteWebHookParameterForm .error").remove();
    	},

    	error: function($element, message) {
    		var next = $element.next();
    		if (next != null && next.prop("class") != null && next.prop("class").indexOf('error') > 0) {
    			next.text(message);
    		} else {
    			$element.after("<p class='error'>" + message + "</p>");
    		}
    	},

    	ajaxError: function(message) {
    		var next = $j("#ajaxDeleteResult").next();
    		if (next != null && next.prop("class") != null && next.prop("class").indexOf('error') > 0) {
    			next.text(message);
    		} else {
    			$j("#ajaxDeleteResult").after("<p class='error'>" + message + "</p>");
    		}
    	},

    	doPost: function() {
    		this.cleanErrors();

			var dialog = this;
			var templateId = $j("#deleteTemplateItemForm input[id='templateId']").val()
			var templateNumber = $j("#deleteTemplateItemForm input[id='templateNumber']").val()

			$j.ajax ({
				url: window['base_uri'] + '/app/rest/webhooks/templates/id:' + templateId + '/templateItems/' + templateNumber,
				type: "DELETE",
				headers : {
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				},
				success: function (response) {
					dialog.close();
					$("buildEventTemplatesContainer").refresh();
				},
				error: function (response) {
					WebHooksPlugin.handleAjaxError(dialog, response);
				}
			});

    		return false;
    	}
    }))
};

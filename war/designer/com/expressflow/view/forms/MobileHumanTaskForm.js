Ext.namespace('com.expressflow.view.forms')
Ext.define('com.expressflow.view.forms.MobileHumanTaskForm', {
    extend: 'Ext.form.Panel',
    title: 'Mobile Human Task',
    width: '100%',
    height: '100%',
    statics:{
    	_forms: new Array(),
    	addForm: function(form){
    		this._forms.push(form);
    	},
		getForm: function(index){
			return this._forms[index];
		},
		getFormsLength: function(){
			return this._forms.length;
		},
		popForm: function(){
			if(this._forms.length > 0){
				return this._forms.pop();
			}
		}
    },
    _model: 'Empty',
    forms: new Array(),
    setModel: function(model) {
        this._model = model;
    },
    getModel: function(){
    	return this._model;
    },
    items: [{
        xtype: 'hidden',
        name: 'modelIndex'
    },
    {
        xtype: 'textfield',
        name: 'name',
        fieldLabel: 'Name',
        allowblank: false
    },
    {
    	xtype: 'fieldset',
    	title: 'Participants',
    	id: 'participantsFields',
    	items: [{
    		xtype: 'textfield',
    		name: 'participant',
    		fieldLabel: 'Participant',
    		allowblank: true
    	},{
    		xtype: 'button',
    		text: 'Invite',
    		handler: function(){
    			var f = this.up('form').getForm();
    			var participant = f.findField('participant').getValue();
    			console.log(participant +  " invited.");
    		}
    	}]
    },
    {
        xtype: 'fieldset',
        title: 'Form fields',
        id: 'formFields',
        items:[{
        			xtype: 'button',
        			text: 'Add Form Field',
        			handler: function(){
        				var f = this.up('form').getForm();
        				var form = Ext.getCmp('formFields');
        				var mobileForm = Ext.create('com.expressflow.view.forms.MobileFormContainer');
        				var modelIndex = f.findField('modelIndex').getValue();
        				var mobileF = mobileForm.getForm();
        				mobileF.findField('modelIndex').setValue(modelIndex);
        				form.add(mobileForm);
        				com.expressflow.view.forms.MobileHumanTaskForm.addForm(mobileForm);
        			}
        	},
        	{
        		xtype: 'button',
        		text: 'Remove Form',
        		handler: function(){
        			var form = Ext.getCmp('formFields');
        			var mForm = com.expressflow.view.forms.MobileHumanTaskForm.popForm();
        			if(mForm)
        				mForm.hide();
        		}
        	}]
    },
    {
    	xtype: 'button',
    	id: 'saveMhtButton',
    	text: 'Save',
    	handler: function() {
            var form = this.up('form').getForm();
            
            // Get the variablesStore
            var variablesStore = Ext.getStore('com.expressflow.store.VariablesStore');
            
            var mobileHumanTasksStore = Ext.getStore('com.expressflow.store.ElementsStore');
            var modelIndex = form.findField('modelIndex').getValue();
            var model = mobileHumanTasksStore.data.items[modelIndex];
            model.set('name', form.findField('name').getValue());
            
            var modelXml = Ext.String.format('<mobileHumanTask name="{0}">', model.data.name);
            var modelX = Ext.String.format('<mobileHumanTask><name>{0}</name>', model.data.name);
            
            model.variables().each(function(variable){
            	variablesStore.add(variable);
            	// Draw a variable on the Design Area
    			var designArea = com.expressflow.designer.Designer.getDesignArea();
    			var rect = designArea.rect(variable.data.x, 10, 100, 60, 10);
    			var text =  "Form: " + model.data.name + "\n\nVariable: " + variable.data.name + "\nType: " + variable.data.type + "\nValue: " + variable.data.value;
    			var text = designArea.text(variable.data.x + 40, 40, text);
    			modelXml += variable.getXml();
    			modelX += variable.getModel();
            });
            modelXml += "</mobileHumanTask>";
            modelX += "</mobileHumanTask>";
            
            model.data.xml = modelX;
            model.data.execXml = modelXml;
            
            // Get the MobileHumanTasksStore
            var mobileHumanTasksStore = Ext.getStore('com.expressflow.store.MobileHumanTasksStore');
            mobileHumanTasksStore.removeAll();
            mobileHumanTasksStore.add(model);
            
            var mht = Ext.create('com.expressflow.model.MobileHumanTask');
            mht.data.image = com.expressflow.model.MobileHumanTask.imageUrl();
            mht.data.x = model.data.x;
            mht.data.y = model.data.y;
            mht.data.xml = model.data.xml;
            mht.data.execXml = model.data.execXml;
            mht.data.name = model.data.name;
            mht.data.id = 123;
            mht.data.creator = model.data.creator;
            mht.variables = model.variables();
            
            mht.setProxy({
                type: 'rest',
                url: '/api/mobilehumantask'
            });
            
            mht.save({
            	success: function(m){
            		Ext.message.msg("Advice 2", "Created Variables for Form <b>" + model.data.name + "</b><br/><br/>"
            				+ "Mobile Human Task generated. <a href='/mobilehumantask/" + m.raw.mobileHumanTask.id + "' target='_blank'>Explore</a><br />"
            				+ "Deploy your process to publish your mobile human task!<br/><br />"
            				+ "<b>Click to close</b>");
            		var button = Ext.getCmp('saveMhtButton');
            		button.disable();
            		
            		var mobileHumanTaskStore = Ext.getStore('com.expressflow.store.MobileHumanTasksStore');
            		if(mobileHumanTaskStore.getAt(0)){
            			var mobile = mobileHumanTaskStore.getAt(0);
            			mobile.set("id", m.raw.mobileHumanTask.id);
            		}
            	}
            });
    	}
    },
    {
    	xtype: 'button',
    	text: 'Reset',
    	handler: function(){
    		 var form = this.up('form').getForm().reset();
    	}
    }]
});
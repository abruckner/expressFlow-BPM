Ext.namespace('com.expressflow.dd');
Ext.define('com.expressflow.dd.ClickHandler', {
    getForm: function(model) {
        var item;
        switch (model.modelName) {
        case 'com.expressflow.model.Annotate':
            item = Ext.create('com.expressflow.view.forms.AnnotateForm');
            break;
        case 'com.expressflow.model.GPlus':
        	item = Ext.create('com.expressflow.view.forms.GooglePlusForm');
        	break;
        case 'com.expressflow.model.GoogleDocs':
        	item = Ext.create('com.expressflow.view.forms.GoogleDocsForm');
        	break;
        case 'com.expressflow.model.MobileHumanTask':
            item = Ext.create('com.expressflow.view.forms.MobileHumanTaskForm');
            item.autoscroll = true;
            break;
        case 'com.expressflow.model.SendSMS':
            item = Ext.create('com.expressflow.view.forms.SendSMSForm');
            break;
        case 'com.expressflow.model.SendEmail':
        	var adviceStore = Ext.getStore('com.expressflow.store.AdvicesStore');
    		var advice = adviceStore.data.items[2];
    		var title = advice.data.title;
    		var message = advice.data.message;
    		
    		Ext.message.msg(title, message);
        	
            item = Ext.create('com.expressflow.view.forms.SendEmailForm');
            break;
        case 'com.expressflow.model.End':
            item = Ext.create('com.expressflow.view.forms.EndForm');
            break;
        case 'com.expressflow.model.Start':
            item = Ext.create('com.expressflow.view.forms.StartForm');
            break;
        case 'com.expressflow.model.Timer':
        	item = Ext.create('com.expressflow.view.forms.TimerForm');
        	break;
        case 'com.expressflow.model.Condition':
        	item = Ext.create('com.expressflow.view.forms.ConditionForm');
        	break;
        default:
            break;
        }
        return item;
    },
    handle: function(index) {
        var form = Ext.getCmp('properties');
        var elementsStore = Ext.getStore('com.expressflow.store.ElementsStore');
        var element = elementsStore.getAt(index);
        var formItem = this.getForm(element);
        if(formItem){
        	formItem.setModel(element);
        	formItem.form.findField('modelIndex').setValue(index);
        	if(form.items.length == 2){
        		form.remove(form.items.items[1].id);
        		form.show();
        	}
        	if(form.items.length < 2) {
        		formItem.loadRecord(element);
        		form.add(formItem);
        		form.show();
        	}
        }
    }
});
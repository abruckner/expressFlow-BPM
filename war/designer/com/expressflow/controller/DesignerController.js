Ext.namespace('com.expressflow.controller');
Ext.define('com.expressflow.controller.DesignerController',{
	extend: 'Ext.app.Controller',
	// Variables
	designArea: 'Empty',
	init: function(){
		this.control({
			'#saveProcessItem':{
				click: this.saveProcess
			},
			'#deployProcessItem':{
				click: this.deployProcess
			},
			'#designerArea1':{
				notifyDrop: this.saveProcess
			},
			'#manageProcessesGrid':{
				itemdblclick: this.loadProcess
			},
			'#newProcessItem':{
				click: this.newProcess
			}
		});	
	},
	deployProcess: function(){
		var processStore = Ext.getStore('com.expressflow.store.ProcessStore');
		if(processStore.getAt(0)){
        	var process = processStore.getAt(0);
        	
        	var mobileHumanTaskStore = Ext.getStore('com.expressflow.store.MobileHumanTasksStore');
        	if(mobileHumanTaskStore.getAt(0)){
        		var mobileHumanTask = mobileHumanTaskStore.getAt(0);
                
                var mht = Ext.create('com.expressflow.model.MobileHumanTask');
                mht.data.image = com.expressflow.model.MobileHumanTask.imageUrl();
                mht.data.x = mobileHumanTask.data.x;
                mht.data.y = mobileHumanTask.data.y;
                mht.data.xml = mobileHumanTask.data.xml;
                mht.data.execXml = mobileHumanTask.data.execXml;
                mht.data.name = mobileHumanTask.data.name;
                mht.data.id = mobileHumanTask.data.id;
                mht.data.creator = mobileHumanTask.data.creator;
                mht.data.method = process.data.id;
                
                mht.setProxy({
                    type: 'rest',
                    url: '/api/mobilehumantask'
                });
                
                mht.save({
                	success: function(m){
                		var adviceStore = Ext.getStore('com.expressflow.store.AdvicesStore');
                		var advice = adviceStore.data.items[1];
                		var title = advice.data.title;
                		var message = advice.data.message;
                		
                		Ext.message.msg(title, message);
                		
                		var button = Ext.getCmp('saveMhtButton');
                		if(button)
                			button.disable();
                	}
                });
        	}
        		
        		// Deploy the process model
                com.expressflow.model.Process.setProxy({
                    type: 'rest',
                    url: '/api/process/deploy'
                });
                
                com.expressflow.model.Process.load(process.data.id,{
                	success: function(p){
                		console.log("Process " + p.data.id + " deployed successfully.");
                	}
                });
        	
		}
	},
	newProcess:function(){
		var designerDropZone = Ext.create('com.expressflow.dd.xFDropTarget', 'designerArea1');
		var designerCmp = Ext.getCmp('designerArea1');
		designerCmp.setTitle('New Process');
		Ext.getCmp('processNameTxt').setValue('New Process');
		com.expressflow.designer.Designer.resetElements();
		var name = designerCmp.id;
		var elementsStore = Ext.getStore('com.expressflow.store.ElementsStore');
		elementsStore.removeAll();
		var processStore = Ext.getStore('com.expressflow.store.ProcessStore');
		processStore.removeAll();
		com.expressflow.designer.Designer.setDesignArea(designerDropZone.getDesignArea());
		
		// Init the process model
        com.expressflow.model.Process.setProxy({
            type: 'rest',
            url: '/api/process/init'
        });
        
        com.expressflow.model.Process.load(0,{
        	success: function(p){
        		console.log("Process created.");
        		var processStore = Ext.getStore('com.expressflow.store.ProcessStore');
        		p.data.id = p.raw.process.id;
        		p.data.accessDate = p.raw.process.accessDate;
        		p.data.creationDate = p.raw.process.creationDate;
        		p.data.creator = p.raw.process.creator;
        		p.data.name = "New Process";
        		Ext.getCmp('processNameTxt').setValue("New Process");
        		processStore.insert(0, p);	
        	}
        });
	},
	editDesignArea: function(){
		 var form = Ext.getCmp('properties');
		 var formItem = Ext.create('com.expressflow.view.forms.ProcessForm');
		 if(form.items.length == 2){
     		form.remove(form.items.items[1].id);
     		form.show();
     	}
     	if(form.items.length < 2) {
     		formItem.loadRecord(element);
     		form.add(formItem);
     		form.show();
     	}
	},
	loadProcess: function(dataview, record, item, index, e){
		var name = Ext.getCmp('designerArea1').id;
		var elementsStore = Ext.getStore('com.expressflow.store.ElementsStore');
		elementsStore.removeAll();
		// Reset Designer Helper
		com.expressflow.designer.Designer.resetElements();
		com.expressflow.designer.Designer.resetConnections();
		this.designArea = Raphael(name, document.getElementById(name).width, document.getElementById(name).height);
		var variables = this.designArea.text(40, 40, "Variables: ");
		com.expressflow.designer.Designer.setDesignArea(this.designArea);
		com.expressflow.parser.Parser.parseXML(record.data.xml, this.designArea);
		var processStore = Ext.getStore('com.expressflow.store.ProcessStore');
		processStore.removeAll();
		var process = Ext.create('com.expressflow.model.Process');
		if(record.data.name){
			Ext.getCmp('processNameTxt').setValue(record.data.name);
			Ext.getCmp('designerArea1').setTitle(record.data.name);
		}
		process.data.id = record.data.id;
		process.data.access = record.data.access;
		process.data.accessDate = record.data.accessDate;
		process.data.creationDate = record.data.creationDate;
		process.data.creator = record.data.creator;
		process.data.state = record.data.state;
		process.data.timesExecuted = record.data.timesExecuted;
		process.data.xml = record.data.xml;
		processStore.insert(0, process);
	},
	saveProcess: function(){
		console.log("Save Process clicked!");
		var elementsStore = Ext.getStore('com.expressflow.store.ElementsStore');
		var processStore = Ext.getStore('com.expressflow.store.ProcessStore');
		var process = 'Empty';
        if(processStore.getAt(0)){
        	process = processStore.getAt(0);
        }
        else{
        	process = Ext.create('com.expressflow.model.Process', {access: 'private', timesExecuted: 0, xml: _model});
        }
        // Xml Model of the UI model
        var _model = '<process>'
        var _execXml = Ext.String.format('<process name="{0}" access="{1}" timesExecuted="{2}">', process.get('name'), process.get('access'), process.get('timesExecuted'));
        for (var index = 0; index < elementsStore.data.length; index++){
            var el = elementsStore.data.items[index];
            _model += el.getModel();
            _execXml+= el.getXml();
        }
        _model += '</process>';
        _execXml += '</process>';
        var pNameTxT = Ext.getCmp('processNameTxt');
        process.data.name = pNameTxT.getValue();
        Ext.getCmp('designerArea1').setTitle(pNameTxT.getValue());
        process.data.xml = _model;
        process.data.execXml = _execXml;
        
        process.setProxy({
            type: 'rest',
            url: '/api/process'
        });
        process.save({
        	success: function(p){
        		var processStore = Ext.getStore('com.expressflow.store.ProcessStore');
        		p.id = p.raw.process.id;
        		p.data.id = p.raw.process.id;
        		p.data.access = p.raw.process.access;
        		p.data.accessDate = p.raw.process.accessDate;
        		p.data.creationDate = p.raw.process.creationDate;
        		p.data.creator = p.raw.process.creator;
        		p.data.state = p.raw.process.state;
        		p.data.name = p.raw.process.name;
        		processStore.insert(0, p);
        	}
        });
	}
})
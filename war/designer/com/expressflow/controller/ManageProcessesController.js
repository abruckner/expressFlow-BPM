Ext.namespace('com.expressflow.controller');
Ext.define('com.expressflow.controller.ManageProcessesController', {
	extend : 'Ext.app.Controller',
	init : function() {
		this.control({
			'#manageProcessesItem' : {
				click : this.manageProcesses
			}
		});
	},
	manageProcesses : function() {
		
		Ext.create('Ext.data.Store', {
			storeId : 'manageProcessStore',
			autoLoad: true,
			fields : [ 'id', 'creationDate', 'state', 'accessDate', 'creator', 'xml', 'name' ],
			proxy : {
				type : 'rest',
				url: '/api/process/',
				reader : {
					type : 'json',
					root : 'processList'
				}
			}
		});

		var window = Ext.create('Ext.Window', {
			title : 'Manage Your Processes',
			width : 600,
			height : 400,
			constrain : true,
			maximizable : true,
			layout : 'fit',
			items : [ Ext.create('Ext.grid.Panel', {
				id: 'manageProcessesGrid',
				title : 'My processes',
				store : Ext.data.StoreManager.lookup('manageProcessStore'),
				columns : [ {
					header : 'Creation date',
					dataIndex : 'creationDate'
				}, {
					header : 'Status',
					dataIndex : 'state',
					flex : 1
				}, {
					header : 'Access',
					dataIndex : 'accessDate'
				},
				{
					header: 'Creator',
					dataIndex: 'creator'
				}],
				height : 200,
				width : 400,
				renderTo : Ext.getBody()
			}) ]
		});
		window.show();
	}
});
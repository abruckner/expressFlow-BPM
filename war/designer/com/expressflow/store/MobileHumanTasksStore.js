Ext.namespace('com.expressflow.store');

Ext.define('com.expressflow.store.MobileHumanTasksStore',{
    extend: 'Ext.data.Store', 
    requires: 'com.expressflow.model.MobileHumanTask',
    model: 'com.expressflow.model.MobileHumanTask'
});
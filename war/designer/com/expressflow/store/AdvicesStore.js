Ext.namespace('com.expressflow.store');

Ext.define('com.expressflow.store.AdvicesStore',{
    extend: 'Ext.data.Store',
    requires: 'com.expressflow.model.Advice',
    model: 'com.expressflow.model.Advice',
    data: [
           {id: "1", title: "Advice 1", message: "Remember: Each process begins with a Start:<br /><img src='https://s3.amazonaws.com/expressflow/assets/start.png' /><br />"
				+"And ends with an End ;-)<img src='https://s3.amazonaws.com/expressflow/assets/end.png' /> <br /><b>Click to close</b>"},
           // Advice 2 is context sensitive on com.expressflow.view.forms.MobileHumanTasksForm
		   {id: "2", title: "Advice 3", message: "Successfully deployed the process!<br /><b>Click to close</b>"},
           {id: "3", title: "Advice 4", message: "You can access process variables by using <b>$</b>. Example: <br />Form Field To: <b>$Email</b><br /><b>Click to close</b>"}
    ]
});
Ext.namespace('com.expressflow.model');

Ext.define('com.expressflow.model.GPlus',{
    extend: 'com.expressflow.model.Element', 
    statics: {
    	imageUrl: function(){
    		return 'https://s3.amazonaws.com/expressflow/assets/gplus.PNG';
    	},
    	imageWidth: function(){
    		return 101; 
    	},
    	imageHeight: function(){
    		return 60;
    	},
    	fromXML: function(xml){
    		var element = Ext.create('com.expressflow.model.GPlus');
    		element = com.expressflow.parser.Parser.parseBase(element, xml);
    		element.data.image = this.imageUrl();
    		for(var i=0; i < xml.childNodes.length; i++){
    			switch(xml.childNodes[i].nodeName){
    			case "status":
					element.data.status = xml.childNodes[i].firstChild.nodeValue;
					break;
    			default:
    				break;
    			}
    		}
    		return element;
    	}
    },
    fields: [
             {name: 'id', type: 'int'},
             {name: 'status', type: 'String'}
             ],
    getXml: function(){
        var _xml = Ext.String.format('<gplus status="{0}" />', this.get('status'));
        return _xml;
    },
    getModel: function(){
    	var _image = this.get('image');
    	var x;
    	if(this.data.x == 0)
    		x = _image.attr('x');
    	else
    		x = this.data.x;
    	var y;
    	if(this.data.y == 0)
    		y = _image.attr('y');
    	else
    		y = this.data.y;
    	var _model = Ext.String.format('<gplus><status>{0}</status><x>{1}</x><y>{2}</y></gplus>', this.get('status'), x, y);
    	return _model;
    }
});
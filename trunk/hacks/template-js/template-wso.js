/*
  Write out the correct WSO control script into the DOM given a ruleset and key.
  This function should be called from the top of the page under test.
*/
function control_script(rules, key) {
	var rule = find_rule(rules, "key", key);
	if (rule != null) {
		var experiment = rule.experiment;
		var type = rule.type;	
		if (type == "MV") {
			(function(){var k=experiment,d=document,l=d.location,c=d.cookie;function f(n){
			if(c){var i=c.indexOf(n+'=');if(i>-1){var j=c.indexOf(';',i);return c.substring(i+n.
			length+1,j<0?c.length:j)}}}var x=f('__utmx'),xx=f('__utmxx'),h=l.hash;
			d.write('<sc'+'ript src="'+
			'http'+(l.protocol=='https:'?'s://ssl':'://www')+'.google-analytics.com'
			+'/siteopt.js?v=1&utmxkey='+k+'&utmx='+(x?x:'')+'&utmxx='+(xx?xx:'')+'&utmxtime='
			+new Date().valueOf()+(h?'&utmxhash='+escape(h.substr(1)):'')+
			'" type="text/javascript" charset="utf-8"></sc'+'ript>')})();
		}
		else if (type == "AB") {
			(function(){var k=experiment,d=document,l=d.location,c=d.cookie;function f(n){
			if(c){var i=c.indexOf(n+'=');if(i>-1){var j=c.indexOf(';',i);return c.substring(i+n.
			length+1,j<0?c.length:j)}}}var x=f('__utmx'),xx=f('__utmxx'),h=l.hash;
			d.write('<sc'+'ript src="'+
			'http'+(l.protocol=='https:'?'s://ssl':'://www')+'.google-analytics.com'
			+'/siteopt.js?v=1&utmxkey='+k+'&utmx='+(x?x:'')+'&utmxx='+(xx?xx:'')+'&utmxtime='
			+new Date().valueOf()+(h?'&utmxhash='+escape(h.substr(1)):'')+
			'" type="text/javascript" charset="utf-8"></sc'+'ript>')})();
			utmx("url",'A/B');
		}
	}
}

/*
  Write out the correct WSO tracker script into the DOM given a ruleset and key.
  This function should be called from the bottom of the page under test.
*/
function tracker_script(rules, key) {
	var rule = find_rule(rules, "key", key);
	if (rule != null) {
		var experiment = rule.experiment;
		var uacct = rule.uacct;
		if(typeof(_gat)!='object')document.write('<sc'+'ript src="http'+
		(document.location.protocol=='https:'?'s://ssl':'://www')+
		'.google-analytics.com/ga.js"></sc'+'ript>');

		document.write('<sc'+'ript> ' + 'try { var pageTracker=_gat._getTracker(\"' + uacct + '\"); pageTracker._trackPageview(\"/' + experiment + '/test\"); }catch(err){} ' + '</sc'+'ript>');
	}
}

/*
  Write out the correct WSO goal scripts into the DOM given a ruleset and key.
  This function should be called from the bottom of the goal page.
*/
function goal_script(rules, goal_name) {
	var goals = find_rules(rules, "goal", goal_name);	
	if (goals.length > 0) {
		if(typeof(urchinTracker)!='function')document.write('<sc'+'ript src="'+
		'http'+(document.location.protocol=='https:'?'s://ssl':'://www')+
		'.google-analytics.com/urchin.js'+'"></sc'+'ript>');		
	}
	for (var i = 0; i < goals.length; i++) {
		document.write('<sc'+'ript>' + '_uacct = "' + goals[i].uacct + '";urchinTracker("/' + goals[i].experiment +'/goal");' + '</sc'+'ript>');		
	}
}

/*
  Helper function to get the value of a parameter from a URL.
*/
function get_param(name) {
	name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	var regexS = "[\\?&]"+name+"=([^&#]*)";
	var regex = new RegExp( regexS );
	var results = regex.exec( window.location.href );
	if( results == null )
		return "";
	else
	  	return results[1];
}

/*
  Print out the details of a matching rule.
*/
function debug(rules, key_name, key_value) {
	var matches = find_rules(rules, key_name, key_value);
	if (matches.length == 0) {
		return "Null";
	} else {
		return dump(matches);
	}
}

/*
  Find matching rules given a ruleset, key name and key value.
*/
function find_rules(rules, key_name, key_value) {
	var ret = new Array();
	for(var i = 0; i < rules.length; i++) {
		var rule = rules[i];
		if (rule[key_name] == key_value) {
			ret.push(rule);
		}
	}
	return ret;	
}

/*
  Find a single matching rule given a ruleset, key name and key value.
*/
function find_rule(rules, key_name, key_value) {
	var matches = find_rules(rules, key_name, key_value);
	if (matches.length == 0) {
		return null;
	} else {
		return matches[0];
	}
}

/*
  Dump an array to a string.
*/
function dump(arr) {
	var dumped_text = "";
	var level = 0;
	
	var level_padding = "";
	for(var j=0;j<level+1;j++) level_padding += "    ";
	
	if(typeof(arr) == 'object') { //Array/Hashes/Objects 
		for(var item in arr) {
			var value = arr[item];
			
			if(typeof(value) == 'object') { //If it is an array,
				dumped_text += level_padding + "'" + item + "' ...\n";
				dumped_text += dump(value,level+1);
			} else {
				dumped_text += level_padding + "'" + item + "' => \"" + value + "\"\n";
			}
		}
	} else { //Stings/Chars/Numbers etc.
		dumped_text = "===>"+arr+"<===("+typeof(arr)+")";
	}
	return dumped_text;
}

package wso.hacks;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

public class TemplateWso {

	Hashtable rulesByKey;
	Hashtable rulesByGoal;

	public TemplateWso(URL url) throws Exception {
		InputStream in = url.openStream();
		Properties p = new Properties();
		p.load( in );

		int i = 0;
		rulesByKey = new Hashtable();
		rulesByGoal = new Hashtable();

		while(p.containsKey("key."+i)) {
			Rule rule = new Rule(p.getProperty("key."+i), p.getProperty("experiment."+i), p.getProperty("type."+i), p.getProperty("uacct."+i), p.getProperty("goal."+i));
			rulesByKey.put(p.getProperty("key."+i), rule);

			if (rulesByGoal.containsKey(p.getProperty("goal."+i))) {
				((Collection)rulesByGoal.get(p.getProperty("goal."+i))).add(rule);
			} else {
				Collection c = new ArrayList();
				c.add(rule);
				rulesByGoal.put(p.getProperty("goal."+i), c);
			}
			i++;
		}
	}

	/**
	 * Return the correct WSO control script given a key.
	 * This method should be called from the top of the page under test.
     *
	 * @param key
	 * @return The control script
	 */
	public String controlScript(String key) {
		StringBuffer ret = new StringBuffer();

		Rule rule = findRuleByKey(key);
		if (rule != null) {
			String experiment = rule.experiment;
			String type = rule.type;
			if (type.equals("MV")) {
				ret.append("<script>");
				ret.append("function utmx_section(){}function utmx(){}");
				ret.append("(function(){var k='" + experiment + "',d=document,l=d.location,c=d.cookie;function f(n){");
				ret.append("if(c){var i=c.indexOf(n+'=');if(i>-1){var j=c.indexOf(';',i);return c.substring(i+n.");
				ret.append("length+1,j<0?c.length:j)}}}var x=f('__utmx'),xx=f('__utmxx'),h=l.hash;");
				ret.append("d.write('<sc'+'ript src=\"'+");
				ret.append("'http'+(l.protocol=='https:'?'s://ssl':'://www')+'.google-analytics.com'");
				ret.append("+'/siteopt.js?v=1&utmxkey='+k+'&utmx='+(x?x:'')+'&utmxx='+(xx?xx:'')+'&utmxtime='");
				ret.append("+new Date().valueOf()+(h?'&utmxhash='+escape(h.substr(1)):'')+");
				ret.append("'\" type=\"text/javascript\" charset=\"utf-8\"></sc'+'ript>')})();");
				ret.append("</script>");
			}
			else if (type.equals("AB")) {
				ret.append("<script>");
				ret.append("function utmx_section(){}function utmx(){}");
				ret.append("(function(){var k='" + experiment + "',d=document,l=d.location,c=d.cookie;function f(n){");
				ret.append("if(c){var i=c.indexOf(n+'=');if(i>-1){var j=c.indexOf(';',i);return c.substring(i+n.");
				ret.append("length+1,j<0?c.length:j)}}}var x=f('__utmx'),xx=f('__utmxx'),h=l.hash;");
				ret.append("d.write('<sc'+'ript src=\"'+");
				ret.append("'http'+(l.protocol=='https:'?'s://ssl':'://www')+'.google-analytics.com'");
				ret.append("+'/siteopt.js?v=1&utmxkey='+k+'&utmx='+(x?x:'')+'&utmxx='+(xx?xx:'')+'&utmxtime='");
				ret.append("+new Date().valueOf()+(h?'&utmxhash='+escape(h.substr(1)):'')+");
				ret.append("'\" type=\"text/javascript\" charset=\"utf-8\"></sc'+'ript>')})();");
				ret.append("</script><script>utmx(\"url\",'A/B');</script>");
			}
		}
		return ret.toString();
	}

	/**
	 * Return the correct WSO tracker script given a key.
	 * This function should be called from the bottom of the page under test.
	 *
	 * @param key
	 * @return The tracker script
	 */
	public String trackerScript(String key) {
		StringBuffer ret = new StringBuffer();

		Rule rule = findRuleByKey(key);
		if (rule != null) {
			String uacct = rule.uacct;
			ret.append("<script>if(typeof(urchinTracker)!='function')document.write('<sc'+'ript src=\"'+'http'+(document.location.protocol=='https:'?'s://ssl':'://www')+'.google-analytics.com/urchin.js'+'\"></sc'+'ript>');</script>");
			ret.append("<script>");
			ret.append("_uacct = '" + uacct + "';");
			ret.append("urchinTracker('/" + rule.experiment + "/test');");
			ret.append("</script>");
		}
		return ret.toString();
	}

	/**
	 * Return the correct WSO goal script given the goal name.
	 * This function should be called from the bottom of the goal page.
	 *
	 * @param goalName
	 * @return The goal script
	 */
	public String goalScript(String goalName) {
		StringBuffer ret = new StringBuffer();

		Collection goals = findRulesByGoal(goalName);
		if (goals.size() > 0) {
			ret.append("<script>if(typeof(urchinTracker)!='function')document.write('<sc'+'ript src=\"'+'http'+(document.location.protocol=='https:'?'s://ssl':'://www')+'.google-analytics.com/urchin.js'+'\"></sc'+'ript>');</script>");
			ret.append("<script>");
			Iterator it = goals.iterator();
			while (it.hasNext()) {
				Rule rule = (Rule) it.next();
				ret.append("_uacct = '" + rule.uacct + "';");
				ret.append("urchinTracker('/" + rule.experiment + "/goal');");
			}
			ret.append("</script>");
		}
		return ret.toString();
	}

	private Rule findRuleByKey(String key) {
		if (key != null) {
			return (Rule) rulesByKey.get(key);
		}
		return null;
	}

	private Collection findRulesByGoal(String goal) {
		if (goal != null) {
			return (Collection) rulesByGoal.get(goal);
		}
		return null;
	}

	public String debugKey(String key) {
		return ""+findRuleByKey(key);
	}

	public String debugGoal(String key) {
		return ""+findRuleByKey(key);
	}

	protected class Rule {
		String key;
		String experiment;
		String type;
		String uacct;
		String goal;

		public Rule(String key, String experiment, String type, String uacct, String goal) {
			this.key = key;
			this.experiment = experiment;
			this.type = type;
			this.uacct = uacct;
			this.goal = goal;
		}

		public String toString() {
			StringBuffer ret = new StringBuffer();
			ret.append("'key' => \"" + key + "\"\n");
			ret.append("'experiment' => \"" + experiment + "\"\n");
			ret.append("'type' => \"" + type + "\"\n");
			ret.append("'uacct' => \"" + uacct + "\"\n");
			ret.append("'goal' => \"" + goal + "\"\n");
			return ret.toString();
		}
	}
}

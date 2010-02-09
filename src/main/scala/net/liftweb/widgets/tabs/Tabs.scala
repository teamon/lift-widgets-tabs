package net.liftweb.widgets.tabs

import net.liftweb.util.Helpers
import xml.{Unparsed, NodeSeq}
import net.liftweb.http.LiftRules
import net.liftweb.http.js.JsObj
import net.liftweb.http.js.JE.JsObj

/*
 * Widget that provides simple tabs (sing jQuery)
 *
 * Usage:
 *
 * Tabs("one" -> <span>One one one</span>,
 *      "two" -> <span>Foo bar baz</span>)
 */

object Tabs {
	def init {
		import net.liftweb.http.ResourceServer

		ResourceServer.allow({
			case "tabs" :: _ => true
		})
	}

	def apply(params: (String, NodeSeq)*): NodeSeq = new Tabs(JsObj(), params) render

	def apply(jsObj: JsObj, params: (String, NodeSeq)*): NodeSeq = new Tabs(jsObj, params) render
}

class Tabs(jsObj: JsObj, items: Seq[(String, NodeSeq)]) {
	val id = Helpers.nextFuncName

	def render: NodeSeq = head ++ content(items.zipWithIndex)

	def content(items: Seq[((String, NodeSeq), Int)]): NodeSeq =
		<div class="ui-tabs">
			<ul>
				{items.flatMap {case ((s, _), i) => <li><a href={"#" + id + i}>{s}</a></li>}}
			</ul>
			{items.flatMap {case ((_, c), i) => <div id={id + i}>{c}</div>}}
		</div>


	def head: NodeSeq =
		<head>
			<link rel="stylesheet" href={"/" + LiftRules.resourceServerPath + "/tabs/ui.tabs.css"} type="text/css"/>
			<script type="text/javascript" src={"/" + LiftRules.resourceServerPath + "/tabs/ui.core.js"}></script>
			<script type="text/javascript" src={"/" + LiftRules.resourceServerPath + "/tabs/ui.tabs.js"}></script>
			<script type="text/javascript">
				{Unparsed("""
         jQuery(document).ready(function() {
           jQuery('div.ui-tabs').tabs(""" + jsObj.toJsCmd + """);
         })
       """)}
			</script>
		</head>

}



package com.bbyiya.web.filter;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.content.tagrules.TagRuleBundle;
import org.sitemesh.content.tagrules.html.ExportTagToContentRule;
import org.sitemesh.tagprocessor.State;

/**
 * 
 * @ClassName: MyTagRuleBundle 
 * @Description: Sitemesh3默认只提供了 body，title，head等 tag类型，我们可以通过实现 TagRuleBundle扩展自定义的 tag规则
 * @author xiehz 
 * @date 2015年5月13日 下午5:54:24 
 *
 */
public class MyTagRuleBundle implements TagRuleBundle {

	@Override
	public void install(State defaultState, ContentProperty contentProperty, SiteMeshContext siteMeshContext) {
		defaultState.addRule("myHeader", new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("myHeader"), false));
	}

	@Override
	public void cleanUp(State defaultState, ContentProperty contentProperty, SiteMeshContext siteMeshContext) {
		// TODO Auto-generated method stub

	}

}

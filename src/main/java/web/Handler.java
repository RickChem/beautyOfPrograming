package web;

import calculate.CalImpl;
import calculate.CalService;
import calculate.Test;
import dataservice.DataImpl;
import dataservice.DataService;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import utility.FileHelper;

/**
* beautyOfPrograming/web/Handler.java
* @author cxworks
* 2016年5月7日 下午6:37:26
*/
public class Handler implements io.vertx.core.Handler<RoutingContext> {
	public static int ji = 0;
	@Override
	public void handle(RoutingContext event) {
		MultiMap map=event.request().params();
		
		String id1 = map.get("id1");
		String id2 = map.get("id2");
		System.out.println("input:"+id1+" "+id2);
		System.out.println("which number? let's see see: "+ji);
		ji ++;
		if (id1!=null && id2!=null){
			//do
//			CalService calService = new CalImpl();
			DataService service = new DataImpl();
			Test t = new Test(service);
//			String ans = FileHelper.getSingleAns(ji);
			event.response().putHeader("Content-Type","application/json").end(t.calculate(id1, id2));
		}else
			event.fail(403);
		//
	}

}

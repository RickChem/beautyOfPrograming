package build;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import dataservice.DataImpl;
import dataservice.DataService;
import po.AuidNode;
import po.Entity;
import po.IdNode;
import utility.FileHelper;
import utility.SearchType;

/**
* beautyOfPrograming/build/BuildMap.java
* @author cxworks
* 2016年5月7日 下午7:03:16
*/
public class BuildMap {
	
	
	public IdNode getIdTree(long id,Entity entity,DataService service) {
		IdNode ans=new IdNode(id,entity);
		//id to id
		Map<Long , Entity> id2idrid=getChild(entity.getRids(), SearchType.RID, service);
		
		Map<Long, Entity> id2idcid=new HashMap<>();
		if (entity.getCid()!=null) {
			id2idcid=getChild(entity.getCid(), SearchType.CID, service);
		}
		Map<Long, Entity> id2idfid=getChild(entity.getFids(), SearchType.FID, service);
		Map<Long, Entity> id2idjid=new HashMap<>();
		if (entity.getJid()!=null) {
			id2idjid=getChild(entity.getJid(), SearchType.JID, service);
		}
		
		//id to auid
//		Map<Long, List<Entity>> id2auidauid=getAuid(entity.getAuids(), SearchType.AUID, service);
//		Map<Long, Entity> map=new HashMap<>(id2idcid.size()+id2idfid.size()+id2idjid.size()+id2idrid.size());
//		map.putAll(id2idjid);
//		map.putAll(id2idfid);
//		map.putAll(id2idcid);
//		map.putAll(id2idrid);
//		ans.buildSubID(map);
//		ans.buildSubAuid(id2auidauid);
		return ans;
	}
	
	public AuidNode getAuidTree(long auid,List<Entity> entities,DataService service) {
		AuidNode ans=new AuidNode(auid, entities);
		//auid to auid
//		Set<Long> afids=entities.stream().flatMap(e->e.getAfids().stream()).collect(Collectors.toSet());
//		Map<Long, List<Entity>> auid2auidafid=afids.stream()
//				.collect(Collectors.toMap(l->l, l->service.getEntities(l, SearchType.AFID)));
		//auid to id
		Set<Long> ids=entities.stream().map(e->e.getId()).collect(Collectors.toSet());
		Map<Long, Entity> auid2idid=ids.stream()
				.collect(Collectors.toMap(l->l, l->service.getEntities(l, SearchType.ID).get(0)));
		//
//		ans.buildSubAuid(auid2auidafid);
		ans.buildSubID(auid2idid);
		return ans;
	}
	private Map<Long, Entity> getChild(Long num,SearchType searchType,DataService service){
		return service
				.getEntities(num, searchType)
				.stream()
				.collect(Collectors.toMap(Entity::getId,e->e));
	}
	
	private Map<Long, Entity> getChild(List<Long> src,SearchType searchType,DataService service){
		if (src==null) {
			return new HashMap<>();
		}
		List<Entity> s=src.stream()
				.flatMap(
						l->
						service.getEntities(l, searchType)
						.stream())
				.collect(Collectors.toList());
		Map<Long, Entity> ans=new HashMap<>(s.size());
		for (Entity entity : s) {
			if (!ans.containsKey(entity.getId())) {
				ans.put(entity.getId(), entity);
			}
		}
		return ans;
	}
	private Map<Long, List<Entity>> getAuid(List<Long> src,SearchType searchType,DataService service){
		if (src==null) {
			return new HashMap<>();
		}
		Set<Long> set=src.stream().collect(Collectors.toSet());
		return set.stream()
				.collect(Collectors.toMap(l->l, id->service.getEntities(id, searchType)));
	}
	
}

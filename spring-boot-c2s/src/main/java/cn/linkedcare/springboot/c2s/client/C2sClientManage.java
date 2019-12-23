
package cn.linkedcare.springboot.c2s.client;

import com.alibaba.fastjson.JSON;

import cn.linkedcare.springboot.c2s.dto.ServerDto;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundPathable;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class C2sClientManage {

	private Map<String, List<IC2sClient>> serversMap = new HashMap<String, List<IC2sClient>>();

	private void initServerMap(List<IC2sClient> serversClients) {
		for (IC2sClient server : serversClients) {
			String path = server.path();

			List<IC2sClient> list = serversMap.get(path);
			if (list == null) {
				list = new ArrayList<IC2sClient>();

				serversMap.put(path, list);
			}

			list.add(server);
		}
	}

	public C2sClientManage(String zkUrl, List<IC2sClient> serversClients) {
		this.initServerMap(serversClients);
		Iterator var4 = this.serversMap.keySet().iterator();
		while (var4.hasNext()) {
			String path = (String) var4.next();
			CuratorFramework client = CuratorFrameworkFactory.builder().connectString(zkUrl).sessionTimeoutMs(60000)
					.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 10000)).build();

			client.start();

			try {
				client.blockUntilConnected(2, TimeUnit.MINUTES);
			} catch (InterruptedException var7) {
				var7.printStackTrace();
				throw new RuntimeException(var7);
			}

			this.addDataChangeListener(client, path);

			this.notifyChange(client, path);
		}
	}

	private synchronized void notifyChange(CuratorFramework client, String path) {
		ArrayList servers = new ArrayList();
		List serversClients;
		Iterator var6;
		try {
			serversClients = (List) ((BackgroundPathable) client.getChildren().watched()).forPath(path);
			var6 = serversClients.iterator();
			while (true) {
				if (!var6.hasNext()) {
					break;
				}
				String data = (String) var6.next();
				byte[] bytes = (byte[]) client.getData().forPath(path + "/" + data);
				String json = new String(bytes, "UTF-8");
				servers.add(JSON.parseObject(json, ServerDto.class));

				log.info("notifyChange:{},{}", path, JSON.toJSONString(serversClients));
			}
		} catch (Exception var10) {
			var10.printStackTrace();
			log.error("exception:", var10);
			throw new RuntimeException(var10);
		}

		serversClients = (List) this.serversMap.get(path);
		var6 = serversClients.iterator();
		while (var6.hasNext()) {
			IC2sClient l = (IC2sClient) var6.next();
			try {
				l.changeNotify(servers);
			} catch (Exception var9) {
				var9.printStackTrace();
				log.error("exception:", var9);
				throw new RuntimeException(var9);
			}
		}

	}

	private void addDataChangeListener(CuratorFramework client, final String path) {
		PathChildrenCache cache = new PathChildrenCache(client, path, false);
		try {
			cache.start();
		} catch (Exception e) {
			throw new RuntimeException(e);

		}
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				switch (event.getType()) {
				case CHILD_REMOVED:
					notifyChange(client, path);
					break;
				case CHILD_ADDED:
					notifyChange(client, path);
					break;
				case CHILD_UPDATED:
					notifyChange(client, path);
				}
			}
		});
	}

}

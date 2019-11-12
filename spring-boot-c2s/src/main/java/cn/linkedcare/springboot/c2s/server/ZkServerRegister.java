package cn.linkedcare.springboot.c2s.server;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.nodes.PersistentNode;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = { "classpath:application-c2s.properties",
		"classpath:application-c2s-${spring.profiles.active}.properties" }, ignoreResourceNotFound = true, encoding = "UTF-8")
public class ZkServerRegister extends AbstractServerRegister {
	private static final Logger log = LoggerFactory.getLogger(ZkServerRegister.class);
	private CuratorFramework client;
	@Value("${c2s.server}")
	private boolean isServer;
	@Value("${zookeeper.url}")
	private String zkUrl;

	public void destory() {
		if (this.client != null) {
			this.client.close();
		}
	}

	@PostConstruct
	public void init() {
		log.info("server status:{}", this.isServer);
		if (this.isServer) {
			String json = AbstractServerRegister.getJson();

			CuratorFramework client = CuratorFrameworkFactory.builder().connectString(this.zkUrl)
					.sessionTimeoutMs(60000).retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 10000)).build();

			client.getConnectionStateListenable().addListener((cf, newState) -> {

				if (ConnectionState.CONNECTED == newState) {
					this.addNodeToZk(cf, AbstractServerRegister.getPath(), json);
				} else if (ConnectionState.RECONNECTED == newState) {
					this.addNodeToZk(cf, AbstractServerRegister.getPath(), json);
				}
			});

			client.start();

			try {
				client.blockUntilConnected(2, TimeUnit.MINUTES);
			} catch (InterruptedException var4) {
				var4.printStackTrace();
				throw new RuntimeException(var4);
			}
		}
	}

	private void addNodeToZk(CuratorFramework client, String path, String json) {
		PersistentNode node = new PersistentNode(client, CreateMode.EPHEMERAL, false,
				path + "/" + UUID.randomUUID().toString(), json.getBytes());

		node.start();
	}
}

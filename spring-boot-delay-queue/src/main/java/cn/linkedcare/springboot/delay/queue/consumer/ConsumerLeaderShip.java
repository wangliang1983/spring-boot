package cn.linkedcare.springboot.delay.queue.consumer;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import cn.linkedcare.springboot.delay.queue.config.DelayQueueConfig;
import cn.linkedcare.springboot.delay.queue.dto.ConsumerLeaderDto;
import cn.linkedcare.springboot.delay.queue.dto.ConsumerMethodDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerLeaderShip extends LeaderSelectorListenerAdapter implements Closeable {
    //选主类
	private final LeaderSelector leaderSelector;
	private final static CopyOnWriteArraySet<ConsumerLeaderDto> topicSet = new CopyOnWriteArraySet<ConsumerLeaderDto>();
	private String topic;
	private ConsumerMethodDto consumerMethodDto;
	private ConsumerLeaderDto consumerLeaderDto;
	private final ReentrantLock lock  = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	
    public ConsumerLeaderShip(String topic,ConsumerMethodDto consumerMethodDto,CuratorFramework client, String path) {
        this.topic = topic;
    	this.consumerMethodDto = consumerMethodDto;
    	this.leaderSelector = new LeaderSelector(client, path, this);
        this.leaderSelector.autoRequeue();
    }
    
    
    public static CopyOnWriteArraySet<ConsumerLeaderDto> getTopicset() {
		return topicSet;
	}


	public void start() {
        leaderSelector.start();
    }

    @Override
    public void close() {
        leaderSelector.close();
        topicSet.remove(this.consumerLeaderDto);
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
    	this.consumerLeaderDto = ConsumerLeaderDto.builder()
    			.partition(DelayQueueConfig.DEFAULT_PARTITION)
    			.topic(topic)
    			.consumerMethodDto(consumerMethodDto)
    			.condition(condition)
    			.lock(lock)
    			.build();
    	
    	topicSet.add(consumerLeaderDto);
    	
    	Thread.sleep(60*60*1000);//1个小时释放领导权
    	
    	try{
    		lock.lock();
    		condition.await();
    	}finally {
        	topicSet.remove(consumerLeaderDto);
    		lock.unlock();
		}
    	
    }

    
	
}

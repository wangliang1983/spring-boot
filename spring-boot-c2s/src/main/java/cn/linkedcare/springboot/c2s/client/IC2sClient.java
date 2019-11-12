
// Warning: No line numbers available in class file
package cn.linkedcare.springboot.c2s.client;

import java.util.List;

import cn.linkedcare.springboot.c2s.dto.ServerDto;

/**
 * 客户端
 * @author wl
 *
 */
public interface IC2sClient {
   String path();

   void changeNotify(List<ServerDto> var1);
}

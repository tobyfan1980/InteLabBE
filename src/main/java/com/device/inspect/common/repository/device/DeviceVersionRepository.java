package com.device.inspect.common.repository.device;

import com.device.inspect.common.model.device.DeviceVersion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Administrator on 2016/10/9.
 */
public interface DeviceVersionRepository extends CrudRepository<DeviceVersion ,Integer> {
    @Query("select o from DeviceVersion o  order by o.createDate desc")
    public DeviceVersion findTopOrderByCreateDateDesc();
}

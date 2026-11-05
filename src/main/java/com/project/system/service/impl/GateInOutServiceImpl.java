package com.project.system.service.impl;

import java.util.List;

import com.project.common.utils.TimeUuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.system.mapper.GateInOutMapper;
import com.project.system.domain.GateInOut;
import com.project.system.service.IGateInOutService;
import com.project.common.utils.str.Convert;

/**
 * 门禁记录 服务层实现
 *
 * @author administrator
 * @date 2020-05-27
 */
@Service
public class GateInOutServiceImpl implements IGateInOutService {
    @Autowired
    private GateInOutMapper gateInOutMapper;

    /**
     * 查询门禁记录信息
     *
     * @param id 门禁记录ID
     * @return 门禁记录信息
     */
    @Override
    public GateInOut selectGateInOutById(Long id) {
        return gateInOutMapper.selectGateInOutById(id);
    }

    /**
     * 查询门禁记录列表
     *
     * @param gateInOut 门禁记录信息
     * @return 门禁记录集合
     */
    @Override
    public List<GateInOut> selectGateInOutList(GateInOut gateInOut) {
        return gateInOutMapper.selectGateInOutList(gateInOut);
    }

    /**
     * 新增门禁记录
     *
     * @param gateInOut 门禁记录信息
     * @return 结果
     */
    @Override
    public int insertGateInOut(GateInOut gateInOut) {
        gateInOut.setId(TimeUuidUtil.get16UUID());
        return gateInOutMapper.insertGateInOut(gateInOut);
    }

    /**
     * 修改门禁记录
     *
     * @param gateInOut 门禁记录信息
     * @return 结果
     */
    @Override
    public int updateGateInOut(GateInOut gateInOut) {
        return gateInOutMapper.updateGateInOut(gateInOut);
    }

    /**
     * 删除门禁记录对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteGateInOutByIds(String ids) {
        return gateInOutMapper.deleteGateInOutByIds(Convert.toStrArray(ids));
    }

    /**
     * 查询用户上次门禁记录类型
     * @param userId
     * @return
     */
    @Override
    public Integer selectKindLastTime(Long userId){
        return gateInOutMapper.selectKindLastTime(userId);
    }

}

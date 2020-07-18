package com.xpu.school_guide.service;

import com.xpu.school_guide.dto.ResultMsg;
import com.xpu.school_guide.enums.Code;
import com.xpu.school_guide.mapper.GroupsMapper;
import com.xpu.school_guide.mapper.SitesMapper;
import com.xpu.school_guide.mapper.TasksMapper;
import com.xpu.school_guide.pojo.Groups;
import com.xpu.school_guide.pojo.GroupsExample;
import com.xpu.school_guide.pojo.Sites;
import com.xpu.school_guide.pojo.Tasks;
import org.apache.ibatis.mapping.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author sofency
 * @date 2020/5/12 6:13
 * @package IntelliJ IDEA
 * @description
 */
@Service
public class SiteService {
    private SitesMapper sitesMapper;
    private TasksMapper tasksMapper;
    RedisTemplate<String,Object> redisTemplate;
    private GroupsMapper groupsMapper;

    @Autowired
    public SiteService(SitesMapper sitesMapper,TasksMapper tasksMapper,
                       RedisTemplate<String,Object> redisTemplate,
                        GroupsMapper groupMapper) {
        this.sitesMapper = sitesMapper;
        this.tasksMapper = tasksMapper;
        this.redisTemplate = redisTemplate;
        this.groupsMapper = groupMapper;
    }

    /**
     * 添加地点
     * @param sites 具体信息
     *              siteLo  经度
     *              siteIo  //维度
     *              siteName 地理名字
     *              isDelete 是否删除
     * @return
     */
    public ResultMsg addPlace(Sites sites){
        ResultMsg resultMsg = new ResultMsg();
        sites.setIsDelete(false);
        int insert = sitesMapper.insert(sites);
        if(insert>0){//说明插入成功
            redisTemplate.opsForValue().set("site::"+sites.getId(),sites);//存储到缓存中
            resultMsg.setMsg(Code.LOGIN_SUCCESS.getMsg());
            resultMsg.setStatus(Code.LOGIN_SUCCESS.getStatus());
        }else{//插入失败
            resultMsg.setMsg(Code.LOGIN_FAIL.getMsg());
            resultMsg.setStatus(Code.LOGIN_FAIL.getStatus());
        }
        return resultMsg;
    }

    /**
     * 修改地点
     * @param sites
     * @return
     */
    public ResultMsg updatePlace(Sites sites){
        ResultMsg resultMsg = new ResultMsg();
        int i = sitesMapper.updateByPrimaryKeySelective(sites);

        if(i>0){//更新成功
            redisTemplate.opsForValue().set("site::"+sites.getId(),sites);
            resultMsg.setStatus(Code.DELETE_SUCCESS.getStatus());
            resultMsg.setMsg(Code.DELETE_SUCCESS.getMsg());
        }else{  //更新失败
            resultMsg.setMsg(Code.DELETE_FAIL.getMsg());
            resultMsg.setStatus(Code.DELETE_FAIL.getStatus());
        }
        return resultMsg;
    }
    /**
     * 展示所有管理员已经添加的地点
     * @return
     */
    public ResultMsg showSites(){
        ResultMsg resultMsg = new ResultMsg();
        List<Sites> showSites =new ArrayList<>();
        Set<String> keys =null;
        try{
            keys = redisTemplate.keys("site::*");//显示所有已经添加过的点
        }catch (Exception e){
            System.out.println("缓存出现问题 要去查数据库了");
        }

        if(keys!=null&&keys.size()!=0){//判断缓存中是否有值
            for(String key: keys){
                showSites.add((Sites) redisTemplate.opsForValue().get(key));
            }
        }else{
            showSites = sitesMapper.selectByExample(null);
            for(Sites sites:showSites){
                redisTemplate.opsForValue().set("site::"+sites.getId(),sites);//添加到缓存中
            }
        }
        if(showSites.size()==0){
            resultMsg.setStatus(Code.NO_SITES.getStatus());
            resultMsg.setMsg(Code.NO_SITES.getMsg());
        }else{
            resultMsg.setData(showSites);//设置数据
            resultMsg.setStatus(Code.SEARCH_SUCCESS.getStatus());
            resultMsg.setMsg(Code.SEARCH_SUCCESS.getMsg());
        }
        return resultMsg;
    }

    /**
     * 根据删除任务id删除任务
     * @param taskId
     * @return
     */
    public ResultMsg deleteTask(int taskId){
        ResultMsg resultMsg = new ResultMsg();
        int i = tasksMapper.deleteByPrimaryKey(taskId);
        if(i>0){
            resultMsg.setMsg(Code.DELETE_SUCCESS.getMsg());
            resultMsg.setStatus(Code.DELETE_SUCCESS.getStatus());
        }else{
            resultMsg.setMsg(Code.DELETE_FAIL.getMsg());
            resultMsg.setStatus(Code.DELETE_FAIL.getStatus());
        }
        return resultMsg;
    }

    /**
     * 插入任务信息
     */
    @Transactional
    public void insertTask(Tasks tasks,List<Groups> groups){
        tasksMapper.insert(tasks);//返回插入的主键
        for (Groups groups1 : groups) {
            groups1.setTasksId(tasks.getId());
            groups1.setIsDelete(false);
            groupsMapper.insert(groups1);//批量插入数据
        }
    }

    /**
     * 更新任务信息
     * @return
     */
    @Transactional
    public void updateTask(Tasks tasks,int taskId,List<Groups> groups){
        //更新任务的信息
        tasks.setId(taskId);
        tasksMapper.updateByPrimaryKey(tasks);
        //删除taskId的步骤
        GroupsExample groupsExample = new GroupsExample();
        groupsExample.createCriteria().andTasksIdEqualTo(taskId);
        groupsMapper.deleteByExample(groupsExample);
        //插入步骤的信息
        for (Groups groups1 : groups) {//更新的时候带上任务的id
            groups1.setTasksId(taskId);
            groupsMapper.insert(groups1);//批量插入数据
        }
    }
}

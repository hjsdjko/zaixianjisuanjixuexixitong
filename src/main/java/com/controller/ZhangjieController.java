
package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 章节
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/zhangjie")
public class ZhangjieController {
    private static final Logger logger = LoggerFactory.getLogger(ZhangjieController.class);

    private static final String TABLE_NAME = "zhangjie";

    @Autowired
    private ZhangjieService zhangjieService;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private CaozuorizhiService caozuorizhiService;//操作日志
    @Autowired
    private DictionaryService dictionaryService;//字典
    @Autowired
    private GonggaoService gonggaoService;//公告
    @Autowired
    private KechengService kechengService;//课程
    @Autowired
    private KechengCommentbackService kechengCommentbackService;//课程评价
    @Autowired
    private KechengOrderService kechengOrderService;//课程订单
    @Autowired
    private YonghuService yonghuService;//用户
    @Autowired
    private UsersService usersService;//管理员


    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("用户".equals(role))
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        CommonUtil.checkMap(params);
        PageUtils page = zhangjieService.queryPage(params);

        //字典表数据转换
        List<ZhangjieView> list =(List<ZhangjieView>)page.getList();
        for(ZhangjieView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        caozuorizhiService.insertCaozuorizhi(String.valueOf(request.getSession().getAttribute("role")),TABLE_NAME,String.valueOf(request.getSession().getAttribute("username")),"列表查询",list.toString());
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ZhangjieEntity zhangjie = zhangjieService.selectById(id);
        if(zhangjie !=null){
            //entity转view
            ZhangjieView view = new ZhangjieView();
            BeanUtils.copyProperties( zhangjie , view );//把实体数据重构到view中
            //级联表 课程
            //级联表
            KechengEntity kecheng = kechengService.selectById(zhangjie.getKechengId());
            if(kecheng != null){
            BeanUtils.copyProperties( kecheng , view ,new String[]{ "id", "createTime", "insertTime", "updateTime"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setKechengId(kecheng.getId());
            }
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
    caozuorizhiService.insertCaozuorizhi(String.valueOf(request.getSession().getAttribute("role")),TABLE_NAME,String.valueOf(request.getSession().getAttribute("username")),"单条数据查看",view.toString());
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody ZhangjieEntity zhangjie, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,zhangjie:{}",this.getClass().getName(),zhangjie.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        Wrapper<ZhangjieEntity> queryWrapper = new EntityWrapper<ZhangjieEntity>()
            .eq("kecheng_id", zhangjie.getKechengId())
            .eq("zhangjie_name", zhangjie.getZhangjieName())
            .eq("zhangjie_xuhao", zhangjie.getZhangjieXuhao())
            .eq("zhangjie_video", zhangjie.getZhangjieVideo())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ZhangjieEntity zhangjieEntity = zhangjieService.selectOne(queryWrapper);
        if(zhangjieEntity==null){
            zhangjie.setZhangjieClicknum(1);
            zhangjie.setInsertTime(new Date());
            zhangjie.setCreateTime(new Date());
            zhangjieService.insert(zhangjie);
            caozuorizhiService.insertCaozuorizhi(String.valueOf(request.getSession().getAttribute("role")),TABLE_NAME,String.valueOf(request.getSession().getAttribute("username")),"新增",zhangjie.toString());
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody ZhangjieEntity zhangjie, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,zhangjie:{}",this.getClass().getName(),zhangjie.toString());
        ZhangjieEntity oldZhangjieEntity = zhangjieService.selectById(zhangjie.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
        if("".equals(zhangjie.getZhangjieVideo()) || "null".equals(zhangjie.getZhangjieVideo())){
                zhangjie.setZhangjieVideo(null);
        }
        if("".equals(zhangjie.getZhangjieContent()) || "null".equals(zhangjie.getZhangjieContent())){
                zhangjie.setZhangjieContent(null);
        }

            zhangjieService.updateById(zhangjie);//根据id更新
            List<String> strings = caozuorizhiService.clazzDiff(zhangjie, oldZhangjieEntity, request,new String[]{"updateTime"});
            caozuorizhiService.insertCaozuorizhi(String.valueOf(request.getSession().getAttribute("role")),TABLE_NAME,String.valueOf(request.getSession().getAttribute("username")),"修改",strings.toString());
            return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<ZhangjieEntity> oldZhangjieList =zhangjieService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        zhangjieService.deleteBatchIds(Arrays.asList(ids));

        caozuorizhiService.insertCaozuorizhi(String.valueOf(request.getSession().getAttribute("role")),TABLE_NAME,String.valueOf(request.getSession().getAttribute("username")),"删除",oldZhangjieList.toString());
        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //.eq("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
        try {
            List<ZhangjieEntity> zhangjieList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            ZhangjieEntity zhangjieEntity = new ZhangjieEntity();
//                            zhangjieEntity.setKechengId(Integer.valueOf(data.get(0)));   //课程 要改的
//                            zhangjieEntity.setZhangjieName(data.get(0));                    //章节标题 要改的
//                            zhangjieEntity.setZhangjieClicknum(Integer.valueOf(data.get(0)));   //章节热度 要改的
//                            zhangjieEntity.setZhangjieXuhao(Integer.valueOf(data.get(0)));   //序号 要改的
//                            zhangjieEntity.setZhangjieVideo(data.get(0));                    //章节视频 要改的
//                            zhangjieEntity.setZhangjieContent("");//详情和图片
//                            zhangjieEntity.setInsertTime(date);//时间
//                            zhangjieEntity.setCreateTime(date);//时间
                            zhangjieList.add(zhangjieEntity);


                            //把要查询是否重复的字段放入map中
                        }

                        //查询是否重复
                        zhangjieService.insertBatch(zhangjieList);
                        caozuorizhiService.insertCaozuorizhi(String.valueOf(request.getSession().getAttribute("role")),TABLE_NAME,String.valueOf(request.getSession().getAttribute("username")),"批量新增",zhangjieList.toString());
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }




    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        CommonUtil.checkMap(params);
        PageUtils page = zhangjieService.queryPage(params);

        //字典表数据转换
        List<ZhangjieView> list =(List<ZhangjieView>)page.getList();
        for(ZhangjieView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        caozuorizhiService.insertCaozuorizhi(String.valueOf(request.getSession().getAttribute("role")),TABLE_NAME,String.valueOf(request.getSession().getAttribute("username")),"列表查询",list.toString());
        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Integer id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ZhangjieEntity zhangjie = zhangjieService.selectById(id);
            if(zhangjie !=null){

                //点击数量加1
                zhangjie.setZhangjieClicknum(zhangjie.getZhangjieClicknum()+1);
                zhangjieService.updateById(zhangjie);

                //entity转view
                ZhangjieView view = new ZhangjieView();
                BeanUtils.copyProperties( zhangjie , view );//把实体数据重构到view中

                //级联表
                    KechengEntity kecheng = kechengService.selectById(zhangjie.getKechengId());
                if(kecheng != null){
                    BeanUtils.copyProperties( kecheng , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setKechengId(kecheng.getId());
                }
                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view, request);
                    caozuorizhiService.insertCaozuorizhi(String.valueOf(request.getSession().getAttribute("role")),TABLE_NAME,String.valueOf(request.getSession().getAttribute("username")),"单条数据查看",view.toString());
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody ZhangjieEntity zhangjie, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,zhangjie:{}",this.getClass().getName(),zhangjie.toString());
        Wrapper<ZhangjieEntity> queryWrapper = new EntityWrapper<ZhangjieEntity>()
            .eq("kecheng_id", zhangjie.getKechengId())
            .eq("zhangjie_name", zhangjie.getZhangjieName())
            .eq("zhangjie_clicknum", zhangjie.getZhangjieClicknum())
            .eq("zhangjie_xuhao", zhangjie.getZhangjieXuhao())
            .eq("zhangjie_video", zhangjie.getZhangjieVideo())
//            .notIn("zhangjie_types", new Integer[]{102})
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ZhangjieEntity zhangjieEntity = zhangjieService.selectOne(queryWrapper);
        if(zhangjieEntity==null){
            zhangjie.setZhangjieClicknum(1);
            zhangjie.setInsertTime(new Date());
            zhangjie.setCreateTime(new Date());
        zhangjieService.insert(zhangjie);

            caozuorizhiService.insertCaozuorizhi(String.valueOf(request.getSession().getAttribute("role")),TABLE_NAME,String.valueOf(request.getSession().getAttribute("username")),"前台新增",zhangjie.toString());
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

}


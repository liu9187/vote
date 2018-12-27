package com.minxing365.vote.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.minxing365.vote.dao.VotingDetailsMapper;
import com.minxing365.vote.pojo.ResultVo;
import com.minxing365.vote.pojo.VoteDetailsVo;
import com.minxing365.vote.service.VotingDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VotingDetailsServiceImpl implements VotingDetailsService {
    Logger log = LoggerFactory.getLogger(VotingDetailsServiceImpl.class);
    @Autowired
    private VotingDetailsMapper votingDetailsMapper;
    //信息采集主题
    @Value("${name}")
    private String name;

    @Override
    public String selectVotingDetailsByName(String name) {
        String nameStr = "%" + name + "%";
        List<VoteDetailsVo> list = votingDetailsMapper.selectVotingDetailsByName(nameStr);
        List<ResultVo> resultVoList = new ArrayList<>();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ResultVo resultVo = new ResultVo();
                resultVo.setId(list.get(i).getId());
                resultVo.setAppId(list.get(i).getAppId());
                resultVo.setUserId(list.get(i).getUserId());
                resultVo.setName(list.get(i).getName());
                JSONObject jsonObject = JSONObject.parseObject(list.get(i).getBody());
                resultVo.setLabel(jsonObject.getString("label"));
                resultVo.setTip(jsonObject.getString("tip"));
                resultVoList.add(resultVo);
            }
        }

//        JSONObject jsonObject;
//        VoteDetailsVo voteDetailsVo = new VoteDetailsVo();
//        try {
//            jsonObject = JSONObject.parseObject(result);
//            JSONArray array = jsonObject.getJSONArray("data");
//            List<Data> dataList = new ArrayList<>();
//            if (null != array || "".equals(array)) {
//
//                for (int i = 0; i < array.size(); i++) {
//                    Data data = new Data();
//                    JSONObject jo = array.getJSONObject(i);
//                    data.setLabel(jo.getString("label"));
//                    data.setValue(jo.getInteger("value"));
//                    dataList.add(data);
//                }
//            }
////            voteDetailsVo.setCondition(jsonObject.getBoolean("condition"));
////            voteDetailsVo.setType(jsonObject.getString("type"));
//            // voteDetailsVo.setName(jsonObject.getString("name"));
//            voteDetailsVo.setLabel(jsonObject.getString("label"));
//            voteDetailsVo.setTip(jsonObject.getString("tip"));
////            voteDetailsVo.setBind(jsonObject.getString("bind"));
////            voteDetailsVo.setComponentId(jsonObject.getString("componentId"));
////            voteDetailsVo.setValueClassName(jsonObject.getString("valueClassName"));
////            voteDetailsVo.setValid(jsonObject.getBoolean("valid"));
////            Restrict restrict = new Restrict();
////            JSONObject json_restrict = jsonObject.getJSONObject("restrict");
////            restrict.setLayout(json_restrict.getString("layout"));
////            restrict.setRequired(json_restrict.getBoolean("required"));
////            voteDetailsVo.setRestrict(restrict);
////            voteDetailsVo.setValue(jsonObject.getString("value"));
//            voteDetailsVo.setId(jsonObject.getInteger("id"));
//            voteDetailsVo.setData(dataList);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        JSONObject object = new JSONObject();
        object.put("list", resultVoList);
        object.put("message", "查询成功");
        return object.toJSONString();
    }

    @Override
    public JSONArray selectVotingDetails2ByName() throws UnsupportedEncodingException {
        name = "双评双促测试";
        log.info("获取信息采集主题:" + name);
        List<VoteDetailsVo> list = votingDetailsMapper.selectVotingDetails2ByName(name);
        List<ResultVo> resultVoList = new ArrayList<>();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String forms = list.get(i).getForms();
                JSONArray ja = JSONArray.parseArray(forms);
                //解析数组
                String department = "";
                for (int j = 0; j < ja.size(); j++) {
                    String type = ja.getJSONObject(j).getString("type");
                    if (type.equals("explanation")) {
                        String bodyStr = ja.getJSONObject(j).getString("body");
                        department = JSONObject.parseObject(bodyStr).getString("label");
                    } else {
                        ResultVo resultVo = new ResultVo();
                        //获取部门
                        resultVo.setDepartment(department);
                        //获取label
                        resultVo.setAuditStandard(JSONObject.parseObject(ja.getJSONObject(j).getString("body")).getString("label"));
                        JSONArray value = JSONObject.parseObject(ja.getJSONObject(j).getString("body")).getJSONArray("value");
                        //获取value
                        if (value.size() > 0) {
                            // log.info("value="+value.get(0).toString());
                            resultVo.setValue(Integer.valueOf(value.get(0).toString()));
                        }
                        //获取userId
                        resultVo.setUserId(list.get(i).getUserId());
                        //获取appId
                        resultVo.setAppId(list.get(i).getAppId());
                        //添加到序列
                        resultVoList.add(resultVo);
                    }
                }

            }
        }
        JSONArray groupList = null;
        try {
            log.info("resultVoList长度=" + resultVoList.size());

            groupList = getGroup(resultVoList);
        } catch (Exception e) {
            log.error("<<<<<<<<list 分组失败", e);
        }
        //对数据进行分组

        return groupList;
    }

    /**
     * 对list数据进行分组
     *
     * @param resultVoList
     */
    private JSONArray getGroup(List<ResultVo> resultVoList) {
        //公司业务部
        //职能发挥
        List<ResultVo> list1_1 = new ArrayList<>();
        //工作效率
        List<ResultVo> list1_2 = new ArrayList<>();
        //服务质量
        List<ResultVo> list1_3 = new ArrayList<>();
        //服务态度
        List<ResultVo> list1_4 = new ArrayList<>();
        //三农业务部
        List<ResultVo> list2_1 = new ArrayList<>();
        List<ResultVo> list2_2 = new ArrayList<>();
        List<ResultVo> list2_3 = new ArrayList<>();
        List<ResultVo> list2_4 = new ArrayList<>();
        //电子银行部
        List<ResultVo> list3_1 = new ArrayList<>();
        List<ResultVo> list3_2 = new ArrayList<>();
        List<ResultVo> list3_3 = new ArrayList<>();
        List<ResultVo> list3_4 = new ArrayList<>();
        //资金业务部
        List<ResultVo> list4_1 = new ArrayList<>();
        List<ResultVo> list4_2 = new ArrayList<>();
        List<ResultVo> list4_3 = new ArrayList<>();
        List<ResultVo> list4_4 = new ArrayList<>();
        //国际业务部
        List<ResultVo> list5_1 = new ArrayList<>();
        List<ResultVo> list5_2 = new ArrayList<>();
        List<ResultVo> list5_3 = new ArrayList<>();
        List<ResultVo> list5_4 = new ArrayList<>();
        //风险管理部
        List<ResultVo> list6_1 = new ArrayList<>();
        List<ResultVo> list6_2 = new ArrayList<>();
        List<ResultVo> list6_3 = new ArrayList<>();
        List<ResultVo> list6_4 = new ArrayList<>();
        //7.运营管理部
        List<ResultVo> list7_1 = new ArrayList<>();
        List<ResultVo> list7_2 = new ArrayList<>();
        List<ResultVo> list7_3 = new ArrayList<>();
        List<ResultVo> list7_4 = new ArrayList<>();
        //8.合规管理部
        List<ResultVo> list8_1 = new ArrayList<>();
        List<ResultVo> list8_2 = new ArrayList<>();
        List<ResultVo> list8_3 = new ArrayList<>();
        List<ResultVo> list8_4 = new ArrayList<>();
        //9.计划财务部
        List<ResultVo> list9_1 = new ArrayList<>();
        List<ResultVo> list9_2 = new ArrayList<>();
        List<ResultVo> list9_3 = new ArrayList<>();
        List<ResultVo> list9_4 = new ArrayList<>();
        //10.信贷管理部
        List<ResultVo> list10_1 = new ArrayList<>();
        List<ResultVo> list10_2 = new ArrayList<>();
        List<ResultVo> list10_3 = new ArrayList<>();
        List<ResultVo> list10_4 = new ArrayList<>();
        //11.科技信息部
        List<ResultVo> list11_1 = new ArrayList<>();
        List<ResultVo> list11_2 = new ArrayList<>();
        List<ResultVo> list11_3 = new ArrayList<>();
        List<ResultVo> list11_4 = new ArrayList<>();
        //12.人力资源部
        List<ResultVo> list12_1 = new ArrayList<>();
        List<ResultVo> list12_2 = new ArrayList<>();
        List<ResultVo> list12_3 = new ArrayList<>();
        List<ResultVo> list12_4 = new ArrayList<>();
        //13.行长办公室
        List<ResultVo> list13_1 = new ArrayList<>();
        List<ResultVo> list13_2 = new ArrayList<>();
        List<ResultVo> list13_3 = new ArrayList<>();
        List<ResultVo> list13_4 = new ArrayList<>();
        //14.后勤保障部
        List<ResultVo> list14_1 = new ArrayList<>();
        List<ResultVo> list14_2 = new ArrayList<>();
        List<ResultVo> list14_3 = new ArrayList<>();
        List<ResultVo> list14_4 = new ArrayList<>();
        //15.安全保卫部
        List<ResultVo> list15_1 = new ArrayList<>();
        List<ResultVo> list15_2 = new ArrayList<>();
        List<ResultVo> list15_3 = new ArrayList<>();
        List<ResultVo> list15_4 = new ArrayList<>();
        //16.稽核审计部
        List<ResultVo> list16_1 = new ArrayList<>();
        List<ResultVo> list16_2 = new ArrayList<>();
        List<ResultVo> list16_3 = new ArrayList<>();
        List<ResultVo> list16_4 = new ArrayList<>();
        //17.调查统计部
        List<ResultVo> list17_1 = new ArrayList<>();
        List<ResultVo> list17_2 = new ArrayList<>();
        List<ResultVo> list17_3 = new ArrayList<>();
        List<ResultVo> list17_4 = new ArrayList<>();
        //18.纪检监察部
        List<ResultVo> list18_1 = new ArrayList<>();
        List<ResultVo> list18_2 = new ArrayList<>();
        List<ResultVo> list18_3 = new ArrayList<>();
        List<ResultVo> list18_4 = new ArrayList<>();
        //19.董事会办公室
        List<ResultVo> list19_1 = new ArrayList<>();
        List<ResultVo> list19_2 = new ArrayList<>();
        List<ResultVo> list19_3 = new ArrayList<>();
        List<ResultVo> list19_4 = new ArrayList<>();
        //20.监事会办公室
        List<ResultVo> list20_1 = new ArrayList<>();
        List<ResultVo> list20_2 = new ArrayList<>();
        List<ResultVo> list20_3 = new ArrayList<>();
        List<ResultVo> list20_4 = new ArrayList<>();
        //21.党群工作部
        List<ResultVo> list21_1 = new ArrayList<>();
        List<ResultVo> list21_2 = new ArrayList<>();
        List<ResultVo> list21_3 = new ArrayList<>();
        List<ResultVo> list21_4 = new ArrayList<>();
        //22.工会
        List<ResultVo> list22_1 = new ArrayList<>();
        List<ResultVo> list22_2 = new ArrayList<>();
        List<ResultVo> list22_3 = new ArrayList<>();
        List<ResultVo> list22_4 = new ArrayList<>();

        for (int i = 0; i < resultVoList.size(); i++) {
            String auditStandard = resultVoList.get(i).getAuditStandard();

            String department = resultVoList.get(i).getDepartment();
            //分组
            //  log.info("-----auditStandard"+auditStandard+";department="+department);
            if (auditStandard.equals("职能发挥") && department.equals("公司业务部")) {
                list1_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("公司业务部")) {
                list1_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("公司业务部")) {
                list1_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("公司业务部")) {
                list1_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("三农业务部")) {
                list2_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("三农业务部")) {
                list2_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("三农业务部")) {
                list2_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("三农业务部")) {
                list2_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("电子银行部")) {
                list3_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("电子银行部")) {
                list3_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("电子银行部")) {
                list3_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("电子银行部")) {
                list3_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("资金业务部")) {
                list4_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("资金业务部")) {
                list4_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("资金业务部")) {
                list4_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("资金业务部")) {
                list4_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("国际业务部")) {
                list5_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("国际业务部")) {
                list5_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("国际业务部")) {
                list5_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("国际业务部")) {
                list5_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("风险管理部")) {
                list6_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("风险管理部")) {
                list6_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("风险管理部")) {
                list6_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("风险管理部")) {
                list6_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("运营管理部")) {
                list7_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("运营管理部")) {
                list7_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("运营管理部")) {
                list7_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("运营管理部")) {
                list7_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("合规管理部")) {
                list8_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("合规管理部")) {
                list8_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("合规管理部")) {
                list8_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("合规管理部")) {
                list8_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("计划财务部")) {
                list9_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("计划财务部")) {
                list9_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("计划财务部")) {
                list9_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("计划财务部")) {
                list9_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("信贷管理部")) {
                list10_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("信贷管理部")) {
                list10_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("信贷管理部")) {
                list10_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("信贷管理部")) {
                list10_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("科技信息部")) {
                list11_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("科技信息部")) {
                list11_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("科技信息部")) {
                list11_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("科技信息部")) {
                list11_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("人力资源部")) {
                list12_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("人力资源部")) {
                list12_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("人力资源部")) {
                list12_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("人力资源部")) {
                list12_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("行长办公室")) {
                list13_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("行长办公室")) {
                list13_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("行长办公室")) {
                list13_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("行长办公室")) {
                list13_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("后勤保障部")) {
                list14_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("后勤保障部")) {
                list14_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("后勤保障部")) {
                list14_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("后勤保障部")) {
                list14_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("安全保卫部")) {
                list15_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("安全保卫部")) {
                list15_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("安全保卫部")) {
                list15_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("安全保卫部")) {
                list15_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("稽核审计部")) {
                list16_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("稽核审计部")) {
                list16_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("稽核审计部")) {
                list16_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("稽核审计部")) {
                list16_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("调查统计部")) {
                list17_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("调查统计部")) {
                list17_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("调查统计部")) {
                list17_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("调查统计部")) {
                list17_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("纪检监察部")) {
                list18_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("纪检监察部")) {
                list18_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("纪检监察部")) {
                list18_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("纪检监察部")) {
                list18_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("董事会办公室")) {
                list19_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("董事会办公室")) {
                list19_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("董事会办公室")) {
                list19_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("董事会办公室")) {
                list19_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("监事会办公室")) {
                list20_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("监事会办公室")) {
                list20_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("监事会办公室")) {
                list20_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("监事会办公室")) {
                list20_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("党群工作部")) {
                list21_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("党群工作部")) {
                list21_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("党群工作部")) {
                list21_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("党群工作部")) {
                list21_4.add(resultVoList.get(i));
            }


            if (auditStandard.equals("职能发挥") && department.equals("工会")) {
                list22_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("工会")) {
                list22_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("工会")) {
                list22_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("工会")) {
                list22_4.add(resultVoList.get(i));
            }

        }

        //统计
        Integer value_1_1 = 0;
        if (list1_1.size() > 0) {
            //log.info("list1_1"+list1_1.get(0).toString());
            for (ResultVo a : list1_1) {
                if (null != a.getValue()) {
                    value_1_1 = value_1_1 + a.getValue();
                }

            }
        }

        Integer value_1_2 = 0;
        if (list1_2.size() > 0) {
            for (ResultVo a : list1_2) {
                if (null!=a.getValue())
                value_1_2 = value_1_2 + a.getValue();
            }
        }
        Integer value_1_3 = 0;
        if (list1_3.size() > 0) {
            for (ResultVo a : list1_3) {
                if (null!=a.getValue())
                value_1_3 = value_1_3 + a.getValue();
            }
        }
        Integer value_1_4 = 0;
        if (list1_4.size() > 0) {
            for (ResultVo a : list1_4) {
                if (null!=a.getValue())
                value_1_4 = value_1_4 + a.getValue();
            }
        }
        Integer value_2_1 = 0;
        if (list2_1.size() > 0) {
            for (ResultVo a : list2_1) {
                if (null!=a.getValue())
                value_2_1 = value_2_1 + a.getValue();
            }
        }
        Integer value_2_2 = 0;
        if (list2_2.size() > 0) {
            for (ResultVo a : list2_2) {
                if (null!=a.getValue())
                value_2_2 = value_2_2 + a.getValue();
            }
        }
        Integer value_2_3 = 0;
        if (list2_3.size() > 0) {
            for (ResultVo a : list2_3) {
                value_2_3 = value_2_3 + a.getValue();
            }
        }
        Integer value_2_4 = 0;
        if (list2_4.size() > 0) {
            for (ResultVo a : list2_4) {
                if (null!=a.getValue())
                value_2_4 = value_2_4 + a.getValue();
            }
        }

        Integer value_3_1 = 0;
        if (list3_1.size() > 0) {
            for (ResultVo a : list3_1) {
                if (null!=a.getValue())
                value_3_1 = value_3_1 + a.getValue();
            }
        }
        Integer value_3_2 = 0;
        if (list3_2.size() > 0) {
            for (ResultVo a : list3_2) {
                if (null!=a.getValue())
                value_3_2 = value_3_2 + a.getValue();
            }
        }
        Integer value_3_3 = 0;
        if (list3_3.size() > 0) {
            for (ResultVo a : list3_3) {
                if (null!=a.getValue())
                value_3_3 = value_3_3 + a.getValue();
            }
        }
        Integer value_3_4 = 0;
        if (list3_4.size() > 0) {
            for (ResultVo a : list3_4) {
                value_3_4 = value_3_4 + a.getValue();
            }
        }

        Integer value_4_1 = 0;
        if (list4_1.size() > 0) {
            for (ResultVo a : list4_1) {
                if (null!=a.getValue())
                value_4_1 = value_4_1 + a.getValue();
            }
        }
        Integer value_4_2 = 0;
        if (list4_2.size() > 0) {
            for (ResultVo a : list4_2) {
                if (null!=a.getValue())
                value_4_2 = value_4_2 + a.getValue();
            }
        }
        Integer value_4_3 = 0;
        if (list4_3.size() > 0) {
            for (ResultVo a : list4_3) {
                if (null!=a.getValue())
                value_4_3 = value_4_3 + a.getValue();
            }
        }
        Integer value_4_4 = 0;
        if (list4_4.size() > 0) {
            for (ResultVo a : list4_4) {
                value_4_4 = value_4_4 + a.getValue();
            }
        }

        Integer value_5_1 = 0;
        if (list5_1.size() > 0) {
            for (ResultVo a : list5_1) {
                if (null!=a.getValue())
                value_5_1 = value_5_1 + a.getValue();
            }
        }
        Integer value_5_2 = 0;
        if (list5_2.size() > 0) {
            for (ResultVo a : list5_2) {
                if (null!=a.getValue())
                value_5_2 = value_5_2 + a.getValue();
            }
        }
        Integer value_5_3 = 0;
        if (list5_3.size() > 0) {
            for (ResultVo a : list5_3) {
                if (null!=a.getValue())
                value_5_3 = value_5_3 + a.getValue();
            }
        }
        Integer value_5_4 = 0;
        if (list5_4.size() > 0) {
            for (ResultVo a : list5_4) {
                if (null!=a.getValue())
                value_5_4 = value_5_4 + a.getValue();
            }
        }

        Integer value_6_1 = 0;
        if (list6_1.size() > 0) {
            for (ResultVo a : list6_1) {
                value_6_1 = value_6_1 + a.getValue();
            }
        }
        Integer value_6_2 = 0;
        if (list6_2.size() > 0) {
            for (ResultVo a : list6_2) {
                if (null!=a.getValue())
                value_6_2 = value_6_2 + a.getValue();
            }
        }
        Integer value_6_3 = 0;
        if (list6_3.size() > 0) {
            for (ResultVo a : list6_3) {
                if (null!=a.getValue())
                value_6_3 = value_6_3 + a.getValue();
            }
        }
        Integer value_6_4 = 0;
        if (list6_4.size() > 0) {
            for (ResultVo a : list6_4) {
                value_6_4 = value_6_4 + a.getValue();
            }
        }

        Integer value_7_1 = 0;
        if (list7_1.size() > 0) {
            for (ResultVo a : list7_1) {
                if (null!=a.getValue())
                value_7_1 = value_7_1 + a.getValue();
            }
        }
        Integer value_7_2 = 0;
        if (list7_2.size() > 0) {
            for (ResultVo a : list7_2) {
                if (null!=a.getValue())
                value_7_2 = value_7_2 + a.getValue();
            }
        }
        Integer value_7_3 = 0;
        if (list7_3.size() > 0) {
            for (ResultVo a : list7_3) {
                if (null!=a.getValue())
                value_7_3 = value_7_3 + a.getValue();
            }
        }
        Integer value_7_4 = 0;
        if (list7_4.size() > 0) {
            for (ResultVo a : list7_4) {
                if (null!=a.getValue())
                value_7_4 = value_7_4 + a.getValue();
            }
        }

        Integer value_8_1 = 0;
        if (list8_1.size() > 0) {
            for (ResultVo a : list8_1) {
                value_8_1 = value_8_1 + a.getValue();
            }
        }
        Integer value_8_2 = 0;
        if (list8_2.size() > 0) {
            for (ResultVo a : list8_2) {
                if (null!=a.getValue())
                value_8_2 = value_8_2 + a.getValue();
            }
        }
        Integer value_8_3 = 0;
        if (list8_3.size() > 0) {
            for (ResultVo a : list8_3) {
                if (null!=a.getValue())
                value_8_3 = value_8_3 + a.getValue();
            }
        }
        Integer value_8_4 = 0;
        if (list8_4.size() > 0) {
            for (ResultVo a : list8_4) {
                if (null!=a.getValue())
                value_8_4 = value_8_4 + a.getValue();
            }
        }

        Integer value_9_1 = 0;
        if (list9_1.size() > 0) {
            for (ResultVo a : list9_1) {
                if (null!=a.getValue())
                value_9_1 = value_9_1 + a.getValue();
            }
        }
        Integer value_9_2 = 0;
        if (list9_2.size() > 0) {
            for (ResultVo a : list9_2) {
                if (null!=a.getValue())
                value_9_2 = value_9_2 + a.getValue();
            }
        }
        Integer value_9_3 = 0;
        if (list9_3.size() > 0) {
            for (ResultVo a : list9_3) {
                if (null!=a.getValue())
                value_9_3 = value_9_3 + a.getValue();
            }
        }
        Integer value_9_4 = 0;
        if (list9_4.size() > 0) {
            for (ResultVo a : list9_4) {
                if (null!=a.getValue())
                value_9_4 = value_9_4 + a.getValue();
            }
        }

        Integer value_10_1 = 0;
        if (list10_1.size() > 0) {
            for (ResultVo a : list10_1) {
                if (null!=a.getValue())
                value_10_1 = value_10_1 + a.getValue();
            }
        }
        Integer value_10_2 = 0;
        if (list10_2.size() > 0) {
            for (ResultVo a : list10_2) {
                if (null!=a.getValue())
                value_10_2 = value_10_2 + a.getValue();
            }
        }
        Integer value_10_3 = 0;
        if (list10_3.size() > 0) {
            for (ResultVo a : list10_3) {
                if (null!=a.getValue())
                value_10_3 = value_10_3 + a.getValue();
            }
        }
        Integer value_10_4 = 0;
        if (list10_4.size() > 0) {
            for (ResultVo a : list10_4) {
                if (null!=a.getValue())
                value_10_4 = value_10_4 + a.getValue();
            }
        }

        Integer value_11_1 = 0;
        if (list11_1.size() > 0) {
            for (ResultVo a : list11_1) {
                value_11_1 = value_11_1 + a.getValue();
            }
        }
        Integer value_11_2 = 0;
        if (list11_2.size() > 0) {
            for (ResultVo a : list11_2) {
                if (null!=a.getValue())
                value_11_2 = value_11_2 + a.getValue();
            }
        }
        Integer value_11_3 = 0;
        if (list11_3.size() > 0) {
            for (ResultVo a : list11_3) {
                value_11_3 = value_11_3 + a.getValue();
            }
        }
        Integer value_11_4 = 0;
        if (list11_4.size() > 0) {
            for (ResultVo a : list11_4) {
                if (null!=a.getValue())
                value_11_4 = value_11_4 + a.getValue();
            }
        }

        Integer value_12_1 = 0;
        if (list12_1.size() > 0) {
            for (ResultVo a : list12_1) {
                if (null!=a.getValue())
                value_12_1 = value_12_1 + a.getValue();
            }
        }
        Integer value_12_2 = 0;
        if (list12_2.size() > 0) {
            for (ResultVo a : list12_2) {
                value_12_2 = value_12_2 + a.getValue();
            }
        }
        Integer value_12_3 = 0;
        if (list12_3.size() > 0) {
            for (ResultVo a : list12_3) {
                if (null!=a.getValue())
                value_12_3 = value_12_3 + a.getValue();
            }
        }
        Integer value_12_4 = 0;
        if (list12_4.size() > 0) {
            for (ResultVo a : list12_4) {
                if (null!=a.getValue())
                value_12_4 = value_12_4 + a.getValue();
            }
        }

        Integer value_13_1 = 0;
        if (list13_1.size() > 0) {
            for (ResultVo a : list13_1) {
                if (null!=a.getValue())
                value_13_1 = value_13_1 + a.getValue();
            }
        }
        Integer value_13_2 = 0;
        if (list13_2.size() > 0) {
            for (ResultVo a : list13_2) {
                if (null!=a.getValue())
                value_13_2 = value_13_2 + a.getValue();
            }
        }
        Integer value_13_3 = 0;
        if (list13_3.size() > 0) {
            for (ResultVo a : list13_3) {
                if (null!=a.getValue())
                value_13_3 = value_13_3 + a.getValue();
            }
        }
        Integer value_13_4 = 0;
        if (list13_4.size() > 0) {
            for (ResultVo a : list13_4) {
                if (null!=a.getValue())
                value_13_4 = value_13_4 + a.getValue();
            }
        }

        Integer value_14_1 = 0;
        if (list14_1.size() > 0) {
            for (ResultVo a : list14_1) {
                if (null!=a.getValue())
                value_14_1 = value_14_1 + a.getValue();
            }
        }
        Integer value_14_2 = 0;
        if (list14_2.size() > 0) {
            for (ResultVo a : list14_2) {
                value_14_2 = value_14_2 + a.getValue();
            }
        }
        Integer value_14_3 = 0;
        if (list14_3.size() > 0) {
            for (ResultVo a : list14_3) {
                if (null!=a.getValue())
                value_14_3 = value_14_3 + a.getValue();
            }
        }
        Integer value_14_4 = 0;
        if (list14_4.size() > 0) {
            for (ResultVo a : list14_4) {
                if (null!=a.getValue())
                value_14_4 = value_14_4 + a.getValue();
            }
        }

        Integer value_15_1 = 0;
        if (list15_1.size() > 0) {
            for (ResultVo a : list15_1) {
                if (null!=a.getValue())
                value_15_1 = value_15_1 + a.getValue();
            }
        }
        Integer value_15_2 = 0;
        if (list15_2.size() > 0) {
            for (ResultVo a : list15_2) {
                value_15_2 = value_15_2 + a.getValue();
            }
        }
        Integer value_15_3 = 0;
        if (list15_3.size() > 0) {
            for (ResultVo a : list15_3) {
                if (null!=a.getValue())
                value_15_3 = value_15_3 + a.getValue();
            }
        }
        Integer value_15_4 = 0;
        if (list15_4.size() > 0) {
            for (ResultVo a : list15_4) {
                if (null!=a.getValue())
                value_15_4 = value_15_4 + a.getValue();
            }
        }

        Integer value_16_1 = 0;
        if (list16_1.size() > 0) {
            for (ResultVo a : list16_1) {
                if (null!=a.getValue())
                value_16_1 = value_16_1 + a.getValue();
            }
        }
        Integer value_16_2 = 0;
        if (list16_2.size() > 0) {
            for (ResultVo a : list16_2) {
                if (null!=a.getValue())
                value_16_2 = value_16_2 + a.getValue();
            }
        }
        Integer value_16_3 = 0;
        if (list16_3.size() > 0) {
            for (ResultVo a : list16_3) {
                if (null!=a.getValue())
                value_16_3 = value_16_3 + a.getValue();
            }
        }
        Integer value_16_4 = 0;
        if (list16_4.size() > 0) {
            for (ResultVo a : list16_4) {
                if (null!=a.getValue())
                value_16_4 = value_16_4 + a.getValue();
            }
        }


        Integer value_17_1 = 0;
        if (list17_1.size() > 0) {
            for (ResultVo a : list17_1) {
                if (null!=a.getValue())
                value_17_1 = value_17_1 + a.getValue();
            }
        }
        Integer value_17_2 = 0;
        if (list17_2.size() > 0) {
            for (ResultVo a : list17_2) {
                if (null!=a.getValue())
                value_17_2 = value_17_2 + a.getValue();
            }
        }
        Integer value_17_3 = 0;
        if (list17_3.size() > 0) {
            for (ResultVo a : list17_3) {
                if (null!=a.getValue())
                value_17_3 = value_17_3 + a.getValue();
            }
        }
        Integer value_17_4 = 0;
        if (list17_4.size() > 0) {
            for (ResultVo a : list17_4) {
                if (null!=a.getValue())
                value_17_4 = value_17_4 + a.getValue();
            }
        }

        Integer value_18_1 = 0;
        if (list18_1.size() > 0) {
            for (ResultVo a : list18_1) {
                if (null!=a.getValue())
                value_18_1 = value_18_1 + a.getValue();
            }
        }
        Integer value_18_2 = 0;
        if (list18_2.size() > 0) {
            for (ResultVo a : list18_2) {
                if (null!=a.getValue())
                value_18_2 = value_18_2 + a.getValue();
            }
        }
        Integer value_18_3 = 0;
        if (list18_3.size() > 0) {
            for (ResultVo a : list18_3) {
                if (null!=a.getValue())
                value_18_3 = value_18_3 + a.getValue();
            }
        }
        Integer value_18_4 = 0;
        if (list18_4.size() > 0) {
            for (ResultVo a : list18_4) {
                if (null!=a.getValue())
                value_18_4 = value_18_4 + a.getValue();
            }
        }

        Integer value_19_1 = 0;
        if (list19_1.size() > 0) {
            for (ResultVo a : list19_1) {
                if (null!=a.getValue())
                value_19_1 = value_19_1 + a.getValue();
            }
        }
        Integer value_19_2 = 0;
        if (list19_2.size() > 0) {
            for (ResultVo a : list19_2) {
                if (null!=a.getValue())
                value_19_2 = value_19_2 + a.getValue();
            }
        }
        Integer value_19_3 = 0;
        if (list19_3.size() > 0) {
            for (ResultVo a : list19_3) {
                if (null!=a.getValue())
                value_19_3 = value_19_3 + a.getValue();
            }
        }
        Integer value_19_4 = 0;
        if (list19_4.size() > 0) {
            for (ResultVo a : list19_4) {
                if (null!=a.getValue())
                value_19_4 = value_19_4 + a.getValue();
            }
        }

        Integer value_20_1 = 0;
        if (list20_1.size() > 0) {
            for (ResultVo a : list20_1) {
                if (null!=a.getValue())
                value_20_1 = value_20_1 + a.getValue();
            }
        }
        Integer value_20_2 = 0;
        if (list20_2.size() > 0) {
            for (ResultVo a : list20_2) {
                if (null!=a.getValue())
                value_20_2 = value_20_2 + a.getValue();
            }
        }
        Integer value_20_3 = 0;
        if (list20_3.size() > 0) {
            for (ResultVo a : list20_3) {
                if (null!=a.getValue())
                value_20_3 = value_20_3 + a.getValue();
            }
        }
        Integer value_20_4 = 0;
        if (list20_4.size() > 0) {
            for (ResultVo a : list20_4) {
                if (null!=a.getValue())
                value_20_4 = value_20_4 + a.getValue();
            }
        }


        Integer value_21_1 = 0;
        if (list21_1.size() > 0) {
            for (ResultVo a : list21_1) {
                if (null!=a.getValue())
                value_21_1 = value_21_1 + a.getValue();
            }
        }
        Integer value_21_2 = 0;
        if (list21_2.size() > 0) {
            for (ResultVo a : list21_2) {
                if (null!=a.getValue())
                value_21_2 = value_21_2 + a.getValue();
            }
        }
        Integer value_21_3 = 0;
        if (list21_3.size() > 0) {
            for (ResultVo a : list21_3) {
                if (null!=a.getValue())
                value_21_3 = value_21_3 + a.getValue();
            }
        }
        Integer value_21_4 = 0;
        if (list21_4.size() > 0) {
            for (ResultVo a : list21_4) {
                if (null!=a.getValue())
                value_21_4 = value_21_4 + a.getValue();
            }
        }

        Integer value_22_1 = 0;
        if (list22_1.size() > 0) {
            for (ResultVo a : list22_1) {
                if (null!=a.getValue())
                value_22_1 = value_22_1 + a.getValue();
            }
        }
        Integer value_22_2 = 0;
        if (list22_2.size() > 0) {
            for (ResultVo a : list22_2) {
                if (null!=a.getValue())
                value_22_2 = value_22_2 + a.getValue();
            }
        }
        Integer value_22_3 = 0;
        if (list22_3.size() > 0) {
            for (ResultVo a : list22_3) {
                if (null!=a.getValue())
                value_22_3 = value_22_3 + a.getValue();
            }
        }
        Integer value_22_4 = 0;
        if (list22_4.size() > 0) {
            for (ResultVo a : list22_4) {
                if (null!=a.getValue())
                value_22_4 = value_22_4 + a.getValue();
            }
        }

        //输出
        JSONArray jar = new JSONArray();

        JSONObject jb1 = new JSONObject();
        JSONObject jb2 = new JSONObject();
        JSONObject jb3 = new JSONObject();
        JSONObject jb4 = new JSONObject();
        JSONObject jb5 = new JSONObject();
        JSONObject jb6 = new JSONObject();
        JSONObject jb7 = new JSONObject();
        JSONObject jb8 = new JSONObject();
        JSONObject jb9 = new JSONObject();
        JSONObject jb10 = new JSONObject();
        JSONObject jb11 = new JSONObject();
        JSONObject jb12 = new JSONObject();
        JSONObject jb13 = new JSONObject();
        JSONObject jb14 = new JSONObject();
        JSONObject jb15 = new JSONObject();
        JSONObject jb16 = new JSONObject();
        JSONObject jb17 = new JSONObject();
        JSONObject jb18 = new JSONObject();
        JSONObject jb19 = new JSONObject();
        JSONObject jb20 = new JSONObject();
        JSONObject jb21 = new JSONObject();
        JSONObject jb22 = new JSONObject();

        //封装对象
        //职能发挥
        //工作效率
        //服务质量
        //服务态度
        jb1.put("department", "公司业务部");
        jb1.put("v1", value_1_1);
        jb1.put("v2", value_1_2);
        jb1.put("v3", value_1_3);
        jb1.put("v4", value_1_4);
        jb1.put("number", list1_1.size());

        jb2.put("department", "三农业务部");
        jb2.put("v1", value_2_1);
        jb2.put("v2", value_2_2);
        jb2.put("v3", value_2_3);
        jb2.put("v4", value_2_4);
        jb2.put("number", list2_1.size());

        jb3.put("department", "电子银行部");
        jb3.put("v1", value_3_1);
        jb3.put("v2", value_3_2);
        jb3.put("v3", value_3_3);
        jb3.put("v4", value_3_4);
        jb3.put("number", list3_1.size());

        jb4.put("department", "资金业务部");
        jb4.put("v1", value_4_1);
        jb4.put("v2", value_4_2);
        jb4.put("v3", value_4_3);
        jb4.put("v4", value_4_4);
        jb4.put("number", list4_1.size());

        jb5.put("department", "国际业务部");
        jb5.put("v1", value_5_1);
        jb5.put("v2", value_5_2);
        jb5.put("v3", value_5_3);
        jb5.put("v4", value_5_4);
        jb5.put("number", list5_1.size());

        jb6.put("department", "风险管理部");
        jb6.put("v1", value_6_1);
        jb6.put("v2", value_6_2);
        jb6.put("v3", value_6_3);
        jb6.put("v4", value_6_4);
        jb6.put("number", list6_1.size());

        jb7.put("department", "运营管理部");
        jb7.put("v1", value_7_1);
        jb7.put("v2", value_7_2);
        jb7.put("v3", value_7_3);
        jb7.put("v4", value_7_4);
        jb7.put("number", list7_1.size());

        jb8.put("department", "合规管理部");
        jb8.put("v1", value_8_1);
        jb8.put("v2", value_8_2);
        jb8.put("v3", value_8_3);
        jb8.put("v4", value_8_4);
        jb8.put("number", list8_1.size());

        jb9.put("department", "计划财务部");
        jb9.put("v1", value_9_1);
        jb9.put("v2", value_9_2);
        jb9.put("v3", value_9_3);
        jb9.put("v4", value_9_4);
        jb9.put("number", list9_1.size());

        jb10.put("department", "信贷管理部");
        jb10.put("v1", value_10_1);
        jb10.put("v2", value_10_2);
        jb10.put("v3", value_10_3);
        jb10.put("v4", value_10_4);
        jb10.put("number", list10_1.size());

        jb11.put("department", "科技信息部");
        jb11.put("v1", value_11_1);
        jb11.put("v2", value_11_2);
        jb11.put("v3", value_11_3);
        jb11.put("v4", value_11_4);
        jb11.put("number", list11_1.size());

        jb12.put("department", "人力资源部");
        jb12.put("v1", value_12_1);
        jb12.put("v2", value_12_2);
        jb12.put("v3", value_12_3);
        jb12.put("v4", value_12_4);
        jb12.put("number", list12_1.size());

        jb13.put("department", "行长办公室");
        jb13.put("v1", value_13_1);
        jb13.put("v2", value_13_2);
        jb13.put("v3", value_13_3);
        jb13.put("v4", value_13_4);
        jb13.put("number", list13_1.size());

        jb14.put("department", "后勤保障部");
        jb14.put("v1", value_14_1);
        jb14.put("v2", value_14_2);
        jb14.put("v3", value_14_3);
        jb14.put("v4", value_14_4);
        jb14.put("number", list14_1.size());

        jb15.put("department", "安全保卫部");
        jb15.put("v1", value_15_1);
        jb15.put("v2", value_15_2);
        jb15.put("v3", value_15_3);
        jb15.put("v4", value_15_4);
        jb15.put("number", list15_1.size());

        jb16.put("department", "稽核审计部");
        jb16.put("v1", value_16_1);
        jb16.put("v2", value_16_2);
        jb16.put("v3", value_16_3);
        jb16.put("v4", value_16_4);
        jb16.put("number", list16_1.size());

        jb17.put("department", "调查统计部");
        jb17.put("v1", value_17_1);
        jb17.put("v2", value_17_2);
        jb17.put("v3", value_17_3);
        jb17.put("v4", value_17_4);
        jb17.put("number", list17_1.size());

        jb18.put("department", "纪检监察部");
        jb18.put("v1", value_18_1);
        jb18.put("v2", value_18_2);
        jb18.put("v3", value_18_3);
        jb18.put("v4", value_18_4);
        jb18.put("number", list18_1.size());

        jb19.put("department", "董事会办公室");
        jb19.put("v1", value_19_1);
        jb19.put("v2", value_19_2);
        jb19.put("v3", value_19_3);
        jb19.put("v4", value_19_4);
        jb19.put("number", list19_1.size());

        jb20.put("department", "监事会办公室");
        jb20.put("v1", value_20_1);
        jb20.put("v2", value_20_2);
        jb20.put("v3", value_20_3);
        jb20.put("v4", value_20_4);
        jb20.put("number", list20_1.size());

        jb21.put("department", "党群工作部");
        jb21.put("v1", value_21_1);
        jb21.put("v2", value_21_2);
        jb21.put("v3", value_21_3);
        jb21.put("v4", value_21_4);
        jb21.put("number", list21_1.size());

        jb22.put("department", "工会");
        jb22.put("v1", value_22_1);
        jb22.put("v2", value_22_2);
        jb22.put("v3", value_22_3);
        jb22.put("v4", value_22_4);
        jb22.put("number", list22_1.size());

        //加入到list当中
        jar.add(jb1);
        jar.add(jb2);
        jar.add(jb3);
        jar.add(jb4);
        jar.add(jb5);
        jar.add(jb6);
        jar.add(jb7);
        jar.add(jb8);
        jar.add(jb9);
        jar.add(jb10);
        jar.add(jb11);
        jar.add(jb12);
        jar.add(jb13);
        jar.add(jb14);
        jar.add(jb15);
        jar.add(jb16);
        jar.add(jb17);
        jar.add(jb18);
        jar.add(jb19);
        jar.add(jb20);
        jar.add(jb21);
        jar.add(jb22);
        return jar;

    }
}

package com.minxing365.vote.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.minxing365.vote.dao.VotingDetailsMapper;
import com.minxing365.vote.pojo.ResultVo;
import com.minxing365.vote.pojo.VoteDetailsVo;
import com.minxing365.vote.pojo.excel.CountExcel;
import com.minxing365.vote.service.VotingDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class VotingDetailsServiceImpl implements VotingDetailsService {
    private Logger log = LoggerFactory.getLogger(VotingDetailsServiceImpl.class);
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
    public List<CountExcel> selectVotingDetails2ByName() throws UnsupportedEncodingException {
        name = "2019双测双评民主评测";
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
        List<CountExcel> groupList = null;
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
    private List<CountExcel> getGroup(List<ResultVo> resultVoList) {
        //公司业务部 //三农业务部
        //党群工作部
        List<ResultVo> list1_1 = new ArrayList<>();
        List<ResultVo> list1_2 = new ArrayList<>();
        List<ResultVo> list1_3 = new ArrayList<>();
        List<ResultVo> list1_4 = new ArrayList<>();
        //董事会办公室
        List<ResultVo> list2_1 = new ArrayList<>();
        List<ResultVo> list2_2 = new ArrayList<>();
        List<ResultVo> list2_3 = new ArrayList<>();
        List<ResultVo> list2_4 = new ArrayList<>();
        //行长办公室
        List<ResultVo> list3_1 = new ArrayList<>();
        List<ResultVo> list3_2 = new ArrayList<>();
        List<ResultVo> list3_3 = new ArrayList<>();
        List<ResultVo> list3_4 = new ArrayList<>();
        //零售业务营销部（普惠金融部）
        List<ResultVo> list4_1 = new ArrayList<>();
        List<ResultVo> list4_2 = new ArrayList<>();
        List<ResultVo> list4_3 = new ArrayList<>();
        List<ResultVo> list4_4 = new ArrayList<>();
        //零售业务管理部
        List<ResultVo> list5_1 = new ArrayList<>();
        List<ResultVo> list5_2 = new ArrayList<>();
        List<ResultVo> list5_3 = new ArrayList<>();
        List<ResultVo> list5_4 = new ArrayList<>();
        //战略客户与投资银行部
        List<ResultVo> list6_1 = new ArrayList<>();
        List<ResultVo> list6_2 = new ArrayList<>();
        List<ResultVo> list6_3 = new ArrayList<>();
        List<ResultVo> list6_4 = new ArrayList<>();
        //7.公司业务管理部
        List<ResultVo> list7_1 = new ArrayList<>();
        List<ResultVo> list7_2 = new ArrayList<>();
        List<ResultVo> list7_3 = new ArrayList<>();
        List<ResultVo> list7_4 = new ArrayList<>();
        //8.交易银行部
        List<ResultVo> list8_1 = new ArrayList<>();
        List<ResultVo> list8_2 = new ArrayList<>();
        List<ResultVo> list8_3 = new ArrayList<>();
        List<ResultVo> list8_4 = new ArrayList<>();
        //9.互联网金融部（电子银行部）
        List<ResultVo> list9_1 = new ArrayList<>();
        List<ResultVo> list9_2 = new ArrayList<>();
        List<ResultVo> list9_3 = new ArrayList<>();
        List<ResultVo> list9_4 = new ArrayList<>();
        //10.监事会办公室
        List<ResultVo> list10_1 = new ArrayList<>();
        List<ResultVo> list10_2 = new ArrayList<>();
        List<ResultVo> list10_3 = new ArrayList<>();
        List<ResultVo> list10_4 = new ArrayList<>();
        //11.金融同业部
        List<ResultVo> list11_1 = new ArrayList<>();
        List<ResultVo> list11_2 = new ArrayList<>();
        List<ResultVo> list11_3 = new ArrayList<>();
        List<ResultVo> list11_4 = new ArrayList<>();
        //12.金融市场部
        List<ResultVo> list12_1 = new ArrayList<>();
        List<ResultVo> list12_2 = new ArrayList<>();
        List<ResultVo> list12_3 = new ArrayList<>();
        List<ResultVo> list12_4 = new ArrayList<>();
        //13.资产管理部
        List<ResultVo> list13_1 = new ArrayList<>();
        List<ResultVo> list13_2 = new ArrayList<>();
        List<ResultVo> list13_3 = new ArrayList<>();
        List<ResultVo> list13_4 = new ArrayList<>();
        //14.资金业务管理部
        List<ResultVo> list14_1 = new ArrayList<>();
        List<ResultVo> list14_2 = new ArrayList<>();
        List<ResultVo> list14_3 = new ArrayList<>();
        List<ResultVo> list14_4 = new ArrayList<>();
        //15.授信审批部
        List<ResultVo> list15_1 = new ArrayList<>();
        List<ResultVo> list15_2 = new ArrayList<>();
        List<ResultVo> list15_3 = new ArrayList<>();
        List<ResultVo> list15_4 = new ArrayList<>();
        //16.信贷管理部
        List<ResultVo> list16_1 = new ArrayList<>();
        List<ResultVo> list16_2 = new ArrayList<>();
        List<ResultVo> list16_3 = new ArrayList<>();
        List<ResultVo> list16_4 = new ArrayList<>();
        //17.风险管理部
        List<ResultVo> list17_1 = new ArrayList<>();
        List<ResultVo> list17_2 = new ArrayList<>();
        List<ResultVo> list17_3 = new ArrayList<>();
        List<ResultVo> list17_4 = new ArrayList<>();
        //18.合规管理部
        List<ResultVo> list18_1 = new ArrayList<>();
        List<ResultVo> list18_2 = new ArrayList<>();
        List<ResultVo> list18_3 = new ArrayList<>();
        List<ResultVo> list18_4 = new ArrayList<>();
        //19.计划财务部
        List<ResultVo> list19_1 = new ArrayList<>();
        List<ResultVo> list19_2 = new ArrayList<>();
        List<ResultVo> list19_3 = new ArrayList<>();
        List<ResultVo> list19_4 = new ArrayList<>();
        //20.资产负债管理部
        List<ResultVo> list20_1 = new ArrayList<>();
        List<ResultVo> list20_2 = new ArrayList<>();
        List<ResultVo> list20_3 = new ArrayList<>();
        List<ResultVo> list20_4 = new ArrayList<>();
        //21.运营管理部
        List<ResultVo> list21_1 = new ArrayList<>();
        List<ResultVo> list21_2 = new ArrayList<>();
        List<ResultVo> list21_3 = new ArrayList<>();
        List<ResultVo> list21_4 = new ArrayList<>();
        //22.信息技术部
        List<ResultVo> list22_1 = new ArrayList<>();
        List<ResultVo> list22_2 = new ArrayList<>();
        List<ResultVo> list22_3 = new ArrayList<>();
        List<ResultVo> list22_4 = new ArrayList<>();
        //23.人力资源部
        List<ResultVo> list23_1 = new ArrayList<>();
        List<ResultVo> list23_2 = new ArrayList<>();
        List<ResultVo> list23_3 = new ArrayList<>();
        List<ResultVo> list23_4 = new ArrayList<>();
        //24.安全保障部
        List<ResultVo> list24_1 = new ArrayList<>();
        List<ResultVo> list24_2 = new ArrayList<>();
        List<ResultVo> list24_3 = new ArrayList<>();
        List<ResultVo> list24_4 = new ArrayList<>();
        //25.消费者权益保护部
        List<ResultVo> list25_1 = new ArrayList<>();
        List<ResultVo> list25_2 = new ArrayList<>();
        List<ResultVo> list25_3 = new ArrayList<>();
        List<ResultVo> list25_4 = new ArrayList<>();
        //26.数据分析部
        List<ResultVo> list26_1 = new ArrayList<>();
        List<ResultVo> list26_2 = new ArrayList<>();
        List<ResultVo> list26_3 = new ArrayList<>();
        List<ResultVo> list26_4 = new ArrayList<>();
        //27.纪检监察部
        List<ResultVo> list27_1 = new ArrayList<>();
        List<ResultVo> list27_2 = new ArrayList<>();
        List<ResultVo> list27_3 = new ArrayList<>();
        List<ResultVo> list27_4 = new ArrayList<>();
        //28.稽核审计部
        List<ResultVo> list28_1 = new ArrayList<>();
        List<ResultVo> list28_2 = new ArrayList<>();
        List<ResultVo> list28_3 = new ArrayList<>();
        List<ResultVo> list28_4 = new ArrayList<>();
        //29.工会办公室
        List<ResultVo> list29_1 = new ArrayList<>();
        List<ResultVo> list29_2 = new ArrayList<>();
        List<ResultVo> list29_3 = new ArrayList<>();
        List<ResultVo> list29_4 = new ArrayList<>();
//        //30.公司业务部
//        List<ResultVo> list30_1 = new ArrayList<>();
//        List<ResultVo> list30_2 = new ArrayList<>();
//        List<ResultVo> list30_3 = new ArrayList<>();
//        List<ResultVo> list30_4 = new ArrayList<>();

        for (int i = 0; i < resultVoList.size(); i++) {
            String auditStandard = resultVoList.get(i).getAuditStandard();

            String department = resultVoList.get(i).getDepartment();
            //分组
            //  log.info("-----auditStandard"+auditStandard+";department="+department);
            if (auditStandard.equals("职能发挥") && department.equals("党群工作部")) {
              //  log.info("党群工作部:职能发挥="+resultVoList.get(i));
                list1_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("党群工作部")) {
             //   log.info("党群工作部:工作效率="+resultVoList.get(i));
                list1_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("党群工作部")) {
              //  log.info("党群工作部:服务质量="+resultVoList.get(i));
                list1_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("党群工作部")) {
              //  log.info("党群工作部:服务态度="+resultVoList.get(i));
                list1_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("董事会办公室")) {
                list2_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("董事会办公室")) {
                list2_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("董事会办公室")) {
                list2_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("董事会办公室")) {
                list2_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("监事会办公室")) {
                list3_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("监事会办公室")) {
                list3_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("监事会办公室")) {
                list3_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("监事会办公室")) {
                list3_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("行长办公室")) {
                list4_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("行长办公室")) {
                list4_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("行长办公室")) {
                list4_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("行长办公室")) {
                list4_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("零售业务营销部（普惠金融部）")) {
                list5_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("零售业务营销部（普惠金融部）")) {
                list5_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("零售业务营销部（普惠金融部）")) {
                list5_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("零售业务营销部（普惠金融部）")) {
                list5_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("零售业务管理部")) {
                list6_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("零售业务管理部")) {
                list6_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("零售业务管理部")) {
                list6_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("零售业务管理部")) {
                list6_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("战略客户与投资银行部")) {
                list7_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("战略客户与投资银行部")) {
                list7_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("战略客户与投资银行部")) {
                list7_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("战略客户与投资银行部")) {
                list7_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("公司业务管理部")) {
                list8_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("公司业务管理部")) {
                list8_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("公司业务管理部")) {
                list8_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("公司业务管理部")) {
                list8_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("交易银行部")) {
                list9_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("交易银行部")) {
                list9_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("交易银行部")) {
                list9_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("交易银行部")) {
                list9_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("互联网金融部（电子银行部）")) {
                list10_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("互联网金融部（电子银行部）")) {
                list10_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("互联网金融部（电子银行部）")) {
                list10_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("互联网金融部（电子银行部）")) {
                list10_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("金融同业部")) {
                list11_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("金融同业部")) {
                list11_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("金融同业部")) {
                list11_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("金融同业部")) {
                list11_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("金融市场部")) {
                list12_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("金融市场部")) {
                list12_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("金融市场部")) {
                list12_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("金融市场部")) {
                list12_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("资产管理部")) {
                list13_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("资产管理部")) {
                list13_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("资产管理部")) {
                list13_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("资产管理部")) {
                list13_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("资金业务管理部")) {
                list14_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("资金业务管理部")) {
                list14_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("资金业务管理部")) {
                list14_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("资金业务管理部")) {
                list14_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("授信审批部")) {
                list15_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("授信审批部")) {
                list15_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("授信审批部")) {
                list15_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("授信审批部")) {
                list15_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("信贷管理部")) {
                list16_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("信贷管理部")) {
                list16_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("信贷管理部")) {
                list16_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("信贷管理部")) {
                list16_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("风险管理部")) {
                list17_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("风险管理部")) {
                list17_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("风险管理部")) {
                list17_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("风险管理部")) {
                list17_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("合规管理部")) {
                list18_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("合规管理部")) {
                list18_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("合规管理部")) {
                list18_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("合规管理部")) {
                list18_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("计划财务部")) {
                list19_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("计划财务部")) {
                list19_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("计划财务部")) {
                list19_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("计划财务部")) {
                list19_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("资产负债管理部")) {
                list20_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("资产负债管理部")) {
                list20_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("资产负债管理部")) {
                list20_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("资产负债管理部")) {
                list20_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("运营管理部")) {
                list21_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("运营管理部")) {
                list21_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("运营管理部")) {
                list21_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("运营管理部")) {
                list21_4.add(resultVoList.get(i));
            }


            if (auditStandard.equals("职能发挥") && department.equals("信息技术部")) {
                list22_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("信息技术部")) {
                list22_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("信息技术部")) {
                list22_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("信息技术部")) {
                list22_4.add(resultVoList.get(i));
            }
            //23
            if (auditStandard.equals("职能发挥") && department.equals("人力资源部")) {
                list23_1.add(resultVoList.get(i));
                log.info("人力资源部:职能发挥="+resultVoList.get(i).getValue());
            }
            if (auditStandard.equals("工作效率") && department.equals("人力资源部")) {
                list23_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("人力资源部")) {
                list23_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("人力资源部")) {
                list23_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("安全保障部")) {
                list24_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("安全保障部")) {
                list24_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("安全保障部")) {
                list24_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("安全保障部")) {
                list24_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("消费者权益保护部")) {
                list25_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("消费者权益保护部")) {
                list25_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("消费者权益保护部")) {
                list25_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("消费者权益保护部")) {
                list25_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("数据分析部")) {
                list26_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("数据分析部")) {
                list26_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("数据分析部")) {
                list26_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("数据分析部")) {
                list26_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("纪检监察部")) {
                list27_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("纪检监察部")) {
                list27_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("纪检监察部")) {
                list27_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("纪检监察部")) {
                list27_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("稽核审计部")) {
                list28_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("稽核审计部")) {
                list28_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("稽核审计部")) {
                list28_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("稽核审计部")) {
                list28_4.add(resultVoList.get(i));
            }

            if (auditStandard.equals("职能发挥") && department.equals("工会办公室")) {
                list29_1.add(resultVoList.get(i));
            }
            if (auditStandard.equals("工作效率") && department.equals("工会办公室")) {
                list29_2.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务质量") && department.equals("工会办公室")) {
                list29_3.add(resultVoList.get(i));
            }
            if (auditStandard.equals("服务态度") && department.equals("工会办公室")) {
                list29_4.add(resultVoList.get(i));
            }

            //公司业务部
//            if (auditStandard.equals("职能发挥") && department.equals("公司业务部")) {
//                list30_1.add(resultVoList.get(i));
//            }
//            if (auditStandard.equals("工作效率") && department.equals("公司业务部")) {
//                list30_2.add(resultVoList.get(i));
//            }
//            if (auditStandard.equals("服务质量") && department.equals("公司业务部")) {
//                list30_3.add(resultVoList.get(i));
//            }
//            if (auditStandard.equals("服务态度") && department.equals("公司业务部")) {
//                list30_4.add(resultVoList.get(i));
//            }

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
                if (null != a.getValue())
                    value_1_2 = value_1_2 + a.getValue();
            }
        }
        Integer value_1_3 = 0;
        if (list1_3.size() > 0) {
            for (ResultVo a : list1_3) {
                if (null != a.getValue())
                    value_1_3 = value_1_3 + a.getValue();
            }
        }
        Integer value_1_4 = 0;
        if (list1_4.size() > 0) {
            for (ResultVo a : list1_4) {
                if (null != a.getValue())
                    value_1_4 = value_1_4 + a.getValue();
            }
        }
        Integer value_2_1 = 0;
        if (list2_1.size() > 0) {
            for (ResultVo a : list2_1) {
                if (null != a.getValue())
                    value_2_1 = value_2_1 + a.getValue();
            }
        }
        Integer value_2_2 = 0;
        if (list2_2.size() > 0) {
            for (ResultVo a : list2_2) {
                if (null != a.getValue())
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
                if (null != a.getValue())
                    value_2_4 = value_2_4 + a.getValue();
            }
        }

        Integer value_3_1 = 0;
        if (list3_1.size() > 0) {
            for (ResultVo a : list3_1) {
                if (null != a.getValue())
                    value_3_1 = value_3_1 + a.getValue();
            }
        }
        Integer value_3_2 = 0;
        if (list3_2.size() > 0) {
            for (ResultVo a : list3_2) {
                if (null != a.getValue())
                    value_3_2 = value_3_2 + a.getValue();
            }
        }
        Integer value_3_3 = 0;
        if (list3_3.size() > 0) {
            for (ResultVo a : list3_3) {
                if (null != a.getValue())
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
                if (null != a.getValue())
                    value_4_1 = value_4_1 + a.getValue();
            }
        }
        Integer value_4_2 = 0;
        if (list4_2.size() > 0) {
            for (ResultVo a : list4_2) {
                if (null != a.getValue())
                    value_4_2 = value_4_2 + a.getValue();
            }
        }
        Integer value_4_3 = 0;
        if (list4_3.size() > 0) {
            for (ResultVo a : list4_3) {
                if (null != a.getValue())
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
                if (null != a.getValue())
                    value_5_1 = value_5_1 + a.getValue();
            }
        }
        Integer value_5_2 = 0;
        if (list5_2.size() > 0) {
            for (ResultVo a : list5_2) {
                if (null != a.getValue())
                    value_5_2 = value_5_2 + a.getValue();
            }
        }
        Integer value_5_3 = 0;
        if (list5_3.size() > 0) {
            for (ResultVo a : list5_3) {
                if (null != a.getValue())
                    value_5_3 = value_5_3 + a.getValue();
            }
        }
        Integer value_5_4 = 0;
        if (list5_4.size() > 0) {
            for (ResultVo a : list5_4) {
                if (null != a.getValue())
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
                if (null != a.getValue())
                    value_6_2 = value_6_2 + a.getValue();
            }
        }
        Integer value_6_3 = 0;
        if (list6_3.size() > 0) {
            for (ResultVo a : list6_3) {
                if (null != a.getValue())
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
                if (null != a.getValue())
                    value_7_1 = value_7_1 + a.getValue();
            }
        }
        Integer value_7_2 = 0;
        if (list7_2.size() > 0) {
            for (ResultVo a : list7_2) {
                if (null != a.getValue())
                    value_7_2 = value_7_2 + a.getValue();
            }
        }
        Integer value_7_3 = 0;
        if (list7_3.size() > 0) {
            for (ResultVo a : list7_3) {
                if (null != a.getValue())
                    value_7_3 = value_7_3 + a.getValue();
            }
        }
        Integer value_7_4 = 0;
        if (list7_4.size() > 0) {
            for (ResultVo a : list7_4) {
                if (null != a.getValue())
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
                if (null != a.getValue())
                    value_8_2 = value_8_2 + a.getValue();
            }
        }
        Integer value_8_3 = 0;
        if (list8_3.size() > 0) {
            for (ResultVo a : list8_3) {
                if (null != a.getValue())
                    value_8_3 = value_8_3 + a.getValue();
            }
        }
        Integer value_8_4 = 0;
        if (list8_4.size() > 0) {
            for (ResultVo a : list8_4) {
                if (null != a.getValue())
                    value_8_4 = value_8_4 + a.getValue();
            }
        }

        Integer value_9_1 = 0;
        if (list9_1.size() > 0) {
            for (ResultVo a : list9_1) {
                if (null != a.getValue())
                    value_9_1 = value_9_1 + a.getValue();
            }
        }
        Integer value_9_2 = 0;
        if (list9_2.size() > 0) {
            for (ResultVo a : list9_2) {
                if (null != a.getValue())
                    value_9_2 = value_9_2 + a.getValue();
            }
        }
        Integer value_9_3 = 0;
        if (list9_3.size() > 0) {
            for (ResultVo a : list9_3) {
                if (null != a.getValue())
                    value_9_3 = value_9_3 + a.getValue();
            }
        }
        Integer value_9_4 = 0;
        if (list9_4.size() > 0) {
            for (ResultVo a : list9_4) {
                if (null != a.getValue())
                    value_9_4 = value_9_4 + a.getValue();
            }
        }

        Integer value_10_1 = 0;
        if (list10_1.size() > 0) {
            for (ResultVo a : list10_1) {
                if (null != a.getValue())
                    value_10_1 = value_10_1 + a.getValue();
            }
        }
        Integer value_10_2 = 0;
        if (list10_2.size() > 0) {
            for (ResultVo a : list10_2) {
                if (null != a.getValue())
                    value_10_2 = value_10_2 + a.getValue();
            }
        }
        Integer value_10_3 = 0;
        if (list10_3.size() > 0) {
            for (ResultVo a : list10_3) {
                if (null != a.getValue())
                    value_10_3 = value_10_3 + a.getValue();
            }
        }
        Integer value_10_4 = 0;
        if (list10_4.size() > 0) {
            for (ResultVo a : list10_4) {
                if (null != a.getValue())
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
                if (null != a.getValue())
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
                if (null != a.getValue())
                    value_11_4 = value_11_4 + a.getValue();
            }
        }

        Integer value_12_1 = 0;
        if (list12_1.size() > 0) {
            for (ResultVo a : list12_1) {
                if (null != a.getValue())
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
                if (null != a.getValue())
                    value_12_3 = value_12_3 + a.getValue();
            }
        }
        Integer value_12_4 = 0;
        if (list12_4.size() > 0) {
            for (ResultVo a : list12_4) {
                if (null != a.getValue())
                    value_12_4 = value_12_4 + a.getValue();
            }
        }

        Integer value_13_1 = 0;
        if (list13_1.size() > 0) {
            for (ResultVo a : list13_1) {
                if (null != a.getValue())
                    value_13_1 = value_13_1 + a.getValue();
            }
        }
        Integer value_13_2 = 0;
        if (list13_2.size() > 0) {
            for (ResultVo a : list13_2) {
                if (null != a.getValue())
                    value_13_2 = value_13_2 + a.getValue();
            }
        }
        Integer value_13_3 = 0;
        if (list13_3.size() > 0) {
            for (ResultVo a : list13_3) {
                if (null != a.getValue())
                    value_13_3 = value_13_3 + a.getValue();
            }
        }
        Integer value_13_4 = 0;
        if (list13_4.size() > 0) {
            for (ResultVo a : list13_4) {
                if (null != a.getValue())
                    value_13_4 = value_13_4 + a.getValue();
            }
        }

        Integer value_14_1 = 0;
        if (list14_1.size() > 0) {
            for (ResultVo a : list14_1) {
                if (null != a.getValue())
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
                if (null != a.getValue())
                    value_14_3 = value_14_3 + a.getValue();
            }
        }
        Integer value_14_4 = 0;
        if (list14_4.size() > 0) {
            for (ResultVo a : list14_4) {
                if (null != a.getValue())
                    value_14_4 = value_14_4 + a.getValue();
            }
        }

        Integer value_15_1 = 0;
        if (list15_1.size() > 0) {
            for (ResultVo a : list15_1) {
                if (null != a.getValue())
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
                if (null != a.getValue())
                    value_15_3 = value_15_3 + a.getValue();
            }
        }
        Integer value_15_4 = 0;
        if (list15_4.size() > 0) {
            for (ResultVo a : list15_4) {
                if (null != a.getValue())
                    value_15_4 = value_15_4 + a.getValue();
            }
        }

        Integer value_16_1 = 0;
        if (list16_1.size() > 0) {
            for (ResultVo a : list16_1) {
                if (null != a.getValue())
                    value_16_1 = value_16_1 + a.getValue();
            }
        }
        Integer value_16_2 = 0;
        if (list16_2.size() > 0) {
            for (ResultVo a : list16_2) {
                if (null != a.getValue())
                    value_16_2 = value_16_2 + a.getValue();
            }
        }
        Integer value_16_3 = 0;
        if (list16_3.size() > 0) {
            for (ResultVo a : list16_3) {
                if (null != a.getValue())
                    value_16_3 = value_16_3 + a.getValue();
            }
        }
        Integer value_16_4 = 0;
        if (list16_4.size() > 0) {
            for (ResultVo a : list16_4) {
                if (null != a.getValue())
                    value_16_4 = value_16_4 + a.getValue();
            }
        }


        Integer value_17_1 = 0;
        if (list17_1.size() > 0) {
            for (ResultVo a : list17_1) {
                if (null != a.getValue())
                    value_17_1 = value_17_1 + a.getValue();
            }
        }
        Integer value_17_2 = 0;
        if (list17_2.size() > 0) {
            for (ResultVo a : list17_2) {
                if (null != a.getValue())
                    value_17_2 = value_17_2 + a.getValue();
            }
        }
        Integer value_17_3 = 0;
        if (list17_3.size() > 0) {
            for (ResultVo a : list17_3) {
                if (null != a.getValue())
                    value_17_3 = value_17_3 + a.getValue();
            }
        }
        Integer value_17_4 = 0;
        if (list17_4.size() > 0) {
            for (ResultVo a : list17_4) {
                if (null != a.getValue())
                    value_17_4 = value_17_4 + a.getValue();
            }
        }

        Integer value_18_1 = 0;
        if (list18_1.size() > 0) {
            for (ResultVo a : list18_1) {
                if (null != a.getValue())
                    value_18_1 = value_18_1 + a.getValue();
            }
        }
        Integer value_18_2 = 0;
        if (list18_2.size() > 0) {
            for (ResultVo a : list18_2) {
                if (null != a.getValue())
                    value_18_2 = value_18_2 + a.getValue();
            }
        }
        Integer value_18_3 = 0;
        if (list18_3.size() > 0) {
            for (ResultVo a : list18_3) {
                if (null != a.getValue())
                    value_18_3 = value_18_3 + a.getValue();
            }
        }
        Integer value_18_4 = 0;
        if (list18_4.size() > 0) {
            for (ResultVo a : list18_4) {
                if (null != a.getValue())
                    value_18_4 = value_18_4 + a.getValue();
            }
        }

        Integer value_19_1 = 0;
        if (list19_1.size() > 0) {
            for (ResultVo a : list19_1) {
                if (null != a.getValue())
                    value_19_1 = value_19_1 + a.getValue();
            }
        }
        Integer value_19_2 = 0;
        if (list19_2.size() > 0) {
            for (ResultVo a : list19_2) {
                if (null != a.getValue())
                    value_19_2 = value_19_2 + a.getValue();
            }
        }
        Integer value_19_3 = 0;
        if (list19_3.size() > 0) {
            for (ResultVo a : list19_3) {
                if (null != a.getValue())
                    value_19_3 = value_19_3 + a.getValue();
            }
        }
        Integer value_19_4 = 0;
        if (list19_4.size() > 0) {
            for (ResultVo a : list19_4) {
                if (null != a.getValue())
                    value_19_4 = value_19_4 + a.getValue();
            }
        }

        Integer value_20_1 = 0;
        if (list20_1.size() > 0) {
            for (ResultVo a : list20_1) {
                if (null != a.getValue())
                    value_20_1 = value_20_1 + a.getValue();
            }
        }
        Integer value_20_2 = 0;
        if (list20_2.size() > 0) {
            for (ResultVo a : list20_2) {
                if (null != a.getValue())
                    value_20_2 = value_20_2 + a.getValue();
            }
        }
        Integer value_20_3 = 0;
        if (list20_3.size() > 0) {
            for (ResultVo a : list20_3) {
                if (null != a.getValue())
                    value_20_3 = value_20_3 + a.getValue();
            }
        }
        Integer value_20_4 = 0;
        if (list20_4.size() > 0) {
            for (ResultVo a : list20_4) {
                if (null != a.getValue())
                    value_20_4 = value_20_4 + a.getValue();
            }
        }


        Integer value_21_1 = 0;
        if (list21_1.size() > 0) {
            for (ResultVo a : list21_1) {
                if (null != a.getValue())
                    value_21_1 = value_21_1 + a.getValue();
            }
        }
        Integer value_21_2 = 0;
        if (list21_2.size() > 0) {
            for (ResultVo a : list21_2) {
                if (null != a.getValue())
                    value_21_2 = value_21_2 + a.getValue();
            }
        }
        Integer value_21_3 = 0;
        if (list21_3.size() > 0) {
            for (ResultVo a : list21_3) {
                if (null != a.getValue())
                    value_21_3 = value_21_3 + a.getValue();
            }
        }
        Integer value_21_4 = 0;
        if (list21_4.size() > 0) {
            for (ResultVo a : list21_4) {
                if (null != a.getValue())
                    value_21_4 = value_21_4 + a.getValue();
            }
        }

        Integer value_22_1 = 0;
        if (list22_1.size() > 0) {
            for (ResultVo a : list22_1) {
                if (null != a.getValue())
                    value_22_1 = value_22_1 + a.getValue();
            }
        }
        Integer value_22_2 = 0;
        if (list22_2.size() > 0) {
            for (ResultVo a : list22_2) {
                if (null != a.getValue())
                    value_22_2 = value_22_2 + a.getValue();
            }
        }
        Integer value_22_3 = 0;
        if (list22_3.size() > 0) {
            for (ResultVo a : list22_3) {
                if (null != a.getValue())
                    value_22_3 = value_22_3 + a.getValue();
            }
        }
        Integer value_22_4 = 0;
        if (list22_4.size() > 0) {
            for (ResultVo a : list22_4) {
                if (null != a.getValue())
                    value_22_4 = value_22_4 + a.getValue();
            }
        }

        Integer value_23_1 = 0;
        if (list23_1.size() > 0) {
            for (ResultVo a : list23_1) {
                if (null != a.getValue())
                    value_23_1 = value_23_1 + a.getValue();
                 log.info("value_23_1 ="+value_23_1);
            }
        }
        Integer value_23_2 = 0;
        if (list23_2.size() > 0) {
            for (ResultVo a : list23_2) {
                if (null != a.getValue())
                    value_23_2 = value_23_2 + a.getValue();
            }
        }
        Integer value_23_3 = 0;
        if (list23_3.size() > 0) {
            for (ResultVo a : list23_3) {
                if (null != a.getValue())
                    value_23_3 = value_23_3 + a.getValue();
            }
        }
        Integer value_23_4 = 0;
        if (list23_4.size() > 0) {
            for (ResultVo a : list23_4) {
                if (null != a.getValue())
                    value_23_4 = value_23_4 + a.getValue();
            }
        }

        Integer value_24_1 = 0;
        if (list24_1.size() > 0) {
            for (ResultVo a : list24_1) {
                if (null != a.getValue())
                    value_24_1 = value_24_1 + a.getValue();
            }
        }
        Integer value_24_2 = 0;
        if (list24_2.size() > 0) {
            for (ResultVo a : list24_2) {
                if (null != a.getValue())
                    value_24_2 = value_24_2 + a.getValue();
            }
        }
        Integer value_24_3 = 0;
        if (list24_3.size() > 0) {
            for (ResultVo a : list24_3) {
                if (null != a.getValue())
                    value_24_3 = value_24_3 + a.getValue();
            }
        }
        Integer value_24_4 = 0;
        if (list24_4.size() > 0) {
            for (ResultVo a : list24_4) {
                if (null != a.getValue())
                    value_24_4 = value_24_4 + a.getValue();
            }
        }

        Integer value_25_1 = 0;
        if (list25_1.size() > 0) {
            for (ResultVo a : list25_1) {
                if (null != a.getValue())
                    value_25_1 = value_25_1 + a.getValue();
            }
        }
        Integer value_25_2 = 0;
        if (list25_2.size() > 0) {
            for (ResultVo a : list25_2) {
                if (null != a.getValue())
                    value_25_2 = value_25_2 + a.getValue();
            }
        }
        Integer value_25_3 = 0;
        if (list25_3.size() > 0) {
            for (ResultVo a : list25_3) {
                if (null != a.getValue())
                    value_25_3 = value_25_3 + a.getValue();
            }
        }
        Integer value_25_4 = 0;
        if (list25_4.size() > 0) {
            for (ResultVo a : list25_4) {
                if (null != a.getValue())
                    value_25_4 = value_25_4 + a.getValue();
            }
        }

        Integer value_26_1 = 0;
        if (list26_1.size() > 0) {
            for (ResultVo a : list26_1) {
                if (null != a.getValue())
                    value_26_1 = value_26_1 + a.getValue();
            }
        }
        Integer value_26_2 = 0;
        if (list26_2.size() > 0) {
            for (ResultVo a : list26_2) {
                if (null != a.getValue())
                    value_26_2 = value_26_2 + a.getValue();
            }
        }
        Integer value_26_3 = 0;
        if (list26_3.size() > 0) {
            for (ResultVo a : list26_3) {
                if (null != a.getValue())
                    value_26_3 = value_26_3 + a.getValue();
            }
        }
        Integer value_26_4 = 0;
        if (list26_4.size() > 0) {
            for (ResultVo a : list26_4) {
                if (null != a.getValue())
                    value_26_4 = value_26_4 + a.getValue();
            }
        }

        Integer value_27_1 = 0;
        if (list27_1.size() > 0) {
            for (ResultVo a : list27_1) {
                if (null != a.getValue())
                    value_27_1 = value_27_1 + a.getValue();
            }
        }
        Integer value_27_2 = 0;
        if (list27_2.size() > 0) {
            for (ResultVo a : list27_2) {
                if (null != a.getValue())
                    value_27_2 = value_27_2 + a.getValue();
            }
        }
        Integer value_27_3 = 0;
        if (list27_3.size() > 0) {
            for (ResultVo a : list27_3) {
                if (null != a.getValue())
                    value_27_3 = value_27_3 + a.getValue();
            }
        }
        Integer value_27_4 = 0;
        if (list27_4.size() > 0) {
            for (ResultVo a : list27_4) {
                if (null != a.getValue())
                    value_27_4 = value_27_4 + a.getValue();
            }
        }

        Integer value_28_1 = 0;
        if (list28_1.size() > 0) {
            for (ResultVo a : list28_1) {
                if (null != a.getValue())
                    value_28_1 = value_28_1 + a.getValue();
            }
        }
        Integer value_28_2 = 0;
        if (list28_2.size() > 0) {
            for (ResultVo a : list28_2) {
                if (null != a.getValue())
                    value_28_2 = value_28_2 + a.getValue();
            }
        }
        Integer value_28_3 = 0;
        if (list28_3.size() > 0) {
            for (ResultVo a : list28_3) {
                if (null != a.getValue())
                    value_28_3 = value_28_3 + a.getValue();
            }
        }
        Integer value_28_4 = 0;
        if (list28_4.size() > 0) {
            for (ResultVo a : list28_4) {
                if (null != a.getValue())
                    value_28_4 = value_28_4 + a.getValue();
            }
        }

        Integer value_29_1 = 0;
        if (list23_1.size() > 0) {
            for (ResultVo a : list29_1) {
                if (null != a.getValue())
                    value_29_1 = value_29_1 + a.getValue();
                log.info("value_29_1="+value_29_1);
            }
        }
        Integer value_29_2 = 0;
        if (list29_2.size() > 0) {
            for (ResultVo a : list29_2) {
                if (null != a.getValue())
                    value_29_2 = value_29_2 + a.getValue();
                log.info("value_29_2="+value_29_2);
            }
        }
        Integer value_29_3 = 0;
        if (list29_3.size() > 0) {
            for (ResultVo a : list29_3) {
                if (null != a.getValue())
                    value_29_3 = value_29_3 + a.getValue();
                log.info("value_29_3="+value_29_3);
            }
        }
        Integer value_29_4 = 0;
        if (list29_4.size() > 0) {
            for (ResultVo a : list29_4) {
                if (null != a.getValue())
                    value_29_4 = value_29_4 + a.getValue();
                  log.info("value_29_4="+value_29_4);
            }
        }

//        Integer value_30_1 = 0;
//        if (list30_1.size() > 0) {
//            for (ResultVo a : list30_1) {
//                if (null != a.getValue())
//                    value_30_1 = value_30_1 + a.getValue();
//            }
//        }
//        Integer value_30_2 = 0;
//        if (list30_2.size() > 0) {
//            for (ResultVo a : list30_2) {
//                if (null != a.getValue())
//                    value_30_2 = value_30_2 + a.getValue();
//            }
//        }
//        Integer value_30_3 = 0;
//        if (list30_3.size() > 0) {
//            for (ResultVo a : list30_3) {
//                if (null != a.getValue())
//                    value_30_3 = value_30_3 + a.getValue();
//            }
//        }
//        Integer value_30_4 = 0;
//        if (list30_4.size() > 0) {
//            for (ResultVo a : list30_4) {
//                if (null != a.getValue())
//                    value_30_4 = value_30_4 + a.getValue();
//            }
//        }

        //输出
//        JSONArray jar = new JSONArray();
//
//        JSONObject jb1 = new JSONObject();
//        JSONObject jb2 = new JSONObject();
//        JSONObject jb3 = new JSONObject();
//        JSONObject jb4 = new JSONObject();
//        JSONObject jb5 = new JSONObject();
//        JSONObject jb6 = new JSONObject();
//        JSONObject jb7 = new JSONObject();
//        JSONObject jb8 = new JSONObject();
//        JSONObject jb9 = new JSONObject();
//        JSONObject jb10 = new JSONObject();
//        JSONObject jb11 = new JSONObject();
//        JSONObject jb12 = new JSONObject();
//        JSONObject jb13 = new JSONObject();
//        JSONObject jb14 = new JSONObject();
//        JSONObject jb15 = new JSONObject();
//        JSONObject jb16 = new JSONObject();
//        JSONObject jb17 = new JSONObject();
//        JSONObject jb18 = new JSONObject();
//        JSONObject jb19 = new JSONObject();
//        JSONObject jb20 = new JSONObject();
//        JSONObject jb21 = new JSONObject();
//        JSONObject jb22 = new JSONObject();
        //封装到对象
        List<CountExcel> list = new ArrayList<>();
        CountExcel countExcel1 = new CountExcel();
        CountExcel countExcel2 = new CountExcel();
        CountExcel countExcel3 = new CountExcel();
        CountExcel countExcel4 = new CountExcel();
        CountExcel countExcel5 = new CountExcel();
        CountExcel countExcel6 = new CountExcel();
        CountExcel countExcel7 = new CountExcel();
        CountExcel countExcel8 = new CountExcel();
        CountExcel countExcel9 = new CountExcel();
        CountExcel countExcel10 = new CountExcel();
        CountExcel countExcel11 = new CountExcel();
        CountExcel countExcel12 = new CountExcel();
        CountExcel countExcel13 = new CountExcel();
        CountExcel countExcel14 = new CountExcel();
        CountExcel countExcel15 = new CountExcel();
        CountExcel countExcel16 = new CountExcel();
        CountExcel countExcel17 = new CountExcel();
        CountExcel countExcel18 = new CountExcel();
        CountExcel countExcel19 = new CountExcel();
        CountExcel countExcel20 = new CountExcel();
        CountExcel countExcel21 = new CountExcel();
        CountExcel countExcel22 = new CountExcel();
        CountExcel countExcel23 = new CountExcel();
        CountExcel countExcel24 = new CountExcel();
        CountExcel countExcel25 = new CountExcel();
        CountExcel countExcel26 = new CountExcel();
        CountExcel countExcel27 = new CountExcel();
        CountExcel countExcel28 = new CountExcel();
        CountExcel countExcel29 = new CountExcel();
      //  CountExcel countExcel30 = new CountExcel();
        //封装对象
        //职能发挥
        //工作效率
        //服务质量
        //服务态度
//        jb1.put("department", "公司业务部");
//        jb1.put("v1", value_1_1);
//        jb1.put("v2", value_1_2);
//        jb1.put("v3", value_1_3);
//        jb1.put("v4", value_1_4);
//        jb1.put("number", list1_1.size());

        countExcel1.setDepartment("党群工作部");
        countExcel1.setNumber(list1_1.size());
        countExcel1.setV1(value_1_1);
        countExcel1.setV2(value_1_2);
        countExcel1.setV3(value_1_3);
        countExcel1.setV4(value_1_4);
         log.info("党群工作部: v1="+value_1_1+";v2="+value_1_2+" ;v3="+value_1_3+" ;v4="+value_1_4+" 人数="+list1_1.size());
        double average1 = 0;
        if (list1_1.size() > 0) {
            average1 = (value_1_1 + value_1_2 + value_1_3 + value_1_4) / list1_1.size()*2.5;
            countExcel1.setAverage(average1);
        }else {
            countExcel1.setAverage(0);
        }


//        jb2.put("department", "三农业务部");
//        jb2.put("v1", value_2_1);
//        jb2.put("v2", value_2_2);
//        jb2.put("v3", value_2_3);
//        jb2.put("v4", value_2_4);
//        jb2.put("number", list2_1.size());
        countExcel2.setDepartment("董事会办公室");
        countExcel2.setNumber(list2_1.size());
        countExcel2.setV1(value_2_1);
        countExcel2.setV2(value_2_2);
        countExcel2.setV3(value_2_3);
        countExcel2.setV4(value_2_4);
        double average2 = 0;
        if (list2_1.size() > 0) {
            average2 = (value_2_1 + value_2_2 + value_2_3 + value_2_4) / list2_1.size()*2.5;
            countExcel2.setAverage(average2);
        }else {
            countExcel2.setAverage(0);
        }
//
//        jb3.put("department", "电子银行部");
//        jb3.put("v1", value_3_1);
//        jb3.put("v2", value_3_2);
//        jb3.put("v3", value_3_3);
//        jb3.put("v4", value_3_4);
//        jb3.put("number", list3_1.size());
        countExcel3.setDepartment("监事会办公室");
        countExcel3.setNumber(list3_1.size());
        countExcel3.setV1(value_3_1);
        countExcel3.setV2(value_3_2);
        countExcel3.setV3(value_3_3);
        countExcel3.setV4(value_3_4);
        double average3 = 0;
        if (list3_1.size() > 0) {
            average3 = (value_3_1 + value_3_2 + value_3_3 + value_3_4) / list3_1.size()*2.5;
            countExcel3.setAverage(average3);
        }else {
            countExcel3.setAverage(0);
        }

//        jb4.put("department", "资金业务部");
//        jb4.put("v1", value_4_1);
//        jb4.put("v2", value_4_2);
//        jb4.put("v3", value_4_3);
//        jb4.put("v4", value_4_4);
//        jb4.put("number", list4_1.size());
        countExcel4.setDepartment("行长办公室");
        countExcel4.setNumber(list4_1.size());
        countExcel4.setV1(value_4_1);
        countExcel4.setV2(value_4_2);
        countExcel4.setV3(value_4_3);
        countExcel4.setV4(value_4_4);
        double average4 = 0;
        if (list4_1.size() > 0) {
            average4 = (value_4_1 + value_4_2 + value_4_3 + value_4_4) / list4_1.size()*2.5;
            countExcel4.setAverage(average4);
        }else {
            countExcel4.setAverage(0);
        }

//        jb5.put("department", "国际业务部");
//        jb5.put("v1", value_5_1);
//        jb5.put("v2", value_5_2);
//        jb5.put("v3", value_5_3);
//        jb5.put("v4", value_5_4);
//        jb5.put("number", list5_1.size());
        countExcel5.setDepartment("零售业务营销部（普惠金融部)");
        countExcel5.setNumber(list5_1.size());
        countExcel5.setV1(value_5_1);
        countExcel5.setV2(value_5_2);
        countExcel5.setV3(value_5_3);
        countExcel5.setV4(value_5_4);
        double average5 = 0;
        if (list5_1.size() > 0) {
            average5 = (value_5_1 + value_5_2 + value_5_3 + value_5_4) / list5_1.size()*2.5;
            countExcel5.setAverage(average5);
        }else {
            countExcel5.setAverage(0);
        }

//        jb6.put("department", "风险管理部");
//        jb6.put("v1", value_6_1);
//        jb6.put("v2", value_6_2);
//        jb6.put("v3", value_6_3);
//        jb6.put("v4", value_6_4);
//        jb6.put("number", list6_1.size());
        countExcel6.setDepartment("零售业务管理部");
        countExcel6.setNumber(list6_1.size());
        countExcel6.setV1(value_6_1);
        countExcel6.setV2(value_6_2);
        countExcel6.setV3(value_6_3);
        countExcel6.setV4(value_6_4);
        double average6 = 0;
        if (list6_1.size() > 0) {
            average6 = (value_6_1 + value_6_2 + value_6_3 + value_6_4) / list6_1.size()*2.5;
            countExcel6.setAverage(average6);
        }else {
            countExcel6.setAverage(0);
        }

//        jb7.put("department", "运营管理部");
//        jb7.put("v1", value_7_1);
//        jb7.put("v2", value_7_2);
//        jb7.put("v3", value_7_3);
//        jb7.put("v4", value_7_4);
//        jb7.put("number", list7_1.size());
        countExcel7.setDepartment("战略客户与投资银行部战略客户与投资银行部");
        countExcel7.setNumber(list7_1.size());
        countExcel7.setV1(value_7_1);
        countExcel7.setV2(value_7_2);
        countExcel7.setV3(value_7_3);
        countExcel7.setV4(value_7_4);
        double average7 = 0;
        if (list7_1.size() > 0) {
            average7 = (value_7_1 + value_7_2 + value_7_3 + value_7_4) / list7_1.size()*2.5;
            countExcel7.setAverage(average7);
        }else {
            countExcel7.setAverage(0);
        }

//        jb8.put("department", "合规管理部");
//        jb8.put("v1", value_8_1);
//        jb8.put("v2", value_8_2);
//        jb8.put("v3", value_8_3);
//        jb8.put("v4", value_8_4);
//        jb8.put("number", list8_1.size());
        countExcel8.setDepartment("公司业务管理部");
        countExcel8.setNumber(list8_1.size());
        countExcel8.setV1(value_8_1);
        countExcel8.setV2(value_8_2);
        countExcel8.setV3(value_8_3);
        countExcel8.setV4(value_8_4);
        double average8 = 0;
        if (list8_1.size() > 0) {
            average8 = (value_8_1 + value_8_2 + value_8_3 + value_8_4) / list8_1.size()*2.5;
            countExcel8.setAverage(average8);
        }else {
            countExcel8.setAverage(0);
        }

//        jb9.put("department", "计划财务部");
//        jb9.put("v1", value_9_1);
//        jb9.put("v2", value_9_2);
//        jb9.put("v3", value_9_3);
//        jb9.put("v4", value_9_4);
//        jb9.put("number", list9_1.size());
        countExcel9.setDepartment("交易银行部");
        countExcel9.setNumber(list9_1.size());
        countExcel9.setV1(value_9_1);
        countExcel9.setV2(value_9_2);
        countExcel9.setV3(value_9_3);
        countExcel9.setV4(value_9_4);
        double average9 = 0;
        if (list9_1.size() > 0) {
            average9 = (value_9_1 + value_9_2 + value_9_3 + value_9_4) / list9_1.size()*2.5;
            countExcel9.setAverage(average9);
        }else {
            countExcel9.setAverage(0);
        }

//        jb10.put("department", "信贷管理部");
//        jb10.put("v1", value_10_1);
//        jb10.put("v2", value_10_2);
//        jb10.put("v3", value_10_3);
//        jb10.put("v4", value_10_4);
//        jb10.put("number", list10_1.size());
        countExcel10.setDepartment("互联网金融部（电子银行部）");
        countExcel10.setNumber(list10_1.size());
        countExcel10.setV1(value_10_1);
        countExcel10.setV2(value_10_2);
        countExcel10.setV3(value_10_3);
        countExcel10.setV4(value_10_4);
        double average10 = 0;
        if (list10_1.size() > 0) {
            average10 = (value_10_1 + value_10_2 + value_10_3 + value_10_4) / list10_1.size()*2.5;
            countExcel10.setAverage(average10);
        }else {
            countExcel10.setAverage(0);
        }

//        jb11.put("department", "科技信息部");
//        jb11.put("v1", value_11_1);
//        jb11.put("v2", value_11_2);
//        jb11.put("v3", value_11_3);
//        jb11.put("v4", value_11_4);
//        jb11.put("number", list11_1.size());
        countExcel11.setDepartment("金融同业部");
        countExcel11.setNumber(list11_1.size());
        countExcel11.setV1(value_11_1);
        countExcel11.setV2(value_11_2);
        countExcel11.setV3(value_11_3);
        countExcel11.setV4(value_11_4);
        double average11 = 0;
        if (list11_1.size() > 0) {
            average11 = (value_11_1 + value_11_2 + value_11_3 + value_11_4) / list11_1.size()*2.5;
            countExcel11.setAverage(average11);
        }else {
            countExcel11.setAverage(0);
        }

//        jb12.put("department", "人力资源部");
//        jb12.put("v1", value_12_1);
//        jb12.put("v2", value_12_2);
//        jb12.put("v3", value_12_3);
//        jb12.put("v4", value_12_4);
//        jb12.put("number", list12_1.size());
        countExcel12.setDepartment("金融市场部");
        countExcel12.setNumber(list12_1.size());
        countExcel12.setV1(value_12_1);
        countExcel12.setV2(value_12_2);
        countExcel12.setV3(value_12_3);
        countExcel12.setV4(value_12_4);
        double average12 = 0;
        if (list12_1.size() > 0) {
            average12 = (value_12_1 + value_12_2 + value_12_3 + value_12_4) / list12_1.size()*2.5;
            countExcel12.setAverage(average12);
        }else {
            countExcel12.setAverage(0);
        }


//        jb13.put("department", "行长办公室");
//        jb13.put("v1", value_13_1);
//        jb13.put("v2", value_13_2);
//        jb13.put("v3", value_13_3);
//        jb13.put("v4", value_13_4);
//        jb13.put("number", list13_1.size());
        countExcel13.setDepartment("资产管理部");
        countExcel13.setNumber(list13_1.size());
        countExcel13.setV1(value_13_1);
        countExcel13.setV2(value_13_2);
        countExcel13.setV3(value_13_3);
        countExcel13.setV4(value_13_4);
        double average13 = 0;
        if (list13_1.size() > 0) {
            average13 = (value_13_1 + value_13_2 + value_13_3 + value_13_4) / list13_1.size()*2.5;
            countExcel13.setAverage(average13);
        }else {
            countExcel13.setAverage(0);
        }

//        jb14.put("department", "后勤保障部");
//        jb14.put("v1", value_14_1);
//        jb14.put("v2", value_14_2);
//        jb14.put("v3", value_14_3);
//        jb14.put("v4", value_14_4);
//        jb14.put("number", list14_1.size());
        countExcel14.setDepartment("资金业务管理部");
        countExcel14.setNumber(list14_1.size());
        countExcel14.setV1(value_14_1);
        countExcel14.setV2(value_14_2);
        countExcel14.setV3(value_14_3);
        countExcel14.setV4(value_14_4);
        double average14 = 0;
        if (list14_1.size() > 0) {
            average14 = (value_14_1 + value_14_2 + value_14_3 + value_14_4) / list14_1.size()*2.5;
            countExcel14.setAverage(average14);
        }else {
            countExcel14.setAverage(0);
        }

//        jb15.put("department", "安全保卫部");
//        jb15.put("v1", value_15_1);
//        jb15.put("v2", value_15_2);
//        jb15.put("v3", value_15_3);
//        jb15.put("v4", value_15_4);
//        jb15.put("number", list15_1.size());
        countExcel15.setDepartment("授信审批部");
        countExcel15.setNumber(list15_1.size());
        countExcel15.setV1(value_15_1);
        countExcel15.setV2(value_15_2);
        countExcel15.setV3(value_15_3);
        countExcel15.setV4(value_15_4);
        double average15 = 0;
        if (list15_1.size() > 0) {
            average15 = (value_15_1 + value_15_2 + value_15_3 + value_15_4) / list15_1.size()*2.5;
            countExcel15.setAverage(average15);
        }else {
            countExcel15.setAverage(0);
        }


//        jb16.put("department", "稽核审计部");
//        jb16.put("v1", value_16_1);
//        jb16.put("v2", value_16_2);
//        jb16.put("v3", value_16_3);
//        jb16.put("v4", value_16_4);
//        jb16.put("number", list16_1.size());
        countExcel16.setDepartment("信贷管理部");
        countExcel16.setNumber(list16_1.size());
        countExcel16.setV1(value_16_1);
        countExcel16.setV2(value_16_2);
        countExcel16.setV3(value_16_3);
        countExcel16.setV4(value_16_4);
        double average16 = 0;
        if (list16_1.size() > 0) {
            average16 = (value_16_1 + value_16_2 + value_16_3 + value_16_4) / list16_1.size()*2.5;
            countExcel16.setAverage(average16);
        }else {
            countExcel16.setAverage(0);
        }

//        jb17.put("department", "调查统计部");
//        jb17.put("v1", value_17_1);
//        jb17.put("v2", value_17_2);
//        jb17.put("v3", value_17_3);
//        jb17.put("v4", value_17_4);
//        jb17.put("number", list17_1.size());
        countExcel17.setDepartment("风险管理部");
        countExcel17.setNumber(list17_1.size());
        countExcel17.setV1(value_17_1);
        countExcel17.setV2(value_17_2);
        countExcel17.setV3(value_17_3);
        countExcel17.setV4(value_17_4);
        double average17 = 0;
        if (list17_1.size() > 0) {
            average17 = (value_17_1 + value_17_2 + value_17_3 + value_17_4) / list17_1.size()*2.5;
            countExcel17.setAverage(average17);
        }else {
            countExcel17.setAverage(0);
        }

//        jb18.put("department", "纪检监察部");
//        jb18.put("v1", value_18_1);
//        jb18.put("v2", value_18_2);
//        jb18.put("v3", value_18_3);
//        jb18.put("v4", value_18_4);
//        jb18.put("number", list18_1.size());
        countExcel18.setDepartment("合规管理部");
        countExcel18.setNumber(list18_1.size());
        countExcel18.setV1(value_18_1);
        countExcel18.setV2(value_18_2);
        countExcel18.setV3(value_18_3);
        countExcel18.setV4(value_18_4);
        double average18 = 0;
        if (list18_1.size() > 0) {
            average18 = (value_18_1 + value_18_2 + value_18_3 + value_18_4) / list18_1.size()*2.5;
            countExcel18.setAverage(average18);
        }else {
            countExcel18.setAverage(0);
        }

//        jb19.put("department", "董事会办公室");
//        jb19.put("v1", value_19_1);
//        jb19.put("v2", value_19_2);
//        jb19.put("v3", value_19_3);
//        jb19.put("v4", value_19_4);
//        jb19.put("number", list19_1.size());
        countExcel19.setDepartment("计划财务部");
        countExcel19.setNumber(list19_1.size());
        countExcel19.setV1(value_19_1);
        countExcel19.setV2(value_19_2);
        countExcel19.setV3(value_19_3);
        countExcel19.setV4(value_19_4);
        double average19 = 0;
        if (list19_1.size() > 0) {
            average19 = (value_19_1 + value_19_2 + value_19_3 + value_19_4) / list19_1.size()*2.5;
            countExcel19.setAverage(average19);
        }else {
            countExcel19.setAverage(0);
        }


//        jb20.put("department", "监事会办公室");
//        jb20.put("v1", value_20_1);
//        jb20.put("v2", value_20_2);
//        jb20.put("v3", value_20_3);
//        jb20.put("v4", value_20_4);
//        jb20.put("number", list20_1.size());
        countExcel20.setDepartment("资产负债管理部");
        countExcel20.setNumber(list20_1.size());
        countExcel20.setV1(value_20_1);
        countExcel20.setV2(value_20_2);
        countExcel20.setV3(value_20_3);
        countExcel20.setV4(value_20_4);
        double average20 = 0;
        if (list20_1.size() > 0) {
            average20 = (value_20_1 + value_20_2 + value_20_3 + value_20_4) / list20_1.size()*2.5;
            countExcel20.setAverage(average20);
        }else {
            countExcel20.setAverage(0);
        }


//        jb21.put("department", "党群工作部");
//        jb21.put("v1", value_21_1);
//        jb21.put("v2", value_21_2);
//        jb21.put("v3", value_21_3);
//        jb21.put("v4", value_21_4);
//        jb21.put("number", list21_1.size());
        countExcel21.setDepartment("运营管理部");
        countExcel21.setNumber(list21_1.size());
        countExcel21.setV1(value_21_1);
        countExcel21.setV2(value_21_2);
        countExcel21.setV3(value_21_3);
        countExcel21.setV4(value_21_4);
        double average21 = 0;
        if (list21_1.size() > 0) {
            average21 = (value_21_1 + value_21_2 + value_21_3 + value_21_4) / list21_1.size()*2.5;
            countExcel21.setAverage(average21);
        }else {
            countExcel21.setAverage(0);
        }

//        jb22.put("department", "工会");
//        jb22.put("v1", value_22_1);
//        jb22.put("v2", value_22_2);
//        jb22.put("v3", value_22_3);
//        jb22.put("v4", value_22_4);
//        jb22.put("number", list22_1.size());
        countExcel22.setDepartment("信息技术部");
        countExcel22.setNumber(list22_1.size());
        countExcel22.setV1(value_22_1);
        countExcel22.setV2(value_22_2);
        countExcel22.setV3(value_22_3);
        countExcel22.setV4(value_22_4);
        double average22 = 0;
        if (list22_1.size() > 0) {
            average22 = (value_22_1 + value_22_2 + value_22_3 + value_22_4) / list22_1.size()*2.5;
            countExcel22.setAverage(average22);
        }else {
            countExcel22.setAverage(0);
        }

        countExcel23.setDepartment("人力资源部");
        countExcel23.setNumber(list23_1.size());
        countExcel23.setV1(value_23_1);
        countExcel23.setV2(value_23_2);
        countExcel23.setV3(value_23_3);
        countExcel23.setV4(value_23_4);
         log.info("人力资源数据：v1="+value_23_1+" ; v2="+value_23_2+" ; v3="+value_23_3+" ; v4="+value_23_4+"人数："+list23_1.size());
        double average23 = 0;
        if (list23_1.size() > 0) {
            average23 = (value_23_1 + value_23_2 + value_23_3 + value_23_4) / list23_1.size()*2.5;
            countExcel23.setAverage(average23);
        }else {
            countExcel23.setAverage(0);
        }

        countExcel24.setDepartment("安全保障部");
        countExcel24.setNumber(list24_1.size());
        countExcel24.setV1(value_24_1);
        countExcel24.setV2(value_24_2);
        countExcel24.setV3(value_24_3);
        countExcel24.setV4(value_24_4);
        double average24 = 0;
        if (list24_1.size() > 0) {
            average24 = (value_24_1 + value_24_2 + value_24_3 + value_24_4) / list24_1.size()*2.5;
            countExcel24.setAverage(average24);
        }else {
            countExcel24.setAverage(0);
        }

        countExcel25.setDepartment("消费者权益保护部");
        countExcel25.setNumber(list25_1.size());
        countExcel25.setV1(value_25_1);
        countExcel25.setV2(value_25_2);
        countExcel25.setV3(value_25_3);
        countExcel25.setV4(value_25_4);
        double average25 = 0;
        if (list25_1.size() > 0) {
            average25= (value_25_1 + value_25_2 + value_25_3 + value_25_4) / list25_1.size()*2.5;
            countExcel25.setAverage(average25);
        }else {
            countExcel25.setAverage(0);
        }

        countExcel26.setDepartment("数据分析部");
        countExcel26.setNumber(list26_1.size());
        countExcel26.setV1(value_26_1);
        countExcel26.setV2(value_26_2);
        countExcel26.setV3(value_26_3);
        countExcel26.setV4(value_26_4);
        double average26 = 0;
        if (list26_1.size() > 0) {
            average26 = (value_26_1 + value_26_2 + value_26_3 + value_26_4) / list26_1.size()*2.5;
            countExcel26.setAverage(average26);
        }else {
            countExcel26.setAverage(0);
        }

        countExcel27.setDepartment("纪检监察部");
        countExcel27.setNumber(list27_1.size());
        countExcel27.setV1(value_27_1);
        countExcel27.setV2(value_27_2);
        countExcel27.setV3(value_27_3);
        countExcel27.setV4(value_27_4);
        double average27 = 0;
        if (list27_1.size() > 0) {
            average27= (value_27_1 + value_27_2 + value_27_3 + value_27_4) / list27_1.size()*2.5;
            countExcel27.setAverage(average27);
        }else {
            countExcel27.setAverage(0);
        }

        countExcel28.setDepartment("稽核审计部");
        countExcel28.setNumber(list28_1.size());
        countExcel28.setV1(value_28_1);
        countExcel28.setV2(value_28_2);
        countExcel28.setV3(value_28_3);
        countExcel28.setV4(value_28_4);
        double average28 = 0;
        if (list28_1.size() > 0) {
            average28= (value_28_1 + value_28_2 + value_28_3 + value_28_4) / list28_1.size()*2.5;
            log.info("average28=="+average28);
            countExcel28.setAverage(average28);
        }else {
            countExcel28.setAverage(0);
        }


        countExcel29.setDepartment("工会办公室");
        countExcel29.setNumber(list29_1.size());
        countExcel29.setV1(value_29_1);
        countExcel29.setV2(value_29_2);
        countExcel29.setV3(value_29_3);
        countExcel29.setV4(value_29_4);
        log.info("工会办公室 v1="+value_29_1+" ;v2="+value_29_2+" ;v3="+value_29_3+" ; v4="+value_29_4+" ;人数："+list29_1.size());
        double average29 = 0;
        if (list29_1.size() > 0) {
            average29= (value_29_1 + value_29_2 + value_29_3 + value_29_4) / list29_1.size()*2.5;
            countExcel29.setAverage(average29);
        }else {
            countExcel29.setAverage(0);
        }

//        countExcel30.setDepartment("公司业务部");
//        countExcel30.setNumber(list30_1.size());
//        countExcel30.setV1(value_30_1);
//        countExcel30.setV2(value_30_2);
//        countExcel30.setV3(value_30_3);
//        countExcel30.setV4(value_30_4);
//        double average30 = 0;
//        if (list30_1.size() > 0) {
//            average30= (value_30_1 + value_30_2 + value_30_3 + value_30_4) / list30_1.size()*2.5;
//            log.info("average30=="+average30);
//            countExcel30.setAverage(average30);
//        }else {
//            countExcel30.setAverage(0);
//        }


        //加入到list当中
//        jar.add(jb1);
//        jar.add(jb2);
//        jar.add(jb3);
//        jar.add(jb4);
//        jar.add(jb5);
//        jar.add(jb6);
//        jar.add(jb7);
//        jar.add(jb8);
//        jar.add(jb9);
//        jar.add(jb10);
//        jar.add(jb11);
//        jar.add(jb12);
//        jar.add(jb13);
//        jar.add(jb14);
//        jar.add(jb15);
//        jar.add(jb16);
//        jar.add(jb17);
//        jar.add(jb18);
//        jar.add(jb19);
//        jar.add(jb20);
//        jar.add(jb21);
//        jar.add(jb22);

        list.add(countExcel1);
        list.add(countExcel2);
        list.add(countExcel3);
        list.add(countExcel4);
        list.add(countExcel5);
        list.add(countExcel6);
        list.add(countExcel7);
        list.add(countExcel8);
        list.add(countExcel9);
        list.add(countExcel10);
        list.add(countExcel11);
        list.add(countExcel12);
        list.add(countExcel13);
        list.add(countExcel14);
        list.add(countExcel15);
        list.add(countExcel16);
        list.add(countExcel17);
        list.add(countExcel18);
        list.add(countExcel19);
        list.add(countExcel20);
        list.add(countExcel21);
        list.add(countExcel22);
        list.add(countExcel23);
        list.add(countExcel24);
        list.add(countExcel25);
        list.add(countExcel26);
        list.add(countExcel27);
        list.add(countExcel28);
        list.add(countExcel29);
      //  list.add(countExcel30);
        Collections.sort(list, countExcel1);

        return list;

    }
}
